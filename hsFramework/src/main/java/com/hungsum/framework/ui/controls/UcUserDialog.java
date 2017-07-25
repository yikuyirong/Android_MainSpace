package com.hungsum.framework.ui.controls;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.hungsum.framework.interfaces.IControlValue;
import com.hungsum.framework.ui.activities.HsActivity;

public class UcUserDialog
{
	private HsActivity mContext;
	
	private List<IControlValue> mControls;
	
	private Builder mBuilder;
	
	public UcUserDialog(HsActivity context)
	{
		mContext = context;
		
		mControls = new ArrayList<IControlValue>();
		
		mBuilder = new AlertDialog.Builder(context);
	}

	public UcUserDialog setTitle(String title)
	{
		mBuilder.setTitle(title);
		
		return this;
	}
	
	public UcUserDialog setView(View view)
	{
		mBuilder.setView(view);
		
		return this;
	}
	
	public UcUserDialog addControl(IControlValue control)
	{
		mControls.add(control);
		return this;
	}

	
	public UcUserDialog setPositiveButton(String title,final OnClickListener listener)
	{
		mBuilder.setPositiveButton(title, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				try
				{
					for (IControlValue controlValue : mControls)
					{
						controlValue.Validate();
					}
					listener.onClick(dialog, which);
				} catch (Exception e)
				{
					mContext.ShowError(e);
				}
				
			}
		});
		
		return this;
	}
	
	public UcUserDialog setNegativeButton(String title,OnClickListener listener)
	{
		mBuilder.setNegativeButton(title, listener);
		
		return this;
	}
	
	public void show()
	{
		AlertDialog ad = mBuilder.create();
		ad.show();

	 	//设置大小  
        WindowManager.LayoutParams layoutParams = ad.getWindow().getAttributes();  
        layoutParams.width = LayoutParams.MATCH_PARENT;  
        layoutParams.height = LayoutParams.WRAP_CONTENT;  
        ad.getWindow().setAttributes(layoutParams);  		
	}
	
}
