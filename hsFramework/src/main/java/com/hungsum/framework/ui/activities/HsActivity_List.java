package com.hungsum.framework.ui.activities;

import java.io.Serializable;
import java.util.List;

import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.ui.controls.UcListView;

public abstract class HsActivity_List<T extends Serializable> extends HsActivity implements OnItemClickListener
{
	protected UcListView ucListView;
	
	//protected BaseAdapter adapter;
	
	protected List<T> items;

	/**
	 * 指示是否显示刷新条件，真时列表刷新时会弹出窗口输入条件，假时则不显示，
	 */
	protected boolean mIsShowRetrieveCondition = false;

	@Override
	protected void initLayout()
	{
		ucListView = new UcListView(this);
		ucListView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));

		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
		ll.setBackgroundResource(getBackgroundResId());
		ll.addView(ucListView);

		setContentView(ll);
	}

	@Override
	protected void initActivityView()
	{
		super.initActivityView();

		if(items != null)
		{
			this.ucListView.setAdapter(this.getAdapter(items));
		}else {
			callRetrieveAfterInit();
		}
	}

	/**
	 * 初始化后调用调用列表刷新，如果在刷新之前需要初始化数据，请重新此方法，此方法工作在UI线程。
	 */
	protected void callRetrieveAfterInit()
	{
		this.callRetrieveOnOtherThread(false);
	}
	
	@Override
	protected void initEvent()
	{
		super.initEvent();
		
		this.ucListView.setOnItemClickListener(this);
		
	}

	/**
	 * 在工作线程调用刷新
	 * @param isShowConditionIfHas 是否显示刷新条件（如果有），例如更新单据后的刷新或列表初始化的刷新就不需要显示刷新条件。
	 */
	protected void callRetrieveOnOtherThread(boolean isShowConditionIfHas)
	{
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
						sendDataMessage(retrieve());
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
		finally
		{
			
		}
	}
	
	protected abstract HsWSReturnObject retrieve() throws Exception;

	protected abstract BaseAdapter getAdapter(List<T> items);

}


