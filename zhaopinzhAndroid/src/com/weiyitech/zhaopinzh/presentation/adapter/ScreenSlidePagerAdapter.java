package com.weiyitech.zhaopinzh.presentation.adapter;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-12-10
 * Time: 下午2:17
 * To change this template use File | Settings | File Templates.
 */

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import com.weiyitech.zhaopinzh.presentation.component.ScreenSlidePageFragment;
import com.weiyitech.zhaopinzh.struct.RunEmployerComponent;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
 * sequence.
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    public int pageCount;

    public Context context;
    public ViewPager viewPager;

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public ScreenSlidePageFragment getItem(int position) {
        ScreenSlidePageFragment screenSlidePageFragment = ScreenSlidePageFragment.create(position);
        return screenSlidePageFragment;
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