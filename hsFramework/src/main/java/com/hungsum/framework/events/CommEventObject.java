package com.hungsum.framework.events;

import java.util.EventObject;

public class CommEventObject extends EventObject
{

	private static final long serialVersionUID = 2033129080436148373L;

	private Object[] mDatas;
	
	public CommEventObject(Object source,Object data)
	{
		super(source);
		
		this.SetData(data);
	}
	
	public CommEventObject(Object source,Object[] datas)
	{
		super(source);
		
		this.SetDatas(datas);
	}
	
	public void SetDatas(Object[] datas)
	{
		mDatas = datas;
	}
	
	public void SetData(Object data)
	{
		mDatas = new Object[1];
		mDatas[0] = data;
	}
	
	public Object[] GetDatas()
	{
		return mDatas;
	}
	
	public Object GetData(int index)
	{
		if(mDatas != null && mDatas.length > index)
		{
			return mDatas[index];
		}else {
			return null;
		}
	}
	
	public Object GetData()
	{
		if(mDatas != null && mDatas.length > 0)
		{
			return mDatas[0];
		}else {
			return null;
		}
	}

}
