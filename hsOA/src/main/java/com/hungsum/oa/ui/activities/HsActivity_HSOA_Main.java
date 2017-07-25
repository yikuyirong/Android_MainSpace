package com.hungsum.oa.ui.activities;

import android.content.Intent;

import com.hungsum.framework.ui.activities.HsActivity_Main;
import com.hungsum.oa.others.HSOADjlx;
import com.hungsum.oa.others.HSOAFuncKey;
import com.hungsum.oa.ui.activities.clgl.HsActivity_List_HsClbmddjl;
import com.hungsum.oa.ui.activities.clgl.HsActivity_List_HsClbxjl;
import com.hungsum.oa.ui.activities.clgl.HsActivity_List_HsClda;
import com.hungsum.oa.ui.activities.clgl.HsActivity_List_HsClsyjl;
import com.hungsum.oa.ui.activities.clgl.HsActivity_List_HsClwbjl;
import com.hungsum.oa.ui.activities.clgl.HsActivity_List_HsClxsjl;
import com.hungsum.oa.ui.activities.clgl.HsActivity_List_HsClztbhjl;
import com.hungsum.oa.ui.activities.kq.HsActivity_DJ_HsKq;
import com.hungsum.oa.ui.activities.kq.HsActivity_List_HsKqjl;
import com.hungsum.oa.ui.activities.rygl.HsActivity_List_HsRybmddjl;
import com.hungsum.oa.ui.activities.rygl.HsActivity_List_HsRyda;
import com.hungsum.oa.ui.activities.rygl.HsActivity_List_HsRyjcjl;
import com.hungsum.oa.ui.activities.rygl.HsActivity_List_HsRyldht;
import com.hungsum.oa.ui.activities.rygl.HsActivity_List_HsRysyjl;
import com.hungsum.oa.ui.activities.rygl.HsActivity_List_HsRyxjjl_Confirm;
import com.hungsum.oa.ui.activities.rygl.HsActivity_List_HsRyxjjl_Operation;
import com.hungsum.oa.ui.activities.rygl.HsActivity_List_HsRyztbhjl;
import com.hungsum.oa.ui.activities.rygl.HsActivity_List_HsRyzwddjl;
import com.hungsum.oa.workflow.ui.activities.HsActivity_List_HsFyspd;
import com.hungsum.oa.workflow.ui.activities.HsActivity_List_HsUserLcdj;

public abstract class HsActivity_HSOA_Main extends HsActivity_Main
{
	@Override
	protected void doFunc(String funcKey) throws Exception
	{
		if (funcKey.equals(HSOAFuncKey.HS考勤打卡))
		{
			Intent intent = new Intent();
			intent.setClass(this, HsActivity_DJ_HsKq.class);
			intent.putExtra("Title", HSOADjlx.HL考勤记录);
			startActivity(intent);
		} else if (funcKey.equals(HSOAFuncKey.HS考勤查询))
		{
			Intent intent = new Intent();
			intent.setClass(this, HsActivity_List_HsKqjl.class);
			intent.putExtra("Title", HSOADjlx.HL考勤记录);
			startActivity(intent);
		} else if(funcKey.equals(HSOAFuncKey.HS人员档案管理))
		{
			Intent intent = new Intent();
			intent.setClass(this, HsActivity_List_HsRyda.class);
			intent.putExtra("Title","人员档案管理");
			startActivity(intent);
		}else if(funcKey.equals(HSOAFuncKey.HS劳动合同管理))
		{
			Intent intent = new Intent();
			intent.setClass(this, HsActivity_List_HsRyldht.class);
			intent.putExtra("Title","劳动合同管理");
			startActivity(intent);
		}else if(funcKey.equals(HSOAFuncKey.HS人员部门调动))
		{
			Intent intent = new Intent();
			intent.setClass(this, HsActivity_List_HsRybmddjl.class);
			intent.putExtra("Title","人员部门调动");
			startActivity(intent);
		}else if(funcKey.equals(HSOAFuncKey.HS人员职务调动))
		{
			Intent intent = new Intent();
			intent.setClass(this, HsActivity_List_HsRyzwddjl.class);
			intent.putExtra("Title","人员职务调动");
			startActivity(intent);
		}else if(funcKey.equals(HSOAFuncKey.HS人员奖惩记录))
		{
			Intent intent = new Intent();
			intent.setClass(this, HsActivity_List_HsRyjcjl.class);
			intent.putExtra("Title","人员奖惩记录");
			startActivity(intent);
		}else if(funcKey.equals(HSOAFuncKey.HS人员休假管理))
		{
			Intent intent = new Intent();
			intent.setClass(this, HsActivity_List_HsRyxjjl_Operation.class);
			intent.putExtra("Title","人员休假管理");
			startActivity(intent);
		}else if(funcKey.equals(HSOAFuncKey.HS人员休假确认))
		{
			Intent intent = new Intent();
			intent.setClass(this, HsActivity_List_HsRyxjjl_Confirm.class);
			intent.putExtra("Title","人员休假确认");
			startActivity(intent);
		}else if(funcKey.equals(HSOAFuncKey.HS人员状态变化))
		{
			Intent intent = new Intent();
			intent.setClass(this, HsActivity_List_HsRyztbhjl.class);
			intent.putExtra("Title","人员状态变化");
			startActivity(intent);
		}else if(funcKey.equals(HSOAFuncKey.HS人员生涯记录))
		{
			Intent intent = new Intent();
			intent.setClass(this, HsActivity_List_HsRysyjl.class);
			intent.putExtra("Title","人员生涯记录");
			startActivity(intent);
		}else if(funcKey.equals(HSOAFuncKey.HS车辆档案管理))
		{
			Intent intent = new Intent();
			intent.setClass(this, HsActivity_List_HsClda.class);
			intent.putExtra("Title","车辆档案管理");
			startActivity(intent);
		}else if(funcKey.equals(HSOAFuncKey.HS车辆部门调动))
		{
			Intent intent = new Intent();
			intent.setClass(this, HsActivity_List_HsClbmddjl.class);
			intent.putExtra("Title","车辆部门调动");
			startActivity(intent);
		}else if(funcKey.equals(HSOAFuncKey.HS车辆维修保养记录))
		{
			Intent intent = new Intent();
			intent.setClass(this, HsActivity_List_HsClwbjl.class);
			intent.putExtra("Title","车位维保记录");
			startActivity(intent);
		}else if(funcKey.equals(HSOAFuncKey.HS车辆保险记录))
		{
			Intent intent = new Intent();
			intent.setClass(this, HsActivity_List_HsClbxjl.class);
			intent.putExtra("Title","车辆保险记录");
			startActivity(intent);
		}else if(funcKey.equals(HSOAFuncKey.HS车辆行驶记录))
		{
			Intent intent = new Intent();
			intent.setClass(this, HsActivity_List_HsClxsjl.class);
			intent.putExtra("Title","车辆行驶记录");
			startActivity(intent);
		}else if(funcKey.equals(HSOAFuncKey.HS车辆状态变化))
		{
			Intent intent = new Intent();
			intent.setClass(this, HsActivity_List_HsClztbhjl.class);
			intent.putExtra("Title","车辆状态变化");
			startActivity(intent);
		}else if(funcKey.equals(HSOAFuncKey.HS车辆生涯记录))
		{
			Intent intent = new Intent();
			intent.setClass(this, HsActivity_List_HsClsyjl.class);
			intent.putExtra("Title","车辆生涯记录");
			startActivity(intent);
		}
		else if(funcKey.equals(HSOAFuncKey.HS待办事项)) //待办事项
		{
			Intent intent = new Intent();
			intent.setAction(com.hungsum.oa.HSOAActionAttr.ACTION_DBSX);
			intent.addCategory(getPackageName());
			intent.putExtra("Title", "待办事项");
			startActivity(intent);
		}
		else if(funcKey.equals(HSOAFuncKey.HS费用审批单)) //费用审批单
		{
			Intent intent = new Intent();
			intent.setClass(this, HsActivity_List_HsFyspd.class);
			intent.putExtra("Title","费用审批单");
			startActivity(intent);
		}
		else if(funcKey.equals(HSOAFuncKey.HS自定义流程单据)) //自定义流程单据
		{
			Intent intent = new Intent();
			intent.setClass(this, HsActivity_List_HsUserLcdj.class);
			intent.putExtra("Title","自定义流程单据");
			startActivity(intent);
		}else 
		{
			super.doFunc(funcKey);
		}
	}
}
