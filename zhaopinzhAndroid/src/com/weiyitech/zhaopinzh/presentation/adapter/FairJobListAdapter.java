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
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.activity.JobDetailActivity;
import com.weiyitech.zhaopinzh.presentation.activity.JobsOfFairActivity;
import com.weiyitech.zhaopinzh.presentation.component.MessageProvider;
import com.weiyitech.zhaopinzh.struct.RunEmployerComponent;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.cache.ImageLoader;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-2-23
 * Time: 上午10:19
 * To change this template use File | Settings | File Templates.
 */
public class FairJobListAdapter extends BaseAdapter {
    public ArrayList<RunEmployerComponent> runEmployerComponents;
    public JobsOfFairActivity activity;
    public boolean mBusy = false;
    public ImageLoader mImageLoader;
    public int employGetClickedItem = -1;
    public int detailClickedItem = -1;
    public int pageIndex = 0;

    public FairJobListAdapter(Activity context, ArrayList<RunEmployerComponent> runEmployerComponents, int pageIndex) {
        super();
        this.runEmployerComponents = runEmployerComponents;
        activity = (JobsOfFairActivity) context;
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
        public TextView standPicTextView;
        public TextView readTimesTextView;
        public TextView commentCntTextView;
        public TextView jobCountsTxtView;
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
            holder.newImgView = (ImageView) v.findViewById(R.id.job_fragment_new_image_view);
            holder.timeTextView = (TextView) v.findViewById(R.id.job_fragment_time_textview);
            holder.standPicTextView = (TextView) v.findViewById(R.id.job_fragment_standpic);
            holder.readTimesTextView = (TextView) v.findViewById(R.id.job_fragment_read_times_text_view);
            holder.commentCntTextView = (TextView) v.findViewById(R.id.job_fragment_cment_cnt_text_view);
            holder.hallTextView = (TextView) v.findViewById(R.id.job_fragment_job_hall);
            holder.favoriteImgBtn = (ImageButton) v.findViewById(R.id.job_fragment_favorite);
//            holder.standImgBtn = (ImageButton) v.findViewById(R.id.job_fragment_stand_img_btn);
//            holder.trafficImgBtn = (ImageButton) v.findViewById(R.id.job_fragment_traffic_img_btn);
            //holder.overflowImgBtn = (ImageButton) v.findViewById(R.id.job_fragment_overflow);

            holder.expandLayout = (LinearLayout) v.findViewById(R.id.expandable);


//            final ImageView helpImageView = (ImageView) v.findViewById(R.id.job_fragment_tip_img_view);
//            SharedPreferences sharedata = activity.getSharedPreferences("fristrun", 0);
//            Boolean isSoupon = sharedata.getBoolean("isSoupon", true);
//            if (isSoupon && position == 0 && pageIndex == 0) {
//                helpImageView.setVisibility(View.VISIBLE);
//            } else {
//                helpImageView.setVisibility(View.GONE);
//            }
//            helpImageView.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    helpImageView.setVisibility(View.GONE);
//                    SharedPreferences.Editor sharedata = activity.getSharedPreferences("fristrun", 0).edit();
//                    sharedata.putBoolean("isSoupon", false);
//                    sharedata.commit();
//                }
//            });

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
        if(cursor != null){
            cursor.close();
            cursor = null;
            holder.employerNameTxtView.setTextColor(holder.standPicTextView.getTextColors());
        }else {

            holder.employerNameTxtView.setTextColor(Color.rgb(0,0,0));
        }
//        holder.overflowImgBtn.setTag(holder);
//        holder.overflowImgBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ViewHolder viewHolder = (ViewHolder) v.getTag();
//                if (!viewHolder.overflowed) {
//                    viewHolder.overflowed = true;
//                    RelativeLayout relativeLayout = (RelativeLayout) viewHolder.conciseLayout;
//                    int margin = (int) (68 * ZhaopinzhApp.getInstance().hardDensity);
//                    float xDelta = (((float) margin) / ZhaopinzhApp.getInstance().widthPixels);
//                    TranslateAnimation translateAnimation = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, -xDelta, 0, 0, 0, 0);
//                    translateAnimation.setInterpolator(new LinearInterpolator());
//                    translateAnimation.setDuration(300);
//                    translateAnimation.setFillAfter(false);
//                    relativeLayout.setAnimation(translateAnimation);
//                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
//
//                    layoutParams.leftMargin = -margin;
//                    layoutParams.rightMargin = 0;
//                    relativeLayout.setLayoutParams(layoutParams);
//                    relativeLayout.requestLayout();
//                } else {
//                    viewHolder.overflowed = false;
//                    RelativeLayout relativeLayout = (RelativeLayout) viewHolder.conciseLayout;
//                    int margin = (int) (68 * ZhaopinzhApp.getInstance().hardDensity);
//                    float xDelta = (((float) 100.0) / ZhaopinzhApp.getInstance().widthPixels);
//                    TranslateAnimation translateAnimation = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, xDelta, 0, 0, 0, 0);
//                    translateAnimation.setInterpolator(new LinearInterpolator());
//                    translateAnimation.setDuration(300);
//                    translateAnimation.setFillAfter(false);
//                    relativeLayout.setAnimation(translateAnimation);
//                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
//
//                    layoutParams.leftMargin = 0;
//                    layoutParams.rightMargin = -margin;
//                    relativeLayout.setLayoutParams(layoutParams);
//                    relativeLayout.requestLayout();
//                }
//            }
//        });

//        holder.standImgBtn.setTag(position);
//        holder.standImgBtn.setOnClickListener(new ImageButton.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                RunEmployerComponent jobConciseNetRsp = jobConciseNetRsps.get((Integer) view.getTag());
//                String standStr = "";
//                if (jobConciseNetRsp.runEmployer.stands == null) {
//                    Toast.makeText(activity, "抱歉，还没有展位信息！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                for (int i = 0; i < jobConciseNetRsp.runEmployer.stands.size(); ++i) {
//                    standStr += jobConciseNetRsp.runEmployer.stands.get(i).standNumber + " ";
//                }
//                intent.putExtra("standStr", standStr);
//                intent.putExtra("fairId", jobConciseNetRsp.runEmployer.fairId);
//                intent.putExtra("employerId", jobConciseNetRsp.runEmployer.employerId);
//                intent.putExtra("standPicId", jobConciseNetRsp.runEmployer.stands.get(0).standPicId);
//                intent.putExtra("where", JobsOfFairActivity.class.getName());
//                intent.setClass(activity, StandPicActivity.class);
//                activity.startActivity(intent);
//                activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//            }
//        });
//
//        holder.trafficImgBtn.setTag(position);
//        holder.trafficImgBtn.setOnClickListener(new ImageButton.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.putExtra("fairId", jobConciseNetRsps.get((Integer) view.getTag()).runEmployer.fairId);
//                intent.putExtra("where", JobsOfFairActivity.class.getName());
//                intent.setClass(activity, TrafficActivity.class);
//                activity.startActivity(intent);
//                activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//            }
//        });

        holder.favoriteImgBtn.setTag(position);
        holder.favoriteImgBtn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton favorImageButton = (ImageButton) view;
                int position = (Integer) view.getTag();
                if (favorImageButton.isSelected()) {
                    FairJobListAdapter.this.runEmployerComponents.get(position).isFavorite = false;
                    favorImageButton.setSelected(false);
                    ContentResolver resolver = activity.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    String mSelectionClause = "employer_id = CAST(? AS INT) AND fair_id = CAST(? AS INT)";
                    String[] mSelectionArgs = {String.valueOf(FairJobListAdapter.this.runEmployerComponents.get(position).runEmployer.employerId),
                            String.valueOf(FairJobListAdapter.this.runEmployerComponents.get(position).runEmployer.fairId)};
                    int val = resolver.delete(MessageProvider.FAVOR_RUNEMPLOYER_URI, mSelectionClause, mSelectionArgs);
                    resolver.delete(MessageProvider.STAND_URI, mSelectionClause, mSelectionArgs);
                } else {
                    FairJobListAdapter.this.runEmployerComponents.get(position).isFavorite = true;
                    favorImageButton.setSelected(true);
                    //存入数据库
                    ContentResolver resolver = activity.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    Common.putRunEmployer(contentValues, FairJobListAdapter.this.runEmployerComponents.get(position).runEmployer);
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
//            if(holder.runEmployerComponent.runEmployer.recruitTimes == 0){
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
                for (int i = 0; i < runEmployerComponents.get(position).jobConciseRspList.size(); ++i) {
                    View expendItemView = listInflater.inflate(R.layout.job_fragment_list_item_expand_item, null);
                    TextView jobNameTxtView = (TextView) expendItemView.findViewById(R.id.job_name_text_view);
                    jobNameTxtView.setText(runEmployerComponents.get(position).jobConciseRspList.get(i).jobName);
                    TextView recuitmentNumberTxtView = (TextView) expendItemView.findViewById(R.id.recuitment_text_view);
                    int demandNumber = runEmployerComponents.get(position).jobConciseRspList.get(i).demandNumber;
                    recuitmentNumberTxtView.setText("招聘人数：" + (demandNumber == 0 ? "不限":demandNumber));
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(64*ZhaopinzhApp.getInstance().hardDensity));
                    layoutParams.setMargins(0, 0, (int)(8*ZhaopinzhApp.getInstance().hardDensity), 0);
                    expendItemView.setLayoutParams(layoutParams);
                    holder.expandLayout.addView(expendItemView);
                    if (i != runEmployerComponents.get(position).jobConciseRspList.size() - 1) {
                        dividerView = new View(activity);
                        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
                        layoutParams.setMargins(0, 0, (int)(8*ZhaopinzhApp.getInstance().hardDensity), 0);
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
                            String twPosition = (String) v.getTag();
                            String[] strPosition = twPosition.split(" ");
                            int position = Integer.parseInt(strPosition[1]);
                            int jobConcisePosition = Integer.parseInt(strPosition[0]);
                            intent.putExtra("where", JobsOfFairActivity.class.getName());
                            intent.putParcelableArrayListExtra("job_consice_rsp_list", runEmployerComponents.get(position).jobConciseRspList);
                            intent.putExtra("position", jobConcisePosition);
                            intent.putExtra("run_employer_component", runEmployerComponents.get(position));
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                        }
                    });
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