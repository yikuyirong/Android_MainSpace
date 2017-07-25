package com.hungsum.framework.componments;

import java.io.Serializable;
import java.util.List;

public class HsQuery implements Serializable
{
	private static final long serialVersionUID = 3691460728825516005L;

	public String QueryTitle;
	
	public String QueryName;
	
	public int QueryNameRestId;
	
	public List<HsQueryArg> QueryArgs;
	
	public Class<?> queryResultClass;
	
}
