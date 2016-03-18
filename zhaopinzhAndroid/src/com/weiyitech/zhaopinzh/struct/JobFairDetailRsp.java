package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-2-12
 * Time: 下午12:36
 * To change this template use File | Settings | File Templates.
 */
public class JobFairDetailRsp  implements Parcelable {
    public int fairId;
    public String fairName;
    public int hallId;
    public String hallName;
    public String runningTime;
    public int runnerId;
    public String runner;
    public String dynamicInfo;
    public String ownerName;
    public String desc;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(fairId);
        parcel.writeString(fairName);
        parcel.writeInt(hallId);
        parcel.writeString(hallName);
        parcel.writeInt(runnerId);
        parcel.writeString(runningTime);
        parcel.writeString(runner);
        parcel.writeString(dynamicInfo);
        parcel.writeString(desc);
    }
}