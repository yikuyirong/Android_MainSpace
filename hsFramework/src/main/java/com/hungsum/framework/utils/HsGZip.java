package com.hungsum.framework.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import android.util.Base64;

public abstract class HsGZip
{
	public static String CompressString(String value) throws IOException
	{
		return Base64
				.encodeToString(compress(value.getBytes()), Base64.DEFAULT);
	}

	private static byte[] compress(byte[] bytes) throws IOException
	{
		// 建立压缩文件输出流
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// 建立GZip压缩输出流
		GZIPOutputStream gzOutputStream = new GZIPOutputStream(outputStream);

		gzOutputStream.write(bytes);

		gzOutputStream.close();

		outputStream.close();

		return outputStream.toByteArray();
	}

	public static String DecompressString(String value) throws IOException
	{
		String returnString = new String(decompress(Base64.decode(value, Base64.DEFAULT)));

		return returnString;
	}

	private static byte[] decompress(byte[] bytes) throws IOException
	{
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
//		do
//		{
//			try
//			{
//				len = gzipInputStream.read(buffer,0,buffer.length);
//				outputStream.write(buffer,0,len);
//			} catch (Exception e){}
//		} while (len != -1);

		while ((len = gzipInputStream.read(buffer,0,buffer.length)) != -1)
		{
			outputStream.write(buffer, 0, len);
		}

		outputStream.flush();
		outputStream.close();
		gzipInputStream.close();
		inputStream.close();

		return outputStream.toByteArray();
	}
}
