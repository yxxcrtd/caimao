<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>正在跳转...</title>
    <meta http-equiv="refresh" content="2; URL=${url}">
    <link rel="stylesheet" href="/css/bootstrap.min.css">
</head>
<body>
<div class="body_container">
    <div class="container">
        <h4 class="heading">正在跳转..</h4>
        <c:if test="${failMsg != null}">
            <div class="alert alert-danger">${failMsg}<p><br/><a href="${url}">正在为您跳转</a></p></div>
        </c:if>
        <c:if test="${successMsg != null}">
            <div class="alert alert-success">${successMsg}<p><br/><a href="${url}">正在为您跳转</a></p></div>
        </c:if>
    </div>
</div>
</body>
</html>