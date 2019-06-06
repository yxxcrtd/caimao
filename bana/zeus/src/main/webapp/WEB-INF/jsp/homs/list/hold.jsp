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
            <h4 class="heading">Homs账户持仓</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form class="form-inline" role="form" name="searchForm" action="/homs/hold_list" method="post">
                <input type="hidden" name="currentPage" value="1" id="currentPage">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>&nbsp;&nbsp;&nbsp;userId：</label>
                    <input type="text" class="form-control input-sm" name="userId" value="${list.userId}" placeholder="用户ID" style="width: 140px;">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>&nbsp;　　　　姓名：</label>
                    <input type="text" class="form-control input-sm" name="realName" value="${list.realName}" placeholder="姓名" style="width: 140px;">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>　　　手机号：</label>
                    <input type="text" class="form-control input-sm" name="mobile" value="${list.mobile}" placeholder="手机号" style="width: 140px;">
                </div>
                <br>
                <div class="form-group">
                    <label>查找日期：</label>
                    <input type="date" class="form-control input-sm" name="updated" id="beginDateTime" value="${list.updated}" style="width: 140px;">
                </div>
                <div class="form-group">
                    <label>HOMS子账户：</label>
                    <input type="text" class="form-control input-sm" name="homsCombineId" value="${list.homsCombineId}" style="width: 140px;">
                </div>
                <div class="form-group">
                    <label>HOMS主账户：</label>
                    <input type="text" class="form-control input-sm" name="homsFundAccount" value="${list.homsFundAccount}" style="width: 140px;">
                </div>
                <button type="button" class="btn btn-primary btn-sm" onclick="redirectTo(1);">查询</button>
            </form>
            <table class="table table-hover table-bordered">
                <thead>
                <tr>
                    <th>更新时间</th>
                    <th>用户ID</th>
                    <th>姓名</th>
                    <th>手机号</th>
                    <th>HOMS主账户</th>
                    <th>HOMS子账户</th>
                    <th>交易类型</th>
                    <th>股票代码</th>
                    <th>股票名称</th>
                    <th>当前数量</th>
                    <th>可用数量</th>
                    <th>成本</th>
                    <th>市值</th>
                    <th>买数量</th>
                    <th>卖数量</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list.getItems()}" var="u">
                    <tr>
                        <td>${u.updated}</td>
                        <td>${u.userId}</td>
                        <td>${u.realName}</td>
                        <td>${u.mobile}</td>
                        <td>${u.homsFundAccount}</td>
                        <td>${u.homsCombineId}</td>
                        <td>${u.exchangeType}</td>
                        <td>${u.stockCode}</td>
                        <td>${u.stockName}</td>
                        <td>${u.currentAmount}</td>
                        <td>${u.enableAmount}</td>
                        <td>${u.costBalance}</td>
                        <td>${u.marketValue}</td>
                        <td>${u.buyAmount}</td>
                        <td>${u.sellAmount}</td>
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