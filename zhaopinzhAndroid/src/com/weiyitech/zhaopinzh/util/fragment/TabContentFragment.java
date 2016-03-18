package com.weiyitech.zhaopinzh.util.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.weiyitech.zhaopinzh.presentation.R;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-11-23
 * Time: 上午11:08
 * To change this template use File | Settings | File Templates.
 */

public class TabContentFragment extends Fragment {
    private String mText;

    public TabContentFragment() {

    }

    public TabContentFragment(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.action_bar_tab_content, container, false);
//
//        TextView text = (TextView) fragView.findViewById(R.id.text);
//        text.setText(mText);

        return fragView;
    }
}