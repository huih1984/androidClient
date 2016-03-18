package com.weiyitech.zhaopinzh.util.imageviewpager;

/**
 * Created with IntelliJ IDEA.
 * User: john
 * Date: 14-11-3
 * Time: 下午4:24
 * To change this template use File | Settings | File Templates.
 */

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

/**
 * 填充ViewPager页面的适配器
 *
 * @author Administrator
 */
public class ImageViewPagerAdapter extends PagerAdapter {


    public List<ImageView> mImageViewList;

    public ImageViewPagerAdapter(List<ImageView> imageViewList) {
        mImageViewList = imageViewList;
    }

    @Override
    public int getCount() {
        return mImageViewList.size();
    }

    @Override
    public Object instantiateItem(View arg0, int arg1) {
        ((ViewPager) arg0).addView(mImageViewList.get(arg1));
        return mImageViewList.get(arg1);
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {

    }

    @Override
    public void finishUpdate(View arg0) {

    }
}
