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
            <h4 class="heading">账户财务流水</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form class="form-inline" action="/homs/homs_jour_import" target="_blank" method="post" enctype="multipart/form-data" style="background-color: #E3E3E3;width: 310px;padding: 10px;">
                <input type="file" name="file" style="float: left">
                <button type="submit" class="btn btn-sm btn-primary">上传</button>
            </form>
            <form class="form-inline" action="/homs/homs_jour_list" method="post" name="searchForm">
                <input type="hidden" name="currentPage" value="1" id="currentPage">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>账户单元编号：</label>
                    <input type="text" class="form-control input-sm" name="accountUnitNo" value="${list.accountUnitNo}" style="width: 140px;">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>　流水号：</label>
                    <input type="text" class="form-control input-sm" name="transNo" value="${list.transNo}" style="width: 140px;">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>业务类型：</label>
                    <input type="text" class="form-control input-sm" name="transBizType" value="${list.transBizType}" style="width: 140px;">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>股票代码：</label>
                    <input type="text" class="form-control input-sm" name="stockCode" value="${list.stockCode}" style="width: 140px;">
                </div>
                <br>

                <div class="form-group" style="margin-bottom: 5px;">
                    <label>账户单元名称：</label>
                    <input type="text" class="form-control input-sm" name="accountUnitName" value="${list.accountUnitName}" style="width: 140px;">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>股票名称：</label>
                    <input type="text" class="form-control input-sm" name="stockName" value="${list.stockName}" style="width: 140px;">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>　　账户：</label>
                    <input type="text" class="form-control input-sm" name="account" value="${list.account}" style="width: 140px;">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>发生金额：</label>
                    <input type="text" class="form-control input-sm" name="transAmount" value="${list.transAmount}" style="width: 140px;">
                </div>
                <br>

                <div class="form-group">
                    <label>　　单元分类：</label>
                    <select class="form-control input-sm" name="accountType" style="width: 140px;">
                        <option value="">全部</option>
                        <option value="1" <c:if test="${1 == list.accountType}">selected</c:if>>管理账户</option>
                        <option value="2" <c:if test="${2 == list.accountType}">selected</c:if>>子账户</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>开始日期：</label>
                    <input type="date" class="form-control input-sm" name="dateStart" value="${list.dateStart}" style="width: 140px;">
                </div>
                <div class="form-group">
                    <label>结束日期：</label>
                    <input type="date" class="form-control input-sm" name="dateEnd" value="${list.dateEnd}" style="width: 140px;">
                </div>
                <button type="button" class="btn btn-primary btn-sm" onclick="redirectTo(1);">查询</button>
                <button type="button" class="btn btn-success btn-sm" onclick="export_excel()">导出</button>
            </form>
            <table class="table table-hover table-bordered">
                <thead>
                <tr>
                    <th>发生日期</th>
                    <th>流水序号</th>
                    <th>发生业务</th>
                    <th>证券代码</th>
                    <th>证券名称</th>
                    <th>账户</th>
                    <th>单元编号</th>
                    <th>单元名称</th>
                    <th>发生金额</th>
                    <th>发生后余额</th>
                    <th>委托方向</th>
                    <th>委托价格</th>
                    <th>发生数量</th>
                    <th>交易费</th>
                    <th>印花税</th>
                    <th>过户费</th>
                    <th>佣金</th>
                    <th>经手费</th>
                    <th>证管费</th>
                    <th>币种</th>
                    <th>发生科目</th>
                    <th>科目发生额</th>
                    <th>科目发生后余额</th>
                    <th>备注</th>
                    <th>技术服务费</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list.getItems()}" var="u">
                    <tr>
                        <td><fmt:formatDate value="${u.transDate}" pattern="yyyy-MM-dd"/></td>
                        <td>${u.transNo}</td>
                        <td>${u.transBizType}</td>
                        <td>${u.stockCode}</td>
                        <td>${u.stockName}</td>
                        <td>${u.account}</td>
                        <td>${u.accountUnitNo}</td>
                        <td>${u.accountUnitName}</td>
                        <td><fmt:formatNumber value="${u.transAmount/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.postAmount/100}" type="number"/></td>
                        <td>${u.entrustDirection}</td>
                        <td><fmt:formatNumber value="${u.entrustPrice/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.entrustAmount/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.tradeFee/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.stampDuty/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.transferFee/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.commission/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.handlingFee/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.secCharges/100}" type="number"/></td>
                        <td>${u.currency}</td>
                        <td>${u.transSubject}</td>
                        <td><fmt:formatNumber value="${u.subjectTransAmount/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.subjectPostAmount/100}" type="number"/></td>
                        <td>${u.remark}</td>
                        <td><fmt:formatNumber value="${u.technicalServices/100}" type="number"/></td>
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