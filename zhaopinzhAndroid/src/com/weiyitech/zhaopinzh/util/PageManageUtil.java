package com.weiyitech.zhaopinzh.util;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.component.FixedGridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-10-21
 * Time: 下午8:18
 * To change this template use File | Settings | File Templates.
 */
public class PageManageUtil {

    final int PAGEGROUPSTATUSSTART0 = 0;  //有右箭头
    final int PAGEGROUPSTATUSSTART1 = 1;  //
    final int PAGEGROUPSTATUSMID = 2;     //双箭头
    final int PAGEGROUPSTATUSEND = 3;     //左箭头
    final int CELLHEIGHTDPI = 24;
    final int CELLWIDTHDPI = 24;

    final int PAGEBUTTONHEIGHT = 30;

    public int pageGroupType = 0;
    public int thisGroupfirstIndex = 0;
    public int visibleMaxIndexNotContainArrow = 0;
    public ViewGroup pageIndexContainer = null;
    public int pageCount;
    View rootView;
    Activity activity;
    ViewPager jobPager;
    public int currentPage;
    int pageButtonsNumber;
    List<View> pageButtons;

    public PageManageUtil(Activity activity, View rootView, ViewPager jobPager) {
        this.activity = activity;
        this.jobPager = jobPager;
        this.rootView = rootView;
    }

    /**
     *
     */
    public void addPageSelectView(ViewGroup parent) {
        pageIndexContainer = new LinearLayout(activity);
        pageIndexContainer.setClipChildren(false);
        pageIndexContainer.setId(R.id.page_index_viewgroup);
//        ((LinearLayout) pageIndexContainer).setCellHeight((int) (PAGEBUTTONHEIGHT * ZhaopinzhApp.getInstance().hardDensity));
//        ((LinearLayout) pageIndexContainer).setCellWidth((int) (PAGEBUTTONHEIGHT * ZhaopinzhApp.getInstance().hardDensity));
        parent.addView(pageIndexContainer, 1);
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ((int) (PAGEBUTTONHEIGHT * ZhaopinzhApp.getInstance().hardDensity)));
        pageIndexContainer.setLayoutParams(layoutParams);
        pageIndexContainer.setBackgroundColor(Color.rgb(0xd4,0xd4,0xd4));
        ((LinearLayout) pageIndexContainer).setGravity(Gravity.CENTER);
        ((LinearLayout) pageIndexContainer).setOrientation(LinearLayout.HORIZONTAL);
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        pageButtonsNumber = ((int) displayMetrics.widthPixels - 48) / ((int) (PAGEBUTTONHEIGHT * ZhaopinzhApp.getInstance().hardDensity));
        ViewGroup.LayoutParams layoutParamsBtn = new LinearLayout.LayoutParams(((int) (PAGEBUTTONHEIGHT * ZhaopinzhApp.getInstance().hardDensity)), ((int) (PAGEBUTTONHEIGHT * ZhaopinzhApp.getInstance().hardDensity)));
        for (int i = 0; i < pageButtonsNumber; ++i) {
            ImageButton newButton = new ImageButton(activity);
//            newButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            newButton.setBackgroundResource(android.R.color.transparent);
            newButton.setLayoutParams(layoutParamsBtn);
//            newButton.setGravity(Gravity.CENTER);
//            newButton.setTextColor(Color.BLACK);
            newButton.setVisibility(View.GONE);
            pageIndexContainer.addView(newButton);
        }
        pageButtons = new ArrayList<View>();
        for (int i = 0; i < pageButtonsNumber; ++i) {
            pageButtons.add(pageIndexContainer.getChildAt(i));
        }
    }


    public void setVisibleOfPageButtons() {
        //当前页处于第一个group
        //当前页处于中间group
        //当前页处于最好一个group

        int visibleMaxIndex = 0;  //包括右向箭头在内的button最大索引
        for (int i = 0; i < pageButtonsNumber; ++i) {
            ImageButton button = (ImageButton) pageButtons.get(i);
            button.setOnClickListener(null);
            button.setVisibility(View.GONE);
        }

        if ((pageCount > pageButtonsNumber && currentPage < pageButtonsNumber - 1) || pageCount <= pageButtonsNumber) {
            //第一页
            thisGroupfirstIndex = 0;
            if (pageCount > pageButtonsNumber) {
                visibleMaxIndex = pageButtonsNumber - 1;
                pageGroupType = PAGEGROUPSTATUSSTART0;
            } else {
                pageGroupType = PAGEGROUPSTATUSSTART1;
                visibleMaxIndex = pageCount - 1;
            }
        } else if (pageCount - currentPage <= pageButtonsNumber - 1) {    //最后一页
            pageGroupType = PAGEGROUPSTATUSEND;
            visibleMaxIndex = pageCount - currentPage;
            thisGroupfirstIndex = pageCount - visibleMaxIndex;
        } else {
            pageGroupType = PAGEGROUPSTATUSMID;
            visibleMaxIndex = pageButtonsNumber - 1;
            thisGroupfirstIndex = ((currentPage - (pageButtonsNumber - 1)) / (pageButtonsNumber - 2)) * (pageButtonsNumber - 2) + (pageButtonsNumber - 1);
        }

        if (pageGroupType == PAGEGROUPSTATUSSTART0) {
            visibleMaxIndexNotContainArrow = visibleMaxIndex - 1;
        } else if (pageGroupType == PAGEGROUPSTATUSMID) {
            visibleMaxIndexNotContainArrow = visibleMaxIndex - 2;
        } else if (pageGroupType == PAGEGROUPSTATUSEND) {
            visibleMaxIndexNotContainArrow = visibleMaxIndex - 1;
        } else {
            visibleMaxIndexNotContainArrow = visibleMaxIndex;
        }

        for (int i = 0; i <= visibleMaxIndex; ++i) {
            ImageButton button = (ImageButton) pageButtons.get(i);
            button.setVisibility(View.VISIBLE);
            if (i == 0) {
                if (pageGroupType == PAGEGROUPSTATUSMID || pageGroupType == PAGEGROUPSTATUSEND) {
                    //此时currentPage肯定是这一组的第一个
                    button.setImageResource(R.drawable.back);
                    button.setTag(thisGroupfirstIndex - 1);
                    //button.setText("");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jobPager.setCurrentItem((Integer) v.getTag());
                            setVisibleOfPageButtons();
                        }
                    });
                } else {
                    button.setImageResource(R.drawable.page_selector1);
//                    button.setText("1");
                    button.setTag(thisGroupfirstIndex);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jobPager.setCurrentItem((Integer) v.getTag());
                        }
                    });
                }
            } else if (i == visibleMaxIndex) {
                if (pageGroupType == PAGEGROUPSTATUSMID || pageGroupType == PAGEGROUPSTATUSSTART0) {
                    button.setImageResource(R.drawable.forword);
                    button.setTag(thisGroupfirstIndex + visibleMaxIndexNotContainArrow + 1);
                    //button.setText("");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jobPager.setCurrentItem((Integer) v.getTag());
                            setVisibleOfPageButtons();
                        }
                    });
                } else {
                    button.setImageResource(R.drawable.page_selector1);
                    int buttonInPageIndex;
                    if (pageGroupType == PAGEGROUPSTATUSMID || pageGroupType == PAGEGROUPSTATUSEND) {
                        buttonInPageIndex = thisGroupfirstIndex + i - 1;
                    } else {
                        buttonInPageIndex = thisGroupfirstIndex + i;
                    }
                    button.setTag(buttonInPageIndex);
//                    button.setText(String.valueOf(buttonInPageIndex + 1));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jobPager.setCurrentItem((Integer) v.getTag());
                        }
                    });
                }
            } else {
                button.setImageResource(R.drawable.page_selector1);
                int buttonInPageIndex;
                if (pageGroupType == PAGEGROUPSTATUSMID || pageGroupType == PAGEGROUPSTATUSEND) {
                    buttonInPageIndex = thisGroupfirstIndex + i - 1;
                } else {
                    buttonInPageIndex = thisGroupfirstIndex + i;
                }
                button.setTag(buttonInPageIndex);
//                button.setText(String.valueOf(buttonInPageIndex + 1));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        jobPager.setCurrentItem((Integer) v.getTag());
                    }
                });
            }
        }
    }

    public void pageSelectSettings(int position) {
        currentPage = position;
        ((ZhaopinzhApp) activity.getApplicationContext()).jobActivityPageIndex = position;
        if ((position == thisGroupfirstIndex - 1 || position == thisGroupfirstIndex + visibleMaxIndexNotContainArrow + 1)) {
            setVisibleOfPageButtons();
        }
        for (int i = 0; i < pageButtonsNumber; ++i) {
            View pageButton = pageButtons.get(i);
            if (pageGroupType == PAGEGROUPSTATUSMID || pageGroupType == PAGEGROUPSTATUSEND) {
                if (thisGroupfirstIndex + i == position + 1) {
                    pageButton.setSelected(true);
                } else {
                    pageButton.setSelected(false);
                }
            } else {
                if (thisGroupfirstIndex + i == position) {
                    pageButton.setSelected(true);
                } else {
                    pageButton.setSelected(false);
                }
            }
        }
    }

    public void setFirstSelect() {
        for (int i = 0; i < pageButtonsNumber; ++i) {
            View pageButton = pageButtons.get(i);
            if (i == 0) {
                pageButton.setSelected(true);
            } else {
                pageButton.setSelected(false);
            }
        }
    }
}
