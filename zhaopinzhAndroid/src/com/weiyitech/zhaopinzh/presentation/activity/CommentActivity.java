package com.weiyitech.zhaopinzh.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.tencent.weibo.sdk.android.api.util.Util;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.business.CommentBusiness;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.adapter.CommentListAdapter;
import com.weiyitech.zhaopinzh.struct.Comment;
import com.weiyitech.zhaopinzh.struct.CommentConcise;
import com.weiyitech.zhaopinzh.struct.Pager;
import com.weiyitech.zhaopinzh.struct.PagerComment;
import com.weiyitech.zhaopinzh.util.CommentUtil;
import com.weiyitech.zhaopinzh.util.Common;
import com.weiyitech.zhaopinzh.util.weibo.sina.AccessTokenKeeper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-10-23
 * Time: 上午11:31
 * To change this template use File | Settings | File Templates.
 */
public class CommentActivity extends CommonActivity implements BusinessInterface {
    final String TAG = CommentActivity.class.getName();
    final int MYCOMMENTREQCODE = 100;
    final int MYLOGINREQCODE = 101;
    private Oauth2AccessToken mAccessToken;
    int employerId;
    String employerName;
    TextView companyNameTextView;
    ListView commentListView;
    TextView commentTextView;
    CommentListAdapter commentListAdapter;
    ArrayList<Comment> commentList = new ArrayList<Comment>();
    int firstIndex = 0;
    int total = 0;
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int dataType = bundle.getInt("type");
            if (dataType == Common.COMMENT_TOTAL_TYPE) {
                CommentConcise commentConcise = bundle.getParcelable("comment_concise");
                total = commentConcise.total;
                if (total == 0) {
                    commentTextView.setVisibility(View.VISIBLE);
                    commentListView.setVisibility(View.GONE);
                } else if (total > 0) {
                    commentListView.setVisibility(View.VISIBLE);
                    commentTextView.setVisibility(View.GONE);
                }
                if (total > 0) {
                    Pager pager = new Pager();
                    pager.current = 0;
                    pager.length = 20;
                    Comment comment = new Comment();
                    comment.setEmployerId(employerId);
                    comment.setPager(pager);
                    CommentBusiness commentBusiness = new CommentBusiness();
                    commentBusiness.getEmployerComment(comment, CommentActivity.this);
                }
            } else if (dataType == Common.COMMENT_TYPE) {
                PagerComment pagerComment = bundle.getParcelable("pagercomment");
                commentList.addAll(pagerComment.getCommentList());
                commentListAdapter.notifyDataSetChanged();
                Pager pager = pagerComment.getPager();
                firstIndex = pager.current + pager.length;

                if (firstIndex < total) {
                    commentListAdapter.footViewExist = true;
                } else {
                    commentListAdapter.footViewExist = false;
                }
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        employerId = getIntent().getIntExtra("employerId", 0);
        employerName = getIntent().getStringExtra("employerName");
        setContentView(R.layout.comment_activity);
        companyNameTextView = (TextView) findViewById(R.id.comment_activity_employer_name_text_view);
        companyNameTextView.setText(employerName);
        commentListView = (ListView) findViewById(R.id.comment_list_view);
        commentTextView = (TextView) findViewById(R.id.comment_default_text_view);
        commentListAdapter = new CommentListAdapter(this, commentList);
        commentListView.setAdapter(commentListAdapter);
        commentListView.setOnScrollListener(mScrollListener);

        CommentConcise commentConcise = new CommentConcise();
        commentConcise.employerId = employerId;
        CommentUtil.queryCommentCnt(this, commentConcise);
    }

    AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {   //对listview的滚动监听
        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
            switch (i) {
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    commentListAdapter.setFlagBusy(false);
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    commentListAdapter.setFlagBusy(false);
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                    commentListAdapter.setFlagBusy(true);
                    break;
                default:
                    break;
            }
            commentListAdapter.notifyDataSetChanged();
        }
        @Override
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MYCOMMENTREQCODE) {
            if(data == null) return;
            Bundle bundle = data.getExtras().getBundle("result");
            if (bundle != null) {
                Comment comment = bundle.getParcelable("comment");
                if (comment != null) {
                    //添加到当前目录去
                    commentList.add(0, comment);
                    if (commentTextView.getVisibility() == View.VISIBLE) {
                        commentListView.setVisibility(View.VISIBLE);
                        commentTextView.setVisibility(View.GONE);
                    }
                    commentListAdapter = new CommentListAdapter(this, commentList);
                    commentListView.setAdapter(commentListAdapter);
                   // commentListAdapter.notifyDataSetChanged();
                }
            }
        } else if (requestCode == MYLOGINREQCODE) {
          //  Toast.makeText(this, "授权成功，请发表评论！", 1000).show();
        }
    }

    @Override
    public void goHome(){
        Intent intent = new Intent();
        intent.setClass(this, EmployerDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.comment_activity_action_comment:
                mAccessToken = AccessTokenKeeper.readAccessToken(this);
                if (mAccessToken != null && mAccessToken.isSessionValid()) {
                    Intent intent = new Intent();
                    intent.putExtra("employerId", employerId);
                    intent.setClass(this, MyCommentActivity.class);
                    startActivityForResult(intent, MYCOMMENTREQCODE);
                } else {
                    String accessToken;// 用户访问令牌
                    accessToken = Util.getSharePersistent(getApplicationContext(),
                            "ACCESS_TOKEN");
                    if (accessToken != null && accessToken != "") {
                        Intent intent = new Intent();
                        intent.putExtra("employerId", employerId);
                        intent.setClass(this, MyCommentActivity.class);
                        startActivityForResult(intent, MYCOMMENTREQCODE);
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("employerId", employerId);
                        intent.setClass(this, WeiboActivity.class);
                        startActivityForResult(intent, MYLOGINREQCODE);
                    }
                }

                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.comment_activity_actions, menu);
        return true;
    }

    @Override
    public void getDataFromBusiness(int dataType, Object t, int... position) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        if (dataType == Common.COMMENT_TOTAL_TYPE) {
            CommentConcise commentConcise = (CommentConcise) t;
            bundle.putParcelable("comment_concise", commentConcise);
            bundle.putInt("type", Common.COMMENT_TOTAL_TYPE);
        } else if (dataType == Common.COMMENT_TYPE) {
            PagerComment pagerComment = (PagerComment) t;
            bundle.putInt("type", Common.COMMENT_TYPE);
            bundle.putParcelable("pagercomment", pagerComment);
        }
        message.setData(bundle);
        handler.sendMessage(message);
    }


}