package com.hungsum.framework.ui.activities;

import java.io.Serializable;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.hungsum.framework.componments.IHsLoginData;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.ui.controls.UcFormItem;
import com.hungsum.framework.ui.controls.UcPassword;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;

public class HsActivity_ChangePassword extends HsActivity_ZD
{
	private UcPassword _ucOldPassword;
	
	private UcPassword _ucNewPassword;
	
	private UcPassword _ucNewPassword2;

	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		this.inTitle = "更改密码";
	}

	@Override
	protected HsFragment_ZD_Main createMainFragment()
	{
		return new ChangePasswordFragment();
	}
	
	@Override
	protected void validate() throws Exception
	{
		super.validate();
		
		if(!_ucNewPassword.getControlValue().equals(_ucNewPassword2.getControlValue()))
		{
			throw new Exception("新密码和确认密码不一致。");
		}
	}

	@Override
	protected HsWSReturnObject updateOnOtherThread() throws Exception
	{
		IHsLoginData loginData = application.getLoginData();
		
		return application.getWebService().changePassword(
				loginData.getProgressId(),
				_ucOldPassword.getControlValue().toString(),
				_ucNewPassword.getControlValue().toString());
	}
	
	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if(funcname.equals("ChangePassword"))
		{
			ShowInformation("密码更新成功。");
			
			this.finish();
		}
	}

	private class ChangePasswordFragment extends HsFragment_ZD_Main
	{

		@Override
		protected void initMainView(Context context, LinearLayout mainView)
		{
			_ucOldPassword = new UcPassword(context);
			_ucOldPassword.setCName("原密码");
			_ucOldPassword.setAllowEmpty(false);
			controls.add(_ucOldPassword);
			mainView.addView(new UcFormItem(context, _ucOldPassword).GetView());
			
			_ucNewPassword = new UcPassword(context);
			_ucNewPassword.setCName("新密码");
			_ucNewPassword.setAllowEmpty(false);
			controls.add(_ucNewPassword);
			mainView.addView(new UcFormItem(context, _ucNewPassword).GetView());
			
			_ucNewPassword2 = new UcPassword(context);
			_ucNewPassword2.setCName("密码确认");
			_ucNewPassword2.setAllowEmpty(false);
			controls.add(_ucNewPassword2);
			mainView.addView(new UcFormItem(context, _ucNewPassword2).GetView());

		}
	}

	@Override
	protected void setData(IHsLabelValue Object)
	{
		// TODO Auto-generated method stub
		
	}

}
