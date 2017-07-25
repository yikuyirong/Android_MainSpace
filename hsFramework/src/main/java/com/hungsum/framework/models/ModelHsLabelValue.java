package com.hungsum.framework.models;

import org.xml.sax.SAXException;

import com.hungsum.framework.componments.HsLabelValue;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.others.HsSaxHandler;

public class ModelHsLabelValue extends ModelBase<IHsLabelValue>
{
	private static final long serialVersionUID = 6602161559109156973L;

	private String mRootName;
	
	public ModelHsLabelValue()
	{
		this("Data");
	}
	
	public ModelHsLabelValue(String rootName)
	{
		mRootName = rootName;
	}
	
	@Override
	protected HsSaxHandler<IHsLabelValue> getHandler()
	{
		return new HsDataHandler();
	}

	protected class HsDataHandler extends HsSaxHandler<IHsLabelValue>
	{
		@Override
		protected IHsLabelValue getNewInstance()
		{
			return new HsLabelValue();
		}
		
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException
		{
			super.endElement(uri, localName, qName);

			if(!localName.equals(mRootName))
			{
				returnObject.addDetail(new HsLabelValue(localName, sb.toString()));
			}
		}
	}
}
