package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * User: john
 * Date: 14-11-3
 * Time: 下午3:54
 * To change this template use File | Settings | File Templates.
 */
public class JobDetailZhaopinzh implements Parcelable {
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

    //招聘人数
    public int demandNumber;
    //职位描述
    public String jobDesc;
    //技能要求,
    public String skillReq;
    //经验要求
    public String expReq;
    //学历要求
    public String eduReq;
    //专业要求
    public String majorReq;
    //性别要求
    public String sexReq;
    //工资待遇
    public String payment;
    //工作地点
    public String workplace;
    //职位类型
    public int jobType;

    public String workType;

    public int readTimes;

    public String logoPath;

    public String hrEmail;

    public String tel;

    public String publishTime;

    public String deadTime;

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

    public int getDemandNumber() {
        return demandNumber;
    }

    public void setDemandNumber(int demandNumber) {
        this.demandNumber = demandNumber;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getSkillReq() {
        return skillReq;
    }

    public void setSkillReq(String skillReq) {
        this.skillReq = skillReq;
    }

    public String getExpReq() {
        return expReq;
    }

    public void setExpReq(String expReq) {
        this.expReq = expReq;
    }

    public String getEduReq() {
        return eduReq;
    }

    public void setEduReq(String eduReq) {
        this.eduReq = eduReq;
    }

    public String getMajorReq() {
        return majorReq;
    }

    public void setMajorReq(String majorReq) {
        this.majorReq = majorReq;
    }

    public String getSexReq() {
        return sexReq;
    }

    public void setSexReq(String sexReq) {
        this.sexReq = sexReq;
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

    public int getJobType() {
        return jobType;
    }

    public void setJobType(int jobType) {
        this.jobType = jobType;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
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

    public String getHrEmail() {
        return hrEmail;
    }

    public void setHrEmail(String hrEmail) {
        this.hrEmail = hrEmail;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(int commentCnt) {
        this.commentCnt = commentCnt;
    }

    public static final Parcelable.Creator<JobDetailZhaopinzh> CREATOR
            = new Parcelable.Creator<JobDetailZhaopinzh>() {
        public JobDetailZhaopinzh createFromParcel(Parcel in) {
            JobDetailZhaopinzh jobDetailZhaopinzh = new JobDetailZhaopinzh();
            jobDetailZhaopinzh.jobId = in.readInt();
            jobDetailZhaopinzh.jobName = in.readString();
            jobDetailZhaopinzh.employerId = in.readInt();
            jobDetailZhaopinzh.employerName = in.readString();
            jobDetailZhaopinzh.employerType = in.readInt();
            jobDetailZhaopinzh.employerScale = in.readInt();
            jobDetailZhaopinzh.industry = in.readInt();
            jobDetailZhaopinzh.demandNumber = in.readInt();
            jobDetailZhaopinzh.jobDesc = in.readString();
            jobDetailZhaopinzh.skillReq = in.readString();
            jobDetailZhaopinzh.expReq = in.readString();
            jobDetailZhaopinzh.eduReq = in.readString();
            jobDetailZhaopinzh.majorReq = in.readString();
            jobDetailZhaopinzh.sexReq = in.readString();
            jobDetailZhaopinzh.payment = in.readString();
            jobDetailZhaopinzh.workplace = in.readString();
            jobDetailZhaopinzh.jobType = in.readInt();
            jobDetailZhaopinzh.workType = in.readString();
            jobDetailZhaopinzh.readTimes = in.readInt();
            jobDetailZhaopinzh.logoPath = in.readString();
            jobDetailZhaopinzh.hrEmail = in.readString();
            jobDetailZhaopinzh.tel = in.readString();
            jobDetailZhaopinzh.publishTime = in.readString();
            jobDetailZhaopinzh.deadTime = in.readString();
            jobDetailZhaopinzh.position = in.readInt();
            jobDetailZhaopinzh.isFavorite = in.readByte() != 0;
            jobDetailZhaopinzh.commentCnt = in.readInt();
            return jobDetailZhaopinzh;
        }

        public JobDetailZhaopinzh[] newArray(int size) {
            return new JobDetailZhaopinzh[size];
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
        parcel.writeInt(demandNumber);
        parcel.writeString(jobDesc);
        parcel.writeString(skillReq);
        parcel.writeString(expReq);
        parcel.writeString(eduReq);
        parcel.writeString(majorReq);
        parcel.writeString(sexReq);
        parcel.writeString(payment);
        parcel.writeString(workplace);
        parcel.writeInt(jobType);
        parcel.writeString(workType);
        parcel.writeInt(readTimes);
        parcel.writeString(logoPath);
        parcel.writeString(hrEmail);
        parcel.writeString(tel);
        parcel.writeString(publishTime);
        parcel.writeString(deadTime);
        parcel.writeInt(position);
        parcel.writeByte((byte) (isFavorite ? 1 : 0));
        parcel.writeInt(commentCnt);
    }
}
