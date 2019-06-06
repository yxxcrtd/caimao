function fireData(stockChart) {
	var _this = stockChart;
	function chartData(data) {
		for(var l = data.length; l--;){
			data[l] = {
				high : (data[l].highPx).toFixed(2)*1,
				low  : (data[l].lowPx).toFixed(2)*1,
				open : (data[l].openPx).toFixed(2)*1,
				cur  : (data[l].closePx).toFixed(2)*1,
				vol  : (data[l].businessAmount).toFixed()*1,
				//date : data[l].minTime,
				date : new Date(data[l].minTime.slice(0,4),data[l].minTime.slice(4,6)-1,data[l].minTime.slice(6,8),data[l].minTime.slice(8,10),data[l].minTime.slice(10,12),data[l].minTime.slice(12,14)),
				rate : data[l].pxChangeRate
			}
		}
		data.reverse();
		stockChart.options.data.DATA = data;
		kma(data);
		vma(data);

	}
function more(){
	var d = [],
		v2 = 1444449534000,
		v1;
	for(var i = 0; i < 1262; i++){
		v1 = Math.random()*20000;
		d.push({
			avg: v1 + Math.random()*1000 + 1000,
			pri : v1,
			datetime : v2 + 60*1000*(i+1),
			vol : Math.random()*2000,
			preClosePx : 19398
		});
	}
	return d;
}
	function timeShare(data) { //act106
		if (!data) return;
		if(data[0]){
			data = data[0].length ? data[0] : data
		}else{
			data = [];
		}
		for(var l = data.length; l--;){
			data[l] = {
				avg : data[l].averagePx*1,
				preClosePx : data[l].preClosePx*1,
				pri  : data[l].closePx*1,
				vol  : (data[l].businessAmount).toFixed()*1,
				datetime : new Date(data[l].minTime.slice(0,4),data[l].minTime.slice(4,6)-1,data[l].minTime.slice(6,8),data[l].minTime.slice(8,10),data[l].minTime.slice(10,12),data[l].minTime.slice(12,14))
			}
		}
		data.reverse();
		stockChart.options.data.DATAT = {
			formatData: [],
			data: data
		};
	}

	function macd(data) { //act161
		if (!data) return;
		stockChart.options.data.IND.MACD = data;
		//console.log(1301,data)
	}

	function kdj(data) { //act161
		if (!data) return;
		stockChart.options.data.IND.KDJ = data;
		//console.log(1301,data)
	}

	function rsi(data) { //act161
		if (!data) return;
		stockChart.options.data.IND.RSI = data;
		//console.log(1301,data)
	}


	function MA(key,index,data,n){
		var v = 0;
		if(n - 1 > index)return NaN;
		for(var i = 0;i < n;i++){
			v += data[index - i][key]*1;
		}
		return v/n
	}
	function kma(data) { //act161
		var ma = [5,10,20,30,60],
			d = [];
		if (!data) return;
		for(var i = 0,l = data.length; i < l; i++){
			d[i] = {};
			for(var j = 0;j < ma.length; j ++)
				format(i,ma[j]);
		}
		
		function format(i,n){
			if(i < n){
				d[i]['MA'+n] = 0;
			}else{
				d[i]['MA'+n] = MA('cur',i,data,n);
			}
		}
		stockChart.options.data.IND.KMA = d;
		//console.log(1301,data)
	}
	function vma(data) { //act161
		var ma = [5,20],
			d = [];
		if (!data) return;
		for(var i = 0,l = data.length; i < l; i++){
			d[i] = {};
			for(var j = 0;j < ma.length; j ++)
				format(i,ma[j]);
		}
		
		function format(i,n){
			if(i < n){
				d[i]['MA'+n] = 0;
			}else{
				d[i]['MA'+n] = MA('vol',i,data,n);
			}
		}
		stockChart.options.data.IND.VMA = d;
		//console.log(1301,data)
	}
	function bigScreen(w, timesLens,rw) {
		var old = stockChart.options.data.DATAT.data,
			x = 1,
			stepNo = 0,
			newArr = [],
			base = ~~ (w / timesLens),
			debris = w % timesLens,
			patch = debris / timesLens,
			afterFloat = 0;

		for (var i = 0; i < timesLens; i++) {
			stepNo = getsur();
			x += (base + stepNo);
			old[i] && newArr.push({
				x: x - base + rw*1,
				data: [
					old[i]
				],
				drawData : old[i]
			})
			//console.log(x - base + rw*1)
		}

		function getsur() {
			var res = afterFloat + patch
			afterFloat = res - ~~res;
			return ~~res;
		}
		return newArr;
	}

	function smallScreen(w, timesLens,rw) {
		var old = stockChart.options.data.DATAT.data,
			newArr = [],
			lots = timesLens / w,
			afterFloat = 0,
			data,
			limit = 0;

		for (i = 1; i <= w; i++) {
			data = getData();
			data[0] && newArr.push({
				x: i + rw*1,
				data : data,
				drawData : data[0]//newAvg(data)
			})
		}
		function newAvg(d){
			var res = {avg:0,pri : 0,vol : 0};
			for(var i = 0,l = d.length;i < l;i++){
				res.avg += d[i].avg*1;
				res.pri += d[i].pri*1;
				res.vol += d[i].vol*1;
			}
			res.avg = cutFixed(res.avg / l,3);
			res.pri = cutFixed(res.pri / l,2);
			return l ? res : null;
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
		return newArr;
	}
	stockChart.formatTimeData = function(w,rw) {
		var timesLens = _this.options.user.timesLens;
		stockChart.options.data.DATAT.formatData = w > timesLens ?
			bigScreen(w, timesLens ,rw) :
			smallScreen(w, timesLens,rw);

	}
	return {
		queryHistory: chartData,
		query_macd: macd,
		query_kdj: kdj,
		query_rsi: rsi,
		getMultiDaySnapshot: timeShare
	}
}
