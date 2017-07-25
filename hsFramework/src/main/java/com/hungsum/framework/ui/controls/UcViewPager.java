package com.hungsum.framework.ui.controls;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/*
 *
 * 这个类是在网上摘抄的，原文有如下一段注释，应该是屏蔽掉某些异常。
 * 
 * Hacky fix for Issue #4 and
 * http://code.google.com/p/android/issues/detail?id=18990
 * <p/>
 * ScaleGestureDetector seems to mess up the touch events, which means that
 * ViewGroups which make use of onInterceptTouchEvent throw a lot of
 * IllegalArgumentException: pointerIndex out of range.
 * <p/>
 * There's not much I can do in my code for now, but we can mask the result by
 * just catching the problem and ignoring it.
 *
 * @author Chris Banes
 */
public class UcViewPager extends ViewPager
{

	public UcViewPager(Context context)
	{
		super(context);
	}

	public UcViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0)
	{
        try {
            return super.onInterceptTouchEvent(arg0);
        } catch (IllegalArgumentException e) 
        {
            return false;
        }
	}

}
