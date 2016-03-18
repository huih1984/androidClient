package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-12-4
 * Time: 下午2:40
 * To change this template use File | Settings | File Templates.
 */
public class EmployerDetailRsp implements Parcelable {
    public int employerId;
    public String employerName;
    public int type;
    public int scale;
    public String hrEmail;
    public String website;
    public String desc;
    public String address;
    public String province;
    public String city;
    public String tel;
    public String logoPath;
    public String bigLogoPath;
    public int industry;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(employerId);
        parcel.writeString(employerName);
        parcel.writeInt(type);
        parcel.writeInt(scale);
        parcel.writeString(hrEmail);
        parcel.writeString(website);
        parcel.writeString(desc);
        parcel.writeString(address);
        parcel.writeString(province);
        parcel.writeString(city);
        parcel.writeString(tel);
        parcel.writeString(logoPath);
        parcel.writeString(bigLogoPath);
        parcel.writeInt(industry);
    }

}
