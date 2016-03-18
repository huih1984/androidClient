package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * User: john
 * Date: 14-11-13
 * Time: 下午8:28
 * To change this template use File | Settings | File Templates.
 */
public class CommentConcise implements Parcelable {
    public int employerId;
    public int total;
    public int position;
    public int pageIndex;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(employerId);
        dest.writeInt(total);
        dest.writeInt(position);
        dest.writeInt(pageIndex);
    }
}
