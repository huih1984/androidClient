package com.weiyitech.zhaopinzh.presentation.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.business.QueryWebJob;
import com.weiyitech.zhaopinzh.presentation.R;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-7-9
 * Time: 上午9:59
 * To change this template use File | Settings | File Templates.
 */
public class WebJobActivity extends CommonActivity implements BusinessInterface {
    WebView webView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_job_activity);
        webView = (WebView)findViewById(R.id.web_job_activity_web_view);
//        QueryWebJob queryWebJob = new QueryWebJob();
//        queryWebJob.queryWeb("http://jobs.zhaopin.com/nanjing/p3", this);
    }

    @Override
    public void getDataFromBusiness(int dataType, Object t, int... position) {
        webView.loadData((String)t, "text/html; charset=utf8", null);
    }
}