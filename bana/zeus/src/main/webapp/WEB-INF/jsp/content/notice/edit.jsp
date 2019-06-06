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
            <h4 class="heading">公告修改</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form id="save" method="post" action="/content/notice/edit" class="form-horizontal" style="width: 90%">
                <input type="hidden" name="id" value="${noticeInfo.id}">
                <div class="form-group">
                    <label class="col-sm-3 control-label">标题</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="title" id="title" value="${noticeInfo.title}" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">内容</label>
                    <div class="col-sm-9">
                        <script id="content" name="content" type="text/plain">${noticeInfo.content}</script>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">来源</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="source" value='${noticeInfo.source}' autocomplete="off" required="required" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">来源网址</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="source_href" value='${noticeInfo.sourceHref}' autocomplete="off" required="required" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">显示？（0 隐藏 1 显示）</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="list_show" dtype="int" value='${noticeInfo.listShow}' autocomplete="off" required="required" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">置顶？（0 不置顶 1 置顶）</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="top_show" dtype="int" value='${noticeInfo.topShow}' autocomplete="off" required="required" />
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-9">
                        <button type="submit" class="btn btn-blood">修改公告</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
$(function(){
	$("input[dtype='int']").keyup(function () {
		 $(this).val(toNum($(this).val()));  
    }).bind("paste",function(){
        $(this).val(toNum($(this).val()));     
    }).css("ime-mode", "disabled");

    $("input[dtype='float']").keyup(function () {
    	$(this).val(toFloat($(this).val()));    
    }).bind("paste",function(){
        $(this).val(toFloat($(this).val()));     
    }).css("ime-mode", "disabled");

    var ue = UE.getEditor('content');
})
function toFloat(num){ 
	num = num.replace(/[^\d.]/g,'');
	num = num.replace(/^\.{1,}/g,"");
	num = num.replace(/^0{1,}/g,"0");
	num = num.replace(".","$#$").replace(/\./g,"").replace("$#$","."); 
	return num;
}

function toNum(num){
	num = num.replace(/[^\d]/g,'');
	num = num.replace(/^0{1,}/g,"0");
	if(num.length>1){
		num = num.replace(/^0/g,"");
	}
	return num;
}
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>