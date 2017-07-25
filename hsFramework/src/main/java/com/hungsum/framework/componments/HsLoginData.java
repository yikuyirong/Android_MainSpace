package com.hungsum.framework.componments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HsLoginData implements IHsLoginData
{

	private static final long serialVersionUID = 7751133090008426323L;

	//region 用户编号
	
	private String mUserbh;

	@Override
	public String getUserbh()
	{
		return mUserbh;
	}

	@Override
	public void setUserbh(String userbh)
	{
		this.mUserbh = userbh;
	}

	//endregion
	
	//region 用户名称
	
	private String mUsername;

	@Override
	public String getUsername()
	{
		return mUsername;
	}

	@Override
	public void setUsername(String username)
	{
		this.mUsername = username;
	}

	//endregion
	
	//region 用户密码
	
	private String mPassword;

	@Override
	public String getPassword()
	{
		return mPassword;
	}

	@Override
	public void setPassword(String value)
	{
		mPassword = value;
	}
	
	//endregion
	
	//region 单位编号
	
	private String mDeptBh;
	
	public String getDeptBh()
	{
		return mDeptBh;
	}
	
	public void setDeptBh(String value)
	{
		mDeptBh = value;
	}
	
	//endregion
	
	//region 单位名称
	
	private String mDeptMc;
	
	public String getDeptMc()
	{
		return mDeptMc;
	}
	
	public void setDeptMc(String value)
	{
		mDeptMc = value;
	}
	
	//endregion
	
	//region 角色编号集合
	
	private String mRoleBhs;
	
	@Override
	public String getRoleBhs()
	{
		return mRoleBhs;
	}
	
	@Override
	public void setRoleBhs(String value)
	{
		mRoleBhs = value;
	}
	
	//endregion
	
	//region 角色名称集合
	
	private String mRoleMcs;
	
	@Override
	public String getRoleMcs()
	{
		return mRoleMcs;
	}
	
	@Override
	public void setRoleMcs(String value)
	{
		mRoleMcs = value;
	}
	
	//endregion
	
	
	//region 进程ID
	
	private String mSimpleProgressId;
	
	@Override
	public String getSimpleProgressId()
	{
		return mSimpleProgressId;
	}
	
	@Override
	public void setSimpleProgressId(String value)
	{
		mSimpleProgressId = value;
	}
	
	private String mProgressId;

	@Override
	public String getProgressId()
	{
		return mProgressId;
	}

	@Override
	public void setProgressId(String progressId)
	{
		this.mProgressId = progressId;
	}
	
	//endregion
	
	//region 当前菜单集合
	
	private HsMenus mMenus = new HsMenus();

	@Override
	public void addMenu(HsMenu menu)
	{
		mMenus.add(menu);
	}

	@Override
	public HsMenus getMenus()
	{
		return mMenus;
	}

	@Override
	public void clearMenus()
	{
		mMenus.clear();
	}

	@Override
	public void addMenus(List<HsMenu> menus)
	{
		mMenus.addAll(menus);
	}
	
	//endregion
	
	//region 当前用户角色集合

    @Override
	public List<HsRole> getRoles()
	{
		List<HsRole> roles = new ArrayList<HsRole>();

		if(mRoleBhs != null && mRoleMcs != null)
		{
			String[] bhs = mRoleBhs.split(";");
			String[] mcs = mRoleMcs.split(";");
			
			
			for (int i = 0; i < bhs.length; i++)
			{
				roles.add(new HsRole(bhs[i],mcs[i]));
			}
		}
		
		return roles;
	}
	
	//endregion

}

