package com.hungsum.framework.ui.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hungsum.framework.R;
import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.events.CommEventListener;
import com.hungsum.framework.events.CommEventListener.EventCategory;
import com.hungsum.framework.events.CommEventObject;
import com.hungsum.framework.interfaces.IControlValue;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.interfaces.IOnRetrieveListener;
import com.hungsum.framework.interfaces.IUserControlWithAdapter;
import com.hungsum.framework.models.ModelHsLabelValues;
import com.hungsum.framework.ui.fragments.HsFragment;
import com.hungsum.framework.ui.fragments.HsFragment.CreateCompletedEventListener;
import com.hungsum.framework.ui.fragments.HsFragment_DJ_File;
import com.hungsum.framework.ui.fragments.HsFragment_DJ_Image;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Detail;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Detail.HsZDDetailAdapter;
import com.hungsum.framework.ui.fragments.HsFragment_ZD_Main;
import com.hungsum.framework.utils.HsGZip;
import com.hungsum.framework.utils.HsLocationUtil;
import com.hungsum.framework.utils.HsLocationUtil.EDispatchEventFrequency;
import com.hungsum.framework.utils.HsLocationUtil.ENeedLocationInformation;
import com.hungsum.framework.utils.HsLocationUtil.HsLocationListener;

public abstract class HsActivity_ZD extends HsTabActivity
{
	/**
	 * 定位超时时间，毫秒单位
	 */
	public static int Location_Timeout = 10000;
	
	//{{ 实例变量
	
	/**
	 * 单据ID
	 */
	protected String mDJId = "-1";

	protected int mIsNewRecorder;

	//{{通过bundle传入的数据

	protected IHsLabelValue inData;
	
	//}}
	
	
	protected UcControls controls = new UcControls();

	protected IUserControlWithAdapter currentChooseControl;

	protected String mAnnexClass;

	/**
	 * 是否出发数据变化事件
	 */
	protected Boolean mIsTriggerDataChangedEvent = true;
	
	/**
	 * 数据变化侦听器
	 */
	protected CommEventListener mDataChangedEventListener = new CommEventListener(EventCategory.DataChanged)
	{
		@Override
		public void EventHandler(CommEventObject object)
		{
			if(mIsTriggerDataChangedEvent)
			{
				setIsModified(true);
			}
		}
	};

	

	/*****MainFragment 使用字段****************/
	protected HsFragment_ZD_Main mMainFragment;

	/*****DetailPage 使用字段******************/
	protected HsFragment_ZD_Detail mDetailFragemnt;
	
	/*****ImageFragment 使用字段***************/
	
	protected HsFragment_DJ_Image mImageFragment;
	
	/*****FileFragmeng 使用字段****************/
	
	protected HsFragment_DJ_File mFileFragment;

	
	private String mTitleSaved;
	
	private String mTitleNotSaved;
	
	protected Boolean mIsModified = false;


	protected boolean mIsCloseWhenUpdateCompleted = true;

	
	protected boolean mLocationHasUpload = false;
	
	protected HsLocationUtil mHsLocationUtil;

	//{{通过bundle传入的变量

	protected DJParams inDJParams;
	
	/**
	 * 是否只能查看
	 */
	private Boolean inAuditOnly = false;
	
	//}}
	
	
	//}}
	
	//{{ 构造方法
	
	public HsActivity_ZD()
	{
		super();
		
		//单据参数默认值
		setDJParams(new DJParams(false, false, false));
	}

	//}}
	
	//{{ 属性
	
		//{{ IsModified
	
	protected void setIsModified(Boolean value)
	{
		this.mIsModified = value;
		
		this.setTitle(value ? this.mTitleNotSaved : this.mTitleSaved);
	}
	
	protected Boolean getIsModified()
	{
		return this.mIsModified;
	}

			//}}
	
		//{{ DJParams
	
	protected void setDJParams(DJParams value)
	{
		inDJParams = value;
	}
	
			//}}
	
		//{{ AnnexClass
	
	public void setAnnexClass(String imageClass)
	{
		mAnnexClass = imageClass;
	}
	
			//}}
	
		//{{ AuditOnly
	
	protected Boolean getAuditOnly()
	{
		return this.inAuditOnly;
	}
	
	protected void setAuditOnly(Boolean value)
	{
		this.inAuditOnly = value;
		
		for(IControlValue control : this.controls)
		{
			if(control.getDefaultAllowEdit())
			{
				control.setAllowEdit(!value);
			}
		}
	}
	
			//}}
	
		//{{ TitleSaved
	
	protected void setTitleSaved(String value)
	{
		this.mTitleSaved = value;
		this.mTitleNotSaved = value + "*";
	}

			//}}
	
		//{{ DJId
	
	protected void setDJId(String value)
	{
		mDJId = value;
		
		if(inDJParams.getHasImages() && mImageFragment != null)
		{
			mImageFragment.setAnnexClassId(value);
		}
		
		if(inDJParams.getHasFiles() && mFileFragment != null)
		{
			mFileFragment.setAnnexClassId(value);
		}
	}
	
	protected String getDJId()
	{
		return mDJId;
	}

			//}}
	
	//}}
	
	//{{ 数据

	protected void setNew()
	{
		this.mIsNewRecorder = 0;
		this.setIsModified(false);
	}
	
	protected void newData()
	{
		for(IControlValue control : this.controls)
		{
			control.Reset();
		}
	}
	
	protected void setSaved()
	{
		this.mIsNewRecorder = 1;
		this.setIsModified(false);
	}
	
	protected abstract void setData(IHsLabelValue Object) throws Exception;

	protected String getBhs(String bh)
	{
		return String.format("<Data><Bh>%s</Bh></Data>",bh);
	}

	//}}
	
	//{{ 布局与初始化
	
	/**
	 * 传入的变量在此处统一初始化，赋初值
	 * @param bundle
	 * @throws Exception 
	 */
	protected void initInComingVariable(Bundle bundle) throws Exception
	{
		super.initInComingVariable(bundle);
		
		if(bundle.containsKey("AuditOnly"))
		{
			inAuditOnly = bundle.getBoolean("AuditOnly", false);
		}

		if(bundle.containsKey("DJParams"))
		{
			setDJParams((DJParams) bundle.getSerializable("DJParams"));
		}

		if(bundle.containsKey("Data"))
		{
			Serializable obj = this.bundle.getSerializable("Data");
			
			if(obj instanceof IHsLabelValue)
			{
				//初始化数据
				this.inData = (IHsLabelValue) this.bundle.getSerializable("Data");
			}
		}
	}
	
	@Override
	protected void initActivityView()
	{
		super.initActivityView();

		this.setAuditOnly(inAuditOnly);
	}
	
	//}}
	
	//{{ 构造片段与适配器

	protected abstract HsFragment_ZD_Main createMainFragment();

	/**
	 * 获取单据明细适配器，如果单据包含明细页，实现此函数。
	 * @return
	 */
	protected HsZDDetailAdapter createDJDetailAdapter()
	{
		return null;
	}

	/**
	 * 如果需要自定义明细条目页，请重载此函数。
	 * @return
	 */
	protected HsFragment_ZD_Detail createDJDetailFragment()
	{
		throw new UnsupportedOperationException("请在子代中实现createDJDetailFragment");
	}

	protected HsFragment_DJ_Image createDJImageFragment(String imageClass)
	{
		HsFragment_DJ_Image f = new HsFragment_DJ_Image();
		f.setAnnexClass(imageClass);
		return f;
	}
	
	protected HsFragment_DJ_File createDJFileFragment(String fileClass)
	{
		HsFragment_DJ_File f = new HsFragment_DJ_File();
		f.setAnnexClass(fileClass);
		return f;
	}
	
	@Override
	protected List<HsFragment> getFragments(Bundle bundle)
	{
		List<HsFragment> fragments = new ArrayList<HsFragment>();

		//{{ 主Tab页
		
		mMainFragment = createMainFragment();
		
		mMainFragment.Title = inDJParams.getMainPageTitle();
		
		mMainFragment.setOnCreateCompletedEventListener(new CreateCompletedEventListener()
		{
			@Override
			public void action(Bundle savedInstanceState)
			{
				try
				{
					if(inData != null)
					{
						mIsNewRecorder = 1;
						//初始数据时不触发数据更新事件。
						mIsTriggerDataChangedEvent = false;
						setData(inData);
						mIsTriggerDataChangedEvent = true;
						setSaved();
					}else 
					{
						mIsNewRecorder = 0;
						newData();
						setNew();
					}
				} catch (Exception e)
				{
					ShowError(e);
				}
			}
		});

		fragments.add(mMainFragment);

		//}}
		
		//{{ 明细Tab页
		
		if(inDJParams.getHasDetail())
		{
			mDetailFragemnt = createDJDetailFragment();

			//在此处设置AllowEdit纯属无奈之举，如果才用SetAuditOnly方法统一设置组件AllowEdit则，菜单已经生成完毕了，无法设置菜单隐藏项。
			mDetailFragemnt.setAllowEdit(!getAuditOnly());
			mDetailFragemnt.setAllowEmpty(inDJParams.getDetailPageAllowEmpty());
			
			controls.add(mDetailFragemnt);
			
			fragments.add(mDetailFragemnt);
		}
		
		//}}
		
		//{{ 图片Tab页
		
		if(inDJParams.getHasImages() && mAnnexClass != null)
		{
			mImageFragment = createDJImageFragment(mAnnexClass);
			
			mImageFragment.addCommEventListener(new CommEventListener(EventCategory.UpdateCompleted)
			{
				@Override
				public void EventHandler(CommEventObject object)
				{
					updateAnnex();
				}
			});
			
			//在此处设置AllowEdit纯属无奈之举，如果才用SetAuditOnly方法统一设置组件AllowEdit则，菜单已经生成完毕了，无法设置菜单隐藏项。
			mImageFragment.setAllowEdit(!getAuditOnly());
			mImageFragment.setAllowEmpty(inDJParams.getImagePageAllowEmpty());
			
			controls.add(mImageFragment);
			
			fragments.add(mImageFragment);
		}
		
		//}}

		//{{ 文件Tab页
		
		if(inDJParams.getHasFiles() && mAnnexClass != null)
		{
			mFileFragment = createDJFileFragment(mAnnexClass);
			
			mFileFragment.addCommEventListener(new CommEventListener(EventCategory.UpdateCompleted)
			{
				@Override
				public void EventHandler(CommEventObject object)
				{
					updateAnnex();
				}
			});
			
			//在此处设置AllowEdit纯属无奈之举，如果才用SetAuditOnly方法统一设置组件AllowEdit则，菜单已经生成完毕了，无法设置菜单隐藏项。
			mFileFragment.setAllowEdit(!getAuditOnly());
			mFileFragment.setAllowEmpty(inDJParams.getFilePageAllowEmpty());
			
			controls.add(mFileFragment);
			
			fragments.add(mFileFragment);
		}

		//}}

		return fragments;
	}

	//}}

	//{{ 系统与上下文菜单

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		//确定按钮 此按钮是主按钮,MainFragment上就不设置按钮了。
		menu.add(0, R.string.str_enter, 0, getString(R.string.str_enter))
		.setIcon(R.drawable.navigation_accept)
		.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId() == R.string.str_enter)
		{
			final ENeedLocationInformation locationInformation = this.inDJParams.getIsNeedLocation();
			
			if(locationInformation != ENeedLocationInformation.None)
			{
				mHsLocationUtil = HsLocationUtil.getInstance(this,HsLocationUtil.DEFAULT_LOCATION_RESULTEXPIREDTIME);

				mHsLocationUtil.setDispatchEventFrequency(EDispatchEventFrequency.Once);
				
				//添加侦听，定位结果返回后更新
				mHsLocationUtil.addOnHsLocationListener(new HsLocationListener()
				{
					@Override
					public void onLocationChanged(Location location)
					{
						CloseWait();

						mHsLocationUtil.removeOnHsLocationListener(this);
						
						if(locationInformation == ENeedLocationInformation.Optional || location != null)
						{
							callUpdateOnOtherThread();
						}else {
							ShowError("定位失败，单据无法保存。");
						}
					}
				});
				
				//定位
				try
				{
					mHsLocationUtil.requestSingleLocation(true,HsLocationUtil.MAX_LOCATION_TIMEOUT);

					ShowWait("提示", "正在进行定位，请稍候...");

				} catch (Exception e)
				{
					ShowError(e);
				}

			}else //不需要定位
			{
				callUpdateOnOtherThread();
			}

			return true;
		}else 
		{
			return super.onOptionsItemSelected(item);
		}
	}

	//}}
	
	//{{ 验证与更新

	/**
	 * 验证每个字段是否合法
	 * @throws Exception
	 */
	protected void validate() throws Exception
	{
		for (IControlValue control : controls)
		{
			control.Validate();
		}
	}

	/**
	 * 此方法工作在非UI线程，记得在函数执行完毕调用 handler.sendmessage @throws Exception
	 */
	protected abstract HsWSReturnObject updateOnOtherThread() throws Exception;
	

	/**
	 * 更新附件
	 */
	protected void updateAnnex()
	{
		if(inDJParams.getIsNeedLocation() != HsLocationUtil.ENeedLocationInformation.None && !mLocationHasUpload)
		{
			this.updateLocation();
		}else if(inDJParams.getHasImages() && mImageFragment.getIsModified())
		{
			this.mImageFragment.updateAnnex();
		}else if(inDJParams.getHasFiles() && mFileFragment.getIsModified())
		{
			this.mFileFragment.updateAnnex();
		}else 
		{
			ShowInformation("更新成功。");
			
			mIsModified = false;
			
			if(mIsCloseWhenUpdateCompleted)
			{
				finish();
			}
		}
	}

	private void updateLocation()
	{
		if(mHsLocationUtil != null)
		{
			final Location location = mHsLocationUtil.getBestLocation();
			
			if(location != null)
			{
				this.ShowWait("请稍候", "正在更新位置信息...");
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							HsWSReturnObject object = application.getWebService().updateSysLocation(
									application.getLoginData().getProgressId(),
									mAnnexClass,
									mDJId,
									location);
							sendDataMessage(object); 
						} catch (Exception e)
						{
							sendErrorMessage(e.getMessage());
						}
					}
				}).start();
			}else
			{
				this.mLocationHasUpload = true;
				this.updateAnnex();
			}
		}
	}

	/*
	 * 调用更新，如果是不是修改状态或未修改则退出。
	 */
	protected void callUpdateOnOtherThread()
	{
		if(!this.getAuditOnly() && this.getIsModified())
		{
			try
			{
				validate();

				ShowWait("请稍候", "正在努力处理数据...");
				
				//设置返回值，List收到此返回值后执行列表刷新
				setResult(Activity.RESULT_OK);

				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							HsWSReturnObject object = updateOnOtherThread();
							sendDataMessage(object);
						} catch (Exception e)
						{
							sendErrorMessage(e.getMessage());
						}
					}
				}).start();
			} catch (Exception e)
			{
				ShowError(e);
			}
		}else 
		{
			finish();
		}
		
	}

	//}}

	//{{ 刷新IUserControlWithAdapter接口数据
	
	protected void callSingleChooseRetrieve(final String flag,
			final String params)
	{
		try
		{
			ShowWait("请稍候", "正在努力处理数据...");

			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						String progressId = application.getLoginData()
								.getProgressId();
						HsWSReturnObject object = application
								.getWebService()
								.getAutoCompleteData(progressId, flag, params);
						sendDataMessage(object);
					} catch (Exception e)
					{
						sendErrorMessage(e.getMessage());
					}
				}
			}).start();
		} catch (Exception e)
		{
			ShowError(e);
		}
	}
	
	//}}

	//{{ WS返回值统一处理
	
	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data) throws Exception
	{
		if(funcname.startsWith("GetAutoCompleteData_") && currentChooseControl != null)// .equals("GetAutoCompleteData_" + currentChooseControl == null ? "" : currentChooseControl.GetFlag()))
		{
			ArrayList<IHsLabelValue> hsKeyLabels = new ModelHsLabelValues().Create(HsGZip.DecompressString(data.toString()));
			currentChooseControl.SetDatas(hsKeyLabels);
		}else if (funcname.equals("UpdateSysLocation"))
		{
			this.mLocationHasUpload = true;
			updateAnnex();
			
		}else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}

	//}}
	
	//{{ 内部类
	
	/**
	 * 字典样式参数，可以配置多页字典分别包括明细页，图片页，文件页等。
	 * @author zhaixuan
	 *
	 */
	public class DJParams
	{
		private String mMainPageTitle = "基本信息";
		
		private String mDetailPageTitle = "明细信息";
		private boolean mHasDetailPage;
		
		private String mImagePageTitle = "图片信息";
		private boolean mHasImagePage;
		
		private String mFilePageTitle = "文件信息";
		private boolean mHasFilePage;
		
		//是否需要未知信息
		private HsLocationUtil.ENeedLocationInformation mIsNeedLocation;
		
		/**
		 * 明细页默认不能为空
		 */
		private boolean mDetailPageAllowEmpty;
		
		private boolean mImagePageAllowEmpty = true;
		
		private boolean mFilePageAllowEmpty = true;
		
		/**
		 * 
		 * @param hasDetail
		 * @param hasImages
		 * @param hasFiles
		 * @param isNeedLocation 是否需要位置信息，0 不需要 1 可以有 2 必须
		 */
		public DJParams(boolean hasDetail,boolean hasImages,boolean hasFiles,HsLocationUtil.ENeedLocationInformation isNeedLocation)
		{
			mHasDetailPage = hasDetail;
			mHasImagePage = hasImages;
			mHasFilePage = hasFiles;
			mIsNeedLocation = isNeedLocation;
		}
		
		public DJParams(boolean hasDetail,boolean hasImages,boolean hasFiles)
		{
			this(hasDetail, hasImages, hasFiles, HsLocationUtil.ENeedLocationInformation.None);
		}
		
		public String getMainPageTitle()
		{
			return mMainPageTitle;
		}
		
		public void setMainPageTitle(String value)
		{
			mMainPageTitle = value;
		}
		
		public String getDetailPageTitle()
		{
			return mDetailPageTitle;
		}
		
		public void setDetailPageTitle(String value)
		{
			mDetailPageTitle = value;
		}
		
		public String getImagePageTitle()
		{
			return mImagePageTitle;
		}
		
		public void setImagePageTitle(String value)
		{
			mImagePageTitle = value;
		}
		
		public String getFilePageTitle()
		{
			return mFilePageTitle;
		}
		
		public void getFilePageTitle(String value)
		{
			mFilePageTitle = value;
		}
		
		/**
		 * 有明细记录
		 */
		public boolean getHasDetail()
		{
			return mHasDetailPage;
		}
		
		/**
		 * 有图片
		 */
		public boolean getHasImages()
		{
			return mHasImagePage;
		}
		
		/**
		 * 有文件
		 */
		public boolean getHasFiles()
		{
			return mHasFilePage;
		}
		
		public HsLocationUtil.ENeedLocationInformation getIsNeedLocation()
		{
			return mIsNeedLocation;
		}
		
		public boolean getHasMultiViews()
		{
			return mHasDetailPage || mHasImagePage || mHasFilePage;
		}
		
		public void setPageDetailAllowEmpty(boolean value)
		{
			this.mDetailPageAllowEmpty = value;
		}
		
		public boolean getDetailPageAllowEmpty()
		{
			return this.mDetailPageAllowEmpty;
		}
		
		public void setImagePageAllowEmpty(boolean value)
		{
			this.mImagePageAllowEmpty = value;
		}
		
		public boolean getImagePageAllowEmpty()
		{
			return this.mImagePageAllowEmpty;
		}
		
		public void setFilePageAllowEmpty(boolean value)
		{
			this.mFilePageAllowEmpty = value;
		}
		
		public boolean getFilePageAllowEmpty()
		{
			return this.mFilePageAllowEmpty;
		}
		
	}
	
	/**
	 * 控件集合
	 * @author zhaixuan
	 *
	 */
	public class UcControls extends ArrayList<IControlValue> implements IOnRetrieveListener
	{
		private static final long serialVersionUID = 1526437016444002960L;

		@Override
		public boolean add(IControlValue object)
		{
			super.add(object);
			
			//添加数据变更事件
			object.addOnDataChangedListener(mDataChangedEventListener);
			
			if(object instanceof IUserControlWithAdapter)
			{
				((IUserControlWithAdapter)object).SetOnRetrieveListener(this);
			}
			
			return true;
		}

		@Override
		public void Retrieve(IUserControlWithAdapter control, String flag,
				String params)
		{
			currentChooseControl = control;
			callSingleChooseRetrieve(flag, params);
		}
		
	}

	//}}
	
}
