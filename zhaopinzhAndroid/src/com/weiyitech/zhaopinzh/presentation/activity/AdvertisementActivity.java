package com.weiyitech.zhaopinzh.presentation.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.*;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.navdrawer.SimpleSideDrawer;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.business.QueryZhaopinzhJob;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.component.MessageProvider;
import com.weiyitech.zhaopinzh.struct.*;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.baidupush.Utils;
import com.weiyitech.zhaopinzh.util.cache.ImageLoader;
import com.weiyitech.zhaopinzh.util.imageviewpager.ImageViewPager;
import com.weiyitech.zhaopinzh.util.imageviewpager.ImageViewPagerAdapter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-5-13
 * Time: 下午2:49
 * To change this template use File | Settings | File Templates.
 */
public class AdvertisementActivity extends CommonActivity implements BusinessInterface {

    ImageViewPager imageViewPager;
    ViewPager viewPager;
    RelativeLayout m_marketLayout;
    RelativeLayout m_campusRelativeLayout;
    RelativeLayout m_netRelativeLayout;
    RelativeLayout m_talkRelativeLayout;
    ImageView m_marketHintImageView;
    ImageView m_campusHintImageView;
    TextView m_marketHintTextView;
    TextView m_campusHintTextView;
    private SimpleSideDrawer mNav;
    public List<ImageView> dotViews;
    public List<ImageView> imageViewList = new ArrayList<ImageView>();
    public ImageViewPagerAdapter imageViewPagerAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advertisement_activity);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(false);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mNav = new SimpleSideDrawer(this);
        mNav.setLeftBehindContentView(R.layout.activity_behind_left_simple);

//        try {
//            ViewConfiguration mconfig = ViewConfiguration.get(this);
//            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
//            if (menuKeyField != null) {
//                menuKeyField.setAccessible(true);
//                menuKeyField.setBoolean(mconfig, false);
//            }
//        } catch (Exception ex) {
//        }

        final RelativeLayout marketLayout = (RelativeLayout) findViewById(R.id.advertisement_activity_market_image_view);
        final RelativeLayout campusRelativeLayout = (RelativeLayout) findViewById(R.id.advertisement_activity_campus_image_view);
        RelativeLayout netRelativeLayout = (RelativeLayout) findViewById(R.id.advertisement_activity_net_image_view);
        final RelativeLayout talkRelativeLayout = (RelativeLayout) findViewById(R.id.advertisement_activity_talk_image_view);
        m_marketHintImageView = (ImageView) findViewById(R.id.advertisement_activity_market_new_hint_image_view);
        m_campusHintImageView = (ImageView) findViewById(R.id.advertisement_activity_campus_new_hint_image_view);
        m_marketHintTextView = (TextView) findViewById(R.id.advertisement_activity_market_new_text_view);
        m_campusHintTextView = (TextView) findViewById(R.id.advertisement_activity_campus_new_text_view);

        m_marketLayout = marketLayout;
        m_campusRelativeLayout = campusRelativeLayout;
        m_netRelativeLayout = netRelativeLayout;
        m_talkRelativeLayout = talkRelativeLayout;
        setViewClickEvent(marketLayout, HomeActivity.class);
        setViewClickEvent(campusRelativeLayout, CampusActivity.class);
        setViewClickEvent(netRelativeLayout, NetJobConciseListActivity.class);
        setViewClickEvent(talkRelativeLayout, TalkActivity.class);


        if (!Utils.hasBind(getApplicationContext())) {
            Log.d("YYY", "before start work at " + Calendar.getInstance().getTimeInMillis());
            if (Utils.getBindSetting(this)) {
                PushManager.startWork(getApplicationContext(),
                        PushConstants.LOGIN_TYPE_API_KEY,
                        Utils.getMetaValue(AdvertisementActivity.this, "api_key"));
                Log.d("YYY", "after start work at " + Calendar.getInstance().getTimeInMillis());
                // Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
                PushManager.enableLbs(getApplicationContext());
                Log.d("YYY", "after enableLbs at " + Calendar.getInstance().getTimeInMillis());
            }
        } else {
            if (!Utils.getBindSetting(this)) {
                PushManager.stopWork(getApplicationContext());
            }
        }

        viewPager = (ViewPager) findViewById(R.id.advertisement_activity_ad_viewpager);
        dotViews = new ArrayList<ImageView>();
        imageViewPagerAdapter = new ImageViewPagerAdapter(this.imageViewList);
        viewPager.setAdapter(imageViewPagerAdapter);
        imageViewPager = new ImageViewPager(this, viewPager, imageViewList, dotViews);
        QueryZhaopinzhJob queryZhaopinzhJob = new QueryZhaopinzhJob();
        queryZhaopinzhJob.queryAdvertisementEmployer(new SearchTerm(), this);

    }

    public android.os.Handler handler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int dataType = bundle.getInt("type");
            if (dataType == Common.ADVERTISEMENT_EMPLOYER_TYPE) {
                ArrayList<AdvertisementEmployer> advertisementEmployers = bundle.getParcelableArrayList("advertisementEmployers");
                int cnt = bundle.getInt("cnt");
                LinearLayout layout = (LinearLayout) findViewById(R.id.advertisment_gallery_item_dot_layout);
                for (int i = 0; i < cnt; ++i) {
                    ImageView dotView = (ImageView) getLayoutInflater().inflate(R.layout.advertisement_list_dot, null);
                    layout.addView(dotView);
                    dotView.setEnabled(false);
                    dotViews.add(dotView);

                    ImageView imageView = (ImageView) getLayoutInflater().inflate(R.layout.advertisement_viewpager_item, null);
                    imageView.setImageResource(android.R.color.darker_gray);
                    imageViewList.add(imageView);
                    ImageLoader mImageLoader = new ImageLoader(AdvertisementActivity.this);
                    mImageLoader.DisplayImage(Common.URL + "/" + advertisementEmployers.get(i).getAdvertisementPicPath(), imageView, false, false);
                    imageView.setTag(advertisementEmployers.get(i));
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            AdvertisementEmployer advertisementEmployer = (AdvertisementEmployer) v.getTag();
                            intent.putExtra("employerId", advertisementEmployer.getEmployerId());
                            intent.setClass(AdvertisementActivity.this, ZhaopinzhJobConciseListActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                layout.requestLayout();
                if (dotViews.size() > 0) {
                    dotViews.get(0).setEnabled(true);
                    AdvertisementActivity.this.imageViewPagerAdapter.notifyDataSetChanged();
                    imageViewPager.onStart(cnt);
                }

            }
        }
    };

    @Override
    public void getDataFromBusiness(int dataType, Object t, int... position) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        if (dataType == Common.ADVERTISEMENT_EMPLOYER_TYPE) {
            ArrayList<AdvertisementEmployer> advertisementEmployers = (ArrayList<AdvertisementEmployer>) t;
            bundle.putParcelableArrayList("advertisementEmployers", advertisementEmployers);
            bundle.putInt("cnt", position[0]);
            bundle.putInt("type", Common.ADVERTISEMENT_EMPLOYER_TYPE);
        }
        message.setData(bundle);
        handler.sendMessage(message);
    }

    /**
     * 利用反射让隐藏在Overflow中的MenuItem显示Icon图标
     *
     * @param featureId
     * @param menu      onMenuOpened方法中调用
     */
    public static void setOverflowIconVisible(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        setOverflowIconVisible(featureId, menu);
        return super.onMenuOpened(featureId, menu);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.advertisement_activity_actions, menu);
//        MenuItem searchItem = menu.findItem(R.id.activity_action_search);
//       SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        SearchManager searchManager =
//                (SearchManager) getSystemService(SEARCH_SERVICE);
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false);
        return true;
    }

    public void popUpMyOverflow() {
        /**
         * 定位PopupWindow，让它恰好显示在Action Bar的下方。 通过设置Gravity，确定PopupWindow的大致位置。
         * 首先获得状态栏的高度，再获取Action bar的高度，这两者相加设置y方向的offset样PopupWindow就显示在action
         * bar的下方了。 通过dp计算出px，就可以在不同密度屏幕统一X方向的offset.但是要注意不要让背景阴影大于所设置的offset，
         * 否则阴影的宽度为offset.
         */
        // 获取状态栏高度
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//        状态栏高度：frame.top
        int xOffset = frame.top + getActionBar().getHeight() - 25;//减去阴影宽度，适配UI.
        int yOffset = Dp2Px(this, 5f); //设置x方向offset为5dp
        View parentView = getLayoutInflater().inflate(R.layout.advertisement_activity,
                null);
        View popView = getLayoutInflater().inflate(
                R.layout.action_overflow_popwindow, null);
        PopupWindow popWind = new PopupWindow(popView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);//popView即popupWindow的布局，ture设置focusAble.

        //必须设置BackgroundDrawable后setOutsideTouchable(true)才会有效。这里在XML中定义背景，所以这里设置为null;
        popWind.setBackgroundDrawable(new BitmapDrawable(getResources(),
                (Bitmap) null));
        popWind.setOutsideTouchable(true); //点击外部关闭。
        popWind.setAnimationStyle(android.R.style.Animation_Dialog);    //设置一个动画。
        //设置Gravity，让它显示在右上角。
        popWind.showAtLocation(parentView, Gravity.RIGHT | Gravity.TOP,
                yOffset, xOffset);
    }

    public int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_action_search:
                Intent intent = new Intent();
                intent.setClass(this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.action_traffic:
                naviNewActivity(TrafficActivity.class);
                break;
            case R.id.action_feedback:
                naviNewActivity(FeedBackActivity.class);
                break;
            case R.id.action_favorite:
                naviNewActivity(FavoriteActivity.class);
                break;
            case R.id.action_settings:
                naviNewActivity(SettingActivity.class);
                break;

            case android.R.id.home:
                mNav.toggleLeftDrawer();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void setViewClickEvent(final View view, final Class<?> cls) {
        final ScaleAnimation animation = new ScaleAnimation(1f, .9f, 1f, .9f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        animation.setFillAfter(true);
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (cls.getName().equals(HomeActivity.class.getName())) {
                    m_marketHintImageView.setVisibility(View.GONE);
                    m_marketHintTextView.setVisibility(View.GONE);
                    ContentResolver resolver = ZhaopinzhApp.getInstance().getContentResolver();
                    String selection = "type = CAST(? AS INT)";
                    String[] selectionArgs = {String.valueOf(1)};
                    resolver.delete(MessageProvider.NEW_HINT_URI, selection, selectionArgs);
                } else if (cls.getName().equals(CampusActivity.class.getName())) {
                    m_campusHintImageView.setVisibility(View.GONE);
                    m_campusHintTextView.setVisibility(View.GONE);
                    ContentResolver resolver = ZhaopinzhApp.getInstance().getContentResolver();
                    String selection = "type = CAST(? AS INT)";
                    String[] selectionArgs = {String.valueOf(2)};
                    resolver.delete(MessageProvider.NEW_HINT_URI, selection, selectionArgs);
                }
                view.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        naviNewActivity(cls);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
    }

    void naviNewActivity(Class cls) {
        Intent intent = new Intent(AdvertisementActivity.this, cls);
        intent.putExtra("where", AdvertisementActivity.class.getName());
        AdvertisementActivity.this.startActivity(intent);
        //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    boolean exitVal = false;

    @Override
    public void onBackPressed() {
        if (exitVal) {
            System.exit(0);
        } else {
            exitVal = true;
            Toast.makeText(this, "再按一次退出程序", 1000).show();
            java.util.Timer timer;
            timer = new Timer(true);
            timer.schedule(
                    new java.util.TimerTask() {
                        public void run() {
                            exitVal = false;
                        }
                    }, 2000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_marketLayout.clearAnimation();
        m_campusRelativeLayout.clearAnimation();
        m_netRelativeLayout.clearAnimation();
        m_talkRelativeLayout.clearAnimation();
        ContentResolver resolver = ZhaopinzhApp.getInstance().getContentResolver();
        String selection = "type = CAST(? AS INT)";
        String[] selectionArgs = {String.valueOf(1)};
        Cursor cursor = resolver.query(MessageProvider.NEW_HINT_URI, null, selection, selectionArgs, null);
        if (cursor != null) {
            m_marketHintImageView.setVisibility(View.VISIBLE);
            m_marketHintTextView.setVisibility(View.VISIBLE);
            int i = 0;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                i++;
            }
            m_marketHintTextView.setText(String.valueOf(i));
            cursor.close();
        } else {
            m_marketHintImageView.setVisibility(View.GONE);
            m_marketHintTextView.setVisibility(View.GONE);
        }

        String[] selection2Args = {String.valueOf(2)};
        Cursor cursor2 = resolver.query(MessageProvider.NEW_HINT_URI, null, selection, selection2Args, null);
        if (cursor2 != null) {
            m_campusHintTextView.setVisibility(View.VISIBLE);
            m_campusHintImageView.setVisibility(View.VISIBLE);
            int i = 0;
            for (cursor2.moveToFirst(); !cursor2.isAfterLast(); cursor2.moveToNext()) {
                i++;
            }
            m_campusHintTextView.setText(String.valueOf(i));
            cursor2.close();
        } else {
            m_campusHintTextView.setVisibility(View.GONE);
            m_campusHintImageView.setVisibility(View.GONE);
        }

        if (Common.getIsUNSpecified(this).equals("竖屏显示")) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    protected void onStop() {
        imageViewPager.onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        if (dotViews.size() > 0 && imageViewPager.isShutdown()) {
            imageViewPager.onStart(dotViews.size());
        }
        super.onStart();
    }
}