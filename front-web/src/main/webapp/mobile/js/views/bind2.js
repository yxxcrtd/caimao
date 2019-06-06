/*修改用户密码*/
var container = {};
// 获取用户交易密码
container.bankcard = basePath + "/bankcard";

function parseBankcardNo(bankcardNo) {
	var casecard = "", temp, i = 0;

	while ((temp = bankcardNo.substr(i++*4, 4)) != "") {
		casecard += temp+" ";
	}
	return casecard;
}


$(function() {
	$.commAjax({
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
							cardHtml += "<a  class='am-bankInfo bankInfo-"+bankcards[i].bankNo+"'>"
									+ "<p class='name'>"
									+ bankcards[i].bankName
									+ "</p>"
									+ "<p class='number'><span class='am-ft-xl'>"
									+ parseBankcardNo(bankcards[i].bankCardNo)
									+ "</span> <span>"
									+ bankcards[i].bankCardName
									+ "</span></p>"
									+ "</a>";

						}
					}
				}

				$("#cardInfo").html(cardHtml);
			} else {
				$.dialog({
					content:data.msg,
					title:"alert",
					ok:function(){}
				})
			}
		}
	});
})