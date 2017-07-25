package com.hungsum.framework.ui.controls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hungsum.framework.componments.HsLabelValue;
import com.hungsum.framework.interfaces.IHsLabelValue;

public class UcMultiRadio extends UcChooseSingleBase
{
	public UcMultiRadio(Context context)
	{
		super(context);
	}

	public UcMultiRadio(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public UcMultiRadio(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	private String _defaultValue;

	@Override
	public void Reset()
	{
		if(_defaultValue != null)
		{
			SetData(_defaultValue);
		}
	}

	@Override
	protected void showDialog()
	{
		if(this.mAdapter != null)
		{
			int position =  getPosition(getControlValue().toString());
			AlertDialog ad = new AlertDialog.Builder(this.getContext())
			.setTitle("请选择" + (getCName() == null ? "" : getCName()))
			.setIcon(android.R.drawable.ic_dialog_info)
			.setSingleChoiceItems(mAdapter, position,null)
			.setPositiveButton("确认",this).create();
			ad.show();
		}
	}
	
	public void SetData(int index)
	{
		Object obj = mAdapter.getItem(index);
		if (obj instanceof HsLabelValue)
		{
			HsLabelValue item = (HsLabelValue)obj;
			
			setData(item);
		}
	}

	/**************************实现 DialogInterface.OnClickListener*************************************/

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		ListView listView = ((AlertDialog)dialog).getListView();
		HsLabelValue keyValue = (HsLabelValue) ((ArrayAdapter<IHsLabelValue>)mAdapter).getItem(listView.getCheckedItemPosition());
		if(keyValue != null)
		{
			setData(keyValue);
		}
	}

	@SuppressWarnings("unchecked")
	public void SetDatas(String values,String defaultValue)
	{
		if(values != null && !values.equals(""))
		{
			if(this.mAdapter == null)
			{
				this.mAdapter = new ArrayAdapter<IHsLabelValue>(getContext(), android.R.layout.simple_list_item_single_choice);
			}
			((ArrayAdapter<IHsLabelValue>) mAdapter).clear();
			for (String value : values.split(";")) 
			{
				((ArrayAdapter<HsLabelValue>)mAdapter).add(new HsLabelValue(value));
			}
			
			if(defaultValue != null)
			{
				_defaultValue = defaultValue;
				SetData(_defaultValue);
			}
		}
	}
}
