package com.weiyitech.zhaopinzh.presentation.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.navdrawer.SimpleSideDrawer;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.tencent.weibo.sdk.android.api.*;
import com.tencent.weibo.sdk.android.api.util.Util;
import com.tencent.weibo.sdk.android.model.AccountModel;
import com.tencent.weibo.sdk.android.model.BaseVO;
import com.tencent.weibo.sdk.android.model.ModelResult;
import com.tencent.weibo.sdk.android.network.HttpCallback;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.component.MessageProvider;
import com.weiyitech.zhaopinzh.util.baidupush.Utils;
import com.weiyitech.zhaopinzh.util.weibo.sina.AccessTokenKeeper;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Timer;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-8-21
 * Time: 上午9:40
 * To change this template use File | Settings | File Templates.
 */
public class TalkActivity extends CommonActivity {
    private static final String TAG = "TalkActivity";
    WebView webView;
    public SimpleSideDrawer mNav;

    RelativeLayout m_marketLayout;
    RelativeLayout m_campusRelativeLayout;
    RelativeLayout m_netRelativeLayout;
    RelativeLayout m_talkRelativeLayout;
    ImageView m_marketHintImageView;
    ImageView m_campusHintImageView;
    TextView m_marketHintTextView;
    TextView m_campusHintTextView;
    RelativeLayout m_searchView;
    RelativeLayout m_favorView;
    Button loginBtn;
    TextView nicknameTxtView;


    private Oauth2AccessToken mAccessToken;

    UsersAPI mUsersAPI;    //新浪微博api
    com.sina.weibo.sdk.openapi.models.User user;             //新浪微博api
    String tencentUser;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk_activity);

        mNav = new SimpleSideDrawer(this);
        mNav.setLeftBehindContentView(R.layout.activity_behind_left_simple);


        final RelativeLayout marketLayout = (RelativeLayout) findViewById(R.id.advertisement_activity_market_image_view);
        final RelativeLayout campusRelativeLayout = (RelativeLayout) findViewById(R.id.advertisement_activity_campus_image_view);
        RelativeLayout netRelativeLayout = (RelativeLayout) findViewById(R.id.advertisement_activity_net_image_view);
        final RelativeLayout talkRelativeLayout = (RelativeLayout) findViewById(R.id.advertisement_activity_talk_image_view);
        final RelativeLayout searchView = (RelativeLayout) findViewById(R.id.net_job_activity_search_view);
        final RelativeLayout favorView = (RelativeLayout) findViewById(R.id.net_job_activity_favorite_view);

        m_marketHintImageView = (ImageView) findViewById(R.id.advertisement_activity_market_new_hint_image_view);
        m_campusHintImageView = (ImageView) findViewById(R.id.advertisement_activity_campus_new_hint_image_view);
        m_marketHintTextView = (TextView) findViewById(R.id.advertisement_activity_market_new_text_view);
        m_campusHintTextView = (TextView) findViewById(R.id.advertisement_activity_campus_new_text_view);
        m_marketLayout = marketLayout;
        m_campusRelativeLayout = campusRelativeLayout;
        m_netRelativeLayout = netRelativeLayout;
        m_talkRelativeLayout = talkRelativeLayout;
        m_searchView = searchView;
        m_favorView = favorView;
        setViewClickEvent(marketLayout, HomeActivity.class);
        setViewClickEvent(campusRelativeLayout, CampusActivity.class);
        setViewClickEvent(netRelativeLayout, NetJobConciseListActivity.class);
        setViewClickEvent(talkRelativeLayout, TalkActivity.class);
        setViewClickEvent(searchView, SearchActivity.class);
        setViewClickEvent(favorView, FavoriteActivity.class);

        loginBtn = (Button)findViewById(R.id.default_avatar);
        nicknameTxtView = (TextView)findViewById(R.id.nickname);
        setViewClickEvent(loginBtn, WeiboActivity.class);
        if (!Utils.hasBind(getApplicationContext())) {
            Log.d("YYY", "before start work at " + Calendar.getInstance().getTimeInMillis());
            if (Utils.getBindSetting(this)) {
                PushManager.startWork(getApplicationContext(),
                        PushConstants.LOGIN_TYPE_API_KEY,
                        Utils.getMetaValue(TalkActivity.this, "api_key"));
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

        webView = (WebView) findViewById(R.id.talk_activity_web_view);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().supportMultipleWindows();
        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl("http://xiaoqu.qq.com/mobile/barindex.html?_bid=128&_wv=1027&bid=157088");                          // http://m.wsq.qq.com/263310625http://www.work020.com:8088/startbbs/index.php http://www.tzwsq.com/                                     //http://m.wsq.qq.com/263310625
        webView.setWebViewClient(new WebViewClientDemo());


        //查询账号信息
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken != null &&  mAccessToken.isSessionValid()) {
            mUsersAPI = new UsersAPI(mAccessToken); // 获取用户信息接口
            long uid = Long.parseLong(mAccessToken.getUid());
            mUsersAPI.show(uid, mListener);
        } else {
            getAccountInfo(this);
        }
    }


    /**********************sina*****************/
    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                // 调用 UserBusiness#parse 将JSON串解析成User对象
                user = User.parse(response);
                if (user != null) {
                    if(user.gender == "男"){
                        loginBtn.setBackgroundResource(R.drawable.default_avatar_male);
                    } else {
                        loginBtn.setBackgroundResource(R.drawable.default_avatar_female);
                    }

                    loginBtn.setText("");
                    loginBtn.setClickable(false);
                    loginBtn.invalidate();
                    nicknameTxtView.setText(user.screen_name);
                } else {
                    Toast.makeText(TalkActivity.this, response, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(TalkActivity.this, info.toString(), Toast.LENGTH_LONG).show();
        }
    };
    /**
     * ********************************************
     */
    private String accessToken;// 用户访问令牌
    private FriendAPI friendAPI;//好友相关API
    private TimeLineAPI timeLineAPI;//时间线API
    private WeiboAPI weiboAPI;//微博相关API
    private UserAPI userAPI;//帐户相关API
    private LbsAPI lbsAPI;//LBS相关API
    private HttpCallback mCallBack = new HttpCallback() {
        @Override
        public void onResult(Object object) {
            ModelResult result = (ModelResult) object;
            if (result != null && result.isSuccess()) {
                tencentUser = result.getObj().toString();
                if (tencentUser != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(tencentUser);
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        if(jsonData.getString("sex").equals("1")){
                            loginBtn.setBackgroundResource(R.drawable.default_avatar_male);
                        } else {
                            loginBtn.setBackgroundResource(R.drawable.default_avatar_female);
                        }
                        loginBtn.setText("");
                        loginBtn.setClickable(false);
                        loginBtn.invalidate();
                        nicknameTxtView.setText(jsonData.getString("nick"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Toast.makeText(TalkActivity.this,
                        "调用失败", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };//回调函数

    private String requestFormat = "json";

    void getAccountInfo(Context context) {
        accessToken = Util.getSharePersistent(context.getApplicationContext(),
                "ACCESS_TOKEN");
        if (accessToken == null || "".equals(accessToken)) {
            return;
        }
        AccountModel account = new AccountModel(accessToken);
        friendAPI = new FriendAPI(account);
        timeLineAPI = new TimeLineAPI(account);
        weiboAPI = new WeiboAPI(account);
        userAPI = new UserAPI(account);
        lbsAPI = new LbsAPI(account);
        userAPI.getUserInfo(context, requestFormat, mCallBack, null, BaseVO.TYPE_JSON);
    }

    /**
     * *********************tencent**************************
     */

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
                } else if(cls.getName().equals(WeiboActivity.class.getName())){

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
        Intent intent = new Intent(TalkActivity.this, cls);
        intent.putExtra("where", TalkActivity.class.getName());
        TalkActivity.this.startActivity(intent);
        //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    private class WebViewClientDemo extends WebViewClient {
        @Override
        // 在WebView中而不是默认浏览器中显示页面
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case android.R.id.home:
//                goHome();
////            case R.id.home_activity_action_search:
////                openSearch();
//                break;
            case android.R.id.home:
                mNav.toggleLeftDrawer();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    boolean exitVal = false;

    @Override
    public void onBackPressed() {
        if (exitVal) {
//            System.exit(0);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onResume() {
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
        super.onResume();
    }
}