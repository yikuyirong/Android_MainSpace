package com.hungsum.framework.ui.activities;

//import android.R;
import java.io.Serializable;
import java.lang.ref.WeakReference;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hungsum.framework.R;
import com.hungsum.framework.componments.HsApplication;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsActivity;

public abstract class HsActivity extends FragmentActivity implements IHsActivity
{
	protected static String FUNCNAME = "funcname";

	protected static String DATA = "DATA";

	protected Bundle bundle;

	/**
	 * 是否把左上角图标作为按钮添加，并加一个<的箭头,默认true
	 */
	protected Boolean displayHomeAsUpEnabled = true;

	/**
	 * 是否显示左上角图标，默认true
	 */
	protected Boolean displayShowHomeEnabled = true;

	protected ActionBar actionBar;

	protected SharedPreferences preference;

	protected ProgressDialog progressDialog;

	protected HsApplication<?> application;

	protected String inTitle;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		try
		{
			switch (keyCode)
			{
			case KeyEvent.KEYCODE_BACK:
				this.actionAfterCallBack();
				break;
			default:
				return super.onKeyDown(keyCode, event);
			}
			return true;
		} catch (Exception e)
		{
			this.ShowError(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			this.actionAfterCallBack();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		try
		{
			this.initComponent(savedInstanceState);

			this.initLayout();

			this.actionBar = this.getActionBar();

			if (this.actionBar != null)
			{
				this.initActionbar(this.actionBar);
			}

			this.initActivityView();
			
			if(this.inTitle != null)
			{
				setTitle(inTitle);
			}
				

			this.initEvent();

		} catch (Exception e)
		{
			ShowError(e.getMessage());
		}
	}

	/**
	 * 初始化组件
	 * @throws Exception 
	 */
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		this.preference = this.getPreferences(MODE_PRIVATE);

		this.application = (HsApplication<?>) getApplication();

		this.bundle = this.getIntent().getExtras();
		
		if(this.bundle != null)
		{
			this.initInComingVariable(this.bundle);
		}
	};
	
	protected void initInComingVariable(Bundle bundle) throws Exception
	{
		if(bundle.containsKey("Title"))
		{
			this.inTitle = bundle.getString("Title");
		}
	}


	/**
	 * 初始化布局
	 */
	protected abstract void initLayout();

	protected void initActionbar(ActionBar bar)
	{
		if(inTitle != null)
		{
			bar.setTitle(inTitle);
		}

		// 将应用图标显示为按钮
		bar.setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled);

		bar.setDisplayShowHomeEnabled(displayShowHomeEnabled);
	}

	/**
	 * 关联视图对象
	 */
	protected void initActivityView() { }

	/**
	 * 初始化事件
	 */
	protected void initEvent(){}

	protected int getBackgroundResId()
	{
		return R.drawable.background_repeat;
	}

	protected void actionAfterCallBack()
	{
		this.finish();
	}

	/************Error Information Alert Wait******************/
	
	public void ShowError(Throwable err)
	{
		this.ShowError(err.getMessage() == null ? "发生异常" : err.getMessage());

	}

	public void ShowError(CharSequence text)
	{
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	public Toast ShowInformation(CharSequence text,int duration)
	{
		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
		return toast;
	}
	
	public Toast ShowInformation(CharSequence text)
	{
		return this.ShowInformation(text,  Toast.LENGTH_SHORT);
	}

	protected AlertDialog showAlert(String title, String positiveButtonTitle,
			OnClickListener positiveButtonListener, String negativeButtonTitle,
			OnClickListener negativeButtonListener)
	{
		AlertDialog ad = new AlertDialog.Builder(this).setTitle(title)
				.setIcon(R.drawable.action_help)
				.setPositiveButton(positiveButtonTitle, positiveButtonListener)
				.setNegativeButton(negativeButtonTitle, negativeButtonListener)
				.show();
		ad.setCanceledOnTouchOutside(false);
		return ad;
	}

	public void ShowWait(String title, String msg)
	{
		this.progressDialog = ProgressDialog.show(this, title, msg);
	}

	public void CloseWait()
	{
		// 关闭等待框
		if (progressDialog != null && progressDialog.isShowing())
		{
			progressDialog.dismiss();
		}
	}

	protected WSInnerHandler mHandler = new WSInnerHandler(this);

	/**
	 * Handler对象，用于线程间通信。
	 */
	static class WSInnerHandler extends Handler
	{
		WeakReference<HsActivity> wr;

		public WSInnerHandler(HsActivity activity)
		{
			wr = new WeakReference<HsActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);

			HsActivity activity = wr.get();

			// 关闭等待框
			activity.CloseWait();

			Bundle bundle = msg.getData();
			if (msg.what == R.integer.msg_data)
			{
				try
				{
					activity.actionAfterWSReturnData(
							bundle.getString(FUNCNAME), bundle.getSerializable(DATA));
				} catch (Exception e)
				{
					activity.ShowError(e);
				}
			} else
			{
				activity.ShowError(bundle.getSerializable(DATA).toString());
			}
		}

	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hungsum.framework.ui.activities.IHsActivity#actionAfterWSReturnData
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		throw new Exception("调用未知的方法【" + funcname + "】。");
	}

	protected void sendErrorMessage(String data)
	{
		Bundle bundle = new Bundle();
		bundle.putString(DATA, data);
		Message message = new Message();
		message.what = R.integer.msg_error;
		message.setData(bundle);
		mHandler.sendMessage(message);
	}

	protected void sendDataMessage(HsWSReturnObject data)
	{
		if(data != null)
		{
			Bundle bundle = new Bundle();
			bundle.putString(FUNCNAME, data.GetFuncName());
			bundle.putSerializable(DATA, data.GetData());
			Message message = new Message();
			message.what = R.integer.msg_data;
			message.setData(bundle);
			mHandler.sendMessage(message);
		}
	}

	protected void shareMessage(String message)
	{
		this.shareMessage(message, null);
	}

	protected void shareMessage(Uri imageUri)
	{
		this.shareMessage("", imageUri);
	}

	protected void shareMessage(String content, Uri imageUri)
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (imageUri != null)
		{
			intent.putExtra(Intent.EXTRA_STREAM, imageUri);
			intent.setType("image/*");
			// 当用户选择短信时使用sms_body取得文字
			intent.putExtra("sms_body", content);
		} else
		{
			intent.setType("text/plain");
		}
		intent.putExtra(Intent.EXTRA_TEXT, content);

		Intent chooseIntent = Intent.createChooser(intent, "好东西,共分享 :)");

		if (chooseIntent != null)
		{
			startActivity(chooseIntent);
		} else
		{
			ShowError("没有可以分享的程序。:(");
		}
	}

}
