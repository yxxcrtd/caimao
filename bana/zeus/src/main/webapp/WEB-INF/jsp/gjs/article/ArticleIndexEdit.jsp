<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
            <h4 class="heading"><c:if test="${0 == entity.id}">新增</c:if><c:if test="${0 < entity.id}">修改</c:if>首页文章</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form id="save" method="post" action="/gjs/article/index/save" class="form-horizontal" style="width: 90%">

                <div class="form-group">
                    <label class="col-sm-2 control-label"><span style="color: #ff0000;">发布人：*</span></label>
                    <div class="col-sm-10">
                        <c:forEach items="${userList}" var="u">
                            <input class="category" type="radio" name="user" value="${u}" <c:if test="${entity.user == u}">checked="checked"</c:if> />
                            <img src="${domain_url}/upload/article/${fn:split(u, ',')[2]}" width="25" /> ${fn:split(u, ',')[1]}
                        </c:forEach>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label"><span style="color: #ff0000;">文章标签：*</span></label>
                    <div class="col-sm-10">
                        <c:forEach items="${categoryList}" var="c">
                            <input class="category" type="radio" name="category" value="${c.id}" <c:if test="${entity.category == c.id}">checked="checked"</c:if> /> ${c.name}
                        </c:forEach>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label"><span style="color: #ff0000;">标题：*</span></label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="title" name="title" value="${entity.title}" />
                    </div>
                </div>

                <c:if test="${0 < entity.id}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">文章详情地址：</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" value="${share_url}" />
                        </div>
                    </div>
                </c:if>

                <div class="form-group" style="height: auto;">
                    <label class="col-sm-2 control-label">内容：</label>
                    <div class="col-sm-10">
                        <textarea name="content" id="content" style="min-height: 300px; width: 930px;">${entity.content}</textarea>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">阅读数量：</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="view" value="${entity.view}" onkeyup="this.value = this.value.replace(/\s/g, '')" />
                    </div>
                </div>

                <div class="form-group" style="margin-top: 30px;">
                    <div class="col-sm-offset-5 col-sm-7">
                        <c:if test="${0 == entity.id || -1 == entity.status}"><button type="submit" class="btn btn-blood" id="sketch" style="margin-right: 20px;">存草稿</button></c:if>
                        <button type="submit" class="btn btn-blood" id="sub" style="margin-right: 20px;"><c:if test="${0 == entity.id}">插入</c:if><c:if test="${0 < entity.id}">修改</c:if></button>
                        <button type="button" class="btn btn-blood" onclick="javascript:history.go(-1);">取消</button>
                    </div>
                </div>

                <input type="hidden" name="id" value="${entity.id}" />
                <input type="hidden" name="isShow" id="isShow" value="${entity.isShow}" />
                <input type="hidden" name="status" id="status" value="${entity.status}" />
                <input type="hidden" name="view" value="${entity.view}" />
                <input type="hidden" name="pub" value="${entity.pub}" />
                <input type="hidden" name="created" value="${entity.created}" />
            </form>
        </div>
    </div>
</div>
<script type="text/javascript" language="javascript" src="/js/kindeditor/kindeditor.js"></script>
<script>
    $(function() {
//        UE.getEditor('content', { textarea:'content'});
//        UE.getEditor('editor', { 'enterTag' : '' });

        KindEditor.ready(function(K) {
            var editor = K.create("#content");
        });

        $("#sketch").on("click", function() {
            $("#status").val("-1");
            //$("#isShow").val("1");
            var user = $('input:radio[name="user"]:checked').val();
            if (null == user) {
                alert("请选择一个发布人！");
                return false;
            }
            var val = $('input:radio[name="category"]:checked').val();
            if (null == val) {
                alert("请选择一个文章标签！");
                return false;
            }
            if ("" == $("#title").val()) {
                alert("请输入文章标题！");
                return false;
            }
        });

        $("#sub").on("click", function() {
            <c:if test="${-1 == entity.status}">$("#status").val("0");</c:if>
            //$("#isShow").val("0");
            var user = $('input:radio[name="user"]:checked').val();
            if (null == user) {
                alert("请选择一个发布人！");
                return false;
            }
            var val = $('input:radio[name="category"]:checked').val();
            if (null == val) {
                alert("请选择一个文章标签！");
                return false;
            }
            if ("" == $("#title").val()) {
                alert("请输入文章标题！");
                return false;
            }
        });
    })
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>