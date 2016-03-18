package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-12-12
 * Time: 上午9:37
 * To change this template use File | Settings | File Templates.
 */
public class TrafficRsp implements Parcelable {
   public int jobFairId;
   public int hallId;
   public String detail;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(jobFairId);
        parcel.writeInt(hallId);
        parcel.writeString(detail);
    }

}
