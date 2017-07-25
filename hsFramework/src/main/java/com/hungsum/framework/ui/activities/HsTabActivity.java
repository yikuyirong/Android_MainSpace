package com.hungsum.framework.ui.activities;

import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.hungsum.framework.R;
import com.hungsum.framework.adapter.UcFragmentPagerAdapter;
import com.hungsum.framework.ui.fragments.HsFragment;

public abstract class HsTabActivity extends HsActivity
{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	protected UcFragmentPagerAdapter mFragmentPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	protected ViewPager ucViewPager;

	/**
	 * 初始化布局，利用此函数构建页面布局
	 */
	protected void initLayout() 
	{
		setContentView(R.layout.control_viewpager);
		
		//createPagerAdapter();
	}

	protected void initActionbar(ActionBar bar)
	{
		super.initActionbar(bar);

		createPagerAdapter();

		if(mFragmentPagerAdapter.getCount() > 1)
		{
			bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); //actionbar的样式设置为选项卡式
		}
	}

	/**
	 * 初始化视图，在此函数中关联视图对象到成员变量
	 */
	protected void initActivityView() 
	{

		ucViewPager = (ViewPager)findViewById(R.id.pager1);

		ucViewPager.removeAllViews();

		ucViewPager.setAdapter(mFragmentPagerAdapter);
		
		mFragmentPagerAdapter.notifyDataSetChanged();

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		ucViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
				{
					@Override
					public void onPageSelected(int position)
					{
						actionBar.setSelectedNavigationItem(position);
					}
				});
		
		// For each of the sections in the app, add a tab to the action bar.
		actionBar.removeAllTabs();
		
		for (int i = 0; i < mFragmentPagerAdapter.getCount(); i++)
		{
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			
			CharSequence title = mFragmentPagerAdapter.getPageTitle(i);
			Drawable icon = mFragmentPagerAdapter.getPageIconResId(i) == 0 ? null : getResources().getDrawable(mFragmentPagerAdapter.getPageIconResId(i));
			
			actionBar.addTab(actionBar.newTab()
					.setText(title)
					.setIcon(icon)
					.setTabListener(lisener));
		}
	}
	
	
	/**
	 * ActionBar 侦听器
	 */
	ActionBar.TabListener lisener = new ActionBar.TabListener()
	{
		
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft)
		{
		}
		
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft)
		{
			ucViewPager.setCurrentItem(tab.getPosition());
			
			HsFragment currentFragment = (HsFragment) ((UcFragmentPagerAdapter)ucViewPager.getAdapter()).getItem(tab.getPosition());
			
			currentFragment.onSelected();
		}
		
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft)
		{
		}
	};


	protected void createPagerAdapter()
	{

		mFragmentPagerAdapter = new UcFragmentPagerAdapter(getSupportFragmentManager(),getFragments(this.bundle));
	}

	/**
	 * 非常重要，子代中必须重载，生成分页Fragment
	 * @param bundle
	 * @return
	 */
	protected abstract List<HsFragment> getFragments(Bundle bundle);

}
