package com.hungsum.framework.ui.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class UcTextBoxWithDialog extends UcTextBox implements OnClickListener
{

	public UcTextBoxWithDialog(Context context)
	{
		super(context);

		this.setKeyListener(null);
		
		this.setOnClickListener(this);
	}
	
	public UcTextBoxWithDialog(Context context, AttributeSet attrs)		
	{
		super(context,attrs);

		this.setKeyListener(null);
		
		this.setOnClickListener(this);
	}
	
	public UcTextBoxWithDialog(Context context, AttributeSet attrs, int defStyle)		
	{
		super(context,attrs,defStyle);

		this.setKeyListener(null);
		
		this.setOnClickListener(this);
	}

	protected abstract void showDialog();

	@Override
	public void onClick(View v)
	{
		showDialog();
	}
	
	@Override
	public void setAllowEdit(boolean value)
	{
		super.setAllowEdit(value);
		
		if(value)
		{
			setOnClickListener(this);
		}else {
			setOnClickListener(null);
		}
	}
	
}
