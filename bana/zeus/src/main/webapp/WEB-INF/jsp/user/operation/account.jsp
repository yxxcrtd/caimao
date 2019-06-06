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
            <h4 class="heading">用户资产</h4>
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


            <form method="post" class="form-horizontal" style="width: 300px;" action="/user/operation/user_account">
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
                    <label class="col-sm-3 control-label">可用资产:</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" disabled value="${(account.avalaibleAmount - account.freezeAmount) / 100}" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">冻结资产:</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" disabled value="${account.freezeAmount / 100}" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">变动类型:</label>
                    <div class="col-sm-9">
                        <select name="bizType" class="form-control" >
                            <option value="00">其他</option>
                            <option value="26">保证金冻结</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">增减冻结:</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="frozen" autocomplete="off" required="required" />
                        <span class="help-block"> * 正数 为冻结； 负数 为解冻</span>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-9">
                        <button type="submit" class="btn btn-blood">修改</button>
                    </div>
                </div>
            </form>
        </div>

        <%--列表--%>
        <div class="container">
            <h4 class="heading">用户资产冻结变动历史</h4>
            <form class="form-inline" role="form" name="searchForm" action="/user/operation/user_account" method="get">
                <input type="hidden" name="mobile" value="${mobile}" />
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>变动类型：</label>
                    <select name="bizType">
                        <option value="all" <c:if test="${bizType == 'all'}">selected</c:if>>所有</option>
                        <option value="00" <c:if test="${bizType == '00'}">selected</c:if>>其他</option>
                        <option value="26" <c:if test="${bizType == '26'}">selected</c:if>>保证金冻结</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>投资时间：</label>
                    <input type="date" class="form-control input-sm" name="startDate" id="beginDateTime" value="${startDate}"> -
                    <input type="date" class="form-control input-sm" name="endDate" id="endDateTime" value="${endDate}">
                </div>
                <button type="button" class="btn btn-primary btn-sm" onclick="redirectTo(1);">查询</button>
            </form>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>流水号</th>
                    <th>姓名</th>
                    <th>业务类型</th>
                    <th>变动金额</th>
                    <th>变动后金额</th>
                    <th>变动时间</th>
                    <th>关联号</th>
                </tr>
                </thead>
                <c:forEach items="${req.getItems()}" var="u">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.userRealName}</td>
                        <td>${u.accountBizType}</td>
                        <td><fmt:formatNumber value="${u.transAmount/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.postAmount/100}" type="number"/></td>
                        <td><fmt:formatDate value="${u.transDatetime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td>${u.refSerialNo}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            ${pageHtml}
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>