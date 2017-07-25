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

public class HsActivity_DJ_HsRyxjjl extends HsActivity_DJ
{
	private UcDateBox ucJlrq;

	private UcChooseSingleItem ucRy;

	private UcMultiRadio ucXjlx;

	private UcDateBox ucKsrq;

	private UcNumericInput ucTs;

	private UcTextBox ucBz;

	public HsActivity_DJ_HsRyxjjl()
	{
		super();
		
		this.inDJParams = new DJParams(false, true, false);
		
		this.mAnnexClass = HSOADjlx.HSRYXJJL;
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

		this.ucRy = new UcChooseSingleItem(this);
		this.ucRy.SetFlag(HSOADjlx.HSRYDA);
		this.ucRy.setCName("人员");
		this.ucRy.setAllowEmpty(false);
		this.controls.add(this.ucRy);

		this.ucXjlx = new UcMultiRadio(this);
		this.ucXjlx.SetDatas("0,公休;1,病假;2,事假","0");
		this.ucXjlx.setCName("休假类型");
		this.ucXjlx.setAllowEmpty(false);
		this.controls.add(this.ucXjlx);
		
		this.ucKsrq = new UcDateBox(this);
		this.ucKsrq.setCName("开始日期");
		this.ucKsrq.setAllowEmpty(false);
		this.controls.add(this.ucKsrq);

		this.ucTs = new UcNumericInput(this);
		this.ucTs.setCName("休假天数");
		this.ucTs.setAllowEmpty(false);
		this.controls.add(this.ucTs);
		
		this.ucBz = new UcTextBox(this);
		this.ucBz.setCName("备注");
		this.ucBz.setAllowEmpty(true);
		this.controls.add(this.ucBz);
		
	}

	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new HsRyxjjlFragment();
	}
	
	@Override
	protected void setData(IHsLabelValue data)
	{
		// 表头数据

		this.setDJId(data.getValue("JlId", "-1").toString());

		this.ucRy.setControlValue(data.getValue("RyId", "") + "," + data.getValue("Ryxm",""));

		this.ucXjlx.setControlValue(data.getValue("Xjlx", "").toString());
		
		this.ucKsrq.setControlValue(data.getValue("Ksrq", "").toString());

		this.ucJlrq.setControlValue(data.getValue("Jlrq", "").toString());

		this.ucTs.setControlValue(data.getValue("Ts", "").toString());

		this.ucBz.setControlValue(data.getValue("Bz", "").toString());

	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		return ((HSOAWebService) application.getWebService()).updateHsRyxjjl(
							application.getLoginData().getProgressId(),
							this.getDJId(),
							this.ucRy.getControlValue().toString(),
							this.ucJlrq.getControlValue().toString(),
							this.ucXjlx.getControlValue().toString(),
							this.ucKsrq.getControlValue().toString(),
							this.ucTs.getControlValue().toString(),
							this.ucBz.getControlValue().toString(),
							this.mIsNewRecorder);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("UpdateHsRyxjjl"))
		{
			// 在此处需要调用updateAnnex方法更新图像、附件即可，对于新建单据更新图像前一定要赋值mDJId。
			this.setDJId(data.toString());
			
			this.updateAnnex();
		}else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}


	private class HsRyxjjlFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
			UcFormItem f_ry = new UcFormItem(context, ucRy);

			UcFormItem f_xjlx = new UcFormItem(context, ucXjlx);

			UcFormItem f_ksrq = new UcFormItem(context, ucKsrq);
			
			UcFormItem f_jlrq = new UcFormItem(context, ucJlrq);

			UcFormItem f_ts = new UcFormItem(context, ucTs);

			UcFormItem f_bz = new UcFormItem(context, ucBz);

			mainView.addView(f_jlrq.GetView());
			mainView.addView(f_ry.GetView());
			mainView.addView(f_xjlx.GetView());
			mainView.addView(f_ksrq.GetView());
			mainView.addView(f_ts.GetView());
			mainView.addView(f_bz.GetView());
		}
		
	}
	
}
