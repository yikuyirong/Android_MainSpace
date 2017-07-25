package com.hungsum.framework.interfaces;

import java.io.Serializable;
import java.util.List;

import android.graphics.drawable.Drawable;

import com.hungsum.framework.componments.HsLabelValue;

public interface IHsLabelValue extends Serializable
{
	String getLabel(); //显示Label内容

	Object getValue(); //显示Key内容
	
	void setValue(Object value);
	
	int getColor(); //显示Label Key 颜色
	
	boolean getIsShowDetailImage();
	
	Drawable getOperationImage();
	
	List<IHsLabelValue> getDetails();

	int getDetailsCount();
	
	Object getValue(String key,Object defaultValue);

	void addDetail(HsLabelValue detail);
}