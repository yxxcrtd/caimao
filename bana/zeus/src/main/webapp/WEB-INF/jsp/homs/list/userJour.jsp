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
            <h4 class="heading">账户财务流水</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form class="form-inline" role="form" name="searchForm" action="/homs/user_jour_list" method="post">
                <input type="hidden" name="currentPage" value="1" id="currentPage">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>用户编号：</label>
                    <input type="text" class="form-control input-sm" name="userId" value="${list.userId}" style="width: 140px;">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>用户姓名：</label>
                    <input type="text" class="form-control input-sm" name="userName" value="${list.userName}" placeholder="用户姓名支持模糊查询" style="width: 140px;">
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>手机号码：</label>
                    <input type="text" class="form-control input-sm" name="mobile" value="${list.mobile}" style="width: 140px;">
                </div>
                <button type="button" class="btn btn-success btn-sm" onclick="export_excel()" style="margin-bottom: 5px;">导出</button>
                <br>
                <div class="form-group">
                    <label>开始日期：</label>
                    <input type="date" class="form-control input-sm" name="dateStart" value="${list.dateStart}" style="width: 140px;">
                </div>
                <div class="form-group">
                    <label>结束日期：</label>
                    <input type="date" class="form-control input-sm" name="dateEnd" value="${list.dateEnd}" style="width: 140px;">
                </div>
                <div class="form-group">
                    <label>业务类型：</label>
                    <select class="form-control input-sm" name="accountBizType" style="width: 140px;">
                        <option value="">全部</option>
                        <c:forEach items="${accountBizTypeList}" var="u">
                            <option value="${u.key}" <c:if test="${u.key == list.accountBizType}">selected</c:if>>${u.value}</option>
                        </c:forEach>
                    </select>
                </div>
                <button type="button" class="btn btn-primary btn-sm" onclick="redirectTo(1);">查询</button>
            </form>
            <table class="table table-hover table-bordered">
                <thead>
                <tr>
                    <th>流水号</th>
                    <th>用户编号</th>
                    <th>用户姓名</th>
                    <th>用户手机号</th>
                    <th>配资账户</th>
                    <th>业务类型</th>
                    <th>变动前金额(元)</th>
                    <th>变动金额(元)</th>
                    <th>变动后金额(元)</th>
                    <th>变动时间</th>
                    <th>发生方向</th>
                    <th>关联流水号</th>
                    <th>备注</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list.getItems()}" var="u">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.userId}</td>
                        <td>${u.userName}</td>
                        <td>${u.mobile}</td>
                        <td>${u.pzAccountId}</td>
                        <td class="bizType_${u.id}">${u.accountBizType}<c:if test="${u.accountBizType == '其它'}">　<a href="javascript:;" onclick="changeAccountBizType(${u.id})">修改</a></c:if></td>
                        <td>${u.preAmount / 100}</td>
                        <td>${u.transAmount / 100}</td>
                        <td>${u.postAmount / 100}</td>
                        <td><fmt:formatDate value="${u.transDatetime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><c:if test="${u.seqFlag == 1}">减少</c:if><c:if test="${u.seqFlag == 2}">增加</c:if></td>
                        <td>${u.refSerialNo}</td>
                        <td>${u.remark}</td>
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
                <h4 class="modal-title" id="myModalLabel2">修改业务类型</h4>
            </div>
            <div class="modal-body">
                <input type="hidden" id="changeId">
                <label>业务类型：</label>
                <select class="form-control input-sm" id="changeValue" style="width: 140px;display: inline-block;">
                    <c:forEach items="${accountBizTypeList}" var="u">
                        <option value="${u.key}">${u.value}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="saveAccountBizType()">保存</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
	$(function(){
	     $("#page").my_page();
	});
    function export_excel(){
        $("form[name='searchForm']").append('<input type="hidden" name="isExp" value="1">');
        $("#currentPage").val(1);
        $("form[name='searchForm']").submit();
    }
    function changeAccountBizType(id){
        $("#changeId").val(id);
        $("#myModalLabel2").text("确定修改流水号：" + id + " 的业务类型吗？");
        $('#myModal2').modal('show');
    }

    function saveAccountBizType(){
        var id = $("#changeId").val();
        var value = $("#changeValue").val();
        if(id != "" && value != ""){
            $.post("/homs/user_jour_biz_type_change", {id:id,bizType:value}, function(d){
                if(d.code == 0){
                    var changeText = $("#changeValue").find("option:selected").text();
                    $(".bizType_" + id).html(changeText);
                    $('#myModal2').modal('hide');
                }else{
                    show_alert(d.msg, "提示");
                }
            },"JSON");
        }
    }
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>