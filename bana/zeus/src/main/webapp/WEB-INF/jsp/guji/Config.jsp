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
                    <h4 class="heading">配置</h4>

                    <table class="table table-hover table-bordered" style="width: 1000px;">
                        <thead>
                            <tr>
                                <th width="10%">编号</th>
                                <th width="10%">类型</th>
                                <th width="30%">key</th>
                                <th width="30%">value</th>
                                <th width="20%">操作</th>
                            </tr>
                        </thead>
                        <c:forEach items="${list}" var="l">
                            <tr>
                                <td>${l.id}</td>
                                <td>${l.type}</td>
                                <td>
                                    ${l.key}
                                    <br />
                                    ${l.comment}
                                </td>
                                <td>
                                    <c:if test="${'boolean' == l.type}">
                                        <input type="radio" class="radio1" name="value${l.id}" value="y" <c:if test="${'y' == l.value}">checked="checked"</c:if> /> 是
                                        <input type="radio" class="radio1" name="value${l.id}" value="n" <c:if test="${'n' == l.value}">checked="checked"</c:if> /> 否
                                        <span class="did" style="display: none;">${l.id}</span>
                                    </c:if>
                                </td>
                                <td></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <script type="text/javascript">
        $(function() {
            $(".radio1").on("click", function () {
                var This = $(this);
                var cur = parseInt(This.siblings(".did").text());
                var item = $("input[name='value" + cur + "']:checked").val();
                $.get("/guji/config/set", {"id" : cur, "v" : item }, function(data) {
                    if ("ok" == data) {
                        alert("设置成功！");
                    } else {
                        alert("发生未知错误，请稍后重试！");
                    }
                });
            });
        });
        </script>

        <jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>
