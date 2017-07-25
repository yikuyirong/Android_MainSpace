package com.hungsum.framework.ui.controls;

import java.util.regex.Pattern;

import android.content.Context;
import android.util.AttributeSet;

public class UcSfzh extends UcTextBox
{

	public UcSfzh(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	public UcSfzh(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public UcSfzh(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void Validate() throws Exception
	{
		super.Validate();
		
		if(!this.getControlValue().equals(""))
		{
            //定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）  
            Pattern pattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");  
            //通过Pattern获得Matcher  
            if(!pattern.matcher(this.getControlValue()).matches())
            {
            	throw new Exception("身份证号码不合法，应为15位或18位数字。");
            }
		}
	}

}
