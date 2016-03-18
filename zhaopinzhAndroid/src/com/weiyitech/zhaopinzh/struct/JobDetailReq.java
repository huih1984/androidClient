package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-11-15
 * Time: 下午1:29
 * To change this template use File | Settings | File Templates.
 */
public class JobDetailReq implements Parcelable {
    public int jobId;
    public int employerId;
    public int fairId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(jobId);
        parcel.writeInt(employerId);
        parcel.writeInt(fairId);
    }
}
