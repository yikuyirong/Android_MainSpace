package com.hungsum.jbboss.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.hungsum.framework.componments.HsLabelValue;
import com.hungsum.jbboss.componments.JbSnyLoginData;
import com.hungsum.jbboss.others.JbbossFuncKey;
import com.hungsum.oa.ui.activities.HsActivity_HSOA_Main;

public class Activity_Jbboss_Main extends HsActivity_HSOA_Main
{

	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		JbSnyLoginData loginData = (JbSnyLoginData) application.getLoginData();

		if(mUserInformationAdapter != null)
		{
			mUserInformationAdapter.add(new HsLabelValue("[人员信息]" + loginData.getUsername() + " " + loginData.getUserbh(),loginData.getNzBh()));
			mUserInformationAdapter.add(new HsLabelValue("[奶站信息]" + loginData.getNzmc() + " " + loginData.getNzBh(),loginData.getNzBh()));
			mUserInformationAdapter.add(new HsLabelValue("[发奶点信息]" + loginData.getFndmc() + " " + loginData.getFndBh(),loginData.getFndBh()));
		}

	}


	@Override
	protected void doFunc(String funcKey) throws Exception
	{
		if(funcKey.equals(JbbossFuncKey.JB订奶单据录入)) //信息记录维护
		{
			Intent intent = new Intent();
			intent.setClass(this, Activity_List_JbKhzd.class);
			intent.putExtra("Title","订奶单据录入");
			startActivity(intent);
		}else if(funcKey.equals(JbbossFuncKey.JB客户信息维护))
		{
			Intent intent = new Intent();
			intent.setClass(this, Activity_List_JbKhzd.class);
			intent.putExtra("Title","客户信息维护");
			startActivity(intent);
		}else {
			super.doFunc(funcKey);
		}
	}
}
