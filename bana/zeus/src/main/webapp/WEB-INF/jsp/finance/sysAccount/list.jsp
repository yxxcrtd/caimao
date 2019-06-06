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
            <h4 class="heading">系统账户列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>#</th>
                    <th>别名</th>
                    <th>可用资产</th>
                    <th>备注</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${sysAccountList}" var="v1">
                    <tr>
                        <td>${v1.getId()}</td>
                        <td>${v1.getAliasName()}</td>
                        <td>${v1.getAvailableAmount() / 100}</td>
                        <td>${v1.getMemo()}</td>
                        <td>
                            <a href="/finance/sysAccount/save?id=${v1.getId()}" class="btn btn-sm btn-primary">编辑</a>
                            <a href="/finance/sysAccount/changeBalance?id=${v1.getId()}" class="btn btn-sm btn-success">变更资产</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>