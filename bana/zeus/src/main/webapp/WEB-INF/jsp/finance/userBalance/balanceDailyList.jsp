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
            <h4 class="heading">用户资产汇总日报列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form class="form-inline" action="/finance/userBalance/balanceDailyList" method="post" name="searchForm">
                <input type="hidden" name="currentPage" value="1" id="currentPage">
                <div class="form-group">
                    <label>日期：</label>
                    <input type="date" class="form-control input-sm" name="dateStart" value="${list.dateStart}"> -
                    <input type="date" class="form-control input-sm" name="dateEnd" value="${list.dateEnd}">
                </div>
                <button type="button" class="btn btn-primary btn-sm" onclick="redirectTo(1);">查询</button>
                <button type="button" class="btn btn-success btn-sm" onclick="export_excel()">导出</button>
            </form>
            <table class="table table-hover table-bordered">
                <thead>
                <tr>
                    <th>日期</th>
                    <th>可用资产</th>
                    <th>冻结资产</th>
                    <th>用户数量</th>
                    <th>累计利息</th>
                    <th>累计借贷金额</th>
                    <th>累计还款金额</th>
                    <th>累计P2P利息金额</th>
                    <th>累计P2P利息发放金额</th>
                    <th>累计P2P投资金额</th>
                    <th>累计P2P投资流标金额</th>
                    <th>累计P2P投资满标金额</th>
                    <th>累计P2P投资已还金额</th>
                    <th>累计充值金额</th>
                    <th>累计提现金额</th>
                    <th>当前借款金额</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list.getItems()}" var="u">
                    <tr>
                        <td><fmt:formatDate value="${u.created}" pattern="yyyy-MM-dd"/></td>
                        <td><fmt:formatNumber value="${u.availableAmount/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.freezeAmount/100}" type="number"/></td>
                        <td>${u.userCount}</td>
                        <td><fmt:formatNumber value="${u.loanInterestTotal/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.loanTotal/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.loanTotalRepay/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.p2pInterestTotal/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.p2pInterestTotalPay/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.p2pInvestTotal/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.p2pInvestTotalFail/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.p2pInvestTotalSuccess/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.p2pInvestTotalRepay/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.depositTotal/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.withdrawTotal/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.loanBalance/100}" type="number"/></td>
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