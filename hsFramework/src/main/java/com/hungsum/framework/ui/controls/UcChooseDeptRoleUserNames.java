package com.hungsum.framework.ui.controls;

import java.util.EventListener;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hungsum.framework.R;
import com.hungsum.framework.adapter.HsDeptRoleUserNamesAdapter;
import com.hungsum.framework.ui.activities.HsActivity;

public class UcChooseDeptRoleUserNames extends UcChooseMultiBase
{
	private Filter mFilter;
	
	//protected HsLabelValueAdapter mAdapter;

	private AlertDialog mAd;

	private RetrieveEventListener mRetrieveEventListener;

	private UcListView ucListView;

	public UcChooseDeptRoleUserNames(Context context)
	{
		super(context);
	}

	public UcChooseDeptRoleUserNames(Context context, AttributeSet attrs)		
	{
		super(context,attrs);
	}

	public UcChooseDeptRoleUserNames(Context context, AttributeSet attrs, int defStyle)		
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

		if (mRetrieveEventListener != null)
		{
			mRetrieveEventListener.Retrieve();;
		}
	}

	/************************** 实现 DialogInterface.OnClickListener *************************************/

	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		switch (which)
		{
			//取消
			case DialogInterface.BUTTON_NEGATIVE:
				Reset();
				break;
			//确定
			case DialogInterface.BUTTON_NEUTRAL:
				setControlValue(((HsDeptRoleUserNamesAdapter)mAdapter).getSelectedData());
				break;
		}
	}

	public void SetDatas(List<String> datas)
	{
		if (this.mAdapter == null)
		{
			this.mAdapter = new HsDeptRoleUserNamesAdapter(getContext(),R.layout.control_list_roleuser_1);
			this.mFilter = ((HsDeptRoleUserNamesAdapter) this.mAdapter).getFilter();
		}
		
		((HsDeptRoleUserNamesAdapter) this.mAdapter).add(datas,getControlValue().toString());

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
		this.ucListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
		this.ucListView.setAdapter(this.mAdapter);
		view.addView(this.ucListView);

		mAd = new AlertDialog.Builder(getContext()).setView(view)
				.setTitle("请选择" + (getCName() == null ? "" : getCName()))
				.setNegativeButton("重置", this)
				.setPositiveButton("取消", null)
				.setNeutralButton("确定", this) .create();

		mAd.show();

		// 设置大小
		WindowManager.LayoutParams layoutParams = mAd.getWindow()
				.getAttributes();
		layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
		layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mAd.getWindow().setAttributes(layoutParams);
	}

	public void SetOnRetrieveEventListener(RetrieveEventListener listener)
	{
		this.mRetrieveEventListener = listener;
	}

	@Override
	public void Reset()
	{
		((HsDeptRoleUserNamesAdapter)mAdapter).setSelectedData("");
		setControlValue("");
	}

	public interface RetrieveEventListener extends EventListener
	{
		void Retrieve();
	}
}
