// @koala-append "_step1.js"
// @koala-append "_step2.js"
// @koala-append "_step3.js"
// @koala-append "_step4.js"
// @koala-append "_step5.js"

var STEP = {};
function step(){
	var step1 = [  "stepDom",
					document.querySelector("#step1"),
					document.querySelector("#step2"),
					document.querySelector("#step3"),
					document.querySelector("#step4"),
					document.querySelector("#step5"),
					document.querySelector("#njsFinish")
				],
		bank = document.querySelector("#bank"),
		bankNo = document.querySelector("#bankNo"),
		ul = bank.parentNode.querySelector("ul"),
		njs_code = document.querySelector("#njs_code"),
		error_info = document.querySelector("#error_info"),
		bigBox = document.querySelector("#bigBox"),
		pat = location.search.replace("?","").split("&"),
		step = pat[0]*1 || 1,
		stepAction = actions(),
		fav,
		data = (localStorage.getItem("OAD") && step != 1) ? JSON.parse(localStorage.getItem("OAD")) : {exchange : "njs",cur : 1};
	if(step == 1){
		localStorage.clear();
	}
	if(step != data.cur && !pat[1])return bigBox.innerHTML = "<div style='padding:100px 0;text-align:center;font-size:30px;'>参数异常</div>";


	data.exchange = pat[1] ? pat[1] : data.exchange;
	STEP.cur = step;
	STEP.exchange = data.exchange;
	stepAction.reDrawStep(data.exchange,bank,bankNo,ul);
	if(step == 5 && data.exchange == "sjs"){
		fav = localStorage.getItem("metalFav");
		localStorage.clear();
		fav && localStorage.setItem("metalFav",fav);
	}


	if(step == 3 && data.exchange == "njs"){
		step1[6].querySelector("div").style.display = "none";
		step1[6].querySelector("div[suberr]").style.display = "none";
		data.token = token;
		h.post(h.api.do_open_account_NJS(data,{callback:function(d){
			if(d.success){
				window["step6"] && window["step6"](d.data);
				step1[6].style.display = "block";
				step1[6].querySelector("div").style.display = "block";
				njs_code.innerHTML = d.data.result;
				fav = localStorage.getItem("metalFav");
				localStorage.clear();
				fav && localStorage.setItem("metalFav",fav);
			}else{
				stepAction.reDrawStep(data.exchange,bank,bankNo,ul,"error");
				step1[6].querySelector("div[suberr]").style.display = "block";
				step1[6].style.display = "block";
				error_info.innerHTML = d.msg;
			}
		}}));
	}else{
		step1[step].style.display = "block";
		window["step"+step] && window["step"+step]();
	}
	
}



function actions(){
	var	ss = document.querySelector("#stepShow");
	function fillBank(tar){
		var val = tar.getAttribute("data").split("|"),
			inputs = tar.parentNode.parentNode.querySelectorAll("input");
		inputs[0].value = val[0];
		inputs[1].value = val[1];
	}
	function showError(input,attr){
		var span = input.parentNode.querySelector("span");
		span.className = "error";
		span.innerHTML = span.getAttribute(attr);
	}
	function check(tar,e){
		Event.stop(e);
		var inputs = attFather(tar,"stepBox")[0].querySelectorAll("input"),
			pass = 1,
			data = localStorage.getItem("OAD") ? JSON.parse(localStorage.getItem("OAD")) : {},
			comparison = {},
			comlen;
		for(var _input,_comp,i = 0, l = inputs.length; i < l; i++){
			_input = inputs[i];
			data[_input.name] = _input.value;
			if(_comp = _input.getAttribute("comp")){
				if(comparison[_comp]){
					comparison[_comp].push(_input);
				}else{
					comparison[_comp] = [_input];
				}
			}
			if(_input.value == "" && _input.getAttribute("require")){
				showError(_input,"empty")
				pass = 0;
			}
		}
		if(pass){
			for(var k in comparison){
				for(var i = 0, l = comparison[k].length; i < l; i++){
					if(i == 0){
						comlen = comparison[k][i].value;
					}
					if(comlen != comparison[k][i].value){
						showError(comparison[k][i],"comp")
						pass = 0;
					}
				}
			}
		}
		return [data,pass];
	}
	function next(tar,e){
		var obj = check(tar,e),
			data = obj[0],
			pass = obj[1];
		data.exchange = STEP.exchange;
		if(pass){
			data.cur = STEP.cur*1 + 1;
			localStorage.setItem("OAD",JSON.stringify(data));
			window.location.href = "?" + (STEP.cur*1+1)
		}
	}
	function submit(tar,e){
		var obj = check(tar,e),
			data = obj[0],
			pass = obj[1];
		data.exchange = STEP.exchange;
		if(pass){
			data.cur = STEP.cur*1 + 1;
			localStorage.setItem("OAD",JSON.stringify(data));
			//console.log(data.exchange)
			window.location.href = "?" + (STEP.cur*1+1) + "&"+data.exchange;
		}
	}

	function hiddenNotice(input){
		var span = input.parentNode.querySelector("span");
		if(input.getAttribute("require")){
			Event.add(input,"focus",function(){
				span.className = "hidden";
			})
		}
	}

	function reDrawStep(ex,bank,bankNo,ul,error){
		var html = {
			"njs" : '<em <%if(s == 1){%>class="on"<%}%>><i>1</i>填写账户信息</em><em <%if(s == 2){%>class="on"<%}%>><i>2</i>设置密码</em><em <%if(s == 3){%>class="on"<%}%>><i>3</i>开户成功</em>',
			"njsError" : '<em <%if(s == 1){%>class="on"<%}%>><i>1</i>填写账户信息</em><em <%if(s == 2){%>class="on"<%}%>><i>2</i>设置密码</em><em <%if(s == 3){%>class="on"<%}%>><i>3</i>开户失败</em>',
			"sjs" : '<em <%if(s == 1){%>class="on"<%}%>><i>1</i>填写账户信息</em><em <%if(s == 2){%>class="on"<%}%>><i>2</i>设置密码</em><em <%if(s == 3){%>class="on"<%}%>><i>3</i>调查问卷</em>'
					+'<em <%if(s == 4){%>class="on"<%}%>><i>4</i>短信验证</em><em <%if(s == 5){%>class="on"<%}%>><i>5</i>开户成功</em>'
			},
			bankHtml = '<%for(var i = 0,l = data.length; i < l; i++){%><li act="fillBank" data="<%=data[i].bankName%>|<%=data[i].bankNo%>"><%=data[i].bankName%></li><%}%>',
			bankObj = bankList[ex];
		ss.innerHTML = rentmpl(html[error ? ex+"Error" : ex],{
			s : STEP.cur
		});
		STEP.exchange = ex;
		bank.value = bankObj[0].bankName;
		bankNo.value = bankObj[0].bankNo;
		ul.innerHTML = rentmpl(bankHtml,{data:bankObj})
	}
	function getScore(tar,e){
		Event.stop(e);
		var box = attFather(tar,"stepBox")[0],
			input = box.querySelectorAll("input"),
			dl = box.querySelectorAll("dl"),
			data = localStorage.getItem("OAD") ? JSON.parse(localStorage.getItem("OAD")) : {},
			score = 0;
		for(var _this,i = 0, l = dl.length; i < l; i++){
			_this = dl[i].querySelectorAll("input");
			for(var j = 0, len = _this.length; j < len; j++){
				_this[j].value = 10 - 2*j;
			}
		}
		for(var i = 0, len = input.length; i < len; i++){
			if(input[i].checked)score += input[i].value*1;
		}
		if(score < 50){
			tar.parentNode.querySelector("p").className = "error";
			setTimeout(function(){
				tar.parentNode.querySelector("p").className = "";
			},5000);
		}else{
			data.cur = STEP.cur*1 + 1;
			data.score = score;
			localStorage.setItem("OAD",JSON.stringify(data));
			window.location.href = "?" + (STEP.cur*1+1);
		}

	}
	return {
		submit : submit,
		fillBank : fillBank,
		next : next,
		getScore : getScore,
		reDrawStep : reDrawStep,
		hiddenNotice : hiddenNotice
	}
}