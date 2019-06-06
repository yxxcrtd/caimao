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
            <h4 class="heading">用户homs账目</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form class="form-inline" action="/finance/userBalance/homsLog" method="get" name="searchForm">
                <div class="form-group">
                    <label>用户编号：</label>
                    <input type="text" class="form-control input-sm" name="userId" value="${userId}" style="width: 140px;">
                </div>
                <button type="submit" class="btn btn-primary btn-sm">查询</button>
            </form>
            <table class="table table-hover table-bordered">
                <thead>
                <tr>
                    <th>姓名</th>
                    <th>homs资产账户id</th>
                    <th>启用日期</th>
                    <th>开始资金</th>
                    <th>借款成功</th>
                    <th>证券买入</th>
                    <th>证券卖出</th>
                    <th>债券买入</th>
                    <th>债券卖出</th>
                    <th>还款</th>
                    <th>提盈</th>
                    <th>追保</th>
                    <th>调整佣金差</th>
                    <th>红利到帐</th>
                    <th>结息</th>
                    <th>资金划转</th>
                    <th>账户回收</th>
                    <th>佣金清算补差</th>
                    <th>现货资金自动核对</th>
                    <th>融券回购</th>
                    <th>融券购回</th>
                    <th>账户余额</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${homsDetail}" var="u">
                    <tr>
                        <td>${u.userRealName}</td>
                        <td>${u.assetId}</td>
                        <td><fmt:formatDate value="${u.createDatetime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><fmt:formatNumber value="${u.beginAmount/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.LoanSuccess/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.stockBuy/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.stockSell/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.bondBuy/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.bondSell/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.repay/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.mentionSurplus/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.paulChase/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.poorAdjustmentCommission/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.bonusArrive/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.bearInterest/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.transferOfFunds/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.accountRecovery/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.liquidationCommissionMakeup/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.stockFundsCheck/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.rongquanhuigou/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.rongquangouhui/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.accountBalance/100}" type="number"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>