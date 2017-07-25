package com.hungsum.framework.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.Filterable;

import com.hungsum.framework.R;

public class HsDeptRoleUserNamesAdapter extends BaseAdapter implements Filterable
{
	
	private List<String> mDatas;
	
	private List<String> mOriginalValues;

	/*
	 * 布局扩展
	 */
	private LayoutInflater mInflater;
	
	private int mLayoutRes;
	
	private List<String> mSelectedDatas = new ArrayList<String>();

	private OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener()
	{
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{
			String value = (String) buttonView.getText();
			if(isChecked)
			{
				if(!mSelectedDatas.contains(value))
				{
					mSelectedDatas.add(value);
				}
			}else {
				if(mSelectedDatas.contains(value))
				{
					mSelectedDatas.remove(value);
				}
			}
		}
	};

	public HsDeptRoleUserNamesAdapter(Context context,int layoutRes)
	{
		mInflater = LayoutInflater.from(context);
		
		mLayoutRes = layoutRes;
		
		mDatas = new ArrayList<String>();
		
	}
	
	public HsDeptRoleUserNamesAdapter(Context context)
	{
		this(context, R.layout.control_list_keyvalue_2);
	}

	@Override
	public int getCount()
	{
		return mDatas.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	public String getShareMessage()
	{
		StringBuffer sb = new StringBuffer();
		for (String item: mDatas)
		{
			sb.append(item + ";");
		}
		return sb.toString();
	}
	
	public void clear()
	{
		mDatas.clear();
		notifyDataSetChanged();
	}

	public void add(String value)
	{
		mDatas.add(value);
		notifyDataSetChanged();
	}
	
	public void add(List<String> datas,String selectedDatas)
	{
		mDatas.clear();
		mDatas.addAll(datas);
		
		if(selectedDatas != null)
		{
			for (String selectedData : selectedDatas.split(";"))
			{
				if(!selectedData.isEmpty() && !mSelectedDatas.contains(selectedData))
				{
					mSelectedDatas.add(selectedData);
				}
			}
		}
		
		notifyDataSetChanged();
	}
	
	public void remove(int position)
	{
		mDatas.remove(position);
		notifyDataSetChanged();
	}
	
	public String getSelectedData()
	{
		StringBuffer sb = new StringBuffer();
		
		for (String data : mSelectedDatas)
		{
			sb.append(data + ";");
		}

		//删除结尾";"
		if(sb.length() > 0)
		{
			sb.deleteCharAt(sb.length() - 1);
		}
		
		return sb.toString();
	}
	
	public void setSelectedData(String value)
	{
		mSelectedDatas.clear();
		
		for (String data : value.split(";"))
		{
			mSelectedDatas.add(data);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		String value = (String) getItem(position);

		ViewHolder holder;
		if(convertView == null)
		{
			convertView = mInflater.inflate(mLayoutRes, null);
			holder = new ViewHolder();
			holder.ucCheckBox = (CheckBox) convertView.findViewById(R.id.list_roleuser_checkbox);
			holder.ucCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(holder.ucCheckBox != null)
		{
			holder.ucCheckBox.setText(value);
			
			holder.ucCheckBox.setChecked(mSelectedDatas.contains(value));
		}
		
		return convertView;
	}

	@Override
	public Filter getFilter()
	{
		Filter filter = new Filter()
		{

			@Override
			protected FilterResults performFiltering(CharSequence constraint)
			{

                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<String> FilteredArrList = new ArrayList<String>();

                
                
                if (mOriginalValues == null) 
                {
                    mOriginalValues = new ArrayList<String>(mDatas); // saves the original data in mOriginalValues
                }

                /********
                 * 
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)  
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return  
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase(Locale.getDefault());
                    for (int i = 0; i < mOriginalValues.size(); i++) 
                    {
                    	String value = mOriginalValues.get(i);
                        if (value.toLowerCase(Locale.getDefault()).contains(constraint.toString())) 
                        {
                            FilteredArrList.add(value);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results)
			{
				mDatas = (List<String>) results.values;
				notifyDataSetChanged();
			}
			
		};
		return filter;
	}

	public class ViewHolder
	{
		public CheckBox ucCheckBox;
	}
}
