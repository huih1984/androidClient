package com.weiyitech.zhaopinzh.presentation.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.business.QueryVersion;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.struct.VersionReq;
import com.weiyitech.zhaopinzh.struct.VersionRsp;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.MyDatabaseUtil;
import com.weiyitech.zhaopinzh.util.SearchConditionUtil;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-10-10
 * Time: 下午3:55
 * To change this template use File | Settings | File Templates.
 */
public class SplashActivity extends Activity implements BusinessInterface {
    private static final String TAG = "SplashActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        if (Common.isNetworkConnected(this)) {
        } else {
            Toast.makeText(this, "您的网络没有打开！请打开您的网络或者连接上wifi再使用", 1000).show();
        }
        checkVersion();
        MyDatabaseUtil.deleteObsoleteIds(this);
    }

    boolean isFirstIn = false;
    int updateType = -1;
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    // 延迟0.5秒
    private static final long SPLASH_DELAY_MILLIS = 1000;

    private static final String SHAREDPREFERENCES_NAME = "first_pref";

    /**
     * Handler:跳转到不同界面
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            SharedPreferences preferences = getSharedPreferences(
                    SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            // 存入数据
            try {
                editor.putString("ver_name", getVersionName());
                // 提交修改
                editor.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
                case ALERTDIALOG:
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                    builder.setTitle("5.0.6更新");
                    builder.setMessage("修改程序退出的bug");

                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (updateType == 1000) {
                                mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
                            } else {
                                mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    break;
            }
            super.handleMessage(msg);
        }
    };

    final static int ALERTDIALOG = 1111;

    private void init() {
        // 读取SharedPreferences中需要的数据
        // 使用SharedPreferences来记录程序的使用次数
        SharedPreferences preferences = getSharedPreferences(
                SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        try {
            String versionName = getVersionName();
            String settingVerName = preferences.getString("ver_name", "0.0.0");
            if (settingVerName.equals("0.0.0")) {
                //首次安装，setting文件中没有版本记录
                SearchConditionUtil.saveSettings(SearchConditionUtil.getIndustryList(), "industry", null);
                SearchConditionUtil.saveSettings(SearchConditionUtil.getJobTypeList(), "jobtype", null);
                mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
            } else {
                if (Common.verEquals(versionName, settingVerName) == 0) {
                    //相同版本
                    mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
                } else if (Common.verEquals(versionName, settingVerName) == 1) {
                    //当前版本大于setting中的版本
                    mHandler.sendEmptyMessage(ALERTDIALOG);
                    //在5.0.1进行搜索页面的更新，将过去的设置清空
                    if(Common.verEquals(settingVerName, "5.0.1") == -1){
                        if (Common.verEquals(settingVerName, "5.0.1") == -1) {
                            SearchConditionUtil.saveSettings(SearchConditionUtil.getIndustryList(), "industry", null);
                            SearchConditionUtil.saveSettings(SearchConditionUtil.getJobTypeList(), "jobtype", null);
                        }
                    }
                }
            }

            //设置的赋值内容发生改变，需要将之前的设置重新设置值时使用，这个在第一个版本到第二个版本中使用，现在不需要这个方法了
            //Common.resetSettingValue(SplashActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goHome() {
        Intent intent = new Intent(SplashActivity.this, TalkActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
        //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    private void goGuide() {
        Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();


    }

    @Override
    public void getDataFromBusiness(int dataType, Object t, int... position) {
        if (dataType == Common.VERSION_QUERY_TYPE) {
            VersionRsp versionRsp = (VersionRsp) t;
            //只在版本更新为必须更新时，才提醒用户更新
            if (versionRsp.importance == 2) {
                showUpdateDialog(versionRsp.updatePath);
            } else {
                init();
            }
        } else if (dataType == Common.VERSION_QUERY_NO_UPDATE_TYPE) {
            updateType = ((VersionRsp) t).updateType;
            init();
        } else if (dataType == Common.TIMEOUT_TYPE) {
            init();
        } else if (dataType == Common.APK_TYPE) {
            installApk((File) t);
        }

    }

    private String getVersionName() throws Exception {
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packInfo.versionName;
    }

    void checkVersion() {
        try {
            VersionReq versionReq = new VersionReq();
            versionReq.versionNum = getVersionName();
            QueryVersion queryVersion = new QueryVersion();
            queryVersion.queryVersion(versionReq, this);
        } catch (Exception e) {

        }
    }

    /*
     *
     * 弹出对话框通知用户更新程序
     *
     * 弹出对话框的步骤：
     *  1.创建alertDialog的builder.
     *  2.要给builder设置属性, 对话框的内容,样式,按钮
     *  3.通过builder 创建一个对话框
     *  4.对话框show()出来
     */
    protected void showUpdateDialog(final String updatePath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("版本升级");
        builder.setMessage("检查到最新版本，请及时更新！");
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "下载apk,更新");
                QueryVersion queryVersion = new QueryVersion();
                queryVersion.downLoadApk(updatePath, SplashActivity.this);
            }
        });
        //当点取消按钮时进行登录
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                init();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
        //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Common.getIsUNSpecified(this).equals("竖屏显示")) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        ((ZhaopinzhApp) getApplicationContext()).setActiveActivity(this.getClass().getName());
    }
}