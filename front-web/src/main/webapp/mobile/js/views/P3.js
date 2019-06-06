$(function() {
	var container = {};
	//1: 免费体验，2：实盘大赛（某期），3：按月融资，4：按日融资，5：最新特惠
	container.product = basePath + "/pz/product?product_id=1";
	container.rsa = basePath + "/sec/rsa";
	$('#panel2').css({
		"display" : "none"
	});
	$('#panel3').css({
		"display" : "none"
	});
	$('#panel4').css({
		"display" : "none"
	});
	function dialog(msg){
		$.dialog({
			 content:msg,
			 title:"alert",
			 ok:function(){}
		 });
	}
	//查询产品信息
	$.commAjax({
		url : container.product,
		type : "get",
		data : {

		},
		success : function(data2) {
			if (data2.success) {
				container.productItem = data2.data;
				container.loanAccount = parseInt(data2.data.prodAmountMax/100);
				container.prodLoanRatioMax=parseInt(data2.data.prodLoanRatioMax);
				container.riskAccount = parseInt(container.loanAccount/container.prodLoanRatioMax);
				//最大借款额度
				container.prodAmountMax = data2.data.prodAmountMax/100;
				//最小借款额度
				container.prodAmountMin = data2.data.prodAmountMin/100;
				$('#loanAccount').text(container.loanAccount);
				$('#riskAccount1').text(container.riskAccount);
				$('#riskAccount').text(container.riskAccount);
				$('#allAccount').text(parseInt(container.loanAccount+container.riskAccount));
			}else{
				dialog(data2.msg);
				
			}
		}
	});
	//充值
	function toRecharge(){
		$('#trustPanel').css({
			"display" : "none"
		});
		$('#selfDetail').css({
			"display" : ""
		});
		container.avalible_amount = container.avalible_amount||0;
		$('#selfAccount').text(container.avalible_amount);
		$('#selfPass').css({
			"display" : "none"
		});
		$('#addBtn2').parent().css({
			"display" : "none"
		});	
	}
	//实名认证	
	function  toMakeTrust(){
		$('#riskAccountP').hide();
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
	$('#addBtn1').click(function() {
		$('#panel1').css({
			"display" : "none"
		});
		
		$('.am-footer').removeClass('am-footerBtn');
		$('#addBtn1').css({
			"display" : "none"
		});
		$('#panel2').css({
			"display" : ""
		});
		
		$('.ui-step-3 li').removeClass('ui-step-active');
		$('.ui-step-3 li').eq(1).addClass(
		'ui-step-active');
		$.commAjax({
			url : basePath + '/user',
			type : "get",
			data : {},
			success : function(data2) {
				if (data2.success) {
					var is_trust = data2.data.isTrust;
					
					// 已经实名
					if (is_trust == '1') {
						$.commAjax({
							url : basePath + "/account",
							type : "get",
							data : {},
							success : function(data2) {
								if (data2.success) {
									
									container.avalible_amount = (data2.data.avalaibleAmount-data2.data.freezeAmount)/100;
								
									// 余额大于100
									if ((container.avalible_amount!='')&&(parseInt(container.avalible_amount) >= container.riskAccount)) {
										//融资 申请
										$('#trustPanel').css({
											"display" : "none"
										});
										$('#selfDetail').css({
											"display" : "none"
										});
										container.avalible_amount = container.avalible_amount||0;
										$('#selfAccount1').html(container.avalible_amount);
										$('#selfPass').css({
											"display" : ""
										});
										$('#addBtn2').parent().css({
											"display" : ""
										});	
										
										
										$('#go_back').unbind("click");
										$('#go_back').click(function(){
											$('.ui-step-3 li').removeClass('ui-step-active');
											$('.ui-step-3 li').eq(0).addClass(
											'ui-step-active');
											$('#panel1').show();
											
											$('.am-footer').addClass('am-footerBtn');
											$('#addBtn1').show();
											$('#panel2').hide();
											
											$('#go_back').unbind("click");
											$('#go_back').click(function(){
												history.go(-1);
											});
										});
					
									} else {										
										//充值
										toRecharge();
										
									}
								} else {
									//充值
									toRecharge();					
								}
							}
						});
	
					} else {
						//实名认证
						toMakeTrust();
					
					}
				} else {
					//实名认证
					window.location.href = mobileUrl+"/user/login.htm?tg="+location.href;
	
				}
			}
		});
		
	});
	
	$('#addBtn2').click(function() {
	var $password = $('#password');
		
		if($password.val()==''){
			$.dialog({
				 content:"安全密码不能为空",
				 title:"alert",
				 ok:function(){
					 $password.focus();
				 }
			 });
				
			
			return  ;
		}
		
		$('.ui-step-3 li').removeClass('ui-step-active');
		$('.ui-step-3 li').eq(1).addClass(
		'ui-step-active');
		
	
	
		$('#go_back').unbind("click");
		$('#go_back').click(function(){
			history.go(-1);
		});
		var dialogLoad = $.dialog();
		$.commAjax({
			url : container.rsa,
			success : function(data) {
				if (!data.success) {
					dialogLoad.close();
					dialog("系统错误");					
					return;
				}
				var modulus = data.data.modulus;						
				
				var exponent = data.data.exponent;
				
				var key = RSAUtils.getKeyPair(exponent, "", modulus);
				
				// 执行借款
			$.commAjax({
					url : basePath + '/pz/loan/apply',
					type : "post",
					data : {
						trade_pwd : RSAUtils.encryptedString(key, $password.val()),
						produce_id : proFreeId,
						produce_term : container.productItem.prodTerms,
						deposit_amount : container.riskAccount*100,
						loan_amount : container.loanAccount*100,
						prev_day : 2
					},
					success : function(data2) {
						dialogLoad.close();
						if (data2.success) {
							// 请求成功更新页面信息
							$('.ui-step-3 li').removeClass('ui-step-active');
							$('.ui-step-3 li').eq(2).addClass(
							'ui-step-active');
							$('#panel3').css({
								"display" : ""
							});
							$('#panel2').css({
								"display" : "none"
							});	
							
						

						} else {	

							$.dialog({
								content:data2.msg,
								title:"alert",
								ok:function(){}
							});
							
						}
					}

				});
			}
		});

		
	});
});