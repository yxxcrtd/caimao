<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>财猫后台管理系统</title>
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_head.jsp"/>
    <link href="/css/ztree.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/js/jquery.ztree.all-3.5.min.js"></script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_top.jsp"/>
<div class="body_container">
    <jsp:include page="/WEB-INF/jsp/include/tpl_menu_left.jsp"/>
    <div class="main_content">
        <div class="container">
            <h4 class="heading">用户权限分配</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <div class="col-xs-4">
                <ul id="tree" class="ztree" style="margin-bottom: 20px;"></ul>
                <form id="AjaxForm" class="form-horizontal" role="form" method="post">
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-10">
                            <button type="submit" class="btn btn-blood btn-sm" id="save_group_rule">保存</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    var setting = {
        check: {enable: true,chkboxType :{ "Y" : "ps", "N" : "s" }},
        data: {simpleData: {enable: true}},
        view:{showIcon:false,dblClickExpand :false},
        callback:{onClick:onClick}
    };
    var zNodes = ${ruleData};
    function onClick(e,treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj("tree");
        zTree.expandNode(treeNode);
    }
    $(function(){$.fn.zTree.init($("#tree"), setting, zNodes);});
    $("#AjaxForm").submit(function(){
        var treeObj = $.fn.zTree.getZTreeObj('tree');
        var nodes = treeObj.getCheckedNodes(true);
        var checkedStr = '';
        $.each(nodes,function(k,v){
            if(v.id > 0) checkedStr += '<input type="hidden" name="rule" value="'+v.id+'" />';
        });
        $(this).prepend(checkedStr);
    });
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>