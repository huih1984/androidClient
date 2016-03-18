package com.weiyitech.zhaopinzh.presentation.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.adapter.SearchConditionListAdapter;
import com.weiyitech.zhaopinzh.presentation.adapter.SearchConditionSelectedListAdapter;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.SearchConditionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: john
 * Date: 14-11-12
 * Time: 上午11:19
 * To change this template use File | Settings | File Templates.
 */
public class SearchConditionActivity extends CommonActivity implements Animation.AnimationListener {
    ListView selectedListView;
    ListView allListView;
    ImageButton toExpendButton;
    SearchConditionListAdapter searchConditionListAdapter;
    SearchConditionSelectedListAdapter searchConditionSelectedListAdapter;
    List<String> stringArrayAllList;
    List<String> stringArraySelectedList;
    List<String> oldSelectedList;
    int type;

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (selectedListView.getVisibility() == View.GONE) {
            toExpendButton.setImageResource(R.drawable.to_close_icon);
        } else {
            toExpendButton.setImageResource(R.drawable.to_expend_icon);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_condition_activity);
        selectedListView = (ListView) findViewById(R.id.search_condition_activity_selected_list_view);
        allListView = (ListView) findViewById(R.id.search_condition_activity_all_list_view);
        toExpendButton = (ImageButton) findViewById(R.id.search_condition_activity_selected_img_btn);
        toExpendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation rotateAnimation = AnimationUtils.loadAnimation(SearchConditionActivity.this, R.anim.rotate_180);
                LinearInterpolator lin = new LinearInterpolator();
                rotateAnimation.setInterpolator(lin);
                v.startAnimation(rotateAnimation);
                rotateAnimation.setAnimationListener(SearchConditionActivity.this);
                if (selectedListView.getVisibility() == View.VISIBLE) {
                    selectedListView.setVisibility(View.GONE);
                } else {
                    selectedListView.setVisibility(View.VISIBLE);
                }
            }
        });
        type = getIntent().getIntExtra("type", -1);
        if (type == Common.INDUSTRY_TYPE) {
            stringArrayAllList = SearchConditionUtil.getIndustryList();
        } else if (type == Common.JOB_TYPE) {
            stringArrayAllList = SearchConditionUtil.getJobTypeList();
        } else if (type == Common.PUBLISH_TYPE) {
            stringArrayAllList = SearchConditionUtil.getPublishTimeList();
        } else if (type == Common.EDUCATION_TYPE) {
            stringArrayAllList = SearchConditionUtil.getEduReqList();
        } else if (type == Common.EXPERIENCE_TYPE) {
            stringArrayAllList = SearchConditionUtil.getExpReqList();
        }
        if(stringArrayAllList == null){
            Log.e("SearchConditionActivity type = ", "" + type);
            stringArrayAllList = new ArrayList<String>();
            return;
        }
        searchConditionListAdapter = new SearchConditionListAdapter(this, stringArrayAllList, type);
        allListView.setAdapter(searchConditionListAdapter);

        stringArraySelectedList = SearchConditionUtil.getSettings(type);
        oldSelectedList = new ArrayList<String>(stringArraySelectedList);
        searchConditionSelectedListAdapter = new SearchConditionSelectedListAdapter(this, stringArraySelectedList);
        selectedListView.setAdapter(searchConditionSelectedListAdapter);
        selectedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stringArraySelectedList.remove(view.findViewById(R.id.search_condition_activity_selected_item_btn).getTag());
                searchConditionSelectedListAdapter.notifyDataSetChanged();
                searchConditionListAdapter.notifyDataSetChanged();
            }
        });

        searchConditionListAdapter.mStringArraySelectedList = stringArraySelectedList;
        searchConditionListAdapter.mSearchConditionSelectedListAdapter = searchConditionSelectedListAdapter;

        searchConditionSelectedListAdapter.mSearchConditionListAdapter = searchConditionListAdapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search_condition_activity_actions, menu);
        return true;
    }

    void goHome() {
        boolean changed = true;
        if (stringArraySelectedList != null && oldSelectedList != null) {
            if (stringArraySelectedList.size() != oldSelectedList.size()) {
                changed = true;
            } else {
                changed = false;
                for (int i = 0; i < stringArraySelectedList.size(); ++i) {
                    if (!stringArraySelectedList.get(i).equals(oldSelectedList.get(i))) {
                        changed = true;
                        break;
                    }
                }
            }
        } else {
            Log.e("SearchConditionActivity", "stringArraySelectedList为空或者oldSelectedList为空");
        }
        if (changed) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("保存");
            builder.setMessage("您还没有保存，是否放弃保存修改！");

            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setClass(SearchConditionActivity.this, SearchActivity.class);
                    SearchConditionActivity.this.setResult(RESULT_OK, intent);
                    SearchConditionActivity.this.finish();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
//            Intent intent = new Intent();
//            intent.setClass(SearchConditionActivity.this, SearchActivity.class);
//            startActivity(intent);
            Intent intent = new Intent();
            intent.setClass(SearchConditionActivity.this, SearchActivity.class);
            SearchConditionActivity.this.setResult(RESULT_OK, intent);
            this.finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.search_condition_activity_action_save:
                SearchConditionUtil.saveSettings(type, stringArraySelectedList);
                oldSelectedList = stringArraySelectedList;
                break;
        }
        return true;
    }
}