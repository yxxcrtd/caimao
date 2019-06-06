// @koala-prepend "./lib/jquery-1.11.1.min.js"
// @koala-prepend "./common/utils.js"
// @koala-prepend "./common/Data.js"
// @koala-prepend "./lib/RSA.js"
// @koala-prepend "./lib/formValidate.js"
// @koala-prepend "./v2/actionTip.js"
// @koala-prepend "./ux/alert.js"


$(document).ready(function() {

    //修改打开关闭
    $(".title a.detail").click(function(){
        var $continer = $(this).parent().next();
        if($continer.hasClass("hide")){
            //关闭其他
            $("li > div.info").each(function(){
                if(!$(this).hasClass("hide")){
                    $(this).addClass("hide");
                    var $detail = $(this).prev().find('.detail');
                    $detail.html($detail.attr("text"));
                }
            });
            $continer.removeClass("hide");
            $(this).attr("text",$(this).html());
            $(this).html("取消");
        }else{
            $continer.addClass("hide");
            $("div.info input").val("");
            $(this).html($(this).attr("text"));
        }
    });
});
//
//var mobileCheck,selectArr;
//var intervalid,codeTime=60,tradeintervalid,tradeTime;
//var fun = function(){
//	if(codeTime==0){
//		$("#jsGetCode").html("重新获取").show().next().hide();
//		clearInterval(intervalid);
//	}else{
//		$("#Tcount").html(--codeTime+"秒后重试")
//	}
//}
//var tradefun = function(){
//	if(tradeTime==0){
//		$("#jsGetTradeCode").html("重新获取").show().next().hide();
//		clearInterval(tradeintervalid);
//	}else{
//		$("#Tradecount").html(--tradeTime+"秒后重试");
//	}
//}
//$(function () {
//
//	$.get("/userpwdquestion",function(response){
//		if(response.success&&response.data.totalCount>0){
//			$("#question").hide();
//			$("#jsQuestionStatus").removeClass('icon_complete_no').addClass("icon_complete");
//		}
//	});
//
//	//加载密保问题选项，互斥
//	$.get("/pwdquestion",function(response){
//		if(response.success){
//			var elements = $("select");
//			selectArr=response.data;
//			elements.each(function(i){
//				var html = '<option value="" selected="true">请选择</option>';
//				for(var q in selectArr){
//					html += '<option value="'+ selectArr[q].questionId +'">' + selectArr[q].questionTitle + '</option>';
//				}
//				$(this).html(html);
//			});
//			elements.change(function(){
//				var cIndex = $("select").index($(this)); //当前select选框索引值
//				var selected = [];
//				$("select").each(function(){
//					selected.push($(this).val());
//				});
//				//遍历所有select选框，重置所有问题
//				$("select").each(function(i){
//					//跳过当前的select选框，因为该内容无需校正
//					if (cIndex == i) return;
//					var html="";
//					var tValue = $(this).val();
//					if(tValue==""){
//						html = '<option value="" selected="true">请选择</option>'
//					}else{
//						html = '<option value="' + tValue + '" selected="true">'+ $(this).find("option:selected").text() + '</option>';
//					}
//					for(var q in selectArr){
//						if($.inArray(selectArr[q].questionId, selected)==-1){
//							html += '<option value="'+ selectArr[q].questionId +'" >' + selectArr[q].questionTitle + '</option>';
//						}
//					}
//					$(this).html(html);
//				});
//			});
//		}
//	});
//
//	/**
//	 * 密码强度
//	 */
//	$('#jsNewPwd,#jsNewTradePwd').keyup(function(e) {
//     	var strongRegex = new RegExp("^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$", "g");
//     	var mediumRegex = new RegExp("^(?=.{7,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g");
//     	var enoughRegex = new RegExp("(?=.{6,}).*", "g");
//     	if (false == enoughRegex.test($(this).val())) {
//
//     	} else if (strongRegex.test($(this).val())) {
//              $(this).next().removeClass().addClass("icons icon_high");
//     	} else if (mediumRegex.test($(this).val())) {
//              $(this).next().removeClass().addClass("icons icon_medium");
//     	} else {
//              $(this).next().removeClass().addClass("icons icon_low");
//     	}
//    	return true;
//	});
//	//修改密码
//	$("#jsPwdSub").click(function(){
//		var oldPwd = RSAUtils.encryptedString($("#jsCurPwd").val());
//		var newPwd = $("#jsNewPwd").val();
//		var repeat = $("#jsRepeat").val();
//		if(newPwd==repeat){
//			$.post("/user/loginpwd/reset",{oldPwd:oldPwd,newPwd:RSAUtils.encryptedString(newPwd)},function(response){
//				if(response.success){
//					$("div.info")[0].addClass("hide");
//					$("div.info input").val("");
//					$("#jspwdShow").attr("class",$('#jsNewPwd').next().attr("class"));
//					$("div.info")[0].prev().find(".detail").html("修改");
//                    window.location.href = '/';
//				}else{
//					alert(response.msg);
//				}
//			})
//		}else{
//			alert("两次输入不一致");
//		}
//	});
//	//修改（设置）安全密码
//	$("#jsTradePwdSub").click(function(){
//		var type=$(this).attr("sub-data-type");
//		var url,data={};
//		var oldPwd = $("#jsCurTradePwd").val();
//		var mobile = $("#mobile").val();
//		var code = $("#jsTradeCode").val();
//		var newPwd = $("#jsNewTradePwd").val();
//		var repeat = $("#jsTradeRepeat").val();
//		if(type!=1){
//            if(code==""||newPwd==""){
//                alert("请输入完整");
//                return false;
//            }
//            url="/user/tradepwd/set";
//            data["mobile"] = mobile;
//            data["code"]=code;
//            data["trade_pwd"] = RSAUtils.encryptedString(newPwd);
//		}else{
//            if(oldPwd==""||newPwd==""){
//                alert("请输入完整");
//                return false;
//            }
//            url = "/user/tradepwd/reset";
//            data["old_trade_pwd"] = RSAUtils.encryptedString(oldPwd);
//            data["new_trade_pwd"] = RSAUtils.encryptedString(newPwd);
//		}
//		if(newPwd==repeat){
//			$.post(url,data,function(response){
//				if(response.success){
//					$("div.info")[2].addClass("hide");
//					$("div.info input").val("");
//					$("#jsTradeComplete").attr("class",$('#jsNewTradePwd').next().attr("class"));
//					if(type!=1){
//						var list = $(".news_list li:eq(2) .group");
//						list[0].removeClass("hide");
//						list[1].addClass("hide");
//						list[2].addClass("hide");
//					}
//					$("div.info")[2].prev().find(".detail").html("修改");
//				}else{
//					alert(response.msg);
//				}
//			})
//		}else{
//			alert("两次输入不一致");
//		}
//	});
//	//安全密码设置获取验证码
//	$("#jsGetTradeCode").click(function(){
//		codeTime=60;
//		$.post("/sms/changemobile",{mobile:$("#mobile").val()},function(response){
//			if(response.success){
//				$("#jsGetTradeCode").hide();
//				tradeintervalid = setInterval("tradefun()", 1000);
//				$("#jsGetTradeCode").next().show();
//			}
//		});
//	});
//	//修改手机号验证
//	$("#jsMobile").blur(function(){
//		mobileCheck = false;
//		if(CMUTILS.checkMobile($(this).val())){
//			$.get("/user/newmobile/check",{mobile:$(this).val()},function(response){
//				mobileCheck = response.success;
//				if(!mobileCheck){
//					alert(response.msg);
//				}
//			});
//		}else{
//			alert("请输入有效手机号");
//		}
//	});
//	//修改手机号获取验证码
//	$("#jsGetCode").click(function(){
//		codeTime=60;
//		if(mobileCheck){
//			$.post("/sms/changemobile",{mobile:$("#jsMobile").val()},function(response){
//				if(response.success){
//					$("#jsGetCode").hide();
//					intervalid = setInterval("fun()", 1000);
//					$("#jsGetCode").next().show();
//				}
//			});
//		}else{
//			alert("请输入有效手机号");
//		}
//	});
//	//修改手机
//	$("#jsMobileSub").click(function(){
//		var mobile = $("#jsMobile").val();
//		var code = $("#jsCode").val();
//		var trade = $("#tradepwd").val();
//		if(mobileCheck&&code!=""&&trade!=""){
//			$.post("/user/mobile/reset",{mobile:mobile,check_code:code,trade_pwd:RSAUtils.encryptedString(trade)},function(response){
//				if(response.success){
//					$("div.info")[1].addClass("hide");
//					$("div.info input").val("");
//					$("div.info")[1].prev().find(".detail").html("修改");
//				}else{
//					alert(response.msg);
//				}
//			});
//		}else{
//			alert("请填写完整信息");
//		}
//	});
//	//设置密保问题
//	$("#jsQuestionSub").click(function(){
//		var ques1 = $("#ques1").val();
//		var ques2 = $("#ques2").val();
//		var ques3 = $("#ques3").val();
//		var ans1  = $("#ans1").val();
//		var ans2  = $("#ans2").val();
//		var ans3  = $("#ans3").val();
//		if(ques1!=""||ques2!=""||ques3!=""||ans1!=""||ans2!=""||ans3!=""){
//			$.post("/user/pwdquestion/set",{question1:ques1,answer1:ans1,question2:ques2,answer2:ans2,question3:ques3,answer3:ans3},function(response){
//				if (response.success) {
//					$("div.info")[3].addClass("hide");
//					$("#jsQuestionStatus").removeClass('icon_complete_no').addClass('icon_complete');
//					$("div.info input").val("");
//					$("#question").html("");
//				} else {
//					alert(response.msg);
//				}
//			})
//		}else{
//			alert("请填写完整信息");
//		}
//	});
//
//	//实名认证
//	$("#jsRealSub").click(function(){
//		var realName=$("#jsName").val();
//		var idCard = $("#jsId").val();
//		if(realName!=""||idCard!=""){
//			$.post("/user/identity",{real_name:realName,idcard:idCard},function(response){
//				if(response.success){
//					$("div.info")[4].addClass("hide");
//					$("#jsRealStatus").removeClass('icon_complete_no').addClass('icon_complete');
//					$("div.info input").val("");
//					$("#jsReal").html("修改");
//				}else{
//					alert(response.msg);
//				}
//			})
//		}else{
//			alert("请填写完整信息");
//		}
//	});
//	//绑定邮箱
//	$("#jsMailSub").click(function(){
//		var mail=$("#jsMail").val();
//		var trade = $("#jsMtrade").val();
//		if(mail!=""||trade!=null){
//			$.post("/user/email/change",{email:mail,tradePwd:RSAUtils.encryptedString(trade)},function(response){
//				if(response.success){
//					$("div.info")[5].addClass("hide");
//					$("#jsMailStatus").removeClass('icon_complete_no').addClass('icon_complete');
//					$("div.info input").val("");
//					$("div.info")[5].prev().find(".detail").html("修改");
//				}else{
//					alert(response.msg);
//				}
//			})
//		}else{
//			alert("请填写完整信息");
//		}
//	})
//});
//
