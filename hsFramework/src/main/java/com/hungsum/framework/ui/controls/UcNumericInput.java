package com.hungsum.framework.ui.controls;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

public class UcNumericInput extends UcTextBox
{
	public UcNumericInput(Context context)
	{
		super(context);
		
		setRawInputType(InputType.TYPE_CLASS_NUMBER);
	}

	public UcNumericInput(Context context, AttributeSet attrs)		
	{
		super(context,attrs);
		
		setRawInputType(InputType.TYPE_CLASS_NUMBER);
	}

	public UcNumericInput(Context context, AttributeSet attrs, int defStyle)		
	{
		super(context,attrs,defStyle);
		
		setRawInputType(InputType.TYPE_CLASS_NUMBER);
	}

	public Double getNumberValue()
	{
		if(getControlValue().toString().equals(""))
		{
			return (double) 0;
		}else {
			return Double.valueOf(getControlValue().toString());
		}
	}
	
	public void setNumberValue(Double value)
	{
		setControlValue(String.valueOf(value));
	}
}
