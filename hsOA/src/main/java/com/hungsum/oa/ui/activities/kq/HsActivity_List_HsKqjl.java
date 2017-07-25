package com.hungsum.oa.ui.activities.kq;

import android.view.Menu;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.activities.HsActivity_List_DJ;
import com.hungsum.oa.R;
import com.hungsum.oa.webservices.HSOAWebService;

public class HsActivity_List_HsKqjl extends HsActivity_List_DJ
{
	public HsActivity_List_HsKqjl()
	{
		super();

		this.mIsShowRetrieveCondition = true;

		this.setConditionBeginDateVisible(true);

		this.setConditionEndDateVisible(true);

		this.setConditionSwithVisible(false);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		menu.removeItem(R.string.str_new);

		return true;
	}

	@Override
	protected void modifyItem(IHsLabelValue item) throws Exception
	{
		//super.modifyItem(item);
	}

	@Override
	protected HsWSReturnObject retrieve() throws Exception
	{
		return ((HSOAWebService) application.getWebService()).getHsKqjls(
				application.getLoginData().getProgressId(),
				this.getBeginDateValue(), this.getEndDateValue());
	}

}
