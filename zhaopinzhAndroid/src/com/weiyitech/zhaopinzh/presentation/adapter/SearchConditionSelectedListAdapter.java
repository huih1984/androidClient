package com.weiyitech.zhaopinzh.presentation.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.activity.SearchConditionActivity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-2-11
 * Time: 下午7:38
 * To change this template use File | Settings | File Templates.
 */
public class SearchConditionSelectedListAdapter extends BaseAdapter {
    List<String> mStringArraySelectedList;
    SearchConditionActivity activity;
    public SearchConditionListAdapter mSearchConditionListAdapter;
    public static class ViewHolder {
        public View listItem;
        public Button contentTxtView;
    }

    public SearchConditionSelectedListAdapter(Activity context, List<String> mStringArraySelectedList) {
        super();
        this.mStringArraySelectedList = mStringArraySelectedList;
        activity = (SearchConditionActivity) context;
    }

    @Override
    public int getCount() {
        return mStringArraySelectedList.size();
    }

    @Override
    public Object getItem(int position) {
        return mStringArraySelectedList.get(position);
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
        v = vi.inflate(R.layout.search_condition_activity_selected_list_item, null);
        holder = new ViewHolder();
        holder.listItem = v;
        holder.contentTxtView = (Button) v.findViewById(R.id.search_condition_activity_selected_item_btn);
        v.setTag(holder);
        }else{
            holder = (ViewHolder) v.getTag();
        }

        holder.contentTxtView.setText(mStringArraySelectedList.get(position));
        holder.contentTxtView.setTag(mStringArraySelectedList.get(position));
//        holder.listItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mStringArraySelectedList.remove(v.findViewById(R.id.search_condition_activity_selected_item_btn).getTag());
//                notifyDataSetChanged();
//                mSearchConditionListAdapter.notifyDataSetChanged();
//            }
//        });
        return v;

    }
}
