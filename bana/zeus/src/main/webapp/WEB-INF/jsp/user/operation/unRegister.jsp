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
            <h4 class="heading">已发短信但未注册的用户</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form method="post" class="form-inline" action="/user/operation/unRegister" name="searchForm">
                <input type="hidden" name="currentPage" value="1" id="currentPage">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>短信编号：</label>
                    <input type="text" class="form-control input-sm" name="id" value="${list.id}" style="width: 140px;">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>手机号码：</label>
                    <input type="text" class="form-control input-sm" name="mobile" value="${list.mobile}" placeholder="手机号码支持模糊查询" style="width: 150px;">
                </div>
                <br>
                <div class="form-group">
                    <label>发送时间：</label>
                    <input type="date" class="form-control input-sm" name="startDate" value="${list.startDate}" style="width: 140px;">
                    -
                    <input type="date" class="form-control input-sm" name="endDate" value="${list.endDate}" style="width: 140px;">
                </div>
                <button type="button" class="btn btn-primary btn-sm" onclick="redirectTo(1);">查询</button>
            </form>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>短信编号</th>
                    <th>手机号</th>
                    <th>发送时间</th>
                </tr>
                </thead>
                <c:forEach items="${list.getItems()}" var="s">
                    <tr>
                        <td>${s.id}</td>
                        <td>${s.mobile}</td>
                        <td><fmt:formatDate value="${s.createDatetime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div id="page" currentPage="${list.currentPage}" pageCount="${list.pages}" ></div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function(){
        $("#page").my_page();
    });
    function export_excel(){
        $("form[name='searchForm']").append('<input type="hidden" name="isExp" value="1">');
        $("#currentPage").val(1);
        $("form[name='searchForm']").submit();
    }
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>