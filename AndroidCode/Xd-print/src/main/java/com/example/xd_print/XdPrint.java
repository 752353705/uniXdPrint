package com.example.xd_print;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;
import com.caysn.autoreplyprint.AutoReplyPrint;
import com.sun.jna.Pointer;

public class XdPrint extends UniModule  {
    public Pointer h = Pointer.NULL;
    private static final String TAG = "TestTask";
    public Boolean isOpen = false;

    /**
     * 测试插件是否正常引入
     * @param callback
     */
    @UniJSMethod(uiThread = true)
    public void testPlugin (UniJSCallback callback) {
        if (callback != null) {
            JSONObject data = new JSONObject();
            data.put("msg", "插件引入成功");
            data.put("code", 200);
            callback.invoke(data);
        }
    }

    /**
     * 打开打印机端口，并获取端口句柄 h
     * 用于接下来的打印方法 调用
     * strCOMPort 端口名称
     * nCOMBaudrate 波特率
     * nCOMFlowControl 指定流控制
     */
    @UniJSMethod(uiThread = true)
    public void openPort (String strCOMPort,int nCOMBaudrate,int nCOMFlowControl,UniJSCallback callback) {
        h = AutoReplyPrint.INSTANCE.CP_Port_OpenCom(strCOMPort, nCOMBaudrate, AutoReplyPrint.CP_ComDataBits_8, AutoReplyPrint.CP_ComParity_NoParity, AutoReplyPrint.CP_ComStopBits_One, nCOMFlowControl, 0);
        Boolean state = false;
        if (h == Pointer.NULL) {
            state = false;
        } else {
            state = true;
        }
        if (callback != null) {
            JSONObject data = new JSONObject();
            data.put("code", 200);
            data.put("state", state);
            callback.invoke(data);
        }
    }

    AutoReplyPrint.CP_OnPrinterPrintedEvent_Callback printed_callback = new AutoReplyPrint.CP_OnPrinterPrintedEvent_Callback() {
        @Override
        public void CP_OnPrinterPrintedEvent(Pointer h, final int printer_printed_page_id, Pointer private_data) {

        }
    };


    /**
     * 自动回传
     * 打开打印机端口，并获取端口句柄 h
     * 用于接下来的打印方法 调用
     * strCOMPort 端口名称
     * nCOMBaudrate 波特率
     * nCOMFlowControl 指定流控制
     */
    @UniJSMethod(uiThread = true)
    public void openPortReply (String strCOMPort,int nCOMBaudrate,int nCOMFlowControl,UniJSCallback callback) {
        h = AutoReplyPrint.INSTANCE.CP_Port_OpenCom(strCOMPort, nCOMBaudrate, AutoReplyPrint.CP_ComDataBits_8, AutoReplyPrint.CP_ComParity_NoParity, AutoReplyPrint.CP_ComStopBits_One, nCOMFlowControl, 1);
        Boolean state = false;
        state = AutoReplyPrint.INSTANCE.CP_Printer_AddOnPrinterPrintedEvent(printed_callback, h);
        if (callback != null) {
            JSONObject data = new JSONObject();
            data.put("msg", "插件引入成功");
            data.put("code", 200);
            data.put("state", state);
            callback.invoke(data);
        }
    }

    /**
     * 关闭打印机端口
     */
    @UniJSMethod(uiThread = true)
    public void closePort () {
        if (h != Pointer.NULL) {
            AutoReplyPrint.INSTANCE.CP_Port_Close(h);
            h = Pointer.NULL;
            isOpen = false;
        }
    }

    /**
     * 判断当前端口是否打开
     * 如果端口已打开，且连接未断开未关闭，则返回 true
     * 如果端口未打开，或连接已断开已关闭，则返回 false
     */
    @UniJSMethod(uiThread = true)
    public void isOpen (UniJSCallback callback) {
        if (AutoReplyPrint.INSTANCE.CP_Port_IsOpened(h)) {
            isOpen = true;
        } else {
            isOpen = false;
        }
        if (callback != null) {
            JSONObject data = new JSONObject();
            data.put("isOpen", isOpen);
            callback.invoke(data);
        }
    }

    /**
     * 判断当前端口是否打开
     * 如果端口已打开，且连接未断开未关闭，则返回 true
     * 如果端口未打开，或连接已断开已关闭，则返回 false
     */
    @UniJSMethod(uiThread = true)
    public void isOpenReply (UniJSCallback callback) {
        Boolean state = false;
        state = AutoReplyPrint.INSTANCE.CP_Port_IsOpened(h);
        if (callback != null) {
            JSONObject data = new JSONObject();
            data.put("state", state);
            callback.invoke(data);
        }
    }

    /**
     * 页面开始走纸，并设置标签纸大小，参考点坐标和旋转角度
     * x 页面起始点 x 坐标
     * y 页面起始点 y 坐标
     * width 页面宽度
     * height 页面高度
     * rotation 页面旋转 取值范围{0,1}；0 页面不旋转打印；1 页面旋转打印
     */
    @UniJSMethod(uiThread = true)
    public void pageBegin (int x, int y, int width, int height, int rotation,UniJSCallback callback) {

        AutoReplyPrint.INSTANCE.CP_Pos_SetMultiByteMode(h); // 设置打印机为多字节编码
        AutoReplyPrint.INSTANCE.CP_Pos_SetMultiByteEncoding(h, AutoReplyPrint.CP_MultiByteEncoding_UTF8);

        AutoReplyPrint.INSTANCE.CP_Label_PageBegin(h, x, y, width, height, rotation);

        if (callback != null) {
            JSONObject data = new JSONObject();
            data.put("msg", "页面开始走纸打印");
            data.put("code", 200);
            callback.invoke(data);
        }
    }

    /**
     * 进行文本的打印；只能单行打印
     *  x 文本起始位置x坐标
     *  y 文本起始位置y 坐标
     *  font 选择字体
     *  style 字符风格
     *  str 打印的字符串
     */
    @UniJSMethod(uiThread = true)
    public void drawText (int x, int y, int font, int style, String str, UniJSCallback callback) {

        AutoReplyPrint.INSTANCE.CP_Label_DrawText(h, x, y, font, style, str);

        if (callback != null) {
            JSONObject data = new JSONObject();
            data.put("msg", "文本打印成功");
            data.put("code", 200);
            callback.invoke(data);
        }
    }

    /**
     * 带有格式的文字打印
     */
    @UniJSMethod(uiThread = true)
    public void drawStyleText (int x, int y, int font, Boolean bold, Boolean underline, Boolean highlight,Boolean strikethrough,int
            rotation, int widthscale, int heightscale, String str, UniJSCallback callback) {

        AutoReplyPrint.INSTANCE.CP_Label_DrawText(h, x, y, font,
                new AutoReplyPrint.CP_Label_TextStyle(bold, underline, highlight, strikethrough,
                        rotation, widthscale, heightscale).getStyle(),
                str);

        if (callback != null) {
            JSONObject data = new JSONObject();
            data.put("msg", "文本打印成功");
            data.put("code", 200);
            callback.invoke(data);
        }
    }

    /**
     * 进行条形码的打印
     * x 条码左上角 x 坐标值
     * y 条码左上角y 坐标值
     * height 定义条码高度。
     * unitwidth 定义码块单元宽度。取值范围：[1, 4]。
     * rotation 表示旋转角度。取值范围：[0, 3]。0 不旋转绘制。1 旋转 90°绘制。2 旋转 180°绘制。3 旋转 270°绘制。
     * str 要打印的条码
     */
    @UniJSMethod(uiThread = true)
    public void drawBarCode (int x, int y, int height, int unitwidth, int rotation, String str, UniJSCallback callback) {

        AutoReplyPrint.INSTANCE.CP_Label_DrawBarcode(h, x, y, AutoReplyPrint.CP_Label_BarcodeType_CODE128, AutoReplyPrint.CP_Label_BarcodeTextPrintPosition_BelowBarcode, height, unitwidth, rotation, str);

        if (callback != null) {
            JSONObject data = new JSONObject();
            data.put("msg", "文本打印成功");
            data.put("code", 200);
            callback.invoke(data);
        }
    }


    /**
     * 进行打印操作
     * state : 返回 true 表示写入成功，返回 false 表示写入失败。
     */
    @UniJSMethod(uiThread = true)
    public void print (int i, UniJSCallback callback) {
        Boolean state = false;
        state = AutoReplyPrint.INSTANCE.CP_Label_PagePrint(h, i); // 将标签页上的内容打印到标签纸上
        AutoReplyPrint.INSTANCE.CP_Pos_FullCutPaper(h); // 切刀全切

        if (callback != null) {
            JSONObject data = new JSONObject();
            data.put("msg", "打印操作完成");
            data.put("state", state);
            callback.invoke(data);
        }
    }

}