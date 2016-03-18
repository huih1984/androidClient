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
import com.weiyitech.zhaopinzh.presentation.component.NetJobFavoriteFragment;
import com.weiyitech.zhaopinzh.presentation.component.NetJobListFragment;
import com.weiyitech.zhaopinzh.presentation.component.ZhaopinzhJobFavoriteFragment;
import com.weiyitech.zhaopinzh.presentation.component.ZhaopinzhJobListFragment;
import com.weiyitech.zhaopinzh.util.Common;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-7-28
 * Time: 上午11:04
 * To change this template use File | Settings | File Templates.
 */
public class ZhaopinzhJobConciseListActivity extends CommonActivity {
    public FragmentTabHost mTabHost;
    public int resumeFromWhere;
    public final static int FROM_SEARCHACTIVITY = 1000;
    public final static int FROM_OTHER = 1001;


    void addFragment(FragmentTabHost mTabHost, int index, String[] array) {
        if (index == 0) {
            mTabHost.addTab(mTabHost.newTabSpec("zhaopinzhjoblist").setIndicator(array[0]),
                    ZhaopinzhJobListFragment.class, null);
        } else if (index == 1) {
            mTabHost.addTab(mTabHost.newTabSpec("zhaopinzhjobfavoritelist").setIndicator(array[1]),
                    ZhaopinzhJobFavoriteFragment.class, null);
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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mTabHost.setup(this, getSupportFragmentManager(), R.id.content_frame);

        String[] array;
        array = getResources().getStringArray(R.array.zhaopinzh_job_concise_activity_fragment_name_list);
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
            layoutParams.height = (int)(40*ZhaopinzhApp.getInstance().hardDensity);
            view.setLayoutParams(layoutParams);
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
        if(((ZhaopinzhApp)(getApplicationContext())).getActiveActivity() == SearchActivity.class.getName()){
            resumeFromWhere = FROM_SEARCHACTIVITY;
            mTabHost.setCurrentTabByTag("zhaopinzhjoblist");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, AdvertisementActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;
//            case R.id.home_activity_action_search:
//                openSearch();
//                break;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, AdvertisementActivity.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}