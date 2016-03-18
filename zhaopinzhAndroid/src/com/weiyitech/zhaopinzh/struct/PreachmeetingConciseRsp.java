package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-5-22
 * Time: 下午4:02
 * To change this template use File | Settings | File Templates.
 */
public class PreachmeetingConciseRsp implements Parcelable {
    public int preachMeetingId;
    public String preachMeetingName;
    public String employerName;
    public String runningTimeBegin;
    public String runningTimeEnd;
    public String runPlace;
    public String runningUniversity;
    public int readTimes;
    public String logoPath;
    public String addTime;
    public String updateTime;
    public boolean isFavorite;
    public boolean favoriteValidMessage;
    public int position;
    public boolean isNew;
    public PreachmeetingConciseRsp(){
        position = -1;
        isNew = true;
        isFavorite = false;
    }

    public static final Parcelable.Creator<PreachmeetingConciseRsp> CREATOR
            = new Parcelable.Creator<PreachmeetingConciseRsp>() {
        public PreachmeetingConciseRsp createFromParcel(Parcel in) {
            PreachmeetingConciseRsp preachmeetingConciseRsp = new PreachmeetingConciseRsp();
            preachmeetingConciseRsp.preachMeetingId = in.readInt();
            preachmeetingConciseRsp.preachMeetingName = in.readString();
            preachmeetingConciseRsp.employerName = in.readString();
            preachmeetingConciseRsp.runningTimeBegin = in.readString();
            preachmeetingConciseRsp.runningTimeEnd = in.readString();
            preachmeetingConciseRsp.runPlace = in.readString();
            preachmeetingConciseRsp.runningUniversity = in.readString();
            preachmeetingConciseRsp.readTimes = in.readInt();
            preachmeetingConciseRsp.logoPath = in.readString();
            preachmeetingConciseRsp.addTime = in.readString();
            preachmeetingConciseRsp.updateTime = in.readString();
            preachmeetingConciseRsp.isFavorite = in.readByte() != 0;
            preachmeetingConciseRsp.position = in.readInt();
            preachmeetingConciseRsp.isNew = in.readByte() != 0;
            return preachmeetingConciseRsp;
        }

        public PreachmeetingConciseRsp[] newArray(int size) {
            return new PreachmeetingConciseRsp[size];
        }
    };

    public int getPreachMeetingId() {
        return preachMeetingId;
    }

    public void setPreachMeetingId(int preachMeetingId) {
        this.preachMeetingId = preachMeetingId;
    }

    public String getPreachMeetingName() {
        return preachMeetingName;
    }

    public void setPreachMeetingName(String preachMeetingName) {
        this.preachMeetingName = preachMeetingName;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getRunningTimeBegin() {
        return runningTimeBegin;
    }

    public void setRunningTimeBegin(String runningTimeBegin) {
        this.runningTimeBegin = runningTimeBegin;
    }

    public String getRunningTimeEnd() {
        return runningTimeEnd;
    }

    public void setRunningTimeEnd(String runningTimeEnd) {
        this.runningTimeEnd = runningTimeEnd;
    }

    public String getRunPlace() {
        return runPlace;
    }

    public void setRunPlace(String runPlace) {
        this.runPlace = runPlace;
    }

    public String getRunningUniversity() {
        return runningUniversity;
    }

    public void setRunningUniversity(String runningUniversity) {
        this.runningUniversity = runningUniversity;
    }

    public int getReadTimes() {
        return readTimes;
    }

    public void setReadTimes(int readTimes) {
        this.readTimes = readTimes;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(preachMeetingId);
        parcel.writeString(preachMeetingName);
        parcel.writeString(employerName);
        parcel.writeString(runningTimeBegin);
        parcel.writeString(runningTimeEnd);
        parcel.writeString(runPlace);
        parcel.writeString(runningUniversity);
        parcel.writeInt(readTimes);
        parcel.writeString(logoPath);
        parcel.writeString(addTime);
        parcel.writeString(updateTime);
        parcel.writeByte((byte)(isFavorite ? 1:0));
        parcel.writeInt(position);
        parcel.writeByte((byte)(isNew ? 1:0));
    }
}
