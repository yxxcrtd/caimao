/*合约详情*/
var container = {};
//展期前提
container.premiseAdd = basePath + "/financing/add/premise";
//追加前提
container.premiseDefered = basePath + "/financing/defered/premise";
// 合约详情
container.contractdetail =basePath +  "/financing/contract/detail";

container.loanapplydetail =basePath +  "/financing/loanapply/detail";

container.hiscontractdetail =basePath +  "/financing/hiscontract/detail";
//产品详情
container.proddetail = basePath + "/pz/proddetail";
//还款前提
container.premiseRepay = basePath + "/financing/repay/premise";


//http://192.168.76.48:8080/hy/financing/interest/page?_=1422345940977&contract_no=795092365344769&start_date=2000-01-01&end_date=3016-01-01&start=0&limit=5
//利息支付记录
container.interestPage = basePath + "/financing/interest/page"; 

container.contractList = basePath + "/financing/contract/list";

container.assetsinfo = basePath + "/homs/assetsinfo";




function getProddetail(proddetail, loanAmount, loanTerm, loanRatio) {

	
	for ( var i in proddetail) {
		if(proddetail[i].loanTermFrom == 210){
			console.info(proddetail[i].loanRatioFrom);
			console.info(proddetail[i].loanRatioTo);
			console.info(proddetail[i].loanAmountFrom);
			console.info( proddetail[i].loanAmountTo);
		}
		if (loanRatio >= proddetail[i].loanRatioFrom
				&& loanRatio < proddetail[i].loanRatioTo
				&& loanAmount >= proddetail[i].loanAmountFrom
				&& loanAmount <= proddetail[i].loanAmountTo
				&& loanTerm >= proddetail[i].loanTermFrom
				&& loanTerm < proddetail[i].loanTermTo) {
			return proddetail[i];
		}
	}
	return null;
}

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
var contractNo = request("contract_no");
var type = request("type");

if(contractNo == ""){
	window.location.href = mobileUrl + "/user/contracts.htm";
}


$(function(){
	var detailUrl,data;
	switch(parseInt(type)){
		case 2:detailUrl = container.loanapplydetail;
		data = {loan_apply_no:contractNo};
		break;
		case 0:detailUrl = container.contractdetail;
		data = {contract_no:contractNo};
		break;
		case 1:detailUrl = container.hiscontractdetail;
		data = {contract_no:contractNo};
		break;
	}
	$.commAjax({
		url: detailUrl,
		data:data,
		success:function(data){
			if(data.success){
	
				var contract = data.data;
				$("#allAmount").html(parseFloat((contract.cashAmount+contract.loanAmount)/100).toLocaleString());
				$("#cashAmount").html(parseFloat(contract.cashAmount/100).toLocaleString());
				$("#loanAmount").html(parseFloat(contract.loanAmount/100).toLocaleString());
				

				$("#beginInterestDate").html(contract.contractBeginDate);
				$("#contractBeginDate").html(contract.contractBeginDate);
				$("#contractEndDate").html(contract.contractEndDate);
				
				if(contract.prodId==proMonthId){
					$("#extensionBtn").parent().show();
					$("#addBtn").parent().show();
					$("#interestRecorded").show();
				}else if(contract.prodId==proDayId){
					$("#addBtn").parent().removeClass("am-grid-item-50");
					$("#addBtn").parent().show();
					$("#interestRecorded").show();
				}
				
				$.commAjax({
					url : container.proddetail,
					async : false,
					data : {
						product_id : contract.prodId
					},
					success : function(data) {
						if (null != data&&data.success) {
							var proddetails = data.data;
							//var proddetail = getProddetail(proddetails, contract.loanAmount,contract.prodTerm,contract.loanRatio);
							//$("#enableRatio").html(proddetail.enableRatio*contract.loanAmount/100+"元");
							//$("#exposureRatio").html(proddetail.exposureRatio*contract.loanAmount/100+"元");
							if(contract.prodId==proMonthId){
								
								$("#interestRatetitle").html("月利率");
								$("#interestRate").html(parseFloat(contract.interestRate*100).toFixed(3)+"%");
							}else if(contract.prodId==proDayId){
								
								$("#interestRatetitle").html("管理费");
								$("#interestRate").html(parseFloat(contract.loanAmount*contract.interestRate/100).toFixed(0)+"元/日");
							}else if(contract.prodId==proFreeId){

								$("#interestRatetitle").html("费用");
								$("#interestRate").html("免费");
							}else if(contract.prodId==proContestId){
								$("#interestRatetitle").html("报名费");
								$("#interestRate").html(parseFloat(contract.fee/100).toFixed(0)+"元");
							}
						} else {

							if(contract.prodId==proFreeId){

								$("#interestRatetitle").html("费用");
								$("#interestRate").html("免费");
							}
						}
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
	
	$.commAjax({
		url:container.assetsinfo,
		type:"get",
		data:{
			homs_fund_account:request('fundaccount'),
			homs_combine_id:request('combine')
		},
		success:function(data){
			if(data.success){
				var assetinfo = data.data;
				if(assetinfo.enableRatio == null){
					$("#enableRatio").html("<div style='text-align:center;'>- -<div>");
				}else{

					$("#enableRatio").html(parseFloat(assetinfo.loanAmount*assetinfo.enableRatio/100).toFixed(2)+" 元");
				}
				
				if(assetinfo.exposureRatio == null){
					$("#exposureRatio").html("<div style='text-align:center;'>- -<div>");
				}else{

					$("#exposureRatio").html(parseFloat(assetinfo.loanAmount*assetinfo.exposureRatio/100).toFixed(2)+" 元");
				}
				
				$("#totalMarketValue").html(parseFloat(assetinfo.totalMarketValue/100).toFixed(2)+" 元");
				$("#totalProfit").html(parseFloat(assetinfo.totalProfit/100).toFixed(2)+" 元");
				
			}else{
				$.dialog({
					content:data.msg,
					title:"alert",
					ok:function(){}
				});
			}
		}
	});
	
	var start = 0,pageSize = 5,totalCount;
	var pageAjax;
	function showInterstPages(){
		if(pageAjax && pageAjax.readyState != 4){
			return;
		}
		pageAjax = $.commAjax({
			url:container.interestPage,
			type:"get",
			data:{
				contract_no:contractNo,
				start_date:'2000-01-01',
				end_date:'3016-01-01',
				start:start++,
				limit:pageSize
			},
			success:function(data){
				if(data.success){
					totalCount = data.totalCount;
					var interestInfos = data.data.items,interHtml = "";
					
					for(var i in interestInfos){
						interHtml +=  '<tr>'+
							    '<td class="tb-left">'+interestInfos[i].settleInterestBeginDate+'</td>'+
							    '<td class="tb-center">'+interestInfos[i].orderAmount/100+'元</td>'+
							    '<td class="tb-right">'+interestInfos[i].billAbstract+'</td>'+
							  '</tr>';		
						 
					}
					
					$("#interestList").append(interHtml);
					
				}else{
					
				}
			}
		});
	}
	
	showInterstPages();
	
	$(window).bind("scroll",function(){
		if(($(window).scrollTop() + $(window).height() > $(document).height()-40) && (start*pageSize<totalCount)){
			showInterstPages();
        }
	});
	
	
	$.commAjax({
		url:container.premiseAdd,
		type:"post",
		data:{
			contract_no:contractNo
		},
		success:function(data){
			if(data.success){
				$("#addBtn").removeClass("btn-disabled");
				$("#addBtn span").addClass("am-ft-white");
				$("#addBtn").click(function(){
					window.location.href=mobileUrl + "/financing/add.htm?contract_no="+contractNo;
				});
			}
		}
	});
	
	

	$.commAjax({
		url:container.premiseRepay,
		type:"post",
		data:{
			contract_no:contractNo
		},
		success:function(data){
			if(data.success){
				$("#repayUrlBtn").show();
				$("#repayUrlBtn").click(function(){
					window.location.href=mobileUrl + "/financing/repayment.htm?contract_no="+contractNo;
				});
			}
		}
	});
	
	

	
	$.commAjax({
		url:container.premiseDefered,
		type:"post",
		data:{
			contract_no:contractNo
		},
		success:function(data){
			if(data.success){
				$("#extensionBtn").removeClass("btn-disabled");
				$("#extensionBtn span").addClass("am-ft-white");
				$("#extensionBtn").click(function(){
					window.location.href=mobileUrl + "/financing/extension.htm?contract_no="+contractNo;
				});
			}
		}
	});
	
	
	
	
});