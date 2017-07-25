package com.hungsum.framework.ui.controls;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.BaseAdapter;

public abstract class UcChooseMultiBase extends UcTextBoxWithDialog implements
		DialogInterface.OnClickListener
{

	protected BaseAdapter mAdapter;

	public UcChooseMultiBase(Context context)
	{
		super(context);
	}

	public UcChooseMultiBase(Context context, AttributeSet attrs)		
	{
		super(context,attrs);
	}

	public UcChooseMultiBase(Context context, AttributeSet attrs, int defStyle)		
	{
		super(context,attrs,defStyle);
	}
}
