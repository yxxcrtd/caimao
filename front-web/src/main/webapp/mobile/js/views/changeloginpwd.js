/*修改用户密码*/
var container = {};

//秘钥接口地址 get
container.rsa = basePath + "/sec/rsa";
//密码修改接口
container.loginpwd_reset= basePath + "/user/loginpwd/reset";
//获取用户信息
container.user= basePath + "/user";

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


$(function(){
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
				$("#userName").html(data.data.mobile.substr(0,3)+"*****" +
						data.data.mobile.substr(8,3));
			}
		}
	});
	
	$("#reset_submit").click(function(){
		
		if(!container.old_pwd_bool){
			$.dialog({
				content:"旧密码格式不正确",
				title:"alert",
				ok:function(){
				}
			});
			return;
		}
		
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
		
		if(container.old_pwd_bool&&container.new_pwd_bool&&container.new_pwd2_bool){
			$.commAjax({
				url:container.loginpwd_reset,
				type:"post",
				data:{
					oldPwd:RSAUtils.encryptedString(key,oldPwd.val()),
					newPwd:RSAUtils.encryptedString(key,newPwd.val())
				},
				success:function(data){
					if(data.success){
						$.dialog({
							content:"修改成功！",
							title:"ok",
							ok:function(){
								window.location.href= mobileUrl + "/user/login.htm";
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
	
	var oldPwd = $("#old_pwd");
	var oldFtItem = oldPwd.parent().parent().find(".f-right");
	oldPwd.blur(function(){
		var old_pwd = oldPwd.val();
		if(old_pwd == ""){
			oldFtItem.html("");
			return;
		}
		if(old_pwd.length >= 6&&old_pwd.length <= 16){
			container.old_pwd_bool = true;
			container.old_pwd = old_pwd;
			oldFtItem.html("");
		}else{
			container.newPwd_bool = false;
			oldFtItem.html("<i class='type-icon icon-errorLogin'></i>");

		
		}
	});
	

	var pwdMsg = [{name:'弱',level:'level-weak'},{name:'中',level:'level-in'},{name:'强',level:'level-strong'}]


	
	var newPwd = $("#new_pwd");
	var ftItem = newPwd.parent().parent().find(".f-right");
	var newPwd2 = $("#new_pwd2");
	var ftItem2 = newPwd2.parent().parent().find(".f-right");
	
	newPwd.keyup(function(){
		var new_pwd = newPwd.val();
		var ftItem = newPwd.parent().parent().find(".f-right");
		if(new_pwd.length >= 6&&new_pwd.length <= 16){
			var lv = -1;
			if(new_pwd.match(/[0-9]/ig)){
				lv++;
			}
			if(new_pwd.match(/[a-zA-z]/ig))
			{
				lv++;
			}
			if(new_pwd.match(/.[^0-9a-zA-z]/ig))
			{
				lv++;	
			}
			
			ftItem.html("<span class='level "+pwdMsg[lv].level+"'>"+pwdMsg[lv].name+"</span>");
		}else{
			ftItem.html("");
		}
	});
	
	newPwd.blur(function(){
		var new_pwd = newPwd.val();
		if(new_pwd == ""){
			ftItem.html("");
			return;
		}
		if(new_pwd.length >= 6&&new_pwd.length <= 16){
			container.new_pwd_bool = true;
			container.new_pwd = new_pwd;
		}else{
			container.newPwd_bool = false;
			ftItem.html("<i class='type-icon icon-errorLogin'></i>");

			return;
		}
		
		if(newPwd2.val() != ""){
			if(newPwd.val() == newPwd2.val()){
				ftItem2.html("<i class='type-icon icon-successLogin'></i>");
				container.new_pwd2_bool = true;
			}else{
				ftItem2.html("<i class='type-icon icon-errorLogin'></i>");
				container.new_pwd2_bool = false;
			}	
		}
		
	});
	
	newPwd2.blur(function(){
		if(container.new_pwd_bool){
			if(newPwd.val() == newPwd2.val()){
				ftItem2.html("<i class='type-icon icon-successLogin'></i>");
				container.new_pwd2_bool = true;
			}else{
				ftItem2.html("<i class='type-icon icon-errorLogin'></i>");
				container.new_pwd2_bool = false;
		
			}			
		}
	});
});