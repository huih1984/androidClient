package com.weiyitech.zhaopinzh.presentation.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.database.Cursor;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.activity.ZhaopinzhJobConciseListActivity;
import com.weiyitech.zhaopinzh.presentation.activity.ZhaopinzhJobDetailActivity;
import com.weiyitech.zhaopinzh.presentation.component.MessageProvider;
import com.weiyitech.zhaopinzh.struct.JobDetailZhaopinzh;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.cache.ImageLoader;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-8-5
 * Time: 下午3:48
 * To change this template use File | Settings | File Templates.
 */
public class ZhaopinzhJobListAdapter extends BaseAdapter {
    public ArrayList<JobDetailZhaopinzh> jobDetailZhaopinzhs;
    public ZhaopinzhJobConciseListActivity activity;
    public boolean mBusy = false;
    public ImageLoader mImageLoader;
    public int pageIndex = 0;

    public ZhaopinzhJobListAdapter(Activity context, ArrayList<JobDetailZhaopinzh> jobDetailZhaopinzhs, int pageIndex) {
        super();
        this.jobDetailZhaopinzhs = jobDetailZhaopinzhs;
        activity = (ZhaopinzhJobConciseListActivity) context;
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
        public TextView commentCntTextView;
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
            v = vi.inflate(R.layout.zhaopinzh_job_fragment_list_item, null);
            //v.setBackgroundColor(Color.GRAY);
            holder = new ViewHolder();
            holder.context = activity;
            holder.listItem = v;
            holder.conciseLayout = (RelativeLayout) v.findViewById(R.id.zhaopinzh_job_fragment_concise);
            holder.employerNameTxtView = (TextView) v.findViewById(R.id.zhaopinzh_job_fragment_employer_name);
            holder.jobNameTxtView = (TextView)v.findViewById(R.id.zhaopinzh_job_fragment_job_name);
            holder.paymentTxtView = (TextView)v.findViewById(R.id.zhaopinzh_job_fragment_payment_text_view);
            holder.workplaceTxtView = (TextView)v.findViewById(R.id.zhaopinzh_job_fragment_workplace_text_view);
            holder.readTimesTextView = (TextView) v.findViewById(R.id.zhaopinzh_job_fragment_read_times_text_view);
            holder.commentCntTextView = (TextView) v.findViewById(R.id.zhaopinzh_job_fragment_cment_cnt_text_view);
            holder.favoriteImgBtn = (ImageButton) v.findViewById(R.id.zhaopinzh_job_fragment_favorite);

            holder.expandLayout = (LinearLayout) v.findViewById(R.id.expandable);
            v.setTag(holder);
        } else holder = (ViewHolder) v.getTag();

        holder.jobDetailZhaopinzh = jobDetailZhaopinzhs.get(position);
        ContentResolver resolver = activity.getContentResolver();
        String selection = "job_id = CAST(? AS INT) ";
        String[] selectionArgs = {String.valueOf(holder.jobDetailZhaopinzh.jobId)};
        Cursor cursor = null;
        cursor = resolver.query(MessageProvider.READ_ZHAOPINZH_JOB_DETAIL_URI, null, selection, selectionArgs, null);
        if(cursor != null){
            cursor.close();
            cursor = null;
            holder.jobNameTxtView.setTextColor(holder.readTimesTextView.getTextColors());
        }else {
            holder.jobNameTxtView.setTextColor(Color.rgb(0, 0, 0));
        }

        holder.jobDetailZhaopinzh = jobDetailZhaopinzhs.get(position);
        holder.jobDetailZhaopinzh.position = position;
        holder.conciseLayout.setTag(holder.jobDetailZhaopinzh);
        holder.conciseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentResolver resolver = activity.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put("job_id", ((JobDetailZhaopinzh)v.getTag()).jobId);
                resolver.insert(MessageProvider.READ_ZHAOPINZH_JOB_DETAIL_URI, contentValues);
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
                int position = (Integer) view.getTag();
                if (favorImageButton.isSelected()) {
                    ZhaopinzhJobListAdapter.this.jobDetailZhaopinzhs.get(position).isFavorite = false;
                    favorImageButton.setSelected(false);
                    ContentResolver resolver = activity.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    String mSelectionClause = "job_id = CAST(? AS INT)";
                    String[] mSelectionArgs = {String.valueOf(ZhaopinzhJobListAdapter.this.jobDetailZhaopinzhs.get(position).jobId)};
                    int val = resolver.delete(MessageProvider.FAVOR_ZHAOPINZH_JOB_DETAIL_URI, mSelectionClause, mSelectionArgs);
                } else {
                    ZhaopinzhJobListAdapter.this.jobDetailZhaopinzhs.get(position).isFavorite = true;
                    favorImageButton.setSelected(true);
                    //存入数据库
                    ContentResolver resolver = activity.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    Common.putZhaopinzhJobDetail(contentValues, ZhaopinzhJobListAdapter.this.jobDetailZhaopinzhs.get(position));
                    resolver.insert(MessageProvider.FAVOR_ZHAOPINZH_JOB_DETAIL_URI, contentValues);
                }

            }
        });

        //根据阅读情况确定是否着色标记
        cursor = resolver.query(MessageProvider.FAVOR_ZHAOPINZH_JOB_DETAIL_URI, null, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.close();
            holder.jobDetailZhaopinzh.isFavorite = true;
        } else {
            holder.jobDetailZhaopinzh.isFavorite = false;
        }
        if (holder.jobDetailZhaopinzh.isFavorite) {
            holder.favoriteImgBtn.setSelected(true);
        } else {
            holder.favoriteImgBtn.setSelected(false);
        }

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
            holder.commentCntTextView.setText("" + holder.jobDetailZhaopinzh.commentCnt);
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
