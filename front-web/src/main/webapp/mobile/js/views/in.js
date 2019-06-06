/*合约追加*/
var container = {}
// 追宝接口地址
container.inUrl =  basePath +"/pz/operation/in";

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

//homs_combine_id
//homs_fund_account
container.prodName = decodeURI(request("prodName"));
container.operatorNo = request("operatorNo");
container.contractNo = request("contractNo");
container.homsCombineId = request("homsCombineId");
container.homsFundAccount = request("homsFundAccount");

function addAction(){
	$.commAjax({
		url:container.inUrl,
		type:"post",
		data:{
			contract_no:container.contractNo,
			homs_combine_id:container.homsCombineId,
			homs_fund_account:container.homsFundAccount,
			trans_amount:parseInt($.parseMoney($("#inMoney").val())*100)
		},
		success:function(data){
			if(data.success){
				$.dialog({
					content:"追加成功",
					title:"ok",
					ok:function(){
						history.go(-1);
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

$(function(){
	$("#homsInfo").val(container.operatorNo +" "+ container.prodName);
	
	$("#inBtn").click(function(){
		$.dialog({
			content:"确定要追加吗?",
			title:"alert",
			ok:function(){
				setTimeout(addAction,0);
			},
			cancel:function(){}
		});
	
	});
});