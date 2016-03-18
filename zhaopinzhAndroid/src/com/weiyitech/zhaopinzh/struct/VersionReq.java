package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-11-29
 * Time: 下午5:30
 * To change this template use File | Settings | File Templates.
 */
public class VersionReq implements Parcelable {
    public String versionNum;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(versionNum);
    }
}
