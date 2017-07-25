package com.hungsum.framework.models;

import org.xml.sax.SAXException;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.others.HsSaxHandler;

public class ModelWsReturnObject extends ModelBase<HsWSReturnObject>
{
	private static final long serialVersionUID = -3621737912825932665L;

	@Override
	protected HsSaxHandler<HsWSReturnObject> getHandler()
	{
		return new Handler();
	}
	
	private class Handler extends HsSaxHandler<HsWSReturnObject>
	{
		@Override
		protected HsWSReturnObject getNewInstance()
		{
			return new HsWSReturnObject();
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException
		{
			super.endElement(uri, localName, qName);
			
			if (localName.equals("Code"))
			{
				returnObject.SetCodeNum(Integer.parseInt(sb.toString()));
			}else if (localName.equals("CodeDesc"))
			{
				returnObject.SetCodeDesc(sb.toString());
			}else if (localName.equals("FuncName"))
			{
				returnObject.SetFuncName(sb.toString());
			}else if (localName.equals("Data"))
			{
				returnObject.SetData(sb.toString());
			}
		}

	}

}
