/*修改用户密码*/
var container = {},key;
// 秘钥接口地址 get
container.rsa = basePath + "/sec/rsa";
// 发送短信验证码
container.smsCode = basePath + "/sms/code";
// 获取用户信息
container.user = basePath + "/user";
//获取所有密保问题
container.pwdquestion = basePath + "/pwdquestion";
// 验证安全密码与密保问题

container.pwdquestioncheck = basePath + "/user/findpwdquestion/check";
// 重置密保问题
container.pwdquestionreset = basePath +"/user/pwdquestion/reset";

// 获取秘钥
$.commAjax({
	url : container.rsa,
	success : function(data) {
		if (!data.success) {
			alert("系统错误");
			return;
		}
		var modulus = data.data.modulus;
		var exponent = data.data.exponent;
		key = RSAUtils.getKeyPair(exponent, "", modulus);

	}
});

//验证码倒计时
function countdown(sec){
	var blackActive = $("#button_black_active label");
	if(sec <= 0){
		$("#button_lineblack_active").hide();
		$("#button_lineblack").show();
	}else{
		$("#button_lineblack_active label").text(sec);
		sec--;
		container.setTimeout = setTimeout("countdown("+sec+")",1000);
	}
	
	
}

//发送验证码
function sendSms(){
	var lastSendTime = new Date($.getCookie("regist_sendTime"));
	var multi = new Date().getTime() - lastSendTime.getTime();
	
	if(multi < 30000){
		$.dialog({
			content:"发送过于频繁请稍后",
			title:"alert",
			ok:function(){}
		})
		clearTimeout(container.setTimeout);
		countdown(countdownSeconds-parseInt(multi/1000));
	}else{
		$.commAjax({
			url :container.smsCode,
			type:"post",
			data:{
				biz_type:7
			},
			success:function(data){
				if(data.success){
					$("#button_lineblack").hide();
					$("#button_lineblack_active").show();
					$.setCookie("regist_sendTime", new Date());
					countdown(countdownSeconds);
				}else{
					$.dialog({
						content:data.msg,
						title:"alert",
						ok:function(){}
					});
				}
			}
		});
	}
	
	
}

$(function() {
	$.commAjax({
		url:container.user,
		success:function(data){
			if(data.success){
				container.mobile = data.data.mobile;
				$("#userName").html(container.mobile.substr(0,3)+"*****" +
						container.mobile.substr(8,3))
			}else{

				$.dialog({
					content:data.msg,
					title:"alert",
					ok:function(){}
				});
			}
		}
	});

	$("#nextBtn").click(function(){
		if($("#smCode").val() == ""){
			$.dialog({
				content:"验证码不能为空",
				title:"alert",
				ok:function(){}
			});
			return;	
		}
		if($("#trade_Pwd").val()==""){
			$.dialog({
				content:"安全密码不能为空",
				title:"alert",
				ok:function(){}
			});
			return;
		}
		$.commAjax({
			url:container.pwdquestioncheck,
			type:"post",
			data:{
				mobile:container.mobile,
				code:$("#smCode").val(),
				tradePwd:RSAUtils.encryptedString(key, $("#trade_Pwd").val())
			},
			success:function(data){
				if(data.success){
					$("#nextBtn").parent().hide();
					$("#panel1").hide();
					$("#findBtn").parent().show();
					$("#panel2").show();
					
					$("#go_back").unbind("click");
					$("#go_back").click(function(){
						$("#nextBtn").parent().show();
						$("#panel1").show();
						$("#findBtn").parent().hide();
						$("#panel2").hide();
						$("#go_back").unbind("click");
						$("#go_back").click(function(){
							history.go(-1);
						});
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

	var questionData;
	$.commAjax({
		url:container.pwdquestion,
		success:function(data){
			if(data.success){
				var options = "";
				questionData = data.data;
				for(var i in questionData){
					options += "<option value='"+questionData[i].questionId+"'>"+questionData[i].questionTitle +"</option>";
				}
				$(".question").html(options);
				$(".question").scroller('destroy').scroller({
					preset:"select",
		    		theme: 'default', //皮肤样式
		            display: 'modal', //显示方式 
		            mode: 'scroller', //日期选择模式
		            lang:'zh',
		            labels: ['Cars','cart']
				});
				$("#question1_dummy").val("点击请选择");
				$("#question2_dummy").val("点击请选择");
				$("#question3_dummy").val("点击请选择");
				$("#question1_dummy").css("border","none");
				$("#question2_dummy").css("border","none");
				$("#question3_dummy").css("border","none");
				$(".question").val("");
			}
		}
	});
	$(".question").change(function(){
		$(".question").not($(this)).each(function(){
			var options = "";
			for(var i in questionData){
				var t = true;
				for(var j =0 ; j < $(".question").size();j++){
					if(questionData[i].questionId == $(".question").eq(j).val() && $(this).val() != $(".question").eq(j).val()){
						t = false;
					}
				}
					
				
				if(t){
					options += "<option value='"+questionData[i].questionId+"'>"+questionData[i].questionTitle +"</option>";
				}
			}
				
			var contran = {};
			for(var j =0 ; j < $(".question").size();j++){
				contran[j] =  $(".question").eq(j).val();
			}
			$(this).html(options);		
			for(var j =0 ; j < $(".question").size();j++){		
				$(".question").eq(j).val(contran[j]);
			}
			
		})
	});
	
	$("#findBtn").click(function(){
		
		if($("#smCode").val() == ""){
			$.dialog({
				content:"验证码不能为空",
				title:"alert",
				ok:function(){}
			});
			return;	
		}
		if($("#trade_Pwd").val()==""){
			$.dialog({
				content:"安全密码不能为空",
				title:"alert",
				ok:function(){}
			});
			return;
		}
		
		if($("#question1").val() == ""){
			$.dialog({
				title:"alert",
				content:"请选择问题1",
				ok:function(){
				}
			});
			return;
		}
		
		if($("#answer1").val() == ""){
			$.dialog({
				title:"alert",
				content:"请输入答案1",
				ok:function(){
				}
			});
			return;
		}
		if($("#question2").val() == ""){
			$.dialog({
				title:"alert",
				content:"请选择问题2",
				ok:function(){
				}
			});
			return;
		}
		
		if($("#answer2").val() == ""){
			$.dialog({
				title:"alert",
				content:"请输入答案2",
				ok:function(){
				}
			});
			return;
		}
		if($("#question3").val() == ""){
			$.dialog({
				title:"alert",
				content:"请选择问题3",
				ok:function(){
				}
			});
			return;
		}
		
		if($("#answer3").val() == ""){
			$.dialog({
				title:"alert",
				content:"请输入答案3",
				ok:function(){
				}
			});
			return;
		}1
		
		$.commAjax({
			url:container.pwdquestionreset,
			type:"post",
			data:{
				mobile:container.mobile,
				code:$("#smCode").val(),
				tradePwd:RSAUtils.encryptedString(key, $("#trade_Pwd").val()),
				question1:$("#question1").val(),
				answer1:$("#answer1").val(),
				question2:$("#question2").val(),
				answer2:$("#answer2").val(),
				question3:$("#question3").val(),
				answer3:$("#answer3").val()
			},
			success:function(data){
				if(data.success){
					$.dialog({
						title:"ok",
						content:"密保问题设置成功",
						ok:function(){
							window.location.href= mobileUrl + "/user/personSet.htm";
						}
					});
				}else{
					
					$.dialog({
						title:"alert",
						content:data.msg,
						ok:function(){
						}
					});
				}
			}
		});
	});
	

	//发送验证码
	$("#button_lineblack").click(function(){
		sendSms();
	});

});