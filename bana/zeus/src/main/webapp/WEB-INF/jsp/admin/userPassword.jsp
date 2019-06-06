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
            <h4 class="heading">修改密码</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form method="post" class="form-horizontal" style="width: 500px">
                <div class="form-group">
                    <label class="col-sm-3 control-label">旧密码</label>
                    <div class="col-sm-9">
                        <input type="password" class="form-control" name="passwordOld" autocomplete="off">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">新密码</label>
                    <div class="col-sm-9">
                        <input type="password" class="form-control" name="passwordNew" autocomplete="off">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">确认密码</label>
                    <div class="col-sm-9">
                        <input type="password" class="form-control" name="passwordNewConfirm" autocomplete="off">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-9">
                        <button type="submit" class="btn btn-blood">保存</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>