package com.caimao.bana.api.entity.req;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 更新用户扩展信息请求对象
 * Created by WangXu on 2015/5/27.
 */
public class FUserRefreshUserExtReq implements Serializable {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    private String nickName;
    private String comefrom;
    private String address;
    private String occupation;
    private String education;
    private String inveExperience;
    private String qq;
    private String weixin;


    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getComefrom() {
        return comefrom;
    }

    public void setComefrom(String comefrom) {
        this.comefrom = comefrom;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getInveExperience() {
        return inveExperience;
    }

    public void setInveExperience(String inveExperience) {
        this.inveExperience = inveExperience;
    }
}
