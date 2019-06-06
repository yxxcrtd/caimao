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
            <h4 class="heading">用户列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form method="get" class="form-inline">
                <div class="form-group">
                    <label>审核状态:</label>
                    <select name="status" class="form-control input-sm">
                        <option value="0">全部</option>
                        <c:forEach var="type" items="${statusMap}">
                            <option value="${type.key}" <c:if test="${type.key == status}">selected</c:if>>${type.value}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>交易所:</label>
                    <select name="exchangeIdApply" class="form-control input-sm">
                        <option value="0">全部</option>
                        <c:if test="${exchangeList != null}">
                            <c:forEach items="${exchangeList}" var="e">
                                <option value="${e.id}" <c:if test="${e.id == exchangeIdApply}">selected</c:if> >${e.name}</option>
                            </c:forEach>
                        </c:if>
                    </select>
                </div>
                <button class="btn btn-sm btn-primary" type="submit">查询</button>
            </form>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>编号</th>
                    <th>用户姓名</th>
                    <th>手机号码</th>
                    <th>注册卡类型</th>
                    <th>注册卡号</th>
                    <th>申请开户交易所</th>
                    <th>审核状态</th>
                    <!-- <th>验证码</th>-->
                    <th>创建时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <c:forEach items="${accountList.getItems()}" var="a">
                    <tr>
                        <td>${a.id}</td>
                        <td>${a.userName}</td>
                        <td>${a.phoneNo}</td>
                        <td>${cardMap[a.cardType.toString()]}</td>
                        <td>${a.cardNumber}</td>
                        <td>${exchangeMap[a.exchangeIdApply].name}</td>
                        <td>
                            ${statusMap[a.status]}
                        </td>
                        <!-- <td><img id="img${a.id}" style="display: none"><input id="yzm${a.id}" size="5" type="text" style="display: none"></td>-->
                        <td><fmt:formatDate value="${a.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td>
                            <!--<input type="button" value="验证码" onclick="getYZM(${a.id})">
                            <input type="button" value="开户" onclick="openAccount(${a.id})">-->
                            <a href="/ybk/account/edit?id=${a.id}" class="btn btn-sm btn-primary">审核</a>　
                            <a href="/ybk/account/delete?id=${a.id}" class="btn btn-sm btn-primary" onclick="return confirm('请慎重考虑，确认要删除？')">删除</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            ${pageHtml}
        </div>
    </div>
    <audio id="bgsound" controls="controls" preload="auto" style="position:absolute; visibility:hidden;"><source src="/img/bgsound.wav" /></audio>
</div>
<script type="text/javascript">
    function getYZM(id){
        document.getElementById('img'+id).style.display="";
        document.getElementById('img'+id).src='/ybk/account/getYZM?i='+Math.random();
        document.getElementById('yzm'+id).style.display="";
    }
    function openAccount(id){
        var  yzm = document.getElementById('yzm'+id).value;
        if(yzm==''){
            show_alert("验证码不能为空")
            return;
        }
        var url ='/ybk/account/openAccount?id='+id+"&yzm="+yzm;
        $.ajax({ url: url,success: function(data){
           if(data=="success"){
               document.getElementById('img'+id).style.display="none";
               document.getElementById('yzm'+id).style.display="none";
               document.getElementById('yzm'+id).value='';
           }
            show_alert(data);
        }});
    }
    function check_new_apply() {
        $.ajax({url: "/ybk/account/check_new_apply",success: function(data){
            if (typeof data != 'undefined') {
                if (data.totalCount > 0) {
                    $('#bgsound').get(0).play();
                }
            }
        }});
    }
    check_new_apply();

    // 每 5 秒测试下
    setInterval("check_new_apply()", 5 * 60 * 1000);

</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>