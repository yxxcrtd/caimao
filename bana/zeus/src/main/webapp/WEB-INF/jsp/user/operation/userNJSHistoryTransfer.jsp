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
            <h4 class="heading">NJS历史出入金</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th colspan="8">NJS历史出入金</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td width="15%">处理日期</td>
                    <td width="10%">出入金流水号</td>
                    <td width="10%">资金状态</td>
                    <td width="10%">出入金标识</td>
                    <td width="10%">变动类型</td>
                    <td width="10%">币种</td>
                    <td width="10%">变动金额</td>
                </tr>
                <c:forEach items="${list}" var="transfer">
                    <tr>
                        <td>${transfer.deal_date}${transfer.deal_time}</td>
                        <td>${transfer.bank_water_id}</td>
                        <td>
                            <c:choose>
                                <c:when test="${'Y' == transfer.style}">正常</c:when>
                                <c:when test="${'N' == transfer.style}">不正常</c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${'A' == transfer.flag}">结算出入金</c:when>
                                <c:when test="${'B' == transfer.flag}">在线出入金</c:when>
                                <c:when test="${'C' == transfer.flag}">银行出入金</c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${'A' == transfer.change_type}">入金</c:when>
                                <c:when test="${'B' == transfer.change_type}">出金</c:when>
                                <c:when test="${'C' == transfer.change_type}">质押</c:when>
                                <c:when test="${'D' == transfer.change_type}">解除质押</c:when>
                                <c:when test="${'E' == transfer.change_type}">银行入金</c:when>
                                <c:when test="${'F' == transfer.change_type}">银行出金</c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${'0' == transfer.money_type}">人民币</c:when>
                                <c:when test="${'1' == transfer.money_type}">美元</c:when>
                                <c:when test="${'2' == transfer.money_type}">港币</c:when>
                            </c:choose>
                        </td>
                        <td>${transfer.change_money}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>