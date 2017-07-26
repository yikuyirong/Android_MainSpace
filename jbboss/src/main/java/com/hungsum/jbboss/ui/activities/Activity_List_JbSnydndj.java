package com.hungsum.jbboss.ui.activities;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.enums.EBase;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.models.ModelHsLabelValues;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.activities.HsActivity_List_DJ;
import com.hungsum.framework.utils.HsGZip;
import com.hungsum.framework.webservices.HsWebService;
import com.hungsum.jbboss.componments.JbSnyLoginData;
import com.hungsum.jbboss.webservices.JbbossWebService;
import com.hungsum.oa.enums.ELcJlzt;

import java.io.Serializable;
import java.util.ArrayList;

public class Activity_List_JbSnydndj extends HsActivity_List_DJ
{
	public Activity_List_JbSnydndj()
	{
		this.mIsShowRetrieveCondition = true;

		this.setConditionSwitchDatas("0,未提交;1,提交");

		this.setConditionSwitchDefaultValues("0,1");
	}

	/*
	 * 创建上下文菜单
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
									ContextMenu.ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);

		IHsLabelValue item = (IHsLabelValue) this.ucListView.getAdapter().getItem(((AdapterView.AdapterContextMenuInfo)menuInfo).position);

		menu.addSubMenu(0, com.hungsum.framework.R.string.str_new, 0,"新增").setIcon(com.hungsum.framework.R.drawable.content_new);

		if(item.getValue("Djzt","0").toString().equals("0")) //未提交
		{
			menu.addSubMenu(0, com.hungsum.framework.R.string.str_modify, 1, "修改");
			menu.addSubMenu(0, com.hungsum.framework.R.string.str_delete, 2, "删除");
			menu.addSubMenu(0, com.hungsum.framework.R.string.str_submit, 2, "提交");
		}else //提交
		{
			menu.addSubMenu(0, com.hungsum.framework.R.string.str_modify, 1, "查看");
			menu.addSubMenu(0, com.hungsum.framework.R.string.str_print, 2, "打印");
		}
	}

	@Override
	protected void addItem() throws Exception
	{
		Intent intent = new Intent(this, Activity_DJ_JbSnydndj.class);

		startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);
	}

	@Override
	protected void modifyItem(IHsLabelValue item) throws Exception
	{
		Intent intent = new Intent(this, Activity_DJ_JbSnydndj.class);
		intent.putExtra("Title",this.getTitle());
		intent.putExtra("Data", item);

		if(item.getValue("Djzt","0") == "1")
		{
			intent.putExtra("AuditOnly", true);
		}

		startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);
	}

	@Override
	protected HsWSReturnObject deleteItem(IHsLabelValue item) throws Exception
	{
		return ((HsWebService)application.getWebService()).doDatas(
				application.getLoginData().getProgressId(),
				getBhs(item, "DjId"),
				"Delete_JbSnydndj");
	}

	@Override
	protected String getActionPromptString(int actionId)
	{
		if (actionId == com.hungsum.framework.R.string.str_submit)
		{
			return "提交之后不能修改、删除，提交之后才能打印，是否提交？";
		}else
		{
			return super.getActionPromptString(actionId);
		}
	}

	@Override
	protected HsWSReturnObject submitItem(IHsLabelValue item) throws Exception
	{
		return ((HsWebService)application.getWebService()).doDatas(
				application.getLoginData().getProgressId(),
				getBhs(item, "DjId"),
				"Submit_JbSnydndj");
	}

	@Override
	protected void actionAfterMenuOrButtonClick(int actionId, IHsLabelValue object) throws Exception
	{
		if(actionId == com.hungsum.framework.R.string.str_print)
		{
			ShowInformation("调用打印。");
		}else
		{
			super.actionAfterMenuOrButtonClick(actionId, object);
		}
	}

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
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if(funcname.equals("ShowJbSnydndjs")) {
			ArrayList<IHsLabelValue> items = new ModelHsLabelValues().Create(HsGZip.DecompressString(data.toString()));
			this.ucListView.setAdapter(getAdapter(items));
		}else if(funcname.equals("DoDatas_Delete_JbSnydndj") ||
					funcname.equals("DoDatas_Submit_JbSnydndj"))
		{
				ShowInformation(data.toString());
				callRetrieveOnOtherThread(false);
		}else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}

}
