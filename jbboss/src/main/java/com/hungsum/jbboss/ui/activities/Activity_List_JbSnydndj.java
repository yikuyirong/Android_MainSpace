package com.hungsum.jbboss.ui.activities;

import android.content.Intent;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.activities.HsActivity_List_DJ;
import com.hungsum.jbboss.webservices.JbbossWebService;

public class Activity_List_JbSnydndj extends HsActivity_List_DJ
{

	@Override
	protected HsWSReturnObject retrieve() throws Exception
	{
		return ((JbbossWebService)application.getWebService()).showJbSnydndjs(
				application.getLoginData().getProgressId(),
				getBeginDateValue(),
				getEndDateValue(),
				getSwitchValues());
	}

	@Override
	protected void modifyItem(IHsLabelValue item) throws Exception
	{
		Intent intent = new Intent(this, Activity_DJ_JbSnydndj.class);
		intent.putExtra("Title",this.getTitle());
		intent.putExtra("Data", item);
		intent.putExtra("AuditOnly", true);

		startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);
	}
}
