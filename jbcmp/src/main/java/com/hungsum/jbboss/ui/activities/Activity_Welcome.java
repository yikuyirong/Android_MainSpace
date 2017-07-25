package com.hungsum.jbboss.ui.activities;

import android.app.ActionBar;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hungsum.framework.ui.activities.HsActivity_Welcome;
import com.hungsum.jbboss.R;

public class Activity_Welcome extends HsActivity_Welcome
{
	@Override
	protected void initActionbar(ActionBar bar)
	{
		super.initActionbar(bar);
		
		bar.hide();
	}

	@Override
	protected void initLayout()
	{
		this.setContentView(R.layout.activity_welcome);
	}


	@Override
	protected ProgressBar getProgressBar()
	{
		return (ProgressBar) findViewById(R.id.ucProgressBar);
	}

	@Override
	protected TextView getMessage()
	{
		return (TextView) findViewById(R.id.ucMessage);
	}

	@Override
	protected TextView getVersionNumber()
	{
		return (TextView) findViewById(R.id.ucVersionNumber);
	}

}
