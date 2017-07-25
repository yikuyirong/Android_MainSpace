package com.hungsum.framework.ui.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.hungsum.framework.R;


public class HsActivity_ShowImages extends HsActivity
{
	private ArrayList<String> mImagesCachedPath;
	
	private ViewPager viewPager;
	
	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);
		
		this.inTitle = "图片浏览";

	}
	
	@Override
	protected void initInComingVariable(Bundle bundle) throws Exception
	{
		super.initInComingVariable(bundle);
		
		if(bundle.containsKey("ImagesCachedPath"))
		{
			mImagesCachedPath = bundle.getStringArrayList("ImagesCachedPath");
		}
	}
	
	@Override
	protected void initLayout()
	{
		viewPager = new ViewPager(this);
		viewPager.setBackgroundColor(Color.BLACK);
		viewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		
		setContentView(viewPager);
	}
	
	@Override
	protected void initActivityView()
	{
		super.initActivityView();
		
		viewPager.setAdapter(new SamplePagerAdapter(mImagesCachedPath));
		
		viewPager.setCurrentItem(bundle.getInt("Position"));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		// 拍照
		menu.add(0, R.string.str_share, 0,
				getString(R.string.str_share))
				.setIcon(android.R.drawable.ic_menu_share)
				.setShowAsActionFlags(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}

	/*
	 * 系统菜单回调函数
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.string.str_share)
		{
			try
			{
				int position = viewPager.getCurrentItem();
				shareMessage(Uri.fromFile(new File(mImagesCachedPath.get(position))));
			} catch (Exception e)
			{
				ShowError(e);
			}
			return true;
		} else
		{
			return super.onOptionsItemSelected(item);
		}
	}

	static class SamplePagerAdapter extends PagerAdapter 
	{

		private Uri[] mImagesUri;
		
		public SamplePagerAdapter(List<String> imageCachedPaths)
		{
			mImagesUri = new Uri[imageCachedPaths.size()];
			for(int i = 0;i< imageCachedPaths.size();i++)
			{
				mImagesUri[i] = Uri.parse(imageCachedPaths.get(i));
			}
		}
		
		@Override
		public int getCount() 
		{
			return mImagesUri.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) 
		{
			PhotoView photoView = new PhotoView(container.getContext());
			
			
			
			photoView.setImageURI(mImagesUri[position]);

			//photoView.setImageResource(sDrawables[position]);

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}
	
}
