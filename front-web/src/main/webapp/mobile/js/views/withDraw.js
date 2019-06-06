//取现JS
function parseBankcardNo(bankcardNo) {
	var casecard = "", temp, i = 0;

	while ((temp = bankcardNo.substr(i++*4, 4)) != "") {
		casecard += temp+" ";
	}
	return casecard;
}

$(function() {
	var container = {};
	container.rsa = basePath + "/sec/rsa";
	//获取帐户信息
	
	container.accountUrl = basePath + "/account";
	//	获取存活银行卡信息
	container.bankUrl =basePath + "/bankcard";
	function dialog(msg){
		$.dialog({
			 content:msg,
			 title:"alert",
			 ok:function(){}
		 });
	}
	
	//取得个人帐户余额和银行卡信息
	$.commAjax({
		url : container.accountUrl,
		type : "get",
		data : {
		},
		success : function(data2) {

			if (data2.success) {
				// 请求成功更新页面信息
				container.account = data2.data;	
				container.avalaibleAmount=(data2.data.avalaibleAmount-data2.data.freezeAmount)/100||0;
				$('#avalaibleAmount').html(container.avalaibleAmount);						
				

				//bankNumber
				$.commAjax({
					url : container.bankUrl,
					type : "get",
					data : {
					},
					success : function(data2) {
						
					
						
						if (data2.success) {
							// 请求成功更新页面信息
							var bankCardInfoHtml = ""; 
							if(data2.data.length>0){

								var bankcards  = data2.data;
								for(var i in bankcards){
									if(bankcards[i].isDefault == 1){
										bankCardInfoHtml +=  "<a href='"
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
								
								
							
							}else{
								var msgHtml = '<span class="am-ft-tabText">暂未绑定银行卡，不能取现。</span><a href="'+
									mobileUrl+'/account/bankcard/bind.htm">去绑卡</a>'
									
								bankCardInfoHtml += '<ul class="datalist">' +
									'<li class="datalist-item clearfix">' +
									'<div class="am-grid datalist-line">' +
									        '<div class="am-grid-item am-grid-item-25  am-ft-right">' +
									        '<h4 class="am-ft-md cm-left">银行卡</h4>' +
									        '</div>' +
									        '<div class="am-grid-item am-grid-item-75 am-ft-right am-right-15">' +
									        '<span class="am-ft-orange">暂未绑卡</span>' +
									        '</div>' +
									'</div>'+
									'</li>'+
									'</ul>';
								
								$("#bankCardMsg").html(msgHtml);

								$("#bankCardMsg").show();
							}	
							
							$("#bankCardInfo").html(bankCardInfoHtml);
							
						} else {
							// 请 求不成功，跳转错误页面
							dialog(data2.msg);
						}
					}

				});
				
				
			} else {
				$.dialog({
					content:"请先实名认证!",
					title:"alert",
					ok:function(){
						window.location.href= mobileUrl + "/user/certification.htm";
					}
				});
			}
		}

	});
/*	$('#withdraw_amount').blur(function () {
		var value = $('#withdraw_amount').val();
		if(value<0){
			$.dialog({
				 content:"请确保输入金额正确!",
				 title:"alert",
				 ok:function(){
					 $('#withdraw_amount').focus();
				 }
			 });
			return;
		}
		$('#withdraw_amount').val(parseFloat(value).toFixed(4));
	});*/
	//取款下一步
	$("#addBtn1").click(
			function() {				
				//判断输入 
				var $withdraw_amount = $('#withdraw_amount');
				container.withdraw_amount =$.parseMoney($withdraw_amount.val())||0; 
				if(container.withdraw_amount <= 100||container.withdraw_amount>container.avalaibleAmount){
					dialog("取款金额必须大于100元，小于"+container.avalaibleAmount+"元!");
					$('#withdraw_amount').focus();
					return;
				}
				var tradePwd = $('#trade_pwd').val();
				if(tradePwd=="") {
					$.dialog({
						 content:"请输入安全密码!",
						 title:"alert",
						 ok:function(){
							 $('#trade_pwd').focus();
						 }
					 });					
					
					return;
				}
				
				//取得加密密钥
				$.commAjax({
					url : container.rsa,
					success : function(data) {
						
						if (!data.success) {
							dialog("系统错误");
							return;
						}
						var modulus = data.data.modulus;
						
						
						var exponent = data.data.exponent;
					
						var key = RSAUtils.getKeyPair(exponent, "", modulus);
						
								// 执行借款
						$.commAjax({
							url : basePath +'/account/withdraw',
							type : "post",
							data : {
								'withdraw_amount':parseInt(container.withdraw_amount*100),
		                		//三方支付公司编号
		                		'trade_pwd':RSAUtils.encryptedString(key, tradePwd)
							},
							success : function(data2) {
								if (data2.success) {
									// 取款成功
									
									$.dialog({
										 content:"取现申请已经提交成功，请等待后台审批 。",
										 title:"ok",
										 ok:function(){
											 window.location.href=mobileUrl+"/user/index.htm";
										 }
									 });	
									
								} else {
									// 取款失败
									dialog(data2.msg);
									return;
								}
							}
		
						});	
					}
				});
			});
	//添加银行卡
});