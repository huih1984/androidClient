package com.weiyitech.zhaopinzh.presentation.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TabWidget;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.component.FairListFragment;
import com.weiyitech.zhaopinzh.presentation.component.FavoriteListFragment;
import com.weiyitech.zhaopinzh.presentation.component.JobListFragment;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.baidupush.Utils;

import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-10-10
 * Time: 下午4:11
 */
public class HomeActivity extends CommonActivity  /*ActionBar.TabListener*/ {
    public FragmentTabHost mTabHost;
    public int resumeFromWhere;
    public final static int FROM_SEARCHACTIVITY = 1000;
    public final static int FROM_OTHER = 1001;
    String fromWhere;

    void addFragment(FragmentTabHost mTabHost, int index, String[] array) {
        if (index == 0) {
            mTabHost.addTab(mTabHost.newTabSpec("fairlist").setIndicator(array[0]),
                    FairListFragment.class, null);
        } else if (index == 1) {
            mTabHost.addTab(mTabHost.newTabSpec("joblist").setIndicator(array[1]),
                    JobListFragment.class, null);
        } else if (index == 2) {
            mTabHost.addTab(mTabHost.newTabSpec("favoritelist").setIndicator(array[2]),
                    FavoriteListFragment.class, null);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null).commit();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        super.onCreate(null);
        //        /*设置布局*/
        mTabHost = new FragmentTabHost(this);
        setContentView(mTabHost);
        fromWhere = getIntent().getStringExtra("where");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mTabHost.setup(this, getSupportFragmentManager(), R.id.content_frame);

        TabWidget tabWidget;
        tabWidget = mTabHost.getTabWidget();
//        tabWidget.setDividerDrawable(new ColorDrawable(0x00000000));
//        tabWidget.setShowDividers(TabWidget.SHOW_DIVIDER_MIDDLE);
//        tabWidget.setDividerPadding(12);


        String[] array;
        array = getResources().getStringArray(R.array.home_activity_fragment_name_list);
        List<Integer> order = Common.getHomeActivityFragmentSettingOrder(this);
        addFragment(mTabHost, order.get(0), array);
        addFragment(mTabHost, order.get(1), array);
        addFragment(mTabHost, order.get(2), array);

        tabWidget.setBackgroundResource(R.drawable.actionbar_tab_indicator);

        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            View view = tabWidget.getChildAt(i);
            view.setBackgroundResource(R.drawable.actionbar_tab_indicator);
            ViewGroup.LayoutParams layoutParams = tabWidget.getChildAt(i).getLayoutParams();
            layoutParams.height = (int) (40 * ZhaopinzhApp.getInstance().hardDensity);
            view.setLayoutParams(layoutParams);
//            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
//            params.setMargins(0, 0, 0, 0);
        }


        ZhaopinzhApp app = ((ZhaopinzhApp) getApplicationContext());
        app.addActivityToMap(this);

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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
            mTabHost.setCurrentTabByTag("joblist");
        }
        super.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    void goHome() {
        super.goHome();
        Intent intent = null;
        if (fromWhere != null) {
            if (fromWhere.equals(TalkActivity.class.getName())) {
                intent = new Intent(this, TalkActivity.class);
                startActivity(intent);
            } else if (fromWhere.equals(SearchActivity.class.getName())) {
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goHome();
                break;
//            case R.id.home_activity_action_search:
//                openSearch();
//                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void onBackPressed() {
        goHome();
        //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }


}
