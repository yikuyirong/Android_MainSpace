package com.hungsum.framework.ui.fragments;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;

import com.hungsum.framework.R;
import com.hungsum.framework.adapter.HsBitmapAdapter;
import com.hungsum.framework.componments.HsBitmap;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.events.CommEventListener.EventCategory;
import com.hungsum.framework.models.ModelHsBitmap;
import com.hungsum.framework.models.ModelHsBitmaps;
import com.hungsum.framework.ui.activities.HsActivity_Grid_HsBitmap;
import com.hungsum.framework.ui.activities.HsActivity_ShowImages;
import com.hungsum.framework.ui.controls.UcGridView;
import com.hungsum.framework.utils.HsGZip;

public class HsFragment_DJ_Image extends HsFragment_ZD_Annex<HsBitmap>
{
	//{{ 实例成员
	
	public static final String THREAD = "Thread";
	
	public static final int REQUESTCODE_CHOOSEIMAGE = 1000;

	public static final int REQUESTCODE_CROPPHOTO = 1001;

	public static final int REQUESTCODE_TAKEPHOTO = 1002;
	/*
	 * 图片缓存目录
	 */
	private String mImageCachedDir;

	/*
	 * 图片缓存路径
	 */
	private ArrayList<String> mImageCachedPaths;

	/*
	 * 当前图片缓存标识
	 */
	private Uri mCurrentTempImageCachedUri;

	private UcGridView ucGridView;

	private int mImageIndex;
	
	private int mNeedDeleteImageMaxId = Integer.MAX_VALUE;
	
	private List<HsBitmap> mSysImageHashDatas;

	private ShowImageThread mPrevShowImageThread = null;
	
	//}}
	

	//{{ 构造方法
	
	public HsFragment_DJ_Image()
	{
		super();
		
		Icon = R.drawable.content_picture;
		
		Title = "图片信息";
		
	}
	
	//}}
	
	
	//{{ 初始化
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater,container, savedInstanceState);

		View rootView = inflater.inflate(R.layout.hsactivity_grid, null,false);
		ucGridView = (UcGridView) rootView.findViewById(R.id.ucGridView2);
		
		ucGridView.setOnItemClickListener(this);

		//注册上下文菜单
		this.registerForContextMenu(ucGridView);
		
		mHasCreateCompleted = true;

		//第一次建立后触发
		if(mListenerOfCreateCompletedEventListener != null)
		{
			mListenerOfCreateCompletedEventListener.action(savedInstanceState);

			removeOnCreateCompletedEventListener();
		}

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		try
		{
			mImageCachedDir = initImageCachedDir();
			
		} catch (Exception e)
		{
			showError(e);
		}
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
		menu.addSubMenu(0, R.string.str_browse, 1, "浏览").setIcon(
				R.drawable.content_picture);
		
		SubMenu menu_delete = menu.addSubMenu(0, R.string.str_delete, 2, "删除").setIcon(
				R.drawable.content_remove);
		
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

				if (item.getItemId() == R.string.str_browse)
				{
					// 浏览图像
					browseImages(position);
				} else if (item.getItemId() == R.string.str_delete)
				{
					// 在适配器中删除图像
					removeItem(position);

					// 删除磁盘缓存文件
					new File(mImageCachedPaths.get(position)).delete();
					
					// 删除缓存列表文件
					mImageCachedPaths.remove(position);

					setIsModified(true);

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

		// 拍照
		menu.add(1, R.string.str_takePhoto, 0,
				getString(R.string.str_takePhoto))
				.setIcon(R.drawable.device_access_camera)
				.setShowAsActionFlags(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		// 选择
		menu.add(1, R.string.str_chooseImage, 1,
				getString(R.string.str_chooseImage))
				.setIcon(R.drawable.content_picture)
				.setShowAsActionFlags(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	}

	/*
	 * 系统菜单回调函数
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.string.str_takePhoto)
		{
			try
			{
				this.takePhoto();
			} catch (Exception e)
			{
				showError(e);
			}
			return true;
		} else if (item.getItemId() == R.string.str_chooseImage)
		{
			try
			{
				this.chooseBigImage();
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
	

	//{{ 图像目录与URI
	
	/**
	 * 
	 * 初始化图像缓存目录
	 * 
	 * @return 目录路径
	 * @throws Exception
	 */
	private String initImageCachedDir() throws Exception
	{
		// 临时文件
		String tempImageStoreDir = this.mApplication.getDownloadRootPath()
				+ "ImagesCached/";

		File file = new File(tempImageStoreDir);
		// 如果目录不存在则创建，如果存在则清空其中之内容
		if (!file.exists())
		{
			file.mkdirs();
		} else
		{
			if (!mHasRetrieved) // 如果已经刷新过图片则不能删除缓存图片。
			{
				// 删除所有缓存照片
				for (File f : new File(tempImageStoreDir).listFiles())
				{
					if(f.getName().startsWith("Temp"))
					{
						f.delete();
					}
				}
			}
		}

		return tempImageStoreDir;

	}

	
	
	/*
	 * 创建图片缓存URI,选择图片或拍照后暂存的路径
	 */
	private Uri createTempImageCachedUri() throws IOException
	{
		String fileName = mImageCachedDir + "Temp_" + new Date().getTime() + ".png";

		File file = new File(fileName);

		return Uri.fromFile(file);

	}
	
	//}}
	
	
	//{{ 浏览图册或拍照

	/**
	 * 浏览图片
	 * @param position
	 */
	private void browseImages(int position)
	{
		Intent intent = new Intent(getActivity(), HsActivity_ShowImages.class);
		intent.putExtra("ImagesCachedPath", mImageCachedPaths);
		intent.putExtra("Position", position);
		startActivity(intent);
	}

	/**
	 * 调用系统方法选择图库
	 * @throws Exception
	 */
	private void chooseBigImage() throws Exception
	{
		//生成当前图像文件URI
		mCurrentTempImageCachedUri = createTempImageCachedUri();

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
//		intent.putExtra("aspectX", 1);
//		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 800);
//		intent.putExtra("outputY", 800);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentTempImageCachedUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent,
				HsActivity_Grid_HsBitmap.REQUESTCODE_CHOOSEIMAGE);
	}

	/*
	 * private void chooseSmallImage() { Intent intent = new
	 * Intent(Intent.ACTION_GET_CONTENT, null); intent.setType("image/*");
	 * intent.putExtra("crop", "true"); intent.putExtra("aspectX", 2);
	 * intent.putExtra("aspectY", 1); intent.putExtra("outputX", 800);
	 * intent.putExtra("outputY", 600); intent.putExtra("scale", true);
	 * intent.putExtra("return-data", true); intent.putExtra("outputFormat",
	 * Bitmap.CompressFormat.PNG.toString()); intent.putExtra("noFaceDetection",
	 * true); // no face detection startActivityForResult(intent,
	 * HsActivity_Grid_HsBitmap.REQUESTCODE_CHOOSESMALLIMAGE); }
	 */

	/**
	 * 调用相机拍照
	 * @throws Exception
	 */
	private void takePhoto() throws Exception
	{
		mCurrentTempImageCachedUri = createTempImageCachedUri();

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra("return-data", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentTempImageCachedUri);

		startActivityForResult(intent,
				HsActivity_Grid_HsBitmap.REQUESTCODE_TAKEPHOTO);
	}

	// }}
	
	
	//{{ 图片适配器
	
	/**
	 * c
	 * @param datas
	 * @return
	 */
	@Override
	protected BaseAdapter createAdapter(List<HsBitmap> datas)
	{
		return new HsBitmapAdapter(getActivity(), datas);
	}

	@Override
	protected BaseAdapter getAdapter()
	{
		if (this.ucGridView.getAdapter() == null)
		{
			this.ucGridView
					.setAdapter(createAdapter(new ArrayList<HsBitmap>()));
		}
		return (HsBitmapAdapter) this.ucGridView.getAdapter();
	}
	
	//}}

	//{{ 重载祖先或接口
	
	@Override
	protected void addItem(HsBitmap item)
	{
		super.addItem(item);
		
		HsBitmapAdapter adapter = (HsBitmapAdapter) this.getAdapter();

		adapter.addItem(item);
		
		//对图片重新排序
		adapter.sortItem();
	}
	
	@Override
	protected void addItems(List<HsBitmap> items)
	{
		super.addItems(items);
		
		HsBitmapAdapter adapter = (HsBitmapAdapter) this.getAdapter();

		adapter.addItems(items);
	}
	
	@Override
	protected void removeItem(int position)
	{
		super.removeItem(position);
		
		HsBitmapAdapter adapter = (HsBitmapAdapter) this.getAdapter();

		adapter.removeItem(position);
	}
	
	@Override
	protected HsBitmap getItem(int position)
	{
		return (HsBitmap) this.getAdapter().getItem(position);
	}

	@Override
	protected void setSummaryText() { }

	/**
	 * 获取图像数量
	 */
	@Override
	protected int getAnnexCount()
	{
		return mImageCachedPaths == null ? 0 : mImageCachedPaths.size();
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
			mImageCachedPaths = new ArrayList<String>();
			super.callRetrieveOnOtherThread();
		}
	}

	/**
	 * 此方法用于刷新获取数据，如果适配器中已经包含数据则不需要实现此方法。开始链式调用
	 */
	@Override
	protected HsWSReturnObject retrieve() throws Exception
	{
		return this.mApplication.getWebService().getSysImageHashDatas(this.mApplication.getLoginData().getProgressId(), mAnnexClass, mAnnexClassId);
	}
	
	private void getSysImageById()
	{
		if(mSysImageHashDatas.size() > 0 && mImageIndex < mSysImageHashDatas.size())
		{
			File file = new File(mImageCachedDir + mSysImageHashDatas.get(mImageIndex).HashData + ".png");

			//加入图片如果已经缓存过直接复制显示，否则从网络检索
			if(file.exists())
			{
				ShowImageThread t = new ShowImageThread(file,mPrevShowImageThread);
				t.start();
				mPrevShowImageThread = t;

				this.mImageIndex++;
				// 再次调用刷新，直接调用祖先方法获取下一张图片
				getSysImageById();
				
			}else 
			{
				//启动链式调用反应。
				showWait("请稍候", "正在显示第" + (mImageIndex + 1) + "张图片...");
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							HsWSReturnObject object = mApplication.getWebService().getSysImageById(
									mApplication.getLoginData().getProgressId(),
									mSysImageHashDatas.get(mImageIndex).ImageId);
							sendDataMessage(object); 
						} catch (Exception e)
						{
							sendErrorMessage(e.getMessage());
						}
					}
				}).start();
			}
		}else {
			mHasRetrieved = true;
			if(mSysImageHashDatas.size() > 0)
			{
				showInformation("正在显示剩余图片，请稍候...");
			}else {
				showInformation("没有图片信息。");
			}
		}
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
			mImageIndex = 0;
			updateSysImage();
		}else 
		{
			showInformation("更新成功");
			
			triggerCommEvent(EventCategory.UpdateCompleted,null);
		}
	}
	

	/**
	 * 更新图像
	 */
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
	
	//}}

	
	/**
	 * 点击图片时调度，此处对应浏览图片
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		browseImages(arg2);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == Activity.RESULT_OK)
		{
			switch (requestCode)
			{
			case HsActivity_Grid_HsBitmap.REQUESTCODE_TAKEPHOTO: // 对拍照返回的数据进行裁切
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(mCurrentTempImageCachedUri, "image/*");
				intent.putExtra("crop", "true");
//				intent.putExtra("aspectX", 1);
//				intent.putExtra("aspectY", 1);
//				intent.putExtra("outputX", 800);
//				intent.putExtra("outputY", 800);
				intent.putExtra("scale", false);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentTempImageCachedUri);
				intent.putExtra("return-data", false);
				intent.putExtra("outputFormat",
						Bitmap.CompressFormat.PNG.toString());
				intent.putExtra("noFaceDetection", true); // no face detection
				startActivityForResult(intent, REQUESTCODE_CROPPHOTO);
				break;
			case HsActivity_Grid_HsBitmap.REQUESTCODE_CHOOSEIMAGE:
			case HsActivity_Grid_HsBitmap.REQUESTCODE_CROPPHOTO:
				try
				{
					Bitmap bitmap = HsBitmap.loadBitmapFromDisk(this.getActivity(),mCurrentTempImageCachedUri);// decode
																		// bitmap
					if (bitmap != null)
					{
						HsBitmap previewBitmap = new HsBitmap(bitmap)
								.getPreviewHsBitmap(150);
						previewBitmap.setStorePath(mCurrentTempImageCachedUri
								.getPath());
						mImageCachedPaths.add(mCurrentTempImageCachedUri.getPath());
						
						addItem(previewBitmap);

						// 标记修改
						setIsModified(true);


					}
				} catch (Exception e)
				{
					showError(e);
				}
				break;
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
		if(funcname.equals("GetSysImageHashDatas"))
		{
			//获取服务器端图片列表哈希值
			mSysImageHashDatas = new ModelHsBitmaps().Create(HsGZip.DecompressString(data.toString()));

			mImageIndex = 0; //赋初始值，开始链式调用
			
			getSysImageById();
		}
		else if(funcname.equals("GetSysImageById"))
		{
			if (!data.equals(""))
			{
				HsBitmap bitmap = new ModelHsBitmap().Create(HsGZip.DecompressString(data.toString()));
				
				ShowImageThread t = new ShowImageThread(bitmap, mPrevShowImageThread);

				t.start();
				mPrevShowImageThread = t;

				this.mImageIndex++;
				// 再次调用刷新，直接调用祖先方法获取下一张图片
				getSysImageById();

			}
		}
		else if(funcname.equals("UpdateSysImage"))
		{
			mNeedDeleteImageMaxId = Math.min(Integer.valueOf(data.toString()),mNeedDeleteImageMaxId);
			mImageIndex ++;
			updateSysImage();
		}else if(funcname.equals("DeleteSysImages"))
		{
			showInformation("更新成功");
			
			mIsModified = false;
			
			//发布更新完成事件
			triggerCommEvent(EventCategory.UpdateCompleted, null);
		}
		else
		{
			super.actionAfterWSReturnData(funcname, data);

		}
	}

	
	//}}
	
	
	//{{ 内部类
	
	/**
	 * 显示图片进程类，此进程将图片添加至显示列表时，应保证上一张图片已经处理完毕。这样确保图片按顺序显示。
	 * @author zhaixuan
	 *
	 */
	private class ShowImageThread extends Thread
	{
		/**
		 * 线程顺序号
		 */
		//private int mIndex;
		
		private HsBitmap mHsBitmap;
		
		/**
		 * 图片数据
		 */
		private String mImageData;
		
		private Uri mUri;

		/**
		 * 上一个图片处理类
		 */
		private ShowImageThread mPrevThread;
		
		public ShowImageThread(HsBitmap hsBitmap,ShowImageThread prevThred)
		{
			mHsBitmap = hsBitmap;
			mUri = Uri.fromFile(new File(mImageCachedDir + hsBitmap.HashData + ".png"));
			mPrevThread = prevThred;
		}
		
		//public ShowImageThread(int index,String data, ShowImageThread prevThread)
		public ShowImageThread(String imageName, String imageData,ShowImageThread prevThread)
		{
			//mIndex = index;
			mImageData = imageData;
			mUri = Uri.fromFile(new File(mImageCachedDir + imageName + ".png"));
			mPrevThread = prevThread;
		}
		
		public ShowImageThread(File file,ShowImageThread prevThread)
		{
			mUri = Uri.fromFile(file);
			mPrevThread = prevThread;
		}

		@Override
		public void run()
		{
			try
			{
//				HsBitmap bitmap = null;
				
				if(mHsBitmap == null)
				{
					if(mImageData != null)
					{
//						logi("准备解压缩图片。");
						mHsBitmap = new HsBitmap(HsGZip.DecompressString(mImageData));
//						logi("解压缩图片完成。");
//						logi("准备缓存图像到本地存储。");
						
						// 缓存图像到本地，并将图像本地存储路径保存至集合
						mHsBitmap.saveHsBitmapToDisk(mUri);

//						logi("缓存图像到本地存储完成。");
					}else 
					{
						mHsBitmap = new HsBitmap(HsBitmap.loadBitmapFromDisk(getActivity(),mUri));
					}
				}else {
					mHsBitmap.saveHsBitmapToDisk(mUri);
				}
				
				
//
//				logi("准备生成缩略图。");
				// 添加缩略图到显示列表
				final HsBitmap previewBitmap = mHsBitmap
						.getPreviewHsBitmap(150);
				previewBitmap.setStorePath(mUri.getPath());
//				logi("生成缩略图完成。");

				if(mPrevThread != null && !mPrevThread.isInterrupted())
				{
//					logi("等待上一线程");
					mPrevThread.join();
				}
				
//				logi("恢复线程执行");
				mImageCachedPaths.add(mUri.getPath());
				
				// 如果需要刷新完数据一起显示，可以取消注释下行，将刷新的数据添加至列表中。
				mHandler.post(new Runnable()
				{
					@Override
					public void run()
					{
						addItem(previewBitmap);
					}
				});
			} catch (final Exception e)
			{
				mHandler.post(new Runnable()
				{
					@Override
					public void run()
					{
						showError(e);

					}
				});
			}
		}
	}


	//}}
	

}
