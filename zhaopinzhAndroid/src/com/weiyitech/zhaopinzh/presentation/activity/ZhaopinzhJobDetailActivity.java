package com.weiyitech.zhaopinzh.presentation.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.business.CommentBusiness;
import com.weiyitech.zhaopinzh.business.QueryZhaopinzhJob;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.component.MessageProvider;
import com.weiyitech.zhaopinzh.struct.*;
import com.weiyitech.zhaopinzh.util.CommentUtil;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.webview.MyWebView;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-8-1
 * Time: 下午8:00
 * To change this template use File | Settings | File Templates.
 */
public class ZhaopinzhJobDetailActivity extends CommonActivity implements BusinessInterface {
    int jobId;
    Title title;
    Company company;
    JobDesc jobDesc;


    public static class Title {
        public int jobId;
        public String jobName;
        public String payment;
        public int demandNumber;
        public String workplace;
        public String expReq;
        public String eduReq;
        public String sexReq;
        public String publishTime;
        public String deadTime;
        public boolean isFavorite;

    }

    public static class Company {
        public int employerId;
        public String employerName;
        public int employerScale;
        public int employerIndustry;
        public int employerType;
        public String hrEmail;
        public String tel;
    }

    public static class JobDesc {
        public String jobDesc;
    }

    TextView jobNameTextView;
    TextView paymentTextView;
    TextView reqNumberTextView;
    TextView workPlaceTextView;
    TextView expReqTextView;
    TextView eduReqTextView;
    TextView sexReqTextView;
    ImageButton favorImgButton;

    TextView companyNameTextView;
    TextView companyTypeTextView;
    TextView scaleTextView;
    TextView industryTextView;
    TextView hrEmailTextView;
    TextView telTextView;
    TextView commentCntTextView;
    TextView publishTimeTextView;
    TextView deadTimeTextView;


    MyWebView descWebView;
    View companyItemView;

    JobDetailZhaopinzh jobDetailZhaopinzh;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhaopinzh_job_detail_activity);

        jobNameTextView = (TextView) findViewById(R.id.zhaopinzh_job_detail_jobname_text_view);
        paymentTextView = (TextView) findViewById(R.id.zhaopinzh_job_detail_payment_text_view);
        reqNumberTextView = (TextView) findViewById(R.id.zhaopinzh_job_detail_demandnumber_text_view);
        workPlaceTextView = (TextView) findViewById(R.id.zhaopinzh_job_detail_workplace_text_view);
        expReqTextView = (TextView) findViewById(R.id.zhaopinzh_job_detail_expreq_text_view);
        eduReqTextView = (TextView) findViewById(R.id.zhaopinzh_job_detail_edureq_text_view);
        sexReqTextView = (TextView) findViewById(R.id.zhaopinzh_job_detail_sexreq_text_view);
        favorImgButton = (ImageButton) findViewById(R.id.zhaopinzh_job_detail_favor_image_button);

        companyNameTextView = (TextView) findViewById(R.id.zhaopinzh_job_detail_company_employer_name);
        companyTypeTextView = (TextView) findViewById(R.id.zhaopinzh_job_detail_company_type);
        scaleTextView = (TextView) findViewById(R.id.zhaopinzh_job_detail_company_scale);
        industryTextView = (TextView) findViewById(R.id.zhaopinzh_job_detail_company_industry_text_view);
        hrEmailTextView = (TextView) findViewById(R.id.zhaopinzh_job_detail_hr_email_text_view);
        telTextView = (TextView) findViewById(R.id.zhaopinzh_job_detail_tel_text_view);
        commentCntTextView = (TextView) findViewById(R.id.zhaopinzh_job_detail_activity_cment_cnt_text_view);


        descWebView = (MyWebView) findViewById(R.id.zhaopinzh_job_detail_job_desc_web_view);
        publishTimeTextView = (TextView) findViewById(R.id.zhaopinzh_job_detail_publish_time_text_view);
        deadTimeTextView = (TextView) findViewById(R.id.zhaopinzh_job_detail_dead_time_text_view);
        companyItemView = findViewById(R.id.zhaopinzh_job_detail_company_item_layout);

        JobDetailZhaopinzh jobDetailZhaopinzh = (JobDetailZhaopinzh) getIntent().getParcelableExtra("data");
        if (jobDetailZhaopinzh == null) {
            Log.e("ZhaopinzhJobDetailActivity", "从未知页面传入，未获取职位概况信息");
        } else {
            company = new Company();
            company.employerName = jobDetailZhaopinzh.employerName;
            company.employerIndustry = jobDetailZhaopinzh.industry;
            company.employerScale = jobDetailZhaopinzh.employerScale;
            company.employerType = jobDetailZhaopinzh.employerType;
            company.employerId = jobDetailZhaopinzh.employerId;
            company.hrEmail = jobDetailZhaopinzh.hrEmail;
            company.tel = jobDetailZhaopinzh.tel;

            QueryZhaopinzhJob queryZhaopinzhJob = new QueryZhaopinzhJob();
            queryZhaopinzhJob.queryJobDetail(jobDetailZhaopinzh, this);
            Comment comment = new Comment();
            comment.setEmployerId(jobDetailZhaopinzh.employerId);
            CommentBusiness commentBusiness = new CommentBusiness();
            commentBusiness.getEmployerAvarageDiamond(comment, this);

            CommentConcise commentConcise = new CommentConcise();
            commentConcise.employerId = jobDetailZhaopinzh.employerId;
            CommentUtil.queryCommentCnt(this, commentConcise);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int dataType = bundle.getInt("type");
            if (dataType == Common.COMMENT_AVARAGE_TYPE) {
                Comment comment = bundle.getParcelable("comment");
                ((RatingBar) findViewById(R.id.zhaopinzh_job_detail_company_rating_bar)).setRating(comment.getAvarageDiamond());
            }  else if (dataType == Common.COMMENT_TOTAL_TYPE) {
                commentCntTextView.setText("" + ((CommentConcise)bundle.get("comment_concise")).total);
            }
        }
    };

    @Override
    public void getDataFromBusiness(int dataType, Object t, int... position) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        if (dataType == Common.ZHAOPINZH_JOB_DETAIL_TYPE) {
            jobDetailZhaopinzh = (JobDetailZhaopinzh) t;
            title = new Title();
            jobDesc = new JobDesc();
            title.jobId = jobDetailZhaopinzh.jobId;
            title.jobName = jobDetailZhaopinzh.jobName;
            title.demandNumber = jobDetailZhaopinzh.demandNumber;
            title.eduReq = jobDetailZhaopinzh.eduReq;
            title.expReq = jobDetailZhaopinzh.expReq;
            title.sexReq = jobDetailZhaopinzh.sexReq;
            title.payment = jobDetailZhaopinzh.payment;
            title.workplace = jobDetailZhaopinzh.workplace;
            title.isFavorite = jobDetailZhaopinzh.isFavorite;
            title.deadTime = jobDetailZhaopinzh.deadTime;
            title.publishTime = jobDetailZhaopinzh.publishTime;



            jobDesc.jobDesc = jobDetailZhaopinzh.jobDesc;
            setJobItemTextView();
            setCompanyItemTextView();
            setDescTextView();
        } else if (dataType == Common.COMMENT_AVARAGE_TYPE) {
            bundle.putInt("type", Common.COMMENT_AVARAGE_TYPE);
            bundle.putParcelable("comment", (Comment) t);
        }else if(dataType == Common.COMMENT_TOTAL_TYPE){
            bundle.putInt("type", Common.COMMENT_TOTAL_TYPE);
            bundle.putParcelable("comment_concise", (CommentConcise) t);
        }

        message.setData(bundle);
        handler.sendMessage(message);
    }

    void setJobItemTextView() {
        jobNameTextView.setText(title.jobName);
        if (title.payment != null && !"".equals(title.payment)) {
            paymentTextView.setText("薪资：" + title.payment);
        } else {
            paymentTextView.setVisibility(View.GONE);
        }
        reqNumberTextView.setText("招聘人数：" + (title.demandNumber == 0 ? "不限" : title.demandNumber));
        if (title.workplace != null && !"".equals(title.workplace)) {
            workPlaceTextView.setText("工作地点：" + title.workplace);
        } else {
            workPlaceTextView.setVisibility(View.GONE);
        }
        if (title.expReq != null && !"".equals(title.expReq)) {
            expReqTextView.setText("经验要求：" + title.expReq);
        } else {
            expReqTextView.setVisibility(View.GONE);
        }
        if (title.eduReq != null && !"".equals(title.eduReq)) {
            eduReqTextView.setText("学历要求：" + title.eduReq);
        } else {
            eduReqTextView.setVisibility(View.GONE);
        }
        if (title.sexReq != null && !"".equals(title.sexReq)) {
            sexReqTextView.setText("性别要求：" + title.sexReq);
        } else {
            sexReqTextView.setVisibility(View.GONE);
        }
        if (title.publishTime != null && !"".equals(title.publishTime)){
            publishTimeTextView.setText("发布时间：" + Common.formatTime(title.publishTime, "yyyy-MM-dd HH:mm:ss", "MM月/dd日"));
        } else {
            publishTimeTextView.setVisibility(View.GONE);
        }
        if (title.deadTime != null && !"".equals(title.deadTime)){
            deadTimeTextView.setText("截止时间：" +  Common.formatTime(title.deadTime, "yyyy-MM-dd HH:mm:ss", "MM月/dd日"));
        } else {
            deadTimeTextView.setVisibility(View.GONE);
        }
        favorImgButton.setSelected(title.isFavorite);
        favorImgButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton favorImageButton = (ImageButton) view;
                if (favorImageButton.isSelected()) {
                    title.isFavorite = false;
                    favorImageButton.setSelected(false);
                    ContentResolver resolver = getContentResolver();
                    String mSelectionClause = "job_id = CAST(? AS INT)";
                    String[] mSelectionArgs = {String.valueOf(title.jobId)};
                    int val = resolver.delete(MessageProvider.FAVOR_ZHAOPINZH_JOB_DETAIL_URI, mSelectionClause, mSelectionArgs);
                } else {
                    title.isFavorite = true;
                    favorImageButton.setSelected(true);
                    //存入数据库
                    ContentResolver resolver = getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    Common.putZhaopinzhJobDetail(contentValues, jobDetailZhaopinzh);
                    resolver.insert(MessageProvider.FAVOR_ZHAOPINZH_JOB_DETAIL_URI, contentValues);
                }
            }
        });
    }

    void setCompanyItemTextView() {
        ZhaopinzhApp zhaopinzhApp = ZhaopinzhApp.getInstance();
        try {
            companyNameTextView.setText(company.employerName);
            if (company.employerScale > 0 && company.employerScale < 7) {
                scaleTextView.setText("企业规模：" + zhaopinzhApp.companyScaleList.get(company.employerScale));
            } else {
                scaleTextView.setVisibility(View.GONE);
            }
            if (company.employerIndustry > 0 && company.employerIndustry < 38) {
                industryTextView.setText("行业：" + zhaopinzhApp.industryList.get(company.employerIndustry));
            } else {
                industryTextView.setVisibility(View.GONE);
            }
            if (company.employerType > 0 && company.employerType < 10) {
                companyTypeTextView.setText("性质：" + zhaopinzhApp.companyTypeList.get(company.employerType));
            } else {
                companyTypeTextView.setVisibility(View.GONE);
            }
            if (company.hrEmail != null && !"".equals(company.hrEmail) && !"NULL".equals(company.hrEmail)){
                hrEmailTextView.setText("HR邮箱：" + company.hrEmail);
            } else {
                hrEmailTextView.setVisibility(View.GONE);
            }
            if (company.tel != null && !"".equals(company.tel) && !"NULL".equals(company.tel)){
                telTextView.setText("联系电话：" + company.tel);
            } else {
                telTextView.setVisibility(View.GONE);
            }

            companyItemView.setTag(company.employerId);
            companyItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int employerId = (Integer) v.getTag();
                    Intent intent = new Intent();
                    intent.setClass(ZhaopinzhJobDetailActivity.this, EmployerDetailActivity.class);
                    intent.putExtra("employerId", employerId);
                    intent.putExtra("where", ZhaopinzhJobDetailActivity.class.getName());
                    ZhaopinzhJobDetailActivity.this.startActivity(intent);
                    ZhaopinzhJobDetailActivity.this.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setDescTextView() {
        descWebView.setData(jobDesc.jobDesc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.net_job_detail_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, ZhaopinzhJobConciseListActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;
            case R.id.net_job_detail_activity_share:
                String urlStr = null;
                String shareContent = "职位：" + jobDetailZhaopinzh.jobName +
                        "\n企业：" + jobDetailZhaopinzh.employerName +
                        (urlStr != null ? urlStr : "") +
                        "\n __来自<南京招聘汇>";
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, shareContent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ZhaopinzhJobConciseListActivity.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}