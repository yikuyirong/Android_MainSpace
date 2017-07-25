//package com.hungsum.framework.ui.activities;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Color;
//import android.opengl.Visibility;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
//import android.view.ContextMenu;
//import android.view.ContextMenu.ContextMenuInfo;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.hungsum.framework.R;
//import com.hungsum.framework.componments.HsWSReturnObject;
//import com.hungsum.framework.events.CommEventListener;
//import com.hungsum.framework.events.CommEventListener.EventCategory;
//import com.hungsum.framework.events.CommEventObject;
//import com.hungsum.framework.interfaces.IControlValue;
//import com.hungsum.framework.interfaces.IHsLabelValue;
//import com.hungsum.framework.ui.controls.UcListView;
//import com.hungsum.framework.ui.controls.UcViewPager;
//import com.hungsum.framework.zxing.activity.MipcaActivityCapture;
//import com.viewpagerindicator.TitlePageIndicator;
//
///**
// * @author zhaixuan
// *
// */
//public abstract class HsActivity_DJ extends HsActivity_ZD
//{
//	public static int REQUESTCODE_ITEM = 1000;
//	
//	public static int REQUESTCODE_SCAN_QRCODE = 1001;
//
//	protected Boolean mIsTriggerDataChangedEvent = true;
//	
//	private Boolean mIsModified = false;
//
//	/**
//	 * 是否只能查看
//	 */
//	private Boolean mAuditOnly = false;
//
//	protected int isNewRecorder;
//	
//	protected String mTitleSaved;
//	
//	protected String mTitleNotSaved;
//	
//	protected IHsLabelValue mData;
//	
//	protected boolean mIsCloseWhenUpdateCompleted = true;
//
//	protected String getBhs(String bh)
//	{
//		return String.format("<Data><Bh>%s</Bh></Data>",bh);
//	}
//	
//	/*
//	 * 单据ID
//	 */
//	private String mDJId = "-1";
//
//	protected void setDJId(String value)
//	{
//		mDJId = value;
//	}
//	
//	protected String getDJId()
//	{
//		return mDJId;
//	}
//	
//	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu)
//	{
//		super.onCreateOptionsMenu(menu);
//
////		// 审核状态下不允许拍照、选择相册
////		if (!mAuditOnly)
////		{
////			// 拍照
////			menu.add(0, R.string.str_takePhoto, 0,
////					getString(R.string.str_takePhoto))
////					.setIcon(android.R.drawable.ic_menu_camera)
////					.setShowAsActionFlags(
////							MenuItem.SHOW_AS_ACTION_IF_ROOM
////									| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
////
////			// 选择
////			menu.add(0, R.string.str_chooseImage, 1,
////					getString(R.string.str_chooseImage))
////					.setIcon(android.R.drawable.ic_menu_gallery)
////					.setShowAsActionFlags(
////							MenuItem.SHOW_AS_ACTION_IF_ROOM
////									| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
////		}		
//		
//		
//		return true;
//		
//		
//	}
//
//	/* 
//	 * 构建上下文菜单
//	 */
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo)
//	{
//		super.onCreateContextMenu(menu, v, menuInfo);
//		
//		menu.setHeaderTitle("请选择");
//	}
//	
//	@Override
//	protected void initLayout()
//	{
//		super.initLayout();
//		setContentView(R.layout.hsactivity_viewpager);
//
//		ucViewPager = (UcViewPager) findViewById(R.id.ucViewPager);
//		
//		ucViewPager.setAdapter(new ViewerPagerAdapter(getViews(LayoutInflater.from(this))));
//		
//		ucViewPager.setCurrentItem(0);
//		
//		ucIndicator = (TitlePageIndicator) findViewById(R.id.ucIndicator);
//		
//		ucIndicator.setTextColor(Color.GRAY);
//		
//		ucIndicator.setSelectedColor(Color.BLACK);
//
//		ucIndicator.setViewPager(ucViewPager);
//		
//		ucIndicator.setOnPageChangeListener(new OnPageChangeListener()
//		{
//			@Override
//			public void onPageSelected(int arg0)
//			{
//				HsActivity_DJ.this.onPageSelected(arg0);
//			}
//			
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2)
//			{
//			}
//			
//			@Override
//			public void onPageScrollStateChanged(int arg0)
//			{
//			}
//		});
//		
//		ucIndicator.setVisibility(View.GONE);
//		
//		
////		//如果只有主页面则不显示指示器
////		if(mDJParams.GetHasMultiViews())
////		{
////			this.ucIndicator.setVisibility(View.VISIBLE);
////		}else {
////			ucIndicator.setVisibility(View.GONE);
////			
////		}
//
//	}
//
//	@Override
//	protected void initView()
//	{
//		if(this.bundle != null)
//		{		
//			//初始化数据
//			this.mData = (IHsLabelValue) this.bundle.getSerializable("Data");
//			//初始化编辑状态
//			this.setAuditOnly(bundle.getBoolean("AuditOnly", false));
//		}
//
//		if(this.mData != null)
//		{
//			this.isNewRecorder = 1;
//			//初始数据时不触发数据更新事件。
//			mIsTriggerDataChangedEvent = false;
//			this.setData(this.mData);
//			mIsTriggerDataChangedEvent = true;
//			this.setSaved();
//		}else 
//		{
//			this.isNewRecorder = 0;
//			this.newData();
//			this.setNew();
//		}
//	}
//	
//	protected List<View> getViews(LayoutInflater inflater)
//	{
//		//主信息页 MainPage
//		ArrayList<View> views = new ArrayList<View>();
//		
//		View mainView = inflater.inflate(R.layout.hsactivity_zd, null);
//
//		this.ucMain = (LinearLayout) mainView.findViewById(R.id.container);
//		
//		views.add(mainView);
//
//		return views; 
//	}
//	
//	
//	@Override
//	protected void initEvent()
//	{
//		super.initEvent();
//		
//		CommEventListener dataChangedEventListener = new CommEventListener(EventCategory.DataChanged)
//		{
//			@Override
//			public void EventHandler(CommEventObject object)
//			{
//				setIsModified(true);
//			}
//		};
//		
//		for (IControlValue control : controls)
//		{
//			control.addOnDataChangedListener(dataChangedEventListener);
//		}
//	}
//
//
//	protected void setIsModified(Boolean value)
//	{
//		this.mIsModified = value;
//		
//		this.setTitle(value ? this.mTitleNotSaved : this.mTitleSaved);
//	}
//	
//	protected Boolean getIsModified()
//	{
//		return this.mIsModified;
//	}
//
//	protected void setNew()
//	{
//		this.isNewRecorder = 0;
//		this.setIsModified(false);
//	}
//	
//	protected void newData()
//	{
//		for(IControlValue control : this.controls)
//		{
//			control.Reset();
//		}
//	}
//	
//	protected void setSaved()
//	{
//		this.isNewRecorder = 1;
//		this.setIsModified(false);
//	}
//	
//	protected abstract void setData(IHsLabelValue Object);
//
//	protected Boolean getAuditOnly()
//	{
//		return this.mAuditOnly;
//	}
//	
//	protected void setAuditOnly(Boolean value)
//	{
//		this.mAuditOnly = value;
//		
//		for(IControlValue control : this.controls)
//		{
//			if(control.getDefaultAllowEdit())
//			{
//				control.setAllowEdit(!value);
//			}
//		}
//	}
//
//	protected void scanQRCode(boolean isRepeat)
//	{
//		Intent intent = new Intent();
//		intent.setClass(HsActivity_DJ.this, MipcaActivityCapture.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		intent.putExtra("isRepeat", isRepeat);
//		startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_SCAN_QRCODE);
//	}
//	
//	/*
//	 * 调用更新，如果是不是修改状态或未修改则退出。
//	 */
//	@Override
//	protected void callUpdateOnOtherThread()
//	{
//		if(!this.getAuditOnly() && this.getIsModified())
//		{
//			try
//			{
//				validate();
//				ShowWait("请稍候", "正在努力处理数据...");
//
//				//设置返回值，List收到此返回值后执行列表刷新
//				setResult(Activity.RESULT_OK);
//
//				new Thread(new Runnable()
//				{
//					@Override
//					public void run()
//					{
//						try
//						{
//							HsWSReturnObject object = update();
//							sendDataMessage(object);
//						} catch (Exception e)
//						{
//							sendErrorMessage(e.getMessage());
//						}
//					}
//				}).start();
//			} catch (Exception e)
//			{
//				ShowError(e);
//			}		
//		}else 
//		{
//			finish();
//		}
//	}
//
//}
