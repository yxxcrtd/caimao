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
            <h4 class="heading">产品p2p设置列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>杠杆倍数</th>
                    <th>是否启用p2p</th>
                    <th>最小出资(元)</th>
                    <th>最大出资(元)</th>
                    <th>投资比例</th>
                    <th>管理费(元)</th>
                    <th>管理费比例</th>
                    <th>财猫出资利率</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach begin="${min}" end="${max}" step="1" var="x">
                	<c:if test="${x>0}">
                	<tr>
                	<td>${x}倍杠杆</td>
                	<c:choose>
                		<c:when test="${map[x]!=null}">
                        <td><c:choose><c:when test="${map[x].isAvailable}">启用</c:when><c:otherwise>禁止</c:otherwise></c:choose></td>
                        <td><fmt:formatNumber value="${map[x].chuziMin/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${map[x].chuziMax/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${map[x].chuziRate*100}" type="number" pattern="#.##"/>%</td>
                        <td><fmt:formatNumber value="${map[x].manageFee/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${map[x].manageRate*100}" type="number" pattern="#.##"/>%</td>
                        <td><fmt:formatNumber value="${map[x].caimaoRate*100}" type="number" pattern="#.##"/>%</td>
                		</c:when>
                		<c:otherwise>
                        <td>未开启</td>
                        <td>--</td>
                        <td>--</td>
                        <td>--</td>
                        <td>--</td>
                        <td>--</td>
                        <td>--</td>
                		</c:otherwise>
                	</c:choose>
                		<td>
                    		<a href="/p2p/setting/product/leverEdit?lever=${x}&prod_id=${prodId}" class="btn btn-sm btn-primary">设置</a>
                    	</td>
                	</tr>
                	</c:if>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>