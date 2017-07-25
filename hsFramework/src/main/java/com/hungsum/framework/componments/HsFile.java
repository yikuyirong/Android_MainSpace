package com.hungsum.framework.componments;

import java.io.Serializable;

public class HsFile implements Serializable ,Comparable<HsFile>
{

	private static final long serialVersionUID = -3876940317364994416L;

	protected long mFileSize;
	
	public HsFile()
	{
	}

	@Override
	public int compareTo(HsFile another)
	{
		return (int) (mFileSize - another.mFileSize);
	}

}
