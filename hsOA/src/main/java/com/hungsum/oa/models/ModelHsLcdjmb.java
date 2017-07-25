package com.hungsum.oa.models;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.hungsum.framework.models.ModelBase;
import com.hungsum.framework.others.HsSaxHandler;
import com.hungsum.oa.componments.HsUserLcdjmb;
import com.hungsum.oa.componments.HsUserLcdjmbmx;

public class ModelHsLcdjmb extends
		ModelBase<HsUserLcdjmb>
{

	private static final long serialVersionUID = 6816101764904789199L;

	@Override
	protected HsSaxHandler<HsUserLcdjmb> getHandler()
	{
		return new HsDataHandler();
	}
	
	protected class HsDataHandler extends HsSaxHandler<HsUserLcdjmb>
	{
		private HsUserLcdjmbmx tmp;

		@Override
		protected HsUserLcdjmb getNewInstance()
		{
			return new HsUserLcdjmb();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException
		{
			super.startElement(uri, localName, qName, attributes);

			if(localName.equals("Item"))
			{
				tmp = new HsUserLcdjmbmx();
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException
		{
			super.endElement(uri, localName, qName);

			if (localName.equals("Item"))
			{
				returnObject.Mxs.add(tmp);
			}else if (localName.equals("MbId"))
			{
				returnObject.MbId = Integer.parseInt(sb.toString());
			}else if (localName.equals("Mbmc"))
			{
				returnObject.Mbmc = sb.toString();
			}else if (localName.equals("Lclx"))
			{
				returnObject.Lclx = Integer.parseInt(sb.toString());
			}else if (localName.equals("HasImage"))
			{
				returnObject.HasImage = Integer.parseInt(sb.toString());
			}else if (localName.equals("SfxgImage"))
			{
				returnObject.SfxgImage = Integer.parseInt(sb.toString());
			}else if (localName.equals("HasFile"))
			{
				returnObject.HasFile = Integer.parseInt(sb.toString());
			}else if (localName.equals("SfxgFile"))
			{
				returnObject.SfxgFile = Integer.parseInt(sb.toString());
			}else if (localName.equals("Sfsy"))
			{
				returnObject.Sfsy = Integer.parseInt(sb.toString());
			}else if (localName.equals("Zdr"))
			{
				returnObject.Zdr = sb.toString();
			}else if (localName.equals("Zdrq"))
			{
				returnObject.Zdrq = sb.toString();
			}else if (localName.equals("MxId"))
			{
				tmp.MxId = Integer.parseInt(sb.toString());
			}else if (localName.equals("Name"))
			{
				tmp.Name = sb.toString();
			}else if (localName.equals("Title"))
			{
				tmp.Title = sb.toString();
			}else if (localName.equals("Order"))
			{
				tmp.Order = Integer.parseInt(sb.toString());
			}else if (localName.equals("Class"))
			{
				tmp.Class =sb.toString();
			}else if (localName.equals("ClassInfo"))
			{
				tmp.ClassInfo =sb.toString();
			}else if (localName.equals("ClassParams"))
			{
				tmp.ClassParams = sb.toString();
			}else if (localName.equals("CanNull"))
			{
				tmp.CanNull = Integer.parseInt(sb.toString());
			}else if (localName.equals("Default"))
			{
				tmp.Default = sb.toString();
			}else if (localName.equals("Sfxg"))
			{
				tmp.Sfxg = Integer.parseInt(sb.toString());
			}
		}
	}
}
