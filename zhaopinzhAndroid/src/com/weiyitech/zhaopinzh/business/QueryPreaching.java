package com.weiyitech.zhaopinzh.business;

import android.app.Activity;
import android.support.v4.app.Fragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.exception.ZhaopinzhException;
import com.weiyitech.zhaopinzh.presentation.activity.PreachMeetingInstructionActivity;
import com.weiyitech.zhaopinzh.presentation.component.PreachingMeetListFragment;
import com.weiyitech.zhaopinzh.presentation.component.PreachingUniversityListFragment;
import com.weiyitech.zhaopinzh.struct.*;
import com.weiyitech.zhaopinzh.util.Common;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-5-22
 * Time: 下午5:25
 * To change this template use File | Settings | File Templates.
 */
public class QueryPreaching extends CommonBusiness {
    /**
     *
     * @param fragment
     * @param jsonObject
     * @throws com.weiyitech.zhaopinzh.exception.ZhaopinzhException
     */
    void sendPreachingUniversityListDataToPresentation(final Fragment fragment, JSONObject jsonObject) throws ZhaopinzhException {
        ArrayList<PreachingUniversityRsp> jobFairConciseList = new ArrayList<PreachingUniversityRsp>();
        try {
            JSONArray list = jsonObject.getJSONArray("list");
            Gson gson = new Gson();
            jobFairConciseList = gson.fromJson(list.toString(), new TypeToken<List<PreachingUniversityRsp>>() {
            }.getType());
            ((PreachingUniversityListFragment) fragment).getDataFromBusiness(Common.PREACH_MEETING_UNIVERSITY_TYPE, jobFairConciseList);
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendFairListDataToPresentation JSONException", e);
        }
    }

    void sendPreachingListDataToPresentation(final Fragment fragment, JSONObject jsonObject) throws ZhaopinzhException {
        ArrayList<PreachmeetingConciseRsp> preachmeetingConciseRsps = new ArrayList<PreachmeetingConciseRsp>();
        try {
            Gson gson = new Gson();
            PreachmeetingConciseRspPager preachmeetingConciseRspPager = new PreachmeetingConciseRspPager();
            JSONArray list = jsonObject.getJSONArray("list");
            JSONObject jsonObject1 = jsonObject.getJSONObject("pager");
            preachmeetingConciseRspPager.pager = gson.fromJson(jsonObject1.toString(), Pager.class);
            preachmeetingConciseRspPager.list = gson.fromJson(list.toString(), new TypeToken<List<PreachmeetingConciseRsp>>(){}.getType());
            ((PreachingMeetListFragment) fragment).getDataFromBusiness(Common.PREACH_MEETING_LIST_TYPE, preachmeetingConciseRspPager);
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendPreachingListDataToPresentation JSONException", e);
        }
    }

    void sendPreachingIdsDataToPresentation(final Fragment fragment, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            Gson gson = new Gson();
            JSONArray list = jsonObject.getJSONArray("list");
            List<PreachmeetingConciseRsp> preachmeetingConciseRsps = gson.fromJson(list.toString(), new TypeToken<List<PreachmeetingConciseRsp>>(){}.getType());
            ((PreachingUniversityListFragment) fragment).getDataFromBusiness(Common.PREACH_MEETING_IDS_TYPE, preachmeetingConciseRsps);
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendPreachingIdsDataToPresentation JSONException", e);
        }
    }

    void sendPreachingTotalDataToPresentation(final Fragment fragment, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            Gson gson = new Gson();
            Pager pager = gson.fromJson(jsonObject.getJSONObject("pager").toString(), Pager.class);
            ((PreachingMeetListFragment) fragment).getDataFromBusiness(Common.PREACH_MEETING_TOTAL_TYPE, pager);
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendPreachingTotalDataToPresentation JSONException", e);
        }
    }

    void sendPreachingDataToPresentation(final Activity activity, JSONObject jsonObject) throws ZhaopinzhException {

        try {
            PreachmeetingRsp preachmeetingRsp = new PreachmeetingRsp();
            JSONObject jsonObject1 = jsonObject.getJSONObject("preachMeeting");
            Gson gson = new Gson();
            preachmeetingRsp = gson.fromJson(jsonObject1.toString(), PreachmeetingRsp.class);
            ((PreachMeetingInstructionActivity)activity).getDataFromBusiness(Common.PREACH_MEETING_TYPE, preachmeetingRsp);
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendPreachingDataToPresentation JSONException", e);
        }
    }

    public void queryPreachInstruction(Object params, final Activity activity) {
        if(!checkNetWork(activity)){
            ((PreachMeetingInstructionActivity) activity).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/preach/preachMeeting.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            final ZhaopinzhApp zhaopinzhApp = (ZhaopinzhApp) activity.getApplicationContext();
            zhaopinzhApp.campusTimeout = true;

            java.util.Timer timer;
            timer = new Timer(true);
            timer.schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            if (zhaopinzhApp.campusTimeout == true) {
                                timeoutBusiness(activity);
                            }
                        }
                    }, Common.TIMEOUTSECONDS);
            client.get(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        zhaopinzhApp.campusTimeout = false;
                        sendPreachingDataToPresentation(activity, getValuesFromResult(jsonObject));
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

    public void queryPreachUniversityList(Object params, final Fragment fragment) {
        if(!checkNetWork(fragment.getActivity())){
            ((PreachingUniversityListFragment) fragment).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/preach/preachUniversityList.do";
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
                        sendPreachingUniversityListDataToPresentation(fragment, getValuesFromResult(jsonObject));
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

    public void queryPreachingList(Object params, final Fragment fragment) {
        if(!checkNetWork(fragment.getActivity())){
            ((PreachingMeetListFragment) fragment).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
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

    public void queryPreachingIds(Object params, final Fragment fragment) {
        if(!checkNetWork(fragment.getActivity())){
            ((PreachingUniversityListFragment) fragment).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/preach/preachMeetingIds.do";
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
                        sendPreachingIdsDataToPresentation(fragment, getValuesFromResult(jsonObject));
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
            ((PreachingMeetListFragment) fragment).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
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