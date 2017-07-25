package com.hungsum.framework.models;

import org.xml.sax.SAXException;

import com.hungsum.framework.componments.HsBitmap;
import com.hungsum.framework.others.HsSaxHandler;

public class ModelHsBitmap extends ModelBase<HsBitmap>
{
	private static final long serialVersionUID = 5108620813146722200L;

	@Override
	protected HsSaxHandler<HsBitmap> getHandler()
	{
		return new HsBitmapDataHandler();
	}

	protected class HsBitmapDataHandler extends HsSaxHandler<HsBitmap>
	{
		@Override
		protected HsBitmap getNewInstance()
		{
			return new HsBitmap();
		}
		
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException
		{
			super.endElement(uri, localName, qName);

			if(localName.equals("ImageId"))
			{
				returnObject.ImageId = Integer.valueOf(sb.toString());
			}else if(localName.equals("Class")) 
			{
				returnObject.Class = sb.toString();
			}else if(localName.equals("ClassId"))
			{
				returnObject.ClassId = sb.toString();
			}else if(localName.equals("HashType"))
			{
				returnObject.HashType = sb.toString();
			}else if(localName.equals("HashData"))
			{
				returnObject.HashData = sb.toString();
			}else if(localName.equals("Data"))
			{
				returnObject.setBitmapString(sb.toString());
			}
		}
	}
}
