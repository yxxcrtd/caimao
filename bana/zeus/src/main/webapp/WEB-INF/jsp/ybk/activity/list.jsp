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
</head>
<body>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_top.jsp"/>
<div class="body_container">
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_left.jsp"/>
    <div class="main_content">
        <div class="container">
            <h4 class="heading">活动列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>

            <div style="margin-bottom: 15px;">
                <p>活动聚合页上边提示的一句话？</p>
                <form action="/ybk/activity/update_topic" method="post">
                    <label>
                        <input type="text" name="topic" value="${topic}" style="width: 300px;"/>
                    </label>
                    <input type="submit" value="更新" />
                </form>
            </div>

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
                <div class="form-group">
                    <label>日期：</label>
                    <input type="date" class="form-control input-sm" name="dateStart" value="${dateStart}"> -
                    <input type="date" class="form-control input-sm" name="dateEnd" value="${dateEnd}">
                </div>
                <button class="btn btn-sm btn-primary" type="submit">查询</button>
            </form>

            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>编号</th>
                    <th>排序</th>
                    <th>交易所</th>
                    <th>活动标题</th>
                    <th>活动Banner</th>
                    <th>截止日期</th>
                    <th>发布时间</th>
                    <th>是否显示</th>
                    <th>操作</th>
                </tr>
                </thead>
                <c:forEach items="${list.getItems()}" var="u">
                    <tr>
                        <td>${u.id}</td>
                        <td width="100"><input type="text" value="${u.sort}" class="form-control input-sm" onblur="article_sort(this, ${u.id})"></td>
                        <td><a href="${u.activityUrl}" target="_blank">${u.exchangeName}</a></td>
                        <td><a href="${u.activityUrl}" target="_blank">${u.activityName}</a></td>
                        <td><a href="javascript:void(0);" class="showPic" data-img-src="${ybkUrl}${u.activityBanner}">Banner点击查看</a></td>
                        <td><fmt:formatDate value="${u.endDatetime}" pattern="yyyy-MM-dd"/></td>
                        <td><fmt:formatDate value="${u.created}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${u.isShow==0}">显示</c:when>
                                <c:when test="${u.isShow==1}">隐藏</c:when>
                            </c:choose>
                        </td>
                        <td><a href="/ybk/activity/save?id=${u.id}" class="btn btn-sm btn-primary">编辑</a>　<a href="/ybk/activity/del?id=${u.id}" onclick="return confirm('确认要删除此活动吗？');" class="btn btn-sm btn-danger">删除</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            ${pageHtml}
        </div>
    </div>
</div>
<script type="text/javascript">
    function article_sort(obj, id){
        $.post("/ybk/activity/sort", {id:id, sort:obj.value});
    }

    $(document).ready(function() {
        $(".showPic").click(function() {
            var picUrl = $(this).attr('data-img-src');
            $(this).avgrund({
                width : 900,
                height: 500,
                holderClass: 'custom',
                showClose: true,
                showCloseText: 'Close',
                enableStackAnimation: false,
                openOnEvent : false,
                onBlurContainer: '.container',
                template: '<div style="text-align: center;"><img src="'+ picUrl +'" width="800px" height="450px" /></div>'
            });
        });
    });

</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>