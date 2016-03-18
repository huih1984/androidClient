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
import com.weiyitech.zhaopinzh.presentation.activity.NetJobConciseListActivity;
import com.weiyitech.zhaopinzh.presentation.activity.NetJobDetailActivity;
import com.weiyitech.zhaopinzh.presentation.component.NetJobListFragment;
import com.weiyitech.zhaopinzh.struct.JobConciseNetRsp;
import com.weiyitech.zhaopinzh.struct.JobDetailNetRsp;
import com.weiyitech.zhaopinzh.struct.NetJobPager;
import com.weiyitech.zhaopinzh.struct.Pager;
import com.weiyitech.zhaopinzh.util.Common;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-8-1
 * Time: 下午7:21
 * To change this template use File | Settings | File Templates.
 */
public class QueryNetJob extends CommonBusiness{
    void sendTotalDataToPresentaition(final Fragment fragment, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            if (jsonObject != null) {
                Gson gson = new Gson();
                Pager pager;
                JSONObject jsonObject1 = jsonObject.getJSONObject("pager");
                pager = gson.fromJson(jsonObject1.toString(), Pager.class);
                ((NetJobListFragment)fragment).getDataFromBusiness(Common.NETJOBTOTAL_TYPE, pager);
            } else {
                throw new ZhaopinzhException("Function sendTotalNumberDataToPresentaition jsonObject对象为空");
            }
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendTotalNumberDataToPresentaition JSONException", e);
        }
    }

    /**
     * @param fragment
     * @param jsonObject
     * @throws ZhaopinzhException
     */
    void sendNetJobsDataToPresentation(final Fragment fragment, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            if (jsonObject != null) {
                Gson gson = new Gson();
                NetJobPager netJobPager = new NetJobPager();
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                netJobPager.pager = gson.fromJson(jsonObject.getJSONObject("pager").toString(), Pager.class);
                netJobPager.list = gson.fromJson(jsonArray.toString(), new TypeToken<List<JobConciseNetRsp>>() {
                }.getType());
                ((NetJobListFragment) fragment).getDataFromBusiness(Common.NETJOBCONCISE_TYPE, netJobPager);
            } else {
                throw new ZhaopinzhException("Function sendNetJobsDataToPresentation jsonObject对象为空");
            }
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendNetJobsDataToPresentation JSONException", e);
        }
    }

    /**
     *
     * @param jsonObject
     * @throws ZhaopinzhException
     */
    void sendNetJobDetailDataToPresentation(Context context, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            JSONObject detailObject = jsonObject.getJSONObject("detail");
            if (jsonObject != null) {
                Gson gson = new Gson();
                JobDetailNetRsp jobDetailNetRsp = gson.fromJson(detailObject.toString(), JobDetailNetRsp.class);
                ((NetJobDetailActivity) context).getDataFromBusiness(Common.NETJOBDETAIL_TYPE, jobDetailNetRsp);
            } else {
                throw new ZhaopinzhException("Function sendDetailDataToPresentation jsonObject对象为空");
            }
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendDetailDataToPresentation JSONException", e);
        }
    }

    public void queryNetJobTotal(Object params, final Fragment fragment){
        if(!checkNetWork(fragment.getActivity())){
            ((NetJobListFragment)fragment).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/job/netPager.do";
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

    /**
     * 页面查询函数
     *
     * @param params      请求参数
     * @param fragment 发送到的页面
     */
    public void queryNetJobConcises(Object params, final Fragment fragment) {
        if(!checkNetWork(fragment.getActivity())){
            ((NetJobListFragment)fragment).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/job/netJobs.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            final ZhaopinzhApp zhaopinzhApp = (ZhaopinzhApp) fragment.getActivity().getApplicationContext();
            zhaopinzhApp.runEmployerRspTimeout = true;

            java.util.Timer timer;
            timer = new Timer(true);
            timer.schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            if (zhaopinzhApp.runEmployerRspTimeout == true) {
                                timeoutBusiness(fragment.getActivity());
                            }
                        }
                    }, Common.TIMEOUTSECONDS);

            client.get(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        zhaopinzhApp.runEmployerRspTimeout = false;
                        sendNetJobsDataToPresentation(fragment, getValuesFromResult(jsonObject));
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

    public void queryJobDetail(Object params, final Context context) {
        if(!checkNetWork(context)){
            ((NetJobDetailActivity)context).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/job/netJobDetail.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            final ZhaopinzhApp zhaopinzhApp = (ZhaopinzhApp) context.getApplicationContext();
            zhaopinzhApp.jobConciseRspTimeout = true;

            java.util.Timer timer;
            timer = new Timer(true);
            timer.schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            if (zhaopinzhApp.runEmployerRspTimeout == true) {
                                timeoutBusiness(context);
                            }
                        }
                    }, Common.TIMEOUTSECONDS);

            client.post(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        zhaopinzhApp.jobConciseRspTimeout = false;
                        sendNetJobDetailDataToPresentation(context, getValuesFromResult(jsonObject));
                    } catch (Exception e) {
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
            e.printStackTrace();
        }
    }
}
