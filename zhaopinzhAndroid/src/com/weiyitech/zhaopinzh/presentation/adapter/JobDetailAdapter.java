package com.weiyitech.zhaopinzh.presentation.adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import com.weiyitech.zhaopinzh.presentation.*;
import com.weiyitech.zhaopinzh.presentation.activity.EmployerDetailActivity;
import com.weiyitech.zhaopinzh.presentation.activity.HomeActivity;
import com.weiyitech.zhaopinzh.presentation.activity.JobDetailActivity;
import com.weiyitech.zhaopinzh.presentation.activity.JobsOfFairActivity;
import com.weiyitech.zhaopinzh.presentation.component.MessageProvider;
import com.weiyitech.zhaopinzh.struct.JobConciseRsp;
import com.weiyitech.zhaopinzh.struct.RunEmployer;
import com.weiyitech.zhaopinzh.struct.RunEmployerComponent;
import com.weiyitech.zhaopinzh.util.Common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-4-5
 * Time: 下午7:38
 * To change this template use File | Settings | File Templates.
 */
public class JobDetailAdapter extends BaseAdapter {
    List<Object> list;
    Context context;
    ArrayList<JobConciseRsp> jobConciseRsps;
    int inListIndex;
    public float companyRating;
    public int commentCnt;

    public JobDetailAdapter(Context context, List<Object> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<JobConciseRsp> getJobConciseRsps() {
        return jobConciseRsps;
    }

    public void setJobConciseRsps(ArrayList<JobConciseRsp> jobConciseRsps) {
        this.jobConciseRsps = jobConciseRsps;
    }

    public int getInListIndex() {
        return inListIndex;
    }

    public void setInListIndex(int inListIndex) {
        this.inListIndex = inListIndex;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater vi =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (position == 0) {
            v = vi.inflate(R.layout.job_detail_activity_job_concise_item, null);
            if (list.get(0) != null) {
                TextView jobNameTxtView = (TextView) v.findViewById(R.id.job_detail_activity_job_name_text_view);
                TextView demandNumberTxtView = (TextView) v.findViewById(R.id.job_detail_activity_demand_number);
                JobConciseRsp jobConciseRsp = (JobConciseRsp) list.get(0);
                jobNameTxtView.setText(jobConciseRsp.jobName);
                demandNumberTxtView.setText("招聘人数：" + (jobConciseRsp.demandNumber == 0 ? "不限" : jobConciseRsp.demandNumber));
            }
        } else if (position == 1) {
            v = vi.inflate(R.layout.job_detail_activity_company_item, null);
            if (list.get(1) != null) {
                final RunEmployerComponent runEmployerComponent = (RunEmployerComponent) list.get(1);
                TextView employerNameTxtView = (TextView) v.findViewById(R.id.job_detail_activity_employer_name);
                employerNameTxtView.setText(runEmployerComponent.runEmployer.employerName);
                TextView fairNameTxtView = (TextView) v.findViewById(R.id.job_detail_activity_fair_name);
                fairNameTxtView.setText(runEmployerComponent.runEmployer.fairName);
                TextView jobCountsTxtView = (TextView) v.findViewById(R.id.job_detail_activity_jobcounts);
                RatingBar companyRatingBar = (RatingBar) v.findViewById(R.id.job_detail_company_rating_bar);
                companyRatingBar.setRating(companyRating);
                TextView textView = (TextView) v.findViewById(R.id.job_detail_activity_cment_cnt_text_view);
                textView.setText("" + commentCnt);
                if (runEmployerComponent.runEmployer.jobCounts == 0) {
                    jobCountsTxtView.setText("发布职位：见现场海报");
                } else {
                    jobCountsTxtView.setText("发布职位：" + runEmployerComponent.runEmployer.jobCounts);
                }
                TextView runningTxtView = (TextView) v.findViewById(R.id.job_detail_activity_running_time);
                try {
                    Date date = null;
                    DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                    runningTxtView.setText("招聘时间：" + format1.format(format1.parse(runEmployerComponent.runEmployer.runningTime)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                TextView standTxtView = (TextView) v.findViewById(R.id.job_detail_activity_stand);
                String standStr = "展位号: ";
                for (int i = 0; i < runEmployerComponent.runEmployer.stands.size(); ++i) {
                    standStr += runEmployerComponent.runEmployer.stands.get(i).standNumber + " ";
                }
                standTxtView.setText(standStr);
                v.setTag(runEmployerComponent.runEmployer.employerId);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int employerId = (Integer) v.getTag();
                        Intent intent = new Intent();
                        intent.setClass(context, EmployerDetailActivity.class);
                        intent.putExtra("employerId", employerId);

                        intent.putExtra("where", JobDetailActivity.class.getName());
                        intent.putParcelableArrayListExtra("job_consice_rsp_list", getJobConciseRsps());
                        intent.putExtra("position", getInListIndex());
                        intent.putExtra("run_employer", ((RunEmployerComponent) list.get(1)).runEmployer);

                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    }
                });
                ImageButton imageButton = (ImageButton)v.findViewById(R.id.job_detail_activity_favor_image_view);
                if (runEmployerComponent.isFavorite) {
                    imageButton.setSelected(true);
                } else {
                    imageButton.setSelected(false);
                }
                imageButton.setTag(position);
                imageButton.setOnClickListener(new ImageButton.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImageButton favorImageButton = (ImageButton) view;
                        if (favorImageButton.isSelected()) {
                            runEmployerComponent.isFavorite = false;
                            favorImageButton.setSelected(false);
                            ContentResolver resolver = context.getContentResolver();
                            ContentValues contentValues = new ContentValues();
                            String mSelectionClause = "employer_id = CAST(? AS INT) AND fair_id = CAST(? AS INT)";
                            String[] mSelectionArgs = {String.valueOf(runEmployerComponent.runEmployer.employerId),
                                    String.valueOf(runEmployerComponent.runEmployer.fairId)};
                            int val = resolver.delete(MessageProvider.FAVOR_RUNEMPLOYER_URI, mSelectionClause, mSelectionArgs);
                            resolver.delete(MessageProvider.STAND_URI, mSelectionClause, mSelectionArgs);
                        } else {
                            runEmployerComponent.isFavorite = true;
                            favorImageButton.setSelected(true);
                            //存入数据库
                            ContentResolver resolver = context.getContentResolver();
                            ContentValues contentValues = new ContentValues();
                            Common.putRunEmployer(contentValues, runEmployerComponent.runEmployer);
                            resolver.insert(MessageProvider.FAVOR_RUNEMPLOYER_URI, contentValues);
                            if (runEmployerComponent.runEmployer.stands != null) {
                                for (int i = 0; i < runEmployerComponent.runEmployer.stands.size(); ++i) {
                                    ContentValues standContentValues = new ContentValues();
                                    standContentValues.put("employer_id", runEmployerComponent.runEmployer.stands.get(i).employerId);
                                    standContentValues.put("fair_id", runEmployerComponent.runEmployer.stands.get(i).fairId);
                                    standContentValues.put("stand_number", runEmployerComponent.runEmployer.stands.get(i).standNumber);
                                    resolver.insert(MessageProvider.STAND_URI, standContentValues);
                                }
                            }
                        }
                    }
                });
            }
        } else if (position < list.size() - 1) {
            v = vi.inflate(R.layout.job_detail_activity_list_item, null);
            String text = (String) list.get(position);
            TextView textView = (TextView) v.findViewById(R.id.job_detail_activity_item_text_view);
            textView.setText(text);
        } else {
            if (list.get(position) instanceof Boolean) {
                v = vi.inflate(R.layout.job_detail_activity_list_item, null);
                TextView textView = (TextView) v.findViewById(R.id.job_detail_activity_item_text_view);
                textView.setText("该公司其它职位");

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((JobDetailActivity) context).fromWhere.equals(HomeActivity.class.getName())) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            context.startActivity(intent);
                        } else if (((JobDetailActivity) context).fromWhere.equals(JobsOfFairActivity.class.getName())) {
                            Intent intent = new Intent(context, JobsOfFairActivity.class);
                            context.startActivity(intent);
                        }
                        ((JobDetailActivity) context).overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                    }
                });
            } else {
                v = vi.inflate(R.layout.job_detail_activity_list_item, null);
                String text = (String) list.get(position);
                TextView textView = (TextView) v.findViewById(R.id.job_detail_activity_item_text_view);
                textView.setText(text);
            }
        }
        return v;
    }
}
