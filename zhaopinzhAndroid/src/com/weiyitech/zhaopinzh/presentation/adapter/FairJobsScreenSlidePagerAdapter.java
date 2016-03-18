package com.weiyitech.zhaopinzh.presentation.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import com.weiyitech.zhaopinzh.presentation.component.FairJobsScreenSlidePageFragment;
import com.weiyitech.zhaopinzh.presentation.component.ScreenSlidePageFragment;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-2-23
 * Time: 下午6:40
 * To change this template use File | Settings | File Templates.
 */
public class FairJobsScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    public int pageCount;

    public Context context;
    public ViewPager viewPager;

    public FairJobsScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public FairJobsScreenSlidePageFragment getItem(int position) {
        FairJobsScreenSlidePageFragment fairJobsScreenSlidePageFragment = FairJobsScreenSlidePageFragment.create(position);
        return fairJobsScreenSlidePageFragment;
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
}
