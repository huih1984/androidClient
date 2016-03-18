package com.weiyitech.zhaopinzh.struct;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-10-24
 * Time: 下午9:43
 * To change this template use File | Settings | File Templates.
 */
public class Comment implements Parcelable {
    int commentId;
    int employerId;
    float occupationGrowing;
    float technicalGrowing;
    float workEnviroment;
    float workPress;
    float companyExpectation;
    float avarageDiamond;
    String comment;
    int userId;
    String userName;
    String avatarPath;
    int userLoginType;
    String commentTime;
    Pager pager;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(commentId);
        parcel.writeInt(employerId);
        parcel.writeFloat(occupationGrowing);
        parcel.writeFloat(technicalGrowing);
        parcel.writeFloat(workEnviroment);
        parcel.writeFloat(workPress);
        parcel.writeFloat(companyExpectation);
        parcel.writeString(comment);
        parcel.writeInt(userId);
        parcel.writeString(userName);
        parcel.writeString(avatarPath);
        parcel.writeInt(userLoginType);
        parcel.writeString(commentTime);
        parcel.writeParcelable(pager, flags);
    }

    public static final Parcelable.Creator<Comment> CREATOR
            = new Parcelable.Creator<Comment>() {
        public Comment createFromParcel(Parcel in) {
            Comment comment1 = new Comment();
            comment1.commentId = in.readInt();
            comment1.employerId = in.readInt();
            comment1.occupationGrowing = in.readFloat();
            comment1.technicalGrowing = in.readFloat();
            comment1.workEnviroment = in.readFloat();
            comment1.workPress = in.readFloat();
            comment1.companyExpectation = in.readFloat();
            comment1.comment = in.readString();
            comment1.userId = in.readInt();
            comment1.userName = in.readString();
            comment1.avatarPath = in.readString();
            comment1.userLoginType = in.readInt();
            comment1.commentTime = in.readString();
            comment1.pager = in.readParcelable(Pager.class.getClassLoader());
            return comment1;
        }

        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getEmployerId() {
        return employerId;
    }

    public void setEmployerId(int employerId) {
        this.employerId = employerId;
    }

    public float getOccupationGrowing() {
        return occupationGrowing;
    }

    public void setOccupationGrowing(float occupationGrowing) {
        this.occupationGrowing = occupationGrowing;
    }

    public float getTechnicalGrowing() {
        return technicalGrowing;
    }

    public void setTechnicalGrowing(float technicalGrowing) {
        this.technicalGrowing = technicalGrowing;
    }

    public float getWorkEnviroment() {
        return workEnviroment;
    }

    public void setWorkEnviroment(float workEnviroment) {
        this.workEnviroment = workEnviroment;
    }

    public float getWorkPress() {
        return workPress;
    }

    public void setWorkPress(float workPress) {
        this.workPress = workPress;
    }

    public float getCompanyExpectation() {
        return companyExpectation;
    }

    public void setCompanyExpectation(float companyExpectation) {
        this.companyExpectation = companyExpectation;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public int getUserLoginType() {
        return userLoginType;
    }

    public void setUserLoginType(int userLoginType) {
        this.userLoginType = userLoginType;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public float getAvarageDiamond() {
        return avarageDiamond;
    }

    public void setAvarageDiamond(float avarageDiamond) {
        this.avarageDiamond = avarageDiamond;
    }
}

