package com.hungsum.testprint;

import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.trendit.basesdk.device.printer.OnPrintListener;
import com.trendit.basesdk.device.printer.PrinterDeviceDriver;
import com.trendit.basesdk.device.printer.format.PrintAlign;
import com.trendit.basesdk.device.printer.format.PrintFontSize;
import com.trendit.basesdk.device.printer.format.TextFormat;
import com.trendit.basesdk.service.BaseSDKServiceManager;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setClickable(true);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                try
                {
                    BaseSDKServiceManager manager = BaseSDKServiceManager.getInstance();

                    PrinterDeviceDriver printerDeviceDriver =  manager.getPrinterDeviceDriver();

                    TextFormat returnFormat = new TextFormat();
                    returnFormat.setFontSize(PrintFontSize.FORMAT_FONT_SIZE_EXTRA_SMALL);

                    TextFormat textFormat = new TextFormat();

                    printerDeviceDriver.printDottedLines(textFormat,2);

                    textFormat.setFontSize(PrintFontSize.FORMAT_FONT_SIZE_LARGE);
                    textFormat.setAlign(PrintAlign.FORMAT_ALIGN_CENTER);
                    textFormat.setBold(true);

                    printerDeviceDriver.printText(textFormat,"佳宝公司订奶收据\n");

                    printerDeviceDriver.printText(returnFormat,"\n");

                    textFormat.setFontSize(PrintFontSize.FORMAT_FONT_SIZE_MEDIUM);
                    textFormat.setAlign(PrintAlign.FORMAT_ALIGN_LEFT);
                    textFormat.setBold(false);

                    printerDeviceDriver.printText(textFormat,"订单编号：000000001\n");

                    printerDeviceDriver.printText(returnFormat,"\n");

                    printerDeviceDriver.printText(textFormat,"订单日期：20170720\n");

                    printerDeviceDriver.printText(returnFormat,"\n");

                    printerDeviceDriver.printText(textFormat,"奶站：010101 八一奶站\n");

                    printerDeviceDriver.printText(returnFormat,"\n");

                    printerDeviceDriver.printText(textFormat,"送奶员：0101010001 解彬\n");

                    printerDeviceDriver.printText(returnFormat,"\n");

                    printerDeviceDriver.printText(textFormat,"客户姓名：01010100010001 李先生\n");

                    printerDeviceDriver.printText(returnFormat,"\n");

                    printerDeviceDriver.printText(textFormat,"联系信息：山东省济南市历下区XXX街道XXX小区 15689632569\n");

                    printerDeviceDriver.printText(returnFormat,"\n");

                    printerDeviceDriver.printText(textFormat,"订奶期间：2017/08/01-2017/08/31\n");

                    printerDeviceDriver.printText(returnFormat,"\n");

                    printerDeviceDriver.printMultiText(textFormat,"产品（单价）","数量");

                    printerDeviceDriver.printMultiText(textFormat,"0001 高钙牛奶（2.2）","2");

                    printerDeviceDriver.printMultiText(textFormat,"0002 儿童牛奶（2.5）","1");

                    printerDeviceDriver.printText(returnFormat,"\n");

                    printerDeviceDriver.printText(textFormat,"总金额：213.9\n");

                    printerDeviceDriver.printText(returnFormat,"\n");

                    printerDeviceDriver.printText(textFormat,"打印时间：2017-06-20 18:32:23\n");

                    printerDeviceDriver.printText(returnFormat,"\n");

                    textFormat.setFontSize(PrintFontSize.FORMAT_FONT_SIZE_LARGE);
                    textFormat.setBold(true);
                    printerDeviceDriver.printText(textFormat,"送奶员签字：\n\n\n\n\n");

                    printerDeviceDriver.printDottedLines(textFormat,2);

                    printerDeviceDriver.printText(textFormat,"\n\n\n\n\n");

                    printerDeviceDriver.cutPaper();

                    printerDeviceDriver.startPrint(new OnPrintListener.Stub () {
                        @Override
                        public void onPrintResult(int i) throws RemoteException
                        {
                            Toast.makeText(view.getContext(),"打印成功",Toast.LENGTH_LONG);

                        }
                    });

                }catch (Exception e)
                {
                    Toast.makeText(view.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void printReturn(TextFormat format)
    {

    }
}
