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
            <h4 class="heading">销户</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form class="form-inline" action="/gjs/config/removeGJSAccount" method="post">
                <div class="form-group">
                    <label>交易所：</label>
                    <select class="form-control input-sm" name="exchange" style="width: 140px;">
                        <option value="NJS" selected>南交所</option>
                        <%--<option value="SJS">上金所</option>--%>
                    </select>
                </div>
                <div class="form-group">
                    <label>用户编号：</label>
                    <input type="text" class="form-control input-sm" name="userId" style="width: 140px;">
                </div>
                <div class="form-group">
                    <label>交易员编号：</label>
                    <input type="text" class="form-control input-sm" name="traderId" style="width: 140px;">
                </div>
                <button type="submit" class="btn btn-primary btn-sm">销户</button>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>