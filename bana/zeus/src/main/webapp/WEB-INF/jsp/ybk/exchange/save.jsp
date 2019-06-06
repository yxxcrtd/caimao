<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>财猫后台管理系统</title>
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_head.jsp"/>
    <!-- 配置文件 -->
    <script type="text/javascript" src="/js/ueditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="/js/ueditor/ueditor.all.js"></script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_top.jsp"/>
<div class="body_container">
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_left.jsp"/>
    <div class="main_content">
        <div class="container">
            <h4 class="heading">交易所添加</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form id="save" method="post" action="/ybk/exchange/save" class="form-horizontal" style="width: 90%">

                <div class="form-group">
                    <label class="col-sm-2 control-label">简称</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="shortName" id="shortName" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">号段</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="number" id="number" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">名称</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="name" id="name" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">省份</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="province" id="province" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">城市</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="city" id="city" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">支持银行</label>
                    <div class="col-sm-10">
                        <c:forEach items="${bankList}" var="u">
                            <label><input type="checkbox" name="supportBanks" value="${u.bankNo}" />${u.bankName}</label>
                        </c:forEach>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">交易日类型</label>
                    <div class="col-sm-10">
                        <select name="tradeDayType" class="form-control input-sm">
                            <c:forEach items="${tradeDayTypeMap}" var="u">
                                <option value="${u.key}" >${u.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">交易时间</label>
                    <div class="col-sm-10">
                        AM <input type="text" class="form-control" name="amBeginTime" id="amBeginTime" style="width: 100px;display: inline;" /> - <input type="text" class="form-control" name="amEndTime" id="amEndTime" style="width: 100px;display: inline;" />
                        PM <input type="text" class="form-control" name="pmBeginTime" id="pmBeginTime" style="width: 100px;display: inline;" /> - <input type="text" class="form-control" name="pmEndTime" id="pmEndTime" style="width: 100px;display: inline;" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">状态</label>
                    <div class="col-sm-10">
                        <select name="status" class="form-control input-sm">
                            <c:forEach items="${statusMap}" var="u">
                                <option value="${u.key}" >${u.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">排序</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="sortNo" id="sortNo" maxlength="2"/>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">开户处理时间段</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="openProcessTimes" id="openProcessTimes" value="" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">出入金时间段</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="comeInTimes" id="comeInTimes" value="" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">银商绑定时间段</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="bankBindTimes" id="bankBindTimes" value="" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">交易时间段</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="tradeTimes" id="tradeTimes" value="" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">交易手续费字段</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="tradeFee" id="tradeFee" value="" />
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">分时行情地址</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="apiMarketUrl" id="apiMarketUrl"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">日K行情地址</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="apiTickerUrl" id="apiTickerUrl"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">文章地址</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="articleUrl" id="articleUrl"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">打新申购数量</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="shenGouNum" id="shenGouNum"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">官方网址</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="officialUrl" id="officialUrl"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-5 col-sm-7">
                        <button type="submit" class="btn btn-blood">保存</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>