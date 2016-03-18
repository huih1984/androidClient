package com.weiyitech.zhaopinzh.presentation.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.activity.SearchActivity;
import com.weiyitech.zhaopinzh.presentation.activity.SearchConditionActivity;
import com.weiyitech.zhaopinzh.util.Common;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-2-11
 * Time: 下午7:38
 * To change this template use File | Settings | File Templates.
 */
public class SearchSettingListAdapter extends BaseAdapter {
    List<Map<String, ?>> conditonList;
    SearchActivity activity;

    public static class ViewHolder {
        public View listItem;

        public TextView nameTxtView;
        public TextView contentTxtView;
    }

    public SearchSettingListAdapter(Activity context, List<Map<String, ?>> conditonList) {
        super();
        this.conditonList = conditonList;
        activity = (SearchActivity) context;
    }

    @Override
    public int getCount() {
        return conditonList.size();
    }

    @Override
    public Object getItem(int position) {
        return conditonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
        LayoutInflater vi =
                (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.search_activity_list_item, null);
        holder = new ViewHolder();
        holder.listItem = v;
        holder.nameTxtView = (TextView) v.findViewById(R.id.search_activity_item_name_text_view);
        holder.contentTxtView = (TextView) v.findViewById(R.id.search_activity_item_content_text_view);
        v.setTag(holder);
        }else{
            holder = (ViewHolder) v.getTag();
        }
        String keyStr =  conditonList.get(position).keySet().iterator().next();
        holder.nameTxtView.setText(keyStr);
        holder.contentTxtView.setText((String)conditonList.get(position).get(keyStr));
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                TextView textView =  (TextView)v.findViewById(R.id.search_activity_item_name_text_view);
                intent.setClass(activity, SearchConditionActivity.class);
                if(textView.getText().equals("行业")){
                    intent.putExtra("type", Common.INDUSTRY_TYPE);
                } else if(textView.getText().equals("职位")){
                    intent.putExtra("type", Common.JOB_TYPE);
                } else if(textView.getText().equals("发布时间")){
                    intent.putExtra("type", Common.PUBLISH_TYPE);
                } else if(textView.getText().equals("经验要求")){
                    intent.putExtra("type", Common.EXPERIENCE_TYPE);
                } else if(textView.getText().equals("学历要求")){
                    intent.putExtra("type", Common.EDUCATION_TYPE);
                }
                activity.startActivityForResult(intent, SearchActivity.SEARCH_SETTING_REQ_FLAG);
                activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        return v;

    }
}
