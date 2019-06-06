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
            <h4 class="heading">首页文章列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form method="post" class="form-inline" action="/gjs/article/index" name="searchForm">
                <input type="hidden" name="currentPage" value="1" id="currentPage">
                <div class="form-group">
                    <label>状态:</label>
                    <select name="category" class="form-control input-sm">
                        <option value="">全部</option>
                        <option value="-1" <c:if test="${-1 == list.category}">selected="selected"</c:if>>草稿</option>
                        <option value="0" <c:if test="${0 == list.category}">selected="selected"</c:if>>普通</option>
                        <option value="1" <c:if test="${1 == list.category}">selected="selected"</c:if>>置顶</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>是否显示:</label>
                    <select name="isShow" class="form-control input-sm">
                        <option value="">全部</option>
                        <option value="0" <c:if test="${0 == list.isShow}">selected="selected"</c:if>>显示</option>
                        <option value="1" <c:if test="${1 == list.isShow}">selected="selected"</c:if>>不显示</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>编辑时间:</label>
                    <input type="date" class="form-control input-sm" name="dateStart" value="${list.dateStart}"> -
                    <input type="date" class="form-control input-sm" name="dateEnd" value="${list.dateEnd}">
                </div>
                <br/>
                <div class="form-group" style="margin-top: 5px;">
                    <label>定时发布日期:</label>
                    <input type="date" class="form-control input-sm" name="pubStart" value="${list.pubStart}"> -
                    <input type="date" class="form-control input-sm" name="pubEnd" value="${list.pubEnd}">
                </div>
                <button class="btn btn-sm btn-primary" type="submit">查询</button>
            </form>

            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                    <tr>
                        <th width="5%" style="text-align: center;">编号</th>
                        <th width="6%" style="text-align: center;">状态</th>
                        <th width="10%" style="text-align: center;">标签</th>
                        <th width="6%" style="text-align: center;">显示否</th>
                        <th width="20%" style="text-align: center;">标题</th>
                        <th width="14%" style="text-align: center;">发布时间</th>
                        <th width="14%" style="text-align: center;">创建时间</th>
                        <th width="6%" style="text-align: center;">阅读数</th>
                        <th width="19%">操作</th>
                    </tr>
                </thead>
                <c:forEach items="${list.getItems()}" var="l">
                    <tr>
                        <td style="text-align: center;">${l.id}</td>
                        <td style="text-align: center;">
                            <c:if test="${0 < l.status}"><span>置顶-${l.status}</span></c:if>
                            <c:choose>
                                <c:when test="${-1 == l.status}">草稿</c:when>
                                <c:when test="${0 == l.status}"><span>普通</span></c:when>
                            </c:choose>
                        </td>
                        <td style="text-align: center;">${l.category}</td>
                        <td style="text-align: center;">
                            <c:choose>
                                <c:when test="${0 == l.isShow}"><span class="showing">显示</span></c:when>
                                <c:when test="${1 == l.isShow}"><span>不显示</span></c:when>
                            </c:choose>
                        </td>
                        <td>${l.title}</td>
                        <td style="text-align: center;" class="pub_time">${l.pub}</td>
                        <td style="text-align: center;">${l.created}</td>
                        <td style="text-align: center;">${l.view}</td>
                        <td>
                            <a href="/gjs/article/index/edit?id=${l.id}">编辑</a>
                            <%--<c:if test="${-1 == l.status}"><a href="javascript:;" class="myshow" value="${l.id}">显示</a></c:if>--%>
                            <c:if test="${-1 < l.status}">
                                <c:choose>
                                    <c:when test="${1 == l.isShow}"><a href="javascript:;" class="myshow" value="${l.id}">发布</a></c:when>
                                    <c:when test="${0 == l.isShow}"><a href="javascript:;" class="unmyshow">不显示</a></c:when>
                                </c:choose>

                                <c:if test="${'' != l.pub}">
                                    <a href="javascript:;" class="top" value="${l.id}">设置置顶</a>
                                </c:if>
                            </c:if>
                            <c:if test="${1 > l.status}">
                                <a href="/gjs/article/index/del?id=${l.id}" onclick="return confirm('确认要删除此当前标签吗？');">删除</a>
                            </c:if>
                            <span class="did" style="display: none;">${l.id}</span>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${0 == list.getItems().size()}">
                    <tr style="text-align: center"><td colspan="10">没有数据</td></tr>
                </c:if>
                </tbody>
            </table>
            共有<b> ${list.totalCount} </b>条记录
            <div id="page" currentPage="${list.currentPage}" pageCount="${list.pages}"></div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function(){
        $("#page").my_page();

        $(".myshow").on("click", function() {
            var t;
            var seted = $(this).parents("tr").children("td").eq(5).html();

            // if (seted.value !== null || seted.value !== undefined || seted.value !== "") {
            if (0 < seted.length) {
                t = seted;
            } else {
                t = getCurrentTime();
            }
            $('#myModalLabel').html("显示设置");
            $('#show_alert_msg').html('发布时间：<input id="pubTime" style="width: 230px; border: 1px solid #ccc; font-size: 14px; padding: 6px 12px;" value="' + t + '" />&nbsp;&nbsp;' +
                                      '<input type="button" class="btn btn-default" style="padding-bottom: 3px;" value=" 发 布 " onClick="setShow(' + "'" + $(this).attr("value") + "', '" + t + "'" + ');" />');
            $('#myModal').modal('show');
        });

        // 设置不显示
        $(".unmyshow").on("click", function() {
            var This = $(this);
            var cur = parseInt(This.siblings(".did").text());
            if (confirm("确定不显示吗？")) {
                $.get("/gjs/article/index/show", {"id" : cur}, function(data) {
                    if ("ok" == data) {
                        alert("设置成功！");
                        This.parents("tr").find(".showing").html("<span>不显示</span>");
                        location.reload();
                    } else if ("no" == data) {
                        alert("没有修改！");
                    } else {
                        alert("发生未知错误，请稍后重试！");
                    }
                });
            }
        });

        $(".top").on("click", function() {
            var This = $(this);
            var cur = parseInt(This.siblings(".did").text());
            $.get("/gjs/article/index/top", {"id" : cur}, function(data) {
                $('#myModalLabel').html("置顶设置");
                $('#show_alert_msg').html(data);
                $('#myModal').modal('show');
            });
        });
    });

    // 获取当前时间
    function getCurrentTime() {
        var now = new Date();
        var year = now.getFullYear();
        var month = now.getMonth() + 1;
        var day = now.getDate();
        var hh = now.getHours();
        var mm = now.getMinutes();
        var clock = year + "-";
        if(month < 10) { clock += "0"; }
        clock += month + "-";
        if(day < 10) { clock += "0"; }
        clock += day + " ";
        if(hh < 10) { clock += "0"; }
        clock += hh + ":";
        if (mm < 10) { clock += '0'; }
        clock += mm;
        return clock;
    }

    // 设置显示时间
    function setShow(id) {
        $.get("/gjs/article/index/setShow", {"id" : id, "d" : $("#pubTime").val()}, function(data) {
            if ("ok" == data) {
                alert("设置成功！");
                location.reload();
                // This.parents("tr").find(".showing").html("<span>不显示</span>");
            } else {
                alert("发生未知错误，请稍后重试！");
            }
        });
    }

    function unTop(id) {
        $.get("/gjs/article/index/unTop", {"id" : id}, function(data) {
            if ("ok" == data) {
                alert("取消成功！");
                location.reload();
            } else if ("no" == data) {
                alert("普通文章无法取消置顶！");
            } else {
                alert("发生未知错误，请稍后重试！");
            }
        });
    }

    function setTop(id) {
        $.get("/gjs/article/index/setTop", {"id" : id, "status" : $("#select").val()}, function(data) {
            if ("ok" == data) {
                alert("设置成功！");
                location.reload();
            } else if ("no" == data) {
                alert("不能重复设置！");
            } else {
                alert("发生未知错误，请稍后重试！");
            }
        });
    }

    function export_excel(){
        $("form[name='searchForm']").append('<input type="hidden" name="isExp" value="1">');
        $("#currentPage").val(1);
        $("form[name='searchForm']").submit();
    }
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>