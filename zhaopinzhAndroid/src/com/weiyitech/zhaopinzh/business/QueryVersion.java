package com.weiyitech.zhaopinzh.business;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.loopj.android.http.*;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.exception.ZhaopinzhException;
import com.weiyitech.zhaopinzh.presentation.activity.SplashActivity;
import com.weiyitech.zhaopinzh.struct.VersionRsp;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.filemanager.CommonUtil;
import com.weiyitech.zhaopinzh.util.filemanager.FileHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-11-29
 * Time: 下午5:22
 * To change this template use File | Settings | File Templates.
 */
public class QueryVersion extends CommonBusiness {

    public void queryVersion(Object params, final Context context) {
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", (new Gson()).toJson(params));
        String url = Common.URL + "/home/update.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            final ZhaopinzhApp zhaopinzhApp = (ZhaopinzhApp) context.getApplicationContext();
            zhaopinzhApp.versionReqTimeout = true;
            java.util.Timer timer;
            timer = new Timer(true);
            timer.schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            if (zhaopinzhApp.versionReqTimeout == true) {
                                timeoutBusiness(context);
                                ((SplashActivity) context).getDataFromBusiness(Common.TIMEOUT_TYPE, null);
                            }
                        }
                    },  Common.QUERYVERSIONTIMEOUTSECONDS);

            client.get(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        zhaopinzhApp.versionReqTimeout = false;
                        JSONObject jsonObject1 = getValuesFromResult(jsonObject);
                        if (jsonObject1 != null) {
                            Gson gson = new Gson();
                            VersionRsp versionRsp = gson.fromJson(jsonObject1.getJSONObject("detail").toString(), VersionRsp.class);
                            if ((jsonObject1.getBoolean("updateStatus"))) {
                               ((SplashActivity) context).getDataFromBusiness(Common.VERSION_QUERY_TYPE, versionRsp);
                            } else {
                                ((SplashActivity) context).getDataFromBusiness(Common.VERSION_QUERY_NO_UPDATE_TYPE, versionRsp);
                            }
                        } else{
                            //如果是超前的版本，走没有更新的路径
                            Gson gson = new Gson();
                            JSONArray jsonArray = jsonObject.getJSONArray("errors");
                            JSONObject jsonObject11 = jsonArray.getJSONObject(0);
                            VersionRsp versionRsp = gson.fromJson(jsonObject11.toString(), VersionRsp.class);
                            ((SplashActivity) context).getDataFromBusiness(Common.VERSION_QUERY_NO_UPDATE_TYPE, versionRsp);
                        }
                    } catch (ZhaopinzhException e) {
                        //对最终的异常要进行处理
                    } catch (Exception e) {

                    }
                    super.onSuccess(jsonObject);
                }

                @Override
                public void onFailure(Throwable throwable, JSONObject jsonObject) {
                    super.onFailure(throwable, jsonObject);
                }
            });
        } catch (Exception e) {
            Log.i("exception", "out");
        }
    }

    public void downLoadApk(final String apkPath, final Context destContext) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(destContext);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("正在下载更新");
        pd.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    String url = Common.URL + "/" + apkPath;
                    URL apkUrl = new URL(url);
                    conn = (HttpURLConnection) apkUrl
                            .openConnection();
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(30000);
                    conn.setInstanceFollowRedirects(true);
                    InputStream is = conn.getInputStream();

                    String[] subStr = apkPath.split("/");
                    String filePath = CommonUtil.getRootFilePath();
                    if (filePath != null) {
                        filePath += "download/" + subStr[subStr.length - 1];
                    }
                    File f = new File(filePath);
                    String directoryPath = filePath.substring(0, filePath.lastIndexOf("/"));
                    FileHelper.createDirectory(directoryPath) ;
                    f.createNewFile();

                    OutputStream os = new FileOutputStream(f);
                    final int buffer_size = 1024;
                    byte[] bytes = new byte[buffer_size];
                    for (; ; ) {
                        int count = is.read(bytes, 0, buffer_size);
                        if (count == -1)
                            break;
                        os.write(bytes, 0, count);
                    }
                    os.close();
                    ((SplashActivity) destContext).getDataFromBusiness(Common.APK_TYPE, f);

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                } ;
            }
        });
        thread.start();
    }
}
