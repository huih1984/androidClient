package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-10-20
 * Time: 下午6:27
 * To change this template use File | Settings | File Templates.
 */
public class JobConciseRsp  implements Parcelable {
    public int jobId;
    public String jobName;
    public int employerId;
    public int fairId;
    public int demandNumber;
    public boolean read;

    public static final Parcelable.Creator<JobConciseRsp> CREATOR
            = new Parcelable.Creator<JobConciseRsp>() {
        public JobConciseRsp createFromParcel(Parcel in) {
            JobConciseRsp jobConciseRsp = new JobConciseRsp();
            jobConciseRsp.jobId = in.readInt();
            jobConciseRsp.jobName = in.readString();
            jobConciseRsp.employerId = in.readInt();
            jobConciseRsp.fairId = in.readInt();
            jobConciseRsp.demandNumber = in.readInt();
            jobConciseRsp.read = in.readByte() != 0 ;
            return jobConciseRsp;
        }

        public JobConciseRsp[] newArray(int size) {
            return new JobConciseRsp[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(jobId);
        parcel.writeString(jobName);
        parcel.writeInt(employerId);
        parcel.writeInt(fairId);
        parcel.writeInt(demandNumber);
        parcel.writeByte((byte) (read ? 1 : 0));
    }


}
