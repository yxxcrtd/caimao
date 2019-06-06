// @koala-append "_splitWindows.js"
function _canvasLayout(stockChart) {
	//this -> box
	var _this = this,mode;
	this.canvasLayout = function(size, canvas) {
		var volHeight = ~~ (stockChart.options.user.viceHeight * size.h),
			indHeight = ~~ (stockChart.options.user.indicatorHeight * size.h),
			mainHeight = size.h - volHeight - (stockChart.options.layout[2] ? indHeight : 0) - stockChart.options.user.timeline,
			timeVolHeight = ~~ ((1 - stockChart.options.user.timeHeight) * size.h),
			timeShareHeight = size.h - timeVolHeight - stockChart.options.user.timeline,
			outRule = size.w > stockChart.options.user.outRuleWidth;
		if(_this['size'] && _this.size == size && mode == stockChart.options.user.ctrlVer)return;
		_this.size = size;

		mode = stockChart.options.user.ctrlVer;
		canvas.volCanvas[0].style.cssText = "top:" + mainHeight + "px;";
		canvas.timeVolCanvas[0].style.cssText = "top:" + (timeShareHeight) + "px;";
		canvas.mainCanvas[0].width = size.w;
		canvas.volCanvas[0].width = size.w;
		canvas.toolsCanvas[0].width = size.w;
		canvas.timeCanvas[0].width = size.w;
		canvas.timeVolCanvas[0].width = size.w;

		if(stockChart.options.layout[2]){
			canvas.indCanvas[0].style.cssText = "top:" + (mainHeight + volHeight) + "px;";
			canvas.indCanvas[0].width = size.w;
			canvas.indCanvas[0].height = indHeight;
			canvas.indCanvas[1].w = size.w;
			canvas.indCanvas[2].style.width = size.w - (outRule ? stockChart.options.user.ruleWidth : 0)+'px';
			canvas.indCanvas[1].h = indHeight;
			canvas.indCanvas[1].types = 0;
			canvas.indCanvas[2].style.display = '';
		}else{
			canvas.indCanvas[2].style.display = 'none';
		}

		canvas.mainCanvas[0].height = mainHeight;
		canvas.volCanvas[0].height = volHeight;
		canvas.toolsCanvas[0].height = size.h;
		canvas.timeCanvas[0].height = timeShareHeight;
		canvas.timeVolCanvas[0].height = timeVolHeight;

		canvas.mainCanvas[1].w = size.w;
		canvas.volCanvas[1].w = size.w;
		canvas.toolsCanvas[1].w = size.w;
		canvas.timeCanvas[1].w = size.w;
		canvas.timeVolCanvas[1].w = size.w;

		canvas.mainCanvas[2].style.width = size.w - (outRule ? stockChart.options.user.ruleWidth : 0)+'px';
		canvas.volCanvas[2].style.width = size.w - (outRule ? stockChart.options.user.ruleWidth : 0)+'px';
		canvas.timeCanvas[2].style.width = size.w - (outRule ? stockChart.options.user.ruleWidth : 0)+'px';
		canvas.timeVolCanvas[2].style.width = size.w - (outRule ? stockChart.options.user.ruleWidth : 0)+'px';
		canvas.timeCanvas[2].style.left = (outRule ? stockChart.options.user.ruleWidth : 0) +'px';
		canvas.timeVolCanvas[2].style.left = (outRule ? stockChart.options.user.ruleWidth : 0) +'px';

		canvas.mainCanvas[1].h = mainHeight;
		canvas.volCanvas[1].h = volHeight;
		canvas.toolsCanvas[1].h = size.h;
		canvas.timeCanvas[1].h = timeShareHeight;
		canvas.timeVolCanvas[1].h = timeVolHeight;

		canvas.mainCanvas[1].types = 1;
		canvas.volCanvas[1].types = 0;
		canvas.toolsCanvas[1].types = 1;
		canvas.timeCanvas[1].types = 1;
		canvas.timeVolCanvas[1].types = 0;

		stockChart.options.user.w = size.w;
		_this.size = size;
		_this.splitWindows = function() {
			splitWindows(stockChart, size, canvas, mainHeight, volHeight, indHeight, timeShareHeight, timeVolHeight);
		}
	}
}