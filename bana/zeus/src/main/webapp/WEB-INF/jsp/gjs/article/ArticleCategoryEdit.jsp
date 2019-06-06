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
            <h4 class="heading"><c:if test="${0 == entity.id}">新增</c:if><c:if test="${0 < entity.id}">修改</c:if>标签</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form id="save" method="post" action="/gjs/article/category/save" class="form-horizontal" style="width: 90%">
                <input type="hidden" name="id" value="${entity.id}">

                <div id="data">
                    <div class="form-group" style="margin: 20px;">
                        <label class="col-sm-2 control-label">名称（请输入2-6个汉字）：</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" style="width: 300px;" name="name" maxlength="6" value="${entity.name}" onkeyup="this.value = this.value.replace(/\s/g, '')" />
                        </div>
                    </div>
                </div>

                <div class="form-group" style="margin-top: 30px;">
                    <div class="col-sm-offset-5 col-sm-7">
                        <button type="submit" class="btn btn-blood" style="margin-right: 20px;">插入</button>
                        <button type="button" class="btn btn-blood" onclick="javascript:history.go(-1);">取消</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>