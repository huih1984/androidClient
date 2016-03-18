package com.weiyitech.zhaopinzh.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import com.google.gson.Gson;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.utils.LogUtil;
import com.tencent.weibo.sdk.android.api.*;
import com.tencent.weibo.sdk.android.api.util.Util;
import com.tencent.weibo.sdk.android.component.GeneralDataShowActivity;
import com.tencent.weibo.sdk.android.model.AccountModel;
import com.tencent.weibo.sdk.android.model.BaseVO;
import com.tencent.weibo.sdk.android.model.ModelResult;
import com.tencent.weibo.sdk.android.network.HttpCallback;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.business.CommentBusiness;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.struct.Comment;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.weibo.sina.AccessTokenKeeper;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-10-25
 * Time: 上午9:57
 * To change this template use File | Settings | File Templates.
 */
public class MyCommentActivity extends CommonActivity implements BusinessInterface {
    final String TAG = MyCommentActivity.class.getName();
    private Oauth2AccessToken mAccessToken;

    UsersAPI mUsersAPI;    //新浪微博api
    User user;             //新浪微博api
    String tencentUser;
    EditText editText;
    RatingBar occupationGrowingRatingBar;
    RatingBar techGrowingRatingBar;
    RatingBar workEnviromentRatingBar;
    RatingBar workPressRatingBar;
    RatingBar companyFutrueRatingBar;
    int employerId;
    Comment comment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        employerId = getIntent().getIntExtra("employerId", 0);
        setContentView(R.layout.mycomment_activity);
        editText = (EditText) findViewById(R.id.mycomment_edit_text);
        occupationGrowingRatingBar = (RatingBar) findViewById(R.id.occupation_growing_rating_bar);
        techGrowingRatingBar = (RatingBar) findViewById(R.id.tech_growing_rating_bar);
        workEnviromentRatingBar = (RatingBar) findViewById(R.id.work_enviroment_rating_bar);
        workPressRatingBar = (RatingBar) findViewById(R.id.work_press_rating_bar);
        companyFutrueRatingBar = (RatingBar) findViewById(R.id.company_futrue_rating_bar);
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken != null &&  mAccessToken.isSessionValid()) {
            mUsersAPI = new UsersAPI(mAccessToken); // 获取用户信息接口
            long uid = Long.parseLong(mAccessToken.getUid());
            mUsersAPI.show(uid, mListener);
        } else {
            getAccountInfo(this);
        }
    }

    @Override
    public void goHome(){
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.my_comment_activity_action_comment:
                if (user == null && tencentUser == null) {
                    Toast.makeText(MyCommentActivity.this, "请稍后发送！", 1000).show();
                } else {
                    CommentBusiness commentBusiness = new CommentBusiness();
                    if (occupationGrowingRatingBar.getRating() == 0.0) {
                        Toast.makeText(MyCommentActivity.this, "请给公司的职业成长评分！", 1000).show();
                    } else if (techGrowingRatingBar.getRating() == 0.0) {
                        Toast.makeText(MyCommentActivity.this, "请给公司的技能成长评分！", 1000).show();
                    } else if (workEnviromentRatingBar.getRating() == 0.0) {
                        Toast.makeText(MyCommentActivity.this, "请给公司的工作氛围评分！", 1000).show();
                    } else if (workPressRatingBar.getRating() == 0.0) {
                        Toast.makeText(MyCommentActivity.this, "请给公司的工作压力评分！", 1000).show();
                    } else if (companyFutrueRatingBar.getRating() == 0.0) {
                        Toast.makeText(MyCommentActivity.this, "请给公司的公司前景评分！", 1000).show();
                    } else {
                        comment = new Comment();
                        comment.setCommentTime(Common.currentTime("yyyy-MM-dd HH:mm:ss"));
                        comment.setEmployerId(employerId);
                        comment.setOccupationGrowing(occupationGrowingRatingBar.getRating());
                        comment.setTechnicalGrowing(techGrowingRatingBar.getRating());
                        comment.setWorkEnviroment(workEnviromentRatingBar.getRating());
                        comment.setWorkPress(workPressRatingBar.getRating());
                        comment.setCompanyExpectation(companyFutrueRatingBar.getRating());
                        comment.setComment(editText.getText().toString());
                        if (user != null) {
                            comment.setUserName(user.screen_name);
                            comment.setAvatarPath(user.profile_image_url);
                            comment.setUserLoginType(1);
                        } else if (tencentUser != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(tencentUser);
                                JSONObject jsonData = jsonObject.getJSONObject("data");
                                comment.setAvatarPath(jsonData.getString("head"));
                                comment.setUserName(jsonData.getString("nick"));
                                comment.setUserLoginType(2);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        commentBusiness.addComment(comment, this);
                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("comment", comment);
                        intent.putExtra("result", bundle);
                        //设置返回数据
                        MyCommentActivity.this.setResult(RESULT_OK, intent);
                        this.finish();
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_comment_activity_actions, menu);
        return true;
    }

    @Override
    public void getDataFromBusiness(int dataType, Object t, int... position) {

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
                } else {
                    Toast.makeText(MyCommentActivity.this, response, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            LogUtil.e(TAG, e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(MyCommentActivity.this, info.toString(), Toast.LENGTH_LONG).show();
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
            } else {
                Toast.makeText(MyCommentActivity.this,
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

}