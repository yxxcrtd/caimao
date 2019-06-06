package com.caimao.bana.api.entity.ybk;

import java.io.Serializable;
import java.util.Date;

/**
 * 邮币卡开户信息
 */
public class YBKAccountEntity implements Serializable {
    private Long id;
    private Long userId;
    private String userName;
    private String phoneNo;
    private Integer cardType;
    private String cardPath;
    private String cardOppositePath;
    private String cardNumber;
    private String bankCode;
    private String bankNum;
    private String bankPath;
    private String province;
    private String city;
    private String street;
    private String contactMan;
    private String contacterPhoneNo;
    private Integer sex;
    private Integer exchangeIdApply;
    private Integer status;
    private String reason;
    private Date createDate;
    private String exchangeAccount;

    public void setId(Long id){
        this.id = id;
    }
    public Long getId(){
        return this.id;
    }
    public void setUserId(Long userId){
        this.userId = userId;
    }
    public Long getUserId(){
        return this.userId;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getUserName(){
        return this.userName;
    }
    public void setPhoneNo(String phoneNo){
        this.phoneNo = phoneNo;
    }
    public String getPhoneNo(){
        return this.phoneNo;
    }
    public void setCardType(Integer cardType){
        this.cardType = cardType;
    }
    public Integer getCardType(){
        return this.cardType;
    }
    public void setCardNumber(String cardNumber){
        this.cardNumber = cardNumber;
    }
    public String getCardNumber(){
        return this.cardNumber;
    }
    public void setCardPath(String cardPath){
        this.cardPath = cardPath;
    }
    public String getCardPath(){
        return this.cardPath;
    }
    public void setCardOppositePath(String cardOppositePath){
        this.cardOppositePath = cardOppositePath;
    }
    public String getCardOppositePath(){
        return this.cardOppositePath;
    }
    public void setProvince(String province){
        this.province = province;
    }
    public String getProvince(){
        return this.province;
    }
    public void setCity(String city){
        this.city = city;
    }
    public String getCity(){
        return this.city;
    }
    public void setStreet(String street){
        this.street = street;
    }
    public String getStreet(){
        return this.street;
    }
    public void setBankCode(String bankCode){
        this.bankCode = bankCode;
    }
    public String getBankCode(){
        return this.bankCode;
    }
    public void setBankNum(String bankNum){
        this.bankNum = bankNum;
    }
    public String getBankNum(){
        return this.bankNum;
    }
    public void setBankPath(String bankPath){
        this.bankPath = bankPath;
    }
    public String getBankPath(){
        return this.bankPath;
    }
    public void setContactMan(String contactMan){
        this.contactMan = contactMan;
    }
    public String getContactMan(){
        return this.contactMan;
    }
    public void setContacterPhoneNo(String contacterPhoneNo){
        this.contacterPhoneNo = contacterPhoneNo;
    }
    public String getContacterPhoneNo(){
        return this.contacterPhoneNo;
    }
    public void setSex(Integer sex){
        this.sex = sex;
    }
    public Integer getSex(){
        return this.sex;
    }
    public void setExchangeIdApply(Integer exchangeIdApply){
        this.exchangeIdApply = exchangeIdApply;
    }
    public Integer getExchangeIdApply(){
        return this.exchangeIdApply;
    }
    public void setStatus(Integer status){
        this.status = status;
    }
    public Integer getStatus(){
        return this.status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getExchangeAccount() {
        return exchangeAccount;
    }

    public void setExchangeAccount(String exchangeAccount) {
        this.exchangeAccount = exchangeAccount;
    }
}
