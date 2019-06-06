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
            <h4 class="heading">节假日<c:if test="${0 == entity.id}">添加</c:if><c:if test="${0 < entity.id}">修改</c:if></h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form name="searchForm" class="form-inline" action="/setup/holidaySave" method="post">
                <input type="hidden" name="currentPage" value="1" id="currentPage">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label class="col-sm-3 control-label">交易所：</label>
                    <div class="col-sm-9">
                        <select name="exchange" style="width: 150px; border: 1px solid #cccccc; padding: 5px;">
                            <c:forEach items="${exchangeMap}" var="e">
                                <option value="${e.key}" <c:if test="${e.key == list.exchange}">selected="selected"</c:if>>${e.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <br />
                <div class="form-group" style="margin-bottom: 5px;">
                    <label class="col-sm-3 control-label">日期:</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control input-sm" name="holiday" value="${entity.holiday}" placeholder="格式：20151212" style="width: 150px;">
                    </div>
                </div>
                <br />
                <div class="form-group" style="margin-bottom: 5px;">
                    <label class="col-sm-3 control-label">时间段:</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control input-sm" name="tradeTime" value="${entity.tradeTime}" placeholder="格式：0900-1300,1330-1530" style="width: 350px;">
                    </div>
                </div>
                <br />
                <input type="hidden" name="id" value="${entity.id}" />
                <input type="hidden" name="status" value="${entity.status}" />
                <input type="hidden" name="category" value="${entity.category}" />
                <input type="hidden" name="optDate" value="${entity.optDate}" />
                <button type="submit" class="btn btn-primary btn-sm">保存</button>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>