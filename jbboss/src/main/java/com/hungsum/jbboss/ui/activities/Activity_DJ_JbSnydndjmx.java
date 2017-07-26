package com.hungsum.jbboss.ui.activities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.hungsum.framework.componments.HsApplication;
import com.hungsum.framework.componments.HsLabelValue;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.events.CommEventListener;
import com.hungsum.framework.events.CommEventObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.models.ModelHsLabelValues;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.controls.UcChooseSingleItem;
import com.hungsum.framework.ui.controls.UcFormItem;
import com.hungsum.framework.ui.controls.UcNumericInput;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;
import com.hungsum.framework.utils.HsGZip;
import com.hungsum.framework.utils.HsRound;
import com.hungsum.jbboss.webservices.JbbossWebService;

public class Activity_DJ_JbSnydndjmx extends HsActivity_DJ
{
	private double Ccdj,Fwdj;

	private UcChooseSingleItem ucCp;

	private UcNumericInput ucDj;

	private UcNumericInput ucSl;

	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		DJParams params = new DJParams(false, false, false);

		//
		this.setDJParams(params);

		this.ucCp = new UcChooseSingleItem(this).SetFlag("JBBOSSCP");
		this.ucCp.setCName("产品");
		this.ucCp.setAllowEmpty(false);
		this.ucCp.addOnDataChangedListener(new CommEventListener(CommEventListener.EventCategory.DataChanged)
		{
			@Override
			public void EventHandler(CommEventObject object)
			{
				String cpbh = ucCp.getControlValue().toString();

				if(cpbh.equals(""))
				{
					Ccdj = Fwdj = 0.00;
					ucDj.setNumberValue(0.00);
				}else
				{
					//获取产品信息

					setControlDj(cpbh,null);
				}
			}
		});

		this.controls.add(this.ucCp);

		this.ucDj = new UcNumericInput(this);
		this.ucDj.setCName("单价");
		this.ucDj.setAllowEmpty(false);
		this.ucDj.setAllowEdit(false);
		this.controls.add(this.ucDj);

		this.ucSl = new UcNumericInput(this);
		this.ucSl.setCName("数量");
		this.ucSl.setAllowEmpty(false);
		this.controls.add(this.ucSl);

	}

	private void setControlDj(String cpbh, List<IHsLabelValue> items)
	{
		final HsApplication<?> application = (HsApplication<?>)getApplication();

		if(items != null)
		{
			application.SetData("JBBOSSCP",items); //缓存数据
		}

		List<IHsLabelValue> datas = (List<IHsLabelValue>) application.GetData("JBBOSSCP");

		if(datas != null)
		{
			for (IHsLabelValue item: datas)
			{
				if(item.getValue("Cpbh","").equals(cpbh))
				{
					this.Ccdj = Double.parseDouble(item.getValue("Ccdj","0").toString());
					this.Fwdj = Double.parseDouble(item.getValue("Fwdj","0").toString());

					this.ucDj.setNumberValue(HsRound.Round(this.Ccdj + this.Fwdj , 2));

					break;
				}
			}
		}else
		{
			//获取账套信息
			try
			{
				ShowWait("请稍候", "正在获取产品信息...");

				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							HsWSReturnObject returnObject = ((JbbossWebService)application.getWebService()).showJbCpzds(application.getLoginData().getProgressId());

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
	}

	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new MainFragment();
	}


	@Override
	protected void newData()
	{
		super.newData();

		this.ucDj.setAllowEdit(false);
	}

	@Override
	protected void setData(IHsLabelValue data) throws Exception
	{
		this.Ccdj = Double.parseDouble(data.getValue("Ccdj","0").toString());
		this.Fwdj = Double.parseDouble(data.getValue("Fwdj","0").toString());

		this.ucCp.setControlValue(data.getValue("Cpbh", "").toString() + "," + data.getValue("Cpmc", "").toString());

		this.ucDj.setNumberValue(HsRound.Round(this.Ccdj + this.Fwdj,2));

		this.ucDj.setAllowEdit(false);

		this.ucSl.setControlValue(data.getValue("Sl", "").toString());
	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		String key = this.ucCp.getControlValue().toString() + " " + this.ucCp.getControlTitle();
		
		String value = "数量：" + this.ucSl.getControlValue().toString() + " 单价：" + this.ucDj.getControlValue() + " 单日金额：" + HsRound.Round(this.ucSl.getNumberValue() * (this.Ccdj + this.Fwdj),2);
		
		IHsLabelValue keyValue = new HsLabelValue(key, value);

		keyValue.addDetail(new HsLabelValue("Cpbh", this.ucCp.getControlValue()));
		keyValue.addDetail(new HsLabelValue("Cpmc", this.ucCp.getControlTitle()));
		keyValue.addDetail(new HsLabelValue("Ccdj", this.Ccdj));
		keyValue.addDetail(new HsLabelValue("Fwdj", this.Fwdj));
		keyValue.addDetail(new HsLabelValue("Dj", HsRound.Round(this.Ccdj + this.Fwdj,2)));
		keyValue.addDetail(new HsLabelValue("Sl", this.ucSl.getControlValue()));

		HsWSReturnObject returnObject = new HsWSReturnObject();
		
		returnObject.SetCodeNum(0); //
		returnObject.SetFuncName("UpdateJbSnydndjmx");
		returnObject.SetData(keyValue);
		
		return returnObject;
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("UpdateJbSnydndjmx"))
		{
			Intent intent = new Intent();
			intent.putExtra("Data", data);
			this.setResult(Activity.RESULT_OK , intent);
			
			this.finish();
		}else if(funcname.equals("ShowJbCpzds"))
		{
			ArrayList<IHsLabelValue> items = new ModelHsLabelValues().Create(HsGZip.DecompressString(data.toString()));

			this.setControlDj(this.ucCp.getControlValue().toString(),items);
		}
		else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}

	public static class MainFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
			Activity_DJ_JbSnydndjmx activity = (Activity_DJ_JbSnydndjmx) getActivity();

			UcFormItem f_bm = new UcFormItem(context, activity.ucCp);

			UcFormItem f_dj = new UcFormItem(context, activity.ucDj);

			UcFormItem f_sl = new UcFormItem(context, activity.ucSl);

			mainView.addView(f_bm.GetView());
			mainView.addView(f_dj.GetView());
			mainView.addView(f_sl.GetView());

		}

	}
}
