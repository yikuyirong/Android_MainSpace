package com.hungsum.framework.models;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.hungsum.framework.componments.HsQuery;
import com.hungsum.framework.componments.HsQueryArg;
import com.hungsum.framework.others.HsSaxHandler;

public class ModelQuery extends
		ModelBase<HsQuery>
{
	private static final long serialVersionUID = 6726481769870333413L;

	@Override
	protected HsSaxHandler<HsQuery> getHandler()
	{
		return new HsQueryDataHandler();
	}
	
	protected class HsQueryDataHandler extends HsSaxHandler<HsQuery>
	{
		private HsQueryArg tmpArg;
		
		private ArrayList<HsQueryArg> args;

		@Override
		protected HsQuery getNewInstance()
		{
			return new HsQuery();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException
		{
			super.startElement(uri, localName, qName, attributes);

			if(localName.equals("Arg"))
			{
				tmpArg = new HsQueryArg();
			}else if (localName.equals("QueryArgs"))
			{
				args = new ArrayList<HsQueryArg>();
			}
			
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException
		{
			super.endElement(uri, localName, qName);

			if (localName.equals("QueryName"))
			{
				returnObject.QueryName = sb.toString();
			}else if (localName.equals("QueryTitle"))
			{
				returnObject.QueryTitle = sb.toString();
			}else if (localName.equals("Arg"))
			{
				args.add(tmpArg);
			}else if (localName.equals("Name"))
			{
				tmpArg.Name = sb.toString();
			}else if (localName.equals("CName"))
			{
				tmpArg.CName = sb.toString();
			}else if (localName.equals("SqlOrder"))
			{
				tmpArg.SqlOrder = sb.toString();
			}else if (localName.equals("Order"))
			{
				tmpArg.Order = sb.toString();
			}else if (localName.equals("Class"))
			{
				tmpArg.Class = sb.toString();
			}else if (localName.equals("ClassInfo"))
			{
				tmpArg.ClassInfo = sb.toString();
			}else if (localName.equals("ClassParams"))
			{
				tmpArg.ClassParams = sb.toString();
			}else if (localName.equals("AllowEmpty"))
			{
				tmpArg.AllowEmpty = sb.toString();
			}else if (localName.equals("Default"))
			{
				tmpArg.Default = sb.toString();
			}
		}

		@Override
		public void endDocument() throws SAXException
		{
			super.endDocument();
			
			returnObject.QueryArgs = args;
		}
	}
}
