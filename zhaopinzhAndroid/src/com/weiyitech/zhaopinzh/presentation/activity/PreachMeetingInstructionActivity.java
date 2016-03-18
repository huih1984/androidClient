package com.weiyitech.zhaopinzh.presentation.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.business.QueryPreaching;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.component.MessageProvider;
import com.weiyitech.zhaopinzh.struct.PreachmeetingConciseRsp;
import com.weiyitech.zhaopinzh.struct.PreachmeetingReq;
import com.weiyitech.zhaopinzh.struct.PreachmeetingRsp;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.webview.MyWebView;


/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-6-3
 * Time: 上午10:17
 * To change this template use File | Settings | File Templates.
 */
public class PreachMeetingInstructionActivity extends CommonActivity implements BusinessInterface {
    String fromWhere;
    MyWebView webView;
    TextView endTimeTxtView;
    TextView beginTimeTxtView;
    TextView placeTxtView;
    TextView universityTxtView;
    TextView preachMeetingTxtView;
    TextView readTimesTxtView;
    ImageButton favoriteImgButton;
    PreachmeetingConciseRsp preachmeetingConciseRsp;

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.preach_instruction_activity);
            fromWhere = getIntent().getStringExtra("where");

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            beginTimeTxtView = (TextView) findViewById(R.id.preach_meeting_begin_time_textview);
            endTimeTxtView = (TextView) findViewById(R.id.preach_meeting_end_time_textview);
            placeTxtView = (TextView) findViewById(R.id.preach_meeting_place);
            universityTxtView = (TextView) findViewById(R.id.preach_meeting_university);
            preachMeetingTxtView = (TextView) findViewById(R.id.preach_meeting_preach_name);
            readTimesTxtView = (TextView) findViewById(R.id.preach_meeting_read_times_text_view);
            favoriteImgButton = (ImageButton) findViewById(R.id.preach_meeting_favor_image_button);
            Bundle bundle = getIntent().getExtras();
            preachmeetingConciseRsp = bundle.getParcelable("preachmeeting");
            beginTimeTxtView.setText(preachmeetingConciseRsp.getRunningTimeBegin());
            endTimeTxtView.setText(preachmeetingConciseRsp.getRunningTimeEnd());
            placeTxtView.setText(preachmeetingConciseRsp.getRunPlace());
            universityTxtView.setText(preachmeetingConciseRsp.getRunningUniversity());
            preachMeetingTxtView.setText(preachmeetingConciseRsp.getPreachMeetingName());
            readTimesTxtView.setText("" + preachmeetingConciseRsp.getReadTimes());
            if (preachmeetingConciseRsp.isFavorite) {
                favoriteImgButton.setSelected(true);
            } else {
                favoriteImgButton.setSelected(false);
            }
            favoriteImgButton.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageButton favorImageButton = (ImageButton) view;
                    if (favorImageButton.isSelected()) {
                        preachmeetingConciseRsp.isFavorite = false;
                        favorImageButton.setSelected(false);
                        ContentResolver resolver = getContentResolver();
                        String mSelectionClause = "preach_meeting_id = CAST(? AS INT)";
                        String[] mSelectionArgs = {String.valueOf(preachmeetingConciseRsp.preachMeetingId)};
                        int val = resolver.delete(MessageProvider.FAVOR_PREACH_MEETING_URI, mSelectionClause, mSelectionArgs);
                    } else {
                        preachmeetingConciseRsp.isFavorite = true;
                        favorImageButton.setSelected(true);
                        //存入数据库
                        ContentResolver resolver = getContentResolver();
                        ContentValues contentValues = new ContentValues();
                        Common.putPreachMeeting(contentValues, preachmeetingConciseRsp);
                        resolver.insert(MessageProvider.FAVOR_PREACH_MEETING_URI, contentValues);
                    }
                }
            });
            webView = (MyWebView) findViewById(R.id.preach_meeting_web_view);
            ContentResolver resolver = getContentResolver();
            String selection = "preach_meeting_id = CAST(? AS INT)";
            String[] selectionArgs = {String.valueOf(preachmeetingConciseRsp.preachMeetingId)};
            Cursor cursor = resolver.query(MessageProvider.READ_PREACH_INSTRUCTION_URI, null, selection, selectionArgs, null);
            String instruction = null;
            if (cursor != null) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    instruction = cursor.getString(cursor.getColumnIndex("instruction"));
                }
            }
            if (instruction != null) {
                String summary = "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"><body>"
                        + instruction;
                webView.setData(summary);
            } else {
                queryPreaching(preachmeetingConciseRsp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void queryPreaching(PreachmeetingConciseRsp preachmeetingConciseRsp) {
        QueryPreaching queryPreaching = new QueryPreaching();
        PreachmeetingReq preachmeetingReq = new PreachmeetingReq();
        preachmeetingReq.preachMeetingId = preachmeetingConciseRsp.getPreachMeetingId();
        queryPreaching.queryPreachInstruction(preachmeetingReq, this);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            //此处可以更新UI
            Bundle bundle = msg.getData();
            int dataType = bundle.getInt("type");
            if (dataType == Common.PREACH_MEETING_TYPE) {
                PreachmeetingRsp preachmeetingRsp = bundle.getParcelable("detail");
                String summary = "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"><body>"
                        + preachmeetingRsp.instruction;
                webView.loadData(summary, "text/html; charset=utf8", null);

                String selection = "preach_meeting_id = CAST(? AS INT)";
                String[] selectionArgs = {String.valueOf(preachmeetingRsp.preachMeetingId)};
                ContentResolver resolver = getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put("instruction", preachmeetingRsp.instruction);
                if (resolver.query(MessageProvider.READ_PREACH_INSTRUCTION_URI, null, selection, selectionArgs, null) != null) {
                    resolver.update(MessageProvider.READ_PREACH_INSTRUCTION_URI, contentValues, selection, selectionArgs);
                } else {
                    contentValues.put("preach_meeting_id", preachmeetingRsp.preachMeetingId);
                    resolver.insert(MessageProvider.READ_PREACH_INSTRUCTION_URI, contentValues);
                }
            }
        }
    };

    @Override
    public void getDataFromBusiness(int dataType, Object t, int... position) {
        Message message = null;
        Bundle bundle = new Bundle();
        message = new Message();
        if (dataType == Common.PREACH_MEETING_TYPE) {
            PreachmeetingRsp preachmeetingRsp = (PreachmeetingRsp) t;
            bundle.putInt("type", Common.PREACH_MEETING_TYPE);
            bundle.putParcelable("detail", preachmeetingRsp);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }

    void goHome(){
        if (fromWhere.equals(CampusActivity.class.getName())) {
            Intent intent = new Intent(this, CampusActivity.class);
            startActivity(intent);
        } else if (fromWhere.equals(UniversityCampusActivity.class.getName())) {
            Intent intent = new Intent(this, UniversityCampusActivity.class);
            startActivity(intent);
        }
        //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goHome();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goHome();
                break;
            case R.id.preach_instruction_activity_share:
                String shareContent = "宣讲会：" + preachmeetingConciseRsp.preachMeetingName +
                        "\n所在大学：" + preachmeetingConciseRsp.runningUniversity +
                        "\n举办地点：" + preachmeetingConciseRsp.runPlace +
                        "\n开始时间：" + preachmeetingConciseRsp.runningTimeBegin +
                        "\n结束时间：" + preachmeetingConciseRsp.runningTimeEnd +
                        "\n __来自<南京招聘会APP客户端>";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, shareContent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.preach_instruction_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

}