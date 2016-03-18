package com.weiyitech.zhaopinzh.presentation.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-12-3
 * Time: 上午9:54
 * To change this template use File | Settings | File Templates.
 */
public class JobViewPagerAdapter extends PagerAdapter {
    // 界面列表
    public List<View> views;
    private int mChildCount = 0;
    private View mCurrentView;

    public JobViewPagerAdapter(List<View> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    /*@Override
    public Object instantiateItem(View container, int position) {
        return super.instantiateItem(container, position);
    }  */
    // 初始化arg1位置的界面
    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(views.get(position), 0);
        return views.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentView = (View) object;
    }

    public View getPrimaryItem() {
        return mCurrentView;
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }
}
