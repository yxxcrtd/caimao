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
            <h4 class="heading">变更资产</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form method="post" class="form-horizontal" style="width: 500px">
                <c:if test="${not empty sysAccount}">
                    <input type="hidden" name="id" value="${sysAccount.getId()}">
                </c:if>
                <div class="form-group">
                    <label class="col-sm-3 control-label">系统账户ID</label>
                    <div class="col-sm-9">
                        <p class="form-control-static">${sysAccount.getId()}</p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">系统账户别名</label>
                    <div class="col-sm-9">
                        <p class="form-control-static">${sysAccount.getAliasName()}</p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">当前资产</label>
                    <div class="col-sm-9">
                        <p class="form-control-static">${sysAccount.getAvailableAmount() / 100}</p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">变更数量</label>
                    <div class="col-sm-4">
                        <input type="text" class="form-control" name="amount" autocomplete="off">
                    </div>
                    <div class="col-sm-5" style="padding: 0;">
                        <p class="form-control-static" style="color: #737373">大于0为增加资金，小于0为减少资金</p>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-9">
                        <button type="submit" class="btn btn-blood">保存</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>