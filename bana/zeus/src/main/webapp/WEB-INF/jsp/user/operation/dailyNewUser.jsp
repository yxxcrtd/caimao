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
            <h4 class="heading">每日新增数据</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <div style="color: red;">
                查询请使用 “用户编号”，推广加 i 请使用 “用户推广ID”，自定义编号查询支持多个查询，多个值使用 , 号分隔 <br />
                可在 www.caimao.com  www.fmall.com  ybk.fmall.com 域名下的任何页面后添加  i 参数，都可以统计到
            </div>
            <form class="form-inline" role="form" name="searchForm" action="/user/operation/dailyNewUser" method="get">
                <div class="form-group">
                    <label>查询日期：</label>
                    <input type="date" class="form-control input-sm" name="dateStart" value="${dateStart}" id="dateStart" style="width: 140px;">
                    -
                    <input type="date" class="form-control input-sm" name="dateEnd" value="${dateEnd}" id="dateEnd" style="width: 140px;">
                </div>
                <div class="form-group">
                    <label>查询用户编号：</label>
                    <select name="selectId" class="form-control input-sm" onchange="changeSelectId(this.value)">
                        <c:forEach var="i" begin="1" end="10" step="1">
                            <option value="${i}" <c:if test="${selectId == i}">selected</c:if>>${(i - 1) * 50 + 1}-${i * 50}</option>
                        </c:forEach>
                        <option value="999" <c:if test="${selectId == 999}">selected</c:if>>自定义</option>
                    </select>
                </div>
                <div class="form-group" id="customIdInput" <c:if test="${selectId != 3}">style="display: none"</c:if>>
                    <label>自定义编号：</label>
                    <input type="text" class="form-control input-sm" name="customId" value="${customId}" style="width: 200px;">
                </div>
                <button type="submit" class="btn btn-primary btn-sm">查询</button>
            </form>
            <table class="table table-hover table-bordered">
                <thead>
                <tr>
                    <th>用户编号</th>
                    <th>用户名</th>
                    <th>用户推广ID</th>
                    <th>当日注册数</th>
                    <th>当日总注册数</th>
                    <th>总注册数</th>
                    <th>当日邮币卡开户数</th>
                    <th>总邮币卡开户数</th>
                    <th>当日实名数</th>
                    <th>当日总实名数</th>
                    <th>总实名数</th>
                    <th>当日配资人数</th>
                    <th>当日总配资人数</th>
                    <th>总配资人数</th>
                    <th>当日配资金额</th>
                    <th>当日总配资金额</th>
                    <th>总配资金额</th>
                    <th>当日配资利息</th>
                    <th>当日总配资利息</th>
                    <th>总配资利息</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${dailyNewUserData}" var="u">
                    <tr>
                        <td>${u.userId}</td>
                        <td><input type="text" class="userNickName" data-userId="${u.userId}" value="${u.userName}" /></td>
                        <td>${u.caimaoId}</td>
                        <td>${u.regNew}</td>
                        <td>${u.regTmp}</td>
                        <td>${u.regAll}</td>
                        <td>${u.ybkRegNew}</td>
                        <td>${u.ybkRegTmp}</td>
                        <td style="cursor: pointer;" onclick="showRealNameTmp(${u.userId})">${u.realNameNew}</td>
                        <td>${u.realNameTmp}</td>
                        <td>${u.realNameAll}</td>
                        <td>${u.loanUserNew}</td>
                        <td>${u.loanUserTmp}</td>
                        <td>${u.loanUserAll}</td>
                        <td><fmt:formatNumber value="${u.loanAmountNew/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.loanAmountTmp/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.loanAmountAll/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.loanInterestNew/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.loanInterestTmp/100}" type="number"/></td>
                        <td><fmt:formatNumber value="${u.loanInterestAll/100}" type="number"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div id="page" currentPage="${list.currentPage}" pageCount="${list.pages}" ></div>
        </div>
    </div>
</div>
<div class="modal fade" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel2" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel2">实名认证列表</h4>
            </div>
            <div class="modal-body" id="show_data_block">

            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    function changeSelectId(value){
        if(value == 999){
            $("#customIdInput").show();
        }else{
            $("#customIdInput").hide();
        }
    }


    function showRealNameTmp(userId){
        $("#show_data_block").load("/user/operation/refRealUserList", {refUserId:userId, dateStart:$("#dateStart").val(), dateEnd:$("#dateEnd").val()}, function(){
            $('#myModal2').modal('show');
        });
    }
    $(document).ready(function() {
        $(".userNickName").change(function() {
            var userId = $(this).attr("data-userId");
            var nickName = $(this).val();
            $.get("/user/operation/set_user_nick_name?userId="+userId+"&nickName="+nickName, function() {

            });
        });
    });
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>