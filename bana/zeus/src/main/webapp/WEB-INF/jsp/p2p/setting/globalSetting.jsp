<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            <h4 class="heading">p2p全局设置</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form method="post" class="form-horizontal" style="width: 500px">
            	<c:forEach items="${list}" var="obj">
                <div class="form-group">
                    <label class="col-sm-3 control-label">${obj.paramName}</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="${obj.paramCode}" value="${obj.paramValue}" autocomplete="off" required="required" readonly="readonly">
                    </div>
                </div>
                </c:forEach>
                <!-- 
                <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-9">
                        <button type="submit" class="btn btn-blood">保存</button>
                    </div>
                </div>
                 -->
            </form>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>