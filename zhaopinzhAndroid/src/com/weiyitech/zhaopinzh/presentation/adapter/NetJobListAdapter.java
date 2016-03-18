package com.weiyitech.zhaopinzh.presentation.adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.presentation.*;
import com.weiyitech.zhaopinzh.presentation.activity.NetJobConciseListActivity;
import com.weiyitech.zhaopinzh.presentation.activity.NetJobDetailActivity;
import com.weiyitech.zhaopinzh.presentation.component.MessageProvider;
import com.weiyitech.zhaopinzh.struct.JobConciseNetRsp;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.cache.ImageLoader;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-7-28
 * Time: 上午11:34
 * To change this template use File | Settings | File Templates.
 */
public class NetJobListAdapter  extends BaseAdapter {
    public ArrayList<JobConciseNetRsp> jobConciseNetRsps;
    public NetJobConciseListActivity activity;
    public boolean mBusy = false;
    public ImageLoader mImageLoader;
    public int pageIndex = 0;

    public NetJobListAdapter(Activity context, ArrayList<JobConciseNetRsp> jobConciseNetRsps, int pageIndex) {
        super();
        this.jobConciseNetRsps = jobConciseNetRsps;
        activity = (NetJobConciseListActivity) context;
        mImageLoader = new ImageLoader(context);
        this.pageIndex = pageIndex;
    }

    public void setFlagBusy(boolean busy) {
        this.mBusy = busy;
    }

    /**
     * Define the ViewHolder for our adapter
     */
    public static class ViewHolder {
        public View listItem;

        public RelativeLayout conciseLayout;
        public ImageView employerLogoImgView;
        public TextView timeTextView;
        public TextView employerNameTxtView;
        public TextView jobNameTxtView;
        public TextView jobCountsTxtView;
        public TextView paymentTxtView;
        public TextView workplaceTxtView;
        public TextView readTimesTextView;
        public ImageButton favoriteImgBtn;
        public TextView commentCntTextView;

        public LinearLayout expandLayout;
        public Context context;
        public JobConciseNetRsp jobConciseNetRsp;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ---------------
        // Boilerplate view inflation and ViewHolder code
        // ---------------
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater vi =
                    (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.net_job_fragment_list_item, null);
            holder = new ViewHolder();
            holder.context = activity;
            holder.listItem = v;
            holder.conciseLayout = (RelativeLayout) v.findViewById(R.id.net_job_fragment_concise);
            holder.employerNameTxtView = (TextView) v.findViewById(R.id.net_job_fragment_employer_name);
            holder.jobNameTxtView = (TextView)v.findViewById(R.id.net_job_fragment_job_name);
            holder.paymentTxtView = (TextView)v.findViewById(R.id.net_job_fragment_payment_text_view);
            holder.workplaceTxtView = (TextView)v.findViewById(R.id.net_job_fragment_workplace_text_view);
            holder.readTimesTextView = (TextView) v.findViewById(R.id.net_job_fragment_read_times_text_view);
            holder.favoriteImgBtn = (ImageButton) v.findViewById(R.id.net_job_fragment_favorite);
            holder.commentCntTextView = (TextView) v.findViewById(R.id.net_job_fragment_net_cment_cnt_text_view);
            holder.timeTextView = (TextView) v.findViewById(R.id.net_job_fragment_time_text_view);
            holder.expandLayout = (LinearLayout) v.findViewById(R.id.expandable);
            v.setTag(holder);
        } else holder = (ViewHolder) v.getTag();


        holder.jobConciseNetRsp = jobConciseNetRsps.get(position);
        holder.jobConciseNetRsp.position = position;

        String bigTime = Common.bigTime(holder.jobConciseNetRsp.job51UpdateTime, holder.jobConciseNetRsp.chinahrUpdateTime, "yyyy-MM-dd HH:mm:ss");
        bigTime = Common.bigTime(bigTime, holder.jobConciseNetRsp.zhilianUpdateTime, "yyyy-MM-dd HH:mm:ss");
        holder.timeTextView.setText(Common.formatTime(bigTime, "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd"));

        holder.conciseLayout.setTag(holder.jobConciseNetRsp);
        holder.conciseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentResolver resolver = activity.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put("job_id", ((JobConciseNetRsp)v.getTag()).jobId);
                resolver.insert(MessageProvider.READ_NET_JOB_DETAIL_URI, contentValues);
                JobConciseNetRsp jobConciseNetRsp = (JobConciseNetRsp)v.getTag();
                Intent intent = new Intent(activity, NetJobDetailActivity.class);
                intent.putExtra("data", jobConciseNetRsp);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });
        //根据阅读情况确定是否着色标记
        ContentResolver resolver = activity.getContentResolver();
        String selection = "job_id = CAST(? AS INT) ";
        String[] selectionArgs = {String.valueOf(holder.jobConciseNetRsp.jobId)};
        Cursor cursor = null;
        cursor = resolver.query(MessageProvider.READ_NET_JOB_DETAIL_URI, null, selection, selectionArgs, null);
        if(cursor != null){
            cursor.close();
            cursor = null;
            holder.jobNameTxtView.setTextColor(holder.readTimesTextView.getTextColors());
        }else {
            holder.jobNameTxtView.setTextColor(Color.rgb(0,0,0));
        }

        holder.favoriteImgBtn.setTag(position);
        holder.favoriteImgBtn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton favorImageButton = (ImageButton) view;
                int position = (Integer) view.getTag();
                if (favorImageButton.isSelected()) {
                    NetJobListAdapter.this.jobConciseNetRsps.get(position).isFavorite = false;
                    favorImageButton.setSelected(false);
                    ContentResolver resolver = activity.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    String mSelectionClause = "job_id = CAST(? AS INT)";
                    String[] mSelectionArgs = {String.valueOf(NetJobListAdapter.this.jobConciseNetRsps.get(position).jobId)};
                    int val = resolver.delete(MessageProvider.FAVOR_NET_JOB_DETAIL_URI, mSelectionClause, mSelectionArgs);
                } else {
                    NetJobListAdapter.this.jobConciseNetRsps.get(position).isFavorite = true;
                    favorImageButton.setSelected(true);
                    //存入数据库
                    ContentResolver resolver = activity.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    Common.putNetJobDetail(contentValues, NetJobListAdapter.this.jobConciseNetRsps.get(position));
                    resolver.insert(MessageProvider.FAVOR_NET_JOB_DETAIL_URI, contentValues);
                }
            }
        });

        //根据阅读情况确定是否着色标记
        cursor = resolver.query(MessageProvider.FAVOR_NET_JOB_DETAIL_URI, null, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.close();
            holder.jobConciseNetRsp.isFavorite = true;
        } else {
            holder.jobConciseNetRsp.isFavorite = false;
        }
        if (holder.jobConciseNetRsp.isFavorite) {
            holder.favoriteImgBtn.setSelected(true);
        } else {
            holder.favoriteImgBtn.setSelected(false);
        }

        if (holder.jobConciseNetRsp != null) {
            ZhaopinzhApp app = (ZhaopinzhApp) activity.getApplicationContext();
            if (!TextUtils.isEmpty(holder.jobConciseNetRsp.employerName)) {
                holder.employerNameTxtView.setText(holder.jobConciseNetRsp.employerName);
                holder.employerNameTxtView.setVisibility(View.VISIBLE);
            } else {
                holder.employerNameTxtView.setVisibility(View.GONE);
            }

            if(!TextUtils.isEmpty(holder.jobConciseNetRsp.jobName)){
                holder.jobNameTxtView.setText(holder.jobConciseNetRsp.jobName);
                holder.jobNameTxtView.setVisibility(View.VISIBLE);
            } else {
                holder.jobNameTxtView.setVisibility(View.GONE);
            }

            if(!TextUtils.isEmpty(holder.jobConciseNetRsp.payment)){
                holder.paymentTxtView.setText(holder.jobConciseNetRsp.payment);
                holder.paymentTxtView.setVisibility(View.VISIBLE);
            } else {
                holder.paymentTxtView.setVisibility(View.GONE);
            }

            if(!TextUtils.isEmpty(holder.jobConciseNetRsp.workplace)){
                holder.workplaceTxtView.setText(holder.jobConciseNetRsp.workplace);
                holder.workplaceTxtView.setVisibility(View.VISIBLE);
            } else {
                holder.workplaceTxtView.setVisibility(View.GONE);
            }

            holder.readTimesTextView.setText("" + holder.jobConciseNetRsp.readTimes);
            holder.commentCntTextView.setText("" + holder.jobConciseNetRsp.commentCnt);
        }
        return v;
    }

    // ---------------
    // More boilerplate methods
    // ---------------

    @Override
    public int getCount() {
        return jobConciseNetRsps == null ? 0 : jobConciseNetRsps.size();
    }

    @Override
    public Object getItem(int position) {
        return jobConciseNetRsps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
