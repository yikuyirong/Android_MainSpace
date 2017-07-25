package com.hungsum.framework.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hungsum.framework.ui.fragments.HsFragment;

public class UcFragmentPagerAdapter extends FragmentStatePagerAdapter
{
	protected List<HsFragment> mFragments;

	public UcFragmentPagerAdapter(FragmentManager fm,List<HsFragment> fragments)
	{
		super(fm);

		mFragments = fragments;

	}
	

	public UcFragmentPagerAdapter(FragmentManager fm)
	{
		super(fm);
	}

	@Override
	public Fragment getItem(int position)
	{
		return mFragments.get(position);
	}
	
	@Override
	public int getItemPosition(Object object)
	{
		return FragmentPagerAdapter.POSITION_NONE;
	}

	@Override
	public int getCount()
	{
		return mFragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		return ((HsFragment)mFragments.get(position)).Title;
	}

	public int getPageIconResId(int position)
	{
		HsFragment fm = (HsFragment)mFragments.get(position);

		return fm.Icon;
	}

}
