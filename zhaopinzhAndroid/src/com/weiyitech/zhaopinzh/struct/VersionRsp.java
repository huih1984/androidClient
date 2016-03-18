package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-11-29
 * Time: 下午5:58
 * To change this template use File | Settings | File Templates.
 */
public class VersionRsp implements Parcelable {
    public int updateId;
    public String distTime;
    public String versionNum;
    public int importance;
    public int updateType;
    public String updatePath;
    public String addTime;
    public String updateTime;
    public String adderName;
    public String updaterName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(updateId);
        parcel.writeString(distTime);
        parcel.writeString(versionNum);
        parcel.writeInt(importance);
        parcel.writeInt(updateType);
        parcel.writeString(updatePath);
        parcel.writeString(addTime);
        parcel.writeString(updateTime);
        parcel.writeString(adderName);
        parcel.writeString(updaterName);
    }
}
