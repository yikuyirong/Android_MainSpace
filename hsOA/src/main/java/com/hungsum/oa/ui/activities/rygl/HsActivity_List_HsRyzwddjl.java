package com.hungsum.oa.ui.activities.rygl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.hungsum.framework.adapter.HsUserLabelValueAdapter;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.events.CommEventListener;
import com.hungsum.framework.events.CommEventListener.EventCategory;
import com.hungsum.framework.events.CommEventObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.activities.HsActivity_List_DJ;
import com.hungsum.framework.webservices.HsWebService;
import com.hungsum.oa.R;
import com.hungsum.oa.R.drawable;
import com.hungsum.oa.webservices.HSOAWebService;

public class HsActivity_List_HsRyzwddjl extends HsActivity_List_DJ
{
	public HsActivity_List_HsRyzwddjl()
	{
		super();
		
		this.mIsShowRetrieveCondition = true;

		this.setConditionSwitchDatas("0,未提交;1,提交");
		
		this.setConditionSwitchDefaultValues("0,1");
	}
	
	
	@Override
	protected void initActivityView()
	{
		super.initActivityView();
		
		//根据选择条目确定滑动菜单
		ucListView.setOnSliderListener(new CommEventListener(EventCategory.None)
		{
			@Override
			public void EventHandler(CommEventObject object)
			{
				int position = Integer.valueOf(object.GetData().toString());
				
				IHsLabelValue value = (IHsLabelValue)(((HsUserLabelValueAdapter)ucListView.getAdapter()).getItem(position));
				
				String jlzt = value.getValue("Jlzt", "-1").toString();

				List<String> rightButtonDatas = new ArrayList<String>();
				
				List<String> leftButtonDatas = new ArrayList<String>();

				if(jlzt.equals("0")) //未提交
				{
					rightButtonDatas.add(String.format("%s,%s,%s,%s",R.string.str_modify,"修改",Color.WHITE, drawable.slider_button_modify_selector));
					rightButtonDatas.add(String.format("%s,%s,%s,%s",R.string.str_delete,"删除",Color.WHITE, drawable.slider_button_delete_selector));
					
					leftButtonDatas.add(String.format("%s,%s,%s,%s",R.string.str_submit,"提交",Color.WHITE, drawable.slider_button_submit_selector));
				}else {
					rightButtonDatas.add(String.format("%s,%s,%s,%s",R.string.str_modify,"查看",Color.WHITE, drawable.slider_button_modify_selector));
					rightButtonDatas.add(String.format("%s,%s,%s,%s",R.string.str_unsubmit,"取消提交",Color.WHITE, drawable.slider_button_submit_selector));
				}

				ucListView.setLeftSliderButtonDatas(rightButtonDatas);
				ucListView.setRightSliderButtonDatas(leftButtonDatas);
			}
		});
	}

	/* 
	 * 创建上下文菜单
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);

		IHsLabelValue item = (IHsLabelValue) this.ucListView.getAdapter().getItem(((AdapterContextMenuInfo)menuInfo).position);

		String jlzt = item.getValue("Jlzt", "-1").toString();

		menu.addSubMenu(0,R.string.str_new, 0,"新增").setIcon(R.drawable.content_new);
		if(jlzt.equals("0")) //未提交
		{
			menu.addSubMenu(0,R.string.str_modify, 1, "修改").setIcon(R.drawable.content_edit);
			menu.addSubMenu(0,R.string.str_delete, 2, "删除").setIcon(R.drawable.content_remove);
			menu.addSubMenu(0,R.string.str_submit, 2, "提交");
		}else {
			menu.addSubMenu(0,R.string.str_modify, 1, "查看").setIcon(R.drawable.content_edit);
			menu.addSubMenu(0,R.string.str_unsubmit, 2, "取消提交");
		}
		
	}

	@Override
	protected HsWSReturnObject retrieve() throws Exception
	{
		return ((HSOAWebService)application.getWebService()).showHsRyzwddjls(
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
				"Delete_HsRyzwddjl");
	}
	
	@Override
	protected HsWSReturnObject submitItem(IHsLabelValue item) throws Exception
	{
		return ((HsWebService)application.getWebService()).doDatas(
				application.getLoginData().getProgressId(),
				getBhs(item, "JlId"),
				"Submit_HsRyzwddjl");
	}
	
	@Override
	protected HsWSReturnObject unSubmitItem(IHsLabelValue item)
			throws Exception
	{
		return ((HsWebService)application.getWebService()).doDatas(
				application.getLoginData().getProgressId(),
				getBhs(item, "JlId"),
				"UnSubmit_HsRyzwddjl");
	}

	@Override
	protected void addItem() throws Exception
	{
		Intent intent = new Intent(this,HsActivity_DJ_HsRyzwddjl.class);

		startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);

	}
	
	@Override
	protected void modifyItem(IHsLabelValue item) throws Exception
	{
		Intent intent = new Intent(this, HsActivity_DJ_HsRyzwddjl.class);
		intent.putExtra("Title",this.getTitle());
		intent.putExtra("Data", item);
		if(item.getValue("Jlzt", "0").equals("1"))
		{
			intent.putExtra("AuditOnly", true);
		}
		

		startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if(funcname.equals("DoDatas_Delete_HsRyzwddjl") ||
		   funcname.equals("DoDatas_Submit_HsRyzwddjl") ||
		   funcname.equals("DoDatas_UnSubmit_HsRyzwddjl"))
		{
			ShowInformation(data.toString());
			callRetrieveOnOtherThread(false);
		}else //查询
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}
	
}
