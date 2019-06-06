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
            <h4 class="heading">警报通知人列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>

            <form method="post" class="form-inline" action="/content/alarm_people_save">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>唯一标识：</label>
                    <input type="text" class="form-control input-sm" name="key" style="width: 80px;" autocomplete="off" />
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>名称：</label>
                    <input type="text" class="form-control input-sm" name="name" style="width: 100px;" autocomplete="off" />
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>邮箱s：</label>
                    <input type="text" class="form-control input-sm" name="emails" style="width: 180px;" autocomplete="off" />
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>短信s：</label>
                    <input type="text" class="form-control input-sm" name="smss" style="width: 180px;" autocomplete="off" />
                </div>
                <button type="submit" class="btn btn-primary btn-sm">添加</button>
                * 多个邮件、短信 使用 , 号分割
            </form>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>唯一标识</th>
                    <th>名称</th>
                    <th>邮箱s</th>
                    <th>短信s</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list}" var="u">
                    <form action="/content/alarm_people_update" method="post">
                        <tr>
                            <td>${u.key}<input type="hidden" name="key" value="${u.key}" /></td>
                            <td><input type="text" name="name" value="${u.name} "/></td>
                            <td><input type="text" name="emails" value="${u.emails}" /></td>
                            <td><input type="text" name="smss" value="${u.smss}" /></td>
                            <td>
                                <input type="submit" value="更新" />
                                <a href="/content/alarm_people_del?key=${u.key}">删除</a>
                            </td>
                        </tr>
                    </form>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>