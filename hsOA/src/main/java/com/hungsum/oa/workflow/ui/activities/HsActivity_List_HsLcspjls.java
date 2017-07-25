package com.hungsum.oa.workflow.ui.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.View;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.events.CommEventListener;
import com.hungsum.framework.events.CommEventListener.EventCategory;
import com.hungsum.framework.events.CommEventObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.activities.HsActivity_List_DJ;
import com.hungsum.oa.R;
import com.hungsum.oa.R.drawable;
import com.hungsum.oa.webservices.HSOAWebService;

public class HsActivity_List_HsLcspjls extends HsActivity_List_DJ
{
	private String inDjlx;
	
	private String inDjId;
	
	public HsActivity_List_HsLcspjls()
	{
		super();

		this.mIsShowRetrieveCondition = false;
	}

	@Override
	protected void initInComingVariable(Bundle bundle) throws Exception
	{
		super.initInComingVariable(bundle);
		
		if(bundle.containsKey("Djlx"))
		{
			this.inDjlx = bundle.getString("Djlx","");
		}
		
		if(bundle.containsKey("DjId"))
		{
			this.inDjId = bundle.getString("DjId", "");
		}
	}
	
	@Override
	protected void initActivityView()
	{
		super.initActivityView();
		
		ucListView.setOnSliderListener(new CommEventListener(EventCategory.None)
		{
			@Override
			public void EventHandler(CommEventObject object)
			{
				ArrayList<String> rightButtonDatas = new ArrayList<String>();

				rightButtonDatas.add(String.format("%s,%s,%s,%s",R.string.userdo1,"查看",Color.WHITE, drawable.slider_button_darkblue_blue_selector));

				ucListView.setLeftSliderButtonDatas(rightButtonDatas);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		menu.removeItem(R.string.str_new);

		return true;
	}

	/*
	 * 创建上下文菜单
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.addSubMenu(0,R.string.userdo1,0, "查看");

	}
	
	@Override
	protected void actionAfterMenuOrButtonClick(int actionId,
			IHsLabelValue object) throws Exception
	{
		if(actionId == R.string.userdo1)
		{
			this.modifyItem(object);
			
		}else {

			super.actionAfterMenuOrButtonClick(actionId, object);
			
		}
	}

	@Override
	protected void modifyItem(IHsLabelValue item) throws Exception
	{
		Intent intent = new Intent(this, HsActivity_DJ_HsLcspjl.class);
		intent.putExtra("Title",this.getTitle());
		intent.putExtra("Data", item);
		intent.putExtra("AuditOnly", true);

		startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);
	}

	@Override
	protected HsWSReturnObject retrieve() throws Exception
	{
		return  ((HSOAWebService)application.getWebService()).showHsLcspjls(
				application.getLoginData().getProgressId(),
				this.inDjlx,this.inDjId);
	}
}
