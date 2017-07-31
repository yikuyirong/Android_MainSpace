package com.hungsum.framework.ui.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hungsum.framework.SysActionAttr;
import com.hungsum.framework.componments.HsWSReturnObject;

public abstract class HsActivity_Welcome extends HsActivity
{
	private TextView _ucVersionNumber;
	
	private TextView _ucMessage;
	
	private ProgressBar _ucProgressBar;

	private int _progress;

	/**
	 * 更新文件的网络URL
	 */
	private String _downloadFileURL;

	/**
	 * 更新文件的本地存储路径
	 */
	private String _downloadFilePath;

	/**
	 * 更新文件的本地存储文件名
	 */
	private String _downloadFileName;

	public static int DOWNLOAD_PROGRESS = 0;
	public static int DOWNLOAD_FINISHED = 1;

	// 完成变量，登录过程默认延时3秒，利用这个时间进行更新判断，延时时间等于判断更新与3秒中的最大者。
	private Boolean _actionIsCompleted = false;

	protected abstract ProgressBar getProgressBar();
	
	protected abstract TextView getMessage();
	
	protected abstract TextView getVersionNumber();
	
	@Override
	protected void initActivityView()
	{
		super.initActivityView();

		this._ucMessage = getMessage();
		
		this._ucProgressBar = getProgressBar();
		
		this._ucVersionNumber = getVersionNumber();
		
		if(this._ucVersionNumber != null)
		{
			this._ucVersionNumber.setText(this.application.GetVersion());
		}

		if(this._ucMessage != null)
		{
			this._ucMessage.setText("正在登录，请稍候...");
		}

		// 检查版本
		this._checkNeedUpdate();

		//
		new Handler().postDelayed(new Runnable()
		{

			@Override
			public void run()
			{
				if (_actionIsCompleted)
				{
					_startLoginActivity();
				} else
				{
					_actionIsCompleted = true;
				}
			}
		}, 3000);

	}

	private void _checkNeedUpdate()
	{
		try
		{
			ShowWait("请稍候", "正在检查新版本...");

			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						HsWSReturnObject object = application.getWebService()
								.getAndroidClientInfo();
						sendDataMessage(object);
					} catch (Exception e)
					{
						sendErrorMessage(e.getMessage());
						
						//如果登录发生错误，则退出系统
						mHandler.postDelayed(new Runnable()
						{
							@Override
							public void run()
							{
								finish();
							}
						},3000);
					}
				}
			}).start();
		} catch (Exception e)
		{
			ShowError(e);
		}
	}

	private void _update(Boolean isForced)
	{
		if (isForced) //重要更新
		{
			AlertDialog ad = showAlert("发现软件版本关键更新，是否立刻下载安装？" , "需要更新才能继续使用？",
					"更新",
					new UpgradeHandler(),
					"退出软件",
					new QuitHandler());
			ad.setOnCancelListener(new OnCancelListener()
			{
				@Override
				public void onCancel(DialogInterface dialog)
				{
					finish();
				}
			});
		} else
		{
			AlertDialog ad = showAlert("发现软件版本更新，是否立刻下载安装？", null,
					"更新",
					new UpgradeHandler(), 
					"下次再说",
					new StartLoginActivityHandler());
			ad.setOnCancelListener(new OnCancelListener()
			{
				
				@Override
				public void onCancel(DialogInterface dialog)
				{
					if(_actionIsCompleted)
					{
						_startLoginActivity();
					}else {
						_actionIsCompleted = true;
					}
				}
			});
		}
	}

	void _upgradeFile() throws Exception
	{
		//初始化进度条
		if(this._ucProgressBar != null)
		{
			this._ucProgressBar.setMax(100);
			this._ucProgressBar.setProgress(0);
		}
		
		new SingleThreadDownloadTask().start();
	}

	// 启动登录界面
	private void _startLoginActivity()
	{
		Intent intent = new Intent();
		intent.setAction(SysActionAttr.ACTION_HSLOGIN);
		intent.addCategory(getPackageName());
		startActivity(intent);
		finish();
	}

	@Override
	public void actionAfterWSReturnData(String funcname, Serializable data)
			throws Exception
	{
		if (funcname.equals("GetAndroidClientInfo"))
		{
			// 服务器返回android客户端信息，“；”分割，第一段表示版本号(必须)，第二段表示存储路径（必须）,第三段表示一些额外的提示（选填）。
			String[] androidClientInfo = data.toString().split(";");

			// apk版本
			String androidClientVersion = androidClientInfo[0];

			StringBuffer sb = new StringBuffer();
			sb.append(this.application.GetApplicationName());
			sb.append(androidClientVersion);
			sb.append(".apk");

			_downloadFileName = sb.toString();

			_downloadFileURL = this.application.GetWebPath()
					+ androidClientInfo[1];

			//如果存在额外提示
			if(androidClientInfo.length >= 3)
			{
				ShowInformation(androidClientInfo[2]);
			}
			
			switch (application.CheckNeedUpdate(androidClientVersion))
			{
				case 0: // 不需要升级
					if (_actionIsCompleted)
					{
						_startLoginActivity();
					} else
					{
						_actionIsCompleted = true;
					}
					ShowInformation("不需要更新，目前是最新版本。");
					break;
				case 1: // 建议升级
					this._update(false);
					break;
				case 2: // 强制升级
					this._update(true);
					break;
				default:
					break;
			}
		} else
		{
			super.actionAfterWSReturnData(funcname, data);
		}
	}

	private WelcomeInnerHandler _handler = new WelcomeInnerHandler(this);

	/**
	 * Handler对象，用于线程间通信。
	 */
	static class WelcomeInnerHandler extends Handler
	{
		WeakReference<HsActivity> wr;

		public WelcomeInnerHandler(HsActivity activity)
		{
			wr = new WeakReference<HsActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg)
		{
			HsActivity_Welcome activity = (HsActivity_Welcome) wr.get();

			switch (msg.what)
			{
				case 0: // 下载中
					if(activity._ucProgressBar != null)
					{
						activity._ucProgressBar.setProgress(activity._progress);
					}
					if(activity._ucMessage != null)
					{
						activity._ucMessage.setText("已下载 " + activity._progress + "%");
					}
					break;
				case 1: // 下载结束
					File apkfile = new File(activity._downloadFilePath
							+ activity._downloadFileName);
					if (!apkfile.exists())
					{
						return;
					}
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
							"application/vnd.android.package-archive");
					activity.startActivity(i);
					
					activity.finish();
					break;
				case -1: // 下载进程发生错误
					activity.ShowError(msg.getData().getString("error"));
					activity.finish();
			}
		}
	};

	public class SingleThreadDownloadTask extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED))
				{
					throw new Exception("扩展存储未准备好，不能下载更新。");
				}

				// 下载目录
				_downloadFilePath = Environment.getExternalStorageDirectory()
						+ "/HSDownload/" + getPackageName() + "/";

				File file = new File(_downloadFilePath);
				// 创建下载目录
				if (!file.exists())
				{
					if(!file.mkdirs())
					{
						throw new Exception("创建下载目录失败。");
					}
				}
				URL url = new URL(_downloadFileURL);
				// 创建连接
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				// 获取文件大小
				int length = conn.getContentLength();
				// 创建输入流
				InputStream is = conn.getInputStream();

				File apkFile = new File(_downloadFilePath + _downloadFileName);
				FileOutputStream fos = new FileOutputStream(apkFile);
				int count = 0;
				// 缓存
				byte buf[] = new byte[1024];
				// 写入到文件中
				do
				{
					int numread = is.read(buf);
					count += numread;
					// 计算进度条位置
					_progress = (int) (((float) count / length) * 100);
					// 更新进度
					_handler.sendEmptyMessage(DOWNLOAD_PROGRESS);
					if (numread <= 0)
					{
						// 下载完成
						_handler.sendEmptyMessage(DOWNLOAD_FINISHED);
						break;
					}
					// 写入文件
					fos.write(buf, 0, numread);
				} while (true);
				fos.close();
				is.close();
			} catch (Exception e)
			{
				Message msg = new Message();
				msg.what = -1;
				Bundle bundle = new Bundle();
				bundle.putString("error", e.getMessage());
				msg.setData(bundle);
				_handler.sendMessage(msg);
			}
		}
	}
	
	/**
	 * @author zhaixuan
	 * 点击升级按钮处理程序
	 */
	private class UpgradeHandler implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			try
			{
				_upgradeFile();
			} catch (Exception e)
			{
				//更新中发生异常退出。
				ShowError(e);
				finish();
			}
		}
	}
	
	/**
	 * @author zhaixuan
	 * 点击退出按钮处理程序
	 */
	private class QuitHandler implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			finish();
		}
	}

	/**
	 * @author zhaixuan
	 * 点击按钮启动登录界面处理程序
	 */
	private class StartLoginActivityHandler implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			if(_actionIsCompleted)
			{
				_startLoginActivity();
			}else {
				_actionIsCompleted = true;
			}
		}
	}
}
