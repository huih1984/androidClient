package com.weiyitech.zhaopinzh.util.weibo.sina;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import com.weiyitech.zhaopinzh.presentation.R;

/**
 * Created with IntelliJ IDEA.
 * User: john
 * Date: 14-10-30
 * Time: 下午3:46
 * To change this template use File | Settings | File Templates.
 */
public class SinaLoginButton extends Button {
    private static final String TAG = "SinaLoginButton";

    /** 微博授权时，启动 SSO 界面的 Activity */
    private Context mContext;

    public SinaLoginButton(Context context) {
        this(context, null);
    }

    public SinaLoginButton(Context context, AttributeSet attrs) {
        this(context, attrs, /*R.style.com_sina_weibo_sdk_loginview_default_style*/0);
    }

    public SinaLoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        mContext = context;
        // 如果布局文件中未设置 style，加载默认的 style
        loadDefaultStyle(attrs);
    }
    /**
     * 加载默认的样式（蓝色）。
     *
     * @param attrs XML 属性集合对象
     */
    private void loadDefaultStyle(AttributeSet attrs) {
        if (attrs != null && 0 == attrs.getStyleAttribute()) {
            Resources res = getResources();
            this.setBackgroundResource(R.drawable.com_sina_weibo_sdk_button_blue);
            this.setPadding(res.getDimensionPixelSize(R.dimen.com_sina_weibo_sdk_loginview_padding_left),
                    res.getDimensionPixelSize(R.dimen.com_sina_weibo_sdk_loginview_padding_top),
                    res.getDimensionPixelSize(R.dimen.com_sina_weibo_sdk_loginview_padding_right),
                    res.getDimensionPixelSize(R.dimen.com_sina_weibo_sdk_loginview_padding_bottom));
            this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_com_sina_weibo_sdk_logo, 0, 0, 0);
            this.setCompoundDrawablePadding(
                    res.getDimensionPixelSize(R.dimen.com_sina_weibo_sdk_loginview_compound_drawable_padding));
            this.setTextColor(res.getColor(R.color.com_sina_weibo_sdk_loginview_text_color));
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    res.getDimension(R.dimen.com_sina_weibo_sdk_loginview_text_size));
            this.setTypeface(Typeface.DEFAULT_BOLD);
            this.setGravity(Gravity.CENTER);
            this.setText(R.string.com_sina_weibo_sdk_login_with_weibo_account);
        }
    }
}
