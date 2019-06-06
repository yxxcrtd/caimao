/*
*AdminDao.java
*Created on 2015/5/8 16:51
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.zeus.admin.dao;

import com.caimao.zeus.admin.entity.Rule;
import com.caimao.zeus.admin.entity.User;
import com.caimao.zeus.admin.entity.UserRule;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员Dao
 */
@Repository
public class UserDao extends SqlSessionDaoSupport {
    public User getLogin(String account, String Pwd) throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("account", account);
        paramMap.put("pwd", Pwd);
        return getSqlSession().selectOne("AdminUser.getLogin", paramMap);
    }

    public Rule getRuleByUrl(String ruleUrl) throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("rule", ruleUrl);
        return getSqlSession().selectOne("AdminUser.getRuleByUrl", paramMap);
    }

    public Rule getRuleById(Long Id) throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", Id);
        return getSqlSession().selectOne("AdminUser.getRuleById", paramMap);
    }

    public UserRule checkUserRule(Long userId, Long ruleId) throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("ruleId", ruleId);
        return getSqlSession().selectOne("AdminUser.checkUserRule", paramMap);
    }

    public String getUserRuleStr(Long userId) throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        return getSqlSession().selectOne("AdminUser.getUserRuleStr", paramMap);
    }

    public List<Rule> getUserRuleDetail(String ruleStr) throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ruleStr", ruleStr);
        return getSqlSession().selectList("AdminUser.getUserRuleDetail", paramMap);
    }

    public List<Rule> getAllRule() throws Exception{
        return getSqlSession().selectList("AdminUser.getAllRule");
    }

    public List<User> getUserList() throws Exception{
        return getSqlSession().selectList("AdminUser.getUserList");
    }

    public User getUserById(Long id) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        return getSqlSession().selectOne("AdminUser.getUserById", paramMap);
    }

    public Integer updateUser(Long id, Map<String, Object> updateInfo) throws Exception{
        Map<String, Object> paramMap = new HashMap<>(updateInfo);
        paramMap.put("id", id);
        return getSqlSession().insert("AdminUser.updateUser", paramMap);
    }

    public Integer insertUser(User user) throws Exception{
        return getSqlSession().insert("AdminUser.insertUser", user);
    }

    public Integer insertUserRule(UserRule userRule) throws Exception{
        return getSqlSession().insert("AdminUser.insertUserRule", userRule);
    }

    public Integer updateUserRule(Long id, String rules) throws Exception{
        Map<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("userId", id);
        updateInfo.put("rules", rules);
        return getSqlSession().update("AdminUser.updateUserRule", updateInfo);
    }

    public Integer userStatus(Long id, Map<String, Object> updateInfo) throws Exception{
        Map<String, Object> paramMap = new HashMap<>(updateInfo);
        paramMap.put("id", id);
        return getSqlSession().insert("AdminUser.userStatus", paramMap);
    }

    public Integer userDel(Long id) throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        return getSqlSession().delete("AdminUser.userDel", paramMap);
    }

    public UserRule getUserRuleById(Long id) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", id);
        return getSqlSession().selectOne("AdminUser.getUserRuleById", paramMap);
    }

    public Integer insertRule(Rule rule) throws Exception{
        return getSqlSession().insert("AdminUser.insertRule", rule);
    }

    public Integer updateRule(Long id, Map<String, Object> updateInfo) throws Exception{
        Map<String, Object> paramMap = new HashMap<>(updateInfo);
        paramMap.put("id", id);
        return getSqlSession().update("AdminUser.updateRule", paramMap);
    }

    public Integer ruleDel(String ids) throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ids", ids);
        return getSqlSession().delete("AdminUser.ruleDel", paramMap);
    }

    public Integer ruleSort(Long id, Long sort) throws Exception {
        Map<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("id", id);
        updateInfo.put("sort", sort);
        return getSqlSession().update("AdminUser.updateRuleSort", updateInfo);
    }

    public List<Map<String, String>> ruleHave(Long id) throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        return getSqlSession().selectList("AdminUser.ruleHave", paramMap);
    }
}
