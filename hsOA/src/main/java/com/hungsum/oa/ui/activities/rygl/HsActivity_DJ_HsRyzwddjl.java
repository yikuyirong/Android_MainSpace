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
import com.hungsum.framework.ui.controls.UcTextBox;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;
import com.hungsum.oa.others.HSOADjlx;
import com.hungsum.oa.webservices.HSOAWebService;

public class HsActivity_DJ_HsRyzwddjl extends HsActivity_DJ
{
	private UcChooseSingleItem ucRy;

	private UcDateBox ucJlrq;

	private UcTextBox ucDdyy;

	private UcChooseSingleItem ucXzw;

	private UcTextBox ucBz;

	public HsActivity_DJ_HsRyzwddjl()
	{
		super();
		
		this.inDJParams = new DJParams(false, true, false);
		
		this.mAnnexClass = HSOADjlx.HSRYZWDDJL;
	}

	
	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.setTitleSaved(HSOADjlx.HS人员职务调动记录);

		this.ucRy = new UcChooseSingleItem(this);
		this.ucRy.SetFlag(HSOADjlx.HSRYDA);
		this.ucRy.setCName("人员");
		this.ucRy.setAllowEmpty(false);
		this.controls.add(this.ucRy);

		this.ucXzw = new UcChooseSingleItem(this);
		this.ucXzw.SetFlag(HSOADjlx.HSRYZW);
		this.ucXzw.setCName("新职务");
		this.ucXzw.setAllowEmpty(false);
		this.controls.add(this.ucXzw);
		
		this.ucDdyy = new UcTextBox(this);
		this.ucDdyy.setCName("调动原因");
		this.ucDdyy.setAllowEmpty(false);
		this.controls.add(this.ucDdyy);

		this.ucJlrq = new UcDateBox(this);
		this.ucJlrq.setCName("记录日期");
		this.ucJlrq.setAllowEmpty(false);
		this.controls.add(this.ucJlrq);

		this.ucBz = new UcTextBox(this);
		this.ucBz.setCName("备注");
		this.ucBz.setAllowEmpty(true);
		this.controls.add(this.ucBz);
		
	}

	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new HsRyzwddjlFragment();
	}
	
	@Override
	protected void setData(IHsLabelValue data)
	{
		// 表头数据

		this.setDJId(data.getValue("JlId", "-1").toString());

		this.ucRy.setControlValue(data.getValue("RyId", "") + "," + data.getValue("Ryxm",""));

		this.ucDdyy.setControlValue(data.getValue("Ddyy", "").toString());

		this.ucJlrq.setControlValue(data.getValue("Jlrq", "").toString());

		this.ucXzw.setControlValue(data.getValue("Xzw", "").toString());

		this.ucBz.setControlValue(data.getValue("Bz", "").toString());

	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		return ((HSOAWebService) application.getWebService()).updateHsRyzwddjl(
							application.getLoginData().getProgressId(),
							this.getDJId(),
							this.ucRy.getControlValue().toString(),
							this.ucJlrq.getControlValue().toString(),
							this.ucDdyy.getControlValue().toString(),
							this.ucXzw.getControlValue().toString(),
							this.ucBz.getControlValue().toString(),
							this.mIsNewRecorder);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("UpdateHsRyzwddjl"))
		{
			// 在此处需要调用updateAnnex方法更新图像、附件即可，对于新建单据更新图像前一定要赋值mDJId。
			this.setDJId(data.toString());
			
			this.updateAnnex();
		}else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}


	private class HsRyzwddjlFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
			UcFormItem f_ry = new UcFormItem(context, ucRy);

			UcFormItem f_ddyy = new UcFormItem(context, ucDdyy);
			
			UcFormItem f_jlrq = new UcFormItem(context, ucJlrq);

			UcFormItem f_xzw = new UcFormItem(context, ucXzw);

			UcFormItem f_bz = new UcFormItem(context, ucBz);

			mainView.addView(f_jlrq.GetView());
			mainView.addView(f_ry.GetView());
			mainView.addView(f_xzw.GetView());
			mainView.addView(f_ddyy.GetView());
			mainView.addView(f_bz.GetView());
		}
		
	}
	
}
