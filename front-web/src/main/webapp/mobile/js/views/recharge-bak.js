//函数

$(function() {
    pay_company_no = 3;
	var container = {};
	// URL地址
	container.bankUrl = basePath + "/bank?pay_company_no="+pay_company_no;
	//取回调地址
	container.tg = getUrlParam("tg");
	container.accountUrl = basePath + "/account";
	$('#panel2').css({
		"display" : "none"
	});
	$('#panel3').css({
		"display" : "none"
	});
	$('#addBtn3').css({
		"display" : "none"
	});
	
	$('.listLateral li').click(function(){
		var tabText = $("a", this).text();
		if($('.listLateral li').index(this)==0){
			$('#panel1').show();
			$('#addBtn1').parent().show();
			$('#panel3').hide();
			$('#addBtn3').hide();
			$(".am-footer").show();
		}else if($('.listLateral li').index(this)==1){
			$('#panel1').hide();
			$('#panel3').show();
			$('#addBtn1').parent().hide();
			$('#addBtn3').show();
			$('#panel2').hide();
			$(".am-footer").hide();
		}
		
		$(".listLateral li").removeClass("active");
		$(this).addClass("active");
		// 请求事件
		
		
	});
	function dialog(msg){
		$.dialog({
			 content:msg,
			 title:"alert",
			 ok:function(){}
		 });
	}
	function getUrlParam(name)
	{
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
	var r = window.location.search.substr(1).match(reg);  //匹配目标参数
	if (r!=null) return unescape(r[2]); return null; //返回参数值
	} 
	// 请求当前支持的银行卡信息
	//请求帐户信息
	$.commAjax({
		url : container.accountUrl,
		type : "get",
		data : {
		},
		success : function(data2) {

			if (data2.success) {
				// 请求成功更新页面信息
				container.account = data2.data;
				$('#avalaibleAmount').html(parseFloat((data2.data.avalaibleAmount-data2.data.freezeAmount)/100).toFixed(2));
				
				
			} else {
				// 请 求不成功，跳转错误页面
				$.dialog({
					content:"请先实名认证!",
					title:"alert",
					ok:function(){
						window.location.href = mobileUrl + '/user/certification.htm';
					}
				});
			}
		}

	});
	//请求银行卡信息
	$("#addBtn2").click(function() {
		var $this = $("#addBtn1");
		
	    var $charge_amount = $('#charge_amount');
	    container.amount =$.parseMoney($charge_amount.val()); 

		if(container.amount == ""||container.amount==0||parseFloat(container.amount)<0){
			dialog("请保证充值金额大于零！");
			$charge_amount.focus();
			return;
		}
		
		
		var path = container.tg;

        accountChargeUrl = "/account/charge";
        if (pay_company_no == 3) {   // 汇付宝单独的
            accountChargeUrl = "/account/charge/heepay";
        }
		//判断输入 
		$.commAjax({
			url : basePath + accountChargeUrl,
			type : "post",
			data : {
				'charge_amount':parseInt(parseFloat(container.amount)*100),
        		//三方支付公司编号
        		'pay_company_no' : pay_company_no,
        		//银行行别
        		'bank_no':"",/*\rechangeInfoPanel.getValues().bank.bankNo*/
        		'pay_type': '10', // 网银
                'terminal_type': 1
        	
			},
			success : function(data2) {
				if (data2.success) {
					// 获取URL并提交
                    if (pay_company_no == 1) {  // 通联支付
                        var $form = $('#recharge-form');
                        $form.attr("action", data2.data.submitUrl);
                        $('#orderCurrency').val(data2.data.orderCurrency);
                        $('#orderAmount').val(data2.data.orderAmount);
                        $('#orderNo').val(data2.data.orderNo);
                        $('#signMsg').val(data2.data.signMsg);
                        $('#inputCharset').val(data2.data.inputCharset);
                        if(path!=null){
                            $('#pickupUrl').val(window.location.href);
                        }
                        $('#pickupUrl').val(data2.data.pickupUrl);

                        $('#receiveUrl').val(data2.data.receiveUrl);
                        $('#version').val(data2.data.version);
                        $('#version').val(data2.data.version);
                        $('#language').val(data2.data.language);
                        $('#signType').val(data2.data.signType);
                        $('#merchantId').val(data2.data.merchantId);
                        $('#payType').val(data2.data.payType);
                        $('#orderDatetime').val(data2.data.orderDatetime);
                        $('#issuerId').val(data2.data.issuerId);
                        $('#pan').val(data2.data.pan);
                        $('#ext1').val(data2.data.ext1);
                        $('#ext2').val(data2.data.ext2);
                        $form.submit();
                    } else if (pay_company_no == 2) {   // 易宝支付
                        var $form = $('#recharge-form_yibao');
                        $form.attr("action", data2.data.submitUrl);
                        $('#p0_Cmd').val(data2.data.ext1);
                        $('#p1_MerId').val(data2.data.merchantId);
                        $('#p2_Order').val(data2.data.orderNo);
                        $('#p3_Amt').val(data2.data.orderAmount);
                        $('#p4_Cur').val(data2.data.orderCurrency);
                        $('#p8_Url').val(data2.data.receiveUrl);
                        $('#pa_MP').val(data2.data.ext2);
                        $('#pd_FrpId').val(data2.data.pan);
                        $('#hmac').val(data2.data.signMsg);
                        $form.submit();
                    } else if (pay_company_no == 3) { // 汇付宝支付
                        var $form = $('#recharge-form_heepay');
                        //$form.attr("action", data2.data.submitUrl);
                        $form.attr("action", "http://211.103.157.45/PayHeepay/Payment/Index.aspx");
                        $('#heepay_version').val(data2.data.version);
                        //$('#is_phone').val(data2.data.isPhone);
                        $('#pay_type').val(data2.data.payType);
                        $('#pay_code').val(data2.data.payCode);
                        $('#agent_id').val(data2.data.agentId);
                        $('#agent_bill_id').val(data2.data.agentBillId);
                        $('#pay_amt').val(data2.data.payAmt);
                        $('#notify_url').val(data2.data.notifyUrl);
                        $('#return_url').val(data2.data.returnUrl);
                        $('#user_ip').val(data2.data.userIp);
                        $('#agent_bill_time').val(data2.data.agentBillTime);
                        $('#goods_name').val(data2.data.goodsName);
                        $('#goods_num').val(data2.data.goodsNum);
                        $('#remark').val(data2.data.remark);
                        $('#goods_note').val(data2.data.goodsNote);
                        $('#sign').val(data2.data.sign);
                        $form.submit();
                    }

				} else {
					// 请 求不成功，跳转错误页面
					dialog(data2.msg);
				}
			}

		});					
	});
	
});