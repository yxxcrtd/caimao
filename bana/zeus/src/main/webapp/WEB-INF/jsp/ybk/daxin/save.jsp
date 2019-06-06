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
            <h4 class="heading">打新编辑&添加</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form id="save" method="post" action="/ybk/daxin/save" class="form-horizontal" style="width: 90%">
                <input type="hidden" name="id" value="${articleDetail.id}">
                <div class="form-group">
                    <label class="col-sm-2 control-label">交易所</label>
                    <div class="col-sm-10">
                        <select name="exchangeId" class="form-control input-sm">
                            <c:forEach items="${exchangeList}" var="e">
                                <option value="${e.id}" <c:if test="${articleDetail.exchangeId == e.id}">selected</c:if> >${e.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">打新名称</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="daxinName" id="title" value="${articleDetail.daxinName}" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">打新链接</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="daxinUrl" id="daxinUrl" value="${articleDetail.daxinUrl}" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">截止时间</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="endDatetime" id="endDatetime" value="${articleDetail.endDatetime}" />
                        <span class="help-block">* 格式：2015-11-18 18:30:00</span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">排序</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="sort" id="sort" value="${articleDetail.sort}" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">状态</label>
                    <div class="col-sm-10">
                        <label class="radio-inline">
                            <input type="radio" name="isShow" value="0" <c:if test="${empty articleDetail.isShow || articleDetail.isShow == 0}">checked</c:if>> 显示
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="isShow" value="1" <c:if test="${articleDetail.isShow == 1}">checked</c:if>> 隐藏
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