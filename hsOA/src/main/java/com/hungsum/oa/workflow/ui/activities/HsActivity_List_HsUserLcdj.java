package com.hungsum.oa.workflow.ui.activities;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Intent;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.models.ModelHsLabelValues;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.utils.HsGZip;
import com.hungsum.framework.webservices.HsWebService;
import com.hungsum.oa.componments.HsUserLcdjmb;
import com.hungsum.oa.models.ModelHsLcdjmb;
import com.hungsum.oa.others.HSOADjlx;
import com.hungsum.oa.webservices.HSOAWebService;

public class HsActivity_List_HsUserLcdj extends HsActivity_List_DJWithLc
{

	@Override
	protected HsWSReturnObject retrieve() throws Exception
	{
		return ((HSOAWebService)application.getWebService()).showHsUserLcdjs(
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
				getBhs(item, "DjId"),
				"Delete_HsUserLcdj");
	}

	@Override
	protected void addItem() throws Exception
	{
		try
		{
			this.mSelectedItem = null; //清除当前选择数据
			
			ShowWait("请稍候", "正在努力加载数据...");

			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						HsWSReturnObject returnObject = ((HSOAWebService)application.getWebService()).getHsUserLcdjmbs(application.getLoginData().getProgressId());
						sendDataMessage(returnObject);
					} catch (Exception e)
					{
						sendErrorMessage(e.getMessage());
					}

				}
			}).start();
		} catch (Exception e)
		{
			ShowError(e);
		}
	}

	@Override
	protected void actionAfterChooseNecessaryData(final IHsLabelValue data)
	{
		//获取单据模板信息
		
		Object object = this.application.GetData(HSOADjlx.HSLCDJMB + "_" + data.getValue("MbId", "0"));
		
		if(object != null && object instanceof HsUserLcdjmb) //如果存在本地缓存
		{
			HsUserLcdjmb djmb = (HsUserLcdjmb)object;
		
			this.openDJIntent(djmb);
			
		}else {
			try
			{
				ShowWait("请稍候", "正在努力加载数据...");

				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							HsWSReturnObject object = ((HSOAWebService)application.getWebService()).getHsUserLcdjmb(
									application.getLoginData().getProgressId(),
									data.getValue("MbId","-1").toString());
							sendDataMessage(object);
						} catch (Exception e)
						{
							sendErrorMessage(e.getMessage());
						}

					}
				}).start();
			} catch (Exception e)
			{
				ShowError(e);
			}
		}
	}

	@Override
	protected void modifyItem(IHsLabelValue item) throws Exception
	{
		mSelectedItem = item;
		
		this.actionAfterChooseNecessaryData(item);
	}
	
	private void openDJIntent(HsUserLcdjmb djmb)
	{
		Intent intent = new Intent(this, HsActivity_DJ_HsUserLcdj.class);
		intent.putExtra("Title",this.getTitle());
		intent.putExtra("Djmb", djmb);
		intent.putExtra("Data", mSelectedItem);
		if(mSelectedItem != null && !mSelectedItem.getValue("Spzt", "0").equals("0"))
		{
			intent.putExtra("AuditOnly", true);
		}
		startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if(funcname.equals("DoDatas_Delete_HsUserLcdj"))
		{
			ShowInformation(data.toString());
			callRetrieveOnOtherThread(false);
		}else if(funcname.equals("GetHsUserLcdjmbs")) //
		{
			//可用的自定义单据模版
			ArrayList<IHsLabelValue> items = new ModelHsLabelValues().Create(HsGZip.DecompressString(data.toString()));

			actionAfterReturnChooseData(items,false);
		}else if(funcname.equals("GetHsUserLcdjmb"))
		{
			//格式化模板信息
			String value = HsGZip.DecompressString(data.toString());
		 	HsUserLcdjmb djmb = new ModelHsLcdjmb().Create(value);
			
		 	//缓存模板信息
		 	this.application.SetData(HSOADjlx.HSLCDJMB + "_" + djmb.MbId, djmb);

		 	this.openDJIntent(djmb);
		 	
		}
		else //查询
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}


	@Override
	protected String getDjlx()
	{
		return HSOADjlx.HSUSERLCDJ;
	}

	@Override
	protected boolean getAllowFreeLc()
	{
		return true;
	}

	@Override
	protected boolean getAllowRegularLc()
	{
		return true;
	}
	
}
