/*修改用户密码*/
var container = {}, key;

// 秘钥接口地址 get
container.rsa = basePath + "/sec/rsa";
// 密码修改接口
container.loginpwd_reset = basePath + "/user/loginpwd/find";
// 找回登录密码验证码
container.smsCode = basePath + "/sms/loginpwdcode";

// 获取秘钥
$.commAjax({
	url : container.rsa,
	success : function(data) {
		if (!data.success) {
			alert("系统错误");
			return;
		}
		var modulus = data.data.modulus;
		var exponent = data.data.exponent;
		key = RSAUtils.getKeyPair(exponent, "", modulus);

	}
});

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
		$.dialog({
			content:"发送过于频繁请稍后",
			title:"alert",
			ok:function(){}
		})
		clearTimeout(container.setTimeout);
		countdown(countdownSeconds-parseInt(multi/1000));
	}else{
		if($("#mobile").val().length != 11){
			$.dialog({
				content:"请输入正确的手机号码",
				title:"alert",
				ok:function(){}
			});
			return;
		}
		$.commAjax({
			url :container.smsCode,
			type:"post",
			data:{
				mobile:$("#mobile").val()
			},
		
			success:function(data){
				if(data.success){
					$("#button_lineblack").hide();
					$("#button_lineblack_active").show();
					$.setCookie("regist_sendTime", new Date());
					countdown(countdownSeconds);
				}else{
					$.dialog({
						content:data.msg,
						title:"alert",
						ok:function(){}
					});
				}
			}
		});
	}
	
	
}

$(function() {


	var pwdMsg = [ {
		name : '弱',
		level : 'level-weak'
	}, {
		name : '中',
		level : 'level-in'
	}, {
		name : '强',
		level : 'level-strong'
	} ]

	var newPwd = $("#new_pwd");
	var ftItem = newPwd.parent().parent().find(".f-right");
	var newPwd2 = $("#new_pwd2");
	var ftItem2 = newPwd2.parent().parent().find(".f-right");

	newPwd.keyup(function() {
		var new_pwd = newPwd.val();
		var ftItem = newPwd.parent().parent().find(".f-right");
		if (new_pwd.length >= 6 && new_pwd.length <= 16) {
			var lv = -1;
			if (new_pwd.match(/[0-9]/ig)) {
				lv++;
			}
			if (new_pwd.match(/[a-zA-z]/ig)) {
				lv++;
			}
			if (new_pwd.match(/.[^0-9a-zA-z]/ig)) {
				lv++;
			}

			ftItem.html("<span class='level " + pwdMsg[lv].level + "'>"
					+ pwdMsg[lv].name + "</span>");
		} else {
			ftItem.html("");
		}
	});

	newPwd.blur(function() {
		var new_pwd = newPwd.val();
		if (new_pwd == "") {
			ftItem.html("");
			return;
		}
		if (new_pwd.length >= 6 && new_pwd.length <= 16) {
			container.new_pwd_bool = true;
			container.new_pwd = new_pwd;
		} else {
			container.new_pwd_bool = false;
			ftItem.html("<i class='type-icon icon-errorLogin'></i>");
		}

		if (newPwd2.val() != "") {
			if (newPwd.val() == newPwd2.val()) {
				ftItem2.html("<i class='type-icon icon-successLogin'></i>");
				container.new_pwd2_bool = true;
			} else {
				ftItem2.html("<i class='type-icon icon-errorLogin'></i>");
				container.new_pwd2_bool = false;
			}
		}

	});

	newPwd2.blur(function() {
		if (container.new_pwd_bool) {
			if (newPwd.val() == newPwd2.val()) {
				ftItem2.html("<i class='type-icon icon-successLogin'></i>");
				container.new_pwd2_bool = true;
			} else {
				ftItem2.html("<i class='type-icon icon-errorLogin'></i>");
				container.new_pwd2_bool = false;
			}
		}
	});

	$("#findBtn").click(function() {
		if(!container.new_pwd_bool){
			$.dialog({
				content:"新密码格式不正确",
				title:"alert",
				ok:function(){
				}
			});
			return;
		}
		
		if(!container.new_pwd2_bool){
			$.dialog({
				content:"两次密码输入不一致",
				title:"alert",
				ok:function(){
				}
			});
			return;
		}
		if ( container.new_pwd2_bool && container.new_pwd_bool) {
			$.commAjax({
				url : container.loginpwd_reset,
				type : "post",
				data : {
					mobile:$("#mobile").val(),
					check_code: $("#smsCode").val(),
					user_pwd : RSAUtils.encryptedString(key, newPwd.val())
				},
				success : function(data) {
					if(data.success){

						$.dialog({
							content:"密码找回成功",
							title:"ok",
							ok:function(){
								window.location.href = mobileUrl + "/user/login.htm";
							}
						});
					}else{
						$.dialog({
							content:data.msg,
							title:"alert",
							ok:function(){}
						});
					}
				}
			});
		}
	});
	

	//发送验证码
	$("#button_lineblack").click(function(){
		sendSms();
	});

});