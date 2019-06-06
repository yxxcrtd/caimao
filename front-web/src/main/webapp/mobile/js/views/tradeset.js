/*设置安全密码*/
var container = {},key;

//秘钥接口地址 get
container.rsa = basePath + "/sec/rsa?_="+new Date().getTime();
//交易密码设置
container.tradepwdSet = basePath +"/user/tradepwd/set";
//短信验证码发送
container.smsCode =  basePath +"/sms/code";
//获取用户信息
container.user= basePath + "/user?_="+new Date().getTime();

//获取秘钥
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
		key = RSAUtils.getKeyPair(exponent, "", modulus);

	}
});

//验证码倒计时
function countdown(sec){
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
		$.commAjax({
			url :container.smsCode,
			type:"post",
			data:{
				biz_type:3
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

$(function(){
	var mobile;
	$.commAjax({
		url : container.user,
		success : function(data) {
			if (!data.success) {
				$.dialog({
					content:data.msg,
					title:"alert",
					ok:function(){}
				});
				return;
			}else{
				mobile = data.data.mobile;
				$("#mobile").text(mobile);
			}
		}
	});
	
	
	//发送验证码
	$("#button_lineblack").click(function(){
		$(this).hide();
		$("#button_lineblack_active").show();
		sendSms();
	});
	
	var pwdMsg = [{name:'弱',level:'level-weak'},{name:'中',level:'level-in'},{name:'强',level:'level-strong'}]
	
	var tradePwd = $("#trade_Pwd");
	var ftItem = tradePwd.parent().parent().find(".f-right");
	var tradePwd2 = $("#trade_Pwd2");
	var ftItem2 = tradePwd2.parent().parent().find(".f-right");
	
	tradePwd.keyup(function(){
		var trade_pwd = tradePwd.val();
		var ftItem = tradePwd.parent().parent().find(".f-right");
		if(trade_pwd.length >= 6&&trade_pwd.length <= 16){
			var lv = -1;
			if(trade_pwd.match(/[0-9]/ig)){
				lv++;
			}
			if(trade_pwd.match(/[a-zA-z]/ig))
			{
				lv++;
			}
			if(trade_pwd.match(/.[^0-9a-zA-z]/ig))
			{
				lv++;	
			}
			
			ftItem.html("<span class='level "+pwdMsg[lv].level+"'>"+pwdMsg[lv].name+"</span>");
		}else{
			ftItem.html("");
		}
	});
	
	tradePwd.blur(function(){
		var trade_pwd = tradePwd.val();
		if(trade_pwd == ""){
			ftItem.html("");
			return;
		}
		if(trade_pwd.length >= 6&&trade_pwd.length <= 16){
			container.trade_pwd_bool = true;
			container.trade_pwd = trade_pwd;
		}else{
			container.tradePwd_bool = false;
			ftItem.html("<i class='type-icon icon-errorLogin'></i>");
	
			return;
		}
		
		if(tradePwd2.val() != ""){
			if(tradePwd.val() == tradePwd2.val()){
				ftItem2.html("<i class='type-icon icon-successLogin'></i>");
				container.trade_pwd2_bool = true;
			}else{
				ftItem2.html("<i class='type-icon icon-errorLogin'></i>");
				container.trade_pwd2_bool = false;
		
			}	
		}
		
	});
	
	tradePwd2.blur(function(){
		if(container.trade_pwd_bool){
			if(tradePwd.val() == tradePwd2.val()){
				ftItem2.html("<i class='type-icon icon-successLogin'></i>");
				container.trade_pwd2_bool = true;
			}else{
				ftItem2.html("<i class='type-icon icon-errorLogin'></i>");
				container.trade_pwd2_bool = false;
	
			}			
		}
	});
	

	
	$("#submit_btn").click(function(){
		if(!container.trade_pwd_bool){
			$.dialog({
				content:"密码格式不正确",
				title:"alert",
				ok:function(){
				}
			});
			return;
		}
		
		if(!container.trade_pwd2_bool){
			$.dialog({
				content:"两次密码输入不一致",
				title:"alert",
				ok:function(){
				}
			});
			return;
		}
		
		
		
		if(container.trade_pwd_bool&&container.trade_pwd2_bool){
			var dialog = $.dialog();
			$.commAjax({
				url:container.tradepwdSet,
				type:"post",
				data:{
					mobile:mobile,
					trade_pwd:RSAUtils.encryptedString(key,tradePwd.val()),
					code:$("#code").val()
				},
				success:function(data){
					dialog.close();
					if(data.success){
						$.dialog({
							content:"密码设置成功",
							title:"ok",
							ok:function(){
								window.history.go(-1);
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
});