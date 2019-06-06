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
            <form class="form-inline" role="form" name="searchForm" action="/homs/jour_list" method="post">
                <input type="hidden" name="currentPage" value="1" id="currentPage">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>用户编号：</label>
                    <input type="text" class="form-control input-sm" name="userId" value="${list.userId}" style="width: 140px;">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>用户姓名：</label>
                    <input type="text" class="form-control input-sm" name="userName" value="${list.userName}" placeholder="用户姓名支持模糊查询" style="width: 140px;">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>手机号码：</label>
                    <input type="text" class="form-control input-sm" name="mobile" value="${list.mobile}" style="width: 140px;">
                </div>
                <button type="button" class="btn btn-success btn-sm" onclick="export_excel()" style="margin-bottom: 5px;">导出</button>
                <br>
                <div class="form-group">
                    <label>开始日期：</label>
                    <input type="date" class="form-control input-sm" name="dateStart" value="${list.dateStart}" style="width: 140px;">
                </div>
                <div class="form-group">
                    <label>结束日期：</label>
                    <input type="date" class="form-control input-sm" name="dateEnd" value="${list.dateEnd}" style="width: 140px;">
                </div>
                <div class="form-group">
                    <label>业务类型：</label>
                    <select class="form-control input-sm" name="accountBizType" style="width: 140px;">
                        <option value="">全部</option>
                        <c:forEach items="${accountBizTypeList}" var="u">
                            <option value="${u.key}" <c:if test="${u.key == list.accountBizType}">selected</c:if>>${u.value}</option>
                        </c:forEach>
                    </select>
                </div>
                <button type="button" class="btn btn-primary btn-sm" onclick="redirectTo(1);">查询</button>
            </form>
            <table class="table table-hover table-bordered">
                <thead>
                <tr>
                    <th>流水号</th>
                    <th>用户编号</th>
                    <th>用户姓名</th>
                    <th>用户手机号</th>
                    <th>配资账户号</th>
                    <th>HOMS主账号</th>
                    <th>HOMS子帐号</th>
                    <th>管理子帐号</th>
                    <th>发生金额(元)</th>
                    <th>发生时间</th>
                    <th>发生方向</th>
                    <th>业务类型</th>
                    <th>关联流水号</th>
                    <th>备注</th>
                    <th>会计时间</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list.getItems()}" var="u">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.userId}</td>
                        <td>${u.userName}</td>
                        <td>${u.mobile}</td>
                        <td>${u.pzAccountId}</td>
                        <td>${u.homsFundAccount}</td>
                        <td>${u.homsCombineId}</td>
                        <td>${u.homsManageId}</td>
                        <td>${u.transAmount / 100}</td>
                        <td><fmt:formatDate value="${u.transDatetime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><c:if test="${u.seqFlag == 1}">增加</c:if><c:if test="${u.seqFlag == 2}">减少</c:if></td>
                        <td>${u.accountBizType}</td>
                        <td>${u.refSerialNo}</td>
                        <td>${u.remark}</td>
                        <td>${u.workDate}</td>
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