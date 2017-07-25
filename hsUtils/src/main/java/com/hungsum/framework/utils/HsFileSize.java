package com.hungsum.framework.utils;

public class HsFileSize
{
	public static String Size2String(int bytecount)
	{
		if(bytecount < 1024)
		{
			return bytecount + "B";
		}else if(bytecount < 1024 * 1024)
		{
			return Math.round((bytecount * 100.0 / 1024)) / 100 + "KB";
		}else if(bytecount < 1024 * 1024 * 1024)
		{
			return Math.round(bytecount * 100.0 / (1024 * 1024)) / 100 + "MB";
		}else
		{
			return Math.round(bytecount * 100.0 / (1024 * 1024 * 1024)) / 100 + "GB";
		}
	}
}
