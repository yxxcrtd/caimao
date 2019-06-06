/*用户登录页面*/
var container = {}, key;

// 秘钥接口地址 get
container.rsa = basePath + "/sec/rsa?_="+new Date().getTime();
// 登录接口地址 post
container.login = basePath + "/user/login";
// 跳转主页地址
container.index = mobileUrl + "/index.htm";

/*$(document).click(function(event) {
var evEL = $(event.srcElement);
console.info(evEL.attr("id"));
while (true) {
	if (evEL.attr("id") == "login_pwd") {

		alert(1);
		break;
	} else if ($("#login_pwd", evEL).attr("id")) {
		alert(2);
		break;
	}
	evEL = evEL.parent();
}

});*/

function request(paras) {
	var url = location.href;
	var paramStr = url.substring(url.indexOf('?') + 1, url.length).split('&');
	var j;
	var paramObj = {};
	for (var i = 0; j = paramStr[i]; i++) {
		paramObj[j.substring(0, j.indexOf('=')).toLowerCase()] = j.substring(j
				.indexOf('=') + 1, j.length);
	}

	var returnValue = paramObj[paras.toLowerCase()];

	if (typeof (returnValue) == "undefined") {
		return "";
	} else {
		return returnValue;
	}
}

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

container.am_grid_item_active = 3;

$(function() {

	$("#login_name").val($.getCookie("login_name"));
	
	
	
	container.login_action = function() {
		var $this = $("#login_button");
		$this.unbind("click");
		var login_name = $("#login_name").val();
		if (login_name == "") {
			 $.dialog({
				 content:"用户名不能为空!",
				 title:"alert",
				 ok:function(){}
			 });
			$this.click(function() {
				container.login_action();
			});
			return;
		}
		var login_pwd = $("#login_pwd").val();
		if (login_pwd == "") {
			 $.dialog({
				 content:"密码不能为空!",
				 title:"alert",
				 ok:function(){}
			 });
			$this.click(function() {
				container.login_action();
			});
			return;
		}

		 var dialog = $.dialog();
		// 执行登录
		$.commAjax({
			url : container.login,
			type : "post",
			data : {
				login_name : login_name,
				login_pwd : RSAUtils.encryptedString(key, login_pwd)
			},
			success : function(data2) {

				if (data2.success) {
					$.setCookie("login_name",login_name);
					var tg = request("tg");
					if(tg == ""){
						window.location.href = container.index;	
						
					}else{
						window.location.href = tg;		
					}
				} else {
					dialog.close();
					 $.dialog({
						 content:data2.msg,
						 title:"alert",
						 ok:function(){}
					 });
					$this.click(function() {
						container.login_action();
					});
				}
			}

		});

	}

  	document.onkeydown=function(event){
	    var e = event || window.event || arguments.callee.caller.arguments[0];
	          
	    if(e && e.keyCode==13){ // enter 键
			container.login_action();
	    }
	}; 
	
	$("#login_button").click(function() {
		container.login_action();
	});

});