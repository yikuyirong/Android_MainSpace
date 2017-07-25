package com.hungsum.framework.interfaces;

import java.io.Serializable;

public interface IModel<T extends Serializable>
{
	T Create(String value);
}
