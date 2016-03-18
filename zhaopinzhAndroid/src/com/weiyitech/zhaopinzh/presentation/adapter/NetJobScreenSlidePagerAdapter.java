package com.weiyitech.zhaopinzh.presentation.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import com.weiyitech.zhaopinzh.presentation.component.NetJobConciseScreenSlidePageFragment;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-7-28
 * Time: 上午11:35
 * To change this template use File | Settings | File Templates.
 */
public class NetJobScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    public int pageCount;

    public Context context;
    public ViewPager viewPager;

    public NetJobScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public NetJobConciseScreenSlidePageFragment getItem(int position) {
        NetJobConciseScreenSlidePageFragment netJobConciseScreenSlidePageFragment = NetJobConciseScreenSlidePageFragment.create(position);
        return netJobConciseScreenSlidePageFragment;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

   /* @Override
    public Object instantiateItem(ViewGroup container, int position) {
        finishUpdate(container);
        ScreenSlidePageFragment f = (ScreenSlidePageFragment) super.instantiateItem(container, position);
        return f;
    }     */
}
