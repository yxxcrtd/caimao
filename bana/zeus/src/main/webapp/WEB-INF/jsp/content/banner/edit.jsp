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
            <h4 class="heading">Banner修改</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form id="save" method="post" action="/content/banner/edit" class="form-horizontal" style="width: 90%" enctype="multipart/form-data">
                <input type="hidden" name="id" value="${bannerInfo.id}">
                <div class="form-group">
                    <label class="col-sm-3 control-label">分类简称</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="appType" id="appType" value="${bannerInfo.appType}" required="required"  />
                        <span class="help">* 贵金属简称：gjs  邮币卡简称：ybk  主站简称：home</span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">名称</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="name" id="name" value="${bannerInfo.name} "/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">排序</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="sort" value='${bannerInfo.sort}' autocomplete="off" required="required" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">显示？（0 显示 1 隐藏）</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="isShow" value='${bannerInfo.isShow}' autocomplete="off" required="required" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">PC Banner图片</label>
                    <div class="col-sm-9">
                        <input type="file" name="pcPicFile" id="pcPicFile" class="form-control" />
                        <a href="javascript:void(0);" class="showPic" data-img-src="${ybkUrl}${bannerInfo.pcPic}">原图片查看</a>
                        <span class="help">* 请上传正确尺寸的banner图片</span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">PC Banner跳转链接</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="pcJumpUrl" value='${bannerInfo.pcJumpUrl}' autocomplete="off" />
                        <span class="help">* 可以为空，空为不可点击</span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">APP Banner图片</label>
                    <div class="col-sm-9">
                        <input type="file" name="appPicFile" id="appPicFile" class="form-control" />
                        <a href="javascript:void(0);" class="showPic" data-img-src="${ybkUrl}${bannerInfo.appPic}">原图片查看</a>
                        <span class="help">* 请上传正确尺寸的banner图片</span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">APP Banner跳转链接</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="appJumpUrl" value='${bannerInfo.appJumpUrl}' autocomplete="off" />
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-9">
                        <button type="submit" class="btn btn-blood">修改Banner</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
$(function() {
    $(".showPic").click(function() {
        var picUrl = $(this).attr('data-img-src');
        $(this).avgrund({
            width : 900,
            height: 500,
            holderClass: 'custom',
            showClose: true,
            showCloseText: 'Close',
            enableStackAnimation: false,
            openOnEvent : false,
            onBlurContainer: '.container',
            template: '<div style="text-align: center;"><img src="'+ picUrl +'" width="800px" height="450px" /></div>'
        });
    });
});
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>