package com.hungsum.oa.enums;

import com.hungsum.framework.enums.EBase;


public class EHsLcSpzt extends EBase
{
	private static final long serialVersionUID = -7210872421560393880L;

	protected EHsLcSpzt(int value)
	{
		super(value);
	}

	public static EHsLcSpzt 未审批 = new EHsLcSpzt(0);
	
	public static EHsLcSpzt 正在审批 = new EHsLcSpzt(1);
	
	public static EHsLcSpzt 审批同意  = new EHsLcSpzt(2);

	public static EHsLcSpzt 审批驳回 = new EHsLcSpzt(3);

	public static EHsLcSpzt 用户终止 = new EHsLcSpzt(4);
}
