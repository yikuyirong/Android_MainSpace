package com.hungsum.oa.workflow.ui.activities;

import java.io.Serializable;

import android.content.Intent;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.webservices.HsWebService;
import com.hungsum.oa.others.HSOADjlx;
import com.hungsum.oa.webservices.HSOAWebService;

public class HsActivity_List_HsFyspd extends HsActivity_List_DJWithLc
{

	@Override
	protected HsWSReturnObject retrieve() throws Exception
	{
		return ((HSOAWebService)application.getWebService()).showHsfyspds(
				application.getLoginData().getProgressId(),
				getBeginDateValue(),
				getEndDateValue(),
				getSwitchValues());
	}

	@Override
	protected HsWSReturnObject deleteItem(IHsLabelValue item) throws Exception
	{
		return ((HsWebService)application.getWebService()).doDatas(
				application.getLoginData().getProgressId(),
				getBhs(item, "JlId"),
				"Delete_HsFyspd");
	}

	@Override
	protected void addItem() throws Exception
	{
		Intent intent = new Intent(this,HsActivity_DJ_HsFyspd.class);

		startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);

	}
	
	@Override
	protected void modifyItem(IHsLabelValue item) throws Exception
	{
		Intent intent = new Intent(this, HsActivity_DJ_HsFyspd.class);
		intent.putExtra("Title",this.getTitle());
		intent.putExtra("Data", item);
		if(item.getValue("Spzt", "0").equals("1"))
		{
			intent.putExtra("AuditOnly", true);
		}

		startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if(funcname.equals("DoDatas_Delete_HsFyspd"))
		{
			ShowInformation(data.toString());
			callRetrieveOnOtherThread(false);
		}else //查询
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}


	@Override
	protected String getDjlx()
	{
		return HSOADjlx.HSFYSPD;
	}

	@Override
	protected boolean getAllowFreeLc()
	{
		return false;
	}

	@Override
	protected boolean getAllowRegularLc()
	{
		return true;
	}
	
}
