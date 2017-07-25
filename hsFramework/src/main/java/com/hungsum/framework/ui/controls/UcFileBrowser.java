package com.hungsum.framework.ui.controls;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventListener;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hungsum.framework.R;
import com.hungsum.framework.adapter.HsUserLabelValueAdapter;
import com.hungsum.framework.adapter.HsUserLabelValueAdapter.ViewHolder;
import com.hungsum.framework.componments.HsLabelValue;
import com.hungsum.framework.interfaces.IHsLabelValue;

/**
 * 文件浏览器，实现文件的打开与存储选择
 * @author zhaixuan
 *
 */
public class UcFileBrowser
{
	private Context mContext;
	
	private File mCurrentFile;
	
	private File mCurrentFolder;

	private HsUserLabelValueAdapter mAdapter;

	private UcListView ucListView;
	
	private AlertDialog mAd;

	private EFileBrowerType mBrowerType;
	
	private FileChooseEventListener mListener;
	
	public UcFileBrowser(Context context,File currentFile)
	{
		mContext = context;
		
		if(currentFile.isDirectory())
		{
			mCurrentFolder = currentFile;
		}else {
			mCurrentFile = currentFile;
		}
	}

	public void showDialog(final EFileBrowerType browerType) throws FileNotFoundException
	{
		mBrowerType = browerType;
		
		if (mContext == null)
		{
			return;
		}
		
		if(mCurrentFile == null && mCurrentFolder == null)
		{
			throw new FileNotFoundException("当前目录文件不存在。");
		}
		
		if (this.mAdapter == null)
		{
			this.mAdapter = new HsUserLabelValueAdapter(mContext,R.layout.control_list_file_1);
		}
		
		this.retrieve(browerType); //刷新数据

		LinearLayout view = new LinearLayout(mContext);
		view.setOrientation(LinearLayout.VERTICAL);
		view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

//		UcTextBox ucTextBox = new UcTextBox(mContext);
//		ucTextBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//		ucTextBox.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_search,0);
//		ucTextBox.addTextChangedListener(new TextWatcher()
//		{
//			
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count)
//			{
//				mFilter.filter(s.toString());
//				//adapter.getFilter().filter(s.toString());
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after)
//			{
//			}
//			
//			@Override
//			public void afterTextChanged(Editable s)
//			{
//			}
//		});
//		view.addView(ucTextBox);

		this.ucListView = new UcListView(mContext);
		this.ucListView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		//this.ucListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		this.ucListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
		this.ucListView.setAdapter(this.mAdapter);
		this.ucListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				HsUserLabelValueAdapter.ViewHolder holder = (ViewHolder) arg1.getTag();
				if(holder != null)
				{
					mCurrentFile = new File((String) holder.UcValue.getText());
					
					if(mCurrentFile.isDirectory())
					{
						mCurrentFolder = mCurrentFile;
						
						retrieve(browerType);
					}else 
					{
						if(mBrowerType == EFileBrowerType.Choose)
						{
							try
							{
								setReturnFile();
							} catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					}
				}else 
				{
					mAd.dismiss();
				}
			}
			
		});
		view.addView(this.ucListView);

		mAd = new AlertDialog.Builder(mContext).setView(view)
				.setTitle(browerType == EFileBrowerType.Choose ? "选择文件" : "保存文件")
				.setPositiveButton(browerType == EFileBrowerType.Choose ? "取消" : "确定",
						browerType == EFileBrowerType.Choose ? 
						new DialogInterface.OnClickListener()
						{
							
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								mAd.dismiss();
							}
						}: 
						new DialogInterface.OnClickListener()
						{
							
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								try
								{
									setReturnFile();
								} catch (Exception e)
								{
									e.printStackTrace();
								}
							}
						} ).create();

		mAd.show();

		// 设置大小
		WindowManager.LayoutParams layoutParams = mAd.getWindow()
				.getAttributes();
		layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
		layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mAd.getWindow().setAttributes(layoutParams);
	}
	
	private void retrieve(EFileBrowerType browerType)
	{
		File current = mCurrentFolder != null ? mCurrentFolder : mCurrentFile;
		
		this.mAdapter.clear();
		
//		List<IHsLabelValue> items = new ArrayList<IHsLabelValue>();
		
		String parentFile = current.getParent();
		String rootPath = Environment.getExternalStorageDirectory().getParent();

		File[] listFiles = current.listFiles();
		
		if(listFiles != null)
		{
			List<IHsLabelValue> files = new ArrayList<IHsLabelValue>();
			List<IHsLabelValue> dirs = new ArrayList<IHsLabelValue>();

			for (final File file : listFiles)
			{
				//选择模式下不能读，保存模式下不能写则略过。
				if((browerType == EFileBrowerType.Choose && !file.canRead()) ||
					browerType == EFileBrowerType.Save && !file.canWrite())
				{
					continue;
				}
				
				if(file.isDirectory())
				{
					files.add(new HsLabelValue(file.getName(),file.getAbsolutePath())
					{
						private static final long serialVersionUID = 4995164281899559828L;
	
						@Override
						public Drawable getOperationImage()
						{
							return mContext.getResources().getDrawable(R.drawable.folder_48);
						}
					});
				}else {
					dirs.add(new HsLabelValue(file.getName(),file.getAbsolutePath())
					{
						private static final long serialVersionUID = -4486407954120335970L;

						@Override
						public Drawable getOperationImage()
						{
							return mContext.getResources().getDrawable(R.drawable.paper_48); 
						}
					});
				}
			}
			
			Comparator<IHsLabelValue> comparator = new Comparator<IHsLabelValue>()
			{

				@Override
				public int compare(IHsLabelValue lhs, IHsLabelValue rhs)
				{
					return lhs.getLabel().compareTo(rhs.getLabel());
				}
			};

			Collections.sort(dirs, comparator);
			
			Collections.sort(files, comparator);

			files.addAll(dirs);

			if(!parentFile.equals(rootPath))
			{
				files.add(0, new HsLabelValue("..",parentFile));
			}

			this.mAdapter.add(files);
		}
	}
	
	private void setReturnFile() throws Exception
	{
		if(mListener != null)
		{
			if(mBrowerType == EFileBrowerType.Choose)
			{
				if(mCurrentFile != null)
				{
					mListener.doAction(mCurrentFile,mBrowerType);
				}
			}else {
				if(mCurrentFolder != null)
				{
					mListener.doAction(mCurrentFolder,mBrowerType);
				}
			}
		}
		
		mAd.dismiss();
	}
	
	public void setOnFileChooseEventListener(FileChooseEventListener listener)
	{
		this.mListener = listener;
	}
	
	public interface FileChooseEventListener extends EventListener
	{
		void doAction(File file,EFileBrowerType browerType) throws Exception;
	}
	
	public enum EFileBrowerType
	{
		Choose,Save
	}
	
}
