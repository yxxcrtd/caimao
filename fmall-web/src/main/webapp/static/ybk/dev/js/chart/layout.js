function singleLayout(obj){
	var timer;
	obj['setdomlist'] && obj['setdomlist']({});
	Event.add(window, 'resize', function() {
			timer && clearTimeout(timer);
			timer = setTimeout(_resize, 10);
		});
	obj.size = {
		w : obj.box.offsetWidth,
		h : obj.box.offsetHeight
	}
	function _resize(){
		obj['draw'] && obj.draw({
			w : obj.box.offsetWidth,
			h : obj.box.offsetHeight
		})
	}
	_resize();
	obj.stockChart.winReset = _resize;
}