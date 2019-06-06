/*还款*/
var container = {},key,dialog;
//获取迷药
container.rsa = basePath + "/sec/rsa?_="+new Date().getTime();
//还款数据准备
container.repayData = basePath + "/financing/repay/data";
//还款本金对应利息的计算
container.repayinterest = basePath + "/financing/repayinterest";
//还款
container.repay = basePath + "/pz/operation/repay";

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
$(function(){
	var contractNo = request("contract_no");
	$("#contract_no").html(contractNo);

	dialog = $.dialog();
	$.commAjax({
		url:container.repayData,
		type:"post",
		data:{
			contract_no:contractNo
		},
		success:function(data){
			if(data.success){
				var contract = data.data.contract;
				$("#loanAmount").html(parseFloat(contract.loanAmount/100).toFixed(2));
				$("#balanceAmount").html(parseFloat(data.data.availableAmount/100).toFixed(2));

				$.commAjax({
					url:container.repayinterest,
					type:"post",
					data:{
						repay_amount:data.data.availableAmount,
						contract_no:contractNo
					},
					success:function(data){
						dialog.close();
						if(data.success){
							var punitive = data.data.penaltyAmount;
							var totalAmount = contract.loanAmount + punitive;
//							$("#punitive").html(parseFloat(punitive/100).toFixed(2));
							$("#totalAmount").html(parseFloat(totalAmount/100).toFixed(2));
							
							$("#repayBtn").click(function(){
								dialog = $.dialog();
								$.commAjax({
									url:container.repay,
									type:"post",
									data:{
										contract_no:contractNo,
										repay_amount:data.data.penaltyAmount+contract.loanAmount,
										trade_pwd:RSAUtils.encryptedString(key, $("#tradepwd").val())
									},
									success:function(data){
										dialog.close();
										if(data.success){
											$.dialog({
												content:"恭喜,还款成功!",
												title:"ok",
												ok:function(){
													window.location.href = mobileUrl + "/user/contracts.htm";
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
							});
						}else{

							dialog.close();
							$.dialog({
								content:data.msg,
								title:"alert",
								ok:function(){}
							});
						}
					}
				});
			}else{
				dialog.close();
				$.dialog({
					content:data.msg,
					title:"alert",
					ok:function(){}
				});
			}
		}
	});
	
});