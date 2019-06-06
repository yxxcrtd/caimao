//打开页面前查询

$(function() {
	var container = {};
    proMonthId = 800461779107843;
	// 秘钥接口地址 get
	container.product = basePath + "/pz/product";
	container.productDetail = basePath + "/pz/proddetail";
	container.productId = "?product_id="+proMonthId;
	container.agree = true;
	//设置默认保证金
	container.riskMargin = 10000;
	//设置默认天数
	container.selectedTerm = 30;
	
	// 秘钥接口地址 get
	container.rsa = basePath + "/sec/rsa";

    var closePZ = false;
    var userInfo = $.commAjax({
        url : "/user"
    });
    var newRegirestDate = $.commAjax({
        url : "/other/data/o20150715"
    });
    $.when(userInfo, newRegirestDate).then(function(data1,data2){
        data1 = data1[0];
        data2 = data2[0];
        // 查询用户是否登录，为登录显示需要登录
        if (data1.success == false) {
            closePZ = true;
            $('#errorStr').html('请先登录');
            $("#addBtn1").addClass("btn-disabled");
        } else {
            // 检查用户是否新注册用户
            if (data2.data > 0) {
                if (data1.data.registerDatetime > data2.data) {
                    closePZ = true;
                    $('#errorStr').html('暂停融资');
                    $("#addBtn1").addClass("btn-disabled");
                }
            }
        }
    });

	$('#panel2').css({
		"display" : "none"
	});
	$('#panel3').css({
		"display" : "none"
	});
	$('#panel4').css({
		"display" : "none"
	});

	function dialog2(msg){
		
	 	$.dialog({
			 content:msg,
			 title:"alert",
			 ok:function(){}
		 });
	
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

	
	function action1(data2){
		if (data2.success) {
			
			$("#riskMargin").attr("placeholder","最少"+parseFloat(data2.data.prodAmountMin/100/data2.data.prodLoanRatioMax).toFixed(0).toLocaleString()+
			","+"最多"+parseFloat(data2.data.prodAmountMax/1000000/data2.data.prodLoanRatioMin).toFixed(0).toLocaleString()+"万");
			// 请求成功更新页面信息上
			container.max_ration = data2.data.prodLoanRatioMax || 10;
			container.min_ration = data2.data.prodLoanRatioMin || 3;
			container.loan_prodTerms = data2.data.prodTerms;
			//最大借款额度
			container.prodAmountMax = data2.data.prodAmountMax/100;
			//最小借款额度
			container.prodAmountMin = data2.data.prodAmountMin/100;

			$("#prodNote").html(data2.data.prodNote);
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
										container.account,container.riskMargin);
								var interest_month_rate = container.interestDayRate;
								$('#interest_month_rate').html(
										(interest_month_rate*100).toFixed(3)+"%");
								$('#interest_rate_desc').html(
										(interest_month_rate * 10000).toFixed(2));
							});
		} else {
			// 请 求不成功，跳转错误页面
			dialog2(data2.msg);
			

		}
	}
	var commAjax1 = $.commAjax({
				url : container.product + container.productId,
				type : "get",
				data : {

				}/*,
				success : function(data2) {

					
				}*/

			});
	// 请求产品详情信息
	function action2(data2){
		if (data2.success) {
			// 请求成功更新页面信息
			container.detailValue = data2.data;

		} else {
			// 请 求不成功，跳转错误页面
			dialog2(data2.msg);
		}
	}
	var commAjax2=$.commAjax({
		url : container.productDetail + container.productId,
		type : "get",
		data : {

		}/*,
		success : function(data2) {

			
		}*/

	});
	
	//	是否同意xx服务协议
	$("#agree_checkbox").click(function(){
        if (closePZ == true) return false;
		if(container.agree){
			$(this).removeClass("ui-input-checkbox-active");
			$("#addBtn1").addClass("btn-disabled");
			container.agree = false;
		}else{
			container.agree = true;
			$(this).addClass("ui-input-checkbox-active");
			$("#addBtn1").removeClass("btn-disabled");
		}
	});

	$("#loan_term_select").click(function() {

		$("#loan_term").show();
	});
	$.when(commAjax1,commAjax2).then(function(data1,data2){
		
		action1(data1[0]);
		action2(data2[0]);
		//createValibleHtml();
	});
	var n = 0;
	//生成可融资列表
   function createValibleHtml(){

		var htmlContent = "";
		var ration_value = 0;
		var rationMap = {};
		var rkey, rvalue;	
		
	   for (var i = container.min_ration; i <= container.max_ration; i++) {

			ration_value = i * container.riskMargin;
			rkey = parseInt(ration_value) + "元";
			rvalue = parseFloat(ration_value).toFixed(2);
			rationMap[rkey] = parseFloat(rvalue).toFixed(2);
	
			htmlContent += "<li class='list-amoutCompetition-item-left";
			if(ration_value < container.prodAmountMin || ration_value>= container.prodAmountMax){
				htmlContent += " list-amoutCompetition-item gray'style='background-color:#d3d3d3'";
			}else{
				htmlContent += " list-amoutCompetition-item'";
			}
			htmlContent += "> <a>"
					+ rkey + "</a></li>";
		    
		}
	   	container.Ifcheck="";
		$('#margin_ratio').html(htmlContent);
		// 给所有的按钮加事件
		var margin_ratios = $("#margin_ratio li:not(.gray)");
		margin_ratios.click(
						function() {
							$("#margin_ratio li").removeClass(
							"active");
							n = $("#margin_ratio li").index($(this));
					       $(this).addClass("active");
							var ratioKey = $("a", this).text();
							var ratioValue = rationMap[ratioKey];
							container.Ifcheck=1;
							
							$("#selected_ratio").html(
									ratioValue);
							// 借款资金
							container.account = ratioValue;
							
							getLoanDayRate(container.selectedTerm,container.account,container.riskMargin);
							var totalAccount = parseFloat(container.account)+
									+  parseFloat(container.riskMargin);

							$("#all_account")
									.html(parseFloat(totalAccount).toFixed(2));
							// 利息
							container.selectedTerm = container.selectedTerm || 30;
							var interest_month_rate = container.prodDetailItem.interestRate;
							$('#interest_month_rate').html(
									(interest_month_rate*100).toFixed(3)+"%");
							$('#interest_rate_desc').html(
									parseInt(interest_month_rate * 10000));
							// 亏损警告线
							var enableAccount = parseFloat(
									container.prodDetailItem.enableRatio
											* container.account)
									.toFixed(0);

							$("#enable_account").text(
									enableAccount);
							// 止损线
							var exposure_account = parseFloat(
									container.prodDetailItem.exposureRatio
											* container.account)
									.toFixed(0);

							$("#exposure_account").text(
									exposure_account);

						});
		$("#margin_ratio li").eq(n).click();
   };

	$("#addBtn2").click(
					function() {
						
						// 判断相应的密码是否输入
						var $this = $("#addBtn2");
						
						var password = $("#password").val();
						if (password == "") {
							dialog2("密码不能为空!");							
							return;
						}
						var dialogLoad = $.dialog();
						// 生成借款合约
						// 取得加密密钥
						
						$('#go_back').unbind("click");
						$('#go_back').click(function(){
							history.go(-1);
						});
						$.commAjax({
									url : container.rsa,
									success : function(data) {
										if (!data.success) {
											dialogLoad.close();
											dialog2("系统错误!");
											
											return;
										}
										var modulus = data.data.modulus;

										var exponent = data.data.exponent;

										var key = RSAUtils.getKeyPair(exponent,
												"", modulus);

										// 执行借款
										$.commAjax({
													url : basePath
															+ '/pz/loan/apply',
													type : "post",
													data : {
														trade_pwd : RSAUtils.encryptedString(
																		key,
																		password),
														produce_id : proMonthId,
														produce_term : container.selectedTerm,
														deposit_amount : parseInt(container.riskMargin*100),
														loan_amount : parseInt(container.account*100),
														prev_day : 0
													},
													success : function(data2) {
														dialogLoad.close();
														if (data2.success) {
															// 请求成功更新页面信息
															$("#margin_ratio li").removeClass("active");
													
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
																ok:function(){}
															})
														}
													}

												});
									}
								});

					});

	// 我要投资按钮事件
	$("#addBtn1")
			.click(function() {
            if (closePZ == true) return false;
						 if(!container.agree){
							 return;
						 }
					 if(container.Ifcheck==''||container.Ifcheck!=1){
						 $.dialog({
							 content:"请选择借款金额!",
							 title:"alert",
							 ok:function(){
								 $('#margin_ratio').focus();
							 }
						 });
						 
						 return;
					 }else{
						 $('#errMsg1').html(" ");
					 }
						getLoanDayRate(container.selectedTerm,
								container.account,container.riskMargin);
						
				
						$('#riskValue').html(container.riskMargin);
						container.interestDayRate = container.interestDayRate || 0;
						container.account = container.account || 0;
						
						container.dayRateAccount = container.account
								* container.interestDayRate ;
						
						$('#rateAccount').html((container.dayRateAccount+"").indexOf(".")==-1?container.dayRateAccount:container.dayRateAccount.toFixed(2));
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
												$("#panel2_prompt2").show();
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
																	container.avalaible_amount = avalaible_amount==0?0:parseFloat(avalaible_amount).toFixed(2);
																	container.allAmount = parseFloat(container.riskMargin)
																			+ parseFloat(container.dayRateAccount);
															
																	$('#selfAccount').html(container.avalaible_amount);
																	$('#selfAccount1').html(container.avalaible_amount);

																	if (avalaible_amount >= container.allAmount) {
																		$('#panel2').css({"display" : ""});
																		$('#trustPanel').css({
																			"display" : "none"
																		});
																		$('#selfDetail').css({"display" : ""});
																		$('#addBtn2').parent().css({"display" : ""});
																		$('#selfPass').css({"display" : "none"});
																	} else {
																		$('#lackAccount').html(parseFloat(container.allAmount-container.avalaible_amount).toFixed(2)||0);
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

											showPanel2();
										} else {
											// 实名认证
											window.location.href = mobileUrl+"/user/login.htm?tg="+location.href;

										}
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
		container.avalaible_amount = container.avalaible_amount||0;
		$('#selfAccount').html(container.avalaible_amount);
		$('#selfAccount1').html(container.avalaible_amount);
		
		$('#selfPass').css({
			"display" : ""
		});
		$('#addBtn2').parent().css({
			"display" : "none"
		});	
		
	}
	//实名认证
	
	function  toMakeTrust(){
		$("#panel2_prompt1").show();
		$('#trustPanel').css({
			"display" : ""
		});
		$('#selfDetail').css({
			"display" : "none"
		});
		$('#selfPass').css({
			"display" : "none"
		});
		$('#addBtn2').parent().css({
			"display" : "none"
		});	
	}
	
	// 显示面版控制
	function showPanel1() {
		$('#ol_steps li').removeClass('ui-step-active');
		$('#ol_steps li').eq(0).addClass('ui-step-active');
		
		$('.am-footer').removeClass('am-footerBtn');
		$('#panel2').css({
			"display" : "none"
		});
		$('#addBtn1').css({
			"display" : ""
		});


		$('#panel1').show();
		
		
		$('#go_back').unbind("click");
		
		$('#go_back').click(function(){
			history.go(-1);
		});
	}
	// 显示面版控制
	function showPanel2() {
		$('#ol_steps li').removeClass('ui-step-active');
		$('#ol_steps li').eq(1).addClass('ui-step-active');
		
		$('.am-footer').removeClass('am-footerBtn');
		$('#panel1').css({
			"display" : "none"
		});
		$('#addBtn1').css({
			"display" : "none"
		});


		$('#panel2').show();
		$('#go_back').unbind("click");

		$('#go_back').click(function(){
			showPanel1();
		});
	}
	// 取得日利息
	function getLoanDayRate(day, account,riskAmount) {
		var ratio;
		day = day || 30;
		account = account || 0;
		riskAmount = riskAmount||0;
		if(riskAmount == 0){
			container.interestDayRate = 0;
			container.prodDetailItem = 0;
			return;
		}else{
			ratio  = parseInt(account/riskAmount);
		}
		var len = container.detailValue.length;
		for ( var i = 0; i < len; i++) {
			var item = container.detailValue[i];			
			
			if (day >= item.loanTermFrom && day < item.loanTermTo
					&& account >= item.loanAmountFrom/100
					&& account < item.loanAmountTo/100
					&& ratio >=item.loanRatioFrom && ratio < item.loanRatioTo) {
				container.interestDayRate = item.interestRate; 
				container.prodDetailItem = item;
				break;
			} else {
				container.interestDayRate = null;
				container.prodDetailItem = null;

			}
		}
	}

	var riskMargin = $('#riskMargin');
	function riskMarginChange(){
		var value = $.parseMoney(riskMargin.val());
		if(value == ""){
			$("#margin_ratio").html("");
			$("#additionalts").show();
			return;
			
		}
		$("#additionalts").hide();
		container.riskMargin = value;
		// 可配金额付值
		createValibleHtml();
		// 变更页面数据
	}
	// 保证金输入完成
	riskMargin.keyup(riskMarginChange);
	
	riskMargin.blur(function(){
		var riskMarginVal = parseInt($.parseMoney(riskMargin.val())/proMultiple)*proMultiple;
		riskMargin.val(riskMarginVal.toLocaleString());

		riskMarginChange();
	});
});