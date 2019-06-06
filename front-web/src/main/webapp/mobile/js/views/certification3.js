//实名认证身份证上传
var container = {};
// 身份证图片上传
container.idCardUploadUrl = basePath + "/user/idcard/upload";

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


$(function(){
	var data  = {};
	if(isTrueCertification){
		$("#nextBtn").show();
	}else{
		container.positiveBool = true;
		container.oppositeBool = true;
		$("#sureBtn").show();
	}
	
	
	$("#positiveBtn").click(function(){
		$("#positive").click();
	});
	$("#oppositeBtn").click(function(){
		$("#opposite").click();
	});
	//图片上传预览
    new uploadPreview({ UpBtn: "positive", DivShow: "positivediv", ImgShow: "positiveShow" });
    new uploadPreview({ UpBtn: "opposite", DivShow: "oppositediv", ImgShow: "oppositeShow" });

	$("#positive").change(function(){
		container.positiveBool = true;
		$("#positiveBtn p").hide();
		$("#positivediv").show();
	});
	
	$("#opposite").change(function(){
		container.oppositeBool = true;
		$("#oppositeBtn p").hide();
		$("#oppositediv").show();
	});
	
	$("#idCardNumber").blur(function(){
		if(	$.certificateValidate($(this).val())==0){
			container.idCardBool = true;
			$("#cardRight").html('<i class=" type-icon icon-successLogin"></i>');
			
		}else{
			container.idCardBool = false;
			$("#cardRight").html('<i class=" type-icon icon-errorLogin"></i>');
			
		}
		//<i class=" type-icon icon-errorLogin"></i>
	});
	
	//下一步按钮事件
	$("#nextBtn").click(function(){
		if($("#userName").val().trim() ==""){
			$.dialog({
				content:"请输入用户名",
				title:"alert",
				ok:function(){}
			});
			return;
		}

		if(!container.idCardBool){
			$.dialog({
				content:"请输入正确的身份证号码",
				title:"alert",
				ok:function(){}
			});
			return;
		}
		
		if(!container.idCardBool){
			$.dialog({
				content:"请输入正确的身份证号码",
				title:"alert",
				ok:function(){}
			});
			return;
		}
		
		
		$(this).hide();
		$("#sureBtn").show();
		$("#panel1").hide();
		$("#panel2").show();
	});
	$("#sureBtn").click(function(){
		$("#userRealName").val($("#userName").val());
		$("#idcard").val($("#idCardNumber").val());
		if(!container.positiveBool){
			$.dialog({
				content:"请上传您的身份证正面",
				title:"alert",
				ok:function(){}
			});
			return;
		}
		

		if(!container.oppositeBool){
			$.dialog({
				content:"请上传您的身份证反面",
				title:"alert",
				ok:function(){}
			});
			return;
		}
		var dialog = $.dialog();
		$("#idFrom").ajaxSubmit({
			url:container.idCardUploadUrl,
			type:"post",
			dataType:"json",
			success:function(data){
				dialog.close();
				if(data.success){
					$.dialog({
						content:"提交成功！请耐心等待审核",
						title:"ok",
						ok:function(){
							window.location.href= mobileUrl +"/index.htm";
						}
					});
				}else{
					$.dialog({
						content:data.msg,
						title:"alert",
						ok:function(){}
					});
				}
			}
			
		});
		
	
	});
	
});