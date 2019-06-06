/*个人配置展示*/
var container = {};
// 合约列表
container.contracts = basePath + "/financing/contract/page";
// 产品详情
container.proddetail = basePath + "/pz/proddetail";

// 详情集合
var prodInfos = [ {
	contractNum : "contractNum-free"
}, {
	contractNum : "contractNum-competition"
}, {
	contractNum : "contractNum-month"
}, {
	contractNum : "contractNum-day"
} ];

function getProddetail(proddetail,loanAmount,loanTerm,loanRatio){
	for(var i in proddetail){
		if(loanRatio>= proddetail[i].loanRatioFrom && loanRatio < proddetail[i].loanRatioTo&&
				loanAmount >= proddetail[i].loanAmountFrom&&loanAmount < proddetail[i].loanAmountTo&&
				loanTerm >= proddetail[i].loanTermFrom&&loanTerm < proddetail[i].loanTermTo){
			return proddetail[i];
		}
	}
	return null;
}

function createItemHtml(contract) {
	var prodInfo = prodInfos[contract.prodId - 1];

	if (prodInfo.proddetail == null){
		$.commAjax({
			url : container.proddetail,
			async : false,
			data : {
				product_id : contract.prodId
			},
			success:function(data){
				if (data.success) {
					prodInfos[contract.prodId - 1].proddetail = data.data;
					prodInfo.proddetail = data.data;
				} else {
					alert(data.msg);
				}
			}
		});
	}
	var htmlPanel = '';
	var allAmount = parseInt((contract.cashAmount + contract.loanAmount) / 100)
			.toLocaleString();
	htmlPanel += '<a href="infomation-3.html" class="am-ft-main">'
			+ '<div class="contractNum bg-gray con-spacing10 clearfix">'
			+ '<p class="cm-left '
			+ prodInfo.contractNum
			+ '"><mark class="label">'
			+ contract.prodName
			+ '</mark>'
			+ '<b class="am-left-10">'
			+ contract.contractNo
			+ '</b>'
			+ '</p>'
			+ '<p class="cm-right"><span class="am-ft-sm am-ft-gray">开始<em class="bar">|</em>'
			+ contract.contractSignDatetime.substr(0, 10)
			+ '</span></p>'
			+ '</div>'
			+ '<dl class="dl-moneyMain bg-white">'
			+ '<dt>'
			+ '<p><b>'
			+ allAmount
			+ '</b><span class="am-ft-sm">元</span></p>'
			+ '<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">总操盘资产</span></p>'
			+ '</dt>';
	if(contract.prodId = 3 || contract.prodId == 4 ){
		getProddetail(prodInfo.proddetail,contract.loanAmount,contract.loanTerm,contract.loanRatio);
	}

	htmlPanel +=  '<dt>'
			+ '<p><b>35,000</b><span class="am-ft-sm">元</span></p>'
			+ '<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">亏损警告线</span></p>'
			+ '</dt>'
			+ '<dt>'
			+ '<p><b>33,000</b><span class="am-ft-sm">元</span></p>'
			+ '<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">亏损平仓线</span></p>'
			+ '</dt>' + '</dl>' + '</a>';

	return htmlPanel;
}

$(function() {
	$.commAjax({
		url : container.contracts,
		data : {
			start : 0,
			limit : 10
		},
		success : function(data) {
			panelHtml = "";
			if (data.success) {
				var items = data.data.items;
				if (items == null || items.length == 0) {
					$("#noPzPanel").show();
				} else {
					for ( var i in items) {
						panelHtml += createItemHtml(items[i]);
					}
				}
				$("#content").html(panelHtml);
			} else {
				alert(data.msg);
			}
		}
	})
});