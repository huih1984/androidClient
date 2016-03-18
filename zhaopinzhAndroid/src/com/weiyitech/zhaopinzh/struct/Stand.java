package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-3-31
 * Time: 下午8:57
 * To change this template use File | Settings | File Templates.
 */
public class Stand implements Parcelable {
    public int standId;
    public String standNumber;
    public int fairId;
    public int hallId;
    public int hallFloor;
    public int standPicId;
    public int employerId;

    public Stand() {

    }

    private Stand(Parcel source) {
        standId = source.readInt();
        standNumber = source.readString();
        fairId = source.readInt();
        hallId = source.readInt();
        hallFloor = source.readInt();
        standPicId = source.readInt();
        employerId = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(standId);
        parcel.writeString(standNumber);
        parcel.writeInt(fairId);
        parcel.writeInt(hallId);
        parcel.writeInt(hallFloor);
        parcel.writeInt(standPicId);
        parcel.writeInt(employerId);
    }

    public static final Creator<Stand> CREATOR = new Parcelable.Creator<Stand>(){
        @Override
        public Stand createFromParcel(Parcel source) {
            Stand stand = new Stand();
            stand.standId = source.readInt();
            stand.standNumber = source.readString();
            stand.fairId = source.readInt();
            stand.hallId = source.readInt();
            stand.hallFloor = source.readInt();
            stand.standPicId = source.readInt();
            stand.employerId = source.readInt();
            return stand;
        }

        @Override
        public Stand[] newArray(int size) {
            return new Stand[size];
        }
    };
}
