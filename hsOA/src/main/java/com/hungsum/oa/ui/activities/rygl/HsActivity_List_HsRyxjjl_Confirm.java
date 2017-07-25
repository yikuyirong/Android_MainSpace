package com.hungsum.oa.ui.activities.rygl;

import java.util.ArrayList;
import java.util.List;

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
import com.hungsum.framework.webservices.HsWebService;
import com.hungsum.oa.R;
import com.hungsum.oa.R.drawable;

public class HsActivity_List_HsRyxjjl_Confirm extends HsActivity_List_HsRyxjjl_Base
{
	public HsActivity_List_HsRyxjjl_Confirm()
	{
		super();

		this.setConditionSwitchDatas("1,提交;2,确认");
		
		this.setConditionSwitchDefaultValues("1,2");
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

				if(jlzt.equals("1")) //提交
				{
					rightButtonDatas.add(String.format("%s,%s,%s,%s",R.string.str_modify,"查看",Color.WHITE, drawable.slider_button_modify_selector));
					
					leftButtonDatas.add(String.format("%s,%s,%s,%s",R.string.str_confirm,"确认",Color.WHITE, drawable.slider_button_confirm_selector));
				}else //确认 
				{
					rightButtonDatas.add(String.format("%s,%s,%s,%s",R.string.str_unconfirm,"取消确认",Color.WHITE, drawable.slider_button_confirm_selector));
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
		if(jlzt.equals("1")) //提交
		{
			menu.addSubMenu(0,R.string.str_modify, 1, "修改").setIcon(R.drawable.content_edit);
			menu.addSubMenu(0,R.string.str_submit, 2, "提交");
		}else //确认
		{
			menu.addSubMenu(0,R.string.str_modify, 1, "查看").setIcon(R.drawable.content_edit);
			menu.addSubMenu(0,R.string.str_unsubmit, 2, "取消提交");
		}
		
	}

	@Override
	protected HsWSReturnObject confirmItem(IHsLabelValue item) throws Exception
	{
		return ((HsWebService)application.getWebService()).doDatas(
				application.getLoginData().getProgressId(),
				getBhs(item, "JlId"),
				"Confirm_HsRyxjjl");
	}
	
	@Override
	protected HsWSReturnObject unConfirmItem(IHsLabelValue item)
			throws Exception
	{
		return ((HsWebService)application.getWebService()).doDatas(
				application.getLoginData().getProgressId(),
				getBhs(item, "JlId"),
				"UnConfirm_HsRyxjjl");
	}
}
