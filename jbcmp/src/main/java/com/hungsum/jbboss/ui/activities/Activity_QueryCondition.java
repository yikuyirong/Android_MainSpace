package com.hungsum.jbboss.ui.activities;

import android.content.Intent;

import com.hungsum.framework.ui.activities.HsActivity_QueryCondition;
import com.hungsum.jbboss.R;
import com.hungsum.jbboss.charts.BmxsthtjbChart;
import com.hungsum.jbboss.charts.DwsnltjbChart;

public class Activity_QueryCondition extends HsActivity_QueryCondition
{
	@Override
	protected Intent getUserChartIntent(String key, String zippedItems)
	{
		Intent intent;
		if(key.equals(getString(R.string.key_bmxsthtjb_week)))
		{
			intent = new BmxsthtjbChart(zippedItems).execute(this);
		}else if(key.equals(getString(R.string.key_dwsnltjb)))
		{
			intent = new DwsnltjbChart(zippedItems).execute(this);
		}else 
		{
			intent = super.getUserChartIntent(key, zippedItems);
		}
		return intent;
	}

}
