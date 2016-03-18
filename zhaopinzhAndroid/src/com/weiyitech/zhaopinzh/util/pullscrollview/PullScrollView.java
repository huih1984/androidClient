package com.weiyitech.zhaopinzh.util.pullscrollview;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-11-9
 * Time: 下午4:34
 * To change this template use File | Settings | File Templates.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import com.weiyitech.zhaopinzh.presentation.component.ActionSlideExpandableListView;
import com.weiyitech.zhaopinzh.util.slideexpandablelistview.*;

/**
 * 自定义ScrollView实现ListView的下拉刷新和上拉加载更多
 *
 * @author Macro
 */
public class PullScrollView extends ScrollView {
    // pull state 上拉开始加载更多
    public static final int PULL_UP_STATE = 0;
    // pull state 下拉开始刷新
    public static final int PULL_DOWN_STATE = 1;
    // release states 释放 去刷新
    public static final int RELEASE_TO_REFRESH = 3;
    // release states 释放 去加载更多
    public static final int RELEASE_TO_LOADING = 4;
    // 正在刷新
    public static final int REFRESHING = 5;
    // 正在加载更多
    public static final int LOADING = 6;
    // 没做任何操作
    private static final int DONE = 7;
    // 实际的padding的距离与界面上偏移距离的比例
    private final static int RATIO = 2;

    private LinearLayout innerLayout;
    private LinearLayout bodyLayout;

    private FooterView footerView;
    public View bodyView;
    private int headContentHeight;
    private int footContentHeight;
    // Down 初始化 Y
    private int startY;
    private int scrollY = -1;

    private Context mContext;
    // 上下拉状态
    private int pullState;
    // 刷新、加载更多 接口
    private OnPullListener onPullListener;

    public PullScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PullScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullScrollView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        innerLayout = new LinearLayout(context);
        innerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));
        innerLayout.setOrientation(LinearLayout.VERTICAL);
        // 设置 bodyLayout 区域
        bodyLayout = new LinearLayout(context);
        bodyLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));
        bodyLayout.setOrientation(LinearLayout.VERTICAL);
        this.addView(innerLayout);
        innerLayout.addView(bodyLayout);
        // 初始化刷新、加载状态
        pullState = DONE;
    }


    /**
     * 添加 footerView
     */
    private void addFooterView() {
        footerView = new FooterView(mContext);
        measureView(footerView);
        footContentHeight = footerView.getMeasuredHeight();
        footerView.setPaddingButtom();
        footerView.invalidate();
        innerLayout.addView(footerView);
    }

    /**
     * 添加 BodyView : 滑动内容区域
     */
    public void addBodyView(View view) {
        bodyLayout.addView(view);
    }

    /**
     * footer view 在此添加保证添加到 innerLayout 中的最后
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addFooterView();
    }

    /**
     * 滑动时，首先会触发 onInterceptTouchEvent事件，然后触发 onTouchEvent 事件时
     * <p/>
     * onInterceptTouchEvent 总是将 onTouchEvent 事件中的 ACTION_DOWN 事件拦截
     * <p/>
     * 所以在此做监听，以防万一
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 首先拦截down事件,记录y坐标
                scrollY = getScrollY();
                startY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
        }
        return super.onInterceptTouchEvent(event);
    }

    /**
     * 监听上下拉手势操作
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        onScrollViewMoveListener.onScrollMove(event);
        if (isLoadable()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    scrollY = getScrollY();
                    startY = (int) event.getY();

                    break;
                case MotionEvent.ACTION_MOVE:
                    int tempY = (int) event.getY() - startY;
                    if (tempY < 0) { // 上拉加载更多
                        changeFooterViewHeight(tempY);
                    }

                    break;

                case MotionEvent.ACTION_UP:
                    // 重置 headerView、footerView ,激化监听
                    resetPullStateForActionUp();

                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    /*
         * 改变 footerView 高度
         */
    private void changeFooterViewHeight(int tempY) {
        if (footerView.getVisibility() == View.GONE) {
            return;
        }
        if (getChildAt(0).getMeasuredHeight() <= getScrollY() + getHeight()) {
            if (pullState == DONE) {
                pullState = footerView.setStartLoad();
            }
        }
        if (pullState == PULL_UP_STATE || pullState == RELEASE_TO_LOADING) {
            pullState = footerView.setPadding(footContentHeight,
                    (Math.abs(-tempY) / RATIO));
        }
    }

    /*
     * 当手离开屏幕后重置 pullState 状态及 footerView，激活监听事件
     */
    private void resetPullStateForActionUp() {
        if (pullState != REFRESHING && pullState != LOADING) {

            // 松开手加载更多
            if (pullState == RELEASE_TO_LOADING) {
                pullState = footerView.setLoading();
                footerView.setPaddingButtom();
                onPullListener.loadMore();
            }
            // 重置到最初状态
            else {
                footerView.setPaddingButtom();
                pullState = DONE;
            }
        }
    }

    /**
     * 判断是否可以上下拉刷新加载手势
     *
     * @return true：可以
     */
    private boolean isLoadable() {
        if (pullState == REFRESHING || pullState == LOADING)
            return false;
        return true;
    }


    /**
     * 加载更多按钮不可见
     */
    public void setFooterViewGone() {
        footerView.setStartLoad();
        footerView.hide();
        pullState = DONE;
    }

    /**
     * 显示加载按钮
     */
    public void setFooterViewVisible() {
        footerView.setVisibility(View.VISIBLE);
    }

    /**
     * 加载更多按钮重置为加载更多状态
     */
    public void setFooterViewReset() {
        footerView.setStartLoad();
        pullState = DONE;
    }


    /**
     * 如果：高度>0,则有父类完全决定子窗口高度大小；否则，由子窗口自己觉得自己的高度大小
     * <p/>
     * 设置 headerView、HootViwe 的 LayoutParams 属性
     *
     * @param childView
     */
    private void measureView(View childView) {
        ViewGroup.LayoutParams p = childView.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        childView.measure(childWidthSpec, childHeightSpec);
    }

    public interface OnPullListener {
        void loadMore();
    }

    public void setOnPullListener(OnPullListener onPullListener) {
        this.onPullListener = onPullListener;
    }

    OnScrollViewMoveListener onScrollViewMoveListener;

    public interface OnScrollViewMoveListener {
        void onScrollMove(MotionEvent event);
    }

    public void setOnScrollViewMoveListener(OnScrollViewMoveListener onScrollViewMoveListener) {
        this.onScrollViewMoveListener = onScrollViewMoveListener;
    }

}