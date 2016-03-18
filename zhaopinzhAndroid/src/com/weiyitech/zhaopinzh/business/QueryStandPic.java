package com.weiyitech.zhaopinzh.business;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.*;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.exception.ZhaopinzhException;
import com.weiyitech.zhaopinzh.presentation.activity.StandPicActivity;
import com.weiyitech.zhaopinzh.struct.StandPicRsp;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.filemanager.CommonUtil;
import com.weiyitech.zhaopinzh.util.filemanager.FileHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-10-10
 * Time: 下午2:13
 * To change this template use File | Settings | File Templates.
 */
public class QueryStandPic extends CommonBusiness {
    /**
     *
     * @param destContext
     * @param jsonObject
     * @throws ZhaopinzhException
     */
    void sendStandPicDataToPresentation(final Context destContext, JSONObject jsonObject) throws ZhaopinzhException {
        List<StandPicRsp> standPicRspList = new ArrayList<StandPicRsp>();
        try {
            JSONArray list = jsonObject.getJSONArray("list");
            Gson gson = new Gson();
            standPicRspList = gson.fromJson(list.toString(), new TypeToken<List<StandPicRsp>>() {
            }.getType());
            ((StandPicActivity) destContext).getDataFromBusiness(Common.STANDPIC_DETAIL_TYPE, standPicRspList);
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendStandPicDataToPresentation JSONException", e);
        }
    }

    public void queryStandPic(Object params, final Context context) {
        if(!checkNetWork(context)){
            ((StandPicActivity)context).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/jobFair/standPicDetail.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            final ZhaopinzhApp zhaopinzhApp = (ZhaopinzhApp) context.getApplicationContext();
            zhaopinzhApp.standRspTimeout = true;

            java.util.Timer timer;
            timer = new Timer(true);
            timer.schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            if (zhaopinzhApp.standRspTimeout == true) {
                                timeoutBusiness(context);
                            }
                        }
                    },  Common.TIMEOUTSECONDS);

            client.get(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        zhaopinzhApp.standRspTimeout = false;
                        sendStandPicDataToPresentation(context, getValuesFromResult(jsonObject));
                    } catch (ZhaopinzhException e) {
                        //对最终的异常要进行处理
                    }
                    super.onSuccess(jsonObject);
                }

                @Override
                public void onFailure(Throwable throwable, JSONObject jsonObject) {
                    super.onFailure(throwable, jsonObject);
                }
            });
        } catch (Exception e) {

        }
    }

    public void getStandPic(final String url,  final Context destContext) {
        AsyncHttpClient client = new AsyncHttpClient();
        String[] allowedContentTypes = new String[]{"image/png", "image/jpeg"};
        client.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {
            @Override
            public void onSuccess(byte[] fileData) {
                try {
                    String filePath = CommonUtil.getRootFilePath();
                    FileHelper.createDirectory(filePath + "com.zhaopinzh/standPic");
                    String[] subStr = url.split("/");
                    if (filePath != null) {
                        filePath += "com.zhaopinzh/standPic/" + subStr[subStr.length - 1];
                    }
                    File f = new File(filePath);
                    f.createNewFile();

                    OutputStream os = new FileOutputStream(f);
                    os.write(fileData, 0, fileData.length);
                    os.close();
                    ((StandPicActivity) destContext).getDataFromBusiness(Common.STANDPIC_GOT_PIC_TYPE, null);
                } catch (Exception e) {

                }
            }
        });
    }
}
