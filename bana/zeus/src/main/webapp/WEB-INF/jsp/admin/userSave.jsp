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
            <h4 class="heading">管理员编辑</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form method="post" class="form-horizontal" style="width: 500px">
                <c:if test="${not empty user}">
                    <input type="hidden" name="id" value="${user.getId()}">
                </c:if>
                <c:if test="${empty user}">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">账号</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" name="account" autocomplete="off">
                        </div>
                    </div>
                </c:if>
                <c:if test="${not empty user}">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">账号</label>
                        <div class="col-sm-9">
                            <p class="form-control-static">${user.getAccount()}</p>
                        </div>
                    </div>
                </c:if>
                <div class="form-group">
                    <label class="col-sm-3 control-label">密码</label>
                    <div class="col-sm-6">
                        <input type="password" class="form-control" name="pwd" autocomplete="off">
                    </div>
                    <div class="col-sm-3">
                        <p class="form-control-static" style="color: #737373"><c:if test="${not empty user}">空表示不修改密码</c:if><c:if test="${empty user}">必须填写密码</c:if></p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">姓名</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="name" autocomplete="off" value="${user.getName()}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">手机</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="phone" autocomplete="off" value="${user.getPhone()}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">邮箱</label>
                    <div class="col-sm-9">
                        <input type="email" class="form-control" name="email" autocomplete="off" value="${user.getEmail()}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">状态</label>
                    <div class="col-sm-9">
                        <label class="radio-inline">
                            <input type="radio" name="status" value="0" <c:if test="${empty user.getStatus() || user.getStatus() == 0}">checked</c:if>> 正常
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="status" value="1" <c:if test="${user.getStatus() == 1}">checked</c:if>> 禁用
                        </label>
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