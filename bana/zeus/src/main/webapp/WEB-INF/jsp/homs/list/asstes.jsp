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
            <h4 class="heading">Homs账户资金</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form class="form-inline" role="form" name="searchForm" action="/homs/assets_list" method="post">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>姓名/手机：</label>
                    <input type="text" class="form-control input-sm" name="match" value="${list.match}" placeholder="支持模糊查询" style="width: 120px;">
                    <input type="hidden" name="currentPage" value="1" id="currentPage">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>查找日期：</label>
                    <input type="date" class="form-control input-sm" name="updateDate" id="beginDateTime" value="${list.updateDate}">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>类型：</label>
                    <select name="type">
                        <option value="" <c:if test="${list.type == null}">selected</c:if>>全部</option>
                        <option value="1" <c:if test="${list.type == 1}">selected</c:if> >亏损</option>
                    </select>
                </div>
                <button type="button" class="btn btn-primary btn-sm" onclick="redirectTo(1);">查询</button>
            </form>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>更新时间</th>
                    <th>姓名</th>
                    <th>手机</th>
                    <th>HOMS主账户</th>
                    <th>HOMS子账户</th>
                    <th>借款金额</th>
                    <th>股票市值</th>
                    <th>资产净值</th>
                    <th>可用</th>
                    <th>浮动盈亏</th>
                    <th>亏损警戒线</th>
                    <th>平台亏损值</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list.getItems()}" var="u">
                    <tr>
                    	<td>${u.updateDate}</td>
                        <td>${u.userName}</td>
                        <td>${u.mobile}</td>
                        <td>${u.homsFundAccount}</td>
                        <td>${u.homsCombineId}</td>
                        <td><fmt:formatNumber value="${u.loanAmount/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.totalMarketValue/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.totalNetAssets/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.enableWithdraw/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.totalProfit/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.loanAmount*u.exposureRatio/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${(u.totalNetAssets - u.loanAmount)/100}" type="number"/></td>
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
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>