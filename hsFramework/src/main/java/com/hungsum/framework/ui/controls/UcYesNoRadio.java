package com.hungsum.framework.ui.controls;

import android.content.Context;
import android.util.AttributeSet;

public class UcYesNoRadio extends UcMultiRadio
{

	public UcYesNoRadio(Context context)
	{
		super(context);
		
		this.SetDatas("1,是;0,否", "0");
	}

	public UcYesNoRadio(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		this.SetDatas("1,是;0,否", "0");
	}

	public UcYesNoRadio(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		this.SetDatas("1,是;0,否", "0");
	}

}
