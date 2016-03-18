package com.weiyitech.zhaopinzh.business;

import android.content.Context;
import android.support.v4.app.Fragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.exception.ZhaopinzhException;
import com.weiyitech.zhaopinzh.presentation.activity.FairActivity;
import com.weiyitech.zhaopinzh.presentation.activity.TrafficActivity;
import com.weiyitech.zhaopinzh.presentation.component.FairListFragment;
import com.weiyitech.zhaopinzh.struct.*;
import com.weiyitech.zhaopinzh.util.Common;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-2-11
 * Time: 下午8:12
 * To change this template use File | Settings | File Templates.
 */
public class QueryFair  extends CommonBusiness {
    /**
     *
     * @param fragment
     * @param jsonObject
     * @throws com.weiyitech.zhaopinzh.exception.ZhaopinzhException
     */
    void sendFairListDataToPresentation(final Fragment fragment, JSONObject jsonObject) throws ZhaopinzhException {
        try {

            JSONArray list = jsonObject.getJSONArray("list");
            Gson gson = new Gson();
            JobFairPager jobFairPager = new JobFairPager();
            JSONObject jsonObject1 = jsonObject.getJSONObject("pager");
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            jobFairPager.pager = gson.fromJson(jsonObject1.toString(), Pager.class);
            jobFairPager.list = gson.fromJson(jsonArray.toString(), new TypeToken<List<JobFairConcise>>(){
            }.getType());
            ((FairListFragment) fragment).getDataFromBusiness(Common.JOBFAIRCONCISE_TYPE, jobFairPager);
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendFairListDataToPresentation JSONException", e);
        }
    }

    void sendFairDetailDataToPresentation(final Context context, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            JSONObject detailJsonObject = jsonObject.getJSONObject("detail");
            Gson gson = new Gson();
            JobFairDetailRsp jobFairDetailRsp = gson.fromJson(detailJsonObject.toString(), JobFairDetailRsp.class);
            ((FairActivity) context).getDataFromBusiness(Common.JOBFAIRDETAIL_TYPE, jobFairDetailRsp);
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendFairDetailDataToPresentation JSONException", e);
        }
    }

    void sendTotalDataToPresentaition(final Fragment fragment, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            if (jsonObject != null) {
                Gson gson = new Gson();
                Pager pager;
                JSONObject jsonObject1 = jsonObject.getJSONObject("pager");
                pager = gson.fromJson(jsonObject1.toString(), Pager.class);
                ((FairListFragment)fragment).getDataFromBusiness(Common.FAIRTOTAL_TYPE, pager);
            } else {
                throw new ZhaopinzhException("Function sendTotalNumberDataToPresentaition jsonObject对象为空");
            }
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendTotalNumberDataToPresentaition JSONException", e);
        }
    }

    void sendLocationInMapToPresentaition(final Context context, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            if (jsonObject != null) {
                Gson gson = new Gson();
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                List<LocationInMapRsp> locationInMapRsps =  gson.fromJson(jsonArray.toString(), new TypeToken<List<LocationInMapRsp>>(){
                }.getType());
                ((TrafficActivity)context).getDataFromBusiness(Common.LOCATION_IN_MAP_TYPE, locationInMapRsps);
            } else {
                throw new ZhaopinzhException("Function sendLocationInMapToPresentaition jsonObject对象为空");
            }
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendLocationInMapToPresentaition JSONException", e);
        }
    }

    public void queryTotal(Object params, final Fragment fragment){
        if(!checkNetWork(fragment.getActivity())){
            ((FairListFragment) fragment).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/fair/pager.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            final ZhaopinzhApp zhaopinzhApp = (ZhaopinzhApp) fragment.getActivity().getApplicationContext();
            zhaopinzhApp.getTotalTimeout = true;

            java.util.Timer timer;
            timer = new Timer(true);
            timer.schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            if (zhaopinzhApp.getTotalTimeout == true) {
                                timeoutBusiness(fragment.getActivity());
                            }
                        }
                    }, Common.TIMEOUTSECONDS);

            client.get(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        zhaopinzhApp.getTotalTimeout = false;
                        sendTotalDataToPresentaition(fragment, getValuesFromResult(jsonObject));
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

    public void queryFairList(Object params,  final Fragment fragment) {
        if(!checkNetWork(fragment.getActivity())){
            ((FairListFragment) fragment).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/fair/fairList.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            final ZhaopinzhApp zhaopinzhApp = (ZhaopinzhApp) fragment.getActivity().getApplicationContext();
            zhaopinzhApp.fairRspTimeout = true;

            java.util.Timer timer;
            timer = new Timer(true);
            timer.schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            if (zhaopinzhApp.fairRspTimeout == true) {
                                timeoutBusiness(fragment.getActivity());
                            }
                        }
                    }, Common.TIMEOUTSECONDS);
            client.get(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        zhaopinzhApp.fairRspTimeout = false;
                        sendFairListDataToPresentation(fragment, getValuesFromResult(jsonObject));
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

    public void queryFairDetail(Object params,  final Context context) {
        if(!checkNetWork(context)){
            ((FairActivity)context).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/fair/detail.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            final ZhaopinzhApp zhaopinzhApp = (ZhaopinzhApp) context.getApplicationContext();
            zhaopinzhApp.fairDetailRspTimeout = true;

            java.util.Timer timer;
            timer = new Timer(true);
            timer.schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            if (zhaopinzhApp.fairDetailRspTimeout == true) {
                                timeoutBusiness(context);
                            }
                        }
                    }, Common.TIMEOUTSECONDS);

            client.get(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        zhaopinzhApp.fairDetailRspTimeout = false;
                        sendFairDetailDataToPresentation(context, getValuesFromResult(jsonObject));
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

    public void queryLocationInMap(Object params, final Context context){
        if(!checkNetWork(context)){
            ((TrafficActivity)context).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/fair/location.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            final ZhaopinzhApp zhaopinzhApp = (ZhaopinzhApp) context.getApplicationContext();
            zhaopinzhApp.fairDetailRspTimeout = true;

            java.util.Timer timer;
            timer = new Timer(true);
            timer.schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            if (zhaopinzhApp.fairDetailRspTimeout == true) {
                                timeoutBusiness(context);
                            }
                        }
                    }, Common.TIMEOUTSECONDS);

            client.get(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        zhaopinzhApp.fairDetailRspTimeout = false;
                        sendLocationInMapToPresentaition(context, getValuesFromResult(jsonObject));
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
