package com.hungsum.framework.ui.fragments;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.EventListener;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hungsum.framework.R;
import com.hungsum.framework.componments.HsApplication;
import com.hungsum.framework.componments.HsCommEventListeners;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.events.CommEventListener;
import com.hungsum.framework.events.CommEventListener.EventCategory;
import com.hungsum.framework.events.CommEventObject;
import com.hungsum.framework.webservices.HsWebService;

public class HsFragment extends android.support.v4.app.Fragment
{

	protected static String FUNCNAME = "funcname";

	protected static String DATA = "DATA";

	public String Title;
	
	public int Icon;

	protected CreateCompletedEventListener mListener;
	
	protected boolean mHasCreateCompleted;

	protected HsCommEventListeners mListeners;
	
	protected HsApplication<HsWebService> mApplication;
	
	protected ProgressDialog mProgressDialog;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		this.mApplication = (HsApplication<HsWebService>)getActivity().getApplication();
	}

	public void addCommEventListener(CommEventListener listener)
	{
		if(mListeners == null)
		{
			mListeners = new HsCommEventListeners();
		}
		
		mListeners.add(listener);
		
	}
	
	/**
	 * Tab页选中时会调用此方法。
	 */
	public void onSelected(){}

	public void removeCommEventListener(EventCategory eventCategory)
	{
		mListeners.remove(eventCategory);
	}
	
	/**
	 * 分发事件
	 * @param category
	 * @param object
	 */
	protected void dispatchCommEvent(EventCategory category,CommEventObject object)
	{
		for (CommEventListener listener : mListeners.getItems(category))
		{
			listener.EventHandler(object);
		}
	}

	protected void showWait(String title, String msg)
	{
		this.mProgressDialog = ProgressDialog.show(getActivity(), title, msg);
	}

	protected void closeWait()
	{
		// 关闭等待框
		if (this.mProgressDialog != null && mProgressDialog.isShowing())
		{
			mProgressDialog.dismiss();
		}
	}
	
	
	public void showError(Throwable err)
	{
		this.showError(err.getMessage() == null ? "发生异常":err.getMessage());
	}
	
	public void showError(CharSequence text)
	{
		Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
	}

	protected void showInformation(CharSequence text)
	{
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
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
		Bundle bundle = new Bundle();
		bundle.putString(FUNCNAME, data.GetFuncName());
		bundle.putSerializable(DATA, data.GetData());
		Message message = new Message();
		message.what = R.integer.msg_data;
		message.setData(bundle);
		mHandler.sendMessage(message);
	}
	
	protected WSInnerHandler mHandler = new WSInnerHandler(this);

	/**
	 * Handler对象，用于线程间通信。
	 */
	static class WSInnerHandler extends Handler
	{
		WeakReference<HsFragment> wr;

		public WSInnerHandler(HsFragment fragment)
		{
			wr = new WeakReference<HsFragment>(fragment);
		}

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);

			HsFragment activity = wr.get();

			// 关闭等待框
			activity.closeWait();

			Bundle bundle = msg.getData();
			if (msg.what == R.integer.msg_data)
			{
				try
				{
					activity.actionAfterWSReturnData(
							bundle.getString(FUNCNAME), bundle.getSerializable(DATA));
				} catch (Exception e)
				{
					activity.showError(e);
				}
			} else
			{
				activity.showError(bundle.getSerializable(DATA).toString());
			}
		}

	}

	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		throw new Exception("调用未知的方法【" + funcname + "】。");
	}

	
	public void setOnCreateCompletedEventListener(CreateCompletedEventListener listener)
	{
		this.mListener = listener;
	}
	
	public void removeOnCreateCompletedEventListener()
	{
		this.mListener = null;
	}
	
	public interface CreateCompletedEventListener extends EventListener
	{
		void action(Bundle savedInstanceState);
	}

	
}
