package com.hungsum.jbboss.webservices;

import org.ksoap2.serialization.SoapObject;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.utils.HsGZip;
import com.hungsum.oa.webservices.HSOAWebService;

public class JbbossWebService extends HSOAWebService
{
	public JbbossWebService(String wsdl, String namespace)
	{
		super(wsdl, namespace);
	}

	//region 登录

	@Override
	public HsWSReturnObject login(CharSequence username, CharSequence password, CharSequence connString, CharSequence args) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<LoginName>" + username + "</LoginName>");
		sb.append("<Password>" + password + "</Password>");
		sb.append("<ConnString>" + connString + "</ConnString>");
		sb.append("</Data>");


		String funcName = "SnyLogin";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}


	//endregion

	//region 客户信息
	
	public HsWSReturnObject showJbKhzds(String progressId, String nzbh, String rybh,String khlb) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<Nzbh>" + nzbh + "</Nzbh>");
		sb.append("<Rybh>" + rybh + "</Rybh>");
		sb.append("<Khlb>" + khlb + "</Khlb>");
		sb.append("</Data>");
		
		String funcName = "ShowJbKhzds";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	public HsWSReturnObject updateJbKhzd(
			CharSequence progressId,
			CharSequence khbh,
			CharSequence khmc,
			CharSequence dqbh,
			CharSequence dz,
			CharSequence gddh,
			CharSequence yddh,
			int flag) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<Khbh>" + khbh + "</Khbh>");
		sb.append("<Khmc>" + khmc + "</Khmc>");
		sb.append("<Dqbh>" + dqbh + "</Dqbh>");
		sb.append("<Dz>" + dz + "</Dz>");
		sb.append("<Gddh>" + gddh + "</Gddh>");
		sb.append("<Yddh>" + yddh + "</Yddh>");
		sb.append("<Flag>" + flag + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateJbKhzd";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	//endregion
	
	//region 送奶员订单
	
	public HsWSReturnObject showJbSnydndjs(String progressId,String beginDate,String endDate,String djzt) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("<Djzt>" + djzt + "</Djzt>");
		sb.append("</Data>");
		
		String funcName = "ShowJbSnydndjs";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject updateJbSnydndjs(String progressId,String djId,String khbh,
			String ksrq,String jsrq,String bz,String strMx,int flag) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<DjId>" + djId + "</DjId>");
		sb.append("<Khbh>" + khbh + "</Khbh>");
		sb.append("<Ksrq>" + ksrq + "</Ksrq>");
		sb.append("<Jsrq>" + jsrq + "</Jsrq>");
		sb.append("<Bz>" + bz + "</Bz>");
		sb.append("<StrMx>" + strMx + "</StrMx>");
		sb.append("<Flag>" + flag + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateJbSnydndjs";

		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	public HsWSReturnObject getJbCgspd(String progressId,String djId) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<DjId>" + djId + "</DjId>");
		sb.append("</Data>");

		String funcName = "GetJbCgspd";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	//endregion

	//region 产品信息

	public HsWSReturnObject showJbCpzds(String progressId) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("</Data>");

		String funcName = "ShowJbCpzds";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
}



	//endregion
}
