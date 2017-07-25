package com.hungsum.framework.ui.controls;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

import com.hungsum.framework.ui.activities.HsActivity_Grid_HsBitmap;

public class UcImageBox extends UcTextBoxWithDialog
{
	public static int REQUESTCODE_SHOWIMAGE = 0;
	
	public static int RESULTCODE_IMAGE_CHANGED = 0;
	
	public static int RESULTCODE_IMAGE_DOTCHANGED = 1;

	private boolean mAuditOnly = false;
	
	private ArrayList<String> mImagesCachedPath;
	
	/*
	 * 指示是否已经远程获取过数据。
	 */
	private boolean mHasRetrieved = false;
	
	public UcImageBox(Context context)
	{
		super(context);
	}

	public UcImageBox(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public UcImageBox(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public void setImagesCachedPath(ArrayList<String> value)
	{
		mImagesCachedPath = value;
		
		setImageCount(value.size());
		
		mHasRetrieved = true; //标记已经刷新过数据了
	}
	
	public ArrayList<String> getImagesCachedPath()
	{
		return mImagesCachedPath == null ? new ArrayList<String>() : mImagesCachedPath;
	}
	
	public int getImagesCount()
	{
		return mImagesCachedPath == null ? 0 : mImagesCachedPath.size();
	}

	@Override
	protected void showDialog()
	{
		Intent intent = new Intent(getContext(), HsActivity_Grid_HsBitmap.class);	
		intent.putExtra("Class", mClass);
		intent.putExtra("ClassId",mClassId);
		intent.putExtra("AuditOnly", mAuditOnly);
		intent.putExtra("HasRetrieved", mHasRetrieved);
		intent.putExtra("ImagesCachedPath", mImagesCachedPath);
		

		((Activity)this.getContext()).startActivityForResult(intent, UcImageBox.REQUESTCODE_SHOWIMAGE);

	}
	
	private String mClass;
	
	public UcImageBox setImageClass(String value)
	{
		mClass = value;
		return this;
	}
	
	private String mClassId;
	
	public UcImageBox setImageClassId(String value)
	{
		mClassId = value;
		return this;
	}
	
	
	/* 
	 * 重写此方法，因为ImageBox触发数据更改时间的原因与祖先不同
	 * 
	 */
	@Override
	protected void onTextChanged(CharSequence text, int start,
			int lengthBefore, int lengthAfter)
	{
		return ;
	}

	/******************* 实现 IControlValue ****************************************/

	public CharSequence getControlTitle()
	{
		return this.getText();
	}

	public void setControlTitle(String value)
	{
		this.setText(value);
	}
	
	public void setImageCount(int value)
	{
		setControlTitle(String.format("%s图",value == 0 ? "无" : value));
	}

	private CharSequence mControlValue;
	
	@Override
	public CharSequence getControlValue()
	{
		return mControlValue;
	}
	
	@Override
	public void setControlValue(CharSequence value)
	{
		mControlValue = value;
	}
	
	@Override
	public void setAllowEdit(boolean value)
	{
		mAuditOnly = !value;
	}
}
