package com.hungsum.framework.ui.controls;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.hungsum.framework.adapter.HsUserLabelValueAdapter;
import com.hungsum.framework.events.CommEventListener;
import com.hungsum.framework.events.CommEventObject;

public class UcListView extends ListView 
{
	private CommEventListener mOnSliderListener;

	public UcListView(Context context)
	{
		super(context);
		
		this.setOnTouchListener(listener);
	}

	public UcListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		this.setOnTouchListener(listener);
	}

	public UcListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		this.setOnTouchListener(listener);
	}
	
	public void setOnSliderListener(CommEventListener listener)
	{
		mOnSliderListener = listener;
	}
	
	private OnTouchListener listener = new OnTouchListener()
	{
        float x, y, ux, uy;  
        @SuppressLint("ClickableViewAccessibility")
		@Override  
        public boolean onTouch(View v, MotionEvent event) {  
            // TODO Auto-generated method stub  
            switch (event.getAction()) 
            {  
	            case MotionEvent.ACTION_DOWN:  
	                x = event.getX();  
	                y = event.getY();  
	                break;  
	            case MotionEvent.ACTION_UP:  
	                ux = event.getX();  
	                uy = event.getY();  
	                
	                int currentPosition = pointToPosition((int)x, (int)y);

	                if(currentPosition == -1) return false;
	                
	                if (currentPosition == pointToPosition((int)ux, (int)uy) && Math.abs(y - uy) < 50) 
	                {  
	                	if(x - ux > 24)
	                	{
	                		if(mOnSliderListener != null)
	                		{
	                			mOnSliderListener.EventHandler(new CommEventObject(this,currentPosition));
	                		}
	                		return createLeftSlider(currentPosition); //左滑
	                	}
	                	
	                	if(ux - x > 24)
	                	{
	                		if(mOnSliderListener != null)
	                		{
	                			mOnSliderListener.EventHandler(new CommEventObject(this,currentPosition));
	                		}
	                		return createRightSlider(currentPosition); //右滑
	                	}
	                }  
            }  
  
            return false;  
        }  
	};

	/**
	 * 左滑菜单按钮信息
	 */
	protected List<String> mLeftSliderButtonDatas;
	
	public void setLeftSliderButtonDatas(List<String> buttonDatas)
	{
		mLeftSliderButtonDatas = buttonDatas;
	}

	private boolean createLeftSlider(int position)
	{
		HsUserLabelValueAdapter adapter;
		
		if(getAdapter() instanceof HsUserLabelValueAdapter)
		{
			adapter = (HsUserLabelValueAdapter) getAdapter();
			
			adapter.onLeftSlider(position,mLeftSliderButtonDatas);
			
			adapter.notifyDataSetChanged(); //触发数据变化，刷新Listview视图
		}

		return false;
	}
	
	/**
	 * 右滑菜单按钮信息
	 */
	protected List<String> mRightSliderButtonDatas;

	public void setRightSliderButtonDatas(List<String> buttonDatas)
	{
		mRightSliderButtonDatas = buttonDatas;
	}
	
	private boolean createRightSlider(int position)
	{
		HsUserLabelValueAdapter adapter;
		
		if(getAdapter() instanceof HsUserLabelValueAdapter)
		{
			adapter = (HsUserLabelValueAdapter) getAdapter();
			
			adapter.onRightSlider(position,mRightSliderButtonDatas);
			
			adapter.notifyDataSetChanged(); //触发数据变化，刷新Listview视图
		}

		return false;
	}

	
	

}






