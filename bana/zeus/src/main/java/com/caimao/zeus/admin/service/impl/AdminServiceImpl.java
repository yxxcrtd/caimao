/*
*AccountServiceImpl.java
*Created on 2015/5/8 17:40
*Copyright (c) 2015 北京火币天下网络技术有限公司. All Rights Reserved
*/
package com.caimao.zeus.admin.service.impl;

import com.caimao.zeus.admin.dao.UserDao;
import com.caimao.zeus.admin.entity.Rule;
import com.caimao.zeus.admin.entity.User;
import com.caimao.zeus.admin.entity.UserRule;
import com.caimao.zeus.admin.service.AdminService;
import com.huobi.commons.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 管理员实现类
 */
@Service("adminServiceImpl")
public class AdminServiceImpl implements AdminService {
    @Resource
    private UserDao userDao;

    @Override
    public User getLogin(String account, String pwd) throws Exception{
        return userDao.getLogin(account, new User().createPwd(pwd));
    }

    @Override
    public Boolean checkRule(Long userId, String ruleUrl) throws Exception {
        Rule rule = userDao.getRuleByUrl(ruleUrl);
        if(rule == null) return false;

        if(rule.getIsPublic() == 1) return true;

        UserRule userRule = userDao.checkUserRule(userId, rule.getId());
        return userRule != null;
    }

    @Override
    public List<Rule> getUserRules(Long userId) throws Exception {
        String userRuleString = userDao.getUserRuleStr(userId);
        if("".equals(userRuleString)){
            return null;
        }
        return userDao.getUserRuleDetail(userRuleString);
    }

    @Override
    public Rule getRule(String ruleUrl) throws Exception {
        return userDao.getRuleByUrl(ruleUrl);
    }

    @Override
    public Rule getRuleById(Long id) throws Exception {
        return userDao.getRuleById(id);
    }

    @Override
    public List<User> getUserList() throws Exception {
        return userDao.getUserList();
    }

    @Override
    public List<Rule> getAllRule() throws Exception {
        return userDao.getAllRule();
    }

    @Override
    public User getUserById(Long id) throws Exception {
        return userDao.getUserById(id);
    }

    @Override
    public Integer saveUser(String account, String pwd, String name, String phone, String email, Byte status, Long id) throws Exception {
        if(id !=null && id > 0){
            Map<String, Object> updateInfo = new HashMap<>();
            updateInfo.put("email", email);
            updateInfo.put("name", name);
            updateInfo.put("phone", phone);
            updateInfo.put("status", status);
            updateInfo.put("updated", System.currentTimeMillis() / 1000);
            if(!"".equals(pwd)){
                updateInfo.put("pwd", new User().createPwd(pwd));
            }
            return userDao.updateUser(id, updateInfo);
        }else{
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setAccount(account);
            user.setPhone(phone);
            user.setPwd(user.createPwd(pwd));
            user.setStatus(status);
            Integer keyId =  userDao.insertUser(user);
            //插入权限数据
            UserRule userRule = new UserRule();
            userRule.setUserId(user.getId());
            userRule.setRules("");
            userDao.insertUserRule(userRule);
            return keyId;
        }
    }

    @Override
    public Integer userStatus(Long id, Byte status) throws Exception {
        if(id == null || id <= 0) throw new Exception("用户不存在");
        Map<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("status", status);
        updateInfo.put("updated", System.currentTimeMillis() / 1000);
        return userDao.userStatus(id, updateInfo);
    }

    @Override
    public Integer userDel(Long id) throws Exception {
        if(id == null || id <= 0) throw new Exception("用户不存在");
        return userDao.userDel(id);
    }

    @Override
    public String getUserRuleTree(Long id) throws Exception {
        UserRule userRule = userDao.getUserRuleById(id);
        if(userRule == null) throw new Exception("管理员权限不存在");
        String[] ruleArray = userRule.getRules().split(",") ;
        Arrays.sort(ruleArray);

        List<Rule> ruleList = userDao.getAllRule();

        //初始化
        List<Map<String, Object>> ruleData = new ArrayList<>();
        Map<String, Object> definedMap = new HashMap<>();
        definedMap.put("id", 0);
        definedMap.put("pId", 0);
        definedMap.put("name", "全选/取消");
        definedMap.put("open", "true");
        ruleData.add(definedMap);

        if(ruleList != null){
            for (Rule rule:ruleList){
                Map<String, Object> tmpMap = new HashMap<>();
                tmpMap.put("id", rule.getId());
                tmpMap.put("pId", rule.getPid());
                tmpMap.put("name", rule.getRuleName());
                tmpMap.put("open", Arrays.binarySearch(ruleArray, Long.toString(rule.getId())) >= 0 ?"true":"false");
                tmpMap.put("checked", Arrays.binarySearch(ruleArray, Long.toString(rule.getId())) >= 0 ?"true":"false");
                ruleData.add(tmpMap);
            }
        }
        return JsonUtil.toJson(ruleData);
    }

    @Override
    public Integer userRuleUpdate(Long id, String[] rules) throws Exception {
        String ruleString = "";
        for (String rule : rules) {
            if (Integer.parseInt(rule) > 0) {
                ruleString += "".equals(ruleString)?rule:"," + rule;
            }
        }
        return userDao.updateUserRule(id, ruleString);
    }

    @Override
    public Integer saveRule(Long pid, String ruleName, String rule, Long sort, Byte ruleType, Byte status, Byte isPublic, Long id) throws Exception {
        if(id !=null && id > 0){
            Map<String, Object> updateInfo = new HashMap<>();
            updateInfo.put("pid", pid);
            updateInfo.put("ruleName", ruleName);
            updateInfo.put("rule", rule);
            updateInfo.put("sort", sort);
            updateInfo.put("ruleType", ruleType);
            updateInfo.put("status", status);
            updateInfo.put("isPublic", isPublic);
            return userDao.updateRule(id, updateInfo);
        }else{
            Rule rule1 = new Rule();
            rule1.setPid(pid);
            rule1.setRuleName(ruleName);
            rule1.setRule(rule);
            rule1.setSort(sort);
            rule1.setRuleType(ruleType);
            rule1.setStatus(status);
            rule1.setIsPublic(isPublic);
            return userDao.insertRule(rule1);
        }
    }

    @Override
    public Integer ruleDel(String ids) throws Exception {
        if(!"".equals(ids)){
            return userDao.ruleDel(ids);
        }else{
            return null;
        }
    }

    @Override
    public Integer ruleSort(Long id, Long sort) throws Exception {
        return userDao.ruleSort(id, sort);
    }

    @Override
    public String ruleHave(Long id) throws Exception {
        List<Map<String, String>> ruleUserList = userDao.ruleHave(id);
        String userRuleList = "<table class=\"table table-bordered table-hover\"><thead><tr><th>账号</th><th>姓名</th></tr></thead><tbody>";
        if(ruleUserList != null){
            for (Map<String, String> hashMap:ruleUserList){
                userRuleList += "<tr><td>" + hashMap.get("account") + "</td><td>" + hashMap.get("name") + "</td></tr>";
            }
        }
        userRuleList += "</tbody></table>";
        return userRuleList;
    }

    @Override
    public Integer changeUserPassword(Long id, String passwordOld, String passwordNew, String passwordNewConfirm) throws Exception {
        User user = userDao.getUserById(id);
        if(user == null) throw new Exception("管理员不存在");
        if(!user.getPwd().equals(new User().createPwd(passwordOld))) throw new Exception("旧密码不正确");
        if(!passwordNew.equals(passwordNewConfirm)) throw new Exception("新密码不一致");

        Map<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("pwd", new User().createPwd(passwordNew));
        return userDao.updateUser(id, updateInfo);
    }
}