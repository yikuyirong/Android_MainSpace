package com.hungsum.framework.ui.controls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hungsum.framework.R;
import com.hungsum.framework.interfaces.IControlValue;

public class UcFormItem
{
	protected IControlValue control;
	
	protected ViewGroup itemView;
	
	public UcFormItem(){}
	
	public UcFormItem(Context context,IControlValue control)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		itemView = (LinearLayout) inflater.inflate(R.layout.control_ucform, null);
		
		TextView ucLabel = (TextView) itemView.findViewById(R.id.ucform_label);
		ucLabel.setText(control.getCName());
		
		TextView ucStart = (TextView) itemView.findViewById(R.id.ucform_star);
		if(control.getAllowEmpty())
		{
			ucStart.setVisibility(TextView.INVISIBLE);
		}else {
			ucStart.setVisibility(TextView.VISIBLE);
		}

		//如果control已经存在父对象，则先移除。
		
		View view = (View)control;

		ViewGroup parent = (ViewGroup) view.getParent();
		
		if(parent != null)
		{
			parent.removeView(view);
		}
		
		itemView.addView(view,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

	}
	

	public IControlValue GetControl()
	{
		return control;
	}

	public ViewGroup GetView()
	{
		return itemView;
	}
	
}
