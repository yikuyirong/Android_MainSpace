package com.hungsum.oa.models;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.hungsum.framework.models.ModelBase;
import com.hungsum.framework.others.HsSaxHandler;
import com.hungsum.oa.componments.HsLcbzfz;

public class ModelHsLcbzfzs extends
		ModelBase<ArrayList<HsLcbzfz>>
{

	private static final long serialVersionUID = 6816101764904789199L;

	@Override
	protected HsSaxHandler<ArrayList<HsLcbzfz>> getHandler()
	{
		return new HsDataHandler();
	}
	
	protected class HsDataHandler extends HsSaxHandler<ArrayList<HsLcbzfz>>
	{
		private HsLcbzfz tmp;

		@Override
		protected ArrayList<HsLcbzfz> getNewInstance()
		{
			return new ArrayList<HsLcbzfz>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException
		{
			super.startElement(uri, localName, qName, attributes);

			if(localName.equals("Item"))
			{
				tmp = new HsLcbzfz();
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException
		{
			super.endElement(uri, localName, qName);

			if (localName.equals("Item"))
			{
				returnObject.add(tmp);
			}else if (localName.equals("FzId"))
			{
				tmp.FzId = Integer.parseInt(sb.toString());
			}else if (localName.equals("BzId"))
			{
				tmp.BzId = Integer.parseInt(sb.toString());
			}else if (localName.equals("MbId"))
			{
				tmp.MbId = Integer.parseInt(sb.toString());
			}else if (localName.equals("Fzmc"))
			{
				tmp.Fzmc = sb.toString();
			}else if (localName.equals("Fztj"))
			{
				tmp.Fztj = sb.toString();
			}else if (localName.equals("XybzId"))
			{
				tmp.XybzId = Integer.parseInt(sb.toString());
			}else if (localName.equals("Xybzmc"))
			{
				tmp.Xybzmc = sb.toString();
			}else if (localName.equals("Order"))
			{
				tmp.Order = Integer.parseInt(sb.toString());
			}
		}
	}
}
