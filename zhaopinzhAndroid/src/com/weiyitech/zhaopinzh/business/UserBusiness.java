package com.weiyitech.zhaopinzh.business;

import android.content.Context;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.weiyitech.zhaopinzh.exception.ZhaopinzhException;
import com.weiyitech.zhaopinzh.presentation.activity.CommentActivity;
import com.weiyitech.zhaopinzh.struct.User;
import com.weiyitech.zhaopinzh.util.Common;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-10-24
 * Time: 下午9:55
 * To change this template use File | Settings | File Templates.
 */
public class UserBusiness {


    void sendUserToPresentation(final Context destContext, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            JSONObject jsonUser = jsonObject.getJSONObject("userBusiness");
            Gson gson = new Gson();
            User user = gson.fromJson(jsonUser.toString(), User.class);
            ((CommentActivity) destContext).getDataFromBusiness(Common.USER_TYPE, user);
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendUserToPresentation JSONException", e);
        }
    }

    public void addUser(Object params, final Context destContext) {
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/user/addUser.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.get(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {

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

    public void queryUser(Object params, final Context destContext){
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/user/queryUser.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.get(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        sendUserToPresentation(destContext, jsonObject);
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
