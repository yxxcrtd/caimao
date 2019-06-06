function _domctrl() {
	//this -> stockChart
	var _this = this,
		stockChart = _this,
		html = {};
	_this.stockInfo = function() {
		if (_this.domlist.stock){
			if(!html['stock'])
				html.stock = _this.domlist.stock.querySelector('script').innerHTML;
			_this.domlist.stock.innerHTML = rentmpl(html.stock, {
				data: stockChart.options.data.DATA2
			});
		}
	}

	function getMarket() {
		var ul = _this.domlist.market.getElementsByTagName('ul'),
			script = _this.domlist.market.getElementsByTagName('script'),
			cur = _this.domlist.market.querySelector('div');
		html.market = {
			sel: {
				dom: ul[0],
				str: script[0].innerHTML
			},
			buy: {
				dom: ul[1],
				str: script[2].innerHTML
			},
			cur: {
				dom: cur,
				str: script[1].innerHTML
			}
		}
	}
	_this.market = function() {
		if (!html['market']) getMarket();
		html.market.sel.dom.innerHTML = rentmpl(html.market.sel.str, {
			data: stockChart.options.data.MARKET.bid,
			pre: stockChart.options.data.MARKET.pre
		});
		html.market.buy.dom.innerHTML = rentmpl(html.market.buy.str, {
			data: stockChart.options.data.MARKET.ask,
			pre: stockChart.options.data.MARKET.pre
		});

		html.market.cur.dom.innerHTML = rentmpl(html.market.cur.str, {
			cur: stockChart.options.data.MARKET.cur,
			pre: stockChart.options.data.MARKET.pre
		});
		if(_this['tradeForm']){
			if(!_this.tradeForm.buy.inputs[0].getAttribute('cooldown'))
				_this.tradeForm.buy.inputs[0].value = stockChart.options.data.MARKET.bid[stockChart.options.data.MARKET.bid.length-1].pri;
			if(!_this.tradeForm.sell.inputs[0].getAttribute('cooldown'))
				_this.tradeForm.sell.inputs[0].value = stockChart.options.data.MARKET.ask[0].pri;
		}

	}

	_this.tradeHistory = function() {
		if (!html['tradeHistory']){
			html.tradeHistory = {
				str : _this.domlist.tradeHistory.querySelector('script').innerHTML,
				dom : _this.domlist.tradeHistory.querySelector('ul')
			}
		}
		html.tradeHistory.dom.innerHTML = rentmpl(html.tradeHistory.str, {
			data: stockChart.options.data.TRADELIST.reverse(),
			pre: stockChart.options.data.MARKET.pre

		});
	}
}