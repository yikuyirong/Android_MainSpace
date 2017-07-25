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
import com.hungsum.framework.ui.controls.UcSfzh;
import com.hungsum.framework.ui.controls.UcTextBox;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;
import com.hungsum.oa.others.HSOADjlx;
import com.hungsum.oa.webservices.HSOAWebService;

public class HsActivity_DJ_HsRyda extends HsActivity_DJ
{
	private UcTextBox ucDabh;

	private UcTextBox ucRyxm;

	private UcChooseSingleItem ucBm;

	private UcChooseSingleItem ucZw;

	private UcMultiRadio ucXb;

	private UcChooseSingleItem ucMz;

	private UcChooseSingleItem ucWhcd;

	private UcDateBox ucGzrq;

	private UcDateBox ucRzrq;

	private UcSfzh ucSfzh;

	private UcMultiRadio ucRyzt;

	public HsActivity_DJ_HsRyda()
	{
		super();
		
		this.inDJParams = new DJParams(false, true, false);
		
		this.inDJParams.setImagePageAllowEmpty(true);
		
		this.mAnnexClass = HSOADjlx.HSRYDA;
	}

	
	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.setTitleSaved(HSOADjlx.HS人员档案);

		this.ucDabh = new UcTextBox(this);
		this.ucDabh.setCName("档案编号");
		this.ucDabh.setAllowEmpty(false);
		this.controls.add(this.ucDabh);
		
		this.ucRyxm = new UcTextBox(this);
		this.ucRyxm.setCName("人员姓名");
		this.ucRyxm.setAllowEmpty(false);
		this.controls.add(this.ucRyxm);
		
		this.ucBm = new UcChooseSingleItem(this);
		this.ucBm.SetFlag(HSOADjlx.SYSDEPT);
		this.ucBm.setCName("部门");
		this.ucBm.setAllowEmpty(false);
		this.controls.add(this.ucBm);
		
		this.ucZw = new UcChooseSingleItem(this);
		this.ucZw.SetFlag(HSOADjlx.HSRYZW);
		this.ucZw.setCName("职务");
		this.ucZw.setAllowEmpty(false);
		this.controls.add(this.ucZw);
		
		this.ucXb = new UcMultiRadio(this);
		this.ucXb.SetDatas("0,男;1,女", "0");
		this.ucXb.setCName("性别");
		this.ucXb.setAllowEmpty(false);
		this.controls.add(this.ucXb);
		
		this.ucMz = new UcChooseSingleItem(this);
		this.ucMz.SetFlag(HSOADjlx.HSRYMZ);
		this.ucMz.setCName("民族");
		this.ucMz.setAllowEmpty(false);
		this.controls.add(this.ucMz);
		
		this.ucWhcd = new UcChooseSingleItem(this);
		this.ucWhcd.SetFlag(HSOADjlx.HSWHCD);
		this.ucWhcd.setCName("文化程度");
		this.ucWhcd.setAllowEmpty(false);
		this.controls.add(this.ucWhcd);
		
		this.ucGzrq = new UcDateBox(this);
		this.ucGzrq.setCName("工作日期");
		this.ucGzrq.setAllowEmpty(false);
		this.controls.add(this.ucGzrq);
		
		this.ucRzrq = new UcDateBox(this);
		this.ucRzrq.setCName("入职日期");
		this.ucRzrq.setAllowEmpty(false);
		this.controls.add(this.ucRzrq);
		
		this.ucSfzh = new UcSfzh(this);
		this.ucSfzh.setCName("身份证号");
		this.ucSfzh.setAllowEmpty(false);
		this.controls.add(this.ucSfzh);
		
		this.ucRyzt = new UcMultiRadio(this);
		this.ucRyzt.setCName("当前状态");
		this.ucRyzt.SetDatas("0,在岗;1,内退;2,长病假;3,产假;4,停薪留职;100,退休;101,离职", "0");

	}

	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new HsRydaFragment();
	}
	
	@Override
	protected void newData()
	{
		super.newData();
		
		this.ucGzrq.SetFlag("Now");
		
		this.ucRzrq.SetFlag("Now");
	}
	

	@Override
	protected void setData(IHsLabelValue data)
	{
		// 表头数据

		this.setDJId(data.getValue("RyId", "-1").toString());

		this.ucDabh.setControlValue(data.getValue("Dabh", "").toString());

		this.ucRyxm.setControlValue(data.getValue("Ryxm", "").toString());

		this.ucXb.setControlValue(data.getValue("Ryxb", "").toString());

		this.ucMz.setControlValue(data.getValue("Rymz", "") + "," + data.getValue("Rymzmc",""));

		this.ucWhcd.setControlValue(data.getValue("Whcd", "") + "," + data.getValue("Whcdmc",""));

		this.ucSfzh.setControlValue(data.getValue("Sfzh", "").toString());

		this.ucGzrq.setControlValue(data.getValue("Gzrq", "").toString());

		this.ucRzrq.setControlValue(data.getValue("Rzrq", "").toString());

		this.ucBm.setControlValue(data.getValue("Bmbh", "") + "," + data.getValue("Bmmc",""));
		this.ucBm.setAllowEdit(false);

		this.ucZw.setControlValue(data.getValue("Zwbh", "") + "," + data.getValue("Zwmc",""));
		this.ucZw.setAllowEdit(false);
		
		this.ucRyzt.setControlValue(data.getValue("Ryzt", "").toString());
		this.ucRyzt.setAllowEdit(false);

	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		return ((HSOAWebService) application.getWebService()).updateHsRyda(
							application.getLoginData().getProgressId(),
							this.getDJId(),
							this.ucDabh.getControlValue().toString(),
							this.ucRyxm.getControlValue().toString(),
							this.ucBm.getControlValue().toString(),
							this.ucZw.getControlValue().toString(),
							this.ucXb.getControlValue().toString(),
							this.ucMz.getControlValue().toString(),
							this.ucWhcd.getControlValue().toString(),
							this.ucGzrq.getControlValue().toString(),
							this.ucRzrq.getControlValue().toString(),
							this.ucSfzh.getControlValue().toString(),
							this.ucRyzt.getControlValue().toString(),
							this.mIsNewRecorder);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("UpdateHsRyda"))
		{
			// 在此处需要调用updateAnnex方法更新图像即可，对于新建单据更新图像前一定要赋值mDJId。

			this.setDJId(data.toString());

			this.updateAnnex();

		}else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}


	private class HsRydaFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
			UcFormItem f_dabh = new UcFormItem(context, ucDabh);
			
			UcFormItem f_ryxm = new UcFormItem(context,ucRyxm);
			
			UcFormItem f_xb = new UcFormItem(context, ucXb);
			
			UcFormItem f_mz = new UcFormItem(context, ucMz);
			
			UcFormItem f_whcd = new UcFormItem(context,ucWhcd);
			
			UcFormItem f_gzrq = new UcFormItem(context, ucGzrq);

			UcFormItem f_rzrq = new UcFormItem(context, ucRzrq);

			UcFormItem f_sfzh = new UcFormItem(context, ucSfzh);

			UcFormItem f_ryzt = new UcFormItem(context, ucRyzt);

			UcFormItem f_bm = new UcFormItem(context, ucBm);

			UcFormItem f_zw = new UcFormItem(context, ucZw);

			mainView.addView(f_dabh.GetView());
			mainView.addView(f_ryxm.GetView());
			mainView.addView(f_xb.GetView());
			mainView.addView(f_mz.GetView());
			mainView.addView(f_whcd.GetView());
			mainView.addView(f_sfzh.GetView());
			mainView.addView(f_gzrq.GetView());
			mainView.addView(f_rzrq.GetView());
			mainView.addView(f_bm.GetView());
			mainView.addView(f_zw.GetView());
			mainView.addView(f_ryzt.GetView());
		}
		
	}
	
}
