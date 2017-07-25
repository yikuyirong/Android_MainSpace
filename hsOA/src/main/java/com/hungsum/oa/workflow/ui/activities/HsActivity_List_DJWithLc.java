package com.hungsum.oa.workflow.ui.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import com.hungsum.framework.models.ModelHsLabelValues;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.activities.HsActivity_List_DJ;
import com.hungsum.framework.utils.HsGZip;
import com.hungsum.oa.R;
import com.hungsum.oa.R.drawable;
import com.hungsum.oa.others.HSOADjlx;
import com.hungsum.oa.webservices.HSOAWebService;

public abstract class HsActivity_List_DJWithLc extends HsActivity_List_DJ
{
	protected boolean mAllowFreeLc = false;
	
	protected boolean mAllowRegularLc = false;
	
	protected String mDjlx;
	
	protected IHsLabelValue mSelectedItem = null;
	
	public HsActivity_List_DJWithLc()
	{
		super();
		
		this.mIsShowRetrieveCondition = true;

		this.setConditionSwitchDatas("0,未审批;1,正在审批;2,审批同意;3,审批驳回;4,用户终止");
		
		this.setConditionSwitchDefaultValues("0,1,2,3,4");
		
		mDjlx = getDjlx();
		
		mAllowRegularLc = getAllowRegularLc();
		
		mAllowFreeLc = getAllowFreeLc();
	}

	protected abstract String getDjlx();
	
	protected abstract boolean getAllowFreeLc();
	
	protected abstract boolean getAllowRegularLc();
	{
		
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
				
				String spzt = value.getValue("Spzt", "-1").toString();

				List<String> rightButtonDatas = new ArrayList<String>();
				
				List<String> leftButtonDatas = new ArrayList<String>();

				if(spzt.equals("0")) //未审批
				{
					rightButtonDatas.add(String.format("%s,%s,%s,%s",R.string.str_modify,"修改",Color.WHITE, drawable.slider_button_modify_selector));
					rightButtonDatas.add(String.format("%s,%s,%s,%s",R.string.str_delete,"删除",Color.WHITE, drawable.slider_button_delete_selector));
					
					if(mAllowRegularLc)
					{
						leftButtonDatas.add(String.format("%s,%s,%s,%s",R.string.str_startregularlc,"启动规则流程",Color.WHITE, drawable.slider_button_submit_selector));
					}
					
					if(mAllowFreeLc)
					{
						leftButtonDatas.add(String.format("%s,%s,%s,%s",R.string.str_startfreelc,"启动自由流程",Color.WHITE, drawable.slider_button_submit_selector));
					}
					
				}else {
					rightButtonDatas.add(String.format("%s,%s,%s,%s",R.string.str_modify,"查看",Color.WHITE, drawable.slider_button_modify_selector));

					leftButtonDatas.add(String.format("%s,%s,%s,%s",R.string.str_browse,"查看审批记录",Color.WHITE, drawable.slider_button_darkblue_blue_selector));
					
					if(spzt.equals("1")) //正在审批
					{
						leftButtonDatas.add(String.format("%s,%s,%s,%s",R.string.str_overlc,"终止流程",Color.WHITE, drawable.slider_button_darkred_red_selector));
					}
					
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

		String spzt = item.getValue("Spzt", "-1").toString();

		if(spzt.equals("0")) //未审批
		{
			menu.addSubMenu(0,R.string.str_modify, 1, "修改").setIcon(R.drawable.content_edit);
			menu.addSubMenu(0,R.string.str_delete, 2, "删除").setIcon(R.drawable.content_remove);
			
			if(mAllowRegularLc)
			{
				menu.addSubMenu(0,R.string.str_startregularlc, 3, "启动规则流程");
			}
			
			if(mAllowFreeLc)
			{
				menu.addSubMenu(0,R.string.str_startfreelc, 3, "启动自由流程");
			}
			
		}else {
			menu.addSubMenu(0,R.string.str_modify, 1, "查看");
			menu.addSubMenu(0,R.string.str_browse, 2, "查看审批记录");
			if(spzt.equals("1"))
			{
				menu.addSubMenu(0,R.string.str_overlc, 3, "终止流程");
			}
		}
		
	}

	@Override
	protected boolean doSomeThingByContextItemSelected(int actionId,
			IHsLabelValue item) throws Exception
	{
		if(mDjlx.equals(HSOADjlx.HSUSERLCDJ))
		{
			mDjlx = HSOADjlx.HSUSERLCDJ + "_" + item.getValue("MbId", "");
		}

		if(actionId == R.string.str_browse) //查看流程审批记录
		{
			Intent intent = new Intent(this,HsActivity_List_HsLcspjls.class);
			intent.putExtra("Title", String.format("单据【 %s】审批记录", item.getValue("DjId", "0")));
			intent.putExtra("Djlx",mDjlx);
			intent.putExtra("DjId",item.getValue("DjId", "").toString());
			startActivity(intent);
			
			return false;
		}else if(actionId == R.string.str_startregularlc)
		{
			this.mSelectedItem = item; //缓存当前的选中值。
			this.mIsNeedChooseNecessaryData = true;
			callRetrieveAfterInit();
			this.mIsNeedChooseNecessaryData = false;
			return false;
		}else if(actionId == R.string.str_startfreelc)
		{
			Intent intent = new Intent(this,HsActivity_DJ_HsLcbz.class);
			intent.putExtra("Jdlx", "1"); //中间步骤
			intent.putExtra("Djlx", mDjlx);
			intent.putExtra("DjId", item.getValue("DjId", "-1").toString());

			startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);
			
			return false;
		}
		else{
			return super.doSomeThingByContextItemSelected(actionId, item);
		}
	}
	
	@Override
	protected HsWSReturnObject doSomeThingOnOtherThreadByContextItemSelected(
			int actionId, IHsLabelValue item) throws Exception
	{
		if(actionId == R.string.str_overlc) 
		{
			return overLc(item);
		}else {
			return super.doSomeThingOnOtherThreadByContextItemSelected(actionId, item);
		}
	}
	
	/**
	 * 获取流程模版数据
	 */
	@Override
	protected HsWSReturnObject chooseNecessaryData() throws Exception
	{
		return ((HSOAWebService) application.getWebService()).getHsLcmbs(
				application.getLoginData().getProgressId(),
				mDjlx);
	}
	
	@Override
	protected void actionAfterChooseNecessaryData(final IHsLabelValue data)
	{
		
		try
		{
			if(this.mSelectedItem != null)
			{
				ShowWait("请稍候", "正在努力加载数据...");

				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							//启动规则流程
							HsWSReturnObject returnObject = ((HSOAWebService) application.getWebService()).startRegularHsLc(
									application.getLoginData().getProgressId(),
									data.getValue("MbId", "-1").toString(),
									mSelectedItem.getValue("DjId", "-1").toString());

							sendDataMessage(returnObject);
						} catch (Exception e)
						{
							sendErrorMessage(e.getMessage());
						}

					}
				}).start();
			}
		} catch (Exception e)
		{
			ShowError(e);
		}
	}

	/**
	 * 结束流程
	 * @param item
	 * @return
	 * @throws Exception
	 */
	protected HsWSReturnObject overLc(IHsLabelValue item) throws Exception
	{
		return ((HSOAWebService) application.getWebService()).overHsLc(
				application.getLoginData().getProgressId(),
				mDjlx,
				item.getValue("DjId", "-1").toString());
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == HsActivity_DJ.REQUESTCODE_ITEM)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				callRetrieveOnOtherThread(false);
			}else {
				super.onActivityResult(requestCode, resultCode, data);
			}
		}else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	
	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if(funcname.equals("StartRegularHsLc") ||
			funcname.equals("OverHsLc"))
			{
				ShowInformation(data.toString());
				callRetrieveOnOtherThread(false);
			}else if(funcname.equals("GetHsLcmbs"))
			{
				//可用的自定义单据模版
				ArrayList<IHsLabelValue> items = new ModelHsLabelValues().Create(HsGZip.DecompressString(data.toString()));

				actionAfterReturnChooseData(items,false);
			}else
			{
				super.actionAfterWSReturnData(funcname, data);
			}
	}
}
