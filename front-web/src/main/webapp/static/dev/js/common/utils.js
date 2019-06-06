// @koala-prepend "./init.js"

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
     * 把分的格式数字转出成万的单位显示
     * @param nStr
     * @returns {*}
     */
    toWanAndCommas:function (nStr) {
        return CMUTILS.addCommas(parseInt(nStr)/1000000);
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
        return CMUTILS.dateFormat(date,"yyyy-MM-dd hh:mm:ss");
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

    // 精确乘法计算  http://blog.sina.com.cn/s/blog_53edf7c10100wlcw.html
    accMul : function (arg1, arg2) {
        var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
        try {m += s1.split(".")[1].length;} catch (e) {}
        try {m += s2.split(".")[1].length;} catch (e) {}
        return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
    },

    // 精确加法计算
    accAdd : function (arg1, arg2){
        var r1,r2,m;
        try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
        try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
        m = Math.pow(10, Math.max(r1, r2));
        return (this.accMul(arg1,m) + this.accMul(arg2,m))/m;
    },

    // 精确减法计算
    accSub : function (arg1,arg2){
        var r1,r2,m,n;
        try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
        try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
        m=Math.pow(10,Math.max(r1,r2));
        n=(r1>=r2)?r1:r2;
        return ((this.accMul(arg1,m) - this.accMul(arg2,m))/m).toFixed(n);
    },

    //精确除法运算
    accDiv : function (arg1,arg2){
        var t1=0,t2=0,r1,r2;
        try{t1=arg1.toString().split(".")[1].length}catch(e){}
        try{t2=arg2.toString().split(".")[1].length}catch(e){}
        with(Math){
            r1=Number(arg1.toString().replace(".",""))
            r2=Number(arg2.toString().replace(".",""))
            return (r1/r2)*pow(10,t2-t1);
        }
    },

    /**
     * 模板替换的方法
     * @param str
     * @param data
     * @returns {*}
     */
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
    },
    formatPer:function(value){
        value = parseInt(value * 10000) / 100;
        return value.toFixed(2);
    },
    formatPerNum:function(value, num){
        value = value * 100;
        var tmpFix = Number(value + 1).toFixed(num);
        value = Number(tmpFix - 1).toFixed(num);
        return Number(value).toFixed(2);
    },
    formatTime:function (value){
        value = value + "";
        for(var i=value.length; i<6 ;i++){
            value = "0" + value;
        }
        return value.substring(0, 2) + ":" + value.substring(2, 4) + ":" + value.substring(4, 6);
    },
    fillZero: function(value, decNum) {
        if (typeof value === 'number') {
            value = value + '';
        }
        if (typeof decNum === 'undefined') {
            decNum = 2;
        }
        var pre = '0000000000000000000000000';
        var num = value.split('.');
        return decNum === 0 ? num[0] : num[0] + '.' + (num[1] || '') + pre.slice(0, decNum - (num[1] || '').length);
    },
    formatAmountUnit: function(value) {
        var num = value.split('.')[0];
        if (num.length >= 5) {
            value = this.roundAmount(parseFloat(value) / 10000, 6);
            return value + '万';
        } else {
            return value;
        }
    },
    formatAmountUnitCeil: function(value) {
        var num = value.split('.')[0];
        if (num.length >= 5) {
            value = parseInt(parseFloat(value) / 10000);
            return value + '万';
        } else {
            return value;
        }
    },
    // fix precision
    roundAmount: function(value, places) {
        if (typeof value === 'string') {
            value = parseFloat(value);
        }
        if (typeof places === 'number') {
            return this.round(value, places);
        }
        return this.round(value, 2);
    },
    round: function(value, places, increment){
        var wholeFigs = Math.log(Math.abs(value))/Math.log(10);
        var factor = 10 / (increment || 10);
        var delta = Math.pow(10, -15 + wholeFigs);
        return (factor * (+value + (value > 0 ? delta : -delta))).toFixed(places) / factor;
    },
    clearNoNum: function(value){
        value = value.replace(/[^\d.]/g,"");
        value = value.replace(/^\./g,"");
        value = value.replace(/\.{2,}/g,".");
        value = value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
        return value;
	},
    checkMobile : function(mobile){
    	var mobileReg = /^(((13[0-9]{1})|(14[567]{1})|(15[0-9]{1})|(17[0678]{1})|(18[0-9]{1}))+\d{8})$/;
    	return mobileReg.test(mobile);
    },
    formatNumToUnit:function(value){
        if(parseInt(parseFloat(value / 100000000).toFixed(5)) > 0){
            return parseFloat(value / 100000000).toFixed(2) + "亿";
        }else if(parseInt(parseFloat(value / 10000).toFixed(5)) > 0){
            return parseFloat(value / 10000).toFixed(2) + "万";
        }else{
            return parseFloat(value).toFixed(2);
        }
    },
    parseUrl:function () {
        var paramIndex = location.href.indexOf('?'),
            paramsObj = {};
        if (paramIndex > 0) {
            var params = location.href.slice(paramIndex + 1).split('&');
            for (var i = 0, len = params.length; i < len; i++) {
                var dict = params[i].split('=');
                paramsObj[dict[0]] = dict[1];
            }
        }
        return paramsObj;
    },
    /**
     * 返回密码的安全级别
     * @param s
     * @returns {number}
     */
    pwdStrong : function(password) {
        //var strongRegex = new RegExp("^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$", "g");
     	//var mediumRegex = new RegExp("^(?=.{7,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g");
     	//var enoughRegex = new RegExp("(?=.{6,}).*", "g");
        //if (false == enoughRegex.test(val)) {
        //    return 0;
        //} else if (strongRegex.test(val)) {
        //    return 3;
        //} else if (mediumRegex.test(val)) {
        //    return 2;
        //} else {
        //    return 1;
        //}
        //bitTotal函数
        //计算密码模式
        var bitTotal = function(num) {
            var modes = 0;
            for (var i = 0; i < 4; i++) {
                if (num & 1) modes++;
                num >>>= 1;
            }
            return modes;
        };
        //判断输入密码的类型
        var CharMode = function(character) {
            if (character >= 48 && character <= 57)
                return 1;
            if ((character >= 65 && character <= 90) || (character >= 97 && character <= 122))
                return 2;
            if ((character >= 32 && character <= 47) || (character >= 58 && character <= 64) || (character >= 94 && character <= 96) || (character >= 123 && character <= 126))
                return 4;
            else
                return 8;
        };
        if (password.length <= 0) {
            return 0; //密码太短
        }
        var modes = 0;
        for (var i = 0; i < password.length; i++) {
            //密码模式
            modes |= CharMode(password.charCodeAt(i));
        }
        return bitTotal(modes);
    },
    /**
     * 设置COOKIE
     * @param key   键名
     * @param value 键值
     * @param expire    有效天数
     * @param domain    作用域名
     * @param path      作用地址
     */
    setCookie : function(key, value, expire, domain, path){
        var cookie = "";
        if(key != null && value != null) {
            cookie += key + "=" + value + ";";
        }
        if(expire != null) {
            var expire_time = new Date(new Date().getTime() + expire * 60 * 60 * 1000);
            cookie += "expires=" + expire_time.toGMTString() + ";";
        }
        if(domain != null)
            cookie += "domain=" + domain + ";";
        if (typeof path == "undefined") path = "/";
        if(path != null)
            cookie += "path=" + path + ";";
        document.cookie = cookie;
    },
    /**
     * 获取COOKIE
     * @param key   键名
     * @returns {boolean}
     */
    getCookie : function(key) {
        var cookie = document.cookie;
        var cookieArray = cookie.split(';');
        var getvalue = '';
        for(var i = 0; i < cookieArray.length; i++) {
            if(cookieArray[i].trim().substr(0, key.length) == key) {
                getvalue = cookieArray[i].trim().substr(key.length + 1);
                break;
            }
        }
        return getvalue;
    }
};