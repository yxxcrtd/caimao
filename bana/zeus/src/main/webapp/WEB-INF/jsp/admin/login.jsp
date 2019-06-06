<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>登录</title>
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_head.jsp"/>
</head>
<body class="login-body">
<div class="container-login">
    <div class="login_logo">
        <img src="/img/login_logo.png" width="200">
    </div>
    <form class="form-signin" action="/admin/login" method="post">
        <div class="input-group">
            <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
            <input type="text" class="form-control" placeholder="用户名" name="account" autocomplete="off" autofocus="autofocus">
        </div>
        <div class="input-group">
            <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
            <input type="password" class="form-control" placeholder="密码" name="pwd" autocomplete="off">
        </div>
        <button class="btn btn-lg btn-success btn-block" type="submit" style="margin-top: 10px;">登录</button>
    </form>
</div>
</body>
</html>