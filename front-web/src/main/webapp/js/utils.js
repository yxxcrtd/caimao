var rentmplCache = [];
CMUTILS = {
	/**
	 * 把阿拉伯数字转为中文大写
	 * @param num
	 * @returns {string}
	 */
	getChangeNum:function(num){
		var strOutput = "", strUnit = '仟佰拾亿仟佰拾万仟佰拾元角分';
		num += "00";
		var intPos = num.indexOf('.');
		if (intPos >= 0) {
			num = num.substring(0, intPos) + num.substr(intPos + 1, 2);
		}
		strUnit = strUnit.substr(strUnit.length - num.length);
		for (var i = 0; i < num.length; i++) {
			strOutput += '零壹贰叁肆伍陆柒捌玖'.substr(num.substr(i, 1), 1)
			+ strUnit.substr(i, 1);
		}
		return strOutput.replace(/零角零分$/, '整').replace(/零[仟佰拾]/g, '零').replace(
			/零{2,}/g, '零').replace(/零([亿|万])/g, '$1').replace(/零+元/, '元')
			.replace(/亿零{0,3}万/, '亿').replace(/^元/, "零元")
	},

	/**
	 * 把数字转换为货币形式
	 * @param nStr
	 * @returns {*}
	 */
	addCommas:function (nStr) {
		nStr += '';
		x = nStr.split('.');
		x1 = x[0];
		x2 = x.length > 1 ? '.' + x[1] : '';
		var rgx = /(\d+)(\d{3})/;
		while (rgx.test(x1)) {
			x1 = x1.replace(rgx, '$1' + ',' + '$2');
		}
		return x1 + x2;
	},

	/**
	 * 把分格式的数字转换为元为单位的货币显示
	 * @param nStr
	 * @returns {*}
	 */
	toYuanAndCommas:function (nStr) {
		return CMUTILS.addCommas(parseInt(nStr)/100);
	},

	/**
	 * 把货币形式转为数字
	 * @param nStr
	 * @returns {*}
	 */
	removeCommas:function(nStr){
		return nStr.replace(/,/g,"");
	},

	toFenRemoveCommas:function(nStr){
		return parseInt(CMUTILS.removeCommas(nStr))*100;
	},

	toFloat:function(num){
		num = num.replace(/[^\d.]/g,'');
		num = num.replace(/^\.{1,}/g,"");
		num = num.replace(/^0{1,}/g,"0");
		num = num.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
		return num;
	},

	/**
	 * 日期格式化
	 * @param date
	 * @param fmt
	 * @returns {*}
	 */
	dateFormat:function(date,fmt){
		date = new Date(date);
		var o = {
			"M+": date.getMonth() + 1, //月份
			"d+": date.getDate(), //日
			"h+": date.getHours(), //小时
			"m+": date.getMinutes(), //分
			"s+": date.getSeconds(), //秒
			"q+": Math.floor((date.getMonth() + 3) / 3), //季度
			"S": date.getMilliseconds() //毫秒
		};
		if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
		for (var k in o)
			if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		return fmt;
	},

	/**
	 * 格式化时间为yyyy-MM-dd
	 * @param date
	 */
	getDate:function(date){
		return CMUTILS.dateFormat(date,'yyyy-MM-dd')
	},

	/**
	 * 格式化时间为yyyy-MM-dd HH:mm:ss
	 * @param date
	 */
	getDateTime:function(date){
		return CMUTILS.dateFormat(date,"yyyy-MM-dd HH:mm:ss");
	},

	/**
	 * 月份加减
	 * @param date
	 * @param value
	 * @returns {*}
	 */
	addMonths:function(date, value){
		date.setMonth(date.getMonth() + value);
		return date;
	},

	/**
	 * 月份加减
	 * @param date
	 * @param value
	 * @returns {*}
	 */
	addDays:function(date, value){
		date.setDate(date.getDate() + value);
		return date;
	},

	/**
	 * 日期加减
	 * @param date
	 * @param interval
	 * @param amount
	 * @returns {Date}
	 */
	dateAdd:function(date, interval, amount){
		var sum = new Date(date);
		var fixOvershoot = false;
		var property = "Date";

		switch(interval){
			case "day":
				break;
			case "weekday":
				var days, weeks;
				var mod = amount % 5;
				if(!mod){
					days = (amount > 0) ? 5 : -5;
					weeks = (amount > 0) ? ((amount-5)/5) : ((amount+5)/5);
				}else{
					days = mod;
					weeks = parseInt(amount/5);
				}
				var strt = date.getDay();
				var adj = 0;
				if(strt == 6 && amount > 0){
					adj = 1;
				}else if(strt == 0 && amount < 0){
					adj = -1;
				}
				var trgt = strt + days;
				if(trgt == 0 || trgt == 6){
					adj = (amount > 0) ? 2 : -2;
				}
				amount = (7 * weeks) + days + adj;
				break;
			case "year":
				property = "FullYear";
				fixOvershoot = true;
				break;
			case "week":
				amount *= 7;
				break;
			case "quarter":
				amount *= 3;
				break;
			case "month":
				fixOvershoot = true;
				property = "Month";
				break;
			default:
				property = "UTC"+interval.charAt(0).toUpperCase() + interval.substring(1) + "s";
		}


		if(property){
			sum["set"+property](sum["get"+property]()+parseInt(amount));
		}

		if(fixOvershoot && (sum.getDate() < new Date(date).getDate())){
			sum.setDate(0);
		}
		return sum;
	},
	
	rentmpl:function (str, data) {
        var fn = !/\W/.test(str) ?
            rentmplCache[str] = rentmplCache[str] ||
            CMUTILS.rentmpl(document.getElementById(str).innerHTML) :
            new Function("obj",
                "var p=[];" +
                "with(obj){p.push('" +
                str
                    .replace(/\\/g, "\\\\")
                    .replace(/[\r\t\n]/g, " ")
                    .split("<%").join("\t")
                    .replace(/((^|%>)[^\t]*)'/g, "$1\r")
                    .replace(/\t=(.*?)%>/g, "',$1,'")
                    .split("\t").join("');")
                    .split("%>").join("p.push('")
                    .split("\r").join("\\'") + "');}return p.join('');");
        return data ? fn(data) : fn;
    }
};
