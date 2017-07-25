package com.hungsum.oa.webservices;

import org.ksoap2.serialization.SoapObject;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.utils.HsGZip;
import com.hungsum.framework.webservices.HsWebService;

/**
 * 
 * 
 * @author zhaixuan
 * 服务类，封装了所有调用ws的方法。
 *
 *
 */
public class HSOAWebService extends HsWebService
{

	public HSOAWebService(String wsdl,String namespace)
	{
		super(wsdl, namespace);
	}

	//{{ 考勤管理
	
	public HsWSReturnObject signHsKq(String progressId,String jlzt) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<Jlzt>" + jlzt + "</Jlzt>");
		sb.append("</Data>");
		
		String funcName = "SignHsKq";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}	

	public HsWSReturnObject getHsKqjls(String progressId,String beginDate,String endDate) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("</Data>");
		
		String funcName = "GetHsKqjls";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}	
	
	public HsWSReturnObject getCurrentHsKqzt(String progressId) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("</Data>");
		
		String funcName = "GetCurrentHsKqzt";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}	
	
	
	//}}
	
	//{{ 人员管理
	
	/**
	 * 显示人员档案
	 * @param progressId
	 * @param ryzt
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject showHsRydas(String progressId,String ryzt) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<Ryzt>" + ryzt + "</Ryzt>");
		sb.append("</Data>");
		
		String funcName = "ShowHsRydas";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	/**
	 * 更新人员档案
	 * @param progressId	进程号
	 * @param ryId	人员ID
	 * @param dabh	档案编号
	 * @param ryxm	人员姓名
	 * @param bmbh	部门编号
	 * @param zwbh	职务编号
	 * @param ryxb	人员性别
	 * @param rymz	人员民族
	 * @param whcd	文化程度
	 * @param gzrq	工作日期
	 * @param rzrq	入职日期
	 * @param sfzh	身份证号
	 * @param ryzt	人员状态
	 * @param flag	更新标志
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject updateHsRyda(String progressId,String ryId,String dabh,String ryxm,
			String bmbh,String zwbh,String ryxb,String rymz,String whcd,String gzrq,String rzrq,
			String sfzh,String ryzt,int flag) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<RyId>" + ryId + "</RyId>");
		sb.append("<Dabh>" + dabh + "</Dabh>");
		sb.append("<Ryxm>" + ryxm + "</Ryxm>");
		sb.append("<Bmbh>" + bmbh + "</Bmbh>");
		sb.append("<Zwbh>" + zwbh + "</Zwbh>");
		sb.append("<Ryxb>" + ryxb + "</Ryxb>");
		sb.append("<Rymz>" + rymz + "</Rymz>");
		sb.append("<Whcd>" + whcd + "</Whcd>");
		sb.append("<Gzrq>" + gzrq + "</Gzrq>");
		sb.append("<Rzrq>" + rzrq + "</Rzrq>");
		sb.append("<Sfzh>" + sfzh + "</Sfzh>");
		sb.append("<Ryzt>" + ryzt + "</Ryzt>");
		sb.append("<Flag>" + String.valueOf(flag) + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsRyda";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}


	/**
	 * 显示劳动合同
	 * @param progressId
	 * @param beginDate
	 * @param endDate
	 * @param htzt
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject showHsRyldhts(String progressId,String beginDate,String endDate, String htzt) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("<Htzt>" + htzt + "</Htzt>");
		sb.append("</Data>");
		
		String funcName = "ShowHsRyldhts";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	/**
	 * 更新人员劳动合同
	 * @param progressId	进程号
	 * @param htId	合同ID
	 * @param htbh	合同编号
	 * @param ryId	人员ID
	 * @param htrq	合同日期
	 * @param htlx	合同类型
	 * @param htnx	合同年限
	 * @param bz	备注
	 * @param flag	更新标志
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject updateHsRyldht(String progressId,String htId,String htbh,String ryId,
			String htrq,String htlx,String htnx, String bz,int flag) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<HtId>" + htId + "</HtId>");
		sb.append("<Htbh>" + htbh + "</Htbh>");
		sb.append("<RyId>" + ryId + "</RyId>");
		sb.append("<Htrq>" + htrq + "</Htrq>");
		sb.append("<Htlx>" + htlx + "</Htlx>");
		sb.append("<Htnx>" + htnx + "</Htnx>");
		sb.append("<Bz>" + bz + "</Bz>");
		sb.append("<Flag>" + String.valueOf(flag) + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsRyldht";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	/**
	 * 显示人员部门调动记录
	 * @param progressId
	 * @param beginDate
	 * @param endDate
	 * @param jlzt
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject showHsRybmddjls(String progressId,String beginDate,String endDate, String jlzt) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("<Jlzt>" + jlzt + "</Jlzt>");
		sb.append("</Data>");
		
		String funcName = "ShowHsRybmddjls";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	/**
	 * 更新人员部门调动记录
	 * @param progressId	进程号
	 * @param jlId	记录ID
	 * @param ryId	人员ID
	 * @param jlrq	记录日期
	 * @param ddyy	调用原因
	 * @param drbm	调入部门编号
	 * @param bz	备注
	 * @param flag	更新标志
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject updateHsRybmddjl(String progressId,String jlId,String ryId,String jlrq,
			String ddyy,String drbm,String bz,int flag) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<JlId>" + jlId + "</JlId>");
		sb.append("<RyId>" + ryId + "</RyId>");
		sb.append("<Jlrq>" + jlrq + "</Jlrq>");
		sb.append("<Ddyy>" + ddyy + "</Ddyy>");
		sb.append("<Drbm>" + drbm + "</Drbm>");
		sb.append("<Bz>" + bz + "</Bz>");
		sb.append("<Flag>" + String.valueOf(flag) + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsRybmddjl";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	/**
	 * 显示人员职务调动记录
	 * @param progressId
	 * @param beginDate
	 * @param endDate
	 * @param jlzt
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject showHsRyzwddjls(String progressId,String beginDate,String endDate, String jlzt) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("<Jlzt>" + jlzt + "</Jlzt>");
		sb.append("</Data>");
		
		String funcName = "ShowHsRyzwddjls";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	/**
	 * 更新人员职务调动记录
	 * @param progressId	进程号
	 * @param jlId	记录ID
	 * @param ryId	人员ID
	 * @param jlrq	记录日期
	 * @param ddyy	调用原因
	 * @param xzw	新职务编号
	 * @param bz	备注
	 * @param flag	更新标志
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject updateHsRyzwddjl(String progressId,String jlId,String ryId,String jlrq,
			String ddyy,String xzw,String bz,int flag) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<JlId>" + jlId + "</JlId>");
		sb.append("<RyId>" + ryId + "</RyId>");
		sb.append("<Jlrq>" + jlrq + "</Jlrq>");
		sb.append("<Ddyy>" + ddyy + "</Ddyy>");
		sb.append("<Xzw>" + xzw + "</Xzw>");
		sb.append("<Bz>" + bz + "</Bz>");
		sb.append("<Flag>" + String.valueOf(flag) + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsRyzwddjl";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	/**
	 * 显示人员奖惩记录
	 * @param progressId
	 * @param beginDate
	 * @param endDate
	 * @param jlzt
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject showHsRyjcjls(String progressId,String beginDate,String endDate, String jlzt) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("<Jlzt>" + jlzt + "</Jlzt>");
		sb.append("</Data>");
		
		String funcName = "ShowHsRyjcjls";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	/**
	 * 更新人员奖惩记录
	 * @param progressId	进程号
	 * @param jlId	记录ID
	 * @param ryId	人员ID
	 * @param jlrq	记录日期
	 * @param jclx	奖惩类型
	 * @param jcyy	奖惩原因
	 * @param jcje	奖惩金额
	 * @param bz	备注
	 * @param flag	更新标志
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject updateHsRyjcjl(String progressId,String jlId,String ryId,String jlrq,
			String jclx,String jcyy,String jcje,String bz,int flag) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<JlId>" + jlId + "</JlId>");
		sb.append("<RyId>" + ryId + "</RyId>");
		sb.append("<Jlrq>" + jlrq + "</Jlrq>");
		sb.append("<Jclx>" + jclx + "</Jclx>");
		sb.append("<Jcyy>" + jcyy + "</Jcyy>");
		sb.append("<Jcje>" + jcje + "</Jcje>");
		sb.append("<Bz>" + bz + "</Bz>");
		sb.append("<Flag>" + String.valueOf(flag) + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsRyjcjl";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	/**
	 * 显示人员休假记录
	 * @param progressId
	 * @param beginDate
	 * @param endDate
	 * @param jlzt
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject showHsRyxjjls(String progressId,String beginDate,String endDate, String jlzt) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("<Jlzt>" + jlzt + "</Jlzt>");
		sb.append("</Data>");
		
		String funcName = "ShowHsRyxjjls";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	/**
	 * 更新人员休假记录
	 * @param progressId	进程号
	 * @param jlId	记录ID
	 * @param ryId	人员ID
	 * @param jlrq	记录日期
	 * @param xjlx	休假类型
	 * @param ksrq	开始日期
	 * @param ts	休假天数
	 * @param bz	备注
	 * @param flag	更新标志
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject updateHsRyxjjl(String progressId,String jlId,String ryId,String jlrq,
			String xjlx,String ksrq,String ts,String bz,int flag) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<JlId>" + jlId + "</JlId>");
		sb.append("<RyId>" + ryId + "</RyId>");
		sb.append("<Jlrq>" + jlrq + "</Jlrq>");
		sb.append("<Xjlx>" + xjlx + "</Xjlx>");
		sb.append("<Ksrq>" + ksrq + "</Ksrq>");
		sb.append("<Ts>" + ts + "</Ts>");
		sb.append("<Bz>" + bz + "</Bz>");
		sb.append("<Flag>" + String.valueOf(flag) + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsRyxjjl";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject showHsRyztbhjls(String progressId,String beginDate,String endDate, String jlzt) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("<Jlzt>" + jlzt + "</Jlzt>");
		sb.append("</Data>");
		
		String funcName = "ShowHsRyztbhjls";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	/**
	 * 更新人员状态编号记录
	 * @param progressId	进程号
	 * @param jlId	记录ID
	 * @param ryId	人员ID
	 * @param jlrq	记录日期
	 * @param bhyy	变化原因
	 * @param xzt	新状态
	 * @param bz	备注
	 * @param flag	更新标志
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject updateHsRyztbhjl(String progressId,String jlId,String ryId,String jlrq,
			String bhyy,String xzt,String bz,int flag) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<JlId>" + jlId + "</JlId>");
		sb.append("<RyId>" + ryId + "</RyId>");
		sb.append("<Jlrq>" + jlrq + "</Jlrq>");
		sb.append("<Bhyy>" + bhyy + "</Bhyy>");
		sb.append("<Xzt>" + xzt + "</Xzt>");
		sb.append("<Bz>" + bz + "</Bz>");
		sb.append("<Flag>" + String.valueOf(flag) + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsRyztbhjl";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	
	public HsWSReturnObject showHsRysyjls(String progressId,String ryId, String beginDate,String endDate, String jlzt) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<RyId>" + ryId + "</RyId>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("<Jlzt>" + jlzt + "</Jlzt>");
		sb.append("</Data>");
		
		String funcName = "ShowHsRysyjls";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	/**
	 * 更新生涯记录
	 * @param progressId	进程号
	 * @param jlId	记录ID
	 * @param ryId	人员ID
	 * @param jlrq	记录日期
	 * @param jlzy	记录摘要
	 * @param flag	更新标志
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject updateHsRysyjl(String progressId,String jlId,String ryId,String jlrq,
			String jlzy,int flag) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<JlId>" + jlId + "</JlId>");
		sb.append("<RyId>" + ryId + "</RyId>");
		sb.append("<Jlrq>" + jlrq + "</Jlrq>");
		sb.append("<Jlzy>" + jlzy + "</Jlzy>");
		sb.append("<Flag>" + String.valueOf(flag) + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsRysyjl";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	//}}
	
	//{{ 车辆管理
	
	public HsWSReturnObject showHsCldas(String progressId,String clzt) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<Clzt>" + clzt + "</Clzt>");
		sb.append("</Data>");
		
		String funcName = "ShowHsCldas";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	/**
	 * 更新车辆档案
	 * @param progressId	进程号
	 * @param clId	车辆ID
	 * @param clcq	车辆产权
	 * @param cphm	车牌号码
	 * @param cllx	车辆类型
	 * @param syr	所有人
	 * @param zz	住址
	 * @param syxz	使用性质
	 * @param ppxh	品牌型号
	 * @param sbdh	识别代号
	 * @param fdjh	发动机号
	 * @param zcrq	注册日期
	 * @param fzrq	发证日期
	 * @param bmbh	部门编号
	 * @param clzt	车辆状态
	 * @param flag	更新标志
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject updateHsClda(String progressId,String clId,String clcq,String cphm,
			String cllx,String syr,String zz,String syxz,String ppxh,String sbdh,String fdjh,
			String zcrq,String fzrq,String bmbh,String clzt,int flag) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<ClId>" + clId + "</ClId>");
		sb.append("<Clcq>" + clcq + "</Clcq>");
		sb.append("<Cphm>" + cphm + "</Cphm>");
		sb.append("<Cllx>" + cllx + "</Cllx>");
		sb.append("<Syr>" + syr + "</Syr>");
		sb.append("<Zz>" + zz + "</Zz>");
		sb.append("<Syxz>" + syxz + "</Syxz>");
		sb.append("<Ppxh>" + ppxh + "</Ppxh>");
		sb.append("<Sbdh>" + sbdh + "</Sbdh>");
		sb.append("<Fdjh>" + fdjh + "</Fdjh>");
		sb.append("<Zcrq>" + zcrq + "</Zcrq>");
		sb.append("<Fzrq>" + fzrq + "</Fzrq>");
		sb.append("<Bmbh>" + bmbh + "</Bmbh>");
		sb.append("<Clzt>" + clzt + "</Clzt>");
		sb.append("<Flag>" + String.valueOf(flag) + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsClda";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	/**
	 * 显示车辆部门调动记录
	 * @param progressId
	 * @param beginDate
	 * @param endDate
	 * @param jlzt
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject showHsClbmddjls(String progressId,String beginDate,String endDate, String jlzt) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("<Jlzt>" + jlzt + "</Jlzt>");
		sb.append("</Data>");
		
		String funcName = "ShowHsClbmddjls";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	/**
	 * 更新车辆部门调动记录
	 * @param progressId	进程号
	 * @param jlId	记录ID
	 * @param clId	人员ID
	 * @param jlrq	记录日期
	 * @param ddyy	调用原因
	 * @param drbm	调入部门编号
	 * @param bz	备注
	 * @param flag	更新标志
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject updateHsClbmddjl(String progressId,String jlId,String clId,String jlrq,
			String ddyy,String drbm,String bz,int flag) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<JlId>" + jlId + "</JlId>");
		sb.append("<ClId>" + clId + "</ClId>");
		sb.append("<Jlrq>" + jlrq + "</Jlrq>");
		sb.append("<Ddyy>" + ddyy + "</Ddyy>");
		sb.append("<Drbm>" + drbm + "</Drbm>");
		sb.append("<Bz>" + bz + "</Bz>");
		sb.append("<Flag>" + String.valueOf(flag) + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsClbmddjl";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	/**
	 * 显示车辆维保记录
	 * @param progressId
	 * @param beginDate
	 * @param endDate
	 * @param jlzt
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject showHsClwbjls(String progressId,String beginDate,String endDate, String jlzt) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("<Jlzt>" + jlzt + "</Jlzt>");
		sb.append("</Data>");
		
		String funcName = "ShowHsClwbjls";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	/**
	 * 更新车辆维保记录
	 * @param progressId	进程号
	 * @param jlId	记录ID
	 * @param clId	车辆ID
	 * @param jlrq	记录日期
	 * @param wblx	维保类型
	 * @param wbnr	维保内容
	 * @param wbdd	维保地点
	 * @param bxje	保险金额
	 * @param zfje	自费金额
	 * @param bz	备注
	 * @param flag	更新标志
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject updateHsClwbjl(String progressId,String jlId,String clId,String jlrq,
			String wblx,String wbnr,String wbdd,String bxje,String zfje,String bz,int flag) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<JlId>" + jlId + "</JlId>");
		sb.append("<ClId>" + clId + "</ClId>");
		sb.append("<Jlrq>" + jlrq + "</Jlrq>");
		sb.append("<Wblx>" + wblx + "</Wblx>");
		sb.append("<Wbnr>" + wbnr + "</Wbnr>");
		sb.append("<Wbdd>" + wbdd + "</Wbdd>");
		sb.append("<Bxje>" + bxje + "</Bxje>");
		sb.append("<Zfje>" + zfje + "</Zfje>");
		sb.append("<Bz>" + bz + "</Bz>");
		sb.append("<Flag>" + String.valueOf(flag) + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsClwbjl";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	/**
	 * 显示车辆保险记录
	 * @param progressId
	 * @param beginDate
	 * @param endDate
	 * @param jlzt
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject showHsClbxjls(String progressId,String beginDate,String endDate, String jlzt) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("<Jlzt>" + jlzt + "</Jlzt>");
		sb.append("</Data>");
		
		String funcName = "ShowHsClbxjls";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	/**
	 * 更新车辆保险记录
	 * @param progressId	进程号
	 * @param jlId	记录ID
	 * @param clId	车辆ID
	 * @param jlrq	记录日期
	 * @param sxrq	生效日期
	 * @param bxgs	保险公司
	 * @param bdhm	保单号码
	 * @param ccse	车船税额
	 * @param jqxe	交强险额
	 * @param syxe	商业险额
	 * @param syxz	商业险种
	 * @param bz	备注
	 * @param flag	更新标志
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject updateHsClbxjl(String progressId,String jlId,String clId,String jlrq,
			String sxrq,String bxgs,String bdhm,String ccse,String jqxe,String syxe,String syxz,String bz,int flag) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<JlId>" + jlId + "</JlId>");
		sb.append("<ClId>" + clId + "</ClId>");
		sb.append("<Jlrq>" + jlrq + "</Jlrq>");
		sb.append("<Sxrq>" + sxrq + "</Sxrq>");
		sb.append("<Bxgs>" + bxgs + "</Bxgs>");
		sb.append("<Bdhm>" + bdhm + "</Bdhm>");
		sb.append("<Ccse>" + ccse + "</Ccse>");
		sb.append("<Jqxe>" + jqxe + "</Jqxe>");
		sb.append("<Syxe>" + syxe + "</Syxe>");
		sb.append("<Syxz>" + syxz + "</Syxz>");
		sb.append("<Bz>" + bz + "</Bz>");
		sb.append("<Flag>" + String.valueOf(flag) + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsClbxjl";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	/**
	 * 显示车辆行驶记录
	 * @param progressId
	 * @param beginDate
	 * @param endDate
	 * @param jlzt
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject showHsClxsjls(String progressId,String beginDate,String endDate, String jlzt) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("<Jlzt>" + jlzt + "</Jlzt>");
		sb.append("</Data>");
		
		String funcName = "ShowHsClxsjls";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	/**
	 * 更新车辆行驶记录
	 * @param progressId	进程号
	 * @param jlId	记录ID
	 * @param clId	车辆ID
	 * @param jlrq	记录日期
	 * @param ksrq	开始日期
	 * @param jsrq	结束日期
	 * @param jsy	驾驶人
	 * @param lcsj	里程数据
	 * @param fkxx	反馈信息
	 * @param bz	备注
	 * @param flag	更新标志
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject updateHsClxsjl(String progressId,String jlId,String clId,String jlrq,
			String ksrq,String jsrq,String jsy,String lcsj,String fkxx,String bz,int flag) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<JlId>" + jlId + "</JlId>");
		sb.append("<ClId>" + clId + "</ClId>");
		sb.append("<Jlrq>" + jlrq + "</Jlrq>");
		sb.append("<Xjlx>" + ksrq + "</Xjlx>");
		sb.append("<Ksrq>" + jsrq + "</Ksrq>");
		sb.append("<Jsy>" + jsy + "</Jsy>");
		sb.append("<Lcsj>" + lcsj + "</Lcsj>");
		sb.append("<Fkxx>" + fkxx + "</Fkxx>");
		sb.append("<Bz>" + bz + "</Bz>");
		sb.append("<Flag>" + String.valueOf(flag) + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsClxsjl";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	/**
	 * 显示车辆状态变化记录
	 * @param progressId
	 * @param beginDate
	 * @param endDate
	 * @param jlzt
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject showHsClztbhjls(String progressId,String beginDate,String endDate, String jlzt) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("<Jlzt>" + jlzt + "</Jlzt>");
		sb.append("</Data>");
		
		String funcName = "ShowHsClztbhjls";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	/**
	 * 显示车辆状态变化记录
	 * @param progressId	进程号
	 * @param jlId	记录ID
	 * @param clId	车辆ID
	 * @param jlrq	记录日期
	 * @param bhyy	变化原因
	 * @param xzt	新状态
	 * @param bz	备注
	 * @param flag	更新标志
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject updateHsClztbhjl(String progressId,String jlId,String clId,String jlrq,
			String bhyy,String xzt,String bz,int flag) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<JlId>" + jlId + "</JlId>");
		sb.append("<ClId>" + clId + "</ClId>");
		sb.append("<Jlrq>" + jlrq + "</Jlrq>");
		sb.append("<Bhyy>" + bhyy + "</Bhyy>");
		sb.append("<Xzt>" + xzt + "</Xzt>");
		sb.append("<Bz>" + bz + "</Bz>");
		sb.append("<Flag>" + String.valueOf(flag) + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsClztbhjl";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	/**
	 * 显示车辆生涯记录
	 * @param progressId
	 * @param beginDate
	 * @param endDate
	 * @param jlzt
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject showHsClsyjls(String progressId,String clId,String beginDate,String endDate, String jlzt) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<ClId>" + clId + "</ClId>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("<Jlzt>" + jlzt + "</Jlzt>");
		sb.append("</Data>");
		
		String funcName = "ShowHsClsyjls";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	/**
	 * 更新车辆生涯记录
	 * @param progressId	进程号
	 * @param jlId	记录ID
	 * @param clId	车辆ID
	 * @param jlrq	记录日期
	 * @param jlzy	记录摘要
	 * @param flag	更新标志
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject updateHsClsyjl(String progressId,String jlId,String clId,String jlrq,
			String jlzy,int flag) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<JlId>" + jlId + "</JlId>");
		sb.append("<ClId>" + clId + "</ClId>");
		sb.append("<Jlrq>" + jlrq + "</Jlrq>");
		sb.append("<Jlzy>" + jlzy + "</Jlzy>");
		sb.append("<Flag>" + String.valueOf(flag) + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsClsyjl";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	//}}
	
	//{{ 流程处理

	/**
	 * 更新自由流程起始步骤
	 * @param progressId
	 * @param bzId
	 * @param bzlx
	 * @param tyfs
	 * @param bhcl
	 * @param roleUsers
	 * @param flag
	 * @param rolebh
	 * @param djlx
	 * @param djId
	 * @param sfjs
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject updateHsLcbz(String progressId,String bzId,String bzlx,String tyfs,String bhcl,String tybm,String sfxg,String deptRoleUserNames,int flag,
			String djlx,String djId,String sfjs) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<BzId>" + bzId + "</BzId>");
		sb.append("<Bzlx>" + bzlx + "</Bzlx>");
		sb.append("<Tyfs>" + tyfs + "</Tyfs>");
		sb.append("<Bhcl>" + bhcl + "</Bhcl>");
		sb.append("<Tybm>" + tybm + "</Tybm>");
		sb.append("<Sfxg>" + sfxg + "</Sfxg>");
		sb.append("<DeptRoleUserName>" + deptRoleUserNames + "</DeptRoleUserName>");
		sb.append("<Flag>" + String.valueOf(flag) + "</Flag>");
		sb.append("<Djlx>" + djlx + "</Djlx>");
		sb.append("<DjId>" + djId + "</DjId>");
		sb.append("<Sfjs>" + sfjs + "</Sfjs>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsLcbz";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	/**
	 * 更新自由流程中间步骤
	 * @param progressId	进程ID
	 * @param bzId	步骤ID
	 * @param bzlx	步骤类型
	 * @param tyfs	审批方式
	 * @param bhcl	驳回处理
	 * @param deptRoleUserNames	审批角色人员
	 * @param flag	更新标记
	 * @param jlId	审批记录ID
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject updateHsLcbz(String progressId,String bzId,String bzlx,String tyfs,String bhcl,String tybm,String sfxg,String deptRoleUserNames,int flag,
			String jlId,String sfjs) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<BzId>" + bzId + "</BzId>");
		sb.append("<Bzlx>" + bzlx + "</Bzlx>");
		sb.append("<Tyfs>" + tyfs + "</Tyfs>");
		sb.append("<Bhcl>" + bhcl + "</Bhcl>");
		sb.append("<Tybm>" + tybm + "</Tybm>");
		sb.append("<Sfxg>" + sfxg + "</Sfxg>");
		sb.append("<DeptRoleUserName>" + deptRoleUserNames + "</DeptRoleUserName>");
		sb.append("<Flag>" + String.valueOf(flag) + "</Flag>");
		sb.append("<JlId>" + jlId + "</JlId>");
		sb.append("<Sfjs>" + sfjs + "</Sfjs>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsLcbz";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	/**
	 * 更新流程审批记录
	 * @param progressId	进程ID
	 * @param jlId	记录ID
	 * @param spyj	审批意见
	 * @param jlzt	记录状态
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject updateHsLcspjl(String progressId,String jlId,String spyj,String jlzt) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<JlId>" + jlId + "</JlId>");
		sb.append("<Spyj>" + spyj + "</Spyj>");
		sb.append("<Jlzt>" + jlzt + "</Jlzt>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsLcspjl";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	/**
	 * 显示流程步骤分支
	 * @param progressId	进程ID
	 * @param jlId	记录ID
	 * @return
	 * @throws Exception 
	 */
	public HsWSReturnObject showHsLcbzfzs(String progressId,String jlId) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<JlId>" + jlId + "</JlId>");
		sb.append("</Data>");
		
		String funcName = "ShowHsLcbzfzs";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	/**
	 * 显示待办事项
	 * @param progressId 进程ID
	 * @param rolebh 角色编号
	 * @param beginDate 开始日期
	 * @param endDate 结束日期
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject showDbsxs(String progressId,String beginDate,String endDate,String jlzt) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("<Jlzt>" + jlzt + "</Jlzt>");
		sb.append("</Data>");
		
		String funcName = "ShowDbsxs";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	/**
	 * 显示审批记录
	 * @param progressId 进程ID
	 * @param djlx 单据类型
	 * @param djId 单据ID
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject showHsLcspjls(String progressId,String djlx,String djId) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<Djlx>" + djlx + "</Djlx>");
		sb.append("<DjId>" + djId + "</DjId>");
		sb.append("</Data>");
		
		String funcName = "ShowHsLcspjls";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	public HsWSReturnObject getHsLcmbs(String progressId,String djlx) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<Djlx>" + djlx + "</Djlx>");
		sb.append("</Data>");
		
		String funcName = "GetHsLcmbs";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	/**
	 * 启动规则流程
	 * @param progressId	进程号
	 * @param rolebh	角色编号
	 * @param djlx	单据类型
	 * @param djId	单据ID
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject startRegularHsLc(String progressId,String mbId,String djId) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<MbId>" + mbId + "</MbId>");
		sb.append("<DjId>" + djId + "</DjId>");
		sb.append("</Data>");
		
		String funcName = "StartRegularHsLc";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	/**
	 * 终止流程
	 * @param progressId	进程号
	 * @param djlx	单据类型
	 * @param djId	单据ID
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject overHsLc(String progressId,String djlx,String djId) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<Djlx>" + djlx + "</Djlx>");
		sb.append("<DjId>" + djId + "</DjId>");
		sb.append("</Data>");
		
		String funcName = "OverHsLc";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	//}}

	//{{ 自定义流程单据模板
	
	public HsWSReturnObject getHsUserLcdjmbs(String progressId) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("</Data>");

		String funcName = "GetHsUserLcdjmbs";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject getHsUserLcdjmb(String progressId,String mbId) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<MbId>" + mbId + "</MbId>");
		sb.append("</Data>");

		String funcName = "GetHsUserLcdjmb";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	//}}

	//{{ 自定义流程
	
	public HsWSReturnObject showHsUserLcdjs(String progressId,String beginDate,String endDate,String spzt) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("<Spzt>" + spzt + "</Spzt>");
		sb.append("</Data>");
		
		String funcName = "ShowHsUserLcdjs";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject getHsUserLcdj(String progressId,String djId) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<DjId>" + djId + "</DjId>");
		sb.append("</Data>");

		String funcName = "GetHsUserLcdj";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data", HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject updateHsUserLcdj(String progressId,String djId,int mbId,String djrq,String djzy,String xData,int flag) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<DjId>" + djId + "</DjId>");
		sb.append("<MbId>" + mbId + "</MbId>");
		sb.append("<Djrq>" + djrq + "</Djrq>");
		sb.append("<Djzy>" + djzy + "</Djzy>");
		sb.append("<Data>" + xData + "</Data>");
		sb.append("<Flag>" + flag + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsUserLcdj";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	//}}
	
	//{{ 费用审批单
	
	public HsWSReturnObject showHsfyspds(String progressId,String beginDate,String endDate,String spzt) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("<Spzt>" + spzt + "</Spzt>");
		sb.append("</Data>");
		
		String funcName = "ShowHsFyspds";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject getHsFyspd(String progressId,String djId) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<DjId>" + djId + "</DjId>");
		sb.append("</Data>");

		String funcName = "GetHsFyspd";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data", HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	/**
	 * 更新费用审批单
	 * @param progressId
	 * @param djId
	 * @param djrq
	 * @param message
	 * @param je
	 * @param bz
	 * @param flag
	 * @return
	 * @throws Exception
	 */
	public HsWSReturnObject updateHsFyspd(String progressId,String djId,String djrq,String message,String je,String bz,int flag) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<DjId>" + djId + "</DjId>");
		sb.append("<Djrq>" + djrq + "</Djrq>");
		sb.append("<Message>" + message + "</Message>");
		sb.append("<Je>" + je + "</Je>");
		sb.append("<Bz>" + bz + "</Bz>");
		sb.append("<Flag>" + String.valueOf(flag) + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsFyspd";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}

	//}}

	//{{ 通用流程单据
	
	public HsWSReturnObject showHsTylcdjs(String progressId,String djlx, String beginDate,String endDate,String spzt) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<Djlx>" + djlx + "</Djlx>");
		sb.append("<BeginDate>" + beginDate + "</BeginDate>");
		sb.append("<EndDate>" + endDate + "</EndDate>");
		sb.append("<Spzt>" + spzt + "</Spzt>");
		sb.append("</Data>");

		String funcName = "ShowHsTylcdjs";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data", HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject getHsTylcdj(String progressId,String djId) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<DjId>" + djId + "</DjId>");
		sb.append("</Data>");

		String funcName = "GetHsTylcdj";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data", HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	public HsWSReturnObject updateHsTylcdj(String progressId,String djId,String djlx,String djrq,String djbt,String djnr,String bz,int flag) throws Exception
	{

		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<DjId>" + djId + "</DjId>");
		sb.append("<Djlx>" + djlx + "</Djlx>");
		sb.append("<Djrq>" + djrq + "</Djrq>");
		sb.append("<Djbt>" + djbt + "</Djbt>");
		sb.append("<Djnr>" + djnr + "</Djnr>");
		sb.append("<Bz>" + bz + "</Bz>");
		sb.append("<Flag>" + String.valueOf(flag) + "</Flag>");
		sb.append("</Data>");
		
		String funcName = "UpdateHsTylcdj";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data",HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	//}}

	//{{ 单据传送门
	
	public HsWSReturnObject getHsDJCsm(String progressId,String djId) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<Data>");
		sb.append("<ProgressId>" + progressId + "</ProgressId>");
		sb.append("<DjId>" + djId + "</DjId>");
		sb.append("</Data>");

		String funcName = "GetHsDJCsm";
		SoapObject request = new SoapObject(this.namespace, funcName);
		request.addProperty("data", HsGZip.CompressString(sb.toString()));

		return this.formatReturnData(funcName, request);
	}
	
	//}}
}
