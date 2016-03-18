package com.weiyitech.zhaopinzh.presentation.component;

import android.view.View;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-1-23
 * Time: 下午1:53
 * To change this template use File | Settings | File Templates.
 */
public class FeedBackTranslateAnimation extends TranslateAnimation {

    RelativeLayout animatedView;
    int xDelta;
   public  FeedBackTranslateAnimation(int fromXType, float fromXValue, int toXType, float toXValue,
                                                        int fromYType, float fromYValue, int toYType, float toYValue){
      super(fromXType, fromXValue, toXType, toXValue, fromYType, fromYValue, toYType, toYValue) ;
       xDelta = (int)(toXValue - fromXValue);
   }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) animatedView.getLayoutParams();
        if (animatedView != null) {
            if (xDelta < 0) {
                layoutParams.leftMargin = -xDelta;
                layoutParams.rightMargin = 0;
            } else {
                layoutParams.leftMargin = 0;
                layoutParams.rightMargin = -xDelta;
            }
            animatedView.setLayoutParams(layoutParams);
            animatedView.requestLayout();
        }

    }

    public View getAnimatedView() {
        return animatedView;
    }

    public void setAnimatedView(RelativeLayout animatiedView) {
        this.animatedView = animatiedView;
    }


}
