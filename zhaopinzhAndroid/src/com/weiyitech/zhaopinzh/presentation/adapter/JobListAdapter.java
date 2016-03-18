package com.weiyitech.zhaopinzh.presentation.adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.presentation.activity.HomeActivity;
import com.weiyitech.zhaopinzh.presentation.activity.JobDetailActivity;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.component.MessageProvider;
import com.weiyitech.zhaopinzh.struct.RunEmployerComponent;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.cache.ImageLoader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-10-20
 * Time: 下午6:19
 */
public class JobListAdapter extends BaseAdapter {
    public ArrayList<RunEmployerComponent> runEmployerComponents;
    public HomeActivity activity;
    public boolean mBusy = false;
    public ImageLoader mImageLoader;
    public int detailClickedItem = -1;
    public int pageIndex = 0;

    public JobListAdapter(Activity context, ArrayList<RunEmployerComponent> runEmployerComponents, int pageIndex) {
        super();
        this.runEmployerComponents = runEmployerComponents;
        activity = (HomeActivity) context;
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
        public TextView jobCountsTxtView;
        public TextView standPicTextView;
        public TextView readTimesTextView;
        public TextView commentCntTextView;
        public ImageView newImgView;
        public TextView hallTextView;
        public ImageButton trafficImgBtn;
        public ImageButton standImgBtn;
        public ImageButton favoriteImgBtn;
        public ImageButton overflowImgBtn;
        public boolean overflowed;

        public LinearLayout expandLayout;
        public Context context;
        public RunEmployerComponent runEmployerComponent;
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
            v = vi.inflate(R.layout.home_activity_jobfragment_list_item, null);
            //v.setBackgroundColor(Color.GRAY);
            holder = new ViewHolder();
            holder.context = activity;
            holder.listItem = v;
            holder.conciseLayout = (RelativeLayout) v.findViewById(R.id.job_fragment_concise);
            holder.employerNameTxtView = (TextView) v.findViewById(R.id.job_fragment_employer_name);
            holder.jobCountsTxtView = (TextView) v.findViewById(R.id.job_fragment_jobcounts);
            holder.timeTextView = (TextView) v.findViewById(R.id.job_fragment_time_textview);
            holder.standPicTextView = (TextView) v.findViewById(R.id.job_fragment_standpic);
            holder.readTimesTextView = (TextView) v.findViewById(R.id.job_fragment_read_times_text_view);
            holder.commentCntTextView = (TextView) v.findViewById(R.id.job_fragment_cment_cnt_text_view);
            holder.newImgView = (ImageView) v.findViewById(R.id.job_fragment_new_image_view);
            holder.hallTextView = (TextView) v.findViewById(R.id.job_fragment_job_hall);
            holder.favoriteImgBtn = (ImageButton) v.findViewById(R.id.job_fragment_favorite);
            holder.expandLayout = (LinearLayout) v.findViewById(R.id.expandable);



            v.setTag(holder);
        } else holder = (ViewHolder) v.getTag();

        holder.runEmployerComponent = runEmployerComponents.get(position);
        holder.runEmployerComponent.position = position;

        //根据阅读情况确定是否着色标记
        ContentResolver resolver = activity.getContentResolver();
        String selection = "employer_id = CAST(? AS INT) AND fair_id = CAST(? AS INT)";
        String[] selectionArgs = {String.valueOf(holder.runEmployerComponent.runEmployer.employerId), String.valueOf(holder.runEmployerComponent.runEmployer.fairId)};
        Cursor cursor = null;
        cursor = resolver.query(MessageProvider.READ_RUNEMPLOYER_URI, null, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.close();
            cursor = null;
            holder.employerNameTxtView.setTextColor(holder.standPicTextView.getTextColors());
        } else {

            holder.employerNameTxtView.setTextColor(Color.rgb(0, 0, 0));
        }

        holder.favoriteImgBtn.setTag(position);
        holder.favoriteImgBtn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton favorImageButton = (ImageButton) view;
                int position = (Integer) view.getTag();
                if (favorImageButton.isSelected()) {
                    JobListAdapter.this.runEmployerComponents.get(position).isFavorite = false;
                    favorImageButton.setSelected(false);
                    ContentResolver resolver = activity.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    String mSelectionClause = "employer_id = CAST(? AS INT) AND fair_id = CAST(? AS INT)";
                    String[] mSelectionArgs = {String.valueOf(JobListAdapter.this.runEmployerComponents.get(position).runEmployer.employerId),
                            String.valueOf(JobListAdapter.this.runEmployerComponents.get(position).runEmployer.fairId)};
                    int val = resolver.delete(MessageProvider.FAVOR_RUNEMPLOYER_URI, mSelectionClause, mSelectionArgs);
                    resolver.delete(MessageProvider.STAND_URI, mSelectionClause, mSelectionArgs);
                } else {
                    JobListAdapter.this.runEmployerComponents.get(position).isFavorite = true;
                    favorImageButton.setSelected(true);
                    //存入数据库
                    ContentResolver resolver = activity.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    Common.putRunEmployer(contentValues, JobListAdapter.this.runEmployerComponents.get(position).runEmployer);
                    resolver.insert(MessageProvider.FAVOR_RUNEMPLOYER_URI, contentValues);
                    if (runEmployerComponents.get(position).runEmployer.stands != null) {
                        for (int i = 0; i < runEmployerComponents.get(position).runEmployer.stands.size(); ++i) {
                            ContentValues standContentValues = new ContentValues();
                            standContentValues.put("employer_id", runEmployerComponents.get(position).runEmployer.stands.get(i).employerId);
                            standContentValues.put("fair_id", runEmployerComponents.get(position).runEmployer.stands.get(i).fairId);
                            standContentValues.put("stand_number", runEmployerComponents.get(position).runEmployer.stands.get(i).standNumber);
                            resolver.insert(MessageProvider.STAND_URI, standContentValues);
                        }
                    }
                }
            }
        });

        //根据阅读情况确定是否着色标记
        cursor = resolver.query(MessageProvider.FAVOR_RUNEMPLOYER_URI, null, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.close();
            holder.runEmployerComponent.isFavorite = true;
        } else {
            holder.runEmployerComponent.isFavorite = false;
        }
        if (holder.runEmployerComponent.isFavorite) {
            holder.favoriteImgBtn.setSelected(true);
        } else {
            holder.favoriteImgBtn.setSelected(false);
        }

        if (holder.runEmployerComponent != null) {
            ZhaopinzhApp app = (ZhaopinzhApp) activity.getApplicationContext();
            if (!TextUtils.isEmpty(holder.runEmployerComponent.runEmployer.employerName)) {
                holder.employerNameTxtView.setText(holder.runEmployerComponent.runEmployer.employerName);
                holder.employerNameTxtView.setVisibility(View.VISIBLE);
            } else {
                holder.employerNameTxtView.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(holder.runEmployerComponent.runEmployer.hallName)) {
                holder.hallTextView.setText(holder.runEmployerComponent.runEmployer.hallName);
                holder.hallTextView.setVisibility(View.VISIBLE);
            } else {
                holder.hallTextView.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(holder.runEmployerComponent.runEmployer.runningTime)) {
                holder.timeTextView.setText(Common.formatTime(holder.runEmployerComponent.runEmployer.runningTime,"yyyy-MM-dd HH:mm:ss", "MM月/dd日"));
                holder.timeTextView.setVisibility(View.VISIBLE);
            } else {
                holder.timeTextView.setVisibility(View.GONE);
            }
//            if (holder.runEmployerComponent.runEmployer.recruitTimes == 0) {
//                holder.newImgView.setVisibility(View.VISIBLE);
//            } else {
//                holder.newImgView.setVisibility(View.GONE);
//            }
            holder.newImgView.setVisibility(View.GONE);
            holder.readTimesTextView.setText("" + holder.runEmployerComponent.runEmployer.readTimes);
            holder.commentCntTextView.setText("" + holder.runEmployerComponent.commentCnt);
            if (holder.runEmployerComponent.runEmployer.jobCounts == 0) {
                holder.jobCountsTxtView.setText("发布职位：见现场海报");
            } else {
                holder.jobCountsTxtView.setText("发布职位：" + holder.runEmployerComponent.runEmployer.jobCounts);
            }
            // JobConciseRsp stand
            String standStr = "展位号 ";
            if (holder.runEmployerComponent.runEmployer.stands != null) {
                for (int i = 0; i < holder.runEmployerComponent.runEmployer.stands.size(); ++i) {
                    standStr += holder.runEmployerComponent.runEmployer.stands.get(i).standNumber + " ";
                }
                holder.standPicTextView.setText(standStr);
                holder.standPicTextView.setVisibility(View.VISIBLE);
            } else {
                holder.standPicTextView.setVisibility(View.GONE);
            }

            LayoutInflater listInflater =
                    (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (runEmployerComponents.get(position).jobConciseRspList != null) {
                holder.expandLayout.removeAllViewsInLayout();
                View dividerView = new View(activity);
//                dividerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 1));
//
//                dividerView.setBackgroundResource(android.R.color.holo_green_dark);
//                holder.expandLayout.addView(dividerView);
                for (int i = 0; i < runEmployerComponents.get(position).jobConciseRspList.size(); ++i) {
                    final View expendItemView = listInflater.inflate(R.layout.job_fragment_list_item_expand_item, null);
                    final TextView jobNameTxtView = (TextView) expendItemView.findViewById(R.id.job_name_text_view);
                    jobNameTxtView.setText(runEmployerComponents.get(position).jobConciseRspList.get(i).jobName);
                    TextView recuitmentNumberTxtView = (TextView) expendItemView.findViewById(R.id.recuitment_text_view);
                    int demandNumber = runEmployerComponents.get(position).jobConciseRspList.get(i).demandNumber;
                    recuitmentNumberTxtView.setText("招聘人数：" + (demandNumber == 0 ? "不限" : demandNumber));
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (64 * ZhaopinzhApp.getInstance().hardDensity));
                    layoutParams.setMargins(0, 0, (int) (8 * ZhaopinzhApp.getInstance().hardDensity), 0);
                    expendItemView.setLayoutParams(layoutParams);
                    holder.expandLayout.addView(expendItemView);
                    if (i != runEmployerComponents.get(position).jobConciseRspList.size() - 1) {
                        dividerView = new View(activity);
                        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
                        layoutParams.setMargins(0, 0, (int) (8 * ZhaopinzhApp.getInstance().hardDensity), 0);
                        dividerView.setLayoutParams(layoutParams);

                        dividerView.setBackgroundResource(R.drawable.divider_line);
                        holder.expandLayout.addView(dividerView);
                    }
                    String twPosition = "" + i + " " + position;
                    expendItemView.setTag(twPosition);
                    expendItemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //查询职位详情
                            Intent intent = new Intent();
                            intent.setClass(activity, JobDetailActivity.class);
                            Resources resource = (Resources) activity.getBaseContext().getResources();
                            ColorStateList csl = (ColorStateList) resource.getColorStateList(android.R.color.darker_gray);
                            jobNameTxtView.setTextColor(csl);
                            String twPosition = (String) v.getTag();
                            String[] strPosition = twPosition.split(" ");
                            int position = Integer.parseInt(strPosition[1]);
                            int jobConcisePosition = Integer.parseInt(strPosition[0]);
                            runEmployerComponents.get(position).jobConciseRspList.get(jobConcisePosition).read = true;
                            intent.putExtra("where", HomeActivity.class.getName());
                            intent.putParcelableArrayListExtra("job_consice_rsp_list", runEmployerComponents.get(position).jobConciseRspList);
                            intent.putExtra("position", jobConcisePosition);
                            intent.putExtra("run_employer_component", runEmployerComponents.get(position));
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                        }
                    });
                    if (runEmployerComponents.get(position).jobConciseRspList.get(i).read) {
                        jobNameTxtView.setTextColor(holder.readTimesTextView.getTextColors());
                    }
                }
                holder.expandLayout.requestLayout();
            }

        }
        return v;
    }

    // ---------------
    // More boilerplate methods
    // ---------------

    @Override
    public int getCount() {
        return runEmployerComponents == null ? 0 : runEmployerComponents.size();
    }

    @Override
    public Object getItem(int position) {
        return runEmployerComponents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}