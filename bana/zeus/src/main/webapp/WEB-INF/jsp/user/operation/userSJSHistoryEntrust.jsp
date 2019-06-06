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
            <h4 class="heading">SJS历史委托</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th colspan="8">SJS历史委托</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td width="15%">委托日期</td>
                    <td width="10%">委托编号</td>
                    <td width="10%">交易类型</td>
                    <td width="10%">委托价格</td>
                    <td width="10%">委托数量</td>
                    <td width="10%">剩余数量</td>
                    <td width="10%">买卖方向</td>
                    <td width="10%">委托状态</td>
                </tr>
                <c:forEach items="${list}" var="trade">
                    <tr>
                        <td>${trade.entr_date}${trade.e_exch_time}</td>
                        <td>${trade.order_no}</td>
                        <td>${trade.exch_type}</td>
                        <td>${trade.entr_price}</td>
                        <td>${trade.entr_amount}</td>
                        <td>${trade.remain_amount}</td>
                        <td>${trade.bs}</td>
                        <td>${trade.entr_stat}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>