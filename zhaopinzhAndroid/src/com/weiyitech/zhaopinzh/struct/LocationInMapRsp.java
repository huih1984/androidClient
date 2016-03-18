package com.weiyitech.zhaopinzh.struct;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-7-8
 * Time: 上午10:28
 * To change this template use File | Settings | File Templates.
 */
public class LocationInMapRsp {
    public int locationId;
    public double longitude;
    public double latitude;
    public String address;
    public String addTime;

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
