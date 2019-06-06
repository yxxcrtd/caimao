<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<c:if test="${not empty ruleMap['nav_page']}">
    <ul class="nav nav-tabs nav_height">
        <c:forEach items="${ruleMap['nav_page']}" var="v1">
            <c:if test="${v1.getValue().getRuleType() == 1}">
                <li<c:if test="${not empty ruleMap['curRuleId'] && (ruleMap['curRuleId'] == v1.getValue().getId() || ruleMap['parentIds'].contains(v1.getValue().getId()))}"> class="active"</c:if>><a href="${v1.getValue().getRule()}">${v1.getValue().getRuleName()}</a></li>
            </c:if>
        </c:forEach>
    </ul>
</c:if>
