package com.weiyitech.zhaopinzh.business;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;
import com.weiyitech.zhaopinzh.exception.ZhaopinzhException;
import com.weiyitech.zhaopinzh.util.Common;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-10-10
 * Time: 下午2:36
 */
public abstract class CommonBusiness {
    public static final String URL = "http://118.144.94.123:8080";
    // "http://192.168.1.100:8080";

    // "http://validate.jsontest.com/";
    //"http://validate.jsontest.com/?json=%7B%22key%22:%22value%22";
    //"http://validate.jsontest.com/?json=[JSON-code-to-validate]";
    public JSONObject getValuesFromResult(JSONObject jsonObject) throws ZhaopinzhException {
        try {
            if ((jsonObject.getString("result")).equals("success")) {
               /*返回成功，将values给具体的解析类*/
                return jsonObject.getJSONObject("values");
            } else {
                /*失败，将errrors打印出来，返回null*/
                return null;
            }
        } catch (JSONException e) {
                /*对解析异常进行处理*/
            throw new ZhaopinzhException("Function getValuesFromResult JSONException", e);
        }
    }

    public boolean checkNetWork(final Context context) {
        if (context == null){
            return false;
        }
        if (Common.isNetworkConnected(context)) {
            return true;
        } else {

            Handler handler = new Handler(context.getMainLooper()) {
                @Override
                public void handleMessage(android.os.Message msg) {
                    super.handleMessage(msg);
                    Toast.makeText(context, "您的网络没有打开！请打开您的网络或者连接上wifi再使用", 1000).show();
                };
            };

            handler.sendEmptyMessage(0);
            return false;
        }
    }

    public void timeoutBusiness(final Context context) {
        if (context != null) {
            Handler handler = new Handler(context.getMainLooper()) {
                @Override
                public void handleMessage(android.os.Message msg) {
                    super.handleMessage(msg);
                    //Toast.makeText(context, "对不起，网络超时！", 1000).show();
                }
            };

            handler.sendEmptyMessage(0);
        }

    }

}
