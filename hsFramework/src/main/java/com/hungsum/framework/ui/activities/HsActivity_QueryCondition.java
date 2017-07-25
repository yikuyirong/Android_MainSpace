package com.hungsum.framework.ui.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;

import com.hungsum.framework.R;
import com.hungsum.framework.componments.HsQuery;
import com.hungsum.framework.componments.HsQueryArg;
import com.hungsum.framework.componments.HsQueryConditionItem;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IControlValue;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;

public class HsActivity_QueryCondition extends HsActivity_ZD
{
	protected List<HsQueryConditionItem> mConditionItems = new ArrayList<HsQueryConditionItem>();

	protected HsQuery inHsQueryCondition;

	@Override
	protected void initInComingVariable(Bundle bundle) throws Exception
	{
		super.initInComingVariable(bundle);
		
		if(bundle.containsKey("Data"))
		{
			this.inHsQueryCondition = (HsQuery) bundle.getSerializable("Data");
			
			this.inTitle = this.inHsQueryCondition.QueryTitle;
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		
		menu.findItem(R.string.str_enter).setIcon(R.drawable.action_search);

		return true;
	}
	
	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new QueryConditionFragment();
	}
	
	
	private String _getQueryArg() throws Exception
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<Args>");
		for (HsQueryConditionItem item : mConditionItems)
		{
			sb.append("<Arg>");
			sb.append("<Value>" + item.GetControl().getControlValue() + "</Value>");
			sb.append("<SqlOrder>" + item.GetSqlOrder() + "</SqlOrder>");
			sb.append("</Arg>");
		}
		sb.append("</Args>");
		
		return sb.toString();
	}

	@Override
	protected void callUpdateOnOtherThread()
	{
		this.setIsModified(true); //设置单据修改状态才能出发确认动作。
		super.callUpdateOnOtherThread();
	}
	
	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		if(inHsQueryCondition != null)
		{
			return application.getWebService().queryResult(
					application.getLoginData().getProgressId(),
					this.inHsQueryCondition.QueryName,
					_getQueryArg());
		}else {
			return null;
		}
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if(funcname.equals("QueryResult"))
		{
			Intent intent = this.getUserChartIntent(inHsQueryCondition.QueryName,data.toString());

			startActivity(intent);
		}else 
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}
	
	
	protected Intent getUserChartIntent(String key,String zippedItems)
	{
		Intent intent = new Intent(this, inHsQueryCondition.queryResultClass == null ? HsActivity_QueryResult.class : inHsQueryCondition.queryResultClass);
		intent.putExtra("Title", inHsQueryCondition.QueryTitle);
		intent.putExtra("ZippedData", zippedItems);
		return intent;
	}

	@Override
	protected void newData()
	{
	}

	@Override
	protected void setData(IHsLabelValue Object)
	{
	}

	private class QueryConditionFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
			if(inHsQueryCondition != null)
			{
				for (HsQueryArg arg : inHsQueryCondition.QueryArgs)
				{
					
					HsQueryConditionItem item = new HsQueryConditionItem(context, 
							arg.CName, 
							arg.SqlOrder, 
							arg.Class, 
							arg.ClassInfo, 
							arg.ClassParams, 
							arg.AllowEmpty.equals("1"), 
							arg.Default);
					mConditionItems.add(item);
					IControlValue control = item.GetControl();
					controls.add(control);
					mainView.addView(item.GetView());
				}
			}
		}
		
	}

}
