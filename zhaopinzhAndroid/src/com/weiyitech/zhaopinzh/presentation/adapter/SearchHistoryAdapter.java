package com.weiyitech.zhaopinzh.presentation.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-11-24
 * Time: 上午10:46
 * To change this template use File | Settings | File Templates.
 */
public class SearchHistoryAdapter extends BaseAdapter {

    private ArrayList<String> searchHistoryList;
    private Activity activity;

    public SearchHistoryAdapter(Activity context, ArrayList<String> searchHistoryList) {
        super();
        this.searchHistoryList = searchHistoryList;
        activity = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
