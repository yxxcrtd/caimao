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
            <h4 class="heading">添加自定义P2P借款记录</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form id="setting" method="post" action="/p2p/setting/add_custom_loan" class="form-horizontal" style="width: 500px">
                <div class="form-group">
                    <label class="col-sm-3 control-label">借款名称</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="targetName" autocomplete="off" required="required" />
                        <span class="help-block">* 例如：曹先生续约借款</span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">详情页标题</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="customTitle" autocomplete="off" required="required" />
                        <span class="help-block">* 例如：曹先生借款500万元 市值600万元股票做质押</span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">项目介绍</label>
                    <div class="col-sm-9">
                        <textarea name="customContent" class="form-control"></textarea>
                        <span class="help-block"> </span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">标的金额</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="targetAmount" autocomplete="off" required="required" />
                        <span class="help-block"> </span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">标的期限</label>
                    <div class="col-sm-9">
                        <select name="liftTime" class="form-control">
                            <option value="30">1个月</option>
                            <option value="60">2个月</option>
                            <option value="90">3个月</option>
                            <option value="120">4个月</option>
                            <option value="150">5个月</option>
                            <option value="180">6个月</option>
                        </select>
                        <span class="help-block"> </span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">年息利率</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="yearRate" autocomplete="off" required="required" />
                        <span class="help-block">* 例如：“0.18”  即为 18% 的年利率</span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">投标人姓名</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="customUsername" autocomplete="off" required="required" />
                        <span class="help-block">* 例如：曹先生</span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">投标人手机号</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="customMobile" autocomplete="off" required="required" />
                        <span class="help-block">* 例如：186****854</span>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-9">
                        <button type="submit" class="btn btn-blood">保存</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>