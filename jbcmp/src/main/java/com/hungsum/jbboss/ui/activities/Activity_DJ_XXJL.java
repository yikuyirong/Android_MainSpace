package com.hungsum.jbboss.ui.activities;

import java.io.Serializable;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.controls.UcChooseSingleItem;
import com.hungsum.framework.ui.controls.UcFormItem;
import com.hungsum.framework.ui.controls.UcTextBox;
import com.hungsum.framework.ui.fragments.HsFragment_DJ_Image;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;
import com.hungsum.jbboss.webservices.JbcmpWebService;
import com.hungsum.oa.workflow.ui.activities.HsActivity_DJ_HsFyspd;

public class Activity_DJ_XXJL extends HsActivity_DJ
{
	/*
	 * 信息来源
	 */
	private UcChooseSingleItem ucXxly;
	
	/*
	 * 接收人
	 */
	private UcTextBox ucJsr;
	
	/*
	 * 信息类型
	 */
	private UcChooseSingleItem ucXxlx;

	/*
	 * 通用编号
	 */
	private UcTextBox ucTybh;
	
	/*
	 * 摘要
	 */
	private UcTextBox ucZy;

	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.setTitleSaved("信息记录维护");

		DJParams params = new DJParams(false, true , false);

		this.setDJParams(params);
		mAnnexClass = "XXJL";

		ucXxly = new UcChooseSingleItem(this).SetFlag("Xxly");
		ucXxly.setCName("信息来源");
		ucXxly.setAllowEmpty(false);
		this.controls.add(this.ucXxly);

		ucJsr = new UcTextBox(this);
		ucJsr.setCName("接收人");
		ucJsr.setAllowEmpty(false);
		this.controls.add(this.ucJsr);
		
		ucXxlx = new UcChooseSingleItem(this).SetFlag("Xxlx");
		ucXxlx.setCName("信息类型");
		ucXxlx.setAllowEmpty(false);
		this.controls.add(this.ucXxlx);
		
		ucTybh = new UcTextBox(this);
		ucTybh.setCName("通用编号");
		ucTybh.setAllowEmpty(true);
		this.controls.add(this.ucTybh);

		ucZy = new UcTextBox(this);
		ucZy.setCName("摘要");
		ucZy.setAllowEmpty(false);
		this.controls.add(this.ucZy);

		
	}
	

	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new MainFragement();
	}
	
	@Override
	protected HsFragment_DJ_Image createDJImageFragment(String imageClass)
	{
		ImageFragment f = new ImageFragment();
		f.setAnnexClass(imageClass);
		return f;
	}

	@Override
	protected void newData()
	{
		super.newData();
		
		//接收人默认为当前操作员
		this.ucJsr.setControlValue(application.getLoginData().getUsername());
	}
	
	@Override
	protected void setData(IHsLabelValue data)
	{
		this.setDJId(data.getValue("Id","-1").toString());
		
		this.ucXxly.SetData(data.getValue("Xxly", "").toString(),data.getValue("Xxlymc", "").toString());
		this.ucJsr.setControlValue(data.getValue("Jsr","").toString());
		this.ucXxlx.SetData(data.getValue("Xxlx","").toString(),data.getValue("Xxlxmc","").toString());
		this.ucTybh.setControlValue(data.getValue("Bh","").toString());
		this.ucZy.setControlValue(data.getValue("Zy","").toString());
		
		
	}
	
	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		return ((JbcmpWebService)application.getWebService()).UpdateXxjl(
				application.getLoginData().getProgressId(),
				getDJId(),
				ucXxly.getControlValue(),
				ucJsr.getControlValue(),
				ucXxlx.getControlValue(),
				ucTybh.getControlValue(),
				ucZy.getControlValue(),
				mIsNewRecorder);
	}
	
	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if(funcname.equals("UpdateXxjl"))
		{
			//在此处需要调用updateAnnex方法更新图像即可，对于新建单据更新图像前一定要赋值mDJId。
			this.setDJId(data.toString());
			
			this.updateAnnex(); //更新附件，包括文件、图片等。
			
			
			//updateSysImages();
		}else {
			super.actionAfterWSReturnData(funcname, data);
		}
	}

	public static class MainFragement extends HsFragment_ZD_Main
	{
		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
			Activity_DJ_XXJL activity = (Activity_DJ_XXJL) getActivity();

			UcFormItem f_xxly = new UcFormItem(context, activity.ucXxly);
			
			UcFormItem f_jsr = new UcFormItem(context, activity.ucJsr);
			
			UcFormItem f_xxlx = new UcFormItem(context, activity.ucXxlx);
			
			UcFormItem f_tybh = new UcFormItem(context, activity.ucTybh);
			
			UcFormItem f_zy = new UcFormItem(context, activity.ucZy);

			mainView.addView(f_xxly.GetView());
			mainView.addView(f_jsr.GetView());
			mainView.addView(f_xxlx.GetView());
			mainView.addView(f_tybh.GetView());
			mainView.addView(f_zy.GetView());
		}
	}

	public static class ImageFragment extends HsFragment_DJ_Image
	{

		public ImageFragment()
		{
			super();
			
			Title = "记录影印件";
		}
		
	}
	
}
