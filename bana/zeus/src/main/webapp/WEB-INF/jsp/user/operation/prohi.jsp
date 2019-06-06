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
            <h4 class="heading">禁止用户某些操作</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form method="get" class="form-inline">
                <div class="form-group">
                    <label>手机号:</label>
                    <input type="text" class="form-control" name="mobile" autocomplete="off" required="required" value="${mobile}" />
                </div>
                <div class="form-group">
                        <button type="submit" class="btn btn-primary btn-sm">查询</button>
                </div>
            </form>


            <form method="post" action="/user/operation/prohi" class="form-horizontal" style="width: 300px;">
                <input type="hidden" name="userId" value="${user.userId}">
                <div class="form-group">
                    <label class="col-sm-3 control-label">姓名:</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" disabled value="${user.userRealName}" />

                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">手机号:</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" disabled value="${user.mobile}" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">禁止类型:</label>
                    <div class="col-sm-9">
                        <select name="type">
                            <option value="1" selected>手动限制</option>
                            <option value="2">警戒线限制</option>
                            <option value="3">HOMS流水错误</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">禁止提现:</label>
                    <div class="col-sm-9">
                        <label><input name="prohi_withdraw" type="radio" value="0" <c:if test="${userExt.prohiWithdraw == 0}">checked</c:if> /> 允许提现</label>
                        <label><input name="prohi_withdraw" type="radio" value="1" <c:if test="${userExt.prohiWithdraw == 1}">checked</c:if> /> 禁止提现</label>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">备注:</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="memo" autocomplete="off" required="required" value="" />
                        <span class="help-block">* 必填</span>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-9">
                        <button type="submit" class="btn btn-blood">保存</button>
                    </div>
                </div>
            </form>

        </div>

        <%--列表--%>
        <div class="container">
            <form method="get" class="form-inline">
                <input type="hidden" class="form-control" name="mobile" autocomplete="off" required="required" value="${mobile}" />
                <div class="form-group">
                    <label>变动类型:</label>
                    <select name="type">
                        <option value="">全部</option>
                        <option <c:if test="${type == '1'}">selected</c:if> value="1">手动限制</option>
                        <option <c:if test="${type == '2'}">selected</c:if> value="2">警戒线限制</option>
                        <option <c:if test="${type == '3'}">selected</c:if> value="3">HOMS流水错误</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>变动值:</label>
                    <select name="status">
                        <option value="">全部</option>
                        <option <c:if test="${status == '1'}">selected</c:if> value="1">限制提现</option>
                        <option <c:if test="${status == '0'}">selected</c:if> value="0">允许提现</option>
                    </select>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-sm">查询</button>
                </div>
            </form>

            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>用户编号</th>
                    <th>姓名</th>
                    <th>手机号</th>
                    <th>操作类型</th>
                    <th>变动值</th>
                    <th>备注</th>
                    <th>变动时间</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list.getItems()}" var="u">
                    <tr>
                        <td><a href="/user/operation/prohi?mobile=${u.mobile}">${u.userId}</a></td>
                        <td>${u.userName}</td>
                        <td>${u.mobile}</td>
                        <td>${u.type}</td>
                        <td>${u.status}</td>
                        <td>${u.memo}</td>
                        <td><fmt:formatDate value="${u.created}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            ${pageHtml}
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>