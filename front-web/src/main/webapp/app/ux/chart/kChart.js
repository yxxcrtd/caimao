var chart;
var dayKData = [];
var weekKData = [];
var monthKData = [];
//dataLengthToShow = 100;

// overwrite the function controlling the color of 'column'
var originalDrawPoints = Highcharts.seriesTypes.column.prototype.drawPoints;
Highcharts.seriesTypes.column.prototype.drawPoints = function() {
	var merge = Highcharts.merge, series = this, chart = this.chart, points = series.points, i = points.length;
	while (i--) {
		var candlePoint = chart.series[0].points[i];
		var seriesPointAttr;
		if (typeof (candlePoint.open) != "undefined"&& typeof (candlePoint.close) != "undefined") {
			var color = (candlePoint.open < candlePoint.close) ? '#DD2200' : '#33AA11';
			seriesPointAttr = merge(series.pointAttr);
			seriesPointAttr[''].fill = color;
			seriesPointAttr.hover.fill = Highcharts.Color(color).brighten(0.3).get();
			seriesPointAttr.select.fill = color;
		} else if (typeof (candlePoint.y) != "undefined") {
			var color = null;
			if (i == 0)
				color = (candlePoint.y >= 0) ? '#DD2200' : '#33AA11';
			else
				color = (candlePoint.y >= chart.series[0].points[i - 1].y) ? '#DD2200' : '#33AA11';
			seriesPointAttr = merge(series.pointAttr);
			seriesPointAttr[''].fill = color;
			seriesPointAttr.hover.fill = Highcharts.Color(color).brighten(0.3).get();
			seriesPointAttr.select.fill = color;
		} else {
			 seriesPointAttr = merge(series.pointAttr);
		}
		points[i].pointAttr = seriesPointAttr;
	}
	originalDrawPoints.call(this);
};

// 处理数据显示单位
var bigDataFormat = function(data, fixedNumber) {
	if (data >= 10000 && data < 100000000)
		return (data / 10000).toFixed(fixedNumber).toString() + '万元';
	else if (data >= 100000000)
		return (data / 100000000).toFixed(fixedNumber).toString() + '亿元';
	else
		return data.toString()+ '元';
};

//5、10、30日均价、均量
var getMaPriceKData = function(priceKData,volumeKData) {
	var maData = new Array([],[],[],[],[],[]);
	temp = [5,10,30];
	for( var t = 0; t < temp.length; t++){
		if (priceKData.length >= temp[t]) {
			for ( var i = temp[t]-1; i < priceKData.length; i++) {
				var sumPrice = 0;
				var sumVolume = 0;
				for ( var j = 0; j < temp[t]; j++) {
					sumPrice += parseFloat(priceKData[i - j][4]);
					sumVolume += parseFloat(volumeKData[i - j][1]);
				}
				maData[t].push([ priceKData[i][0], sumPrice / temp[t] ]);
				maData[t+3].push([ volumeKData[i][0], sumVolume / temp[t] ]);
			}
		}
	}
	return maData;
};

//获取k线数据
var getSingleKData = function(kType, data) {
	var kDataSet = [];
	var priceKData = [];
	var volumeKData = [];
	if (data != null && data.length > 0) {
		for ( var i = 0; i < data.length; i++) {
			priceKData.push([ parseInt(data[i].timeStamp),parseFloat(data[i].openPrice),
					parseFloat(data[i].maxPrice),parseFloat(data[i].minPrice),parseFloat(data[i].closePrice) ]);
			volumeKData.push([ parseInt(data[i].timeStamp),parseInt(data[i].total) ]);
		}
		var maData = getMaPriceKData(priceKData,volumeKData);

		kDataSet[0] = priceKData;
		kDataSet[1] = maData[0];
		kDataSet[2] = maData[1];
		kDataSet[3] = maData[2];
		kDataSet[4] = volumeKData;
		kDataSet[5] = maData[3];
		kDataSet[6] = maData[4];
		kDataSet[7] = maData[5];

		if (kType == 'dayK')
			dayKData = kDataSet;
		else if (kType == 'weekK')
			weekKData = kDataSet;
		else if (kType == 'monthK')
			monthKData = kDataSet;
	}
	return kDataSet;
};

//根据k线类型设置不同的缩放按钮
var zoomButton = function(kType){
	var buttons = [];
	if(kType == 'dayK'){
		buttons =  [{ type: 'month',count: 1,text: '1M'}, {type: 'month',count: 3,text: '3M' }, 
		            { type: 'month',count: 6, text: '6M'}, { type: 'year',count: 1,text: '1Y'}];
	}else if(kType == 'weekK'){
		buttons =  [{ type: 'month',count: 3,text: '3M'}, {type: 'month',count: 6,text: '6M' }, 
		            { type: 'year',count: 1,text: '1Y'},  { type: 'year',count: 2,text: '2Y'}];
	}else if(kType == 'monthK'){
		buttons =  [{ type: 'year',count: 1,text: '1Y'},  { type: 'year',count: 2,text: '2Y'},
		            { type: 'year',count: 3,text: '3Y'},  { type: 'year',count: 5,text: '5Y'}];
	}
	return buttons;
};

/*var IndexOfValue = function(dataArray, value) {
	for ( var i = 0; i < dataArray.length; i++) {
		if (dataArray[i] == value)
			return i;
	}
	return 0;
};*/

/*var getDataIndexByExtreme = function(data, date) {
	var diffArray = [];
	for ( var i = 0; i < data.length; i++) {
		diffArray.push(Math.abs(data[i][0] - date));
	}
	var minValue = Math.min.apply(Math, diffArray);
	return IndexOfValue(diffArray, minValue);
};*/

/*var afterSetExtremes = function(e) {	
	var beginIndex = getDataIndexByExtreme(dayKData[0], e.min);
	var endIndex = getDataIndexByExtreme(dayKData[0], e.max);
	
	var dataSetToDisplay = [];
	var interval = Math.round((endIndex - beginIndex) / dataLengthToShow) + 1;
	for ( var j = 0; j < dayKData.length; j++) {
		dataSetToDisplay[j] = [];
		for ( var i = 0; i < dayKData[j].length; i += interval){
			dataSetToDisplay[j].push(dayKData[j][i]);
		}
	}
	 var series = chart.series; 
     $.each(series,function(index, serie){
              serie.setData(dataSetToDisplay[index-1], false);//第二个参数设置为false表示不会在每个点加载后都重新渲染
      });
     chart.redraw();
	
	for ( var i = 0; i < 8; i++)
		this.series[i].setData(dataSetToDisplay[i],false);
	chart.redraw();
};*/
 
//画k线图
var drawKChart = function(renderDivId, kType, data) {
	var kDataSet = [];
	if (kType == 'dayK') {
		kDataSet =  dayKData;
		//kDataSet = sparseDayKData(kDataSet);
	} else if (kType == 'weekK') {
		kDataSet = weekKData;
	} else if (kType == 'monthK') {
		kDataSet = monthKData;
	}
	var priceKData = kDataSet[0];
	var ma5kPriceData = kDataSet[1];
	var ma10kPriceData = kDataSet[2];
	var ma30kPriceData = kDataSet[3];
	var volumeData = kDataSet[4];
	var ma5kVolumeData = kDataSet[5];
	var ma10kVolumeData = kDataSet[6];
	var ma30kVolumeData = kDataSet[7];

	if (priceKData.length == 0) {
		$('#' + renderDivId).html('<span style="padding: 20px;color: $cicc-red;font-size: 20px;">抱歉，暂无数据。</span>');
		return;
	}

	var kChartOption = {
		chart : {
			plotBorderColor : '#868686',
			plotBorderWidth : 0.3,
			zoomType : 'x',
			renderTo : renderDivId,
			height : 420
			//width : 550
		},
		rangeSelector: {
            inputEnabled: false,
            buttons: zoomButton(kType),
            selected: 1
        },
		scrollbar : {
			liveRedraw : false,
			enabled : false
		},
		navigator : {
			enabled:false
		    //adaptToUpdatedData: false,
		    /*xAxis : {
				dateTimeLabelFormats : {
					second : '%y-%m-%d %H:%M:%S',
					minute : '%y-%m-%d %H:%M',
					hour : '%y-%m-%d %H:%M',
					day : '%Y-%m',
					week : '%Y-%m',
					month : '%Y',
					year : '%Y'
				}
			}*/
		},
		xAxis : {
			ordinal :true ,
			tickPixelInterval : 200,
			minRange : kType == 'monthK' ? 12 * 30 * 24 * 3600 * 1000 :  12 * 24 * 3600 * 1000,
			type : 'datetime',
			gridLineWidth : 0,
			dateTimeLabelFormats : {
				second : '%y-%m-%d %H:%M:%S',
				minute : '%y-%m-%d %H:%M',
				hour : '%y-%m-%d %H:%M',
				day : '%Y/%m/%d',
				week : '%Y/%m',
				month : '%Y/%m',
				year : '%Y'
			},
			labels : {
				maxStaggerLines : 1,
				align : 'center',
				overflow : false
			}/*,
			events : {
				afterSetExtremes : afterSetExtremes
		    }*/
		},
		credits : {
			enabled : false
		},
		title : {
			align : 'left',
			text : ''
		},
		yAxis : [{
			showFirstLabel : true,
			showLastLabel : true,
			height : '65%',
			alternateGridColor : '#F7F7F7',
			lineWidth : 0.3,
			opposite : false,
			labels : {
				align : 'right',
				x : -5,
				y : 5,
				formatter : function() {
					return this.value.toFixed(2);
				}
			}
		}, {
			showFirstLabel : true,
			showLastLabel : true,
			height : '30%',
			alternateGridColor : '#F7F7F7',
			top : '70%',
			offset : 0,
			lineWidth : 0.3,
			opposite : false,
			labels : {
				align : 'right',
				x : -5,
				y : 5,
				formatter : function() {
					return bigDataFormat(this.value, 0);
				}
			}
		}],
		tooltip : {
			crosshairs : {
				width : 1,
				color : '#640000',
				dashStyle : 'shortdot'
			},
			borderColor : '#be8c4b',
			pointFormat : ' | <span style="color:{this.points[1].color}">{MA5}</span>: <b>{this.points[1].y}</b>',
			valueDecimals : 2,
			useHTML : true,
			formatter : function() {
				var s = '<b>' + Highcharts.dateFormat('%Y-%m-%d', this.x + 24*3600*1000) + '</b><br/>';
				if (typeof (this.points) == "undefined" || this.points.length < 8)
					return;
				s += '开盘价：' + this.points[0].point.open.toFixed(2);
				s += '<br/>最高价：' + this.points[0].point.high.toFixed(2);
				s += '<br/>最低价：' + this.points[0].point.low.toFixed(2);
				s += '<br/>收盘价：' + this.points[0].point.close.toFixed(2);
				s += '<br/><font color="' + this.points[1].series.color + '">MA5  ：&nbsp;' + (Math.round(this.points[1].y * 100) / 100).toFixed(2) + '</font>';
				s += '<br/><font color="' + this.points[2].series.color + '">MA10：' + (Math.round(this.points[2].y * 100) / 100).toFixed(2) + '</font>';
				s += '<br/><font color="' + this.points[3].series.color + '">MA30：' + (Math.round(this.points[3].y * 100) / 100).toFixed(2) + '</font>';
				s += '<br/>--------------';
				s += '<br/>成交量：' + bigDataFormat(this.points[4].y, 2);
				s += '<br/><font color="' + this.points[5].series.color + '">MA5  ：&nbsp;' + bigDataFormat(this.points[5].y, 2) + '</font>';
				s += '<br/><font color="' + this.points[6].series.color + '">MA10：' + bigDataFormat(this.points[6].y, 2) + '</font>';
				s += '<br/><font color="' + this.points[7].series.color + '">' + 'MA30：' + bigDataFormat(this.points[7].y, 2) + '</font>';
				return s;
			}
		},
		exporting : {
			enabled : false
		},
		plotOptions : {
			candlestick : {
				color : 'green',
				upColor : 'red',
				lineColor : 'green',
				upLineColor : 'red',
				dataGrouping : {
					enabled : false
				}
			},
			column : {
				colorByPoint : false,
				dataGrouping : {
					enabled : false
				}
			},
			spline : {
				dataGrouping : {
					enabled : false
				}
			}
		},
		series : [ {
			name : 'OHLC',
			type : 'candlestick',
			data : priceKData,
			dataGrouping: {
                enabled: false
            }
		}, {
			name : 'MA5',
			data : ma5kPriceData,
			type : 'spline',
			color : '#be8c4b',
			threshold : null,
			lineWidth:1,
			tooltip : {
				valueDecimals : 2
			},
			dataGrouping: {
                enabled: false
            }
		}, {
			name : 'MA10',
			data : ma10kPriceData,
			type : 'spline',
			color : '#640000',
			threshold : null,
			lineWidth:1,
			tooltip : {
				valueDecimals : 2
			},
			dataGrouping: {
                enabled: false
            }
		}, {
			name : 'MA30',
			data : ma30kPriceData,
			type : 'spline',
			color : '#7f7f7f',
			threshold : null,
			lineWidth:1,
			tooltip : {
				valueDecimals : 2
			},
			dataGrouping: {
                enabled: false
            }
		}, {
			type : 'column',
			name : 'Volume',
			data : volumeData,
			yAxis : 1,
			dataGrouping: {
                enabled: false
            }
		}, {
			name : 'MA5',
			data : ma5kVolumeData,
			type : 'spline',
			color : '#be8c4b',
			yAxis : 1,
			threshold : null,
			lineWidth:1,
			tooltip : {
				valueDecimals : 2
			},
			dataGrouping: {
                enabled: false
            }
		}, {
			name : 'MA10',
			data : ma10kVolumeData,
			type : 'spline',
			color : '#640000',
			yAxis : 1,
			threshold : null,
			lineWidth:1,
			tooltip : {
				valueDecimals : 2
			},
			dataGrouping: {
                enabled: false
            }
		}, {
			name : 'MA30',
			data : ma30kVolumeData,
			type : 'spline',
			color : '#7f7f7f',
			yAxis : 1,
			threshold : null,
			lineWidth:1,
			tooltip : {
				valueDecimals : 2
			},
			dataGrouping: {
                enabled: false
            }
		} ]
	};
	chart = new Highcharts.StockChart(kChartOption);
};