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
		//!stockChart.crossStart && stockChart.fillCurData(null);
		stockChart.fillCurData(null);
	}

	function kt(ts, td) {
		if (ts === 'timeshare' || td === "timeshare") {
			if (stockChart.options.user.timeShare) return;
			stockChart.options.user.timeShare = 1;
			stockChart.options.user.ctrlVer = new Date() * 1;
			stockChart.options.user.cyc = "timeshare";
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
			stockChart.options.user.cyc = "1day";
			stockChart.options.layout = [1,1,1];
		}
		stockChart.options.user.ctrlVer = new Date() * 1;
		stockChart.getNewStock();
		window["ISCHART"] && (HASINDKEY = !stockChart.options.user.timeShare);
	}

	function setInd(ind,force) {
		if (stockChart.options.user.vIndicator.toLowerCase() == ind.toLowerCase() && !force) return;
		stockChart.options.user.vIndicator = ind;
		stockChart.options.user.ctrlVer = new Date() * 1;
		if (stockChart.options.user.timeShare) {
			stockChart.options.user.timeShare = 0;
		}
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
		objLoading.innerHTML = "<div style='text-align:center;line-height:500px;font-size:30px;height:552px;background:#fff;'>loading</div>";
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
	stockChart.setLevel = setLevel;

	function toStock(id, ex) {
		stockChart.changeStock({
			code: id,
			ex: ex
		})
	}
	return {
		draw: draw,
		toStock: toStock,
		box: box,
		stockChart: stockChart,
		setLevel: setLevel,
		setdomlist: setdomlist,
		kt: kt,
		setInd: setInd
	};
}