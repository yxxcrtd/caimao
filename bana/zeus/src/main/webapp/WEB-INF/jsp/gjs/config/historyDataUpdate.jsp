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
            <h4 class="heading">更新历史数据</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form class="form-inline" method="post">
                <div class="form-group">
                    <label>交易所：</label>
                    <select class="form-control input-sm" name="exchange" style="width: 140px;">
                        <option value="NJS">南交所</option>
                        <%--<option value="SJS">上金所</option>--%>
                    </select>
                </div>
                <div class="form-group">
                    <label>数据类型：</label>
                    <select class="form-control input-sm" name="dataType" style="width: 140px;">
                        <option value="wt">历史委托</option>
                        <option value="cj">历史成交</option>
                        <option value="cr">历史出入金</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>日期：</label>
                    <input type="date" class="form-control input-sm" name="date" value="${date}" style="width: 140px;">
                </div>
                <button type="submit" class="btn btn-primary btn-sm">更新</button>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>