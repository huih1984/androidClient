package com.weiyitech.zhaopinzh.presentation.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.presentation.activity.CampusActivity;
import com.weiyitech.zhaopinzh.presentation.activity.PreachMeetingInstructionActivity;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.component.MessageProvider;
import com.weiyitech.zhaopinzh.struct.PreachmeetingConciseRsp;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.cache.ImageLoader;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-5-29
 * Time: 上午8:24
 * To change this template use File | Settings | File Templates.
 */
public class PreachFavoriteListAdapter extends BaseAdapter {
    public ArrayList<PreachmeetingConciseRsp> preachmeetingConciseRspArrayList;
    public CampusActivity activity;
    public boolean mBusy = false;
    public ImageLoader mImageLoader;

    public PreachFavoriteListAdapter(Activity context, ArrayList<PreachmeetingConciseRsp> preachmeetingConciseRspArrayList) {
        super();
        this.preachmeetingConciseRspArrayList = preachmeetingConciseRspArrayList;
        activity = (CampusActivity) context;
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

        public ImageView employerLogoImgView;
        public TextView timeBeginTextView;
        public TextView timeEndTextView;
        public TextView universityTextView;
        public TextView preachNameTextView;
        public TextView placeTextView;
        public TextView readTimesTextView;
        public ImageButton favoriteImgBtn;
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
            v = vi.inflate(R.layout.preach_meeting_favorite_fragment_list_item, null);
            holder = new ViewHolder();
            holder.listItem = v;
            holder.preachNameTextView = (TextView) v.findViewById(R.id.preach_meeting_favorite_fragment_preach_name);
            holder.favoriteImgBtn = (ImageButton) v.findViewById(R.id.preach_meeting_favorite_fragment_favorite);
            holder.placeTextView = (TextView) v.findViewById(R.id.preach_meeting_favorite_fragment_place);
            holder.timeBeginTextView = (TextView) v.findViewById(R.id.preach_meeting_favorite_fragment_begin_time_textview);
            holder.timeEndTextView = (TextView) v.findViewById(R.id.preach_meeting_favorite_fragment_end_time_textview);
            holder.readTimesTextView = (TextView) v.findViewById(R.id.preach_meeting_favorite_fragment_read_times_text_view);
            holder.universityTextView = (TextView) v.findViewById(R.id.preach_meeting_favorite_fragment_university);
            v.setTag(holder);
        } else holder = (ViewHolder) v.getTag();

        final PreachmeetingConciseRsp preachmeetingConciseRsp = preachmeetingConciseRspArrayList.get(position);

        holder.listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PreachMeetingInstructionActivity.class);
                intent.putExtra("preachmeeting", preachmeetingConciseRsp);
                intent.putExtra("where", CampusActivity.class.getName());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

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
                AlertDialog.Builder builder = new AlertDialog.Builder(PreachFavoriteListAdapter.this.activity);
                builder.setTitle("确定");
                builder.setMessage("确定要删除此收藏职位吗？");
                //当点确定按钮时从服务器上下载 新的apk 然后安装
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ContentResolver resolver = activity.getContentResolver();
                        String mSelectionClause = "preach_meeting_id = CAST(? AS INT)";
                        String[] mSelectionArgs = {String.valueOf(PreachFavoriteListAdapter.this.preachmeetingConciseRspArrayList.get(position).preachMeetingId)};
                        int val = resolver.delete(MessageProvider.FAVOR_PREACH_MEETING_URI, mSelectionClause, mSelectionArgs);

                        PreachFavoriteListAdapter.this.preachmeetingConciseRspArrayList.remove(position);
                        PreachFavoriteListAdapter.this.notifyDataSetChanged();
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

//        if (!preachmeetingConciseRsp.favoriteValidMessage) {
//            holder.obsoleteImgView.setVisibility(View.VISIBLE);
//        } else {
//            holder.obsoleteImgView.setVisibility(View.GONE);
//        }

        return v;
    }

    // ---------------
    // More boilerplate methods
    // ---------------

    @Override
    public int getCount() {
        return preachmeetingConciseRspArrayList == null ? 0 : preachmeetingConciseRspArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return preachmeetingConciseRspArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}

