package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-11-15
 * Time: 上午10:43
 * To change this template use File | Settings | File Templates.
 */
public class JobDetailRsp implements Parcelable {
    public int jobId;
    public int fairId;
    public int employerId;
    public String jobName;
    public String resp;
    public String payment;
    public String workplace;
    public int jobType;
    public String workType;
    public String sexReq;
    public String skillReq;
    public String expReq;
    public String majorReq;
    public String eduReq;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(jobId);
        parcel.writeInt(fairId);
        parcel.writeInt(employerId);
        parcel.writeString(jobName);
        parcel.writeString(resp);
        parcel.writeString(payment);
        parcel.writeString(workplace);
        parcel.writeInt(jobType);
        parcel.writeString(workType);
        parcel.writeString(sexReq);
        parcel.writeString(skillReq);
        parcel.writeString(expReq);
        parcel.writeString(majorReq);
        parcel.writeString(eduReq);
    }

}
