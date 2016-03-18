package com.weiyitech.zhaopinzh.util.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Created with IntelliJ IDEA.
 * User: john
 * Date: 14-10-29
 * Time: 下午3:24
 * To change this template use File | Settings | File Templates.
 */
public class MyWebView extends WebView {
    public final static String CSS_STYLE ="<style>* {font-size:18px;line-height:26px;}p </style>";

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        WebSettings webSettings = getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
       // webSettings.setTextSize(WebSettings.TextSize.NORMAL);
    }

    public void setData(String data){
        String summary = "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">"+
                CSS_STYLE + "</head>"
                + data;
        loadData(summary, "text/html; charset=utf8", null);
    }
}
