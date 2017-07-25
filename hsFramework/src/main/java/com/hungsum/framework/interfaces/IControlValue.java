package com.hungsum.framework.interfaces;

import com.hungsum.framework.events.CommEventListener;


public interface IControlValue
{
	CharSequence getControlId();
	void setControlId(CharSequence value );
	
	CharSequence getControlValue() throws Exception;
	void setControlValue(CharSequence value) throws Exception;

	void setDefaultControlValue(CharSequence value);

	CharSequence getControlTitle(CharSequence value);
	
	CharSequence getCName();
	
	void setCName(CharSequence value);
	
	boolean getAllowEmpty();
	
	void setAllowEmpty(boolean value);
	
	boolean getAllowEdit();
	
	void setAllowEdit(boolean value);
	
	boolean getDefaultAllowEdit();
	
	void setDefaultAllowEdit(boolean value);
	
	void Validate() throws Exception;
	
	void Reset();

	void dispatchDataChangedEvents();
	
	void addOnDataChangedListener(CommEventListener listener);
	
	void removeOnDataChangedListener(CommEventListener listener);
	
	void removeAllOnDataChangedListener();
	
	boolean getCanTriggerDataChangedEvent();
	
	void setCanTriggerDataChangedEvent(boolean value);
}
