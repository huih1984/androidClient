package com.weiyitech.zhaopinzh.business;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.weiyitech.zhaopinzh.exception.ZhaopinzhException;
import com.weiyitech.zhaopinzh.presentation.activity.CommentActivity;
import com.weiyitech.zhaopinzh.presentation.activity.CommonActivity;
import com.weiyitech.zhaopinzh.presentation.component.CommonFragment;
import com.weiyitech.zhaopinzh.struct.Comment;
import com.weiyitech.zhaopinzh.struct.CommentConcise;
import com.weiyitech.zhaopinzh.struct.Pager;
import com.weiyitech.zhaopinzh.struct.PagerComment;
import com.weiyitech.zhaopinzh.util.CommentUtil;
import com.weiyitech.zhaopinzh.util.Common;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-10-24
 * Time: 下午9:55
 * To change this template use File | Settings | File Templates.
 */
public class CommentBusiness extends CommonBusiness {

    void sendEmployerCommentToPresentation(final Context destContext, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            List<Comment> commentList;
            JSONArray list = jsonObject.getJSONArray("list");
            Gson gson = new Gson();
            commentList = gson.fromJson(list.toString(), new TypeToken<List<Comment>>() {
            }.getType());
            Pager pager = gson.fromJson(jsonObject.getJSONObject("pager").toString(), Pager.class);
            PagerComment pagerComment = new PagerComment();
            pagerComment.setPager(pager);
            pagerComment.setCommentList(commentList);
            ((CommonActivity) destContext).getDataFromBusiness(Common.COMMENT_TYPE, pagerComment);
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendEmployerCommentToPresentation JSONException", e);
        }
    }

    CommentConcise parseJsonToCommentConcise(JSONObject jsonObject) {
        try {
            JSONObject jsonObject1 = jsonObject.getJSONObject("pager");
            Gson gson = new Gson();
            Pager pager = gson.fromJson(jsonObject1.toString(), Pager.class);
            JSONObject jsonObject2 = jsonObject.getJSONObject("form");
            int employerId = jsonObject2.getInt("employerId");
            CommentConcise commentConcise = new CommentConcise();
            commentConcise.employerId = employerId;
            commentConcise.total = pager.total;
            commentConcise.pageIndex = jsonObject2.getInt("pageIndex");
            commentConcise.position = jsonObject2.getInt("position");
            return commentConcise;
        } catch (Exception e) {
            throw new ZhaopinzhException("Function parseJsonToCommentConcise JSONException", e);
        }
    }

    void sendEmployerCommentCntToPresentation(final Context destContext, JSONObject jsonObject) throws ZhaopinzhException {
        CommentUtil.showCommentCnt((CommonActivity) destContext, parseJsonToCommentConcise(jsonObject));
    }


    void sendEmployerCommentCntToPresentation(final CommonFragment commonFragment, JSONObject jsonObject) throws ZhaopinzhException {
        CommentUtil.showCommentCnt((CommonFragment) commonFragment, parseJsonToCommentConcise(jsonObject));
    }

    void sendEmployerAvarageDiamondToPresentation(final Context destContext, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            float avarageDiamond = (float) jsonObject.getDouble("avarageDiamond");
            Comment comment = new Comment();
            comment.setAvarageDiamond(avarageDiamond);
            comment.setEmployerId(jsonObject.getInt("employerId"));
            ((CommonActivity) destContext).getDataFromBusiness(Common.COMMENT_AVARAGE_TYPE, comment);
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendEmployerAvarageDiamondToPresentation JSONException", e);
        }
    }

    void sendEmployerRatingToPresentation(final Context destContext, JSONObject jsonObject) throws ZhaopinzhException {
        try {
            JSONObject jsonObject1 = jsonObject.getJSONObject("comment");
            Gson gson = new Gson();
            Comment comment = gson.fromJson(jsonObject1.toString(), Comment.class);
            ((CommonActivity) destContext).getDataFromBusiness(Common.RATING_TYPE, comment);
        } catch (Exception e) {
            throw new ZhaopinzhException("Function sendEmployerRatingToPresentation JSONException", e);
        }
    }

    public void addComment(Object params, final Context destContext) {
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/employer/addComment.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.get(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                    } catch (ZhaopinzhException e) {
                        //对最终的异常要进行处理
                    }
                    super.onSuccess(jsonObject);
                }

                @Override
                public void onFailure(Throwable throwable, JSONObject jsonObject) {
                    super.onFailure(throwable, jsonObject);
                }
            });
        } catch (Exception e) {

        }
    }


    public void getEmployerComment(Object params, final Context destContext) {
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/employer/employerComment.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.get(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        sendEmployerCommentToPresentation(destContext, getValuesFromResult(jsonObject));
                    } catch (ZhaopinzhException e) {
                        //对最终的异常要进行处理
                    }
                    super.onSuccess(jsonObject);
                }

                @Override
                public void onFailure(Throwable throwable, JSONObject jsonObject) {
                    super.onFailure(throwable, jsonObject);
                }
            });
        } catch (Exception e) {

        }
    }

    public void getCommentTotal(Object params, final Object object) {
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/employer/employerCommentTotal.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.get(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        if(object instanceof CommonActivity){
                            sendEmployerCommentCntToPresentation((CommonActivity )object, getValuesFromResult(jsonObject));
                        } else if(object instanceof CommonFragment){
                            sendEmployerCommentCntToPresentation((CommonFragment )object, getValuesFromResult(jsonObject));
                        }

                    } catch (ZhaopinzhException e) {
                        e.printStackTrace();
                    }
                    super.onSuccess(jsonObject);
                }

                @Override
                public void onFailure(Throwable throwable, JSONObject jsonObject) {
                    super.onFailure(throwable, jsonObject);
                }
            });
        } catch (Exception e) {

        }
    }

    public void getEmployerAvarageDiamond(Object params, final Context context) {
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/employer/employerAvarageDiamond.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.get(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        sendEmployerAvarageDiamondToPresentation(context, getValuesFromResult(jsonObject));
                    } catch (ZhaopinzhException e) {
                        //对最终的异常要进行处理
                    }
                    super.onSuccess(jsonObject);
                }

                @Override
                public void onFailure(Throwable throwable, JSONObject jsonObject) {
                    super.onFailure(throwable, jsonObject);
                }
            });
        } catch (Exception e) {

        }
    }

    public void getEmployerRating(Object params, final Context context) {
        RequestParams rqParams = new RequestParams();
        rqParams.put("json", new Gson().toJson(params));
        String url = Common.URL + "/employer/employerRating.do";
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.get(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        sendEmployerRatingToPresentation(context, getValuesFromResult(jsonObject));
                    } catch (ZhaopinzhException e) {
                        //对最终的异常要进行处理
                    }
                    super.onSuccess(jsonObject);
                }

                @Override
                public void onFailure(Throwable throwable, JSONObject jsonObject) {
                    super.onFailure(throwable, jsonObject);
                }
            });
        } catch (Exception e) {

        }
    }
}
