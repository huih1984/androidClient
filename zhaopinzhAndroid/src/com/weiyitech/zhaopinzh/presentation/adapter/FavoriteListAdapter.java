package com.weiyitech.zhaopinzh.presentation.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.presentation.*;
import com.weiyitech.zhaopinzh.presentation.activity.HomeActivity;
import com.weiyitech.zhaopinzh.presentation.activity.JobDetailActivity;
import com.weiyitech.zhaopinzh.presentation.component.MessageProvider;
import com.weiyitech.zhaopinzh.struct.RunEmployerComponent;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.cache.ImageLoader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-2-27
 * Time: 上午8:49
 * To change this template use File | Settings | File Templates.
 */
public class FavoriteListAdapter extends BaseAdapter {
    public ArrayList<RunEmployerComponent> jobList;
    public HomeActivity activity;
    public boolean mBusy = false;
    public ImageLoader mImageLoader;
    public int detailClickedItem = -1;

    public FavoriteListAdapter(Activity context, ArrayList<RunEmployerComponent> jobList) {
        super();
        this.jobList = jobList;
        activity = (HomeActivity) context;
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
        public TextView standPicTextView;
        public TextView jobCountsTxtView;
        public TextView readTimesTextView;
        public TextView hallTextView;
        public ImageButton favoriteImgBtn;
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
            v = vi.inflate(R.layout.home_activity_favoritefragment_list_item, null);
            holder = new ViewHolder();
            holder.context = activity;
            holder.listItem = v;
            holder.conciseLayout = (RelativeLayout) v.findViewById(R.id.favorite_fragment_concise);
            holder.employerNameTxtView = (TextView) v.findViewById(R.id.favorite_fragment_employer_name);
            holder.jobCountsTxtView = (TextView) v.findViewById(R.id.favorite_fragment_jobcounts);
            holder.timeTextView = (TextView) v.findViewById(R.id.favorite_fragment_time_textview);
            holder.standPicTextView = (TextView) v.findViewById(R.id.favorite_fragment_standpic);
            holder.hallTextView = (TextView) v.findViewById(R.id.favorite_fragment_job_hall);
            holder.readTimesTextView = (TextView) v.findViewById(R.id.favorite_fragment_read_times_text_view);
            holder.favoriteImgBtn = (ImageButton) v.findViewById(R.id.favorite_fragment_favorite);
            holder.expandLayout = (LinearLayout) v.findViewById(R.id.expandable);
            v.setTag(holder);
        } else holder = (ViewHolder) v.getTag();
        holder.runEmployerComponent = jobList.get(position);
        holder.runEmployerComponent.position = position;
        if (!holder.runEmployerComponent.favoriteValidMessage) {
            holder.employerNameTxtView.setTextColor(Color.LTGRAY);
            holder.timeTextView.setTextColor(Color.LTGRAY);
        }


        holder.favoriteImgBtn.setTag(position);
        holder.favoriteImgBtn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton favorImageButton = (ImageButton) view;
                final int position = (Integer) view.getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder(FavoriteListAdapter.this.activity);
                builder.setTitle("确定");
                builder.setMessage("确定要删除此收藏职位吗？");
                //当点确定按钮时从服务器上下载 新的apk 然后安装
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ContentResolver resolver = activity.getContentResolver();
                        String mSelectionClause = "employer_id MATCH ?";
                        String[] mSelectionArgs = {"" + FavoriteListAdapter.this.jobList.get(position).runEmployer.employerId};
                        resolver.delete(MessageProvider.FAVOR_RUNEMPLOYER_URI, mSelectionClause, mSelectionArgs);
                        FavoriteListAdapter.this.jobList.remove(position);
                        FavoriteListAdapter.this.notifyDataSetChanged();
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
                Date date = null;
                DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (holder.runEmployerComponent.runEmployer.runningTime != null) {
                        date = format1.parse(holder.runEmployerComponent.runEmployer.runningTime);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                holder.timeTextView.setText(format1.format(date));
                holder.timeTextView.setVisibility(View.VISIBLE);
            } else {
                holder.timeTextView.setVisibility(View.GONE);
            }

            holder.readTimesTextView.setText("" + holder.runEmployerComponent.runEmployer.readTimes);
            holder.readTimesTextView.setText("" + holder.runEmployerComponent.runEmployer.readTimes);
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
        }
        LayoutInflater listInflater =
                (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (holder.runEmployerComponent.jobConciseRspList != null) {
            holder.expandLayout.removeAllViewsInLayout();
            View dividerView;
            for (int i = 0; i < holder.runEmployerComponent.jobConciseRspList.size(); ++i) {
                View expendItemView = listInflater.inflate(R.layout.favorite_fragment_list_item_expand_item, null);
                final TextView jobNameTxtView = (TextView) expendItemView.findViewById(R.id.job_name_text_view);
                jobNameTxtView.setText(holder.runEmployerComponent.jobConciseRspList.get(i).jobName);
                TextView recuitmentNumberTxtView = (TextView) expendItemView.findViewById(R.id.recuitment_text_view);
                int demandNumber = holder.runEmployerComponent.jobConciseRspList.get(i).demandNumber;
                recuitmentNumberTxtView.setText("招聘人数：" + (demandNumber == 0 ? "不限":demandNumber));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(64*ZhaopinzhApp.getInstance().hardDensity));
                layoutParams.setMargins(0, 0, (int)(8*ZhaopinzhApp.getInstance().hardDensity), 0);
                expendItemView.setLayoutParams(layoutParams);
                holder.expandLayout.addView(expendItemView);
                if (i != holder.runEmployerComponent.jobConciseRspList.size() - 1) {
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
                        Resources resource = (Resources)activity.getBaseContext().getResources();
                        ColorStateList csl = (ColorStateList) resource.getColorStateList(android.R.color.darker_gray);
                        jobNameTxtView.setTextColor(csl);
                        String twPosition = (String) v.getTag();
                        String[] strPosition = twPosition.split(" ");
                        int position = Integer.parseInt(strPosition[1]);
                        int jobConcisePosition = Integer.parseInt(strPosition[0]);
                        jobList.get(position).jobConciseRspList.get(jobConcisePosition).read = true;
                        intent.putExtra("where", HomeActivity.class.getName());
                        intent.putParcelableArrayListExtra("job_consice_rsp_list", jobList.get(position).jobConciseRspList);
                        intent.putExtra("position", jobConcisePosition);
                        intent.putExtra("run_employer_component", jobList.get(position));
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    }
                });
                if(jobList.get(position).jobConciseRspList.get(i).read){
                    Resources resource = (Resources)activity.getBaseContext().getResources();
                    ColorStateList csl = (ColorStateList) resource.getColorStateList(android.R.color.darker_gray);
                    jobNameTxtView.setTextColor(csl);
                }
            }
            holder.expandLayout.requestLayout();

        }

        return v;
    }

    // ---------------
    // More boilerplate methods
    // ---------------

    @Override
    public int getCount() {
        return jobList == null ? 0 : jobList.size();
    }

    @Override
    public Object getItem(int position) {
        return jobList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
