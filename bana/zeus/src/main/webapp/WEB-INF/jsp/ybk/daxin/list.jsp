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
            <h4 class="heading">邮币卡打新列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form method="get" class="form-inline">
                <div class="form-group">
                    <label>交易所:</label>
                    <select name="exchangeId" class="form-control input-sm">
                        <option value="0">全部</option>
                        <c:if test="${exchangeList != null}">
                            <c:forEach items="${exchangeList}" var="e">
                                <option value="${e.id}" <c:if test="${e.id == exchangeId}">selected</c:if> >${e.name}</option>
                            </c:forEach>
                        </c:if>
                    </select>
                </div>
                <button class="btn btn-sm btn-primary" type="submit">查询</button>
            </form>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>编号</th>
                    <th>交易所</th>
                    <th>打新名称</th>
                    <th>截止时间</th>
                    <th>打新链接</th>
                    <th>是否显示</th>
                    <th>排序</th>
                    <th>操作</th>
                </tr>
                </thead>
                <c:forEach items="${list.getItems()}" var="u">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.exchangeName}</td>
                        <td>${u.daxinName}</td>
                        <td><fmt:formatDate value="${u.endDatetime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><a href="${u.daxinUrl}" target="_blank">${u.daxinUrl}</a></td>
                        <td>
                            <c:choose>
                                <c:when test="${u.isShow==0}">显示</c:when>
                                <c:when test="${u.isShow==1}">隐藏</c:when>
                            </c:choose>
                        </td>
                        <td>${u.sort}</td>
                        <td><a href="/ybk/daxin/save?id=${u.id}" class="btn btn-sm btn-primary">编辑</a>　<a href="/ybk/daxin/del?id=${u.id}" onclick="return confirm('确认要删除此文章吗？');" class="btn btn-sm btn-danger">删除</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            ${pageHtml}
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>