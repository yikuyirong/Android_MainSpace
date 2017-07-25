package com.hungsum.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class HsDate
{
	/**
	 * ת�����ڵ��ַ���
	 * @param pattern ģ��
	 * @param date ����(������)
	 * @return
	 */
	public static String TransDateToString(String pattern, long date)
	{
		return TransDateToString(pattern,new Date(date));
	}
	
	/**
	 * ת�����ڵ��ַ���
	 * @param pattern ģ��
	 * @param date ����
	 * @return
	 */
	public static String TransDateToString(String pattern,Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(pattern,Locale.getDefault());
		
		return sdf.format(date);
	}
	
	/**
	 * ת�����ڵ��ַ��� yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String TransDateToString(Date date)
	{
		return TransDateToString("yyyy-MM-dd", date);
	}
	
	/**
	 * ת������ʱ�䵽�ַ��� yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String TransDateTimeToString(Date date)
	{
		return TransDateToString("yyyy-MM-dd HH:mm:ss", date);
	}
	
	public static Date TransStringToDate(String pattern,String date) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(pattern,Locale.getDefault());
		
		return sdf.parse(date);
	}
}
