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
            <h4 class="heading">当前合约列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form class="form-inline" action="/finance/loan/contractList" method="post" name="searchForm">
                <input type="hidden" name="currentPage" value="1" id="currentPage">
                <div class="form-group">
                    <label>用户编号：</label>
                    <input type="text" class="form-control input-sm" name="userId" value="${list.userId}" style="width: 140px;">
                </div>
                <div class="form-group">
                    <label>用户姓名：</label>
                    <input type="text" class="form-control input-sm" name="userRealName" value="${list.userRealName}" style="width: 140px;">
                </div>
                <div class="form-group">
                    <label>合约编号：</label>
                    <input type="text" class="form-control input-sm" name="contractNo" value="${list.contractNo}" style="width: 140px;">
                </div>
                <div class="form-group">
                    <label>是否还款：</label>
                    <select class="form-control input-sm" name="contractStatus" style="width: 140px;">
                        <option value="-1" <c:if test="${-1 == list.contractStatus}">selected</c:if>>全部</option>
                        <option value="0" <c:if test="${0 == list.contractStatus}">selected</c:if>>未还款</option>
                        <option value="1" <c:if test="${1 == list.contractStatus}">selected</c:if>>已还款</option>
                    </select>
                </div>
                <button type="button" class="btn btn-primary btn-sm" onclick="redirectTo(1);">查询</button>
                <button type="button" class="btn btn-success btn-sm" onclick="export_excel()">导出</button>
            </form>
            <table class="table table-hover table-bordered">
                <thead>
                <tr>
                    <th>合约编号</th>
                    <th>合约类型</th>
                    <th>用户编号</th>
                    <th>用户姓名</th>
                    <th>产品</th>
                    <th>合约开始日期</th>
                    <th>合约结束日期</th>
                    <th>起息日</th>
                    <th>保证金(元)</th>
                    <th>借款金额(元)</th>
                    <th>利率</th>
                    <th>计息方式</th>
                    <th>结息方式</th>
                    <th>结息周期天数</th>
                    <th>产品期限天数</th>
                    <th>是否还款</th>
                    <th>还款金额</th>
                    <th>还款时间</th>
                    <th>应收金额</th>
                    <th>已收金额</th>
                    <th>未收金额</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list.getItems()}" var="u">
                    <tr>
                        <td>${u.contractNo}</td>
                        <td>
                            <c:choose>
                                <c:when test="${u.contractType==0}">借款</c:when>
                                <c:when test="${u.contractType==1}">追加</c:when>
                            </c:choose>
                        </td>
                        <td>${u.userId}</td>
                        <td>${u.userRealName}</td>
                        <td>
                            <c:choose>
                                <c:when test="${u.prodId==1}">免费体验</c:when>
                                <c:when test="${u.prodId==3}">按月配</c:when>
                                <c:when test="${u.prodId==4}">按日配</c:when>
                                <c:when test="${u.prodId==800461611335681}">按月配(免息)</c:when>
                                <c:when test="${u.prodId==800461779107841}">按月</c:when>
                                <c:when test="${u.prodId==800461779107843}">按月配(低息)</c:when>
                                <c:when test="${u.prodId==800461812662273}">按日配(免息)</c:when>
                                <c:when test="${u.prodId==800461812662274}">按日配(免息1)</c:when>
                                <c:when test="${u.prodId==801527635640321}">按月保留</c:when>
                                <c:when test="${u.prodId==802234593968142}">按月配(借贷)</c:when>
                                <c:otherwise>${u.prodId}</c:otherwise>
                            </c:choose>
                        </td>
                        <td>${u.contractBeginDate}</td>
                        <td>${u.contractEndDate}</td>
                        <td>${u.beginInterestDate}</td>
                        <td><fmt:formatNumber value="${u.cashAmount/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.loanAmount/100}" type="number"/></td>
                        <td>${u.interestRate}</td>
                        <td>
                            <c:choose>
                                <c:when test="${u.interestAccrualMode==0}">按自然日计息</c:when>
                                <c:when test="${u.interestAccrualMode==1}">按交易日计息</c:when>
                                <c:when test="${u.interestAccrualMode==2}">按月计息</c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${u.interestSettleMode==0}">先结</c:when>
                                <c:when test="${u.interestSettleMode==1}">后结</c:when>
                            </c:choose>
                        </td>
                        <td>${u.interestSettleDays}</td>
                        <td>${u.prodTerm}</td>
                        <td>
                            <c:choose>
                                <c:when test="${u.contractStatus==0}">未还款</c:when>
                                <c:when test="${u.contractStatus==1}">已还款</c:when>
                            </c:choose>
                        </td>
                        <td><fmt:formatNumber value="${u.repayAmount/100}" type="number"/></td>
                        <td><fmt:formatDate value="${u.repayDatetime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><fmt:formatNumber value="${u.interestShould/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.interestAlready/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${(u.interestShould - u.interestAlready)/100}" type="number"/></td>
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