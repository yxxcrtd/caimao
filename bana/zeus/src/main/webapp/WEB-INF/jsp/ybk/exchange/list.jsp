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
            <h4 class="heading">交易所列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form method="get" class="form-inline">
                <div class="form-group">
                    <label>状态:</label>
                    <select name="status" class="form-control input-sm">
                        <option value="">全部</option>
                        <c:forEach items="${statusMap}" var="u">
                            <option value="${u.key}" <c:if test="${u.key == status}">selected</c:if>>${u.value}</option>
                        </c:forEach>
                    </select>
                </div>
                <button class="btn btn-sm btn-primary" type="submit">查询</button>
            </form>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>编号</th>
                    <th style="width: 40px;">排序</th>
                    <th>简称</th>
                    <th>号段</th>
                    <th>名称</th>
                    <th>交易日类型</th>
                    <th>开盘收盘时间</th>
                    <th style="width: 100px;">支持银行</th>
                    <th>省份</th>
                    <th>城市</th>
                    <th style="width: 50px;">状态</th>
                    <th>添加时间</th>
                    <th style="width: 135px;">操作</th>
                </tr>
                </thead>
                <c:forEach items="${exchangeList}" var="u">
                    <tr>
                        <td>${u.id}</td>
                        <td><input type="text" class="form-control" value="${u.sortNo}" style="width: 35px;" onblur="exchange_sort(this, ${u.id})" maxlength="2"></td>
                        <td>${u.shortName}</td>
                        <td>${u.number}</td>
                        <td>${u.name}</td>
                        <td>${tradeDayTypeMap[u.tradeDayType]}</td>
                        <td>AM ${u.amBeginTime} - ${u.amEndTime} PM ${u.pmBeginTime} - ${u.pmEndTime}</td>
                        <td>
                            <c:forEach items="${exchangeBanks[u.shortName]}" var="b">
                                ${b.bankName} <br/>
                            </c:forEach>
                        </td>
                        <td>${u.province}</td>
                        <td>${u.city}</td>
                        <td>${statusMap[u.status]}</td>
                        <td><fmt:formatDate value="${u.addDatetime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td>
                            <a href="/ybk/exchange/edit?id=${u.id}" class="btn btn-sm btn-primary">编辑</a>　&nbsp;&nbsp;
                            <a href="/ybk/exchange/del?id=${u.id}" class="btn btn-sm btn-primary" onclick="return confirm('请慎重考虑，确认要删除？')">删除</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript">
    function exchange_sort(obj, id){
        $.post("/ybk/exchange/edit", {id:id, sortNo:obj.value});
    }
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>