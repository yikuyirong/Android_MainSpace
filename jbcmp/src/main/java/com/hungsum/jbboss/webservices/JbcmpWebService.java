package com.hungsum.jbboss.webservices;

import org.ksoap2.serialization.SoapObject;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.utils.HsGZip;
import com.hungsum.oa.webservices.HSOAWebService;

public class JbcmpWebService extends HSOAWebService
{
	public JbcmpWebService(String wsdl, String namespace)
	{
		super(wsdl, namespace);
	}
	
	//{{ 信息记录
	
	public HsWSReturnObject ShowXxjls(String progressId,String beginDate,String endDate) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("</Data>");
		
		String funcName = "ShowXxjls";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject UpdateXxjl(
			CharSequence progressId,
			CharSequence jlId,
			CharSequence xxly,
			CharSequence jsr,
			CharSequence xxlx,
			CharSequence bh,
			CharSequence zy,
			int flag) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<JlId>" + jlId + "</JlId>");
		sb.append("<Xxly>" + xxly + "</Xxly>");
		sb.append("<Jsr>" + jsr + "</Jsr>");
		sb.append("<Xxlx>" + xxlx + "</Xxlx>");
		sb.append("<Bh>" + bh + "</Bh>");
		sb.append("<Zy>" + zy + "</Zy>");
		sb.append("<Flag>" + flag + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateXxjl";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	// }}
	
	//{{ 采购审批单
	
	public HsWSReturnObject showJbCgspds(String progressId,String beginDate,String endDate) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("<Spzt>2</Spzt>");
		sb.append("</Data>");
		
		String funcName = "ShowJbCgspds";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject updateJbCgspd(String progressId,String djId,String djrq,
			String cgbt,String sfjj,
			String cgyy,String bz,
			String strMx,int flag) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<DjId>" + djId + "</DjId>");
		sb.append("<Djrq>" + djrq + "</Djrq>");
		sb.append("<Cgbt>" + cgbt + "</Cgbt>");
		sb.append("<Sfjj>" + sfjj + "</Sfjj>");
		sb.append("<Cgyy>" + cgyy + "</Cgyy>");
		sb.append("<Bz>" + bz + "</Bz>");
		sb.append("<StrMx>" + strMx + "</StrMx>");
		sb.append("<Flag>" + flag + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateJbCgspd";

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

	//}}
}
