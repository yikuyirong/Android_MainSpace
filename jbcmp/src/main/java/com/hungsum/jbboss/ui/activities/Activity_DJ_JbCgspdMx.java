package com.hungsum.jbboss.ui.activities;

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.hungsum.framework.componments.HsLabelValue;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.controls.UcFormItem;
import com.hungsum.framework.ui.controls.UcTextBox;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;
import com.hungsum.jbboss.others.JbcmpDjlx;

public class Activity_DJ_JbCgspdMx extends HsActivity_DJ
{

	private UcTextBox ucMc;

	private UcTextBox ucXh;

	private UcTextBox ucKcsl;

	private UcTextBox ucSl;

	private UcTextBox ucDj;

	private UcTextBox ucJe;

	private UcTextBox ucYq;

	private UcTextBox ucBz;

	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.setTitleSaved(JbcmpDjlx.JB采购审批单明细);

		DJParams params = new DJParams(false, false, false);

		//
		this.setDJParams(params);

		this.ucMc = new UcTextBox(this);
		this.ucMc.setCName("名称");
		this.ucMc.setAllowEmpty(false);
		this.controls.add(this.ucMc);

		this.ucXh = new UcTextBox(this);
		this.ucXh.setCName("规格型号");
		this.ucXh.setAllowEmpty(true);
		this.controls.add(this.ucXh);

		this.ucKcsl = new UcTextBox(this);
		this.ucKcsl.setCName("库存数量");
		this.ucKcsl.setAllowEmpty(true);
		this.controls.add(this.ucKcsl);

		this.ucSl = new UcTextBox(this);
		this.ucSl.setCName("数量");
		this.ucSl.setAllowEmpty(false);
		this.controls.add(this.ucSl);

		this.ucDj = new UcTextBox(this);
		this.ucDj.setCName("单价");
		this.ucDj.setAllowEmpty(true);
		this.controls.add(this.ucDj);

		this.ucJe = new UcTextBox(this);
		this.ucJe.setCName("金额");
		this.ucJe.setAllowEmpty(true);
		this.controls.add(this.ucJe);

		this.ucYq = new UcTextBox(this);
		this.ucYq.setCName("要求");
		this.ucYq.setAllowEmpty(true);
		this.controls.add(this.ucYq);

		this.ucBz = new UcTextBox(this);
		this.ucBz.setCName("备注");
		this.ucBz.setAllowEmpty(true);
		this.controls.add(this.ucBz);


	}

	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new MainFragment();
	}

	@Override
	protected void setData(IHsLabelValue data) throws Exception
	{
		this.ucMc.setControlValue(data.getValue("Mc", "").toString());
		this.ucXh.setControlValue(data.getValue("Xh", "").toString());
		this.ucKcsl.setControlValue(data.getValue("Kcsl", "").toString());
		this.ucSl.setControlValue(data.getValue("Sl", "").toString());
		this.ucDj.setControlValue(data.getValue("Dj", "").toString());
		this.ucJe.setControlValue(data.getValue("Je", "").toString());
		this.ucYq.setControlValue(data.getValue("Yq", "").toString());
		this.ucBz.setControlValue(data.getValue("Bz", "").toString());
	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		String key = this.ucMc.getControlValue().toString();
		
		String value = this.ucSl.getControlValue().toString();
		
		IHsLabelValue keyValue = new HsLabelValue(key, value);
		
		keyValue.addDetail(new HsLabelValue("Mc", key));
		keyValue.addDetail(new HsLabelValue("Xh", this.ucXh.getControlValue()));
		keyValue.addDetail(new HsLabelValue("Kcsl", this.ucKcsl.getControlValue()));
		keyValue.addDetail(new HsLabelValue("Sl", this.ucSl.getControlValue()));
		keyValue.addDetail(new HsLabelValue("Dj", this.ucDj.getControlValue()));
		keyValue.addDetail(new HsLabelValue("Je", this.ucJe.getControlValue()));
		keyValue.addDetail(new HsLabelValue("Yq", this.ucYq.getControlValue()));
		keyValue.addDetail(new HsLabelValue("Bz", this.ucBz.getControlValue()));

		HsWSReturnObject returnObject = new HsWSReturnObject();
		
		returnObject.SetCodeNum(0); //
		returnObject.SetFuncName("UpdateJbCgspdMx");
		returnObject.SetData(keyValue);
		
		return returnObject;
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("UpdateJbCgspdMx"))
		{
			Intent intent = new Intent();
			intent.putExtra("Data", data);
			this.setResult(Activity.RESULT_OK , intent);
			
			this.finish();
		} else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}

	private class MainFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
			UcFormItem f_bm = new UcFormItem(context, ucMc);

			UcFormItem f_xh = new UcFormItem(context, ucXh);

			UcFormItem f_kcsl = new UcFormItem(context, ucKcsl);

			UcFormItem f_sl = new UcFormItem(context, ucSl);

			UcFormItem f_dj = new UcFormItem(context, ucDj);

			UcFormItem f_je = new UcFormItem(context, ucJe);

			UcFormItem f_yq = new UcFormItem(context, ucYq);

			UcFormItem f_bz = new UcFormItem(context, ucBz);

			mainView.addView(f_bm.GetView());
			mainView.addView(f_xh.GetView());
			mainView.addView(f_kcsl.GetView());
			mainView.addView(f_sl.GetView());
			mainView.addView(f_dj.GetView());
			mainView.addView(f_je.GetView());
			mainView.addView(f_yq.GetView());
			mainView.addView(f_bz.GetView());

		}

	}
}
