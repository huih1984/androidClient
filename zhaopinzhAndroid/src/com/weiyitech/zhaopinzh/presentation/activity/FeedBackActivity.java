package com.weiyitech.zhaopinzh.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.business.AddFeedback;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.struct.FeedbackReq;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-1-22
 * Time: 下午3:07
 * To change this template use File | Settings | File Templates.
 */
public class FeedBackActivity extends ActionBarActivity {
    public ArrayList<String> stringArrayList;
    ViewHolder viewHolders[];

    public static class ViewHolder {
        public RelativeLayout frontRelativeLayout;
        public RelativeLayout backRelativeLayout;
        public EditText editText;
        public TextView textView;
        public CheckBox checkBox;
        public Button button;
        public Button sureButton;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        stringArrayList = new ArrayList<String>();
        stringArrayList.add("招聘会信息不够全面");
        stringArrayList.add("信息浏览层次不够分明");
        stringArrayList.add("手势不符合您的操作习惯");
        stringArrayList.add("出现运行错误或不够稳定等软件质量问题");
        stringArrayList.add("其它问题");
        TableLayout tableLayout = (TableLayout) findViewById(R.id.feedback_activity_table_layout);
        viewHolders = new ViewHolder[stringArrayList.size()];
        
        for(int i = 0; i < tableLayout.getChildCount() - 1 ; ++i){
            TableRow tableRow =  (TableRow)tableLayout.getChildAt(i);
            View v = tableRow.findViewById(R.id.feedback_activity_list_item);
            viewHolders[i] = new ViewHolder();
            viewHolders[i].backRelativeLayout = (RelativeLayout) v.findViewById(R.id.feedback_activity_back);
            viewHolders[i].frontRelativeLayout = (RelativeLayout) v.findViewById(R.id.feedback_activity_front);
            viewHolders[i].checkBox = (CheckBox) v.findViewById(R.id.feedback_activity_checkbox);
            viewHolders[i].button = (Button) v.findViewById(R.id.feedback_activity_button);
            viewHolders[i].textView = (TextView) v.findViewById(R.id.feedback_activity_front_textView);
            viewHolders[i].editText = (EditText) v.findViewById(R.id.feedback_activity_suggestion);
            viewHolders[i].sureButton = (Button) v.findViewById(R.id.feedback_activity_back_button);

            viewHolders[i].textView.setText(stringArrayList.get(i));
            viewHolders[i].checkBox.setTag(viewHolders[i].frontRelativeLayout);
            viewHolders[i].checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        RelativeLayout relativeLayout = (RelativeLayout) buttonView.getTag();
                        int margin = (int) (100 * ZhaopinzhApp.getInstance().hardDensity);
                        float xDelta = (((float)margin)/ZhaopinzhApp.getInstance().widthPixels);
                        TranslateAnimation translateAnimation =  new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, -xDelta, 0,0,0,0);
                        translateAnimation.setInterpolator(new LinearInterpolator());
                        translateAnimation.setDuration(300);
                        translateAnimation.setFillAfter(false);
                        relativeLayout.setAnimation(translateAnimation);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();

                        layoutParams.leftMargin = -margin;
                        layoutParams.rightMargin = 0;
                        relativeLayout.setLayoutParams(layoutParams);
                        relativeLayout.requestLayout();
                    } else {
                        RelativeLayout relativeLayout = (RelativeLayout) buttonView.getTag();
                        int margin = (int) (100 * ZhaopinzhApp.getInstance().hardDensity);
                        float xDelta = (((float)100.0)/ZhaopinzhApp.getInstance().widthPixels);
                        TranslateAnimation translateAnimation =  new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, xDelta, 0,0,0,0);
                        translateAnimation.setInterpolator(new LinearInterpolator());
                        translateAnimation.setDuration(300);
                        translateAnimation.setFillAfter(false);
                        relativeLayout.setAnimation(translateAnimation);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();

                        layoutParams.leftMargin = 0;
                        layoutParams.rightMargin = -margin;
                        relativeLayout.setLayoutParams(layoutParams);
                        relativeLayout.requestLayout();
                    }
                }
            });

            viewHolders[i].button.setTag(viewHolders[i]);
            viewHolders[i].button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewHolder holder = (ViewHolder) v.getTag();
                    holder.frontRelativeLayout.setVisibility(View.INVISIBLE);
                    holder.backRelativeLayout.setVisibility(View.VISIBLE);
                    holder.editText.setFocusable(true);
                    holder.editText.setFocusableInTouchMode(true);
                    holder.editText.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            });

            viewHolders[i].sureButton.setTag(viewHolders[i]);
            viewHolders[i].sureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewHolder holder = (ViewHolder)v.getTag();
                    holder.frontRelativeLayout.setVisibility(View.VISIBLE);
                    holder.backRelativeLayout.setVisibility(View.INVISIBLE);
                    InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(holder.editText.getWindowToken(), 0);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.feedback_activity_actions, menu);
        return true;
    }

    void sendFeedback(){
        AddFeedback addFeedback = new AddFeedback();
        FeedbackReq feedbackReq = new FeedbackReq();
        feedbackReq.onFst = viewHolders[0].checkBox.isChecked() ? 1 : 0;
        feedbackReq.onScd = viewHolders[1].checkBox.isChecked() ? 1 : 0;
        feedbackReq.onThd = viewHolders[2].checkBox.isChecked() ? 1 : 0;
        feedbackReq.onFur = viewHolders[3].checkBox.isChecked() ? 1 : 0;
        feedbackReq.onFth = viewHolders[4].checkBox.isChecked() ? 1 : 0;
        if (feedbackReq.onFst == 1) {
            feedbackReq.contentFst = viewHolders[0].editText.getText().toString();
        }

        if (feedbackReq.onScd == 1) {
            feedbackReq.contentFst = viewHolders[1].editText.getText().toString();
        }

        if (feedbackReq.onThd == 1) {
            feedbackReq.contentFst = viewHolders[2].editText.getText().toString();
        }

        if (feedbackReq.onFur == 1) {
            feedbackReq.contentFst = viewHolders[3].editText.getText().toString();
        }

        if (feedbackReq.onFth == 1) {
            feedbackReq.contentFst = viewHolders[4].editText.getText().toString();
        }

        feedbackReq.email = ((EditText) findViewById(R.id.feedback_activity_emailText)).getText().toString();
        addFeedback.addFeedback(feedbackReq, this);
    }

    /*选择不同菜单的响应*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.feedback_activity_action_send:
                sendFeedback();
                Intent jobIntent = new Intent();
                jobIntent.setClass(this, TalkActivity.class);
                //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                startActivity(jobIntent);
                break;
            case android.R.id.home:
                // action bar中的应用程序图标被点击了，返回home
                Intent intent = new Intent(this, TalkActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onBackPressed() {
        // action bar中的应用程序图标被点击了，返回home
        Intent intent = new Intent(this, TalkActivity.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}