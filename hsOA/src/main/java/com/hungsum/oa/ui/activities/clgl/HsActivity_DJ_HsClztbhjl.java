package com.hungsum.oa.ui.activities.clgl;

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
import com.hungsum.framework.ui.controls.UcTextBox;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;
import com.hungsum.oa.others.HSOADjlx;
import com.hungsum.oa.webservices.HSOAWebService;

public class HsActivity_DJ_HsClztbhjl extends HsActivity_DJ
{
	private UcChooseSingleItem ucCl;

	private UcDateBox ucJlrq;

	private UcTextBox ucBhyy;

	private UcMultiRadio ucXzt;

	private UcTextBox ucBz;

	public HsActivity_DJ_HsClztbhjl()
	{
		super();
		
		this.inDJParams = new DJParams(false, true, false);
		
		this.mAnnexClass = HSOADjlx.HSCLZTBHJL;
	}

	
	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.setTitleSaved(HSOADjlx.HS车辆状态变化记录);

		this.ucCl = new UcChooseSingleItem(this);
		this.ucCl.SetFlag(HSOADjlx.HSCLDA);
		this.ucCl.setCName("车辆");
		this.ucCl.setAllowEmpty(false);
		this.controls.add(this.ucCl);

		this.ucJlrq = new UcDateBox(this);
		this.ucJlrq.setCName("记录日期");
		this.ucJlrq.setAllowEmpty(false);
		this.controls.add(this.ucJlrq);

		this.ucBhyy = new UcTextBox(this);
		this.ucBhyy.setCName("变化原因");
		this.ucBhyy.setAllowEmpty(false);
		this.controls.add(this.ucBhyy);

		this.ucXzt = new UcMultiRadio(this);
		this.ucXzt.SetDatas("0,在用;1,闲置;2,出借;100,处置;101,报废","0");
		this.ucXzt.setCName("新状态");
		this.ucXzt.setAllowEmpty(false);
		this.controls.add(this.ucXzt);
		
		this.ucBz = new UcTextBox(this);
		this.ucBz.setCName("备注");
		this.ucBz.setAllowEmpty(true);
		this.controls.add(this.ucBz);
		
	}

	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new HsClztbhjlFragment();
	}
	
	@Override
	protected void setData(IHsLabelValue data)
	{
		// 表头数据

		this.setDJId(data.getValue("JlId", "-1").toString());

		this.ucCl.setControlValue(data.getValue("ClId", "") + "," + data.getValue("Cphm",""));

		this.ucBhyy.setControlValue(data.getValue("Bhyy", "").toString());

		this.ucJlrq.setControlValue(data.getValue("Jlrq", "").toString());

		this.ucXzt.setControlValue(data.getValue("Xzt", "").toString());

		this.ucBz.setControlValue(data.getValue("Bz", "").toString());

	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		return ((HSOAWebService) application.getWebService()).updateHsClztbhjl(
							application.getLoginData().getProgressId(),
							this.getDJId(),
							this.ucCl.getControlValue().toString(),
							this.ucJlrq.getControlValue().toString(),
							this.ucBhyy.getControlValue().toString(),
							this.ucXzt.getControlValue().toString(),
							this.ucBz.getControlValue().toString(),
							this.mIsNewRecorder);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("UpdateHsClztbhjl"))
		{
			// 在此处需要调用updateAnnex方法更新图像、附件即可，对于新建单据更新图像前一定要赋值mDJId。
			this.setDJId(data.toString());
			
			this.updateAnnex();
		}else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}


	private class HsClztbhjlFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
			UcFormItem f_cl = new UcFormItem(context, ucCl);

			UcFormItem f_bhyy = new UcFormItem(context, ucBhyy);
			
			UcFormItem f_jlrq = new UcFormItem(context, ucJlrq);

			UcFormItem f_xzt = new UcFormItem(context, ucXzt);

			UcFormItem f_bz = new UcFormItem(context, ucBz);

			mainView.addView(f_jlrq.GetView());
			mainView.addView(f_cl.GetView());
			mainView.addView(f_xzt.GetView());
			mainView.addView(f_bhyy.GetView());
			mainView.addView(f_bz.GetView());
		}
		
	}
	
}
