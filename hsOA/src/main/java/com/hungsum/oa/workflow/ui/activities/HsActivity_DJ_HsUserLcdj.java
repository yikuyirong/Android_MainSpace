package com.hungsum.oa.workflow.ui.activities;

import java.io.Serializable;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.hungsum.framework.componments.HsQueryConditionItem;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.enums.EYesNo;
import com.hungsum.framework.interfaces.IControlValue;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.controls.UcChooseSingleBase;
import com.hungsum.framework.ui.controls.UcDateBox;
import com.hungsum.framework.ui.controls.UcFormItem;
import com.hungsum.framework.ui.fragments.HsFragment_DJ_Image;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;
import com.hungsum.oa.componments.HsUserLcdjmb;
import com.hungsum.oa.componments.HsUserLcdjmbmx;
import com.hungsum.oa.others.HSOADjlx;
import com.hungsum.oa.webservices.HSOAWebService;

public class HsActivity_DJ_HsUserLcdj extends HsActivity_DJ
{
	private UcDateBox ucDjrq;
	
	private HsUserLcdjmb inDjmb;
	
	private UcControls userControls = new UcControls();

	private boolean mOpenInHslc = false;
	
	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);
		
		this.setTitleSaved(inDjmb.Mbmc);

		DJParams params = new DJParams(false, inDjmb.HasImage > 0 , false);
		params.setImagePageAllowEmpty(inDjmb.HasImage < 2);

		this.setDJParams(params);

		this.mAnnexClass = HSOADjlx.HSLCDJMB + "_" + inDjmb.MbId;

		this.ucDjrq = new UcDateBox(this);
		this.ucDjrq.setCName("单据日期");
		this.ucDjrq.setAllowEmpty(false);
		this.controls.add(this.ucDjrq);

	}
	
	@Override
	protected void initInComingVariable(Bundle bundle) throws Exception
	{
		super.initInComingVariable(bundle);
		
		if(bundle.containsKey("Djmb"))
		{
			this.inDjmb = (HsUserLcdjmb) bundle.getSerializable("Djmb");
		}
	}

	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new MainFragment();
	}

	@Override
	protected HsFragment_DJ_Image createDJImageFragment(String imageClass)
	{
		HsFragment_DJ_Image f = new HsFragment_DJ_Image();
		f.setAnnexClass(imageClass);
		return f;
	}
	
	@Override
	protected void newData()
	{
		super.newData();
		
		this.ucDjrq.SetFlag("Now");

	}

	@Override
	protected void setData(IHsLabelValue data)
	{
		//单据Id
		this.setDJId(data.getValue("DjId","-1").toString());

		//单据日期
		this.ucDjrq.setControlValue(data.getValue("Djrq","1900-01-01").toString());
		
		if(!this.getAuditOnly() && this.mOpenInHslc) //设置图像与文件在流程审批中的可编辑状态。
		{
			if(mImageFragment != null)
			{
				mImageFragment.setAllowEdit(this.inDjmb.SfxgImage == EYesNo.Yes.value());
			}
			
			if(mFileFragment != null)
			{
				mFileFragment.setAllowEdit(this.inDjmb.SfxgFile == EYesNo.Yes.value());
			}
		}
		

		try
		{
			for (IHsLabelValue i : data.getDetails())
			{
				for (IControlValue control : userControls)
				{
					if(control.getControlId().equals(i.getLabel()))
					{
						control.setControlValue(i.getValue().toString());

						if(this.getAuditOnly())
						{
							control.setAllowEdit(false);
						}else
						{
							if(mOpenInHslc)
							{
								if(i.getValue("Sfxg",EYesNo.No.toString()).equals(EYesNo.Yes.toString()))
								{
									control.setAllowEdit(true);
								}else {
									control.setAllowEdit(false);
								}
								
							}else {
								control.setAllowEdit(true);
							}
						}
					}
				}
			}
			
		} catch (Exception e)
		{
			ShowError(e.getMessage());
		}
	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		StringBuffer djzy = new StringBuffer();

		StringBuffer xData = new StringBuffer();

		for(IControlValue control : this.userControls)
		{
			if(control instanceof UcChooseSingleBase)
			{
				djzy.append(String.format("【%s】%s ; ",control.getCName(),((UcChooseSingleBase) control).getControlTitle()));
				xData.append(String.format("<%s>%s,%s</%s>",control.getControlId(),control.getControlValue(),((UcChooseSingleBase) control).getControlTitle(),control.getControlId()));
			}else {
				djzy.append(String.format("【%s】%s ; ",control.getCName(),control.getControlValue()));
				xData.append(String.format("<%s>%s</%s>",control.getControlId(),control.getControlValue(),control.getControlId()));
			}
		}

		return ((HSOAWebService)application.getWebService()).updateHsUserLcdj(
				application.getLoginData().getProgressId(),
				this.getDJId(),
				this.inDjmb.MbId,
				this.ucDjrq.getControlValue().toString(),
				djzy.toString(),
				xData.toString(),
				this.mIsNewRecorder);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("UpdateHsUserLcdj"))
		{
			this.setDJId(data.toString());
			
			this.updateAnnex();

		}else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}


	private class MainFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
			UcFormItem f_djrq = new UcFormItem(context,ucDjrq);
			
			mainView.addView(f_djrq.GetView());
			
			if(inDjmb != null)
			{
				for (HsUserLcdjmbmx mx : inDjmb.Mxs)
				{
					HsQueryConditionItem v = new HsQueryConditionItem(context, 
							mx.Title, 
							"",
							mx.Class, 
							mx.ClassInfo, 
							mx.ClassParams, 
							mx.CanNull == 1, 
							mx.Default);
					IControlValue control = v.GetControl();
					control.setControlId(mx.Name);
					control.setCName(mx.Title);
					controls.add(control);
					userControls.add(control);
					mainView.addView(v.GetView());
				}
			}
		}
	}
}
