/*
*IAccountService.java
*Created on 2015/5/8 17:38
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.zeus.admin.service;

import com.caimao.zeus.admin.entity.Rule;
import com.caimao.zeus.admin.entity.User;

import java.util.List;

/**
 * 管理员服务
 */
public interface AdminService {
    public User getLogin(String account, String pwd) throws Exception;

    public Boolean checkRule(Long userId, String ruleUrl) throws Exception;

    public List<Rule> getUserRules(Long UserId) throws Exception;

    public Rule getRule(String ruleUrl) throws Exception;

    public Rule getRuleById(Long id) throws Exception;

    public List<User> getUserList() throws Exception;

    public List<Rule> getAllRule() throws Exception;

    public User getUserById(Long id) throws Exception;

    public Integer saveUser(String account, String pwd, String name, String phone, String email, Byte status, Long id) throws Exception;

    public Integer userStatus(Long id, Byte status) throws Exception;

    public Integer userDel(Long id) throws Exception;

    public String getUserRuleTree(Long id) throws Exception;

    public Integer userRuleUpdate(Long id, String[] rules) throws Exception;

    public Integer saveRule(Long pid, String ruleName, String rule, Long sort, Byte ruleType, Byte status, Byte isPublic, Long id) throws Exception;

    public Integer ruleDel(String ids) throws Exception;

    public Integer ruleSort(Long id, Long sort) throws Exception;

    public String ruleHave(Long id) throws Exception;

    public Integer changeUserPassword(Long id, String passwordOld, String passwordNew, String passwordNewConfirm) throws Exception;
}
