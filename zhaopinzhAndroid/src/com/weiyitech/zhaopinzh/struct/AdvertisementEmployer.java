package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-5-15
 * Time: 上午9:36
 * To change this template use File | Settings | File Templates.
 */
public class AdvertisementEmployer  implements Parcelable {
    private int employerId;
    private String advertisementPicPath;
    private String publishTime;
    private String deadTime;

    public int getEmployerId() {
        return employerId;
    }

    public void setEmployerId(int employerId) {
        this.employerId = employerId;
    }

    public String getAdvertisementPicPath() {
        return advertisementPicPath;
    }

    public void setAdvertisementPicPath(String advertisementPicPath) {
        this.advertisementPicPath = advertisementPicPath;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(String deadTime) {
        this.deadTime = deadTime;
    }

    @Override
    public int describeContents() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(employerId);
        dest.writeString(advertisementPicPath);
        dest.writeString(publishTime);
        dest.writeString(deadTime);

    }
}
