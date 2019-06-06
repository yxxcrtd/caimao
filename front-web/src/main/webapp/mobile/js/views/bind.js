/*银行卡绑定*/
var container = {},bankNo = "";
// 获取用户信息
container.user = basePath + "/user";
// 获取可绑银行开
container.bankcard = basePath + "/bank?pay_company_no=3";
// 银行卡绑定
container.bankcard_bind = basePath + "/user/bankcard/bind";

function createCardHtml(bankNo, bankName) {
	return '<li class="datalist-item clearfix bankCard">'
			+ '<a class="am-grid datalist-line">'
			+ '<div class="am-grid-item am-grid-item-50  am-ft-left">'
			+ '<input class="bankNo" type="hidden" value="'
			+ bankNo
			+ '" />'
			+ '<h4 class="am-ft-md cm-left am-ft-main">'
			+ bankName
			+ '</h4>'
			+ '</div>'
			+ '<div class="am-grid-item am-grid-item-50 am-ft-right am-right-15">'
			+ '<span class="type-ico am-icon-arrow-right"></span>' + '</div>'
			+ '</a>' + '</li>';
}

//绑定时间
function bindCardAction(){
	var dialog = $.dialog();
	$.commAjax({
		url : container.bankcard_bind,
		type : "post",
		data : {
			bank_no:bankNo,
			bank_card_no : $("#bankcard").val()
		},
		success : function(data) {
			dialog.close();
			if (data.success) {
				$.dialog({
					content:"绑卡成功！",
					title:"ok",
					ok:function(){
						window.history.go(-1);
					}
				});
			}else{
				dialog.close();
				$.dialog({
					content:data.msg,
					title:"alert",
					ok:function(){
					}
				});
			}
		}
	});
}


$(function() {
	$.commAjax({
		url : container.user,
		success : function(data) {
			if (data.success) {
				var data2 = data.data;
				var userRealName = data2.userRealName;
				container.idcard = data2.idcard;
				var idcard = container.idcard.substr(0, 2) + "**************"
						+ container.idcard.substr(16, 2);
				$("#userInfo").html(userRealName + " " + idcard);

			} else {
				alert(data.msg)
			}

		}
	});

	var bankcard = $("#bankcard");
	bankcard.blur(function() {
		if (bankcard.val() == "") {
			$("#f_bankCard").html("");
			container.bankcard_bool = false;
		} else if ($.luhmCheck(bankcard.val())) {
			$("#f_bankCard")
					.html("<i class='type-icon icon-successLogin'></i>");
			container.bankcard_bool = true;
		} else {
			$("#f_bankCard").html("<i class='type-icon icon-errorLogin'></i>");
			container.bankcard_bool = false;
		}
	});

	$("#selectCard").click(function() {
		$("#panel1").hide();
		$("#panel2").show();
	});

	$.commAjax({
		url : container.bankcard,
		success : function(data) {
			if (data.success) {
				var banks = data.data, bankHtml = "";

				for ( var i in banks) {
					bankHtml += createCardHtml(banks[i].bankNo,
							banks[i].bankName);
				}
				$("#panel2").html(bankHtml);
				
				$(".bankCard").click(function(){
					$("#panel2").hide();
					$("#panel1").show();
					bankNo = $(this).find("input").eq(0).val();
					$("#bankCardType").removeClass("am-ft-gray");
					$("#bankCardType").html($(this).find("h4").text());
				});
			}
		}
	});

	/*
	 * public String doBindBankcard(@RequestParam("bank_no") String bankNo,
	 * @RequestParam("bank_code") String bankCode, @RequestParam("bank_name")
	 * String bankName, @RequestParam("bank_card_no") String bankCardNo,
	 * @RequestParam("is_default") String isDefault) {
	 * memberAO.doBindBankCard(getSessionUser().getUser_id(), bankNo, bankCode,
	 * bankName, bankCardNo, true); return null;
	 *  }
	 */

	$("#submit").click(function() {
		if (bankNo == ""){
			$.dialog({
				content:"请选择银行卡类型",
				title:"alert",
				ok:function(){}
			});
			return; 
		} 

		if(!container.bankcard_bool) {
			$.dialog({
				content:"请输入正确的银行卡号",
				title:"alert",
				ok:function(){}
			});
			return;
		}
		$.dialog({
			content:"确定要绑定吗?",
			title:"alert",
			ok:function(){
				setTimeout(bindCardAction,0);
			},
			cancel:function(){}
		});
		
	});
});
