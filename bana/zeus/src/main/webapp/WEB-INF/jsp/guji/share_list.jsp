<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>财猫后台管理系统</title>
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_head.jsp"/>
    <%--弹框的--%>
    <link rel="stylesheet" href="/css/avgrund.css" />
    <script type="text/javascript" src="/js/jquery.avgrund.js"></script>
    <style>
        .custom {overflow: auto;}
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_top.jsp"/>
<div class="body_container">
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_left.jsp"/>
    <div class="main_content">
        <div class="container">
            <h4 class="heading">股计推荐股票列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form class="form-inline" role="form" name="searchForm" action="/guji/share_list.html" method="GET">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>昵称(*模糊)：</label>
                    <input type="text" class="form-control input-sm" name="nickName" value="${nickName}" placeholder="支持模糊查询" style="width: 120px;">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>股票代码：</label>
                    <input type="text" class="form-control input-sm" name="stockCode" value="${stockCode}" style="width: 120px;">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>推荐类型：</label>
                    <select name="stockType">
                        <option value="">全部</option>
                        <option value="GP" <c:if test="${stockType == 'GP'}">selected</c:if> >股票</option>
                        <option value="DP" <c:if test="${stockType == 'DP'}">selected</c:if> >大盘</option>
                    </select>
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>操作类型：</label>
                    <select name="operType">
                        <option value="">全部</option>
                        <option value="1" <c:if test="${operType == 1}">selected</c:if> >建仓</option>
                        <option value="2" <c:if test="${operType == 2}">selected</c:if> >调仓</option>
                        <option value="3" <c:if test="${operType == 3}">selected</c:if> >大盘</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary btn-sm">查询</button>
            </form>
            <table class="table table-hover table-bordered" style="width: 100%;">
                <thead>
                <tr>
                    <th>昵称</th>
                    <th>推荐类型</th>
                    <th>股票（代码）</th>
                    <th>时价（目标价）</th>
                    <th>仓位（前仓位）%</th>
                    <th>操作类型</th>
                    <th>原因</th>
                    <th>点赞数</th>
                    <th>创建时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${shareList.getItems()}" var="u">
                    <tr>
                        <td>${u.nickname}</td>
                        <td>
                            <c:choose>
                                <c:when test="${u.stockType == 'GP'}">股票</c:when>
                                <c:when test="${u.stockType == 'DP'}">大盘</c:when>
                            </c:choose>
                        </td>
                        <td>${u.stockName} (${u.stockCode})</td>
                        <td>${u.stockPrice} (${u.targetPrice})</td>
                        <td>
                            <c:if test="${u.stockType == 'GP'}">
                                ${u.positions}% (${u.prePositions}%)
                            </c:if>
                            <c:if test="${u.stockType == 'DP'}">
                                <c:choose>
                                    <c:when test="${u.positions == 1}">看多</c:when>
                                    <c:when test="${u.positions == 2}">中性</c:when>
                                    <c:when test="${u.positions == 3}">看空</c:when>
                                </c:choose>
                            </c:if>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${u.operType == 1}">建仓</c:when>
                                <c:when test="${u.operType == 2}">调仓</c:when>
                                <c:when test="${u.operType == 3}">大盘</c:when>
                            </c:choose>
                        </td>
                        <td>
                            <span>
                                <c:if test="${u.reason.length() > 20}">
                                    ${u.reason.substring(0, 20)}...
                                </c:if>
                                <c:if test="${u.reason.length() < 20}">
                                    ${u.reason}
                                </c:if>
                            </span>
                            <br />
                            <a href="javascript:void(0);" onclick="showReason('reason_${u.shareId}')">点击查看理由</a>
                            <div id="reason_${u.shareId}" style="display: none;">${u.reason}</div>
                        </td>
                        <td>${u.favour}</td>
                        <td><fmt:formatDate value="${u.createTime}" type="both" dateStyle="default" /></td>
                        <td>
                            <a href="javascript:void(0)" onclick="return confirm('你确认要删除这个推荐？') ? delShare(this, ${u.shareId}) : false">删除</a> <br /><br />
                            <c:if test="${u.isPublic == 1}">
                                <a href="javascript:void(0)" onclick="return confirm('你确认要在大厅隐藏这个优秀的推荐？') ? updateIsPublic(this, ${u.shareId}, 0) : false">在大厅隐藏</a>
                            </c:if>
                            <c:if test="${u.isPublic == 0}">
                                <a href="javascript:void(0)" onclick="return confirm('你确认要在大厅显示这个垃圾？') ? updateIsPublic(this, ${u.shareId}, 1) : false">在大厅显示</a>
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
    function showReason(id) {
        var html = $("#"+id).html();
        $('#myModalLabel').html("推荐内容");
        $('#show_alert_msg').html('<div style="text-align: left;">'+html+'</div>');
        $('#myModal').modal('show');
    }
    function delShare(e, id) {
        $.ajax({
            url : '/guji/del_share',
            data : {id : id},
            type : 'post',
            success : function(m) {
                $(e).remove();
                show_alert("删除成功");
            }
        });
    }

    function updateIsPublic(e, id, isPublic) {
        $.ajax({
            url : '/guji/update_is_public',
            data : {id : id, isPublic : isPublic},
            type : 'post',
            success : function(m) {
                $(e).remove();
                show_alert("更新成功");
            }
        });
    }
</script>

<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>