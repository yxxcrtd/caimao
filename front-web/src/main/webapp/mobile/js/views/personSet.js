/*修改用户密码*/
var container = {};
// 获取用户信息
container.user = basePath + "/user";
// 获取用户交易密码
container.tradepwd = basePath + "/tradepwd";
// 获取用户交易密码
container.bankcard = basePath + "/bankcard";
// 密保问题
container.userpwdquestion = basePath + "/userpwdquestion";

var pwdMsg = [ {
	name : '弱',
	level : 'level-weak'
}, {
	name : '中',
	level : 'level-in'
}, {
	name : '强',
	level : 'level-strong'
} ];

function parseBankcardNo(bankcardNo) {
	var casecard = "", temp, i = 0;

	while ((temp = bankcardNo.substr(i++*4, 4)) != "") {
		casecard += temp+" ";
	}
	return casecard;
}

$(function() {
	$.commAjax({
				url : container.user,
				success : function(data) {
					if (data.success) {
						var data2 = data.data;
						var userName = data2.userName;
						
						$("#userName").html(data2.mobile.substr(0,3)+"*****" +
						data2.mobile.substr(8,3));
						var isTrust = data2.isTrust;
						if (isTrust == "0") {
							var cUrl;
							if(upload){
								cUrl = "/user/certification3.htm";
							}else{
								cUrl = "/user/certification.htm";
							}
							$("#certification_icon").parent().attr("href",mobileUrl + cUrl);
						
							$("#certification_icon")
									.html(
											"<span class='type-ico am-icon-arrow-right'></span>");
						} else {
							$("#certification_icon").html(
									"<span class='am-ft-gray'>已认证</span>");
						}

						var userPwdStrength = data2.userPwdStrength;
						$("#f_login").append(
								"<span class='level "
										+ pwdMsg[userPwdStrength - 1].level
										+ " am-left-10'>"
										+ pwdMsg[userPwdStrength - 1].name
										+ "</span>");
					} else {
						$.dialog({
							content:data.msg,
							title:"alert",
							ok:function(){}
						});
					}
				}
			});

	$
			.commAjax({
				url : container.bankcard,
				success : function(data) {
					if (data.success) {
						var bankcards = data.data;
						var cardHtml = "";
						if (bankcards.length == 0) {
							cardHtml += "<a href='"
									+ mobileUrl
									+ "/account/bankcard/bind.htm' class='btn-addBank'> "
									+ "<i class='type-icon icon-add'></i> <span class='am-ft-blue'>绑定银行卡</span>"
									+ "</a>";

						} else {
							for ( var i in bankcards) {
								if (bankcards[i].isDefault == 1) {
									cardHtml += "<a href='"
									+ mobileUrl
									+ "/account/bankcard/bind2.htm' class='am-bankInfo bankInfo-"+bankcards[i].bankNo+"'>"
										+ "<p class='name'>"
										+ bankcards[i].bankName
										+ "</p>"
										+ "<p class='number'><span class='am-ft-xl'>"
										+ parseBankcardNo(bankcards[i].bankCardNo)
										+ "</span> <span>"+bankcards[i].bankCardName+"</span></p>"
										+ "<span class='type-ico am-icon-arrow-right'></span>"
										+ "</a>";

								}
							}
						}

						$("#cardInfo").html(cardHtml);
					} else {
						alert(data.msg);
					}
				}
			});

	$.commAjax({
		url : container.tradepwd,
		success : function(data) {
			if (data.success) {

				var userTradePwdStrength = data.data.userTradePwdStrength;
				var fTrade = $("#f_trade");
				if (userTradePwdStrength == null) {
					fTrade.parent().attr("href", mobileUrl + "/user/tradeset.htm");
				} else {

					fTrade.parent().attr("href",
							mobileUrl + "/user/tradepwd.htm");
					fTrade
							.append("<span class='level "
									+ pwdMsg[userTradePwdStrength - 1].level
									+ " am-left-10'>"
									+ pwdMsg[userTradePwdStrength - 1].name
									+ "</span>");
				}
			} else {
				alert(data.msg);
			}
		}
	});
	
	$.commAjax({
		url : container.userpwdquestion,
		success : function(data) {
			if(data.success){
				if(data.data.items == null){
					$("#questionMsg").html("设置");
					$("#tradePwdQuestion").attr("href",mobileUrl+"/user/question.htm");
				}else{
					$("#questionMsg").html("忘记了，找回密保");
					$("#tradePwdQuestion").attr("href",mobileUrl+"/user/findpwdquestion.htm");
				}
			} else {
				alert(data.msg);
			}
		}
	});
})