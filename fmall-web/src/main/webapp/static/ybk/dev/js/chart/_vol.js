function _vol(stockChart) {
	//this -> ctx
	var _this = this,
		max = Number.MIN_VALUE,
		min = Number.MAX_VALUE,
		rule, verticalOffset, preData;

	function getRule() {
		var dp = max - min;
		return !dp ? 0 : (_this.h - stockChart.options.user.offsetTop) / dp;
	}

	function getY(v) {
		return ((max - v) * rule).toFixed() * 1 + verticalOffset * 1 + 0.5;
	}

	function drawVolDo(data, i, leftOffset, line) /*(x,y,w,h,isRise)*/ {
		var volume = data['vol'],
			level = stockChart.options.level[stockChart.options.user.level],
			isRise = data.cur * 1 >= data.open * 1,
			color = isRise ?
			stockChart.options.schemes[stockChart.options.user.schemes].rise :
			stockChart.options.schemes[stockChart.options.user.schemes].fall,
			oddNum = level[0] / 2 != (level[0] / 2).toFixed(),
			y, candleX, currentX;
		_this.strokeStyle = color;
		_this.lineWidth = 1;
		_this.fillStyle = color;
		_this.beginPath();
		if (!rule) {
			rule = getRule();
		}
		y = getY(volume);
		currentX = level[0] * i + level[0] / 2 + (oddNum ? 0 : .5) + leftOffset;
		if (level[1] > 1 && !line) {
			candleX = currentX - level[1] / 2 + .5;
			drawRectStroke(candleX, y, level[1], _this.h, isRise);
		} else {
			_this.moveTo(currentX, y);
			_this.lineTo(currentX, _this.h);
			_this.stroke();
		}
		_this.stroke();
	}
	function vma(){
		var total = stockChart.options.data.IND.VMA.length,
			tmplens = stockChart.options.lens - 1,
			data = [].concat(stockChart.options.data.IND.VMA).reverse().slice(0, stockChart.options.lens),
			level = stockChart.options.level[stockChart.options.user.level],
			oddNum = level[0] / 2 != (level[0] / 2).toFixed(),
			currentX,n = 0,
			len = Math.min(stockChart.options.lens,data.length);
		_this.lineWidth = 1.5;
		for(var k in data[0]){
			_this.beginPath();
			currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
			_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[n++];
			_this.moveTo(currentX,getY(data[0][k]));

			for (var i = 0; i < len; i++) {
				if(1*data[i][k]){
					currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
					_this.lineTo(currentX,getY(data[i][k]))
				}
			}
			_this.stroke();
		}
		return data;
	}

	function drawRectStroke(x, y, w, h, isRise) {
		var style = isRise ?
			stockChart.options.schemes[stockChart.options.user.schemes].riseStyle :
			stockChart.options.schemes[stockChart.options.user.schemes].fallStyle;
		style * 1 && _this.fillRect(x, y, w, h);
		if (h) {
			_this.rect(x, y, w, h);
		} else {
			_this.moveTo(x, y);
			_this.lineTo(x + w, y);
		}
	}
	_this.drawVol = function(fillBox, key) {
		var total = stockChart.options.data.DATA.length,
			tmplens = stockChart.options.lens - 1,
			data = [].concat(stockChart.options.data.DATA).reverse().slice(0, stockChart.options.lens);
		for (var i = 0, l = data.length; i < l; i++) {
			max = Math.max(data[i].vol, max);
			//min = Math.min(data[i].vol, min);
		}
		min = 0;
		rule = getRule();
		verticalOffset = stockChart.options.user.offsetTop;
		_this.drawLine(max, min, rule, 0, 1, null, 1);
		for (var i = 0, l = data.length; i < l; i++) {
			drawVolDo(data[i], tmplens - i,0)
		}

		ind = stockChart.options.data.IND.VMA && vma();
		stockChart.fillData[key] = {
			data: data,
			ind : ind
		};
		stockChart.fillBoxs[key] = fillBox;
	}
	
	function drawTimeVolDo(data, i, leftOffset, line) /*(x,y,w,h,isRise)*/ {
		var volume = data.drawData['vol'],
			y, candleX, currentX;
		_this.lineWidth = 1;
		if (!i || data.drawData.pri * 1 == preData * 1) {
			_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].timeVolStable;
		} else if (data.drawData.pri * 1 < preData * 1) {
			_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].timeVolFall;
		} else {
			_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].timeVolRise;
		}
		preData = data.drawData.pri;
		
		_this.beginPath();

		if (!rule) {
			rule = getRule();
		}
		y = getY(volume);
		_this.moveTo(data.x*1 + .5, y);
		_this.lineTo(data.x*1 + .5, _this.h);
		_this.stroke();
	}	
	_this.drawTimeVol = function() {
		var total = stockChart.options.data.DATAT.formatData.length,
			sourData = [].concat(stockChart.options.data.DATAT.data).reverse(),
			data = [].concat(stockChart.options.data.DATAT.formatData).reverse();
		for (var i = 0, l = sourData.length; i < l; i++) {
			max = Math.max(sourData[i].vol, max);
			//min = Math.min(sourData[i].vol, min);
		}
		min = 0;
		preData = data[0].drawData.pri;
		rule = getRule();
		verticalOffset = stockChart.options.user.offsetTop;
		_this.drawLine(max, min, rule, 1, 1, null, 1);
		for (var i = 0, l = data.length; l--; i++) {
			drawTimeVolDo(data[l], i,  stockChart.options.user.ruleWidth, 1)
		}
	}

	_this.clearVol = function() {
		_this.clearRect(0, 0, _this.w, _this.h);
		max = Number.MIN_VALUE;
		min = Number.MAX_VALUE;
		rule = null;
		verticalOffset = null;
	}
}