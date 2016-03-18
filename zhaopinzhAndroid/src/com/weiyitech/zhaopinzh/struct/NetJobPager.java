package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-8-1
 * Time: 下午7:46
 * To change this template use File | Settings | File Templates.
 */
public class NetJobPager implements Parcelable {
    public Pager pager;
    public List<JobConciseNetRsp> list;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(pager, flags);
        parcel.writeArray(list.toArray());
    }
}
