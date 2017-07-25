package com.hungsum.framework.models;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.hungsum.framework.componments.HsBitmap;
import com.hungsum.framework.others.HsSaxHandler;

public class ModelHsBitmaps extends ModelBase<ArrayList<HsBitmap>>
{
	private static final long serialVersionUID = 5108620813146722200L;

	@Override
	protected HsSaxHandler<ArrayList<HsBitmap>> getHandler()
	{
		return new HsBitmapsDataHandler();
	}

	protected class HsBitmapsDataHandler extends HsSaxHandler<ArrayList<HsBitmap>>
	{
		private HsBitmap bmp;
		
		@Override
		protected ArrayList<HsBitmap> getNewInstance()
		{
			return new ArrayList<HsBitmap>();
		}

		
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException
		{
			super.startElement(uri, localName, qName, attributes);
			
			if(localName.equals("Item"))
			{
				bmp = new HsBitmap();
			}
		}
		
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException
		{
			super.endElement(uri, localName, qName);

			if(localName.equals("ImageId"))
			{
				bmp.ImageId = Integer.valueOf(sb.toString());
			}else if(localName.equals("Class"))
			{
				bmp.Class = sb.toString();
			}else if(localName.equals("ClassId"))
			{
				bmp.ClassId = sb.toString();
			}else if(localName.equals("HashType"))
			{
				bmp.HashType = sb.toString();
			}else if(localName.equals("HashData"))
			{
				bmp.HashData = sb.toString();
			}else if(localName.equals("Data"))
			{
				bmp.setBitmapString(sb.toString());
			}else if(localName.equals("Item"))
			{
				returnObject.add(bmp);
			}
		}
	}
}
