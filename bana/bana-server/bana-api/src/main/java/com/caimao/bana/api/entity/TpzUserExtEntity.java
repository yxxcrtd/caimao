package com.caimao.bana.api.entity;

import java.io.Serializable;

/**
 * 用户扩展信息
 * Created by WangXu on 2015/5/18.
 */
public class TpzUserExtEntity implements Serializable {
    private static final long serialVersionUID = 6055176124563362029L;
    private Long userId;
    private String gender;
    private String birthday;
    private String userIntro;
    private String userComefrom;
    private String userQq;
    private String userWeixin;
    private String userPhone;
    private String userPhoto;
    private String userSignature;
    private String userOccupation;
    private String userEducation;
    private String invrEmpiric;
    private Integer prohiWithdraw;
    private String idcardPositivePath;
    private String idcardOppositePath;

    public String getUserWeixin() {
        return userWeixin;
    }

    public void setUserWeixin(String userWeixin) {
        this.userWeixin = userWeixin;
    }

    public Integer getProhiWithdraw() {
        return prohiWithdraw;
    }

    public void setProhiWithdraw(Integer prohiWithdraw) {
        this.prohiWithdraw = prohiWithdraw;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUserIntro() {
        return userIntro;
    }

    public void setUserIntro(String userIntro) {
        this.userIntro = userIntro;
    }

    public String getUserComefrom() {
        return userComefrom;
    }

    public void setUserComefrom(String userComefrom) {
        this.userComefrom = userComefrom;
    }

    public String getUserQq() {
        return userQq;
    }

    public void setUserQq(String userQq) {
        this.userQq = userQq;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserSignature() {
        return userSignature;
    }

    public void setUserSignature(String userSignature) {
        this.userSignature = userSignature;
    }

    public String getUserOccupation() {
        return userOccupation;
    }

    public void setUserOccupation(String userOccupation) {
        this.userOccupation = userOccupation;
    }

    public String getUserEducation() {
        return userEducation;
    }

    public void setUserEducation(String userEducation) {
        this.userEducation = userEducation;
    }

    public String getInvrEmpiric() {
        return invrEmpiric;
    }

    public void setInvrEmpiric(String invrEmpiric) {
        this.invrEmpiric = invrEmpiric;
    }

    public String getIdcardPositivePath() {
        return idcardPositivePath;
    }

    public void setIdcardPositivePath(String idcardPositivePath) {
        this.idcardPositivePath = idcardPositivePath;
    }

    public String getIdcardOppositePath() {
        return idcardOppositePath;
    }

    public void setIdcardOppositePath(String idcardOppositePath) {
        this.idcardOppositePath = idcardOppositePath;
    }
}
