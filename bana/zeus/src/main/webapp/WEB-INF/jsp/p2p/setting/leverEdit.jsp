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
            <h4 class="heading">${prod.prodName }-${setting.prodLever}倍杠杆设置</h4>
            <jsp:include page="/WEB-INF/jsp/include/tpl_menu_page.jsp"/>
            <form id="setting" method="post" action="/p2p/setting/product/leverSave" class="form-horizontal" style="width: 500px">
                <div class="form-group">
                    <label class="col-sm-3 control-label">是否启用p2p</label>
                    <div class="col-sm-9">
                        <input type="radio"  name="isAvailable" value="true" <c:if test="${setting.isAvailable}">checked="checked"</c:if>>启用
                        <input type="radio"  name="isAvailable" value="false" <c:if test="${!setting.isAvailable}">checked="checked"</c:if>>禁止
                        <input type="hidden" name="prodLever" value="${setting.prodLever}" >
                        <input type="hidden" name="insert" value="${insert}" >
                        <input type="hidden" name="prodId" value="${setting.prodId}" >
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">财猫最小出资(元)</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="chuziMin" dtype="int" value='<fmt:formatNumber type="number" value="${setting.chuziMin/100}"  pattern="#"/>' autocomplete="off" required="required" >
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">财猫最大出资(元)</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="chuziMax" dtype="int" value='<fmt:formatNumber type="number" value="${setting.chuziMax/100}"   pattern="#"/>' autocomplete="off" required="required" >
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">投资比例</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="chuziRate" dtype="float" value='${setting.chuziRate}' autocomplete="off" required="required" >
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">管理费(元)</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="manageFee" dtype="int" value='<fmt:formatNumber type="number" value="${setting.manageFee/100}" pattern="#"/>' autocomplete="off" required="required" >
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">管理费比例</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="manageRate" dtype="float" value='${setting.manageRate}' autocomplete="off" required="required" >
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">财猫最小出资利率</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control" name="caimaoRate" dtype="float" value='${setting.caimaoRate}' autocomplete="off" required="required" >
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