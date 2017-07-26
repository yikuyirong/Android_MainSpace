package com.hungsum.framework.ui.controls;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.hungsum.framework.ui.activities.HsActivity;
import com.hungsum.framework.utils.HsDate;

public class UcDateBox extends UcTextBoxWithDialog
{
	private String mShowStyle = "DATE";
	
	//private Calendar mCalendar = Calendar.getInstance();

	private Date mCurrentDate = new Date();

	public UcDateBox(Context context)
	{
		super(context);
		
		this.setKeyListener(null);
		
		this.setOnClickListener(this);
	}
	
	public UcDateBox(Context context, AttributeSet attrs)
	{
		super(context,attrs);
		
		this.setKeyListener(null);
		
		this.setOnClickListener(this);
	}
	
	public UcDateBox(Context context, AttributeSet attrs, int defStyle )
	{
		super(context,attrs,defStyle);
		
		this.setKeyListener(null);
		
		this.setOnClickListener(this);
	}

	public Date getControlDate()
	{
		return mCurrentDate;
	}

	private void setControlValue(Date date)
	{
		mCurrentDate = date;

		if(this.mShowStyle.equals("YEARMONTH"))
		{
			super.setControlValue(HsDate.TransDateToString("yyyy-MM", date));
		}else if(this.mShowStyle.equals("YEAR"))
		{
			super.setControlValue(HsDate.TransDateToString("yyyy", date));
		}else {
			super.setControlValue(HsDate.TransDateToString("yyyy-MM-dd", date));
		}
	}

	@Override
	public void setControlValue(CharSequence value) 
	{
		try 
		{
			String dateFormatString = "yyyy-MM-dd";
			if(mShowStyle.equals("YEARMONTH"))
			{
				dateFormatString = "yyyy-MM";
			}else if(mShowStyle.equals("YEAR"))
			{
				dateFormatString = "yyyy";
			}

			this.setControlValue(HsDate.TransStringToDate(dateFormatString , value.toString()));

			//super.setControlValue(HsDate.TransDateToString(dateFormatString, HsDate.TransStringToDate(dateFormatString , value.toString())));

		} catch (Exception e) {
			super.setControlValue("");
		}
	}

	public UcDateBox SetShowStyle(String showStyle)
	{
		this.mShowStyle = showStyle.toUpperCase(Locale.getDefault());
		return this;
	}

	public void SetFlag(String flag)
	{
		this.setControlValueByFlag(flag);
	}
	
	private void setControlValueByFlag(String flag)
	{
		Calendar calendar = Calendar.getInstance();
		if (flag.toUpperCase(Locale.getDefault()).equals("MONTHFIRST"))
		{
			calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
			
		}else if (flag.toUpperCase(Locale.getDefault()).equals("YEARFIRST")) 
		{
			calendar.set(calendar.get(Calendar.YEAR), 1, 1);
		}

		this.setControlValue(calendar.getTime());
	}
	
	/*
	 * 根据标记返回对应日期字符串 yyyy-MM-dd
	 */
	public static String fromFlag(String flag)
	{
		Calendar calendar = Calendar.getInstance();
		if (flag.toUpperCase(Locale.getDefault()).equals("MONTHFIRST"))
		{
			calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
		}else if (flag.toUpperCase(Locale.getDefault()).equals("YEARFIRST")) 
		{
			calendar.set(calendar.get(Calendar.YEAR), 1, 1);
		}	
		return HsDate.TransDateToString("yyyy-MM-dd", calendar.getTime());
	}
	

	@Override
	protected void showDialog()
	{
		final Calendar calendar = Calendar.getInstance();

		try
		{
			if(!getControlValue().toString().equals(""))
			{
				if(mShowStyle.equals("YEAR"))
				{
					calendar.setTime(HsDate.TransStringToDate("yyyy",getControlValue().toString()));
				}else if(mShowStyle.equals("YEARMONTH"))
				{
					calendar.setTime(HsDate.TransStringToDate("yyyy-MM",getControlValue().toString()));
				}else
				{
					calendar.setTime(HsDate.TransStringToDate("yyyy-MM-dd",getControlValue().toString()));
				}
			}
			
		} catch (ParseException e) 
		{
			HsActivity activity = (HsActivity) getContext();
			activity.ShowError(e);
		}
		
		DatePickerDialog picker = null;
		
				
		if(this.mShowStyle.equals("YEARMONTH"))
		{
			picker = new YearMonthPickerDialog((UcDateBox.this).getContext(),

					new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth)
						{
							calendar.set(year, monthOfYear, dayOfMonth);
							setControlValue(calendar.getTime());
						}
					}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
		}else if(this.mShowStyle.equals("YEAR"))
		{
			picker = new YearPickerDialog((UcDateBox.this).getContext(),
					new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							calendar.set(year, monthOfYear, dayOfMonth);
							setControlValue(calendar.getTime());
						}
					}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
		}else {
			picker = new DatePickerDialog((UcDateBox.this).getContext(),
					new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							calendar.set(year, monthOfYear, dayOfMonth);
							setControlValue(calendar.getTime());
						}
					}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
		}
		
		picker.setIcon(android.R.drawable.ic_dialog_info);
		picker.setTitle("请选择" + (getCName() == null ? "" : getCName()));
		picker.show();
	}
	
	private class YearMonthPickerDialog extends DatePickerDialog
	{
		public YearMonthPickerDialog(Context context,
				OnDateSetListener callBack, int year, int monthOfYear,
				int dayOfMonth)
		{
			super(context, callBack, year, monthOfYear, dayOfMonth);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void show()
		{
			super.show();
			
			DatePicker dp = this.getDatePicker();
            if (dp != null) 
            {  
                Class<?> c=dp.getClass();  
                try 
                {
                	for (Field  f : c.getDeclaredFields())
					{
                		String name = f.getName();
                		if(name.equals("mDayPicker") || name.equals("mDaySpinner"))
                		{
                            f.setAccessible(true );    
                            ((LinearLayout)f.get(dp)).setVisibility(View.GONE);
                        }
					}
                } catch (Exception e) {}  
            }   			
		}
		
	}
	private class YearPickerDialog extends DatePickerDialog
	{
		public YearPickerDialog(Context context,
				OnDateSetListener callBack, int year, int monthOfYear,
				int dayOfMonth)
		{
			super(context, callBack, year, monthOfYear, dayOfMonth);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void show()
		{
			super.show();
			
			DatePicker dp = this.getDatePicker();
            if (dp != null) 
            {  
                Class<?> c=dp.getClass();  
                try {  
                	for (Field  f : c.getDeclaredFields())
					{
                		String name = f.getName();
                		if(name.equals("mDayPicker") || name.equals("mDaySpinner") ||
                				name.equals("mMonthPicker") || name.equals("mMonthSpinner"))
                		{
                            f.setAccessible(true );    
                            ((LinearLayout)f.get(dp)).setVisibility(View.GONE);
                        }
					}
                } catch (Exception e) {}  
            }   			
			
		}
		
	}
}
