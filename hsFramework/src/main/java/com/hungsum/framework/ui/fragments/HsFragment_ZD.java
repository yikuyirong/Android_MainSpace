package com.hungsum.framework.ui.fragments;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class HsFragment_ZD extends HsFragment
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		//允许Fragment菜单项显示在Activity上
		setHasOptionsMenu(true);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);

		//根据Fragment是否显示确定菜单是否显示。
		for(int i = 0;i< menu.size() ; i++)
		{
			MenuItem menuItem = menu.getItem(i);
			
			if(menuItem.getGroupId() == 1)
			{
				menuItem.setVisible(this.isVisible());
				
			}
		}
	}
}
