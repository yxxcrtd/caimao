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
            <h4 class="heading">投资人列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form class="form-inline" role="form" name="searchForm" action="/p2p/setting/invest/list2" method="get">
                <div class="form-group">
                    <label>姓名：</label>
                    <input type="text" class="form-control input-sm" name="userName" value="${req.userName }" placeholder="支持模糊查询" style="width: 120px;">
                </div>
                 <div class="form-group">
                    <label>手机：</label>
                    <input type="text" class="form-control input-sm" name="mobile" value="${req.mobile }" style="width: 120px;">
                </div>
                <div class="form-group">
                    <label>投资时间：</label>
                    <input type="date" class="form-control input-sm" name="beginDateTime" id="beginDateTime" value="${req.beginDateTime.substring(0, 10)}"> -
                    <input type="date" class="form-control input-sm" name="endDateTime" id="endDateTime" value="${req.endDateTime.substring(0, 10)}">
                </div>
                <div class="form-group">
                    <label>标的编号：</label>
                    <input type="text" class="form-control input-sm" name="targetId" value="${req.targetId}" style="width: 120px;">
                </div>
                <button type="submit" class="btn btn-primary btn-sm">查询</button>
                <button type="button" class="btn btn-success btn-sm" onclick="export_excel()">导出</button>
            </form>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>投资编号</th>
                    <th>姓名</th>
                    <th>手机</th>
                    <th>标的名称</th>
                    <th>标的期限</th>
                    <th>投资金额</th>
                    <th>年利息</th>
                    <th>已收利息</th>
                    <th>投资时间</th>
                    <th>计息时间</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${req.getItems()}" var="u">
                    <tr>
                        <td>${u.investId}</td>
                    	<td>${u.userName}</td>
                        <td>${u.mobile}</td>
                        <td>${u.targetName} -- (编号: ${u.targetId})</td>
                        <td>${u.liftTime}</td>
                        <td><fmt:formatNumber value="${u.investValue/100}" type="number"/></td>
                        <td><fmt:formatNumber type="number" value="${u.yearRate*100}" pattern="#.##"/>%</td>
                        <td><fmt:formatNumber value="${u.payInterest/100}" type="number"/></td>
                        <td><fmt:formatDate value="${u.investCreated}" type="both" dateStyle="default" /></td>
                        <td><c:if test="${not empty u.interestDatetime}"><fmt:formatDate value="${u.interestDatetime}" type="both" dateStyle="default" /></c:if></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            ${pageHtml}
        </div>
    </div>
</div>
<script type="text/javascript">
    function export_excel(){
        $("form[name='searchForm']").append('<input type="hidden" name="isExp" value="1">');
        $("#currentPage").val(1);
        $("form[name='searchForm']").submit();
    }
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>