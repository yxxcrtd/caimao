$(function() {
	var container = {};
	$('#panel2').css({
		"display" : "none"
	});
	$('#panel3').css({
		"display" : "none"
	});
	$('#panel4').css({
		"display" : "none"
	});
    proDayId = 4;
	container.productUrl = basePath + "/pz/product?product_id="+proDayId;
	container.productDetailUrl = basePath + "/pz/proddetail?product_id="+proDayId;
	container.rsa = basePath + "/sec/rsa";
	container.selectAmount=[10000, 20000, 30000, 40000, 50000, 100000, 150000, 250000];
	container.selectAmountStr=["1万", "2万", "3万", "4万", "5万", "10万", "15万", "25万"];

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

	
	function action1(data2){
		if (data2.success) {
			$("#loanAccount").attr("placeholder","最少"+parseFloat(data2.data.prodAmountMin/100).toFixed(0).toLocaleString()
			+","+"最多"+parseFloat(data2.data.prodAmountMax/1000000).toFixed(0).toLocaleString()+"万");
			// 请求成功更新页面信息
			container.prodLoanRatioMax = data2.data.prodLoanRatioMax || 10;
			container.prodLoanRatioMin = data2.data.prodLoanRatioMin || 3;
			container.loan_prodTerms = data2.data.prodTerms;
			//最大借款额度
			container.prodAmountMax = data2.data.prodAmountMax/100;
			//最小借款额度
			container.prodAmountMin = data2.data.prodAmountMin/100;
			//设置默认比例
			container.selectedRatio=container.prodLoanRatioMin;
			
			$("#prodNote").html(data2.data.prodNote);
			
		} else {
			// 请 求不成功，跳转错误页面
			$.dialog({
				 content:data2.msg,
				 title:"alert",
				 ok:function(){}
			 });
		}
	}
	//请求产品信息
	var commAjax1=$.commAjax({
		url : container.productUrl
	});
	function action2(data2){
		if (data2.success) {
			// 请求成功更新页面信息
			container.detailValue = data2.data;
			if(container.detailValue!=null){
				var ratios=[];
				var len = container.detailValue.length;
				for(var i = 0 ; i < len ; i++){
					var item = container.detailValue[i];	
					 ratios.push(item.loanRatioFrom);				
					}
				container.ratios = ratios;
			}
		} else {
			// 请 求不成功，跳转错误页面
			dialog(data2.msg);
		}
	}
	// 请求产品详情信息
	var commAjax2=$.commAjax({
		url : container.productDetailUrl
	});
	$.when(commAjax1,commAjax2).then(function(data1,data2){
		action1(data1[0]);
		action2(data2[0]);
	});
	container.agree = true;
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
	
	var $obj = $('#loanAccount');
	
	function loanChangeAction(){
		var value = $obj.val();
		container.loanAccount = $.parseMoney(value);
		//风险保证金列表变化
		interestRate();
		//风险比例变化
		getRatioItem();
		//警示线变化 
		upateItemHtml();
	}
	
	//自己输入借款金额
   $obj.keyup(function(){
		 loanChangeAction();
	});
   
	$obj.blur(function(){
		var loanAccount = parseInt($.parseMoney($obj.val())/proMultiple)*proMultiple;
		$obj.val(loanAccount.toLocaleString());
		loanChangeAction();
	});
   $('#prev_store').blur(function(){
	   var value = $('#prev_store').val();
	   if(value<2||value>200){
		   $.dialog({
				 content:"最小预存2天,最大预存200天。",
				 title:"alert",
				 ok:function(){}
			 });
		   return;
	   }
	   $('#prev_store').val(parseInt(value));
	   var interestRate =parseFloat(container.loanAccount*container.interestDayRate).toFixed(2);
	   $('#prev_amount').html(interestRate.split(".")[1]=="00"?parseFloat(interestRate).toFixed(0):interestRate||0);
   });
	//借款金额变化
	
	$("#addBtn1").click(
			function() {
                if (closePZ == true) return false;
				 if(!container.agree){
					 return;
				 }
				if(container.selectedRatio = null){
					$.dialog({
						 content:"请选择可融资金!",
						 title:"alert",
						 ok:function(){
						 }
					 });
				}
				$('#riskAmount1').html(parseInt(container.riskValue));
				var amount = $.parseMoney($('#loanAccount').val());
				if (amount < container.prodAmountMin||amount >= container.prodAmountMax) {
					$.dialog({
						 content:"借款金额最小值为"+container.prodAmountMin+"元,最大金额小于"+parseFloat(container.prodAmountMax/10000).toFixed(0)+"万!",
						 title:"alert",
						 ok:function(){
							 $('#loanAccount').focus();
						 }
					 });
					return;
				}
				var interestRate =parseFloat(container.loanAccount*container.interestDayRate).toFixed(2)
				var totalValue = parseInt(container.riskValue)+parseFloat(interestRate);
				$('#prev_amount').html(interestRate.split(".")[1]=="00"?parseFloat(interestRate).toFixed(0):interestRate||0);
				// 查询可用额度
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
											var avalible_amount = (data2.data.avalaibleAmount-data2.data.freezeAmount)/100;
											container.avalible_amount = avalible_amount;
											// 余额大于100
											if ((avalible_amount!='')&&(parseInt(avalible_amount) >= totalValue)) {
												//融资 申请
												goLoanAvalible();
							
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
							showPanel2();
						} else {
							//登录
							window.location.href = mobileUrl+"/user/login.htm?tg="+location.href;
						}
					}
				});
			});
		//确认按日融资
	$('#addBtn2').click(function() {
		var value = $('#prev_store').val();
		if(value==''||parseInt(value)<2){
			$.dialog({
				 content:"预存天数至少为2天！",
				 title:"alert",
				 ok:function(){
					 $('#prev_store').focus();
				 }
			 });				  
			
			return;
			}	
	var $password = $('#password');
		if($password.val()==''){
			$.dialog({
				 content:"安全密码不能为空！",
				 title:"alert",
				 ok:function(){
					 $password.focus();
				 }
			 });
			return  ;
		}
		var dialogLoad = $.dialog();
		$('#go_back').unbind("click");
		$('#go_back').click(function(){
			history.go(-1);
		});
		$.commAjax({
			url : container.rsa,
			success : function(data) {
				if (!data.success) {
					dialogLoad.close();
					$.dialog({
						 content:"系统错误！",
						 title:"alert",
						 ok:function(){}
					 });
					
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
						produce_id : proDayId,
						produce_term : container.loan_prodTerms,
						deposit_amount : parseInt(container.riskValue)*100,
						loan_amount : parseInt(container.loanAccount*100),
						prev_day :$('#prev_store').val()||2
					},
					success : function(data2) {
						dialogLoad.close();
						if (data2.success) {
							// 请求成功更新页面信息	
							$('#ol_steps li').removeClass('ui-step-active');
							$('#ol_steps li').eq(2).addClass('ui-step-active');
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
	//允许融资
   function goLoanAvalible(){
	   $('#trustPanel').css({
			"display" : "none"
		});
		$('#selfDetail').css({
			"display" : "none"
		});		
		container.avalible_amount = container.avalible_amount||0;
		$('#selfAccount1').html(parseFloat(container.avalible_amount).toFixed(2));
		$('#selfPass').css({
			"display" : ""
		});
		$('#addBtn2').parent().css({
			"display" : ""
		});	
   }
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
		$("#riskPanel").hide();
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
		
	//取得警示线和平仓 线
	function getRatioItem(){
		var seletRatio = container.selectedRatio;
		var len = container.detailValue.length;
		for(var i = 0 ; i < len ; i++){
			var item = container.detailValue[i];
			if (seletRatio >= item.loanRatioFrom && seletRatio <= item.loanRatioTo) {
				container.interestDayRate = item.interestRate;
				container.prodDetailItem = item;
				break;
			} else {
				container.interestDayRate = null;
				container.prodDetailItem = null;
			}			
		}
	}
	
	var n = 0;
	//计算风险保证金
	function interestRate() {
		var ratios= container.ratios;
		var amount = container.loanAccount;
		var len=ratios.length;
		var htmlContent="";
		container.selectedRatio = null;
		container.riskValue = null;
		if(n>=len){
			n=0;
		}
		for(var i= 0 ; i < len;i++){
			if(ratios[i]==0){
				ratios[i]=1;
			}
			var value=parseFloat(amount/ratios[i]);
			if(amount*100 < container.detailValue[i].loanAmountFrom  || amount*100 >= container.detailValue[i].loanAmountTo){
				continue;
			}
			if(i==n){
				container.selectedRatio= ratios[i];
				container.riskValue= value;
			    htmlContent+="<li class='list-amoutCompetition-item list-amoutCompetition-item-left active 111111111'>";
			}else{
				htmlContent+="<li class='list-amoutCompetition-item list-amoutCompetition-item-left'>";	
			}
			
		    htmlContent+="<a>"+parseInt(value)+"元</a>";
		    htmlContent+="<input type='hidden' value='"+ratios[i]+"'/>";
		    htmlContent+="<input type='hidden' value='"+parseInt(value)+"'/></li>";	
		}
	     $('#riskAmount').html(htmlContent);
	     
	     $("#riskAmount li").click(function() {
			 $("#riskAmount li").removeClass('active');
			 n = $("#riskAmount li").index($(this));
			$(this).addClass('active');
			container.selectedRatio=$("input", this).eq(0).val();
			container.riskValue = $("input", this).eq(1).val();	
			//风险比例变化
			getRatioItem();
			//警示线变化 
			upateItemHtml();
		});
				
	}
	
	//变更日利息、警示线、平仓线
	function upateItemHtml(){
		// 利息	
		var dayRatioValue = parseFloat(container.interestDayRate*container.loanAccount).toFixed(2);
		console.info(dayRatioValue);
		$('#dayRatioValue').html(dayRatioValue.split(".")[1]=="00"?parseInt(dayRatioValue):dayRatioValue||0);
		var totalAccount=parseInt(container.loanAccount)+parseInt(container.riskValue);
		
		
		$('#total_account').html(totalAccount||0);
		$('#enable_account').html(container.prodDetailItem==null?0:parseInt(container.loanAccount*container.prodDetailItem.enableRatio));
		// 止损线
		$('#exposure_account').html(container.prodDetailItem==null?0:parseInt(container.loanAccount*container.prodDetailItem.exposureRatio));		
	}
});