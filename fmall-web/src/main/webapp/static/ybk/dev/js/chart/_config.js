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
			"xhr": "ybk.caimao.com/api/ybk/"
			//"xhr": "ybk.caimao.com/api/ybk/"
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
				"riseStyle": "0", //阳线样式
				"fall": "#6cbf0a", //阴线颜色
				"fallStyle": "1", //阴线样式
				"riseFont" : "#eb4636",//上涨文字颜色
				"fallFont" : "#6cbf0a",//下跌文字颜色
				"timeVolRise" : "#eb4636",
				"timeVolFall" : "#6cbf0a",
				"timeVolStable" : "#5588cc",
				"timeShareShadow" : 1,
				"specialLine" : 0,
				"shadowFill" : "rgba(71, 169, 238, 0.15)",
				"MACDrise" : "#eb4636",
				"MACDfall" : "#6cbf0a" ,
				"crossLine": "#ccc", //工具线颜色
				"layoutColor": "#dcdbdb", //分割线颜色
				"layoutFont": "#999", //布局文字颜色
				"fontColor": "#999", //文字颜色
				"iLines": ["#5588cc", "#fcc83b", "#ff80b6", "#00e600", "#02e2f4", "#ffffb9", "#2c684d", "#ff3232"], //指标线依次颜色
				"background": "#fff" //背景颜色
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
		"hasInd" : {
			"1day" : 1,
			"1week" : 1,
			"1month" : 1,
			"1min": 1,
			"5min": 1,
			"15min": 1,
			"30min": 1,
			"1hour": 1
		},
		"hasKma" : {
			"1day" : 1,
			"1week" : 1,
			"1month" : 1,
			"1min": 1,
			"5min": 1,
			"15min": 1,
			"30min": 1,
			"1hour": 1
		},
		"user": {
			"showInfo": 1,
			"double" : 1,
			"dataVersion": '0.1',
			"popstop" : 0,
			"ctrlVer": new Date()*1,
			"paddingRight" : 15,
			"outRuleWidth" : 414,
			"binary" : 10000,
			"symbol": 0, //品类
			"schemes": "mobile", //主题
			/***绘图区上偏移***/
			"offsetTop": 20,
			/***标尺最小高度***/
			"ruleWidth": 60,
			/***副图高度***/
			"viceHeight": 30 / 100,
			/***指标高度***/
			"indicatorHeight": 0 / 100,
			/***分时图高度***/
			"timeHeight": 2 / 3,
			/***蜡烛尺寸***/
			"level": 3,
			/***分时周期***/
			"period": '1day', //
			/***主要指标['MA']***/
			"indicator": 'MA',
			/***副指标['MACD','KDJ','RSI','WR']***/
			"vIndicator": 'MACD',
			"drawRange": 80 / 100, //绘图区间
			"minRuleHeight": 75, //标尺间距
			"vMinRuleHeight": 40, //副图标尺间距
			"timeLineMinRuleHeight": 30, //副图标尺间距
			"timeline": 0, //时间线高度
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