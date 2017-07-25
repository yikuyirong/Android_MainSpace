package com.hungsum.oa.enums;

import com.hungsum.framework.enums.EBase;


public class EHsLcBzFzBhcl extends EBase
{
	private static final long serialVersionUID = 8604742965863952279L;

	protected EHsLcBzFzBhcl(int value)
	{
		super(value);
	}

	public static EHsLcBzFzBhcl 退回上一步 = new EHsLcBzFzBhcl(0);

	public static EHsLcBzFzBhcl 直接终止 = new EHsLcBzFzBhcl(1);

}
