package com.weiyitech.zhaopinzh.business;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.weiyitech.zhaopinzh.exception.ZhaopinzhException;
import com.weiyitech.zhaopinzh.presentation.activity.AdvertisementActivity;
import com.weiyitech.zhaopinzh.presentation.activity.NetJobDetailActivity;
import com.weiyitech.zhaopinzh.presentation.activity.ZhaopinzhJobDetailActivity;
import com.weiyitech.zhaopinzh.presentation.component.ZhaopinzhJobListFragment;
import com.weiyitech.zhaopinzh.struct.*;
import com.weiyitech.zhaopinzh.util.Common;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-8-1
 * Time: 下午7:21
 * To change this template use File | Settings | File Templates.
 */
public class QueryZhaopinzhJob extends CommonBusiness {
    /**
     * @param activity
     * @param jsonObject
     * @throws com.weiyitech.zhaopinzh.exception.ZhaopinzhException
     *
     */
    void sendAdvertisementEmployerDataToPresentation(final Activity activity, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            if (jsonObject != null) {
                Gson gson = new Gson();
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                List<AdvertisementEmployer> advertisementEmployerList = gson.fromJson(jsonArray.toString(), new TypeToken<List<AdvertisementEmployer>>() {
                }.getType());
                int cnt = jsonObject.getInt("count");
                ((AdvertisementActivity) activity).getDataFromBusiness(Common.ADVERTISEMENT_EMPLOYER_TYPE, advertisementEmployerList, cnt);
            } else {
                throw new ZhaopinzhException("Function sendAdvertisementEmployerDataToPresentation jsonObject对象为空");
            }
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendAdvertisementEmployerDataToPresentation JSONException", e);
        }
    }

    /**
     * @param jsonObject
     * @throws com.weiyitech.zhaopinzh.exception.ZhaopinzhException
     *
     */
    void sendZhaopinzhJobDetailDataToPresentation(Context context, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            JSONObject detailObject = jsonObject.getJSONObject("detail");
            if (jsonObject != null) {
                Gson gson = new Gson();
                JobDetailZhaopinzh jobDetailZhaopinzh = gson.fromJson(detailObject.toString(), JobDetailZhaopinzh.class);
                ((ZhaopinzhJobDetailActivity) context).getDataFromBusiness(Common.ZHAOPINZH_JOB_DETAIL_TYPE, jobDetailZhaopinzh);
            } else {
                throw new ZhaopinzhException("Function sendDetailDataToPresentation jsonObject对象为空");
            }
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendDetailDataToPresentation JSONException", e);
        }
    }

    /**
     * @param jsonObject
     * @throws com.weiyitech.zhaopinzh.exception.ZhaopinzhException
     *
     */
    void sendZhaopinzhJobConciseDataToPresentation(Fragment fragment, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            if (jsonObject != null) {
                Gson gson = new Gson();
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                ZhaopinzhJobPager zhaopinzhJobPager = new ZhaopinzhJobPager();
                zhaopinzhJobPager.pager = gson.fromJson(jsonObject.getJSONObject("pager").toString(), Pager.class);
                zhaopinzhJobPager.list  = gson.fromJson(jsonArray.toString(), new TypeToken<List<JobDetailZhaopinzh>>() {
                }.getType());
                ((ZhaopinzhJobListFragment) fragment).getDataFromBusiness(Common.ZHAOPINZH_JOB_CONCISE_TYPE, zhaopinzhJobPager);
            } else {
                throw new ZhaopinzhException("Function sendZhaopinzhJobConciseDataToPresentation jsonObject对象为空");
            }
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendZhaopinzhJobConciseDataToPresentation JSONException", e);
        }
    }

    /**
     * @param jsonObject
     * @throws com.weiyitech.zhaopinzh.exception.ZhaopinzhException
     *
     */
    void sendZhaopinzhTotalDataToPresentation(Fragment fragment, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            Gson gson = new Gson();
            Pager pager;
            if (jsonObject != null) {
                JSONObject jsonObject1 = jsonObject.getJSONObject("pager");
                pager = gson.fromJson(jsonObject1.toString(), Pager.class);
                ((ZhaopinzhJobListFragment) fragment).getDataFromBusiness(Common.JOBTOTAL_TYPE, pager);
            } else {
                throw new ZhaopinzhException("Function sendZhaopinzhTotalDataToPresentation jsonObject对象为空");
            }
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendZhaopinzhTotalDataToPresentation JSONException", e);
        }
    }

    /**
     * 页面查询函数
     *
     * @param params   请求参数
     * @param activity 发送到的页面
     */
    public void queryAdvertisementEmployer(Object params, final Activity activity) {
        if (!checkNetWork(activity)) {
            ((AdvertisementActivity) activity).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/job/advertimsentEmployer.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.get(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        sendAdvertisementEmployerDataToPresentation(activity, getValuesFromResult(jsonObject));
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
        if (!checkNetWork(context)) {
            ((NetJobDetailActivity) context).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/job/jobZhaopinzhDetail.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.post(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        sendZhaopinzhJobDetailDataToPresentation(context, getValuesFromResult(jsonObject));
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

    public void queryJobConcise(Object params, final Fragment fragment) {
        if (!checkNetWork(fragment.getActivity())) {
            ((ZhaopinzhJobListFragment) fragment).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/job/zhaopinzhJobList.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.post(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        sendZhaopinzhJobConciseDataToPresentation(fragment, getValuesFromResult(jsonObject));
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


    public void queryJobTotal(Object params, final Fragment fragment) {
        if (!checkNetWork(fragment.getActivity())) {
            ((ZhaopinzhJobListFragment) fragment).getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/job/zhaopinzhPager.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.post(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        sendZhaopinzhTotalDataToPresentation(fragment, getValuesFromResult(jsonObject));
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
