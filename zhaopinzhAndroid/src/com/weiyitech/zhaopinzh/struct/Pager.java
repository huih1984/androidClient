package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-11-2
 * Time: 上午10:12
 * To change this template use File | Settings | File Templates.
 */
public class Pager implements Parcelable {
    public int current;
    public int length;
    public int total;
    public int rows;

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Pager> CREATOR
            = new Parcelable.Creator<Pager>() {
        public Pager createFromParcel(Parcel in) {
            Pager pager = new Pager();
            pager.current = in.readInt();
            pager.length = in.readInt();
            pager.total = in.readInt();
            pager.rows = in.readInt();
            return pager;
        }

        public Pager[] newArray(int size) {
            return new Pager[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(current);
        parcel.writeInt(length);
        parcel.writeInt(total);
        parcel.writeInt(rows);
    }
}
