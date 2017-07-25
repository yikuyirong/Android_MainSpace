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

public class HsActivity_DJ_HsClda extends HsActivity_DJ
{
	private UcTextBox ucCphm;

	private UcMultiRadio ucClcq;

	private UcTextBox ucCllx;

	private UcChooseSingleItem ucBm;

	private UcTextBox ucSyr;

	private UcTextBox ucZz;

	private UcTextBox ucSyxz;

	private UcTextBox ucPpxh;

	private UcTextBox ucSbdh;
	
	private UcTextBox ucFdjh;
	
	private UcDateBox ucZcrq;

	private UcDateBox ucFzrq;

	private UcMultiRadio ucClzt;

	public HsActivity_DJ_HsClda()
	{
		super();
		
		this.inDJParams = new DJParams(false, true, false);
		
		this.mAnnexClass = HSOADjlx.HSCLDA;
	}

	
	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.setTitleSaved(HSOADjlx.HS车辆档案);

		this.ucCphm = new UcTextBox(this);
		this.ucCphm.setCName("车牌号码");
		this.ucCphm.setAllowEmpty(false);
		this.controls.add(this.ucCphm);
		
		this.ucClcq = new UcMultiRadio(this);
		this.ucClcq.SetDatas("0,公司;1,个人","0");
		this.ucClcq.setCName("车辆产权");
		this.ucClcq.setAllowEmpty(false);
		this.controls.add(this.ucClcq);
		
		this.ucCllx = new UcTextBox(this);
		this.ucCllx.setCName("车辆类型");
		this.ucCllx.setAllowEmpty(false);
		this.controls.add(this.ucCllx);
		
		this.ucSyr = new UcTextBox(this);
		this.ucSyr.setCName("所有人");
		this.ucSyr.setAllowEmpty(false);
		this.controls.add(this.ucSyr);
		
		this.ucZz = new UcTextBox(this);
		this.ucZz.setCName("住址");
		this.ucZz.setAllowEmpty(false);
		this.controls.add(this.ucZz);
		
		this.ucSyxz = new UcTextBox(this);
		this.ucSyxz.setCName("使用性质");
		this.ucSyxz.setAllowEmpty(false);
		this.controls.add(this.ucSyxz);
		
		this.ucPpxh = new UcTextBox(this);
		this.ucPpxh.setCName("品牌型号");
		this.ucPpxh.setAllowEmpty(false);
		this.controls.add(this.ucPpxh);
		
		this.ucSbdh = new UcTextBox(this);
		this.ucSbdh.setCName("识别代号");
		this.ucSbdh.setAllowEmpty(false);
		this.controls.add(this.ucSbdh);
		
		this.ucFdjh = new UcTextBox(this);
		this.ucFdjh.setCName("发动机号");
		this.ucFdjh.setAllowEmpty(false);
		this.controls.add(this.ucFdjh);
		
		this.ucZcrq = new UcDateBox(this);
		this.ucZcrq.setCName("注册日期");
		this.ucZcrq.setAllowEmpty(false);
		this.controls.add(this.ucZcrq);
		
		this.ucFzrq = new UcDateBox(this);
		this.ucFzrq.setCName("发证日期");
		this.ucFzrq.setAllowEmpty(false);
		this.controls.add(this.ucFzrq);
		
		this.ucBm = new UcChooseSingleItem(this);
		this.ucBm.SetFlag(HSOADjlx.SYSDEPT);
		this.ucBm.setCName("部门");
		this.ucBm.setAllowEmpty(false);
		this.controls.add(this.ucBm);
		
		this.ucClzt = new UcMultiRadio(this);
		this.ucClzt.setCName("当前状态");
		this.ucClzt.SetDatas("0,在用;1,闲置;2,出借;100,处置;101,报废","0");

	}

	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new HsCldaFragment();
	}

	@Override
	protected void setData(IHsLabelValue data)
	{
		// 表头数据

		this.setDJId(data.getValue("ClId", "-1").toString());

		this.ucCphm.setControlValue(data.getValue("Cphm", "").toString());

		this.ucClcq.setControlValue(data.getValue("Clcq", "").toString());

		this.ucCllx.setControlValue(data.getValue("Cllx", "").toString());

		this.ucSyr.setControlValue(data.getValue("Syr", "").toString());

		this.ucZz.setControlValue(data.getValue("Zz", "").toString());

		this.ucSyxz.setControlValue(data.getValue("Syxz", "").toString());

		this.ucPpxh.setControlValue(data.getValue("Ppxh", "").toString());
		
		this.ucSbdh.setControlValue(data.getValue("Sbdh", "").toString());

		this.ucFdjh.setControlValue(data.getValue("Fdjh", "").toString());
		
		this.ucZcrq.setControlValue(data.getValue("Zcrq", "").toString());

		this.ucFzrq.setControlValue(data.getValue("Fzrq", "").toString());

		this.ucBm.setControlValue(data.getValue("Bmbh", "") + "," + data.getValue("Bmmc",""));
		this.ucBm.setAllowEdit(false);
		
		this.ucClzt.setControlValue(data.getValue("Clzt", "").toString());
		this.ucClzt.setAllowEdit(false);
	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		return ((HSOAWebService) application.getWebService()).updateHsClda(
							application.getLoginData().getProgressId(),
							this.getDJId(),
							this.ucClcq.getControlValue().toString(),
							this.ucCphm.getControlValue().toString(),
							this.ucCllx.getControlValue().toString(),
							this.ucSyr.getControlValue().toString(),
							this.ucZz.getControlValue().toString(),
							this.ucSyxz.getControlValue().toString(),
							this.ucPpxh.getControlValue().toString(),
							this.ucSbdh.getControlValue().toString(),
							this.ucFdjh.getControlValue().toString(),
							this.ucZcrq.getControlValue().toString(),
							this.ucFzrq.getControlValue().toString(),
							this.ucBm.getControlValue().toString(),
							this.ucClzt.getControlValue().toString(),
							this.mIsNewRecorder);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("UpdateHsClda"))
		{
			// 在此处需要调用updateAnnex方法更新图像、附件即可，对于新建单据更新图像前一定要赋值mDJId。
			this.setDJId(data.toString());
			
			this.updateAnnex();
		}else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}


	private class HsCldaFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
			UcFormItem f_cphm = new UcFormItem(context, ucCphm);
			
			UcFormItem f_cllx = new UcFormItem(context,ucCllx);
			
			UcFormItem f_clcq = new UcFormItem(context, ucClcq);
			
			UcFormItem f_zz = new UcFormItem(context, ucZz);
			
			UcFormItem f_syxz = new UcFormItem(context,ucSyxz);
			
			UcFormItem f_zcrq = new UcFormItem(context, ucZcrq);

			UcFormItem f_fzrq = new UcFormItem(context, ucFzrq);

			UcFormItem f_ppxh = new UcFormItem(context, ucPpxh);

			UcFormItem f_sbdh = new UcFormItem(context, ucSbdh);

			UcFormItem f_fdjh = new UcFormItem(context, ucFdjh);

			UcFormItem f_clzt = new UcFormItem(context, ucClzt);

			UcFormItem f_bm = new UcFormItem(context, ucBm);

			UcFormItem f_syr = new UcFormItem(context, ucSyr);

			mainView.addView(f_cphm.GetView());
			mainView.addView(f_clcq.GetView());
			mainView.addView(f_cllx.GetView());
			mainView.addView(f_syr.GetView());
			mainView.addView(f_zz.GetView());
			mainView.addView(f_syxz.GetView());
			mainView.addView(f_ppxh.GetView());
			mainView.addView(f_sbdh.GetView());
			mainView.addView(f_fdjh.GetView());
			mainView.addView(f_zcrq.GetView());
			mainView.addView(f_fzrq.GetView());
			mainView.addView(f_bm.GetView());
			mainView.addView(f_clzt.GetView());
		}
		
	}
	
}
