package com.hungsum.oa.ui.activities.rygl;

import java.io.Serializable;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.controls.UcChooseSingleItem;
import com.hungsum.framework.ui.controls.UcDateBox;
import com.hungsum.framework.ui.controls.UcFormItem;
import com.hungsum.framework.ui.controls.UcMultiRadio;
import com.hungsum.framework.ui.controls.UcNumericInput;
import com.hungsum.framework.ui.controls.UcTextBox;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;
import com.hungsum.oa.others.HSOADjlx;
import com.hungsum.oa.webservices.HSOAWebService;

public class HsActivity_DJ_HsRyjcjl extends HsActivity_DJ
{
	private UcChooseSingleItem ucRy;

	private UcDateBox ucJlrq;

	private UcMultiRadio ucJclx;
	
	private UcTextBox ucJcyy;

	private UcNumericInput ucJe;

	private UcTextBox ucBz;

	public HsActivity_DJ_HsRyjcjl()
	{
		super();
		
		this.inDJParams = new DJParams(false, true, false);
		
		this.mAnnexClass = HSOADjlx.HSRYJCJL;
	}

	
	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.setTitleSaved(HSOADjlx.HS人员奖惩记录);

		this.ucJlrq = new UcDateBox(this);
		this.ucJlrq.setCName("记录日期");
		this.ucJlrq.setAllowEmpty(false);
		this.controls.add(this.ucJlrq);

		this.ucRy = new UcChooseSingleItem(this);
		this.ucRy.SetFlag(HSOADjlx.HSRYDA);
		this.ucRy.setCName("人员");
		this.ucRy.setAllowEmpty(false);
		this.controls.add(this.ucRy);

		this.ucJclx = new UcMultiRadio(this);
		this.ucJclx.SetDatas("-1,惩;0,一般记录;1,奖","0");
		this.ucJclx.setCName("奖惩类型");
		this.ucJclx.setAllowEmpty(false);
		this.controls.add(this.ucJclx);
		
		this.ucJcyy = new UcTextBox(this);
		this.ucJcyy.setCName("奖惩原因");
		this.ucJcyy.setAllowEmpty(false);
		this.controls.add(this.ucJcyy);

		this.ucJe = new UcNumericInput(this);
		this.ucJe.setCName("奖惩金额");
		this.ucJe.setAllowEmpty(true);
		this.controls.add(this.ucJe);
		
		this.ucBz = new UcTextBox(this);
		this.ucBz.setCName("备注");
		this.ucBz.setAllowEmpty(true);
		this.controls.add(this.ucBz);
		
	}

	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new HsRyjcjlFragment();
	}
	
	@Override
	protected void setData(IHsLabelValue data)
	{
		// 表头数据

		this.setDJId(data.getValue("JlId", "-1").toString());

		this.ucJlrq.setControlValue(data.getValue("Jlrq", "").toString());

		this.ucRy.setControlValue(data.getValue("RyId", "") + "," + data.getValue("Ryxm",""));

		this.ucJclx.setControlValue(data.getValue("Jclx", "").toString());
		
		this.ucJcyy.setControlValue(data.getValue("Jcyy", "").toString());

		this.ucJe.setControlValue(data.getValue("Jcje", "").toString());

		this.ucBz.setControlValue(data.getValue("Bz", "").toString());

	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		return ((HSOAWebService) application.getWebService()).updateHsRyjcjl(
							application.getLoginData().getProgressId(),
							this.getDJId(),
							this.ucRy.getControlValue().toString(),
							this.ucJlrq.getControlValue().toString(),
							this.ucJclx.getControlValue().toString(),
							this.ucJcyy.getControlValue().toString(),
							this.ucJe.getControlValue().toString(),
							this.ucBz.getControlValue().toString(),
							this.mIsNewRecorder);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("UpdateHsRyjcjl"))
		{
			// 在此处需要调用updateAnnex方法更新图像、附件即可，对于新建单据更新图像前一定要赋值mDJId。
			this.setDJId(data.toString());
			
			this.updateAnnex();
		}else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}


	private class HsRyjcjlFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
			UcFormItem f_ry = new UcFormItem(context, ucRy);

			UcFormItem f_jclx = new UcFormItem(context, ucJclx);

			UcFormItem f_jcyy = new UcFormItem(context, ucJcyy);
			
			UcFormItem f_jlrq = new UcFormItem(context, ucJlrq);

			UcFormItem f_je = new UcFormItem(context, ucJe);

			UcFormItem f_bz = new UcFormItem(context, ucBz);

			mainView.addView(f_jlrq.GetView());
			mainView.addView(f_ry.GetView());
			mainView.addView(f_jclx.GetView());
			mainView.addView(f_jcyy.GetView());
			mainView.addView(f_je.GetView());
			mainView.addView(f_bz.GetView());
		}
		
	}
	
}
