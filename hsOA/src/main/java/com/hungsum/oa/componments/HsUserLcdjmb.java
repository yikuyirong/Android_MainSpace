package com.hungsum.oa.componments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class HsUserLcdjmb implements Serializable
{
	private static final long serialVersionUID = -6420677765317425882L;

	public int MbId;
	
	public String Mbmc;
	
	public int Lclx;
	
	public int HasImage;
	
	public int SfxgImage;
	
	public int HasFile;
	
	public int SfxgFile;
	
	public int Sfsy;
	
	public int IsAuth;
	
	public String Zdr;
	
	public String Zdrq;
	
	public List<HsUserLcdjmbmx> Mxs = new ArrayList<HsUserLcdjmbmx>();
	
}
