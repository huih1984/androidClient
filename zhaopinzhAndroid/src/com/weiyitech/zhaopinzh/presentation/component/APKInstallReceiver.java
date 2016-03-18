package com.weiyitech.zhaopinzh.presentation.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-12-2
 * Time: 下午1:50
 * To change this template use File | Settings | File Templates.
 */
public class APKInstallReceiver extends BroadcastReceiver {


    @Override

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String packageName = intent.getDataString().substring(8);
            System.out.println("安装:" + packageName + "包名的程序");
        }
        //接收卸载广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            String packageName = intent.getDataString().substring(8);
            System.out.println("卸载:" + packageName + "包名的程序");
            Intent newIntent =
                    new Intent();
            newIntent.setClassName(packageName, packageName +
                    ".SplashActivity");
            newIntent.setAction(
                    "android.intent.action.MAIN");
            newIntent.addCategory(
                    "android.intent.category.LAUNCHER");
            newIntent.setFlags(Intent.
                    FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newIntent);
        }
    }
}
