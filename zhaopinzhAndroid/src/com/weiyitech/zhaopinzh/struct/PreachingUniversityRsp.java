package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-5-22
 * Time: 下午3:31
 * To change this template use File | Settings | File Templates.
 */
public class PreachingUniversityRsp implements Parcelable {
    public String university;
    public int preachCount;
    public int rating;
    public boolean addNew;

    PreachingUniversityRsp(){
       addNew = false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(university);
        parcel.writeInt(preachCount);
        parcel.writeInt(rating);
        parcel.writeByte((byte)(addNew ?1:0));
    }
}
