/*
 * Timeloit.com Inc.
 * Copyright (c) 2012 
 * All Rights Reserved
 */
package com.caimao.bana.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 发送短信和email模板
 * @author yanjg
 */
public class MessageTemplate {
    private static final Logger logger = LoggerFactory.getLogger(MessageTemplate.class);
    //用户邀请消息模板
    public static final String USER_INVITE_TEMPLATE = "您的朋友{0}邀请您加入财猫理财";

}