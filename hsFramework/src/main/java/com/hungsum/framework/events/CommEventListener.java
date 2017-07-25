package com.hungsum.framework.events;

import java.util.EventListener;

public abstract class CommEventListener implements EventListener
{
	private EventCategory mCategory;
	
	public CommEventListener(EventCategory category)
	{
		mCategory = category;
	}
	
	public EventCategory getCategory()
	{
		return mCategory;
	}
	
	public abstract void EventHandler(CommEventObject object);
	
	
	
	public enum EventCategory
	{
		None,
		UserFunc,
		Retrieve,
		DataChanged,
		UpdateCompleted,
		DoFunc1,
		DoFunc2,
		DoFunc3,
		DoFunc4,
		DoFunc5,
		DoFunc6
	}
}
