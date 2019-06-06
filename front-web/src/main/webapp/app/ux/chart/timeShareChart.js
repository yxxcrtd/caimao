var todayTime = new Date();
var timeShareRefreshFunction = null;
var timeShareUpdateInternal = 30000; // 30 seconds
var timeShareLastPrice = null;

// 获取时间戳
var getTimeByIndex = function(index) {
	return new Date(todayTime.getFullYear(), todayTime.getMonth(), todayTime
			.getDate(), 17, 0, 0, 0).getTime() + index * 60 * 1000;
};

// 处理数据显示单位
var bigDataFormat = function(data, fixedNumber) {
	if (data >= 10000 && data < 100000000)
		return (data / 10000).toFixed(fixedNumber).toString() + '万元';
	else if (data >= 100000000)
		return (data / 100000000).toFixed(fixedNumber).toString() + '亿元';
	else
		return data + '元';
};

// 数组数据比较大小
var finder = function(cmp, arr, attr) {
	var val = arr[0][attr];
	for ( var i = 1; i < arr.length; i++) {
		val = cmp(val, arr[i][attr]);
	}
	return val;
};

// 画分时空图
var drawEmptyChart = function(renderDivId) {
	var chartHeight = 240;
	var priceChartHeight = 200;
	var timeSharePriceData = [];
	var timeShareAveragePriceData = [];
	var timeShareVolumeData = [];
	timeShareLastPrice = null;
	
	// 获取的时间戳集合：每天的17:00 ~21:00
	for ( var i = 0; i < 241; i++) {
		timeSharePriceData.push([ getTimeByIndex(i), 0 ]);
		timeShareAveragePriceData.push([ getTimeByIndex(i), 0 ]);
		timeShareVolumeData.push([ getTimeByIndex(i), -1 ]);
	};

	var lastClosePrice = 0.5;
	var timeShareOption = {
		chart : {
			plotBorderColor : '#868686',
			plotBorderWidth : 1.2,
			renderTo : renderDivId,
			height : chartHeight
		},
		title : {
			text : ' ',
			align : 'left'
		},
		rangeSelector : {
			enabled : false
		},
		navigator : {
			enabled : false
		},
		scrollbar : {
			enabled : false
		},
		exporting : {
			enabled : false
		},
		credits : {
			enabled : false
		},
		xAxis : {
			gridLineWidth : 1,
			showFirstLabel : true,
			showLastLabel : true,
			ordinal : false,
			type : 'datetime',
			labels : {
				formatter : function() {
					var hour = parseInt(Highcharts.dateFormat('%H', this.value));
					var minute = parseInt(Highcharts.dateFormat('%M',this.value));
					var xLabel = hour.toString() + ':' + (minute < 10 ? ('0' + minute.toString()) : minute.toString());
					if (hour == 9 && minute == 0) {
						xLabel = '9:30';
					} else if (hour == 10 && minute == 0) {
						xLabel = '10:30';
					} else if (hour == 11 && minute == 0) {
						xLabel = '11:30/13:00';
					} else if (hour == 12 && minute == 0) {
						xLabel = '14:00';
					} else if (hour == 13 && minute == 0) {
						xLabel = '15:00';
					}
					return xLabel;
				}
			},
			min : new Date(todayTime.getFullYear(), todayTime.getMonth(),
					todayTime.getDate(), 17, 0, 0, 0).getTime(),
			max : new Date(todayTime.getFullYear(), todayTime.getMonth(),
					todayTime.getDate(), 21, 0, 0, 0).getTime()
		},
		yAxis : [{
			showFirstLabel : true,
			showLastLabel : true,
			startOnTick : true,
			endOnTick : true,
			ordinal : false,
			height : priceChartHeight,
			lineWidth : 1,
			gridLineDashStyle : 'shortdot  ',
			opposite : false,
			labels : {
				align : 'right',
				x : -5,
				y : 5,
				useHTML : true,
				formatter : function() {
					var color = (this.value > lastClosePrice ? '#DD2200' : '#33AA11');
					color = (this.value == lastClosePrice ? '#000000' : color);
					return '<font color="' + color + '">' + (this.value).toFixed(2) + '</font>';
				}
			},
			min : 0,
			max : 1,
			plotLines : [ {
				color : '#000000',
				width : 2,
				dashStyle : 'shortdot',
				value : 0.5
			} ]
		},{
			showFirstLabel : true,
			showLastLabel : true,
			startOnTick : true,
			endOnTick : true,
			ordinal : false,
			height : priceChartHeight,
			lineWidth : 1,
			opposite : true,
			labels : {
				align : 'left',
				x : 5,
				y : 5,
				useHTML : true,
				formatter : function() {
					var color = (this.value > 0 ? '#DD2200' : '#33AA11');
					color = (this.value == 0 ? '#000000' : color);
					return '<font color="' + color + '">' + (this.value * 100).toFixed(2) + '%' + '</font>';
				}
			},
			min : -1,
			max : 1
		},{
			id : 'volume-yaxis',
			showFirstLabel : true,
			showLastLabel : true,
			ordinal : false,
			height : 100,
			top : 240,
			offset : 0,
			lineWidth : 1,
			labels : {
				align : 'right',
				x : -5,
				y : 5,
				formatter : function() {
					return bigDataFormat(this.value, 0);
				}
			},
			min : 0
		} ],
		plotOptions : {
			column : {
				dataGrouping : {
					enabled : false
				}
			},
			area : {
				dataGrouping : {
					enabled : false
				}
			},
			spline : {
				lineWidth : 1,
				dataGrouping : {
					enabled : false
				}
			},
			line : {
				lineWidth : 1,
				dataGrouping : {
					enabled : false
				}
			}
		},
		tooltip : {
			enabled : false
		},
		series : [ {
			name : 'newPrice',
			type : 'area',
			data : timeSharePriceData,
			color : '#be8c4b',
			gapSize : Number.MAX_VALUE,
			threshold : null
		}, {
			name : 'averagePrice',
			type : 'line',
			color : '#640000',
			data : timeShareAveragePriceData,
			yAxis : 0,
			threshold : null
		}, {
			name : 'volume',
			type : 'column',
			data : timeShareVolumeData,
			yAxis : 2
		} ]
	};
	chart = new Highcharts.StockChart(timeShareOption);
};

// 画分时图
var drawTimeShareChart = function(renderDivId, stockCode, url) {
	//置空
	timeShareLastPrice = null; 
	// 重置自动刷新
	if (timeShareRefreshFunction != null) {
		clearInterval(timeShareRefreshFunction);
		timeShareRefreshFunction = null;
	}
	// 无stockCode，画空图
	if (typeof (url) == "undefined" || url == null) {
		if (typeof (chart) != "undefined" && chart != null)
			while (chart.series.length > 0)
				chart.series[0].remove(true);
		else
			drawEmptyChart(renderDivId);
		return;
	}
	var chartHeight = 240;
	var priceChartHeight = 200;
	$.ajax({
		url : url,
		type : 'GET',
		success : function(res) {
			if (res.error != null) {
				return;
			}
			var timeSharePriceData = [];
			var timeShareAveragePriceData = [];
			var timeShareVolumeData = [];
			var newPointIndex = 0;
			var lastClosePrice = 0;
			var yAxis0Internal = 0;
			
			if(res['success'] == false){
				drawEmptyChart(renderDivId);
				return;
			}

			resp = res['data'];
			if (resp['trendList'] != null&& typeof (resp['trendList']) != undefined&& resp['trendList'].length > 0) {
				lastClosePrice = parseFloat(resp['partRealTimeData']['prevClosePrice']);
				if (timeSharePriceData.length >= resp['trendList'].length) {
					return;
				} else {
					var newData = resp['trendList'];
					for ( var i = 0; i < newData.length; i++) {						
						if (parseFloat(newData[i].total) < 0.0){
							break;
						}else if(parseFloat(newData[i].newPrice) == 0.0){
							if(timeShareLastPrice == null){
								if(parseFloat(resp['partRealTimeData'].openPrice) != 0.0){
									timeShareLastPrice = resp['partRealTimeData'].openPrice;
								}else{
									timeShareLastPrice = resp['partRealTimeData'].prevClosePrice;
								}							
							}
							var nextIndex = timeSharePriceData.length;
							timeSharePriceData.push([getTimeByIndex(nextIndex),parseFloat(timeShareLastPrice) ]);
							timeShareAveragePriceData.push([getTimeByIndex(nextIndex),parseFloat(newData[i].averagePrice) ]);
							timeShareVolumeData.push([getTimeByIndex(nextIndex),parseInt(newData[i].total) ]);
						}else{
							var nextIndex = timeSharePriceData.length;
							timeSharePriceData.push([getTimeByIndex(nextIndex),parseFloat(newData[i].newPrice) ]);
							timeShareAveragePriceData.push([getTimeByIndex(nextIndex),parseFloat(newData[i].averagePrice) ]);
							timeShareVolumeData.push([getTimeByIndex(nextIndex),parseInt(newData[i].total) ]);
						}
						if(parseFloat(newData[i].newPrice) != 0.0){
							timeShareLastPrice = newData[i].newPrice;
						}
					}
					newPointIndex = timeSharePriceData.length;
					if (newPointIndex == 0)
						return;
					var currentMax = Math.max(parseFloat(resp['partRealTimeData']['maxPrice']),finder(Math.max,timeShareAveragePriceData,1));
					var currentMin = Math.min(parseFloat(resp['partRealTimeData']['minPrice']),finder(Math.min,timeShareAveragePriceData,1));
					yAxis0Internal = (Math.abs(lastClosePrice- currentMax) > Math.abs(lastClosePrice- currentMin))? Math.abs(lastClosePrice- currentMax) : Math.abs(lastClosePrice - currentMin);
				}
			} else {
				return;
			}

			var timeShareOption = {
				chart : {
					plotBorderColor : '#868686',
					plotBorderWidth : 1.2,
					renderTo : renderDivId,
					height : chartHeight,
					// width: 550,
					events : {
						load : function() {
							var newPriceSeries = this.series[0];
							var averagePriceSeries = this.series[1];
							var yaxis0 = this.yAxis[0];
							var yaxis1 = this.yAxis[1];
							if (todayTime.getHours() >= 15)
								return;
							if (url == null || typeof (url) == "undefined")
								return;
							timeShareRefreshFunction = setInterval(function() {
								$.ajax({
									url : url,
									type : 'GET',
									success : function(res) {
										if (res.error != null) {
										} else {
											resp = res['data'];
											if (resp['trendList'] != null&& typeof (resp['trendList']) != undefined&& resp['trendList'].length > 0) {
												if (timeSharePriceData.length >= resp['trendList'].length) {
													return;
												} else {
													var newData = resp['trendList'].slice(newPointIndex);
													if (newData.length >= 1)
														for ( var i = 0; i < newData.length; i++) {
															if (parseFloat(newData[i].total) < 0.0 || Math.abs(parseInt(newData[i].chargeRate)) >= 1){
																return;
															}else if(parseFloat(newData[i].newPrice) == 0.0){
																if(timeShareLastPrice == null){
																	if(parseFloat(resp['partRealTimeData'].openPrice) != 0.0){
																		timeShareLastPrice = resp['partRealTimeData'].openPrice;
																	}else{
																		timeShareLastPrice = resp['partRealTimeData'].prevClosePrice;
																	}							
																}
																newPriceSeries.addPoint([getTimeByIndex(newPointIndex),parseFloat(timeShareLastPrice) ],true,false);
																averagePriceSeries.addPoint([getTimeByIndex(newPointIndex),parseFloat(newData[i].averagePrice) ],true,false);
																newPointIndex++;
															}else{
																newPriceSeries.addPoint([getTimeByIndex(newPointIndex),parseFloat(newData[i].newPrice) ],true,false);
																averagePriceSeries.addPoint([getTimeByIndex(newPointIndex),parseFloat(newData[i].averagePrice) ],true,false);
																newPointIndex++;
															}
															if(newData[i].newPrice != 0.0){
																timeShareLastPrice = newData[i].newPrice;
															}															 
														}
													var currentMax = Math.max(parseFloat(resp['partRealTimeData']['maxPrice']),finder(Math.max,averagePriceSeries.data,'y'));
													var currentMin = Math.min(parseFloat(resp['partRealTimeData']['minPrice']),finder(Math.min,averagePriceSeries.data,'y'));
													yAxis0Internal = ((Math.abs(lastClosePrice- currentMax) > Math.abs(lastClosePrice - currentMin)) ? Math.abs(lastClosePrice- currentMax): Math.abs(lastClosePrice- currentMin));
													yaxis0.update({
														tickPositioner : function() {
															var positions = [], min = lastClosePrice- yAxis0Internal, increment = yAxis0Internal / 2;
															for ( var i = 0; i <= 4; i++) {
																positions.push(min + increment* i);
															}
															return positions;
														}
													});
													yaxis1.update({
														tickPositioner : function() {
															var positions = [], min = -(yAxis0Internal)/ lastClosePrice, increment = yAxis0Internal/ (2 * lastClosePrice);
															for ( var i = 0; i <= 4; i++) {
																positions.push(min+ increment * i);
															}
															return positions;
														}
													});
												}
											}
										}
									},
									error : function() {
									}
								});
							}, timeShareUpdateInternal);
						}
					}
				},
				title : {
					text : ' ',
					align : 'left'
				},
				rangeSelector : {
					enabled : false
				},
				navigator : {
					enabled : false
				},
				scrollbar : {
					enabled : false
				},
				exporting : {
					enabled : false
				},
				credits : {
					enabled : false
				},
				xAxis : {
					gridLineWidth : 1,
					showFirstLabel : true,
					showLastLabel : true,
					ordinal : false,
					type : 'datetime',
					labels : {
						formatter : function() {
							var hour = parseInt(Highcharts.dateFormat('%H', this.value));
							var minute = parseInt(Highcharts.dateFormat('%M', this.value));
							var xLabel = hour.toString() + ':' + (minute < 10 ? ('0' + minute.toString()) : minute.toString());
							if (hour == 9 && minute == 0) {
								xLabel = '9:30';
							} else if (hour == 10 && minute == 0) {
								xLabel = '10:30';
							} else if (hour == 11 && minute == 0) {
								xLabel = '11:30/13:00';
							} else if (hour == 12 && minute == 0) {
								xLabel = '14:00';
							} else if (hour == 13 && minute == 0) {
								xLabel = '15:00';
							}
							return xLabel;
						}
					},
					min : new Date(todayTime.getFullYear(), todayTime.getMonth(), todayTime.getDate(), 17, 0, 0,0).getTime(),
					max : new Date(todayTime.getFullYear(), todayTime.getMonth(), todayTime.getDate(), 21, 0, 0,0).getTime()
				},
				yAxis : [{
					maxPadding : 0.05,
					minPadding : 0.05,
					showFirstLabel : true,
					showLastLabel : true,
					startOnTick : true,
					endOnTick : true,
					ordinal : false,
					height : priceChartHeight,
					lineWidth : 1,
					gridLineDashStyle : 'shortdot  ',
					opposite : false,
					labels : {
						align : 'right',
						x : -5,
						y : 5,
						useHTML : true,
						formatter : function() {
							var color = (this.value > lastClosePrice ? '#DD2200' : '#33AA11');
							color = (this.value == lastClosePrice ? '#000000' : color);
							return '<font color="' + color + '">' + (this.value).toFixed(2) + '</font>';
						}
					},
					tickPositioner : function() {
						var positions = [], min = lastClosePrice - yAxis0Internal, increment = yAxis0Internal / 2;
						for ( var i = 0; i <= 4; i++) {
							positions.push(min + increment * i);
						}
						return positions;
					},
					plotLines : [ {
						color : '#000000',
						width : 2,
						dashStyle : 'shortdot',
						value : lastClosePrice
					} ]
				},{
					maxPadding : 0.05,
					minPadding : 0.05,
					showFirstLabel : true,
					showLastLabel : true,
					ordinal : true,
					height : priceChartHeight,
					lineWidth : 1,
					opposite : true,
					labels : {
						align : 'left',
						x : 5,
						y : 5,
						useHTML : true,
						formatter : function() {
							var color = (this.value > 0 ? '#DD2200' : '#33AA11');
							color = (this.value == 0 ? '#000000' : color);
							return '<font color="' + color + '">' + (this.value * 100).toFixed(2) + '%' + '</font>';
						}
					},
					tickPositioner : function() {
						var positions = [], min = -(yAxis0Internal) / lastClosePrice, increment = yAxis0Internal / (2 * lastClosePrice);
						for ( var i = 0; i <= 4; i++) {
							positions.push(min + increment * i);
						}
						return positions;
					}
				}, {
					id : 'volume-yaxis',
					showFirstLabel : true,
					showLastLabel : true,
					height : 100,
					top : 280,
					offset : 0,
					lineWidth : 1,
					labels : {
						align : 'right',
						x : -5,
						y : 5,
						formatter : function() {
							return this.value.toFixed(0);
						}
					},
					min : 0
				} ],
			    plotOptions : {
			    	column : {
			    		dataGrouping : {
			    			enabled : false
				    	}
			    	},
					area : {
						dataGrouping : {
							enabled : false
						}
					},
					spline : {
						lineWidth : 1,
						dataGrouping : {
							enabled : false
						}
					},
					line : {
						lineWidth : 1,
						dataGrouping : {
							enabled : false
						}
					}
			    },
				tooltip : {
					crosshairs : {
						width : 1,
						color : '#640000',
						dashStyle : 'shortdot'
					},
					borderColor : '#be8c4b',
					valueDecimals : 2,
					useHTML : true,
					formatter : function() {
						var timeLag = ((parseInt(Highcharts.dateFormat('%H', this.x)) == 11 && parseInt(Highcharts.dateFormat('%M', this.x)) > 0) 
								|| parseInt(Highcharts.dateFormat('%H', this.x)) > 11) ? 119 * 60 * 1000 : 30 * 60 * 1000;
						var text = '<b>' + Highcharts.dateFormat('%m-%d %H:%M',this.x + timeLag) + '</b>';
						var index = 0;// this.xData.indexOf(this.x);
						var priceColor = (index == 0) ? '#000000' : ((this.series[0].yData[index - 1] <= this.points[0].y) ? '#DD2200' : '#33AA11');
						var apColor = (index == 0) ? '#000000' : ((this.series[1].yData[index - 1] <= this.points[1].y) ? '#DD2200' : '#33AA11');
						var changeRate = (this.points[0].y - lastClosePrice) / lastClosePrice;
						var crColor = ((changeRate > 0) ? '#DD2200' : '#33AA11');
						text += '<br/><font color="' + this.points[0].series.color + '">最新价：</font>';
						text += '<font color="' + priceColor + '">' + (this.points[0].y).toFixed(2) + '</font>';
						text += '<br/><font color="' + this.points[1].series.color + '">' + ((stockCode == '1A0001' || stockCode == '2A01') ? '领先：' : '均价：') + '</font>';
						text += '<font color="' + apColor + '">' + (this.points[1].y).toFixed(2) + '</font>';
						text += '<br/><font color="' + '#000000' + '">涨幅：</font>';
						text += '<font color="' + crColor + '">' + (Math.round(changeRate * 10000) / 100).toFixed(2) + '%' + '</font>';
						return text;
					}
				},
				series : [{
					name : 'newPrice',
					type : 'area',// 'area',
					data : timeSharePriceData,
					color : '#be8c4b',
					gapSize : Number.MAX_VALUE,
					fillColor : {
						linearGradient : {
							x1 : 0,
							y1 : 0,
							x2 : 0,
							y2 : 1
						},
						stops : [ [ 0, '#be8c4b' ],[ 1, 'rgba(0,0,0,0)' ] ]
					},
					threshold : null
				}, {
					name : 'averagePrice',
					type : 'line',
					color : '#640000',
					data : timeShareAveragePriceData,
					yAxis : 0,
					threshold : null
				},{
					name : 'volume',
					type : 'column',
					data : timeShareVolumeData,
					yAxis : 2
				}, {
					name : 'chargeRate',
					type : 'spline',
					color : '#7f7f7f',
					// data: timeShareChargeRateData,
					yAxis : 1,
					thresold : null
				}]
			};
			chart = new Highcharts.StockChart(timeShareOption);
			if (chart.series != null && typeof (chart.series) != undefined && chart.series.length >= 3) {
				chart.series[2].remove();
			}
		},
		error : function() {
		}
	});
};
