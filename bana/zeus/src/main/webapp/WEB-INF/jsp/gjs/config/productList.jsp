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
            <h4 class="heading">商品排序</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <div class="btn-group" role="group">
                <a href="/gjs/config/productList?exchange=NJS" class="btn btn-default">南交所</a>
                <a href="/gjs/config/productList?exchange=SJS" class="btn btn-default">上金所</a>
            </div>
            <a href="/gjs/config/productUpdateCache" class="btn btn-primary">更新缓存</a>
            <table class="table table-hover table-bordered" style="width: 1000px;margin-top: 10px;">
                <thead>
                <tr>
                    <th>排序</th>
                    <th>交易所</th>
                    <th>商品代码</th>
                    <th>商品名称</th>
                    <th>交易类型</th>
                    <th>是否是商品</th>
                    <th>是否显示</th>
                    <th>价格单位</th>
                    <th>每手单位</th>
                    <th>价格变动单位</th>
                    <th>价格限制百分比</th>
                    <th>保证金比例</th>
                    <th>手续费费率</th>
                </tr>
                </thead>
                <c:forEach items="${list}" var="u">
                    <tr>
                        <td width="100"><input type="text" value="${u.sort}" class="form-control input-sm" onblur="article_sort(this, ${u.productId})"></td>
                        <td>
                            <c:choose>
                                <c:when test="${u.exchange == 'NJS'}">南交所</c:when>
                                <c:when test="${u.exchange == 'SJS'}">上金所</c:when>
                            </c:choose>
                        </td>
                        <td>${u.prodCode}</td>
                        <td>${u.prodName}</td>
                        <td>
                            <c:choose>
                                <c:when test="${u.tradeType == 0}">默认</c:when>
                                <c:when test="${u.tradeType == 1}">现货</c:when>
                                <c:when test="${u.tradeType == 2}">期货</c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${u.isGoods == 0}">否</c:when>
                                <c:when test="${u.isGoods == 1}">是</c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${u.isShow == 0}">隐藏</c:when>
                                <c:when test="${u.isShow == 1}">显示</c:when>
                            </c:choose>
                        </td>
                        <td>${u.priceUnit}</td>
                        <td>${u.handUnit}</td>
                        <td>${u.priceChangeUnit}</td>
                        <td>${u.priceLimit}</td>
                        <td>${u.marginRatio}</td>
                        <td>${u.marginRatio}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript">
    function article_sort(obj, id){
        $.post("/gjs/config/productOp", {id:id, sort:obj.value});
    }
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>