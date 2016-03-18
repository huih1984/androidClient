package com.weiyitech.zhaopinzh.util.fragment;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.widget.Toast;
import com.weiyitech.zhaopinzh.presentation.R;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-11-23
 * Time: 上午11:12
 * To change this template use File | Settings | File Templates.
 */
public class TabListener implements ActionBar.TabListener {
    private TabContentFragment mFragment;

    public TabListener(TabContentFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        //ft.add(R.id.fragment_content, mFragment, mFragment.getText());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.remove(mFragment);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // Toast.makeText(getApplicationContext(), "Reselected!", Toast.LENGTH_SHORT).show();
    }

}
