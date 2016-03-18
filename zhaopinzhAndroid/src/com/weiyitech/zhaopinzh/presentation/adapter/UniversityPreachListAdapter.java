package com.weiyitech.zhaopinzh.presentation.adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.presentation.activity.PreachMeetingInstructionActivity;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.activity.UniversityCampusActivity;
import com.weiyitech.zhaopinzh.presentation.component.MessageProvider;
import com.weiyitech.zhaopinzh.struct.PreachmeetingConciseRsp;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.MyDatabaseUtil;
import com.weiyitech.zhaopinzh.util.cache.ImageLoader;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-5-27
 * Time: 下午2:43
 * To change this template use File | Settings | File Templates.
 */
public class UniversityPreachListAdapter extends BaseAdapter {
    public ArrayList<PreachmeetingConciseRsp> preachmeetingConciseRspArrayList;
    public UniversityCampusActivity activity;
    public boolean mBusy = false;

    public UniversityPreachListAdapter(Activity context, ArrayList<PreachmeetingConciseRsp> preachmeetingConciseRspArrayList) {
        super();
        this.preachmeetingConciseRspArrayList = preachmeetingConciseRspArrayList;
        activity = (UniversityCampusActivity) context;
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
        public ImageView isNewImgView;
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
            v = vi.inflate(R.layout.preach_meeting_list_fragment_list_item, null);
            holder = new ViewHolder();
            holder.listItem = v;
            holder.preachNameTextView = (TextView) v.findViewById(R.id.preach_meeting_list_fragment_preach_name);
            holder.favoriteImgBtn = (ImageButton) v.findViewById(R.id.preach_meeting_list_fragment_favorite);
            holder.placeTextView = (TextView) v.findViewById(R.id.preach_meeting_list_fragment_place);
            holder.timeBeginTextView = (TextView) v.findViewById(R.id.preach_meeting_list_fragment_begin_time_textview);
            holder.timeEndTextView = (TextView) v.findViewById(R.id.preach_meeting_list_fragment_end_time_textview);
            holder.readTimesTextView = (TextView) v.findViewById(R.id.preach_meeting_list_fragment_read_times_text_view);
            holder.universityTextView = (TextView) v.findViewById(R.id.preach_meeting_list_fragment_university);
            holder.isNewImgView = (ImageView)v.findViewById(R.id.preach_meeting_list_fragment_new_image_view);

            v.setTag(holder);
        } else holder = (ViewHolder) v.getTag();

        final PreachmeetingConciseRsp preachmeetingConciseRsp = preachmeetingConciseRspArrayList.get(position);

        if(preachmeetingConciseRsp.isNew){
            holder.isNewImgView.setVisibility(View.VISIBLE);
        } else {
            holder.isNewImgView.setVisibility(View.GONE);
        }

        holder.listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PreachMeetingInstructionActivity.class);
                Resources resource = (Resources)activity.getBaseContext().getResources();
                ColorStateList csl = (ColorStateList) resource.getColorStateList(android.R.color.darker_gray);
                ((TextView)v.findViewById(R.id.preach_meeting_list_fragment_preach_name)).setTextColor(csl);
                intent.putExtra("preachmeeting", preachmeetingConciseRsp);
                intent.putExtra("where", UniversityCampusActivity.class.getName());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                ContentResolver resolver = activity.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put("preach_meeting_id", preachmeetingConciseRsp.preachMeetingId);
                resolver.insert(MessageProvider.READ_PREACH_MEETING_URI, contentValues);
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
                int position = (Integer) view.getTag();
                if (favorImageButton.isSelected()) {
                    UniversityPreachListAdapter.this.preachmeetingConciseRspArrayList.get(position).isFavorite = false;
                    favorImageButton.setSelected(false);
                    ContentResolver resolver = activity.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    String mSelectionClause = "preach_meeting_id = CAST(? AS INT)";
                    String[] mSelectionArgs = {String.valueOf(UniversityPreachListAdapter.this.preachmeetingConciseRspArrayList.get(position).preachMeetingId)};
                    int val = resolver.delete(MessageProvider.FAVOR_PREACH_MEETING_URI, mSelectionClause, mSelectionArgs);
                } else {
                    UniversityPreachListAdapter.this.preachmeetingConciseRspArrayList.get(position).isFavorite = true;
                    favorImageButton.setSelected(true);
                    //存入数据库
                    ContentResolver resolver = activity.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    Common.putPreachMeeting(contentValues, UniversityPreachListAdapter.this.preachmeetingConciseRspArrayList.get(position));
                    resolver.insert(MessageProvider.FAVOR_PREACH_MEETING_URI, contentValues);
                }
            }
        });
        //根据阅读情况确定是否着色标记
        cursor = resolver.query(MessageProvider.FAVOR_PREACH_MEETING_URI, null, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.close();
            preachmeetingConciseRsp.isFavorite = true;
        } else {
            preachmeetingConciseRsp.isFavorite = false;
        }
        if (preachmeetingConciseRsp.isFavorite) {
            holder.favoriteImgBtn.setSelected(true);
        } else {
            holder.favoriteImgBtn.setSelected(false);
        }

        ZhaopinzhApp app = (ZhaopinzhApp) activity.getApplicationContext();
        if (!TextUtils.isEmpty(preachmeetingConciseRsp.preachMeetingName)) {
            holder.preachNameTextView.setText(preachmeetingConciseRsp.preachMeetingName);
            holder.preachNameTextView.setVisibility(View.VISIBLE);
        } else {
            //holder.preachNameTextView.setVisibility(View.GONE);
            holder.preachNameTextView.setText("宣讲会名称");

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