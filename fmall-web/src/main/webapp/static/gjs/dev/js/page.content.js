
function contentLayout(cfg,dom){
	var html = dom.querySelector("script").innerHTML,
		req = request();
	function wHtml(data){
		if(!data[cfg.key])return;
		data = JSON.parse(JSON.stringify(data).replace(/\<img\:([\d]+)([^>]*)\>/g,function(res,_thisVal,otherVal){
			return "<img src='"+IMGURL+"/help/bank_"+cfg.exchange.toLowerCase()+"_"+cfg.bank+"/"+_thisVal+".png' "+otherVal+"/>";
		}))
		dom.innerHTML = rentmpl(html,{
			data : data[cfg.key][cfg.exchange.toLowerCase()][cfg.bank]
		});
	}
	req.ajax({
		url:"dataExt.html",
		type : "GET",
		success : wHtml
	})
}

function contentLayout2(cfg,dom){
	var html = dom.querySelector("script").innerHTML,
		req = request();
	function wHtml(data){
		if(!data[cfg.key])return;
		dom.innerHTML = rentmpl(html,{
			data : data[cfg.key][cfg.idx]
		});
	}
	req.ajax({
		url:"dataExt.html",
		type : "GET",
		success : wHtml
	})
}