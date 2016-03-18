package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-1-8
 * Time: 下午4:54
 * To change this template use File | Settings | File Templates.
 */
public class StandPicRsp implements Parcelable {
    public int standPicId;
    public int fairId;
    public int hallId;
    public String standPicPath;
    public int floor;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(standPicId);
        parcel.writeInt(fairId);
        parcel.writeInt(hallId);
        parcel.writeString(standPicPath);
        parcel.writeInt(floor);
    }
}
