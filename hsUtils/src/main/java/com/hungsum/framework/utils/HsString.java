package com.hungsum.framework.utils;

/**
 * Created by zhaixuan on 2017/7/28.
 */

public class HsString
{
    public static String padLeft(String str, int len,char chr)
    {
        len = len - str.length();

        if(len > 0)
        {
            StringBuilder sb = new StringBuilder();

            for(int i = 0;i<len;i++)
            {
                sb.append(chr);
            }

            return sb.toString() + str;

        }else
        {
            return str;
        }
    }
}
