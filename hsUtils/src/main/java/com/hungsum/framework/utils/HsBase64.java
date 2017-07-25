package com.hungsum.framework.utils;

import java.io.UnsupportedEncodingException;

import android.util.Base64;

public class HsBase64
{
	public static String encode(byte[] bytes)
	{
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}
	
	public static byte[] decode(String data)
	{
		return Base64.decode(data, Base64.DEFAULT);
	}
	
	public static String encodeString(String value) throws UnsupportedEncodingException
	{
		return encode(value.getBytes("UTF_8"));
	}
	
	public static String decodeString(String value) throws UnsupportedEncodingException
	{
		byte[] result = decode(value);
		
		return new String(result,"UTF_8");
	}
}
