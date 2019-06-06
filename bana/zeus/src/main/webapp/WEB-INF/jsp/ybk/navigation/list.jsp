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
            <h4 class="heading">邮币卡导航列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>

            <div>
                <p>1. 导航地址格式：导航名称,导航地址,导航名称,导航地址</p>
                <p>例如：百度,https://www.baidu.com,网易,http://www.163.com</p>
            </div>

            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>编号</th>
                    <th>导航名称</th>
                    <th>导航地址s</th>
                    <th>添加时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <c:forEach items="${list}" var="u">
                    <tr>
                        <form action="/ybk/navigation/save" method="post">
                            <td>${u.id}<input type="hidden" name="id" value="${u.id}" /></td>
                            <td><input type="text" name="name" value="${u.name}" /></td>
                            <td><textarea name="urls" style="width: 550px; height: 80px;">${u.urls}</textarea></td>
                            <td><fmt:formatDate value="${u.created}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td>
                                <input type="submit" name="submit" value="修改" />
                                <a href="/ybk/navigation/del?id=${u.id}" onclick="return confirm('确认要删除此导航吗？');" class="btn btn-sm btn-danger">删除</a>
                            </td>
                        </form>
                    </tr>
                </c:forEach>
                <tr>
                    <form action="/ybk/navigation/save" method="post">
                        <td>--</td>
                        <td><input type="text" name="name" value="" /></td>
                        <td><textarea name="urls" style="width: 550px; height: 80px;"></textarea></td>
                        <td>--</td>
                        <td>
                            <input type="submit" name="submit" value="添加" />
                        </td>
                    </form>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>