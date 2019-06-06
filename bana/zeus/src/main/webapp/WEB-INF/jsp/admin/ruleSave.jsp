<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>财猫后台管理系统</title>
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_head.jsp"/>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_top.jsp"/>
<div class="body_container">
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_left.jsp"/>
    <div class="main_content">
        <div class="container">
            <h4 class="heading">权限编辑</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form method="post" class="form-horizontal" style="width: 500px">
                <c:if test="${not empty rule}">
                    <input type="hidden" name="id" value="${rule.getId()}">
                </c:if>
                <div class="form-group">
                    <label class="col-sm-3 control-label">父级节点：</label>
                    <div class="col-sm-9">${ruleListTree}</div>
                </div>
                <div class="form-group">
                    <label for="rule_name" class="col-sm-3 control-label">规则名称：</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" id="rule_name" name="rule_name" value="${rule.getRuleName()}">
                    </div>
                </div>
                <div class="form-group">
                    <label for="rule" class="col-sm-3 control-label">规　　则：</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" id="rule" name="rule" value="${rule.getRule()}">
                    </div>
                </div>
                <div class="form-group">
                    <label for="sort" class="col-sm-3 control-label">规则排序：</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" id="sort" name="sort" value="${rule.getSort()}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">节点类型：</label>
                    <div for="rule_type" class="col-sm-9">
                        <select name="rule_type" id="rule_type" class="form-control">
                            <option value="0">操作类型</option>
                            <option value="1">显示类型</option>
                            <option value="2">导航类型</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">是否启用：</label>
                    <div class="col-sm-9">
                        <label class="radio-inline">
                            <input type="radio" name="status" value="1" checked>启用
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="status" value="0">禁用
                        </label>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">是否是公共权限：</label>
                    <div class="col-sm-9">
                        <label class="radio-inline">
                            <input type="radio" name="is_public" value="0" checked>否
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="is_public" value="1">是
                        </label>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-10">
                        <button type="submit" class="btn btn-blood">保存</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">
    <c:if test="${not empty rule}">
    $("#pid").val('${rule.getPid()}');
    $("#rule_type").val('${rule.getRuleType()}');
    $("input[name=status][value=${rule.getStatus()}]").attr("checked",'checked');
    $("input[name=is_public][value=${rule.getIsPublic()}]").attr("checked",'checked');
    </c:if>

    <c:if test="${not empty pid}">
    $("#pid").val("${pid}");
    </c:if>
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>