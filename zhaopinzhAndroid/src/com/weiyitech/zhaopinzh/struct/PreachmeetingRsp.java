package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-5-24
 * Time: 下午4:44
 * To change this template use File | Settings | File Templates.
 */
public class PreachmeetingRsp implements Parcelable {
    public int preachMeetingId;
    public String instruction;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(preachMeetingId);
        parcel.writeString(instruction);
    }
}