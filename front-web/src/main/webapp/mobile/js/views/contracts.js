/*个人配置展示*/
var container = {},
    type=0,
    proFreeId= 1,
    proDayId = 4,
    proMonthId = 3,
    proContestId = 1;

// 查询操盘中合约列表
container.contracts = basePath + "/financing/contract/page";
// 查询操作结束合约列表
container.historys = basePath + "/financing/hiscontract/page";
// 查询申请中合约列表
container.loanapplys = basePath + "/financing/loanapply/page";
// 子账号合约列表
container.richhbi =  basePath + "/home/richhbi";
// 产品详情
container.proddetail = basePath + "/pz/proddetail";
// 产品
container.product = basePath + "/pz/product";

var page,pageSize = 10,commAjax,totalCount,products = {};


var dialog,contractList = {},HomsCombineIds = {};
// 详情集合
var prodInfos = [
		{
			id:proFreeId,
			contractNum : "contractNum-free"
		},
		{
			id:proDayId,
			contractNum : "contractNum-day"
		},
		{
			id:proMonthId,
			contractNum : "contractNum-month"
		},
		{
			id:proContestId,
			contractNum : "contractNum-competition"
		}
];

/*function getProddetail(proddetail, loanAmount, loanTerm, loanRatio) {
	console.info(loanAmount);
	console.info(loanTerm);
	console.info(loanRatio);
	for ( var i in proddetail) {
		if (loanRatio >= proddetail[i].loanRatioFrom
				&& loanRatio <= proddetail[i].loanRatioTo
				&& loanAmount >= proddetail[i].loanAmountFrom
				&& loanAmount <= proddetail[i].loanAmountTo
				&& loanTerm >= proddetail[i].loanTermFrom
				&& loanTerm < proddetail[i].loanTermTo) {
			return proddetail[i];
		}
	}
	return null;
}*/

function linkIn(prodName, contractNo, operatorNo,homsCombineId,homsFundAccount){
	event.stopPropagation();
	window.location.href= mobileUrl +'/trade/in.htm?prodName='+prodName+
	'&operatorNo='+operatorNo+
	'&contractNo='+contractNo+
	'&homsCombineId='+homsCombineId+
	'&homsFundAccount='+homsFundAccount;
}

function linkOut(prodName,operatorNo,homsCombineId,homsFundAccount){
	event.stopPropagation();
	window.location.href= mobileUrl +'/trade/out.htm?prodName='+prodName+
	'&operatorNo='+operatorNo+
	'&homsCombineId='+homsCombineId+
	'&homsFundAccount='+homsFundAccount;
}

//从homs中获取合约号信息
//push
function createRichhbiHtml(richhbi,i){
	
	var prodId = contractList[i][0].prodId;
	var prodInfo;
	for(var j in prodInfos){
		var temp = prodInfos[j]
		if(temp.id == prodId){
			prodInfo = temp;
		}
	}
	if(!prodInfo){
		return "";
	}
	var HomsAssetsInfo = richhbi.HomsAssetsInfo;
	var contractDetail = richhbi.contractList[0];
	var loanAmount = HomsAssetsInfo.loanAmount,
	enableRatio = HomsAssetsInfo.enableRatio,
	exposureRatio= HomsAssetsInfo.exposureRatio;
 
	var prodName = richhbi.HomsCombineId.prodName;
	var contractNo = richhbi.HomsCombineId.contractNo;
	var homsCombineId = richhbi.HomsCombineId.homsCombineId;
	var homsFundAccount = richhbi.HomsCombineId.homsFundAccount;
	var operatorNo = richhbi.HomsCombineId.operatorNo;
	var html = '<a onclick="contracList('+i+',this);" class="am-ft-main">'+
	'<div class="contractNum bg-gray con-spacing10 clearfix">'+
	'<p class="cm-left '+prodInfo.contractNum+'"><mark class="label">'+
	prodName +'</mark>'+
	'</p>'+
	'<p class="cm-right"><span class="type-ico am-icon-arrow-right"></span>';
	if(prodId == proMonthId||prodId == proDayId){
		html  += '<span onclick=\'linkIn("'+
		prodName+'","'+
		contractNo+'","'+
		operatorNo+'","'+
		homsCombineId+'","'+
		homsFundAccount+
		'")\' class="am-button btn1 am-button-lineRed">追保</span>';
		//'<span onclick=\'linkOut("'+
		//prodName+'","'+
		//operatorNo+'","'+
		//homsCombineId+'","'+
		//homsFundAccount+
		//'")\' class="am-button btn2 am-button-lineRed">提盈</span>';
	}
	
	var totalAsset = parseFloat(HomsAssetsInfo.totalAsset/100).toFixed(2);
	var totalMarketValue  = parseFloat(HomsAssetsInfo.totalMarketValue/100).toFixed(2);
	var curAmount  = parseFloat(HomsAssetsInfo.curAmount/100).toFixed(2);
	var fee  = parseFloat(contractDetail.fee/100).toFixed(2);
	var enableRatioValue = parseFloat(loanAmount*enableRatio/100).toFixed(2);
	var exposureRatioValue = parseFloat(loanAmount*exposureRatio/100).toFixed(2);
	
	
	if(prodId == proFreeId){
		
		html  += '</p>'+
		'</div>'+
		'<dl class="dl-moneyMain bg-white">'+
		'<dt>'+ 
		'<p><b>'+totalAsset+'</b><span class="am-ft-sm">元</span></p>'+
		'<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">总操盘资产</span></p>'+
		'</dt>'+
		'<dt>'+
		'<p><b>'+ totalMarketValue +'</b></p>'+
		'<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">股票市值</span></p>'+
		'</dt>'+
		'<dt>'+
		'<p><b>'+ curAmount +'</b></p>'+
		'<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">可用余额</span></p>'+
		'</dt>'+
		'</dl>'+
		'</a>';
		
	}else if(prodId == proContestId){
		html  += '</p>'+
		'</div>'+
		'<dl class="dl-moneyMain bg-white">'+
		'<dt>'+ 
		'<p><b>'+totalAsset+'</b><span class="am-ft-sm">元</span></p>'+
		'<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">总操盘资产</span></p>'+
		'</dt>'+
		'<dt>'+
		'<p><b>'+ totalMarketValue +'</b></p>'+
		'<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">股票市值</span></p>'+
		'</dt>'+
		'<dt>'+
		'<p><b>'+ fee +'</b><span class="am-ft-sm">元</span></p>'+
		'<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">报名费</span></p>'+
		'</dt>'+
		'</dl>'+
		'</a>';
	}else{
		html  += '</p>'+
		'</div>'+
		'<dl class="dl-moneyMain bg-white">'+
		'<dt>'+ 
		'<p><b>'+totalAsset+'</b><span class="am-ft-sm">元</span></p>'+
		'<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">总操盘资产</span></p>'+
		'</dt>'+
		'<dt>'+
		'<p><b>'+enableRatioValue+'</b><span class="am-ft-sm">元</span></p>'+
		'<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">亏损警告线</span></p>'+
		'</dt>'+
		'<dt>'+
		'<p><b>'+exposureRatioValue+'</b><span class="am-ft-sm">元</span></p>'+
		'<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">亏损平仓线</span></p>'+
		'</dt>'+
		'</dl>'+
		'</a>';
	}
	
	return html;
}

function contracList(index,e){
	console.info(index);
	console.info(contractList);
	var contracts = contractList[index];
	$(".am-ft-main").hide();
	var panelHtml = "",HomsCombineId = HomsCombineIds[index];

	for(var i in contracts){
		panelHtml += createItemHtml(contracts[i],HomsCombineId);
	}

	$("#go_back").unbind("click");
	$("#loading").before(panelHtml);
	$("#go_back").click(function(){
		$(".contractList").remove();
		$(".am-ft-main").show();
		$("#go_back").unbind("click");

		$("#go_back").click(function(){
			history.go(-1);
		});
	});
}

function createItemHtml(contract,HomsCombineId) {	
	var prodInfo;
	for(var i in prodInfos){
		var temp = prodInfos[i]
		if(temp.id == contract.prodId){
			prodInfo = temp;
		}
	}
	if(!prodInfo){
		return "";
	}
	
	if (prodInfo.proddetail == null) {
		$.commAjax({
			url : container.proddetail,
			async : false,
			data : {
				product_id : contract.prodId
			},
			success : function(data) {
				if (data.success) {
					prodInfo.proddetail = data.data;
				} else {

				}
			}
		});
	}
	var contractNo,loanAmount;
	if(commAjaxUrl == container.richhbi || commAjaxUrl == container.historys){
		contractNo = contract.contractNo;
		loanAmount = contract.loanAmount;
	}else if(commAjaxUrl == container.loanapplys) {
		contractNo = contract.orderNo;
		loanAmount = contract.orderAmount;
	}
	var htmlPanel = '';
	var allAmount = parseInt((contract.cashAmount + loanAmount) / 100)
			.toLocaleString();
	htmlPanel += '<a '
	if(commAjaxUrl == container.richhbi){
		
		htmlPanel +=  'href="' + mobileUrl + '/user/contractInfo.htm?contract_no='
			+ contractNo + '&type='+type+'&combine='+HomsCombineId.homsCombineId+'&fundaccount='+HomsCombineId.homsFundAccount+'"';
	}	

	
	var rateName;
	if(contract.prodId == proMonthId ){
		rateName = "月利率";
	}else if(contract.prodId == proContestId){
		rateName = "报名费";
	}else{
		rateName = "费用";
	}
	var titleType = "子合约";
	if(contract.relContractNo == 0){
		titleType = "主合约";
	}	
	htmlPanel += ' class="am-ft-main contractList">'
			+ '<div class="contractNum bg-gray con-spacing10 clearfix">'
			+ '<p class="cm-left '
			+ prodInfo.contractNum
			+ '"><mark class="label">'
			+ titleType
			+ '</mark>'
			+ '<b ' ;
	
	if(commAjaxUrl == container.richhbi){
		htmlPanel += 'style="text-decoration: underline;"';
	}
	htmlPanel += ' class="am-left-10">'
			+ contractNo
			+ '</b>'
			+ '</p>'
			+ '<p class="cm-right"><span class="am-ft-sm am-ft-gray">开始<em class="bar">|</em>'
			+ contract.contractBeginDate
			+ '</span></p>'
			+ '</div>'
			+ '<dl class="dl-moneyMain bg-white">'
			+ '<dt>'
			+ '<p><b>'
			+ parseFloat(loanAmount/100).toFixed(2)
			+ '</b><span class="am-ft-sm">元</span></p>'
			+ '<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">借款金额</span></p>'
			+ '</dt><dt><p><b>'
			+	 parseFloat(contract.cashAmount/100).toFixed(2) +'</b><span class="am-ft-sm">元</span></p>'
			+ '<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">保证金</span></p>'
			+ '</dt>' + '<dt>' + '<p>';
	if(contract.prodId == proContestId){
		htmlPanel += parseFloat(contract.fee/100).toFixed(2)+'<span class="am-ft-sm">元</span>';
	}else if(contract.interestRate == 0){
		htmlPanel += '<span class="am-ft-sm am-ft-tabText">免费</span>';
	}else if(contract.prodId == proMonthId ){
		htmlPanel += parseFloat(contract.interestRate*100).toFixed(3) + '%';
	}else{
		var interestRateMoney = parseFloat(contract.interestRate*loanAmount/100).toFixed(2);
		htmlPanel += interestRateMoney.split(".")[1] == "00"?parseFloat(interestRateMoney).toFixed(0):interestRateMoney;
		htmlPanel += "元/日"
	}
	htmlPanel +=  '</b><span class="am-ft-sm"></span>';
	
	htmlPanel += '</p>'
			+ '<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">'+rateName+'</span></p>'
			+ '</dt>' + '</dl>' + '</a>';
	return htmlPanel;
}


var commAjaxUrl = container.richhbi;

function nextPage(){
	if(commAjax.readyState != 4){
		return;
	}
	$("#loading").hide();
	commAjax = $.commAjax({
		url : commAjaxUrl,
		data : {
			start : page++*pageSize,
			limit : pageSize,
			status : 0
		},
		success : function(data) {
			panelHtml = "";
			if (data.success) {
				$("#loading").hide();
				var items = data.data.items;
				if (items == null || items.length == 0) {
					return;
				} else {
					for ( var i in items) {
						panelHtml += createItemHtml(items[i]);
					}
				}
				$("#loading").before(panelHtml)
			} else {
				$.dialog({
					content : data.msg,
					title : "alert",
					ok : function() {
					}
				});
			}
		}
	});
}

function showFirstPage(){
	$("#noPzPanel").hide();
	$("#loading").hide();
	page = 0;

	dialog = $.dialog();
	if(commAjax){
		commAjax.abort();
	}
	commAjax = $.commAjax({
		url : commAjaxUrl,
		data : {
			start : page++*pageSize,
			limit : pageSize,
			status : 0
		},
		success : function(data) {
			panelHtml = "";
			if (data.success) {
				totalCount = data.totalCount;
				
				if(commAjaxUrl == container.richhbi){
					var datas = data.data;
					if (datas == null || datas.length == 0) {
						$("#noPzPanel").show();
					} else{
						for ( var i in datas) {
							if(datas[i].contractList == null){
								continue;
							}
							contractList[i] = datas[i].contractList;
							HomsCombineIds[i] = datas[i].HomsCombineId;
							panelHtml += createRichhbiHtml(datas[i],i);
						}
					}
					
				}else{
					var items = data.data.items;
					if (items == null || items.length == 0) {
						$("#noPzPanel").show();
					} else {
						for ( var i in items) {
							panelHtml += createItemHtml(items[i]);
						}
					}
				}
				$("#loading").before(panelHtml);
				dialog.close();
			} else {
				dialog.close();
				$.dialog({
					content : data.msg,
					title : "alert",
					ok : function() {
					}
				});
			}
		}
	});
}

$(function() {
	showFirstPage();
	var tabs = $("#tabList li");
	console.info(tabs);
	tabs.click(function(){
		if($(this).attr("class").indexOf("active") != -1){
			return;
		}
		tabs.removeClass("active");
		$(this).addClass("active");
		type = tabs.indexOf(this);
		switch(type){
			case 0:commAjaxUrl= container.richhbi;$(window).bind("scroll",scrollAction);break;
			case 1:commAjaxUrl= container.historys;$(window).unbind("scroll");break;
			case 2:commAjaxUrl= container.loanapplys;$(window).bind("scroll",scrollAction);break;
		}
		$(".am-ft-main").remove();
		showFirstPage();
	});
	
	
	var scrollAction = function(){

        if(($(window).scrollTop() + $(window).height() > $(document).height()-40) && (page*pageSize<totalCount)){
        	nextPage();
        }

    }
    
    
	
});