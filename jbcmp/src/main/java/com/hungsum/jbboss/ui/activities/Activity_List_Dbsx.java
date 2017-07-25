package com.hungsum.jbboss.ui.activities;

import java.io.Serializable;

import android.content.Intent;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.models.ModelHsLabelValue;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.utils.HsGZip;
import com.hungsum.jbboss.others.JbcmpDjlx;
import com.hungsum.jbboss.webservices.JbcmpWebService;
import com.hungsum.oa.workflow.ui.activities.HsActivity_List_Dbsx;

public class Activity_List_Dbsx extends HsActivity_List_Dbsx
{
	@Override
	protected HsWSReturnObject getDJOnOtherThread(String djlx, String djId,
			Boolean sfxg) throws Exception
	{
		this.mCurrentSfxg = sfxg;

		if (djlx.equals(JbcmpDjlx.JBCGSPD))
		{
			return ((JbcmpWebService)this.application.getWebService()).getJbCgspd(application.getLoginData().getProgressId(), djId);
		}else
		{
			return super.getDJOnOtherThread(djlx, djId, sfxg);
		}
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		IHsLabelValue item = new ModelHsLabelValue().Create(HsGZip.DecompressString(data.toString()));
		
		if (funcname.equals("GetJbCgspd"))
		{
			Intent intent = new Intent(this,Activity_DJ_JbCgspd.class);
			intent.putExtra("Data", item);
			intent.putExtra("AuditOnly",!this.mCurrentSfxg);
			startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);
		}else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}

}
