package com.hungsum.framework.ui.activities;

import java.io.Serializable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hungsum.framework.R;
import com.hungsum.framework.componments.HsLoginData;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.models.ModelLogin;
import com.hungsum.framework.ui.controls.UcMultiRadio;
import com.hungsum.framework.ui.controls.UcTextBox;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;
import com.hungsum.framework.utils.HsGZip;

public class HsActivity_Login extends HsActivity_ZD
{
	private static String STORE_USERNAME = "username";

	private static String STORE_PASSWORD = "password";
	
	private static String STORE_DB = "db";
	
	private static String STORE_SAVEPASSWORD = "savePassword";
	
	protected UcTextBox ucUsername;
	
	protected UcTextBox ucPassword;
	
	protected UcMultiRadio ucDb;
	
	protected CheckBox ucSavepassword;
	
	protected TextView ucSerialNum;

	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new LogonFragment();
	}

	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);
		
		this.inTitle = "欢迎，请登录";
	}

	@Override
	protected void initActivityView()
	{
		super.initActivityView();
		
		//获取账套信息
		try
		{
			ShowWait("请稍候", "正在获取账套信息...");

			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						HsWSReturnObject returnObject = application.getWebService().getDbs();

						sendDataMessage(returnObject);
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
		finally
		{
			
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		this.getMenuInflater().inflate(R.menu.menu_logon, menu);

		if(!this.application.AllowConfigWSDL)
		{
			menu.removeItem(R.id.config);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		try
		{
			if(item.getItemId() == R.id.login)
			{
				callUpdateOnOtherThread();
				return true;
			}else if(item.getItemId() == R.id.config)
			{
				Intent intent = new Intent(this, HsActivity_Config.class);
				startActivity(intent);
				return true;
			}else 
			{
				return super.onOptionsItemSelected(item);
			}
		} catch (Exception e)
		{
			this.ShowError(e);
			return false;
		}
	}


	@Override
	protected void newData()
	{
		super.newData();

		//载入储存值

		ucSavepassword.setChecked(preference.getBoolean(STORE_SAVEPASSWORD,false));

		//initValue
		ucUsername.setControlValue(preference.getString(STORE_USERNAME, ""));

		if(ucSavepassword.isChecked())
		{
			ucPassword.setControlValue(preference.getString(STORE_PASSWORD,""));
		}

	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		String args = null;
		
		if(this.application.ValidateDeviceSn)
		{
			args = "<Sn>" + this.ucSerialNum.getText() + "</Sn>";
		}
		
		return application.getWebService().login(
				this.ucUsername.getControlValue(),
				this.ucPassword.getControlValue(),
				this.ucDb.getControlValue(),
				args);
	}


	protected void saveLastLoginData()
	{
		//将登录信息保存到共享存储中
		Editor editor = this.preference.edit();
		editor.putString(STORE_USERNAME, this.ucUsername.getControlValue().toString());
		editor.putBoolean(STORE_SAVEPASSWORD,this.ucSavepassword.isChecked());
		editor.putString(STORE_DB,this.ucDb.getControlValue().toString());
		editor.putString(STORE_PASSWORD,this.ucSavepassword.isChecked() ? this.ucPassword.getControlValue().toString() : "");
		editor.commit();
	}

	@Override
	public void actionAfterWSReturnData(String funcname,Serializable data) throws Exception
	{
		if(funcname.equals("Login"))
		{
			String returnData = HsGZip.DecompressString(data.toString());
			
			HsLoginData loginData = new ModelLogin().Create(returnData);
			
			//loginData.SetCurrentRole(0); //默认第一个角色

			this.saveLastLoginData();

			application.ClearData();


			//将登录信息保存至本地缓存中
			application.setLoginData(loginData);
		
			//登录主页面
			Intent intent = new Intent();
			intent.setAction(com.hungsum.framework.SysActionAttr.ACTION_HSMAIN);
			intent.addCategory(getPackageName());
			intent.putExtra(getString(R.string.comm_logindata),loginData);
			startActivity(intent);



			//将当前页自栈中移出。
			finish();
		}else if(funcname.equals("GetDbs"))
		{
			String dbs = HsGZip.DecompressString(data.toString());
			
			String storeDb = preference.getString(STORE_DB, null);
			
			if(storeDb != null)
			{
				this.ucDb.SetDatas(dbs,storeDb);
			}else {
				this.ucDb.SetDatas(dbs,null);
				this.ucDb.SetData(0);
			}

		}else
		{
			super.actionAfterWSReturnData(funcname, data);
		}

	}
	
	public static class LogonFragment extends HsFragment_ZD_Main
	{
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			final HsActivity_Login activity = (HsActivity_Login) getActivity();


			View rootView = inflater.inflate(R.layout.hsactivity_login, null,false);

			activity.ucUsername = (UcTextBox)rootView.findViewById(R.id.ucUserName);
			activity.ucUsername.setCName("用户名");
			activity.ucUsername.setAllowEmpty(false);
			activity.controls.add(activity.ucUsername);

			activity.ucPassword = (UcTextBox)rootView.findViewById(R.id.ucPassword);
			activity.ucPassword.setCName("密码");
			activity.ucPassword.setAllowEmpty(false);
			activity.controls.add(activity.ucPassword);

			activity.ucDb = (UcMultiRadio) rootView.findViewById(R.id.ucDb);
			activity.ucDb.setCName("账套");
			activity.ucDb.setAllowEmpty(false);
			activity.controls.add(activity.ucDb);

			activity.ucSavepassword = (CheckBox)rootView.findViewById(R.id.ucSavePassword);

			activity.ucSerialNum = (TextView)rootView.findViewById(R.id.ucSerialNum);
			activity.ucSerialNum.setText(android.os.Build.SERIAL);
			activity.ucSerialNum.setText(mApplication.GetSerialNumber());
			//分享序列号，将序列号发送出去方便注册
			activity.ucSerialNum.setOnLongClickListener(new OnLongClickListener()
			{
				
				@Override
				public boolean onLongClick(View v)
				{
				    Intent intent = new Intent(Intent.ACTION_SEND);
				    intent.setType("text/plain");
				    intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
				    intent.putExtra(Intent.EXTRA_TEXT,
				    		( activity.ucUsername.getControlValue().equals("") ? "" : "[用户]" + activity.ucUsername.getControlValue()) +
				    		"[设备号]" + activity.ucSerialNum.getText());
				    
				    Intent chooseIntent = Intent.createChooser(intent,"分享");
				    
				    if(chooseIntent != null)
				    {
					    startActivity(chooseIntent);
				    }else 
				    {
				    	showError("没有可以分享的程序。:(");
					}
					return false;
				}
			});

			mHasCreateCompleted = true;

			if(mListenerOfCreateCompletedEventListener != null)
			{
				mListenerOfCreateCompletedEventListener.action(savedInstanceState);

				removeOnCreateCompletedEventListener();
			}

			return rootView;

		}

		/**
		 * 因为此Fragment重写了OnCreateView所以不再实现此方法了。
		 * @throws Exception 
		 */
		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
		}
	}

	@Override
	protected void setData(IHsLabelValue Object)
	{
		// TODO Auto-generated method stub
		
	}
	
	
}
