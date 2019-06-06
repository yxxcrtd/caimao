package com.caimao.gjs.server.trade.protocol.sjs.entity.req;

import java.io.Serializable;

public class FSJSDoTradeLoginReq implements Serializable{
    private String user_pwd; //客户交易密码
    private String login_ip; //登录ip
    private String login_server_code; //登录服务器编码

    public String getUser_pwd() {
        return user_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = user_pwd;
    }

    public String getLogin_ip() {
        return login_ip;
    }

    public void setLogin_ip(String login_ip) {
        this.login_ip = login_ip;
    }

    public String getLogin_server_code() {
        return login_server_code;
    }

    public void setLogin_server_code(String login_server_code) {
        this.login_server_code = login_server_code;
    }
}
