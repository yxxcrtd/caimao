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
            <h4 class="heading">产品列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form class="form-inline" role="form" name="searchForm" action="/p2p/setting/product/list">
                    <input type="hidden" name="currentPage" value="1" id="currentPage">
                    <input type="hidden" name="targetId" value="${req.targetId }">
            </form>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>姓名</th>
                    <th>手机</th>
                    <th>投资</th>
                    <th>时间</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${req.getItems()}" var="u">
                    <tr>
                    	<td>${u.userName }</td>
                        <td>${u.mobile}</td>
                        <td><fmt:formatNumber value="${u.investValue/100}" type="number"/></td>
                        <td><fmt:formatDate value="${u.investCreated}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div id="page" currentPage="${req.currentPage}" pageCount="${req.pages}" ></div>
        </div>
    </div>
</div>
<script type="text/javascript">
	$(function(){
	     $("#page").my_page();
	});
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>