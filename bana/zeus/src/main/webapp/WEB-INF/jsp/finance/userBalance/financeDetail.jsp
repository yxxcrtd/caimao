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
            <h4 class="heading">用户资产流量</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form class="form-inline" action="/finance/userBalance/financeDetail" method="post" name="searchForm" style="margin-bottom: 20px;">
                <div class="form-group">
                    <label>用户编号：</label>
                    <input type="text" class="form-control input-sm" name="userId" value="${req.userId}" style="width: 140px;">
                </div>
                <div class="form-group">
                    <label>日期：</label>
                    <input type="date" class="form-control input-sm" name="dateStart" value="${req.dateStart}"> -
                    <input type="date" class="form-control input-sm" name="dateEnd" value="${req.dateEnd}">
                </div>
                <button type="submit" class="btn btn-primary btn-sm">查询</button>
            </form>
            <table class="table table-bordered" style="float: left;width: 350px;">
                <tr class="danger">
                    <th colspan="2" style="text-align: center">收入</th>
                </tr>
                <c:forEach items="${accountIn}" var="u">
                    <tr>
                        <td>${u.typeName}</td>
                        <td><fmt:formatNumber value="${financeDetail['accountIn'.concat(u.typeCode)] / 100}" type="number"/></td>
                    </tr>
                </c:forEach>
            </table>

            <table class="table table-bordered" style="float: left;width: 350px;margin-left: -1px;">
                <tr class="success">
                    <th colspan="2" style="text-align: center">支出</th>
                </tr>
                <c:forEach items="${accountOut}" var="u">
                    <tr>
                        <td>${u.typeName}</td>
                        <td><fmt:formatNumber value="${financeDetail['accountOut'.concat(u.typeCode)] / 100}" type="number"/></td>
                    </tr>
                </c:forEach>
            </table>
            <div style="clear: both"></div>
            <div class="alert alert-info" style="width: 700px;">
                流量计算余额：<fmt:formatNumber value="${financeDetail['accountTotal'] / 100}" type="number"/>&nbsp;&nbsp;&nbsp;&nbsp;
                当前余额：<fmt:formatNumber value="${tpzAccount.avalaibleAmount / 100}" type="number"/>
            </div>
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