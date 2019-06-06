// @koala-append "http.comm.js" 
// @koala-append "http.api.js" 
// @koala-append "http.render.js" 
function http(url){
	var _this = {};
	J(_comm,_this,url);
	J(_api,_this,url);
	J(_render,_this,url);
	_this.request = function(opt){
		_this.jsonp(opt)
	}
	_this.post = function(opt){
		_this.ajax(opt)
	}
	_this.loop = function(opt,timer){
		var timer = timer || 5000,
			__arg = arguments;
		_this.jsonp(opt)
		setTimeout(function() {
			_this.loop.apply(_this,__arg)
		}, timer);
	}
	return _this;
}