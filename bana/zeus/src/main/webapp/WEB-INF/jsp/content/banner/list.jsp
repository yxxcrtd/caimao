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
            <h4 class="heading">Banner列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form method="get" class="form-inline">
                <div class="form-group">
                    <label>类型简称:</label>
                    <select name="appType" class="form-control input-sm">
                        <option value="">全部</option>
                        <option value="home" <c:if test="${appType == 'home'}">selected</c:if> >home(主站)</option>
                        <option value="gjs" <c:if test="${appType == 'gjs'}">selected</c:if> >gjs(贵金属)</option>
                        <option value="ybk" <c:if test="${appType == 'ybk'}">selected</c:if>>ybk(邮币卡)</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>显示与否:</label>
                    <select name="isShow" class="form-control input-sm">
                        <option value="">全部</option>
                        <option value="0" <c:if test="${isShow == 0}">selected</c:if>>显示</option>
                        <option value="1" <c:if test="${isShow == 1}">selected</c:if>>隐藏</option>
                    </select>
                </div>
                <button class="btn btn-sm btn-primary" type="submit">查询</button>
            </form>
            <div><p>贵金属简称：gjs  邮币卡简称：ybk  主站简称：home</p></div>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>类型简称</th>
                    <th>名称（点击修改）</th>
                    <th>排序</th>
                    <th>是否显示</th>
                    <th>电脑版图片</th>
                    <th>电脑版跳转</th>
                    <th>APP版图片</th>
                    <th>APP版跳转</th>
                    <th>添加时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${req.getItems()}" var="u">
                    <tr>
                        <td>${u.appType}</td>
                        <td><a href="/content/banner/edit?id=${u.id}">${u.name}</a></td>
                        <td>${u.sort}</td>
                        <td>
                            <c:choose>
                                <c:when test="${u.isShow==0}">显示</c:when>
                                <c:when test="${u.isShow==1}">隐藏</c:when>
                            </c:choose>
                        </td>
                        <td><a href="javascript:void(0);" class="showPic" data-img-src="${ybkUrl}${u.pcPic}">PC图片点击查看</a></td>
                        <td><a href="${u.pcJumpUrl}" target="_blank">${u.pcJumpUrl}</a> </td>
                        <td><a href="javascript:void(0);" class="showPic" data-img-src="${ybkUrl}${u.appPic}">APP图片点击查看</a></td>
                        <td><a href="${u.appJumpUrl}" target="_blank">${u.appJumpUrl}</a> </td>
                        <td><fmt:formatDate value="${u.createTime}" type="both" dateStyle="default" /></td>
                        <td>
                            <a href="/content/banner/edit?id=${u.id}">修改</a> &nbsp; || &nbsp;
                            <a href="/content/banner/del?id=${u.id}" onclick="return confirm('确定要删除吗？你可要想好了啊')">删除</a>
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