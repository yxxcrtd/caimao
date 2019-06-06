function _action(box, canvas,doms) {
	//this -> stockChart
	var _this = this,
		stockChart = _this,
		html = {},
		tip = besideTip(),
		tmpStr, lens = 0,
		cur, selectorCurrent = -1,
		timer,mouseBusy;
	_this.keyMapInput = function(e) {
		var val = Event.target(e).value,
			kc = e.keyCode,
			types = e.type,
			data, curdata, idx;
		if (!val) {
			data = [];
		} else {
			data = _this.setKeyMap(val) || [];
		}
		if (tmpStr == val) {
			//38 up, 40 down
			if (kc == 38 || kc == 40) Event.stop(e);
			if (types.toLowerCase() == 'keyup') return;
			if (kc == 38) {
				selectorCurrent += -1;
			} else if (kc == 40) {
				selectorCurrent += 1;
			}
			if (kc == 38 || kc == 40) {
				if (selectorCurrent < 0) selectorCurrent = 0;
				if (selectorCurrent >= lens) selectorCurrent = lens - 1;
				go(selectorCurrent, kc);
			}
			if (kc == 13) {
				idx = selectorCurrent > 0 ? selectorCurrent : 0;
				curdata = data[idx];
				_this.shortcutHidden();
				doms.parentNode.getElementsByTagName('input')[0].value = '';
				if(!curdata)return;
				_this.changeStock(JSON.parse(curdata),1)
			}
		} else {
			tmpStr = val;
			cur = null;
			selectorCurrent = -1;
			data.splice(50, 100000);
			data = JSON.parse('[' + data.join(',') + ']');
			lens = data.length;
			if(!html['shortcut'] && !doms.querySelector('script'))return;
			if (!html['shortcut']) {
				html.shortcut = doms.querySelector('script').innerHTML;
			}
			doms.innerHTML = rentmpl(html.shortcut, {
				data: data
			});
		}
	}

	function go(n) {
		var li = doms.getElementsByTagName('li'),
			offset = (n + 1) * li[0].offsetHeight,
			ot = doms.scrollTop + doms.offsetHeight;
		cur && dom.hasClass(cur, 'on') && dom.removeClass(cur, 'on');
		cur = li[n];
		offset = offset - doms.offsetHeight;
		doms.scrollTop = offset > 0 ? offset : 0;
		!dom.hasClass(cur, 'on') && dom.addClass(cur, 'on');
	}

	_this.shortcutShow = function(doms) {
		stockChart.shortcutIsShow = 1;
		doms.parentNode.parentNode.style.bottom = 0;
	}
	_this.shortcutHidden = function() {
		stockChart.shortcutIsShow = 0;
		doms.parentNode.getElementsByTagName('input')[0].value = '';
		doms.parentNode.parentNode.style.bottom = -1 * doms.parentNode.parentNode.offsetHeight + 'px';
	}
	_this.changeStock = function(data,iskey) {
		var ex = data.ex,
			code = data.code;
		if(data["action"]){
			return _this[data.action] && _this[data.action](data);
		}
		if(window['MAP'] && iskey){
			return window.location.href = 'stock.html?id='+code+'&ex='+ex; 
		}
		_this.options.user.stockID = data.code;
		_this.options.user.ex = data.ex;
		stockChart.options.user.ctrlVer = new Date()*1;
		_this.getNewStock();
	}
	_this.goDetail = function(tar, data) {
		var d = data.split('/'),
			ex = d[0],
			code = d[1];
		_this.shortcutHidden();
		window.location.href = 'stock.html?id='+code+'&ex='+ex;
	}
	_this.clickChangeStock = function(tar, data) {
		var d = data.split('/'),
			ex = d[0],
			code = d[1];
		_this.options.user.stockID = code;
		_this.options.user.ex = ex;
		stockChart.options.user.ctrlVer = new Date()*1;
		_this.getNewStock();
		_this.shortcutIsShow && _this.shortcutHidden();
	}
	_this.actionSetInd = function(tar,data){
		var name = data && data.split("/");
		if(!stockChart.options.hasInd[stockChart.options.user.cyc]){
			return tip.changeObj(tar).top().notice("当前周期下没有这个指标数据",2000,"warning");
		}
		if(!data && tar["code"])return _this.setInd(tar.code);
		name[1] && _this.setInd(name[1],stockChart.options.user.vIndicator.toLowerCase() != name[1] || stockChart.options.user.timeShare);
	}
	_this.changePeriod = function(tar,data){
		//console.log(tar,data);
		var period = data;
		if(!data || stockChart.options.user.cyc == data && !stockChart.options.user.timeShare)return;
		stockChart.options.user.timeShare = 0;
		stockChart.options.layout = [1,1,stockChart.options.hasInd[data]];

		stockChart.options.data.DATA = [];
		stockChart.options.data.DATA2 = {};
		stockChart.options.data.IND = {};
		stockChart.options.user.cyc = data;
		stockChart.winReset();
		stockChart.getNewStock();
	}
	_this.cross = function(tar, data, e) {
		stockChart["kBox"] && (stockChart.kBox.style.left = "-299px");
		if (stockChart.crossStart) {
			stockChart.crossStart = 0;
			_this.mouseout(e);
			canvas.toolsCanvas[1].clear();
		} else {
			stockChart.crossStart = 1;
			_this.mouseover(e);
			_this.mousemove(e);
			!stockChart.options.user.timeShare && stockChart["kBox"] && (stockChart.kBox.style.left = "22px");
		}
	}

	_this.mouseover = function(e) {
		if (!stockChart.crossStart) return;
		clearTimeout(timer);
		timer = null;
	}

	_this.mousemove = function(e) {
		if (!stockChart.crossStart) return;
		mouseMoveSwitch(e);
		//throwing && throwCanvas(e);
	}

	_this.mouseout = function(e) {
		canvas.toolsCanvas[1].clear();
		canvas.toolsCanvas[1].drawTimeAndVol(stockChart.options.lens - 1,null,null);
		!stockChart.options.user.timeShare && stockChart["kBox"] && (stockChart.kBox.style.left = "-295px");
	}
	function mouseMoveSwitch(e) {
		//switch mouse move event
		var pos = Event.mouse(e);
		var level = stockChart.options.level[stockChart.options.user.level];
		if(mouseBusy)return;
		mouseBusy = 1;
		var outRule = box.size.w > stockChart.options.user.outRuleWidth;
		var idx = (pos.x - dom.getPosition(box).x - (stockChart.options.user.timeShare ? ( outRule ? stockChart.options.user.ruleWidth : 0 ) : 0));

		!stockChart.options.user.timeShare && stockChart["kBox"] && (stockChart.kBox.style.left = "5px");
		canvas.toolsCanvas[1].crossLine(idx, pos.y - dom.getPosition(box).y + (document.documentElement.scrollTop || document.body.scrollTop), level[0]);

		canvas.toolsCanvas[1].drawTimeAndVol(idx, pos.y - dom.getPosition(box).y, level[0]);



		setTimeout(function() {
			mouseBusy = 0;
		}, 20);
	}

	_this.historyGo = function(){
		if(stockChart.crossStart){
			return _this.cross();
		}
		if(stockChart.shortcutIsShow){
			return _this.shortcutHidden();
		}
		if(!stockChart.options.user.timeShare){
			stockChart.kt();
		}else if(window.location.href.split('/').pop() != 'index.html' && !stockChart.options.user.popstop){
			window.location.href = './index.html';
		}
	}
	_this.doubleAction = function(){

		if(stockChart.options.user.timeShare && stockChart.options.user.double){
			return stockChart.kt();
		}
	}
}