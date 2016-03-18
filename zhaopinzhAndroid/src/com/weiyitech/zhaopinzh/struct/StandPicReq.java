package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-1-6
 * Time: 下午4:09
 * To change this template use File | Settings | File Templates.
 */
public class StandPicReq implements Parcelable {
    public int fairId;
    public int employerId;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(fairId);
        parcel.writeInt(employerId);
    }
}
