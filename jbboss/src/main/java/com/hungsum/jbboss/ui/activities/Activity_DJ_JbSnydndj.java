package com.hungsum.jbboss.ui.activities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.hungsum.framework.adapter.HsUserLabelValueAdapter;
import com.hungsum.framework.componments.HsLabelValue;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.events.CommEventListener;
import com.hungsum.framework.events.CommEventObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.controls.UcChooseSingleItem;
import com.hungsum.framework.ui.controls.UcDateBox;
import com.hungsum.framework.ui.controls.UcFormItem;
import com.hungsum.framework.ui.controls.UcNumericInput;
import com.hungsum.framework.ui.controls.UcTextBox;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Detail;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;
import com.hungsum.framework.utils.HsDate;
import com.hungsum.framework.utils.HsRound;
import com.hungsum.jbboss.componments.JbSnyLoginData;
import com.hungsum.jbboss.others.JbbossDjlx;
import com.hungsum.jbboss.webservices.JbbossWebService;

public class Activity_DJ_JbSnydndj extends HsActivity_DJ
{

    private UcChooseSingleItem ucKh;

    private UcDateBox ucKsrq;

	private UcDateBox ucJsrq;

	/**
	 * 备注
	 */
	private UcTextBox ucBz;

	// {{通过bundle传入的数据

	// }}

	private CommEventListener jeChangeEventListener = new CommEventListener(CommEventListener.EventCategory.DataChanged) {
		@Override
		public void EventHandler(CommEventObject object)
		{
			if(mDetailFragemnt != null)
			{
				mDetailFragemnt.setSummaryText();
			}
		}
	};

	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.setTitleSaved(JbbossDjlx.JB订奶单据);

		DJParams params = new DJParams(true, false, false);
		params.setImagePageAllowEmpty(true);

		this.setDJParams(params);

		this.mAnnexClass = JbbossDjlx.JBDNDJ;

		JbSnyLoginData loginData = (JbSnyLoginData)application.getLoginData();

        this.ucKh = new UcChooseSingleItem(this);
        this.ucKh.SetFlag("JBBOSSKH");
		this.ucKh.SetParams("<Nzbh>" + loginData.getNzBh() + "</Nzbh><Rybh>" + loginData.getUserbh() + "</Rybh><Khlb>0</Khlb>");
        this.ucKh.setCName("客户信息");
        this.ucKh.setAllowEmpty(false);
        this.controls.add(this.ucKh);

        this.ucKsrq = new UcDateBox(this);
		this.ucKsrq.setCName("开始日期");
		this.ucKsrq.setAllowEmpty(false);
		this.controls.add(this.ucKsrq);
		this.ucKsrq.addOnDataChangedListener(jeChangeEventListener);

        this.ucJsrq = new UcDateBox(this);
        this.ucJsrq.setCName("结束日期");
        this.ucJsrq.setAllowEmpty(false);
        this.controls.add(this.ucJsrq);
		this.ucJsrq.addOnDataChangedListener(jeChangeEventListener);

        this.ucBz = new UcTextBox(this);
        this.ucBz.setCName("备注");
        this.ucBz.setAllowEmpty(true);
        this.controls.add(this.ucBz);

    }

	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new MainFragment();
	}
	
	@Override
	protected HsFragment_ZD_Detail createDJDetailFragment()
	{
		DetailFragment f = new DetailFragment();
		f.addOnDataChangedListener(jeChangeEventListener);
		return f;
	}

	private int getDnts()
	{
		long ksrq = ucKsrq.getControlDate().getTime();
		long jsrq = ucJsrq.getControlDate().getTime();

		if(ksrq > jsrq)
		{
			return 0;
		}else
		{
			return 1 + (int)((jsrq - ksrq) / (24 * 3600 * 1000));
		}
	}

	@Override
	protected void newData()
	{
		super.newData();

		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.MONTH,1);

		calendar.set(Calendar.DAY_OF_MONTH,1);

		ucKsrq.setControlDate(calendar.getTime());

		calendar.add(Calendar.MONTH,1);

		calendar.add(Calendar.DAY_OF_MONTH,-1);

		ucJsrq.setControlDate(calendar.getTime());
	}

	@Override
	protected void setData(IHsLabelValue data) throws Exception
	{
		this.setDJId(data.getValue("DjId", "-1").toString());
        this.ucKh.setControlValue(data.getValue("Khbh", "").toString() + "," + data.getValue("Khmc","").toString());
		this.ucKsrq.setControlValue(data.getValue("Ksrq", "").toString());
		this.ucJsrq.setControlValue(data.getValue("Jsrq", "").toString());
		this.ucBz.setControlValue(data.getValue("Bz", "").toString());

		//表体数据
		this.mDetailFragemnt.setControlValue(data.getValue("StrMx", "[]").toString(), "Label","Value");

	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		return ((JbbossWebService) application.getWebService()).updateJbSnydndjs(
				application.getLoginData().getProgressId(),
				this.getDJId(),
                this.ucKh.getControlValue().toString(),
				this.ucKsrq.getControlValue().toString(),
				this.ucJsrq.getControlValue().toString(),
				this.ucBz.getControlValue().toString(),
				this.mDetailFragemnt.getControlValue().toString(),
				this.mIsNewRecorder);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("UpdateJbSnydndjs"))
		{
			this.setDJId(data.toString());

			this.updateAnnex();

		} else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}

	public static class MainFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
            Activity_DJ_JbSnydndj activity = (Activity_DJ_JbSnydndj)getActivity();

            UcFormItem f_kh = new UcFormItem(context,activity.ucKh);

            UcFormItem f_ksrq = new UcFormItem(context, activity.ucKsrq);

			UcFormItem f_jsrq = new UcFormItem(context, activity.ucJsrq);

			UcFormItem f_bz = new UcFormItem(context, activity.ucBz);

			mainView.addView(f_kh.GetView());
			mainView.addView(f_ksrq.GetView());
            mainView.addView(f_jsrq.GetView());
			mainView.addView(f_bz.GetView());
		}

	}

	public static class DetailFragment extends HsFragment_ZD_Detail
	{

		public DetailFragment()
		{
			super();
			
			this.Title = "订单明细";
		}

		public Double getRje()
		{
			Double rje = 0.00;

			if (this.getAdapter() != null && this.getAdapter() instanceof HsUserLabelValueAdapter) {
				List<IHsLabelValue> items = ((HsUserLabelValueAdapter) this.getAdapter()).getAllItems();

				Double dj = 0.00;
				int sl = 0;

				for (IHsLabelValue item : items) {
					dj = Double.parseDouble(item.getValue("Dj", "0").toString());
					sl = Integer.parseInt(item.getValue("Sl", "0").toString());

					rje += dj * sl;

				}
			}

			return rje;
		}

		@Override
		public void setSummaryText()
		{
			if(mSummary != null)
			{
				Activity_DJ_JbSnydndj activity = (Activity_DJ_JbSnydndj) getActivity();

				int ts = activity.getDnts();

				mSummary.setText("共" + activity.getDnts() +"天，金额合计" + HsRound.Round(getRje() * activity.getDnts(),2) + "元");
			}
		}

		@Override
		protected void openDetailIntent(IHsLabelValue item)
		{
			Intent intent = new Intent(getActivity(), Activity_DJ_JbSnydndjmx.class);
			
			if(item != null)
			{
				intent.putExtra("Data",item);
			}
			
			if(!getAllowEdit())
			{
				intent.putExtra("AuditOnly", true);
			}

			startActivityForResult(intent, 0);

		}

		
		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data)
		{
			try
			{
				if(resultCode == Activity.RESULT_OK)
				{
					IHsLabelValue item = (IHsLabelValue) data.getSerializableExtra("Data");

					List<IHsLabelValue> items = ((HsUserLabelValueAdapter) this.getAdapter()).getAllItems();

					int index = -1;
					int sl = 0;

					for(int i = 0;i<items.size();i++)
					{
						if(items.get(i).getValue("Cpbh","").equals(item.getValue("Cpbh","")))
						{
							index = i;
							sl = Integer.parseInt(items.get(i).getValue("Sl","0").toString());
							break;
						}
					}

					if(index > 0)
					{
						removeItem(index);
					}

					if(sl > 0)
					{
						item.addDetail(new HsLabelValue("Sl",Integer.parseInt(item.getValue("Sl","0").toString()) + sl));
					}

					sl = Integer.parseInt(item.getValue("Sl","0").toString());

					double dj = Double.parseDouble(item.getValue("Dj","0").toString());

					double rje = HsRound.Round(sl * dj,2);

					item.setValue("数量：" + sl + " 单价：" + dj + " 单日金额：" + rje);

					addItem(item);

				}else {
					super.onActivityResult(requestCode, resultCode, data);
				}
			}catch (Exception e)
			{
				showError(e.getMessage());
			}

		}
	}
	
}
