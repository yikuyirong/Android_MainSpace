package com.hungsum.framework.ui.controls;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hungsum.framework.R;
import com.hungsum.framework.adapter.HsUserLabelValueAdapter;
import com.hungsum.framework.adapter.HsUserLabelValueAdapter.ViewHolder;
import com.hungsum.framework.componments.HsApplication;
import com.hungsum.framework.componments.HsLabelValue;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.interfaces.IOnRetrieveListener;
import com.hungsum.framework.interfaces.IUserControlWithAdapter;
import com.hungsum.framework.ui.activities.HsActivity;

public class UcChooseSingleItem extends UcChooseSingleBase implements IUserControlWithAdapter
{
	private Filter mFilter;
	
	//protected HsLabelValueAdapter mAdapter;

	private AlertDialog mAd;

	private String mFlag;

	private String mParams;

	private Boolean mForceRetrieve = false;
	
	private String mCacheFlag;

	private IOnRetrieveListener mRetrieveListener;
	
	private UcListView ucListView;
	
	private HsApplication<?> mApplication;

	
	public UcChooseSingleItem(Context context)
	{
		super(context);
	}

	public UcChooseSingleItem(Context context, AttributeSet attrs)		
	{
		super(context,attrs);
	}

	public UcChooseSingleItem(Context context, AttributeSet attrs, int defStyle)		
	{
		super(context,attrs,defStyle);
	}

	/************************** 实现 IDialog *************************************/

	@Override
	protected void showDialog()
	{

		HsActivity activity = (HsActivity) getContext();

		if (activity == null)
		{
			return;
		}

		//没有设定强制刷新时可以使用缓存数据。
		if (!this.mForceRetrieve && activity.getApplication() instanceof HsApplication<?>)
		{
			this.mApplication = (HsApplication<?>) activity
					.getApplication();

			this.mCacheFlag = mFlag + "_" + mParams + "_DATA";
			// 如果存在缓存数据，则调入缓存数据。
			@SuppressWarnings("unchecked")
			List<IHsLabelValue> datas = (List<IHsLabelValue>) mApplication
					.GetData(this.mCacheFlag);
			if (datas != null)
			{
				SetDatas(datas);
			} else
			{
				if (mRetrieveListener != null )
				{
					mRetrieveListener.Retrieve(this, mFlag, mParams);
				}
			}
		} else
		{
			if (mRetrieveListener != null)
			{
				mRetrieveListener.Retrieve(this, mFlag, mParams);
			}
		}
	}

	/************************** 实现 DialogInterface.OnClickListener *************************************/

	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		Reset();
	}

	/************************** 实现 IUserAdapter *************************************/

	@Override
	public void SetDatas(List<IHsLabelValue> datas)
	{
		if (this.mAdapter == null)
		{
			//this.mAdapter = new HsLabelValueAdapter(getContext(),R.layout.control_list_keylabel_1,null);
			this.mAdapter = new HsUserLabelValueAdapter(getContext(),R.layout.control_list_keyvalue_1);
			this.mFilter = ((HsUserLabelValueAdapter) this.mAdapter).getFilter();
		}
		((HsUserLabelValueAdapter) this.mAdapter).add(datas);

		//缓存当前数据。
		if(this.mApplication != null && this.mCacheFlag != null)
		{
			this.mApplication.SetData(this.mCacheFlag, datas);
		}

		LinearLayout view = new LinearLayout(getContext());
		view.setOrientation(LinearLayout.VERTICAL);
		view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		UcTextBox ucTextBox = new UcTextBox(getContext());
		ucTextBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		ucTextBox.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_search,0);
		ucTextBox.addTextChangedListener(new TextWatcher()
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				mFilter.filter(s.toString());
				//adapter.getFilter().filter(s.toString());
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
		view.addView(ucTextBox);

		this.ucListView = new UcListView(getContext());
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
				HsUserLabelValueAdapter.ViewHolder holder = (ViewHolder) arg1.getTag();
				if(holder != null)
				{
					setData(new HsLabelValue(holder.UcKey.getText().toString(),holder.UcValue.getText().toString()));
				}
				
				mAd.dismiss();

			}
			
		});
		view.addView(this.ucListView);

		mAd = new AlertDialog.Builder(getContext()).setView(view)
				.setTitle("请选择" + (getCName() == null ? "" : getCName()))
				.setNegativeButton("重置", this).setPositiveButton("取消", null).create();

		mAd.show();

		// 设置大小
		WindowManager.LayoutParams layoutParams = mAd.getWindow()
				.getAttributes();
		layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
		layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mAd.getWindow().setAttributes(layoutParams);
	}

	@Override
	public void SetOnRetrieveListener(IOnRetrieveListener listener)
	{
		this.mRetrieveListener = listener;
	}

	
	
	@Override
	public String GetFlag()
	{
		return mFlag;
	}
	
	public UcChooseSingleItem SetFlag(String flag)
	{
		this.mFlag = flag;
		return this;
	}
	
	public String Params()
	{
		return mParams;
	}
	
	public UcChooseSingleItem SetParams(String params)
	{
		this.mParams = params;
		return this;
	}
	
	public UcChooseSingleItem SetForceRetrieve(Boolean forceRetrieve)
	{
		this.mForceRetrieve = forceRetrieve;
		
		return this;
	}

	@Override
	public void Reset()
	{
		setData(new HsLabelValue("",""));
	}

}
