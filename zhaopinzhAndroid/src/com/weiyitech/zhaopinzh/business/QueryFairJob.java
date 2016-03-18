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
import com.weiyitech.zhaopinzh.presentation.activity.JobDetailActivity;
import com.weiyitech.zhaopinzh.presentation.component.FairJobListFragment;
import com.weiyitech.zhaopinzh.struct.*;
import com.weiyitech.zhaopinzh.util.Common;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-2-23
 * Time: 上午9:59
 * To change this template use File | Settings | File Templates.
 */
public class QueryFairJob extends CommonBusiness {
    public static Integer JOBCONCISEREQ = 0;
    public static Integer JOBDETAILREQ = 1;
    public static Integer EMPLOYERDETAILREQ = 2;

    void sendTotalDataToPresentaition(final Fragment fragment, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            if (jsonObject != null) {
                Gson gson = new Gson();
                Pager pager;
                JSONObject jsonObject1 = jsonObject.getJSONObject("pager");
                pager = gson.fromJson(jsonObject1.toString(), Pager.class);
                ((FairJobListFragment)fragment).getDataFromBusiness(Common.JOBTOTAL_TYPE, pager);
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
    void sendRunEmployerDataToPresentation(final Fragment fragment, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            if (jsonObject != null) {
                Gson gson = new Gson();
                RunEmployerPager runEmployerPager = new RunEmployerPager();
                JSONObject jsonObject1 = jsonObject.getJSONObject("pager");
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                runEmployerPager.pager = gson.fromJson(jsonObject1.toString(), Pager.class);
                runEmployerPager.list = gson.fromJson(jsonArray.toString(), new TypeToken<List<RunEmployer>>() {
                }.getType());
                ((FairJobListFragment) fragment).getDataFromBusiness(Common.RUNEMPLOYER_TYPE, runEmployerPager);
            } else {
                throw new ZhaopinzhException("Function sendNetJobsDataToPresentation jsonObject对象为空");
            }
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendNetJobsDataToPresentation JSONException", e);
        }
    }

    /**
     * @param fragment
     * @param jsonObject
     * @param position
     * @throws ZhaopinzhException
     */
    void sendJobConciseDataToPresentation(Fragment fragment, JSONObject jsonObject, int position) throws ZhaopinzhException {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            if (jsonArray != null) {
                Gson gson = new Gson();
                List<JobConciseRsp> jobConciseRsps = gson.fromJson(jsonArray.toString(), new TypeToken<List<JobConciseRsp>>() {
                }.getType());
                ((FairJobListFragment) fragment).getDataFromBusiness(Common.JOBCONSICE_TYPE, jobConciseRsps, position);
            } else {
                throw new ZhaopinzhException("Function sendJobConciseDataToPresentation jsonObject对象为空");
            }
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendJobConciseDataToPresentation JSONException", e);
        }
    }

    /**
     *
     * @param jsonObject
     * @throws ZhaopinzhException
     */
    void sendDetailDataToPresentation(Context context, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            JSONObject detailObject = jsonObject.getJSONObject("detail");
            if (jsonObject != null) {
                Gson gson = new Gson();
                JobDetailRsp jobDetailRsp = gson.fromJson(detailObject.toString(), JobDetailRsp.class);
                ((JobDetailActivity) context).getDataFromBusiness(Common.JOBDETAIL_TYPE, jobDetailRsp);
            } else {
                throw new ZhaopinzhException("Function sendDetailDataToPresentation jsonObject对象为空");
            }
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendDetailDataToPresentation JSONException", e);
        }
    }

    /**
     * @param fragment
     * @param jsonObject
     * @param position
     * @throws ZhaopinzhException
     */
    void sendEmployerDetailDataToPresentation(Fragment fragment, JSONObject jsonObject, int position) throws ZhaopinzhException {
        try {
            JSONObject detailObject = jsonObject.getJSONObject("detail");
            if (jsonObject != null) {
                Gson gson = new Gson();
                EmployerDetailRsp employerDetailRsp = gson.fromJson(detailObject.toString(), EmployerDetailRsp.class);
                ((FairJobListFragment) fragment).getDataFromBusiness(Common.EMPLOYERDETAIL_TYPE, employerDetailRsp, position);
            } else {
                throw new ZhaopinzhException("Function sendEmployerDetailDataToPresentation jsonObject对象为空");
            }
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendEmployerDetailDataToPresentation JSONException", e);
        }
    }

    public void queryTotal(Object params, final Fragment fragment){
        if(!checkNetWork(fragment.getActivity())){
            ((FairJobListFragment) fragment).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/job/pager.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            final ZhaopinzhApp zhaopinzhApp = (ZhaopinzhApp) fragment.getActivity().getApplicationContext();
            zhaopinzhApp.getTotalTimeout = true;

            java.util.Timer timer;
            timer = new Timer(true);
            timer.schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            Integer whichReq = JOBCONCISEREQ;
                            if (zhaopinzhApp.runEmployerRspTimeout == true) {
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
    public void queryRunEmployer(Object params, final Fragment fragment) {
        if(!checkNetWork(fragment.getActivity())){
            ((FairJobListFragment) fragment).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/job/runEmployer.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            final ZhaopinzhApp zhaopinzhApp = (ZhaopinzhApp) fragment.getActivity().getApplicationContext();
            zhaopinzhApp.runEmployerRspTimeout = true;

            java.util.Timer timer;
            timer = new Timer(true);
            timer.schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            Integer whichReq = JOBCONCISEREQ;
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
                        sendRunEmployerDataToPresentation(fragment, getValuesFromResult(jsonObject));
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

    public void queryJobConcise(Object params, final Fragment fragment, final int postion) {
        if(!checkNetWork(fragment.getActivity())){
            ((FairJobListFragment) fragment).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/job/jobList.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            final ZhaopinzhApp zhaopinzhApp = (ZhaopinzhApp) fragment.getActivity().getApplicationContext();
            zhaopinzhApp.jobConciseRspTimeout = true;

            java.util.Timer timer;
            timer = new Timer(true);
            timer.schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            if (zhaopinzhApp.jobConciseRspTimeout == true) {
                                timeoutBusiness(fragment.getActivity());
                            }
                        }
                    }, Common.TIMEOUTSECONDS);
            client.post(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        zhaopinzhApp.jobConciseRspTimeout = false;
                        sendJobConciseDataToPresentation(fragment, getValuesFromResult(jsonObject), postion);
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
