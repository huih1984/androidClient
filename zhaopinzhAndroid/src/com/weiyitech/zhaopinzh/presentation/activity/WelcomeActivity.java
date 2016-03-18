package com.weiyitech.zhaopinzh.presentation.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.adapter.WelcomeViewPagerAdapter;
import com.weiyitech.zhaopinzh.util.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-10-8
 * Time: 下午6:56
 * To change this template use File | Settings | File Templates.
 */
public class WelcomeActivity extends Activity implements ViewPager.OnPageChangeListener {

    private ViewPager vp;
    private WelcomeViewPagerAdapter vpAdapter;
    private List<View> views;

    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        // 初始化页面
        initViews();

        // 初始化底部小点
        initDots();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);

        views = new ArrayList<View>();
        // 初始化引导图片列表
        views.add(inflater.inflate(R.layout.welcome_activity_one, null));
        views.add(inflater.inflate(R.layout.welcome_activity_two, null));
        views.add(inflater.inflate(R.layout.welcome_activity_three, null));

        // 初始化Adapter
        vpAdapter = new WelcomeViewPagerAdapter(views, this);

        vp = (ViewPager) findViewById(R.id.welcome_activity_viewpager);
        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(this);
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[views.size()];

        // 循环取得小点图片
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(false);// 都设为灰色
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(true);// 设置为白色，即选中状态
    }

    private void setCurrentDot(int position) {
        if (position < 0 || position > views.size() - 1
                || currentIndex == position) {
            return;
        }

//        dots[position].setEnabled(true);
//        dots[currentIndex].setEnabled(false);
        dots[position].setImageResource(R.drawable.dot_focused);
        dots[currentIndex].setImageResource(R.drawable.dot_normal);
        currentIndex = position;
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        // 设置底部小点选中状态
        setCurrentDot(arg0);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Common.getIsUNSpecified(this).equals("竖屏显示")) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        ((ZhaopinzhApp) getApplicationContext()).setActiveActivity(this.getClass().getName());
    }
}
