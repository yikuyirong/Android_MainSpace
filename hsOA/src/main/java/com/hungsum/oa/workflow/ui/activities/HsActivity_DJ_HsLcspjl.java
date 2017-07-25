package com.hungsum.oa.workflow.ui.activities;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.enums.EBase;
import com.hungsum.framework.enums.EDjlx;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.controls.UcFormItem;
import com.hungsum.framework.ui.controls.UcMultiRadio;
import com.hungsum.framework.ui.controls.UcTextBox;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;
import com.hungsum.framework.utils.HsGZip;
import com.hungsum.oa.componments.HsLcbzfz;
import com.hungsum.oa.enums.EHsLcBzlx;
import com.hungsum.oa.enums.ELcJlzt;
import com.hungsum.oa.enums.ELcStyle;
import com.hungsum.oa.models.ModelHsLcbzfzs;
import com.hungsum.oa.webservices.HSOAWebService;

public class HsActivity_DJ_HsLcspjl extends HsActivity_DJ
{
	private boolean mHasNextBz = false;

	private UcTextBox ucBzmc;

	private UcTextBox ucDwmc;
	
	private UcTextBox ucRoleMc;
	
	private UcTextBox ucZdr;

	private UcTextBox ucSpyj;

	private UcMultiRadio ucJlzt;

	//{{通过bundle传入的数据

	private EBase inLcStyle;

	//}}

	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.setTitleSaved(String.format("【%s】审批记录", this.inData.getValue("Bzmc", "")));

		DJParams params = new DJParams(false, true, false);
		params.setImagePageTitle("审批图片影印件");
		params.setImagePageAllowEmpty(true);
		this.setDJParams(params);
		this.mAnnexClass = EDjlx.SysLcspjl.toString();

		this.ucBzmc = new UcTextBox(this);
		this.ucBzmc.setCName("步骤名称");
		this.ucBzmc.setAllowEmpty(false);
		this.ucBzmc.setAllowEdit(false);
		this.controls.add(this.ucBzmc);

		this.ucDwmc = new UcTextBox(this);
		this.ucDwmc.setCName("部门");
		this.ucDwmc.setAllowEmpty(false);
		this.ucDwmc.setAllowEdit(false);
		this.controls.add(this.ucDwmc);

		this.ucRoleMc = new UcTextBox(this);
		this.ucRoleMc.setCName("角色");
		this.ucRoleMc.setAllowEmpty(false);
		this.ucRoleMc.setAllowEdit(false);
		this.controls.add(this.ucRoleMc);

		this.ucZdr = new UcTextBox(this);
		this.ucZdr.setCName("审批人");
		this.ucZdr.setAllowEmpty(false);
		this.ucZdr.setAllowEdit(false);
		this.controls.add(this.ucZdr);

		this.ucSpyj = new UcTextBox(this);
		this.ucSpyj.setCName("审批意见");
		this.ucSpyj.setAllowEmpty(true);
		this.controls.add(this.ucSpyj);

		this.ucJlzt = new UcMultiRadio(this);
		this.ucJlzt.SetDatas("0,待审批;1,同意;2,驳回",ELcJlzt.待审批.toString());
		this.ucJlzt.setCName("审批状态");
		this.ucJlzt.setAllowEmpty(false);
		this.controls.add(this.ucJlzt);

	}

	@Override
	protected void initInComingVariable(Bundle bundle) throws Exception
	{
		super.initInComingVariable(bundle);

		if(bundle.containsKey("LcStyle"))
		{
			this.inLcStyle = (EBase) this.bundle.getSerializable("LcStyle");
		}
	}

	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new LcspjlFragment();
	}
	
	@Override
	protected void setData(IHsLabelValue data)
	{
		this.setDJId(data.getValue("JlId", "-1").toString());
		
		this.ucBzmc.setControlValue(data.getValue("Bzmc", "").toString());
		this.ucBzmc.setAllowEdit(false);
		
		this.ucDwmc.setControlValue(data.getValue("Dwmc","").toString());
		this.ucDwmc.setAllowEdit(false);
		
		this.ucRoleMc.setControlValue(data.getValue("Rolemc", "").toString());
		this.ucRoleMc.setAllowEdit(false);
		
		this.ucZdr.setControlValue(data.getValue("Zdr", "").toString());
		this.ucZdr.setAllowEdit(false);
		
		this.ucSpyj.setControlValue(data.getValue("Spyj", "").toString());
		
		if(data.getValue("Bzlx", "0").equals(EHsLcBzlx.通知确认类.toString()))
		{
			this.ucJlzt.setControlValue(ELcJlzt.审批同意.toString());
			((View)this.ucJlzt.getParent()).setVisibility(View.GONE);
		}else {
			this.ucJlzt.setControlValue(data.getValue("Jlzt", ELcJlzt.审批同意.toString()).toString());
		}

		if(mImageFragment != null)
		{
			mImageFragment.setAnnexClassId(this.getDJId());
		}
		
		if(mFileFragment != null)
		{
			mImageFragment.setAnnexClassId(this.getDJId());
		}
	}
	
	@Override
	protected void validate() throws Exception
	{
		super.validate();
		
		if(this.ucJlzt.getControlValue().equals(ELcJlzt.待审批.toString()))
		{
			throw new Exception("请进行审批");
		}
	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		//如果是规则流程，已经存在下一步流程或驳回的流程则直接保存审批记录
		if(this.inLcStyle.equals(ELcStyle.规则流程) || mHasNextBz || ucJlzt.getControlValue().equals(ELcJlzt.审批驳回.toString()))
		{
			return ((HSOAWebService)application.getWebService()).updateHsLcspjl(
					application.getLoginData().getProgressId(),
					this.getDJId(),
					this.ucSpyj.getControlValue().toString(),
					this.ucJlzt.getControlValue().toString());
			
		}else
		{
			return ((HSOAWebService)application.getWebService()).showHsLcbzfzs(
					application.getLoginData().getProgressId(),
					this.getDJId());
		}
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == HsActivity_DJ.REQUESTCODE_ITEM)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				mHasNextBz = true;
				callUpdateOnOtherThread();
			}else {
				super.onActivityResult(requestCode, resultCode, data);
			}
		}else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	
	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("UpdateHsLcspjl"))
		{
			this.setDJId(data.toString());
			
			this.updateAnnex();

		}else if(funcname.equals("ShowHsLcbzfzs"))
		{
			ArrayList<HsLcbzfz> lcbzfzs = new ModelHsLcbzfzs().Create(HsGZip.DecompressString(data.toString()));
			if(lcbzfzs.size() == 0) //还没有定义分支
			{
				Intent intent = new Intent(this,HsActivity_DJ_HsLcbz.class);
				intent.putExtra("JlId", this.getDJId());

				startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);
			}else {
				mHasNextBz = true;
				callUpdateOnOtherThread(); //再次调用更新
			}
		}
		else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}


	private class LcspjlFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
			UcFormItem f_bzmc = new UcFormItem(context, ucBzmc);
			
			UcFormItem f_dwmc = new UcFormItem(context, ucDwmc);
			
			UcFormItem f_roleName = new UcFormItem(context, ucRoleMc);
			
			UcFormItem f_zdr = new UcFormItem(context, ucZdr);
			
			UcFormItem f_spyj = new UcFormItem(context, ucSpyj);
			
			UcFormItem f_jlzt = new UcFormItem(context, ucJlzt);

			mainView.addView(f_bzmc.GetView());
			mainView.addView(f_dwmc.GetView());
			mainView.addView(f_roleName.GetView());
			mainView.addView(f_zdr.GetView());
			mainView.addView(f_spyj.GetView());
			mainView.addView(f_jlzt.GetView());
		}
		
	}
}
