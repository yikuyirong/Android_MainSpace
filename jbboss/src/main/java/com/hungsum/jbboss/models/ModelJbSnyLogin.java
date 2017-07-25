package com.hungsum.jbboss.models;

import com.hungsum.framework.componments.HsMenu;
import com.hungsum.framework.models.ModelBase;
import com.hungsum.framework.others.HsSaxHandler;
import com.hungsum.jbboss.componments.JbSnyLoginData;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Stack;

public class ModelJbSnyLogin extends ModelBase<JbSnyLoginData>
{
	//private static final long serialVersionUID = 1073887428762331959L;

	private Stack<HsMenu> cacheMenus = new Stack<HsMenu>();

	@Override
	protected HsSaxHandler<JbSnyLoginData> getHandler()
	{
		return new JbSnyLoginDataHandler();
	}

	protected class JbSnyLoginDataHandler extends HsSaxHandler<JbSnyLoginData>
	{

		@Override
		public void endDocument() throws SAXException
		{
			super.endDocument();
			
			//有些外联框架中没有Role属性，在此为Rols赋初始值。
			
//			if(returnObject.getRoles().size() == 0)
//			{
//				returnObject.addRole(new HsRole("1","全部权限"));
//			}
//			
//			this.returnObject.setCurrentRole(0); //设计第一个角色为默认角色。
		}

		@Override
		protected JbSnyLoginData getNewInstance()
		{
			return new JbSnyLoginData();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException
		{
			super.startElement(uri, localName, qName, attributes);

			if(localName.equals("Menu")) //菜单
			{
				HsMenu menu = new HsMenu();

				menu.Bh = attributes.getValue("Bh");
				menu.Mc = attributes.getValue("Mc");
				menu.Sjbh = attributes.getValue("Sjbh");
				menu.Gnbh = attributes.getValue("Gnbh");
				menu.Icon = attributes.getValue("Icon2");
				menu.IsMx = attributes.getValue("IsMx").equals("1");

				cacheMenus.push(menu);

			}
		}
		
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException
		{

			super.endElement(uri, localName, qName);

			if (localName.equals("Rybh"))
			{
				this.returnObject.setUserbh(sb.toString());
			}else if (localName.equals("Rymc"))
			{
				this.returnObject.setUsername(sb.toString());
			}else if(localName.equals("Password"))
			{
				this.returnObject.setPassword(sb.toString());
			}else if (localName.equals("ProgressId"))
			{
				this.returnObject.setProgressId(sb.toString());
			}else if(localName.equals("Nzbh"))
			{
				this.returnObject.setNzbh(sb.toString());
			}else if(localName.equals("Nzmc"))
			{
				this.returnObject.setNzmc(sb.toString());
			}else if(localName.equals("Fndbh"))
			{
				this.returnObject.setFndbh(sb.toString());
			}else if(localName.equals("Fndmc"))
			{
				this.returnObject.setFndmc(sb.toString());
			}else if(localName.equals("RoleBhs"))
			{
				this.returnObject.setRoleBhs(sb.toString());
			}else if(localName.equals("RoleMcs"))
			{
				this.returnObject.setRoleMcs(sb.toString());
			}else if (localName.equals("Menu"))
			{
				this.returnObject.addMenu(cacheMenus.pop());
			}

		}
	}

}
