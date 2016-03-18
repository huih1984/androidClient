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
import com.weiyitech.zhaopinzh.presentation.component.FairJobListFragment;
import com.weiyitech.zhaopinzh.util.Common;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-2-22
 * Time: 下午5:47
 * To change this template use File | Settings | File Templates.
 */
public class JobsOfFairActivity extends CommonActivity {
    public FragmentTabHost mTabHost;
    public int fairId;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mTabHost = new FragmentTabHost(this);
        setContentView(mTabHost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.content_frame);
        fairId = getIntent().getIntExtra("fairId", 0);
        mTabHost.addTab(mTabHost.newTabSpec("fairjoblist").setIndicator(getIntent().getExtras().getString("fairName")),
                FairJobListFragment.class, null);

        TabWidget tabWidget;
        tabWidget = mTabHost.getTabWidget();
        tabWidget.setBackgroundResource(R.drawable.tab_selected);

        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            View view = tabWidget.getChildAt(i);
            view.setBackgroundResource(R.drawable.tab_selected);
            ViewGroup.LayoutParams layoutParams = tabWidget.getChildAt(i).getLayoutParams();
            layoutParams.height = (int)(48*ZhaopinzhApp.getInstance().hardDensity);
            view.setLayoutParams(layoutParams);
        }
        ZhaopinzhApp app = ((ZhaopinzhApp) getApplicationContext());
        app.addActivityToMap(this);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);    //To change body of overridden methods use File | Settings | File Templates.
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // action bar中的应用程序图标被点击了，返回home
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;
//            case R.id.jobs_of_fair_activity_detail:
//                Intent intent1 = new Intent(this, FairActivity.class);
//                intent1.putExtra("fairId", fairId);
//                startActivity(intent1);
//                //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//                return true;
//            case R.id.jobs_fair_activity_action_search:
//                openSearch();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.jobs_of_fair_activity_actions, menu);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Common.getIsUNSpecified(this).equals("竖屏显示")) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

}