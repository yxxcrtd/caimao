function _comm(url){
	var _this = this,
		req = request();
	_this.url = url || "http://172.32.1.218:8097";
	_this.jsonp = function(option){
		req.jsonp({
			url : _this.url + option.api,
			data : option.data,
			success : option.fn
		})
	}
	_this.ajax = function(option){
		req.ajax({
			url : _this.url + option.api,
			type : "POST",
			data : option.data,
			success : option.fn
		})
	}
}