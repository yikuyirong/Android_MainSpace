package com.hungsum.framework.ui.activities;

import android.content.Intent;

import com.hungsum.framework.zxing.activity.MipcaActivityCapture;

/**
 * @author zhaixuan
 *
 */
public abstract class HsActivity_DJ extends HsActivity_ZD
{
	public static int REQUESTCODE_ITEM = 1000;
	
	public static int REQUESTCODE_SCAN_QRCODE = 1001;


	/**
	 * 扫面条码
	 * @param isRepeat
	 */
	protected void scanQRCode(boolean isRepeat)
	{
		Intent intent = new Intent();
		intent.setClass(HsActivity_DJ.this, MipcaActivityCapture.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("isRepeat", isRepeat);
		startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_SCAN_QRCODE);
	}

}
