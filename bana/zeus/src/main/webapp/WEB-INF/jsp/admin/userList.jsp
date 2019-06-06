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
            <h4 class="heading">管理员列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>#</th>
                    <th>账号</th>
                    <th>姓名</th>
                    <th>状态</th>
                    <th>权限</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${userList}" var="v1">
                    <tr>
                        <td>${v1.getId()}</td>
                        <td>${v1.getAccount()}</td>
                        <td>${v1.getName()}</td>
                        <td><c:if test="${v1.getStatus() == 0}">启用</c:if><c:if test="${v1.getStatus() == 1}">禁用</c:if></td>
                        <td>
                            <a href="/admin/userRule?id=${v1.getId()}" class="btn btn-sm btn-success">权限分配</a>
                        </td>
                        <td>
                            <a href="/admin/userStatus?id=${v1.getId()}&status=<c:if test="${v1.getStatus() == 0}">1</c:if><c:if test="${v1.getStatus() == 1}">0</c:if>" class="btn btn-sm btn-info"><c:if test="${v1.getStatus() == 0}">禁用</c:if><c:if test="${v1.getStatus() == 1}">启用</c:if></a>
                            <a href="/admin/userSave?id=${v1.getId()}" class="btn btn-sm btn-primary">编辑</a>
                            <a href="/admin/userDel?id=${v1.getId()}" onclick="return confirm('确认要删除此管理员吗？');" class="btn btn-sm btn-danger">删除</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>