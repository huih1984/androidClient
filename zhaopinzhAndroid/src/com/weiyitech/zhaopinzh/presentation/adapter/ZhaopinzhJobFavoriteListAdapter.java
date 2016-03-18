package com.weiyitech.zhaopinzh.presentation.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.activity.NetJobDetailActivity;
import com.weiyitech.zhaopinzh.presentation.activity.ZhaopinzhJobConciseListActivity;
import com.weiyitech.zhaopinzh.presentation.activity.ZhaopinzhJobDetailActivity;
import com.weiyitech.zhaopinzh.presentation.component.MessageProvider;
import com.weiyitech.zhaopinzh.struct.JobConciseNetRsp;
import com.weiyitech.zhaopinzh.struct.JobDetailZhaopinzh;
import com.weiyitech.zhaopinzh.util.cache.ImageLoader;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-8-5
 * Time: 下午3:48
 * To change this template use File | Settings | File Templates.
 */
public class ZhaopinzhJobFavoriteListAdapter extends BaseAdapter {
    public ArrayList<JobDetailZhaopinzh> jobDetailZhaopinzhs;
    public ZhaopinzhJobConciseListActivity activity;
    public boolean mBusy = false;
    public ImageLoader mImageLoader;

    public ZhaopinzhJobFavoriteListAdapter(Activity context, ArrayList<JobDetailZhaopinzh> jobDetailZhaopinzhs) {
        super();
        this.jobDetailZhaopinzhs = jobDetailZhaopinzhs;
        activity = (ZhaopinzhJobConciseListActivity) context;
        mImageLoader = new ImageLoader(context);
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

        public LinearLayout expandLayout;
        public Context context;
        public JobDetailZhaopinzh jobDetailZhaopinzh;
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
            v = vi.inflate(R.layout.zhaopinzh_job_favorite_fragment_list_item, null);
            //v.setBackgroundColor(Color.GRAY);
            holder = new ViewHolder();
            holder.context = activity;
            holder.listItem = v;
            holder.conciseLayout = (RelativeLayout) v.findViewById(R.id.zhaopinzh_favorite_fragment_concise);
            holder.employerNameTxtView = (TextView) v.findViewById(R.id.zhaopinzh_favorite_fragment_employer_name);
            holder.jobNameTxtView = (TextView)v.findViewById(R.id.zhaopinzh_favorite_fragment_job_name);
            holder.paymentTxtView = (TextView)v.findViewById(R.id.zhaopinzh_favorite_fragment_payment_text_view);
            holder.workplaceTxtView = (TextView)v.findViewById(R.id.zhaopinzh_favorite_fragment_workplace_text_view);
            holder.readTimesTextView = (TextView) v.findViewById(R.id.zhaopinzh_favorite_fragment_read_times_text_view);
            holder.favoriteImgBtn = (ImageButton) v.findViewById(R.id.zhaopinzh_favorite_fragment_favorite);

            holder.expandLayout = (LinearLayout) v.findViewById(R.id.expandable);
            v.setTag(holder);
        } else holder = (ViewHolder) v.getTag();

        holder.jobDetailZhaopinzh = jobDetailZhaopinzhs.get(position);
        holder.jobDetailZhaopinzh.position = position;
        holder.conciseLayout.setTag(holder.jobDetailZhaopinzh);
        holder.conciseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobDetailZhaopinzh jobDetailZhaopinzh = (JobDetailZhaopinzh)v.getTag();
                Intent intent = new Intent(activity, ZhaopinzhJobDetailActivity.class);
                intent.putExtra("data", jobDetailZhaopinzh);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        holder.favoriteImgBtn.setTag(position);
        holder.favoriteImgBtn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton favorImageButton = (ImageButton) view;
                final int position = (Integer) view.getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder(ZhaopinzhJobFavoriteListAdapter.this.activity);
                builder.setTitle("确定");
                builder.setMessage("确定要删除此收藏职位吗？");
                //当点确定按钮时从服务器上下载 新的apk 然后安装
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ContentResolver resolver = activity.getContentResolver();
                        String mSelectionClause = "job_id MATCH ?";
                        String[] mSelectionArgs = {"" + ZhaopinzhJobFavoriteListAdapter.this.jobDetailZhaopinzhs.get(position).jobId};
                        resolver.delete(MessageProvider.FAVOR_ZHAOPINZH_JOB_DETAIL_URI, mSelectionClause, mSelectionArgs);
                        ZhaopinzhJobFavoriteListAdapter.this.jobDetailZhaopinzhs.remove(position);
                        ZhaopinzhJobFavoriteListAdapter.this.notifyDataSetChanged();
                    }
                });
                //当点取消按钮时进行登录
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        if (holder.jobDetailZhaopinzh != null) {
            ZhaopinzhApp app = (ZhaopinzhApp) activity.getApplicationContext();
            if (!TextUtils.isEmpty(holder.jobDetailZhaopinzh.employerName)) {
                holder.employerNameTxtView.setText(holder.jobDetailZhaopinzh.employerName);
                holder.employerNameTxtView.setVisibility(View.VISIBLE);
            } else {
                holder.employerNameTxtView.setVisibility(View.GONE);
            }

            if(!TextUtils.isEmpty(holder.jobDetailZhaopinzh.jobName)){
                holder.jobNameTxtView.setText(holder.jobDetailZhaopinzh.jobName);
                holder.jobNameTxtView.setVisibility(View.VISIBLE);
            } else {
                holder.jobNameTxtView.setVisibility(View.GONE);
            }

            if(!TextUtils.isEmpty(holder.jobDetailZhaopinzh.payment)){
                holder.paymentTxtView.setText(holder.jobDetailZhaopinzh.payment);
                holder.paymentTxtView.setVisibility(View.VISIBLE);
            } else {
                holder.paymentTxtView.setVisibility(View.GONE);
            }

            if(!TextUtils.isEmpty(holder.jobDetailZhaopinzh.workplace)){
                holder.workplaceTxtView.setText(holder.jobDetailZhaopinzh.workplace);
                holder.workplaceTxtView.setVisibility(View.VISIBLE);
            } else {
                holder.workplaceTxtView.setVisibility(View.GONE);
            }

            holder.readTimesTextView.setText("" + holder.jobDetailZhaopinzh.readTimes);

        }
        return v;
    }

    // ---------------
    // More boilerplate methods
    // ---------------

    @Override
    public int getCount() {
        return jobDetailZhaopinzhs == null ? 0 : jobDetailZhaopinzhs.size();
    }

    @Override
    public Object getItem(int position) {
        return jobDetailZhaopinzhs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
