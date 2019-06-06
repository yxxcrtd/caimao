/*修改用户密码*/
var container = {}, key;

// 秘钥接口地址 get
container.rsa = basePath + "/sec/rsa";
// 密码修改接口
container.loginpwd_reset = basePath + "/user/tradepwd/reset";
// 获取用户信息
container.user = basePath + "/user";

// 获取秘钥
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

$(function() {
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
			} else {	
				$("#userName").html(data.data.mobile.substr(0,3)+"*****" +
						data.data.mobile.substr(8,3));
			}
		}
	});

	var oldTradePwd = $("#old_trade_pwd");
	var oldFtItem = oldTradePwd.parent().parent().find(".f-right");
	oldTradePwd.blur(function() {
		var old_trade_pwd = oldTradePwd.val();
		if (old_trade_pwd == "") {
			oldFtItem.html("");
			return;
		}
		if (old_trade_pwd.length >= 6 && old_trade_pwd.length <= 16) {
			container.old_trade_pwd_bool = true;
			container.old_trade_pwd = old_trade_pwd;
			oldFtItem.html("");
		} else {
			container.old_trade_pwd_bool = false;
			oldFtItem.html("<i class='type-icon icon-errorLogin'></i>");
		}
	});

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

	var newTradePwd = $("#new_trade_pwd");
	var ftItem = newTradePwd.parent().parent().find(".f-right");
	var newTradePwd2 = $("#new_trade_pwd2");
	var ftItem2 = newTradePwd2.parent().parent().find(".f-right");

	newTradePwd.keyup(function() {
		var new_trade_pwd = newTradePwd.val();
		var ftItem = newTradePwd.parent().parent().find(".f-right");
		if (new_trade_pwd.length >= 6 && new_trade_pwd.length <= 16) {
			var lv = -1;
			if (new_trade_pwd.match(/[0-9]/ig)) {
				lv++;
			}
			if (new_trade_pwd.match(/[a-zA-z]/ig)) {
				lv++;
			}
			if (new_trade_pwd.match(/.[^0-9a-zA-z]/ig)) {
				lv++;
			}

			ftItem.html("<span class='level " + pwdMsg[lv].level + "'>"
					+ pwdMsg[lv].name + "</span>");
		} else {
			ftItem.html("");
		}
	});

	newTradePwd.blur(function() {
		var new_trade_pwd = newTradePwd.val();
		if (new_trade_pwd == "") {
			ftItem.html("");
			return;
		}
		if (new_trade_pwd.length >= 6 && new_trade_pwd.length <= 16) {
			container.new_trade_pwd_bool = true;
			container.new_trade_pwd = new_trade_pwd;
		} else {
			container.new_trade_pwd_bool = false;
			ftItem.html("<i class='type-icon icon-errorLogin'></i>");
		
		}

		if (newTradePwd2.val() != "") {
			if (newTradePwd.val() == newTradePwd2.val()) {
				ftItem2.html("<i class='type-icon icon-successLogin'></i>");
				container.new_trade_pwd2_bool = true;
			} else {
				ftItem2.html("<i class='type-icon icon-errorLogin'></i>");
				container.new_trade_pwd2_bool = false;
			
			}
		}

	});

	newTradePwd2.blur(function() {
		if (container.new_trade_pwd_bool) {
			if (newTradePwd.val() == newTradePwd2.val()) {
				ftItem2.html("<i class='type-icon icon-successLogin'></i>");
				container.new_trade_pwd2_bool = true;
			} else {
				ftItem2.html("<i class='type-icon icon-errorLogin'></i>");
				container.new_trade_pwd2_bool = false;
			}
		}
	});

	$("#reset_submit").click(function() {
		
		if(!container.old_trade_pwd_bool){
			$.dialog({
				content:"旧密码格式不正确",
				title:"alert",
				ok:function(){
				}
			});
			return;
		}
		
		if(!container.new_trade_pwd_bool){
			$.dialog({
				content:"新密码格式不正确",
				title:"alert",
				ok:function(){
				}
			});
			return;
		}
		
		if(!container.new_trade_pwd2_bool){
			$.dialog({
				content:"两次密码输入不一致",
				title:"alert",
				ok:function(){
				}
			});
			return;
		}
		
		
		if (container.old_trade_pwd_bool && container.new_trade_pwd2_bool && container.new_trade_pwd_bool) {
			$.commAjax({
				url : container.loginpwd_reset,
				type : "post",
				data : {
					old_trade_pwd : RSAUtils.encryptedString(key, oldTradePwd.val()),
					new_trade_pwd : RSAUtils.encryptedString(key, newTradePwd.val())
				},
				success : function(data) {
					if(data.success){
						$.dialog({
							content:"密码修改成功",
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