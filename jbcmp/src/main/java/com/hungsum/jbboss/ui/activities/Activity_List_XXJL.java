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
import com.hungsum.jbboss.webservices.JbcmpWebService;

public class Activity_List_XXJL extends HsActivity_List_DJ
{
	public Activity_List_XXJL()
	{
		this.mIsShowRetrieveCondition = true;
		
		this.setConditionSwitchTitle("是否提交");
		this.setConditionSwitchDatas("0,否;1,是");
		this.setConditionSwitchDefaultValues("0,1");
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
		menu.addSubMenu(0,R.string.str_delete, 2, "删除").setIcon(R.drawable.content_remove);
		
	}
	

	@Override
	protected HsWSReturnObject retrieve() throws Exception
	{
		return ((JbcmpWebService)application.getWebService()).ShowXxjls(
				application.getLoginData().getProgressId(),
				getBeginDateValue(),getEndDateValue());
	}
	

	@Override
	protected HsWSReturnObject deleteItem(IHsLabelValue item) throws Exception
	{
		return ((JbcmpWebService)application.getWebService()).doDatas(
				application.getLoginData().getProgressId(),
				getBhs(item, "Id"),
				"Delete_Xxjl");
	}

	@Override
	protected void addItem() throws Exception
	{
		Intent intent = new Intent(this, Activity_DJ_XXJL.class);

		startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);

	}
	
	@Override
	protected void modifyItem(IHsLabelValue item) throws Exception
	{
		Intent intent = new Intent(this, Activity_DJ_XXJL.class);
		intent.putExtra("Title",this.getTitle());
		intent.putExtra("Data", item);
		//intent.putExtra("AuditOnly", true);
		
		startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if(funcname.equals("ShowXxjls"))
		{
			ArrayList<IHsLabelValue> items = new ModelHsLabelValues().Create(HsGZip.DecompressString(data.toString()));
			this.ucListView.setAdapter(getAdapter(items));
		}else if(funcname.equals("DoDatas_Delete_Xxjl"))
		{
			ShowInformation(data.toString());
			callRetrieveOnOtherThread(false);
		}else 
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}
	
}
