package com.hungsum.jbboss.componments;

import android.preference.PreferenceManager;

import com.hungsum.framework.componments.HsApplication;
import com.hungsum.jbboss.webservices.JbbossWebService;

public class Jbboss_Application extends HsApplication<JbbossWebService>
{

	@Override
	protected JbbossWebService initService()
	{
		String wsdl = PreferenceManager.getDefaultSharedPreferences(this).getString("wsdl","");
		wsdl = wsdl.equals("") ? (GetWebPath() + GetWSDLPath()) : wsdl;
		return new JbbossWebService(wsdl,GetWSDLNameSpace());
	}

	@Override
	public String GetWSDLPath()
	{
		return "JbcmpWebService.asmx?wsdl";
	}

	@Override
	public String GetWebPath()
	{
		//return "http://app.jiabaoruye.com.cn/Jbcmp/";
		return "http://192.168.1.227/";
	}

	@Override
	public String GetApplicationName()
	{
		return "Jbboss";
	}
}
