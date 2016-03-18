package com.weiyitech.zhaopinzh.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ListView;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.adapter.AllFavoriteListAdapter;
import com.weiyitech.zhaopinzh.struct.JobConciseNetRsp;
import com.weiyitech.zhaopinzh.struct.PreachmeetingConciseRsp;
import com.weiyitech.zhaopinzh.struct.RunEmployerComponent;
import com.weiyitech.zhaopinzh.util.MyDatabaseUtil;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-8-12
 * Time: 下午2:26
 * To change this template use File | Settings | File Templates.
 */
public class FavoriteActivity extends ActionBarActivity {
    ListView listView;
    AllFavoriteListAdapter allFavoriteListAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_activity);
        listView = (ListView) findViewById(R.id.favorite_activity_list_view);
        ArrayList<RunEmployerComponent> runEmployerComponentArrayList = new ArrayList<RunEmployerComponent>();
        ArrayList<PreachmeetingConciseRsp> preachmeetingConciseRspArrayList = new ArrayList<PreachmeetingConciseRsp>();
        ArrayList<JobConciseNetRsp> jobConciseNetRspArrayList = new ArrayList<JobConciseNetRsp>();
        MyDatabaseUtil.putRunEmployerFromDataBase(runEmployerComponentArrayList, this);
        MyDatabaseUtil.putPreachMeetingFromDataBase(preachmeetingConciseRspArrayList, this);
        MyDatabaseUtil.putNetJobFromDataBase(jobConciseNetRspArrayList, this);
        allFavoriteListAdapter = new AllFavoriteListAdapter(this, runEmployerComponentArrayList, preachmeetingConciseRspArrayList, jobConciseNetRspArrayList);
        listView.setAdapter(allFavoriteListAdapter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, TalkActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, TalkActivity.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}