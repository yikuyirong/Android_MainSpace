package com.hungsum.framework.enums;

import java.io.Serializable;


public class EBase implements Serializable
{

	private static final long serialVersionUID = -5619483055983810950L;

	private int value = 0;

    protected EBase(int value)
	{
    	this.value = value;
	}

    public int value() {
        return this.value;
    }
    
    public static EBase parseString(String value)
    {
    	return new EBase(Integer.parseInt(value));
    }
    
    @Override
    public String toString()
    {
    	return String.valueOf(value);
    }
    
    @Override
    public boolean equals(Object o)
    {
    	if(o == null) return false;

    	return  this.getClass() == o.getClass() ? value == ((EBase)o).value : false;
    }
}
