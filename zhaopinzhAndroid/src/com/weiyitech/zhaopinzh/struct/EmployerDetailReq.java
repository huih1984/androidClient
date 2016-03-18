package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-12-4
 * Time: 下午2:40
 * To change this template use File | Settings | File Templates.
 */
public class EmployerDetailReq implements Parcelable {
    public int employerId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(employerId);
    }

}
