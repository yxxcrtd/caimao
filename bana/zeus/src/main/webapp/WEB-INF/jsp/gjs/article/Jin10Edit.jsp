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
            <h4 class="heading">实时快讯编辑</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form id="save" method="post" action="/gjs/articleJin10/save" class="form-horizontal" style="width: 90%">
                <input type="hidden" name="id" value="${entity.id}">
                <input type="hidden" name="timeId" value="${entity.timeId}">

                <div class="form-group">
                    <label class="col-sm-2 control-label">分类：</label>
                    <div class="col-sm-10">
                        <input class="category" type="radio" name="category" value="1" <c:if test="${1 == entity.category}">checked="checked"</c:if> /> 一般新闻
                        <input class="category" type="radio" name="category" value="2" <c:if test="${2 == entity.category}">checked="checked"</c:if> /> 重要新闻
                        <input class="category" type="radio" name="category" value="3" <c:if test="${3 == entity.category}">checked="checked"</c:if> /> 一般数据
                        <input class="category" type="radio" name="category" value="4" <c:if test="${4 == entity.category}">checked="checked"</c:if> /> 重要数据
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">时间：</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="time" value="${entity.time}" />
                        ${entity.timeId}
                    </div>
                </div>

                <div class="form-group" style="height: auto;">
                    <label class="col-sm-2 control-label">内容：</label>
                    <div class="col-sm-10">
                        <textarea name="content" id="content" style="min-height: 300px;">${entity.content}</textarea>
                    </div>
                </div>

                <div id="data">
                    <div class="form-group" style="margin-bottom: 15px;">
                        <label class="col-sm-2 control-label">前值：</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="beforeValue" value="${entity.beforeValue}" />
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">预期：</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="expect" value="${entity.expect}" />
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">实际：</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="infact" value="${entity.infact}" />
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">数据星级：</label>
                        <div class="col-sm-10">
                            <input type="radio" name="star" value="1" <c:if test="${1 == entity.star}">checked="checked"</c:if> /> 1星数据
                            <input type="radio" name="star" value="2" <c:if test="${2 == entity.star}">checked="checked"</c:if> /> 2星数据
                            <input type="radio" name="star" value="3" <c:if test="${3 == entity.star}">checked="checked"</c:if> /> 3星数据
                            <input type="radio" name="star" value="4" <c:if test="${4 == entity.star}">checked="checked"</c:if> /> 4星数据
                            <input type="radio" name="star" value="5" <c:if test="${5 == entity.star}">checked="checked"</c:if> /> 5星数据
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">结果：</label>
                        <div class="col-sm-10">
                            <input type="radio" name="result" value="1" <c:if test="${1 == entity.result}">checked="checked"</c:if> /> 利空金银
                            <input type="radio" name="result" value="2" <c:if test="${2 == entity.result}">checked="checked"</c:if> /> 利多金银
                            <input type="radio" name="result" value="3" <c:if test="${3 == entity.result}">checked="checked"</c:if> /> 影响较小
                            <input type="radio" name="result" value="4" <c:if test="${4 == entity.result}">checked="checked"</c:if> /> 利多原油
                            <input type="radio" name="result" value="5" <c:if test="${5 == entity.result}">checked="checked"</c:if> /> 利空原油
                        </div>
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

    UE.getEditor('editor', { 'enterTag' : '' });

    <c:if test="${1 == entity.category || 2 == entity.category}">
        $("#data").fadeOut();
    </c:if>

    $(".category").on("click", function() {
        if ("1" == $(this).val() || "2" == $(this).val()) {
            $("#data").fadeOut();
        } else {
            $("#data").fadeIn();
        }
    });
})
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>