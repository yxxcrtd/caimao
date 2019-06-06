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
            <h4 class="heading">文章列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form method="post" class="form-inline" action="/gjs/article/list" name="searchForm">
                <input type="hidden" name="currentPage" value="1" id="currentPage">
                <div class="form-group">
                    <label>分类:</label>
                    <select name="categoryId" class="form-control input-sm">
                        <option value="">全部</option>
                        <option value="1" <c:if test="${1 == list.categoryId}">selected="selected"</c:if> >公告</option>
                        <option value="2" <c:if test="${2 == list.categoryId}">selected="selected"</c:if> >文章</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>是否显示:</label>
                    <select name="isShow" class="form-control input-sm">
                        <option value="">全部</option>
                        <option value="0" <c:if test="${0 == list.isShow}">selected</c:if>>显示</option>
                        <option value="1" <c:if test="${1 == list.isShow}">selected</c:if>>隐藏</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>是否热门:</label>
                    <select name="isHot" class="form-control input-sm">
                        <option value="">全部</option>
                        <option value="0" <c:if test="${0 == list.isHot}">selected</c:if>>正常</option>
                        <option value="1" <c:if test="${1 == list.isHot}">selected</c:if>>热门</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>日期：</label>
                    <input type="date" class="form-control input-sm" name="dateStart" value="${list.dateStart}"> -
                    <input type="date" class="form-control input-sm" name="dateEnd" value="${list.dateEnd}">
                </div>
                <button class="btn btn-sm btn-primary" type="submit">查询</button>
            </form>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>编号</th>
                    <th>排序</th>
                    <th>分类</th>
                    <th>来源</th>
                    <th>标题</th>
                    <th>发布时间</th>
                    <th>阅读数</th>
                    <th>热门？</th>
                    <th>审核？</th>
                    <th>操作</th>
                </tr>
                </thead>
                <c:forEach items="${list.getItems()}" var="u">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.sort}</td>
                        <td>
                            <c:choose>
                                <c:when test="${u.categoryId == 1}">公告</c:when>
                                <c:when test="${u.categoryId == 2}">文章</c:when>
                            </c:choose>
                        </td>
                        <td>${u.sourceName}</td>
                        <td>${u.title}</td>
                        <td>${u.created}</td>
                        <td>${u.viewCount}</td>
                        <td>
                            <c:choose>
                                <c:when test="${u.isHot == 0}">正常</c:when>
                                <c:when test="${u.isHot == 1}">热门</c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${u.isShow == 0}">显示</c:when>
                                <c:when test="${u.isShow == 1}"><span style="color: #FF0000;">隐藏</span></c:when>
                            </c:choose>
                        </td>
                        <td>
                            <a href="/gjs/article/save?id=${u.id}" class="btn btn-sm btn-primary">编辑</a>
                            <a href="/gjs/article/del?id=${u.id}" onclick="return confirm('确认要删除此文章吗？');" class="btn btn-sm btn-danger">删除</a>
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
    });
    function export_excel(){
        $("form[name='searchForm']").append('<input type="hidden" name="isExp" value="1">');
        $("#currentPage").val(1);
        $("form[name='searchForm']").submit();
    }
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>