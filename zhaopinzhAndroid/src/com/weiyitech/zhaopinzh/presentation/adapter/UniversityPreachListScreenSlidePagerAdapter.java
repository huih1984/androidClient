package com.weiyitech.zhaopinzh.presentation.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import com.weiyitech.zhaopinzh.presentation.component.PreachsScreenSlidePageFragment;
import com.weiyitech.zhaopinzh.presentation.component.UniversityPreachingMeetListFragment;
import com.weiyitech.zhaopinzh.presentation.component.UniversityPreachsScreenSlidePagerFragment;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-5-27
 * Time: 下午2:50
 * To change this template use File | Settings | File Templates.
 */
public class UniversityPreachListScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    public int pageCount;

    public Context context;
    public ViewPager viewPager;

    public UniversityPreachListScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public UniversityPreachsScreenSlidePagerFragment getItem(int position) {
        UniversityPreachsScreenSlidePagerFragment universityPreachsScreenSlidePagerFragment = UniversityPreachsScreenSlidePagerFragment.create(position);
        return universityPreachsScreenSlidePagerFragment;
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