package com.hungsum.oa.ui.activities.rygl;

import java.io.Serializable;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.controls.UcDateBox;
import com.hungsum.framework.ui.controls.UcFormItem;
import com.hungsum.framework.ui.controls.UcTextBox;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;
import com.hungsum.oa.others.HSOADjlx;
import com.hungsum.oa.webservices.HSOAWebService;

public class HsActivity_DJ_HsRysyjl extends HsActivity_DJ
{
	private IHsLabelValue mRy;

	private UcDateBox ucJlrq;

	private UcTextBox ucJlzy;

	public HsActivity_DJ_HsRysyjl()
	{
		super();
		
		this.inDJParams = new DJParams(false, true, false);
		
		this.mAnnexClass = HSOADjlx.HSRYSYJL;
	}

	
	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.setTitleSaved(HSOADjlx.HS人员生涯记录);

		this.ucJlrq = new UcDateBox(this);
		this.ucJlrq.setCName("记录日期");
		this.ucJlrq.setAllowEmpty(false);
		this.controls.add(this.ucJlrq);

		this.ucJlzy = new UcTextBox(this);
		this.ucJlzy.setCName("记录摘要");
		this.ucJlzy.setAllowEmpty(false);
		this.controls.add(this.ucJlzy);

	}

	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new HsRysyjlFragment();
	}
	
	@Override
	protected void initInComingVariable(Bundle bundle) throws Exception
	{
		super.initInComingVariable(bundle);
		
		this.mRy = (IHsLabelValue) bundle.getSerializable("Ry");
	}
	
	@Override
	protected void setData(IHsLabelValue data)
	{
		// 表头数据

		this.setDJId(data.getValue("JlId", "-1").toString());

		this.ucJlrq.setControlValue(data.getValue("Jlrq", "").toString());

		this.ucJlzy.setControlValue(data.getValue("Jlzy", "").toString());

	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		return ((HSOAWebService) application.getWebService()).updateHsRysyjl(
							application.getLoginData().getProgressId(),
							this.getDJId(),
							this.mRy.getValue("RyId", "-1").toString(),
							this.ucJlrq.getControlValue().toString(),
							this.ucJlzy.getControlValue().toString(),
							this.mIsNewRecorder);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("UpdateHsRysyjl"))
		{
			// 在此处需要调用updateAnnex方法更新图像、附件即可，对于新建单据更新图像前一定要赋值mDJId。
			this.setDJId(data.toString());
			
			this.updateAnnex();
		}else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}


	private class HsRysyjlFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
			UcFormItem f_jlrq = new UcFormItem(context, ucJlrq);

			UcFormItem f_jlzy = new UcFormItem(context, ucJlzy);

			mainView.addView(f_jlrq.GetView());
			mainView.addView(f_jlzy.GetView());
		}
		
	}
	
}
