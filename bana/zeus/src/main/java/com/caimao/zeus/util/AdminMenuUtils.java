/**
 * Copyright (c) 2006-2012 www.caimao.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.caimao.zeus.util;

import com.caimao.zeus.admin.entity.Rule;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单处理工具包
 */
public class AdminMenuUtils {
    public static Map<Long, Rule> ruleListMenu;
    public static Rule ruleCurMenu;
    public static List<Long> parentIds;
    public static Map<Long, Map<Long, Rule>> parentRuleList;
    public static String ruleMenuHtml;

    public static String ruleListHtml;
    public static String ruleListOptionHtml;
    public static String ruleListIds;

    public Map<String, Object> processMenu(Rule ruleCur, List<Rule> ruleList) throws Exception{
        //初始化一些变量
        ruleListMenu = new HashMap<>();
        parentIds = new ArrayList<>();
        parentRuleList = new HashMap<>();
        ruleMenuHtml = "";
        ruleListHtml = "";
        ruleListOptionHtml = "";
        ruleListIds = "";

        Map<String, Object> returnResult = new HashMap<>();

        //赋值当前菜单
        if(ruleCur.getRuleType() != 2) ruleCurMenu = ruleCur;

        //处理总菜单
        for(Rule rule:ruleList){
            ruleListMenu.put(rule.getId(), rule);
        }
        //获取当前权限父
        getParent(ruleCurMenu);

        //菜单父级组
        parentRuleList = getNavPid();
        returnResult.put("nav_top", parentRuleList.containsKey(0L) ? parentRuleList.get(0L) : null);

        //获取二级&三级菜单
        getNavLeft();
        returnResult.put("nav_left", ruleMenuHtml);

        //获取页面内惨淡
        returnResult.put("nav_page", getNavPage());

        //页面赋值
        returnResult.put("parentIds", parentIds);
        returnResult.put("curRuleId", ruleCurMenu.getId());

        return returnResult;
    }

    /**
     * 获取当前权限父
     * @param rule 当前权限
     * @throws Exception
     */
    private void getParent(Rule rule) throws Exception{
        if(rule != null && rule.getPid() != 0 && ruleListMenu.get(rule.getPid()) != null){
            parentIds.add(ruleListMenu.get(rule.getPid()).getId());
            getParent(ruleListMenu.get(rule.getPid()));
        }
    }

    /**
     * 获取所有的父权限
     * @return map
     * @throws Exception
     */
    private Map<Long, Map<Long, Rule>> getNavPid() throws Exception{
        Map<Long, Map<Long, Rule>> ruleList = new HashMap<>();
        if(ruleListMenu != null){
            for(Map.Entry<Long, Rule> entry:ruleListMenu.entrySet()){
                if(entry.getValue().getRuleType() != 0){
                    Map<Long, Rule> hashMapSub = new HashMap<>();
                    if(ruleList.containsKey(entry.getValue().getPid())){
                        hashMapSub = ruleList.get(entry.getValue().getPid());
                        hashMapSub.put(entry.getValue().getId(), entry.getValue());
                    }else{
                        hashMapSub.put(entry.getValue().getId(), entry.getValue());
                    }
                    ruleList.put(entry.getValue().getPid(), hashMapSub);
                }
            }
        }
        return ruleList;
    }

    /**
     * 获取左侧菜单
     * @throws Exception
     */
    private void getNavLeft() throws Exception{
        if(parentRuleList.containsKey(0L)){
            for (Map.Entry<Long, Rule> entry: parentRuleList.get(0L).entrySet()){
                getNavTree(entry.getValue().getId(), 0);
            }
        }
        if(ruleMenuHtml == null) ruleMenuHtml = "";
    }

    /**
     * 迭代获取左侧菜单树形HTML
     * @param pid 父id
     * @param level 节点层级
     * @throws Exception
     */
    private void getNavTree(Long pid, Integer level) throws Exception{
        if(parentRuleList.containsKey(pid)){
            //是否显示
            Integer isDisplay = 0;
            if((parentIds != null && parentIds.contains(pid)) || level > 0){
                isDisplay = 1;
            }
            //菜单ul
            ruleMenuHtml += "<ul class='" + (level == 0?"menu_list":"") + (isDisplay == 1?"":" hidden") + "' id='left_nav_" + pid + "'>";

            for (Map.Entry<Long, Rule> entry:parentRuleList.get(pid).entrySet()){
                if(level <= 1) {
                    String sonHref = "".equals(entry.getValue().getRule())?"javascript:;":entry.getValue().getRule();
                    Rule son = parentRuleList.containsKey(entry.getValue().getId())?parentRuleList.get(entry.getValue().getId()).entrySet().iterator().next().getValue():null;
                    if(son != null && son.getRuleType() == 1){
                        sonHref = son.getRule();
                    }
                    String currentClass = (((parentIds != null && parentIds.contains(entry.getValue().getId())) || (entry.getValue().getId().equals(ruleCurMenu.getId())))?" class=\"active\"":"");
                    ruleMenuHtml += "<li" + currentClass + "><a href=\"" + sonHref + "\">" + entry.getValue().getRuleName() + "</a>";
                    getNavTree(entry.getValue().getId(), level + 1);
                    ruleMenuHtml += "</li>";
                }
            }
            ruleMenuHtml += "</ul>";
        }
    }

    /**
     * 获取页面内导航
     * @return map
     * @throws Exception
     */
    private Map<Long, Rule> getNavPage() throws Exception{
        Integer parentIdsCnt = parentIds.size();
        Integer pageKey = parentIdsCnt - 3;

        Map<Long, Rule> pageRuleMap = new HashMap<>();
        if(pageKey >= 0 && parentRuleList.containsKey(parentIds.get(pageKey))){
            pageRuleMap = parentRuleList.get(parentIds.get(pageKey));
        }
        return pageRuleMap;
    }

    public String ruleListTree(List<Rule> rules) throws Exception{
        Map<Long, Map<Long, Rule>> ruleList = new HashMap<>();
        if(rules != null){
            for (Rule rule:rules){
                Map<Long, Rule> hashMapSub = new HashMap<>();
                if(ruleList.containsKey(rule.getPid())){
                    hashMapSub = ruleList.get(rule.getPid());
                    hashMapSub.put(rule.getId(), rule);
                }else{
                    hashMapSub.put(rule.getId(), rule);
                }
                ruleList.put(rule.getPid(), hashMapSub);
            }
        }
        createRuleListTree(ruleList, 0L, 0);
        return ruleListHtml;
    }

    private void createRuleListTree(Map<Long, Map<Long, Rule>> ruleList, Long pid, Integer level) throws Exception{
        Map<Byte, String> ruleTypeName = new HashMap<>();
        ruleTypeName.put(new Byte("0"), "操作类型");
        ruleTypeName.put(new Byte("1"), "显示类型");
        ruleTypeName.put(new Byte("2"), "导航类型");

        if(ruleList.containsKey(pid)){
            for (Map.Entry<Long, Rule> entry: ruleList.get(pid).entrySet()){
                ruleListHtml += "<tr class=\"pid_" + entry.getValue().getPid() + "\" data-id=\"" + entry.getValue().getId() + "\" data-level=\"" + level + "\">";
                ruleListHtml += "<td>" + entry.getValue().getId() + "</td>";
                ruleListHtml += "<td><input type=\"text\" class=\"sort_txt\" id=\"sort" + entry.getValue().getId() + "\" value=\"" + entry.getValue().getSort() + "\" onblur=\"saveRuleSort(" + entry.getValue().getId() + ")\"></td>";
                ruleListHtml += "<td class=\"tr_text_left" + (ruleList.containsKey(entry.getValue().getId())?" has_child":"") + "\">" + StringUtils.repeat("　　", level) + entry.getValue().getRuleName() + " <a href='javascript:;' onclick=\"show_rule_user(" + entry.getValue().getId() + ")\" class=\"glyphicon glyphicon-user\"></a></td>";
                ruleListHtml += "<td>" + entry.getValue().getRule() + "</td>";
                ruleListHtml += "<td>" + ruleTypeName.get(entry.getValue().getRuleType()) + "</td>";
                ruleListHtml += "<td>" + (entry.getValue().getStatus() == 1?"启用":"禁用") + "</td>";
                ruleListHtml += "<td><a class=\"btn btn-sm btn-success\" href=\"/admin/ruleSave?pid=" + entry.getValue().getId() + "\">添加子菜单</a> <a class=\"btn btn-sm btn-primary\" href=\"/admin/ruleSave?id=" + entry.getValue().getId() + "\">编辑</a> <a class=\"btn btn-sm btn-danger\" href=\"/admin/ruleDel?id=" + entry.getValue().getId() + "\" onclick=\"return confirm('确认要删除此权限吗，其子权限也会一并删除？')\">删除</a>";
                ruleListHtml += "</tr>";
                if(ruleList.containsKey(entry.getValue().getId())){
                    createRuleListTree(ruleList, entry.getValue().getId(), level + 1);
                }
            }
        }
    }

    public String ruleListTreeOption(List<Rule> rules) throws Exception{
        Map<Long, Map<Long, Rule>> ruleList = new HashMap<>();
        if(rules != null){
            for (Rule rule:rules){
                Map<Long, Rule> hashMapSub = new HashMap<>();
                if(ruleList.containsKey(rule.getPid())){
                    hashMapSub = ruleList.get(rule.getPid());
                    hashMapSub.put(rule.getId(), rule);
                }else{
                    hashMapSub.put(rule.getId(), rule);
                }
                ruleList.put(rule.getPid(), hashMapSub);
            }
        }
        createRuleListTreeOption(ruleList, 0L, 0);
        return "<select name=\"pid\" id=\"pid\" class=\"form-control\"><option value=\"0\">作为一级节点</option>" + ruleListOptionHtml + "</select>";
    }

    private void createRuleListTreeOption(Map<Long, Map<Long, Rule>> ruleList, Long pid, Integer level) throws Exception{
        if(ruleList.containsKey(pid)){
            for (Map.Entry<Long, Rule> entry: ruleList.get(pid).entrySet()){
                ruleListOptionHtml += "<option value=\"" + entry.getValue().getId() + "\">" + StringUtils.repeat("　　", level) + entry.getValue().getRuleName() + "</option>";
                createRuleListTreeOption(ruleList, entry.getValue().getId(), level + 1);
            }
        }
    }

    public String ruleListTreeIds(List<Rule> rules, Long id) throws Exception{
        Map<Long, Map<Long, Rule>> ruleList = new HashMap<>();
        if(rules != null){
            for (Rule rule:rules){
                Map<Long, Rule> hashMapSub = new HashMap<>();
                if(ruleList.containsKey(rule.getPid())){
                    hashMapSub = ruleList.get(rule.getPid());
                    hashMapSub.put(rule.getId(), rule);
                }else{
                    hashMapSub.put(rule.getId(), rule);
                }
                ruleList.put(rule.getPid(), hashMapSub);
            }
        }
        createRuleListIds(ruleList, id);
        return ruleListIds;
    }

    private void createRuleListIds(Map<Long, Map<Long, Rule>> ruleList, Long pid) throws Exception{
        if(ruleList.containsKey(pid)){
            for (Map.Entry<Long, Rule> entry: ruleList.get(pid).entrySet()){
                if("".equals(ruleListIds)){
                    ruleListIds += entry.getValue().getId();
                }else{
                    ruleListIds += "," + entry.getValue().getId();
                }
                createRuleListIds(ruleList, entry.getValue().getId());
            }
        }
    }
}
