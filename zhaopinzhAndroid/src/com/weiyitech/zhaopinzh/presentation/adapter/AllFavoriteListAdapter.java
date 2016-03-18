package com.weiyitech.zhaopinzh.presentation.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
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
import com.weiyitech.zhaopinzh.presentation.activity.*;
import com.weiyitech.zhaopinzh.presentation.component.MessageProvider;
import com.weiyitech.zhaopinzh.struct.JobConciseNetRsp;
import com.weiyitech.zhaopinzh.struct.PreachmeetingConciseRsp;
import com.weiyitech.zhaopinzh.struct.RunEmployerComponent;
import com.weiyitech.zhaopinzh.util.Common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-8-12
 * Time: 下午2:57
 * To change this template use File | Settings | File Templates.
 */
public class AllFavoriteListAdapter extends BaseAdapter {
    public ArrayList<RunEmployerComponent> runEmployerComponents;
    public ArrayList<PreachmeetingConciseRsp> preachmeetingConciseRsps;
    public ArrayList<JobConciseNetRsp> jobConciseNetRsps;
    public FavoriteActivity activity;
    public boolean mBusy = false;
    public int runEmployerSize;
    public int preachMeetingSize;
    public int netJobSize;

    public AllFavoriteListAdapter(Activity context, ArrayList<RunEmployerComponent> runEmployerComponentArrayList, ArrayList<PreachmeetingConciseRsp> preachmeetingConciseRsps, ArrayList<JobConciseNetRsp> jobConciseNetRspArrayList) {
        super();
        this.runEmployerComponents = runEmployerComponentArrayList;
        this.preachmeetingConciseRsps = preachmeetingConciseRsps;
        this.jobConciseNetRsps = jobConciseNetRspArrayList;
        runEmployerSize = runEmployerComponents == null ? 0 : runEmployerComponents.size();
        preachMeetingSize = preachmeetingConciseRsps == null ? 0 : preachmeetingConciseRsps.size();
        netJobSize = jobConciseNetRsps == null ? 0 : jobConciseNetRsps.size();
        activity = (FavoriteActivity) context;
    }

    public void setFlagBusy(boolean busy) {
        this.mBusy = busy;
    }

    /**
     * Define the ViewHolderRunEmployer for our adapter
     */
    public static class ViewHolderRunEmployer {
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

    public static class ViewHolderPreach {
        public View listItem;

        public ImageView employerLogoImgView;
        public TextView timeBeginTextView;
        public TextView timeEndTextView;
        public TextView universityTextView;
        public TextView preachNameTextView;
        public TextView placeTextView;
        public TextView readTimesTextView;
        public ImageButton favoriteImgBtn;
    }

    public static class ViewHolderNetJob {
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
        public JobConciseNetRsp jobConciseNetRsp;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ---------------
        // Boilerplate view inflation and ViewHolderRunEmployer code
        // ---------------
        View v = convertView;
        if (position == 0) {
            LayoutInflater vi =
                    (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.favorite_head_layout, null);
            return v;
        } else if (position < runEmployerSize + 1) {
            ViewHolderRunEmployer holder;
            LayoutInflater vi =
                    (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.home_activity_favoritefragment_list_item, null);
            holder = new ViewHolderRunEmployer();
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
            holder.runEmployerComponent = runEmployerComponents.get(position - 1);
            holder.runEmployerComponent.position = position;
            if (!holder.runEmployerComponent.favoriteValidMessage) {
                holder.employerNameTxtView.setTextColor(Color.LTGRAY);
                holder.timeTextView.setTextColor(Color.LTGRAY);
            }


            holder.favoriteImgBtn.setTag(position);
            holder.favoriteImgBtn.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = (Integer) view.getTag();
                    AlertDialog.Builder builder = new AlertDialog.Builder(AllFavoriteListAdapter.this.activity);
                    builder.setTitle("确定");
                    builder.setMessage("确定要删除此收藏职位吗？");
                    //当点确定按钮时从服务器上下载 新的apk 然后安装
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ContentResolver resolver = activity.getContentResolver();
                            String mSelectionClause = "employer_id MATCH ?";
                            String[] mSelectionArgs = {"" + AllFavoriteListAdapter.this.runEmployerComponents.get(position - 1).runEmployer.employerId};
                            resolver.delete(MessageProvider.FAVOR_RUNEMPLOYER_URI, mSelectionClause, mSelectionArgs);
                            AllFavoriteListAdapter.this.runEmployerComponents.remove(position - 1);
                            AllFavoriteListAdapter.this.notifyDataSetChanged();
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
        } else if (position == runEmployerSize + 1) {
            LayoutInflater vi =
                    (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.favorite_head_layout, null);
            TextView textView = (TextView) v.findViewById(R.id.favorite_head_text_view);
            textView.setText("校园招聘");
        } else if (position < runEmployerSize + preachMeetingSize + 2) {
            ViewHolderPreach holder;
            LayoutInflater vi =
                    (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.preach_meeting_favorite_fragment_list_item, null);
            holder = new ViewHolderPreach();
            holder.listItem = v;
            holder.preachNameTextView = (TextView) v.findViewById(R.id.preach_meeting_favorite_fragment_preach_name);
            holder.favoriteImgBtn = (ImageButton) v.findViewById(R.id.preach_meeting_favorite_fragment_favorite);
            holder.placeTextView = (TextView) v.findViewById(R.id.preach_meeting_favorite_fragment_place);
            holder.timeBeginTextView = (TextView) v.findViewById(R.id.preach_meeting_favorite_fragment_begin_time_textview);
            holder.timeEndTextView = (TextView) v.findViewById(R.id.preach_meeting_favorite_fragment_end_time_textview);
            holder.readTimesTextView = (TextView) v.findViewById(R.id.preach_meeting_favorite_fragment_read_times_text_view);
            holder.universityTextView = (TextView) v.findViewById(R.id.preach_meeting_favorite_fragment_university);

            final PreachmeetingConciseRsp preachmeetingConciseRsp = preachmeetingConciseRsps.get(position - runEmployerSize - 2);


            //根据阅读情况确定是否着色标记
            ContentResolver resolver = activity.getContentResolver();
            String selection = "preach_meeting_id = CAST(? AS INT)";
            String[] selectionArgs = {String.valueOf(preachmeetingConciseRsp.preachMeetingId)};
            Cursor cursor = null;
            cursor = resolver.query(MessageProvider.READ_PREACH_MEETING_URI, null, selection, selectionArgs, null);
            if (cursor != null) {
                cursor.close();
                cursor = null;
                holder.preachNameTextView.setTextColor(holder.readTimesTextView.getTextColors());
            } else {
                holder.preachNameTextView.setTextColor(Color.rgb(0, 0, 0));
            }

            holder.favoriteImgBtn.setTag(position);
            holder.favoriteImgBtn.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageButton favorImageButton = (ImageButton) view;
                    final int position = (Integer) view.getTag();
                    AlertDialog.Builder builder = new AlertDialog.Builder(AllFavoriteListAdapter.this.activity);
                    builder.setTitle("确定");
                    builder.setMessage("确定要删除此收藏职位吗？");
                    //当点确定按钮时从服务器上下载 新的apk 然后安装
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ContentResolver resolver = activity.getContentResolver();
                            String mSelectionClause = "preach_meeting_id = CAST(? AS INT)";
                            String[] mSelectionArgs = {String.valueOf(AllFavoriteListAdapter.this.preachmeetingConciseRsps.get(position - runEmployerSize - 2).preachMeetingId)};
                            int val = resolver.delete(MessageProvider.FAVOR_PREACH_MEETING_URI, mSelectionClause, mSelectionArgs);

                            AllFavoriteListAdapter.this.preachmeetingConciseRsps.remove(position - runEmployerSize - 2);
                            AllFavoriteListAdapter.this.notifyDataSetChanged();
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

            ZhaopinzhApp app = (ZhaopinzhApp) activity.getApplicationContext();
            if (!TextUtils.isEmpty(preachmeetingConciseRsp.preachMeetingName)) {
                holder.preachNameTextView.setText(preachmeetingConciseRsp.preachMeetingName);
                holder.preachNameTextView.setVisibility(View.VISIBLE);
            } else {
                holder.preachNameTextView.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(preachmeetingConciseRsp.getRunningTimeBegin())) {
                holder.timeBeginTextView.setText(Common.formatTime(preachmeetingConciseRsp.getRunningTimeBegin(), "yyyy-MM-dd HH:mm:ss", "MM月/dd日 HH:mm"));
                holder.timeBeginTextView.setVisibility(View.VISIBLE);
            } else {
                holder.timeBeginTextView.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(preachmeetingConciseRsp.getRunningTimeEnd())) {
                holder.timeEndTextView.setText(Common.formatTime(preachmeetingConciseRsp.getRunningTimeEnd(), "yyyy-MM-dd HH:mm:ss", "MM月/dd日 HH:mm"));
                holder.timeEndTextView.setVisibility(View.VISIBLE);
            } else {
                holder.timeEndTextView.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(preachmeetingConciseRsp.getRunningUniversity())) {
                holder.universityTextView.setText(preachmeetingConciseRsp.getRunningUniversity());
                holder.universityTextView.setVisibility(View.VISIBLE);
            } else {
                holder.universityTextView.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(preachmeetingConciseRsp.getRunPlace())) {
                holder.placeTextView.setText(preachmeetingConciseRsp.getRunPlace());
                holder.placeTextView.setVisibility(View.VISIBLE);
            } else {
                holder.placeTextView.setVisibility(View.GONE);
            }

            holder.readTimesTextView.setText("" + preachmeetingConciseRsp.readTimes);

        } else if (position == runEmployerSize + preachMeetingSize + 2) {
            LayoutInflater vi =
                    (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.favorite_head_layout, null);
            TextView textView = (TextView) v.findViewById(R.id.favorite_head_text_view);
            textView.setText("网络招聘");
        } else if (position > runEmployerSize + preachMeetingSize + 2) {
            ViewHolderNetJob holder;
            LayoutInflater vi =
                    (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.net_job_favorite_fragment_list_item, null);
            //v.setBackgroundColor(Color.GRAY);
            holder = new ViewHolderNetJob();
            holder.context = activity;
            holder.listItem = v;
            holder.conciseLayout = (RelativeLayout) v.findViewById(R.id.net_favorite_fragment_concise);
            holder.employerNameTxtView = (TextView) v.findViewById(R.id.net_favorite_fragment_employer_name);
            holder.jobNameTxtView = (TextView) v.findViewById(R.id.net_favorite_fragment_job_name);
            holder.paymentTxtView = (TextView) v.findViewById(R.id.net_favorite_fragment_payment_text_view);
            holder.workplaceTxtView = (TextView) v.findViewById(R.id.net_favorite_fragment_workplace_text_view);
            holder.readTimesTextView = (TextView) v.findViewById(R.id.net_favorite_fragment_read_times_text_view);
            holder.favoriteImgBtn = (ImageButton) v.findViewById(R.id.net_favorite_fragment_favorite);

            holder.expandLayout = (LinearLayout) v.findViewById(R.id.expandable);

            holder.jobConciseNetRsp = jobConciseNetRsps.get(position - runEmployerSize - preachMeetingSize - 3);
            holder.jobConciseNetRsp.position = position;
            holder.conciseLayout.setTag(holder.jobConciseNetRsp);
            holder.favoriteImgBtn.setTag(position);
            holder.favoriteImgBtn.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageButton favorImageButton = (ImageButton) view;
                    final int position = (Integer) view.getTag();
                    AlertDialog.Builder builder = new AlertDialog.Builder(AllFavoriteListAdapter.this.activity);
                    builder.setTitle("确定");
                    builder.setMessage("确定要删除此收藏职位吗？");
                    //当点确定按钮时从服务器上下载 新的apk 然后安装
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ContentResolver resolver = activity.getContentResolver();
                            String mSelectionClause = "job_id MATCH ?";
                            String[] mSelectionArgs = {"" + AllFavoriteListAdapter.this.jobConciseNetRsps.get(position - runEmployerSize - preachMeetingSize - 3).jobId};
                            resolver.delete(MessageProvider.FAVOR_NET_JOB_DETAIL_URI, mSelectionClause, mSelectionArgs);
                            AllFavoriteListAdapter.this.jobConciseNetRsps.remove(position - runEmployerSize - preachMeetingSize - 3);
                            AllFavoriteListAdapter.this.notifyDataSetChanged();
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

            if (holder.jobConciseNetRsp != null) {
                ZhaopinzhApp app = (ZhaopinzhApp) activity.getApplicationContext();
                if (!TextUtils.isEmpty(holder.jobConciseNetRsp.employerName)) {
                    holder.employerNameTxtView.setText(holder.jobConciseNetRsp.employerName);
                    holder.employerNameTxtView.setVisibility(View.VISIBLE);
                } else {
                    holder.employerNameTxtView.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(holder.jobConciseNetRsp.jobName)) {
                    holder.jobNameTxtView.setText(holder.jobConciseNetRsp.jobName);
                    holder.jobNameTxtView.setVisibility(View.VISIBLE);
                } else {
                    holder.jobNameTxtView.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(holder.jobConciseNetRsp.payment)) {
                    holder.paymentTxtView.setText(holder.jobConciseNetRsp.payment);
                    holder.paymentTxtView.setVisibility(View.VISIBLE);
                } else {
                    holder.paymentTxtView.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(holder.jobConciseNetRsp.workplace)) {
                    holder.workplaceTxtView.setText(holder.jobConciseNetRsp.workplace);
                    holder.workplaceTxtView.setVisibility(View.VISIBLE);
                } else {
                    holder.workplaceTxtView.setVisibility(View.GONE);
                }

                holder.readTimesTextView.setText("" + holder.jobConciseNetRsp.readTimes);

            }
        }

        return v;
    }

    // ---------------
    // More boilerplate methods
    // ---------------

    @Override
    public int getCount() {
        runEmployerSize = runEmployerComponents == null ? 0 : runEmployerComponents.size();
        preachMeetingSize = preachmeetingConciseRsps == null ? 0 : preachmeetingConciseRsps.size();
        netJobSize = jobConciseNetRsps == null ? 0 : jobConciseNetRsps.size();
        return runEmployerSize + preachMeetingSize + netJobSize + 3;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0 || position == runEmployerSize + 1 || position == runEmployerSize + preachMeetingSize + 2) {
            return null;
        } else if (position < runEmployerSize + 1) {
            return runEmployerComponents.get(position - 1);
        } else if (position > runEmployerSize + 1 && position < runEmployerSize + preachMeetingSize + 2) {
            return preachmeetingConciseRsps.get(position - runEmployerSize - 2);
        } else if (position > runEmployerSize + preachMeetingSize + 2) {
            return jobConciseNetRsps.get(position - runEmployerSize - preachMeetingSize - 3);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
