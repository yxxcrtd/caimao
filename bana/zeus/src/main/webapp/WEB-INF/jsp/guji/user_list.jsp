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

</head>
<body>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_top.jsp"/>
<div class="body_container">
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_left.jsp"/>
    <div class="main_content">
        <div class="container"> 
            <h4 class="heading">股计用户列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form class="form-inline" role="form" name="searchForm" action="/guji/user_list.html" method="get">
                <div class="form-group" style="margin-bottom: 5px;">
                    <label>昵称(*模糊)：</label>
                    <input type="text" class="form-control input-sm" name="nickName" value="${nickName}" placeholder="支持模糊查询" style="width: 120px;">
                </div>
                 <div class="form-group" style="margin-bottom: 5px;">
                    <label>认证状态：</label>
                     <select name="authStatus">
                         <option value="">全部</option>

                         <c:forEach items="${authStatusMap}" var="entry">
                             <c:choose>
                                 <c:when test="${authStatus == entry.key}">
                                     <option value="${entry.key}" selected><c:out value="${entry.value}"/></option>
                                 </c:when>
                                 <c:otherwise>
                                     <option value="${entry.key}"><c:out value="${entry.value}"/></option>
                                 </c:otherwise>
                             </c:choose>
                         </c:forEach>
                     </select>
                </div>
                <button type="submit" class="btn btn-primary btn-sm">查询</button>
            </form>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th>昵称</th>
                    <th>头像</th>
                    <th>是否关注</th>
                    <th>认证机构</th>
                    <th>名片图片</th>
                    <th>更新时间</th>
                    <th>创建时间</th>
                    <th>状态</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${userList.getItems()}" var="u">
                    <tr>
                    	<td>${u.nickname}</td>
                        <td><img src="${u.headimgurl}" width="64px" height="64px" /></td>
                        <td>
                            <c:choose>
                                <c:when test="${u.subscribe == 0}">未关注</c:when>
                                <c:when test="${u.subscribe == 1}">已关注</c:when>
                            </c:choose>
                        </td>
                        <td>${u.certificationAuth}</td>
                        <td><a href="javascript:void(0);" class="pic" src="${domainUrl}${u.cardPic}">点击查看</a></td>
                        <td><fmt:formatDate value="${u.updateTime}" type="both" dateStyle="default" /></td>
                        <td><fmt:formatDate value="${u.createTime}" type="both" dateStyle="default" /></td>
                        <td>
                                <c:forEach items="${authStatusMap}" var="entry">
                                    <c:choose>
                                        <c:when test="${u.authStatus == entry.key}">
                                              <c:out value="${entry.value}"/>
                                        </c:when>
                                    </c:choose>
                                </c:forEach>
                        </td>
                        <td>
                            <c:if test="${u.authStatus == 3}">
                                <a href="javascript:void(0);" onclick="if (confirm('就这么过了，会不会不严谨？')) authStatus('${u.wxId}', '2')">认证通过</a> <br /><br />
                                <a href="javascript:void(0);" onclick="if (confirm('人家辛苦进行的认证，你确定要打回吗？')) authStatus('${u.wxId}', '1')">打回!!!</a>
                            </c:if>
                            <a href="/guji/edit?id=${u.wxId}">编辑</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            ${pageHtml}
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $(".pic").click(function() {
            var picUrl = $(this).attr('src');
            $('#myModalLabel').html("名片图片");
            $('#show_alert_msg').html('<div style="text-align: center;"><img src="'+ picUrl +'" width="100%" /></div>');
            $('#myModal').modal('show');

        });

    });
    /**
     * 更新用户的认证状态
     * @param wxId
     * @param status
     */
    function authStatus(wxId, status) {
        $.ajax({
            url : "/guji/auth_user",
            type : "post",
            data : {wxId : wxId, authStatus : status},
            success : function(d) {
                alert("操作成功！");
                window.location.reload();
            }
        });
    }
</script>

<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>