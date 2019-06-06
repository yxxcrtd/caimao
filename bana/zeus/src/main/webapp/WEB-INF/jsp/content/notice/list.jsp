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
            <h4 class="heading">公告列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>时间</th>
                    <th>公告名称</th>
                    <th>来源</th>
                    <th>是否显示</th>
                    <th>是否置顶</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${req.getItems()}" var="u">
                    <tr>
                    	<td><fmt:formatDate value="${u.created}" type="both" dateStyle="default" /></td>
                        <td>${u.title}</td>
                        <td>${u.source}</td>
                        <td>${u.listShow}</td>
                        <td>${u.topShow}</td>
                        <td><a href="/content/notice/edit?id=${u.id}">修改</a> </td>
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