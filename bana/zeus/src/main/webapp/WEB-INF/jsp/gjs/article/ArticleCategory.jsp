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
            <h4 class="heading">文章标签</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                    <tr>
                        <th>编号</th>
                        <th>名称</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <c:forEach items="${list}" var="l">
                    <tr>
                        <td>${l.id}</td>
                        <td>${l.name}</td>
                        <td>
                            <a href="/gjs/article/category/edit?id=${l.id}">编辑</a>
                            <a href="/gjs/article/category/del?id=${l.id}" onclick="return confirm('确认要删除此当前标签吗？');">删除</a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${0 == list.size()}">
                    <tr style="text-align: center"><td colspan="10">没有数据</td></tr>
                </c:if>
                </tbody>
            </table>
            <a href="/gjs/article/category/edit?id=0">
                <div style="color: #000000; background-color: #009DD9; width: 100px; height: 30px; line-height: 30px; text-align: center;">新增标签</div>
            </a>
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
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>