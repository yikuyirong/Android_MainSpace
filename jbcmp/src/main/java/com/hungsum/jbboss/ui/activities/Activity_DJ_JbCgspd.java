package com.hungsum.jbboss.ui.activities;

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.controls.UcDateBox;
import com.hungsum.framework.ui.controls.UcFormItem;
import com.hungsum.framework.ui.controls.UcMultiRadio;
import com.hungsum.framework.ui.controls.UcTextBox;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Detail;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;
import com.hungsum.jbboss.others.JbcmpDjlx;
import com.hungsum.jbboss.webservices.JbcmpWebService;

public class Activity_DJ_JbCgspd extends HsActivity_DJ
{
	private UcDateBox ucDjrq;

	private UcTextBox ucCgbt;

	private UcMultiRadio ucSfjj;

	private UcTextBox ucCgyy;

	/**
	 * 备注
	 */
	private UcTextBox ucBz;

	// {{通过bundle传入的数据

	// }}

	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.setTitleSaved(JbcmpDjlx.JB采购审批单);

		DJParams params = new DJParams(true, true, true);
		params.setImagePageAllowEmpty(true);

		this.setDJParams(params);

		this.mAnnexClass = JbcmpDjlx.JBCGSPD;

		this.ucDjrq = new UcDateBox(this);
		this.ucDjrq.setCName("单据日期");
		this.ucDjrq.setAllowEmpty(false);
		this.controls.add(this.ucDjrq);

		this.ucCgbt = new UcTextBox(this);
		this.ucCgbt.setCName("组织单位");
		this.ucCgbt.setAllowEmpty(false);
		this.controls.add(this.ucCgbt);

		this.ucSfjj = new UcMultiRadio(this);
		this.ucSfjj.SetDatas("0,否;1,是", "0");
		this.ucSfjj.setCName("是否加急");
		this.ucSfjj.setAllowEmpty(false);
		this.controls.add(this.ucSfjj);

		this.ucCgyy = new UcTextBox(this);
		this.ucCgyy.setCName("采购原因");
		this.ucCgyy.setAllowEmpty(true);
		this.controls.add(this.ucCgyy);

		this.ucBz = new UcTextBox(this);
		this.ucBz.setCName("备注");
		this.ucBz.setAllowEmpty(true);
		this.controls.add(this.ucBz);

	}

	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new MainFragment();
	}
	
	@Override
	protected HsFragment_ZD_Detail createDJDetailFragment()
	{
		
		return new DetailFragment();
	}

	protected void newData()
	{
		super.newData();

		this.ucDjrq.SetFlag("Now");
	};

	@Override
	protected void setData(IHsLabelValue data) throws Exception
	{
		this.setDJId(data.getValue("DjId", "-1").toString());
		this.ucDjrq.setControlValue(data.getValue("Djrq", "").toString());
		this.ucCgbt.setControlValue(data.getValue("Cgbt", "").toString());
		this.ucSfjj.setControlValue(data.getValue("Sfjj", "0").toString());
		this.ucCgyy.setControlValue(data.getValue("Cgyy", "").toString());
		this.ucBz.setControlValue(data.getValue("Bz", "").toString());

		//表体数据
		this.mDetailFragemnt.setControlValue(data.getValue("StrMx", "[]").toString(), "Mc","Sl");
	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		return ((JbcmpWebService) application.getWebService()).updateJbCgspd(
				application.getLoginData().getProgressId(), 
				this.getDJId(),
				this.ucDjrq.getControlValue().toString(), 
				this.ucCgbt.getControlValue().toString(), 
				this.ucSfjj.getControlValue().toString(),
				this.ucCgyy.getControlValue().toString(),
				this.ucBz.getControlValue().toString(),
				this.mDetailFragemnt.getControlValue().toString(),
				this.mIsNewRecorder);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("UpdateJbCgspd"))
		{
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

			UcFormItem f_hyrq = new UcFormItem(context, ucDjrq);

			UcFormItem f_zzdw = new UcFormItem(context, ucCgbt);

			UcFormItem f_hynr = new UcFormItem(context, ucSfjj);

			UcFormItem f_hysj = new UcFormItem(context, ucCgyy);

			UcFormItem f_bz = new UcFormItem(context, ucBz);

			mainView.addView(f_hyrq.GetView());
			mainView.addView(f_zzdw.GetView());
			mainView.addView(f_hynr.GetView());
			mainView.addView(f_hysj.GetView());
			mainView.addView(f_bz.GetView());
		}

	}

	private class DetailFragment extends HsFragment_ZD_Detail
	{

		public DetailFragment()
		{
			super();
			
			this.Title = "采购明细";
		}

		@Override
		protected void openDetailIntent(IHsLabelValue item)
		{
			Intent intent = new Intent(Activity_DJ_JbCgspd.this, Activity_DJ_JbCgspdMx.class);
			
			if(item != null)
			{
				intent.putExtra("Data",item);
			}
			
			if(!getAllowEdit())
			{
				intent.putExtra("AuditOnly", true);
			}

			startActivityForResult(intent, 0);

		}

		
		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				IHsLabelValue item = (IHsLabelValue) data.getSerializableExtra("Data");
				
				addItem(item);
				
			}else {
				super.onActivityResult(requestCode, resultCode, data);
			}
		}
	}
	
}
