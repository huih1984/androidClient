package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-3-29
 * Time: 下午2:46
 * To change this template use File | Settings | File Templates.
 */
public class JobConciseReq implements Parcelable {
    public int employerId;
    public int fairId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(employerId);
        parcel.writeInt(fairId);
    }
}
