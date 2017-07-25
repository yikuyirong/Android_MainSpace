package com.hungsum.framework.ui.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.hungsum.framework.R;
import com.hungsum.framework.adapter.HsUserLabelValueAdapter;
import com.hungsum.framework.componments.HsLabelValue;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;

public class HsActivity_QueryResult extends HsActivity_HsLabelValueList
{
	
	//{{
	
	private String inTitle;
	
	//}}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0, R.string.str_share, 0, getString(R.string.str_share))
		.setIcon(android.R.drawable.ic_menu_share)
		.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId() == R.string.str_share)
		{
			shareMessage(((HsUserLabelValueAdapter)this.ucListView.getAdapter()).getShareMessage());

		    return true;
		}else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void initInComingVariable(Bundle bundle) throws Exception
	{
		super.initInComingVariable(bundle);
		
		if(bundle.containsKey("Title"))
		{
			this.inTitle = bundle.getString("Title");
		}
	}

	@Override
	protected HsWSReturnObject retrieve() throws Exception
	{
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		HsLabelValue item = (HsLabelValue) this.ucListView.getAdapter().getItem(arg2);
		if(item != null && item.getDetailsCount() > 0)
		{
			Intent intent = new Intent(this,HsActivity_QueryResult.class);
			intent.putExtra("Title",this.getTitle());
			intent.putExtra("Data", (ArrayList<IHsLabelValue>)item.getDetails());
		
			startActivity(intent);
		}
	}


	@Override
	public void onSliderButtonClick(int buttonId, IHsLabelValue object)
	{
	}
}
