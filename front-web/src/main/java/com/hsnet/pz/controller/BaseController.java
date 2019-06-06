/**
 * @Title BaseController.java 
 * @Package com.hsnet.pz.controller 
 * @Description 
 * @author miyb  
 * @date 2014-8-19 上午10:54:17 
 * @version V1.0   
 */
package com.hsnet.pz.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.Resource;

import com.caimao.bana.api.entity.TpzUserEntity;
import com.caimao.bana.api.entity.session.SessionUser;
import com.caimao.bana.api.service.IUserService;
import com.caimao.bana.utils.Session.ControllerContext;
import com.caimao.bana.utils.Session.SessionProvider;
import com.caimao.bana.utils.Session.UserDetailHolder;
import com.hsnet.pz.biz.user.dto.res.F830018Res;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hsnet.pz.ao.member.IMemberAO;
import com.hsnet.pz.ao.service.CustomGenericManageableCaptchaService;

/**
 * 
 * @author zhanggl10620
 * 
 */
@Controller
public class BaseController {

    @Autowired
    protected IMemberAO memberAO;

    @Autowired
    protected SessionProvider sessionProvider;

    @Resource
    private IUserService userService;

    @Resource(name = "imageCaptchaService")
    protected CustomGenericManageableCaptchaService imageCaptchaService;

    /**
     * 获取session user
     * 
     * @return
     */
    protected SessionUser getSessionUser() {
        SessionUser user = (SessionUser) UserDetailHolder.getUserDetail();
        return user;
    }

    /**
     * 获取interface user
     * 
     * @return
     */
    protected F830018Res getUser() {
        return memberAO.doGetUser(getSessionUser().getUser_id());
    }

    /**
     * 获取用户
     * @return
     * @throws Exception
     */
    protected TpzUserEntity getCaimaoUser() throws Exception{
        return userService.getById(getSessionUser().getUser_id());
    }

    /**
     * 获取IP地址
     * 
     * @return
     */
    protected String getRemoteHost() {
    	String ipAddress = ControllerContext.getRequest().getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
            ipAddress = ControllerContext.getRequest().getHeader("Proxy-Client-IP");  
        }  
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
            ipAddress = ControllerContext.getRequest().getHeader("WL-Proxy-Client-IP");  
        }  
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
            ipAddress = ControllerContext.getRequest().getRemoteAddr();  
            if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){  
                //根据网卡取本机配置的IP  
                InetAddress inet=null;  
                try {  
                    inet = InetAddress.getLocalHost();  
                } catch (UnknownHostException e) {  
                    e.printStackTrace();  
                }  
                ipAddress= inet.getHostAddress();  
            }  
        }  
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
        if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15  
            if(ipAddress.indexOf(",")>0){  
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));  
            }  
        }  
        return ipAddress;   
    }


}
