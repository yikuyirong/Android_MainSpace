package com.hungsum.framework.componments;

import java.util.ArrayList;
import java.util.List;

import com.hungsum.framework.events.CommEventListener;
import com.hungsum.framework.events.CommEventListener.EventCategory;

public class HsCommEventListeners extends ArrayList<CommEventListener>
{
	private static final long serialVersionUID = 5258157417766563685L;
	
	public void remove(EventCategory eventCategory)
	{
		for (CommEventListener listener : this)
		{
			if(listener.getCategory().equals(eventCategory))
			{
				this.remove(listener);
				
				//递归调用
				this.remove(eventCategory);
			}
		}
	}
	
	public List<CommEventListener> getItems(EventCategory category)
	{
		List<CommEventListener> listeners = new ArrayList<CommEventListener>();
		
		for (CommEventListener listener : this)
		{
			if(listener.getCategory().equals(category))
			{
				listeners.add(listener);
			}
		}
		
		return listeners;
		
	}

}
