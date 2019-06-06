// @koala-prepend "./lib/jquery-1.11.1.min.js"
// @koala-prepend "./common/utils.js"
// @koala-prepend "./common/Data.js"
// @koala-prepend "./lib/RSA.js"
// @koala-prepend "./lib/formValidate.js"
// @koala-prepend "./v2/actionTip.js"

$(function () {
	var rechargeInput = $("#cNum"),
		form = $('#withdrawForm'),
		pwd = $("#pwd");

	formValidate.init(form);

	rechargeInput.keyup(function(){
		var val = $(this).val();
		val = CMUTILS.toFloat(val);
		$(this).val(val);
		$("#cNumCh").text(CMUTILS.getChangeNum(val)).show();
	});
	rechargeInput.focus(function(){
		var val = $(this).val();
		$(this).val(CMUTILS.removeCommas(val));
		if(rechargeInput.val() > 0){
			$("#cNumCh").show();
		}
	});
	rechargeInput.blur(function(){
		var val = $(this).val();
		$(this).val(CMUTILS.addCommas(val));
		$("#cNumCh").hide();
	});

	$("#withdrawSubmit").click(function(){
		if (formValidate.validateForm(form) == false) {
			return false;
		}
		$.post("/account/withdraw",{withdraw_amount:CMUTILS.accMul(CMUTILS.removeCommas(rechargeInput.val()), 100),trade_pwd:RSAUtils.encryptedString(pwd.val())},function(response){
			if(response.success){
				window.location.href = "/account/hiswithdraw.htm";
			}else{
				formValidate.showFormTips('withdrawSubmit', response.msg);
			}
		});
	});
});