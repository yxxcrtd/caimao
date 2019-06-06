function _bgLine(stockChart) {
	//this -> ctx
	var _this = this,
		_max, _min, _rule, _verticalOffset, _fixed, besid, _bediff;
	_this.drawLine = function(max, min, rule, fixed, position, minHeight,notcut) {
		//position undefined -> center,  1 -> bottom
		var outRule = _this.w > stockChart.options.user.outRuleWidth,
			h = (_this.h - stockChart.options.user.offsetTop) * (stockChart.options.user.timeShare ? 1 : stockChart.options.user.drawRange),
			rh = minHeight || (_this.types ? stockChart.options.user.minRuleHeight : stockChart.options.user.vMinRuleHeight),
			count = ~~ (h / rh),
			unit = h / count,
			xROffset = outRule ? stockChart.options.user.ruleWidth : 0,
			special, xOffset = stockChart.options.user.timeShare ? xROffset : 0,
			splitWidth = (_this.w - xROffset*2) / 8,
			spx = xOffset,__y;

		_rule = rule;
		_fixed = fixed;
		besid = false;
		if (!position) {
			_verticalOffset = ~~ ((_this.h - stockChart.options.user.offsetTop) * (.5 - stockChart.options.user.drawRange / 2) + stockChart.options.user.offsetTop);
		} else {
			_verticalOffset = stockChart.options.user.offsetTop;
			--count;
		}

		_max = max;
		_min = min;
		if (max == min) return;
		_this.font = '12px arial';
		_this.textBaseline = "middle";
		_this.lineWidth = 1;
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].layoutColor;
		_this.fillStyle = stockChart.options.schemes[stockChart.options.user.schemes].layoutFont;
		_this.beginPath();
		for (var i = 0; i <= count; i++) {
			if (i == 0) {
				special = _max;
			} else if (i == count && !position) {
				special = _min;
			} else {
				special = null;
			}
			_this.moveForDashed(xOffset, _verticalOffset + i * unit - .5);
			_this.dashedLineTo(_this.w - xROffset, _verticalOffset + i * unit - .5, 2);
			setVal(_this.w - xROffset + 2, _verticalOffset + i * unit - .5, rule, special,notcut);
		}
		_this.stroke();
		_this.drawZeroLine = function(y) {
			return;
			_this.font = '12px arial';
			_this.textBaseline = "middle";
			_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].layoutColor;
			_this.fillStyle = stockChart.options.schemes[stockChart.options.user.schemes].layoutFont;
			_this.beginPath();
			_this.moveTo(xROffset, y);
			_this.lineTo(_this.w - xROffset, y);
			setVal(_this.w - xROffset + 2, y, rule, '0');
			besid = y;
			_this.stroke();
		}
/*		if(stockChart.options.user.timeShare && outRule){
			for(var i = 0; i < 7;i++){
				spx += splitWidth;
				_this.beginPath();
				if(i % 2){
					_this.lineWidth = stockChart.options.schemes[stockChart.options.user.schemes].specialLine ? 2 : 1;
					_this.moveTo(spx - .5,0);
					_this.lineTo(spx - .5, _this.h);
				}else{
					_this.lineWidth = 1;
					_this.moveForDashed(spx - .5,0);
					_this.dashedLineTo(spx - .5, _this.h, 2);
				}
				_this.stroke();
			}
		}*/
	}
	
	_this.drawTimeLine = function(max, min, rule, fixed, bediff,outRule) {
		var middle = ~~ (((_this.h - stockChart.options.user.offsetTop) / 2) + stockChart.options.user.offsetTop),
			rh = stockChart.options.user.timeLineMinRuleHeight,
			count = ~~ ((_this.h - stockChart.options.user.offsetTop) / rh),
			unit, rg = 1,
			xOffset = outRule ? stockChart.options.user.ruleWidth : 0,
			splitWidth = (_this.w - xOffset*2) / 8,
			spx = xOffset,__y;
		_max = max;
		_min = min;

		count = (count % 2 ? count - 1 : count)/2;
		unit = (_this.h - stockChart.options.user.offsetTop) / (count*2);
		stockChart.options.unit = unit;
		_bediff = bediff*1;
		_fixed = fixed;
		_verticalOffset = stockChart.options.user.offsetTop;
		_rule = getRule();
		if (max == min) return;
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].layoutColor;
		_this.lineWidth = 1;

/*		if(outRule){
			for(var i = 0; i < 7;i++){
				spx += splitWidth;
				_this.beginPath();
				if(i % 2){
					_this.lineWidth = stockChart.options.schemes[stockChart.options.user.schemes].specialLine ? 2 : 1;
					_this.moveTo(spx - .5, cutFixed(middle - (count-1) * unit - .5, 0));
					_this.lineTo(spx - .5, _this.h);
				}else{
					_this.lineWidth = 1;
					_this.moveForDashed(spx - .5, cutFixed(middle - (count-1) * unit - .5, 0));
					_this.dashedLineTo(spx - .5, _this.h, 2);
				}
				_this.stroke();
			}
		}*/

		_this.beginPath();
		if(outRule){
			for (var i = 1; i < count; i++) {
				__y = cutFixed(middle - i * unit - .5, 0);
				_this.moveTo(xOffset, __y);
				_this.lineTo(_this.w - xOffset, __y);

				__y = cutFixed(i * unit + (middle - .5), 0);
				_this.moveTo(xOffset, __y);
				_this.lineTo(_this.w - xOffset, __y);
			}
		}else{
				__y = cutFixed(middle - (count - 1) * unit - .5, 0);
				_this.moveTo(xOffset, __y);
				_this.lineTo(_this.w - xOffset, __y);

				__y = cutFixed((count - 1) * unit + (middle - .5), 0);
				_this.moveTo(xOffset, __y);
				_this.lineTo(_this.w - xOffset, __y);
		}
		_this.stroke();
		_this.beginPath();
		if(outRule){
			for (var i = 1; i < count; i++) {
				__y = cutFixed(middle - i * unit - .5, 0);
				setTimeVal(_this.w - xOffset + 2, __y, rule, 1 , i);
				__y = cutFixed(i * unit + (middle - .5), 0);
				setTimeVal(_this.w - xOffset + 2, __y, rule, -1 , i);
			}
			__y = cutFixed(middle - .5, 0);
			setTimeVal(_this.w - xOffset + 2, __y, rule, 0 , 0);
		}else{
			__y = cutFixed(middle - (count - 1) * unit - .5, 0);
			setTimeVal(_this.w, __y, rule, 1 , (count - 1));

			__y = cutFixed((count - 1) * unit + (middle - .5), 0);
			setTimeVal(_this.w, __y, rule, -1 , (count - 1));
		}
		//zeroLine
		_this.beginPath();
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].layoutColor;
		_this.lineWidth = stockChart.options.schemes[stockChart.options.user.schemes].specialLine ? 3 : 1;
		_this.moveTo(xOffset, middle - .5);
		_this.lineTo(_this.w - xOffset, middle - .5);
		_this.stroke();
		_this.lineWidth = 1;


	function n2v(n) {
		return cutFixed((_max - _bediff) * n/(count-1), 2);
	}

	function setTimeVal(x, y, rule, rg, n) {
		var v;
		if (rg > 0) {
			_this.fillStyle = stockChart.options.schemes[stockChart.options.user.schemes].riseFont;
			v = n2v(n) * 1 + _bediff;
		} else if (rg == 0) {
			v = _bediff
			_this.fillStyle = stockChart.options.schemes[stockChart.options.user.schemes].fontColor;
		} else {
			_this.fillStyle = stockChart.options.schemes[stockChart.options.user.schemes].fallFont;
			v = _bediff - n2v(n)
		}
		_this.font = '12px arial';
		_this.textAlign = outRule ? "right" : "left";
		_this.textBaseline = "middle";
		_this.fillText(
			shortIndex(v, (_fixed || 0), stockChart.options.user.binary, stockChart.options.binary[stockChart.options.user.binary]), xOffset - (outRule ? 5 : -2), y);
		_this.textAlign = outRule ? "left" : "right";
		_this.fillText(
			shortIndex((v - _bediff) * 100 / _bediff, (_fixed || 0), stockChart.options.user.binary, stockChart.options.binary[stockChart.options.user.binary]) + '%', x + (outRule ? 5 : -2), y);
	}



	}

	function getRule() {
		var dp = _max - _min;
		return !dp ? 0 : (_this.h - _verticalOffset) / dp;
	}

	function y2v(y) {
		return cutFixed(_max - (y - _verticalOffset - 0.5) / _rule, 2);
	}

	function setVal(x, y, rule, v, notcut) {
		_this.fillText(
			shortIndex(v === 0 ? v : (v || y2v(y) * 1), (_fixed || 0), stockChart.options.user.binary, stockChart.options.binary[stockChart.options.user.binary], notcut), x + 5, y);
	}

}