package com.hungsum.framework.interfaces;

import java.io.Serializable;

public interface IHsActivity
{

	public abstract void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception;

}