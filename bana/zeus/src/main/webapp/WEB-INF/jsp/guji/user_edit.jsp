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
            <h4 class="heading">编辑</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form id="save" method="post" action="/guji/auth_user" class="form-horizontal" style="width: 90%">
                <input type="hidden" name="wxId" value="${entity.wxId}">
                <div id="data">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">状态：</label>
                        <div class="col-sm-10">
                            <c:forEach items="${authStatus}" var="entry">
                                <c:choose>
                                    <c:when test="${entity.authStatus == entry.key}">
                                        <input class="authStatus" type="radio" name="authStatus" value="${entry.key}" checked="checked"/> <c:out value="${entry.value}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <input class="authStatus" type="radio" name="authStatus" value="${entry.key}"/> <c:out value="${entry.value}"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="form-group" style="margin: 20px;">
                        <label class="col-sm-2 control-label">认证机构：</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" style="width: 300px;" name="certificationAuth" id="certificationAuth" maxlength="30" value="${entity.certificationAuth}" onkeyup="this.value = this.value.replace(/\s/g, '')" />
                        </div>

                    </div>
                </div>

                <div class="form-group" style="margin-top: 30px;">
                    <div class="col-sm-offset-5 col-sm-7">
                        <button type="submit" class="btn btn-blood" style="margin-right: 20px;">提交</button>
                        <button type="button" class="btn btn-blood" onclick="javascript:history.go(-1);">取消</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>