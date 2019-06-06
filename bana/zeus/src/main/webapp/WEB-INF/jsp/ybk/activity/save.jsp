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
            <h4 class="heading">活动编辑&添加</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form id="save" method="post" action="/ybk/activity/save" class="form-horizontal" style="width: 90%" enctype="multipart/form-data">
                <input type="hidden" name="id" value="${activityDetail.id}">
                <div class="form-group">
                    <label class="col-sm-2 control-label">交易所</label>
                    <div class="col-sm-10">
                        <select name="exchangeId" class="form-control input-sm">
                            <option value="0" <c:if test="${activityDetail.exchangeId == 0}">selected</c:if> >无</option>
                            <c:forEach items="${exchangeList}" var="e">
                                <option value="${e.id}" <c:if test="${activityDetail.exchangeId == e.id}">selected</c:if> >${e.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">活动连接</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="activityUrl" id="title" value="${activityDetail.activityUrl}" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">活动名称</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="activityName" id="activityName" value="${activityDetail.activityName}" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">活动Banner</label>
                    <div class="col-sm-10">
                        <input type="file" class="form-control" name="activityBanner" id="activityBanner" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">截止日期</label>
                    <div class="col-sm-10">
                        <input type="date" class="form-control" name="endDatetime" id="endDatetime" value="<fmt:formatDate value="${activityDetail.endDatetime}" pattern="yyyy-MM-dd"/>" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">要求</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="ask" id="ask" value="${activityDetail.ask}" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">奖励</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="reward" id="reward" value="${activityDetail.reward}" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">奖励价值</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="rewardValue" id="rewardValue" value="${activityDetail.rewardValue}" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">排序</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="sort" value="${activityDetail.sort}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">状态</label>
                    <div class="col-sm-10">
                        <label class="radio-inline">
                            <input type="radio" name="isShow" value="0" <c:if test="${empty activityDetail.isShow || activityDetail.isShow == 0}">checked</c:if>> 显示
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="isShow" value="1" <c:if test="${activityDetail.isShow == 1}">checked</c:if>> 隐藏
                        </label>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-5 col-sm-7">
                        <button type="submit" class="btn btn-blood">保存</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>