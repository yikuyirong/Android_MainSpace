package com.hungsum.framework.componments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import com.hungsum.framework.utils.HsZlib;

public class HsBitmap implements Serializable,Comparable<HsBitmap>
{

	private static final long serialVersionUID = -8150229291893882761L;
	
	private static byte ENDIAN_LITTLE = 0;
	
	private static byte ENDIAN_BIG = 1;
	
	private static int METALEN = 128;

	public static int PIXEL = 0;
	
	public static int JPG = 1;
	
	public static int PNG = 2;
	
	public int ImageId;
	
	public String Class;
	
	public String ClassId;
	
	public String HashType;
	
	public String HashData;
	
	private Bitmap mBitmap;
	
	private String mBitmapString;
	
	private int mWidth;
	
	private int mHeight;
	
	private String mStorePath;

	//顺序号 图片的顺序。
	private int sequ;
	
	public HsBitmap()
	{
		
	}
	
	public HsBitmap(Bitmap bitmap)
	{
		setBitmap(bitmap);
	}
	
	public HsBitmap(String bitmapString)
	{
		setBitmapString(bitmapString);
	}
	
	public void setBitmap(Bitmap bitmap)
	{
		mBitmap = bitmap;
		
		mWidth = bitmap.getWidth();
		
		mHeight = bitmap.getHeight();
		
		mBitmapString = this._getStringFromBitmap(bitmap, JPG);
	}
	
	public void setBitmapString(String bitmapString)
	{
		mBitmapString = bitmapString;
		
		mBitmap = this._getBitmapFromString(bitmapString);
		
		mWidth = mBitmap.getWidth();
		
		mHeight = mBitmap.getHeight();
	}
	
	public int getWidth()
	{
		return mWidth;
	}
	
	public int getHeight()
	{
		return mHeight;
	}
	
	public void setStorePath(String value)
	{
		mStorePath = value;
	}
	
	public Bitmap getBitmap()
	{
		return mBitmap == null ? mBitmap = _getBitmapFromString(mBitmapString) : mBitmap;
	}
	
	public String getBitmapString()
	{
		return mBitmapString == null ? mBitmapString = _getStringFromBitmap(mBitmap,JPG) : mBitmapString;
	}
	
	public String getStorePath()
	{
		return mStorePath;
	}
	
	public HsBitmap getPreviewHsBitmap(int width)
	{
		float scaleX = (float)width / getBitmap().getWidth();

		return getPreviewHsBitmap(scaleX, scaleX);
	}
	
	public HsBitmap getPreviewHsBitmap(int width,int height)
	{
		float scaleX = (float)width / getBitmap().getWidth();
		float scaleY = (float)height / getBitmap().getHeight();
		
		return getPreviewHsBitmap(scaleX, scaleY);
	}
	
	public HsBitmap getPreviewHsBitmap(float scaleX)
	{
		return getPreviewHsBitmap(scaleX, scaleX);
	}
	
	public HsBitmap getPreviewHsBitmap(float scaleX,float scaleY)
	{
		Matrix matrix = new Matrix();
		matrix.postScale(scaleX, scaleY);
		
		 // 创建新的图片
        return new HsBitmap(Bitmap.createBitmap(getBitmap(), 0, 0,mWidth,mHeight, matrix, true));	
    }
	
	public Integer getSequ()
	{
		return sequ;
	}
	
	public void setSequ(int sequ)
	{
		this.sequ = sequ;
	}
	
	/**
	 * 转换字符串到Bitmap对象
	 * @param data
	 * @return
	 */
	private Bitmap _getBitmapFromString(String data)
	{
		byte[] sourceBs = Base64.decode(data, Base64.DEFAULT);
		
		ByteBuffer sourceBuffer = ByteBuffer.wrap(sourceBs);
		
		if(sourceBuffer.get() == ENDIAN_LITTLE)
		{
			sourceBuffer.order(ByteOrder.LITTLE_ENDIAN);
		}else {
			sourceBuffer.order(ByteOrder.BIG_ENDIAN);
		}

		int format = sourceBuffer.getInt();
		int width = sourceBuffer.getInt();
		int height = sourceBuffer.getInt();
		
		sourceBuffer.position(METALEN);
		
		byte[] dataBs = new byte[sourceBs.length - METALEN];
		
		sourceBuffer.get(dataBs);

		Bitmap image;
		
		if(format == PIXEL)
		{
			dataBs = HsZlib.decompress(dataBs);
			
			image = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			
			for(int y = 0;y < height ; y++)
			{
				for(int x = 0;x < width ; x++)
				{
					int color = Color.argb(
							dataBs[(y * width + x) * 4] & 0xff,
							dataBs[(y * width + x) * 4 + 1] & 0xff,
							dataBs[(y * width + x) * 4 + 2] & 0xff,
							dataBs[(y * width + x) * 4 + 3] & 0xff);
					image.setPixel(x, y, color); 
				}
			}
		}else {
			image = BitmapFactory.decodeByteArray(dataBs, 0, dataBs.length);
		}

		return image;
	}
	
	private String _getStringFromBitmap(Bitmap image,int format)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		
		byte[] dataBs;

		if(format == JPG)
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();

			image.compress(Bitmap.CompressFormat.JPEG,80,stream);
			
			dataBs = stream.toByteArray();
		}else if(format == PNG)
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();

			image.compress(Bitmap.CompressFormat.PNG,100,stream);
			
			dataBs = stream.toByteArray();
		}else {
			dataBs = new byte[width * height * 4];
			
			for(int y = 0 ; y < height ; y++)
			{
				for(int x = 0;x < width ; x++)
				{
					int color = image.getPixel(x, y);
					dataBs[(y * width + x) * 4] = (byte) Color.alpha(color);
					dataBs[(y * width + x) * 4 + 1] = (byte) Color.red(color);
					dataBs[(y * width + x) * 4 + 2] = (byte) Color.green(color);
					dataBs[(y * width + x) * 4 + 3] = (byte) Color.blue(color);
				}
			}
			
			dataBs = HsZlib.compress(dataBs);
		}
		
		ByteBuffer buffer = ByteBuffer.wrap(new byte[METALEN + dataBs.length]);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(ENDIAN_LITTLE);
		buffer.putInt(format);
		buffer.putInt(width);
		buffer.putInt(height);
		
		buffer.position(METALEN);
		buffer.put(dataBs,0,dataBs.length);

		return Base64.encodeToString(buffer.array(),Base64.DEFAULT);

	}

	public void saveHsBitmapToDisk(Uri uri) throws Exception
	{
		saveHsBitmapToDisk(uri.getPath());
	}
	
	public void saveHsBitmapToDisk(String storePath) throws Exception
	{
		FileOutputStream out = null;
		try
		{
			out = new FileOutputStream(storePath);
			if (!this.getBitmap().compress(Bitmap.CompressFormat.PNG, 80, out))
			{
				throw new Exception("图像保存失败");
			}
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			if(out != null)
			{
				out.close();
			}
		}
	}
	
	public static Bitmap loadBitmapFromDisk(Context context, Uri uri) throws FileNotFoundException, IOException
	{
		// 读取URI所在的图片
		return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
	}

	public static Bitmap loadBitmapFromDisk(Context context,String path) throws FileNotFoundException, IOException
	{
		return loadBitmapFromDisk(context,Uri.fromFile(new File(path)));
	}	
	
	@Override
	public int compareTo(HsBitmap another)
	{
		return getSequ().compareTo(another.getSequ());
	}
	
}
