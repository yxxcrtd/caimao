/*合约追加*/
var container = {},key,product,prodDetails,contract,depositAmount,interestAmount,addAmount,dialog,prodTerms,
interestRate=null,loanRatio,loanTerm=2;

//秘钥接口地址 get
container.rsa = basePath + "/sec/rsa";
// 查询合约详情
container.contractDetail = basePath + "/financing/contract/detail";
// 查询产品
container.product = basePath + "/pz/product";
// 查询产品详情
container.prodDetail = basePath + "/pz/proddetail";
// 查询余额保证金
container.deposit = basePath + "/deposit";
// 查询余额保证金
container.addApply = basePath + "/pz/operation/add";

container.agree = true;
container.minAmount = false;
container.maxAmount = false;
//获取秘钥
$.commAjax({
	url : container.rsa,
	success : function(data) {
		if (!data.success) {
			 $.dialog({
			        content : data.msg,
			        title: "alert",
			        ok:function(){
			        }
			  });
			return;
		}
		var modulus = data.data.modulus;
		var exponent = data.data.exponent;
		key = RSAUtils.getKeyPair(exponent, "", modulus);

	}
});


function selectTerms(index){
	$(".select-results").hide();
	loanTerm = prodTerms[index];
	$(".select-choice span").html($(".select-results li").eq(index).text());
	//获取利息
	var prodDetail = getprodDetail(loanRatio,$("#additional").val().replace(",",""),loanTerm);


	if(prodDetail != null){
		interestRate = prodDetail.interestRate;
		$("#interest").html(interestRate);
		
		if(contract.prodId == proMonthId ){
			$("#interestExplain").html('即每1万元每月'
					+ parseInt(interestRate * 10000) + '元');
		}else{
			$("#interestExplain").html('即每1万元每日'
					+ parseInt(interestRate * 10000)+ '元');
		}
		
	}else{
		$("#interest").html("");
		$("#interestExplain").html("");
	}
}

//返回上一页
function changePre(n){
	$(".navi-container.bg-gray li").removeClass("ui-step-active");
	$(".navi-container.bg-gray li").eq(n).addClass("ui-step-active");
	$("#panel"+(n+1)).show();
	$("#panel"+(n+2)).hide();
	$("#go_back").unbind('click');

	if(n==0){
		$("#btnPage").show();
		footer.addClass("am-footerBtn");
	}
	$("#go_back").click(function(){
		if(n==0){
			history.go(-1);
		}else{
			changePre(n-1);
		}
	})
}

function createFinancingHtml() {
	var htmlPanel = "";
	if (product == null) {
		$.commAjax({
			url : container.product,
			data : {
				product_id : contract.prodId
			},
			async : false,
			success : function(data) {
				if (data.success) {
					product  = data.data;
					console.info(product);
					$("#additional").attr("placeholder","最少"+parseInt(product.prodAmountMin/100)+",最大"
					+parseInt(product.prodAmountMax/1000000)+"万");
					
					if(contract.prodId == proMonthId){
						prodTerms = product.prodTerms.split(","),
						TermHtml = "";
						for(var i in prodTerms){
							
							TermHtml += '<li onclick="selectTerms('+i+')">'+(prodTerms[i]/30)+'个月</li>';
						}
						$(".select-results").html(TermHtml);
						$(".select-choice").click(function(){
							$(".select-results").show();
						});
						
						
					}
				
				} else {

					 $.dialog({
					        content : data.msg,
					        title: "alert",
					        ok:function(){
					        }
					  });
				}
			}
		});

	}
	if (contract.prodId == proDayId) {
		
		htmlPanel += '<dl class="dl-capitalInfo">'
				+ '<dt class="dl-capitalInfo-item">'
				+ '<h4>管理费</h4>'
				+ '<div class="clearfix pt">'
				+ '<div class="cm-left text-left"><span class="am-ft-orange" id="interest" ></span><span>元/日</span></div>'
				+ '<div class="cm-left"><span class="am-ft-gray am-ft-sm" id="interestExplain"></span></div>'
				+ '</div></dt><dt class="dl-capitalInfo-item">'
				+ '<h4>操盘须知</h4>' + '<p class="pt" id="notice">'
				+ product.prodNote + '</p></dt>'
				+ '<dt class="dl-capitalInfo-item"><h4>开始交易</h4>'
				+ '<p class="pt"><span>下个交易日</span></p></dt></dl>';
	} else if (contract.prodId == proMonthId) {
		htmlPanel += '<dl class="dl-capitalInfo">'
			+ '<dt class="dl-capitalInfo-item">'
			+ '<h4>月利率</h4>'
			+ '<div class="clearfix pt">'
			+ '<div class="cm-left text-left"><span class="am-ft-orange"  id="interest" ></span></div>'
			+ '<div class="cm-left"><span class="am-ft-gray am-ft-sm" id="interestExplain"></span></div>'
			+ '</div></dt><dt class="dl-capitalInfo-item">'
			+ '<h4>操盘须知</h4>' + '<p class="pt" id="notice">'
			+ product.prodNote + '</p></dt>'
			+ '<dt class="dl-capitalInfo-item"><h4>开始交易</h4>'
			+ '<p class="pt"><span>下个交易日</span></p></dt></dl>';
	}

	return htmlPanel;
}


function getprodDetail(loanRatio,loanAmount,loanTerm){
	if(prodDetails == null){
		$.commAjax({
			url : container.prodDetail,
			async : false,
			data : {
				product_id : contract.prodId
			},
			success : function(data) {
				if (data.success) {
					prodDetails  = data.data;
				} else {
					 $.dialog({
					        content : data.msg,
					        title: "alert",
					        ok:function(){
					        }
					   });
				}
			}
		});
	}
	//loanAmount
	//contract.prodTerm;
	for(var i in prodDetails){
		if(loanRatio >= prodDetails[i].loanRatioFrom&& loanRatio < prodDetails[i].loanRatioTo
				&&parseInt(loanAmount*100) >= prodDetails[i].loanAmountFrom&&parseInt(loanAmount*100) < prodDetails[i].loanAmountTo
				&&loanTerm >= prodDetails[i].loanTermFrom&& loanTerm < prodDetails[i].loanTermTo){
			return prodDetails[i]
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

function applyAdd(){
	$.commAjax({
		url:container.addApply,
		type:"post",
		data:{
			contract_no:contract.contractNo,
			trade_pwd:RSAUtils.encryptedString(key, $("#tradePwd").val()),
			produce_id :contract.prodId,
			produce_term: loanTerm,
			deposit_amount:parseFloat(depositAmount*100).toFixed(0),
			add_amount: parseFloat(addAmount*100).toFixed(0)
		},
		success:function(data){
			dialog.close();
			if(data.success){
				$("#panel3").show();
				$("#panel2").hide();

				$(".navi-container.bg-gray li").removeClass("ui-step-active");
				$(".navi-container.bg-gray li").eq(2).addClass("ui-step-active");
				$("#go_back").click(function(){
					changePre(1);
				});
			}else{
				$.dialog({
				        content : data.msg,
				        title: "alert",
				        ok:function(){}
				  });
			}
		}
	});
}



var footer;
$(function() {
	$(document).click(function(event) {
		var target = event.target?event.target:event.srcElement;
		var evEL = $(target);
		
		while (true) {
			if (evEL.attr("id") == "loan_term_select") {

				break;
			} else if ($("#loan_term_select", evEL) != null) {
				$("#loan_term").hide();
				break;
			}
			evEL = evEL.parent();
		}

	});
	
	
	footer = $(".am-footer");
	var contractNo = request("contract_no");
	
	$.commAjax({
		url : container.contractDetail,
		data : {
			contract_no : contractNo
		},
		async : false,
		success : function(data) {
			if(data.success){
				contract = data.data;
				if(contract.relContractNo == "0"){
					$("#contract_no").html(contractNo);
				}else{

					$("#contract_no").html(contract.relContractNo);
				}
				
				loanRatio = contract.loanRatio;
				productId = contract.prodId;

				loanTerm = contract.interestSettleDays;
				$("#financingInfo").html(createFinancingHtml());
				if(contract.prodId == proMonthId){
					$("#terms").show();
					$("#agreeMonthText").show();

					$(".select-choice span").html(loanTerm/30+"个月");
				}else{
					loanTerm = 2;
					$("#agreeDayText").show();
				}
				
				
				
				var additional = $("#additional");
				
				function addChangeAction(){
					additionalValue = $.parseMoney(additional.val());
					if(additionalValue == ""){
						$("#additionalts").addClass("am-flexbox");
						$("#additionalts").show();
						$("#additionalDiv").hide();
						return;
					}else{

						$("#additionalts").removeClass("am-flexbox");
						$("#additionalts").hide();
						$("#additionalDiv").show();
						
					}
					
					$("#additionalAmount").html(parseInt((additionalValue/loanRatio)).toLocaleString());
					

				
					//获取利息
					var prodDetail = getprodDetail(loanRatio,additionalValue.replace(",",""),loanTerm);

			
					if(prodDetail != null){
						interestRate = prodDetail.interestRate;
						
						if(contract.prodId == proMonthId ){
							$("#interest").html(parseFloat(interestRate*100).toFixed(3)+"%");
							$("#interestExplain").html('即每1万元每月'
									+ parseFloat(interestRate * 10000).toFixed(2) + '元');
						}else{
							var interestRateValue = parseFloat(interestRate*additionalValue).toFixed(2);
							interestRateValue = interestRateValue.split(".")[1] == "00"?parseInt(interestRateValue):interestRateValue;
							$("#interest").html(interestRateValue);
						}
						
					}else{
						$("#interest").html("");
						$("#interestExplain").html("");
					}
					
					if((additionalValue*100)<product.prodAmountMin){
						container.minAmount = false;
						return;
					}else{
						container.minAmount = true;
					}
					
					if((additionalValue*100)>product.prodAmountMax){
						container.maxAmount = false;
						return;
					}else{
						container.maxAmount = true;
					}
				}
				
				additional.blur(function(){
					var addAccount = parseInt($.parseMoney(additional.val())/proMultiple)*proMultiple;
					additional.val(addAccount.toLocaleString());
					addChangeAction();
				});
				
				additional.keyup(function(){
					addChangeAction()
				});
			}else{
				 $.dialog({
				        content : data.msg,
				        title: "alert",
				        ok:function(){
				        }
				   });
			}
		}
	});
	
	$("#agree_checkbox").click(function(){

		if(container.agree){
			$("#addNext").addClass("btn-disabled");
			$(this).removeClass("ui-input-checkbox-active");
			container.agree = false;
		}else{
			container.agree = true;
			$("#addNext").removeClass("btn-disabled");
			$(this).addClass("ui-input-checkbox-active");
		}
	});
	
	
	$("#addNext").click(function addNext(){
	
		if(!container.agree){
			return;
		}
		
		if(!container.minAmount){
			$.dialog({
				content:"最小金额不能小于"+(product.prodAmountMin/100)+"元",
				title:"alert",
				ok:function(){}
			});
			return;
		}
		
		if(!container.maxAmount){
			$.dialog({
				content:"最大金额不能大于"+(product.prodAmountMax/100)+"元",
				title:"alert",
				ok:function(){}
			});
			return;
		}
		
		if(interestRate == null){
			$.dialog({
				content:"找不到利率区间",
				title:"alert",
				ok:function(){}
			});
			return;
		}
		dialog = $.dialog();
		depositAmount = $.parseMoney($("#additionalAmount").text());
		
		addAmount = $.parseMoney($("#additional").val());
		var commAjax = $.commAjax({
			url:container.deposit,
			data:{
				contract_no:contractNo,
				loan_apply_action:2
			}
		});
		$.when(commAjax).then(function(data){
			var panelHtml;

			var havingAmount = data.data/100;
			if(contract.prodId == proMonthId){
				interestAmount = interestRate*addAmount;
				if(parseFloat(havingAmount) < ((parseFloat(interestAmount)+parseFloat(depositAmount))/100)){
					panelHtml = '<p><i class="icon-prompt icon-prompt-error"></i></p>'
					+'<p><span>很抱歉，还缺少 '+(parseFloat(interestAmount)/100+parseFloat(depositAmount)/100-parseFloat(havingAmount)).toFixed(2)+'元！</span></p>'
					+'<div class="btn-page btn-page-center con-spacing15">'
					       + '<a href="'+mobileUrl+'/account/recharge.htm?tg='+window.location.href+'" class="am-button am-button-red">'
					      +  '<span class="am-ft-white">立即充值</span></a></div>';
				}else{
					
					panelHtml = '<p><span>支付风险保证金<span class="am-ft-orange">'
					+parseFloat(depositAmount).toFixed(0)
					+'</span>元 + 首月利息<span class="am-ft-orange">'
					+parseFloat(interestAmount).toFixed(2)+'</span>元</span></p>'
					+'<p class="am-ft-gray">可用余额 <b class="am-ft-main">'
					+ havingAmount
					+'</b> 元</p>'
					+'<aside class="con-frame"><ul class="datalist datalist-password">'
					+'<li class="datalist-item clearfix">'
					+'<div class="am-grid datalist-line">'
					+'<div class="am-grid-item am-grid-item-25  am-ft-right">'
					+'<h4 class="am-ft-md cm-left">安全密码</h4></div>'
					+'<div class="am-grid-item am-grid-item-75 am-ft-right am-right-15">'
					+'<span class="ui-form-field">'
					+'<input name="" id="tradePwd" type="password" class="text_input" defaultvalue="输入密码" value="" placeholder="输入密码">'
					+'</span></div></div></li></ul></aside>'
					+'<div class="btn-page btn-page-center con-spacing15">'+
			        '<a id="tradePwdBtn" class="am-button am-button-red">'+
			        '<span class="am-ft-white" >确认追加</span></a></div>';
				}
			}else if(contract.prodId == proDayId){
				interestAmount = parseFloat(interestRate*addAmount).toFixed(2);

				interestAmount = interestAmount.split(".")[1]=="00"?parseInt(interestAmount):interestAmount;
				if(parseFloat(havingAmount) < ((parseFloat(interestAmount)+parseFloat(depositAmount))/100)){
					panelHtml = '<p><i class="icon-prompt icon-prompt-error"></i></p>'
					+'<p><span>很抱歉，还缺少 '+(parseFloat(interestAmount)/100+parseFloat(depositAmount)/100-parseFloat(havingAmount)).toFixed(2)+'元！</span></p>'
					+'<div class="btn-page btn-page-center con-spacing15">'
					       + '<a href="'+mobileUrl+'/account/recharge.htm?tg='+window.location.href+'" class="am-button am-button-red">'
					      +  '<span class="am-ft-white">立即充值</span></a></div>';
				}else{

					panelHtml = '<p><span>支付风险保证金<span class="am-ft-orange">'
					+parseFloat(depositAmount).toFixed(0)
					+'</span>元 + 首日利息<span class="am-ft-orange">'
					+interestAmount+'</span>元</span></p>'
					+'<p class="am-ft-gray">可用余额 <b class="am-ft-main">'
					+ havingAmount
					+'</b> 元</p>'
					+'<aside class="con-frame"><ul class="datalist datalist-password">'
					+'<li class="datalist-item clearfix">'
					+'<div class="am-grid datalist-line">'
					+'<div class="am-grid-item am-grid-item-25  am-ft-right">'
					 +'<h4 class="am-ft-md cm-left">安全密码</h4></div>'
					 +'<div class="am-grid-item am-grid-item-75 am-ft-right am-right-15">'
					+'<span class="ui-form-field">'
					+'<input name="" id="tradePwd" type="password" class="text_input" defaultvalue="输入密码" value="" placeholder="输入密码">'
					+'</span></div></div></li></ul></aside>'
					+'<div class="btn-page btn-page-center con-spacing15">'
			        +'<a id="tradePwdBtn" class="am-button am-button-red">'
			        +'<span class="am-ft-white"  >确认追加</span></a></div>';
				}
			}
			$("#prompt").html(panelHtml);

			$("#tradePwdBtn").click(function(){
				dialog = $.dialog();
				applyAdd();
			});

			$(".navi-container.bg-gray li").removeClass("ui-step-active");
			$(".navi-container.bg-gray li").eq(1).addClass("ui-step-active");
			$("#panel1").hide();
			$("#panel2").show();
			
			footer.removeClass("am-footerBtn");
			$("#btnPage").hide();
			$("#go_back").unbind('click');
			$("#go_back").click(function(){
				changePre(0);
			});
			
			dialog.close();
		});
		
	});
	
	
});