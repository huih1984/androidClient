package com.weiyitech.zhaopinzh.presentation.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.business.CommentBusiness;
import com.weiyitech.zhaopinzh.business.QueryJob;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.component.MessageProvider;
import com.weiyitech.zhaopinzh.struct.*;
import com.weiyitech.zhaopinzh.util.CommentUtil;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.webview.MyWebView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-4-6
 * Time: 下午5:11
 * To change this template use File | Settings | File Templates.
 */
public class EmployerDetailActivity extends CommonActivity implements BusinessInterface {
    List<Object> list;
    int employerId;
    String fromWhere;
    RunEmployer runEmployer;
    ArrayList<JobConciseRsp> jobConciseRsps;
    int position;
    TextView nameTextView;
    TextView addressTextView;
    TextView websiteTextView;
    ImageButton commentButton;
    TextView commentTotalTextView;
    EmployerDetailRsp mEmployerDetailRsp;
    MyWebView descWebView;

    RatingBar companyRatingBar;
    RatingBar companyExpactationRatingBar;
    RatingBar techGrowingRatingBar;
    RatingBar occupationGrowingRatingBar;
    RatingBar workpressRatingBar;
    RatingBar workEnviromentRatingBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employer_detail_activity);

        nameTextView = (TextView) findViewById(R.id.employer_detail_activity_employer_name_text_view);
        addressTextView = (TextView) findViewById(R.id.employer_detail_activity_adress);
        websiteTextView = (TextView) findViewById(R.id.web_site_txt_view);
        commentButton = (ImageButton) findViewById(R.id.comment_btn);
        commentTotalTextView = (TextView) findViewById(R.id.comment_num);
        descWebView = (MyWebView) findViewById(R.id.employer_detail_activity_item_web_view);

        companyRatingBar = (RatingBar) findViewById(R.id.company_rating_bar);
        occupationGrowingRatingBar = (RatingBar) findViewById(R.id.occupation_growing_rating_bar);
        techGrowingRatingBar = (RatingBar) findViewById(R.id.tech_growing_rating_bar);
        workEnviromentRatingBar = (RatingBar) findViewById(R.id.work_enviroment_rating_bar);
        workpressRatingBar = (RatingBar) findViewById(R.id.work_press_rating_bar);
        companyExpactationRatingBar = (RatingBar) findViewById(R.id.company_futrue_rating_bar);

        employerId = getIntent().getIntExtra("employerId", 0);
        fromWhere = getIntent().getStringExtra("where");
//        if (fromWhere.equals(JobDetailActivity.class.getName())) {
//            runEmployer = getIntent().getParcelableExtra("run_employer");
//            jobConciseRsps = getIntent().getParcelableArrayListExtra("job_consice_rsp_list");
//            position = (Integer) getIntent().getExtras().get("position");
//        }


        String selection = "employer_id = CAST(? AS INT)";
        String[] selectionArgs = {String.valueOf(employerId)};
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(MessageProvider.EMPLOYER_URI, null, selection, selectionArgs, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                mEmployerDetailRsp = new EmployerDetailRsp();
                mEmployerDetailRsp.employerId = cursor.getInt(cursor.getColumnIndex("employer_id"));
                mEmployerDetailRsp.employerName = cursor.getString(cursor.getColumnIndex("employer_name"));
                mEmployerDetailRsp.type = cursor.getInt(cursor.getColumnIndex("type"));
                mEmployerDetailRsp.scale = cursor.getInt(cursor.getColumnIndex("scale"));
                mEmployerDetailRsp.hrEmail = cursor.getString(cursor.getColumnIndex("hr_email"));
                mEmployerDetailRsp.desc = cursor.getString(cursor.getColumnIndex("desc"));
                mEmployerDetailRsp.address = cursor.getString(cursor.getColumnIndex("address"));
                mEmployerDetailRsp.website = cursor.getString(cursor.getColumnIndex("website"));
                mEmployerDetailRsp.province = cursor.getString(cursor.getColumnIndex("province"));
                mEmployerDetailRsp.city = cursor.getString(cursor.getColumnIndex("city"));
                mEmployerDetailRsp.tel = cursor.getString(cursor.getColumnIndex("tel"));
                mEmployerDetailRsp.logoPath = cursor.getString(cursor.getColumnIndex("logo_path"));
                mEmployerDetailRsp.bigLogoPath = cursor.getString(cursor.getColumnIndex("big_logo_path"));
                mEmployerDetailRsp.industry = cursor.getInt(cursor.getColumnIndex("industry"));
                setEmployerViewValues(mEmployerDetailRsp);
            }

        } else {
            QueryJob queryJob = new QueryJob();
            EmployerDetailReq employerDetailReq = new EmployerDetailReq();
            employerDetailReq.employerId = employerId;
            queryJob.queryEmployerDetail(employerDetailReq, this);
        }

        CommentConcise commentConcise = new CommentConcise();
        commentConcise.employerId = employerId;
        CommentBusiness commentBusiness = new CommentBusiness();
        CommentUtil.queryCommentCnt(this, commentConcise);
        commentBusiness.getEmployerRating(commentConcise, this);
    }


    void setEmployerViewValues(final EmployerDetailRsp employerDetailRsp) {

        if (employerDetailRsp.website != null && employerDetailRsp.website.contains(".")) {
            websiteTextView.setText(employerDetailRsp.website);
        }
        websiteTextView.setAutoLinkMask(Linkify.ALL);
        websiteTextView.setMovementMethod(LinkMovementMethod.getInstance());
        if (employerDetailRsp.employerName != null && !employerDetailRsp.employerName.equalsIgnoreCase("NULL"))
            nameTextView.setText(employerDetailRsp.employerName);
        if (employerDetailRsp.address != null && !employerDetailRsp.address.equalsIgnoreCase("NULL"))
            addressTextView.setText(employerDetailRsp.address);
        if (employerDetailRsp.website != null && !employerDetailRsp.website.equalsIgnoreCase("NULL"))
            websiteTextView.setText(employerDetailRsp.website);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(EmployerDetailActivity.this, CommentActivity.class);
                intent.putExtra("employerId", employerDetailRsp.employerId);
                intent.putExtra("employerName", employerDetailRsp.employerName);
                EmployerDetailActivity.this.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                EmployerDetailActivity.this.startActivity(intent);
            }
        });
        if (employerDetailRsp.desc != null && !employerDetailRsp.desc.equalsIgnoreCase("NULL"))
            descWebView.setData(employerDetailRsp.desc);
        else
            descWebView.setData("还没有这家公司的介绍哦！");
    }

    void setCommentTotalViewValue(int total) {
        commentTotalTextView.setText(String.valueOf(total));
    }


    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int dataType = bundle.getInt("type");
            if (dataType == Common.RATING_TYPE) {
                Comment comment = bundle.getParcelable("comment");

                companyRatingBar.setRating(comment.getAvarageDiamond());
                occupationGrowingRatingBar.setRating(comment.getOccupationGrowing());
                techGrowingRatingBar.setRating(comment.getTechnicalGrowing());
                workEnviromentRatingBar.setRating(comment.getWorkEnviroment());
                workpressRatingBar.setRating(comment.getWorkPress());
                companyExpactationRatingBar.setRating(comment.getCompanyExpectation());
            } else if (dataType == Common.COMMENT_TOTAL_TYPE) {
                CommentConcise commentConcise = bundle.getParcelable("comment_concise");
                setCommentTotalViewValue(commentConcise.total);
            }
        }
    };

    @Override
    public void getDataFromBusiness(int dataType, Object t, int... position) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        if (dataType == Common.EMPLOYERDETAIL_TYPE) {
            mEmployerDetailRsp = (EmployerDetailRsp) t;
            ContentValues contentValues = new ContentValues();
            Common.putEmployer(contentValues, mEmployerDetailRsp);
            ContentResolver resolver = getContentResolver();
            resolver.insert(MessageProvider.EMPLOYER_URI, contentValues);
            setEmployerViewValues(mEmployerDetailRsp);
        } else if (dataType == Common.RATING_TYPE) {
            Comment comment = (Comment) t;
            bundle.putInt("type", Common.RATING_TYPE);
            bundle.putParcelable("comment", comment);
        } else if (dataType == Common.COMMENT_TOTAL_TYPE) {
            bundle.putInt("type", Common.COMMENT_TOTAL_TYPE);
            bundle.putParcelable("comment_concise", (CommentConcise) t);
        }

        message.setData(bundle);
        handler.sendMessage(message);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                goHome();
                break;
        }
        return true;
    }

    void goHome() {
        Intent intent = new Intent();
        if (fromWhere.equals(NetJobDetailActivity.class.getName())) {
            intent.setClass(this, NetJobDetailActivity.class);
        } else if (fromWhere.equals(ZhaopinzhJobDetailActivity.class.getName())) {
            intent.setClass(this, ZhaopinzhJobDetailActivity.class);
        } else {
            intent.setClass(this, JobDetailActivity.class);
        }
//        intent.putExtra("where", fromWhere);
//        if (fromWhere.equals(JobDetailActivity.class.getName())) {
//            intent.putParcelableArrayListExtra("job_consice_rsp_list", jobConciseRsps);
//            intent.putExtra("position", position);
//            intent.putExtra("run_employer", runEmployer);
//        }
        startActivity(intent);
    }

    public void onBackPressed() {
        goHome();
    }
}