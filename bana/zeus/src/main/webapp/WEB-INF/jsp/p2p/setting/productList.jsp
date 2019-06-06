<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            <h4 class="heading">产品列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>#</th>
                    <th>产品编号</th>
                    <th>产品名称</th>
                    <th>产品状态</th>
                    <th>最小杠杆</th>
                    <th>最大杠杆</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list}" var="p">
                    <tr>
                    	<td>#</td>
                        <td>${p.prodId}</td>
                        <td>${p.prodName}</td>
                        <td><c:if test="${p.prodStatus=='0'}">未开放 </c:if><c:if test="${p.prodStatus=='1'}">开放  </c:if><c:if test="${p.prodStatus=='2'}">已终止 </c:if></td>
                        <td>${p.prodLoanRatioMin}</td>
                        <td>${p.prodLoanRatioMax}</td>
                        <td>
                            <a href="/p2p/setting/product/edit?prod_id=${p.prodId}" class="btn btn-sm btn-primary">编辑</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>