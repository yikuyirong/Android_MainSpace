package com.hungsum.framework.ui.activities;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.hungsum.framework.componments.HsBitmap;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.controls.UcFormItem;
import com.hungsum.framework.ui.controls.UcImageBox;
import com.hungsum.framework.utils.HsGZip;

public abstract class HsActivity_DJWithImages extends HsActivity_DJ
{
	protected UcImageBox ucImageBox;
	
	protected UcFormItem f_image;

	protected boolean mNeedUpdateImage = false;

	private int mImageIndex = 0; 
	
	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);
		
		ucImageBox = new UcImageBox(this);
		ucImageBox.setCName("图片");
		ucImageBox.setAllowEmpty(true);
		ucImageBox.setImageClass(getImageClass());
		controls.add(ucImageBox);
		
		f_image = new UcFormItem(this, ucImageBox);

	}
	
	/*
	 * 获取图片分类
	 */
	protected abstract String getImageClass();

	@Override
	protected void setDJId(String value)
	{
		super.setDJId(value);
		
		ucImageBox.setImageClassId(value);
	}
	
	@Override
	protected void newData()
	{
		super.newData();
		
		ucImageBox.setImageCount(0);
	}
	
	@Override
	protected void setData(IHsLabelValue Object)
	{
		//图片数量
		ucImageBox.setImageCount((Integer) Object.getValue("ImageCount","0"));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == UcImageBox.REQUESTCODE_SHOWIMAGE)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				mNeedUpdateImage = mNeedUpdateImage | data.getBooleanExtra("IsModified", false);
				
				if(mNeedUpdateImage)
				{
					ucImageBox.dispatchDataChangedEvents();
				}

				ucImageBox.setImagesCachedPath(data.getStringArrayListExtra("ImagesCachedPath"));
			}
		}else 
		{
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	protected void updateSysImages()
	{
		if(mNeedUpdateImage)
		{
			ShowWait("请稍候", "正在删除现有图片...");
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						HsWSReturnObject object = application.getWebService().deleteSysImages(application.getLoginData().getProgressId(), getImageClass(), getDJId(),Integer.MAX_VALUE);
						sendDataMessage(object); 
					} catch (Exception e)
					{
						sendErrorMessage(e.getMessage());
					}
				}
			}).start();
		}else {
			ShowInformation("更新成功");
			if(mIsCloseWhenUpdateCompleted)
			{
				finish();
			}
		}
	}
	
	private void _updateSysImage()
	{
		ShowWait("请稍候",String.format("正在更新第%d幅图片...",mImageIndex + 1 ) );
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					//自磁盘获取图像数据。
					HsBitmap bitmap = new HsBitmap(HsBitmap.loadBitmapFromDisk(HsActivity_DJWithImages.this, ucImageBox.getImagesCachedPath().get(mImageIndex)));
					//如果图像太大对图像进行裁切,图像宽度最大不超过800px
					if(bitmap.getWidth() > 800)
					{
						bitmap = bitmap.getPreviewHsBitmap(800);
					}
					String imageData = bitmap.getBitmapString();
					//对数据进行压缩
					imageData = HsGZip.CompressString(imageData);
					HsWSReturnObject object = application.getWebService().updateSysImage(application.getLoginData().getProgressId(),getImageClass(),getDJId(),imageData);
					sendDataMessage(object);
				} catch (Exception e)
				{
					sendErrorMessage(e.getMessage());
				}
			}
		}).start();
	}
	
	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if(funcname.equals("DeleteSysImages"))
		{
			if(mImageIndex <  ucImageBox.getImagesCount())
			{
				_updateSysImage();
			}else {
				ShowInformation("更新成功");
				if(mIsCloseWhenUpdateCompleted)
				{
					finish();
				}
			}
		}
		else if(funcname.equals("UpdateSysImage"))
		{
			mImageIndex ++;
			if(mImageIndex < ucImageBox.getImagesCount())
			{
				_updateSysImage();
			}else {
				ShowInformation("更新成功");
				if(mIsCloseWhenUpdateCompleted)
				{
					finish();
				}
			}
			
		}else {
			super.actionAfterWSReturnData(funcname, data);
		}
	}
	
}
