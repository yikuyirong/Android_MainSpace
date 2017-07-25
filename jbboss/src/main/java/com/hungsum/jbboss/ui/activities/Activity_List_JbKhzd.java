package com.hungsum.jbboss.ui.activities;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;

import com.hungsum.framework.R;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.models.ModelHsLabelValues;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.activities.HsActivity_List_DJ;
import com.hungsum.framework.utils.HsGZip;
import com.hungsum.jbboss.componments.JbSnyLoginData;
import com.hungsum.jbboss.webservices.JbbossWebService;

public class Activity_List_JbKhzd extends HsActivity_List_DJ
{
	public Activity_List_JbKhzd()
	{
		this.mIsShowRetrieveCondition = false;

	}
	
	/* 
	 * 创建上下文菜单
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.addSubMenu(0,R.string.str_new, 0,"新增").setIcon(R.drawable.content_new);
		menu.addSubMenu(0,R.string.str_modify, 1, "修改").setIcon(R.drawable.content_edit);
		//menu.addSubMenu(0,R.string.str_delete, 2, "删除").setIcon(R.drawable.content_remove);
		
	}
	

	@Override
	protected HsWSReturnObject retrieve() throws Exception
	{
		JbSnyLoginData loginData = (JbSnyLoginData) application.getLoginData();

		return ((JbbossWebService)application.getWebService()).showJbKhzds(
				loginData.getProgressId(),
				loginData.getNzBh(),
				loginData.getUserbh(),
				"0");
	}
	


	@Override
	protected void addItem() throws Exception
	{
		Intent intent = new Intent(this, Activity_DJ_JbKhzd.class);

		startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);

	}
	
	@Override
	protected void modifyItem(IHsLabelValue item) throws Exception
	{
		Intent intent = new Intent(this, Activity_DJ_JbKhzd.class);
		intent.putExtra("Title",this.getTitle());
		intent.putExtra("Data", item);

		startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if(funcname.equals("ShowJbKhzds"))
		{
			ArrayList<IHsLabelValue> items = new ModelHsLabelValues().Create(HsGZip.DecompressString(data.toString()));
			this.ucListView.setAdapter(getAdapter(items));
		}else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}
	
}
