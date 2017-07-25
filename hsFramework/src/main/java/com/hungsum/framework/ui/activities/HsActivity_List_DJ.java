package com.hungsum.framework.ui.activities;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hungsum.framework.R;
import com.hungsum.framework.adapter.HsUserLabelValueAdapter;
import com.hungsum.framework.adapter.HsUserLabelValueAdapter.ViewHolder;
import com.hungsum.framework.componments.HsLabelValue;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.controls.UcDateBox;
import com.hungsum.framework.ui.controls.UcFormItem;
import com.hungsum.framework.ui.controls.UcListView;
import com.hungsum.framework.ui.controls.UcMultiCheckBox;
import com.hungsum.framework.ui.controls.UcTextBox;
import com.hungsum.framework.ui.controls.UcUserDialog;

public abstract class HsActivity_List_DJ extends HsActivity_HsLabelValueList
{
	private String mBeginDateValue;

	private String mEndDateValue;

	private String mSwitchValues;
	
	private boolean mConditionBeginDateVisible = true;
	
	private boolean mConditionEndDateVisible = true;
	
	private boolean mConditionSwitchVisible = true;
	
	private String mConditionSwitchTitle = "查询条件";
	
	private String mConditionSwitchDatas;
	
	private String mConditionSwitchDefaultValues;
	
	private String mConditionBeginDateFlag = "MonthFirst";
	
	private String mConditionEndDateFlag = "Now";
	
	protected IHsLabelValue mCurrentSelectedItem;

	/**
	 * 是否需要选择必要数据。
	 */
	protected boolean mIsNeedChooseNecessaryData = false;
	
	protected IHsLabelValue mNecessaryData;
	
	private AlertDialog mAd;

	protected String getBeginDateValue()
	{
		return mBeginDateValue == null ? UcDateBox.fromFlag(mConditionBeginDateFlag) : mBeginDateValue;
	}
	
	protected String getEndDateValue()
	{
		return mEndDateValue == null ? UcDateBox.fromFlag(mConditionEndDateFlag) : mEndDateValue;
	}
	
	protected String getSwitchValues()
	{
		return mSwitchValues == null ? mConditionSwitchDefaultValues : mSwitchValues;
	}
	
	protected void setBeginDateValue(String value)
	{
		mBeginDateValue = value;
	}
	
	protected void setEndDateValue(String value)
	{
		mEndDateValue = value;
	}
	
	protected void setSwitchValues(String value)
	{
		mSwitchValues = value;
	}

	protected void setConditionBeginDateFlag(String flag)
	{
		this.mConditionBeginDateFlag = flag;
	}
	
	protected void setConditionEndDateFlag(String flag)
	{
		this.mConditionEndDateFlag = flag;
	}

	/**
	 * 设置选择开关数据源
	 * @param datas
	 */
	protected void setConditionSwitchDatas(String datas)
	{
		this.mConditionSwitchDatas = datas;
	}
	
	/**
	 * 设置选择开关默认值
	 * @param values
	 */
	protected void setConditionSwitchDefaultValues(String values)
	{
		this.mConditionSwitchDefaultValues = values;
	}
	
	/**
	 * 设置选择开关标题
	 * @param title
	 */
	protected void setConditionSwitchTitle(String title)
	{
		this.mConditionSwitchTitle = title;
	}
	
	/**
	 * 设置查询条件开始日期是否显示
	 * @param visible
	 */
	protected void setConditionBeginDateVisible(boolean visible)
	{
		this.mConditionBeginDateVisible = visible;
	}
	
	/**
	 * 设置查询条件结束日期是否显示
	 * @param visible
	 */
	protected void setConditionEndDateVisible(boolean visible)
	{
		this.mConditionEndDateVisible = visible;
	}
	
	/**
	 * 设置查询条件选择开关是否显示
	 * @param visible
	 */
	protected void setConditionSwithVisible(boolean visible)
	{
		this.mConditionSwitchVisible = visible;
	}

	@Override
	protected void initActivityView()
	{
		super.initActivityView();
		
		//为listview注册上下文菜单
		this.registerForContextMenu(this.ucListView);
	}
	
	@Override
	protected void callRetrieveAfterInit()
	{
		if(mIsNeedChooseNecessaryData)
		{
			try
			{
				ShowWait("请稍候", "正在努力加载数据...");

				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							sendDataMessage(chooseNecessaryData());
						} catch (Exception e)
						{
							sendErrorMessage(e.getMessage());
						}

					}
				}).start();
			} catch (Exception e)
			{
				ShowError(e);
			}
		}else {
			super.callRetrieveAfterInit();
		}
	}
	
	/**
	 * 选择必要数据,此方法工作在工作线程上。
	 */
	protected HsWSReturnObject chooseNecessaryData() throws Exception
	{
		throw new Exception("请在子代中实现【chooseData】");
	}
	

	/**
	 * 必要数据返回后调用此方法。
	 * @param datas 必要数据集合
	 * @param isCloseAfterCancle 用户取消后是否关闭当前列表
	 */
	protected void actionAfterReturnChooseData(List<IHsLabelValue> datas,final boolean isCloseAfterCancle)
	{
		ChooseDataView view = new ChooseDataView(this, datas);
		
		view.addItemSelectedListener(new IItemSelectedListener()
		{
			
			@Override
			public void selecteItem(ItemSelectedEvent event)
			{
				actionAfterChooseNecessaryData(event.getData());
				
				mAd.dismiss();
			}
		});
		
		mAd = new AlertDialog.Builder(this).setView(view).setNegativeButton("取消",new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if(isCloseAfterCancle)
				{
					HsActivity_List_DJ.this.finish();
				}
			}
		}).show();
		
		mAd.show();
	}
	
	/**
	 * 用户选择数据条目后调度,工作在UI线程。
	 * @param data
	 * @throws Exception 
	 */
	protected void actionAfterChooseNecessaryData(IHsLabelValue data)
	{
		mNecessaryData = data;
		
		super.callRetrieveOnOtherThread(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		
		//刷新
		menu.add(0,R.string.str_retrieve, 0, getString(R.string.str_retrieve))
		.setIcon(R.drawable.navigation_refresh)
		.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
		//新建
		menu.add(0, R.string.str_new, 0, getString(R.string.str_new))
		.setIcon(R.drawable.content_new_event)
		.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}

	/* 
	 * 系统菜单回调函数
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId() == R.string.str_new)
		{
			try
			{
				this.addItem();
			} catch (Exception e)
			{
				ShowError(e);
			}
			return true;
		}else if(item.getItemId() == R.string.str_retrieve)
		{
			callRetrieveOnOtherThread(true);
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	/* 
	 * 构建上下文菜单
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		
		menu.setHeaderTitle("请选择");
	}

	
	
	/* 
	 * 上下文菜单回调函数
	 */
	@Override
	public boolean onContextItemSelected(final MenuItem item)
	{
		try
		{
			if(item.getItemId() == R.string.str_new)
			{
				this.addItem();
				return true;
			}else {
				AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();

				final IHsLabelValue object = (IHsLabelValue) this.ucListView.getAdapter().getItem(menuInfo.position);
				
				actionAfterMenuOrButtonClick(item.getItemId(),object);
				
				return true;
			}
		} catch (Exception e)
		{
			ShowError(e);
		}
		return true;
	}
	
	@Override
	public void onSliderButtonClick(int buttonId, IHsLabelValue object)
	{
		try
		{
			if(object == null) throw new Exception("请先选择条目");
			
			actionAfterMenuOrButtonClick(buttonId, object);
		} catch (Exception e)
		{
			ShowError(e);
		}
	}

	
	protected void actionAfterMenuOrButtonClick(final int actionId, final IHsLabelValue object) throws Exception
	{
		if(doSomeThingByContextItemSelected(actionId, object))
		{
			//在工作线程中继续进行处理
			showAlert("是否处理选中数据？", "确定", new OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					mCurrentSelectedItem = object;
					
					ShowWait("请稍候", "正在处理数据...");
					new WSActionThread(actionId, object).start();
				}
			} , "取消", null);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		try
		{
			HsLabelValue item = (HsLabelValue) this.ucListView.getAdapter().getItem(arg2);

			if(item != null && item.getDetailsCount() > 0)
			{
				this.mCurrentSelectedItem = item;
				
				this.modifyItem(item);
			}
		} catch (Exception e)
		{
			ShowError(e);
		}
	}
	
	@Override
	protected void callRetrieveOnOtherThread(boolean isShowConditionIfHas)
	{
		if(isShowConditionIfHas && mIsShowRetrieveCondition)
		{
			try
			{
				showRetrieveCondition();
			}catch (Exception e)
			{
				ShowError(e);
			}
		}else {
			super.callRetrieveOnOtherThread(isShowConditionIfHas);
		}
		
	}
	
	/*
	 * 显示刷新条件
	 */
	private void showRetrieveCondition()
	{
		final ConditionView view = new ConditionView(this);
		
		new UcUserDialog(this)
		.setTitle("查询条件")
		.setView(view)
		.setPositiveButton("确认",new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				setBeginDateValue(view.getBeginDate());
				setEndDateValue(view.getEndDate());
				setSwitchValues(view.getSwitchValues());
				
				HsActivity_List_DJ.super.callRetrieveOnOtherThread(true);
			}
		}).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == HsActivity_DJ.REQUESTCODE_ITEM)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				super.callRetrieveOnOtherThread(true);
			}else {
				super.onActivityResult(requestCode, resultCode, data);
			}
		}else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	protected IHsLabelValue getItem()
	{
		return (IHsLabelValue) this.ucListView.getSelectedItem();
	}
	
	
	protected String getBhs(IHsLabelValue item, String bhLabel)
	{
		StringBuffer sb = new StringBuffer();
		if(item != null)
		{
			sb.append("<Bhs><Bh>");
			sb.append(item.getValue(bhLabel, ""));
			sb.append("</Bh></Bhs>");
		}
		return sb.toString();
	}

	protected HsWSReturnObject deleteItem(IHsLabelValue item) throws Exception
	{
		throw new Exception("请在子代方法中实现deleteItem(IHsLabelValue item)方法");
	}
	
	protected void addItem() throws Exception
	{
		throw new Exception("请在子代方法中实现addItem()方法");
	}
	
	protected void modifyItem(IHsLabelValue item) throws Exception
	{
		throw new Exception("请在子代方法中实现modifyItem(IHsLabelValue item)方法");
	}
	
	protected HsWSReturnObject submitItem(IHsLabelValue item) throws Exception
	{
		throw new Exception("请在子代方法中实现submitItem(IHsLabelValue item)方法");
	}
	
	protected HsWSReturnObject unSubmitItem(IHsLabelValue item) throws Exception
	{
		throw new Exception("请在子代方法中实现unSubmitItem(IHsLabelValue item)方法");
	}
	
	protected HsWSReturnObject confirmItem(IHsLabelValue item) throws Exception
	{
		throw new Exception("请在子代方法中实现confirmItem(IHsLabelValue item)方法");
	}
	
	protected HsWSReturnObject unConfirmItem(IHsLabelValue item) throws Exception
	{
		throw new Exception("请在子代方法中实现unConfirmItem(IHsLabelValue item)方法");
	}
	
	protected HsWSReturnObject auditItem(IHsLabelValue item) throws Exception
	{
		throw new Exception("请在子代方法中实现auditItem(IHsLabelValue item)方法");
	}
	
	protected HsWSReturnObject unAuditItem(IHsLabelValue item) throws Exception
	{
		throw new Exception("请在子代方法中实现unAuditItem(IHsLabelValue item)方法");
	}
	
	protected HsWSReturnObject doSomeThingOnOtherThreadByContextItemSelected(int actionId,IHsLabelValue item) throws Exception
	{
		throw new Exception("请在子代方法中实现doSomeThingOnContextItemSelected(int actionId, IHsLabelValue item)方法");
	}
	
	/**
	 * 在UI线程处理
	 * @param actionId
	 * @param item
	 * @return 如果返回真，则之后再工作线程继续处理
	 * @throws Exception 
	 */
	protected boolean doSomeThingByContextItemSelected(int actionId,IHsLabelValue item) throws Exception
	{
		if(actionId == R.string.str_modify)
		{
			modifyItem(item);
			
			return false;
		}else {
			return true;
		}
	}

	/**
	 * @author zhaixuan
	 * 需要与WS交互的方法在这个类中统一处理。
	 */
	private class WSActionThread extends Thread
	{
		private int _actionId;
		
		private IHsLabelValue _item;
		
		public WSActionThread(int actionId, IHsLabelValue item)
		{
			this._actionId = actionId;
			this._item = item;
		}

		@Override
		public void run()
		{
			try
			{
				HsWSReturnObject object = null;
				if(_actionId == R.string.str_delete)
				{
					object = deleteItem(_item);
				}else if(_actionId == R.string.str_submit)
				{
					object = submitItem(_item);
				}else if(_actionId == R.string.str_unsubmit)
				{
					object = unSubmitItem(_item);
				}
				else if(_actionId == R.string.str_audit)
				{
					object = auditItem(_item);
				}else if(_actionId == R.string.str_unaudit)
				{
					object = unAuditItem(_item);
				}else if(_actionId == R.string.str_confirm)
				{
					object = confirmItem(_item);
				}else if(_actionId == R.string.str_unconfirm)
				{
					object = unConfirmItem(_item);
				}else {
					object = doSomeThingOnOtherThreadByContextItemSelected(_actionId, _item);
				}
				sendDataMessage(object);
			} catch (Exception e)
			{
				sendErrorMessage(e.getMessage());
			}
		}
		
	}
	
	/**
	 * 查询条件布局，刷新数据弹出的的选择刷新条件使用此布局
	 * @author zhaixuan
	 *
	 */
	class ConditionView extends LinearLayout
	{
		private UcDateBox ucBeginDate;
		
		private UcDateBox ucEndDate;
		
		private UcMultiCheckBox ucSwitch;
		
		public ConditionView(Context context)
		{
			super(context);

			this.setOrientation(LinearLayout.VERTICAL);
			
			if(mConditionBeginDateVisible)
			{
				//开始日期
				ucBeginDate = new UcDateBox(context);
				ucBeginDate.setControlValue(HsActivity_List_DJ.this.getBeginDateValue());
				ucBeginDate.setCName("开始日期");
				ucBeginDate.setAllowEmpty(false);
				//this.controls.add(this.mUcKh);
				UcFormItem f_beginDate = new UcFormItem(context, ucBeginDate);
				addView(f_beginDate.GetView());
			}

			if(mConditionEndDateVisible)
			{
				//结束日期
				ucEndDate = new UcDateBox(context);
				ucEndDate.setControlValue(HsActivity_List_DJ.this.getEndDateValue());
				ucEndDate.setCName("开始日期");
				ucEndDate.setAllowEmpty(false);
				//this.controls.add(this.mUcKh);
				UcFormItem f_endDate = new UcFormItem(context, ucEndDate);
				addView(f_endDate.GetView());
			}
			
			if(mConditionSwitchVisible && HsActivity_List_DJ.this.mConditionSwitchDatas != null)
			{
				//开关条件
				ucSwitch = new UcMultiCheckBox(context);
				ucSwitch.SetDatas(mConditionSwitchDatas,HsActivity_List_DJ.this.getSwitchValues());
				ucSwitch.setCName(mConditionSwitchTitle);
				ucSwitch.setAllowEmpty(false);
				UcFormItem f_switch = new UcFormItem(context,ucSwitch);
				addView(f_switch.GetView());
			}

		}
		
		public String getBeginDate()
		{
			return ucBeginDate == null ? null : ucBeginDate.getControlValue().toString();
		}
		
		public String getEndDate()
		{
			return ucEndDate == null ? null : ucEndDate.getControlValue().toString();
		}
		
		public String getSwitchValues()
		{
			return ucSwitch == null ? null : ucSwitch.getControlValue().toString();
		}
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		// TODO Auto-generated method stub
		super.actionAfterWSReturnData(funcname, data);
	}
	
	
	public interface IItemSelectedListener extends EventListener
	{
		void selecteItem(ItemSelectedEvent event);
	}
	
	public class ItemSelectedEvent extends EventObject
	{

		private static final long serialVersionUID = -3653922958619656127L;

		private IHsLabelValue mData;
		
		public ItemSelectedEvent(Object source, IHsLabelValue data)
		{
			super(source);
			
			mData = data;
		}
		
		public IHsLabelValue getData()
		{
			return mData;
		}
	}
	
	/**
	 * 选择数据布局，在第一次刷新数据前，如果需要选择数据初始化，则使用此布局
	 * @author zhaixuan
	 *
	 */
	public class ChooseDataView extends LinearLayout
	{
		/**
		 * 检索窗
		 */
		private UcTextBox ucTextBox;

		/**
		 * 列表窗
		 */
		private UcListView ucListView;
		
		/**
		 * 事件接口
		 */
		private List<IItemSelectedListener> mItemSelectedListeners;
		
		private HsUserLabelValueAdapter mAdapter;
		
		private android.widget.Filter mFilter;
		
		public ChooseDataView(Context context,List<IHsLabelValue> datas)
		{
			super(context);
			
			this.setOrientation(LinearLayout.VERTICAL);
			this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			
			mItemSelectedListeners = new ArrayList<IItemSelectedListener>();
			
			this.mAdapter = new HsUserLabelValueAdapter(getContext(),R.layout.control_list_keyvalue_1);
			this.mAdapter.add(datas);
			this.mFilter = mAdapter.getFilter();

			ucTextBox = new UcTextBox(context);
			ucTextBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			ucTextBox.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_search,0);
			ucTextBox.addTextChangedListener(new TextWatcher()
			{
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count)
				{
					mFilter.filter(s.toString());
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after)
				{
				}
				
				@Override
				public void afterTextChanged(Editable s)
				{
				}
			});
			this.addView(ucTextBox);
			
			this.ucListView = new UcListView(context);
			this.ucListView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			//this.ucListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			this.ucListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
			this.ucListView.setAdapter(this.mAdapter);
			this.ucListView.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3)
				{
					if(mItemSelectedListeners.size() > 0)
					{
						HsUserLabelValueAdapter.ViewHolder holder = (ViewHolder) arg1.getTag();
						if(holder != null)
						{
							notifyListeners(new ItemSelectedEvent(ChooseDataView.this,holder.mData));
						}
					}

					//mAd.dismiss();
				}
			});
			
			this.addView(this.ucListView);
		}
		
		public void addItemSelectedListener(IItemSelectedListener listener)
		{
			mItemSelectedListeners.add(listener);
		}
		
		public void notifyListeners(ItemSelectedEvent event)
		{
			for (IItemSelectedListener listener : mItemSelectedListeners)
			{
				listener.selecteItem(event);
			}
		}
		

				
	}
}
