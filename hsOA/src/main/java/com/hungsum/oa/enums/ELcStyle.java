package com.hungsum.oa.enums;

import com.hungsum.framework.enums.EBase;


public class ELcStyle extends EBase
{
	private static final long serialVersionUID = 2477235216146046613L;

	protected ELcStyle(int value)
	{
		super(value);
	}

	public static ELcStyle 规则流程 = new ELcStyle(0);

	public static ELcStyle 自由流程 = new ELcStyle(1);

}
