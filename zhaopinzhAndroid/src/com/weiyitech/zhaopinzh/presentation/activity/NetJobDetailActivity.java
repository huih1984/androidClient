package com.weiyitech.zhaopinzh.presentation.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.business.CommentBusiness;
import com.weiyitech.zhaopinzh.business.QueryNetJob;
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
public class NetJobDetailActivity extends CommonActivity implements BusinessInterface {
    JobConciseNetRsp jobConciseNetRsp;
    JobDetailNetRsp jobDetailNetRsp;
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
        public boolean isFavorite;

    }

    public static class Company {
        public int employerId;
        public String employerName;
        public int employerScale;
        public int employerIndustry;
        public int employerType;
    }

    public static class JobDesc {
        public String jobDesc;
        public String zhilianUpdateTime;
        public String job51UpdateTime;
        public String chinahrUpdateTime;
        public String zhilianUrl;
        public String job51Url;
        public String chinahrUrl;
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
    TextView commentCntTextView;
    MyWebView descWebView;
    RelativeLayout job51RelativeLayout;
    RelativeLayout zhilianRelativeLayout;
    RelativeLayout chinahrRelativeLayout;
    TextView job51UpdateTxtView;
    TextView zhilianUpdateTxtView;
    TextView chinahrUpdateTxtView;
    View companyItemView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.net_job_detail_activity);

        jobNameTextView = (TextView) findViewById(R.id.net_job_detail_jobname_text_view);
        paymentTextView = (TextView) findViewById(R.id.net_job_detail_payment_text_view);
        reqNumberTextView = (TextView) findViewById(R.id.net_job_detail_demandnumber_text_view);
        workPlaceTextView = (TextView) findViewById(R.id.net_job_detail_workplace_text_view);
        expReqTextView = (TextView) findViewById(R.id.net_job_detail_expreq_text_view);
        eduReqTextView = (TextView) findViewById(R.id.net_job_detail_edureq_text_view);
        sexReqTextView = (TextView) findViewById(R.id.net_job_detail_sexreq_text_view);
        favorImgButton = (ImageButton) findViewById(R.id.net_job_detail_favor_image_button);

        companyNameTextView = (TextView) findViewById(R.id.net_job_detail_company_employer_name);
        companyTypeTextView = (TextView) findViewById(R.id.net_job_detail_company_type);
        scaleTextView = (TextView) findViewById(R.id.net_job_detail_company_scale);
        industryTextView = (TextView) findViewById(R.id.net_job_detail_company_industry_text_view);
        commentCntTextView = (TextView) findViewById(R.id.net_job_detail_activity_cment_cnt_text_view);
        descWebView = (MyWebView) findViewById(R.id.net_job_detail_job_desc_web_view);
        companyItemView = findViewById(R.id.net_job_detail_company_item_layout);

        job51RelativeLayout = (RelativeLayout) findViewById(R.id.job51_layout);
        zhilianRelativeLayout = (RelativeLayout) findViewById(R.id.zhilian_layout);
        chinahrRelativeLayout = (RelativeLayout) findViewById(R.id.chinahr_layout);
        job51UpdateTxtView = (TextView) findViewById(R.id.job51_update_label_text_view);
        zhilianUpdateTxtView = (TextView) findViewById(R.id.zhilian_update_label_text_view);
        chinahrUpdateTxtView = (TextView) findViewById(R.id.chinahr_update_label_text_view);
        jobConciseNetRsp = getIntent().getParcelableExtra("data");
        QueryNetJob queryNetJob = new QueryNetJob();
        JobDetailNetReq jobDetailNetReq = new JobDetailNetReq();
        jobDetailNetReq.jobId = jobConciseNetRsp.jobId;
        queryNetJob.queryJobDetail(jobDetailNetReq, this);

        Comment comment = new Comment();
        comment.setEmployerId(jobConciseNetRsp.employerId);
        CommentBusiness commentBusiness = new CommentBusiness();
        commentBusiness.getEmployerAvarageDiamond(comment, this);

        CommentConcise commentConcise = new CommentConcise();
        commentConcise.employerId = jobConciseNetRsp.employerId;
        CommentUtil.queryCommentCnt(this, commentConcise);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int dataType = bundle.getInt("type");
            if (dataType == Common.COMMENT_AVARAGE_TYPE) {
                Comment comment = bundle.getParcelable("comment");
                ((RatingBar) findViewById(R.id.net_job_detail_company_rating_bar)).setRating(comment.getAvarageDiamond());
            } else if (dataType == Common.COMMENT_TOTAL_TYPE) {
                commentCntTextView.setText("" + ((CommentConcise)bundle.get("comment_concise")).total);
            }
        }
    };

    @Override
    public void getDataFromBusiness(int dataType, Object t, int... position) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        if (dataType == Common.NETJOBDETAIL_TYPE) {
            jobDetailNetRsp = (JobDetailNetRsp) t;
            title = new Title();
            company = new Company();
            jobDesc = new JobDesc();
            title.jobId = jobConciseNetRsp.jobId;
            title.jobName = jobConciseNetRsp.jobName;
            title.demandNumber = jobDetailNetRsp.demandNumber;
            title.eduReq = jobDetailNetRsp.eduReq;
            title.expReq = jobDetailNetRsp.expReq;
            title.sexReq = jobDetailNetRsp.sexReq;
            title.payment = jobConciseNetRsp.payment;
            title.workplace = jobConciseNetRsp.workplace;
            title.isFavorite = jobConciseNetRsp.isFavorite;

            company.employerName = jobConciseNetRsp.employerName;
            company.employerIndustry = jobConciseNetRsp.industry;
            company.employerScale = jobConciseNetRsp.employerScale;
            company.employerType = jobConciseNetRsp.employerType;
            company.employerId = jobConciseNetRsp.employerId;

            jobDesc.jobDesc = jobDetailNetRsp.jobDesc;
            jobDesc.job51UpdateTime = jobDetailNetRsp.job51UpdateTime;
            jobDesc.zhilianUpdateTime = jobDetailNetRsp.zhilianUpdateTime;
            jobDesc.chinahrUpdateTime = jobDetailNetRsp.chinahrUpdateTime;
            jobDesc.job51Url = jobDetailNetRsp.job51Url;
            jobDesc.zhilianUrl = jobDetailNetRsp.zhilianUrl;
            jobDesc.chinahrUrl = jobDetailNetRsp.chinahrUrl;
            setJobItemTextView();
            setCompanyItemTextView();
            setDescTextView();
        } else if (dataType == Common.COMMENT_AVARAGE_TYPE) {
            bundle.putInt("type", Common.COMMENT_AVARAGE_TYPE);
            bundle.putParcelable("comment", (Comment) t);
        } else if(dataType == Common.COMMENT_TOTAL_TYPE){
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
                    int val = resolver.delete(MessageProvider.FAVOR_NET_JOB_DETAIL_URI, mSelectionClause, mSelectionArgs);
                } else {
                    title.isFavorite = true;
                    favorImageButton.setSelected(true);
                    //存入数据库
                    ContentResolver resolver = getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    Common.putNetJobDetail(contentValues, jobConciseNetRsp);
                    resolver.insert(MessageProvider.FAVOR_NET_JOB_DETAIL_URI, contentValues);
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

            companyItemView.setTag(company.employerId);
            companyItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int employerId = (Integer) v.getTag();
                    Intent intent = new Intent();
                    intent.setClass(NetJobDetailActivity.this, EmployerDetailActivity.class);
                    intent.putExtra("employerId", employerId);
                    intent.putExtra("where", NetJobDetailActivity.class.getName());
                    NetJobDetailActivity.this.startActivity(intent);
                    NetJobDetailActivity.this.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setDescTextView() {
        descWebView.setData(jobDesc.jobDesc);
        if (jobDesc.job51Url != null && jobDesc.job51Url.length() > 0) {
            job51RelativeLayout.setVisibility(View.VISIBLE);
            job51UpdateTxtView.setText("更新：\n" + jobDesc.job51UpdateTime.substring(0, 10));
            ImageButton imageButton = (ImageButton) findViewById(R.id.job51_img_btn);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = jobDesc.job51Url;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            });
        } else {
            job51RelativeLayout.setVisibility(View.GONE);
        }
        if (jobDesc.zhilianUrl != null && jobDesc.zhilianUrl.length() > 0) {
            zhilianRelativeLayout.setVisibility(View.VISIBLE);
            zhilianUpdateTxtView.setText("更新：\n" + jobDesc.zhilianUpdateTime.substring(0, 10));
            ImageButton imageButton = (ImageButton) findViewById(R.id.zhilian_img_btn);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(jobDesc.zhilianUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        } else {
            zhilianRelativeLayout.setVisibility(View.GONE);
        }
        if (jobDesc.chinahrUrl != null && jobDesc.chinahrUrl.length() > 0) {
            chinahrRelativeLayout.setVisibility(View.VISIBLE);
            chinahrUpdateTxtView.setText("更新：\n" + jobDesc.chinahrUpdateTime.substring(0, 10));
            ImageButton imageButton = (ImageButton) findViewById(R.id.chinahr_img_btn);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(jobDesc.chinahrUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        } else {
            chinahrRelativeLayout.setVisibility(View.GONE);
        }
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
                Intent intent = new Intent(this, TalkActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;
            case R.id.net_job_detail_activity_share:
                String urlStr = null;
                if (jobDetailNetRsp != null) {
                    if (jobDetailNetRsp.zhilianUrl != null && jobDetailNetRsp.zhilianUrl.length() > 0) {
                        urlStr = "\n智联职位：" + jobDetailNetRsp.zhilianUrl;
                    }
                    if (jobDetailNetRsp.job51Url != null && jobDetailNetRsp.job51Url.length() > 0) {
                        urlStr += "\n前程无忧：" + jobDetailNetRsp.job51Url;
                    }
                    if (jobDetailNetRsp.chinahrUrl != null && jobDetailNetRsp.chinahrUrl.length() > 0) {
                        urlStr += "\n中华英才网：" + jobDetailNetRsp.chinahrUrl;
                    }
                    String shareContent = "职位：" + jobConciseNetRsp.jobName +
                            "\n企业：" + jobConciseNetRsp.employerName +
                            (urlStr != null ? urlStr : "") +
                            "\n __来自<南京招聘汇APP客户端>";
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                    intent.putExtra(Intent.EXTRA_TEXT, shareContent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Intent.createChooser(intent, getTitle()));
                }   else{
                    Toast.makeText(this, "没能获取数据，可能网络出现异常，请检查您的网络是否连接。", 1000).show();
                }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, TalkActivity.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}