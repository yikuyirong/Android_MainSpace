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
import com.hungsum.framework.ui.controls.UcNumericInput;
import com.hungsum.framework.ui.controls.UcTextBox;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;
import com.hungsum.oa.others.HSOADjlx;
import com.hungsum.oa.webservices.HSOAWebService;

public class HsActivity_DJ_HsClxsjl extends HsActivity_DJ
{
	private UcChooseSingleItem ucCl;

	private UcDateBox ucJlrq;

	private UcDateBox ucKsrq;

	private UcDateBox ucJsrq;

	private UcTextBox ucJsy;
	
	private UcNumericInput ucLcsj;
	
	private UcTextBox ucFkxx;
	
	private UcTextBox ucBz;

	public HsActivity_DJ_HsClxsjl()
	{
		super();
		
		this.inDJParams = new DJParams(false, true, false);
		
		this.mAnnexClass = HSOADjlx.HSCLXSJL;
	}

	
	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.setTitleSaved(HSOADjlx.HS车辆行驶记录);

		this.ucCl = new UcChooseSingleItem(this);
		this.ucCl.SetFlag(HSOADjlx.HSCLDA);
		this.ucCl.setCName("车辆");
		this.ucCl.setAllowEmpty(false);
		this.controls.add(this.ucCl);

		this.ucJlrq = new UcDateBox(this);
		this.ucJlrq.setCName("记录日期");
		this.ucJlrq.setAllowEmpty(false);
		this.controls.add(this.ucJlrq);

		this.ucKsrq = new UcDateBox(this);
		this.ucKsrq.setCName("开始日期");
		this.ucKsrq.setAllowEmpty(false);
		this.controls.add(this.ucKsrq);

		this.ucJsrq = new UcDateBox(this);
		this.ucJsrq.setCName("结束日期");
		this.ucJsrq.setAllowEmpty(false);
		this.controls.add(this.ucJsrq);
		
		this.ucJsy = new UcTextBox(this);
		this.ucJsy.setCName("驾驶员");
		this.ucJsy.setAllowEmpty(false);
		this.controls.add(this.ucJsy);

		this.ucLcsj = new UcNumericInput(this);
		this.ucLcsj.setCName("里程数据");
		this.ucLcsj.setAllowEmpty(false);
		this.controls.add(this.ucLcsj);

		this.ucFkxx = new UcTextBox(this);
		this.ucFkxx.setCName("反馈信息");
		this.ucFkxx.setAllowEmpty(false);
		this.controls.add(this.ucFkxx);

		this.ucBz = new UcTextBox(this);
		this.ucBz.setCName("备注");
		this.ucBz.setAllowEmpty(true);
		this.controls.add(this.ucBz);
		
	}

	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new HsClxsjlFragment();
	}
	
	@Override
	protected void setData(IHsLabelValue data)
	{
		// 表头数据

		this.setDJId(data.getValue("JlId", "-1").toString());

		this.ucCl.setControlValue(data.getValue("ClId", "") + "," + data.getValue("Cphm",""));

		this.ucJlrq.setControlValue(data.getValue("Jlrq", "").toString());

		this.ucKsrq.setControlValue(data.getValue("Ksrq", "").toString());

		this.ucJsrq.setControlValue(data.getValue("Jsrq", "").toString());

		this.ucJsy.setControlValue(data.getValue("Jsy", "").toString());

		this.ucLcsj.setControlValue(data.getValue("Lcsj", "").toString());

		this.ucFkxx.setControlValue(data.getValue("Fkxx", "").toString());

		this.ucBz.setControlValue(data.getValue("Bz", "").toString());

	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		return ((HSOAWebService) application.getWebService()).updateHsClxsjl(
							application.getLoginData().getProgressId(),
							this.getDJId(),
							this.ucCl.getControlValue().toString(),
							this.ucJlrq.getControlValue().toString(),
							this.ucKsrq.getControlValue().toString(),
							this.ucJsrq.getControlValue().toString(),
							this.ucJsy.getControlValue().toString(),
							this.ucLcsj.getControlValue().toString(),
							this.ucFkxx.getControlValue().toString(),
							this.ucBz.getControlValue().toString(),
							this.mIsNewRecorder);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("UpdateHsClxsjl"))
		{
			// 在此处需要调用updateAnnex方法更新图像、附件即可，对于新建单据更新图像前一定要赋值mDJId。
			this.setDJId(data.toString());
			
			this.updateAnnex();
		}else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}


	private class HsClxsjlFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
			UcFormItem f_cl = new UcFormItem(context, ucCl);

			UcFormItem f_jlrq = new UcFormItem(context, ucJlrq);

			UcFormItem f_ksrq = new UcFormItem(context, ucKsrq);
			
			UcFormItem f_jsrq = new UcFormItem(context, ucJsrq);

			UcFormItem f_jsy = new UcFormItem(context, ucJsy);

			UcFormItem f_lcsj = new UcFormItem(context, ucLcsj);

			UcFormItem f_fkxx = new UcFormItem(context, ucFkxx);

			UcFormItem f_bz = new UcFormItem(context, ucBz);

			mainView.addView(f_jlrq.GetView());
			mainView.addView(f_cl.GetView());
			mainView.addView(f_ksrq.GetView());
			mainView.addView(f_jsrq.GetView());
			mainView.addView(f_jsy.GetView());
			mainView.addView(f_lcsj.GetView());
			mainView.addView(f_fkxx.GetView());
			mainView.addView(f_bz.GetView());
		}
		
	}
	
}
