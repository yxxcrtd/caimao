<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>财猫后台管理系统</title>
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_head.jsp"/>
    <style>
        .topic {color: blue;}
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_top.jsp"/>
<div class="body_container">
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_left.jsp"/>
    <div class="main_content">
        <div class="container">
            <h4 class="heading">节假日设置</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>

            <hr/>
            <form action="/setup/save_trade_time" method="post">
                <p>用于画前端分时图行情，值为每个交易所每天交易的时间，单位是小时</p>
                <label>南交所：<input type="text" name="njsHours" value="${njsHours}" /></label>
                <label>上金所：<input type="text" name="sjsHours" value="${sjsHours}" /></label>
                <label><input type="submit" name="submit" value="保存" /></label>
            </form>
            <hr/>

            <form method="post" class="form-inline" action="/setup/holiday" name="searchForm">
                <input type="hidden" name="currentPage" value="1" id="currentPage">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>交易所：</label>
                    <select name="exchange" style="width: 150px; border: 1px solid #cccccc; padding: 5px;">
                        <option value="">全部</option>
                        <c:forEach items="${exchangeMap}" var="e">
                            <option value="${e.key}" <c:if test="${e.key == list.exchange}">selected="selected"</c:if>>${e.value}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>日期：</label>
                    <input type="text" class="form-control input-sm" name="holiday" value="${list.holiday}" placeholder="格式：20151212" style="width: 150px;">
                </div>
                <button type="button" class="btn btn-primary btn-sm" onclick="redirectTo(1);">查询</button>
                <button type="button" class="btn btn-primary btn-sm" onClick="location.href='/setup/holidayEdit?id=0';">添加</button>
            </form>

            <div>
                <p>1. 默认查询是显示大于等于今天的日期列表，可自定义查询指定一天的设置</p>
                <p>2. 先选择上面的交易所，查询，下边就会自动列出该交易所的时间列表</p>
                <p>3. 鲜艳颜色的列，表示未添加的日期，会自动列出列表中最后一天的延续20天的值，自动带入的时间段使用最后一天的值</p>
                <p style="color: red;">4. 根据交易所的时间，设置每天的交易时间段，一定要认真对待</p>
                <p style="color: green;">5. 时间段格式：小时分钟-小时分钟 ，例如：0900-1100 ，表示从早上9点到11点，多个时间段使用逗号分隔</p>
                <p style="color: green;">6. 节假日格式：0</p>
                <p style="color: blueviolet; font-weight: bold;">7. 时间段必须要连续，必须要连续，必须要连续，重要的事情说三遍</p>
            </div>

            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th width="7%">id</th>
                    <th width="10%">交易所</th>
                    <th width="13%">日期</th>
                    <th width="40%">时间段</th>
                    <th width="15%">操作日期</th>
                    <th width="15%">操作</th>
                </tr>
                </thead>
                <tbody id="tbody">

                <c:forEach items="${list.getItems()}" var="s">

                    <c:set var="endExchange" value="${s.exchange}"/>
                    <c:set var="endHoliday" value="${s.holiday}"/>
                    <c:set var="endTradeTime" value="${s.tradeTime}"/>

                    <tr id="old${s.id}">
                        <form action="/setup/holidaySave" method="post" class="submitForm" onsubmit="return submitForm(this, 'old${s.id}');">
                            <td>
                                <input type="hidden" name="id" value="${s.id}" class="id" />
                                    ${s.id}
                            </td>
                            <td>
                                <select name="exchange">
                                    <option value="NJS" <c:if test="${s.exchange == 'NJS'}">selected</c:if> >南交所</option>
                                    <option value="SJS" <c:if test="${s.exchange == 'SJS'}">selected</c:if> >上金所</option>
                                </select>
                            </td>
                            <td>
                                <input type="text" name="holiday" value="${s.holiday}" style="width: 100px;" />
                            </td>
                            <td>
                                <input type="text" name="tradeTime" value="${s.tradeTime}" style="width: 350px;" />
                            </td>
                            <td>${s.optDate}</td>
                            <td>
                                <input type="submit" name="submit" value="修改" />
                                <a href="/setup/del?id=${s.id}" onClick="return confirm('确定删除吗？');">删除</a>
                                <span class="topic"></span>
                            </td>
                        </form>
                    </tr>
                </c:forEach>

                <c:forEach begin="1" end="20" step="1" var="i">
                    <tr style="background-color: mistyrose;" id="new${i}">
                        <form action="/setup/holidaySave" method="post" class="submitForm" onsubmit="return submitForm(this, 'new${i}');">
                            <td>
                                <input type="hidden" name="id" value="0" class="id" />
                            </td>
                            <td>
                                <select name="exchange">
                                    <option value="NJS" <c:if test="${endExchange == 'NJS'}">selected</c:if> >南交所</option>
                                    <option value="SJS" <c:if test="${endExchange == 'SJS'}">selected</c:if> >上金所</option>
                                </select>
                            </td>
                            <td>
                                <input type="text" name="holiday" value="<c:out value="${endHoliday + i}"/>" style="width: 120px;"  />
                            </td>
                            <td>
                                <input type="text" name="tradeTime" value="<c:out value="${endTradeTime}"/>" style="width: 200px;" />
                            </td>
                            <td>${s.optDate}</td>
                            <td>
                                <input type="submit" name="submit" value="保存" />
                                <span class="topic"></span>
                            </td>
                        </form>
                    </tr>
                </c:forEach>

                </tbody>
            </table>
            <%--<div>--%>
                <%--<button type="button" id="add">添加一个日期</button>--%>
            <%--</div>--%>
            <div id="page" currentPage="${list.currentPage}" pageCount="${list.pages}" ></div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(function(){
        $("#page").my_page();

//        // 添加一个日期的
//        $('#add').click(function() {
//            var html = '<tr><form action="/setup/holidaySave" method="post" class="submitForm" onsubmit="return submitForm(this);"><td><input type="hidden" name="id" value="" /></td><td><select name="exchange"><option value="NJS" selected>南交所</option><option value="SJS" selected>上金所</option></select></td><td><input type="text" name="holiday" value="" /></td><td><input type="text" name="tradeTime" value="" /></td><td>--</td><td><input type="submit" name="submit" value="保存" /><span class="topic"></span></td></form></tr>';
//            $('#tbody').append(html);
//        });

    });

    function submitForm(e, id) {
        var url = "/setup/holidaySave?" + $(e).serialize();
        $.ajax({
            url : url,
            type : "GET",
            dataType : "json",
            success : function (res) {
                if (res.msg != "") {
                    alert(res.msg);
                } else {
                    $('#' + id).find(".topic").html("操作成功");
                }
            }
        });
        return false;
    }


</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>