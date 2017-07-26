package com.hungsum.framework.ui.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hungsum.framework.R;
import com.hungsum.framework.adapter.HsUserLabelValueAdapter;
import com.hungsum.framework.componments.HsLabelValue;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.events.CommEventListener.EventCategory;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.models.ModelHsLabelValue;
import com.hungsum.framework.models.ModelHsLabelValues;
import com.hungsum.framework.ui.controls.UcFileBrowser;
import com.hungsum.framework.ui.controls.UcFileBrowser.EFileBrowerType;
import com.hungsum.framework.ui.controls.UcFileBrowser.FileChooseEventListener;
import com.hungsum.framework.ui.controls.UcListView;
import com.hungsum.framework.utils.HsBase64;
import com.hungsum.framework.utils.HsFileSize;
import com.hungsum.framework.utils.HsGZip;
import com.hungsum.framework.utils.HsMD5;
import com.hungsum.framework.utils.HsZlib;

public class HsFragment_DJ_File extends HsFragment_ZD_Annex<IHsLabelValue>
{
	//{{ 实例成员
	
	/**
	 * 默认缓冲区大小，1M
	 */
	private static final int BUFFERLEN = 1048576;
	
	
	
	public static final String THREAD = "Thread";
	
	public static final int REQUESTCODE_CHOOSEFILE = 1000;

	private TextView ucSummary;

	/**
	 * 列表显示文件
	 */
	private UcListView ucListView;
	
	private List<IHsLabelValue> mFiles = new ArrayList<IHsLabelValue>();

	private int mFileIndex;
	
	private int mFileSegIndex;
	
	/**
	 * 待更新文件列表
	 */
	private List<IHsLabelValue> mNeedUpdateFiles = new ArrayList<IHsLabelValue>();
	
	private IHsLabelValue mCurrentUploadFile;

	private UcFileBrowser mFileBrowser;

	private List<byte[]> mBuffer = new ArrayList<byte[]>();
	
	private String mDownloadFile;
	
	//}}
	

	//{{ 构造方法
	
	public HsFragment_DJ_File()
	{
		super();
		
		Icon = R.drawable.collections_view_as_list;
		
		Title = "附件信息";

	}
	
	//}}
	
	
	//{{ 初始化
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater,container, savedInstanceState);

		View rootView = inflater.inflate(R.layout.hsactivity_list, null,false);
		ucSummary = (TextView) rootView.findViewById(R.id.ucSummary);
		ucListView = (UcListView) rootView.findViewById(R.id.ucListView);
		
		ucListView.setOnItemClickListener(this);

		//注册上下文菜单
		this.registerForContextMenu(ucListView);

		mHasCreateCompleted = true;
		
		if(mListenerOfCreateCompletedEventListener != null)
		{
			mListenerOfCreateCompletedEventListener.action(savedInstanceState);
		}
		
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		mFileBrowser = new UcFileBrowser(getActivity(),Environment.getExternalStorageDirectory());

		mFileBrowser.setOnFileChooseEventListener(new FileChooseEventListener()
		{
			@Override
			public void doAction(File file, EFileBrowerType browerType) throws Exception
			{
				if(browerType == EFileBrowerType.Save) //保存
				{
					mDownloadFile = file.getAbsolutePath();
					
					mFileIndex = 0;
					
					downloadFileSeg();
				}else //选择
				{
					FileInputStream fis = null;
					try
					{
						fis = new FileInputStream(file);

						List<byte[]> buffers = new ArrayList<byte[]>();
						
						byte[] totalBytes = new byte[fis.available()];
						
						while (true)
						{
							byte[] buffer = new byte[Math.min(BUFFERLEN, fis.available())];
							if(buffer.length == 0)
							{
								break;
							}
							fis.read(buffer);
							buffers.add(buffer);
							System.arraycopy(buffer, 0, totalBytes, (buffers.size() - 1) * BUFFERLEN ,buffer.length);
						}

						fis.close();
						
						IHsLabelValue updateFile = new HsLabelValue(file.getName(),file.getAbsolutePath())
						{
							private static final long serialVersionUID = -1483822259347973153L;

							@Override
							public Drawable getOperationImage()
							{
								return getResources().getDrawable(R.drawable.paper_48); 
							}
						};
						
						String fileName = file.getName();

						updateFile.addDetail(new HsLabelValue("FileName", fileName));
						updateFile.addDetail(new HsLabelValue("FileNameBase64",HsBase64.encodeString(fileName)));
						updateFile.addDetail(new HsLabelValue("FileType",fileName.substring(fileName.lastIndexOf(".") + 1)));
						updateFile.addDetail(new HsLabelValue("FileHashType", "MD5"));
						updateFile.addDetail(new HsLabelValue("FileHash", HsMD5.MD5(totalBytes)));
						updateFile.addDetail(new HsLabelValue("Data",buffers));
						updateFile.addDetail(new HsLabelValue("FileSize", totalBytes.length));
						updateFile.addDetail(new HsLabelValue("Editable", "0"));
						
						addItem(updateFile);
					} catch (Exception e)
					{
						showError(e);
					}
					finally
					{
						if(fis != null)
						{
							fis.close();
						}
					}
				}
			}
		});

	}

	//}}

	
	//{{菜单

	/*
	 * 构建上下文菜单
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.setHeaderTitle("请选择");
	
		menu.addSubMenu(0, R.string.str_download, 1, "下载");
		
		SubMenu menu_delete = menu.addSubMenu(0, R.string.str_delete, 2, "删除");
		menu_delete.getItem().setVisible(getAllowEdit());

	}
	
	/*
	 * 上下文菜单回调函数
	 */
	@Override
	public boolean onContextItemSelected(final MenuItem item)
	{
		if(this.getUserVisibleHint())
		{
			try
			{
				int position = ((AdapterContextMenuInfo) item.getMenuInfo()).position;

				mCurrentSelectedIndex = position;
				mCurrentSelectedItem = getItem(position);
				
				if (item.getItemId() == R.string.str_download)
				{
					mFileBrowser.showDialog(EFileBrowerType.Save);
				} else if (item.getItemId() == R.string.str_delete)
				{
					this.removeItem(position);
	
					setIsModified(true);

				}else {
					return super.onContextItemSelected(item);
				}
			} catch (Exception e)
			{
				showError(e);
			}

			return true;
		}else {
			return false;
		}
	}

	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu,inflater);

		// 选择
		menu.add(1, R.string.str_chooseFile, 1,
				getString(R.string.str_chooseFile))
				.setIcon(R.drawable.content_new)
				.setShowAsActionFlags(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.string.str_chooseFile)
		{
			try
			{
				mFileBrowser.showDialog(EFileBrowerType.Choose);
			} catch (Exception e)
			{
				showError(e);
			}
			return true;
		} else
		{
			return super.onOptionsItemSelected(item);
		}
	}

	// }}

	//{{ 图片适配器
	
	/**
	 * c
	 * @param datas
	 * @return
	 */
	@Override
	protected BaseAdapter createAdapter(List<IHsLabelValue> datas)
	{
		return new HsFileAdapter(getActivity());
	}

	@Override
	protected BaseAdapter getAdapter()
	{
		if (this.ucListView.getAdapter() == null)
		{
			this.ucListView
					.setAdapter(createAdapter(new ArrayList<IHsLabelValue>()));
		}
		return (BaseAdapter) this.ucListView.getAdapter();
	}

	//}}
	
	//{{ 添加、删除操作
	
	@Override
	protected void addItem(IHsLabelValue item)
	{
		super.addItem(item);
		
		((HsFileAdapter) this.getAdapter()).add(item);

		mFiles.add(item);
		
		this.setSummaryText();
	}
	
	@Override
	protected void addItems(List<IHsLabelValue> items)
	{
		super.addItems(items);
		
		((HsFileAdapter) this.getAdapter()).add(items);

		this.setSummaryText();
	}
	
	@Override
	protected void removeItem(int position)
	{
		super.removeItem(position);
		
		((HsFileAdapter) this.getAdapter()).remove(position);

		mFiles.remove(position);
		
		this.setSummaryText();
	}
	
	@Override
	protected IHsLabelValue getItem(int position)
	{
		return (IHsLabelValue) this.getAdapter().getItem(position);
	}

	//}}
	
	//{{ 分段下载文件
	
	private void downloadFileSeg()
	{
		showWait("请稍候",String.format("正在下载文件【%s】。",mCurrentSelectedItem.getValue("FileName", "")));

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					String hashData = mCurrentSelectedItem.getValue("FileHash", "").toString();
					HsWSReturnObject returnObject = mApplication.getWebService().getSysFileSeg(
							mApplication.getLoginData().getProgressId(), 
							hashData,
							mFileIndex);
					sendDataMessage(returnObject);
				} catch (Exception e)
				{
					sendErrorMessage(e.getMessage());
				}

			}
		}).start();
	}
	
	
	//}}
	
	//{{ 重载祖先或接口
	
	@Override
	public void setSummaryText()
	{
		if(ucSummary != null)
		{
			ucSummary.setText("共有" + this.getAnnexCount() + "个文件。");
		}
	}

	//{{ 刷新
	
	/**
	 * 此方法重载
	 */
	@Override
	protected void callRetrieveOnOtherThread()
	{
		if (!mHasRetrieved)
		{
			super.callRetrieveOnOtherThread();
		}else {
			addItems(mFiles);
		}
	}

	/**
	 * 此方法用于刷新获取数据，如果适配器中已经包含数据则不需要实现此方法。开始链式调用
	 */
	@Override
	protected HsWSReturnObject retrieve() throws Exception
	{
		return this.mApplication.getWebService().getSysFiles(this.mApplication.getLoginData().getProgressId(), mAnnexClass, mAnnexClassId);
	}
	
	//}}
	
	//{{ 更新
	
	/**
	 * 保存图像
	 */
	@Override
	public void updateAnnex()
	{
		if(mIsModified)
		{
			showWait("请稍候", "正在检查文件状态，请稍候...");

			final StringBuffer sb = new StringBuffer();
			sb.append("<Items>");
			
			for (IHsLabelValue item : mFiles)
			{
				sb.append("<Item>");
				
				sb.append("<FileId>");
				sb.append(item.getValue("FileId", "0"));
				sb.append("</FileId>");
				
				sb.append("<Class>");
				sb.append(mAnnexClass);
				sb.append("</Class>");
				
				sb.append("<ClassId>");
				sb.append(mAnnexClassId);
				sb.append("</ClassId>");
				
				sb.append("<FileNameBase64>");
				sb.append(item.getValue("FileNameBase64", ""));
				sb.append("</FileNameBase64>");
				
				sb.append("<FileType>");
				sb.append(item.getValue("FileType", ""));
				sb.append("</FileType>");
				
				sb.append("<FileHashType>");
				sb.append(item.getValue("FileHashType", "MD5"));
				sb.append("</FileHashType>");
				
				sb.append("<FileHash>");
				sb.append(item.getValue("FileHash", ""));
				sb.append("</FileHash>");
				
				sb.append("<FileSize>");
				sb.append(item.getValue("FileSize", "0"));
				sb.append("</FileSize>");
				
				sb.append("<Editable>");
				sb.append(item.getValue("Editable", "0"));
				sb.append("</Editable>");

				sb.append("</Item>");
			}
			
			sb.append("</Items>");

			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						HsWSReturnObject returnObject = mApplication.getWebService().checkFileStatus(
								mApplication.getLoginData().getProgressId(),
								mAnnexClass,
								mAnnexClassId,
								sb.toString());
						sendDataMessage(returnObject);
					} catch (Exception e)
					{
						sendErrorMessage(e.getMessage());
					}

				}
			}).start();
			

		}else 
		{
			showInformation("更新成功");
			
			triggerCommEvent(EventCategory.UpdateCompleted,null);
		}
	}

	private void uploadFile()
	{
		if(this.mNeedUpdateFiles.size() > this.mFileIndex)
		{
			mCurrentUploadFile = mNeedUpdateFiles.get(mFileIndex);
			
			for (IHsLabelValue file : mFiles)
			{
				if(mCurrentUploadFile.getValue("FileHash", null).equals(file.getValue("FileHash",null)))
				{
					mCurrentUploadFile = file;

					break;
				}
			}
			
			showWait("请稍候",String.format("正在上传文件【%s】。",mCurrentUploadFile.getValue("FileName", "")));

			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						HsWSReturnObject returnObject = mApplication.getWebService().updateSysFile(
								mApplication.getLoginData().getProgressId(),
								mCurrentUploadFile.getValue("FileId", "0").toString(),
								mAnnexClass,
								mAnnexClassId,
								mCurrentUploadFile.getValue("FileNameBase64", "").toString(),
								mCurrentUploadFile.getValue("FileType", "").toString(),
								mCurrentUploadFile.getValue("FileHashType","MD5").toString(),
								mCurrentUploadFile.getValue("FileHash", "").toString(),
								mCurrentUploadFile.getValue("FileSize", "0").toString(),
								mCurrentUploadFile.getValue("Editable", "0").toString());
						sendDataMessage(returnObject);
					} catch (Exception e)
					{
						sendErrorMessage(e.getMessage());
					}

				}
			}).start();
			
		}else
		{
			showInformation("更新成功");
			
			mIsModified = false;
			
			//发布更新完成事件
			triggerCommEvent(EventCategory.UpdateCompleted, null);
		}
	}
	
	private void updateFileSeg(final String fileHash)
	{
		@SuppressWarnings("unchecked")
		final List<byte[]> buffers = (List<byte[]>) mCurrentUploadFile.getValue("Data", null);
		
		if(mFileSegIndex < buffers.size())
		{
			showWait("请稍候",String.format("正在上传文件【%s】，已完成%d%%。",mCurrentUploadFile.getValue("FileName", ""),mFileSegIndex / buffers.size()));

			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						byte[] buffer = buffers.get(mFileSegIndex);
						
						int bufferSize = buffer.length;
						
						buffer = HsZlib.compress(buffer);
						
						HsWSReturnObject returnObject = mApplication.getWebService().updateSysFileSeg(
								mApplication.getLoginData().getProgressId(),
								fileHash,
								buffer,
								bufferSize,
								mFileSegIndex);
						sendDataMessage(returnObject);
					} catch (Exception e)
					{
						sendErrorMessage(e.getMessage());
					}

				}
			}).start();
		}else {

			showWait("请稍候",String.format("正在确认上传文件【%s】。",mCurrentUploadFile.getValue("FileName", "")));

			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						String fileId = (String) mCurrentUploadFile.getValue("FileId", "");
						
						HsWSReturnObject returnObject = mApplication.getWebService().confirmUpdateSysFile(
								mApplication.getLoginData().getProgressId(),
								fileId);
						sendDataMessage(returnObject);
					} catch (Exception e)
					{
						sendErrorMessage(e.getMessage());
					}

				}
			}).start();
		}
	}

	/*
	
	private void updateSysImage()
	{
		if(mImageIndex < mImageCachedPaths.size())
		{
			showWait("请稍候",String.format("正在更新第%d幅图片...",mImageIndex + 1 ) );
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						File imageFile = new File(mImageCachedPaths.get(mImageIndex));
						
						if(imageFile.getName().startsWith("Temp"))
						{
							//自磁盘获取图像数据。
							HsBitmap bitmap = new HsBitmap(HsBitmap.loadBitmapFromDisk(getActivity(), mImageCachedPaths.get(mImageIndex)));

							//如果图像太大对图像进行裁切,图像宽度最大不超过800px
							if(bitmap.getWidth() > 800)
							{
								bitmap = bitmap.getPreviewHsBitmap(800);
							}
							String imageData = bitmap.getBitmapString();
							HsWSReturnObject object = mApplication.getWebService().updateSysImage(mApplication.getLoginData().getProgressId(),mAnnexClass,mAnnexClassId,imageData);
							sendDataMessage(object);
						}else {
							String hashData = imageFile.getName().substring(0,imageFile.getName().lastIndexOf("."));
							HsWSReturnObject object = mApplication.getWebService().updateSysImageByHashData(mApplication.getLoginData().getProgressId(),mAnnexClass,mAnnexClassId,hashData);
							sendDataMessage(object);
						}
						
						
					} catch (Exception e)
					{
						sendErrorMessage(e.getMessage());
					}
				}
			}).start();
		}else 
		{
			deleteSysImages();
		}
		
	}

	private void deleteSysImages()
	{
		showWait("请稍候", "正在进行图片清理...");
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					HsWSReturnObject object = mApplication.getWebService().deleteSysImages(mApplication.getLoginData().getProgressId(),
							mAnnexClass,
							mAnnexClassId,
							mNeedDeleteImageMaxId);
					sendDataMessage(object); 
				} catch (Exception e)
				{
					sendErrorMessage(e.getMessage());
				}
			}
		}).start();
	}
	
	*/
	
	//}}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == Activity.RESULT_OK)
		{
			switch (requestCode)
			{
				default:
					super.onActivityResult(requestCode, resultCode, data);
					break;
			}
		}
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if(funcname.equals("GetSysFiles"))
		{
			//获取服务器端文件
			mFiles = new ModelHsLabelValues().Create(HsGZip.DecompressString(data.toString()));
			
			this.addItems(mFiles);

			//已经刷新过数据
			this.mHasRetrieved = true;
		}
		else if(funcname.equals("GetSysFileSeg"))
		{
			if(data != "")
			{
				IHsLabelValue fileSeg = new ModelHsLabelValue("Item").Create(HsGZip.DecompressString(data.toString()));
				
				byte[] bsFileSeg = HsBase64.decode(fileSeg.getValue("Data", "").toString());
				
				bsFileSeg = HsZlib.decompress(bsFileSeg);

				mBuffer.add(bsFileSeg);
				
				mFileIndex++;
				
				downloadFileSeg();
			}else
			{
				FileOutputStream fos = null;
				
				File file;
				
				try
				{
					file = new File(mDownloadFile + "/" + mCurrentSelectedItem.getValue("FileName", ""));
					
					fos = new FileOutputStream(file);
					
					if(!file.exists())
					{
						file.createNewFile();
					}
					
					for (byte[] buffer : mBuffer)
					{
						fos.write(buffer);
						fos.flush();
					}
					
					fos.close();
					
					showInformation("下载完毕。");
					
				} catch (Exception e)
				{
					throw e;
				}
				finally
				{
					if(fos != null)
					{
						fos.close();
					}
				}
				
			}
		}else if(funcname.equals("CheckFileStatus"))
		{
			mNeedUpdateFiles = new ModelHsLabelValues().Create(HsGZip.DecompressString(data.toString()));
		
			mFileIndex = 0;
			
			uploadFile();
		}else if(funcname.equals("UpdateSysFile"))
		{
			if(data == "") //文件内容在服务器上存有副本
			{
				this.mFileIndex++;
				this.uploadFile();;
			}else //文件没有存有副本
			{
				this.mCurrentUploadFile.addDetail(new HsLabelValue("FileId",data));
				
				String fileHash = (String) mCurrentUploadFile.getValue("FileHash", "");

				mFileSegIndex = 0;
				
				updateFileSeg(fileHash);
			}
		}else if(funcname.equals("UpdateSysFileSeg"))
		{
			mFileSegIndex++;
			updateFileSeg(data.toString());
		}else if(funcname.equals("ConfirmUpdateSysFile"))
		{
			mFileIndex++;
			uploadFile();
		}else
		{
			super.actionAfterWSReturnData(funcname, data);

		}
	}

	
	//}}
	
	
	
	//{{ 内部类
	
	private class HsFileAdapter extends HsUserLabelValueAdapter
	{
		public HsFileAdapter(Context context)
		{
			super(context, R.layout.control_list_file_1);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			IHsLabelValue value = (IHsLabelValue) getItem(position);

			ViewHolder holder;
			if(convertView == null)
			{
				convertView = mInflater.inflate(mLayoutRes, null);
				holder = new ViewHolder();
				holder.UcKey = (TextView) convertView.findViewById(R.id.list_keyvalue_ucKey);
				holder.UcValue = (TextView) convertView.findViewById(R.id.list_keyvalue_ucValue);
				holder.ucOperationImage = (ImageView) convertView.findViewById(R.id.list_keyvalue_ucOperationImage);
				holder.mData = value;
				
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			if(holder.UcKey != null)
			{
				holder.UcKey.setText(value.getValue("FileName","").toString());
				holder.UcKey.setTextColor(value.getColor());
				
			}
			
			if(holder.UcValue != null)
			{
				int fileSize = Integer.valueOf(value.getValue("FileSize", "0").toString());

				holder.UcValue.setText(HsFileSize.Size2String(fileSize));
			}
			

			if(holder.ucOperationImage != null)
			{
				Drawable image = mApplication.GetDrawableByPicName(value.getValue("FileType", "").toString());

				if(image == null)
				{
					image = mApplication.GetDrawableByPicName("dat");
				}
				
				holder.ucOperationImage.setImageDrawable(image);
			}

			return convertView;
		}
		
	}
	
	//}}
}
