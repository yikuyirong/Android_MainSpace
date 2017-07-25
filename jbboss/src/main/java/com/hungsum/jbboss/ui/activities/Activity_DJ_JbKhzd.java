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
import com.hungsum.jbboss.others.JbbossDjlx;
import com.hungsum.jbboss.webservices.JbbossWebService;
import com.hungsum.oa.others.HSOADjlx;

public class Activity_DJ_JbKhzd extends HsActivity_DJ
{

	/*
	 * 客户名称
	 */
	private UcTextBox ucKhmc;
	
	/*
	 * 地区
	 */
	private UcChooseSingleItem ucDq;

	/*
	 * 地址
	 */
	private UcTextBox ucDz;
	
	/*
	 * 固定电话
	 */
	private UcTextBox ucGddh;

	/*
	 * 移动电话
	 */
	private UcTextBox ucYddh;

	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.setTitleSaved(JbbossDjlx.JB客户信息);

		mAnnexClass = JbbossDjlx.JBKHZD;

		ucKhmc = new UcTextBox(this);
		ucKhmc.setCName("客户名称");
		ucKhmc.setAllowEmpty(false);
		this.controls.add(this.ucKhmc);
		
		ucDq = new UcChooseSingleItem(this).SetFlag("JBDQZD");
		ucDq.setCName("地区");
		ucDq.setAllowEmpty(false);
		this.controls.add(this.ucDq);
		
		ucDz = new UcTextBox(this);
		ucDz.setCName("地址");
		ucDz.setAllowEmpty(false);
		this.controls.add(this.ucDz);

		ucGddh = new UcTextBox(this);
		ucGddh.setCName("固定电话");
		ucGddh.setAllowEmpty(true);
		this.controls.add(this.ucGddh);

		ucYddh = new UcTextBox(this);
		ucYddh.setCName("移动电话");
		ucYddh.setAllowEmpty(true);
		this.controls.add(this.ucYddh);

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

	}
	
	@Override
	protected void setData(IHsLabelValue data)
	{
		this.setDJId(data.getValue("Khbh","-1").toString());
		this.ucKhmc.setControlValue(data.getValue("Khmc","").toString());
		this.ucDq.SetData(data.getValue("Dqbh","").toString(),data.getValue("Dqmc","").toString());
		this.ucDz.setControlValue(data.getValue("Dz","").toString());
		this.ucGddh.setControlValue(data.getValue("Gddh","").toString());
		this.ucYddh.setControlValue(data.getValue("Yddh","").toString());
		
	}
	
	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		return ((JbbossWebService)application.getWebService()).updateJbKhzd(
				application.getLoginData().getProgressId(),
				this.getDJId(),
				this.ucKhmc.getControlValue(),
				ucDq.getControlValue(),
				ucDz.getControlValue(),
				ucGddh.getControlValue(),
				ucYddh.getControlValue(),
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

			Activity_DJ_JbKhzd activity = (Activity_DJ_JbKhzd) getActivity();

			UcFormItem f_khmc = new UcFormItem(context, activity.ucKhmc);
			
			UcFormItem f_dq = new UcFormItem(context, activity.ucDq);
			
			UcFormItem f_dz = new UcFormItem(context, activity.ucDz);
			
			UcFormItem f_gddh = new UcFormItem(context, activity.ucGddh);

			UcFormItem f_yddh = new UcFormItem(context, activity.ucYddh);

			mainView.addView(f_khmc.GetView());
			mainView.addView(f_dq.GetView());
			mainView.addView(f_dz.GetView());
			mainView.addView(f_gddh.GetView());
			mainView.addView(f_yddh.GetView());
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
