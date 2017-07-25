package com.hungsum.framework.componments;

import java.io.Serializable;

public class HsWSReturnObject implements Serializable
{
	private static final long serialVersionUID = 6471029308047717437L;

	private int codeNum;

	private String codeDesc;

	private String funcName;

	private Serializable data;

	public static final int SUCCESS = 0;

	public void SetCodeNum(int value)
	{
		this.codeNum = value;
	}

	public int GetCodeNum()
	{
		return this.codeNum;
	}

	public void SetCodeDesc(String value)
	{
		this.codeDesc = value;
	}

	public String GetCodeDesc()
	{
		return this.codeDesc;
	}

	public void SetFuncName(String value)
	{
		this.funcName = value;
	}

	public String GetFuncName()
	{
		return this.funcName;
	}

	public void SetData(Serializable value)
	{
		this.data = value;
	}

	public Serializable GetData()
	{
		return this.data;
	}
}
