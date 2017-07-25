package com.hungsum.framework.webservices;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.location.Location;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.models.ModelWsReturnObject;
import com.hungsum.framework.utils.HsBase64;
import com.hungsum.framework.utils.HsDate;
import com.hungsum.framework.utils.HsGZip;

/**
 * 
 * 
 * @author zhaixuan
 * 服务类，封装了所有调用ws的方法。
 *
 *
 */
public class HsWebService
{
	private final String _wsdl;
	
	protected final String namespace;
	

	private HttpTransportSE _httpTransportSE;
	
	private SoapSerializationEnvelope _envEnvelope;
	
	public HsWebService(String wsdl,String namespace)
	{
		this._wsdl = wsdl;
		this.namespace = namespace;

		this._httpTransportSE = new HttpTransportSE(this._wsdl);
		
		this._envEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		this._envEnvelope.dotNet = true;
	}

	protected HsWSReturnObject formatReturnData(String funcName, SoapObject request) throws Exception
	{
		this._envEnvelope.bodyOut = request;

		try
		{
			this._httpTransportSE.call(this.namespace + funcName, this._envEnvelope);
		} catch (IOException e)
		{
			throw new Exception("通道错误，请检查联网情况与服务器状态。");
		} catch (XmlPullParserException e) 
		{
			throw new Exception("格式错误，请检查服务器状态。");
		}
		if(this._envEnvelope.getResponse() != null)
		{
			SoapObject response = (SoapObject)_envEnvelope.bodyIn;

			HsWSReturnObject returnObject = new ModelWsReturnObject().Create(response.getProperty(0).toString());

			if(returnObject.GetCodeNum() != HsWSReturnObject.SUCCESS)
			{
				throw new Exception(returnObject.GetData().toString());
			}
			return returnObject;

		}else {
			throw new Exception("在框架中不能调用返回值为空的方法。");
		}
	}
	
	//{{ 测试
	
	public HsWSReturnObject hello() throws Exception
	{
		String funcName = "Hello";
		SoapObject request = new SoapObject(this.namespace, funcName);

		return this.formatReturnData(funcName, request);
	}
	
	//}}
	
	//{{ 系统功能 登录 注销 修改密码 版本控制

	
	public HsWSReturnObject getDbs() throws Exception
	{
		String funcName = "GetDbs";
		SoapObject request = new SoapObject(this.namespace, funcName);

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject login(CharSequence username,CharSequence password,CharSequence connString ,CharSequence args) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<UserName>" + username + "</UserName>");
		sb.append("<Password>" + password + "</Password>");
		sb.append("<ConnString>" + connString + "</ConnString>");
		sb.append("<ClientType>1</ClientType>");
		if(args != null)
		{
			sb.append("<Args>" + args + "</Args>");
		}
		sb.append("</Data>");
		
		
		String funcName = "Login";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	public HsWSReturnObject showSysRoleUsers(String progressId) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("</Data>");

		String funcName = "ShowSysRoleUsers";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject showSysDeptRoleUserName(String progressId) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("</Data>");

		String funcName = "ShowSysDeptRoleUserName";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	public HsWSReturnObject logout(String progressId) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("</Data>");

		String funcName = "Logout";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject changePassword(String progressId,String oldpassword,String newpassword) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<OldPassword>" + oldpassword + "</OldPassword>");
		sb.append("<NewPassword>" + newpassword + "</NewPassword>");
		sb.append("</Data>");

		String funcName = "ChangePassword";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",progressId);

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject getAndroidClientInfo() throws Exception
	{
		String funcName = "GetAndroidClientInfo";
		SoapObject request = new SoapObject(this.namespace, funcName);

		return this.formatReturnData(funcName, request);
	}
	
	//}}
	
	//{{ 单据处理
	
	public HsWSReturnObject doDatas(String progressId,String xIds,String flag) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append(xIds);
		sb.append("<ActionFlag>" + flag + "</ActionFlag>");
		sb.append("</Data>");

		String funcName = "DoDatas";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	//}}
	
	//{{ 字典
	
	public HsWSReturnObject getAutoCompleteData(String progressId,String flag,String params) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<Flag>" + flag + "</Flag>");
		sb.append("<Args>" + (params == null ? "" : params) + "</Args>");
		sb.append("</Data>");


		String funcName = "GetAutoCompleteData";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	//}}
	
	//{{ 查询
	
	public HsWSReturnObject getQueryNameAndArgs(String progressId,String queryName) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<QueryName>" + queryName + "</QueryName>");
		sb.append("<QueryFlag>2</QueryFlag>");
		sb.append("</Data>");

		String funcName = "GetQueryNameAndArgs";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject queryResult(String progressId,String queryName,String args) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<QueryName>" + queryName + "</QueryName>");
		sb.append("<QueryFlag>2</QueryFlag>");
		sb.append(args);
		sb.append("</Data>");

		String funcName = "QueryResult";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	//}}
	
	//{{ 图像操作
	
    public HsWSReturnObject getSysImageHashDatas(String progressId,String Class,String classId) throws Exception
    {
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<Class>" + Class + "</Class>");
		sb.append("<ClassId>" + classId + "</ClassId>");
		sb.append("</Data>");

		String funcName = "GetSysImageHashDatas";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
    }
	
	public HsWSReturnObject getSysImageById(String progressId,int imageId) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<ImageId>" + imageId + "</ImageId>");
		sb.append("</Data>");

		String funcName = "GetSysImageById";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	public HsWSReturnObject deleteSysImages(String progressId,String Class,String classId,int maxId) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<Class>" + Class + "</Class>");
		sb.append("<ClassId>" + classId + "</ClassId>");
		sb.append("<MaxId>" + maxId + "</MaxId>");
		sb.append("</Data>");

		String funcName = "DeleteSysImages";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject updateSysImage(String progressId,String Class,String classId,String imageData) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<Class>" + Class + "</Class>");
		sb.append("<ClassId>" + classId + "</ClassId>");
		sb.append("<ImageData>" + imageData + "</ImageData>");
		sb.append("</Data>");

		String funcName = "UpdateSysImage";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject updateSysImageByHashData(String progressId,String Class,String classId,String hashData) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<Class>" + Class + "</Class>");
		sb.append("<ClassId>" + classId + "</ClassId>");
		sb.append("<HashData>" + hashData + "</HashData>");
		sb.append("</Data>");

		String funcName = "UpdateSysImageByHashData";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	//}}

	//{{ 文件操作
	
	public HsWSReturnObject getSysFiles(String progressId,String Class,String classId) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<Class>" + Class + "</Class>");
		sb.append("<ClassId>" + classId + "</ClassId>");
		sb.append("</Data>");

		String funcName = "GetSysFiles";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject getSysFileSeg(String progressId,String hashData,int order) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<HashData>" + hashData + "</HashData>");
		sb.append("<Order>" + order + "</Order>");
		sb.append("</Data>");

		String funcName = "GetSysFileSeg";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject checkFileStatus(String progressId,String Class,String classId,String xFile) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<Class>" + Class + "</Class>");
		sb.append("<ClassId>" + classId + "</ClassId>");
		sb.append(xFile);
		sb.append("</Data>");

		String funcName = "CheckFileStatus";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	public HsWSReturnObject updateSysFile(String progressId,String fileId,String Class,String classId,String filenameBase64,String fileType,String fileHashType,String fileHash,String fileSize,String editable) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<FileId>" + fileId + "</FileId>");
		sb.append("<Class>" + Class + "</Class>");
		sb.append("<ClassId>" + classId + "</ClassId>");
		sb.append("<FileNameBase64>" + filenameBase64 + "</FileNameBase64>");
		sb.append("<FileType>" + fileType + "</FileType>");
		sb.append("<FileHashType>" + fileHashType + "</FileHashType>");
		sb.append("<FileHash>" + fileHash + "</FileHash>");
		sb.append("<FileSize>" + fileSize + "</FileSize>");
		sb.append("<Editable>" + editable + "</Editable>");
		sb.append("</Data>");

		String funcName = "UpdateSysFile";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject updateSysFileSeg(String progressId,String hashData,byte[] buffer, int bufferSize,int order) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<HashData>" + hashData + "</HashData>");
		sb.append("<Size>" + bufferSize + "</Size>");
		sb.append("<Order>" + order + "</Order>");
		sb.append("<Buffer>" + HsBase64.encode(buffer) + "</Buffer>");
		sb.append("</Data>");

		String funcName = "UpdateSysFileSeg";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject confirmUpdateSysFile(String progressId,String fileId) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<FileId>" + fileId + "</FileId>");
		sb.append("</Data>");

		String funcName = "ConfirmUpdateSysFile";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	//}}
	
	//{{ 定位
	
	public HsWSReturnObject updateSysLocation(String progressId,String Class,String classId,Location location) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<Class>" + Class + "</Class>");
		sb.append("<ClassId>" + classId + "</ClassId>");
		sb.append("<Provider>" + location.getProvider() + "</Provider>");
		sb.append("<Accuracy>" + location.getAccuracy() + "</Accuracy>");
		sb.append("<Altitude>" + location.getAltitude() + "</Altitude>");
		sb.append("<Bearing>" + location.getBearing() + "</Bearing>");
		sb.append("<Latitude>" + location.getLatitude() + "</Latitude>");
		sb.append("<Longitude>" + location.getLongitude() + "</Longitude>");
		sb.append("<Speed>" + location.getSpeed() + "</Speed>");
		sb.append("<Time>" + HsDate.TransDateToString("yyyy-MM-dd HH:mm:ss", location.getTime()) + "</Time>");
		sb.append("</Data>");

		String funcName = "UpdateSysLocation";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	//}}
}
