package com.weiyitech.zhaopinzh.presentation.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
//import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.business.CommentBusiness;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.struct.Comment;
import com.weiyitech.zhaopinzh.util.Common;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-11-8
 * Time: 上午11:28
 * To change this template use File | Settings | File Templates.
 */
public class CommonActivity extends ActionBarActivity implements BusinessInterface {
    public boolean destroyed;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            if (savedInstanceState != null) {
                Log.d("zhaopinzhException", "savedInstanceState is not null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("zhaopinzhException", e.toString());
            for (String key : savedInstanceState.keySet()) {
                Log.e("zhaopinzhException", key);
                if (key.equals("android:viewHierarchyState")) {
                    Log.e("zhaopinzhException", "key:" + key + ", value:" + savedInstanceState.get(key).toString());
                }
            }
            Log.e("zhaopinzhException", savedInstanceState.toString());
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * @param employerId
     */
    void getComment(int employerId) {
        Comment comment = new Comment();
        comment.setEmployerId(employerId);
        CommentBusiness commentBusiness = new CommentBusiness();
        commentBusiness.getCommentTotal(comment, this);
    }

    /**
     * 获取评论activity子类必须继承实现的方法
     *
     * @param comment
     */
    void setDiamondViewValue(Comment comment) {

    }

    @Override
    public void getDataFromBusiness(int dataType, Object t, int... position) {
//        if(dataType == Common.COMMENT_TOTAL_TYPE){
//            Pager pager = (Pager) t;
//            setCommentTotalViewValue(pager.total);
//        } else if(dataType == Common.COMMENT_AVARAGE_TYPE){
//            Comment comment = (Comment) t;
//            setDiamondViewValue(comment);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.common_activity_actions, menu);
        return true;
    }

    /*跳转到设置页面*/
    void openSettings() {
        Intent settingActivity = new Intent();
        settingActivity.setClass(this, SettingActivity.class);
        //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        startActivity(settingActivity);
    }

    void openHelp() {
        String url = "http://www.baidu.com";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        startActivity(intent);
    }

    /*跳转到设置页面*/
    void openFeedback() {
        Intent feedBackIntent = new Intent();
        feedBackIntent.setClass(this, FeedBackActivity.class);
        //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        startActivity(feedBackIntent);
    }


    void goHome() {

    }

    @Override
    public void onBackPressed() {
        goHome();
    }

    /*选择不同菜单的响应*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                break;
            case android.R.id.home:
                goHome();
                //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                break;
           /* case R.id.action_help:
                openHelp();
                break;*/
            case R.id.action_feedback:
                openFeedback();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        destroyed = false;
        if (Common.getIsUNSpecified(this).equals("竖屏显示")) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        ((ZhaopinzhApp) getApplicationContext()).setActiveActivity(this.getClass().getName());
        MobclickAgent.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyed = true;
    }
}