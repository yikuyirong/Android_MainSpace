package com.hungsum.oa.enums;

import com.hungsum.framework.enums.EBase;


public class ELcJlzt extends EBase
{
	private static final long serialVersionUID = -924271306649160560L;

	protected ELcJlzt(int value)
	{
		super(value);
	}

	public static ELcJlzt 待审批 = new ELcJlzt(0);

	public static ELcJlzt 审批同意 = new ELcJlzt(1);
	
	public static ELcJlzt 审批驳回 = new ELcJlzt(2);
	
}
