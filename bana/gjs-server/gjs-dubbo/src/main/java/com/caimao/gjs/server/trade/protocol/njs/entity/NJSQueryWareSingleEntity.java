package com.caimao.gjs.server.trade.protocol.njs.entity;

import java.io.Serializable;

/**
 * 查询单个品种
 */
public class NJSQueryWareSingleEntity implements Serializable {
    private String WAREID; //品种代码
    private String WARENAME; //品种名称
    private String LIMITUP; //涨停价
    private String LIMITDOWN; //跌停价
    private String BAILMONEY; //可用保证金
    private String OPENPRICE; //开盘价
    private String NEWPRICE; //最新价
    private String RAISELOSE; //涨跌价
    private String RAISELOSEF; //涨跌幅
    private String HIGHPRICE; //最高价
    private String LOWPRICE; //最低价
    private String AVGPRICE; //平均价
    private String CONTNUM; //成交数量
    private String HOLDNUM; //持仓量
    private String SETPRICE; //昨收价
    private String BUYPRICE1; //买一价
    private String BUYPRICE2; //买二价
    private String BUYPRICE3; //买三价
    private String BUYPRICE4; //买四价
    private String BUYPRICE5; //买五价
    private String BUYQTY1; //买一量
    private String BUYQTY2; //买二量
    private String BUYQTY3; //买三量
    private String BUYQTY4; //买四量
    private String BUYQTY5; //买五量
    private String SALEPRICE1; //卖一价
    private String SALEPRICE2; //卖二价
    private String SALEPRICE3; //卖三价
    private String SALEPRICE4; //卖四价
    private String SALEPRICE5; //卖五价
    private String SALEQTY1; //卖一量
    private String SALEQTY2; //卖二量
    private String SALEQTY3; //卖三量
    private String SALEQTY4; //卖四量
    private String SALEQTY5; //卖五量
    private String MINTRADE; //最小买卖单位
    private String MINPRICE; //最小变动价
    private String MAXSQTY; //最大卖可下单量
    private String MAXBQTY; //最大买可下单量
    private String CYRSHOLD; //当前卖订货量
    private String CURBHOLD; //当前买订货量
    private String ONCENUM; //单笔最大下单量
    private String SPARENUM; //可抵免量
    private String SSPARENUM; //仓单卖可转让量
    private String BSPARENUM; //仓单买可转让量
    private String DEGIT; //小数位数
    private String STORENUM; //交收可申报量
    private String BSETNUM; //买可交收申报量
    private String SSETNUM; //卖可交收申报量
    private String FDATE; //日期
    private String FIRMID; //交易商编号
    private String DINMONEY; //入金
    private String DOUTMONEY; //出金
    private String DCURBAILMONEY; //当日保证金
    private String BALANCEMONEY; //资金余额
    private String ENABLEMONEY; //总可用资金
    private String ENABLEOUTMONEY; //总可出资金
    private String ASSETSUMVALUE; //资产总值
    private String ASSETNETVALUE; //资产净值
    private String SAFEYTAX; //资金安全率

    public String getNEWPRICE() {
        return NEWPRICE;
    }

    public void setNEWPRICE(String NEWPRICE) {
        this.NEWPRICE = NEWPRICE;
    }

    public String getWAREID() {
        return WAREID;
    }

    public void setWAREID(String WAREID) {
        this.WAREID = WAREID;
    }

    public String getWARENAME() {
        return WARENAME;
    }

    public void setWARENAME(String WARENAME) {
        this.WARENAME = WARENAME;
    }

    public String getLIMITUP() {
        return LIMITUP;
    }

    public void setLIMITUP(String LIMITUP) {
        this.LIMITUP = LIMITUP;
    }

    public String getLIMITDOWN() {
        return LIMITDOWN;
    }

    public void setLIMITDOWN(String LIMITDOWN) {
        this.LIMITDOWN = LIMITDOWN;
    }

    public String getBAILMONEY() {
        return BAILMONEY;
    }

    public void setBAILMONEY(String BAILMONEY) {
        this.BAILMONEY = BAILMONEY;
    }

    public String getOPENPRICE() {
        return OPENPRICE;
    }

    public void setOPENPRICE(String OPENPRICE) {
        this.OPENPRICE = OPENPRICE;
    }

    public String getRAISELOSE() {
        return RAISELOSE;
    }

    public void setRAISELOSE(String RAISELOSE) {
        this.RAISELOSE = RAISELOSE;
    }

    public String getRAISELOSEF() {
        return RAISELOSEF;
    }

    public void setRAISELOSEF(String RAISELOSEF) {
        this.RAISELOSEF = RAISELOSEF;
    }

    public String getHIGHPRICE() {
        return HIGHPRICE;
    }

    public void setHIGHPRICE(String HIGHPRICE) {
        this.HIGHPRICE = HIGHPRICE;
    }

    public String getLOWPRICE() {
        return LOWPRICE;
    }

    public void setLOWPRICE(String LOWPRICE) {
        this.LOWPRICE = LOWPRICE;
    }

    public String getAVGPRICE() {
        return AVGPRICE;
    }

    public void setAVGPRICE(String AVGPRICE) {
        this.AVGPRICE = AVGPRICE;
    }

    public String getCONTNUM() {
        return CONTNUM;
    }

    public void setCONTNUM(String CONTNUM) {
        this.CONTNUM = CONTNUM;
    }

    public String getHOLDNUM() {
        return HOLDNUM;
    }

    public void setHOLDNUM(String HOLDNUM) {
        this.HOLDNUM = HOLDNUM;
    }

    public String getSETPRICE() {
        return SETPRICE;
    }

    public void setSETPRICE(String SETPRICE) {
        this.SETPRICE = SETPRICE;
    }

    public String getBUYPRICE1() {
        return BUYPRICE1;
    }

    public void setBUYPRICE1(String BUYPRICE1) {
        this.BUYPRICE1 = BUYPRICE1;
    }

    public String getBUYPRICE2() {
        return BUYPRICE2;
    }

    public void setBUYPRICE2(String BUYPRICE2) {
        this.BUYPRICE2 = BUYPRICE2;
    }

    public String getBUYPRICE3() {
        return BUYPRICE3;
    }

    public void setBUYPRICE3(String BUYPRICE3) {
        this.BUYPRICE3 = BUYPRICE3;
    }

    public String getBUYPRICE4() {
        return BUYPRICE4;
    }

    public void setBUYPRICE4(String BUYPRICE4) {
        this.BUYPRICE4 = BUYPRICE4;
    }

    public String getBUYPRICE5() {
        return BUYPRICE5;
    }

    public void setBUYPRICE5(String BUYPRICE5) {
        this.BUYPRICE5 = BUYPRICE5;
    }

    public String getBUYQTY1() {
        return BUYQTY1;
    }

    public void setBUYQTY1(String BUYQTY1) {
        this.BUYQTY1 = BUYQTY1;
    }

    public String getBUYQTY2() {
        return BUYQTY2;
    }

    public void setBUYQTY2(String BUYQTY2) {
        this.BUYQTY2 = BUYQTY2;
    }

    public String getBUYQTY3() {
        return BUYQTY3;
    }

    public void setBUYQTY3(String BUYQTY3) {
        this.BUYQTY3 = BUYQTY3;
    }

    public String getBUYQTY4() {
        return BUYQTY4;
    }

    public void setBUYQTY4(String BUYQTY4) {
        this.BUYQTY4 = BUYQTY4;
    }

    public String getBUYQTY5() {
        return BUYQTY5;
    }

    public void setBUYQTY5(String BUYQTY5) {
        this.BUYQTY5 = BUYQTY5;
    }

    public String getSALEPRICE1() {
        return SALEPRICE1;
    }

    public void setSALEPRICE1(String SALEPRICE1) {
        this.SALEPRICE1 = SALEPRICE1;
    }

    public String getSALEPRICE2() {
        return SALEPRICE2;
    }

    public void setSALEPRICE2(String SALEPRICE2) {
        this.SALEPRICE2 = SALEPRICE2;
    }

    public String getSALEPRICE3() {
        return SALEPRICE3;
    }

    public void setSALEPRICE3(String SALEPRICE3) {
        this.SALEPRICE3 = SALEPRICE3;
    }

    public String getSALEPRICE4() {
        return SALEPRICE4;
    }

    public void setSALEPRICE4(String SALEPRICE4) {
        this.SALEPRICE4 = SALEPRICE4;
    }

    public String getSALEPRICE5() {
        return SALEPRICE5;
    }

    public void setSALEPRICE5(String SALEPRICE5) {
        this.SALEPRICE5 = SALEPRICE5;
    }

    public String getSALEQTY1() {
        return SALEQTY1;
    }

    public void setSALEQTY1(String SALEQTY1) {
        this.SALEQTY1 = SALEQTY1;
    }

    public String getSALEQTY2() {
        return SALEQTY2;
    }

    public void setSALEQTY2(String SALEQTY2) {
        this.SALEQTY2 = SALEQTY2;
    }

    public String getSALEQTY3() {
        return SALEQTY3;
    }

    public void setSALEQTY3(String SALEQTY3) {
        this.SALEQTY3 = SALEQTY3;
    }

    public String getSALEQTY4() {
        return SALEQTY4;
    }

    public void setSALEQTY4(String SALEQTY4) {
        this.SALEQTY4 = SALEQTY4;
    }

    public String getSALEQTY5() {
        return SALEQTY5;
    }

    public void setSALEQTY5(String SALEQTY5) {
        this.SALEQTY5 = SALEQTY5;
    }

    public String getMINTRADE() {
        return MINTRADE;
    }

    public void setMINTRADE(String MINTRADE) {
        this.MINTRADE = MINTRADE;
    }

    public String getMINPRICE() {
        return MINPRICE;
    }

    public void setMINPRICE(String MINPRICE) {
        this.MINPRICE = MINPRICE;
    }

    public String getMAXSQTY() {
        return MAXSQTY;
    }

    public void setMAXSQTY(String MAXSQTY) {
        this.MAXSQTY = MAXSQTY;
    }

    public String getMAXBQTY() {
        return MAXBQTY;
    }

    public void setMAXBQTY(String MAXBQTY) {
        this.MAXBQTY = MAXBQTY;
    }

    public String getCYRSHOLD() {
        return CYRSHOLD;
    }

    public void setCYRSHOLD(String CYRSHOLD) {
        this.CYRSHOLD = CYRSHOLD;
    }

    public String getCURBHOLD() {
        return CURBHOLD;
    }

    public void setCURBHOLD(String CURBHOLD) {
        this.CURBHOLD = CURBHOLD;
    }

    public String getONCENUM() {
        return ONCENUM;
    }

    public void setONCENUM(String ONCENUM) {
        this.ONCENUM = ONCENUM;
    }

    public String getSPARENUM() {
        return SPARENUM;
    }

    public void setSPARENUM(String SPARENUM) {
        this.SPARENUM = SPARENUM;
    }

    public String getSSPARENUM() {
        return SSPARENUM;
    }

    public void setSSPARENUM(String SSPARENUM) {
        this.SSPARENUM = SSPARENUM;
    }

    public String getBSPARENUM() {
        return BSPARENUM;
    }

    public void setBSPARENUM(String BSPARENUM) {
        this.BSPARENUM = BSPARENUM;
    }

    public String getDEGIT() {
        return DEGIT;
    }

    public void setDEGIT(String DEGIT) {
        this.DEGIT = DEGIT;
    }

    public String getSTORENUM() {
        return STORENUM;
    }

    public void setSTORENUM(String STORENUM) {
        this.STORENUM = STORENUM;
    }

    public String getBSETNUM() {
        return BSETNUM;
    }

    public void setBSETNUM(String BSETNUM) {
        this.BSETNUM = BSETNUM;
    }

    public String getSSETNUM() {
        return SSETNUM;
    }

    public void setSSETNUM(String SSETNUM) {
        this.SSETNUM = SSETNUM;
    }

    public String getFDATE() {
        return FDATE;
    }

    public void setFDATE(String FDATE) {
        this.FDATE = FDATE;
    }

    public String getFIRMID() {
        return FIRMID;
    }

    public void setFIRMID(String FIRMID) {
        this.FIRMID = FIRMID;
    }

    public String getDINMONEY() {
        return DINMONEY;
    }

    public void setDINMONEY(String DINMONEY) {
        this.DINMONEY = DINMONEY;
    }

    public String getDOUTMONEY() {
        return DOUTMONEY;
    }

    public void setDOUTMONEY(String DOUTMONEY) {
        this.DOUTMONEY = DOUTMONEY;
    }

    public String getDCURBAILMONEY() {
        return DCURBAILMONEY;
    }

    public void setDCURBAILMONEY(String DCURBAILMONEY) {
        this.DCURBAILMONEY = DCURBAILMONEY;
    }

    public String getBALANCEMONEY() {
        return BALANCEMONEY;
    }

    public void setBALANCEMONEY(String BALANCEMONEY) {
        this.BALANCEMONEY = BALANCEMONEY;
    }

    public String getENABLEMONEY() {
        return ENABLEMONEY;
    }

    public void setENABLEMONEY(String ENABLEMONEY) {
        this.ENABLEMONEY = ENABLEMONEY;
    }

    public String getENABLEOUTMONEY() {
        return ENABLEOUTMONEY;
    }

    public void setENABLEOUTMONEY(String ENABLEOUTMONEY) {
        this.ENABLEOUTMONEY = ENABLEOUTMONEY;
    }

    public String getASSETSUMVALUE() {
        return ASSETSUMVALUE;
    }

    public void setASSETSUMVALUE(String ASSETSUMVALUE) {
        this.ASSETSUMVALUE = ASSETSUMVALUE;
    }

    public String getASSETNETVALUE() {
        return ASSETNETVALUE;
    }

    public void setASSETNETVALUE(String ASSETNETVALUE) {
        this.ASSETNETVALUE = ASSETNETVALUE;
    }

    public String getSAFEYTAX() {
        return SAFEYTAX;
    }

    public void setSAFEYTAX(String SAFEYTAX) {
        this.SAFEYTAX = SAFEYTAX;
    }
}