function requestAPI(stockChart) {
	var result = [],
		ind = {};

	function release() {
		result = [];
		return result;
	}

	function period(id, period, ex, limit) {
		var limit = limit || 300,
			peroid = stockChart.options.periodMap[period || '1day'];
		result.push({
			"act": "query_kline",
			"code" : id,
			"period" : peroid,
			"exchange_short_name" : ex,
			"limit" : limit
		});

		stockChart.options.hasInd[period] && ind[stockChart.options.user.vIndicator.toLowerCase()](peroid, id, ex, limit);
		//stockChart.options.hasKma[period] && kma(act, id, ex, limit);
		return result;
	}


	ind.macd = function(peroid, id, ex, limit) {
		result.push({
			"act": "query_macd",
			"code": id,
			"exchange_short_name": ex,
			"limit": limit
		});
	}
	ind.rsi = function(peroid, id, ex, limit) {
		result.push({
			"act": "query_rsi",
			"code": id,
			"exchange_short_name": ex,
			"limit": limit
		});
	}
	ind.kdj = function(peroid, id, ex, limit) {
		result.push({
			"act": "query_kdj",
			"code": id,
			"exchange_short_name": ex,
			"limit": limit
		});
	}

	function timeShare(id, ex,limit) {
		var limit = limit || 300;
		result.push({
			"act": "query_timeline",
			"code": id,
			"exchange_short_name": ex,
			"limit": limit
		})
		return result;
	}
	function getdata(){
		return result;
	}

	return {
		data: getdata, //请求参数
		release: release, //释放参数
		period: period, //k线
		timeShare: timeShare //分时
	}
}