package com.weiyitech.zhaopinzh.business;

import android.content.Context;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.weiyitech.zhaopinzh.exception.ZhaopinzhException;
import com.weiyitech.zhaopinzh.presentation.activity.TrafficActivity;
import com.weiyitech.zhaopinzh.struct.TrafficRsp;
import com.weiyitech.zhaopinzh.util.Common;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-1-25
 * Time: 下午7:13
 * To change this template use File | Settings | File Templates.
 */
public class AddFeedback extends CommonBusiness {

    public void addFeedback(Object params, final Context destContext) {
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/survey/answer.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.get(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        Toast.makeText(destContext, "发送成功！", Toast.LENGTH_SHORT).show();
                    } catch (ZhaopinzhException e) {
                        //对最终的异常要进行处理
                    }
                    super.onSuccess(jsonObject);
                }

                @Override
                public void onFailure(Throwable throwable, JSONObject jsonObject) {
                    super.onFailure(throwable, jsonObject);
                    Toast.makeText(destContext, "发送失败！", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {

        }
    }
}
