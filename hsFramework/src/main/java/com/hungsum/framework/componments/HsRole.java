package com.hungsum.framework.componments;

import java.io.Serializable;

public class HsRole implements Serializable
{
	private static final long serialVersionUID = 5371938143034359152L;

	public String RoleBh;
	
	public String RoleMc;

	public HsRole()
	{
		
	}
	
	public HsRole(String rolebh,String rolemc)
	{
		RoleBh = rolebh;
		RoleMc = rolemc;
	}
}
