package com.hungsum.oa.ui.activities.rygl;

import java.io.Serializable;

import android.content.Intent;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.activities.HsActivity_List_DJ;
import com.hungsum.oa.webservices.HSOAWebService;

public class HsActivity_List_HsRyxjjl_Base extends HsActivity_List_DJ
{
	public HsActivity_List_HsRyxjjl_Base()
	{
		super();
		
		this.mIsShowRetrieveCondition = true;

	}
	
	

	@Override
	protected HsWSReturnObject retrieve() throws Exception
	{
		return ((HSOAWebService)application.getWebService()).showHsRyxjjls(
				application.getLoginData().getProgressId(),
				getBeginDateValue(),
				getEndDateValue(),
				getSwitchValues());
	}

	@Override
	protected void modifyItem(IHsLabelValue item) throws Exception
	{
		Intent intent = new Intent(this, HsActivity_DJ_HsRyxjjl.class);
		intent.putExtra("Title",this.getTitle());
		intent.putExtra("Data", item);
		if(!item.getValue("Jlzt", "0").equals("0"))
		{
			intent.putExtra("AuditOnly", true);
		}
		

		startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if(funcname.equals("DoDatas_Delete_HsRyxjjl") ||
		   funcname.equals("DoDatas_Submit_HsRyxjjl") ||
		   funcname.equals("DoDatas_UnSubmit_HsRyxjjl") ||
		   funcname.equals("DoDatas_Confirm_HsRyxjjl") ||
		   funcname.equals("DoDatas_UnConfirm_HsRyxjjl"))
		{
			ShowInformation(data.toString());
			callRetrieveOnOtherThread(false);
		}else //查询
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}
	
}
