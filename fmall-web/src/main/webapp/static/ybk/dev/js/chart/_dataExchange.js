// @koala-append "_http.api.js"
// @koala-append "_fire.data.js"
function _dataExchange() {
	//this -> stockChart
	var _this = this,
		stockChart = _this,
		r = request(),
		api = requestAPI(_this),
		fire = fireData(_this),
		dataTpl = {},
		count = 0;
	_this.getNewStock = function() {
		api.release();
		//行情数据
		stockChart.options.data.DATA = [];
		stockChart.options.data.DATAT = {
			data: [],
			formatData: []
		};
		_this.options.user.timeShare ?
			api.timeShare(_this.options.user.stockID, _this.options.user.ex) :
			api.period(_this.options.user.stockID, _this.options.user.period, _this.options.user.ex);
		reInit(1);
		window['ctrls'] && ctrls['setOptional'] && ctrls.setOptional();
	}

	function reInit(change) {
		count = 0;
		dataTpl = [];
		change && _this.loading.open();
		for(var i = 0,l = api.data().length; i < l; i++){
			requestIt(i,l);
		}
	}
	function requestIt(n,total){
		var key = api.data()[n],
			path = key.act;
		r.ajax({
			url: location.protocol + "//" + stockChart.options.url.xhr + path,
			type : 'GET',
			data: key,
			success: function(data) {
				var l, isRight;
				count += 1;
				data.key = key.act;
				dataTpl.push(data);
				if(count == total){
					dataHandle();
					_this.draw();
					_this.loading.close()
				}
			}
		});
	}
	function autoget() {
		api.release();
		//行情数据
		_this.options.user.timeShare ?
			api.timeShare(_this.options.user.stockID, _this.options.user.ex) :
			api.period(_this.options.user.stockID, _this.options.user.period, _this.options.user.ex);
		reInit(1);
		setTimeout(autoget, 60000);
	}

	function dataHandle() {
		var l = dataTpl.length;
		for (; l--;) {
			if (dataTpl[l].success)
				fire[dataTpl[l].key] && fire[dataTpl[l].key](dataTpl[l].data);
		}
	}! function() {
		'construct';

		var date = totime(new Date() * 1, 'ymd'),
			localDate = localStorage.getItem('ver');
		autoget();
	}.apply(window, arguments);
}
