package com.weiyitech.zhaopinzh.presentation.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabWidget;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.component.*;
import com.weiyitech.zhaopinzh.util.Common;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-5-15
 * Time: 下午2:52
 * To change this template use File | Settings | File Templates.
 */
public class CampusActivity extends CommonActivity {

    public FragmentTabHost mTabHost;
    public int resumeFromWhere;
    public final static int FROM_SEARCHACTIVITY = 1000;
    public final static int FROM_OTHER = 1001;
    String fromWhere;

    void addFragment(FragmentTabHost mTabHost, int index, String[] array) {
        if (index == 0) {
            mTabHost.addTab(mTabHost.newTabSpec("campuslist").setIndicator(array[0]),
                    PreachingUniversityListFragment.class, null);
        } else if (index == 1) {
            mTabHost.addTab(mTabHost.newTabSpec("preachinglist").setIndicator(array[1]),
                    PreachingMeetListFragment.class, null);
        } else if (index == 2) {
            mTabHost.addTab(mTabHost.newTabSpec("preachingfavoritelist").setIndicator(array[2]),
                    PreachingMeetFavoriteFragment.class, null);
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

        mTabHost.setup(this, getSupportFragmentManager(), R.id.content_frame);

        String[] array;
        array = getResources().getStringArray(R.array.campus_activity_fragment_name_list);
        List<Integer> order = Common.getCampusActivityFragmentSettingOrder(this);
        addFragment(mTabHost, order.get(0), array);
        addFragment(mTabHost, order.get(1), array);
        addFragment(mTabHost, order.get(2), array);

        TabWidget tabWidget;
        tabWidget = mTabHost.getTabWidget();
        tabWidget.setBackgroundResource(R.drawable.actionbar_tab_indicator);

        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            View view = tabWidget.getChildAt(i);
            view.setBackgroundResource(R.drawable.actionbar_tab_indicator);
            ViewGroup.LayoutParams layoutParams = tabWidget.getChildAt(i).getLayoutParams();
            layoutParams.height = (int)(40* ZhaopinzhApp.getInstance().hardDensity);
            view.setLayoutParams(layoutParams);
        }

        ZhaopinzhApp app = ((ZhaopinzhApp) getApplicationContext());
        app.addActivityToMap(this);
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
        getMenuInflater().inflate(R.menu.campus_activity_actions, menu);
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
        if(((ZhaopinzhApp)(getApplicationContext())).getActiveActivity() == SearchActivity.class.getName()){
            resumeFromWhere = FROM_SEARCHACTIVITY;
            mTabHost.setCurrentTabByTag("preachinglist");
        }
        super.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    /*点击搜索按钮的响应函数*/
    void openSearch() {
        Intent intent = new Intent();
        intent.putExtra("where", this.getClass().getName());
        intent.setClass(this, SearchActivity.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
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
                return true;
//            case R.id.campus_activity_action_search:
//                openSearch();
//                break;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        goHome();
    }
}