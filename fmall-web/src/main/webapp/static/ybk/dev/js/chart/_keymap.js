function _keymap() {
	//this -> stockChart
	var _this = this,
		stockChart = _this,
		map,
		ind = [
			{"ex":"0","code":"KDJ","name":"随机指标","action":"actionSetInd"},
			{"ex":"0","code":"MACD","name":"指数平滑异同平均线","action":"actionSetInd"},
			{"ex":"0","code":"MTM","name":"动量线","action":"actionSetInd"}
		];
	ind = JSON.stringify(ind);
	_this.setKeyMap = function(v) {
		map = window['MAP'] || stockChart.options.data.MAP;
		if(!v)return;
        var regCode = new RegExp('\\{[^\\}]*"code":"[\\.]*'+v+'[^\\}]*}',"gi"),
            regSpell = new RegExp('\\{[^\\}]*"spell":"[\\.]*'+v+'[^\\}]*}',"gi"),
            befor = (window["HASINDKEY"] && stockChart.options.hasInd[stockChart.options.user.period]) ? ind.match(regCode) || [] : [];
        return befor.concat(map.match(regCode) || map.match(regSpell) || []);
	}
}