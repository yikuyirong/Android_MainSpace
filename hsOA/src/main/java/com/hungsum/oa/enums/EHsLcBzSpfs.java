package com.hungsum.oa.enums;

import com.hungsum.framework.enums.EBase;


public class EHsLcBzSpfs extends EBase
{
	private static final long serialVersionUID = 577213167872778962L;

	protected EHsLcBzSpfs(int value)
	{
		super(value);
	}

	public static EHsLcBzSpfs 至少一个 = new EHsLcBzSpfs(0);

	public static EHsLcBzSpfs 通知确认类 = new EHsLcBzSpfs(1);
	
	public static EHsLcBzSpfs 全部 = new EHsLcBzSpfs(2);
	
}
