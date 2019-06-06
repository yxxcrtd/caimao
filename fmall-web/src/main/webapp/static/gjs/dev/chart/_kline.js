function _kline(stockChart) {
	//this -> ctx
	var _this = this,
		mark = {
			low: null,
			lowCount: 1,
			highCount: 1,
			high: null
		},
		max = Number.MIN_VALUE,
		min = Number.MAX_VALUE,
		_max = Number.MIN_VALUE,
		_min = Number.MAX_VALUE,
		rule, verticalOffset;


	function getRule() {
		var dp = max - min;
		return !dp ? 0 : (_this.h - stockChart.options.user.offsetTop) * stockChart.options.user.drawRange / dp;
	}

	function getY(v) {
		return ((max - v) * rule).toFixed() * 1 + verticalOffset * 1 + 0.5;
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

	function candle(data, i) {
		data.index = i;
		var isRise = data.cur * 1 >= data.open * 1,
			color = isRise ?
			stockChart.options.schemes[stockChart.options.user.schemes].rise :
			stockChart.options.schemes[stockChart.options.user.schemes].fall,
			level = stockChart.options.level[stockChart.options.user.level],
			oddNum = level[0] / 2 != (level[0] / 2).toFixed(),
			candleX, currentX, candleY, candleHeight, topY, bottomY;
		if (!rule) {
			rule = getRule();
		}
		//candleRect
		_this.strokeStyle = color;
		_this.fillStyle = color;
		_this.beginPath();
		currentX = level[0] * i + level[0] / 2 + (oddNum ? 0 : .5);

		if (currentX > _this.w - level[0]) return null;
		topY = getY(data.high);
		bottomY = getY(data.low);
		if (level[1] > 2) {
			candleX = currentX - level[1] / 2 + .5;
			if (isRise) {
				candleY = getY(data.cur);
				candleHeight = getY(data.open) - candleY;
			} else {
				candleY = getY(data.open);
				candleHeight = getY(data.cur) - candleY;
			}
			drawRectStroke(candleX, candleY, level[1], candleHeight, isRise);

			_this.moveTo(currentX, topY);
			_this.lineTo(currentX, candleY)
			_this.moveTo(currentX, candleY + candleHeight);
			_this.lineTo(currentX, bottomY)
		} else { //candleLine
			_this.moveTo(currentX, topY);
			_this.lineTo(currentX, bottomY == topY ? topY + .5 : bottomY);
		}

		_this.stroke();
		if (_max == data.high || _min == data.low) {
			_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].fontColor;
			_this.fillStyle = stockChart.options.schemes[stockChart.options.user.schemes].fontColor;
			_this.beginPath();
			_this.font = '12px arial';
		}
		if (stockChart.options.user.showHigh && _max == data.high && !mark.high) {
			/* 标示最高价 */
			_this.moveTo(currentX + 18, topY - 13 * mark.highCount);
			_this.lineTo(currentX, topY - 13 * mark.highCount);
			_this.lineTo(currentX, topY);
			_this.lineTo(currentX + 3, topY - 5);
			_this.moveTo(currentX, topY);
			_this.lineTo(currentX - 3, topY - 5);
			_this.fillText(cutFixed(data.high, 2), currentX + 20, topY - 13 * mark.highCount);
			_this.stroke();
			mark.high = 1;
		}
		if (stockChart.options.user.showLow && _min == data.low && !mark.low) {
			/* 标示最低价 */
			_this.moveTo(currentX + 10, bottomY + 13 * mark.lowCount);
			_this.lineTo(currentX, bottomY + 13 * mark.lowCount);
			_this.lineTo(currentX, bottomY);
			_this.lineTo(currentX + 3, bottomY + 5);
			_this.moveTo(currentX, bottomY);
			_this.lineTo(currentX - 3, bottomY + 5);
			_this.fillText(cutFixed(data.low, 2), currentX + 10, bottomY + 13 * mark.lowCount);
			_this.stroke();
			mark.low = 1;
		}

	}
	function kma(){
		var total = stockChart.options.data.IND.KMA.length,
			tmplens = stockChart.options.lens - 1,
			data = [].concat(stockChart.options.data.IND.KMA).reverse().slice(0, stockChart.options.lens),
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
	_this.drawKLine = function(fillBox,key) {
		var total = stockChart.options.data.DATA.length,
			tmplens = stockChart.options.lens - 1,
			data = [].concat(stockChart.options.data.DATA).reverse().slice(0, stockChart.options.lens),
			ind,dep = 0.3,gap = 0;
			
		for (var i = 0; i < stockChart.options.lens; i++) {
			max = Math.max(data[i].high, max);
			min = Math.min(data[i].low, min);
		}
		if(max - min < dep){
			gap = Math.ceil((dep - Math.abs(max - min))*100/2)/100;
		}
		_max = max;
		_min = min;
		max = max + gap;
		min = min - gap;
		rule = getRule();
		_this.drawLine(max, min, rule, 2);
		verticalOffset = ~~ ((_this.h - stockChart.options.user.offsetTop) * (.5 - stockChart.options.user.drawRange / 2) + stockChart.options.user.offsetTop);
		for (var i = 0; i < stockChart.options.lens; i++) {
			candle(data[i], tmplens - i)
		}
		ind = stockChart.options.data.IND.KMA && kma();
		stockChart.fillData[key] = {data : data, ind : ind, name : stockChart.options.data.DATA2.name || ''};
		stockChart.fillBoxs[key] = fillBox;
	}

	_this.clearK = function(size) {
		_this.clearRect(0, 0, _this.w, _this.h);
		rule = false;
		max = Number.MIN_VALUE;
		min = Number.MAX_VALUE;
		_max = Number.MIN_VALUE;
		_min = Number.MAX_VALUE;
		verticalOffset = null;
		mark = {
			low: null,
			lowCount: 1,
			highCount: 1,
			high: null
		};
	}
}