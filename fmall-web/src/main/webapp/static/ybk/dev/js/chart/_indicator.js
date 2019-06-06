function _indicator(stockChart){
	//this -> ctx
	var _this = this,
		rule = null,
		max = Number.MIN_VALUE,
		min = Number.MAX_VALUE,
		indFn = {};


	function getRule() {
		var dp = max - min;
		return !dp ? 0 : (_this.h - stockChart.options.user.offsetTop) * stockChart.options.user.drawRange / dp;
	}

	function getY(v) {
		return ((max - v) * rule).toFixed() * 1 + verticalOffset * 1 + 0.5;
	}

	indFn.MACD = function(fillBox){
		if(!stockChart.options.data.IND.MACD)return;
		var total = stockChart.options.data.IND.MACD.length,
			tmplens = stockChart.options.lens - 1,
			data = [].concat(stockChart.options.data.IND.MACD).reverse().slice(0, stockChart.options.lens),
			level = stockChart.options.level[stockChart.options.user.level],
			oddNum = level[0] / 2 != (level[0] / 2).toFixed(),
			currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5),
			isRise;
		for (var i = 0,l = data.length; i < l; i++) {
			data[i].dea = (data[i].dea/100).toFixed(2)*1;
			data[i].dif = (data[i].dif/100).toFixed(2)*1;
			data[i].macd = (data[i].macd/100).toFixed(2)*1;
			max = Math.max(data[i].dea, max);
			max = Math.max(data[i].dif, max);
			max = Math.max(data[i].macd, max);
			min = Math.min(data[i].dea, min);
			min = Math.min(data[i].dif, min);
			min = Math.min(data[i].macd, min);
		}
		rule = getRule();
		verticalOffset = ~~ ((_this.h - stockChart.options.user.offsetTop) * (.5 - stockChart.options.user.drawRange / 2) + stockChart.options.user.offsetTop);
		_this.drawLine(max, min, rule, 2);
		_this.drawZeroLine(getY(0));



		for (var i = 0,l = data.length; i < l; i++) {
			_this.beginPath();
			_this.lineWidth = 1;
			isRise = data[i].macd > 0 ? 'MACDrise' : 'MACDfall';
			_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes][isRise];
			currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
			_this.moveTo(currentX,getY(data[i].macd));
			_this.lineTo(currentX,getY(0));
			_this.stroke();
		}

		_this.beginPath();
		_this.lineWidth = 1.5;
		currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
		_this.moveTo(currentX,getY(data[0].dea));
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[1];
		for (var i = 0,l = data.length; i < l; i++) {
			currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
			_this.lineTo(currentX,getY(data[i].dea));
		}
		_this.stroke();

		_this.beginPath();
		currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
		_this.moveTo(currentX,getY(data[0].dif));
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[0];
		for (var i = 0,l = data.length; i < l; i++) {
			currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
			_this.lineTo(currentX,getY(data[i].dif))
		}
		_this.stroke();
		stockChart.fillData[stockChart.options.user.vIndicator.toUpperCase()] = {data : data};
		stockChart.fillBoxs[stockChart.options.user.vIndicator.toUpperCase()] = fillBox;
	}
	indFn.KDJ = function(fillBox){
		if(!stockChart.options.data.IND.KDJ)return;
		//return;
		var total = stockChart.options.data.IND.KDJ.length,
			tmplens = stockChart.options.lens - 1,
			data = [].concat(stockChart.options.data.IND.KDJ).reverse().slice(0, stockChart.options.lens),
			level = stockChart.options.level[stockChart.options.user.level],
			oddNum = level[0] / 2 != (level[0] / 2).toFixed(),
			currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5),
			isRise;
		for (var i = 0,l = data.length; i < l; i++) {

			data[i].k = (data[i].k/100).toFixed(2)*1;
			data[i].d = (data[i].d/100).toFixed(2)*1;
			data[i].j = (data[i].j/100).toFixed(2)*1;

			max = Math.max(data[i].k, max);
			max = Math.max(data[i].d, max);
			max = Math.max(data[i].j, max);
			min = Math.min(data[i].k, min);
			min = Math.min(data[i].d, min);
			min = Math.min(data[i].j, min);
		}
		max = 100;
		min = 0;
		rule = getRule();
		verticalOffset = ~~ ((_this.h - stockChart.options.user.offsetTop) * (.5 - stockChart.options.user.drawRange / 2) + stockChart.options.user.offsetTop);
		_this.drawLine(max, min, rule, 2);



		_this.beginPath();
		_this.lineWidth = 1.5;
		currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
		_this.moveTo(currentX,getY(data[0].k));
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[0];
		for (var i = 0,l = data.length; i < l; i++) {
			currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
			_this.lineTo(currentX,getY(data[i].k));
		}
		_this.stroke();



		_this.beginPath();
		_this.lineWidth = 1.5;
		currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
		_this.moveTo(currentX,getY(data[0].d));
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[1];
		for (var i = 0,l = data.length; i < l; i++) {
			currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
			_this.lineTo(currentX,getY(data[i].d));
		}
		_this.stroke();

		_this.beginPath();
		currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
		_this.moveTo(currentX,getY(data[0].j));
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[2];
		for (var i = 0,l = data.length; i < l; i++) {
			currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
			_this.lineTo(currentX,getY(data[i].j))
		}
		_this.stroke();
		clearfill();
		stockChart.fillData[stockChart.options.user.vIndicator.toUpperCase()] = {data : data};
		stockChart.fillBoxs[stockChart.options.user.vIndicator.toUpperCase()] = fillBox;
	}

	indFn.MTM = function(fillBox){
		if(!stockChart.options.data.IND.MTM)return;
		var total = stockChart.options.data.IND.MTM.length,
			tmplens = stockChart.options.lens - 1,
			data = [].concat(stockChart.options.data.IND.MTM).reverse().slice(0, stockChart.options.lens),
			level = stockChart.options.level[stockChart.options.user.level],
			oddNum = level[0] / 2 != (level[0] / 2).toFixed(),
			currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5),
			isRise;
		for (var i = 0,l = data.length; i < l; i++) {
			max = Math.max(data[i].mtm, max);
			max = Math.max(data[i].mtmma, max);
			min = Math.min(data[i].mtm, min);
			min = Math.min(data[i].mtmma, min);
		}
		rule = getRule();
		verticalOffset = ~~ ((_this.h - stockChart.options.user.offsetTop) * (.5 - stockChart.options.user.drawRange / 2) + stockChart.options.user.offsetTop);
		_this.drawLine(max, min, rule, 2);



		_this.beginPath();
		_this.lineWidth = 1.5;
		currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
		_this.moveTo(currentX,getY(data[0].mtm));
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[0];
		for (var i = 0,l = data.length; i < l; i++) {
			currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
			_this.lineTo(currentX,getY(data[i].mtm));
		}
		_this.stroke();



		_this.beginPath();
		_this.lineWidth = 1.5;
		currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
		_this.moveTo(currentX,getY(data[0].mtmma));
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[1];
		for (var i = 0,l = data.length; i < l; i++) {
			currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
			_this.lineTo(currentX,getY(data[i].mtmma));
		}
		_this.stroke();
		clearfill();
		stockChart.fillData[stockChart.options.user.vIndicator.toUpperCase()] = {data : data};
		stockChart.fillBoxs[stockChart.options.user.vIndicator.toUpperCase()] = fillBox;
	}



	indFn.RSI = function(fillBox){
		
		if(!stockChart.options.data.IND.RSI)return;
		//return;
		var total = stockChart.options.data.IND.RSI.length,
			tmplens = stockChart.options.lens - 1,
			data = [].concat(stockChart.options.data.IND.RSI).reverse().slice(0, stockChart.options.lens),
			level = stockChart.options.level[stockChart.options.user.level],
			oddNum = level[0] / 2 != (level[0] / 2).toFixed(),
			currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5),
			isRise;
		for (var i = 0,l = data.length; i < l; i++) {
			data[i].rsi1 = (data[i].rsi1/100).toFixed(2)*1;
			data[i].rsi2 = (data[i].rsi2/100).toFixed(2)*1;
			data[i].rsi3 = (data[i].rsi3/100).toFixed(2)*1;
			max = Math.max(data[i].rsi1, max);
			max = Math.max(data[i].rsi2, max);
			max = Math.max(data[i].rsi3, max);
			min = Math.min(data[i].rsi1, min);
			min = Math.min(data[i].rsi2, min);
			min = Math.min(data[i].rsi3, min);
		}
		max = 100;
		min = 0;
		rule = getRule();
		verticalOffset = ~~ ((_this.h - stockChart.options.user.offsetTop) * (.5 - stockChart.options.user.drawRange / 2) + stockChart.options.user.offsetTop);
		_this.drawLine(max, min, rule, 2);



		_this.beginPath();
		_this.lineWidth = 1.5;
		currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
		_this.moveTo(currentX,getY(data[0].rsi1));
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[0];
		for (var i = 0,l = data.length; i < l; i++) {
			currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
			_this.lineTo(currentX,getY(data[i].rsi1));
		}
		_this.stroke();



		_this.beginPath();
		_this.lineWidth = 1.5;
		currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
		_this.moveTo(currentX,getY(data[0].rsi2));
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[1];
		for (var i = 0,l = data.length; i < l; i++) {
			currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
			_this.lineTo(currentX,getY(data[i].rsi2));
		}
		_this.stroke();

		_this.beginPath();
		currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
		_this.moveTo(currentX,getY(data[0].rsi3));
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[2];
		for (var i = 0,l = data.length; i < l; i++) {
			currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
			_this.lineTo(currentX,getY(data[i].rsi3))
		}
		_this.stroke();
		clearfill();
		stockChart.fillData[stockChart.options.user.vIndicator.toUpperCase()] = {data : data};
		stockChart.fillBoxs[stockChart.options.user.vIndicator.toUpperCase()] = fillBox;
	}

	function clearfill(){
		delete stockChart.fillData["MACD"];
		delete stockChart.fillBoxs["MACD"];
		delete stockChart.fillData["KDJ"];
		delete stockChart.fillBoxs["KDJ"];
		delete stockChart.fillData["MTM"];
		delete stockChart.fillBoxs["MTM"];
		delete stockChart.fillData["RSI"];
		delete stockChart.fillBoxs["RSI"];
	}
	_this.clearInd = function(){
		_this.clearRect(0, 0, _this.w, _this.h);
		max = Number.MIN_VALUE;
		min = Number.MAX_VALUE;
		rule = null;
	}

	_this.drawInd = function(fillBox){
		var ind = stockChart.options.user.vIndicator;

		indFn[ind.toUpperCase()] && indFn[ind.toUpperCase()](fillBox);
	}

}