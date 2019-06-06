/*修改用户密码*/
var container = {};
/*// 获取用户信息
container.user = basePath + "/user";
// 获取用户交易密码
container.tradepwd = basePath + "/tradepwd";
// 获取用户交易密码
container.bankcard = basePath + "/bankcard";
// 密保问题
container.userpwdquestion = basePath + "/userpwdquestion";*/
//获取所有密保问题
container.pwdquestion = basePath + "/pwdquestion";//http://localhost:8080/xx/pwdquestion?questionId=*&start=0&count=Infinity
// 密保问题设置
container.pwdquestionset = basePath +"/user/pwdquestion/set";

        console.info(document.cookie);
$(function(){
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
			    		theme: 'android-ics light', //皮肤样式
			            display: 'bottom', //显示方式 
			            mode: 'scroller', //日期选择模式
			            lang:'zh'
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
		
		$("#submit_btn").click(function(){
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
				url:container.pwdquestionset,
				type:"post",
				data:{
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
		
});