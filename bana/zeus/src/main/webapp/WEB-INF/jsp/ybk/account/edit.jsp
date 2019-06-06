<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>财猫后台管理系统</title>
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_head.jsp"/>

    <%--弹框的--%>
    <link rel="stylesheet" href="/css/avgrund.css" />
    <script type="text/javascript" src="/js/jquery.avgrund.js"></script>

    <style type="text/css">
        .form-horizontal .form-group {height: auto;}
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_top.jsp"/>
<div class="body_container">
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_left.jsp"/>
    <div class="main_content">
        <div class="container">
            <h4 class="heading">用户修改</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form id="save" method="post" action="/ybk/account/save" class="form-horizontal" style="width: 90%">
                <input type="hidden" name="id" id="id" value="${account.id}" />
                <div class="form-group">
                    <label class="col-sm-2 control-label">用户姓名</label>
                    <div class="col-sm-10">
                        ${account.userName}
                    </div>
                </div>
                <div class="form-group" style="display: none;">
                    <label class="col-sm-2 control-label">性别</label>
                    <div class="col-sm-10">
                        <c:if test="${account.sex==1}">男</c:if>
                        <c:if test="${account.sex==2}">女</c:if>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">手机号码</label>
                    <div class="col-sm-10">
                        ${account.phoneNo}
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">申请交易所</label>
                    <div class="col-sm-10">
                        ${exchange.name}
                    </div>
                </div>
                <div class="form-group" style="display: none;">
                    <label class="col-sm-2 control-label">注册卡类型</label>
                    <div class="col-sm-10">
                        ${cardMap[account.cardType.toString()]}
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">注册卡号码</label>
                    <div class="col-sm-10">
                        ${account.cardNumber}
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">注册卡图片</label>
                    <div class="col-sm-10">
                        <img src="${ybkUrl}${account.cardPath}" width="218px" height="120px" class="pic" />
                        <br>
                        <img src="${ybkUrl}${account.cardOppositePath}" width="218px" height="120px" class="pic" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">开户银行</label>
                    <div class="col-sm-10">
                        ${bankTypeInfo.bankName}
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">银行卡号</label>
                    <div class="col-sm-10">
                        ${account.bankNum}
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">银行卡照片</label>
                    <div class="col-sm-10">
                        <img src="${ybkUrl}${account.bankPath}" width="218px" height="120px" class="pic" />
                    </div>
                </div>
                <div class="form-group" style="display: none;">
                    <label class="col-sm-2 control-label">省/直辖市</label>
                    <div class="col-sm-10">
                        ${account.province}
                    </div>
                </div>
                <div class="form-group" style="display: none;">
                    <label class="col-sm-2 control-label">市/区/县</label>
                    <div class="col-sm-10">
                        ${account.city}
                    </div>
                </div>
                <div class="form-group" style="display: none;">
                    <label class="col-sm-2 control-label">街/小区/楼/门/室等</label>
                    <div class="col-sm-10">
                        ${account.street}
                    </div>
                </div>
                <div class="form-group" style="display: none;">
                    <label class="col-sm-2 control-label">紧急联系人</label>
                    <div class="col-sm-10">
                        ${account.contactMan}
                    </div>
                </div>
                <div class="form-group" style="display: none;">
                    <label class="col-sm-2 control-label">手机号码</label>
                    <div class="col-sm-10">
                        ${account.contacterPhoneNo}
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">状态</label>
                    <div class="col-sm-10">
                        <select name="status">
                            <c:forEach var="type" items="${statusMap}">
                                <option value="${type.key}" <c:if test="${type.key == account.status}">selected</c:if>>${type.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">状态原因</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="reason" id="reason" value="${account.reason}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">交易账号</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="exchangeAccount" id="exchangeAccount" value="${account.exchangeAccount}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">创建时间</label>
                    <div class="col-sm-10">
                        <fmt:formatDate value="${account.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-5 col-sm-7">
                        <button type="submit" class="btn btn-blood">更新</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $(".pic").click(function() {
            var picUrl = $(this).attr('src');
            $(this).avgrund({
                width : 900,
                height: 500,
                holderClass: 'custom',
                showClose: true,
                showCloseText: 'Close',
                enableStackAnimation: false,
                openOnEvent : false,
                onBlurContainer: '.container',
                template: '<div style="text-align: center;"><img src="'+ picUrl +'" width="800px" height="450px" /></div>'
            });
        });
    });
</script>

<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>