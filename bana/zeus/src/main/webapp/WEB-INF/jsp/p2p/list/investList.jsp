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
            <h4 class="heading">投资人汇总列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form class="form-inline" role="form" name="searchForm" action="/p2p/setting/invest/list" method="post">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>姓名：</label>
                    <input type="text" class="form-control input-sm" name="userName" value="${userName}" placeholder="支持模糊查询" style="width: 120px;">
                </div>
                 <div class="form-group" style="margin-bottom: 5px;">
                    <label>手机：</label>
                    <input type="text" class="form-control input-sm" name="mobile" value="${req.mobile}" style="width: 120px;">
                </div>
                <button type="submit" class="btn btn-primary btn-sm">查询</button>
            </form>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>姓名</th>
                    <th>手机</th>
                    <th>累计投资</th>
                    <th>投资次数</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${req.getItems()}" var="u">
                    <tr>
                    	<td>${u.userName}</td>
                        <td>${u.mobile}</td>
                        <td><fmt:formatNumber value="${u.total/100}" type="number"/></td>
                        <td>${u.num}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            ${pageHtml}
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>