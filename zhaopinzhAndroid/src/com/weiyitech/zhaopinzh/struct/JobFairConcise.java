package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-2-11
 * Time: 下午7:39
 * To change this template use File | Settings | File Templates.
 */
public class JobFairConcise implements Parcelable {
    public int fairId;
    public String fairName;
    public int hallId;
    public String hallName;
    public int employersCount;
    public String runningTime;
    public int runnerId;
    public String runnerName;
    public String dynamicInfo;
    public String ownerLogoPath;
    public boolean isNew;
    public boolean read;

    JobFairConcise(){
        isNew = true;
        read = false;
    }

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
        parcel.writeInt(employersCount);
        parcel.writeInt(runnerId);
        parcel.writeString(runningTime);
        parcel.writeString(runnerName);
        parcel.writeString(dynamicInfo);
        parcel.writeString(ownerLogoPath);
        parcel.writeByte((byte) (isNew ? 1 : 0));
        parcel.writeByte((byte) (read ? 1 : 0));
    }
}
