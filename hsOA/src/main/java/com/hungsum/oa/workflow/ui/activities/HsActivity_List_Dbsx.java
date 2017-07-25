package com.hungsum.oa.workflow.ui.activities;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.hungsum.framework.adapter.HsUserLabelValueAdapter;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.enums.EBase;
import com.hungsum.framework.enums.EYesNo;
import com.hungsum.framework.events.CommEventListener;
import com.hungsum.framework.events.CommEventListener.EventCategory;
import com.hungsum.framework.events.CommEventObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.models.ModelHsLabelValue;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.activities.HsActivity_List_DJ;
import com.hungsum.framework.utils.HsGZip;
import com.hungsum.oa.R;
import com.hungsum.oa.R.drawable;
import com.hungsum.oa.componments.HsUserLcdjmb;
import com.hungsum.oa.enums.EHsLcSpzt;
import com.hungsum.oa.enums.ELcJlzt;
import com.hungsum.oa.enums.ELcStyle;
import com.hungsum.oa.models.ModelHsLcdjmb;
import com.hungsum.oa.others.HSOADjlx;
import com.hungsum.oa.webservices.HSOAWebService;

public abstract class HsActivity_List_Dbsx extends HsActivity_List_DJ
{
	protected boolean mCurrentSfxg;

	public HsActivity_List_Dbsx()
	{
		super();

		this.mIsShowRetrieveCondition = true;

		this.setConditionBeginDateVisible(true);

		this.setConditionEndDateVisible(true);

		this.setConditionSwithVisible(true);

		this.setConditionSwitchTitle("记录状态");

		this.setConditionSwitchDatas("0,待审批;1,审批同意;2,审批驳回");

		this.setConditionSwitchDefaultValues("0,1,2");
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
				int position = Integer.valueOf(object.GetData().toString());
				
				IHsLabelValue value = (IHsLabelValue)(((HsUserLabelValueAdapter)ucListView.getAdapter()).getItem(position));
				
				EBase jlzt = ELcJlzt.parseString(value.getValue("Jlzt", "0").toString());

				ArrayList<String> buttons_leftSlider = new ArrayList<String>();
				
				buttons_leftSlider.add(String.format("%s,%s,%s,%s",R.string.userdo1,"查看",Color.WHITE, drawable.slider_button_modify_selector));

				//待审批记录增加审批功能
				if(jlzt.value() == ELcJlzt.待审批.value())
				{
					buttons_leftSlider.add(String.format("%s,%s,%s,%s",R.string.userdo3,"审批",Color.WHITE, drawable.slider_button_darkgreen_green_selector));
				}

				ArrayList<String> buttons_rightSlider = new ArrayList<String>();
				
				buttons_rightSlider.add(String.format("%s,%s,%s,%s",R.string.str_browse,"审批记录",Color.WHITE, drawable.slider_button_darkblue_blue_selector));
				
				ucListView.setLeftSliderButtonDatas(buttons_leftSlider);
				ucListView.setRightSliderButtonDatas(buttons_rightSlider);
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
		
		IHsLabelValue item = (IHsLabelValue) this.ucListView.getAdapter().getItem(((AdapterContextMenuInfo)menuInfo).position);

		EBase jlzt = ELcJlzt.parseString(item.getValue("Jlzt", "0").toString());
		
		menu.addSubMenu(0,R.string.userdo1, 1, "查看");
		menu.addSubMenu(0,R.string.str_browse, 2, "审批记录");
	
		if(jlzt.value() == ELcJlzt.待审批.value())
		{
			menu.addSubMenu(0,R.string.userdo3, 3, "审批");
		}
	}

	@Override
	protected void modifyItem(IHsLabelValue item) throws Exception
	{
		if(item.getValue("Jlzt", "0").equals(ELcJlzt.待审批.toString()))
		{
			this.actionAfterMenuOrButtonClick(R.string.userdo3, item);
		}
	}

	@Override
	protected HsWSReturnObject retrieve() throws Exception
	{
		return  ((HSOAWebService)application.getWebService()).showDbsxs(
				application.getLoginData().getProgressId(),
				this.getBeginDateValue(),
				this.getEndDateValue(),
				this.getSwitchValues());
	}

	protected HsWSReturnObject getDJOnOtherThread(String djlx,String djId,Boolean sfxg) throws Exception
	{
		this.mCurrentSfxg = sfxg;

		if(djlx.equals(HSOADjlx.HSDJCSM)) //单据传送门
		{
			return ((HSOAWebService)application.getWebService()).getHsDJCsm(application.getLoginData().getProgressId(), djId);
			
		}else if(djlx.startsWith(HSOADjlx.HSUSERLCDJ)) //自定义流程单据
		{
			return ((HSOAWebService)application.getWebService()).getHsUserLcdj(application.getLoginData().getProgressId(), djId);
		}
		else if(djlx.equals(HSOADjlx.HSFYSPD)) //HS费用审批单
		{
			return ((HSOAWebService)application.getWebService()).getHsFyspd(application.getLoginData().getProgressId(), djId);
		}
		else if(djlx.equals(HSOADjlx.HSTYLCDJ)) //通用流程单据
		{
			return ((HSOAWebService)application.getWebService()).getHsTylcdj(application.getLoginData().getProgressId(), djId);
		}
		else {
			throw new Exception("未知的单据类型【" + djlx + "】");
		}
	}
	
	@Override
	protected boolean doSomeThingByContextItemSelected(int actionId,
			IHsLabelValue item) throws Exception
	{
		if(actionId == R.string.str_browse) //查看审批记录
		{
			Intent intent = new Intent(this,HsActivity_List_HsLcspjls.class);
			intent.putExtra("Title", String.format("流程【 %s】审批记录", item.getValue("LcId", "0")));
			intent.putExtra("Djlx",item.getValue("Djlx", "").toString());
			intent.putExtra("DjId",item.getValue("DjId", "").toString());
			startActivity(intent);
			
			return false;	
		}else if(actionId == R.string.userdo3) //审批
		{
			Intent intent = new Intent(this,HsActivity_DJ_HsLcspjl.class);
			intent.putExtra("Data", item);
			intent.putExtra("LcStyle",item.getValue("MbId", "0").equals("0") ? ELcStyle.自由流程 : ELcStyle.规则流程);
			startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);
		
			return false;
		}else {
			return super.doSomeThingByContextItemSelected(actionId, item);
		}
	}
	
	@Override
	protected HsWSReturnObject doSomeThingOnOtherThreadByContextItemSelected(
			int actionId, IHsLabelValue item) throws Exception
	{
		
		if(actionId == R.string.userdo1) //查看原始单据
		{
			if(item.getValue("Spzt", "0").equals(EHsLcSpzt.正在审批.toString()) && 
					item.getValue("Sfxg", EYesNo.No.toString()).equals(EYesNo.Yes.toString()))
			{
				return getDJOnOtherThread(item.getValue("Djlx", "").toString(), item.getValue("DjId", "-1").toString(),true);
			}else {
				return getDJOnOtherThread(item.getValue("Djlx", "").toString(), item.getValue("DjId", "-1").toString(),false);
				
			}
			
		}else {
			return super.doSomeThingOnOtherThreadByContextItemSelected(actionId, item);
		}
	}
	
	/**
	 * 打开自定义流程单据
	 * @param djmb
	 * @param item
	 * @param sfxg
	 */
	private void openHsUserLcdj(HsUserLcdjmb djmb,IHsLabelValue item,boolean sfxg)
	{
		Intent intent = new Intent(this, HsActivity_DJ_HsUserLcdj.class);
		intent.putExtra("Djmb",djmb);
		intent.putExtra("Data", item);
		if(!sfxg)
		{
			intent.putExtra("AuditOnly",true);
		}
		startActivityForResult(intent,HsActivity_DJ.REQUESTCODE_ITEM);
	}
	
	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		final IHsLabelValue item = new ModelHsLabelValue().Create(HsGZip.DecompressString(data.toString()));
		
		if(funcname.equals("GetHsDJCsm"))
		{
			this.getDJOnOtherThread(item.getValue("Djlx","").toString(),
									item.getValue("DjId", "-1").toString(),
									this.mCurrentSfxg);
			
		}else if(funcname.equals("GetHsUserLcdj"))
		{
			Object cacheObject = this.application.GetData(HSOADjlx.HSLCDJMB + "_" + item.getValue("MbId","-1"));
			
			if(cacheObject != null && cacheObject instanceof HsUserLcdjmb)
			{
				HsUserLcdjmb djmb = (HsUserLcdjmb) cacheObject;
				
				this.openHsUserLcdj(djmb,item,this.mCurrentSfxg);
			}else 
			{
				mCurrentSelectedItem = item;

				ShowWait("请稍候", "正在努力加载数据...");

				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							HsWSReturnObject returnObject = ((HSOAWebService)application.getWebService()).getHsUserLcdjmb(application.getLoginData().getProgressId(),item.getValue("MbId","-1").toString());
							sendDataMessage(returnObject);
						} catch (Exception e)
						{
							sendErrorMessage(e.getMessage());
						}
					}
				}).start();
			}

		}else if(funcname.equals("GetHsUserLcdjmb"))
		{
			HsUserLcdjmb djmb = new ModelHsLcdjmb().Create(HsGZip.DecompressString(data.toString()));
			//加入单据模版到缓存中
			this.application.SetData(HSOADjlx.HSLCDJMB + "_" + djmb.MbId, djmb);
			this.openHsUserLcdj(djmb, this.mCurrentSelectedItem,this.mCurrentSfxg);
		}
		else if (funcname.equals("GetHsFyspd"))
		{
			Intent intent = new Intent(this,HsActivity_DJ_HsFyspd.class);
			intent.putExtra("Data", item);
			intent.putExtra("AuditOnly",true);
			startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);
		}else{
			super.actionAfterWSReturnData(funcname, data);
		}
	}
}
