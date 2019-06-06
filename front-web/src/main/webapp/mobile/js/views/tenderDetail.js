
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
$(function() {
	var container = {};
	container.rsa = basePath + "/sec/rsa";
	var inv_id = getUrlParam("invs");
	container.tenderDetailUrl =basePath+"/p2p/subject/detail?id="+getUrlParam("invs");
	container.tenderUserUrl =basePath+"/p2p/payuser?invs_id="+inv_id+"&start=0&limit=1000";	
	function selfDialog(msg){
		$.dialog({
			 content:msg,
			 title:"alert",
			 ok:function(){}
		 });
	}

	container.agree = true;
	//	是否同意xx服务协议
	$("#agree_checkbox").click(function(){
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
	
	function missString(str,pre,end){
		if(str==''||str.length<(parseInt(pre+end))){
			return "";
		}
		var len = str.length;
		
		var middle = len - pre - end;
		var string = str.substr(0,pre);
		
		for(var i = 0 ; i < middle; i++){
			string +="*";
		}
		string +=str.substr(len-end,end+1);
		return string;
	}
	//标的详情
		$.commAjax({
			url : container.tenderDetailUrl,
			type : "get",
			data : {

			},
			success : function(data2) {
				if (data2.success) {
					container.tenderDetail = data2.data;
					container.rate = data2.data.invsRate;
					container.invsAmountPre = data2.data.invsAmountPre;
					//最小投标额
					container.invsAmountMin = data2.data.invsAmountMin/100;
					$('#rate').html(parseFloat(container.rate *100).toFixed(2)+"%");
					$('#invsInterestNode').html(parseFloat(container.rate*container.invsAmountPre/100/12).toFixed(2));
					$('#loanTerm').html(data2.data.invsDuring/30+"个月");
					$('#invsDateRaiseEnd').text(data2.data.invsDateRaiseEnd);
					container.invsAmountActual= data2.data.invsAmountActual;
					container.percent =parseFloat(100*data2.data.invsAmountActual/container.invsAmountPre).toFixed(2)+"%"
					$('#currenPercent').html(container.percent);
					if(data2.data.isAblePay==1){
						$('#panel2').show();
					}
					
					$('#lackAmount').html((container.invsAmountPre-container.invsAmountActual)/100);
					container.invsAmountSafe=data2.data.invsAmountSafe;
					container.invsAmountPre=data2.data.invsAmountPre;
					$('#allAmount').html(parseFloat((container.invsAmountSafe+container.invsAmountPre)/100).toFixed(2));
					
					$('#invsAmountPre').html(container.invsAmountPre/100);
					$('#invsAmountPre2').html(container.invsAmountPre/100);
					$('#riskAmount').html(container.invsAmountSafe/100);
					$('#createDatetime').html(container.tenderDetail.registerDatetime||"");
					$('#invrCount').html(container.tenderDetail.invrCount);
					$('#loanNumber').html(container.tenderDetail.loanNumber);
					$('#repayNumber').html(container.tenderDetail.repayNumber);
					
					$('#payAmount').attr("defaultvalue",container.invsAmountMin);
					$('#payAmount').attr("placeholder","最小投标额："+container.invsAmountMin+"元");
					$('#userName').html(container.tenderDetail.userRealName.substr(0,1)+"**");
					var phone = missString(container.tenderDetail.userPhone,3,4) ;
				
					$('#userPhone').text(phone);
					
						var  idcard =missString(container.tenderDetail.idcard,3,4) ;
					
					$('#idcard').text(idcard);
					$('#currentAmount').text(container.invsAmountActual/100);
					
				} else {
					selfDialog(data2.msg);

				}

			}

		});	
		//投票人列表
		$.commAjax({
			url : container.tenderUserUrl,
			type : "get",
			data : {

			},
			success : function(data2) {
					if(data2.success){
						container.payUserList = data2.data.items;
						 var len
						 if(container.payUserList){
						 len=container.payUserList.length;
						 }else{
							 len=0;
						 }
						 var htmlContent = "";
						for(var i = 0 ; i < len; i++){
						  var item = container.payUserList[i];
						  var name = item.mobile||"";
						  name = name.substr(0,3)+"****"+name.substr(7,4)
						  htmlContent +="<tr><td class='tb-left'>"+name+"</td>";
						  htmlContent +="<td class='tb-center'>"+parseFloat(item.invrAmountPay/100).toFixed(2)+"元</td>";
						  htmlContent +="<td class='tb-right'>"+item.invrDatePay+"</td>";
						  htmlContent +="</tr>";
						}
						 $("#trList").after(htmlContent);
						
					}else{
						selfDialog(data2.msg);
						}
				}
			
		});
		//用户信息判断
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
									container.avalible_amount= (data2.data.avalaibleAmount-data2.data.freezeAmount)/100;
									
									// 余额大于最小投标额度
									if ((container.avalible_amount!='')&&(parseInt(container.avalible_amount) >= container.invsAmountMin)) {
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
					//登录
					window.location.href = mobileUrl+"/user/login.htm?tg="+location.href;
					toLogin();
	
				}
			}
		});
	//确认投标

$('#addBtn1').click(function() {
	
//	$('#addBtn1').removeClass('am-button-red');

	 if(!container.agree){
		 return;
	 }
	
	
	var $password = $('#password');
	
	if($password.val()==''){
		selfDialog("请输入 安全密码！");		
		$password.focus();
		return  ;
	}
	container.pay_amount = $.parseMoney($('#payAmount').val());
	if(container.pay_amount ==''||container.pay_amount<=0||container.pay_amount>container.avalible_amount||container.pay_amount<container.invsAmountMin){
		selfDialog("投标金额必须大于 "+container.invsAmountMin+"元，小于"+container.avalible_amount+"元！");
		return;
	}
	
	$.commAjax({
		url : container.rsa,
		success : function(data) {
			
			if (!data.success) {
				dialog("系统错误！");	
				return;
			}
			var modulus = data.data.modulus;						
			
			var exponent = data.data.exponent;
			
			var key = RSAUtils.getKeyPair(exponent, "", modulus);
			
			// 执行借款', {
        $.dialog({
        	content:"确定要投标吗",
        	title:"alert",
        	ok:function(){
        		$.commAjax({
    				url : basePath + '/p2p/subject/pay',
    				type : "post",
    				data : {
    					trade_pwd : RSAUtils.encryptedString(key, $password.val()),
    					invs_id: inv_id,
                        pay_amount: parseInt($.parseMoney(container.pay_amount)*100)
    				},
    				success : function(data2) {
    					
    					if (data2.success) {
    						// 请求成功更新页面信息	
    						
    						$.dialog({
    							 content:"投标已经提交，请等待确认！",
    							 title:"ok",							 
    							 ok:function(){
    								 window.location.href = mobileUrl+"/account/moneyCapital.htm";
    							 }
    						 });
    					

    					} else {
    						
    						selfDialog(data2.msg);
    						
    					}
    				}

    			});
        	},
        	cancel:function(){}
        });    
		
		}
	});

	
});

		function goLoanAvalible(){
			   $('#trustPanel').css({
					"display" : "none"
				});
				$('#selfDetail').css({
					"display" : "none"
				});		
				container.avalible_amount = container.avalible_amount||0;
				$('#availableAmount1').text(parseFloat(container.avalible_amount).toFixed(2));
				$('#availableAmount').text(parseFloat(container.avalible_amount).toFixed(2));
				$('#selfPass').css({
					"display" : ""
				});
				$('#addBtn1').css({
					"display" : ""
				});	
				$('#addBtn0').css({
					"display" : "none"
				});	
		}
		function toLogin(){
			$('#trustPanel').css({
				"display" : "none"
			});
			$('#selfDetail').css({
				"display" : "none"
			});
			dealFootPassHtml();
			$('#selfPass').css({
				"display" : "none"
			});
			$('#addBtn1').css({
				"display" : "none"
			});
			$('#addBtn0').css({
				"display" : ""
			});
		}
		function dealFootPassHtml(){
			 
//			$('.am-footer').removeClass('am-footerBtn');
//			$('.am-footer').addClass('am-footerMoney');
		}
		
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
			$('#addBtn0').css({
				"display" : "none"
			});	
			$('#addBtn1').css({
				"display" : "none"
			});
		}
		function toRecharge(){
			$('#trustPanel').css({
				"display" : "none"
			});
			$('#selfDetail').css({
				"display" : ""
			});
			container.avalible_amount = container.avalible_amount||0;
			$('#availableAmount1').text(parseFloat(container.avalible_amount).toFixed(2));
			$('#availableAmount').text(parseFloat(container.avalible_amount).toFixed(2));
			
			
			$('#selfPass').css({
				"display" : "none"
			});
			$('#addBtn0').css({
				"display" : "none"
			});	
			$('#addBtn1').css({
				"display" : "none"
			});	
		}
	
		function getUrlParam(name)
		{
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
		var r = window.location.search.substr(1).match(reg);  //匹配目标参数
		if (r!=null) return unescape(r[2]); return null; //返回参数值
		}
			
});