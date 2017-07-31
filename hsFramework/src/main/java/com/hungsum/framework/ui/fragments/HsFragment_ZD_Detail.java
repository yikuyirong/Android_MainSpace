package com.hungsum.framework.ui.fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hungsum.framework.R;
import com.hungsum.framework.adapter.HsUserLabelValueAdapter;
import com.hungsum.framework.componments.HsLabelValue;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.controls.UcListView;

public abstract class HsFragment_ZD_Detail extends HsFragment_ZD_Annex<IHsLabelValue>
{
	//protected HsZDDetailAdapter mAdapter;
	
	protected TextView mSummary;
	
	protected UcListView ucListView;

	//{{ 构造方法
	
	public HsFragment_ZD_Detail()
	{
		super();

		Icon = R.drawable.collections_view_as_list;
		
		Title = "明细信息";
	}
	
	//}}

	//{{ 初始化
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.hsactivity_list, null,false);
		
		this.ucListView = (UcListView) rootView.findViewById(R.id.ucListView);
		this.ucListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
		this.ucListView.setAdapter(getAdapter());

		this.registerForContextMenu(this.ucListView);

		this.mSummary = (TextView) rootView.findViewById(R.id.ucSummary);
		
		this.ucListView.setOnItemClickListener(this);

		mHasCreateCompleted = true;
		
		if(mListenerOfCreateCompletedEventListener != null)
		{
			mListenerOfCreateCompletedEventListener.action(savedInstanceState);
			
			removeOnCreateCompletedEventListener();
		}

		return rootView;
	}
	
	//}}

	//{{ 适配器
	
	@Override
	protected BaseAdapter createAdapter(List<IHsLabelValue> datas)
	{
		return new HsZDDetailAdapter(getActivity());
	}
	
	@Override
	protected BaseAdapter getAdapter()
	{
		if(this.ucListView != null)
		{
			if (this.ucListView.getAdapter() == null)
			{
				this.ucListView
						.setAdapter(createAdapter(new ArrayList<IHsLabelValue>()));
			}
			return (HsZDDetailAdapter) this.ucListView.getAdapter();
		}else
		{
			return null;
		}
	}

	//}}
	
	//{{ 菜单

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);

		if(this.getAllowEdit())
		{
			//新增
			menu.add(1, R.string.str_new, 0,
					getString(R.string.str_new))
			.setIcon(R.drawable.content_new)
			.setShowAsActionFlags(
					MenuItem.SHOW_AS_ACTION_ALWAYS
							| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		}
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		try
		{
			if (item.getItemId() == R.string.str_new) // 新建
			{
				this.openDetailIntent(null);
				return true;
			}else
			{
				return super.onOptionsItemSelected(item);
			}
		} catch (Exception e)
		{
			showError(e);
		}
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		
		if(this.getAllowEdit())
		{
			menu.addSubMenu(1, R.string.str_modify, 0, "修改").setIcon(
					R.drawable.content_edit).getItem().setVisible(getAllowEdit());
			menu.addSubMenu(1, R.string.str_delete, 1, "删除").setIcon(
					R.drawable.content_remove).getItem().setVisible(getAllowEdit());
		}else {
			menu.addSubMenu(1, R.string.str_modify, 0, "查看").setIcon(
					R.drawable.content_edit).getItem().setVisible(getAllowEdit());
		}

	}
	
	/*
	 * 上下文菜单回调函数
	 */
	@Override
	public boolean onContextItemSelected(final MenuItem item)
	{
		if(this.getUserVisibleHint())
		{
			try
			{
				if(item.getItemId() == R.string.str_modify)
				{
					openDetailIntent(mCurrentSelectedItem);
					
				}else if(item.getItemId() == R.string.str_delete)
				{
					if(mCurrentSelectedIndex != -1)
					{
						removeItem(mCurrentSelectedIndex);
					}
				}

				return true;
			} catch (Exception e)
			{
				this.showError(e);
			}
			return true;
		}else {
			return false;
		}
		
	}
	
	//}}

	//{{ 添加、删除操作
	
	@Override
	protected void addItem(IHsLabelValue item)
	{
		super.addItem(item);

		((HsZDDetailAdapter)getAdapter()).add(item);

		mItems.add(item);

		this.setSummaryText();
	}
	
	@Override
	protected void addItems(List<IHsLabelValue> items)
	{
		super.addItems(items);

		((HsZDDetailAdapter)getAdapter()).add(items);

		mItems = items;

		this.setSummaryText();
	}
	
	@Override
	protected void removeItem(int position)
	{
		super.removeItem(position);

		((HsZDDetailAdapter)getAdapter()).remove(position);

		mItems.remove(position);

		this.setSummaryText();

	}
	
	@Override
	protected IHsLabelValue getItem(int position)
	{
		return (IHsLabelValue) this.getAdapter().getItem(position);
	}

	//}}

	/**
	 * 设置汇总行信息
	 */
	@Override
	public void setSummaryText()
	{
		if(mSummary != null)
		{
			mSummary.setText("共有" + this.getAdapter().getCount() + "条记录。");
		}
	}

	/**
	 * 打开明细
	 */
	protected abstract void openDetailIntent(IHsLabelValue item);

	//{{ 重写与实现父类接口

	@Override
	protected void callRetrieveOnOtherThread()
	{
		this.addItems(mItems);
	}
	
	@Override
	protected HsWSReturnObject retrieve() throws Exception
	{
		return null;
	}

	@Override
	public void updateAnnex() { }
	
	//{{ 重写OnItemClickListener的实现方法
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		super.onItemClick(arg0, arg1, arg2, arg3);
		
		if(mCurrentSelectedItem != null)
		{
			this.openDetailIntent(mCurrentSelectedItem);
		}
	}
	
	//}}

	//{{ 重写IControlValue的实现方法
	
	@Override
	public CharSequence getControlValue() throws JSONException
	{
		JSONArray jsonArray = new JSONArray();
		
		JSONObject jsonObject = null;
		
		for (int i = 0; i < this.getAnnexCount(); i++)
		{
			IHsLabelValue item = getItem(i);

			jsonObject = new JSONObject();

			jsonObject.put("Label",item.getLabel());
			jsonObject.put("Value",item.getValue());

			for (IHsLabelValue detail : item.getDetails())
			{
				jsonObject.put(detail.getLabel(),detail.getValue());
			}
			
			jsonArray.put(jsonObject);
			
		}
		
		return jsonArray.toString();
	}
	
	public CharSequence getControlValueXML() throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Items>");
		for (int i = 0; i < this.getAnnexCount(); i++)
		{
			sb.append("<Item>");
			
			IHsLabelValue item = (IHsLabelValue) this.getItem(i);

			for (IHsLabelValue detail : item.getDetails())
			{
				sb.append("<");
				sb.append(detail.getLabel());
				sb.append(">");

				sb.append(detail.getValue());
				
				sb.append("</");
				sb.append(detail.getLabel());
				sb.append(">");
			}
			
			sb.append("</Item>");
			
		}
		
		sb.append("</Items>");

		return sb.toString();
	}

	public void setControlValue(final CharSequence data,final String label,final String value) throws JSONException
	{
		if(mHasCreateCompleted)
		{
			setControlValueDelay(data,label,value);
		}else {
			mListenerOfCreateCompletedEventListener = new CreateCompletedEventListener()
			{
				@Override
				public void action(Bundle savedInstanceState)
				{
					try
					{
						setControlValueDelay(data,label,value);
					} catch (Exception e)
					{
						showError(e);
					}
				}
			};
		}
	}
	
	@Override
	public void setControlValue(CharSequence value) throws Exception
	{
		this.setControlValue(value, "Label", "Value");
	}

	private void setControlValueDelay(CharSequence data,String labelmc,String valuemc) throws JSONException
	{
		JSONTokener tokener = new JSONTokener(data.toString());
		
		JSONArray jsonArray = (JSONArray) tokener.nextValue();
		
		List<IHsLabelValue> items = new ArrayList<IHsLabelValue>();
		
		for (int i = 0; i < jsonArray.length() ; i++)
		{
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			String label = jsonObject.getString(labelmc);
			String value = jsonObject.getString(valuemc);
			
			HsLabelValue item = new HsLabelValue(label,value);
			item.SetIsShowDetailImage(false);
			
			Iterator<String> iterator = jsonObject.keys();

			while (iterator.hasNext())
			{
				label = (String)iterator.next();
				value = jsonObject.getString(label);
				item.addDetail(new HsLabelValue(label,value));
			}
			
			items.add(item);
		}
		
		this.addItems(items);
		
		mItems = items;
		
		mHasRetrieved = true;
	}

	//}}
	
	//}}
	
	//{{ 内部类
	
	public class HsZDDetailAdapter extends HsUserLabelValueAdapter
	{
		public HsZDDetailAdapter(Context context)
		{
			super(context,R.layout.control_list_keyvalue_2);
		}

		/**
		 * 添加记录，如果存在相同Key的记录先删除。
		 */
		@Override
		public void add(IHsLabelValue value)
		{
			int position = this.getPostionByLabel(value.getLabel());
			
			if(position != -1)
			{
				super.remove(position);
			}
			
			super.add(value);
		}
	}

	//}}

}
