package com.hungsum.framework.enums;


public class EDjlx extends EBase
{
	private static final long serialVersionUID = 1077901958313372923L;

	protected EDjlx(int value)
	{
		super(value);
	}

	/**
	 * 流程审批记录
	 */
	public static EDjlx SysLcspjl = new EDjlx(10);
	
	/**
	 * 费用审批单
	 */
	public static EDjlx Fyspd = new EDjlx(100);
	
	

}
