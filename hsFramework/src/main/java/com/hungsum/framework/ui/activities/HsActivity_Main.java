package com.hungsum.framework.ui.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.hungsum.framework.R;
import com.hungsum.framework.SysActionAttr;
import com.hungsum.framework.componments.HsLabelValue;
import com.hungsum.framework.componments.HsLoginData;
import com.hungsum.framework.componments.HsMenu;
import com.hungsum.framework.componments.HsMenus;
import com.hungsum.framework.componments.HsQuery;
import com.hungsum.framework.componments.HsRole;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.componments.IHsLoginData;
import com.hungsum.framework.events.CommEventListener;
import com.hungsum.framework.events.CommEventListener.EventCategory;
import com.hungsum.framework.events.CommEventObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.models.ModelQuery;
import com.hungsum.framework.ui.fragments.HsFragment;
import com.hungsum.framework.ui.fragments.HsFragment_Tile;
import com.hungsum.framework.utils.HsGZip;

public abstract class HsActivity_Main extends HsTabActivity
{

	protected ArrayAdapter<IHsLabelValue> mUserInformationAdapter;
	
	//private HsLabelValue mNextRole;

	@Override
	protected void initComponent(Bundle savedInstanceState) throws Exception
	{
		super.initComponent(savedInstanceState);

		IHsLoginData loginData = application.getLoginData();
		
		this.inTitle = "欢迎，" + loginData.getUsername();
		
		mUserInformationAdapter = new ArrayAdapter<IHsLabelValue>(this, android.R.layout.simple_list_item_1);

		if(loginData instanceof HsLoginData)
		{
			HsLoginData hsLoginData = (HsLoginData)loginData;

			mUserInformationAdapter.add(new HsLabelValue("[部门]" + hsLoginData.getDeptMc(),hsLoginData.getDeptBh()));
		}

		List<HsRole> roles = loginData.getRoles();
		
		for (HsRole role : roles)
		{
			mUserInformationAdapter.add(new HsLabelValue("[角色]" + role.RoleMc, role.RoleBh));
		}
		
	}
	
	@Override
	protected void initActivityView()
	{
		super.initActivityView();

		//登录后做一些额外的事情
		doSomeThingElse();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		//如果用户所属角色大于1个显示更换角色菜单
		menu.add(0, R.string.str_userinfo, 0, getString(R.string.str_userinfo))
		.setIcon(R.drawable.social_cc_bcc)
		.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		//
		menu.add(0, R.string.str_changepwd, 0, getString(R.string.str_changepwd))
		.setIcon(R.drawable.content_edit)
		.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId() == R.string.str_changepwd)
		{
			Intent intent = new Intent(this, HsActivity_ChangePassword.class);
			startActivity(intent);
			return true;
		}else if(item.getItemId() == R.string.str_userinfo)
		{
			if(this.mUserInformationAdapter != null)
			{
				//
//				String currentRolebh = application.getLoginData().getCurrentRole().RoleBh;
//				int position = 0;
//				for(int i = 0 ; i< mRoleAdapter.getCount() ; i++)
//				{
//					if(mRoleAdapter.getItem(i).getValue().equals(currentRolebh))
//					{
//						position = i;
//						break;
//					}
//				}

				AlertDialog ad = new AlertDialog.Builder(this)
				.setTitle("个人信息如下")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setAdapter(mUserInformationAdapter,null)
//				.setSingleChoiceItems(mRoleAdapter, position,null)
//				.setPositiveButton("确认",new OnClickListener()
//				{
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which)
//					{
//						ListView listView = ((AlertDialog)dialog).getListView();
//						mNextRole = (HsLabelValue) ((ArrayAdapter<IHsLabelValue>)mRoleAdapter).getItem(listView.getCheckedItemPosition());
//						if(mNextRole != null)
//						{
//							ShowWait("请稍候", "正在获取" + mNextRole.getKey() + "的菜单...");
//
//							new Thread(new Runnable()
//							{
//								@Override
//								public void run()
//								{
//									try
//									{
//										HsWSReturnObject object = application.getWebService().getRoleMenus(application.getLoginData().getProgressId(), mNextRole.getValue().toString());
//										sendDataMessage(object);
//										application.getLoginData().setCurrentRole(mNextRole.getValue().toString());
//									} catch (Exception e)
//									{
//										sendErrorMessage(e.getMessage());
//									}
//								}
//							}).start();
//							
//						}
//					}
//				})
				.create();
				ad.show();
			}
			return true;
		}else {
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * 登录后做一些额外的事情，此方法工作在UI线程。
	 */
	protected void doSomeThingElse()
	{
		
	}
	

	@Override
	protected List<HsFragment> getFragments(Bundle bundle)
	{
		List<HsFragment> fragments = new ArrayList<HsFragment>();

		HsMenus menus = application.getLoginData().getMenus();

		for (HsMenu top1menu : menus.GetMenus("")) //第一级目录生成卡片
		{
			//Fragment
			ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String,Object>>();
			
			for (HsMenu top2menu : menus.GetMenus(top1menu.Bh)) //第二级生成图标
			{
				if(top2menu.IsMx)
				{
					HashMap<String, Object> data = new HashMap<String, Object>();
					data.put("Label",top2menu.Mc);
					data.put("Icon",this.application.GetDrawableResIdByPicName(top2menu.Icon));
					data.put("Key",top2menu.Gnbh);
					datas.add(data);
				}
			}
			
			if(datas.size() > 0)
			{
				HsFragment fragment = new HsFragment_Tile();
				fragment.Title = top1menu.Mc;
				fragment.Icon = this.application.GetDrawableResIdByPicName(top1menu.Icon);
				((HsFragment_Tile)fragment).SetDatas(datas);
				fragment.addCommEventListener(new CommEventListener(EventCategory.UserFunc)
				{
					@Override
					public void EventHandler(CommEventObject object)
					{
						try
						{
							doFunc(((String)(object.GetData())).toUpperCase());
						} catch (Exception e)
						{
							ShowError(e);
						}
					}
					
				});
				
				fragments.add(fragment);
			}
		}
		return fragments;
	}


	@Override
	protected void actionAfterCallBack()
	{
		close();
	}

	protected void close()
	{
		showAlert("确认退出吗？",null, "确定", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				ShowWait("请稍候", "正在结束应用程序...");

				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							HsWSReturnObject object = application.getWebService().logout(application.getLoginData().getProgressId());
							sendDataMessage(object);
						} catch (Exception e)
						{
							finish();

							//System.exit(0);
						}
					}
				}).start();
			}
		}, "返回",null);
	}
	
	protected void doFunc(String funcKey) throws Exception
	{
		if(funcKey.startsWith("Q_") || funcKey.startsWith("C_")) //查询
		{
			callQuery(funcKey);
		}else 
		{
			throw new Exception("未知的功能【" + funcKey + "】");
		}
	}
	
	protected void callQuery(final String funcKey)
	{
		ShowWait("请稍候", "正在处理数据...");

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					HsWSReturnObject object = getQueryCondition(funcKey);
					sendDataMessage(object);
				} catch (Exception e)
				{
					sendErrorMessage(e.getMessage());
				}
			}
		}).start();
	}
	

	protected HsWSReturnObject getQueryCondition(String queryName) throws Exception
	{
		return application.getWebService().getQueryNameAndArgs(application.getLoginData().getProgressId(), queryName);
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if(funcname.equals("GetRoleMenus_M"))
		{
//			//更新ProgressId
//			HsLoginData loginData = application.getLoginData();
//			loginData.setProgressId(loginData.getSimpleProgressId() + "," + mNextRole.getValue());
//
//			HsMenus hsMenus = new ModelHsMenus().Create(HsGZip.DecompressString(data.toString()));
//			
//			application.getLoginData().clearMenus();
//			application.getLoginData().addMenus(hsMenus);
			//application.getLoginData()
			
//			createPagerAdapter(); //重新建立适配器
//
//			initActivityView();

		}else if(funcname.equals("GetQueryNameAndArgs"))
		{
			HsQuery hsQuery = new ModelQuery().Create(HsGZip.DecompressString(data.toString()));

			Intent intent = new Intent();
			intent.setAction(SysActionAttr.ACTION_HSQUERYCONDITION);
			intent.addCategory(getPackageName());
			intent.putExtra("Data",hsQuery);

			startActivity(intent);
		}else if (funcname.equals("Logout"))
		{
			finish();
			
			//System.exit(0);
		}else {
			super.actionAfterWSReturnData(funcname, data);
		}
	}
	

}
