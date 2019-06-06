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
            <div>
                <form method="post" class="form-inline" action="/ybk/article/saveAppArticle">
                    <p>邮币卡APP首页活动显示的标题与链接地址</p>
                    <label>标题：<input type="text" name="title" value="${_title}" /></label>
                    <label>链接：<input type="text" name="url" value="${_url}" /></label>
                    <label><input type="submit" name="submit" value="保存" /></label>
                </form>
            </div>
            <form method="get" class="form-inline">
                <div class="form-group">
                    <label>分类:</label>
                    <select name="categoryId" class="form-control input-sm">
                        <option value="0">全部</option>
                        <c:forEach items="${categoryList}" var="u">
                            <option value="${u.key}" <c:if test="${u.key == categoryId}">selected</c:if>>${u.value}</option>
                        </c:forEach>
                    </select>
                </div>
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
                    <th>分类编号</th>
                    <th>交易所</th>
                    <th>标题</th>
                    <th>发布时间</th>
                    <th>阅读数</th>
                    <th>是否显示</th>
                    <th>操作</th>
                </tr>
                </thead>
                <c:forEach items="${list.getItems()}" var="u">
                    <tr>
                        <td>${u.id}</td>
                        <td width="100"><input type="text" value="${u.sort}" class="form-control input-sm" onblur="article_sort(this, ${u.id})"></td>
                        <td>${categoryList[u.categoryId.toString()]}</td>
                        <td>${exchangeMap[u.exchangeId].name}</td>
                        <td>${u.title}</td>
                        <td><fmt:formatDate value="${u.created}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td>${u.viewCount}</td>
                        <td>
                            <c:choose>
                                <c:when test="${u.isShow==0}">显示</c:when>
                                <c:when test="${u.isShow==1}">隐藏</c:when>
                            </c:choose>
                        </td>
                        <td><a href="/ybk/article/save?id=${u.id}" class="btn btn-sm btn-primary">编辑</a>　<a href="/ybk/article/del?id=${u.id}" onclick="return confirm('确认要删除此文章吗？');" class="btn btn-sm btn-danger">删除</a></td>
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
        $.post("/ybk/article/sort", {id:id, sort:obj.value});
    }
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>