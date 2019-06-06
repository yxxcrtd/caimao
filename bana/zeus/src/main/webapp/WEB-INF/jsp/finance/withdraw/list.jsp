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
            <h4 class="heading">提现管理</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form class="form-inline" role="form">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>用户姓名：</label>
                    <input type="text" class="form-control input-sm" name="userRealName" placeholder="支持模糊查询" style="width: 120px;">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>提现时间：</label>
                    <input type="date" class="form-control input-sm" name="createDatetimeBegin" id="createDatetimeBegin" value="${f831411req.createDatetimeBegin}"> -
                    <input type="date" class="form-control input-sm" name="createDatetimeEnd" id="createDatetimeEnd" value="${f831411req.createDatetimeEnd}">
                </div>
                <br>
                <div class="form-group">
                    <label>订单状态：</label>
                    <select class="form-control input-sm" name="orderStatus" style="width: 120px;">
                        <option value="">全部</option>
                        <option value="02" <c:if test="${f831411req.orderStatus.equals('02')}">selected</c:if>>待处理</option>
                        <option value="03" <c:if test="${f831411req.orderStatus.equals('03')}">selected</c:if>>成功</option>
                        <option value="04" <c:if test="${f831411req.orderStatus.equals('04')}">selected</c:if>>失败</option>
                        <option value="05" <c:if test="${f831411req.orderStatus.equals('05')}">selected</c:if>>已取消</option>
                        <option value="06" <c:if test="${f831411req.orderStatus.equals('06')}">selected</c:if>>待确认</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>审核状态：</label>
                    <select class="form-control input-sm" name="verifyStatus" style="width: 120px;">
                        <option value="" <c:if test="${empty f831411req.verifyStatus || f831411req.verifyStatus == null}">selected</c:if>>全部</option>
                        <option value="0" <c:if test="${!empty f831411req.verifyStatus && f831411req.verifyStatus == 0}">selected</c:if>>待审核</option>
                        <option value="1" <c:if test="${!empty f831411req.verifyStatus && f831411req.verifyStatus == 1}">selected</c:if>>审核通过</option>
                        <option value="2" <c:if test="${!empty f831411req.verifyStatus && f831411req.verifyStatus == 2}">selected</c:if>>调整后审核通过</option>
                        <option value="3" <c:if test="${!empty f831411req.verifyStatus && f831411req.verifyStatus == 3}">selected</c:if>>审核不通过</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary btn-sm">查询</button>
            </form>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>流水号</th>
                    <th>提现金额</th>
                    <th>订单状态</th>
                    <th>审核状态</th>
                    <th>申请时间</th>
                    <th>审核人</th>
                    <th>审核时间</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${withdrawList}" var="withdraw" varStatus="s">
                    <tr>
                        <td>充值总计：<span style="color: red">${withdraw.totalDeposit / 100}</span></td>
                        <td>提现总计：<span style="color: red">${withdraw.totalWithdraw / 100}</span></td>
                        <td>可能提现最大值：<span style="color: red">${(withdraw.totalDeposit - withdraw.totalWithdraw) * 2 / 100}</span></td>
                        <td<c:if test="${withdraw.totalBad > 0}"> class="danger" </c:if>>错误的流水：${withdraw.totalBad}</td>
                    </tr>
                    <tr class="info">
                        <td>${withdraw.orderNo}</td>
                        <td>${withdraw.currencyType} <span style="color: red">${withdraw.orderAmount/100}</span></td>
                        <td>
                            <c:choose>
                                <c:when test="${withdraw.orderStatus == '02'}">待处理</c:when>
                                <c:when test="${withdraw.orderStatus == '03'}">成功</c:when>
                                <c:when test="${withdraw.orderStatus == '04'}">失败</c:when>
                                <c:when test="${withdraw.orderStatus == '05'}">已取消</c:when>
                                <c:when test="${withdraw.orderStatus == '06'}">待确认</c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${withdraw.verifyStatus==0}">待审核</c:when>
                                <c:when test="${withdraw.verifyStatus==1}">审核通过</c:when>
                                <c:when test="${withdraw.verifyStatus==2}">调整后审核通过</c:when>
                                <c:when test="${withdraw.verifyStatus==3}">审核不通过</c:when>
                            </c:choose>
                        </td>
                        <td>${withdraw.createDateTime}</td>
                        <td>${withdraw.verifyUser}</td>
                        <td>${withdraw.verifyDatetime}</td>
                    </tr>
                    <tr class="warning">
                        <td>${withdraw.userRealName} ( ${withdraw.userId} )</td>
                        <td colspan="3">
                            开户名：${withdraw.bankCardName}　卡号：${withdraw.bankCardNo}　开户行：${withdraw.bankName}<br>
                            支行：${withdraw.province} ${withdraw.city} ${withdraw.openBank}</td>
                        <td>
                            提： ${withdraw.paySubmitDatetime}<br>
                            回： ${withdraw.payResultDatetime}
                        </td>
                        <td colspan="2">
                            <button type="button" class="btn btn-success btn-sm" onclick="javascript:payOnline('${withdraw.province}','${withdraw.city}','${withdraw.openBank}','${withdraw.userRealName}','${withdraw.orderNo}')">提现</button>
                            <button type="button" class="btn btn-success btn-sm" onclick="javascript:checkOrderStatus('${withdraw.orderNo}');">刷新提现状态</button>
                        </td>
                    </tr>
                    <tr style="height: 10px;"></tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript">
    function payOnline(province, city, openBank, userName, orderNo) {
        if (province == null || province == "" || city == null || city == "" || openBank == null || openBank == "") {
            alert("用户提现的开户行信息错误, 请检查!");
            return false;
        }
        if (confirm("是否确认为用户'" + userName + "'提现?")) {
            $.ajax({
                type: 'POST',
                url: '/finance/withdraw/payOnline',
                data: {orderNo: orderNo},
                success: function (payresult) {
                    if (payresult['status'] == '1') {
                        show_alert("提现成功, 请稍后尝试从'待确认'页面刷新该提现记录");
                    } else {
                        show_alert("提现失败, 原因: '" + payresult['message'] + "'");
                    }
                    $('#myModal').on('hidden.bs.modal', function (e) {
                        window.location.reload();
                    })
                }
            });
        }
    }

    function checkOrderStatus(orderNo) {
        $.ajax({
            type: 'POST',
            url: '/finance/withdraw/checkStatus',
            data: {orderNo: orderNo},
            success: function (checkResult) {
                show_alert("刷新成功, 返回: '" + checkResult['message'] + "'");
                $('#myModal').on('hidden.bs.modal', function (e) {
                    window.location.reload();
                })
            }
        });
    }

</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>