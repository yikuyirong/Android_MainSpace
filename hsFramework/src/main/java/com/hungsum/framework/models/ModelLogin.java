package com.hungsum.framework.models;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.hungsum.framework.componments.HsLoginData;
import com.hungsum.framework.componments.HsMenu;
import com.hungsum.framework.others.HsSaxHandler;

public class ModelLogin extends ModelBase<HsLoginData>
{
	private static final long serialVersionUID = 1073887428762331959L;

	private Stack<HsMenu> cacheMenus = new Stack<HsMenu>();

	@Override
	protected HsSaxHandler<HsLoginData> getHandler()
	{
		return new HsLoginDataHandler();
	}

	protected class HsLoginDataHandler extends HsSaxHandler<HsLoginData>
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
		protected HsLoginData getNewInstance()
		{
			return new HsLoginData();
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

			if (localName.equals("UserBh"))
			{
				this.returnObject.setUserbh(sb.toString());
			}else if (localName.equals("UserName"))
			{
				this.returnObject.setUsername(sb.toString());
			}else if(localName.equals("Password"))
			{
				this.returnObject.setPassword(sb.toString());
			}else if (localName.equals("ProgressId"))
			{
				this.returnObject.setProgressId(sb.toString());
			}else if(localName.equals("DeptBh"))
			{
				this.returnObject.setDeptBh(sb.toString());
			}else if(localName.equals("DeptMc"))
			{
				this.returnObject.setDeptMc(sb.toString());
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
