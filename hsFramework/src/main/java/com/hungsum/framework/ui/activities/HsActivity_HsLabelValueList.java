package com.hungsum.framework.ui.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.hungsum.framework.R;
import com.hungsum.framework.adapter.HsUserLabelValueAdapter;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.events.CommEventListener;
import com.hungsum.framework.events.CommEventListener.EventCategory;
import com.hungsum.framework.events.CommEventObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.models.ModelHsLabelValues;
import com.hungsum.framework.utils.HsGZip;

public abstract class HsActivity_HsLabelValueList extends HsActivity_List<IHsLabelValue> implements OnQueryTextListener
{
	private HsUserLabelValueAdapter mAdapter;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		
		SearchView sv = new SearchView(this);
		
		sv.setBackgroundColor(Color.WHITE);

		sv.setOnQueryTextListener(this);
		
		menu.add(0, R.string.str_search, 0, getString(R.string.str_search))
		.setIcon(R.drawable.action_search)
		.setActionView(sv)
		.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	protected void initInComingVariable(Bundle bundle) throws Exception
	{
		super.initInComingVariable(bundle);
		
		if(bundle.containsKey("ZippedData"))
		{
			this.items = new ModelHsLabelValues().Create(HsGZip.DecompressString(bundle.getString("ZippedData")));
		}else if(bundle.containsKey("Data"))
		{
			this.items = (List<IHsLabelValue>) bundle.getSerializable("Data");
		}
	}

	@Override
	protected BaseAdapter getAdapter(List<IHsLabelValue> items)
	{
		if(mAdapter == null)
		{
			mAdapter = new HsUserLabelValueAdapter(this, R.layout.control_list_keyvalue_2);
		}

		mAdapter.add(items);
		
		mAdapter.setOnButtonClickListener(new CommEventListener(EventCategory.None)
		{
			@Override
			public void EventHandler(CommEventObject object)
			{
				try
				{
					//按钮复位
					mAdapter.resetSliederPostion();
					
					onSliderButtonClick(Integer.valueOf(object.GetData(0).toString()),(IHsLabelValue)object.GetData(1));
				} catch (Exception e)
				{
					ShowError(e);
				}
			}
		});

		return mAdapter;
	}
	
	public abstract void onSliderButtonClick(int buttonId,IHsLabelValue object);


	/* 
	 * 此方法用于刷新获取数据，如果适配器中已经包含数据则不需要实现此方法。
	 */
	@Override
	protected HsWSReturnObject retrieve() throws Exception
	{
		return null;
	}
	
	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		try
		{
			ArrayList<IHsLabelValue> items = new ModelHsLabelValues().Create(HsGZip.DecompressString(data.toString()));
			this.ucListView.setAdapter(getAdapter(items));
		} catch (Exception e)
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}

	/***********************实现 OnQueryTextListener*******************/
	
	@Override
	public boolean onQueryTextChange(String text)
	{
		Filterable filter = (Filterable) this.ucListView.getAdapter();
		if(filter != null)
		{
			filter.getFilter().filter(text);
		}
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query)
	{
		return false;
	}

}
