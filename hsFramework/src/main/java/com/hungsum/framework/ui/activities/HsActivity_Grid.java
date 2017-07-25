package com.hungsum.framework.ui.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.hungsum.framework.R;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.ui.controls.UcGridView;

public abstract class HsActivity_Grid<T extends Serializable> extends HsActivity implements OnItemClickListener
{
	protected UcGridView ucGridView;
	
	protected ArrayAdapter<T> adapter;

	protected List<T> items;

	@Override
	protected void initLayout()
	{
		setContentView(R.layout.hsactivity_grid);
		
		ucGridView = (UcGridView) findViewById(R.id.ucGridView2);

	}

	@Override
	protected void initActivityView()
	{
		super.initActivityView();

		if(items != null)
		{
			this.ucGridView.setAdapter(this.createAdapter(items));
		}else
		{
			items = new ArrayList<T>();
			this.callRetrieveOnOtherThread();
		}
	}

	@Override
	protected void initEvent()
	{
		super.initEvent();
		
		this.ucGridView.setOnItemClickListener(this);
		
	}

	protected void callRetrieveOnOtherThread()
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
	}
	
	protected abstract HsWSReturnObject retrieve() throws Exception;

	protected abstract BaseAdapter createAdapter(List<T> items);

}
