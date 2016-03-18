package com.weiyitech.zhaopinzh.presentation.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.activity.SearchConditionActivity;
import com.weiyitech.zhaopinzh.util.Common;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-2-11
 * Time: 下午7:38
 * To change this template use File | Settings | File Templates.
 */
public class SearchConditionListAdapter extends BaseAdapter {
    List<String> conditonList;
    SearchConditionActivity activity;
    int mType;

    public SearchConditionSelectedListAdapter mSearchConditionSelectedListAdapter;
    public List<String> mStringArraySelectedList;

    public static class ViewHolder {
        public View listItem;
        public TextView contentTxtView;
        public ImageView selectedImgView;
    }

    public SearchConditionListAdapter(Activity context, List<String> conditonList, int type) {
        super();
        this.conditonList = conditonList;
        activity = (SearchConditionActivity) context;
        mType = type;
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
            v = vi.inflate(R.layout.search_condition_activity_all_list_item, null);
            holder = new ViewHolder();
            holder.listItem = v;
            holder.contentTxtView = (TextView) v.findViewById(R.id.search_condition_activity_item_text_view);
            holder.selectedImgView = (ImageView) v.findViewById(R.id.search_condition_activity_item_image_view);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.contentTxtView.setText(conditonList.get(position));
        if (mStringArraySelectedList.contains(conditonList.get(position))) {
            holder.selectedImgView.setSelected(true);
        } else {
            holder.selectedImgView.setSelected(false);
        }

        holder.selectedImgView.setTag(conditonList.get(position));
        holder.listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View selectedIndicaterView = v.findViewById(R.id.search_condition_activity_item_image_view);
                if (selectedIndicaterView.isSelected()) {
                    for (String item : mStringArraySelectedList) {
                        if (item.equals((String) selectedIndicaterView.getTag())) {
                            if (conditonList.indexOf(item) != 0) {
                                selectedIndicaterView.setSelected(false);
                                mStringArraySelectedList.remove(item);
                            }
                            break;
                        }
                    }
                } else {
                    String viewValue = (String) selectedIndicaterView.getTag();
                    if (conditonList.indexOf(viewValue) == 0 || mType == Common.PUBLISH_TYPE
                            || mType == Common.EDUCATION_TYPE || mType == Common.EXPERIENCE_TYPE) {
                        mStringArraySelectedList.clear();
                    } else {
                        if (mStringArraySelectedList.contains(conditonList.get(0))) {
                            mStringArraySelectedList.remove(conditonList.get(0));
                        }
                    }
                    selectedIndicaterView.setSelected(true);
                    mStringArraySelectedList.add(viewValue);
                    notifyDataSetChanged();
                }
                mSearchConditionSelectedListAdapter.notifyDataSetChanged();
            }
        });
        return v;

    }
}
