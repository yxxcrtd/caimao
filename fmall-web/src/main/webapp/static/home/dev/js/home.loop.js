//quote.eastmoney.com/hq2data/gb/data/gb_globalindex.js
;(function(){
	var req = request(),head,box = {},html = {},timer = 5000;
	function init(){
		stockIndex();
		gjsIndex();
		ybkIndex();
	}
	function stockIndex(){
		var n = document.createElement('script');
		n.setAttribute('type','text/javascript');
		n.src = "/eastmoney_hq";
		n.async = true;

		n.readyState ? n.onreadystatechange = function(){
			if( /loaded|complete/.test(n.readyState)){
				doLoopStock();
				n.onreadystatechange = null;
			}
		} : n.onload = function(){
			doLoopStock();
			n.onload = null;
		}
		head.appendChild(n);
	}
	function doLoopStock(){
		var data = [];
		data.push(quote_hs.quotation[0]);
		data.push(quote_hs.quotation[1]);
		data.push(quote_hk.quotation[0]);
		data.push(quote_america.quotation[0]);
		data.push(quote_america.quotation[2]);
		box.stock.innerHTML = rentmpl(html.stock,{
				data:data
			});
		setTimeout(stockIndex, timer);
	}

	function gjsIndex(){
		req.jsonp({
			url : "/api/gjshq/snapshot/query",
			data : {
				prodCode : encodeURIComponent("AG.NJS,Ag(T+D).SJS,mAu(T+D).SJS")
			},
			success : doLoopGjs
		});
	}
	function doLoopGjs(d){
		if(d.success){
			box.gjs.innerHTML = rentmpl(html.gjs,{
					data:d.data
				});
		}
		setTimeout(gjsIndex, timer);
	}

	function ybkIndex(){
		req.jsonp({
			url : "/api/ybk/query_composite_index",
			success : doLoopYbk
		});
	}
	function doLoopYbk(d){
		var tmp = [],passed = {njwjs:1,zgyjs:1,znwjs:1};
		if(d.success){
			for(var i = 0,l = d.data.length;i < l;i++){
				if(passed[d.data[i].shortName]){
					tmp.push(d.data[i])
				}
			}
			box.ybk.innerHTML = rentmpl(html.ybk,{
					data:tmp
				});
		}
		setTimeout(ybkIndex, timer);
	}

	domready(function(){
		head = document.querySelector("head");
		box.stock = document.querySelector("#stockIndex");
		html.stock = box.stock.querySelector("script").innerHTML;
		box.gjs = document.querySelector("#gjsIndexBox");
		html.gjs = box.gjs.querySelector("script").innerHTML;
		box.ybk = document.querySelector("#ybkIndexBox");
		html.ybk = box.ybk.querySelector("script").innerHTML;


		init();
	});
})();