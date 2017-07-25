package com.hungsum.framework.utils;

import java.util.EventListener;

public interface HsExceptionListener extends EventListener
{
	public void onExceptionReceived(Exception e);
}
