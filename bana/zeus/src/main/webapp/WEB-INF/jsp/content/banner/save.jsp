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
            <h4 class="heading">Banner添加</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form id="save" method="post" action="/content/banner/save" class="form-horizontal" style="width: 90%" enctype="multipart/form-data">
                <div class="form-group">
                    <label class="col-sm-3 control-label">分类简称</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="appType" id="appType" value="" required="required"  />
                        <span class="help">* 贵金属简称：gjs  邮币卡简称：ybk  主站简称：home</span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">名称</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="name" id="name" value=""/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">排序</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="sort" value='0' autocomplete="off" required="required" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">显示？（0 显示 1 隐藏）</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="isShow" value='0' autocomplete="off" required="required" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">PC Banner图片</label>
                    <div class="col-sm-9">
                        <input type="file" name="pcPicFile" id="pcPicFile" class="form-control" />
                        <span class="help">* 请上传正确尺寸的banner图片</span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">PC Banner跳转链接</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="pcJumpUrl" value='https://' autocomplete="off" />
                        <span class="help">* 可以为空，空为不可点击</span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">APP Banner图片</label>
                    <div class="col-sm-9">
                        <input type="file" name="appPicFile" id="appPicFile" class="form-control" />
                        <span class="help">* 请上传正确尺寸的banner图片</span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">APP Banner跳转链接</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="appJumpUrl" value='https://' autocomplete="off" />
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-9">
                        <button type="submit" class="btn btn-blood">添加Banner</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script>

</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>