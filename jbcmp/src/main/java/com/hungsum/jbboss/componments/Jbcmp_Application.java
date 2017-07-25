package com.hungsum.jbboss.componments;

import android.preference.PreferenceManager;

import com.hungsum.framework.componments.HsApplication;
import com.hungsum.jbboss.webservices.JbcmpWebService;

public class Jbcmp_Application extends HsApplication<JbcmpWebService>
{

	@Override
	protected JbcmpWebService initService()
	{
		String wsdl = PreferenceManager.getDefaultSharedPreferences(this).getString("wsdl","");
		wsdl = wsdl.equals("") ? (GetWebPath() + GetWSDLPath()) : wsdl;
		return new JbcmpWebService(wsdl,GetWSDLNameSpace());
	}

	@Override
	public String GetWSDLPath()
	{
		return "JbcmpService.asmx?wsdl";
	}

	@Override
	public String GetWebPath()
	{
		return "http://app.jiabaoruye.com.cn/Jbcmp/";
		//return "http://192.168.1.118/";
	}

	@Override
	public String GetApplicationName()
	{
		return "Jbcmp";
	}
}
