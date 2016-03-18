package com.weiyitech.zhaopinzh.presentation.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import com.weiyitech.zhaopinzh.presentation.activity.HomeActivity;
import com.weiyitech.zhaopinzh.presentation.activity.JobsOfFairActivity;
import com.weiyitech.zhaopinzh.presentation.component.FairListFragment;
import com.weiyitech.zhaopinzh.struct.JobFairConcise;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.struct.SearchTerm;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.MyDatabaseUtil;
import com.weiyitech.zhaopinzh.util.cache.ImageLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-2-11
 * Time: 下午7:38
 * To change this template use File | Settings | File Templates.
 */
public class FairListAdapter extends BaseAdapter {
    ArrayList<JobFairConcise> jobFairConciseList;
    HomeActivity activity;
    public Boolean footViewExist = false;

    public static class ViewHolder {
        public View listItem;

        public TextView jobFairNameTxtView;
        public TextView hallNameTxtView;
        public TextView fairEmployersCountTxtView;
        public ImageView logoImgView;
        public TextView timeTxtView;
        public TextView dynamicTxtView;
        public ImageView detailImgView;
        public LinearLayout layout;
        public ImageView newImgView;
    }

    public FairListAdapter(Activity context, ArrayList<JobFairConcise> jobFairConciseList) {
        super();
        this.jobFairConciseList = jobFairConciseList;
        activity = (HomeActivity) context;
    }

    @Override
    public int getCount() {
        if (footViewExist) {
            return jobFairConciseList.size() + 1;
        } else {
            return jobFairConciseList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (position < jobFairConciseList.size()) {
            return jobFairConciseList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (position == jobFairConciseList.size()) {
            LayoutInflater vi =
                    (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.footer_view, null);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = (ImageView) v.findViewById(R.id.footer_view_image_view);
                    Animation operatingAnim = AnimationUtils.loadAnimation(activity, R.anim.rotate);
                    LinearInterpolator lin = new LinearInterpolator();
                    operatingAnim.setInterpolator(lin);
                    imageView.setAnimation(operatingAnim);
                    imageView.startAnimation(operatingAnim);
                    Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag("fairlist");
                    SearchTerm searchTerm = Common.getSearchSettings(activity);
                    ((FairListFragment) fragment).queryFairList(searchTerm);
                }
            });
            return v;
        }
        ViewHolder holder;
        LayoutInflater vi =
                (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.home_activity_fairfragment_list_item, null);
        //v.setBackgroundColor(Color.GRAY);
        holder = new ViewHolder();
        holder.listItem = v;
        holder.layout = (LinearLayout) v.findViewById(R.id.fair_fragment_item_layout);
        holder.jobFairNameTxtView = (TextView) v.findViewById(R.id.fair_fragment_jobfair_name_textview);
        holder.hallNameTxtView = (TextView) v.findViewById(R.id.fair_fragment_hall_name_text_view);
        holder.logoImgView = (ImageView) v.findViewById(R.id.fair_fragment_logo_image_view);
        holder.fairEmployersCountTxtView = (TextView) v.findViewById(R.id.fair_fragment_count_employers_text_view);
        holder.timeTxtView = (TextView) v.findViewById(R.id.fair_fragment_time_text_view);
        holder.dynamicTxtView = (TextView) v.findViewById(R.id.fair_fragment_dynamic_info_text_view);
        holder.detailImgView = (ImageView) v.findViewById(R.id.fair_fragment_navigate_image_view);
        holder.newImgView = (ImageView) v.findViewById(R.id.fair_fragment_new_image_view);
        v.setTag(holder);

        if (jobFairConciseList.get(position).isNew){
            holder.newImgView.setVisibility(View.VISIBLE);
        }else {
            holder.newImgView.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(jobFairConciseList.get(position).fairName)) {
            holder.jobFairNameTxtView.setText(jobFairConciseList.get(position).fairName);
            holder.jobFairNameTxtView.setVisibility(View.VISIBLE);
            if(jobFairConciseList.get(position).read){
                holder.jobFairNameTxtView.setTextColor(Color.LTGRAY);
            }
        } else {
            holder.jobFairNameTxtView.setVisibility(View.GONE);
        }

        if (jobFairConciseList.get(position).ownerLogoPath != null) {
            holder.logoImgView.setVisibility(View.VISIBLE);
            ImageLoader mImageLoader = new ImageLoader(activity);
            String url = Common.URL + "/" + jobFairConciseList.get(position).ownerLogoPath;
            mImageLoader.DisplayImage(url, holder.logoImgView, true, false);
        } else {
            holder.logoImgView.setVisibility(View.GONE);
        }

        /**
         * 把举办地隐藏
         */
//        if (!TextUtils.isEmpty(commentArrayList.get(position).hallName)) {
//            holder.hallNameTxtView.setText("举办地：" + commentArrayList.get(position).hallName);
//            holder.hallNameTxtView.setVisibility(View.VISIBLE);
//        } else {
//            holder.hallNameTxtView.setVisibility(View.GONE);
//        }
        String numStr;
        if(jobFairConciseList.get(position).employersCount == 0){
            numStr = "暂无数据";
        } else{
            numStr = "" + jobFairConciseList.get(position).employersCount;
        }
        holder.fairEmployersCountTxtView.setText("参会企业数：" + numStr);
        Date date = null;
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format1.parse(jobFairConciseList.get(position).runningTime);
            String runningTime = format1.format(date);
            if (!TextUtils.isEmpty(format1.format(date))) {
                holder.timeTxtView.setText("举办时间：" + runningTime);
                holder.timeTxtView.setVisibility(View.VISIBLE);
            } else {
                holder.timeTxtView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!TextUtils.isEmpty(jobFairConciseList.get(position).dynamicInfo)) {
            holder.dynamicTxtView.setText(jobFairConciseList.get(position).dynamicInfo);
            holder.dynamicTxtView.setVisibility(View.VISIBLE);
        } else {
            holder.dynamicTxtView.setVisibility(View.GONE);
        }
        holder.layout.setTag(jobFairConciseList.get(position));
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.fair_fragment_item_layout);
                JobFairConcise jobFairConcise = (JobFairConcise) linearLayout.getTag();
                jobFairConcise.read = true;
                intent.putExtra("fairName", jobFairConcise.fairName);
                intent.putExtra("fairId", jobFairConcise.fairId);
                intent.setClass(activity, JobsOfFairActivity.class);
                MyDatabaseUtil.putLocalFairIdsToReadDataBase(jobFairConcise.fairId, jobFairConcise.runningTime, activity);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        return v;

    }
}
