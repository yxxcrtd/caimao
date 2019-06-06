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
            <h4 class="heading">Homs有持仓可还款账户列表（只限停牌用户）</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form class="form-inline" role="form" name="searchForm" action="/homs/exclude/save" method="get">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>HOMS子账户：</label>
                    <input type="text" class="form-control input-sm" name="homsCombineId" value="" style="width: 120px;" />
                </div>
                <button type="submit" class="btn btn-primary btn-sm">添加</button>
            </form>
            <div style="margin: 10px; color: darkred;">用户还款完成后，请立即在白名单中删除此账户</div>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>姓名</th>
                    <th>手机号</th>
                    <th>HOMS主账户</th>
                    <th>HOMS子账户</th>
                    <th>添加时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list}" var="u">
                    <tr>
                        <td>${u.userRealName}</td>
                        <td>${u.mobile}</td>
                        <td>${u.homsFundAccount}</td>
                        <td>${u.homsCombineId}</td>
                        <td><fmt:formatDate value="${u.created}" type="both" dateStyle="default" /></td>
                        <td><a href="/homs/exclude/delete?homsFundAccount=${u.homsFundAccount}&homsCombineId=${u.homsCombineId}">删除</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>