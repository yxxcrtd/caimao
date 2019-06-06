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
            <h4 class="heading">NJS历史委托</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th colspan="8">NJS历史委托</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td width="15%">委托日期</td>
                    <td width="10%">委托编号</td>
                    <td width="10%">品种名称</td>
                    <td width="10%">买卖标记</td>
                    <td width="10%">价格</td>
                    <td width="10%">委托数量</td>
                    <td width="10%">成交数量</td>
                    <td width="10%">交易单状态</td>
                </tr>
                <c:forEach items="${list}" var="entrust">
                    <tr>
                        <td>${entrust.date}${entrust.time}</td>
                        <td>${entrust.serialNo}</td>
                        <td>${entrust.wareId}</td>
                        <td>
                            <c:choose>
                                <c:when test="${'B' == entrust.buyOrSal}">买入</c:when>
                                <c:when test="${'S' == entrust.buyOrSal}">卖出</c:when>
                            </c:choose>
                        </td>
                        <td>${entrust.price}</td>
                        <td>${entrust.num}</td>
                        <td>${entrust.contNum}</td>
                        <td>
                            <c:choose>
                                <c:when test="${'A' == entrust.cStatus}">未成交</c:when>
                                <c:when test="${'B' == entrust.cStatus}">部分成交</c:when>
                                <c:when test="${'C' == entrust.cStatus}">全部成交</c:when>
                                <c:when test="${'D' == entrust.cStatus}">已撤单</c:when>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>