package com.hungsum.jbboss.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.hungsum.framework.componments.HsWSReturnObject;
import com.hungsum.framework.interfaces.IHsLabelValue;
import com.hungsum.framework.models.ModelHsLabelValues;
import com.hungsum.framework.ui.activities.HsActivity_DJ;
import com.hungsum.framework.ui.activities.HsActivity_List_DJ;
import com.hungsum.framework.utils.HsGZip;
import com.hungsum.framework.utils.HsRound;
import com.hungsum.framework.utils.HsString;
import com.hungsum.framework.webservices.HsWebService;
import com.hungsum.jbboss.webservices.JbbossWebService;
import com.trendit.basesdk.device.led.LEDDeviceDriver;
import com.trendit.basesdk.device.led.LedConstants;
import com.trendit.basesdk.device.printer.OnPrintListener;
import com.trendit.basesdk.device.printer.PrinterDeviceDriver;
import com.trendit.basesdk.device.printer.format.PrintAlign;
import com.trendit.basesdk.device.printer.format.PrintFontSize;
import com.trendit.basesdk.device.printer.format.TextFormat;
import com.trendit.basesdk.service.BaseSDKServiceManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Serializable;
import java.util.ArrayList;

public class Activity_List_JbSnydndj extends HsActivity_List_DJ {
    private Handler uiHandler = new Handler();

    public Activity_List_JbSnydndj() {
        this.mIsShowRetrieveCondition = true;

        this.setConditionSwitchDatas("0,未提交;1,提交");

        this.setConditionSwitchDefaultValues("0,1");
    }

    @Override
    protected void initLayout() {
        super.initLayout();


        ucSummary.setTextSize(20);
        ucSummary.setPadding(20, 10, 20, 10);
    }

    /*
         * 创建上下文菜单
         */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        IHsLabelValue item = (IHsLabelValue) this.ucListView.getAdapter().getItem(((AdapterView.AdapterContextMenuInfo) menuInfo).position);

        menu.addSubMenu(0, com.hungsum.framework.R.string.str_new, 0, "新增").setIcon(com.hungsum.framework.R.drawable.content_new);

        if (item.getValue("Djzt", "0").toString().equals("0")) //未提交
        {
            menu.addSubMenu(0, com.hungsum.framework.R.string.str_modify, 1, "修改");
            menu.addSubMenu(0, com.hungsum.framework.R.string.str_delete, 2, "删除");
            menu.addSubMenu(0, com.hungsum.framework.R.string.str_submit, 2, "提交");
        } else //提交
        {
            menu.addSubMenu(0, com.hungsum.framework.R.string.str_modify, 1, "查看");
            menu.addSubMenu(0, com.hungsum.framework.R.string.str_print, 2, "打印");
            menu.addSubMenu(0, com.hungsum.framework.R.string.str_unsubmit, 3, "取消提交");
        }
    }

    @Override
    protected void addItem() throws Exception {
        Intent intent = new Intent(this, Activity_DJ_JbSnydndj.class);

        startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);
    }

    @Override
    protected void modifyItem(IHsLabelValue item) throws Exception {
        Intent intent = new Intent(this, Activity_DJ_JbSnydndj.class);
        intent.putExtra("Title", this.getTitle());
        intent.putExtra("Data", item);

        if (item.getValue("Djzt", "0").equals("1")) {
            intent.putExtra("AuditOnly", true);
        }

        startActivityForResult(intent, HsActivity_DJ.REQUESTCODE_ITEM);
    }

    @Override
    protected HsWSReturnObject deleteItem(IHsLabelValue item) throws Exception {
        return ((HsWebService) application.getWebService()).doDatas(
                application.getLoginData().getProgressId(),
                getBhs(item, "DjId"),
                "Delete_JbSnydndj");
    }

    @Override
    protected String getActionPromptMessage(int actionId) {
        if (actionId == com.hungsum.framework.R.string.str_submit) {
            return "提交之后不能修改、删除，提交之后才能打印。";
        } else {
            return super.getActionPromptMessage(actionId);
        }
    }

    @Override
    protected HsWSReturnObject submitItem(IHsLabelValue item) throws Exception
    {
        mCurrentSelectedItem = item;

        return ((HsWebService) application.getWebService()).doDatas(
                application.getLoginData().getProgressId(),
                getBhs(item, "DjId"),
                "Submit_JbSnydndj");
    }

    @Override
    protected HsWSReturnObject unSubmitItem(IHsLabelValue item) throws Exception {
        return ((HsWebService) application.getWebService()).doDatas(
                application.getLoginData().getProgressId(),
                getBhs(item, "DjId"),
                "UnSubmit_JbSnydndj");
    }

    private void printDJ(IHsLabelValue item, String datetime) throws Exception {
        ShowInformation("开始打印");

        BaseSDKServiceManager manager = BaseSDKServiceManager.getInstance();

        PrinterDeviceDriver printerDeviceDriver = manager.getPrinterDeviceDriver();

        if (printerDeviceDriver == null) {
            throw new Exception("无法找到打印机，请检查设备并联系软件提供商。");
        }

        final LEDDeviceDriver ledDeviceDriver = manager.getLEDDeviceDriver();

        if (ledDeviceDriver == null) {
            throw new Exception("无法找到LED设备，请检查设备并联系软件提供商。");
        }

        final int[] colors = new int[]{LedConstants.COLOR_BLUE, LedConstants.COLOR_RED, LedConstants.COLOR_GREEN, LedConstants.COLOR_YELLOW};

        ledDeviceDriver.turnOn(colors);

        //换行格式
        TextFormat returnFormat = new TextFormat();
        returnFormat.setFontSize(PrintFontSize.FORMAT_FONT_SIZE_EXTRA_SMALL);

        //字体格式
        TextFormat textFormat = new TextFormat();

        //开头打印两行虚线
        printerDeviceDriver.printDottedLines(textFormat, 2);

        textFormat.setFontSize(PrintFontSize.FORMAT_FONT_SIZE_LARGE);
        textFormat.setAlign(PrintAlign.FORMAT_ALIGN_CENTER);
        textFormat.setBold(true);

        printerDeviceDriver.printText(textFormat, "佳宝公司订奶收据\n");

        printerDeviceDriver.printText(returnFormat, "\n");

        textFormat.setFontSize(PrintFontSize.FORMAT_FONT_SIZE_MEDIUM);
        textFormat.setAlign(PrintAlign.FORMAT_ALIGN_LEFT);
        textFormat.setBold(false);

        printerDeviceDriver.printText(textFormat, "订单编号：".concat(HsString.padLeft(item.getValue("DjId", "0").toString(), 9, '0')).concat("\n"));

        printerDeviceDriver.printText(returnFormat, "\n");

        printerDeviceDriver.printText(textFormat, "订单日期：".concat(item.getValue("Djrq", "").toString()).concat("\n"));

        printerDeviceDriver.printText(returnFormat, "\n");

        printerDeviceDriver.printText(textFormat, "奶站：".concat(item.getValue("Nzmc", "").toString()).concat(" ").concat(item.getValue("Fzr", "").toString()).concat("\n"));

        printerDeviceDriver.printText(returnFormat, "\n");

        printerDeviceDriver.printText(textFormat, "送奶员：".concat(item.getValue("Rymc", "").toString()).concat(" ").concat(item.getValue("Rydh", "").toString()).concat("\n"));

        printerDeviceDriver.printText(returnFormat, "\n");

        printerDeviceDriver.printText(textFormat, "客户姓名：".concat(item.getValue("Khbh", "").toString()).concat(" ").concat(item.getValue("Khmc", "").toString()).concat("\n"));

        printerDeviceDriver.printText(returnFormat, "\n");

        printerDeviceDriver.printText(textFormat, "联系信息：".concat(item.getValue("Dqmc", "").toString()).concat(item.getValue("Dz", "").toString()).concat(" ").concat(item.getValue("Gddh", "").toString()).concat(" ").concat(item.getValue("Yddh", "").toString().concat("\n")));

        printerDeviceDriver.printText(returnFormat, "\n");

        printerDeviceDriver.printText(textFormat, "订奶期间：自".concat(item.getValue("Ksrq", "").toString()).concat("至").concat(item.getValue("Jsrq", "").toString()).concat("共").concat(item.getValue("Dnts", "0").toString()).concat("天\n"));

        printerDeviceDriver.printText(returnFormat, "\n");

        printerDeviceDriver.printMultiText(textFormat, "产品 单价", "数量");


        JSONTokener tokener = new JSONTokener(item.getValue("StrMx", "[]").toString());

        JSONArray jsonArray = (JSONArray) tokener.nextValue();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            printerDeviceDriver.printMultiText(textFormat, jsonObject.getString("Cpbh").concat(" ").concat(jsonObject.getString("Cpmc")), jsonObject.getString("Sl"));
        }

        printerDeviceDriver.printText(returnFormat, "\n");

        printerDeviceDriver.printText(textFormat, "总金额：".concat(item.getValue("Zje", "").toString().concat("\n")));

        printerDeviceDriver.printText(returnFormat, "\n");

        printerDeviceDriver.printText(textFormat, "打印时间：".concat(datetime).concat("\n"));

        printerDeviceDriver.printText(returnFormat, "\n");

        textFormat.setFontSize(PrintFontSize.FORMAT_FONT_SIZE_LARGE);
        textFormat.setBold(true);
        printerDeviceDriver.printText(textFormat, "送奶员签字：\n\n\n\n\n");

        printerDeviceDriver.printDottedLines(textFormat, 2);

        printerDeviceDriver.printText(textFormat, "\n\n\n\n\n");

        printerDeviceDriver.startPrint(new OnPrintListener.Stub() {
            @Override
            public void onPrintResult(final int i) throws RemoteException {
                //uiHandler.sendEmptyMessage(i);

                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Activity_List_JbSnydndj.this, "打印成功", Toast.LENGTH_SHORT).show();
                    }
                });

                ledDeviceDriver.turnOff(colors);
            }
        });
    }


    @Override
    protected void actionAfterMenuOrButtonClick(int actionId, IHsLabelValue object) throws Exception {
        if (actionId == com.hungsum.framework.R.string.str_print) {
            this.mCurrentSelectedItem = object;

            //获取服务器时间
            try {
                ShowWait("请稍候", "正在获取信息...");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HsWSReturnObject returnObject = application.getWebService().getCurrentDateTime();

                            sendDataMessage(returnObject);
                        } catch (Exception e) {
                            sendErrorMessage(e.getMessage());
                        }

                    }
                }).start();
            } catch (Exception e) {
                ShowError(e);
            }

        } else {
            super.actionAfterMenuOrButtonClick(actionId, object);
        }
    }

    @Override
    protected HsWSReturnObject retrieve() throws Exception {
        return ((JbbossWebService) application.getWebService()).showJbSnydndjs(
                application.getLoginData().getProgressId(),
                getBeginDateValue(),
                getEndDateValue(),
                getSwitchValues());
    }

    @Override
    public void actionAfterWSReturnData(String funcname, Serializable data)
            throws Exception {
        if (funcname.equals("ShowJbSnydndjs")) {
            ArrayList<IHsLabelValue> items = new ModelHsLabelValues().Create(HsGZip.DecompressString(data.toString()));
            this.ucListView.setAdapter(getAdapter(items));

            double sum = 0;

            for (IHsLabelValue item : items) {
                sum += Double.parseDouble(item.getValue("Zje", "0").toString());
            }

            this.setSummaryText("合计金额：".concat(String.valueOf(HsRound.Round(sum,2))));

        } else if (funcname.equals("DoDatas_Submit_JbSnydndj")) {
            ShowInformation(data.toString());

            if (mCurrentSelectedItem != null) {

                showAlert("提示", "是否打印？", "打印", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            actionAfterMenuOrButtonClick(com.hungsum.framework.R.string.str_print, mCurrentSelectedItem);
                        } catch (Exception e) {
                            ShowInformation(e.getMessage());
                        }
                    }
                }, "取消", null);

            }
            callRetrieveOnOtherThread(false);

        } else if (funcname.equals("DoDatas_Delete_JbSnydndj") || funcname.equals("DoDatas_UnSubmit_JbSnydndj")) {
            ShowInformation(data.toString());
            callRetrieveOnOtherThread(false);
        } else if (funcname.equals("GetCurrentDateTime")) {
            if (mCurrentSelectedItem != null) {
                printDJ(mCurrentSelectedItem, data.toString());
            } else {
                throw new Error("未选择单据。");
            }
        } else {
            super.actionAfterWSReturnData(funcname, data);
        }
    }

}
