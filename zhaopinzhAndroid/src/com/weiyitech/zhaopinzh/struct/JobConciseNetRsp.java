package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-7-28
 * Time: 上午11:23
 * To change this template use File | Settings | File Templates.
 */
public class JobConciseNetRsp  implements Parcelable {
    //'岗位id
    public int jobId;
    //岗位名称
    public String jobName;
    //发布企业
    public int employerId;
    public String employerName;
    public int employerType;
    public int employerScale;
    public int industry;
    //工资待遇
    public String payment;
    //工作地点
    public String workplace;

    public int readTimes;

    public String logoPath;

    public String job51UpdateTime;

    public String zhilianUpdateTime;

    public String chinahrUpdateTime;

    public int position;

    public boolean isFavorite;

    public int commentCnt;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public int getEmployerId() {
        return employerId;
    }

    public void setEmployerId(int employerId) {
        this.employerId = employerId;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public int getEmployerType() {
        return employerType;
    }

    public void setEmployerType(int employerType) {
        this.employerType = employerType;
    }

    public int getEmployerScale() {
        return employerScale;
    }

    public void setEmployerScale(int employerScale) {
        this.employerScale = employerScale;
    }

    public int getIndustry() {
        return industry;
    }

    public void setIndustry(int industry) {
        this.industry = industry;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
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

    public String getJob51UpdateTime() {
        return job51UpdateTime;
    }

    public void setJob51UpdateTime(String job51UpdateTime) {
        this.job51UpdateTime = job51UpdateTime;
    }

    public String getZhilianUpdateTime() {
        return zhilianUpdateTime;
    }

    public void setZhilianUpdateTime(String zhilianUpdateTime) {
        this.zhilianUpdateTime = zhilianUpdateTime;
    }

    public String getChinahrUpdateTime() {
        return chinahrUpdateTime;
    }

    public void setChinahrUpdateTime(String chinahrUpdateTime) {
        this.chinahrUpdateTime = chinahrUpdateTime;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static final Parcelable.Creator<JobConciseNetRsp> CREATOR
            = new Parcelable.Creator<JobConciseNetRsp>() {
        public JobConciseNetRsp createFromParcel(Parcel in) {
            JobConciseNetRsp jobConciseNetRsp = new JobConciseNetRsp();
            jobConciseNetRsp.jobId = in.readInt();
            jobConciseNetRsp.jobName = in.readString();
            jobConciseNetRsp.employerId = in.readInt();
            jobConciseNetRsp.employerName = in.readString();
            jobConciseNetRsp.employerType = in.readInt();
            jobConciseNetRsp.employerScale = in.readInt();
            jobConciseNetRsp.industry = in.readInt();
            jobConciseNetRsp.payment = in.readString();
            jobConciseNetRsp.workplace = in.readString();
            jobConciseNetRsp.readTimes = in.readInt();
            jobConciseNetRsp.logoPath = in.readString();
            jobConciseNetRsp.job51UpdateTime = in.readString();
            jobConciseNetRsp.zhilianUpdateTime = in.readString();
            jobConciseNetRsp.chinahrUpdateTime = in.readString();
            jobConciseNetRsp.position = in.readInt();
            jobConciseNetRsp.isFavorite = in.readByte() != 0;
            jobConciseNetRsp.commentCnt = in.readInt();
            return jobConciseNetRsp;
        }

        public JobConciseNetRsp[] newArray(int size) {
            return new JobConciseNetRsp[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(jobId);
        parcel.writeString(jobName);
        parcel.writeInt(employerId);
        parcel.writeString(employerName);
        parcel.writeInt(employerType);
        parcel.writeInt(employerScale);
        parcel.writeInt(industry);
        parcel.writeString(payment);
        parcel.writeString(workplace);
        parcel.writeInt(readTimes);
        parcel.writeString(logoPath);
        parcel.writeString(job51UpdateTime);
        parcel.writeString(zhilianUpdateTime);
        parcel.writeString(chinahrUpdateTime);
        parcel.writeInt(position);
        parcel.writeByte((byte) (isFavorite ? 1 : 0));
        parcel.writeInt(commentCnt);

    }
}
