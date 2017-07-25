package com.hungsum.framework.componments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.hungsum.framework.interfaces.IHsLabelValue;

public class HsLabelValue implements Serializable, IHsLabelValue
{
	private static final long serialVersionUID = 5733964049870027025L;

	private String mLabel;
	private Object mValue;
	private int mColor;
	private boolean mIsShowDetailImage = true;	

	private ArrayList<IHsLabelValue> mDetails = new ArrayList<IHsLabelValue>();

	public HsLabelValue()
	{
		mColor = Color.BLACK;
	}
	
	public HsLabelValue(String label,Object value,int color)
	{
		this.mValue = value;
		this.mLabel = label;
		this.mColor = color;
		this.mIsShowDetailImage = true;
	}
	
	public HsLabelValue(String key,Object value)
	{
		this(key, value, Color.BLACK);
	}
	
	public HsLabelValue(String value)
	{
		if(value != null)
		{
			String[] ss = value.split(",");
			this.mValue = ss[0];
			this.mLabel = ss[1];
		}
	}

	@Override
	public Object getValue()
	{
		return this.mValue;
	}
	
	@Override
	public void setValue(Object value)
	{
		this.mValue = value;
	}

	@Override
	public String getLabel()
	{
		return this.mLabel;
	}
	
	public void SetLabel(String label)
	{
		this.mLabel = label;
	}
	
	@Override
	public int getColor()
	{
		return mColor;
	}
	
	public void SetColor(int color)
	{
		this.mColor = color;
	}
	
	@Override
	public void addDetail(HsLabelValue detail)
	{
		for (IHsLabelValue item : mDetails)
		{
			if(item.getLabel().equals(detail.getLabel()))
			{
				item.setValue(detail.getValue());
				return;
			}
		}
		mDetails.add(detail);
	}
	
	public int getDetailsCount()
	{
		return mDetails.size();
	}
	
	@Override
	public List<IHsLabelValue> getDetails()
	{
		return mDetails;
	}

	@Override
	public String toString() 
	{
		return mLabel;
	}

	@Override
	public Object getValue(String key, Object defaultValue)
	{
		for (IHsLabelValue item : getDetails())
		{
			if(item.getLabel().equals(key))
			{
				return item.getValue();
			}
		}
		return defaultValue;
	}

	@Override
	public boolean getIsShowDetailImage()
	{
		return mIsShowDetailImage;
	}
	
	public void SetIsShowDetailImage(boolean value)
	{
		mIsShowDetailImage = value;
	}

	@Override
	public Drawable getOperationImage()
	{
		return null;
	}

}
