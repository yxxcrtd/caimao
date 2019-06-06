<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
            <h4 class="heading">禁买股票列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>

            <form method="post" class="form-inline" action="/content/prohibit_stock_save">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>股票名称：</label>
                    <input type="text" class="form-control input-sm" name="name" style="width: 140px;" />
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>股票代码：</label>
                    <input type="text" class="form-control input-sm" name="code" style="width: 140px;">
                </div>
                <button type="submit" class="btn btn-primary btn-sm">添加</button>
            </form>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>股票名称</th>
                    <th>股票代码</th>
                    <th>添加时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list}" var="u">
                    <tr>
                        <td>${u.name}</td>
                        <td>${u.code}</td>
                    	<td><fmt:formatDate value="${u.created}" type="both" dateStyle="default" /></td>
                        <td><a href="/content/prohibit_stock_del?id=${u.id}">删除</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>