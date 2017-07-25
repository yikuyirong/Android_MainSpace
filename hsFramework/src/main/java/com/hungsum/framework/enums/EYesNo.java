package com.hungsum.framework.enums;


public class EYesNo extends EBase
{
	private static final long serialVersionUID = 1077901958313372923L;

	protected EYesNo(int value)
	{
		super(value);
	}

	public static EYesNo Yes = new EYesNo(1);
	
	public static EYesNo No = new EYesNo(0);
}
