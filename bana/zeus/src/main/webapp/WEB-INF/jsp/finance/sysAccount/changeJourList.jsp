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
            <h4 class="heading">资金变动记录</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>#</th>
                    <th>系统账户ID</th>
                    <th>系统账户别名</th>
                    <th>变更类型</th>
                    <th>变更前数量</th>
                    <th>变更数量</th>
                    <th>变更后数量</th>
                    <th>外键ID</th>
                    <th>变更时间</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${req.getItems()}" var="v1">
                    <tr>
                        <td>${v1.id}</td>
                        <td>${v1.sysAccountId}</td>
                        <td>${v1.aliasName}</td>
                        <td>
                            <c:forEach items="<%=com.caimao.zeus.admin.enums.TransType.values()%>" var="v2">
                            <c:if test="${v1.transType == v2.code}">${v2.value}</c:if>
                            </c:forEach>
                        </td>
                        <td>${v1.preAmount / 100}</td>
                        <td class="info">${v1.transAmount / 100}</td>
                        <td>${v1.postAmount / 100}</td>
                        <td>${v1.extId}</td>
                        <td>${v1.created}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>