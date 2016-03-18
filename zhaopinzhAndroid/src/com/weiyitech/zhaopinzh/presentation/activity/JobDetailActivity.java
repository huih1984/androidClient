package com.weiyitech.zhaopinzh.presentation.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RatingBar;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.business.CommentBusiness;
import com.weiyitech.zhaopinzh.business.QueryJob;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.adapter.JobDetailAdapter;
import com.weiyitech.zhaopinzh.presentation.component.MessageProvider;
import com.weiyitech.zhaopinzh.struct.*;
import com.weiyitech.zhaopinzh.util.CommentUtil;
import com.weiyitech.zhaopinzh.util.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-3-31
 * Time: 下午5:09
 * To change this template use File | Settings | File Templates.
 */
public class JobDetailActivity extends CommonActivity implements BusinessInterface {
    public String fromWhere;
    ListView listView;
    JobDetailAdapter jobDetailAdapter;
    List<Object> list;
    Boolean otherExists;
    ArrayList<JobConciseRsp> jobConciseRsps;
    int position;
    JobDetailRsp jobDetailRsp;
    RunEmployerComponent runEmployerComponent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_detail_activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        list = new ArrayList<Object>();
        jobDetailAdapter = new JobDetailAdapter(this, list);
        listView = (ListView) findViewById(R.id.job_detail_activity_list_view);
        listView.setAdapter(jobDetailAdapter);

        fromWhere = getIntent().getStringExtra("where");
        if (fromWhere != null) {
            runEmployerComponent = getIntent().getParcelableExtra("run_employer_component");
            jobConciseRsps = getIntent().getParcelableArrayListExtra("job_consice_rsp_list");
            position = (Integer) getIntent().getExtras().get("position");
            if (runEmployerComponent == null || jobConciseRsps == null) {
                return;
            }
            list.add(jobConciseRsps.get(position));
            list.add(runEmployerComponent);
            otherExists = jobConciseRsps.size() > 1 ? true : false;
            if (jobDetailAdapter != null) {
                jobDetailAdapter.setJobConciseRsps(jobConciseRsps);
                jobDetailAdapter.setInListIndex(position);
            }
            ContentResolver resolver = getContentResolver();
            String selection = "employer_id = CAST(? AS INT) AND fair_id = CAST(? AS INT) AND job_id = CAST(? AS INT)";
            String[] selectionArgs = {String.valueOf(runEmployerComponent.runEmployer.employerId), String.valueOf(runEmployerComponent.runEmployer.fairId), String.valueOf(jobConciseRsps.get(position).jobId)};
            Cursor cursor = resolver.query(MessageProvider.JOB_URI, null, selection, selectionArgs, null);
            if (cursor != null) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    jobDetailRsp = new JobDetailRsp();
                    jobDetailRsp.jobId = cursor.getInt(cursor.getColumnIndex("job_id"));
                    jobDetailRsp.jobName = cursor.getString(cursor.getColumnIndex("job_name"));
                    jobDetailRsp.employerId = cursor.getInt(cursor.getColumnIndex("employer_id"));
                    jobDetailRsp.fairId = cursor.getInt(cursor.getColumnIndex("fair_id"));
                    jobDetailRsp.resp = cursor.getString(cursor.getColumnIndex("resp"));
                    jobDetailRsp.majorReq = cursor.getString(cursor.getColumnIndex("major_req"));
                    jobDetailRsp.eduReq = cursor.getString(cursor.getColumnIndex("edu_req"));
                    jobDetailRsp.expReq = cursor.getString(cursor.getColumnIndex("exp_req"));
                    jobDetailRsp.skillReq = cursor.getString(cursor.getColumnIndex("skill_req"));
                    jobDetailRsp.sexReq = cursor.getString(cursor.getColumnIndex("sex_req"));
                    jobDetailRsp.workplace = cursor.getString(cursor.getColumnIndex("workplace"));
                    jobDetailRsp.payment = cursor.getString(cursor.getColumnIndex("payment"));
                    addValueToList(jobDetailRsp);
                    if (otherExists) {
                        list.add(otherExists);
                    }
                    jobDetailAdapter.notifyDataSetChanged();
                }

                Comment comment = new Comment();
                comment.setEmployerId(jobDetailRsp.employerId);
                CommentBusiness commentBusiness = new CommentBusiness();
                commentBusiness.getEmployerAvarageDiamond(comment, this);


                CommentConcise commentConcise = new CommentConcise();
                commentConcise.employerId = jobDetailRsp.employerId;
                CommentUtil.queryCommentCnt(this, commentConcise);
            } else {
                QueryJob queryJob = new QueryJob();
                JobDetailReq jobDetailReq = new JobDetailReq();
                jobDetailReq.jobId = jobConciseRsps.get(position).jobId;
                jobDetailReq.fairId = jobConciseRsps.get(position).fairId;
                jobDetailReq.employerId = jobConciseRsps.get(position).employerId;
                queryJob.queryJobDetail(jobDetailReq, this);

                Comment comment = new Comment();
                comment.setEmployerId(jobDetailReq.employerId);
                CommentBusiness commentBusiness = new CommentBusiness();
                commentBusiness.getEmployerAvarageDiamond(comment, this);

                CommentConcise commentConcise = new CommentConcise();
                commentConcise.employerId = jobDetailReq.employerId;
                CommentUtil.queryCommentCnt(this, commentConcise);
            }
        }
    }

    void addValueToList(JobDetailRsp jobDetailRsp) {
        if (jobDetailRsp.resp != null) {
            list.add("岗位职责：" + jobDetailRsp.resp);
        }
        if (jobDetailRsp.skillReq != null) {
            list.add("岗位要求：" + jobDetailRsp.skillReq);
        }
        if (jobDetailRsp.workplace != null) {
            list.add("工作地点：" + jobDetailRsp.workplace);
        }
        if (jobDetailRsp.payment != null) {
            list.add("薪资待遇：" + jobDetailRsp.payment);
        }
        if (jobDetailRsp.expReq != null) {
            list.add("经验要求：" + jobDetailRsp.expReq);
        }
        if (jobDetailRsp.eduReq != null) {
            list.add("学历要求：" + jobDetailRsp.eduReq);
        }
        if (jobDetailRsp.majorReq != null) {
            list.add("专业要求：" + jobDetailRsp.majorReq);
        }
        if (jobDetailRsp.sexReq != null) {
            list.add("性别要求：" + jobDetailRsp.payment);
        }

        if (jobDetailRsp.workType != null) {
            list.add("工作类型：" + jobDetailRsp.workType);
        }
    }

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int dataType = bundle.getInt("type");
            if (dataType == Common.COMMENT_AVARAGE_TYPE) {
                Comment comment = bundle.getParcelable("comment");
                jobDetailAdapter.companyRating = comment.getAvarageDiamond();
                jobDetailAdapter.notifyDataSetChanged();
            }  else if (dataType == Common.COMMENT_TOTAL_TYPE) {
                jobDetailAdapter.commentCnt = ((CommentConcise)bundle.get("comment_concise")).total;
                jobDetailAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void getDataFromBusiness(int dataType, Object t, int... position) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        int index = 0;
        if (dataType == Common.JOBDETAIL_TYPE) {
            jobDetailRsp = (JobDetailRsp) t;
            addValueToList(jobDetailRsp);
            if (otherExists) {
                list.add(otherExists);
            }
            jobDetailAdapter.notifyDataSetChanged();

            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            Common.putJobDetail(contentValues, jobDetailRsp);
            resolver.insert(MessageProvider.JOB_URI, contentValues);
        } else if (dataType == Common.COMMENT_AVARAGE_TYPE) {
            bundle.putInt("type", Common.COMMENT_AVARAGE_TYPE);
            bundle.putParcelable("comment", (Comment) t);
        }  else if(dataType == Common.COMMENT_TOTAL_TYPE){
            bundle.putInt("type", Common.COMMENT_TOTAL_TYPE);
            bundle.putParcelable("comment_concise", (CommentConcise) t);
        }
        message.setData(bundle);
        handler.sendMessage(message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.job_detail_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (fromWhere.equals(HomeActivity.class.getName())) {
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                } else if (fromWhere.equals(JobsOfFairActivity.class.getName())) {
                    Intent intent = new Intent(this, JobsOfFairActivity.class);
                    startActivity(intent);
                }
                //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                break;
            case R.id.job_activity_share:
                String shareContent = "职位：" + jobDetailRsp.jobName +
                        "\n企业：" + runEmployerComponent.runEmployer.employerName +
                        "\n招聘时间：" + Common.formatTime(runEmployerComponent.runEmployer.runningTime, "yyyy-MM-dd", "MM月/dd日") +
                        "\n招聘会：" + runEmployerComponent.runEmployer.fairName +
                        "\n __来自<南京招聘汇APP客户端>";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, shareContent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
                break;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (fromWhere.equals(HomeActivity.class.getName())) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else if (fromWhere.equals(JobsOfFairActivity.class.getName())) {
            Intent intent = new Intent(this, JobsOfFairActivity.class);
            startActivity(intent);
        }
        //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}