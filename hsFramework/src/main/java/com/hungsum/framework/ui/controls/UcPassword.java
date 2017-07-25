package com.hungsum.framework.ui.controls;

import android.content.Context;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;

public class UcPassword extends UcTextBox
{

	public UcPassword(Context context)
	{
		super(context);

		this.setTransformationMethod(PasswordTransformationMethod.getInstance());
	}

	public UcPassword(Context context, AttributeSet attrs)
	{
		super(context,attrs);

		this.setTransformationMethod(PasswordTransformationMethod.getInstance());
	}
	
	public UcPassword(Context context, AttributeSet attrs, int defStyle)		
	{
		super(context,attrs,defStyle);

		this.setTransformationMethod(PasswordTransformationMethod.getInstance());
	}

}
