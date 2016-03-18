package com.weiyitech.zhaopinzh.presentation.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.*;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.navdrawer.SimpleSideDrawer;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.*;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.utils.LogUtil;
import com.tencent.weibo.sdk.android.api.*;
import com.tencent.weibo.sdk.android.api.util.Util;
import com.tencent.weibo.sdk.android.model.AccountModel;
import com.tencent.weibo.sdk.android.model.BaseVO;
import com.tencent.weibo.sdk.android.model.ModelResult;
import com.tencent.weibo.sdk.android.network.HttpCallback;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.business.QueryJob;
import com.weiyitech.zhaopinzh.business.QueryNetJob;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.adapter.NetJobScreenSlidePagerAdapter;
import com.weiyitech.zhaopinzh.presentation.adapter.ScreenSlidePagerAdapter;
import com.weiyitech.zhaopinzh.presentation.component.*;
import com.weiyitech.zhaopinzh.struct.*;
import com.weiyitech.zhaopinzh.util.Common;
import android.support.v4.app.FragmentManager;
import com.weiyitech.zhaopinzh.util.baidupush.Utils;
import com.weiyitech.zhaopinzh.util.weibo.sina.AccessTokenKeeper;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-7-28
 * Time: 上午11:04
 * To change this template use File | Settings | File Templates.
 */
public class NetJobConciseListActivity extends CommonActivity {
    public FragmentTabHost mTabHost;
    public int resumeFromWhere;
    public final static int FROM_SEARCHACTIVITY = 1000;
    public final static int FROM_OTHER = 1001;
    String fromWhere;

    void addFragment(FragmentTabHost mTabHost, int index, String[] array) {
        if (index == 0) {
            mTabHost.addTab(mTabHost.newTabSpec("netjoblist").setIndicator(array[0]),
                    NetJobListFragment.class, null);
        } else if (index == 1) {
            mTabHost.addTab(mTabHost.newTabSpec("netjobfavoritelist").setIndicator(array[1]),
                    NetJobFavoriteFragment.class, null);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null).commit();
    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        /*设置布局*/
        mTabHost = new FragmentTabHost(this);
        setContentView(mTabHost);

        fromWhere = getIntent().getStringExtra("where");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

//        try {
//            ViewConfiguration mconfig = ViewConfiguration.get(this);
//            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
//            if (menuKeyField != null) {
//                menuKeyField.setAccessible(true);
//                menuKeyField.setBoolean(mconfig, false);
//            }
//        } catch (Exception ex) {
//        }



        mTabHost.setup(this, getSupportFragmentManager(), R.id.content_frame);

        String[] array;
        array = getResources().getStringArray(R.array.net_job_concise_activity_fragment_name_list);
        List<Integer> order = Common.getNetActivityFragmentSettingOrder(this);
        addFragment(mTabHost, order.get(0), array);
        addFragment(mTabHost, order.get(1), array);

        TabWidget tabWidget;
        tabWidget = mTabHost.getTabWidget();
        tabWidget.setBackgroundResource(R.drawable.actionbar_tab_indicator);

        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            View view = tabWidget.getChildAt(i);
            view.setBackgroundResource(R.drawable.actionbar_tab_indicator);
            ViewGroup.LayoutParams layoutParams = tabWidget.getChildAt(i).getLayoutParams();
            layoutParams.height = (int) (40 * ZhaopinzhApp.getInstance().hardDensity);
            view.setLayoutParams(layoutParams);
        }

        ZhaopinzhApp app = ((ZhaopinzhApp) getApplicationContext());
        app.addActivityToMap(this);

    }


    /**
     * 到Url去下載圖片回傳BITMAP回來
     * @param imgUrl
     * @return
     */
    private Bitmap getBitmapFromUrl(String imgUrl) {
        URL url;
        Bitmap bitmap = null;
        try {
            url = new URL(imgUrl);
            InputStream is = url.openConnection().getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, TalkActivity.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    /*点击搜索按钮的响应函数*/
    void openSearch() {
        Intent intent = new Intent();
        intent.putExtra("where", this.getClass().getName());
        intent.setClass(this, SearchActivity.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    /**
     * 创建菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.home_activity_actions, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     *
     */
    @Override
    protected void onResume() {
        if (Common.getIsUNSpecified(this).equals("竖屏显示")) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if (((ZhaopinzhApp) (getApplicationContext())).getActiveActivity() == SearchActivity.class.getName()) {
            resumeFromWhere = FROM_SEARCHACTIVITY;
            mTabHost.setCurrentTabByTag("netjoblist");
        }
        super.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

//    @Override
//    void goHome() {
//        super.goHome();
//        Intent intent = null;
//        if (fromWhere != null) {
//            if (fromWhere.equals(AdvertisementActivity.class.getName())) {
//                intent = new Intent(this, AdvertisementActivity.class);
//                startActivity(intent);
//            } else if (fromWhere.equals(SearchActivity.class.getName())) {
//                intent = new Intent(this, SearchActivity.class);
//                startActivity(intent);
//            }
//        }
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case android.R.id.home:
//                goHome();
////            case R.id.home_activity_action_search:
////                openSearch();
//                break;
//            case android.R.id.home:
//                mNav.toggleLeftDrawer();
//                break;
            case android.R.id.home:
                Intent intent = new Intent(this, TalkActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
//        return true;
    }

}