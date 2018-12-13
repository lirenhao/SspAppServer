package com.yada.ssp.appServer.model;

import javax.persistence.*;

/**
 * Created by bjy on 2018/8/21.
 * 推送设备表
 */

@Entity
@Table(name = "T_B_APP_DEVICE")
@IdClass(UserInfoPK.class)
public class Device {
    //商户号
    @Id
    @Column(nullable = false)
    private String merNo;
    //登录名
    @Id
    @Column(nullable = false)
    private String loginName;
    //终端号
    @Column
    private String termNo;
    //设备号
    @Column
    private String deviceNo;
    //推送服务标识
    @Column
    private String pushType;
    //设备平台
    @Column
    private String platform;
    //推送标记 0-按终端推送 1-按商户推送
    @Column
    private String pushFlag;

    public String getMerNo() {
        return merNo;
    }

    public void setMerNo(String merNo) {
        this.merNo = merNo;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getTermNo() {
        return termNo;
    }

    public void setTermNo(String termNo) {
        this.termNo = termNo;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPushFlag() {
        return pushFlag;
    }

    public void setPushFlag(String pushFlag) {
        this.pushFlag = pushFlag;
    }
}
