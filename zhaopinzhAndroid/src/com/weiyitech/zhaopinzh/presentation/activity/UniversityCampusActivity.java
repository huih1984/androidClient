package com.weiyitech.zhaopinzh.presentation.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabWidget;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.component.PreachingUniversityListFragment;
import com.weiyitech.zhaopinzh.presentation.component.UniversityPreachingMeetListFragment;
import com.weiyitech.zhaopinzh.util.Common;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-5-26
 * Time: 上午10:11
 * To change this template use File | Settings | File Templates.
 */
public class UniversityCampusActivity extends CommonActivity {

    public FragmentTabHost mTabHost;
    public int resumeFromWhere;
    public final static int FROM_SEARCHACTIVITY = 1000;
    public final static int FROM_OTHER = 1001;
    public String university;

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            //        /*设置布局*/
            mTabHost = new FragmentTabHost(this);
            setContentView(mTabHost);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);

            //从搜索返回的时候，不需要赋值
            if (university == null) university = getIntent().getStringExtra("university");
            mTabHost.setup(this, getSupportFragmentManager(), R.id.content_frame);

            mTabHost.addTab(mTabHost.newTabSpec("universitypreachinglist").setIndicator(university),
                    UniversityPreachingMeetListFragment.class, null);
            getSupportFragmentManager().beginTransaction().addToBackStack(null).commit();

            TabWidget tabWidget;
            tabWidget = mTabHost.getTabWidget();

            for (int i = 0; i < tabWidget.getChildCount(); i++) {
                View view = tabWidget.getChildAt(i);
                view.setBackgroundResource(R.drawable.tab_selected);
                ViewGroup.LayoutParams layoutParams = tabWidget.getChildAt(i).getLayoutParams();
                layoutParams.height = (int) (48 * ZhaopinzhApp.getInstance().hardDensity);
                view.setLayoutParams(layoutParams);
            }

            ZhaopinzhApp app = ((ZhaopinzhApp) getApplicationContext());
            app.addActivityToMap(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (((ZhaopinzhApp) (getApplicationContext())).getActiveActivity() == SearchActivity.class.getName()) {
            resumeFromWhere = FROM_SEARCHACTIVITY;
        }
        super.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    /*点击搜索按钮的响应函数*/
    void openSearch() {
        Intent intent = new Intent();
        intent.putExtra("where", this.getClass().getName());
        intent.putExtra("university", university);
        intent.setClass(this, SearchActivity.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, CampusActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;
//            case R.id.campus_activity_action_search:
//                openSearch();
//                break;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, CampusActivity.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }


}