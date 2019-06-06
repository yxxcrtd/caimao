function _loading(obj) {
	//this -> stockChart
	var _this = this,
		stockChart = _this;
	_this.loading = function() {
		function open() {
			obj.style.display = "block"
			//console.error('loading open')
		}

		function close() {
			obj.style.display = "none"
			//console.error('loading close')
		}
		return {
			open: open,
			close: close
		}
	}()
}