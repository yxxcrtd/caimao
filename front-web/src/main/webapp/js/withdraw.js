$(function () {
	//相应菜单加载样式
 	$("#jsTopMenu > li:eq(3) > a").addClass("hov");
	$(".left_bar > ul > li:eq(2) > a").addClass("hov");
	$("#cNum").keyup(function(){
		var val = $(this).val();
		val =toFloat(val);
		$(this).val(val);
		//$("#chineseNum").val(getChangeNum(val));
	});
	$("#cNum").focus(function(){
		var val = $(this).val();
		$(this).val(removeCommas(val));
	});
	$("#cNum").blur(function(){
		var val = $(this).val();
		$(this).val(addCommas(val));
	});
	$("#jsWithdraw").click(function(){
		$.post("/account/withdraw",{withdraw_amount:toFenRemoveCommas($("#cNum").val()),trade_pwd:RSAUtils.encryptedString($("#jsTradPwd").val())},function(response){
			if(response.success){
				window.location.href="/account/hiswithdraw.htm";
			}else{
				alert(response.msg);
			}
		});
	});
	$("#jsPwdYan").click(function(){
		var type =$("#jsTradPwd").attr("type");
		if(type=="password"){
			$("#jsTradPwd").attr("type","text");
		}else{
			$("#jsTradPwd").attr("type","password");
		}
	});
})