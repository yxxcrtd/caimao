//函数

function parseBankcardNo(bankcardNo) {
	var casecard = "", temp, i = 0;

	while ((temp = bankcardNo.substr(i++*4, 4)) != "") {
		casecard += temp+" ";
	}
	return casecard;
}

$(function() {
	var container = {};	
	// URL地址
	container.bankcardUrl = basePath + "/bankcard";
	container.accountUrl = basePath + "/account";
	container.userUrl = basePath + "/user";
	container.statisticUrl  = basePath + "/financing/statistic";
	container.logoutUrl = basePath + "/user/logout.html?jump=0";
	// 请求帐户信息
	$.commAjax({
		url : container.userUrl,
		type : "get",
		data : {

		},
		success : function(data2) {

			if (data2.success) {
				// 请求成功更新页面信息
				container.user = data2.data;
				$('#mobile').html(container.user.mobile);

			} else {
				// 请 求不成功，跳转错误页面
				$.dialog({
					content:data2.msg,
					title:"alert",
					ok:function(){}
				});
			}
		}

	});
	
	$.commAjax({
		url:container.statisticUrl,
		success : function(data2) {
		
			if (data2.success) {
				// 请求成功更新页面信息
				var account = data2.data;
				$('#loanAmount').html(parseFloat(account.totalLoanAmount/100).toLocaleString());
				$('#loanAmount2').html(parseFloat(account.totalCashAmount/100).toLocaleString());
				
			} else {
				// 请 求不成功，跳转错误页面

				$('#loanAmount').html(0);
				$('#loanAmount2').html(0);
			}
		}
	});
	//请求帐户信息
	$.commAjax({
		url : container.accountUrl,
		type : "get",
		data : {

		},
		success : function(data2) {

			if (data2.success) {
				// 请求成功更新页面信息
				container.account = data2.data;
				//$('#avalaibleAmount').html((container.account.avalaibleAmount-container.account.freezeAmount)/100);
				$('#avalaibleAmount').html(parseFloat(container.account.avalaibleAmount/100).toLocaleString());
				$('#freezeAmount').html(parseFloat(container.account.freezeAmount/100).toLocaleString());
				
			} else {
				// 请 求不成功，跳转错误页面
				$('#avalaibleAmount').html(0);
				$('#freezeAmount').html(0);
			}
		}

	});
	//请求银行卡信息
	$.commAjax({
		url : container.bankcardUrl,
		type : "get",
		data : {

		},
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
				$.dialog({
					content:data2.msg,
					title:"alert",
					ok:function(){}
				});
			}
		}

	});
	
	$("#logout").click(function(){
		$.dialog({
			content:"确定退出吗",
			title:"alert",
			ok:function(){
				$.commAjax({
					url:container.logoutUrl,
					type:"get",
					success:function(data){
                        if (document.cookie.indexOf('caimao_pz') != -1) {
                            window.parent.postMessage("caimaoWindow", "/");
                        } else {
                            window.location.href = basePath +"/mobile/user/login.htm";
                        }
					}
				});
			},
			cancel:function(){}
		});
	});
});