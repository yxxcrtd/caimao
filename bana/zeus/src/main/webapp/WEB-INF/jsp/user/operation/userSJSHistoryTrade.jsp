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
            <h4 class="heading">SJS历史成交</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th colspan="10">SJS历史成交</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td width="10%">成交日期</td>
                    <td width="10%">成交单号</td>
                    <td width="10%">报单号</td>
                    <td width="10%">交易类型</td>
                    <td width="10%">成交价格</td>
                    <td width="10%">成交数量</td>
                    <td width="10%">成交金额</td>
                    <td width="10%">交易费用</td>
                </tr>
                <c:forEach items="${list}" var="trade">
                    <tr>
                        <td>${trade.exch_date}${entrust.exch_time}</td>
                        <td>${trade.match_no}</td>
                        <td>${trade.order_no}</td>
                        <td>${trade.exch_type}</td>
                        <td>${trade.match_price}</td>
                        <td>${trade.match_amount}</td>
                        <td>${trade.exch_bal}</td>
                        <td>${trade.exch_fare}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>