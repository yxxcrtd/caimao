function splitWindows(stockChart, size, canvas, mainHeight, volHeight, indHeight, timeShareHeight, timeVolHeigh) {
	var outRule;
	function drawLine(ctx, h, spill, offset) {
		var x = (offset && offset['x']) ? offset.x : 0,
			y = (offset && offset['y']) ? offset.y : 0;
		ctx.beginPath();
		ctx.strokeStyle = stockChart.options.schemes[stockChart.options.user.schemes].layoutColor;
		ctx.moveTo(~~x + .5, ~~y + .5);
		stockChart.options.user.timeShare && ctx.lineTo(~~x + .5, h - .5);
		ctx.moveTo(~~x + .5, h - .5);
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