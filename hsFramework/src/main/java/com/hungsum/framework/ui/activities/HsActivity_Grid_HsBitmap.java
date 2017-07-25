package com.hungsum.framework.ui.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;

import com.hungsum.framework.R;
import com.hungsum.framework.adapter.HsBitmapAdapter;
import com.hungsum.framework.componments.HsBitmap;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.models.ModelHsBitmaps;
import com.hungsum.framework.utils.HsGZip;

public class HsActivity_Grid_HsBitmap extends HsActivity_Grid<HsBitmap>
{
	public static final String THREAD = "Thread";
	
	public static final int REQUESTCODE_CHOOSEIMAGE = 1000;

	public static final int REQUESTCODE_CROPPHOTO = 1001;

	public static final int REQUESTCODE_TAKEPHOTO = 1002;

	/*
	 * 图片分类
	 */
	private String inClass;

	/*
	 * 图片编号
	 */
	private String inClassId;

	/*
	 * 当前图片索引
	 */
	private int mImageIndex = 0;

	private ArrayList<HsBitmap> bitmapHashdatas;
	
	/*
	 * 图片缓存目录
	 */
	private String mImageCachedDir;

	/*
	 * 图片缓存路径
	 */
	private ArrayList<String> inImagesCachedPath;

	/*
	 * 当前图片缓存标识
	 */
	private Uri mCurrentImageCachedUri;

	/*
	 * 指示是否修改过
	 */
	private boolean mIsModified = false;

	/*
	 * 指示是否只读
	 */
	private boolean inAuditOnly = false;

	private boolean inHasRetrieved = false;

	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.inTitle = "图像处理";

		// 设置缓存图片目录
		mImageCachedDir = _initImageCachedDir();
	}

	@Override
	protected void initInComingVariable(Bundle bundle) throws Exception
	{
		super.initInComingVariable(bundle);
		
		if(bundle.containsKey("Class"))
		{
			inClass = bundle.getString("Class");
		}

		if(bundle.containsKey("ClassId"))
		{
			inClassId = bundle.getString("ClassId");
		}

		if(bundle.containsKey("AuditOnly"))
		{
			inAuditOnly = bundle.getBoolean("AuditOnly", false);
		}

		if(bundle.containsKey("HasRetrieved"))
		{
			inHasRetrieved = bundle.getBoolean("HasRetrieved", false);
		}

		if(bundle.containsKey("ImagesCachedPath"))
		{
			inImagesCachedPath = bundle.getStringArrayList("ImagesCachedPath");
		}
		
	}
	
	@Override
	protected void initActivityView()
	{
		super.initActivityView();

		registerForContextMenu(this.ucGridView);
	}

	// {{菜单

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
		// 审核状态下不允许删除
		if (!inAuditOnly)
		{
			menu.addSubMenu(0, R.string.str_delete, 2, "删除").setIcon(
					R.drawable.content_remove);
		}
	}

	/*
	 * 上下文菜单回调函数
	 */
	@Override
	public boolean onContextItemSelected(final MenuItem item)
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
				getHsBitmapAdapter().removeItem(position);
				// 删除磁盘缓存文件
				new File(inImagesCachedPath.get(position)).delete();
				// 删除缓存列表文件
				inImagesCachedPath.remove(position);

				mIsModified = true;
			}
		} catch (Exception e)
		{
			ShowError(e);
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		// 审核状态下不允许拍照、选择相册
		if (!inAuditOnly)
		{
			// 拍照
			menu.add(0, R.string.str_takePhoto, 0,
					getString(R.string.str_takePhoto))
					.setIcon(android.R.drawable.ic_menu_camera)
					.setShowAsActionFlags(
							MenuItem.SHOW_AS_ACTION_IF_ROOM
									| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

			// 选择
			menu.add(0, R.string.str_chooseImage, 1,
					getString(R.string.str_chooseImage))
					.setIcon(android.R.drawable.ic_menu_gallery)
					.setShowAsActionFlags(
							MenuItem.SHOW_AS_ACTION_IF_ROOM
									| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

			// 保存
			menu.add(0, R.string.str_enter, 2, getString(R.string.str_enter))
					.setIcon(android.R.drawable.ic_menu_save)
					.setShowAsActionFlags(
							MenuItem.SHOW_AS_ACTION_IF_ROOM
									| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		}

		return true;
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
				ShowError(e);
			}
			return true;
		} else if (item.getItemId() == R.string.str_chooseImage)
		{
			try
			{
				this.chooseBigImage();
			} catch (Exception e)
			{
				ShowError(e);
			}
			return true;
		} else if (item.getItemId() == R.string.str_enter)
		{
			this.setResult(Activity.RESULT_OK, _getResultIntent());
			this.finish();
			return true;
		} else
		{
			return super.onOptionsItemSelected(item);
		}
	}

	// }}

	/*
	 * 获取图片缓存URI
	 */
	private Uri _getImageCachedUri() throws IOException
	{
		String fileName = mImageCachedDir + new Date().getTime() + ".png";

		File file = new File(fileName);

		return Uri.fromFile(file);

	}

	// {{ 浏览图册或拍照

	private void browseImages(int position)
	{
		Intent intent = new Intent(this, HsActivity_ShowImages.class);
		intent.putExtra("ImagesCachedPath", inImagesCachedPath);
		intent.putExtra("Position", position);
		startActivity(intent);
	}

	private void chooseBigImage() throws Exception
	{
		mCurrentImageCachedUri = _getImageCachedUri();

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
//		intent.putExtra("aspectX", 1);
//		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 800);
//		intent.putExtra("outputY", 800);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentImageCachedUri);
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

	private void takePhoto() throws Exception
	{
		mCurrentImageCachedUri = _getImageCachedUri();

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra("return-data", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentImageCachedUri);

		startActivityForResult(intent,
				HsActivity_Grid_HsBitmap.REQUESTCODE_TAKEPHOTO);
	}

	// }}

	/**
	 * 
	 * 准备图像缓存目录
	 * 
	 * @return
	 * @throws Exception
	 */
	private String _initImageCachedDir() throws Exception
	{
		// 临时文件
		String tempImageFileString = this.application.getDownloadRootPath()
				+ "ImagesCached/";

		File file = new File(tempImageFileString);
		// 如果目录不存在则创建，如果存在则清空其中之内容
		if (!file.exists())
		{
			file.mkdirs();
		} else
		{
			if (!inHasRetrieved) // 如果已经刷新过图片则不能删除缓存图片。
			{
				// 删除所有缓存照片
				for (File f : new File(tempImageFileString).listFiles())
				{
					f.delete();
				}
			}
		}

		return tempImageFileString;

	}

	/*
	 * 获取返回意图
	 */
	private Intent _getResultIntent()
	{
		Intent intent = new Intent();

		intent.putExtra("IsModified", mIsModified); // 是否修改
		intent.putExtra("ImagesCachedPath", inImagesCachedPath); // 图片本地存储路径

		return intent;

	}

	@Override
	protected BaseAdapter createAdapter(List<HsBitmap> datas)
	{
		return new HsBitmapAdapter(this, datas);
	}

	private HsBitmapAdapter getHsBitmapAdapter()
	{
		if (this.ucGridView.getAdapter() == null)
		{
			this.ucGridView
					.setAdapter(createAdapter(new ArrayList<HsBitmap>()));
		}
		return (HsBitmapAdapter) this.ucGridView.getAdapter();
	}

	/**
	 * 添加图像到显示列表
	 * 
	 * @param bitmap
	 */
	private void _addHsBitmap(HsBitmap bitmap)
	{
		getHsBitmapAdapter().addItem(bitmap);
		
		//对图片重新排序
		getHsBitmapAdapter().sortItem();
	}

	@SuppressWarnings("unused")
	private void _addHsBitmaps(List<HsBitmap> bitmaps)
	{
		getHsBitmapAdapter().addItems(bitmaps);
	}

	@Override
	protected void callRetrieveOnOtherThread()
	{
		//如果图片已经刷新过，则自缓存中载入，否则刷新图片
		if (inHasRetrieved)
		{
			try
			{
				for (String path : inImagesCachedPath)
				{
					Bitmap bitmap = HsBitmap.loadBitmapFromDisk(this, path);

					if (bitmap != null)
					{
						HsBitmap previewBitmap = new HsBitmap(bitmap)
								.getPreviewHsBitmap(150);
						previewBitmap.setStorePath(path);
						_addHsBitmap(previewBitmap);
					}
				}
			} catch (Exception e)
			{
				ShowError(e);
			}
		} else
		{
			inImagesCachedPath = new ArrayList<String>();
			super.callRetrieveOnOtherThread();
		}
	}

	/*
	 * 此方法用于刷新获取数据，如果适配器中已经包含数据则不需要实现此方法。
	 */
	@Override
	protected HsWSReturnObject retrieve() throws Exception
	{
		if(bitmapHashdatas == null)
		{
			return this.application.getWebService().getSysImageHashDatas(
					this.application.getLoginData().getProgressId(), inClass, inClassId);
		}else {
			return this.application.getWebService().getSysImageById(
					this.application.getLoginData().getProgressId(), bitmapHashdatas.get(mImageIndex).ImageId);
		}
	}

	/*
	 * 点击图片是调度，此处对应浏览图片
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		browseImages(arg2);
	}

	private String _cacheHsBitmapToDisk(HsBitmap bitmap) throws Exception
	{
		String storeFileString = mImageCachedDir + new Date().getTime()
				+ ".png";

		FileOutputStream out = null;
		try
		{

			out = new FileOutputStream(storeFileString);
			if (!bitmap.getBitmap()
					.compress(Bitmap.CompressFormat.PNG, 80, out))
			{
				throw new Exception("图像保存失败");
			}
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			out.close();
		}
		return storeFileString;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == Activity.RESULT_OK)
		{
			switch (requestCode)
			{
			case HsActivity_Grid_HsBitmap.REQUESTCODE_TAKEPHOTO: // 对拍照返回的数据进行裁切
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(mCurrentImageCachedUri, "image/*");
				intent.putExtra("crop", "true");
//				intent.putExtra("aspectX", 1);
//				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 800);
//				intent.putExtra("outputY", 800);
				intent.putExtra("scale", false);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentImageCachedUri);
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
					Bitmap bitmap = HsBitmap.loadBitmapFromDisk(this,mCurrentImageCachedUri);

					if (bitmap != null)
					{
						HsBitmap previewBitmap = new HsBitmap(bitmap)
								.getPreviewHsBitmap(150);
						previewBitmap.setStorePath(mCurrentImageCachedUri
								.getPath());
						inImagesCachedPath.add(mCurrentImageCachedUri.getPath());
						_addHsBitmap(previewBitmap);
						// 标记修改
						mIsModified = true;

					}
				} catch (Exception e)
				{
					ShowError(e);
				}
				break;
			default:
				super.onActivityResult(requestCode, resultCode, data);
				break;
			}
		}

	}

	/**
	 * Handler对象，用于线程间通信。
	 */
	static class ImageInnerHandler extends Handler
	{
		WeakReference<HsActivity> wr;

		public ImageInnerHandler(HsActivity activity)
		{
			wr = new WeakReference<HsActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);

			HsActivity activity = wr.get();

			Bundle bundle = msg.getData();
			if (msg.what == R.integer.msg_data)
			{
				try
				{
					activity.actionAfterWSReturnData(
							bundle.getString(FUNCNAME), bundle.getString(DATA));
				} catch (Exception e)
				{
					activity.ShowError(e);
				}
			} else
			{
				activity.ShowError(bundle.getString(DATA));
			}
		}

	}

	private ShowImageThread mPrevShowImageThread = null;
	
	/**
	 * 显示图片进程类，此进程将图片添加至显示列表时，应保证上一张图片已经处理完毕。这样确保图片按顺序显示。
	 * @author zhaixuan
	 *
	 */
	private class ShowImageThread extends Thread
	{
		/**
		 * 图片数据
		 */
		private String mData;
		
		/**
		 * 上一个图片处理类
		 */
		private ShowImageThread mPrevThread;
		
		public ShowImageThread(String data, ShowImageThread prevThread)
		{
			mData = data;
			mPrevThread = prevThread;
		}

		@Override
		public void run()
		{
			try
			{
//				logi("准备解压缩图片。");
				HsBitmap bitmap = new HsBitmap(HsGZip
						.DecompressString(mData));
//				logi("解压缩图片完成。");
//				logi("准备缓存图像到本地存储。");
				// 缓存图像到本地，并将图像本地存储路径保存至集合
				String storePath = _cacheHsBitmapToDisk(bitmap);
//				logi("缓存图像到本地存储完成。");
//
//				logi("准备生成缩略图。");
				// 添加缩略图到显示列表
				final HsBitmap previewBitmap = bitmap
						.getPreviewHsBitmap(150);
				previewBitmap.setStorePath(storePath);
//				logi("生成缩略图完成。");

				if(mPrevThread != null && !mPrevThread.isInterrupted())
				{
//					logi("等待上一线程");
					mPrevThread.join();
				}
				
//				logi("恢复线程执行");
				inImagesCachedPath.add(storePath);
				
				// 如果需要刷新完数据一起显示，可以取消注释下行，将刷新的数据添加至列表中。
				mHandler.post(new Runnable()
				{
					@Override
					public void run()
					{
						_addHsBitmap(previewBitmap);
					}
				});
			} catch (final Exception e)
			{
				mHandler.post(new Runnable()
				{
					@Override
					public void run()
					{
						ShowError(e);

					}
				});
			}
		}
	}
	
	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if(funcname.equals("GetSysImageHashDatas"))
		{
			bitmapHashdatas = new ModelHsBitmaps().Create(HsGZip.DecompressString(data.toString()));
			
			//如果存在图片集则循环调用显示
			if(bitmapHashdatas != null && bitmapHashdatas.size() > 0)
			{
				super.callRetrieveOnOtherThread();
			}
		}
		else if (funcname.equals("GetSysImageById"))
		{
			ShowImageThread t = new ShowImageThread(data.toString(), mPrevShowImageThread);
		
			t.start();
			
			mPrevShowImageThread = t;
			
			this.mImageIndex++;

			if(mImageIndex < bitmapHashdatas.size())
			{
				// 再次调用刷新，直接调用祖先方法获取下一张图片
				super.callRetrieveOnOtherThread();
			}else {
				ShowInformation("正在显示剩余图片，请稍候...");
			}
		} else
		{
			super.actionAfterWSReturnData(funcname, data);

		}
	}

}
