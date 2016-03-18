package com.weiyitech.zhaopinzh.business;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.exception.ZhaopinzhException;
import com.weiyitech.zhaopinzh.presentation.activity.TrafficActivity;
import com.weiyitech.zhaopinzh.struct.TrafficRsp;
import com.weiyitech.zhaopinzh.util.Common;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-10-10
 * Time: 下午2:21
 * To change this template use File | Settings | File Templates.
 */
public class QueryTraffic extends CommonBusiness {
    void sendTrafficlDataToPresentation(final Context destContext, JSONObject jsonObject) throws ZhaopinzhException {
        List<TrafficRsp> trafficRsps = new ArrayList<TrafficRsp>();
        try {
            JSONArray list = jsonObject.getJSONArray("list");
            Gson gson = new Gson();
            trafficRsps = gson.fromJson(list.toString(), new TypeToken<List<TrafficRsp>>() {
            }.getType());
            ((TrafficActivity) destContext).getDataFromBusiness(Common.TRAFFIC_DETAIL_TYPE, trafficRsps);
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendTrafficlDataToPresentation JSONException", e);
        }
    }

    public void queryTraffic(Object params, final Context context) {
        if(!checkNetWork(context)){
            ((TrafficActivity)context).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/jobFair/trafficDetail.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            final ZhaopinzhApp zhaopinzhApp = (ZhaopinzhApp) context.getApplicationContext();
            zhaopinzhApp.trafficRspTimeout = true;

            java.util.Timer timer;
            timer = new Timer(true);
            timer.schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            if (zhaopinzhApp.trafficRspTimeout == true) {
                                timeoutBusiness(context);
                            }
                        }
                    },  Common.TIMEOUTSECONDS);
            client.get(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        zhaopinzhApp.trafficRspTimeout = false;
                        sendTrafficlDataToPresentation(context, getValuesFromResult(jsonObject));
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
}
