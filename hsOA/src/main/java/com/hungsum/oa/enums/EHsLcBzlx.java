package com.hungsum.oa.enums;

import com.hungsum.framework.enums.EBase;


public class EHsLcBzlx extends EBase
{
	private static final long serialVersionUID = -3759287072655751035L;

	protected EHsLcBzlx(int value)
	{
		super(value);
	}

	public static EHsLcBzlx 通知确认类 = new EHsLcBzlx(1);
	
	public static EHsLcBzlx 同意审批类 = new EHsLcBzlx(0);
}
