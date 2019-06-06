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
            <h4 class="heading">项目投资列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form class="form-inline" role="form" name="searchForm" action="/p2p/setting/product/list" method="get">
                <div class="form-group">
                    <label>姓名：</label>
                    <input type="text" class="form-control input-sm" name="userName" value="${req.userName }" placeholder="支持模糊查询" style="width: 120px;">
                </div>
                 <div class="form-group">
                    <label>手机：</label>
                    <input type="text" class="form-control input-sm" name="mobile" value="${req.mobile }" style="width: 120px;">
                </div>
                <div class="form-group">
                    <label>标的时间：</label>
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
                    <th>编号</th>
                    <th>姓名</th>
                    <th>手机</th>
                    <th>本金</th>
                    <th>借款</th>
                    <th>月息利率</th>
                    <th>期限</th>
                    <th>进度</th>
                    <th>剩余</th>
                    <th>财猫</th>
                    <th>投资人</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${req.getItems()}" var="p">
                    <tr>
                        <td>${p.targetId}</td>
                    	<td>${p.userName }</td>
                        <td>${p.mobile}</td>
                        <td><fmt:formatNumber type="number" value="${p.payMargin/100}" /></td>
                        <td><fmt:formatNumber type="number" value="${p.targetAmount/100}" /></td>
                        <td><fmt:formatNumber type="number" value="${p.yearRate*100}" pattern="#.##"/>%</td>
                        <td>${p.liftTime}</td>
                        <td><fmt:formatNumber type="number" value="${(p.actualValue+p.caimaoValue)/p.targetAmount*100}" pattern="#.##"/>%</td>
                        <td><fmt:formatNumber type="number" value="${(p.targetAmount-p.actualValue-p.caimaoValue)/100}" /></td>
                        <td><fmt:formatNumber type="number" value="${p.caimaoValue/100}"/></td>
                        <td>
                            <a href="/p2p/setting/product/invest?targetId=${p.targetId}">${p.investUserNum}</a>
                        </td>
                        <td>
                            <c:if test="${p.targetStatus == 0}">
                                <a href="/p2p/setting/product/caimao_full?target_id=${p.targetId}">直接满标</a>
                            </c:if>
                        </td>
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