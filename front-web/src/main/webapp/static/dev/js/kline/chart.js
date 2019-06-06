var CFG = {
		"level": [
			/***
		[影线距(烛宽+烛距),烛宽barWidth]
	***/
			[84, 75],
			[78, 69],
			[72, 63],
			[66, 57],
			[58, 51],
			[54, 47],
			[50, 43],
			[46, 39],
			[40, 35],
			[36, 31],
			[32, 27],
			[30, 25],
			[28, 23],
			[26, 21],
			[22, 19],
			[20, 17],
			[18, 15],
			[16, 13],
			[14, 11],
			[12, 9],
			[10, 7],
			[8, 5],
			[6, 3],
			[3, 1],
			[1, 1]
		],
		"url": {
			//"xhr": "172.32.1.151:9811/a/bin?act=1001&s=1"
			"xhr": "www.caimao.com/stockapi/a/bin?act=1001&s=1"
		},
		"fingerprint": "caimao.com",
		"schemes": {
			"chinese": //配色名称 
			{
				"rise": "#ff0000", //阳线颜色
				"riseStyle": "0", //阳线样式
				"fall": "#54fcfc", //阴线颜色
				"fallStyle": "1", //阴线样式
				"riseFont" : "#ff0000",//上涨文字颜色
				"fallFont" : "#00ff00",//下跌文字颜色
				"timeVolRise" : "#ffff0b",
				"timeVolFall" : "#02e2f4",
				"timeVolStable" : "#ffffff",
				"timeShareShadow" : 0,
				"specialLine" : 1,
				"shadowFill" : "rgba(200, 23, 23, 0.15)",
				"MACDrise" : "#ff3232",
				"MACDfall" : "#00ff00" ,
				"crossLine": "#ccc", //工具线颜色
				"layoutColor": "#800000", //分割线颜色
				"layoutFont": "#ff2b1c", //布局文字颜色
				"fontColor": "#c4c4c4", //文字颜色
				"iLines": ["#ffffff", "#ffff0b", "#ff80b6", "#00e600", "#02e2f4", "#ffffb9", "#2c684d", "#ff3232"], //指标线依次颜色
				"background": "#000000" //背景颜色
			},
			"mobile": //配色名称 
			{
				"rise": "#eb4636", //阳线颜色
				"riseStyle": "1", //阳线样式
				"fall": "#1da41d", //阴线颜色
				"fallStyle": "1", //阴线样式
				"riseFont" : "#eb4636",//上涨文字颜色
				"fallFont" : "#1da41d",//下跌文字颜色
				"timeVolRise" : "#eb4636",
				"timeVolFall" : "#1da41d",
				"timeVolStable" : "#ffffff",
				"timeShareShadow" : 1,
				"specialLine" : 0,
				"shadowFill" : "rgba(71, 169, 238, 0.15)",
				"MACDrise" : "#eb4636",
				"MACDfall" : "#1da41d" ,
				"crossLine": "#ccc", //工具线颜色
				"layoutColor": "#969696", //分割线颜色
				"layoutFont": "#969696", //布局文字颜色
				"fontColor": "#c4c4c4", //文字颜色
				"iLines": ["#5588cc", "#fcc83b", "#ff80b6", "#00e600", "#02e2f4", "#ffffb9", "#2c684d", "#ff3232"], //指标线依次颜色
				"background": "#000000" //背景颜色
			}
		},
		"binary" : {
			1000 : ['K','M','B'],
			10000 : ['万','亿','兆']
		},
		"layout" : [1,1,1],
		"periodMap" : {
			"1week": 42,
			"1month": 43,
			"1day": 33,
			"1min": 34,
			"5min": 35,
			"15min": 36,
			"30min": 37,
			"1hour": 38
		},
		"user": {
			"showInfo": 1,
			"double" : 1,
			"dataVersion": '0.1',
			"loadingPic": location.protocol + '//www.caimao.com/q/image/chineseloading.gif',
			"popstop" : 0,
			"ctrlVer": new Date()*1,
			"paddingRight" : 15,
			"outRuleWidth" : 414,
			"binary" : 10000,
			"symbol": 0, //品类
			"schemes": "chinese", //主题
			/***绘图区上偏移***/
			"offsetTop": 20,
			/***标尺最小高度***/
			"ruleWidth": 60,
			/***副图高度***/
			"viceHeight": 17 / 100,
			/***指标高度***/
			"indicatorHeight": 22 / 100,
			/***分时图高度***/
			"timeHeight": 2 / 3,
			/***蜡烛尺寸***/
			"level": 3,
			/***分时周期***/
			"period": '1day', //
			/***主要指标['KMA','BOLL']***/
			"coverIndicator": 'KMA',
			/***副指标['MACD','KDJ','RSI','WR','BOLL']***/
			"vIndicator": 'MACD',
			"drawRange": 80 / 100, //绘图区间
			"minRuleHeight": 75, //标尺间距
			"vMinRuleHeight": 40, //副图标尺间距
			"timeLineMinRuleHeight": 30, //副图标尺间距
			"timeline": 20, //时间线高度
			"timeSpace": 140, //时间标尺间距
			"dashed": 1, //虚线背景
			"timeShare": 0, //分时K线开关
			"stopDrag": 0, //拖拽开关
			"stopZoom": 0, //缩放开关
			"showHigh": 1, //显示最高价开关
			"showLow": 1, //显示最低价开关
			"doubleClick": 300, //双击间隔时间
			"moveThreshold": 999999 //允许拖拽操作的单屏数据阈值
		},
		data : {
			DATA : [],
			DATAT : {formatData : [],data : []},
			DATA2 : {},
			MARKET : {},
			TRADELIST : [],
			MAP : '',
			IND : {}
		}

	}
	var house = ['指标','沪','深'];

	var CFG2 = {
		"level": [
			/***
		[影线距(烛宽+烛距),烛宽barWidth]
	***/
			[84, 75],
			[78, 69],
			[72, 63],
			[66, 57],
			[58, 51],
			[54, 47],
			[50, 43],
			[46, 39],
			[40, 35],
			[36, 31],
			[32, 27],
			[30, 25],
			[28, 23],
			[26, 21],
			[22, 19],
			[20, 17],
			[18, 15],
			[16, 13],
			[14, 11],
			[12, 9],
			[10, 7],
			[8, 5],
			[6, 3],
			[3, 1],
			[1, 1]
		],
		"url": {
			//"xhr": "172.32.1.151:9811/a/bin?act=1001&s=1"
			"xhr": "www.caimao.com/stockapi/a/bin?act=1001&s=1"
		},
		"fingerprint": "caimao.com",
		"schemes": {
			"chinese": //配色名称 
			{
				"rise": "#ff0000", //阳线颜色
				"riseStyle": "0", //阳线样式
				"fall": "#54fcfc", //阴线颜色
				"fallStyle": "1", //阴线样式
				"riseFont" : "#ff0000",//上涨文字颜色
				"fallFont" : "#00ff00",//下跌文字颜色
				"timeVolRise" : "#ffff0b",
				"timeVolFall" : "#02e2f4",
				"timeVolStable" : "#ffffff",
				"timeShareShadow" : 1,
				"specialLine" : 1,
				"shadowFill" : "rgba(200, 23, 23, 0.15)",
				"MACDrise" : "#ff3232",
				"MACDfall" : "#00ff00" ,
				"crossLine": "#ccc", //工具线颜色
				"layoutColor": "#800000", //分割线颜色
				"layoutFont": "#ff2b1c", //布局文字颜色
				"fontColor": "#c4c4c4", //文字颜色
				"iLines": ["#ffffff", "#ffff0b", "#ff80b6", "#00e600", "#02e2f4", "#ffffb9", "#2c684d", "#ff3232"], //指标线依次颜色
				"background": "#000000" //背景颜色
			},
			"mobile": //配色名称 
			{
				"rise": "#eb4636", //阳线颜色
				"riseStyle": "1", //阳线样式
				"fall": "#1da41d", //阴线颜色
				"fallStyle": "1", //阴线样式
				"riseFont" : "#eb4636",//上涨文字颜色
				"fallFont" : "#1da41d",//下跌文字颜色
				"timeVolRise" : "#eb4636",
				"timeVolFall" : "#1da41d",
				"timeVolStable" : "#ffffff",
				"timeShareShadow" : 1,
				"specialLine" : 0,
				"shadowFill" : "rgba(71, 169, 238, 0.15)",
				"MACDrise" : "#eb4636",
				"MACDfall" : "#1da41d" ,
				"crossLine": "#ccc", //工具线颜色
				"layoutColor": "#969696", //分割线颜色
				"layoutFont": "#969696", //布局文字颜色
				"fontColor": "#c4c4c4", //文字颜色
				"iLines": ["#5588cc", "#fcc83b", "#ff80b6", "#00e600", "#02e2f4", "#ffffb9", "#2c684d", "#ff3232"], //指标线依次颜色
				"background": "#000000" //背景颜色
			}
		},
		"binary" : {
			1000 : ['K','M','B'],
			10000 : ['万','亿','兆']
		},
		"layout" : [1,1,1],
		"periodMap" : {
			"1week": 42,
			"1month": 43,
			"1day": 33,
			"1min": 34,
			"5min": 35,
			"15min": 36,
			"30min": 37,
			"1hour": 38
		},
		"user": {
			"showInfo": 1,
			"double" : 1,
			"dataVersion": '0.1',
			"loadingPic": location.protocol + '//www.caimao.com/q/image/chineseloading.gif',
			"popstop" : 0,
			"ctrlVer": new Date()*1,
			"paddingRight" : 15,
			"outRuleWidth" : 414,
			"binary" : 10000,
			"symbol": 0, //品类
			"schemes": "chinese", //主题
			/***绘图区上偏移***/
			"offsetTop": 20,
			/***标尺最小高度***/
			"ruleWidth": 60,
			/***副图高度***/
			"viceHeight": 17 / 100,
			/***指标高度***/
			"indicatorHeight": 22 / 100,
			/***分时图高度***/
			"timeHeight": 2 / 3,
			/***蜡烛尺寸***/
			"level": 3,
			/***分时周期***/
			"period": '1day', //
			/***主要指标['MA','BOLL']***/
			"coverIndicator": 'KMA',
			/***副指标['MACD','KDJ','RSI','WR','BOLL']***/
			"vIndicator": 'MACD',
			"drawRange": 80 / 100, //绘图区间
			"minRuleHeight": 75, //标尺间距
			"vMinRuleHeight": 40, //副图标尺间距
			"timeLineMinRuleHeight": 30, //副图标尺间距
			"timeline": 20, //时间线高度
			"timeSpace": 140, //时间标尺间距
			"dashed": 1, //虚线背景
			"timeShare": 0, //分时K线开关
			"stopDrag": 0, //拖拽开关
			"stopZoom": 0, //缩放开关
			"showHigh": 1, //显示最高价开关
			"showLow": 1, //显示最低价开关
			"doubleClick": 300, //双击间隔时间
			"moveThreshold": 999999 //允许拖拽操作的单屏数据阈值
		},
		data : {
			DATA : [],
			DATAT : {formatData : [],data : []},
			DATA2 : {},
			MARKET : {},
			TRADELIST : [],
			MAP : '',
			IND : {}
		}

	}

// @koala-prepend "_config.js"
// @koala-append "_kline.js"
// @koala-append "_timeShare.js"
// @koala-append "_dash.js"
// @koala-append "_vol.js"
// @koala-append "_indicator.js"
// @koala-append "_dataExchange.js"
// @koala-append "_canvasLayout.js"
// @koala-append "_loading.js"
// @koala-append "_bgLine.js"
// @koala-append "_domctrl.js"
// @koala-append "_keymap.js"
// @koala-append "_event.js"
// @koala-append "_tools.js"
// @koala-append "_fillData.js"
function chart() {
	var stockChart = {
			options: {}
		}, //stock chart
		box, mainCanvas, volCanvas, indCanvas, toolsCanvas, timeCanvas, timeVolCanvas, canvas, objLoading;

	function draw(size) {
		var showCount = 0,
			total = 0,
			s = size || box.size,
			outRule = s.w > stockChart.options.user.outRuleWidth;
		stockChart.loading.close();
		box.canvasLayout(s, canvas);
		if(stockChart.options.user.pause)return;

		!stockChart["kBox"] && s.w > 480 && (stockChart.kBox = createTextP('-299px', '20px', 'stockMoreInfo'));
		if (!stockChart.options.data.DATA.length && !stockChart.options.data.DATAT.data.length) return;

		stockChart.options.curLevel = stockChart.options.level[stockChart.options.user.level];


		showCount = ~~ ((stockChart.options.user.w - (stockChart.options.user.timeShare ?
			(outRule ? stockChart.options.user.ruleWidth : 0) * 2 :
			(outRule ? stockChart.options.user.ruleWidth : 0)) - stockChart.options.user.paddingRight) / stockChart.options.curLevel[0]);
		total = stockChart.options.user.timeShare ? stockChart.options.data.DATAT.data.length : stockChart.options.data.DATA.length;
		stockChart.options.lens = showCount > total ? total : showCount;
		stockChart.options.dataOffset = showCount < total ? total - showCount : 0;

		stockChart.formatTimeData(box.size.w - (outRule ? stockChart.options.user.ruleWidth : 0) * 2 - 2, (outRule ? stockChart.options.user.ruleWidth : 0));
		//stockChart.options.data.IND.KMA = stockChart.options.data.IND.KMA.splice(200)

		if (stockChart.options.user.timeShare) {
			volCanvas[1].clearVol();
			mainCanvas[1].clearK();
			indCanvas[1].clearInd();

			timeCanvas[1].clearTimeShare();
			timeCanvas[1].drawTimeShare(timeCanvas[2], 'timeShare', box);
			timeCanvas[2].style.top = '0';

			timeVolCanvas[1].clearVol();
			timeVolCanvas[1].drawTimeVol(box);
			timeVolCanvas[2].style.top = timeCanvas[1].h + 'px';

			mainCanvas[2].style.top = '-99px';
			volCanvas[2].style.top = '-99px';
			indCanvas[2].style.top = '-99px';
			stockChart["kBox"] && (stockChart.kBox.style.left = '-299px');
		} else {


			timeCanvas[1].clearTimeShare();
			timeVolCanvas[1].clearVol();

			mainCanvas[1].clearK();
			mainCanvas[1].drawKLine(mainCanvas[2], 'k');
			mainCanvas[2].style.top = '0';


			volCanvas[1].clearVol();
			volCanvas[1].drawVol(volCanvas[2], 'vol');
			volCanvas[2].style.top = mainCanvas[1].h + 'px';

			indCanvas[1].clearInd();
			indCanvas[1].drawInd(indCanvas[2]);
			indCanvas[2].style.top = (mainCanvas[1].h * 1 + volCanvas[1].h * 1) + 'px';
			timeCanvas[2].style.top = '-99px';
			timeVolCanvas[2].style.top = '-99px';

		}
		box.splitWindows();
		!stockChart.crossStart && stockChart.fillCurData(null);
	}

	function kt(ts, td) {
		if (ts === 'timeShare' || td === "timeShare") {
			if (stockChart.options.user.timeShare) return;
			stockChart.options.user.timeShare = 1;
			stockChart.options.user.ctrlVer = new Date() * 1;
			stockChart.options.user.period = "1day";
			stockChart.options.layout = [1,1,1];
			window["ISCHART"] && (HASINDKEY = 0);
			stockChart.getNewStock();
			return;
		} else if (ts === 'k' || td === "k") {
			if (!stockChart.options.user.timeShare) return;
			stockChart.options.user.timeShare = 0;
			stockChart.options.user.ctrlVer = new Date() * 1;
			window["ISCHART"] && (HASINDKEY = 1);
			stockChart.getNewStock();
			return;
		} else if (ts === 'list' || td === "list") {
			window.location.href = "index.html";
			return;
		}
		stockChart.options.user.timeShare = !stockChart.options.user.timeShare;
		if(stockChart.options.user.timeShare){
			stockChart.options.user.period = "1day";
			stockChart.options.layout = [1,1,1];
		}
		stockChart.options.user.ctrlVer = new Date() * 1;
		stockChart.getNewStock();
		window["ISCHART"] && (HASINDKEY = !stockChart.options.user.timeShare);
	}

	function setInd(ind,force) {
		if (stockChart.options.user.vIndicator.toLowerCase() == ind.toLowerCase() && !force) return;
		stockChart.options.user.vIndicator = ind.toUpperCase();
		stockChart.options.user.ctrlVer = new Date() * 1;
		if (stockChart.options.user.timeShare) {
			stockChart.options.user.timeShare = 0;
		}
		if(stockChart.options.user.coverIndicator == stockChart.options.user.vIndicator)
			stockChart.options.user.coverIndicator = 'kma';
		stockChart.getNewStock();
	}
	function setMainInd(ind,force) {
		if (stockChart.options.user.coverIndicator.toLowerCase() == ind.toLowerCase() && !force) return;
		stockChart.options.user.coverIndicator = ind.toUpperCase();
		stockChart.options.user.ctrlVer = new Date() * 1;
		if (stockChart.options.user.timeShare) {
			stockChart.options.user.timeShare = 0;
		}
		console.log(stockChart.options.user.coverIndicator,stockChart.options.user.vIndicator)
		if(stockChart.options.user.coverIndicator == stockChart.options.user.vIndicator)
			stockChart.options.user.vIndicator = 'macd';
		stockChart.getNewStock();
	}

	function setLevel(n) {
		var level = stockChart.options.user.level,
			levelCount = stockChart.options.level.length;
		level += n * 1;
		if (level < 0) level = 0;
		if (level >= levelCount) level = levelCount - 1;
		stockChart.options.user.level = level;
		draw();
	}

	function setdomlist(d) {
		stockChart.domlist = d;
	}

	function createCanvas() {
		var obj = document.createElement('canvas');
		var ctx = obj.getContext('2d');
		J(_dash, ctx, stockChart);
		ctx.lineCap = "round";
		obj.style.cssText += ';position:absolute;left:0;top:0;';
		box.appendChild(obj);
		J(_kline, ctx, stockChart);
		J(_timeShare, ctx, stockChart);
		J(_vol, ctx, stockChart);
		J(_indicator, ctx, stockChart);
		J(_bgLine, ctx, stockChart);
		J(_tools, ctx, stockChart, box);
		return [obj, ctx];
	}

	function createTextP(x, y, className) {
		var obj = document.createElement('p');
		obj.className = "stockInfo " + (className ? className : '');
		obj.style.cssText = ';position:absolute;left:' + (x || "5px") + ';top:' + (y || "-99px") + ';line-height:20px;';
		box.appendChild(obj);
		return obj;
	}! function(id, opt) {
		'construct';
		box = id && typeof id == 'string' ? document.getElementById(id) : id;
		if (!box) return;
		copy(stockChart.options, opt || CFG)
		mainCanvas = createCanvas();
		volCanvas = createCanvas();
		indCanvas = createCanvas();
		timeCanvas = createCanvas();
		timeVolCanvas = createCanvas();
		toolsCanvas = createCanvas();
		canvas = {
			mainCanvas: mainCanvas,
			volCanvas: volCanvas,
			indCanvas: indCanvas,
			toolsCanvas: toolsCanvas,
			timeCanvas: timeCanvas,
			timeVolCanvas: timeVolCanvas
		};
		canvas.mainCanvas[2] = createTextP();
		canvas.volCanvas[2] = createTextP();
		canvas.indCanvas[2] = createTextP();
		canvas.timeCanvas[2] = createTextP();
		canvas.timeVolCanvas[2] = createTextP();
		stockChart.options.level.reverse();
		J(_canvasLayout, box, stockChart);
		J(_domctrl, stockChart);
		J(_keymap, stockChart);

		objLoading = document.createElement('div');
		objLoading.innerHTML = "<img src='"+stockChart.options.user.loadingPic+"'>";
		objLoading.className = 'loading';
		J(_loading, stockChart, box.appendChild(objLoading));
		
		J(_event, stockChart, box, canvas);
		J(_dataExchange, stockChart);
		J(_fillData, stockChart, box);
		stockChart.tradeStatus = 1;
		stockChart.fillData = {};
		stockChart.fillBoxs = {};
		window["ISCHART"] && (HASINDKEY = !stockChart.options.user.timeShare);
	}.apply(window, arguments);
	stockChart.draw = draw;
	stockChart.kt = kt;
	stockChart.setInd = setInd;
	stockChart.setMainInd = setMainInd;
	stockChart.setLevel = setLevel;

	function toStock(id, ex) {
		stockChart.changeStock({
			code: id,
			ex: ex
		})
	}
	function pause(){
		stockChart.options.user.pause = 1;
	}
	function play(){
		stockChart.options.user.pause = 0;
	}
	return {
		draw: draw,
		toStock: toStock,
		box: box,
		stockChart: stockChart,
		setLevel: setLevel,
		setdomlist: setdomlist,
		kt: kt,
		setInd: setInd,
		play : play,
		pause : pause
	};
}

// @koala-append "_coverIndicator.js"
function _kline(stockChart) {
	//this -> ctx
	var _this = this,
		mark = {
			low: null,
			lowCount: 1,
			highCount: 1,
			high: null
		},
		max = Number.MIN_VALUE,
		min = Number.MAX_VALUE,
		rule, verticalOffset;


	J(_coverIndicator, _this, stockChart);

	function getRule() {
		var dp = max - min;
		return !dp ? 0 : (_this.h - stockChart.options.user.offsetTop) * stockChart.options.user.drawRange / dp;
	}

	function getY(v) {
		return ((max - v) * rule).toFixed() * 1 + verticalOffset * 1 + 0.5;
	}

	function drawRectStroke(x, y, w, h, isRise) {
		var style = isRise ?
			stockChart.options.schemes[stockChart.options.user.schemes].riseStyle :
			stockChart.options.schemes[stockChart.options.user.schemes].fallStyle;
		style * 1 && _this.fillRect(x, y, w, h);
		if (h) {
			_this.rect(x, y, w, h);
		} else {
			_this.moveTo(x, y);
			_this.lineTo(x + w, y);
		}
	}

	function candle(data, i) {
		data.index = i;
		var isRise = data.cur * 1 >= data.open * 1,
			color = isRise ?
			stockChart.options.schemes[stockChart.options.user.schemes].rise :
			stockChart.options.schemes[stockChart.options.user.schemes].fall,
			level = stockChart.options.level[stockChart.options.user.level],
			oddNum = level[0] / 2 != (level[0] / 2).toFixed(),
			candleX, currentX, candleY, candleHeight, topY, bottomY;
		if (!rule) {
			rule = getRule();
		}
		//candleRect
		_this.strokeStyle = color;
		_this.fillStyle = color;
		_this.beginPath();
		currentX = level[0] * i + level[0] / 2 + (oddNum ? 0 : .5);

		if (currentX > _this.w - level[0]) return null;
		topY = getY(data.high);
		bottomY = getY(data.low);
		if (level[1] > 2) {
			candleX = currentX - level[1] / 2 + .5;
			if (isRise) {
				candleY = getY(data.cur);
				candleHeight = getY(data.open) - candleY;
			} else {
				candleY = getY(data.open);
				candleHeight = getY(data.cur) - candleY;
			}
			drawRectStroke(candleX, candleY, level[1], candleHeight, isRise);

			_this.moveTo(currentX, topY);
			_this.lineTo(currentX, candleY)
			_this.moveTo(currentX, candleY + candleHeight);
			_this.lineTo(currentX, bottomY)
		} else { //candleLine
			_this.moveTo(currentX, topY);
			_this.lineTo(currentX, bottomY == topY ? topY + .5 : bottomY);
		}

		_this.stroke();
		if (max == data.high || min == data.low) {
			_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].fontColor;
			_this.fillStyle = stockChart.options.schemes[stockChart.options.user.schemes].fontColor;
			_this.beginPath();
			_this.font = '12px arial';
		}
		if (stockChart.options.user.showHigh && max == data.high && !mark.high) {
			/* 标示最高价 */
			_this.moveTo(currentX + 18, topY - 13 * mark.highCount);
			_this.lineTo(currentX, topY - 13 * mark.highCount);
			_this.lineTo(currentX, topY);
			_this.lineTo(currentX + 3, topY - 5);
			_this.moveTo(currentX, topY);
			_this.lineTo(currentX - 3, topY - 5);
			_this.fillText(cutFixed(data.high, 2), currentX + 20, topY - 13 * mark.highCount);
			_this.stroke();
			mark.high = 1;
		}
		if (stockChart.options.user.showLow && min == data.low && !mark.low) {
			/* 标示最低价 */
			_this.moveTo(currentX + 10, bottomY + 13 * mark.lowCount);
			_this.lineTo(currentX, bottomY + 13 * mark.lowCount);
			_this.lineTo(currentX, bottomY);
			_this.lineTo(currentX + 3, bottomY + 5);
			_this.moveTo(currentX, bottomY);
			_this.lineTo(currentX - 3, bottomY + 5);
			_this.fillText(cutFixed(data.low, 2), currentX + 10, bottomY + 13 * mark.lowCount);
			_this.stroke();
			mark.low = 1;
		}

	}
	
	_this.drawKLine = function(fillBox,key) {
		var total = stockChart.options.data.DATA.length,
			tmplens = stockChart.options.lens - 1,
			data = [].concat(stockChart.options.data.DATA).reverse().slice(0, stockChart.options.lens),
			ind;
		for (var i = 0; i < stockChart.options.lens; i++) {
			max = Math.max(data[i].high, max);
			min = Math.min(data[i].low, min);
		}
		rule = getRule();
		_this.drawLine(max, min, rule, 2);
		verticalOffset = ~~ ((_this.h - stockChart.options.user.offsetTop) * (.5 - stockChart.options.user.drawRange / 2) + stockChart.options.user.offsetTop);
		for (var i = 0; i < stockChart.options.lens; i++) {
			candle(data[i], tmplens - i)
		}
		ind = stockChart.options.data.IND[stockChart.options.user.coverIndicator.toUpperCase()] && _this.drawCoverInd(getY);
		stockChart.fillData[key] = {data : data, ind : ind, name : stockChart.options.data.DATA2.name || ''};
		stockChart.fillBoxs[key] = fillBox;
	}

	_this.clearK = function(size) {
		_this.clearRect(0, 0, _this.w, _this.h);
		rule = false;
		max = Number.MIN_VALUE;
		min = Number.MAX_VALUE;
		verticalOffset = null;
		mark = {
			low: null,
			lowCount: 1,
			highCount: 1,
			high: null
		};
	}
}

function _coverIndicator(stockChart){
	var _this = this,
		indFn = {};

	indFn.KMA = function(fn){
		var total = stockChart.options.data.IND.KMA.length,
			tmplens = stockChart.options.lens - 1,
			data = [].concat(stockChart.options.data.IND.KMA).reverse().slice(0, stockChart.options.lens),
			level = stockChart.options.level[stockChart.options.user.level],
			oddNum = level[0] / 2 != (level[0] / 2).toFixed(),
			currentX,n = 0,
			len = Math.min(stockChart.options.lens,data.length);
		_this.lineWidth = 1.5;
		for(var k in data[0]){
			_this.beginPath();
			currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
			_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[n++];
			_this.moveTo(currentX,fn(data[0][k]));

			for (var i = 0; i < len; i++) {
				if(1*data[i][k]){
					currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
					_this.lineTo(currentX,fn(data[i][k]))
				}
			}
			_this.stroke();
		}
		return data;
	}

	indFn.BOLL = function(fn){
		var total = stockChart.options.data.IND.BOLL.length,
			tmplens = stockChart.options.lens - 1,
			data = [].concat(stockChart.options.data.IND.BOLL).reverse().slice(0, stockChart.options.lens),
			level = stockChart.options.level[stockChart.options.user.level],
			oddNum = level[0] / 2 != (level[0] / 2).toFixed(),
			currentX,n = 0,
			len = Math.min(stockChart.options.lens,data.length);
		_this.lineWidth = 1.5;

		for(var k in data[0]){
			_this.beginPath();
			currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
			_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[n++];
			_this.moveTo(currentX,fn(data[0][k]));

			for (var i = 0; i < len; i++) {
				if(1*data[i][k]){
					currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
					_this.lineTo(currentX,fn(data[i][k]))
				}
			}
			_this.stroke();
		}
		return data;
	}


	_this.drawCoverInd = function(fn){
		var ind = stockChart.options.user.coverIndicator;
		return indFn[ind.toUpperCase()] && indFn[ind.toUpperCase()](fn);
	}
}

function _timeShare(stockChart) {
	//this -> ctx
	var _this = this,
		max = Number.MIN_VALUE,
		min = Number.MAX_VALUE,
		rule, verticalOffset,
		first;

	function getRule() {
		var rh = stockChart.options.user.timeLineMinRuleHeight,
			count = ~~ ((_this.h - stockChart.options.user.offsetTop) / rh),
			middle = ~~ (((_this.h - stockChart.options.user.offsetTop) / 2) + stockChart.options.user.offsetTop),
			dp = max - min,
			unit, offsetTop;
		count = (count % 2 ? count - 1 : count);
		unit = (_this.h - stockChart.options.user.offsetTop) / count;
		offsetTop = middle - unit * (count / 2 - 1);
		verticalOffset = ~~offsetTop;
		return !dp ? 0 : unit * (count - 2) / dp;
	}


	function getY(v) {
		return ((max - v) * rule).toFixed() * 1 + verticalOffset * 1 + 0.5;
	}
	_this.drawTimeShare = function(fillBox,key,box) {

		var total = stockChart.options.data.DATAT.formatData.length,
			sourData = [].concat(stockChart.options.data.DATAT.data).reverse(),
			data = [].concat(stockChart.options.data.DATAT.formatData).reverse(),
			diff,last,
			len = data.length,
			outRule = box.size.w > stockChart.options.user.outRuleWidth;

		_this.clearRect(0, 0, _this.w, _this.h);
		for (var i = 0,l= sourData.length; i < l; i++) {
			max = Math.max(sourData[i].pri, max);
			max = Math.max(sourData[i].avg, max);
			min = Math.min(sourData[i].pri, min);
			min = Math.min(sourData[i].avg, min);
		}
		if(max - min < 0.2){
			max += 0.20;
			min -= 0.20;
		}


		diff = [max - stockChart.options.data.DATA2.pre, stockChart.options.data.DATA2.pre - min];
		if (diff[0] > diff[1]) {
			min = stockChart.options.data.DATA2.pre - diff[0];
		} else {
			max = stockChart.options.data.DATA2.pre * 1 + diff[1] * 1;
		}


		rule = getRule();
		_this.drawTimeLine(max, min, rule, 2, stockChart.options.data.DATA2.pre,outRule);
		

		_this.beginPath();
		_this.lineWidth = 1.5;
		_this.moveTo(data[0].x, getY(data[0].drawData.pri));
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[0];
		
		for (var i = 0; i < len; i++) {
			_this.lineTo(data[i].x, getY(data[i].drawData.pri));
		}
		_this.stroke();



		if(stockChart.options.schemes[stockChart.options.user.schemes].timeShareShadow){
		    _this.beginPath();
			for (var i = 0; i < len; i++) {
				_this.lineTo(data[i].x, getY(data[i].drawData.pri));
				last = data[i];
			}
			_this.lineTo(last.x,_this.h)
			_this.lineTo(data[0].x,_this.h)
	    	_this.closePath();
		    _this.fillStyle = stockChart.options.schemes[stockChart.options.user.schemes].shadowFill;  
		    _this.fill();
	    }



		_this.beginPath();
		_this.moveTo(data[0].x, getY(data[0].drawData.avg));
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[1];
		//console.log(stockChart.options.data.DATAT)
		for (var i = 0; i < len; i++) {
		//	if(!first)console.log(i,data[i].x,data[i].drawData.avg,getY(data[i].drawData.avg))
			_this.lineTo(data[i].x, getY(data[i].drawData.avg))
		}
		first= 1;
		_this.stroke();

		stockChart.fillData[key] = {data :data,name : stockChart.options.data.DATA2.name || ''};
		stockChart.fillBoxs[key] = fillBox;
	}
	_this.clearTimeShare = function() {
		max = Number.MIN_VALUE,
		min = Number.MAX_VALUE,
		rule = null,
		verticalOffset = null;
	}
}

function _dash(){
	var _this = this;
	if(!_this.lastMoveToLocation){
		_this.moveForDashed = function(x,y){
			_this.moveTo(x,y);
			_this.lastMoveToLocation.x = x;
			_this.lastMoveToLocation.y = y;
		}
	}
	_this.lastMoveToLocation = {}; 
	_this.dashedLineTo = function(x, y, dashLength){
		var dashLength = dashLength === undefined ? 3 : dashLength,
			startX     = _this.lastMoveToLocation.x,
			startY     = _this.lastMoveToLocation.y,
			deltaX     = x - startX,
			deltaY     = y - startY,
			numDashes  = Math.floor(Math.sqrt(deltaX * deltaX + deltaY * deltaY) / dashLength),
			tx,ty;
		for (var i = 0; i < numDashes; ++i) {
			tx = startX + (deltaX / numDashes) * i;
			ty = startY + (deltaY / numDashes) * i;
			if(i % 2 === 0){
				_this.moveForDashed(tx,ty)
			}else{
				_this.lineTo(tx, ty);
			}
		}
		_this.moveForDashed(x, y);
	}
}

function _vol(stockChart) {
	//this -> ctx
	var _this = this,
		max = Number.MIN_VALUE,
		min = Number.MAX_VALUE,
		rule, verticalOffset, preData;

	function getRule() {
		var dp = max - min;
		return !dp ? 0 : (_this.h - stockChart.options.user.offsetTop) / dp;
	}

	function getY(v) {
		return ((max - v) * rule).toFixed() * 1 + verticalOffset * 1 + 0.5;
	}

	function drawVolDo(data, i, leftOffset, line) /*(x,y,w,h,isRise)*/ {
		var volume = data['vol'],
			level = stockChart.options.level[stockChart.options.user.level],
			isRise = data.cur * 1 >= data.open * 1,
			color = isRise ?
			stockChart.options.schemes[stockChart.options.user.schemes].rise :
			stockChart.options.schemes[stockChart.options.user.schemes].fall,
			oddNum = level[0] / 2 != (level[0] / 2).toFixed(),
			y, candleX, currentX;
		_this.strokeStyle = color;
		_this.lineWidth = 1;
		_this.fillStyle = color;
		_this.beginPath();
		if (!rule) {
			rule = getRule();
		}
		y = getY(volume);
		currentX = level[0] * i + level[0] / 2 + (oddNum ? 0 : .5) + leftOffset;
		if (level[1] > 1 && !line) {
			candleX = currentX - level[1] / 2 + .5;
			drawRectStroke(candleX, y, level[1], _this.h, isRise);
		} else {
			_this.moveTo(currentX, y);
			_this.lineTo(currentX, _this.h);
			_this.stroke();
		}
		_this.stroke();
	}

	function drawRectStroke(x, y, w, h, isRise) {
		var style = isRise ?
			stockChart.options.schemes[stockChart.options.user.schemes].riseStyle :
			stockChart.options.schemes[stockChart.options.user.schemes].fallStyle;
		style * 1 && _this.fillRect(x, y, w, h);
		if (h) {
			_this.rect(x, y, w, h);
		} else {
			_this.moveTo(x, y);
			_this.lineTo(x + w, y);
		}
	}
	_this.drawVol = function(fillBox, key) {
		var total = stockChart.options.data.DATA.length,
			tmplens = stockChart.options.lens - 1,
			data = [].concat(stockChart.options.data.DATA).reverse().slice(0, stockChart.options.lens);
		for (var i = 0, l = data.length; i < l; i++) {
			max = Math.max(data[i].vol, max);
			//min = Math.min(data[i].vol, min);
		}
		min = 0;
		rule = getRule();
		verticalOffset = stockChart.options.user.offsetTop;
		_this.drawLine(max, min, rule, 0, 1, null, 1);
		for (var i = 0, l = data.length; i < l; i++) {
			drawVolDo(data[i], tmplens - i,0)
		}

		stockChart.fillData[key] = {
			data: data
		};
		stockChart.fillBoxs[key] = fillBox;
	}
	
	function drawTimeVolDo(data, i, leftOffset, line) /*(x,y,w,h,isRise)*/ {
		var volume = data.drawData['vol'],
			y, candleX, currentX;
		_this.lineWidth = 1;
		if (!i || data.drawData.pri * 1 == preData * 1) {
			_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].timeVolStable;
		} else if (data.drawData.pri * 1 < preData * 1) {
			_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].timeVolFall;
		} else {
			_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].timeVolRise;
		}
		preData = data.drawData.pri;
		
		_this.beginPath();

		if (!rule) {
			rule = getRule();
		}
		y = getY(volume);
		_this.moveTo(data.x*1 + .5, y);
		_this.lineTo(data.x*1 + .5, _this.h);
		_this.stroke();
	}	
	_this.drawTimeVol = function() {
		var total = stockChart.options.data.DATAT.formatData.length,
			sourData = [].concat(stockChart.options.data.DATAT.data).reverse(),
			data = [].concat(stockChart.options.data.DATAT.formatData).reverse();
		for (var i = 0, l = sourData.length; i < l; i++) {
			max = Math.max(sourData[i].vol, max);
			//min = Math.min(sourData[i].vol, min);
		}
		min = 0;
		preData = data[0].drawData.pri;
		rule = getRule();
		verticalOffset = stockChart.options.user.offsetTop;
		_this.drawLine(max, min, rule, 1, 1, null, 1);
		for (var i = 0, l = data.length; l--; i++) {
			drawTimeVolDo(data[l], i,  stockChart.options.user.ruleWidth, 1)
		}
	}

	_this.clearVol = function() {
		_this.clearRect(0, 0, _this.w, _this.h);
		max = Number.MIN_VALUE;
		min = Number.MAX_VALUE;
		rule = null;
		verticalOffset = null;
	}
}

function _indicator(stockChart){
	//this -> ctx
	var _this = this,
		rule = null,
		max = Number.MIN_VALUE,
		min = Number.MAX_VALUE,
		indFn = {};


	function getRule() {
		var dp = max - min;
		return !dp ? 0 : (_this.h - stockChart.options.user.offsetTop) * stockChart.options.user.drawRange / dp;
	}

	function getY(v) {
		return ((max - v) * rule).toFixed() * 1 + verticalOffset * 1 + 0.5;
	}

	indFn.MACD = function(fillBox){
		if(!stockChart.options.data.IND.MACD)return;
		var total = stockChart.options.data.IND.MACD.length,
			tmplens = stockChart.options.lens - 1,
			data = [].concat(stockChart.options.data.IND.MACD).reverse().slice(0, stockChart.options.lens),
			level = stockChart.options.level[stockChart.options.user.level],
			oddNum = level[0] / 2 != (level[0] / 2).toFixed(),
			currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5),
			isRise;
		for (var i = 0,l = data.length; i < l; i++) {
			max = Math.max(data[i].idx_DEA, max);
			max = Math.max(data[i].idx_DIF, max);
			max = Math.max(data[i].idx_MACD, max);
			min = Math.min(data[i].idx_DEA, min);
			min = Math.min(data[i].idx_DIF, min);
			min = Math.min(data[i].idx_MACD, min);
		}
		rule = getRule();
		verticalOffset = ~~ ((_this.h - stockChart.options.user.offsetTop) * (.5 - stockChart.options.user.drawRange / 2) + stockChart.options.user.offsetTop);
		_this.drawLine(max, min, rule, 2);
		_this.drawZeroLine(getY(0));



		for (var i = 0,l = data.length; i < l; i++) {
			_this.beginPath();
			_this.lineWidth = 1;
			isRise = data[i].idx_MACD > 0 ? 'MACDrise' : 'MACDfall';
			_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes][isRise];
			currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
			_this.moveTo(currentX,getY(data[i].idx_MACD));
			_this.lineTo(currentX,getY(0));
			_this.stroke();
		}

		_this.beginPath();
		_this.lineWidth = 1.5;
		currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
		_this.moveTo(currentX,getY(data[0].idx_DEA));
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[1];
		for (var i = 0,l = data.length; i < l; i++) {
			currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
			_this.lineTo(currentX,getY(data[i].idx_DEA));
		}
		_this.stroke();

		_this.beginPath();
		currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
		_this.moveTo(currentX,getY(data[0].idx_DIF));
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[0];
		for (var i = 0,l = data.length; i < l; i++) {
			currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
			_this.lineTo(currentX,getY(data[i].idx_DIF))
		}
		_this.stroke();
		clearfill();
		stockChart.fillData[stockChart.options.user.vIndicator.toUpperCase()] = {data : data};
		stockChart.fillBoxs[stockChart.options.user.vIndicator.toUpperCase()] = fillBox;
	}

	indFn.BOLL = function(fillBox){
		if(!stockChart.options.data.IND.BOLL)return;
		//return;
		var total = stockChart.options.data.IND.BOLL.length,
			tmplens = stockChart.options.lens - 1,
			data = [].concat(stockChart.options.data.IND.BOLL).reverse().slice(0, stockChart.options.lens),
			level = stockChart.options.level[stockChart.options.user.level],
			oddNum = level[0] / 2 != (level[0] / 2).toFixed(),
			currentX,n = 0,isRise;
		for (var i = 0,l = data.length; i < l; i++) {
			max = Math.max(data[i].u, max);
			max = Math.max(data[i].m, max);
			max = Math.max(data[i].l, max);
			min = Math.min(data[i].u, min);
			min = Math.min(data[i].m, min);
			min = Math.min(data[i].l, min);
		}
		rule = getRule();
		verticalOffset = ~~ ((_this.h - stockChart.options.user.offsetTop) * (.5 - stockChart.options.user.drawRange / 2) + stockChart.options.user.offsetTop);
		_this.drawLine(max, min, rule, 2);
		_this.lineWidth = 1.5;

		for(var k in data[0]){
			_this.beginPath();
			currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
			_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[n++];
			_this.moveTo(currentX,getY(data[0][k]));

			for (var i = 0,l = data.length; i < l; i++) {
				if(1*data[i][k]){
					currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
					_this.lineTo(currentX,getY(data[i][k]))
				}
			}
			_this.stroke();
		}
		OHLC();
		clearfill();
		stockChart.fillData[stockChart.options.user.vIndicator.toUpperCase()] = {data : data};
		stockChart.fillBoxs[stockChart.options.user.vIndicator.toUpperCase()] = fillBox;
	}


	indFn.KDJ = function(fillBox){
		if(!stockChart.options.data.IND.KDJ)return;
		//return;
		var total = stockChart.options.data.IND.KDJ.length,
			tmplens = stockChart.options.lens - 1,
			data = [].concat(stockChart.options.data.IND.KDJ).reverse().slice(0, stockChart.options.lens),
			level = stockChart.options.level[stockChart.options.user.level],
			oddNum = level[0] / 2 != (level[0] / 2).toFixed(),
			currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5),
			isRise;
		for (var i = 0,l = data.length; i < l; i++) {
			max = Math.max(data[i].k, max);
			max = Math.max(data[i].d, max);
			max = Math.max(data[i].j, max);
			min = Math.min(data[i].k, min);
			min = Math.min(data[i].d, min);
			min = Math.min(data[i].j, min);
		}
		max = 100;
		min = 0;
		rule = getRule();
		verticalOffset = ~~ ((_this.h - stockChart.options.user.offsetTop) * (.5 - stockChart.options.user.drawRange / 2) + stockChart.options.user.offsetTop);
		_this.drawLine(max, min, rule, 2);



		_this.beginPath();
		_this.lineWidth = 1.5;
		currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
		_this.moveTo(currentX,getY(data[0].k));
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[0];
		for (var i = 0,l = data.length; i < l; i++) {
			currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
			_this.lineTo(currentX,getY(data[i].k));
		}
		_this.stroke();



		_this.beginPath();
		_this.lineWidth = 1.5;
		currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
		_this.moveTo(currentX,getY(data[0].d));
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[1];
		for (var i = 0,l = data.length; i < l; i++) {
			currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
			_this.lineTo(currentX,getY(data[i].d));
		}
		_this.stroke();

		_this.beginPath();
		currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
		_this.moveTo(currentX,getY(data[0].j));
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[2];
		for (var i = 0,l = data.length; i < l; i++) {
			currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
			_this.lineTo(currentX,getY(data[i].j))
		}
		_this.stroke();
		clearfill();
		stockChart.fillData[stockChart.options.user.vIndicator.toUpperCase()] = {data : data};
		stockChart.fillBoxs[stockChart.options.user.vIndicator.toUpperCase()] = fillBox;
	}

	indFn.MTM = function(fillBox){
		if(!stockChart.options.data.IND.MTM)return;
		var total = stockChart.options.data.IND.MTM.length,
			tmplens = stockChart.options.lens - 1,
			data = [].concat(stockChart.options.data.IND.MTM).reverse().slice(0, stockChart.options.lens),
			level = stockChart.options.level[stockChart.options.user.level],
			oddNum = level[0] / 2 != (level[0] / 2).toFixed(),
			currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5),
			isRise;
		for (var i = 0,l = data.length; i < l; i++) {
			max = Math.max(data[i].mtm, max);
			max = Math.max(data[i].mtmma, max);
			min = Math.min(data[i].mtm, min);
			min = Math.min(data[i].mtmma, min);
		}
		rule = getRule();
		verticalOffset = ~~ ((_this.h - stockChart.options.user.offsetTop) * (.5 - stockChart.options.user.drawRange / 2) + stockChart.options.user.offsetTop);
		_this.drawLine(max, min, rule, 2);



		_this.beginPath();
		_this.lineWidth = 1.5;
		currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
		_this.moveTo(currentX,getY(data[0].mtm));
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[0];
		for (var i = 0,l = data.length; i < l; i++) {
			currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
			_this.lineTo(currentX,getY(data[i].mtm));
		}
		_this.stroke();



		_this.beginPath();
		_this.lineWidth = 1.5;
		currentX = level[0] * tmplens + level[0] / 2 + (oddNum ? 0 : .5);
		_this.moveTo(currentX,getY(data[0].mtmma));
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].iLines[1];
		for (var i = 0,l = data.length; i < l; i++) {
			currentX = level[0] * (tmplens - i) + level[0] / 2 + (oddNum ? 0 : .5);
			_this.lineTo(currentX,getY(data[i].mtmma));
		}
		_this.stroke();
		clearfill();
		stockChart.fillData[stockChart.options.user.vIndicator.toUpperCase()] = {data : data};
		stockChart.fillBoxs[stockChart.options.user.vIndicator.toUpperCase()] = fillBox;
	}
	function OHLC(){
		var total = stockChart.options.data.DATA.length,
			tmplens = stockChart.options.lens - 1,
			data = [].concat(stockChart.options.data.DATA).reverse().slice(0, stockChart.options.lens),
			ind;
		for (var i = 0; i < stockChart.options.lens; i++) {
			max = Math.max(data[i].high, max);
			min = Math.min(data[i].low, min);
		}
		for (var i = 0; i < stockChart.options.lens; i++) {
			drawOHLC(data[i], tmplens - i)
		}
	}

	function drawOHLC(data, i) {
		data.index = i;
		var isRise = data.cur * 1 >= data.open * 1,
			color = isRise ?
			stockChart.options.schemes[stockChart.options.user.schemes].rise :
			stockChart.options.schemes[stockChart.options.user.schemes].fall,
			level = stockChart.options.level[stockChart.options.user.level],
			oddNum = level[0] / 2 != (level[0] / 2).toFixed(),
			candleX, currentX, candleY, candleHeight, topY, bottomY;
		if (!rule) {
			rule = getRule();
		}
		//candleRect
		_this.strokeStyle = color;
		_this.fillStyle = color;
		_this.beginPath();
		currentX = level[0] * i + level[0] / 2 + (oddNum ? 0 : .5);

		if (currentX > _this.w - level[0]) return null;
		topY = getY(data.high);
		bottomY = getY(data.low);
		if (level[1] > 3) {
			candleX = currentX - level[1] / 2 + .5;
			if (isRise) {
				candleY = getY(data.cur);
				candleHeight = getY(data.open) - candleY;
			} else {
				candleY = getY(data.open);
				candleHeight = getY(data.cur) - candleY;
			}
			_this.moveTo(currentX, topY);
			_this.lineTo(currentX, bottomY)

			_this.moveTo(currentX, candleY);
			_this.lineTo(currentX - ~~(level[1]/2), candleY)

			_this.moveTo(currentX, candleY + candleHeight);
			_this.lineTo(currentX + ~~(level[1]/2), candleY + candleHeight)
		} else { //candleLine
			_this.moveTo(currentX, topY);
			_this.lineTo(currentX, bottomY == topY ? topY + .5 : bottomY);
		}

		_this.stroke();

	}

	function clearfill(){
		delete stockChart.fillData["MACD"];
		delete stockChart.fillBoxs["MACD"];
		delete stockChart.fillData["KDJ"];
		delete stockChart.fillBoxs["KDJ"];
		delete stockChart.fillData["MTM"];
		delete stockChart.fillBoxs["MTM"];
		delete stockChart.fillData["BOLL"];
		delete stockChart.fillBoxs["BOLL"];
	}
	_this.clearInd = function(){
		_this.clearRect(0, 0, _this.w, _this.h);
		max = Number.MIN_VALUE;
		min = Number.MAX_VALUE;
		rule = null;
	}

	_this.drawInd = function(fillBox){
		var ind = stockChart.options.user.vIndicator;
		indFn[ind.toUpperCase()] && indFn[ind.toUpperCase()](fillBox);
	}

}

// @koala-append "_http.api.js"
// @koala-append "_fire.data.js"
function _dataExchange() {
	//this -> stockChart
	var _this = this,
		stockChart = _this,
		r = request(),
		api = requestAPI(_this),
		fire = fireData(_this),
		actfn = {
			"42": "chartData",
			"43": "chartData",
			"33": "chartData",
			"34": "chartData",
			"35": "chartData",
			"36": "chartData",
			"37": "chartData",
			"38": "chartData"
		},
		requestRelease = 1;
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
		if (!stockChart.options.user['chartOnly']) {
			//交易记录
			api.tradeHistory(_this.options.user.stockID, _this.options.user.ex);
			api.market(_this.options.user.stockID, _this.options.user.ex);
			//当前股票信息
			api.curInfo1(_this.options.user.stockID, _this.options.user.ex);
			api.curInfo2(_this.options.user.stockID, _this.options.user.ex);
		}
		api.cur(_this.options.user.stockID, _this.options.user.ex);
		reInit(1, 1);
	}

	function reInit(update, change) {
		change && _this.loading.open();

		if (!stockChart.options.user['chartOnly']) {
			var date = totime(window.UTC8 || new Date() * 1, 'ymd'),
			hour = totime(window.UTC8 || new Date() * 1, 'h'),
			localDate = localStorage.getItem('ver');
			(date != localDate && hour > 9) && api.map();
		}

		requestRelease = 0;
		r.requestPayload({
			url: location.protocol + "//" + stockChart.options.url.xhr,
			data: api.data,
			success: function(data) {
				var l, isRight;
				if (!data.o) return;
				l = data.o.length;
				requestRelease = 1;
				for (; l--;) {
					if (data.o[l].act * 1 == 101) {
						if (data.o[l].o.code * 1 == _this.options.user.stockID && data.o[l].o.ex * 1 == _this.options.user.ex) {
							isRight = 1;
						}
					}
				}
				if (!isRight) return;
				dataHandle(data, update);
				_this.draw();
				_this.domlist && _this.domlist['stock'] && _this.stockInfo();
				_this.domlist && _this.domlist['market'] && _this.market();
				_this.domlist && _this.domlist['tradeHistory'] && _this.tradeHistory();
				_this.loading.close()
				//!update && _this.setKeyMap();
			}
		});
	}

	function autoget() {
		api.release();
		//行情数据
		_this.options.user.timeShare ?
			api.timeShare(_this.options.user.stockID, _this.options.user.ex) :
			api.period(_this.options.user.stockID, _this.options.user.period, _this.options.user.ex);
		api.cur(_this.options.user.stockID, _this.options.user.ex);
		if (!stockChart.options.user['chartOnly']) {
			//交易记录
			api.tradeHistory(_this.options.user.stockID, _this.options.user.ex);
			api.market(_this.options.user.stockID, _this.options.user.ex);
			//当前股票信息
			api.curInfo1(_this.options.user.stockID, _this.options.user.ex);
			api.curInfo2(_this.options.user.stockID, _this.options.user.ex);
		}
		requestRelease && reInit(1);
		setTimeout(autoget, 2000);
	}

	function dataHandle(data, isupdate) {
		var o = data.o,
			l = o.length,
			fn;
		for (; l--;) {
			fn = actfn[o[l].act] || 'data' + o[l].act;
			if (o[l].h == 200)
				fire[fn] && fire[fn](o[l], isupdate, fire);
		}
	}! function() {
		'construct';


		//api.stockList();
		//行情数据
		_this.options.user.timeShare ?
			api.timeShare(_this.options.user.stockID, _this.options.user.ex) :
			api.period(_this.options.user.stockID, _this.options.user.period, _this.options.user.ex);
		if (!stockChart.options.user['chartOnly']) {
			//交易记录
			api.tradeHistory(_this.options.user.stockID, _this.options.user.ex);
			api.market(_this.options.user.stockID, _this.options.user.ex);
			//当前股票信息
			api.curInfo1(_this.options.user.stockID, _this.options.user.ex);
			api.curInfo2(_this.options.user.stockID, _this.options.user.ex);
			//快捷键map
			
			stockChart.options.data.MAP = localStorage.getItem('stockMap') || '[]';
		}
		api.cur(_this.options.user.stockID, _this.options.user.ex);
		reInit(1, 1);
		autoget();
	}.apply(window, arguments);
}


function requestAPI(stockChart) {
	var result = {
			"v": "caimao.json.001",
			"k": "a0aea509d9059bd18735f4b8d499cfcd",
			"o": []
		},
		ind = {};

	function release() {
		result.o = [];
		return result;
	}

	function period(id, period, ex, limit) {
		var limit = limit || [0, 1500],
			act = stockChart.options.periodMap[period || '1min'];
		result.o.push({
			"act": act,
			"id1": id,
			"tp1": ex,
			"px": limit[0],
			"pl": limit[1],
			"p1": 1206
		});

		ind[stockChart.options.user.vIndicator.toLowerCase()](act, id, ex, limit);
		ind[stockChart.options.user.coverIndicator.toLowerCase()](act, id, ex, limit);
		return result;
	}


	ind.macd = function(act, id, ex, limit) {
		result.o.push({
			"act": act,
			"id1": id,
			"tp1": ex,
			"px": limit[0],
			"pl": limit[1],
			"p1": 1301
		});
	}
	ind.kdj = function(act, id, ex, limit) {
		result.o.push({
			"act": act,
			"id1": id,
			"tp1": ex,
			"px": limit[0],
			"pl": limit[1],
			"p1": 1302
		});
	}
	ind.mtm = function(act, id, ex, limit) {
		result.o.push({
			"act": act,
			"id1": id,
			"tp1": ex,
			"px": limit[0],
			"pl": limit[1],
			"p1": 1303
		});
	}
	ind.boll = function(act, id, ex, limit) {
		result.o.push({
			"act": act,
			"id1": id,
			"tp1": ex,
			"px": limit[0],
			"pl": limit[1],
			"p1": 1304
		});
	}

	ind.kma = function(act, id, ex, limit) {
		result.o.push({
			"act": act,
			"id1": id,
			"tp1": ex,
			"px": limit[0],
			"pl": limit[1],
			"p1": 1205
		});

	}

	function codeMap() {
		result.o.push({
			"act": 161
		})
		return result;
	}

	function cur(id, ex) {
		result.o.push({
			"act": 101,
			"id1": id,
			"tp1": ex
		})
		return result;
	}

	function indexNum(id, ex) {
		result.o.push({
			"act": 109,
			"id1": id,
			"tp1": ex
		})
		return result;
	}

	function curInfo1(id, ex) {
		result.o.push({
			"act": 102,
			"id1": id,
			"tp1": ex
		})
		return result;
	}

	function curInfo2(id, ex) {
		result.o.push({
			"act": 103,
			"id1": id,
			"tp1": ex
		})
		return result;
	}

	function market(id, ex) {
		result.o.push({
			"act": 104,
			"id1": id,
			"tp1": ex
		})
		return result;
	}

	function timeShare(id, ex) {
		result.o.push({
			"act": 106,
			"id1": id,
			"tp1": ex
		})
	}

	function tradeHistory(id, ex, start, len) {
		result.o.push({
			"act": 107,
			"id1": id,
			"tp1": ex,
			"px": start || "0",
			"pl": len || "100"
		})
		//wind 0->buy,1->sel
	}

	function stockList(start, lens, type, rank) {
		//console.error(arguments);
		result.o.push({
			"act": 155,
			"px": start, //
			"pl": lens, //
			"p1": type, //板块 70->沪深,24->中小板,25->创业板,10->沪A,11->沪B,12->沪债券,13->沪基金,20->深A,21->深B,22->深债券,23->深基金,101->开放基金,102->封闭基金,103->ETF,104->LOF
			"p2": rank //排序类型 10->涨幅,11->跌幅,49->代码,46->换手,30->金额
		})
	}

	function timestamp() {
		result.o.push({
			"act": 162
		});
		return result;
	}

	return {
		data: result, //请求参数
		map: codeMap, //快捷键map
		release: release, //释放参数
		period: period, //k线
		cur: cur, //当前股票
		curInfo1: curInfo1, //当前股票信息1
		curInfo2: curInfo2, //当前股票信息2
		market: market,
		stockList: stockList, //股票列表
		timeShare: timeShare, //分时
		tradeHistory: tradeHistory, //交易记录
		indexNum: indexNum,
		timestamp: timestamp
	}
}

function fireData(stockChart) {
	function chartData(data, isupdate,callback) { //act[33,34,35,36,37,38]
		if (!data || !data.o || !data.o.o || !data.o.o.length || data.act != stockChart.options.periodMap[stockChart.options.user.period]) return;
		//stockChart.options.periodMap[stockChart.options.user.period]
		//console.log('[33,34,35,36,37,38]', data)
		if(data.o.act == 1206){
			stockChart.options.data.DATA = data.o.o;
		}else{
			callback['data'+data.o.act](data.o,isupdate)
		}

	}

	function curStock(data, isupdate) { //act101
		if (!data) return;
		for (var k in data.o) {
			stockChart.options.data.DATA2[k] = data.o[k];
		}
		stockChart.options.data.MARKET.cur = data.o.cur;
		stockChart.options.data.MARKET.pre = data.o.pre;
		//console.log(101, data)
	}

	function curInfo1(data, isupdate) { //act102
		if (!data) return;
		for (var k in data.o) {
			stockChart.options.data.DATA2[k] = data.o[k];
		}
		//console.log(102, data)
	}

	function curInfo2(data, isupdate) { //act103
		if (!data) return;
		for (var k in data.o) {
			stockChart.options.data.DATA2[k] = data.o[k];
		}
		//console.log(103, data)
	}

	function timeShare(data, isupdate) { //act106
		if (!data) return;
		//console.log(106, data)
		stockChart.options.data.DATAT = {
			formatData: [],
			data: data.o
		};
	}

	function market(data, isupdate) { //act104
		if (!data) return;
		//console.log(104, data);
		stockChart.options.data.MARKET.bid = data.o.bid;
		stockChart.options.data.MARKET.ask = data.o.ask;
	}

	function tradeHistory(data, isupdate) { //act107
		if (!data) return;
		stockChart.options.data.TRADELIST = data.o;
		//console.log(107, data);
	}

	function stockList(data, isupdate) { //act161
		if (!data) return;
		//console.log(155, data)
	}

	function map(data, isupdate) { //act161
		if (!data) return;
		stockChart.options.data.MAP = JSON.stringify(data.o);
		localStorage.setItem('stockMap', stockChart.options.data.MAP);
		localStorage.setItem('ver',totime(new Date()*1,'ymd'));
		//console.log(161,data)
	}

	function macd(data, isupdate) { //act161
		if (!data) return;
		stockChart.options.data.IND.MACD = data.o;
		//console.log(1301,data)
	}

	function kdj(data, isupdate) { //act161
		if (!data) return;
		stockChart.options.data.IND.KDJ = data.o;
		//console.log(1301,data)
	}

	function mtm(data, isupdate) { //act161
		if (!data) return;
		stockChart.options.data.IND.MTM = data.o;
		//console.log(1301,data)
	}

	function kma(data, isupdate) { //act161
		if (!data) return;
		stockChart.options.data.IND.KMA = data.o;
		//console.log(1301,data)
	}
	function boll(data, isupdate) { //act161
		if (!data) return;
		stockChart.options.data.IND.BOLL = data.o;
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
		chartData: chartData,
		data101: curStock,
		data102: curInfo1,
		data103: curInfo2,
		data104: market,
		data106: timeShare,
		data107: tradeHistory,
		data155: stockList,
		data161: map,
		data1301: macd,
		data1302: kdj,
		data1303: mtm,
		data1205: kma,
		data1304: boll
	}
}

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

function splitWindows(stockChart, size, canvas, mainHeight, volHeight, indHeight, timeShareHeight, timeVolHeigh) {
	var outRule;
	function drawLine(ctx, h, spill, offset) {
		var x = (offset && offset['x']) ? offset.x : 0,
			y = (offset && offset['y']) ? offset.y : 0;
		ctx.beginPath();
		ctx.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].layoutColor;
		ctx.moveTo(~~x + .5, ~~y + .5);
		ctx.lineTo(~~x + .5, h - .5);
		if (spill) {
			ctx.lineTo(size.w, h - .5);
			ctx.moveTo(size.w - (outRule ? stockChart.options.user.ruleWidth : 0) - .5, h - .5);
		} else {
			ctx.lineTo(size.w - (outRule ? stockChart.options.user.ruleWidth : 0) - .5, h - .5);
		}
		ctx.lineTo(size.w - (outRule ? stockChart.options.user.ruleWidth : 0) - .5, ~~y + .5);
		ctx.stroke();
	}

	! function() {
		'construct';
		outRule = size.w > stockChart.options.user.outRuleWidth;
		//console.log(size.w,outRule)
		if (stockChart.options.user.timeShare) {

			drawLine(canvas.timeCanvas[1], timeShareHeight, 1, {
				x: outRule ? stockChart.options.user.ruleWidth : 0
			});
			drawLine(canvas.timeVolCanvas[1], timeVolHeigh, 0, {
				x: outRule ? stockChart.options.user.ruleWidth : 0
			});
			
		} else {
			drawLine(canvas.mainCanvas[1], mainHeight, 1);
			drawLine(canvas.volCanvas[1], volHeight, 1);
			drawLine(canvas.indCanvas[1], indHeight, 0);
		}
	}.apply(window, arguments);

}

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

function _bgLine(stockChart) {
	//this -> ctx
	var _this = this,
		_max, _min, _rule, _verticalOffset, _fixed, besid, _bediff;
	_this.drawLine = function(max, min, rule, fixed, position, minHeight,notcut) {
		//position undefined -> center,  1 -> bottom
		var outRule = _this.w > stockChart.options.user.outRuleWidth,
			h = (_this.h - stockChart.options.user.offsetTop) * (stockChart.options.user.timeShare ? 1 : stockChart.options.user.drawRange),
			rh = minHeight || (_this.types ? stockChart.options.user.minRuleHeight : stockChart.options.user.vMinRuleHeight),
			count = ~~ (h / rh),
			unit = h / count,
			xROffset = outRule ? stockChart.options.user.ruleWidth : 0,
			special, xOffset = stockChart.options.user.timeShare ? xROffset : 0,
			splitWidth = (_this.w - xROffset*2) / 8,
			spx = xOffset,__y;

		_rule = rule;
		_fixed = fixed;
		besid = false;
		if (!position) {
			_verticalOffset = ~~ ((_this.h - stockChart.options.user.offsetTop) * (.5 - stockChart.options.user.drawRange / 2) + stockChart.options.user.offsetTop);
		} else {
			_verticalOffset = stockChart.options.user.offsetTop;
			--count;
		}

		_max = max;
		_min = min;
		if (max == min) return;
		_this.font = '12px arial';
		_this.textBaseline = "middle";
		_this.lineWidth = 1;
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].layoutColor;
		_this.fillStyle = stockChart.options.schemes[stockChart.options.user.schemes].layoutFont;
		_this.beginPath();
		for (var i = 0; i <= count; i++) {
			if (i == 0) {
				special = _max;
			} else if (i == count && !position) {
				special = _min;
			} else {
				special = null;
			}
			_this.moveForDashed(xOffset, _verticalOffset + i * unit - .5);
			_this.dashedLineTo(_this.w - xROffset, _verticalOffset + i * unit - .5, 2);
			setVal(_this.w - xROffset + 2, _verticalOffset + i * unit - .5, rule, special,notcut);
		}
		_this.stroke();
		_this.drawZeroLine = function(y) {
			return;
			_this.font = '12px arial';
			_this.textBaseline = "middle";
			_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].layoutColor;
			_this.fillStyle = stockChart.options.schemes[stockChart.options.user.schemes].layoutFont;
			_this.beginPath();
			_this.moveTo(xROffset, y);
			_this.lineTo(_this.w - xROffset, y);
			setVal(_this.w - xROffset + 2, y, rule, '0');
			besid = y;
			_this.stroke();
		}
		if(stockChart.options.user.timeShare && outRule){
			for(var i = 0; i < 7;i++){
				spx += splitWidth;
				_this.beginPath();
				if(i % 2){
					_this.lineWidth = stockChart.options.schemes[stockChart.options.user.schemes].specialLine ? 2 : 1;
					_this.moveTo(spx - .5,0);
					_this.lineTo(spx - .5, _this.h);
				}else{
					_this.lineWidth = 1;
					_this.moveForDashed(spx - .5,0);
					_this.dashedLineTo(spx - .5, _this.h, 2);
				}
				_this.stroke();
			}
		}
	}
	
	_this.drawTimeLine = function(max, min, rule, fixed, bediff,outRule) {
		var middle = ~~ (((_this.h - stockChart.options.user.offsetTop) / 2) + stockChart.options.user.offsetTop),
			rh = stockChart.options.user.timeLineMinRuleHeight,
			count = ~~ ((_this.h - stockChart.options.user.offsetTop) / rh),
			unit, rg = 1,
			xOffset = outRule ? stockChart.options.user.ruleWidth : 0,
			splitWidth = (_this.w - xOffset*2) / 8,
			spx = xOffset,__y;
		_max = max;
		_min = min;

		count = (count % 2 ? count - 1 : count)/2;
		unit = (_this.h - stockChart.options.user.offsetTop) / (count*2);
		stockChart.options.unit = unit;
		_bediff = bediff*1;
		_fixed = fixed;
		_verticalOffset = stockChart.options.user.offsetTop;
		_rule = getRule();
		if (max == min) return;
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].layoutColor;
		_this.lineWidth = 1;

		if(outRule){
			for(var i = 0; i < 7;i++){
				spx += splitWidth;
				_this.beginPath();
				if(i % 2){
					_this.lineWidth = stockChart.options.schemes[stockChart.options.user.schemes].specialLine ? 2 : 1;
					_this.moveTo(spx - .5, cutFixed(middle - (count-1) * unit - .5, 0));
					_this.lineTo(spx - .5, _this.h);
				}else{
					_this.lineWidth = 1;
					_this.moveForDashed(spx - .5, cutFixed(middle - (count-1) * unit - .5, 0));
					_this.dashedLineTo(spx - .5, _this.h, 2);
				}
				_this.stroke();
			}
		}

		_this.beginPath();
		if(outRule){
			for (var i = 1; i < count; i++) {
				__y = cutFixed(middle - i * unit - .5, 0);
				_this.moveTo(xOffset, __y);
				_this.lineTo(_this.w - xOffset, __y);

				__y = cutFixed(i * unit + (middle - .5), 0);
				_this.moveTo(xOffset, __y);
				_this.lineTo(_this.w - xOffset, __y);
			}
		}else{
				__y = cutFixed(middle - (count - 1) * unit - .5, 0);
				_this.moveTo(xOffset, __y);
				_this.lineTo(_this.w - xOffset, __y);

				__y = cutFixed((count - 1) * unit + (middle - .5), 0);
				_this.moveTo(xOffset, __y);
				_this.lineTo(_this.w - xOffset, __y);
		}
		_this.stroke();
		_this.beginPath();
		if(outRule){
			for (var i = 1; i < count; i++) {
				__y = cutFixed(middle - i * unit - .5, 0);
				setTimeVal(_this.w - xOffset + 2, __y, rule, 1 , i);
				__y = cutFixed(i * unit + (middle - .5), 0);
				setTimeVal(_this.w - xOffset + 2, __y, rule, -1 , i);
			}
			__y = cutFixed(middle - .5, 0);
			setTimeVal(_this.w - xOffset + 2, __y, rule, 0 , 0);
		}else{
			__y = cutFixed(middle - (count - 1) * unit - .5, 0);
			setTimeVal(_this.w, __y, rule, 1 , (count - 1));

			__y = cutFixed((count - 1) * unit + (middle - .5), 0);
			setTimeVal(_this.w, __y, rule, -1 , (count - 1));
		}
		//zeroLine
		_this.beginPath();
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].layoutColor;
		_this.lineWidth = stockChart.options.schemes[stockChart.options.user.schemes].specialLine ? 3 : 1;
		_this.moveTo(xOffset, middle - .5);
		_this.lineTo(_this.w - xOffset, middle - .5);
		_this.stroke();
		_this.lineWidth = 1;


	function n2v(n) {
		return cutFixed((_max - _bediff) * n/(count-1), 2);
	}

	function setTimeVal(x, y, rule, rg, n) {
		var v;
		if (rg > 0) {
			_this.fillStyle = stockChart.options.schemes[stockChart.options.user.schemes].riseFont;
			v = n2v(n) * 1 + _bediff;
		} else if (rg == 0) {
			v = _bediff
			_this.fillStyle = stockChart.options.schemes[stockChart.options.user.schemes].fontColor;
		} else {
			_this.fillStyle = stockChart.options.schemes[stockChart.options.user.schemes].fallFont;
			v = _bediff - n2v(n)
		}
		_this.font = '12px arial';
		_this.textAlign = outRule ? "right" : "left";
		_this.textBaseline = "middle";
		_this.fillText(
			shortIndex(v, (_fixed || 0), stockChart.options.user.binary, stockChart.options.binary[stockChart.options.user.binary]), xOffset - (outRule ? 5 : -2), y);
		_this.textAlign = outRule ? "left" : "right";
		_this.fillText(
			shortIndex((v - _bediff) * 100 / _bediff, (_fixed || 0), stockChart.options.user.binary, stockChart.options.binary[stockChart.options.user.binary]) + '%', x + (outRule ? 5 : -2), y);
	}



	}

	function getRule() {
		var dp = _max - _min;
		return !dp ? 0 : (_this.h - _verticalOffset) / dp;
	}

	function y2v(y) {
		return cutFixed(_max - (y - _verticalOffset - 0.5) / _rule, 2);
	}

	function setVal(x, y, rule, v, notcut) {
		_this.fillText(
			shortIndex(v === 0 ? v : (v || y2v(y) * 1), (_fixed || 0), stockChart.options.user.binary, stockChart.options.binary[stockChart.options.user.binary], notcut), x + 5, y);
	}

}

function _domctrl() {
	//this -> stockChart
	var _this = this,
		stockChart = _this,
		html = {};
	_this.stockInfo = function() {
		if (_this.domlist.stock){
			if(!html['stock'])
				html.stock = _this.domlist.stock.querySelector('script').innerHTML;
			_this.domlist.stock.innerHTML = rentmpl(html.stock, {
				data: stockChart.options.data.DATA2
			});
		}
	}

	function getMarket() {
		var ul = _this.domlist.market.getElementsByTagName('ul'),
			script = _this.domlist.market.getElementsByTagName('script'),
			cur = _this.domlist.market.querySelector('div');
		html.market = {
			sel: {
				dom: ul[0],
				str: script[0].innerHTML
			},
			buy: {
				dom: ul[1],
				str: script[2].innerHTML
			},
			cur: {
				dom: cur,
				str: script[1].innerHTML
			}
		}
	}
	_this.market = function() {
		if (!html['market']) getMarket();
		html.market.sel.dom.innerHTML = rentmpl(html.market.sel.str, {
			data: stockChart.options.data.MARKET.bid,
			pre: stockChart.options.data.MARKET.pre
		});
		html.market.buy.dom.innerHTML = rentmpl(html.market.buy.str, {
			data: stockChart.options.data.MARKET.ask,
			pre: stockChart.options.data.MARKET.pre
		});

		html.market.cur.dom.innerHTML = rentmpl(html.market.cur.str, {
			cur: stockChart.options.data.MARKET.cur,
			pre: stockChart.options.data.MARKET.pre
		});
		if(_this['tradeForm']){
			if(!_this.tradeForm.buy.inputs[0].getAttribute('cooldown'))
				_this.tradeForm.buy.inputs[0].value = stockChart.options.data.MARKET.bid[stockChart.options.data.MARKET.bid.length-1].pri;
			if(!_this.tradeForm.sell.inputs[0].getAttribute('cooldown'))
				_this.tradeForm.sell.inputs[0].value = stockChart.options.data.MARKET.ask[0].pri;
		}

	}

	_this.tradeHistory = function() {
		if (!html['tradeHistory']){
			html.tradeHistory = {
				str : _this.domlist.tradeHistory.querySelector('script').innerHTML,
				dom : _this.domlist.tradeHistory.querySelector('ul')
			}
		}
		html.tradeHistory.dom.innerHTML = rentmpl(html.tradeHistory.str, {
			data: stockChart.options.data.TRADELIST.reverse(),
			pre: stockChart.options.data.MARKET.pre

		});
	}
}

function _keymap() {
	//this -> stockChart
	var _this = this,
		stockChart = _this,
		map,
		ind = [
			{"ex":"0","code":"KDJ","codeValue":"KDJ","name":"随机指标","action":"actionSetInd"},
			{"ex":"0","code":"MACD","codeValue":"MACD","name":"指数平滑异同平均线","action":"actionSetInd"},
			{"ex":"0","code":"MTM","codeValue":"MTM","name":"动量线","action":"actionSetInd"},
			{"ex":"0","code":"BOLL","codeValue":"BOLL","name":"布林带","action":"actionSetInd"},
			{"ex":"0","code":"BOLL","codeValue":"BOLL","name":"主图叠加布林带","action":"actionSetMainInd"},
			{"ex":"0","code":"MA","codeValue":"KMA","name":"主图叠加均线","action":"actionSetMainInd"}
		];
	ind = JSON.stringify(ind);
	_this.setKeyMap = function(v) {
		map = window['MAP'] || stockChart.options.data.MAP;
		if(!v)return;
        var regCode = new RegExp('\\{[^\\}]*"code":"[\\.]*'+v+'[^\\}]*}',"gi"),
            regSpell = new RegExp('\\{[^\\}]*"spell":"[\\.]*'+v+'[^\\}]*}',"gi"),
            befor = window["HASINDKEY"] ? ind.match(regCode) || [] : [];
        return befor.concat(map.match(regCode) || map.match(regSpell) || []);
	}
}

// @koala-append "_action.js"
function _event(box, canvas) {
	//this -> stockChart
	var _this = this,
		stockChart = _this,
		shortcutInput, isFocus, doubleWait = 0,doubleEventWait = 0,
		bind;
	stockChart.crossStart = 0;

	function keyEvent() {
		if (!bind) {
			Event.add(document, 'keydown', keyboardEvent);
			Event.add(document, 'keyup', keyboardEvent2);
			J(_action, _this, box, canvas, _this['domlist'] ? _this.domlist.shortcut : document.querySelector('#shortcut'));
		}
		bind = 1;
		
		Event.add(document, 'click', clickEvent);
		//if (!_this['domlist']) return setTimeout(keyEvent, 500);
		box && Event.add(box, 'click', doubleClickEnter);
		Event.add(document, 'click', doubleClickEvent);
		if(document.querySelector('#shortcut')){
			shortcutInput = (_this['domlist'] ? _this.domlist.shortcut : document.querySelector('#shortcut')).parentNode.getElementsByTagName('input')[0];
			Event.add(shortcutInput, 'focus', function() {
				isFocus = 1;
			})
			Event.add(shortcutInput, 'blur', function() {
				isFocus = 0;
			})
		}
	}

	function getStop(n) {
		if (!n || !n.parentNode) return null;
		if (n.getAttribute('stop')) {
			return n;
		}
		return getStop(n.parentNode);
	}

	function once_Event(e) {
		var e = window.event || e,
			kc = e.keyCode,
			tar = Event.target(e),
			act = tar.getAttribute('act');
		//console.log(kc);
		if (kc == 27) {
			Event.stop(e);
			_this.historyGo();
		}
		if (kc == 112 && _this.isLogin) {
			Event.stop(e);
			_this.toBuy();
		}
		if (kc == 113 && _this.isLogin) {
			Event.stop(e);
			_this.toSell();
		}
		if (kc == 114 && _this.isLogin) {
			Event.stop(e);
			_this.toCancel();
		}
		if (kc == 115 && _this.isLogin) {
			Event.stop(e);
			stockChart.toQuery();
		}
		if (act == 'getusable') {
			return tar.setAttribute('cooldown', 1);
		}
		if(!stockChart.options.user.timeShare && !stockChart.shortcutIsShow && kc == 38 && !stockChart.noChart){
			return stockChart.setLevel(1);
		}
		if(!stockChart.options.user.timeShare && !stockChart.shortcutIsShow && kc == 40 && !stockChart.noChart){
			return stockChart.setLevel(-1);
		}
		if(stockChart.options.user.timeShare && !stockChart.shortcutIsShow && kc== 13 && !stockChart.noChart){
			stockChart.kt('k');
		}
		if (getStop(tar) || kc == 116 || kc == 123 || (kc == 8 && !isFocus)) return;
	}

	function lots_Event(e) {
		var e = window.event || e,
			kc = e.keyCode,
			tar = Event.target(e),
			act = tar.getAttribute('act');
		if (getStop(tar) || kc == 116 || kc == 123 || (kc == 8 && !isFocus)) return;

		if (
			(kc > 47 && kc < 58) ||
			(kc > 64 && kc < 91) ||
			(kc > 95 && kc < 106)
		) {
			if (shortcutInput && !_this.shortcutIsShow) {
				_this.shortcutShow(_this['domlist'] ? _this.domlist.shortcut : document.querySelector('#shortcut'));
				act = 'keyMapInput';
				shortcutInput.focus();
			}
		}
		if (_this[act]) {
			return _this[act](e);
		}
		Event.stop(e);
	}

	function keyboardEvent(e) {
		var tar = Event.target(e);
		if(stockChart.options.user.pause)return;
		if(getStop(tar))return;
		once_Event(e);
		lots_Event(e);
	}

	function keyboardEvent2(e) {
		var tar = Event.target(e);
		if(stockChart.options.user.pause)return;
		if(getStop(tar))return;
		lots_Event(e);
	}

	function clickEvent(e) {
		var e = window.event || e,
			tar = Event.target(e),
			act = attFather(tar, 'act'),
			shortcut = attFather(tar, 'shortcut'),
			stop = attFather(tar, 'stop'),
			data = attFather(tar, 'data-val');
		if(stockChart.options.user.pause)return;
		stop && Event.stop(e);
		!shortcut && _this.shortcutIsShow && _this.shortcutHidden();
		_this[act] && _this[act](tar, data, e);
	}

	function doubleClickEnter(e) {
		if(stockChart.options.user.pause)return;
		if (doubleWait) {
			_this.doubleAction();
		}
		doubleWait = 1;
		setTimeout(function() {
			doubleWait = 0;
		}, stockChart.options.user.doubleClick)
	}

	function doubleClickEvent(e) {
		var tar = Event.target(e),
			act = attFather(tar, 'doubleact'),
			stop = attFather(tar, 'stop'),
			data = attFather(tar, 'data-val');
			
		if(stockChart.options.user.pause)return;
		if (doubleEventWait) {
			stop && Event.stop(e);
			_this[act] && _this[act](tar, data, e);
		}
		doubleEventWait = 1;
		setTimeout(function() {
			doubleEventWait = 0;
		}, stockChart.options.user.doubleClick)
	}

	! function(id, opt) {
		'construct';
		keyEvent();
		box && Event.add(box, 'mouseover', _this.mouseover);
		box && Event.add(box, 'mousemove', _this.mousemove);
		box && Event.add(box, 'mouseout', _this.mouseout);

	}.apply(window, arguments);
}

function _action(box, canvas,doms) {
	//this -> stockChart
	var _this = this,
		stockChart = _this,
		html = {},
		tip = besideTip(),
		tmpStr, lens = 0,
		cur, selectorCurrent = -1,
		timer,mouseBusy;
	_this.keyMapInput = function(e) {
		var val = Event.target(e).value,
			kc = e.keyCode,
			types = e.type,
			data, curdata, idx;
		if (!val) {
			data = [];
		} else {
			data = _this.setKeyMap(val) || [];
		}
		if (tmpStr == val) {
			//38 up, 40 down
			if (kc == 38 || kc == 40) Event.stop(e);
			if (types.toLowerCase() == 'keyup') return;
			if (kc == 38) {
				selectorCurrent += -1;
			} else if (kc == 40) {
				selectorCurrent += 1;
			}
			if (kc == 38 || kc == 40) {
				if (selectorCurrent < 0) selectorCurrent = 0;
				if (selectorCurrent >= lens) selectorCurrent = lens - 1;
				go(selectorCurrent, kc);
			}
			if (kc == 13) {
				idx = selectorCurrent > 0 ? selectorCurrent : 0;
				curdata = data[idx];
				_this.shortcutHidden();
				doms.parentNode.getElementsByTagName('input')[0].value = '';
				if(!curdata)return;
				_this.changeStock(JSON.parse(curdata),1)
			}
		} else {
			tmpStr = val;
			cur = null;
			selectorCurrent = -1;
			data.splice(50, 100000);
			data = JSON.parse('[' + data.join(',') + ']');
			lens = data.length;
			if(!html['shortcut'] && !doms.querySelector('script'))return;
			if (!html['shortcut']) {
				html.shortcut = doms.querySelector('script').innerHTML;
			}
			doms.innerHTML = rentmpl(html.shortcut, {
				data: data
			});
		}
	}

	function go(n) {
		var li = doms.getElementsByTagName('li'),
			offset = (n + 1) * li[0].offsetHeight,
			ot = doms.scrollTop + doms.offsetHeight;
		cur && dom.hasClass(cur, 'on') && dom.removeClass(cur, 'on');
		cur = li[n];
		offset = offset - doms.offsetHeight;
		doms.scrollTop = offset > 0 ? offset : 0;
		!dom.hasClass(cur, 'on') && dom.addClass(cur, 'on');
	}

	_this.shortcutShow = function(doms) {
		stockChart.shortcutIsShow = 1;
		doms.parentNode.parentNode.style.bottom = 0;
	}
	_this.shortcutHidden = function() {
		stockChart.shortcutIsShow = 0;
		doms.parentNode.getElementsByTagName('input')[0].value = '';
		doms.parentNode.parentNode.style.bottom = -1 * doms.parentNode.parentNode.offsetHeight + 'px';
	}
	_this.changeStock = function(data,iskey) {
		var ex = data.ex,
			code = data.code;
		if(data["action"]){
			return _this[data.action] && _this[data.action](data);
		}
		if(window['MAP'] && iskey){
			return window.location.href = 'stock.html?id='+code+'&ex='+ex; 
		}
		_this.options.user.stockID = data.code;
		_this.options.user.ex = data.ex;
		stockChart.options.user.ctrlVer = new Date()*1;
		_this.getNewStock();
	}
	_this.goDetail = function(tar, data) {
		var d = data.split('/'),
			ex = d[0],
			code = d[1];
		_this.shortcutHidden();
		window.location.href = 'stock.html?id='+code+'&ex='+ex;
	}
	_this.clickChangeStock = function(tar, data) {
		var d = data.split('/'),
			ex = d[0],
			code = d[1];
		_this.options.user.stockID = code;
		_this.options.user.ex = ex;
		stockChart.options.user.ctrlVer = new Date()*1;
		_this.getNewStock();
		_this.shortcutIsShow && _this.shortcutHidden();
	}
	_this.actionSetInd = function(tar,data){
		var name = data && data.split("/");
		// if(!stockChart.options.hasInd[stockChart.options.user.period]){
		// 	return tip.changeObj(tar).top().notice("当前周期下没有这个指标数据",2000,"warning");
		// }
		if(!data && tar["codeValue"])return _this.setInd(tar.codeValue);
		name[1] && _this.setInd(name[1],stockChart.options.user.vIndicator.toLowerCase() != name[1] || stockChart.options.user.timeShare);
	}
	_this.actionSetMainInd = function(tar,data){
		if(!data && tar["codeValue"])return _this.setMainInd(tar.codeValue);
		data && _this.setMainInd(data,stockChart.options.user.coverIndicator.toLowerCase() != data || stockChart.options.user.timeShare);
	}
	_this.changePeriod = function(tar,data){
		var period = data;
		if(!data || stockChart.options.user.period == data && !stockChart.options.user.timeShare)return;
		stockChart.options.user.timeShare = 0;
		stockChart.options.layout = [1,1,1];

		stockChart.options.data.DATA = [];
		stockChart.options.data.DATA2 = {};
		stockChart.options.data.IND = {};
		stockChart.options.user.period = data;
		stockChart.winReset();
		stockChart.getNewStock();
	}
	_this.cross = function(tar, data, e) {
		if(stockChart.options.user.pause)return;
		stockChart["kBox"] && (stockChart.kBox.style.left = "-299px");
		if (stockChart.crossStart) {
			stockChart.crossStart = 0;
			_this.mouseout(e);
			canvas.toolsCanvas[1].clear();
		} else {
			stockChart.crossStart = 1;
			_this.mouseover(e);
			_this.mousemove(e);
			!stockChart.options.user.timeShare && stockChart["kBox"] && (stockChart.kBox.style.left = "22px");
		}
	}

	_this.mouseover = function(e) {
		if(stockChart.options.user.pause)return;
		if (!stockChart.crossStart) return;
		clearTimeout(timer);
		timer = null;
	}

	_this.goUrl = function(tar,data){
		window.location.href = data + '?id=' + stockChart.options.user.stockID + '&ex=' + stockChart.options.user.ex;
	}

	_this.mousemove = function(e) {
		if(stockChart.options.user.pause)return;
		if (!stockChart.crossStart) return;
		mouseMoveSwitch(e);
		//throwing && throwCanvas(e);
	}

	_this.mouseout = function(e) {
		if(stockChart.options.user.pause)return;
		canvas.toolsCanvas[1].clear();
		canvas.toolsCanvas[1].drawTimeAndVol(stockChart.options.lens - 1,null,null);
	}
	function mouseMoveSwitch(e) {
		//switch mouse move event
		var pos = Event.mouse(e);
		var level = stockChart.options.level[stockChart.options.user.level];
		if(mouseBusy)return;
		mouseBusy = 1;
		var outRule = box.size.w > stockChart.options.user.outRuleWidth;
		var idx = (pos.x - dom.getPosition(box).x - (stockChart.options.user.timeShare ? ( outRule ? stockChart.options.user.ruleWidth : 0 ) : 0));

		canvas.toolsCanvas[1].crossLine(idx, pos.y - dom.getPosition(box).y, level[0]);

		canvas.toolsCanvas[1].drawTimeAndVol(idx, pos.y - dom.getPosition(box).y, level[0]);



		setTimeout(function() {
			mouseBusy = 0;
		}, 20);
	}

	_this.historyGo = function(){
		if(stockChart.options.user.pause)return;
		if(stockChart.crossStart){
			return _this.cross();
		}
		if(stockChart.shortcutIsShow){
			return _this.shortcutHidden();
		}
		if(~window.location.href.indexOf('list.html')){
			window.location.href = window.location.href.replace('list','stock');
			return;
		}
		if(!stockChart.options.user.timeShare){
			stockChart.kt();
		}else if(window.location.href.split('/').pop() != 'index.html' && !stockChart.options.user.popstop){
			window.location.href = './index.html';
		}
	}
	_this.doubleAction = function(){
		if(stockChart.options.user.pause)return;
		if(stockChart.options.user.timeShare && stockChart.options.user.double){
			return stockChart.kt();
		}
	}
}

function _tools(stockChart,box) {
	//this -> ctx
	var _this = this,curIdx;

	function getRule() {
		var dp = max - min;
		return !dp ? 0 : (_this.h - stockChart.options.user.offsetTop) * stockChart.options.user.drawRange / dp;
	}

	function getY(v) {
		return ((max - v) * rule).toFixed() * 1 + verticalOffset * 1 + 0.5;
	}

	function bigScreen(w, timesLens,rw,xpos) {
		var data = stockChart.options.data.DATAT.formatData,
			ind = xpos + rw,tmp;
		for(var i = 0, l = data.length; i < l; i++){
			if(data[i].x > ind)break;
			tmp = data[i].x;
		}
		if(!data[i])
			return [tmp,i-1]
		if(!tmp || Math.abs(tmp - ind) > Math.abs(data[i].x - ind)){
			return [data[i].x,i]
		}else{
			return [tmp,i-1]
		}
	}

	function smallScreen(w, timesLens,rw,xpos) {
		var old = stockChart.options.data.DATAT.data,
			newArr = [],
			lots = timesLens / w,
			afterFloat = 0,
			data,
			limit = 0;

		for (i = 0; i < w; i++) {
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
	function getTimepos(x, y, level,outRule){
		var w = box.size.w - ( outRule ? stockChart.options.user.ruleWidth : 0 )*2 -2,
			idx = x,
			xpos =  w > 242 ? 
			bigScreen(w, 242 ,( outRule ? stockChart.options.user.ruleWidth : 0 ),idx) :
			smallScreen(w, 242,( outRule ? stockChart.options.user.ruleWidth : 0 ),idx),
			y = ~~(y - (document.documentElement.scrollTop || document.body.scrollTop)) - .5;
		return [xpos[0] + .5,y ,xpos[1]];
	}
	function getKpos(x, y, level,outRule){
		var x = ~~(x/level),
			idx = (x >= stockChart.options.lens) ?
					stockChart.options.lens - 1 :
					(x < 0 ? 0 : x),
			x = ~~( idx*level + level/2 + 1*(stockChart.options.user.timeShare ? ( outRule ? stockChart.options.user.ruleWidth : 0 ) : 0)) + 0.5,
			y = ~~(y - (document.documentElement.scrollTop || document.body.scrollTop)) - .5;
		return [x,y,idx];
	}
	_this.crossLine = function(x, y, level){
		var outRule = box.size.w > stockChart.options.user.outRuleWidth;
		var pos = stockChart.options.user.timeShare ? getTimepos(x,y,level,outRule) : getKpos(x,y,level,outRule);
		_this.clear();
		_this.beginPath();
		_this.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].crossLine;
		_this.moveTo(pos[0], 0);
		_this.lineTo(pos[0], _this.h - stockChart.options.user.timeline);
		if(pos[1] < _this.h - stockChart.options.user.timeline){
			_this.moveTo(stockChart.options.user.timeShare ? ( outRule ? stockChart.options.user.ruleWidth : 0 ) : 0, pos[1]);
			_this.lineTo(_this.w - ( outRule ? stockChart.options.user.ruleWidth : 0 ), pos[1]);
		}
		_this.stroke();
	}
	
	_this.clear = function(){
		_this.clearRect(0,0,_this.w,_this.h);
	}
	
	_this.drawTimeAndVol = function(x, y, level){
		var outRule = box.size.w > stockChart.options.user.outRuleWidth;
		var pos = stockChart.options.user.timeShare ? getTimepos(x,y,level,outRule) : getKpos(x,y,level,outRule),
			idx = pos[2];
		if(y === null && level === null){
			idx = null;
		}else{
			//console.log('draw');
		}
		if(curIdx == idx)return;
		//console.log(idx)
		stockChart.fillCurData(idx);
		curIdx = idx;
	}
}

function _fillData(box){
	var _this = this,
		stockChart = _this,outRule,
		title = {
			KMA : {
				a5 : 'MA5',
				a10 : 'MA10',
				a20 : 'MA20',
				a30 : 'MA30',
				a60 : 'MA60',
				a120 : 'MA120'
			},
			BOLL : {
				title : 'BOLL(20,2)',
				m : 'MID',
				u : 'UPPER',
				l : 'LOWER'
			}
		}
		period = {
			"1week": '周K线',
			"1month": '月K线',
			"1day": '日K线',
			"1min": '1分钟',
			"5min": '5分钟',
			"15min": '15分钟',
			"30min": '30分钟',
			"1hour": '1小时'
		},
		tmp = {
				timeShare: '<%if(outRule){%><em><%=data.name%></em><%}%>'
					+'<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[0]%>">最新：<%=(obj.drawData.pri*1).toFixed(2)%></em>'
					+'<% if(code == "000001" || code == "399001" || code == "399006"){%>'
						+'<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[1]%>">领先：<%=(obj.drawData.avg*1).toFixed(2)%></em>'
					+'<% }else{ %>'
						+'<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[1]%>">均价：<%=(obj.drawData.avg*1).toFixed(2)%></em>'
					+'<% } %>'
					+'<%if(outRule){%><em style="color:<%=(obj.pri*1 > cfg.data.DATA2.pre*1 ? cfg.schemes[cfg.user.schemes].rise : cfg.schemes[cfg.user.schemes].fall)%>">量：<%=obj.drawData.vol%></em><%}%>',
				

				k: '<%if(outRule){%><b><%=periodTitle%></b><em><%=data.name%></em><%}%>'
					+ '<%if(data.ind && data.ind[i]){%>'
					+ '<%if(title["title"]){%><em><%=title.title%></em>'
					+ '<%}var j = 0;for(var k in data.ind[i]){%>'
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[j++]%>"><%=title[k]%>:<%=cutFixed(data.ind[i][k],2)%></em>' 
					+ '<%}%>'
					+ '<%}%>',
				kbox : '<ul>'
					+ '<li>时间：<i><%=(/(.{4})(.{2})(.{2})/.exec(obj.date) ? (obj.date).replace(/(.{4})(.{2})(.{2})/g,"$1-$2-$3") : (obj.date).replace(/(.{1,2})(.{2})/g,"$1:$2"))%></i></li>'
					+ '<li>开盘：<i class="<%if(obj.open*1 > pre){%>red<%}else if(obj.open*1 <pre){%>green<%}else{%>white<%}%>"><%=obj.open%></i></li>' 
					+ '<li>最高：<i class="<%if(obj.high*1 > pre){%>red<%}else if(obj.high*1 <pre){%>green<%}else{%>white<%}%>"><%=obj.high%></i></li>' 
					+ '<li>最低：<i class="<%if(obj.low*1 > pre){%>red<%}else if(obj.low*1 <pre){%>green<%}else{%>white<%}%>"><%=obj.low%></i></li>' 
					+ '<li>收盘：<i class="<%if(obj.cur*1 > pre){%>red<%}else if(obj.cur*1 <pre){%>green<%}else{%>white<%}%>"><%=obj.cur%></i></li>' 
					+ '<li>涨幅：<i class="<%if((obj.cur - pre)*100/pre > 0){%>red<%}else if((obj.cur - pre)*100/pre < 0){%>green<%}else{%>white<%}%>"><%=cutFixed((obj.cur - pre)*100/pre,3)%>%</i></li>'
					+ '<li>振幅：<i class="blue"><%=cutFixed((obj.high - obj.low)*100/pre,3)%>%</i></li>'
					+ '<li>总手：<i class="blue"><%=shortIndex(obj.vol*1,3,CFG.user.binary,CFG.binary[CFG.user.binary])%></i></li>'
					+ '<li>金额：<i class="blue"><%=shortIndex(obj.sum*1,3,CFG.user.binary,CFG.binary[CFG.user.binary])%></i></li>'
					+ '<li>换手：<i class="blue"><%=cutFixed(obj.swap,3)%>%</i></li>'
					+ '</ul>',

				vol: '<em style="color:<%=(obj.cur * 1 > obj.open ? cfg.schemes[cfg.user.schemes].rise :'
					+ 'cfg.schemes[cfg.user.schemes].fall)%>">总手：<%=obj.vol%></em>',
				

				MACD: '<em>MACD(12,26,9)</em>' 
					+ '<%if(outRule){%>'
					+ '<em style="color:<%=(cfg.schemes[cfg.user.schemes][obj.idx_MACD > 0 ? "MACDrise" : "MACDfall"])%>">MACD：<%=obj.idx_MACD%></em>' 
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[0]%>">DIFF：<%=obj.idx_DIF%></em>' 
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[1]%>">DEA：<%=obj.idx_DEA%></em>'
					+ '<%}%>',
				

				KDJ: '<em>KDJ(9,3,3)</em>' 
					+ '<%if(outRule){%>'
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[0]%>">K：<%=cutFixed(obj.k,2)%></em>' 
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[1]%>">D：<%=cutFixed(obj.d,2)%></em>' 
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[2]%>">J：<%=cutFixed(obj.j,2)%></em>'
					+ '<%}%>',

				BOLL: '<em>BOLL(20,2)</em>' 
					+ '<%if(outRule){%>'
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[0]%>">MID：<%=cutFixed(obj.m,2)%></em>' 
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[1]%>">UPPER：<%=cutFixed(obj.u,2)%></em>' 
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[2]%>">LOWER：<%=cutFixed(obj.l,2)%></em>'
					+ '<%}%>',
				

				MTM: '<em>MTM(12,6)</em>' 
					+ '<%if(outRule){%>'
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[0]%>">MTM：<%=cutFixed(obj.mtm,2)%></em>' 
					+ '<em style="color:<%=cfg.schemes[cfg.user.schemes].iLines[1]%>">MTMMA：<%=cutFixed(obj.mtmma,2)%></em>' 
					+ '<%}%>'
			};
	_this.fillCurData = function(idx){
		var data,i;
		outRule = box.size.w > stockChart.options.user.outRuleWidth;
		if(stockChart.options.user.showInfo){

			for(var k in stockChart.fillBoxs){
				data = stockChart.fillData[k];
				i    = idx === null ? data.data.length - 1 : idx;
				i = data.data.length - i - 1;
				if(data.data[i]){
					stockChart.fillBoxs[k].innerHTML = rentmpl(
						tmp[k],
						{
							data : data,
							code : _this.options.user.stockID,
							i : i,
							obj : data.data[i],
							cfg : stockChart.options,
							outRule : outRule,
							title : title[stockChart.options.user.coverIndicator.toUpperCase()],
							periodTitle : period[stockChart.options.user.period]
						}
					);
					if(k === 'k'){
						stockChart['kBox'] && (stockChart.kBox.innerHTML = rentmpl(
							tmp['kbox'],
							{
								data : data,
								pre : data.data[i+1] ? data.data[i+1].cur*1 : data.data[i].open*1,
								code : _this.options.user.stockID,
								i : i,
								obj : data.data[i],
								cfg : stockChart.options,
								outRule : outRule
							}
						));
					}
				}
			}

		}
	}
}
