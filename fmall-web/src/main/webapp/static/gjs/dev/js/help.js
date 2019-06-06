function helpLayout(){
	var nav = document.querySelector("#nav"),
		list = document.querySelector("#list"),
		notice = document.querySelector("#notice"),
		listHtml = list.querySelector("script").innerHTML,
		lv1 = $_GET("lv1") || 1,
		lv2 = $_GET("lv2") || 1,
		req = request();
	req.ajax({
		url:"data.html",
		success : html
	})
	notice.style.display = "block";
	function html(data){
		var str = '<%for(var i = 1,l = data.length; i < l;i++){%><li'
				+'<%if(lv1 == i){%> class="on" <%}%>'
				+'<%if(i == 4){%>'
				+'><i></i><a href="?action=pcbank&exchange=njs&bank=408"><%=data[i][0]%></a></li>'
				+'<%}else{%>'
				+'><i></i><a href="?lv1=<%=i%>&lv2=1"><%=data[i][0]%></a></li>'
				+'<%}%><%}%>';
		nav.innerHTML = rentmpl(str,{
			data : data,
			lv1 : lv1
		});
		data = JSON.parse(JSON.stringify(data).replace(/\<img\:([\d]+)([^>]*)\>/g,function(res,_thisVal,otherVal){
			return "<img src='"+IMGURL+"/help/"+_thisVal+".jpg' "+otherVal+"/>";
		}))

		list.innerHTML = rentmpl(listHtml,{
			data : data[lv1],
			lv1 : lv1,
			lv2 : lv2
		});
	}
}

function bankLayout(){
	var nav = document.querySelector("#nav"),
		list = document.querySelector("#list"),
		listHtml = list.querySelector("script[name='bank']").innerHTML,
		req = request();
	req.ajax({
		url:"dataExt.html",
		success : html
	})
	function html(d){
		var exchange = $_GET("exchange") || "njs",
			bank = $_GET("bank") || "003",
			data = d.pcbindbank[exchange],
			str = '<%for(var k in data){%><li'
				+'<%if(bank == k){%> class="on" <%}%>'
				+'><i></i><a href="?action=pcbank&exchange=<%=exchange%>&bank=<%=k%>"><%=data[k].name%></a></li><%}%>';
		nav.innerHTML = rentmpl(str,{
			data : data,
			exchange : exchange,
			bank : bank
		});
		data = JSON.parse(JSON.stringify(data).replace(/\<img\:([\d]+)([^>]*)\>/g,function(res,_thisVal,otherVal){
			return "<img src='"+IMGURL+"/help/pcbank_"+exchange.toLowerCase()+"_"+bank+"/"+_thisVal+".png' "+otherVal+"/>";
		}))

		list.innerHTML = rentmpl(listHtml,{
			data : data[bank]
		});
	}
}

function mHelpLayout(){
	var list = document.querySelector("#list"),
		listHtml = list.querySelector("script").innerHTML,
		l1 = $_GET("lv1") || 1,
		l2 = $_GET("lv2") || 1,
		req = request();
	req.ajax({
		url:"data.html",
		success : html
	})
	function html(data){
		var	data = JSON.parse(JSON.stringify(data).replace(/\<img\:([\d]+)([^>]*)\>/g,function(res,_thisVal,otherVal){
			return "<img src='"+IMGURL+"/help/"+_thisVal+".jpg' "+otherVal+"/>";
		}))
		list.innerHTML = (l1*1 && l2*1) ? rentmpl(listHtml,{
			data : data[l1][l2]
		}) : "参数异常";
	}
	Event.add(list,'click',function(e){
		var tar = Event.target(e),
			status;
		if(tar.nodeName.toLowerCase() == 'dt'){
			status = tar.getAttribute('open')*1;
			if(status){
				tar.parentNode.querySelector('dd').style.display = "none";
				tar.removeAttribute("open");
			}else{
				tar.parentNode.querySelector('dd').style.display = "block";
				tar.setAttribute("open",1);
			}
		}
	})
}
