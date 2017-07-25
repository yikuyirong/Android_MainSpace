package com.hungsum.framework.ui.controls;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.BaseAdapter;

import com.hungsum.framework.componments.HsLabelValue;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.interfaces.ISingleChoose;

public abstract class UcChooseSingleBase extends UcTextBoxWithDialog implements
		ISingleChoose, DialogInterface.OnClickListener
{

	protected BaseAdapter mAdapter;

	protected IHsLabelValue mCurrentValue;

	public UcChooseSingleBase(Context context)
	{
		super(context);
	}

	public UcChooseSingleBase(Context context, AttributeSet attrs)		
	{
		super(context,attrs);
	}

	public UcChooseSingleBase(Context context, AttributeSet attrs, int defStyle)		
	{
		super(context,attrs,defStyle);
	}
	
	protected int getPosition(String value)
	{
		for(int i=0;i<this.mAdapter.getCount();i++)
		{
			if(((HsLabelValue) this.mAdapter.getItem(i)).getValue().equals(value))
			{
				return i;
			}
		}
		return 0;
	}

	protected IHsLabelValue getDataByValue(String value)
	{
		for(int i=0;i<this.mAdapter.getCount();i++)
		{
			if(((HsLabelValue) this.mAdapter.getItem(i)).getValue().equals(value))
			{
				return (IHsLabelValue) this.mAdapter.getItem(i);
			}
		}
		return null;
	}

	protected void setData(IHsLabelValue data)
	{
		this.mCurrentValue = data;

		super.setControlValue(data.getLabel());
	}

	/******************* 实现 IControlValue ****************************************/

	@Override
	public CharSequence getControlValue()
	{
		return mCurrentValue == null ? "" : mCurrentValue.getValue().toString();
	}

	public CharSequence getControlTitle()
	{
		return this.mCurrentValue.getLabel();
	}

	@Override
	public void setControlValue(CharSequence value)
	{
		if (value.toString().contains(","))
		{
			String[] ss = value.toString().split(",");
			if(ss.length > 1)
			{
				SetData(ss[0], ss[1]);
			}else {
				SetData(ss[0], "");
			}
		} else
		{
			SetData(value.toString());
		}
	}

	/******************* 实现 ISingleChoose ****************************************/

	public ISingleChoose SetData(String value, String key)
	{
		setData(new HsLabelValue(key,value));
		return this;
	}

	public ISingleChoose SetData(String key)
	{
		HsLabelValue keyLabel = (HsLabelValue) getDataByValue(key);
		if (keyLabel != null)
		{
			setData(keyLabel);
		}
		return this;
	}

}
