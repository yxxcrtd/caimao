function _timeShare(stockChart) {
	//this -> ctx
	var _this = this,
		max = Number.MIN_VALUE,
		min = Number.MAX_VALUE,
		rule, verticalOffset,
		first;

	function getRule() {
		var rh = stockChart.options.user.timeLineMinRuleHeight,
			count = ~~ ((_this.h - stockChart.options.user.offsetTop) / rh),
			middle = ~~ (((_this.h - stockChart.options.user.offsetTop) / 2) + stockChart.options.user.offsetTop),
			dp = max - min,
			unit, offsetTop;
		count = (count % 2 ? count - 1 : count);
		unit = (_this.h - stockChart.options.user.offsetTop) / count;
		offsetTop = middle - unit * (count / 2 - 1);
		verticalOffset = ~~offsetTop;
		return !dp ? 0 : unit * (count - 2) / dp;
	}


	function getY(v) {
		return ((max - v) * rule).toFixed() * 1 + verticalOffset * 1 + 0.5;
	}
	_this.drawTimeShare = function(fillBox,key,box) {
		var total = stockChart.options.data.DATAT.formatData.length,
			sourData = [].concat(stockChart.options.data.DATAT.data).reverse(),
			data = [].concat(stockChart.options.data.DATAT.formatData).reverse(),
			diff,last,
			len = data.length,
			pre = (sourData[0].preClosePx).toFixed(2),
			outRule = box.size.w > stockChart.options.user.outRuleWidth;

		_this.clearRect(0, 0, _this.w, _this.h);
		for (var i = 0,l= sourData.length; i < l; i++) {
			max = Math.max(sourData[i].pri, max);
		//	if(sourData[i].avg !== null){max = Math.max(sourData[i].avg, max);}
			min = Math.min(sourData[i].pri, min);
		//	if(sourData[i].avg !== null){min = Math.min(sourData[i].avg, min);}
		}
		if(max - min < 0.2){
			max += 0.20;
			min -= 0.20;
		}
		diff = [max - pre, pre - min];
		if (diff[0] > diff[1]) {
			min = pre - diff[0];
		} else {
			max = pre * 1 + diff[1] * 1;
		}

		rule = getRule();
		_this.drawTimeLine(max, min, rule, 2, pre,outRule);
		

		_this.beginPath();
		_this.lineWidth = 1.5;
		_this.moveTo(data[0].x, getY(data[0].drawData.pri));
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[0];
		
		for (var i = 0; i < len; i++) {
			_this.lineTo(data[i].x, getY(data[i].drawData.pri));
		}
		_this.stroke();



		if(stockChart.options.schemes[stockChart.options.user.schemes].timeShareShadow){
		    _this.beginPath();
			for (var i = 0; i < len; i++) {
				_this.lineTo(data[i].x, getY(data[i].drawData.pri));
				last = data[i];
			}
			_this.lineTo(last.x,_this.h)
			_this.lineTo(data[0].x,_this.h)
	    	_this.closePath();
		    _this.fillStyle = stockChart.options.schemes[stockChart.options.user.schemes].shadowFill;  
		    _this.fill();
	    }


	  //  if(data[0].drawData.type * 1 && !1){

			_this.beginPath();
			_this.moveTo(data[0].x, getY(data[0].drawData.avg));
			_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[1];
			for (var i = 0; i < len; i++) {
				/*	if(data[i].drawData.avg !== null){
					_this.lineTo(data[i].x, getY(data[i].drawData.avg))
				}else{
					_this.moveTo(data[i].x, getY(pre));
				}*/
				_this.moveTo(data[i].x, getY(pre));
			}
	//	}

		
		first= 1;
		_this.stroke();

		stockChart.fillData[key] = {data :data,name : stockChart.options.data.DATA2.name || ''};
		stockChart.fillBoxs[key] = fillBox;
	}
	_this.clearTimeShare = function() {
		max = Number.MIN_VALUE,
		min = Number.MAX_VALUE,
		rule = null,
		verticalOffset = null;
	}
}