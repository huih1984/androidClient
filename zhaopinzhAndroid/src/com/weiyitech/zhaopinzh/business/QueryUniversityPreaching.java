package com.weiyitech.zhaopinzh.business;

import android.support.v4.app.Fragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.exception.ZhaopinzhException;
import com.weiyitech.zhaopinzh.presentation.component.UniversityPreachingMeetListFragment;
import com.weiyitech.zhaopinzh.struct.Pager;
import com.weiyitech.zhaopinzh.struct.PreachmeetingConciseRsp;
import com.weiyitech.zhaopinzh.struct.PreachmeetingConciseRspPager;
import com.weiyitech.zhaopinzh.util.Common;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-5-27
 * Time: 下午3:45
 * To change this template use File | Settings | File Templates.
 */
public class QueryUniversityPreaching extends CommonBusiness {

    void sendPreachingListDataToPresentation(final Fragment fragment, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            Gson gson = new Gson();
            PreachmeetingConciseRspPager preachmeetingConciseRspPager = new PreachmeetingConciseRspPager();
            JSONArray list = jsonObject.getJSONArray("list");
            JSONObject jsonObject1 = jsonObject.getJSONObject("pager");
            preachmeetingConciseRspPager.pager = gson.fromJson(jsonObject1.toString(), Pager.class);
            preachmeetingConciseRspPager.list = gson.fromJson(list.toString(), new TypeToken<List<PreachmeetingConciseRsp>>(){}.getType());
            ((UniversityPreachingMeetListFragment) fragment).getDataFromBusiness(Common.PREACH_MEETING_LIST_TYPE, preachmeetingConciseRspPager);
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendPreachingListDataToPresentation JSONException", e);
        }
    }

    void sendPreachingTotalDataToPresentation(final Fragment fragment, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            Gson gson = new Gson();
            Pager pager = gson.fromJson(jsonObject.getJSONObject("pager").toString(), Pager.class);
            ((UniversityPreachingMeetListFragment) fragment).getDataFromBusiness(Common.PREACH_MEETING_TOTAL_TYPE, pager);
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendPreachingTotalDataToPresentation JSONException", e);
        }
    }

    public void queryPreachingList(Object params, final Fragment fragment) {
        if(!checkNetWork(fragment.getActivity())){
            ((UniversityPreachingMeetListFragment) fragment).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/preach/preachMeetingList.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            final ZhaopinzhApp zhaopinzhApp = (ZhaopinzhApp) fragment.getActivity().getApplicationContext();
            zhaopinzhApp.campusTimeout = true;

            java.util.Timer timer;
            timer = new Timer(true);
            timer.schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            if (zhaopinzhApp.campusTimeout == true) {
                                timeoutBusiness(fragment.getActivity());
                            }
                        }
                    }, Common.TIMEOUTSECONDS);
            client.get(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        zhaopinzhApp.campusTimeout = false;
                        sendPreachingListDataToPresentation(fragment, getValuesFromResult(jsonObject));
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

    public void queryPreachingTotal(Object params, final Fragment fragment) {
        if(!checkNetWork(fragment.getActivity())){
            ((UniversityPreachingMeetListFragment) fragment).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/preach/preachPager.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            final ZhaopinzhApp zhaopinzhApp = (ZhaopinzhApp) fragment.getActivity().getApplicationContext();
            zhaopinzhApp.campusTimeout = true;

            java.util.Timer timer;
            timer = new Timer(true);
            timer.schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            if (zhaopinzhApp.campusTimeout == true) {
                                timeoutBusiness(fragment.getActivity());
                            }
                        }
                    }, Common.TIMEOUTSECONDS);
            client.get(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        zhaopinzhApp.campusTimeout = false;
                        sendPreachingTotalDataToPresentation(fragment, getValuesFromResult(jsonObject));
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