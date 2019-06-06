function _tools(stockChart,box) {
	//this -> ctx
	var _this = this,curIdx;

	function getRule() {
		var dp = max - min;
		return !dp ? 0 : (_this.h - stockChart.options.user.offsetTop) * stockChart.options.user.drawRange / dp;
	}

	function getY(v) {
		return ((max - v) * rule).toFixed() * 1 + verticalOffset * 1 + 0.5;
	}

	function bigScreen(w, timesLens,rw,xpos) {
		var data = stockChart.options.data.DATAT.formatData,
			ind = xpos + rw,tmp;
		for(var i = 0, l = data.length; i < l; i++){
			if(data[i].x > ind)break;
			tmp = data[i].x;
		}
		if(!data[i])
			return [tmp,i-1]
		if(!tmp || Math.abs(tmp - ind) > Math.abs(data[i].x - ind)){
			return [data[i].x,i]
		}else{
			return [tmp,i-1]
		}
	}

	function smallScreen(w, timesLens,rw,xpos) {
		var old = stockChart.options.data.DATAT.data,
			newArr = [],
			lots = timesLens / w,
			afterFloat = 0,
			data,
			limit = 0;
		for (i = 0; i < w; i++) {
			data = getData();
			data[0] && newArr.push({
				x: xpos,
				data : data,
				drawData : data[0]
			})
		}
		function getData() {
			var l = afterFloat + lots,
				tmp = [];
			afterFloat = l - ~~l;
			for(var i = 0; i < ~~l; i++){
				old[i+limit] && tmp.push(old[i+limit])
			}
			limit += ~~l;
			return tmp.length ? tmp : [null];
		}
		return [xpos+rw,xpos];
	}
	function getTimepos(x, y, level,outRule){
		var w = box.size.w - ( outRule ? stockChart.options.user.ruleWidth : 0 )*2 -2,
			idx = x,
			xpos =  w > stockChart.options.user.timesLens ? 
			bigScreen(w, stockChart.options.user.timesLens ,( outRule ? stockChart.options.user.ruleWidth : 0 ),idx) :
			smallScreen(w, stockChart.options.user.timesLens,( outRule ? stockChart.options.user.ruleWidth : 0 ),idx),
			y = ~~(y - (document.documentElement.scrollTop || document.body.scrollTop)) - .5;

		return [xpos[0] + .5,y ,xpos[1]];
	}
	function getKpos(x, y, level,outRule){
		var x = ~~(x/level),
			idx = (x >= stockChart.options.lens) ?
					stockChart.options.lens - 1 :
					(x < 0 ? 0 : x),
			x = ~~( idx*level + level/2 + 1*(stockChart.options.user.timeShare ? ( outRule ? stockChart.options.user.ruleWidth : 0 ) : 0)) + 0.5,
			y = ~~(y - (document.documentElement.scrollTop || document.body.scrollTop)) - .5;
		return [x,y,idx];
	}
	_this.crossLine = function(x, y, level){
		var outRule = box.size.w > stockChart.options.user.outRuleWidth;
		var pos = stockChart.options.user.timeShare ? getTimepos(x,y,level,outRule) : getKpos(x,y,level,outRule);
		_this.clear();
		_this.beginPath();
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].crossLine;
		
		_this.moveTo(pos[0], 0);
		_this.lineTo(pos[0], _this.h - stockChart.options.user.timeline);



		if(pos[1] < _this.h - stockChart.options.user.timeline){
			_this.moveTo(stockChart.options.user.timeShare ? ( outRule ? stockChart.options.user.ruleWidth : 0 ) : 0, pos[1]);
			_this.lineTo(_this.w - ( outRule ? stockChart.options.user.ruleWidth : 0 ), pos[1]);
		}
		_this.stroke();
	}
	_this.clear = function(){
		_this.clearRect(0,0,_this.w,_this.h);
	}
	
	_this.drawTimeAndVol = function(x, y, level){
		var outRule = box.size.w > stockChart.options.user.outRuleWidth;
		var pos = stockChart.options.user.timeShare ? getTimepos(x,y,level,outRule) : getKpos(x,y,level,outRule),
			idx = pos[2];
		if(y === null && level === null){
			idx = null;
		}else{
			//console.log('draw');
		}
		if(curIdx == idx)return;
		//console.log(idx)
		stockChart.fillCurData(idx);
		curIdx = idx;
	}
}