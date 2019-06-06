<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>财猫后台管理系统</title>
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_head.jsp"/>
    <!-- 配置文件 -->
    <script type="text/javascript" src="/js/ueditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="/js/ueditor/ueditor.all.js"></script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_top.jsp"/>
<div class="body_container">
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_left.jsp"/>
    <div class="main_content">
        <div class="container">
            <h4 class="heading">文章编辑&添加</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form id="save" method="post" action="/gjs/article/save" class="form-horizontal" style="width: 90%">
                <input type="hidden" name="id" value="${articleDetail.id}">
                <div class="form-group">
                    <label class="col-sm-2 control-label">分类</label>
                    <div class="col-sm-10">
                        <select name="categoryId" class="form-control input-sm">
                            <option value="1" <c:if test="${1 == articleDetail.categoryId}">selected</c:if> >公告</option>
                            <option value="2" <c:if test="${2 == articleDetail.categoryId}">selected</c:if> >文章</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">来源</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="sourceName" id="sourceName" value="${articleDetail.sourceName}" placeholder="来源名称" />
                        <input type="text" class="form-control" name="sourceUrl" id="sourceUrl" value="${articleDetail.sourceUrl}" placeholder="来源地址URL" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">标题</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="title" id="title" value="${articleDetail.title}" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">内容</label>
                    <div class="col-sm-10">
                        <textarea name="content" id="content" style="min-height: 300px;">${articleDetail.content}</textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">排序</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="sort" value="${articleDetail.sort}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">阅读数量</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="viewCount" value="${articleDetail.viewCount}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">热门？</label>
                    <div class="col-sm-10">
                        <label class="radio-inline">
                            <input type="radio" name="isHot" value="0" <c:if test="${empty articleDetail.isHot || articleDetail.isHot == 0}">checked</c:if>> 正常
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="isHot" value="1" <c:if test="${articleDetail.isHot == 1}">checked</c:if>> 热门
                        </label>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">审核</label>
                    <div class="col-sm-10">
                        <label class="radio-inline">
                            <input type="radio" name="isShow" value="0" <c:if test="${empty articleDetail.isShow || articleDetail.isShow == 0}">checked</c:if>>通过
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="isShow" value="1" <c:if test="${articleDetail.isShow == 1}">checked</c:if>>不通过
                        </label>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-5 col-sm-7">
                        <button type="submit" class="btn btn-blood">保 存</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
$(function(){
    UE.getEditor('content', { textarea:'content'});
})
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>