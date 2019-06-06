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
            <h4 class="heading">用户列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form method="post" class="form-inline" action="/user/operation/userList" name="searchForm">
                <input type="hidden" name="currentPage" value="1" id="currentPage">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>用户编号：</label>
                    <input type="text" class="form-control input-sm" name="userId" value="${list.userId}" style="width: 140px;">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>用户姓名：</label>
                    <input type="text" class="form-control input-sm" name="userRealName" value="${list.userRealName}" placeholder="用户姓名支持模糊查询" style="width: 140px;">
                </div>
                <br>
                <div class="form-group">
                    <label>手机号码：</label>
                    <input type="text" class="form-control input-sm" name="mobile" value="${list.mobile}" style="width: 140px;">
                </div>
                <div class="form-group">
                    <label>提现状态：</label>
                    <select class="form-control input-sm" name="withdrawStatus" style="width: 140px;">
                        <option value="-1" <c:if test="${-1 == list.withdrawStatus}">selected</c:if>>全部</option>
                        <option value="0" <c:if test="${0 == list.withdrawStatus}">selected</c:if>>正常</option>
                        <option value="1" <c:if test="${1 == list.withdrawStatus}">selected</c:if>>禁止</option>
                    </select>
                </div>
                <button type="button" class="btn btn-primary btn-sm" onclick="redirectTo(1);">查询</button>
            </form>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>用户编号</th>
                    <th>姓名</th>
                    <th>手机号</th>
                    <th>注册时间</th>
                    <th>邀请人编号</th>
                    <th>提现状态</th>
                </tr>
                </thead>
                <c:forEach items="${list.getItems()}" var="u">
                    <tr>
                        <td><a href="../detail?id=${u.userId}">${u.userId}</a></td>
                        <td>${u.userRealName}</td>
                        <td>${u.mobile}</td>
                        <td><fmt:formatDate value="${u.registerDatetime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><a href="../detail?id=${u.refUserId}">${u.refUserId}</td>
                        <td>
                            <c:choose>
                                <c:when test="${u.prohiWithdraw == 0}">正常</c:when>
                                <c:when test="${u.prohiWithdraw == 1}">禁止</c:when>
                            </c:choose>
                        </td>
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