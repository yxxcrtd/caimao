// 加密个密码
function encodePwd(pwd) {
	var jsEncrypt = new JSEncrypt();
	jsEncrypt.setPublicKey(window['PUBLICKEY']);
	return jsEncrypt.encrypt2Hex(pwd);
}


function reg(){
	var box1 = document.querySelector("#reg1"),
		box2 = document.querySelector("#reg2"),
		box3 = document.querySelector("#reg3"),
		notice1 = document.querySelector("#notice1"),
		notice2 = document.querySelector("#notice2"),
		captcha = document.querySelector("#captcha"),
		captchaPic = document.querySelector("#captchaPic"),
		getMobCode = document.querySelector("#getMobCode"),
		regBtn = document.querySelector("#regBtn"),
		submit = document.querySelector("#submit"),
		input = {
			"box1" : box1.querySelectorAll("input"),
			"box2" : box2.querySelectorAll("input")
		},
		req = request(),
		scd = 60,
		cooldown = 0,
		timer,data = {};

	function getPic(){
		captchaPic.src = captchaPic.src;
	}

	Event.add(captchaPic,'click',getPic);
	
	function getMC(){
		if(cooldown)return;
		data.mob = input.box1[0].value;
		data.captcha = captcha.value;
		if(data.mob == ""){
			notice1.innerHTML = '<i class="icons"></i>请输入手机号';
			notice1.className = "error";
			return;
		}
		if(!data.captcha){
			notice1.innerHTML = '<i class="icons"></i>验证码没有输入，请检查输入';
			notice1.className = "error";
			return;
		}
		notice1.className = "hidden";
		wait();
		cooldown = 1;
		req.ajax({
			type : "POST",
			url  : "/sms/registercode",
			data : { 
				mobile : data.mob,
				captcha : captcha.value
			},
			success : function(d){
				if(!d.success){
					notice1.innerHTML = '<i class="icons"></i>'+d.msg;
					notice1.className = "error";
					clearWait();
				}else{
					mobile = data.mob;
				}
			}
		})
	}

	function wait(){
		scd--;
		timer && clearTimeout(timer);
		getMobCode.innerHTML = scd+"秒后重试";
		if(!scd){
			clearWait();
		}else{
			timer = setTimeout(wait,1000);
		}
	}
	function clearWait(){
		scd = 60;
		cooldown = 0;
		timer && clearTimeout(timer);
		timer = null;
		getMobCode.innerHTML = "获取验证码";
	}

	function nextStep(){
		data.code = input.box1[2].value;
		data.mob = input.box1[0].value;
		if(data.code == ""){
			notice1.innerHTML = '<i class="icons"></i>请输入短信验证码';
			notice1.className = "error";
			return;
		}

		req.ajax({
			type : "POST",
			url  : "/user/mobile/check",
			data : { mobile : data.mob},
			success : function(d){
				if(!d.success){
					notice1.innerHTML = '<i class="icons"></i>'+d.msg;
					notice1.className = "error";
					clearWait();
				}else{
					box1.style.display = "none";
					box2.style.display = "block";
				}
			}
		})
	}
	function checkForm(e){
		var pwd = input.box2[0].value,
			repwd = input.box2[1].value;
		Event.stop(e)
		if(!pwd || !repwd){
			notice2.innerHTML = '<i class="icons"></i>密码和确认密码都是必填项';
			notice2.className = "error";
			return;
		}
        if(pwd.length < 6){
            notice2.innerHTML = '<i class="icons"></i>密码不能短于6位';
            notice2.className = "error";
            return;
        }
		if(pwd != repwd){

			notice2.innerHTML = '<i class="icons"></i>两次输入的密码不相同，请重新输入';
			notice2.className = "error";
			return;
		}
		notice2.className = "success";
		notice2.innerHTML = '<i class="icons"></i>请求发送中请稍后';
		req.ajax({
			url : "/user/register",
			data : {
				mobile : data.mob,
				loginPwd : encodePwd(pwd),
				smsCode : data.code
			},
			type : "POST",
			success : function(d){
				if(d.success){
					req.ajax({
						url : "/user/login",
						type : "POST",
						data : {
							loginName : data.mob,
							loginPwd : encodePwd(pwd)
						},
						success : function(){
							box2.style.display = "none";
							box3.style.display = "block";
						}
					});
				}else{
					notice2.innerHTML = '<i class="icons"></i>'+d.msg;
					notice2.className = "error";
					return;
				}
			}
		})
	}
	;(function(){
		Event.add(getMobCode,"click",getMC);
		Event.add(regBtn,"click",nextStep);
		Event.add(submit,"click",checkForm);
	})();

}

function login(){
	var btn = document.querySelector("#btn"),
		loginName = document.querySelector("#loginName"),
		loginPwd = document.querySelector("#loginPwd"),
		captcha = document.querySelector("#captcha"),
		captchaPic = document.querySelector("#captchaPic"),
		req = request(),
		data = {},count = 0;
	function loginEvent(e){
		Event.stop(e);
		data.mob = loginName.value;
		data.pwd = loginPwd.value;
		data.captcha = captcha.value;
		if(count >= 3 && !data.captcha){
			notice.innerHTML = '<i class="icons"></i>验证码没有输入，请检查输入';
			notice.className = "error";
			return;
		}
		if(!data.mob){
			notice.innerHTML = '<i class="icons"></i>手机号没有输入，请检查输入';
			notice.className = "error";
			return;
		}
		if(!data.pwd){
			notice.innerHTML = '<i class="icons"></i>密码没有输入，请检查输入';
			notice.className = "error";
			return;
		}
		req.ajax({
			url : "/user/loginErrorCount",
			type : "GET",
			data : {
				loginName : data.mob
			},
			success : function(d){
				if(d.success){
					count = d.data*1;
					if(count >= 3 && !captcha.value){
						captcha.parentNode.className = "col";
						getPic();
					}else{
						realLogin();
					}
				}
			}
		});
	}
	function realLogin(){
		var loginPwdStr = encodePwd(data.pwd);
		req.ajax({
			url : "/user/login",
			type : "POST",
			data : {
				loginName : data.mob,
				loginPwd : loginPwdStr,
				captcha : captcha.value
			},
			success : function(d){
				if(!d.success){
					notice.innerHTML = '<i class="icons"></i>'+d.msg;
					notice.className = "error";
					return;
				}else{
					if(window['REF']){
						window.location.href = REF;
					}
				}
			}
		})
	}
	function getPic(){
		captchaPic.src = captchaPic.src;
	}
	Event.add(btn,'click',loginEvent);
	Event.add(captchaPic,'click',getPic);
	/*req.ajax({
		url : "/user/loginErrorCount",
		type : "GET",
		success : function(){}
	})*/
}

function accountList(id){
	var box = document.querySelector("#"+id);
	Event.add(box,'click',function(e){
		var tar = Event.target(e),action;
		if(action = tar.getAttribute("action")){
			Event.stop(e);
			if(action == "showHide"){
				if(tar.getAttribute("isopen")){
					tar.innerHTML = "展开";
					tar.removeAttribute("isopen");
					!dom.hasClass(tar.parentNode,"grayTit") && dom.addClass(tar.parentNode,"grayTit");
					tar.parentNode.parentNode.querySelector("table").className = "";
				}else{
					tar.innerHTML = "收起";
					tar.setAttribute("isopen",1);
					dom.hasClass(tar.parentNode,"grayTit") && dom.removeClass(tar.parentNode,"grayTit");
					tar.parentNode.parentNode.querySelector("table").className = "cur";
				}
			}
		}
	})
}
function infoList(id){
	var box = document.querySelector("#"+id),
		req = request();
	Event.add(box,'click',function(e){
		var tar = Event.target(e),action,doms,input,notice,data = {
			nickName : "",
			address : "",
			qq : "",
			weixin : ""
		};
		var form,name;
		if(action = tar.getAttribute("action")){
			Event.stop(e);
			if(action == "showHide"){
				form = tar.parentNode.parentNode.querySelector("div.form");
				name = tar.parentNode.parentNode.querySelector("div.name");
				if(tar.getAttribute("isopen")){
					tar.innerHTML = "修改";
					tar.removeAttribute("isopen");
					form.style.display = "none";
					name.style.display = "block";
				}else{
					tar.innerHTML = "取消";
					tar.setAttribute("isopen",1);
					name.style.display = "none";
					form.style.display = "block";
				}
			return;
			}
			if(action == "submit"){
				doms = tar.parentNode.parentNode.parentNode;
				notice = doms.querySelector(".icons").parentNode;
				input = doms.querySelector("input");
				form = doms.parentNode.querySelector("div.form");
				name = doms.parentNode.querySelector("div.name");
				if(!input.value){
					notice.innerHTML = '<i class="icons"></i>输入不能为空';
					notice.className = "error";
					return;
				}
				data[tar.getAttribute("name")] = input.value;
				req.ajax({
					url : "/user/update_info",
					type : "POST",
					data : data,
					success : function(d){
						if(d.success){
							doms.parentNode.parentNode.querySelector("a[action='showHide']").innerHTML = "修改";
							doms.parentNode.parentNode.querySelector("a[action='showHide']").removeAttribute("isopen");
							form.style.display = "none";
							name.style.display = "block";
							name.innerHTML = input.value;
						}else{
							notice.innerHTML = '<i class="icons"></i>'+d.msg;
							notice.className = "error";
						}
					}
				})
			}

		}
	})
}


function setNewPwd(url,data){
    var box1 = document.querySelector("#step1"),
        box2 = document.querySelector("#step2"),
        notice = document.querySelector("#notice"),
        input = box1.querySelectorAll("input"),
        data = data || {},
        req = request();
    function modifyPwd(e){
        var tar = Event.target(e),
            action = tar.getAttribute("action");
        if(action){
            Event.stop(e);
                console.log(url)
            if(action == "submit"){
                if(!input[0].value){
                    notice.innerHTML = '<i class="icons"></i>原密码没有填写';
                    notice.className = "error";
                    return;
                }
                if(!input[1].value){
                    notice.innerHTML = '<i class="icons"></i>新密码没有填写';
                    notice.className = "error";
                    return;
                }
                if(!input[2].value){
                    notice.innerHTML = '<i class="icons"></i>确认新密码没有填写';
                    notice.className = "error";
                    return;
                }
                if(input[1].value.length < 6){
                    notice.innerHTML = '<i class="icons"></i>密码不能短于6位';
                    notice.className = "error";
                    return;
                }
                if(input[1].value != input[2].value){
                    notice.innerHTML = '<i class="icons"></i>两次新密码填写的不相同，请确认重新填写';
                    notice.className = "error";
                    return;
                }
				data.oldPwd = encodePwd(input[0].value);
				data.newPwd = encodePwd(input[1].value);
				req.ajax({
                    type : "POST",
                    url  : url || "/user/loginpwd/reset",
                    data : data,
                    success : function(d){
                        if(!d.success){
                            notice.innerHTML = '<i class="icons"></i>'+d.msg;
                            notice.className = "error";
                        }else{
                            box1.style.display = "none";
                            box2.style.display = "block";
                        }
                    }
                })
            }
        }
    }
    Event.add(box1,"click",modifyPwd)
}


function setGJSNewPwd(url,data){
    var box1 = document.querySelector("#step1"),
        box2 = document.querySelector("#step2"),
        notice = document.querySelector("#notice"),
        input = box1.querySelectorAll("input"),
        data = data || {},
        req = request();
    function modifyPwd(e){
        var tar = Event.target(e),
            action = tar.getAttribute("action");
        if(action){
            Event.stop(e);
                console.log(url)
            if(action == "submit"){
                if(!input[0].value){
                    notice.innerHTML = '<i class="icons"></i>原密码没有填写';
                    notice.className = "error";
                    return;
                }
                if(!input[1].value){
                    notice.innerHTML = '<i class="icons"></i>新密码没有填写';
                    notice.className = "error";
                    return;
                }
                if(!input[2].value){
                    notice.innerHTML = '<i class="icons"></i>确认新密码没有填写';
                    notice.className = "error";
                    return;
                }
                /*if(input[1].value.length < 6){
                    notice.innerHTML = '<i class="icons"></i>密码不能短于6位';
                    notice.className = "error";
                    return;
                }*/
                if(input[1].value != input[2].value){
                    notice.innerHTML = '<i class="icons"></i>两次新密码填写的不相同，请确认重新填写';
                    notice.className = "error";
                    return;
                }
                data.oldPwd = input[0].value;
                data.newPwd = input[1].value;

                req.ajax({
                    type : "POST",
                    url  : url || "/user/loginpwd/reset",
                    data : data,
                    success : function(d){
                        if(!d.success){
                            notice.innerHTML = '<i class="icons"></i>'+d.msg;
                            notice.className = "error";
                        }else{
                            box1.style.display = "none";
                            box2.style.display = "block";
                        }
                    }
                })
            }
        }
    }
    Event.add(box1,"click",modifyPwd)
}