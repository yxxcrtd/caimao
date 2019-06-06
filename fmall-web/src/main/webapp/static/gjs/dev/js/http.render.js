function _render(){
	var _this = this,
		html = {};
	function render(){
		function omnipotent(){
			var key = "k"+Math.ceil(1 + Math.random() * 1e10);
			function callback(d){
				writeHtml(d,key);
			}
			return {
				key : key,
				callback : callback
			}
		}
		function noticeContent(){
			var key = "k"+Math.ceil(1 + Math.random() * 1e10);
			function callback(d){
				if(d.success){
					d.data.content = d.data.content.replace(/class="[^"]+"/g,"");
				}
				writeHtml(d,key);
			}
			return {
				key : key,
				callback : callback
			}
		}

		function queryLocalGoods(){
			var key = "k"+Math.ceil(1 + Math.random() * 1e10);
			function callback(){
				var data = localStorage.getItem('metalFav');
				if(!data){
					return writeHtml({
								success : 1,
								data : []
							},key);
				}
				_this.loop(_this.api.query_lots_goods({prodCode : data},queryLotsGoods(key)),3000);
			}
			return {
				key : key,
				callback : callback
			}
		}


		function queryGoodsList(){
			var key = "k"+Math.ceil(1 + Math.random() * 1e10);
			function callback(d){
				var data,td = [];
				if(d.success){
					data = d.data["result"] || d.data;
				}else{
					return;
				}
				for(var i = 0, l = data.length; i < l; i++){
					td.push(data[i].prodCode+"."+data[i].exchange);
				}

				l ? _this.loop(_this.api.query_lots_goods({prodCode : td.join(",")},queryLotsGoods(key)),3000) : 
				writeHtml({success:1,data:[]},key);
			}
			return {
				key : key,
				callback : callback
			}
		}



		function queryLotsGoods(key){
			function callback(d){
				writeHtml(d,key);
			}
			return {
				callback : callback
			}
		}

		function error(msg){
			console.log(msg);
		}
		function writeHtml(d,k){
			var len = html[k].html.length;
			if(d.success){
				if(len == 1){
					html[k].dom.innerHTML = 	rentmpl(html[k].html[0],{
						data : d.data["result"] || d.data
					})
				}
			}else{
				error(d.msg);
			}
		}
		return {
			omnipotent : omnipotent,
			noticeContent : noticeContent(),
			queryGoodsList : queryGoodsList(),
			queryLocalGoods : queryLocalGoods()
		}

	}
	function plan(id,key){
		var dom = document.querySelector(id);
			scripts = dom.querySelectorAll("script");
		html[key] = {
			dom : dom,
			html : []
		};
		for(var i = 0, l = scripts.length; i < l; i++){
			html[key].html.push(scripts[i].innerHTML);
		}
		return html[key];
	}
	_this.plan = plan;
	_this.render = render();
}