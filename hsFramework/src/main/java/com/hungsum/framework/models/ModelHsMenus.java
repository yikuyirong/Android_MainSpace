package com.hungsum.framework.models;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.hungsum.framework.componments.HsMenu;
import com.hungsum.framework.componments.HsMenus;
import com.hungsum.framework.others.HsSaxHandler;

public class ModelHsMenus extends ModelBase<HsMenus>
{
	private static final long serialVersionUID = -1098438663520103691L;

	@Override
	protected HsSaxHandler<HsMenus> getHandler()
	{
		return new HsMenusDataHandler();
	}

	protected class HsMenusDataHandler extends HsSaxHandler<HsMenus>
	{
		private HsMenu menu;

		@Override
		protected HsMenus getNewInstance()
		{
			return new HsMenus();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException
		{
			super.startElement(uri, localName, qName, attributes);
			
			if (localName.equals("Menu"))
			{
				menu = new HsMenu();
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException
		{
			super.endElement(uri, localName, qName);

			if(localName.equals("Menus"))
			{
				//this.returnObject = new ArrayList<IHsLabelValue>();
			}else if (localName.equals("Menu"))
			{
				this.returnObject.add(menu);
			}else if (localName.equals("Bh"))
			{
				menu.Bh = sb.toString();
			}else if (localName.equals("Mc"))
			{
				menu.Mc = sb.toString();
			}else if (localName.equals("Sjbh"))
			{
				menu.Sjbh = sb.toString();
			}else if (localName.equals("Icon"))
			{
				menu.Icon = sb.toString();
			}else if (localName.equals("Gnbh"))
			{
				menu.Gnbh = sb.toString();
			}else if (localName.equals("IsMx"))
			{
				menu.IsMx = sb.toString().equals("1");
			}
		}
	}
}
