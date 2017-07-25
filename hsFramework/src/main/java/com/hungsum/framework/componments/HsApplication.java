package com.hungsum.framework.componments;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;

import android.app.Application;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.hungsum.framework.webservices.HsWebService;

public abstract class HsApplication<T extends HsWebService> extends Application
{
	
	/**
	 * 是否允许配置网络地址，如果为真，则会在登录界面显示配置入口
	 */
	public boolean AllowConfigWSDL = false;
	
	/**
	 * 是否验证设备ID，如果为真，调用Login_M时会传递设备的唯一性ID。
	 */
	public boolean ValidateDeviceSn = false;
	
	protected T webServcice;
	
	public T getWebService()
	{
		return webServcice == null ? initService() : webServcice;
	}
	
	protected abstract T initService();
	
	private HashMap<String, Object> mDatas;
	
	private IHsLoginData mLoginData;

	/**
	 * 设置缓存数据
	 * @param key
	 * @param value
	 */
	public void SetData(String key,Object value)
	{
		if(mDatas == null)
		{
			mDatas = new HashMap<String, Object>();
		}
		mDatas.put(key, value);
	}
	
	/**
	 * 获取缓存数据
	 * @param key
	 * @return
	 */
	public Object GetData(String key)
	{
		return mDatas == null ? null : mDatas.get(key);
	}

	public IHsLoginData getLoginData()
	{
		return mLoginData;
	}

	public void setLoginData(IHsLoginData _loginData)
	{
		this.mLoginData = _loginData;
	}
	
	public String GetWSDLNameSpace()
	{
		return "http://Hungsum.com/";
	}
	
	public abstract String GetWSDLPath();

	public abstract String GetWebPath();
	
	public abstract String GetApplicationName();

	
	/** 根据图片名称获取图片资源ID
	 * @param picName
	 * @return
	 */
	public int GetDrawableResIdByPicName(String picName)
	{
		if(picName != null)
		{
	        int id = this.getResources().getIdentifier(picName.toLowerCase(Locale.getDefault()), "drawable",getPackageName());
	        return Math.max(0, id);
		}else {
			return 0;
		}
	}
	
	
	/** 根据图片名称获取图片资源
	 * @param picName
	 * @return
	 */
	public Drawable GetDrawableByPicName(String picName) 
	{
		int id = GetDrawableResIdByPicName(picName);
		if(id > 0)
		{
			return this.getResources().getDrawable(id);
		}else 
		{
			return null;
		}
    }
	
	/** 获取序列号
	 * @return
	 */
	public String GetSerialNumber()
	{
		String serialnum = "FFFFFFFFFFFFFFFF";                                                                                                                                        
		try {                                                           
		 Class<?> c = Class.forName("android.os.SystemProperties"); 
		 Method get = c.getMethod("get", String.class, String.class );     
		 serialnum = (String)(get.invoke(c, "ro.serialno", serialnum));   
		}                                                                                
		catch (Exception ignored)                                                        
		{                              
		}
		return serialnum;
	}
	
	
	/** 获取软件版本号
	 * @return
	 * @throws NameNotFoundException 
	 */
	public String GetVersion()
	{
		try
		{
			return this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e)
		{
			return "0.0.0.0";
		}
	}
	
	public String getDownloadRootPath() throws Exception
	{
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			throw new Exception("扩展存储未准备好，不能下载更新。");
		}
		// 下载目录
		return Environment.getExternalStorageDirectory()
				+ "/HSDownload/" + getPackageName() + "/";	
	}

	/** 检查是否需要升级
	 * @param newVersion
	 * @return
	 * @throws NameNotFoundException
	 */
	public int CheckNeedUpdate(String newVersion) throws NameNotFoundException
	{
		if(compareVersion(newVersion, GetVersion()) > 0)
		{
			String[] aVer = newVersion.split("\\.");
			int n = Integer.parseInt(aVer[aVer.length - 1]);
			if(n % 2 == 0)
			{
				return 2; //强制升级
			}else {
				return 1; //建议升级
			}
		}else {
			return 0; //不需要升级
		}
			
	}
	
	private int compareVersion(String ver1,String ver2)
	{
		String[] aVer1 = ver1.split("\\.");
		String[] aVer2 = ver2.split("\\.");
		for(int i=0;i< Math.max(aVer1.length, aVer2.length);i++)
		{
			if(aVer1.length == i)
			{
				return -1;
			}
			if(aVer2.length == i)
			{
				return 1;
			}

			if(Integer.parseInt(aVer1[i]) != Integer.parseInt(aVer2[i]))
			{
				return Integer.parseInt(aVer1[i]) - Integer.parseInt(aVer2[i]);
			}
		}
		return 0;
	}
}
