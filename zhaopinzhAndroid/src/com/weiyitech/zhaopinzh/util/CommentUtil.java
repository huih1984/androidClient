package com.weiyitech.zhaopinzh.util;

import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.business.CommentBusiness;
import com.weiyitech.zhaopinzh.presentation.activity.CommonActivity;
import com.weiyitech.zhaopinzh.presentation.component.CommonFragment;
import com.weiyitech.zhaopinzh.struct.Comment;
import com.weiyitech.zhaopinzh.struct.CommentConcise;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: john
 * Date: 14-10-28
 * Time: 上午8:37
 * To change this template use File | Settings | File Templates.
 */
public class CommentUtil {
    public static float getDiamondRating(Comment comment) {
        float avarage = comment.getCompanyExpectation();
        avarage += comment.getOccupationGrowing();
        avarage += comment.getTechnicalGrowing();
        avarage += comment.getWorkEnviroment();
        avarage += comment.getWorkPress();
        avarage /= 5;
        return avarage;
    }

    public static float getDiamondRating(List<Comment> commentList) {
        float avarage = 0;
        for (Comment comment : commentList) {
            avarage += getDiamondRating(comment);
        }
        avarage /= commentList.size();
        return avarage;
    }

    public static float getOccupationGrowingRating(List<Comment> commentList) {
        float avarage = 0;
        for (Comment comment : commentList) {
            avarage += comment.getOccupationGrowing();
        }
        avarage /= commentList.size();
        return avarage;
    }

    public static float getWorkEnviromentRating(List<Comment> commentList) {
        float avarage = 0;
        for (Comment comment : commentList) {
            avarage += comment.getWorkEnviroment();
        }
        avarage /= commentList.size();
        return avarage;
    }

    public static float getTechnicalGrowingRating(List<Comment> commentList) {
        float avarage = 0;
        for (Comment comment : commentList) {
            avarage += comment.getTechnicalGrowing();
        }
        avarage /= commentList.size();
        return avarage;
    }

    public static float getWorkPressRating(List<Comment> commentList) {
        float avarage = 0;
        for (Comment comment : commentList) {
            avarage += comment.getWorkPress();
        }
        avarage /= commentList.size();
        return avarage;
    }

    public static float getCompanyExpactationRating(List<Comment> commentList) {
        float avarage = 0;
        for (Comment comment : commentList) {
            avarage += comment.getCompanyExpectation();
        }
        avarage /= commentList.size();
        return avarage;
    }

    public static void queryCommentCnt(CommonActivity activity, CommentConcise commentConcise, int... positon) {
        for (CommentConcise mCommentConcise : ZhaopinzhApp.getInstance().commentConciseList) {
            if (commentConcise.employerId == mCommentConcise.employerId) {
                activity.getDataFromBusiness(Common.COMMENT_TOTAL_TYPE, mCommentConcise, positon);
                return;
            }
        }
        //如果程序还没结束说明需要通过网络获取评论数
        CommentBusiness commentBusiness = new CommentBusiness();
        commentBusiness.getCommentTotal(commentConcise, activity);
    }

    public static void queryCommentCnt(CommonFragment commonFragment, CommentConcise commentConcise, int... positon) {
        for (CommentConcise mCommentConcise : ZhaopinzhApp.getInstance().commentConciseList) {
            if (commentConcise.employerId == mCommentConcise.employerId) {
                commentConcise.total = mCommentConcise.total;
                commonFragment.getDataFromBusiness(Common.COMMENT_TOTAL_TYPE, commentConcise, positon);
                return;
            }
        }
        //如果程序还没结束说明需要通过网络获取评论数
        CommentBusiness commentBusiness = new CommentBusiness();
        commentBusiness.getCommentTotal(commentConcise, commonFragment);
    }

    public static void showCommentCnt(CommonActivity activity, CommentConcise commentConcise, int... positon) {
        boolean isExists = false;
        for (CommentConcise mCommentConcise : ZhaopinzhApp.getInstance().commentConciseList) {
            if (mCommentConcise.employerId == commentConcise.employerId) {
                isExists = true;
                break;
            }
        }
        if (!isExists) {
            ZhaopinzhApp.getInstance().commentConciseList.add(commentConcise);
        }
        activity.getDataFromBusiness(Common.COMMENT_TOTAL_TYPE, commentConcise, positon);
    }

    public static void showCommentCnt(CommonFragment commonFragment, CommentConcise commentConcise, int... positon) {
        boolean isExists = false;
        for (CommentConcise mCommentConcise : ZhaopinzhApp.getInstance().commentConciseList) {
            if (mCommentConcise.employerId == commentConcise.employerId) {
                isExists = true;
                break;
            }
        }
        if (!isExists) {
            ZhaopinzhApp.getInstance().commentConciseList.add(commentConcise);
        }
        commonFragment.getDataFromBusiness(Common.COMMENT_TOTAL_TYPE, commentConcise, positon);
    }
}
