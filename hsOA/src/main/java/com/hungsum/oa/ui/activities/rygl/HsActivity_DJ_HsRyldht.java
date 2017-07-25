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

public class HsActivity_DJ_HsRyldht extends HsActivity_DJ
{
	private UcChooseSingleItem ucRy;

	private UcTextBox ucHtbh;

	private UcDateBox ucHtrq;

	private UcMultiRadio ucHtlx;

	private UcNumericInput ucHtnx;

	private UcTextBox ucBz;

	public HsActivity_DJ_HsRyldht()
	{
		super();
		
		this.inDJParams = new DJParams(false, true, false);
		
		this.mAnnexClass = HSOADjlx.HSRYLDHT;
	}

	
	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.setTitleSaved(HSOADjlx.HS人员劳动合同);

		this.ucRy = new UcChooseSingleItem(this);
		this.ucRy.SetFlag(HSOADjlx.HSRYDA);
		this.ucRy.setCName("人员");
		this.ucRy.setAllowEmpty(false);
		this.controls.add(this.ucRy);
		
		this.ucHtbh = new UcTextBox(this);
		this.ucHtbh.setCName("合同编号");
		this.ucHtbh.setAllowEmpty(false);
		this.controls.add(this.ucHtbh);
		
		this.ucHtnx = new UcNumericInput(this);
		this.ucHtnx.setCName("合同年限");
		this.ucHtnx.setAllowEmpty(false);
		this.controls.add(this.ucHtnx);
		
		this.ucHtlx = new UcMultiRadio(this);
		this.ucHtlx.SetDatas("0,正式工;1,合同工;2,临时工","0");
		this.ucHtlx.setCName("合同类型");
		this.ucHtlx.setAllowEmpty(false);
		this.controls.add(this.ucHtlx);

		this.ucHtrq = new UcDateBox(this);
		this.ucHtrq.setCName("合同日期");
		this.ucHtrq.setAllowEmpty(false);
		this.controls.add(this.ucHtrq);

		this.ucBz = new UcTextBox(this);
		this.ucBz.setCName("备注");
		this.ucBz.setAllowEmpty(true);
		this.controls.add(this.ucBz);
		
	}

	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new HsRyldhtFragment();
	}
	
	@Override
	protected void newData()
	{
		super.newData();
	}
	
	@Override
	protected void setData(IHsLabelValue data)
	{
		// 表头数据

		this.setDJId(data.getValue("HtId", "-1").toString());

		this.ucRy.setControlValue(data.getValue("RyId", "") + "," + data.getValue("Ryxm",""));

		this.ucHtbh.setControlValue(data.getValue("Htbh", "").toString());

		this.ucHtlx.setControlValue(data.getValue("Htlx", "").toString());

		this.ucHtrq.setControlValue(data.getValue("Htrq", "").toString());

		this.ucHtnx.setControlValue(data.getValue("Htnx", "").toString());

		this.ucBz.setControlValue(data.getValue("Bz", "").toString());

	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		return ((HSOAWebService) application.getWebService()).updateHsRyldht(
							application.getLoginData().getProgressId(),
							this.getDJId(),
							this.ucHtbh.getControlValue().toString(),
							this.ucRy.getControlValue().toString(),
							this.ucHtrq.getControlValue().toString(),
							this.ucHtlx.getControlValue().toString(),
							this.ucHtnx.getControlValue().toString(),
							this.ucBz.getControlValue().toString(),
							this.mIsNewRecorder);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("UpdateHsRyldht"))
		{
			// 在此处需要调用updateAnnex方法更新图像、附件即可，对于新建单据更新图像前一定要赋值mDJId。
			this.setDJId(data.toString());
			
			this.updateAnnex();
		}else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}


	private class HsRyldhtFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
			UcFormItem f_ry = new UcFormItem(context, ucRy);
			
			UcFormItem f_htbh = new UcFormItem(context,ucHtbh);
			
			UcFormItem f_htlx = new UcFormItem(context, ucHtlx);
			
			UcFormItem f_htrq = new UcFormItem(context, ucHtrq);

			UcFormItem f_htnx = new UcFormItem(context, ucHtnx);

			UcFormItem f_bz = new UcFormItem(context, ucBz);

			mainView.addView(f_htrq.GetView());
			mainView.addView(f_htbh.GetView());
			mainView.addView(f_ry.GetView());
			mainView.addView(f_htlx.GetView());
			mainView.addView(f_htnx.GetView());
			mainView.addView(f_bz.GetView());
		}
		
	}
	
}
