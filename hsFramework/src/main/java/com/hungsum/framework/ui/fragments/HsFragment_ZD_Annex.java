package com.hungsum.framework.ui.fragments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.events.CommEventListener;
import com.hungsum.framework.events.CommEventListener.EventCategory;
import com.hungsum.framework.events.CommEventObject;
import com.hungsum.framework.interfaces.IControlValue;

public abstract class HsFragment_ZD_Annex<T extends Serializable> extends HsFragment_ZD implements IControlValue , OnItemClickListener
{
	protected String mAnnexClass;
	
	protected String mAnnexClassId;

	protected boolean mHasRetrieved;

	protected boolean mIsModified;
	
	protected int mCurrentSelectedIndex;

	protected T mCurrentSelectedItem;

	protected List<T> mItems = new ArrayList<T>();
	
	/*
	 * 指示是否只读
	 */
	protected boolean mAuditOnly = false;

	public void setAnnexClass(String annexClass) { this.mAnnexClass = annexClass;}
	
	public void setAnnexClassId(String imageClassId)
	{
		this.mAnnexClassId = imageClassId;
	}
	
	public boolean getIsModified()
	{
		return mIsModified;
	}
	
	protected void setIsModified(boolean value)
	{
		mIsModified = value;
		
		if(mIsModified)
		{
			dispatchCommEvent(EventCategory.DataChanged, null);
		}
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
				menuItem.setVisible(this.isVisible() && getAllowEdit());
				
			}
		}
	}

	
	
	protected abstract BaseAdapter createAdapter(List<T> datas);
	
	protected abstract BaseAdapter getAdapter();

	@Override
	public void onSelected()
	{
		super.onSelected();
		
		this.callRetrieveOnOtherThread();
	}
	
	protected void callRetrieveOnOtherThread()
	{
		try
		{
			
			showWait("请稍候", "正在努力加载数据...");

			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						sendDataMessage(retrieve());
					} catch (Exception e)
					{
						sendErrorMessage(e.getMessage());
					}

				}
			}).start();
		} catch (Exception e)
		{
			showError(e);
		}
		
	}

	protected abstract HsWSReturnObject retrieve() throws Exception;

	public abstract void updateAnnex();

	
	protected int getAnnexCount()
	{
		return getAdapter().getCount();
	}
	
	
	//{{ 添加删除操作
	
	protected void addItem(T item)
	{
		this.setIsModified(true);
	}
	
	protected void addItems(List<T> items)
	{
		this.setIsModified(true);
	}
	
	protected void removeItem(int position)
	{
		this.setIsModified(true);
	}
	
	protected abstract T getItem(int position);
	
	//}}
	
	
	/**
	 * 设置会总行信息
	 */
	protected abstract void setSummaryText();
	

	//{{ IControlValue 接口
	
	@Override
	public CharSequence getControlId()
	{
		return null;
	}

	@Override
	public void setControlId(CharSequence value)
	{
	}

	@Override
	public CharSequence getControlValue() throws Exception
	{
		return null;
	}

	@Override
	public void setControlValue(final CharSequence value) throws Exception
	{
	}
	
	@Override
	public void setDefaultControlValue(CharSequence value)
	{
	}

	@Override
	public CharSequence getControlTitle(CharSequence value)
	{
		return null;
	}

	@Override
	public CharSequence getCName()
	{
		return Title;
	}

	@Override
	public void setCName(CharSequence value)
	{
		Title = (String) value;
	}

	private boolean mAllowEmpty;
	
	@Override
	public boolean getAllowEmpty()
	{
		return mAllowEmpty;
	}

	@Override
	public void setAllowEmpty(boolean value)
	{
		mAllowEmpty = value;
	}

	private boolean mAllowEdit = true;
	
	@Override
	public void setAllowEdit(boolean value)
	{
		mAllowEdit = value;
	}

	public boolean getAllowEdit()
	{
		return mAllowEdit;
	}

	
	private boolean mDefaultAllowEdit;
	
	@Override
	public boolean getDefaultAllowEdit()
	{
		return mDefaultAllowEdit;
	}

	@Override
	public void setDefaultAllowEdit(boolean value)
	{
		mDefaultAllowEdit = value;
	}

	@Override
	public void Validate() throws Exception
	{
		if (!mAllowEmpty && getAnnexCount() < 1)
		{
			throw new Exception(this.getCName() + "不能为空。");
		}
	}

	@Override
	public void Reset()
	{
	}

	@Override
	public void dispatchDataChangedEvents()
	{
		if(this.mListeners != null)
		{
			for (CommEventListener listener : this.mListeners)
			{
				if (listener.getCategory() == EventCategory.DataChanged)
				{
					if(mCanTriggerDataChangedEvent)
					{
						listener.EventHandler(new CommEventObject(this, null));
					}
				}
			}
		}
	}

	@Override
	public void addOnDataChangedListener(CommEventListener listener)
	{
		if (listener.getCategory() == EventCategory.DataChanged)
		{
			addCommEventListener(listener);
		}
	}

	@Override
	public void removeOnDataChangedListener(CommEventListener listener)
	{
		removeCommEventListener(EventCategory.DataChanged);
	}

	@Override
	public void removeAllOnDataChangedListener()
	{
		removeCommEventListener(EventCategory.DataChanged);
	}

	private boolean mCanTriggerDataChangedEvent = true;
	
	@Override
	public boolean getCanTriggerDataChangedEvent()
	{
		return mCanTriggerDataChangedEvent;
	}

	@Override
	public void setCanTriggerDataChangedEvent(boolean value)
	{
		mCanTriggerDataChangedEvent = value;
	}
	
	//}}
	
	//{{ OnItemClickListener 接口

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		try
		{
			@SuppressWarnings("unchecked")
			T item = (T) this.getAdapter().getItem(arg2);
			
			if(item != null)
			{
				mCurrentSelectedItem = item;
				mCurrentSelectedIndex = arg2;
			}else {
				mCurrentSelectedItem = null;
				mCurrentSelectedIndex = -1;
			}

//			if(item != null && item.GetDetailsCount() > 0)
//			{
//				mCurrentSelectedItem = item;
//				mCurrentSelectedIndex = arg2;
//				openDetailIntent(item);
//			}else {
//				mCurrentSelectedItem = null;
//				mCurrentSelectedIndex = -1;
//			}
		} catch (Exception e)
		{
			showError(e);
		}
	}

	//}}
	
	
}
