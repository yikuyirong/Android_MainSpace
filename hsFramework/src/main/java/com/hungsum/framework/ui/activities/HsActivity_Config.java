package com.hungsum.framework.ui.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.hungsum.framework.R;

public class HsActivity_Config extends HsActivity
{
	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);
		
		this.inTitle = "系统设置";
	}
	
	@Override
	protected void initLayout()
	{
		this.getWindow().setBackgroundDrawableResource(R.drawable.background_repeat);
		
		getFragmentManager().beginTransaction().replace(android.R.id.content,new ConfigFragment()).commit();
	}

	public static class ConfigFragment extends PreferenceFragment
	{
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			
			
			
			addPreferencesFromResource(R.xml.activity_config);
		}
		
	}
}
