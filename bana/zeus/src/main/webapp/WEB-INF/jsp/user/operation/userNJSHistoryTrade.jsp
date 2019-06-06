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
            <h4 class="heading">NJS历史成交</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th colspan="10">NJS历史成交</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td width="10%">成交日期</td>
                    <td width="10%">委托编号</td>
                    <td width="10%">成交编号</td>
                    <td width="10%">商品代码</td>
                    <td width="10%">买卖标记</td>
                    <td width="10%">成交价格</td>
                    <td width="10%">成交数量</td>
                    <td width="10%">手续费</td>
                    <td width="10%">委托标志</td>
                    <td width="10%">成交额</td>
                </tr>
                <c:forEach items="${list}" var="trade">
                    <tr>
                        <td>${trade.date}${entrust.fTime}</td>
                        <td>${trade.serialNo}</td>
                        <td>${trade.contNo}</td>
                        <td>${trade.wareId}</td>
                        <td>
                            <c:choose>
                                <c:when test="${'B' == trade.buyOrSal}">买入</c:when>
                                <c:when test="${'S' == trade.buyOrSal}">卖出</c:when>
                            </c:choose>
                        </td>
                        <td>${trade.conPrice}</td>
                        <td>${trade.contNum}</td>
                        <td>${trade.tmpMoney}</td>
                        <td>${trade.orderSty}</td>
                        <td>${trade.contQty}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>