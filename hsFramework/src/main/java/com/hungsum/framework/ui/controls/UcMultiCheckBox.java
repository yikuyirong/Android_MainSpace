package com.hungsum.framework.ui.controls;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ListView;

import com.hungsum.framework.componments.HsLabelValue;

public class UcMultiCheckBox extends UcTextBoxWithDialog implements DialogInterface.OnClickListener
{
	private String[] _defaultKeys;
	
	private HsKeyLabels _currentValues;
	
	private HsKeyLabels _values;

	public UcMultiCheckBox(Context context)
	{
		super(context);
	}

	public UcMultiCheckBox(Context context ,AttributeSet attrs)
	{
		super(context,attrs);
	}

	public UcMultiCheckBox(Context context,AttributeSet attrs,int defStyle)
	{
		super(context,attrs,defStyle);
	}

	public void SetDatas(String data,String defaultKeys)
	{
		this._values = new HsKeyLabels(data);

		//设置默认值
		this._defaultKeys = defaultKeys.split(",");
		
		SetData(this._defaultKeys);
	}
	
	public void SetData(String[] keys)
	{
		_setControlData(_values.getValues(keys));

	}
	
			
	private boolean[] _getCheched()
	{
		boolean[] checkeds = new boolean[_values.size()];
		
		String[] keys = _values.getKeys();
		
		for(int i=0;i < keys.length ; i++)
		{
			for (String currentKey : _currentValues.getKeys())
			{
				if(keys[i].equals(currentKey))
				{
					checkeds[i] = true;
					break;
				}else {
					checkeds[i] = false;
				}
			}
		}
		return checkeds;
	}
	

	@Override
	public CharSequence getControlValue()
	{
		return this._currentValues.getKeyValue();
	}

	public CharSequence getControlTitle()
	{
		return this._currentValues.getLabelValue();
	}

	@Override
	public void setControlValue(CharSequence value)
	{
		SetData(value.toString().split(","));
	}

	private void _setControlData(HsKeyLabels values)
	{
		this._currentValues = values;
		
		super.setControlValue(values.toString());
	}

	@Override
	protected void showDialog()
	{
	 	AlertDialog ad = new AlertDialog.Builder(this.getContext())
		.setTitle("请选择" + (getCName() == null ? "" : getCName()))
		.setIcon(android.R.drawable.ic_dialog_info)
		.setMultiChoiceItems(_values.getLabels(),_getCheched(),new OnMultiChoiceClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked){}
		})
		.setPositiveButton("确认",this).create();
	 	ad.show();

	 	//设置大小  
        WindowManager.LayoutParams layoutParams = ad.getWindow().getAttributes();  
        layoutParams.width = LayoutParams.MATCH_PARENT;  
        layoutParams.height = LayoutParams.WRAP_CONTENT;  
        ad.getWindow().setAttributes(layoutParams);  	
	}

	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		if(_currentValues == null)
		{
			_currentValues = new HsKeyLabels(null);
		}else {
			_currentValues.clear();
		}

		ListView listView = ((AlertDialog)dialog).getListView();
		SparseBooleanArray sba = listView.getCheckedItemPositions();
		
		for(int i=0;i < sba.size();i++)
		{
			if(sba.valueAt(i))
			{
				int key = sba.keyAt(i);
				_currentValues.add(_values.get(key));
			}
			
//			if( sba.get(i))
//			{
//				_currentValues.add(_values.get(i));
//			}
		}

		_setControlData(_currentValues);
	}

	private class HsKeyLabels extends ArrayList<HsLabelValue>
	{
		private static final long serialVersionUID = -8669794228471330863L;
		
		public HsKeyLabels(String data)
		{
			if(data != null && !data.equals(""))
			{
				this.clear();
				for (String value : data.split(";")) 
				{
					this.add(new HsLabelValue(value));
				}
			}
		}
		
		public String[] getKeys()
		{
			String[] keys = new String[this.size()];
			for(int i=0;i<this.size();i++)
			{
				keys[i] = this.get(i).getLabel();
			}
			return keys;
		}
	
		public String[] getLabels()
		{
			String[] labels = new String[this.size()];
			for(int i=0;i<this.size();i++)
			{
				labels[i] = this.get(i).getLabel();
			}
			return labels;
		}
		
		public HsKeyLabels getValues(String[] keys)
		{
			HsKeyLabels values = new HsKeyLabels(null);
			for(int i=0 ; i < this.size(); i++)
			{
				for (String key : keys)
				{
					if(this.get(i).getValue().equals(key))
					{
						values.add(this.get(i));
					}
				}
			}
			return values;
		}

		public String getKeyValue()
		{
			StringBuffer sb = new StringBuffer();
			for(HsLabelValue value : this)
			{
				if(sb.length() > 0)
				{
					sb.append(",");
				}
				sb.append(value.getValue());
			}
			return sb.toString();
		}

		public String getLabelValue()
		{
			StringBuffer sb = new StringBuffer();
			for(HsLabelValue value : this)
			{
				sb.append(value.getLabel() + " ");
			}
			return sb.toString().trim();
		}
		
		@Override
		public String toString()
		{
			return getLabelValue();
		}
		
	}

}
