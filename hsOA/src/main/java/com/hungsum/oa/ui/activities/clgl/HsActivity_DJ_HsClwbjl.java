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
import com.hungsum.framework.ui.controls.UcMultiRadio;
import com.hungsum.framework.ui.controls.UcNumericInput;
import com.hungsum.framework.ui.controls.UcTextBox;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;
import com.hungsum.oa.others.HSOADjlx;
import com.hungsum.oa.webservices.HSOAWebService;

public class HsActivity_DJ_HsClwbjl extends HsActivity_DJ
{
	private UcChooseSingleItem ucCl;

	private UcDateBox ucJlrq;

	private UcMultiRadio ucWblx;
	
	private UcTextBox ucWbnr;

	private UcTextBox ucWbdd;

	private UcNumericInput ucBxje;
	
	private UcNumericInput ucZfje;
	
	private UcNumericInput ucWbje;
	
	private UcTextBox ucBz;

	public HsActivity_DJ_HsClwbjl()
	{
		super();
		
		this.inDJParams = new DJParams(false, true, false);
		
		this.mAnnexClass = HSOADjlx.HSCLWXBYJL;
	}

	
	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.setTitleSaved(HSOADjlx.HS车辆维修保养记录);

		this.ucCl = new UcChooseSingleItem(this);
		this.ucCl.SetFlag(HSOADjlx.HSCLDA);
		this.ucCl.setCName("车辆");
		this.ucCl.setAllowEmpty(false);
		this.controls.add(this.ucCl);

		this.ucJlrq = new UcDateBox(this);
		this.ucJlrq.setCName("记录日期");
		this.ucJlrq.setAllowEmpty(false);
		this.controls.add(this.ucJlrq);

		this.ucWblx = new UcMultiRadio(this);
		this.ucWblx.SetDatas("0,正常保养;1,普通维修;2,事故维修","0");
		this.ucWblx.setCName("维保类型");
		this.ucWblx.setAllowEmpty(false);
		this.controls.add(this.ucWblx);
		
		this.ucWbdd = new UcTextBox(this);
		this.ucWbdd.setCName("维保地点");
		this.ucWbdd.setAllowEmpty(false);
		this.controls.add(this.ucWbdd);
		
		this.ucWbnr = new UcTextBox(this);
		this.ucWbnr.setCName("维保内容");
		this.ucWbnr.setAllowEmpty(false);
		this.controls.add(this.ucWbnr);

		this.ucBxje = new UcNumericInput(this);
		this.ucBxje.setCName("保险金额");
		this.ucBxje.setAllowEmpty(true);
		this.ucBxje.addTextChangedListener(jeWatcher);
		this.controls.add(this.ucBxje);

		this.ucZfje = new UcNumericInput(this);
		this.ucZfje.setCName("自费金额");
		this.ucZfje.setAllowEmpty(true);
		this.ucZfje.addTextChangedListener(jeWatcher);
		this.controls.add(this.ucZfje);

		this.ucWbje = new UcNumericInput(this);
		this.ucWbje.setCName("维保金额");
		this.ucWbje.setAllowEdit(false);
		this.ucWbje.setAllowEmpty(true);
		this.controls.add(this.ucWbje);

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
			ucWbje.setNumberValue(ucBxje.getNumberValue() + ucZfje.getNumberValue());
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
		return new HsClwbjlFragment();
	}
	
	@Override
	protected void setData(IHsLabelValue data)
	{
		// 表头数据

		this.setDJId(data.getValue("JlId", "-1").toString());

		this.ucCl.setControlValue(data.getValue("ClId", "") + "," + data.getValue("Cphm",""));

		this.ucJlrq.setControlValue(data.getValue("Jlrq", "").toString());

		this.ucWblx.setControlValue(data.getValue("Wblx", "").toString());
		
		this.ucWbdd.setControlValue(data.getValue("Wbdd", "").toString());

		this.ucWbnr.setControlValue(data.getValue("Wbnr", "").toString());

		this.ucBxje.setControlValue(data.getValue("Bxje", "").toString());

		this.ucZfje.setControlValue(data.getValue("Zfje", "").toString());

		this.ucWbje.setAllowEdit(false);

		this.ucBz.setControlValue(data.getValue("Bz", "").toString());

	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		return ((HSOAWebService) application.getWebService()).updateHsClwbjl(
							application.getLoginData().getProgressId(),
							this.getDJId(),
							this.ucCl.getControlValue().toString(),
							this.ucJlrq.getControlValue().toString(),
							this.ucWblx.getControlValue().toString(),
							this.ucWbnr.getControlValue().toString(),
							this.ucWbdd.getControlValue().toString(),
							this.ucBxje.getControlValue().toString(),
							this.ucZfje.getControlValue().toString(),
							this.ucBz.getControlValue().toString(),
							this.mIsNewRecorder);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("UpdateHsClwbjl"))
		{
			// 在此处需要调用updateAnnex方法更新图像、附件即可，对于新建单据更新图像前一定要赋值mDJId。
			this.setDJId(data.toString());
			
			this.updateAnnex();
		}else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}


	private class HsClwbjlFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
			UcFormItem f_cl = new UcFormItem(context, ucCl);

			UcFormItem f_jlrq = new UcFormItem(context, ucJlrq);

			UcFormItem f_wblx = new UcFormItem(context, ucWblx);
			
			UcFormItem f_wbnr = new UcFormItem(context, ucWbnr);
			
			UcFormItem f_wbdd = new UcFormItem(context, ucWbdd);

			UcFormItem f_bxje = new UcFormItem(context, ucBxje);

			UcFormItem f_zfje = new UcFormItem(context, ucZfje);

			UcFormItem f_wbje = new UcFormItem(context, ucWbje);

			UcFormItem f_bz = new UcFormItem(context, ucBz);

			mainView.addView(f_jlrq.GetView());
			mainView.addView(f_cl.GetView());
			mainView.addView(f_wblx.GetView());
			mainView.addView(f_wbnr.GetView());
			mainView.addView(f_wbdd.GetView());
			mainView.addView(f_bxje.GetView());
			mainView.addView(f_zfje.GetView());
			mainView.addView(f_wbje.GetView());
			mainView.addView(f_bz.GetView());
		}
		
	}
	
}
