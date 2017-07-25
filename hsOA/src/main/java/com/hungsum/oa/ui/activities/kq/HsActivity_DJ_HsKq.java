package com.hungsum.oa.ui.activities.kq;

import java.io.Serializable;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.controls.UcFormItem;
import com.hungsum.framework.ui.controls.UcMultiRadio;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;
import com.hungsum.framework.utils.HsLocationUtil.ENeedLocationInformation;
import com.hungsum.oa.others.HSOADjlx;
import com.hungsum.oa.webservices.HSOAWebService;

public class HsActivity_DJ_HsKq extends HsActivity_DJ
{

	private UcMultiRadio ucJlzt;

	// {{通过bundle传入的数据

	// }}

	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.setTitleSaved(HSOADjlx.HL考勤记录);

		DJParams params = new DJParams(false, false, false,
				ENeedLocationInformation.Required);
		params.setImagePageAllowEmpty(true);

		this.setDJParams(params);

		this.mAnnexClass = HSOADjlx.HLKQJLMX;

		this.ucJlzt = new UcMultiRadio(this);
		this.ucJlzt.SetDatas("1,上班签到;2,下班签退","1");
		this.ucJlzt.setCName("考勤内容");
		this.ucJlzt.setAllowEmpty(false);
		this.controls.add(this.ucJlzt);

	}

	@Override
	protected void setData(IHsLabelValue Object) throws Exception { }
	
	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new MainFragment();
	}


	@Override
	protected void callUpdateOnOtherThread()
	{
		setIsModified(true);
		super.callUpdateOnOtherThread();
	}
	
	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		return ((HSOAWebService) application.getWebService()).signHsKq(
				application.getLoginData().getProgressId(),this.ucJlzt.getControlValue().toString());
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("SignHsKq"))
		{
			/*
			//启动或关闭服务
			Intent intent = new Intent();
			intent.setAction(HSOAActionAttr.ACTION_LOCATION);
			intent.addCategory(getPackageName());
			intent.putExtra("UserId", Integer.parseInt(application.getLoginData().getUserbh()));
			
			if(this.ucJlzt.getControlValue().equals("1")) //上班签到
			{
				startService(intent);
				
			}else {
				
				intent.putExtra("AllowStopService", true);
				
				startService(intent);
				
				stopService(intent);
			}
			*/

			this.setDJId(data.toString());

			this.updateAnnex();
			
		} else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}

	private class MainFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{

			UcFormItem f_jlzt = new UcFormItem(context, ucJlzt);

			mainView.addView(f_jlzt.GetView());
		}

	}


}
