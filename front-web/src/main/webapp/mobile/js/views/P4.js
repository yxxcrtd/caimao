var container = {};

$(function() {

	container.rsa = basePath + "/sec/rsa";
	// 接口地址 get
	container.contestInfoUrl = basePath + "/contest/info";
	container.rewardInfoUrl = basePath + "/contest/reward";
	container.resultInfoUrl = basePath + "/contest/result";
	container.productUrl = basePath + "/product";
	container.productDetailUrl = basePath + "/proddetail";
	container.time = basePath + "/time";

	// 产品ID
	var contestId = new Array(),prodId  =  new Array();
	// 比赛当前与下一季
	var state = new Array(0, 1);
	var currentState = 0;
	init();
	dealAjax1();
	function dialog(msg){
		$.dialog({
			 content:msg,
			 title:"alert",
			 ok:function(){}
		 });
	}
	function init() {
		$('#header1').css({
			"display" : ""
		});
		$('#header2').css({
			"display" : "none"
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

	}

	// 选择当前的比赛季度
	$('#selectTab li').click(function() {
	
		$('#selectTab li').removeClass("active");
		$(this).addClass("active");
		
	});
	
	$('#select1').click(function(){
		currentState = 0;
		
	
		 dealAjax1();
	});
	$('#select2').click(function(){
		currentState = 1;
		
		 dealAjax1();
	});
		// 显示面版控制
	function showPanel1() {
		$('#ol_steps li').removeClass('ui-step-active');
		$('#ol_steps li').eq(0).addClass('ui-step-active');
		
		
		$('.am-footer').addClass('am-footerBtn');
		$('#panel1').show();
		$('#addBtn1').show();

		$('#panel2').hide();
		
		$("#go_back").unbind("click");
		$("#go_back").click(function(){
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
		
		$("#go_back").unbind("click");
		$("#go_back").click(function(){
			showPanel1();
		});

	}
	function action1(data2){
		if (data2.success) {
			container.contestInfo = data2.data;
			contestId[currentState] = data2.data.contestId;
			prodId[currentState] = data2.data.prodId;
			container.contestBeginDate = data2.data.contestBeginDate;
			container.contestEndDate = data2.data.contestEndDate;
			container.rankingDate = data2.data.rankingDate;
			$('#startEndDate').html(
					container.contestBeginDate +  " ~ "
							+ container.contestEndDate);
			$('#contestUserNum').html(data2.data.contestUserNum);
			$('#startDate').html(container.contestBeginDate);
			$('#stopDate').html(container.contestEndDate);
			$('#resultDate').html(container.rankingDate);

		} else {
			if(data2!=null){
			dialog(data2.msg);
			}
			
		}
	}
	
	$.commAjax({
		url : container.contestInfoUrl,
		type : "get",
		data : {
			state : state[0]
		},
		success : function(data2){
			if(data2.data instanceof Array){
				container.contestInfo1 = data2.data[0];
			}else{
				container.contestInfo1 = data2.data;
			}
			$("#select1").html("<a>"+container.contestInfo1.contestName+"</a>");
			
			dealAjax1()
		}
	});
	$.commAjax({
		url : container.contestInfoUrl,
		type : "get",
		data : {
			state : state[1]
		},
		success : function(data2){
			
			if(data2.data instanceof Array){
				container.contestInfo2 = data2.data[0];
			}else{
				container.contestInfo2 = data2.data;
			}
			$("#select2").html("<a>"+container.contestInfo2.contestName+"</a>");
		}
	});
	
	
	function dealAjax1(){
		
		if (currentState == 0){
			container.contestInfo = container.contestInfo1;
		}else{
			container.contestInfo = container.contestInfo2;
		}
		if(container.contestInfo == null){
			return;
		}
	
		contestId[currentState] = container.contestInfo.contestId;
		prodId[currentState] = container.contestInfo.prodId;
		container.contestBeginDate = container.contestInfo.contestBeginDate;
		container.contestEndDate = container.contestInfo.contestEndDate;
		container.rankingDate = container.contestInfo.rankingDate;
		$('#startEndDate').html(
				container.contestBeginDate + " ~ "
						+ container.contestEndDate);
		$('#contestUserNum').html(container.contestInfo.contestUserNum);
		$('#startDate').html(container.contestBeginDate);
		$('#stopDate').html(container.contestEndDate);
		$('#resultDate').html(container.rankingDate);

		$('#addBtn1').hide();
		$('#addBtn3').hide();
		$.commAjax({
			url:container.time,
			success:function(data){
				if(data.success){
					var today = new Date(data.data.replace(/-/g,"/"))
					
					var endDate = new Date(container.contestEndDate.replace(/-/g,"/"));

					endDate.setDate(endDate.getDate()+1);
					var BeginDate = new Date(container.contestBeginDate.replace(/-/g,"/") );
					
					if(today > endDate){	
						$('#addBtn3').show();
					}else if(today < BeginDate){
						$('#addBtn0').show();
					}else{

						$('#addBtn1').show();
					}
				}
			}
		});
	
		

		var commAjax4 = $.commAjax({
			url : container.productUrl,
			type : "get",
			data : {
				product_id : prodId[currentState]
			}

		});
		

		var commAjax5 = $.commAjax({
			url : container.productDetailUrl,
			type : "get",
			data : {
				product_id : prodId[currentState]
			}

		});
		

		$.when(commAjax4,commAjax5).then(function(data1,data2){
			
			if(data1&&data2){
			
				action4(data1[0]);
				action5(data2[0]);
			}else{
				dialog(data1);
				dialog(data2);
			}
					
		});	
		 dealAjax2();
		 dealAjax3();

	}

	function  dealAjax2 (){
		 $.commAjax({
			url : container.rewardInfoUrl,
			type : "get",
			data : {
				contest_id : contestId[currentState]
			},
			success : function(data2) {
				
				if(data2==null){
					$('#rankAmount1').html(0);
					$('#rankAmount2').html(0);
					$('#rankAmount3').html(0);
					return;
				}
				
				
				if (data2.success) {
					container.rewardInfo = {};
					for(var i in data2.data){
						container.rewardInfo[data2.data[i].rank] = data2.data[i].rewardAmount;
					}
					$('#rankAmount1').html(container.rewardInfo[1]/100||"")
					$('#rankAmount2').html(container.rewardInfo[2]/100||"");
					$('#rankAmount3').html(container.rewardInfo[3]/100||"");
					return;
				} else {
					//dialog(data2.msg);
				}
			}
		});
	}
	function dealAjax3(){
		$.commAjax({
			url : container.resultInfoUrl,
			type : "get",
			data : {
				product_id : prodId[currentState]
			},
			success : function(data2) {
			
				if(data2==null){
					
					return;
				}
				if (data2.success) {
					container.resultInfo = data2.data;
				} else {
					
						//dialog(data2.msg);
						
				}
			}
		});
	}
	
	function action4(data2) {
		if (data2.success) {
			container.product = data2.data;
			container.loanAmount = data2.data.prodAmountMax / 100 || 0;
			container.prodLoanRatioMax = data2.data.prodLoanRatioMax || 1;
			container.riskAmount = parseInt(container.loanAmount/container.prodLoanRatioMax);
			container.prodTerms = data2.data.prodTerms;
			$('#loanAmount').html(container.loanAmount);
			$('#riskAmount').html(container.riskAmount);
			$('#allAmount').html(
					parseInt(container.riskAmount + container.loanAmount));
		} else {
			if(data2!=null){
				//dialog(data2.msg);
				}
		}
	}


	function action5(data2){
		if (data2.success) {
			container.exposureRatio = data2.data[0].exposureRatio;
			$('#exposureAmount').html(container.exposureRatio*container.loanAmount);
			container.fee = data2.data[0].fee;
			$("#fee").html(parseInt(container.fee/100));

		} else {
			if(data2!=null){
				//dialog(data2.msg);
			}
		}
	}
	
	
$("#addBtn1").click(
			
			function() {				
				// 查询可用额度
				$.commAjax({
					url : basePath + '/user',
					type : "get",
					data : {},
					success : function(data2) {
						if (data2.success) {
							showPanel2();
				
							$('#riskAmount1').html(container.riskAmount);
							$("#counterFee").html(parseInt(container.fee/100));
						
							$('#header1').css({
								"display" : "none"
							});
							$('#header2').css({
								"display" : ""
							});
							$('#ol_steps li').removeClass('ui-step-active');
							$('#ol_steps li').eq(1).addClass('ui-step-active');
							
							var is_trust = data2.data.isTrust;							
							// 已经实名
							if (is_trust == '1') {
								$.commAjax({
									url : basePath + "/account",
									type : "get",
									data : {},
									success : function(data2) {
										if (data2.success) {
											container.avalible_amount= (data2.data.avalaibleAmount-data2.data.freezeAmount)/100;
											
											// 余额大于100
											if ((container.avalible_amount!='')&&(parseInt(container.avalible_amount) >= container.riskAmount)) {
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
						} else {
							//未登录
							window.location.href = mobileUrl+"/user/login.htm?tg="+location.href;
			
						}
					}
				});
			});
		//确认按日融资
	$('#addBtn2').click(function() {
		var $password = $('#password');
		
		if($password.val()==''){
			dialog("安全密码不能为空");
			$password.focus();			
			return  ;
		}
		


		var dialog = $.dialog();
		$('#go_back').unbind("click");
		$('#go_back').click(function(){
			history.go(-1);
		});
		$.commAjax({
			url : container.rsa,
			success : function(data) {
				if (!data.success) {
					dialog.close();
					dialog("系统错误");
					
					return;
				}
				var modulus = data.data.modulus;						
				
				var exponent = data.data.exponent;
				
				var key = RSAUtils.getKeyPair(exponent, "", modulus);
				
				// 执行借款
			$.commAjax({
					url : basePath + '/financing/loan/apply',
					type : "post",
					data : {
						trade_pwd : RSAUtils.encryptedString(key, $password.val()),
						produce_id : proContestId,
						produce_term : container.prodTerms,
						deposit_amount : container.riskAmount*100,
						loan_amount : container.loanAmount*100,
						prev_day :0
					},
					success : function(data2) {
						dialog.close();
						if (data2.success) {
							$('#panel2').css({
								"display" : "none"
							});	
							$('#panel3').css({
								"display" : "none"
							});	
								
							$('#ol_steps li').removeClass('ui-step-active');
							$('#ol_steps li').eq(2).addClass(
							'ui-step-active');
							
							// 请求成功更新页面信息	
							$('#ol_steps li').removeClass('ui-step-active');
							$('#ol_steps li').eq(2).addClass(
									'ui-step-active');

							$('#panel4').css({
								"display" : ""
							});
							$('#panel2').css({
								"display" : "none"
							});
							$('#panel3').css({
								"display" : "none"
							});
						

						} else {
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
	//充值
	function toRecharge(){
		$('#trustPanel').css({
			"display" : "none"
		});
		$('#selfDetail').css({
			"display" : "none"
		});
		container.avalible_amount = container.avalible_amount||0;
		$('#selfAccount').html(container.avalible_amount);
		$('#selfAccount1').html(container.avalible_amount);
		$('#selfPass').css({
			"display" : ""
		});
		$('#addBtn2').css({
			"display" : "none"
		});	
	}
	//实名认证
	
	function  toMakeTrust(){
		$("#riskAmountP").hide();
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
	
	//允许融资
	   function goLoanAvalible(){
		   $('#trustPanel').css({
				"display" : "none"
			});
			$('#selfDetail').css({
				"display" : ""
			});		
		
			container.avalible_amount = container.avalible_amount||0;
			$('#selfAccount1').html(container.avalible_amount);
			$('#selfAccount').html(container.avalible_amount);
			$('#selfPass').css({
				"display" : "none"
			});
			$('#addBtn2').css({
				"display" : ""
			});	
	   }

});
