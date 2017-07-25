package com.hungsum.oa.workflow.ui.activities;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.enums.EYesNo;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.models.ModelHsDeptRoleUserNames;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.controls.UcChooseDeptRoleUserNames;
import com.hungsum.framework.ui.controls.UcChooseDeptRoleUserNames.RetrieveEventListener;
import com.hungsum.framework.ui.controls.UcFormItem;
import com.hungsum.framework.ui.controls.UcMultiRadio;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;
import com.hungsum.framework.utils.HsGZip;
import com.hungsum.framework.webservices.HsWebService;
import com.hungsum.oa.enums.EHsLcBzSpfs;
import com.hungsum.oa.enums.EHsLcBzlx;
import com.hungsum.oa.enums.EHsLcBzFzBhcl;
import com.hungsum.oa.webservices.HSOAWebService;

public class HsActivity_DJ_HsLcbz extends HsActivity_DJ
{
	public static boolean SHOW_SFJS = true;
	
	public static EYesNo VALUE_SFJS = EYesNo.No;
	
	public static boolean SHOW_BZLX = true;
	
	public static EHsLcBzlx VALUE_BZLX = EHsLcBzlx.同意审批类;
	
	public static boolean SHOW_TYFS = true;
	
	public static EHsLcBzSpfs VALUE_TYFS = EHsLcBzSpfs.至少一个;
	
	public static boolean SHOW_BHCL = true;
	
	public static EHsLcBzFzBhcl VALUE_BHCL = EHsLcBzFzBhcl.退回上一步;
	
	public static boolean SHOW_TYBM = true;
	
	public static EYesNo VALUE_TYBM = EYesNo.No;
	
	public static boolean SHOW_SFXG = true;
	
	public static EYesNo VALUE_SFXG = EYesNo.No;

	
	
	private UcMultiRadio ucSfjs;

	private UcMultiRadio ucBzlx;
	
	private UcMultiRadio ucTyfs;

	private UcMultiRadio ucBhcl;
	
	private UcMultiRadio ucTybm;

	private UcMultiRadio ucSfxg;

	private UcChooseDeptRoleUserNames ucChooseDeptRoleUserNames;
	
	//{{通过bundle传入的数据

	private String inSpjlId;

	private String inDjlx;
	
	private String inDjId;

	//}}

	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.setTitleSaved("指定步骤");

		this.ucSfjs = new UcMultiRadio(this);
		this.ucSfjs.SetDatas("0,否;1,是", "1");
		this.ucSfjs.setCName("下步结束");
		this.ucSfjs.setAllowEmpty(false);
		this.ucSfjs.setAllowEdit(true);
		this.controls.add(this.ucSfjs);

		this.ucBzlx = new UcMultiRadio(this);
		this.ucBzlx.SetDatas("0,同意审批类;1,通知确认类", "0");
		this.ucBzlx.setCName("步骤类型");
		this.ucBzlx.setAllowEmpty(false);
		this.controls.add(this.ucBzlx);

		this.ucTyfs = new UcMultiRadio(this);
		this.ucTyfs.SetDatas("0,至少一个;1,过半数;2,全部", "0");
		this.ucTyfs.setCName("同意条件");
		this.ucTyfs.setAllowEmpty(false);
		this.controls.add(this.ucTyfs);

		this.ucBhcl = new UcMultiRadio(this);
		this.ucBhcl.SetDatas("0,退回上一步骤;1,直接终止","0");
		this.ucBhcl.setCName("驳回处理");
		this.ucBhcl.setAllowEmpty(false);
		this.controls.add(this.ucBhcl);
		
		this.ucTybm = new UcMultiRadio(this);
		this.ucTybm.SetDatas("0,否;1,是", "0");
		this.ucTybm.setCName("同一部门");
		this.ucTybm.setAllowEmpty(false);
		this.controls.add(this.ucTybm);
		
		this.ucSfxg = new UcMultiRadio(this);
		this.ucSfxg.SetDatas("0,否;1,是", "0");
		this.ucSfxg.setCName("修改审批数据");
		this.ucSfxg.setAllowEmpty(false);
		this.controls.add(this.ucSfxg);

		this.ucChooseDeptRoleUserNames = new UcChooseDeptRoleUserNames(this);
		this.ucChooseDeptRoleUserNames.SetOnRetrieveEventListener(new RetrieveEventListener()
		{
			@Override
			public void Retrieve()
			{
				ShowWait("请稍候", "正在检索人员信息...");
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							HsWSReturnObject object = ((HsWebService)application.getWebService()).showSysDeptRoleUserName(application.getLoginData().getProgressId());
							sendDataMessage(object);
						} catch (Exception e)
						{
							sendErrorMessage(e.getMessage());
						}
					}
				}).start();
			}
		});
		this.ucChooseDeptRoleUserNames.setCName("部门角色人员");
		this.ucChooseDeptRoleUserNames.setAllowEmpty(false);
		this.controls.add(this.ucChooseDeptRoleUserNames);

	}

	@Override
	protected void initInComingVariable(Bundle bundle) throws Exception
	{
		super.initInComingVariable(bundle);

		//{{ 初始化传入变量

		if(bundle.containsKey("JlId"))
		{
			this.inSpjlId = bundle.getString("JlId");
		}

		if(bundle.containsKey("Djlx"))
		{
			this.inDjlx = bundle.getString("Djlx");
		}
		
		if(bundle.containsKey("DjId"))
		{
			this.inDjId = bundle.getString("DjId");
		}
		
		//}}
	}
	
	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new LcbzFragment();
	}
	
	protected void newData() 
	{
		this.ucSfjs.setControlValue(VALUE_SFJS.toString()); //初始状态为不结束
		
		this.ucBzlx.setControlValue(VALUE_BZLX.toString());

		this.ucTyfs.setControlValue(VALUE_TYFS.toString());

		this.ucBhcl.setControlValue(VALUE_BHCL.toString());
	};
	
	@Override
	protected void setData(IHsLabelValue data)
	{
		this.setDJId(data.getValue("BzId","-1").toString());

		this.ucSfjs.setControlValue(data.getValue("Sfjs", VALUE_SFJS.toString()).toString());

		this.ucBzlx.setControlValue(data.getValue("Bzlx", VALUE_BZLX.toString()).toString());

		this.ucTyfs.setControlValue(data.getValue("Tyfs", VALUE_TYFS.toString()).toString());

		this.ucBhcl.setControlValue(data.getValue("Bhcl", VALUE_BHCL.toString()).toString());

		this.ucSfxg.setControlValue(data.getValue("Sfxg", VALUE_SFXG.toString()).toString());
		
		this.ucChooseDeptRoleUserNames.setControlValue(data.getValue("DeptRoleUserName", "").toString());

		if(mImageFragment != null)
		{
			mImageFragment.setAnnexClassId(this.getDJId());
		}
	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		if(this.bundle.containsKey("JlId")) //自由流程的中间步骤，判断依据是存在记录ID
		{
			return  ((HSOAWebService)application.getWebService()).updateHsLcbz(
					application.getLoginData().getProgressId(),
					this.getDJId(),
					this.ucBzlx.getControlValue().toString(),
					this.ucTyfs.getControlValue().toString(),
					this.ucBhcl.getControlValue().toString(),
					this.ucTybm.getControlValue().toString(),
					this.ucSfxg.getControlValue().toString(),
					this.ucChooseDeptRoleUserNames.getControlValue().toString(),
					this.mIsNewRecorder,
					this.inSpjlId,
					this.ucSfjs.getControlValue().toString());
		}else
		{
			return  ((HSOAWebService)application.getWebService()).updateHsLcbz(
					application.getLoginData().getProgressId(),
					this.getDJId(),
					this.ucBzlx.getControlValue().toString(),
					this.ucTyfs.getControlValue().toString(),
					this.ucBhcl.getControlValue().toString(),
					this.ucTybm.getControlValue().toString(),
					this.ucSfxg.getControlValue().toString(),
					this.ucChooseDeptRoleUserNames.getControlValue().toString(),
					this.mIsNewRecorder,
					this.inDjlx,
					this.inDjId,
					this.ucSfjs.getControlValue().toString());
		}
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if(funcname.equals("ShowSysDeptRoleUserName"))
		{
			ArrayList<String> items = new ModelHsDeptRoleUserNames().Create(HsGZip.DecompressString(data.toString()));
			ucChooseDeptRoleUserNames.SetDatas(items);
		}
		else if (funcname.equals("UpdateHsLcbz"))
		{
			this.setDJId(data.toString());
			
			this.updateAnnex();

		}else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}


	private class LcbzFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
			if(SHOW_SFJS)
			{
				UcFormItem f_sfjs = new UcFormItem(context, ucSfjs);
				mainView.addView(f_sfjs.GetView());
			}
			
			if(SHOW_BZLX)
			{
				UcFormItem f_bzlx = new UcFormItem(context, ucBzlx);
				mainView.addView(f_bzlx.GetView());
			}
			
			if(SHOW_TYFS)
			{
				UcFormItem f_tyfs = new UcFormItem(context,ucTyfs);
				mainView.addView(f_tyfs.GetView());
			}

			if(SHOW_BHCL)
			{
				UcFormItem f_bhcl = new UcFormItem(context, ucBhcl);
				mainView.addView(f_bhcl.GetView());
			}

			if(SHOW_TYBM)
			{
				UcFormItem f_tybm = new UcFormItem(context, ucTybm);
				mainView.addView(f_tybm.GetView());
			}

			if(SHOW_SFXG)
			{
				UcFormItem f_sfxg = new UcFormItem(context,ucSfxg);
				mainView.addView(f_sfxg.GetView());
			}

			UcFormItem f_roleUsers = new UcFormItem(context, ucChooseDeptRoleUserNames);
			mainView.addView(f_roleUsers.GetView());

		}
		
	}
}
