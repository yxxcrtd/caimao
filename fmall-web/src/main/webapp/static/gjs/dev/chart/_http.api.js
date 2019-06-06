function requestAPI(stockChart) {
	var result = [],
		ind = {};

	function release() {
		result = [];
		return result;
	}

	function queryHistory(id, period, ex, limit) {
		var limit = limit || 500,
			periodVal = stockChart.options.cycMap[period || '1day'];
		result.push({
			"act": "/api/gjshq/candle/queryHistory",
			"prodCode" : encodeURIComponent(id+"."+ex),
			"cycle" : periodVal,
			"number" : limit
		});

	//	stockChart.options.hasInd[period] && ind[stockChart.options.user.vIndicator.toLowerCase()](period, id, ex, limit);
		//stockChart.options.hasKma[period] && kma(act, id, ex, limit);
		return result;
	}


	ind.macd = function(period, id, ex, limit) {
		result.push({
			"act": "query_macd",
			"code": id,
			"exchange_short_name": ex,
			"limit": limit
		});
	}
	ind.rsi = function(period, id, ex, limit) {
		result.push({
			"act": "query_rsi",
			"code": id,
			"exchange_short_name": ex,
			"limit": limit
		});
	}
	ind.kdj = function(period, id, ex, limit) {
		result.push({
			"act": "query_kdj",
			"code": id,
			"exchange_short_name": ex,
			"limit": limit
		});
	}

	function timeShare(id, ex) {
		result.push({
			"act": "/api/gjshq/snapshot/getMultiDaySnapshot",
			"prodCode" : encodeURIComponent(id+"."+ex),
			"type" : 1
		});
		return result;
	}
	function getdata(){
		return result;
	}

	return {
		data: getdata, //请求参数
		release: release, //释放参数
		queryHistory: queryHistory, //k线
		timeShare: timeShare //分时
	}
}