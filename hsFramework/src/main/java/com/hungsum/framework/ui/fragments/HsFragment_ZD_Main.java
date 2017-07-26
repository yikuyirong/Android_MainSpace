package com.hungsum.framework.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hungsum.framework.R;

public abstract class HsFragment_ZD_Main extends HsFragment_ZD
{
	public HsFragment_ZD_Main()
	{
		super();
		
		Icon = R.drawable.collections_view_as_grid;
		
		Title = "基本信息";
		
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.hsactivity_zd,null,false);

		LinearLayout mainViewLayout = (LinearLayout) rootView.findViewById(R.id.container);

		initMainView(inflater.getContext(), mainViewLayout);
		
		mHasCreateCompleted = true;
		
		if(mListenerOfCreateCompletedEventListener != null)
		{
			mListenerOfCreateCompletedEventListener.action(savedInstanceState);
			
			removeOnCreateCompletedEventListener();
		}
		
		return rootView;
	}
	

	protected abstract void initMainView(Context context, LinearLayout mainView);

	
}
