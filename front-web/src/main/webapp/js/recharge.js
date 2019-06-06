$(function(){
	$("#jsTopMenu > li:eq(3) > a").addClass("hov");
	$(".left_bar > ul > li:eq(1) > a").addClass("hov");
	$("#cNum").keyup(function(){
		var val = $(this).val();
		val =toFloat(val);
		$(this).val(val);
		$("#chineseNum").val(getChangeNum(val));
	});
	$("#cNum").focus(function(){
		var val = $(this).val();
		$(this).val(removeCommas(val));
	});
	$("#cNum").blur(function(){
		var val = $(this).val();
		$(this).val(addCommas(val));
	});
	//支付宝
	$(".fu4 > .btn").click(function(){
		var number = $("#zhifuNum").val();
		var amount = removeCommas($("#cNum").val());
		$.post("/account/charge",{pay_company_no:-1,order_abstract:number,charge_amount:amount*100,terminal_type:0,pay_type:-1},function(data){
			if(data.success){
				$("#jsConfirmAccount").html(number);
				$("#jsConfirmNum").html(addCommas(amount));
				$(".jsfrom").hide();
				$(".jsconfirm").show();
			}
		});
	});
	//银行转账
	$(".me10 > p > .btn").click(function(){
		var remark = $("#remark").val();
		var bank = $("#bank").val();
		var amount = removeCommas($("#cNum").val());
		$.post("/account/charge",{pay_company_no:-2,order_abstract:"入账银行："+bank+" 备注："+remark,charge_amount:amount*100,terminal_type:0,pay_type:-2},function(data){
			if(data.success){
				$(".jsfrom").hide();
				$(".jsconfirm").show();
			}
		});
	});
	//选择银行
	$(".jselect").click(function(){
			$(".jselect").removeClass("hov");
			$(this).addClass("hov");
	});
	//网银充值
	$(".wang3 > button > a").click(function(){
		if($(".jselect").parent().find(".hov").length<=0){
			alert("请选择银行！");
			return false;
		}
		$("#jsBank").attr("src",$(".jselect").parent().find(".hov > a > img").attr("src"));
		$("#jsAmount").html($("#cNum").val());
		$("#jsTotal").html($("#cNum").val());
		$(".jsfrom").hide();
		$(".jsconfirm").show();
	});
	
	$("#jsCancel").click(function(){
		$(".jsconfirm").hide();
		$(".jsfrom").show();
	});
	
	$("#jsConfirm").click(function(){
		var bank = $(".jselect").parent().find(".hov").attr("no");
		var amount = removeCommas($("#cNum").val());
		$.post("/account/charge/heepay",{charge_amount:amount,pay_company_no:3,bank_no:bank,pay_type:20,terminal_type:0},function(response){
			if(response.success){
				$("#recharge-form_heepay").attr("action",response.data.submitUrl);
                $('input[name=orderCurrency]').val(response.data.orderCurrency);
                $('input[name=orderAmount]').val( response.data.orderAmount);
                $('input[name=orderNo]').val(response.data.orderNo);
                $('input[name=signMsg]').val(response.data.signMsg);
                $('input[name=inputCharset]').val(response.data.inputCharset);
                $('input[name=pickupUrl]').val(response.data.pickupUrl);
                $('input[name=receiveUrl]').val(response.data.receiveUrl);
                $('input[name=version]').val(response.data.version);
                $('input[name=language]').val(response.data.language);
                $('input[name=signType]').val(response.data.signType);
                $('input[name=merchantId]').val(response.data.merchantId);
                $('input[name=payType]').val(response.data.payType);
                $('input[name=orderDatetime]').val(response.data.orderDatetime);
                $('input[name=issuerId]').val(response.data.issuerId);
                $('input[name=pan]').val(response.data.pan);
                $('input[name=ext1]').val(response.data.ext1);
                $('input[name=ext2]').val(response.data.ext2);
                $("#recharge-form_heepay").submit();
			}
		});
	});
})