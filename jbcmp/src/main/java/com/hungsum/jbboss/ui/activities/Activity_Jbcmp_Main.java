package com.hungsum.jbboss.ui.activities;

import android.content.Intent;

import com.hungsum.jbboss.others.JbcmpFuncKey;
import com.hungsum.oa.ui.activities.HsActivity_HSOA_Main;

public class Activity_Jbcmp_Main extends HsActivity_HSOA_Main
{
	@Override
	protected void doFunc(String funcKey) throws Exception
	{
		if(funcKey.equals("XXJLWH")) //信息记录维护
		{
			Intent intent = new Intent();
			intent.setClass(this, Activity_List_XXJL.class);
			intent.putExtra("Title","信息记录维护");
			startActivity(intent);
		}else if(funcKey.equals(JbcmpFuncKey.JB采购审批单浏览))
		{
			Intent intent = new Intent();
			intent.setClass(this, Activity_List_JbCgspd.class);
			intent.putExtra("Title","采购审批单浏览");
			startActivity(intent);
		}else {
			super.doFunc(funcKey);
		}
	}
}
