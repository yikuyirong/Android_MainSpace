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
import com.hungsum.framework.ui.controls.UcChooseSingleItem;
import com.hungsum.framework.ui.controls.UcDateBox;
import com.hungsum.framework.ui.controls.UcFormItem;
import com.hungsum.framework.ui.controls.UcTextBox;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Detail;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;
import com.hungsum.jbboss.others.JbbossDjlx;
import com.hungsum.jbboss.webservices.JbbossWebService;

public class Activity_DJ_JbSnydndj extends HsActivity_DJ
{

    private UcChooseSingleItem ucKh;

    private UcDateBox ucKsrq;

	private UcDateBox ucJsrq;

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

		this.setTitleSaved(JbbossDjlx.JB订奶单据);

		DJParams params = new DJParams(true, true, true);
		params.setImagePageAllowEmpty(true);

		this.setDJParams(params);

		this.mAnnexClass = JbbossDjlx.JBDNDJ;

        this.ucKh = new UcChooseSingleItem(this);
        this.ucKh.SetFlag("JBBOSS_KH");
        this.ucKh.SetParams(application.getLoginData().getUserbh());
        this.ucKh.setCName("客户信息");
        this.ucKh.setAllowEmpty(false);
        this.controls.add(this.ucKh);

        this.ucKsrq = new UcDateBox(this);
		this.ucKsrq.setCName("开始日期");
		this.ucKsrq.setAllowEmpty(false);
		this.controls.add(this.ucKsrq);

        this.ucJsrq = new UcDateBox(this);
        this.ucJsrq.setCName("开始日期");
        this.ucJsrq.setAllowEmpty(false);
        this.controls.add(this.ucJsrq);

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

		this.ucKsrq.SetFlag("Now");
	};

	@Override
	protected void setData(IHsLabelValue data) throws Exception
	{
		this.setDJId(data.getValue("DjId", "-1").toString());
        this.ucKh.setControlValue(data.getValue("Khbh", "").toString() + "," + data.getValue("Khmc","").toString());
		this.ucKsrq.setControlValue(data.getValue("Ksrq", "").toString());
		this.ucKsrq.setControlValue(data.getValue("Jsrq", "").toString());
		this.ucBz.setControlValue(data.getValue("Bz", "").toString());

		//表体数据
		this.mDetailFragemnt.setControlValue(data.getValue("StrMx", "[]").toString(), "Cpmc","Sl");
	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		return ((JbbossWebService) application.getWebService()).updateJbSnydndjs(
				application.getLoginData().getProgressId(),
				this.getDJId(),
                this.ucKh.getControlValue().toString(),
				this.ucKsrq.getControlValue().toString(),
				this.ucJsrq.getControlValue().toString(),
				this.ucBz.getControlValue().toString(),
				this.mDetailFragemnt.getControlValue().toString(),
				this.mIsNewRecorder);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("UpdateJbSnydndjs"))
		{
			this.setDJId(data.toString());

			this.updateAnnex();

		} else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}

	public static class MainFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
            Activity_DJ_JbSnydndj activity = (Activity_DJ_JbSnydndj)getActivity();

            UcFormItem f_kh = new UcFormItem(context,activity.ucBz);

            UcFormItem f_ksrq = new UcFormItem(context, activity.ucKsrq);

			UcFormItem f_jsrq = new UcFormItem(context, activity.ucJsrq);

			UcFormItem f_bz = new UcFormItem(context, activity.ucBz);

			mainView.addView(f_ksrq.GetView());
			mainView.addView(f_kh.GetView());
            mainView.addView(f_ksrq.GetView());
            mainView.addView(f_jsrq.GetView());
			mainView.addView(f_bz.GetView());
		}

	}

	public static class DetailFragment extends HsFragment_ZD_Detail
	{

		public DetailFragment()
		{
			super();
			
			this.Title = "订单明细";
		}

		@Override
		protected void openDetailIntent(IHsLabelValue item)
		{
			Intent intent = new Intent(getActivity(), Activity_DJ_JbSnydndjmx.class);
			
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
