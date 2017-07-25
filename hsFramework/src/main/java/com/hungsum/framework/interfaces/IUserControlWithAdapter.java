package com.hungsum.framework.interfaces;

import java.util.List;

public interface IUserControlWithAdapter
{
	void SetDatas(List<IHsLabelValue> datas);
	
	void SetOnRetrieveListener(IOnRetrieveListener listener);
	
	String GetFlag();

}
