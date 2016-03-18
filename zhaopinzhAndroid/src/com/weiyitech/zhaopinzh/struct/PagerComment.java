package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: john
 * Date: 14-10-27
 * Time: 下午11:11
 * To change this template use File | Settings | File Templates.
 */
public class PagerComment implements Parcelable{
    Pager pager;
    List<Comment> commentList;

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(pager, flags);
        parcel.writeArray(commentList.toArray());
    }
}
