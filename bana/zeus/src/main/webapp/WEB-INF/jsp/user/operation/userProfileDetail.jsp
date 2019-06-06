<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>财猫后台管理系统</title>
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_head.jsp"/>
    <link rel="stylesheet" href="/css/avgrund.css" />
    <script type="text/javascript" src="/js/jquery.avgrund.js"></script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_top.jsp"/>
<div class="body_container">
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_left.jsp"/>
    <div class="main_content">
        <div class="container">
            <h4 class="heading">用户详情</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                    <tr>
                        <th colspan="4">用户详情</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td width="25%">用户姓名：${user.userRealName}</td>
                        <td width="25%">手机号：${user.mobile}</td>
                        <td width="25%">身份证：${user.idcard}</td>
                        <td width="25%">身份证照片：<a href="javascript:;" class="showPic" data-img-src="${ybkUrl}${userExt.idcardPositivePath}">点击查看</a></td>
                    </tr>
                    <tr>
                        <td>注册时间：<fmt:formatDate value="${user.registerDatetime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td>注册IP：${user.registerIp}</td>
                        <td>最后登录时间：<fmt:formatDate value="${user.lastLoginDatetime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td>最后登录IP：${user.lastLoginIp}</td>
                    </tr>
                </tbody>
            </table>
            <br />
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th colspan="4">个人信息</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td width="25%">昵称：${user.userNickName}</td>
                    <td width="25%">性别：
                        <c:choose>
                            <c:when test="${userExt.gender == 1}">女</c:when>
                            <c:when test="${userExt.gender == 2}">男</c:when>
                        </c:choose>
                    </td>
                    <td width="25%">QQ：${userExt.userQq}</td>
                    <td width="25%">头像：
                        <c:if test="${not empty userExt.userPhoto}">
                            <a href="javascript:;" class="showPic" data-img-src="${ybkUrl}${userExt.userPhoto}">点击查看</a>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td colspan="4">地址：${user.userAddress}</td>
                </tr>
                </tbody>
            </table>
            <br />
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                    <tr>
                        <th colspan="7">开户信息</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td width="8%" style="text-align: center;">交易所</td>
                        <td width="15%">开户时间</td>
                        <td width="8%">签约状态</td>
                        <td width="10%">交易号</td>
                        <td width="10%">银商绑定</td>
                        <td width="19%">绑定卡号</td>
                        <td width="30%">交易信息</td>
                    </tr>
                    <tr>
                        <td style="text-align: center;">邮币卡</td>
                        <td><fmt:formatDate value="${ybkAccount.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${1 == ybkAccount.status}">待审核</c:when>
                                <c:when test="${2 == ybkAccount.status}">审核中</c:when>
                                <c:when test="${3 == ybkAccount.status}">成功</c:when>
                                <c:when test="${4 == ybkAccount.status}">失败</c:when>
                            </c:choose>
                        </td>
                        <td>${ybkAccount.exchangeAccount}</td>
                        <td><c:if test="${empty bankTypeInfo.bankName}"><span style="color: #FF0000;">未开户</span></c:if><c:if test="${not empty bankTypeInfo.bankName}">${bankTypeInfo.bankName}</c:if></td>
                        <td>${ybkAccount.bankNum}</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td style="text-align: center;">南交所</td>
                        <td><fmt:formatDate value="${njs.createDatetime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${0 == njs.isSign}">未签约</c:when>
                                <c:when test="${1 == njs.isSign}">签约</c:when>
                            </c:choose>
                        </td>
                        <td>${njs.traderId}</td>
                        <td><c:if test="${empty njsBankName}"><span style="color: #FF0000;">未开户</span></c:if><c:if test="${not empty njsBankName}">${njsBankName}</c:if></td>
                        <td>${njs.bankCard}</td>
                        <td>
                            <c:if test="${not empty njsBankName}">
                                <a href="javascript:;">当前持仓</a>
                                <a href="javascript:;">当前委托</a>
                                <a href="historyEntrust?exchange=njs&traderId=${njs.traderId}">历史委托</a>
                                <a href="historyTrade?exchange=njs&traderId=${njs.traderId}">历史成交</a>
                                <a href="historyTransfer?exchange=njs&traderId=${njs.traderId}">历史出入金</a>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align: center;">上交所</td>
                        <td><fmt:formatDate value="${sjs.createDatetime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${0 == sjs.isSign}">未签约</c:when>
                                <c:when test="${1 == sjs.isSign}">签约</c:when>
                            </c:choose>
                        </td>
                        <td>${sjs.traderId}</td>
                        <td><c:if test="${empty sjsBankName}"><span style="color: #FF0000;">未开户</span></c:if><c:if test="${not empty sjsBankName}">${sjsBankName}</c:if></td>
                        <td>${sjs.bankCard}</td>
                        <td>
                            <c:if test="${not empty sjsBankName}">
                                <a href="javascript:;">当前持仓</a>
                                <a href="javascript:;">当前委托</a>
                                <a href="historyEntrust?exchange=sjs&traderId=${sjs.traderId}">历史委托</a>
                                <a href="historyTrade?exchange=sjs&traderId=${sjs.traderId}">历史成交</a>
                                <a href="historyTransfer?exchange=sjs&traderId=${sjs.traderId}">历史出入金</a>
                            </c:if>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        $(".showPic").click(function() {
            var picUrl = $(this).attr('data-img-src');
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