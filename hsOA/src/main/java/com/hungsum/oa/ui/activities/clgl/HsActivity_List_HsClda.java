package com.hungsum.oa.ui.activities.clgl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.activities.HsActivity_List_DJ;
import com.hungsum.framework.webservices.HsWebService;
import com.hungsum.oa.R;
import com.hungsum.oa.R.drawable;
import com.hungsum.oa.webservices.HSOAWebService;

public class HsActivity_List_HsClda extends HsActivity_List_DJ
{
	public HsActivity_List_HsClda()
	{
		super();
		
		this.mIsShowRetrieveCondition = true;
		
		this.setConditionBeginDateVisible(false);

		this.setConditionEndDateVisible(false);
		
		this.setConditionSwitchDatas("0,在用;1,闲置;2,出借;100,处置;101,报废");
		
		this.setConditionSwitchDefaultValues("0,1,2");
	}
	
	
	@Override
	protected void initActivityView()
	{
		super.initActivityView();
		
		List<String> buttonDatas = new ArrayList<String>();

		buttonDatas.add(String.format("%s,%s,%s,%s",R.string.str_delete,"删除",Color.WHITE, drawable.slider_button_delete_selector));
		
		//
		this.ucListView.setLeftSliderButtonDatas(buttonDatas);
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
		return ((HSOAWebService)application.getWebService()).showHsCldas(
				application.getLoginData().getProgressId(),getSwitchValues());
	}

	@Override
	protected HsWSReturnObject deleteItem(IHsLabelValue item) throws Exception
	{
		return ((HsWebService)application.getWebService()).doDatas(
				application.getLoginData().getProgressId(),
				getBhs(item, "RyId"),
				"Delete_HsClda");
	}

	@Override
	protected void addItem() throws Exception
	{
		Intent intent = new Intent(this,HsActivity_DJ_HsClda.class);

		startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);

	}
	
	@Override
	protected void modifyItem(IHsLabelValue item) throws Exception
	{
		Intent intent = new Intent(this, HsActivity_DJ_HsClda.class);
		intent.putExtra("Title",this.getTitle());
		intent.putExtra("Data", item);

		startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if(funcname.equals("DoDatas_Delete_HsClda"))
		{
			ShowInformation(data.toString());
			callRetrieveOnOtherThread(false);
		}else //查询
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}
	
}
