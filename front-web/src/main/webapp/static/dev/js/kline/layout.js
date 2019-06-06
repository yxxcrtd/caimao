// @koala-append "indexNum.js"
function layout(obj, dom) {
	var size = {
			win: {
				w: 0,
				h: 0
			},
			h: {
				header: 0,//51,
				footer: 20,//48,
				trade: 192,
				market: 235,
				stock: 190,
				content: 0
			},
			w: {
				market: 281,
				content: 0
			}
		},
		n = 0,
		timer;

	function _resize() {
		var bigScreen = (document.documentElement.clientHeight || document.body.clientHeight) > 800,
			hiddenHeight = (dom.trade && dom.trade.getAttribute('hiddenHeight')*1) ? (dom.trade.getAttribute('hiddenHeight')*1 + size.h.footer) : 0;
		timer && clearTimeout(timer);
		size.win.screenHeight = document.documentElement.clientHeight || document.body.clientHeight;
		size.win.h = Math.max(document.documentElement.clientHeight || document.body.clientHeight,800);
		size.h.content = size.win.screenHeight - size.h.header - size.h.footer;
		size.h.bigcontent = size.win.h - size.h.header - size.h.footer;
		dom.kline.parentNode.style.height = size.h.bigcontent + "px";
		size.win.w = document.documentElement.clientWidth || document.body.clientWidth;
		size.w.content = size.win.w - size.w.market - ( bigScreen ? 0 : 0 );
		dom.bigBox && (dom.bigBox.style.width = size.w.content + 'px');
		//console.log((obj.stockChart.tradeListShow ? (obj.stockChart.tradeStatus ? size.h.trade : 25) : 0))
		dom.kline.style.cssText = "width:" + size.w.content + "px;height:" + (size.h.content - (obj.stockChart.tradeListShow ? (obj.stockChart.tradeStatus ? size.h.trade : 25) : 0)) + "px;";

		!bigScreen ? (dom.kline.parentNode.style.paddingBottom = size.h.footer + 'px') : (dom.kline.parentNode.style.paddingBottom = 0 + 'px');
		obj['draw'] && obj.draw({
			w: size.w.content,
			h: size.h.content - (obj.stockChart.tradeListShow ? (obj.stockChart.tradeStatus ? size.h.trade : 25) : 0) - 21
		});
		dom.tradeHistory && (dom.tradeHistory.style.height = size.h.bigcontent - (size.h.trade - hiddenHeight + size.h.market + size.h.stock) + "px");


	}! function(obj, dom) {
		'construct';
		Event.add(window, 'resize', function() {
			timer && clearTimeout(timer);
			timer = setTimeout(_resize, 10);
		});
		obj['setdomlist'] && obj['setdomlist'](dom);
		obj.stockChart.winReset = _resize;
		_resize();
	}.apply(window, arguments);
}
function singleLayout(obj){
	var timer;
	obj['setdomlist'] && obj['setdomlist']({});
	Event.add(window, 'resize', function() {
			timer && clearTimeout(timer);
			timer = setTimeout(_resize, 10);
		});
	function _resize(){
		obj['draw'] && obj.draw({
			w : obj.box.offsetWidth,
			h : obj.box.offsetHeight
		})
	}
	_resize();
	obj.stockChart.winReset = _resize;
}
function listLayout(d) {
	var size = {
			win: {
				w: 0,
				h: 0
			},
			h: {
				header: 0,//51,
				footer: 20,//48,
				content: 0
			},
			w: {
				chart: 470,
				frz : 310,
				content: 0
			}
		},
		dom,timer,obj = [];

	function _resize() {
		var sur = 0;
		timer && clearTimeout(timer);
		size.win.w = document.documentElement.clientWidth || document.body.clientWidth;
		size.win.h = document.documentElement.clientHeight || document.body.clientHeight;
		size.h.content = size.win.h - size.h.header - size.h.footer;
		size.w.content = size.win.w - size.w.chart;
		sur = size.h.content - 31 - 24;
		sur = sur - (sur % 30);
		dom.list.style.width = size.w.content + 'px';
		dom.list.style.height = size.h.content + 'px';
		dom.chart.style.height = size.h.content + 'px';
		dom.listBox.style.height = sur + 'px';
		dom.moveList.style.width = (size.w.content - size.w.frz - 22) + 'px';
		dom.moveHead.style.width = (size.w.content - size.w.frz - 22) + 'px';
		dom.listCtrl.style.width = (size.w.content - size.w.frz) + 'px';
		dom.chart1.parentNode.style.height = ( size.h.content/2 ) - 20 + 'px';
		dom.chart2.parentNode.style.height = ( size.h.content/2 ) - 20 + 'px';
		dom.chart1.style.height = ( size.h.content/2 ) - 60 + 'px';
		dom.chart2.style.height = ( size.h.content/2 ) - 60 + 'px';
		for(var i = 0,l = obj.length;i < l;i++){
			obj[i] && obj[i]['draw'] && obj[i].draw({
				w: 420,
				h: (size.h.content / 2) - 60
			});
		}

	}
	function set(o){
		obj.push(o);
		o && o['draw'] && o.draw({
			w: 420,
			h: (size.h.content / 2) - 60
		});
		o && o['stockChart'] && !o.stockChart['winReset'] && (o.stockChart.winReset = _resize);
	}
	! function(d) {
		'construct';
		dom = d;
		obj['setdomlist'] && obj['setdomlist']({});
		Event.add(window, 'resize', function() {
			timer && clearTimeout(timer);
			timer = setTimeout(_resize, 200);
		});
		Event.add(dom.listCtrl,'scroll',function(){
			var sl = dom.listCtrl.scrollLeft,
				cw = dom.listCtrl.offsetWidth,
				bw = dom.moveHead.offsetWidth;
			dom.moveHead.scrollLeft = sl*(1200-bw)/(1200-cw);
			dom.moveList.scrollLeft = sl*(1200-bw)/(1200-cw);
		})
		_resize();
	}.apply(window, arguments);
	return {
		set : set
	}
}

function indexNum(obj,chart){
	if(!obj)return;
	var r = request(),
		html = obj.querySelector('script').innerHTML,
		api = requestAPI();

	function getData(){
		api.release();
		api.indexNum('000001','1')
		api.indexNum('399001','2')
		api.indexNum('399006','2')
		r.requestPayload({
			url: location.protocol + "//" + (typeof chart == 'string' ? chart : chart.stockChart.options.url.xhr),
			data: api.data,
			success: function(data) {
				var d = {},_this;
				if(data.o){
					for(var i = 0,l = data.o.length; i < l; i++){
						_this = data.o[i].o;
						_this.h['code'] && !d[_this.h['code']] && (d[_this.h['code']] = copy(copy(copy({},_this.h),_this.e),_this.m));
						
					}
					obj.innerHTML = rentmpl(html,{
						hz : d['000001'],
						sz : d['399001'],
						cz : d['399006']
					});
				}
			}
		});
		setTimeout(getData,3000);
	}

	!function(){
		getData();
	}()
}

function f(p,n,i){
	return p*Math.pow((1+i/12),n)
}