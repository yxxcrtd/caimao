/*显示用户信息*/
var container = {};
// 获取用户信息
container.user = basePath + "/user";
$(function(){
	$.commAjax({
		url : container.user,
		success : function(data) {
			if (data.success) {
				var data2 = data.data;
				var userName = data2.userName;
				if(userName == ""){
					$("#userName").html(data2.mobile.substr(0,3)+" "+data2.mobile.substr(3,4)+" "+data2.mobile.substr(7,4));
				}else{
					$("#userName").html(userName);
				}

				$("#userRealName").html(data2.userRealName);
				$("#mobile").html(data2.mobile.substr(0,3)+" "+data2.mobile.substr(3,4)+" "+data2.mobile.substr(7,4));
				var isTrust = data2.isTrust;
				if (isTrust == "0") {
					
					var cUrl;
					if(upload){
						cUrl = "/user/certification3.htm";
					}else{
						cUrl = "/user/certification.htm";
					}
					$("#certification_icon").parent().attr("href",mobileUrl + cUrl);
					$("#certification_icon").html("<span class='type-ico am-icon-arrow-right'></span>");
				} else {
					$("#certification_icon").html("<span class='am-ft-gray'>已认证</span>");
				}

			} else {
				alert(data.msg);
			}
		}
	});
})