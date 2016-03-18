package com.weiyitech.zhaopinzh.util.imageviewpager;

import android.app.Activity;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import com.weiyitech.zhaopinzh.presentation.R;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-10-17
 * Time: 下午3:33
 * 传入viewpager,图片列表，自动滚动播放
 */


public class ImageViewPager {
    public Activity activity;
    public ViewPager viewPager;
    public List<ImageView> imageViewList;
    public List<ImageView> dots; // 图片标题正文的那些点

    public ImageViewPager(Activity activity, ViewPager viewPager, List<ImageView> imageViewList, List<ImageView> dots) {
        this.activity = activity;
        this.viewPager = viewPager;
        this.imageViewList = imageViewList;
        this.dots = dots;
        viewPager.setOnPageChangeListener(new ImageViewPageChangeListener());
    }

    private int currentItem;

    private ScheduledExecutorService scheduledExecutorService;

    // 切换当前显示的图片
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
        }

        ;
    };

    public void onStart(int period) {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, period, TimeUnit.SECONDS);
    }

    public void onStop() {
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
        }
    }

    public boolean isShutdown() {
        return scheduledExecutorService.isShutdown();
    }

    /**
     * 换行切换任务
     *
     * @author Administrator
     */
    private class ScrollTask implements Runnable {

        public void run() {
            synchronized (viewPager) {
                System.out.println("currentItem: " + currentItem);
                currentItem = (currentItem + 1) % imageViewList.size();
                handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
            }
        }

    }

    /**
     * 当ViewPager中页面的状态发生改变时调用
     *
     * @author Administrator
     */
    private class ImageViewPageChangeListener implements ViewPager.OnPageChangeListener {
        private int oldPosition = 0;

        /**
         * This method will be invoked when a new page becomes selected.
         * position: Position index of the new selected page.
         */
        public void onPageSelected(int position) {
            currentItem = position;
//            dots.get(oldPosition).setImageResource(R.drawable.dot_normal);
//            dots.get(position).setImageResource(R.drawable.dot_focused);
            dots.get(oldPosition).setEnabled(false);
            dots.get(position).setEnabled(true);
            oldPosition = position;
        }

        public void onPageScrollStateChanged(int arg0) {

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }

}
