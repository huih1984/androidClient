package com.weiyitech.zhaopinzh.struct;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: john
 * Date: 14-10-24
 * Time: 下午9:48
 * To change this template use File | Settings | File Templates.
 */
public class User {
    String id;
    int userLoginType;
    int userId;
    String userName;
    String tencentId;
    String sinaId;
    String tencentWeiboName;
    String sinaWeiboName;
    String tencentAvatarPath;
    String sinaAvatarPath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserLoginType() {
        return userLoginType;
    }

    public void setUserLoginType(int userLoginType) {
        this.userLoginType = userLoginType;
    }

    public String getTencentId() {
        return tencentId;
    }

    public void setTencentId(String tencentId) {
        this.tencentId = tencentId;
    }

    public String getSinaId() {
        return sinaId;
    }

    public void setSinaId(String sinaId) {
        this.sinaId = sinaId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTencentWeiboName() {
        return tencentWeiboName;
    }

    public void setTencentWeiboName(String tencentWeiboName) {
        this.tencentWeiboName = tencentWeiboName;
    }

    public String getSinaWeiboName() {
        return sinaWeiboName;
    }

    public void setSinaWeiboName(String sinaWeiboName) {
        this.sinaWeiboName = sinaWeiboName;
    }

    public String getTencentAvatarPath() {
        return tencentAvatarPath;
    }

    public void setTencentAvatarPath(String tencentAvatarPath) {
        this.tencentAvatarPath = tencentAvatarPath;
    }

    public String getSinaAvatarPath() {
        return sinaAvatarPath;
    }

    public void setSinaAvatarPath(String sinaAvatarPath) {
        this.sinaAvatarPath = sinaAvatarPath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
