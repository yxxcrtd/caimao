

$(function() {
	var container = {};
	var productID = proMonthId;
	container.contract_no = getUrlParam("contract_no");
	container.contractDetailUrl=basePath+"/financing/contract/detail?contract_no="+container.contract_no;
	container.productUrl = basePath + "/pz/product?product_id="+productID;
	container.productDetailUrl = basePath + "/pz/proddetail?product_id="+productID;
	container.rsa = basePath + "/sec/rsa";
	//设置默认天数
	container.selectedTerm = 30;
	$('#panel2').css({
		"display" : "none"
	});
	$('#panel3').css({
		"display" : "none"
	});
	$('#panel4').css({
		"display" : "none"
	});
	//合约详情查询
	function contractDetail(data2){
		if (data2.success) {
			container.contractDetailUrl = data2.data;
			$('#contractBeginDate').text(data2.data.contractBeginDate);
			if(data2.data.relContractNo == "0"){
				$('#contractNo').text(data2.data.contractNo);
			}else{
				$('#contractNo').text(data2.data.relContractNo);
			}
			container.cashAmount = data2.data.cashAmount/100;				
			$('#cashAmount').text(container.cashAmount);
			container.loanAmount = data2.data.loanAmount/100;
			container.loanRatio = data2.data.loanRatio;
			container.allAmount =parseInt(container.cashAmount+ container.loanAmount);
			container.interestRate = data2.data.interestRate;
			$('#interestRate').html(parseFloat(container.interestRate*100).toFixed(3));
			$('#allAmount').html(container.allAmount);
			container.oldprodTerm = data2.data.prodTerm;
		}else{
			$.dialog({
				content:data2.msg,
				title:"alert",
				ok:function(){}
			});
		}
	}
	var commAjax3=$.commAjax({
		url : container.contractDetailUrl,
		type : "get",
		data : {
		}/*,
		success : function(data2) {
			
		
		}*/
	});
	var commAjax1=$.commAjax({
		url : container.productUrl,
		type : "get",
		data : {
		}
	});
	
	function actionProduct(data2){
		if (data2.success) {
			// 请求成功更新页面信息				
			container.loan_prodTerms = data2.data.prodTerms;
		
			// 使用期限
			var loanContent = "";
			var prodTerms = container.loan_prodTerms.split(',');
			var prodTermsMap = {};
			var pkey, pvalue;
			for ( var j = 0; j < prodTerms.length; j++) {
				pkey = prodTerms[j] / 30 + "个月";
				pvalue = prodTerms[j];
				prodTermsMap[pkey] = pvalue;
				loanContent += "<li>" + pkey + "</li>";
			}

			$('#loan_term').html(loanContent);
			
			// 给所有option注册事件
			$("#loan_term li")
					.click(
							function() {
								var selectedV = $(this).text();
								$("#loan_term").hide();
								$('#selectedTerm').text(selectedV);
								container.selectedTerm = prodTermsMap[selectedV];
								// 变更利息
								getLoanDayRate(container.selectedTerm,
										container.loanAmount,container.loanRatio);
								var interest_month_rate = container.interestDayRate* 100;
								$('#interestRateNew').html(
										interest_month_rate.toFixed(3));
								$('#interestRate100New').html(
										(interest_month_rate * 100).toFixed(0));
							});
		} else {
			// 请 求不成功，跳转错误页面
			$.dialog({
				content:data2.msg,
				title:"alert",
				ok:function(){}
			});

		}
	}
	// 请求产品详情信息
	function actionProductDetail(data2){
		if (data2.success) {
			// 请求成功更新页面信息
			container.detailValue = data2.data;

		} else {
			// 请 求不成功，跳转错误页面
			$.dialog({
				content:data2.msg,
				title:"alert",
				ok:function(){}
			});
		}
	};
	$("#loan_term_select").click(function() {

		$("#loan_term").show();
	});

	
	var commAjax2=$.commAjax({
		url : container.productDetailUrl,
		type : "get",
		data : {

		}/*,
		success : function(data2) {

			
		}*/

	});
	
	$.when(commAjax1,commAjax2,commAjax3).then(function(data1,data2,data3){		
		actionProduct(data1[0]);
		actionProductDetail(data2[0]);
		contractDetail(data3[0]);
		getLoanDayRate(30,container.loanAmount,container.loanRatio);
		var interest_month_rate = container.interestDayRate* 100;
		$('#interestRateNew').html(
				interest_month_rate.toFixed(3));
		$('#interestRate100New').html(
				(interest_month_rate * 100).toFixed(0));
	
	});
	
	// 取得日利息
	function getLoanDayRate(day, account,loanRatio) {
		console.info(day,account,loanRatio);
		day = day || 30;
		account = account || 0;
		

		var len = container.detailValue.length;
		for ( var i = 0; i < len; i++) {
			var item = container.detailValue[i];
  
			if (day >= item.loanTermFrom && day < item.loanTermTo
					&& account >= item.loanAmountFrom/100
					&& account < item.loanAmountTo/100
					&& loanRatio >= item.loanRatioFrom
					&& loanRatio < item.loanRatioTo
				) {
				container.interestDayRate = item.interestRate;
				container.prodDetailItem = item;
				break;
			} else {
				container.interestDayRate = null;
				container.prodDetailItem = null;

			}
		}
		

	}
	$("#addBtn1")
	.click(
			function() {

				getLoanDayRate(container.selectedTerm,
						container.loanAmount,container.loanRatio);
				showPanel2();

				container.interestRateAmount = parseFloat(container.interestDayRate*container.loanAmount).toFixed(2);
				$('#interestRateAmount').html(container.interestRateAmount);			
			
				// 查询是否实名认证
				$.commAjax({
							url : basePath + '/user',
							type : "get",
							data : {},
							success : function(data2) {
								if (data2.success) {
									var is_trust = data2.data.isTrust;

									// 已经实名
									if (is_trust == '1') {
										$('#trustPanel').css({"display" : "none"});
										// 查询可用额度
										$.commAjax({
													url : basePath
															+ "/account",
													type : "get",
													data : {},
													success : function(
															data2) {

														if (data2.success) {
															// 请求成功更新页面信息
															var avalaible_amount = (data2.data.avalaibleAmount-data2.data.freezeAmount)/100;
														

															$('#selfAccount').html(avalaible_amount);
															$('#selfAccount1').html(avalaible_amount);
															$('#lackAccount').html(avalaible_amount-container.interestRateAmount);
															if (parseInt(avalaible_amount) >= parseInt(container.interestRateAmount)) {
												
																$('#panel2').css({"display" : ""});
																$('#selfDetail').css({"display" : ""});
																$('#addBtn2').css({"display" : ""});
																$('#selfPass').css({"display" : "none"});
															} else {
																 toRecharge();
																}

														} else {
															toRecharge();

														}
													}

												});
									} else {
										// 实名认证
										toMakeTrust();

									}
								} else {
									// 实名认证
									window.location.href = mobileUrl+"/user/login.htm?tg="+location.href;

								}
							}
						});
			});

	$("#addBtn2").click(function() {
				// 判断相应的密码是否输入
				var password = $("#password").val();
				if (password == "") {
					$.dialog({
						content:"密码不能为空!",
						title:"alert",
						ok:function(){}
					});
					return;
				}
				// 生成借款合约
				// 取得加密密钥
				var dialogLoad = $.dialog();
				$.commAjax({
							url : container.rsa,
							success : function(data) {
								if (!data.success) {
									dialogLoad.close();
									$.dialog({
										content:data.msg,
										title:"alert",
										ok:function(){}
									});
									return;
								}
								var modulus = data.data.modulus;

								var exponent = data.data.exponent;

								var key = RSAUtils.getKeyPair(exponent,
										"", modulus);

								// 执行借款
								$.commAjax({
											url : basePath
													+ '/financing/defered/apply',
											type : "post",
											data : {
												trade_pwd : RSAUtils
														.encryptedString(
																key,
																password),
												contract_no : container.contract_no,
												produce_term : container.selectedTerm,												
												produce_id : productID
											},
											success : function(data2) {

												dialogLoad.close();
												if (data2.success) {
													// 请求成功更新页面信息
													$('#ol_steps li').removeClass('ui-step-active');
													$('#ol_steps li').eq(2).addClass('ui-step-active');

													$('#panel2').css({"display" : "none"});
													$('#panel3').css({"display" : "none"});
													$('#panel4').css({"display" : ""});

												} else {
													// 请 求不成功，跳转错误页面
													$.dialog({
														content:data2.msg,
														title:"alert",
														ok:function(){
															
														}
													});
												
												}
											}

										});
							}
						});

			});
	//充值
	function toRecharge(){
		$('#trustPanel').css({
			"display" : "none"
		});
		$('#selfDetail').css({
			"display" : "none"
		});
		container.avalible_amount = container.avalible_amount||0;
		$('#selfAccount').text(container.avalible_amount);
		$('#selfAccount1').text(container.avalible_amount);
		$('#selfPass').css({
			"display" : ""
		});
		$('#addBtn2').css({
			"display" : "none"
		});	
	}
	//实名认证
	
	function  toMakeTrust(){
		$('#trustPanel').css({
			"display" : ""
		});
		$('#selfDetail').css({
			"display" : "none"
		});
		$('#selfPass').css({
			"display" : "none"
		});
		$('#addBtn2').css({
			"display" : "none"
		});	
		
		
	}
	// 显示面版控制
	function showPanel2() {
		$('#ol_steps li').removeClass('ui-step-active');
		$('#ol_steps li').eq(1).addClass('ui-step-active');
		
		 $(".am-footerBtn").removeAttr("style");
		$('.am-footer').removeClass('am-footerBtn');
		$('#panel1').css({
			"display" : "none"
		});
		$('#addBtn1').parent().css({
			"display" : "none"
		});
		

		$('#panel2').show();

	}
	function getUrlParam(name)
	{
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
	var r = window.location.search.substr(1).match(reg);  //匹配目标参数
	if (r!=null) return unescape(r[2]); return null; //返回参数值
	} 
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
	
	
});