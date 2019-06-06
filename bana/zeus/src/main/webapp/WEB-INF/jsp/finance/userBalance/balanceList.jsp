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
            <h4 class="heading">用户资产列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form class="form-inline" action="/finance/userBalance/balanceList" method="post" name="searchForm">
                <input type="hidden" name="currentPage" value="1" id="currentPage">
                <div class="form-group">
                    <label>用户编号：</label>
                    <input type="text" class="form-control input-sm" name="userId" value="${list.userId}" style="width: 140px;">
                </div>
                <div class="form-group">
                    <label>用户姓名：</label>
                    <input type="text" class="form-control input-sm" name="userRealName" value="${list.userRealName}" style="width: 140px;">
                </div>
                <div class="form-group">
                    <label>手机号码：</label>
                    <input type="text" class="form-control input-sm" name="mobile" value="${list.mobile}" style="width: 140px;">
                </div>
                <button type="button" class="btn btn-primary btn-sm" onclick="redirectTo(1);">查询</button>
                <button type="button" class="btn btn-success btn-sm" onclick="export_excel()">导出</button>
            </form>
            <table class="table table-hover table-bordered">
                <thead>
                <tr>
                    <th>配资账户编号</th>
                    <th>用户编号</th>
                    <th>用户姓名</th>
                    <th>币种</th>
                    <th>可用金额(元)</th>
                    <th>冻结金额(元)</th>
                    <th>资产状态</th>
                    <th>备注</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list.getItems()}" var="u">
                    <tr>
                        <td>${u.pzAccountId}</td>
                        <td>${u.userId}</td>
                        <td>${u.userRealName}</td>
                        <td>${u.currencyType}</td>
                        <td><fmt:formatNumber value="${u.avalaibleAmount/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.freezeAmount/100}" type="number"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${u.accountStatus==0}">手动锁定</c:when>
                                <c:when test="${u.accountStatus==1}">正常</c:when>
                                <c:when test="${u.accountStatus==2}">程序自动锁定</c:when>
                                <c:when test="${u.accountStatus==3}">手动解锁</c:when>
                                <c:otherwise>${u.accountStatus}</c:otherwise>
                            </c:choose>
                        </td>
                        <td>${u.remark}</td>
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