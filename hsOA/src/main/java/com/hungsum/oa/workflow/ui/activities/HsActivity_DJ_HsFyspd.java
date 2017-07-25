package com.hungsum.oa.workflow.ui.activities;

import java.io.Serializable;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.controls.UcDateBox;
import com.hungsum.framework.ui.controls.UcFormItem;
import com.hungsum.framework.ui.controls.UcNumericInput;
import com.hungsum.framework.ui.controls.UcTextBox;
import com.hungsum.framework.ui.fragments.HsFragment_DJ_Image;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;
import com.hungsum.oa.others.HSOADjlx;
import com.hungsum.oa.webservices.HSOAWebService;

public class HsActivity_DJ_HsFyspd extends HsActivity_DJ
{
	private UcDateBox ucDjrq;

	private UcTextBox ucMessage;

	private UcNumericInput ucJe;

	private UcTextBox ucBz;

	//{{通过bundle传入的数据

	//}}

	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.setTitleSaved(HSOADjlx.HS费用审批单);

		DJParams params = new DJParams(false, true, false);
		params.setImagePageAllowEmpty(true);

		this.setDJParams(params);

		this.mAnnexClass = HSOADjlx.HSFYSPD;
		
		
		this.ucDjrq = new UcDateBox(this);
		this.ucDjrq.setCName("单据日期");
		this.ucDjrq.setAllowEmpty(false);
		this.controls.add(this.ucDjrq);

		this.ucMessage = new UcTextBox(this);
		this.ucMessage.setCName("事由");
		this.ucMessage.setAllowEmpty(false);
		this.controls.add(this.ucMessage);

		this.ucJe = new UcNumericInput(this);
		this.ucJe.setCName("金额");
		this.ucJe.setAllowEmpty(false);
		this.controls.add(this.ucJe);

		this.ucBz = new UcTextBox(this);
		this.ucBz.setCName("备注");
		this.ucBz.setAllowEdit(true);
		this.controls.add(this.ucBz);
	}
	
	@Override
	protected void initInComingVariable(Bundle bundle) throws Exception
	{
		// TODO Auto-generated method stub
		super.initInComingVariable(bundle);
	}

	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new MainFragment();
	}
	
	@Override
	protected HsFragment_DJ_Image createDJImageFragment(String imageClass)
	{
		ImageFragment f = new ImageFragment();
		f.setAnnexClass(imageClass);
		return f;

	}
	
	protected void newData() 
	{
		super.newData();
		
		this.ucDjrq.SetFlag("Now");

	};
	
	@Override
	protected void setData(IHsLabelValue data)
	{
		this.setDJId(data.getValue("DjId","-1").toString());
		this.ucDjrq.setControlValue(data.getValue("Djrq","1900-01-01").toString());
		this.ucMessage.setControlValue(data.getValue("Message","").toString());
		this.ucJe.setControlValue(data.getValue("Je","").toString());
		this.ucBz.setControlValue(data.getValue("Bz","").toString());
	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		return  ((HSOAWebService)application.getWebService()).updateHsFyspd(
				application.getLoginData().getProgressId(),
				this.getDJId(),
				this.ucDjrq.getControlValue().toString(),
				this.ucMessage.getControlValue().toString(),
				this.ucJe.getControlValue().toString(),
				this.ucBz.getControlValue().toString(),
				this.mIsNewRecorder);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("UpdateHsFyspd"))
		{
			this.setDJId(data.toString());
			
			this.updateAnnex();

		}else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}


	public static class MainFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
			HsActivity_DJ_HsFyspd activity = (HsActivity_DJ_HsFyspd) getActivity();


			UcFormItem f_djrq = new UcFormItem(context,activity.ucDjrq);
			
			UcFormItem f_message = new UcFormItem(context,activity.ucMessage);
			
			UcFormItem f_je = new UcFormItem(context, activity.ucJe);
			
			UcFormItem f_bz = new UcFormItem(context,activity.ucBz);

			mainView.addView(f_djrq.GetView());
			mainView.addView(f_message.GetView());
			mainView.addView(f_je.GetView());
			mainView.addView(f_bz.GetView());
		}
		
	}

	public static class ImageFragment extends HsFragment_DJ_Image
	{

		public ImageFragment()
		{
			super();

			this.Title = "单据影印件";
		}
		
	}
}
