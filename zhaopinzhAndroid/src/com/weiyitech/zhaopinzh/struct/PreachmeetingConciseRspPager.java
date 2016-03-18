package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-5-25
 * Time: 下午7:54
 * To change this template use File | Settings | File Templates.
 */
public class PreachmeetingConciseRspPager implements Parcelable {
    public Pager pager;
    public List<PreachmeetingConciseRsp> list;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(pager, i);
        parcel.writeArray(list.toArray());
    }
}
