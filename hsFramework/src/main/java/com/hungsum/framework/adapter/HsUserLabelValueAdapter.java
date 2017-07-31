package com.hungsum.framework.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hungsum.framework.R;
import com.hungsum.framework.R.drawable;
import com.hungsum.framework.events.CommEventListener;
import com.hungsum.framework.events.CommEventObject;
import com.hungsum.framework.interfaces.IHsLabelValue;

public class HsUserLabelValueAdapter extends BaseAdapter implements Filterable,OnLongClickListener
{
	
	protected List<IHsLabelValue> mDatas;
	
	private List<IHsLabelValue> mOriginalValues;

	/*
	 * 布局扩展
	 */
	protected LayoutInflater mInflater;
	
	protected int mLayoutRes;
	
	private int mLeftSliderPostion = -1;
	
	private int mRightSliederPostion = -1;
	
	private List<String> mButtonDatas;
	
	private int mLeftContainerWidth = 0;
	
	private int mRightContainerWidth = 0;
	
	/**
	 * 适配器上显示按钮的侦听器
	 */
	private CommEventListener mOnButtonClickListener;

	public HsUserLabelValueAdapter(Context context,int layoutRes)
	{
		mInflater = LayoutInflater.from(context);
		
		mLayoutRes = layoutRes;
		
		mDatas = new ArrayList<IHsLabelValue>();
		
	}
	
	public HsUserLabelValueAdapter(Context context)
	{
		this(context, R.layout.control_list_keyvalue_2);
	}
	
	/**
	 * 自定义按钮侦听事件
	 * @param listener
	 */
	public void setOnButtonClickListener(CommEventListener listener)
	{
		mOnButtonClickListener = listener;
	}
	
	public void resetSliederPostion()
	{
		mLeftSliderPostion = -1;
		
		mRightSliederPostion = -1;
		
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount()
	{
		return mDatas.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mDatas.get(position);
	}
	
	public List<IHsLabelValue> getAllItems()
	{
		return mDatas;
	}

	public int getPosition(IHsLabelValue item)
	{
		for (int i = 0; i < mDatas.size() ; i++)
		{
			if(mDatas.get(i).equals(item))
			{
				return i;
			}
		}
		return -1;
	}

	public int getPostionByLabel(String label)
	{
		for (int i = 0; i < mDatas.size() ; i++)
		{
			if(mDatas.get(i).getLabel().equals(label))
			{
				return i;
			}
		}
		return -1;
	}
	
	public int getPostionByValue(String value)
	{
		for (int i = 0; i < mDatas.size() ; i++)
		{
			if(mDatas.get(i).getValue().equals(value))
			{
				return i;
			}
		}
		return -1;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	public String getShareMessage()
	{
		StringBuffer sb = new StringBuffer();
		for (IHsLabelValue item: mDatas)
		{
			sb.append("[" + item.getLabel() + "]:");
			sb.append(item.getValue());
		}
		return sb.toString();
	}
	
	public void clear()
	{
		mDatas.clear();
		notifyDataSetChanged();
	}

	public void add(IHsLabelValue value)
	{
		mDatas.add(value);
		notifyDataSetChanged();
	}
	
	public void add(List<IHsLabelValue> datas)
	{
		mDatas.clear();
		mDatas.addAll(datas);
		notifyDataSetChanged();
	}
	
	public void remove(int position)
	{
		mDatas.remove(position);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		IHsLabelValue value = (IHsLabelValue) getItem(position);

		ViewHolder holder;
		if(convertView == null)
		{
			convertView = mInflater.inflate(mLayoutRes, null);
			holder = new ViewHolder();
			holder.UcKey = (TextView) convertView.findViewById(R.id.list_keyvalue_ucKey);
			holder.UcValue = (TextView) convertView.findViewById(R.id.list_keyvalue_ucValue);
			holder.ucDetailImage = (ImageView) convertView.findViewById(R.id.list_keyvalue_ucImageDetail);
			holder.ucOperationImage = (ImageView) convertView.findViewById(R.id.list_keyvalue_ucOperationImage);
			holder.mLeftButtonContainer = (ViewGroup) convertView.findViewById(R.id.left_button_container);
			holder.mRightButtonContainer = (ViewGroup) convertView.findViewById(R.id.right_button_container);
			holder.mData = value;
			
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(holder.UcKey != null)
		{
			holder.UcKey.setText(value.getLabel());
			holder.UcKey.setTextColor(value.getColor());
			
		}
		if(holder.UcValue != null)
		{
			holder.UcValue.setText(value.getValue().toString());
		}
		
		if(holder.ucDetailImage != null)
		{
			holder.ucDetailImage.setVisibility(value.getIsShowDetailImage() && value.getDetailsCount() > 0 ? View.VISIBLE : View.INVISIBLE);
		}
		
		if(holder.ucOperationImage != null)
		{
			holder.ucOperationImage.setImageDrawable(value.getOperationImage());
		}
		
		//加入左滑功能
		if(holder.mRightButtonContainer != null)
		{
			mRightContainerWidth = holder.mRightButtonContainer.getWidth();

			ObjectAnimator hideAnimator = ObjectAnimator.ofInt(holder.mRightButtonContainer,"width",mRightContainerWidth,0);

			hideAnimator.setDuration(500);
			hideAnimator.start();
			
			holder.mRightButtonContainer.removeAllViews();
			if(position == mLeftSliderPostion)
			{
				List<Button> buttons = createButtons(convertView.getContext(),value, mButtonDatas);
				
				for (Button button : buttons)
				{
					holder.mRightButtonContainer.addView(button);
				}
			}
			
			ObjectAnimator showAnimator = ObjectAnimator.ofInt(holder.mRightButtonContainer,"width",0,mRightContainerWidth);
			showAnimator.setDuration(500);
			showAnimator.start();
		}

		//加入右滑功能
		if(holder.mLeftButtonContainer != null)
		{
			mLeftContainerWidth = holder.mLeftButtonContainer.getWidth();
			
			//ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(holder.mLeftButtonContainer,"alpha",1f,0f);

			ObjectAnimator hideAnimator = ObjectAnimator.ofInt(holder.mLeftButtonContainer,"width",mLeftContainerWidth,0);
			
			hideAnimator.setDuration(5000);
			hideAnimator.start();
			
			holder.mLeftButtonContainer.removeAllViews();
			if(position == mRightSliederPostion)
			{
				List<Button> buttons = createButtons(convertView.getContext(),value, mButtonDatas);
				
				for (Button button : buttons)
				{
					holder.mLeftButtonContainer.addView(button);
				}
			}

			//ObjectAnimator showAnimator = ObjectAnimator.ofFloat(holder.mLeftButtonContainer,"alpha",0f,1f);
			ObjectAnimator showAnimator = ObjectAnimator.ofInt(holder.mLeftButtonContainer,"width",0,mLeftContainerWidth);
			showAnimator.setDuration(5000);
			showAnimator.start();
		}
		
		return convertView;
	}
	
	
	private List<Button> createButtons(Context context,final IHsLabelValue value, List<String> buttonDatas)
	{
		List<Button> buttons = new ArrayList<Button>();
		
		if(buttonDatas == null || buttonDatas.size() == 0)
		{
			return buttons;
		}else {
			for (String buttonData : buttonDatas)
			{
				String b[] = buttonData.split(",");
				
				if(b.length > 4 || b.length < 2)
				{
					continue;
				}
				
				final Button button = new Button(context);
				button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
		                LinearLayout.LayoutParams.MATCH_PARENT));
				button.setTag(b[0]); //
				button.setText(b[1]);
				
				button.setTextColor(drawable.White);
				button.setTextColor(Color.WHITE);
				if(b.length > 2)
				{
					button.setTextColor(Integer.valueOf(b[2]));
				}
				
				if(b.length > 3)
				{
					button.setBackgroundResource(Integer.valueOf(b[3]));
				}
				
				button.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						if(mOnButtonClickListener != null)
						{
							Object[] objects = new Object[2];
							objects[0] = button.getTag();
							objects[1] = value;
							
							mOnButtonClickListener.EventHandler(new CommEventObject(this,objects));
						}
						
						mLeftSliderPostion = -1;
						mRightSliederPostion = -1;

					}
				});
				
				buttons.add(button);
			}
		}
		return buttons;
	}

	@Override
	public boolean onLongClick(View v)
	{
		return false;
	}

	@Override
	public Filter getFilter()
	{
		Filter filter = new Filter()
		{

			@Override
			protected FilterResults performFiltering(CharSequence constraint)
			{

                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<IHsLabelValue> FilteredArrList = new ArrayList<IHsLabelValue>();

                
                
                if (mOriginalValues == null) 
                {
                    mOriginalValues = new ArrayList<IHsLabelValue>(mDatas); // saves the original data in mOriginalValues
                }

                /********
                 * 
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)  
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return  
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase(Locale.getDefault());
                    for (int i = 0; i < mOriginalValues.size(); i++) 
                    {
                    	IHsLabelValue value = mOriginalValues.get(i);
                        if (value.getLabel().toLowerCase(Locale.getDefault()).contains(constraint.toString()) || 
                        		value.getValue().toString().toLowerCase(Locale.getDefault()).contains(constraint.toString())) 
                        {
                            FilteredArrList.add(value);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results)
			{
				mDatas = (List<IHsLabelValue>) results.values;
				notifyDataSetChanged();
			}
			
		};
		return filter;
	}
	
	public void onLeftSlider(int position,List<String> buttonDatas)
	{
		mButtonDatas = buttonDatas;
		if(mRightSliederPostion == -1)
		{
			mLeftSliderPostion = position;
		}else {
			mLeftSliderPostion = -1;
			mRightSliederPostion = -1;
		}
	}
	
	public void onRightSlider(int position,List<String> buttonDatas)
	{
		mButtonDatas = buttonDatas;
		if(mLeftSliderPostion == -1)
		{
			mRightSliederPostion = position;
		}else {
			mRightSliederPostion = -1;
			mLeftSliderPostion = -1;
		}
	}

	public class ViewHolder
	{
		public TextView UcKey;
		public TextView UcValue;
		public ImageView ucDetailImage;
		public ImageView ucOperationImage;
		public ViewGroup mRightButtonContainer;
		public ViewGroup mLeftButtonContainer;
		public IHsLabelValue mData;
	}
}
