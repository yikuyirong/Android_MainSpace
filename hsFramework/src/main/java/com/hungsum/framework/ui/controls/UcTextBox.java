/**
 * 
 */
package com.hungsum.framework.ui.controls;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.method.SingleLineTransformationMethod;
import android.util.AttributeSet;
import android.widget.EditText;

import com.hungsum.framework.events.CommEventListener;
import com.hungsum.framework.events.CommEventListener.EventCategory;
import com.hungsum.framework.events.CommEventObject;
import com.hungsum.framework.interfaces.IControlValue;

/**
 * @author zhaixuan
 *
 */
public class UcTextBox extends EditText implements IControlValue
{
	
	protected List<CommEventListener> dataChangedListenerlisteners = new ArrayList<CommEventListener>();

	/**
	 * @param context
	 */
	public UcTextBox(Context context)
	{
		super(context);

		//单行显示
		this.setTransformationMethod(SingleLineTransformationMethod.getInstance());

	}
	
	public UcTextBox(Context context ,AttributeSet attrs)
	{
		super(context, attrs);

		//单行显示
		this.setTransformationMethod(SingleLineTransformationMethod.getInstance());
	}
	
	public UcTextBox(Context context, AttributeSet attrs, int defStyle)	
	{
		super(context,attrs,defStyle);

		//单行显示
		this.setTransformationMethod(SingleLineTransformationMethod.getInstance());
	}
	
	private CharSequence ControlId = "";
	
	@Override
	public CharSequence getControlId()
	{
		return this.ControlId;
	}

	/* (non-Javadoc)
	 * @see HsFramework.Interface.IControlValue#SetControlId(java.lang.String)
	 */
	@Override
	public void setControlId(CharSequence value)
	{
		this.ControlId = value;

	}

	/* (non-Javadoc)
	 * @see HsFramework.Interface.IControlValue#GetControlValue()
	 */
	@Override
	public CharSequence getControlValue()
	{
		return this.getText().toString().trim();
	}

	/* (non-Javadoc)
	 * @see HsFramework.Interface.IControlValue#SetControlValue(java.lang.String)
	 */
	@Override
	public void setControlValue(CharSequence value)
	{
		this.setText(value);
	}

	private CharSequence _defaultControlValue = "";
	
	/* (non-Javadoc)
	 * @see HsFramework.Interface.IControlValue#SetDefaultControlValue(java.lang.String)
	 */
	@Override
	public void setDefaultControlValue(CharSequence value)
	{
		this._defaultControlValue = value;
	}


	private CharSequence CName;
	
	/* (non-Javadoc)
	 * @see HsFramework.Interface.IControlValue#GetCName()
	 */
	@Override
	public CharSequence getCName()
	{
		return this.CName;
	}

	/* (non-Javadoc)
	 * @see HsFramework.Interface.IControlValue#SetCName(java.lang.String)
	 */
	@Override
	public void setCName(CharSequence value)
	{
		this.CName = value;
	}

	private boolean AllowEmpty = true;
	
	/* (non-Javadoc)
	 * @see HsFramework.Interface.IControlValue#GetAllowEmpty()
	 */
	@Override
	public boolean getAllowEmpty()
	{
		return this.AllowEmpty;
	}

	/* (non-Javadoc)
	 * @see HsFramework.Interface.IControlValue#SetAllowEmpty(java.lang.Boolean)
	 */
	@Override
	public void setAllowEmpty(boolean value)
	{
		this.AllowEmpty = value;
	}


	private boolean mAllowEdit;
	
	@Override
	public void setAllowEdit(boolean value)
	{
		mAllowEdit = value;
        this.setCursorVisible(value);      //设置输入框中的光标不可见  
        this.setFocusable(value);           //无焦点  
        this.setFocusableInTouchMode(value);  	
    }
	
	@Override
	public boolean getAllowEdit()
	{
		return mAllowEdit;
	}
	
	private boolean DefaultAllowEdit = true;

	/* (non-Javadoc)
	 * @see HsFramework.Interface.IControlValue#GetDefaultAllowEdit()
	 */
	@Override
	public boolean getDefaultAllowEdit()
	{
		return this.DefaultAllowEdit;
	}

	/* (non-Javadoc)
	 * @see HsFramework.Interface.IControlValue#SetDefaultAllowEdit(java.lang.Boolean)
	 */
	@Override
	public void setDefaultAllowEdit(boolean value)
	{
		this.DefaultAllowEdit = value;
	}

	/* (non-Javadoc)
	 * @see HsFramework.Interface.IControlValue#Validate()
	 */
	@Override
	public void Validate() throws Exception
	{
		if(!this.getAllowEmpty() && this.getControlValue().toString().trim().length() == 0)
		{
			
			throw new Exception(this.getCName() + "不能为空。");
		}
	}

	/* (non-Javadoc)
	 * @see HsFramework.Interface.IControlValue#Reset()
	 */
	@Override
	public void Reset()
	{
		this.setControlValue(this._defaultControlValue);
	}

	@Override
	public CharSequence getControlTitle(CharSequence value)
	{
		return null;
	}

	@Override
	protected void onTextChanged(CharSequence text, int start,
			int lengthBefore, int lengthAfter)
	{
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
		
		dispatchDataChangedEvents();
	}

	public void dispatchDataChangedEvents()
	{
		if(this.dataChangedListenerlisteners != null)
		{
			for(CommEventListener listener : this.dataChangedListenerlisteners)
			{
				if(listener.getCategory() == EventCategory.DataChanged)
				{
					if(mCanTriggerDataChangedEvent)
					{
						listener.EventHandler(new CommEventObject(this, null));
					}
				}
			}
		}
	}
	
	@Override
	public void addOnDataChangedListener(CommEventListener listener)
	{
		if(listener.getCategory() == EventCategory.DataChanged)
		{
			if(this.dataChangedListenerlisteners == null)
			{
				this.dataChangedListenerlisteners = new ArrayList<CommEventListener>();
			}
			this.dataChangedListenerlisteners.add(listener);
		}
	}
	
	@Override
	public void removeOnDataChangedListener(CommEventListener listener)
	{
		if(dataChangedListenerlisteners != null)
		{
			this.dataChangedListenerlisteners.remove(listener);
		}
	}

	@Override
	public void removeAllOnDataChangedListener()
	{
		if(this.dataChangedListenerlisteners != null)
		{
			this.dataChangedListenerlisteners.removeAll(dataChangedListenerlisteners);
		}
	}

	private boolean mCanTriggerDataChangedEvent = true;
	
	@Override
	public boolean getCanTriggerDataChangedEvent()
	{
		return mCanTriggerDataChangedEvent;
	}

	@Override
	public void setCanTriggerDataChangedEvent(boolean value)
	{
		mCanTriggerDataChangedEvent = value;
	}
}
