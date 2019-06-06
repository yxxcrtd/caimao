function fireData(stockChart) {
	function chartData(data) {
		for(var l = data.length; l--;){
			data[l] = {
				high : (data[l].highPrice/100).toFixed(2)*1,
				low  : (data[l].lowPrice/100).toFixed(2)*1,
				open : (data[l].openPrice/100).toFixed(2)*1,
				cur  : (data[l].curPrice/100).toFixed(2)*1,
				vol  : (data[l].totalAmount/100).toFixed()*1,
				date : data[l].date,
				rate : data[l].currentGains
			}
		}
		data.reverse();
		stockChart.options.data.DATA = data;
		kma(data);
		vma(data);

	}
function more(){
	var d = [];
	for(var i = 0; i < 100; i++){
		d.push({
			curPrice : Math.random()*20000,
			type : 1,
			totalAmount:  Math.random()*1000000 + 540000,
			totalMoney:  Math.random()*1000000 + 540000,
			datetime : 1444449534000,
			yesterPrice : 19398
		});
	}
	return d;
}
	function timeShare(data) { //act106
		var per = 5;
		if (!data) return;
		data.reverse();
		for(var i = 0, l = data.length; i < l; i++){
			data[i].pri = (data[i].curPrice/100).toFixed(2)*1;
			data[i].vol = (data[i].totalAmount/100).toFixed(2)*1;
			data[i].avg = data[i].totalAmount*1 ? (data[i].totalMoney/data[i].totalAmount).toFixed(2)*1 : null;
		}
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
		var timesLens = 242;
		stockChart.options.data.DATAT.formatData = w > timesLens ?
			bigScreen(w, timesLens ,rw) :
			smallScreen(w, timesLens,rw);

	}
	return {
		query_kline: chartData,
		query_macd: macd,
		query_kdj: kdj,
		query_rsi: rsi,
		query_timeline: timeShare
	}
}