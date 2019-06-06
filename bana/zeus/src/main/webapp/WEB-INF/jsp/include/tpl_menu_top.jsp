<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="navbar-header">
        <img src="/img/login_logo.png" class="header_logo">
        <a class="navbar-brand" href="/welcome">财猫管理后台</a>
    </div>
    <div id="navbar" class="navbar-collapse collapse">
        <ul class="nav navbar-nav">
            <c:forEach items="${ruleMap['nav_top']}" var="navRule">
                <li id="top_nav_${navRule.getValue().getId()}"<c:if test="${ruleMap['parentIds'].contains(navRule.getValue().getId())}"> class="active"</c:if>><a href="javascript:left_show(${navRule.getValue().getId()});">${navRule.getValue().getRuleName()}</a></li>
            </c:forEach>
        </ul>
        <p class="navbar-text navbar-right user_right">
            Hi! ${adminUser.name}
            <a href="/admin/userPassword">修改密码</a>
            <a href="/admin/logout">退出</a>
        </p>
    </div>
</nav>