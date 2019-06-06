/*用户注册*/
var container = {};
// 秘钥接口地址 get
container.rsa = basePath + "/sec/rsa";
// 检测用户名是否重复 post
container.check = basePath + "/user/mobile/check";
// 发送注册验证码 post
container.registercode =  basePath + "/sms/registercode";
// 用户注册 post
container.register = basePath + "/user/regist";
// 登录接口地址 post
container.login = basePath + "/user/login";

//验证码倒计时
function countdown(sec){
	var blackActive = $("#button_black_active label");
	if(sec <= 0){
		$("#button_lineblack_active").hide();
		$("#button_lineblack").show();
	}else{
		$("#button_lineblack_active label").text(sec);
		sec--;
		container.setTimeout = setTimeout("countdown("+sec+")",1000);
	}
	
	
}

//发送验证码
function sendSms(){
	var lastSendTime = new Date($.getCookie("regist_sendTime"));
	var multi = new Date().getTime() - lastSendTime.getTime();
	
	if(multi < 30000){

		clearTimeout(container.setTimeout);
		countdown(countdownSeconds-parseInt(multi/1000));
	}else{
		if(container.login_name.length != 11){
			$.dialog({
				content:"请输入正确的手机号码",
				title:"alert",
				ok:function(){}
			});
			return;
		}
		$.commAjax({
			url :container.registercode,
			type:"post",
			data:{
				mobile:container.login_name
			},
			success:function(data){
				if(data.success){
					$.setCookie("regist_sendTime", new Date());
					countdown(countdownSeconds);
				}
			}
		});
	}
	
	
}

//注册账号
function registUser(){
	
	$.commAjax({
		url : container.rsa,
		success : function(data) {
			if (!data.success) {
				$.dialog({
					content:data.msg,
					title:"alert",
					ok:function(){}
				});
				return;
			}
			var modulus = data.data.modulus;
			var exponent = data.data.exponent;
			var key = RSAUtils.getKeyPair(exponent, "", modulus);
			// 执行登录
			$.commAjax({
				url : container.register,
				type : "post",
				data : {
					sms_code:$("#sms_code").val(),
					login_name : container.login_name,
					login_pwd : RSAUtils.encryptedString(key, container.login_pwd)
				},
				success : function(data2) {

					if (data2.success) {
						$.setCookie("login_name",container.login_name);
						$.dialog({
							content:'注册成功！',
							title:"ok",
							ok:function(){
								//window.location.href=mobileUrl+"/user/login.htm";
								// 执行登录
								$.commAjax({
									url : container.login,
									type : "post",
									data : {
										login_name : container.login_name,
										login_pwd : RSAUtils.encryptedString(key, container.login_pwd)
									},
									success : function(data2) {
										if (data2.success) {
											window.location.href = mobileUrl+"/index.htm";	
										}
									}
						
								});
								
								
							}
						});
					} else {
						$.dialog({
							content:data2.msg,
							title:"alert",
							ok:function(){}
						});
					}
				}

			});
		}
	});
}

$(function() {

	
	$(".text_input").focus(function(){
		$(this).parent().parent().parent().parent().addClass("am-listLogin-item-enter");
	});
	
	$(".text_input").blur(function(){
		$(this).parent().parent().parent().parent().removeClass("am-listLogin-item-enter");
	});

	// 用户名验证
	$("#login_name").blur(function() {
		var login_name = $(this).val();
		var pLi = $(this).parent().parent().parent().parent();
		var ftItem = pLi.find(".ft-item");
		console.info(login_name);
		if(login_name == ""){
			pLi.removeClass("am-listLogin-item-error");
			pLi.find(".am-ft-sm").html("您常用的手机号码");
			container.login_name_bool = false;
			return;
		}
		if(container.commAjaxUser){
			container.commAjaxUser.abort();
		}
		
		container.commAjaxUser = $.commAjax({
			url : container.check,
			type : "post",
			data : {
				mobile : login_name
			},
			success : function(data) {
				if(data.success){
					pLi.find(".am-ft-sm").html("您常用的手机号码");
					pLi.removeClass("am-listLogin-item-error");
					ftItem.html("<i class='type-icon icon-successLogin'></i>");	

					container.login_name_bool = true;
					container.login_name = login_name;
				}else{
					pLi.find(".am-ft-sm").html(data.msg);
					pLi.addClass("am-listLogin-item-error");
					ftItem.html("<i class='type-icon icon-errorLogin'></i>");		
					container.login_name_bool = false;
				}
			}
		});
	});
	
	var pwdMsg = [{name:'弱',level:'level-weak'},{name:'中',level:'level-in'},{name:'强',level:'level-strong'}]
	
	var logPwd = $("#login_pwd")
	var pLi = logPwd.parent().parent().parent().parent()
	var ftItem = pLi.find(".ft-item");
	var logPwd2 = $("#login_pwd2")
	var pLi2 = logPwd2.parent().parent().parent().parent()
	var ftItem2 = pLi2.find(".ft-item");
	logPwd.keyup(function(){
		var login_pwd = logPwd.val();
		if(login_pwd.length >= 6&&login_pwd.length <= 16){
			var lv = -1;
			if(login_pwd.match(/[0-9]/ig)){
				lv++;
			}
			if(login_pwd.match(/[a-zA-z]/ig))
			{
				lv++;
			}
			if(login_pwd.match(/.[^0-9a-zA-z]/ig))
			{
				lv++;	
			}
			ftItem.html("<span class='level "+pwdMsg[lv].level+"'>"+pwdMsg[lv].name+"</span>");
		}else{
			pLi.removeClass("am-listLogin-item-error");
			ftItem.html("");
		}

		
	});
	
	// 密码验证
	logPwd.blur(function(){
		var login_pwd = logPwd.val();
		if(login_pwd == ""){
			pLi.find(".am-ft-sm").html("6~16位字符，至少包含数字、字母、符号。");
			container.login_pwd_bool = false;
			return;
		}
		if(login_pwd.length >= 6&&login_pwd.length <= 16){
			container.login_pwd_bool = true;
			container.login_pwd = login_pwd;
			pLi.removeClass("am-listLogin-item-error");
			pLi.find(".am-ft-sm").html("6~16位字符，至少包含数字、字母、符号。");
		}else{
			container.login_pwd_bool = false;

			pLi.addClass("am-listLogin-item-error");
			pLi.find(".am-ft-sm").html("您输入的密码有误");

			pLi.find(".ft-item").html("<i class='type-icon icon-errorLogin'></i>");
		}
		
		if(logPwd2.val() != ""){
			if(logPwd.val() == logPwd2.val()){
				pLi2.removeClass("am-listLogin-item-error");
				ftItem2.html("<i class='type-icon icon-successLogin'></i>");
				pLi2.find(".am-ft-sm").html("");
				container.login_pwd2_bool = true;
			}else{
				pLi2.addClass("am-listLogin-item-error");
				ftItem2.html("<i class='type-icon icon-errorLogin'></i>");
				pLi2.find(".am-ft-sm").html("您输入的密码有误");
				container.login_pwd2_bool = false;
			}	
		}
		
	});
	
	// 确认密码
	logPwd2.blur(function(){

		if(container.login_pwd){
			if(logPwd.val() == logPwd2.val()){
				pLi2.removeClass("am-listLogin-item-error");
				ftItem2.html("<i class='type-icon icon-successLogin'></i>");
				pLi2.find(".am-ft-sm").html("");
				container.login_pwd2_bool = true;
			}else{
				pLi2.addClass("am-listLogin-item-error");
				ftItem2.html("<i class='type-icon icon-errorLogin'></i>");
				pLi2.find(".am-ft-sm").html("您输入的密码有误");
				container.login_pwd2_bool = false;
			}			
		}
	});
	
	//	是否同意xx服务协议
	$("#login_agree_checkbox").click(function(){
		var bool = $("#login_agree").val();
		
		if(bool == "true"){
			$("#regist").unbind("click");
			$("#regist").addClass("btn-disabled");
			$("#login_agree").val(false);
			$(this).removeClass("ui-input-checkbox-active");
		}else{
			$("#regist").bind("click",regeist_action);
			$("#regist").removeClass("btn-disabled");
			$("#login_agree").val(true);
			$(this).addClass("ui-input-checkbox-active");
		}
	});
	
	// 提交注册，进入下一步验证
	function regeist_action(){
		//验证通过进入下一步同时发送验证码
		if(container.login_name_bool&&container.login_pwd_bool&&container.login_pwd2_bool&&$("#login_agree").val() == "true"){
			
		/*alert($.fx.off);*/
			$("#header").hide();
			$("#register_p1").hide();
			$("#phone_number").html(container.login_name.substr(0,3)+"*****"+container.login_name.substr(8,3));
			$("#register_p2").show();
			sendSms();
				
		}
	}
	
	//发送验证码
	$("#button_lineblack").click(function(){
		$(this).hide();
		$("#button_lineblack_active").show();
		sendSms();
	});
	
	
	
	//输入验证码,完成验证
	$("#regist_button").click(function(){
		registUser();
	});
	
	$("#go_back2").click(function(){
		$("#register_p2").hide();
		$("#header").show();
		$("#register_p1").show();
	});
	
});