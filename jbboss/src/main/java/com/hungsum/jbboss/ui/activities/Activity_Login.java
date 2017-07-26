package com.hungsum.jbboss.ui.activities;

import android.content.Intent;

import com.hungsum.framework.ui.activities.HsActivity_Login;
import com.hungsum.framework.utils.HsGZip;
import com.hungsum.jbboss.componments.JbSnyLoginData;
import com.hungsum.jbboss.models.ModelJbSnyLogin;

import java.io.Serializable;

/**
 * Created by zhaixuan on 2017/7/21.
 */

public class Activity_Login extends HsActivity_Login
{

    @Override
    protected void newData() {

        super.newData();

        this.ucUsername.setHint("请输入用户编号/手机号码");
    }

    @Override
    public void actionAfterWSReturnData(String funcname, Serializable data) throws Exception
    {
        if(funcname.equals("SnyLogin"))
        {
            this.saveLastLoginData();

            String returnData = HsGZip.DecompressString(data.toString());

            JbSnyLoginData loginData = new ModelJbSnyLogin().Create(returnData);

            //将登录信息保存至本地缓存中
            application.setLoginData(loginData);

            //登录主页面
            Intent intent = new Intent();
            intent.setAction(com.hungsum.framework.SysActionAttr.ACTION_HSMAIN);
            intent.addCategory(getPackageName());
            intent.putExtra(getString(com.hungsum.framework.R.string.comm_logindata),loginData);
            startActivity(intent);

            //将当前页自栈中移出。
            finish();
        }else
        {
            super.actionAfterWSReturnData(funcname, data);
        }

    }
}
