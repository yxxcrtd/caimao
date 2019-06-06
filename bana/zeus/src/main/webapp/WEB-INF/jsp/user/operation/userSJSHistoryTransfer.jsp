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
            <h4 class="heading">SJS历史出入金</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th colspan="8">SJS历史出入金</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td width="15%">交易日期</td>
                    <td width="10%">转帐流水号</td>
                    <td width="10%">转账金额</td>
                    <td width="10%">银行日期</td>
                    <td width="10%">银行流水号</td>
                    <td width="10%">是否入账</td>
                    <td width="10%">复核状态</td>
                    <td width="10%">出入金时间</td>
                </tr>
                <c:forEach items="${list}" var="transfer">
                    <tr>
                        <td>${transfer.exch_date}</td>
                        <td>${transfer.serial_no}</td>
                        <td>${transfer.exch_bal}</td>
                        <td>${transfer.bk_plat_date}</td>
                        <td>${transfer.bk_seq_no}</td>
                        <td>${transfer.in_account_flag}</td>
                        <td>${transfer.check_stat1}</td>
                        <td>${transfer.o_date}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>