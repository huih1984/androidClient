package com.weiyitech.zhaopinzh.business;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.presentation.activity.WebJobActivity;
import com.weiyitech.zhaopinzh.util.Common;

import java.util.Timer;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-7-9
 * Time: 上午9:56
 * To change this template use File | Settings | File Templates.
 */
public class QueryWebJob extends CommonBusiness {

    public void queryWeb(String url, final WebJobActivity context) {
        if (!checkNetWork(context)) {
            context.getDataFromBusiness(Common.NET_WORK_DISCONNECTED, null);
            return;
        }

        AsyncHttpClient client = new AsyncHttpClient();
        try {
            final ZhaopinzhApp zhaopinzhApp = (ZhaopinzhApp) context.getApplicationContext();
            zhaopinzhApp.getTotalTimeout = true;

            java.util.Timer timer;
            timer = new Timer(true);
            timer.schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            if (zhaopinzhApp.getTotalTimeout == true) {
                                timeoutBusiness(context.getBaseContext());
                            }
                        }
                    }, Common.TIMEOUTSECONDS);
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String s) {
                    super.onSuccess(s);
                    context.getDataFromBusiness(Common.WEB_JOB_TYPE, s);
                }

                @Override
                public void onFailure(Throwable throwable, String s) {
                    super.onFailure(throwable, s);    //To change body of overridden methods use File | Settings | File Templates.
                }
            });
        } catch (Exception e) {

        }
    }
}
