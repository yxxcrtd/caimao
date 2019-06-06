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
            <h4 class="heading">实时快讯列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form method="post" class="form-inline" action="/gjs/articleJin10/list" name="searchForm">
                <input type="hidden" name="currentPage" value="1" id="currentPage">
                <div class="form-group">
                    <label>类型:</label>
                    <select name="category" class="form-control input-sm">
                        <option value="">全部</option>
                        <option value="1" <c:if test="${1 == list.category}">selected="selected"</c:if>>一般新闻</option>
                        <option value="2" <c:if test="${2 == list.category}">selected="selected"</c:if>>重要新闻</option>
                        <option value="3" <c:if test="${3 == list.category}">selected="selected"</c:if>>一般数据</option>
                        <option value="4" <c:if test="${4 == list.category}">selected="selected"</c:if>>重要数据</option>
                    </select>
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>内容：</label>
                    <input type="text" class="form-control input-sm" name="content" value="${list.content}" placeholder="内容支持模糊查询" style="width: 200px;">
                </div>
                <button class="btn btn-sm btn-primary" type="submit">查询</button>
            </form>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr style="background-color: #EEEEEE;">
                    <th width="10%" style="text-align: center;">资讯类型</th>
                    <th width="10%" style="text-align: center;">发布时间</th>
                    <th width="68%">内容</th>
                    <th width="12%" style="text-align: center;">操作</th>
                </tr>
                </thead>
                <c:forEach items="${list.getItems()}" var="u">
                    <tr class="current">
                        <td style="text-align: center;">
                            <c:choose>
                                <c:when test="${1 == u.category}"><img src="/img/1.png" title="一般新闻" /></c:when>
                                <c:when test="${2 == u.category}"><img src="/img/2.png" title="重要新闻" /></c:when>
                                <c:when test="${3 == u.category}"><img src="/img/3.png" title="一般数据" /></c:when>
                                <c:when test="${4 == u.category}"><img src="/img/4.png" title="重要数据" /></c:when>
                            </c:choose>
                        </td>
                        <td style="text-align: center;">
                            <div>${u.time}</div>
                            <div>${u.timeId}</div>
                        </td>
                        <td <c:if test="${2 == u.category || 4 == u.category}">style="color: #FF0000;"</c:if>>
                            <div class="content">${u.content}</div>
                            <c:if test="${3 == u.category || 4 == u.category}">
                                <div>
                                    前值：${u.beforeValue}&nbsp;&nbsp;
                                    预期：${u.expect}&nbsp;&nbsp;
                                    实际：${u.infact}&nbsp;&nbsp;
                                    <c:choose>
                                        <c:when test="${1 == u.star}"><img src="/img/star1.png" style="width: 15px;" title="1星数据" /></c:when>
                                        <c:when test="${2 == u.star}"><img src="/img/star2.png" style="width: 15px;" title="2星数据" /></c:when>
                                        <c:when test="${3 == u.star}"><img src="/img/star3.png" style="width: 15px;" title="3星数据" /></c:when>
                                        <c:when test="${4 == u.star}"><img src="/img/star4.png" style="width: 15px;" title="4星数据" /></c:when>
                                        <c:when test="${5 == u.star}"><img src="/img/star5.png" style="width: 15px;" title="5星数据" /></c:when>
                                    </c:choose>
                                    &nbsp;&nbsp;
                                    <c:if test="${1 == u.result}">利空金银</c:if>
                                    <c:if test="${2 == u.result}">利多金银</c:if>
                                    <c:if test="${3 == u.result}">影响较小</c:if>
                                    <c:if test="${4 == u.result}">利多原油</c:if>
                                    <c:if test="${5 == u.result}">利空原油</c:if>
                                </div>
                            </c:if>
                        </td>
                        <td>
                            <a href="/gjs/articleJin10/edit?id=${u.id}" class="btn btn-sm btn-primary">编辑</a>
                            <a href="javascript:;" class="btn btn-sm btn-danger">删除</a>
                            <span class="did" style="display: none;">${u.id}</span>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${0 == list.getItems().size()}">
                    <tr style="text-align: center"><td colspan="10">没有数据</td></tr>
                </c:if>
                </tbody>
            </table>
            <%--${pageHtml}--%>共有<b> ${list.totalCount} </b>条记录
            <div id="page" currentPage="${list.currentPage}" pageCount="${list.pages}"></div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function(){
        $("#page").my_page();
        $(".content p").css("margin", "0");
        $(".btn-danger").on("click", function() {
            var This = $(this);
            var cur = parseInt(This.siblings(".did").text());
            if (confirm("确定要删除吗？")) {
                $.get("/gjs/articleJin10/del", {"id" : cur}, function(data) {
                    if ("ok" == data) {
                        This.parents(".current").remove();
                    } else {
                        alert("发生未知错误，请稍后重试！");
                    }
                });
            }
        });
    });
    function export_excel(){
        $("form[name='searchForm']").append('<input type="hidden" name="isExp" value="1">');
        $("#currentPage").val(1);
        $("form[name='searchForm']").submit();
    }
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>