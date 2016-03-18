package com.weiyitech.zhaopinzh.util.ImageGallery;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-10-16
 * Time: 下午1:51
 * To change this template use File | Settings | File Templates.
 */

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Gallery;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 图片gallery 每次只能滑动一个
 *
 * @author cmm
 * @version 1.0
 * @date 2012-02-02
 */

public class ImageGallery extends Gallery {

    public List<View> dotViewList;
    private static final int timerAnimation = 1;
    private static final int time = 4000;
    private final Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case timerAnimation:
                    int position = getSelectedItemPosition();

                    Log.i("msg", "position:" + position);
                    if (position >= (getCount() - 1)) {
                        setSelection(0);
                        dotViewList.get(0).setEnabled(true);
                        dotViewList.get(getCount() - 1).setEnabled(false);
                        //onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
                    } else {
                        dotViewList.get(position).setEnabled(false);
                        dotViewList.get(position + 1).setEnabled(true);
                        setSelection(position + 1);
                        //onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };

    private final Timer timer = new Timer();
    private final TimerTask task = new TimerTask() {
        public void run() {
            mHandler.sendEmptyMessage(timerAnimation);
        }
    };

    public ImageGallery(Context paramContext) {
        super(paramContext);
        timer.schedule(task, time, time);
    }

    public ImageGallery(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        timer.schedule(task, time, time);

    }

    public ImageGallery(Context paramContext, AttributeSet paramAttributeSet,
                        int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        timer.schedule(task, time, time);

    }

    private boolean isScrollingLeft(MotionEvent paramMotionEvent1,
                                    MotionEvent paramMotionEvent2) {
        float f2 = paramMotionEvent2.getX();
        float f1 = paramMotionEvent1.getX();
        if (f2 > f1)
            return true;
        return false;
    }

    public boolean onFling(MotionEvent paramMotionEvent1,
                           MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
        int keyCode;
        if (isScrollingLeft(paramMotionEvent1, paramMotionEvent2)) {
            int selPos = getSelectedItemPosition();
            if (selPos > 0) {
                setSelection(selPos - 1);
                dotViewList.get(selPos).setEnabled(false);
                dotViewList.get(selPos - 1).setEnabled(true);
            }
            //keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
        } else {
            int selPos = getSelectedItemPosition();
            if (selPos < getCount() - 1 ) {
                setSelection(selPos + 1);
                dotViewList.get(selPos).setEnabled(false);
                dotViewList.get(selPos + 1).setEnabled(true);
            }
            //keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
        }
        // onKeyDown(keyCode, null);
        return true;
    }

    public void destroy() {
        timer.cancel();
    }

    public List<View> getDotViews() {
        return dotViewList;
    }

    public void setDotViews(List<View> dotView) {
        this.dotViewList = dotView;
    }
}