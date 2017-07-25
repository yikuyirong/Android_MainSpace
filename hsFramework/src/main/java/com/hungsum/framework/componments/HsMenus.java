package com.hungsum.framework.componments;

import java.util.ArrayList;
import java.util.List;

public class HsMenus extends ArrayList<HsMenu>
{
	private static final long serialVersionUID = -7476395693820746402L;

	public List<HsMenu> GetMenus(String sjbh)
	{
		List<HsMenu> menus = new ArrayList<HsMenu>();
		for (HsMenu menu : this)
		{
			if(menu.Sjbh.equals(sjbh))
			{
				if(menu.IsMx || this.GetMenus(menu.Bh).size() > 0)
				{
					menus.add(menu);
				}
			}
		}
		return menus;
	}
	
}
