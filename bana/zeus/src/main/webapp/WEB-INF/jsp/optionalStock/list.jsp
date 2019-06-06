<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>自选股</title>
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_head.jsp"/>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_top.jsp"/>
<div class="body_container">
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_left.jsp"/>
    <div class="main_content">
        <div class="container">
            <h4 class="heading">热门自选股</h4>
            <form class="form-inline" role="form" method="post" action="/optionalStock/add">
                <div class="form-group">
                    <label>股票代码：</label>
                    <input type="text" class="form-control input-sm" name="stockCode" style="width: 120px;">
                </div>
                <button type="submit" class="btn btn-primary btn-sm">添加</button>
            </form>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>#</th>
                    <th width="100">排序</th>
                    <th>股票代码</th>
                    <th>股票名称</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${optionalStockList}" var="v1">
                    <tr>
                        <td>${v1.id}</td>
                        <td><input type="text" class="sortInput form-control input-sm" data-id="${v1.id}" value="${v1.sort}"></td>
                        <td>${v1.stockCode}</td>
                        <td>${v1.stockName}</td>
                        <td><a href="/optionalStock/del?id=${v1.id}" onclick="return confirm('确认要删除此热门股吗？');" class="btn btn-sm btn-danger">删除</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).on("blur", ".sortInput", function(){
        var id = $(this).attr("data-id");
        var sort = $(this).val();
        $.post("/optionalStock/sort", {id:id, sort:sort}, function(){
            location.reload();
        });
    });
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>