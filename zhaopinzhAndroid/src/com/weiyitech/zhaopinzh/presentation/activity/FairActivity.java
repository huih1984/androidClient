package com.weiyitech.zhaopinzh.presentation.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.business.QueryFair;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.struct.*;
import com.weiyitech.zhaopinzh.util.Common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FairActivity extends CommonActivity implements BusinessInterface {

    JobFairDetailRsp jobFairDetailRsp;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fair_activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        int jobFairId = getIntent().getIntExtra("fairId", 0);
        JobFairDetailReq jobFairDetailReq = new JobFairDetailReq();
        jobFairDetailReq.fairId = jobFairId;
        QueryFair queryFair = new QueryFair();
        queryFair.queryFairDetail(jobFairDetailReq, this);
    }

    /**
     * 句柄处理界面刷新事件
     */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            //此处可以更新UI
            Bundle bundle = msg.getData();
            int dataType = bundle.getInt("type");
            if (dataType == Common.JOBFAIRDETAIL_TYPE) {
               jobFairDetailRsp = bundle.getParcelable("detail");
                TextView descTextView = (TextView)findViewById(R.id.fair_activity_desc_text_view);
                TextView dynamicInfoTextView = (TextView)findViewById(R.id.fair_activity_dynamic_info_text_view);
                TextView fairNameTextView = (TextView)findViewById(R.id.fair_activity_fair_name_text_view);
                TextView runnerTextView = (TextView)findViewById(R.id.fair_activity_runner_text_view);
                TextView timeTextView = (TextView)findViewById(R.id.fair_activity_time_text_view);
                TextView hallTextView = (TextView) findViewById(R.id.fair_activity_hall_text_view);
                if(jobFairDetailRsp.desc != null)  descTextView.setText(jobFairDetailRsp.desc);
                if(jobFairDetailRsp.dynamicInfo != null)  dynamicInfoTextView.setText(jobFairDetailRsp.dynamicInfo);
                if(jobFairDetailRsp.fairName != null)  fairNameTextView.setText(jobFairDetailRsp.fairName);
                if(jobFairDetailRsp.runner != null)  runnerTextView.setText(jobFairDetailRsp.runner);
                if(jobFairDetailRsp.hallName != null)  hallTextView.setText(jobFairDetailRsp.hallName);
                Date date = null;
                DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                try{
                    date = format1.parse(jobFairDetailRsp.runningTime);
                    String runningTime = format1.format(date);
                    if (!TextUtils.isEmpty(format1.format(date))){
                        timeTextView.setText(runningTime);
                    } else {
                        timeTextView.setVisibility(View.GONE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void getDataFromBusiness(int dataType, Object t, int... position) {
        Message message = null;
        Bundle bundle = new Bundle();
        message = new Message();
        if (dataType == Common.JOBFAIRDETAIL_TYPE){
            bundle.putInt("type", Common.JOBFAIRDETAIL_TYPE);
            bundle.putParcelable("detail", (JobFairDetailRsp) t);
        }
        message.setData(bundle);
        handler.sendMessage(message);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // action bar中的应用程序图标被点击了，返回home
                Intent intent = new Intent(this, JobsOfFairActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;
            case R.id.search_activity_action_search:
                onSearchRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }
}