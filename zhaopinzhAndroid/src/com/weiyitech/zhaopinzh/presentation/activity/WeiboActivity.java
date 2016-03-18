package com.weiyitech.zhaopinzh.presentation.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.utils.LogUtil;
import com.tencent.weibo.sdk.android.api.*;
import com.tencent.weibo.sdk.android.api.util.Util;
import com.tencent.weibo.sdk.android.component.Authorize;
import com.tencent.weibo.sdk.android.component.sso.AuthHelper;
import com.tencent.weibo.sdk.android.component.sso.OnAuthListener;
import com.tencent.weibo.sdk.android.component.sso.WeiboToken;
import com.tencent.weibo.sdk.android.model.AccountModel;
import com.tencent.weibo.sdk.android.model.BaseVO;
import com.tencent.weibo.sdk.android.model.ModelResult;
import com.tencent.weibo.sdk.android.network.HttpCallback;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.business.UserBusiness;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.weibo.sina.AccessTokenKeeper;
import com.weiyitech.zhaopinzh.util.weibo.sina.Constants;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-10-23
 * Time: 下午4:39
 * To change this template use File | Settings | File Templates.
 */
public class WeiboActivity extends Activity implements BusinessInterface {
    final String TAG = WeiboActivity.class.getName();
    final int TENCENT_RESULTTYPE = 100;
    /**
     * 微博 Web 授权类，提供登陆等功能
     */
    private WeiboAuth mWeiboAuth;
    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;

    UsersAPI mUsersAPI;    //新浪微博api
    User user;             //新浪微博api
    String tencentUser;
    int employerId;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);            // 创建微博实例
        setContentView(R.layout.weibo_activity);
        mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        employerId = getIntent().getExtras().getInt("employerId");
        // Web 授权
        findViewById(R.id.sina_weibo_login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeiboAuth.anthorize(new AuthListener());
                // 或者使用：mWeiboAuth.authorize(new AuthListener(), Weibo.OBTAIN_AUTH_TOKEN);
            }
        });

        findViewById(R.id.tencent_weibo_login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long appid = Long.valueOf(Util.getConfig().getProperty(
                        "APP_KEY"));
                String app_secket = Util.getConfig().getProperty("APP_KEY_SEC");
                auth(appid, app_secket);
            }
        });

        // 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
        // 第一次启动本应用，AccessToken 不可用
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken.isSessionValid()) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult       /**
        // 当 SSO 授权 Activity 退出时，该函数被调用。
        //
        // @see {@link Activity#onActivityResult}
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

        if (requestCode == TENCENT_RESULTTYPE) {
            getAccountInfo(WeiboActivity.this);
            Intent intent = new Intent();
            //把返回数据存入Intent
            intent.setClass(WeiboActivity.this, TalkActivity.class);
            //设置返回数据
            WeiboActivity.this.setResult(RESULT_OK, intent);
            this.finish();
//            intent.putExtra("employerId", employerId);
//            intent.setClass(WeiboActivity.this, MyCommentActivity.class);
//            startActivity(intent);
        }
    }

    /****************sina weibo***********************************/


    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(WeiboActivity.this, mAccessToken);
                mUsersAPI = new UsersAPI(mAccessToken); // 获取用户信息接口
                long uid = Long.parseLong(mAccessToken.getUid());
                mUsersAPI.show(uid, mListener);

                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.setClass(WeiboActivity.this, NetJobConciseListActivity.class);
                //设置返回数据
                WeiboActivity.this.setResult(RESULT_OK, intent);
                WeiboActivity.this.finish();
//                Intent intent = new Intent();
//                intent.putExtra("employerId", employerId);
//                intent.setClass(WeiboActivity.this, MyCommentActivity.class);
//                startActivity(intent);
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = getString(R.string.weibosdk_demo_toast_auth_failed);
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(WeiboActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(WeiboActivity.this,
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(WeiboActivity.this,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                // 调用 UserBusiness#parse 将JSON串解析成User对象
                user = User.parse(response);
                com.weiyitech.zhaopinzh.struct.User user1 = new com.weiyitech.zhaopinzh.struct.User();
                user1.setSinaAvatarPath(user.profile_image_url);
                user1.setSinaWeiboName(user.screen_name);
                user1.setSinaId(user.id);
                user1.setUserLoginType(1);
                UserBusiness userBusiness = new UserBusiness();
                userBusiness.addUser(user1, WeiboActivity.this);
                if (user != null) {
                } else {
                    Toast.makeText(WeiboActivity.this, response, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            LogUtil.e(TAG, e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(WeiboActivity.this, info.toString(), Toast.LENGTH_LONG).show();
        }
    };

    /**
     * *****************腾讯微博**************************
     */
    private void auth(long appid, String app_secket) {
        final Context context = this.getApplicationContext();
        // 注册当前应用的appid和appkeysec，并指定一个OnAuthListener
        // OnAuthListener在授权过程中实施监听
        AuthHelper.register(this, appid, app_secket, new OnAuthListener() {

            // 如果当前设备没有安装腾讯微博客户端，走这里
            @Override
            public void onWeiBoNotInstalled() {
                AuthHelper.unregister(WeiboActivity.this);
                Intent i = new Intent(WeiboActivity.this, Authorize.class);
                startActivityForResult(i, TENCENT_RESULTTYPE);
                // startActivity(i);
            }

            // 如果当前设备没安装指定版本的微博客户端，走这里
            @Override
            public void onWeiboVersionMisMatch() {
                AuthHelper.unregister(WeiboActivity.this);
                Intent i = new Intent(WeiboActivity.this, Authorize.class);
                startActivityForResult(i, TENCENT_RESULTTYPE);
            }

            // 如果授权失败，走这里
            @Override
            public void onAuthFail(int result, String err) {
                Toast.makeText(WeiboActivity.this, "result : " + result,
                        1000).show();
                AuthHelper.unregister(WeiboActivity.this);
            }

            // 授权成功，走这里
            // 授权成功后，所有的授权信息是存放在WeiboToken对象里面的，可以根据具体的使用场景，将授权信息存放到自己期望的位置，
            // 在这里，存放到了applicationcontext中
            @Override
            public void onAuthPassed(String name, WeiboToken token) {
                //
                Util.saveSharePersistent(context, "ACCESS_TOKEN",
                        token.accessToken);
                Util.saveSharePersistent(context, "EXPIRES_IN",
                        String.valueOf(token.expiresIn));
                Util.saveSharePersistent(context, "OPEN_ID", token.openID);
                // Util.saveSharePersistent(context, "OPEN_KEY", token.omasKey);
                Util.saveSharePersistent(context, "REFRESH_TOKEN", "");
                // Util.saveSharePersistent(context, "NAME", name);
                // Util.saveSharePersistent(context, "NICK", name);
                Util.saveSharePersistent(context, "CLIENT_ID", Util.getConfig()
                        .getProperty("APP_KEY"));
                Util.saveSharePersistent(context, "AUTHORIZETIME",
                        String.valueOf(System.currentTimeMillis() / 1000l));
                AuthHelper.unregister(WeiboActivity.this);
                Intent i = new Intent(WeiboActivity.this, Authorize.class);
                startActivityForResult(i, TENCENT_RESULTTYPE);
            }
        });

        AuthHelper.auth(this, "");
    }

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
                com.weiyitech.zhaopinzh.struct.User user1 = new com.weiyitech.zhaopinzh.struct.User();
                try {
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(tencentUser);
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    user1.setTencentAvatarPath(jsonData.getString("head"));
                    user1.setUserName(jsonData.getString("nick"));
                    user1.setTencentId(jsonData.getString("openid"));
                    user1.setUserLoginType(2);
                    UserBusiness userBusiness = new UserBusiness();
                    userBusiness.addUser(user1, WeiboActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(WeiboActivity.this,
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

    @Override
    protected void onResume() {
        super.onResume();
    }


    /**
     * ****************tencent weibo****************************
     */
    @Override
    public void getDataFromBusiness(int dataType, Object t, int... position) {
        if (dataType == Common.USER_TYPE) {
            com.weiyitech.zhaopinzh.struct.User user = (com.weiyitech.zhaopinzh.struct.User) t;
            Common.setUserIdToPrefs(this, user.getUserId());
        }
    }
}