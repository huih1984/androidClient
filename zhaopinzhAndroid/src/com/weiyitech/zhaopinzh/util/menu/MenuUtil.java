package com.weiyitech.zhaopinzh.util.menu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import com.weiyitech.zhaopinzh.presentation.R;

/**
 * Created with IntelliJ IDEA.
 * User: john
 * Date: 14-11-14
 * Time: 上午9:28
 * To change this template use File | Settings | File Templates.
 */
public class MenuUtil {

    public int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public void popUpMyOverflow(Activity activity, int layoutId) {
        /**
         * 定位PopupWindow，让它恰好显示在Action Bar的下方。 通过设置Gravity，确定PopupWindow的大致位置。
         * 首先获得状态栏的高度，再获取Action bar的高度，这两者相加设置y方向的offset样PopupWindow就显示在action
         * bar的下方了。 通过dp计算出px，就可以在不同密度屏幕统一X方向的offset.但是要注意不要让背景阴影大于所设置的offset，
         * 否则阴影的宽度为offset.
         */
        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//        状态栏高度：frame.top
        int xOffset = frame.top + activity.getActionBar().getHeight() - 25;//减去阴影宽度，适配UI.
        int yOffset = Dp2Px(activity, 5f); //设置x方向offset为5dp
        View parentView = activity.getLayoutInflater().inflate(layoutId,
                null);
        View popView = activity.getLayoutInflater().inflate(
                R.layout.action_overflow_popwindow, null);
        PopupWindow popWind = new PopupWindow(popView,
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);//popView即popupWindow的布局，ture设置focusAble.

        //必须设置BackgroundDrawable后setOutsideTouchable(true)才会有效。这里在XML中定义背景，所以这里设置为null;
        popWind.setBackgroundDrawable(new BitmapDrawable(activity.getResources(),
                (Bitmap) null));
        popWind.setOutsideTouchable(true); //点击外部关闭。
        popWind.setAnimationStyle(android.R.style.Animation_Dialog);    //设置一个动画。
        //设置Gravity，让它显示在右上角。
        popWind.showAtLocation(parentView, Gravity.RIGHT | Gravity.TOP,
                yOffset, xOffset);
    }
}
