package com.caimao.bana.api.service;

import com.caimao.bana.api.enums.FromTypeEnum;

import java.util.Map;

/**
 * 用户鉴权接口
 *
 * @author yanjg
 *         2015年3月20日
 */
public interface CheckService {
    /**
     * 鉴权接口
     */
    public Map checkAccessToken(String access_token, String user_ip, String user_agent, FromTypeEnum fromTypeEnum) throws Exception;

    /**
     * test
     */
    public String test() throws Exception;
}
