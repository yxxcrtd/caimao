<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            <h4 class="heading">权限列表</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <table class="table table-hover table-bordered" style="width: 1000px;">
                <thead>
                <tr>
                    <th width="50">#</th>
                    <th width="80">排序</th>
                    <th>节点</th>
                    <th>规则</th>
                    <th width="70">节点类型</th>
                    <th width="50">状态</th>
                    <th width="200">操作</th>
                </tr>
                </thead>
                <tbody id="category_body">${ruleListTree}</tbody>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript">
    /*$(".has_child").click(function(){
        var tr_parent = $(this).parent();
        var id = tr_parent.attr('data-id');
        if($(".pid_" + id + ":first").is(":visible")){
            var all_tr = $("#category_body").find("tr");
            var level = tr_parent.attr("data-level");
            var on_index = all_tr.index(tr_parent);
            for(var i = on_index+1; i<999; i++){
                var pre_tr = all_tr.eq(i);
                if(pre_tr.attr("data-level") > level){
                    pre_tr.show();
                }else{
                    break;
                }
            }
        }else{
            $(".pid_" + id).toggle();
        }
    });*/

    function saveRuleSort(pk){
        $.post('/admin/ruleSort',{id:pk, sort:$('#sort' + pk).val()});
    }

    function show_rule_user(rule_id){
        $.get('/admin/ruleHave?id=' + rule_id, function(d){
            $('#myModalLabel').html("权限拥有者列表");
            $('#show_alert_msg').html(d);
            $('#myModal').modal('show');
        });
    }
</script>
<jsp:include page="/WEB-INF/jsp/include/tpl_menu_footer.jsp"/>