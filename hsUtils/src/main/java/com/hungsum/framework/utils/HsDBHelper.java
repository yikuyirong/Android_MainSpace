package com.hungsum.framework.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;

public class HsDBHelper extends SQLiteOpenHelper
{
	public final static int INFORMATION = 0x0;

	public final static int ERROR = -0x1;

	public HsDBHelper(Context context, String name, CursorFactory factory,
					  int version)
	{
		super(context, name, factory, version);
	}

	public HsDBHelper(Context context, String name, CursorFactory factory,
					  int version, DatabaseErrorHandler errorHandler)
	{
		super(context, name, factory, version, errorHandler);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// HsLog
		String sql = "CREATE TABLE HSLOG (LOG_DATE DATETIME NOT NULL,LOG_LEVEL INT NOT NULL, LOG_MESSAGE TEXT NOT NULL)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	}

	/**
	 * 记录日志
	 *
	 * @param level
	 *            日志登记 0 普通信息 -1 错误信息
	 * @param message
	 */
	public void log(int level, String message)
	{
		if (message == null)
		{
			message = "No description";
		}

		String sql = "INSERT INTO HSLOG (LOG_DATE,LOG_LEVEL,LOG_MESSAGE) VALUES (?,?,?)";

		SQLiteDatabase db = this.getWritableDatabase();
		try
		{
			db.execSQL(sql, new Object[]
					{ HsDate.TransDateTimeToString(new Date()), level, message });
		} catch (Exception e)
		{
			Log.e("", e.getMessage());
		} finally
		{
			db.close();
			switch (level)
			{
				case INFORMATION:
					Log.i("", message);
					break;
				case ERROR:
					Log.e("", message);
					break;
			}
		}
	}

	/**
	 * 清除过期日志
	 *
	 * @param day
	 *            日志保留天数
	 * @throws
	 */
	public void clearLog(int day) throws Exception
	{
		String date = HsDate.TransDateToString("yyyy-MM-dd HH:mm:ss", new Date(
				System.currentTimeMillis() - day * 24 * 60 * 60 * 1000));

		execSqls(new ExecSqlObject[]
				{ new ExecSqlObject("DELETE FROM HSLOG WHERE LOG_DATE < ?",
						new Object[]
								{ date }) });
	}

	public void execSqls(ExecSqlObject[] datas) throws Exception
	{
		SQLiteDatabase db = this.getWritableDatabase();

		try
		{
			db.beginTransaction();

			for (int i = 0; i < datas.length; i++)
			{
				ExecSqlObject object = datas[i];
				db.execSQL(object.getSql(), object.getBindArgs());
			}

			db.setTransactionSuccessful();
		} catch (Exception e)
		{
			log(ERROR, e.getMessage());

			throw e;
		} finally
		{
			db.endTransaction();

			db.close();
		}

	}

	public Cursor rawQuery(String sql, String[] selectionArgs) throws Exception
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(sql, selectionArgs);

		return cursor;
	}

	public static class ExecSqlObject
	{
		private String mSql;

		private Object[] mBindArgs;

		public ExecSqlObject(String sql, Object[] bindArgs)
		{
			mSql = sql;
			mBindArgs = bindArgs;
		}

		public String getSql()
		{
			return mSql;
		}

		public Object[] getBindArgs()
		{
			return mBindArgs;
		}
	}

}
