package com.hungsum.framework.componments;

import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hungsum.framework.R;
import com.hungsum.framework.ui.controls.UcChooseSingleItem;
import com.hungsum.framework.ui.controls.UcDateBox;
import com.hungsum.framework.ui.controls.UcFormItem;
import com.hungsum.framework.ui.controls.UcMultiCheckBox;
import com.hungsum.framework.ui.controls.UcMultiRadio;
import com.hungsum.framework.ui.controls.UcTextBox;


public class HsQueryConditionItem extends UcFormItem
{
	private String _sqlOrder;	

	public HsQueryConditionItem(Context context,
			   String title,
			   String sqlOrder,
			   String Class,
			   String classInfo,
			   String classParams,
			   boolean canNull, 
			   String Default)
	{
		this._sqlOrder = sqlOrder;

		if(Class.toUpperCase(Locale.getDefault()).equals("AUTOCOMPLETETEXT"))
		{
			control = new UcChooseSingleItem(context);
			((UcChooseSingleItem)control).SetFlag(classInfo);
			((UcChooseSingleItem)control).SetParams(classParams);
		}else if(Class.toUpperCase(Locale.getDefault()).equals("DATE"))
		{
			control = new UcDateBox(context).SetShowStyle("DATE");
			((UcDateBox)control).SetFlag(classInfo);
		}else if(Class.toUpperCase(Locale.getDefault()).equals("YEARMONTH"))
		{
			control = new UcDateBox(context).SetShowStyle("YEARMONTH");
			((UcDateBox)control).SetFlag(classInfo);
			
		}else if(Class.toUpperCase(Locale.getDefault()).equals("YEAR"))
		{
			control = new UcDateBox(context).SetShowStyle("YEAR");
			((UcDateBox)control).SetFlag(classInfo);
			
		}else if(Class.toUpperCase(Locale.getDefault()).equals("CHECKBOX"))
		{
			control = new UcMultiCheckBox(context);
			((UcMultiCheckBox)control).SetDatas(classInfo, Default);
		}else if(Class.toUpperCase(Locale.getDefault()).equals("RADIO"))
		{
			control = new UcMultiRadio(context);
			((UcMultiRadio)control).SetDatas(classInfo,Default);
		}else {
			control = new UcTextBox(context);
			try
			{
				control.setControlValue(Default);
			} catch (Exception e) { }
		}
		if(control != null)
		{
			control.setCName(title);
			control.setAllowEmpty(canNull);
		}

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
		
		

		itemView.addView((View) control,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

	}

	public String GetSqlOrder()
	{
		return _sqlOrder;
	}
}
