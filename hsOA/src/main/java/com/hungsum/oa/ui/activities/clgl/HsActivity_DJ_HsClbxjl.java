package com.hungsum.oa.ui.activities.clgl;

import java.io.Serializable;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class HsActivity_DJ_HsClbxjl extends HsActivity_DJ
{
	private UcChooseSingleItem ucCl;

	private UcDateBox ucJlrq;

	private UcDateBox ucSxrq;

	private UcTextBox ucBxgs;

	private UcTextBox ucBdhm;

	private UcNumericInput ucCcse;

	private UcNumericInput ucJqxe;

	private UcNumericInput ucSyxe;
	
	private UcTextBox ucSyxz;

	private UcNumericInput ucBxje;

	private UcTextBox ucBz;

	public HsActivity_DJ_HsClbxjl()
	{
		super();
		
		this.inDJParams = new DJParams(false, true, false);
		
		this.mAnnexClass = HSOADjlx.HSCLBXJL;
	}

	
	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.setTitleSaved(HSOADjlx.HS车辆保险记录);

		this.ucCl = new UcChooseSingleItem(this);
		this.ucCl.SetFlag(HSOADjlx.HSCLDA);
		this.ucCl.setCName("车辆");
		this.ucCl.setAllowEmpty(false);
		this.controls.add(this.ucCl);

		this.ucJlrq = new UcDateBox(this);
		this.ucJlrq.setCName("记录日期");
		this.ucJlrq.setAllowEmpty(false);
		this.controls.add(this.ucJlrq);

		this.ucSxrq = new UcDateBox(this);
		this.ucSxrq.setCName("生效日期");
		this.ucSxrq.setAllowEmpty(false);
		this.controls.add(this.ucSxrq);
		
		this.ucBxgs = new UcTextBox(this);
		this.ucBxgs.setCName("保险公司");
		this.ucBxgs.setAllowEmpty(false);
		this.controls.add(this.ucBxgs);
		
		this.ucBdhm = new UcTextBox(this);
		this.ucBdhm.setCName("保单号码");
		this.ucBdhm.setAllowEmpty(false);
		this.controls.add(this.ucBdhm);
		
		this.ucCcse = new UcNumericInput(this);
		this.ucCcse.setCName("车船税额");
		this.ucCcse.setAllowEmpty(false);
		this.controls.add(this.ucCcse);
		
		this.ucJqxe = new UcNumericInput(this);
		this.ucJqxe.setCName("交强险额");
		this.ucJqxe.setAllowEmpty(false);
		this.ucJqxe.addTextChangedListener(jeWatcher);
		this.ucJqxe.addTextChangedListener(watcher);
		this.controls.add(this.ucJqxe);
		
		this.ucSyxe = new UcNumericInput(this);
		this.ucSyxe.setCName("商业险额");
		this.ucSyxe.addTextChangedListener(jeWatcher);
		this.ucSyxe.setAllowEmpty(true);
		this.ucSyxe.addTextChangedListener(watcher);
		this.controls.add(this.ucSyxe);
		
		this.ucSyxz = new UcTextBox(this);
		this.ucSyxz.setCName("商业险种");
		this.ucSyxz.setAllowEmpty(true);
		this.controls.add(this.ucSyxz);
		
		this.ucBxje = new UcNumericInput(this);
		this.ucBxje.setCName("保险金额");
		this.ucBxje.setAllowEdit(false);
		this.ucBxje.setAllowEmpty(false);
		this.controls.add(this.ucBxje);

		this.ucBz = new UcTextBox(this);
		this.ucBz.setCName("备注");
		this.ucBz.setAllowEmpty(true);
		this.controls.add(this.ucBz);
	}

	private TextWatcher jeWatcher = new TextWatcher()
	{
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			ucBxje.setNumberValue(ucJqxe.getNumberValue() + ucSyxe.getNumberValue());
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after){}

		@Override
		public void afterTextChanged(Editable s){}

	};
	
	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new HsClbxjlFragment();
	}

	@Override
	protected void setData(IHsLabelValue data)
	{
		// 表头数据

		this.setDJId(data.getValue("JlId", "-1").toString());

		this.ucCl.setControlValue(data.getValue("ClId", "") + "," + data.getValue("Cphm",""));

		this.ucJlrq.setControlValue(data.getValue("Jlrq", "").toString());

		this.ucSxrq.setControlValue(data.getValue("Sxrq", "").toString());

		this.ucBxgs.setControlValue(data.getValue("Bxgs", "").toString());

		this.ucCcse.setControlValue(data.getValue("Ccse", "").toString());

		this.ucJqxe.setControlValue(data.getValue("Jqxe", "").toString());

		this.ucSyxe.setControlValue(data.getValue("Syxe", "").toString());
		
		this.ucSyxz.setControlValue(data.getValue("Syxz", "").toString());
		
		this.ucBxje.setAllowEdit(false);

		this.ucBz.setControlValue(data.getValue("Bz", "").toString());

		this.ucBdhm.setControlValue(data.getValue("Bmbh", "").toString());

	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		return ((HSOAWebService) application.getWebService()).updateHsClbxjl(
							application.getLoginData().getProgressId(),
							this.getDJId(),
							this.ucCl.getControlValue().toString(),
							this.ucJlrq.getControlValue().toString(),
							this.ucSxrq.getControlValue().toString(),
							this.ucBxgs.getControlValue().toString(),
							this.ucBdhm.getControlValue().toString(),
							this.ucCcse.getControlValue().toString(),
							this.ucJqxe.getControlValue().toString(),
							this.ucSyxe.getControlValue().toString(),
							this.ucSyxz.getControlValue().toString(),
							this.ucBz.getControlValue().toString(),
							this.mIsNewRecorder);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("UpdateHsClbxjl"))
		{
			// 在此处需要调用updateAnnex方法更新图像、附件即可，对于新建单据更新图像前一定要赋值mDJId。
			this.setDJId(data.toString());
			
			this.updateAnnex();
		}else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}


	TextWatcher watcher = new TextWatcher()
	{

		@Override
		public void afterTextChanged(Editable s){}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after){}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count)
		{
			ucBxje.setNumberValue(ucJqxe.getNumberValue() + ucSyxe.getNumberValue());
		}
		
	};
	
	private class HsClbxjlFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
			UcFormItem f_sxrq = new UcFormItem(context, ucSxrq);
			
			UcFormItem f_bxgs = new UcFormItem(context,ucBxgs);
			
			UcFormItem f_jqxe = new UcFormItem(context, ucJqxe);
			
			UcFormItem f_syxe = new UcFormItem(context, ucSyxe);
			
			UcFormItem f_bxje = new UcFormItem(context,ucBxje);
			
			UcFormItem f_cl = new UcFormItem(context, ucCl);

			UcFormItem f_jlrq = new UcFormItem(context, ucJlrq);

			UcFormItem f_bz = new UcFormItem(context, ucBz);

			UcFormItem f_syxz = new UcFormItem(context, ucSyxz);

			UcFormItem f_bdhm = new UcFormItem(context, ucBdhm);

			UcFormItem f_ccse = new UcFormItem(context, ucCcse);

			mainView.addView(f_jlrq.GetView());
			mainView.addView(f_cl.GetView());
			mainView.addView(f_sxrq.GetView());
			mainView.addView(f_bxgs.GetView());
			mainView.addView(f_bdhm.GetView());
			mainView.addView(f_ccse.GetView());
			mainView.addView(f_jqxe.GetView());
			mainView.addView(f_syxe.GetView());
			mainView.addView(f_syxz.GetView());
			mainView.addView(f_bxje.GetView());
			mainView.addView(f_bz.GetView());
		}
		
	}
	
}
