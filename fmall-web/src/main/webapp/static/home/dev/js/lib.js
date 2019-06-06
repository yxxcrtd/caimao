function J() {
	var a = [].slice.apply(arguments);
	if (a.length < 2) return;
	return a.shift().apply(a.shift(), a);
};

function copy(){
	var a = [].slice.apply(arguments),obj;
	if (!a.length) return;
	obj = a.shift();
	if (!a.length) return obj;
	for(var i = 0, l = a.length; i <l; i++){
		if(a[i]){
			for(var k in a[i]){
				obj[k] = a[i][k];
			}
		}
	}
	return obj;
}
function $_GET(par){
	var arg;
	if(!window['getSearchParameters']){
		arg = location.search.replace("?","").split("&");
		window['getSearchParameters'] = {};
		for(var tmp,i = 0, l = arg.length; i < l; i++){
			tmp = arg[i].split("=");
			getSearchParameters[tmp[0]] = decodeURIComponent(tmp[1]);
		}
	}
	return getSearchParameters[par]
}
function toFormatStr() {
	var arg = [].slice.apply(arguments);
	return !arg.length ? '' : arg.shift().replace(/\{([^\}]+)\}/ig, function(k) {
		k = k.replace(/[\{\}]/g, '').split('||');
		return arg[k[0].replace(/\s/g, '')] || (k[1] ? k[1] : k[0]).replace(/(^\s+)|(\s+$)/g, "");
	});
}
function splitPoint(_this, n, debug) {
	var _this = _this * 1,
		i = scientific2float(_this).toString().split('.'),
		l;
	if (i.length < 2) {
		i[1] = Math.pow(10, n).toString().substring(1, n + 1);
	} else {
		l = scientific2float(i[1]).toString().length;
		i[1] = (scientific2float(scientific2float(i[1]) / Math.pow(10, l)).toString() + "00000000000000").substring(2, n + 2);
	}
	return i;
};

function cutFixed(_this, n, debug) {
	_this = scientific2float(_this);
	if (!n) return Math.floor(_this);
	return splitPoint(_this, n, debug).join('.');
};
function scientific2float(n) {
	var num = typeof n == 'string' ? n.toLowerCase().replace(/\s/g, '') : n.toString().toLowerCase().replace(/\s/g, ''),
		standby = '000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000',
		zf, pn, base, readyNum, offset, ext, len;
	if (/e/.test(num)) {
		pn = /\+/.test(num);
		base = num.split('e');
		zf = /\-/.test(base[0].toString()) ? '-0.' : '0.';
		base[0] = base[0].replace('-', '');
		offset = base[0].split('.')[1] ? base[0].split('.')[1].length : 0;
		ext = base[1];
		len = ext.split(pn ? '+' : '-')[1];
		base = base[0].replace('.', '');
		return pn ? base + standby.substring(0, len - offset) :
			zf + standby.substring(0, len - 1) + base;
	} else {
		return num;
	}
}

function accMul(arg1, arg2) {
	var m = 0,
		s1 = scientific2float(arg1),
		s2 = scientific2float(arg2);
	try {
		m += s1.split(".")[1].length;
	} catch (e) {}
	try {
		m += s2.split(".")[1].length;
	} catch (e) {}
	return scientific2float(s1.replace(".", "")) * scientific2float(s2.replace(".", "")) / scientific2float(Math.pow(10, m))
}
function accAdd(arg1, arg2){
    var r1,r2,m,
		s1 = scientific2float(arg1),
		s2 = scientific2float(arg2);
    try{r1=s1.toString().split(".")[1].length}catch(e){r1=0}
    try{r2=s2.toString().split(".")[1].length}catch(e){r2=0}
    m = Math.pow(10, Math.max(r1, r2));
    return (accMul(s1,m) + accMul(s2,m))/m;
}

function accSub(arg1,arg2){
    var r1,r2,m,n,
		s1 = scientific2float(arg1),
		s2 = scientific2float(arg2);
    try{r1=s1.toString().split(".")[1].length}catch(e){r1=0}
    try{r2=s2.toString().split(".")[1].length}catch(e){r2=0}
    m=Math.pow(10,Math.max(r1,r2));
    n=(r1>=r2)?r1:r2;
    return ((accMul(s1,m)-accMul(s2,m))/m).toFixed(n);
}
function accDiv(arg1,arg2){
    var t1=0,t2=0,r1,r2,
		s1 = scientific2float(arg1),
		s2 = scientific2float(arg2);
    try{t1=s1.toString().split(".")[1].length}catch(e){}
    try{t2=s2.toString().split(".")[1].length}catch(e){}
    with(Math){
        r1=Number(s1.toString().replace(".",""))
        r2=Number(s2.toString().replace(".",""))
        return (r1/r2)*pow(10,t2-t1);
    }
}
function scientific2float(n) {
	var num = typeof n == 'string' ? n.toLowerCase().replace(/\s/g, '') : n.toString().toLowerCase().replace(/\s/g, ''),
		standby = '000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000',
		zf, pn, base, readyNum, offset, ext, len;
	if (/e/.test(num)) {
		pn = /\+/.test(num);
		base = num.split('e');
		zf = /\-/.test(base[0].toString()) ? '-0.' : '0.';
		base[0] = base[0].replace('-', '');
		offset = base[0].split('.')[1] ? base[0].split('.')[1].length : 0;
		ext = base[1];
		len = ext.split(pn ? '+' : '-')[1];
		base = base[0].replace('.', '');
		return pn ? base + standby.substring(0, len - offset) :
			zf + standby.substring(0, len - 1) + base;
	} else {
		return num;
	}
}

function shortNum(num, binary, nameArray) {
	var len = nameArray.length,
		_unit, res = [num, ''];
	for (; len--;) {
		_unit = Math.pow(binary, len + 1);
		if (num / _unit > 1) {
			res = [num / _unit, nameArray[len], 1];
			break;
		}
	}
	return res;
}

function shortIndex(num, lens, binary, nameArray,cut) {
	var binaryArray = {
			1000 : ['K','M','B'],
			10000 : ['万','亿','兆']
		},
		lens = lens || 0,
		binary = binary || 10000,
		nameArray = nameArray || binaryArray[binary],
		tmp = shortNum(num * 1, binary, nameArray);
	tmp = (!cut || (tmp[0]+tmp[1])*1 != tmp[0]*1) ? cutFixed(tmp[0], lens) + tmp[1] : tmp[0];
	return tmp;
}


function addZero(n) {
	return n > 9 ? n : '0' + n;
}

function totime(_this, f) {
	var date = _this < Math.pow(10, 11) ? new Date(_this * 1000) : new Date(_this),
		year = date.getFullYear(),
		month = addZero(date.getMonth() + 1),
		day = addZero(date.getDate()),
		hour = addZero(date.getHours()),
		minute = addZero(date.getMinutes()),
		second = addZero(date.getSeconds());
	return f ? f.toLowerCase().replace('y', year)
		.replace('m', month)
		.replace('d', day)
		.replace('h', hour)
		.replace('i', minute)
		.replace('s', second) :
		(year + '-' + month + '-' + day + ' ' +
			hour + ':' + minute + ':' + second);
};

// Simple JavaScript Templating
// John Resig - http://ejohn.org/ - MIT Licensed
(function() {
	var cache = {};
	this.rentmpl = function(str, data) {
		var fn = !/\W/.test(str) ?
			cache[str] = cache[str] ||
			rentmpl(document.getElementById(str).innerHTML) :
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
	};
})();;
(function() {
	var isReady = false,
		readyList = [];

	function contentLoaded(fn) {
		var done = false,
			top = true,
			win = window,
			doc = win.document,
			root = doc.documentElement,
			add = doc.addEventListener ? 'addEventListener' : 'attachEvent',
			rem = doc.addEventListener ? 'removeEventListener' : 'detachEvent',
			pre = doc.addEventListener ? '' : 'on',
			init = function(e) {
				if (e.type == 'readystatechange' && doc.readyState != 'complete') return;
				(e.type == 'load' ? win : doc)[rem](pre + e.type, init, false);
				if (!done && (done = true)) fn.call(win, e.type || e);
			},
			poll = function() {
				try {
					root.doScroll('left');
				} catch (e) {
					setTimeout(poll, 50);
					return;
				}
				init('poll');
			};
		if (doc.readyState == 'complete') fn.call(win, 'lazy');
		else {
			if (doc.createEventObject && root.doScroll) {
				try {
					top = !win.frameElement;
				} catch (e) {}
				if (top) poll();
			}
			doc[add](pre + 'DOMContentLoaded', init, false);
			doc[add](pre + 'readystatechange', init, false);
			win[add](pre + 'load', init, false);
		}
	}

	contentLoaded(function() {
		isReady = true;
		fireReadyList();
	});

	function fireReadyList() {
		var i = 0,
			len = readyList.length;
		if (len) {
			for (; readyList[i]; i++) {
				readyList[i]();
			}
		}
	}

	window['domready'] = function(fn) {
		if (isReady) {
			fn && fn();
			return;
		}
		readyList.push(fn);
	}
})();

var Event = {
		add: document.addEventListener ?
			function(o, t, f) {
				o.addEventListener(t, f, false)
		} : function(o, t, f) {
			o.attachEvent('on' + t, f)
		},
		remove: document.removeEventListener ?
			function(o, t, f) {
				o.removeEventListener(t, f, false)
		} : function(o, t, f) {
			o.detachEvent('on' + t, f)
		},
		target: function(e) {
			return e.target ? e.target : window.event.srcElement;
		},
		delta: function(e) {
			var evt = e || window.event,
				d = evt.wheelDelta / -120 || evt.detail / 3;
			return d;
		},
		stop: function(e) {
			if (e && e.stopPropagation) {
				e.stopPropagation();
				e.preventDefault();
			} else {
				window.event.cancelBubble = true;
				window.event.returnValue = false;
			}
		},
		mouse: function(e) { //get mouse position
			return {
				x: e.pageX || (e.clientX + (document.documentElement.scrollLeft || document.body.scrollLeft)),
				y: e.pageY || (e.clientY + (document.documentElement.scrollTop || document.body.scrollTop))
			}
		}
	}
	/*
XMLHttpRequest.prototype.sendAsBinary = function(datastr) {
function byteValue(x) {
return x.charCodeAt(0) & 0xff;
}
var ords = Array.prototype.map.call(datastr, byteValue);
var ui8a = new Uint8Array(ords);
this.send(ui8a.buffer);
}

*/

function request() {
	/***创建XMLHttp对象***/
	var states = [
		"正在初始化……",
		"正在发送请求……",
		"正在接收数据……",
		"正在解析数据……",
		"完成！"
	],head = document.querySelector("head");

	function XmlHttp() {
		var xmlHttp;
		try {
			xmlhttp = new XMLHttpRequest();
		} catch (e) {
			try {
				xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
			} catch (e) {
				xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
		}
		return xmlhttp;
	}

	function format(s) {
		var f;
		try {
			f = eval('(' + s + ')')
		} catch (e) {
			f = s
		};
		return f;
	}

	function showLog(n) {
		//console.log(states[n])
	}
	/***发起请求***/
	function connect(options) {
		var xmlHttp = XmlHttp(),
			linkSign = /[\?]/g.test(options.url) ? '&' : '?',
			argStr = '',
			length = options.data ? options.data.length : 0;
		//showLog(xmlHttp.readyState);
		for (var key in options.data) {
			argStr += key + '=' + options.data[key] + '&';
		}
		argStr = argStr.replace(/\&$/g, '');
		if (options.type.toUpperCase() == 'GET') {
			xmlHttp.open(options.type, options.url + (argStr == '' ? '' : linkSign + argStr), options.async);
		} else {
			xmlHttp.open(options.type, options.url, options.async);
		}
		xmlHttp.setRequestHeader('Content-Type', options.contentType);
		options.myHeader && xmlHttp.setRequestHeader('method', options.myHeader);
		xmlHttp.setRequestHeader('X-Requested-With','XMLHttpRequest');
		xmlHttp.onreadystatechange = function() {
			//showLog(xmlHttp.readyState);
			if (xmlHttp.readyState == 4) {
				if (xmlHttp.status == 200) {
					if (typeof options.success == 'function') {
						var responseData = options.dataType == 'text' ? format(xmlHttp.responseText) : xmlHttp.responseXML;
						options.success(responseData, options.dataType);
					}
					xmlHttp = null;
				} else {
					if (typeof options.error == 'function') options.error('Server Status: ' + xmlHttp.status,xmlHttp.responseText);
				}
			}
		};
		xmlHttp.send(options.type.toUpperCase() == 'POST' ? argStr : null);
	}

	function connect2(options) {
		var xmlHttp = XmlHttp();
		xmlHttp.open(options.type, options.url);
		xmlHttp.onreadystatechange = function() {
			showLog(xmlHttp.readyState);
			if (xmlHttp.readyState == 4) {
				if (xmlHttp.status == 200) {
					if (typeof options.success == 'function') {
						var responseData = options.dataType == 'text' ? format(xmlHttp.responseText) : xmlHttp.responseXML;
						options.success(responseData, options.dataType);
					}
					xmlHttp = null;
				} else {
					if (typeof options.error == 'function') options.error('Server Status: ' + xmlHttp.status,xmlHttp.responseText);
				}
			}
		};
		xmlHttp.send(typeof options.data == 'object' ? JSON.stringify(options.data) : options.data);
	}
	/***初始化请求***/
	function ajax(args) {
		var options = {
			type: 'GET',
			dataType: 'text',
			async: true,
			contentType: 'application/x-www-form-urlencoded;charset=utf-8',
			url: 'about:blank',
			data: {},
			success: {},
			error: {}
		};
		if (!args) {
			console.error('没有填写任何参数');
			return;
		} else if (!args.url) {
			console.error('url地址是必填参数，请检查您的参数！');
			return;
		}
		for (var k in args) {
			options[k] = args[k];
		};
		connect(options);
	}
	/***初始化请求***/
	function requestPayload(args) {
		var options = {
			type: 'POST',
			dataType: 'text',
			url: 'about:blank',
			data: {},
			success: {},
			error: {}
		};
		if (!args) {
			console.error('没有填写任何参数');
			return;
		} else if (!args.url) {
			console.error('url地址是必填参数，请检查您的参数！');
			return;
		}
		for (var k in args) {
			options[k] = args[k];
		};
		connect2(options);
	}
	
	function getARandom() {
		return Math.ceil(1 + Math.random() * 1e3) * Math.ceil(1 + Math.random() * 1e3) * Math.ceil(1 + Math.random() * 1e3)
	}

	function jsonp(e) {
		var options = {
				type: 'JSONP',
				jsonpName: '',
				dataType: 'text',
				avatar: null,
				url: 'about:blank',
				success: {},
				data: {}
			};
		if (!e) {
			console.error("please fill in any parameters first!");
			return
		}
		if (!e.url) {
			console.error("url is required parameters, please check your parameters!");
			return
		}
		for(var k in e){
			options[k] = e[k];
		}
		_callbackfn = options.jsonpName || "jsonpCallbackFunctionNo" + (new Date).getTime() + getARandom();
		window[_callbackfn] = options.success;
		create(options);
	}

	function create(options) {
		var e = document.createElement("script"),
			t = /[\?]/g.test(options.url) ? "&" : "?",
			n = head ? head : head = document.getElementsByTagName("head")[0];
		for (var r in options.data) t += r + "=" + options.data[r] + "&";
		t = t + "callback=" + _callbackfn, e.async = !0, e.src = options.url + (t == "?" ? "" : t), n.appendChild(e)
	}


	return {
		requestPayload: requestPayload,
		ajax: ajax ,
		jsonp: jsonp
	}
}

function filterEvent(dom, obj) {
	var input = dom.querySelectorAll('input[restrict]');

	function checkObj(o, k, t) {

		if (o && o[k]) return o[k](t);
	}

	function restrictInput(e, obj) {
		var tar = e.target || window.event.srcElement,
			val = tar.value,
			res = tar.getAttribute('restrict'),
			keycode = e.keyCode || window.event.keyCode,
			tmp = filterNumber(/[\d]+[\*]+[\d]+/g.test(val) ? "": val, res * 1);
		if (!val || val == tmp || keycode == 8) return checkObj(obj, keycode, tar);
		tar.value = tmp;
		checkObj(obj, keycode, tar);
	}

	for (var l = input.length; l--;) {
		bindEvent(input[l]);
	}

	function bindEvent(o) {
		Event.add(o, 'keydown', function(e) {
			restrictInput(e, obj);
		});
		Event.add(o, 'keyup', function(e) {
			restrictInput(e, obj);
		});
	}
}
function filterNumber(n, l) {
	var n = n.replace(/[^\d\.]/g, ''),
		spt = n.split('.'),
		slen = spt.length;
	if (!l) {
		return spt[0];
	}
	if (!n) return '';
	if (/\.$/.test(n) && slen > 2) {
		return n.substring(0, n.length - 1);
	}
	if (slen > 2) {
		return spt[0] + '.' + spt[1];
	}
	if (slen == 2 && spt[1].length > l) return cutFixed(n * 1,l);
	return n;
}
function tarFather(n, t) {
	if (!n || !t || !n.parentNode) return n;
	if (n.nodeName.toLowerCase() == t.toLowerCase()) {
		return n;
	}
	return tarFather(n.parentNode, t);
}
function attFather(n, a) {
	if (!n || !a || !n.parentNode) return null;
	if (n.getAttribute(a)) {
		return [n,n.getAttribute(a)];
	}
	return attFather(n.parentNode, a);
}

var dom = function() {
	var classList = document.documentElement.classList;
	var avatars = {
		hasClass: function(node, className) {
			if (classList) {
				return node.classList.contains(className)
			} else {
				return new RegExp('(^|\\s)' + className + '(\\s|$)').test(node.className);
			}
		},
		addClass: function(node, className) {
			if (classList) {
				node.classList.add(className);
			} else if (!avatars.hasClass(node, className)) {
				node.className = node.className + ' ' + className;
			}
		},
		removeClass: function(node, className) {
			if (classList) {
				node.classList.remove(className);
			} else if (avatars.hasClass(node, className)) {
				node.className = node.className.replace(new RegExp('(^|\\s)*' + className + '(\\s|$)*', 'g'), '');
			}
		},
		getPosition: function(o, f) {
			var dom = o,
				ff, x = 0,
				y = 0,
				skp, fixBorder;
			if (!o.nodeName) return;
			while (dom) {
				if (dom.nodeName.toLowerCase() == 'body') {
					skp = 1;
				}
				fixBorder = dom.currentStyle ? dom.currentStyle : document.defaultView.getComputedStyle(dom);
				x += dom.offsetLeft + (parseInt(fixBorder.borderLeftWidth) ? parseInt(fixBorder.borderLeftWidth) : 0) - (skp ? 0 : dom.scrollLeft);
				y += dom.offsetTop + (parseInt(fixBorder.borderTopWidth) ? parseInt(fixBorder.borderTopWidth) : 0) - (skp ? 0 : dom.scrollTop);
				//console.log(y,dom,dom.scrollTop);
				ff = dom.offsetParent ? dom.offsetParent : dom;
				dom = dom.offsetParent;
				if (f & f === dom) break;
			}
			return {
				x: x,
				y: y,
				element: dom,
				forefather: f || ff
			};
		}
	}
	return avatars;
}();

/*!function(){
	var __this = arguments.callee,__arguments = arguments;
	return setTimeout(function() {
			__this.apply(this,__arguments)
		}, 1000);
}()*/

function dataDiff(newdata,olddata,diffKey,uniKey){
	var l1 = newdata.length,
		l2 = olddata.length,
		tmp = 0,checked,_this,_that,
		del = [],
		add = [],
		edit = [],
		isdif;
	for(var i = 0; i < l2; i++){
		checked = false;
		for(var j = 0; j < l1; j++){
			if(newdata[j][uniKey] == olddata[i][uniKey])checked = true;
		}
		if(!checked){
			del.push({diff_uni_key : olddata[i][uniKey],data : olddata[i],rank:i});
		}
	}
	for(var i = 0; i < l1; i++){
		checked = false;
		for(var j = 0; j < l2; j++){
			_this = olddata[j],_that = newdata[i];
			if(_this[uniKey] == _that[uniKey]){
				checked = true;
				for(key in diffKey){
					if(_this[key] != _that[k])isdif = true;
				}
				if(isdif){
					edit.push({diff_uni_key : newdata[i][uniKey],data : newdata[i],rank:i});
					isdif = false;
				}
			}
		}
		if(!checked){
			add.push({diff_uni_key : newdata[i][uniKey],data : newdata[i],rank:i});
		}
	}
	return {
		add : add,
		edit : edit,
		del : del
	}
}
var cookies = {
	get:function(name){
		var value = document.cookie.match('(?:^|;)\\s*' + name + '=([^;]*)');
		return (value) ? decodeURIComponent(value[1]) : null;
	},
	set:function(value){
		var str = value.name + '=' + encodeURIComponent(value.value);
			if(value.domain){ str += '; domain=' + value.domain;}
			if(value.path){ str += '; path=' + value.path;}
			if(value.time){
				var time = new Date();
				time.setTime(time.getTime()+value.time*1000);
				str += '; expires=' + time.toGMTString();
			}
		document.cookie = str;
		return;
	},
	del:function(name){
		var str = this.read(name);
		this.write({name:name,value:str,day:-1});
		return;
	}
}
/*
                  〓                  
    〓                                
    〓                                
〓〓〓〓〓    〓〓〓      〓〓〓〓〓  
    〓            〓        〓      〓
    〓            〓        〓      〓
    〓            〓        〓      〓
    〓            〓        〓      〓
      〓〓    〓〓〓〓〓    〓〓〓〓  
                            〓        
                          〓〓〓
*/
function besideTip() {
	var obj,
		customHeight,
		text,
		timer,
		isshow,
		offset = {
			left: 0,
			top: 0,
			right: 0,
			bottom: 0
		},
		path = {
			left: 0,
			top: 0,
			right: 0,
			bottom: 0
		};
	if (window['newBeside']) return newBeside;

	function top(n) {
		isshow && clearPath();
		path.bottom = 0;
		path.top = 1;
		offset.top = n || 0;
		return this;
	}

	function right(n) {
		isshow && clearPath();
		path.left = 0;
		path.right = 1;
		offset.right = n || 0;
		return this;
	}

	function bottom(n) {
		isshow && clearPath();
		path.top = 0;
		path.bottom = 1;
		offset.bottom = n || 0;
		return this;
	}

	function left(n) {
		isshow && clearPath();
		path.right = 0;
		path.left = 1;
		offset.left = n || 0;
		return this;
	}

	function setPosition() {
		var position = dom.getPosition(obj),
			w, h, area, innerSize,
			objSize = {
				w: obj.offsetWidth,
				h: obj.offsetHeight
			},
			contSize = {
				w: document.documentElement.scrollWidth || document.body.scrollWidth,
				sw: document.documentElement.clientWidth || document.body.clientWidth,
				h: document.documentElement.scrollHeight || document.body.scrollHeight,
				sh: document.documentElement.clientHeight || document.body.clientHeight
			};
		text.box.style.lineHeight = customHeight || (objSize.h + 'px');
		innerSize = {
			w: text.cont.offsetWidth,
			h: text.cont.offsetHeight
		}
		area = innerSize.w * innerSize.h;
		if ((path.left || path.right) && (path.top || path.bottom)) {
			dom.addClass(text.box, 'besidePathWidthHeight');
		} else if (path.left || path.right) {
			dom.addClass(text.box, 'besidePathWidth');
		} else {
			dom.addClass(text.box, 'besidePathHeight');
		}
		if (path.left) {
			if (!path.top && !path.bottom) {
				text.box.style.top = (position.y + offset.top) + 'px';
			}
			h = contSize.sh - position.y + offset.top;
			w = contSize.sw - position.x + offset.left;
			text.box.style.right = (contSize.sw - position.x + offset.left) + 'px';
		}
		if (path.right) {
			if (!path.top && !path.bottom) {
				text.box.style.top = (position.y + offset.top) + 'px';
			}
			h = contSize.sh - position.y + offset.top;
			w = contSize.w - position.x - objSize.w - offset.right
			text.box.style.left = (position.x + objSize.w + offset.right) + 'px';
		}
		if (path.top) {
			if (!path.left && !path.right) {
				text.box.style.left = (position.x + offset.left) + 'px';
			}
			h = contSize.sh - position.y + offset.top;
			text.box.style.bottom = (contSize.sh - position.y + offset.top) + 'px';
		}
		if (path.bottom || (!path.top && !path.bottom && !path.left && !path.right)) {
			if (!path.left && !path.right) {
				w = contSize.w - position.x - offset.left;
				text.box.style.left = (position.x + offset.left) - 1 + 'px';
			}
			h = contSize.h - position.y - objSize.h - offset.bottom;
			text.box.style.top = (position.y + objSize.h + offset.bottom) + 'px';
		}
		text.box.style.width = Math.max(objSize.w, innerSize.w) - 2 + 'px';
		text.box.style.height = customHeight || (Math.max(objSize.h, innerSize.h) - 2 + 'px');
	}
	function setHeight(h){
		customHeight = h;
		return this;
	}
	function notice(content, t, lv) {
		var size;
		isshow = 1;
		timer && clearTimeout(timer);
		timer = null;
		text.box.style.cssText = '';
		dom.addClass(text.box, 'beside-' + (lv || 'notice'));
		text.cont.innerHTML = content;
		size = setPosition();
		t && (timer = setTimeout(close, t));
	}

	function confirm() {
		setPosition();
	}

	function clearPath() {
		path = {
			left: 0,
			top: 0,
			right: 0,
			bottom: 0
		}
	}

	function close() {
		text.box.className = 'besidetipbox';
		text.box.style.width = 0;
		text.box.style.height = 0;
		isshow = 0;
		customHeight = null;
		clearPath();
	}

	function setRoles(o) {
		clearPath();
		if (!o && !obj) return null;
		if (o) {
			obj = typeof o == 'string' ? document.querySelector(o) : o;
			return this;
		}
		return this;
	}

	function initFire(d, s) {
		var style = s ? d.createElement('link') : d.createElement('style'),
			body = d.querySelector('body'),
			easeTime = '0.4',
			div = document.createElement('div');
		div.className = "besidetipbox";
		if (s) {
			style.href = s;
			style.rel = 'stylesheet';
			head.appendChild(style);
			div.innerHTML = "<dl><dt class='text'></dt><dd class='button'></dd></dl>";
		} else {
			div.innerHTML = "<br/>" + "<style>.besidetipbox{width:0;height:0;position:absolute;overflow:hidden;z-index:4;}" + ".besidePathWidth{-moz-transition:width " + easeTime + "s ease;" + "-webkit-transition:width " + easeTime + "s ease;" + "-ms-transition:width " + easeTime + "s ease;" + "transition:width " + easeTime + "s ease;}" + ".besidePathHeight{-moz-transition:height " + easeTime + "s ease;" + "-webkit-transition:height " + easeTime + "s ease;" + "-ms-transition:height " + easeTime + "s ease;" + "transition:height " + easeTime + "s ease;}" + ".besidePathWidthHeight{-moz-transition:height " + easeTime + "s ease,width " + easeTime + "s ease;" + "-webkit-transition:height " + easeTime + "s ease,width " + easeTime + "s ease;" + "-ms-transition:height " + easeTime + "s ease,width " + easeTime + "s ease;" + "transition:height " + easeTime + "s ease,width " + easeTime + "s ease;}"

			+ ".besidetipbox dl{position:absolute;left:0;top:0;width:500px;height:500px;}" + ".besidetipbox dt{display:inline-block;padding:0 10px}" + ".beside-notice{border:1px solid #9fd5e9;color:#1b7ca1;background:#d0f2ff;}" + ".beside-success{border:1px solid #b0d38d;color:#519114;background:#e7ffd0;}" + ".beside-warning{border:1px solid #edd8b1;color:#da4d00;background:#fffcd4;}" + ".beside-error{border:1px solid #f0b8b8;color:#d13939;background:#ffebeb;}" + "</style><dl><dt class='text'></dt><dd class='button'></dd></dl>";
		}
		body.appendChild(div);
		Event.add(window, 'resize', function() {
			if (isshow) {
				setPosition();
			}
		});
		text = {
			box: div,
			inbox: div.querySelector('dl'),
			cont: div.querySelector('dt'),
			btn: div.querySelector('dd')
		}
	}

	/*


        var a = besideTip();
        a.changeObj('#buyBtn').left().bottom().notice('购买成功');
	*/
	window['newBeside'] = {
		changeObj: setRoles,
		left: left,
		top: top,
		right: right,
		bottom: bottom,
		close: close,
		notice: notice,
		confirm: confirm,
		setHeight : setHeight
	};

	! function(d, s) {
		'construct';
		initFire(d, s)
	}.apply(this, [document].concat([].slice.apply(arguments)));

	return newBeside;
}




function helpTips(){
	var div = document.createElement('div'),
		body = document.getElementsByTagName('body')[0],
		isAppend,timer,saveMiss;
	div.className = 'futures_tip_box help_tip_box help_tip_box_0';
	function show(c,l,t,w,ol){
		var size;
		if(!c)return;
		!isAppend && body.appendChild(div) && bindKeep();
		isAppend = 1;
		div.innerHTML = c;
		size = [div.offsetWidth,div.offsetHeight];
		//console.log('top:'+(t-size[1]-10)+'px;left:'+(l-size[0]+(ol || w)-12)+'px;')
		div.style.cssText = 'top:'+(t-size[1]-10)+'px;left:'+(l-size[0]+(ol || w)*1-12)+'px;';
	}
	function hide(c,t,l){
		if(!isAppend)return; 
		div.innerHTML = c;
		div.style.cssText = '';
	}
	function setEvent(d){
		if(d.binded){return;}
		Event.add(d,'mouseover',function(){
			saveMiss && clearTimeout(saveMiss);
			saveMiss = null;
			saveMiss = setTimeout(function(){
				var con = d.getAttribute('data-tip-info'),
					ol = d.getAttribute('data-offset-left'),
					right = d.getAttribute('data-right'),
				pos = dom.getPosition(d);
				if(right){
					!dom.hasClass(div,'help_tip_box_right') && dom.addClass(div,'help_tip_box_right');
				}else{
					dom.hasClass(div,'help_tip_box_right') && dom.removeClass(div,'help_tip_box_right');
				}
				timer && clearTimeout(timer);
				timer = null;
				show(con,pos.x,pos.y,d.w || (d.w = d.offsetWidth),ol);
			},200);
		});
		Event.add(d,'mouseout',function(){
			saveMiss && clearTimeout(saveMiss);
			saveMiss = null;
			timer = setTimeout(hide,300);
		});
		d.binded = 1;
	}
	function bindKeep(){
		Event.add(div,'mouseover',function(){
			timer && clearTimeout(timer);
			timer = null;
		});
		Event.add(div,'mouseout',function(){
			saveMiss && clearTimeout(saveMiss);
			saveMiss = null;
			timer = setTimeout(hide,300);
		});
	}
	function bind(o,className){
		if(!o)return;
		var doms = o.querySelectorAll(className),
			lens = doms.length;
		for(;lens--;){
			setEvent(doms[lens]);
		}
	}
	return {
		bind : bind,
		show : show,
		hide : hide,
		div : div
	}
}



/*
                      〓〓    〓〓〓〓〓
                        〓    〓        
                        〓    〓        
〓〓〓〓〓        〓〓〓〓    〓〓〓〓  
  〓  〓  〓    〓      〓    〓      〓
  〓  〓  〓    〓      〓            〓
  〓  〓  〓    〓      〓    〓      〓
  〓  〓  〓    〓      〓    〓      〓
〓〓  〓  〓〓    〓〓〓〓〓    〓〓〓  
*/
var md5 = function() {
	var hexcase = 0;
	var b64pad = "";

	function hex_md5(s) {
		return rstr2hex(rstr_md5(str2rstr_utf8(s)))
	};

	function b64_md5(s) {
		return rstr2b64(rstr_md5(str2rstr_utf8(s)))
	};

	function any_md5(s, e) {
		return rstr2any(rstr_md5(str2rstr_utf8(s)), e)
	};

	function hex_hmac_md5(k, d) {
		return rstr2hex(rstr_hmac_md5(str2rstr_utf8(k), str2rstr_utf8(d)))
	};

	function b64_hmac_md5(k, d) {
		return rstr2b64(rstr_hmac_md5(str2rstr_utf8(k), str2rstr_utf8(d)))
	};

	function any_hmac_md5(k, d, e) {
		return rstr2any(rstr_hmac_md5(str2rstr_utf8(k), str2rstr_utf8(d)), e)
	};

	function rstr_md5(s) {
		return binl2rstr(binl_md5(rstr2binl(s), s.length * 8))
	};

	function rstr_hmac_md5(key, data) {
		var bkey = rstr2binl(key);
		if (bkey.length > 16) bkey = binl_md5(bkey, key.length * 8);
		var ipad = Array(16),
			opad = Array(16);
		for (var i = 0; i < 16; i++) {
			ipad[i] = bkey[i] ^ 0x36363636;
			opad[i] = bkey[i] ^ 0x5C5C5C5C
		};
		var hash = binl_md5(ipad.concat(rstr2binl(data)), 512 + data.length * 8);
		return binl2rstr(binl_md5(opad.concat(hash), 512 + 128))
	};

	function rstr2hex(input) {
		try {
			hexcase
		} catch (e) {
			hexcase = 0
		};
		var hex_tab = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
		var output = "";
		var x;
		for (var i = 0; i < input.length; i++) {
			x = input.charCodeAt(i);
			output += hex_tab.charAt((x >>> 4) & 0x0F) + hex_tab.charAt(x & 0x0F)
		};
		return output
	};

	function rstr2b64(input) {
		try {
			b64pad
		} catch (e) {
			b64pad = ''
		};
		var tab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		var output = "";
		var len = input.length;
		for (var i = 0; i < len; i += 3) {
			var triplet = (input.charCodeAt(i) << 16) | (i + 1 < len ? input.charCodeAt(i + 1) << 8 : 0) | (i + 2 < len ? input.charCodeAt(i + 2) : 0);
			for (var j = 0; j < 4; j++) {
				if (i * 8 + j * 6 > input.length * 8) output += b64pad;
				else output += tab.charAt((triplet >>> 6 * (3 - j)) & 0x3F)
			}
		};
		return output
	};

	function rstr2any(input, encoding) {
		var divisor = encoding.length;
		var i, j, q, x, quotient;
		var dividend = Array(Math.ceil(input.length / 2));
		for (i = 0; i < dividend.length; i++) {
			dividend[i] = (input.charCodeAt(i * 2) << 8) | input.charCodeAt(i * 2 + 1)
		};
		var full_length = Math.ceil(input.length * 8 / (Math.log(encoding.length) / Math.log(2)));
		var remainders = Array(full_length);
		for (j = 0; j < full_length; j++) {
			quotient = Array();
			x = 0;
			for (i = 0; i < dividend.length; i++) {
				x = (x << 16) + dividend[i];
				q = Math.floor(x / divisor);
				x -= q * divisor;
				if (quotient.length > 0 || q > 0) quotient[quotient.length] = q
			};
			remainders[j] = x;
			dividend = quotient
		};
		var output = "";
		for (i = remainders.length - 1; i >= 0; i--) output += encoding.charAt(remainders[i]);
		return output
	};

	function str2rstr_utf8(input) {
		var output = "";
		var i = -1;
		var x, y;
		while (++i < input.length) {
			x = input.charCodeAt(i);
			y = i + 1 < input.length ? input.charCodeAt(i + 1) : 0;
			if (0xD800 <= x && x <= 0xDBFF && 0xDC00 <= y && y <= 0xDFFF) {
				x = 0x10000 + ((x & 0x03FF) << 10) + (y & 0x03FF);
				i++
			};
			if (x <= 0x7F) output += String.fromCharCode(x);
			else if (x <= 0x7FF) output += String.fromCharCode(0xC0 | ((x >>> 6) & 0x1F), 0x80 | (x & 0x3F));
			else if (x <= 0xFFFF) output += String.fromCharCode(0xE0 | ((x >>> 12) & 0x0F), 0x80 | ((x >>> 6) & 0x3F), 0x80 | (x & 0x3F));
			else if (x <= 0x1FFFFF) output += String.fromCharCode(0xF0 | ((x >>> 18) & 0x07), 0x80 | ((x >>> 12) & 0x3F), 0x80 | ((x >>> 6) & 0x3F), 0x80 | (x & 0x3F))
		};
		return output
	};

	function str2rstr_utf16le(input) {
		var output = "";
		for (var i = 0; i < input.length; i++) output += String.fromCharCode(input.charCodeAt(i) & 0xFF, (input.charCodeAt(i) >>> 8) & 0xFF);
		return output
	};

	function str2rstr_utf16be(input) {
		var output = "";
		for (var i = 0; i < input.length; i++) output += String.fromCharCode((input.charCodeAt(i) >>> 8) & 0xFF, input.charCodeAt(i) & 0xFF);
		return output
	};

	function rstr2binl(input) {
		var output = Array(input.length >> 2);
		for (var i = 0; i < output.length; i++) output[i] = 0;
		for (var i = 0; i < input.length * 8; i += 8) output[i >> 5] |= (input.charCodeAt(i / 8) & 0xFF) << (i % 32);
		return output
	};

	function binl2rstr(input) {
		var output = "";
		for (var i = 0; i < input.length * 32; i += 8) output += String.fromCharCode((input[i >> 5] >>> (i % 32)) & 0xFF);
		return output
	};

	function binl_md5(x, len) {
		x[len >> 5] |= 0x80 << ((len) % 32);
		x[(((len + 64) >>> 9) << 4) + 14] = len;
		var a = 1732584193;
		var b = -271733879;
		var c = -1732584194;
		var d = 271733878;
		for (var i = 0; i < x.length; i += 16) {
			var olda = a;
			var oldb = b;
			var oldc = c;
			var oldd = d;
			a = md5_ff(a, b, c, d, x[i + 0], 7, -680876936);
			d = md5_ff(d, a, b, c, x[i + 1], 12, -389564586);
			c = md5_ff(c, d, a, b, x[i + 2], 17, 606105819);
			b = md5_ff(b, c, d, a, x[i + 3], 22, -1044525330);
			a = md5_ff(a, b, c, d, x[i + 4], 7, -176418897);
			d = md5_ff(d, a, b, c, x[i + 5], 12, 1200080426);
			c = md5_ff(c, d, a, b, x[i + 6], 17, -1473231341);
			b = md5_ff(b, c, d, a, x[i + 7], 22, -45705983);
			a = md5_ff(a, b, c, d, x[i + 8], 7, 1770035416);
			d = md5_ff(d, a, b, c, x[i + 9], 12, -1958414417);
			c = md5_ff(c, d, a, b, x[i + 10], 17, -42063);
			b = md5_ff(b, c, d, a, x[i + 11], 22, -1990404162);
			a = md5_ff(a, b, c, d, x[i + 12], 7, 1804603682);
			d = md5_ff(d, a, b, c, x[i + 13], 12, -40341101);
			c = md5_ff(c, d, a, b, x[i + 14], 17, -1502002290);
			b = md5_ff(b, c, d, a, x[i + 15], 22, 1236535329);
			a = md5_gg(a, b, c, d, x[i + 1], 5, -165796510);
			d = md5_gg(d, a, b, c, x[i + 6], 9, -1069501632);
			c = md5_gg(c, d, a, b, x[i + 11], 14, 643717713);
			b = md5_gg(b, c, d, a, x[i + 0], 20, -373897302);
			a = md5_gg(a, b, c, d, x[i + 5], 5, -701558691);
			d = md5_gg(d, a, b, c, x[i + 10], 9, 38016083);
			c = md5_gg(c, d, a, b, x[i + 15], 14, -660478335);
			b = md5_gg(b, c, d, a, x[i + 4], 20, -405537848);
			a = md5_gg(a, b, c, d, x[i + 9], 5, 568446438);
			d = md5_gg(d, a, b, c, x[i + 14], 9, -1019803690);
			c = md5_gg(c, d, a, b, x[i + 3], 14, -187363961);
			b = md5_gg(b, c, d, a, x[i + 8], 20, 1163531501);
			a = md5_gg(a, b, c, d, x[i + 13], 5, -1444681467);
			d = md5_gg(d, a, b, c, x[i + 2], 9, -51403784);
			c = md5_gg(c, d, a, b, x[i + 7], 14, 1735328473);
			b = md5_gg(b, c, d, a, x[i + 12], 20, -1926607734);
			a = md5_hh(a, b, c, d, x[i + 5], 4, -378558);
			d = md5_hh(d, a, b, c, x[i + 8], 11, -2022574463);
			c = md5_hh(c, d, a, b, x[i + 11], 16, 1839030562);
			b = md5_hh(b, c, d, a, x[i + 14], 23, -35309556);
			a = md5_hh(a, b, c, d, x[i + 1], 4, -1530992060);
			d = md5_hh(d, a, b, c, x[i + 4], 11, 1272893353);
			c = md5_hh(c, d, a, b, x[i + 7], 16, -155497632);
			b = md5_hh(b, c, d, a, x[i + 10], 23, -1094730640);
			a = md5_hh(a, b, c, d, x[i + 13], 4, 681279174);
			d = md5_hh(d, a, b, c, x[i + 0], 11, -358537222);
			c = md5_hh(c, d, a, b, x[i + 3], 16, -722521979);
			b = md5_hh(b, c, d, a, x[i + 6], 23, 76029189);
			a = md5_hh(a, b, c, d, x[i + 9], 4, -640364487);
			d = md5_hh(d, a, b, c, x[i + 12], 11, -421815835);
			c = md5_hh(c, d, a, b, x[i + 15], 16, 530742520);
			b = md5_hh(b, c, d, a, x[i + 2], 23, -995338651);
			a = md5_ii(a, b, c, d, x[i + 0], 6, -198630844);
			d = md5_ii(d, a, b, c, x[i + 7], 10, 1126891415);
			c = md5_ii(c, d, a, b, x[i + 14], 15, -1416354905);
			b = md5_ii(b, c, d, a, x[i + 5], 21, -57434055);
			a = md5_ii(a, b, c, d, x[i + 12], 6, 1700485571);
			d = md5_ii(d, a, b, c, x[i + 3], 10, -1894986606);
			c = md5_ii(c, d, a, b, x[i + 10], 15, -1051523);
			b = md5_ii(b, c, d, a, x[i + 1], 21, -2054922799);
			a = md5_ii(a, b, c, d, x[i + 8], 6, 1873313359);
			d = md5_ii(d, a, b, c, x[i + 15], 10, -30611744);
			c = md5_ii(c, d, a, b, x[i + 6], 15, -1560198380);
			b = md5_ii(b, c, d, a, x[i + 13], 21, 1309151649);
			a = md5_ii(a, b, c, d, x[i + 4], 6, -145523070);
			d = md5_ii(d, a, b, c, x[i + 11], 10, -1120210379);
			c = md5_ii(c, d, a, b, x[i + 2], 15, 718787259);
			b = md5_ii(b, c, d, a, x[i + 9], 21, -343485551);
			a = safe_add(a, olda);
			b = safe_add(b, oldb);
			c = safe_add(c, oldc);
			d = safe_add(d, oldd)
		};
		return Array(a, b, c, d)
	};

	function md5_cmn(q, a, b, x, s, t) {
		return safe_add(bit_rol(safe_add(safe_add(a, q), safe_add(x, t)), s), b)
	};

	function md5_ff(a, b, c, d, x, s, t) {
		return md5_cmn((b & c) | ((~b) & d), a, b, x, s, t)
	};

	function md5_gg(a, b, c, d, x, s, t) {
		return md5_cmn((b & d) | (c & (~d)), a, b, x, s, t)
	};

	function md5_hh(a, b, c, d, x, s, t) {
		return md5_cmn(b ^ c ^ d, a, b, x, s, t)
	};

	function md5_ii(a, b, c, d, x, s, t) {
		return md5_cmn(c ^ (b | (~d)), a, b, x, s, t)
	};

	function safe_add(x, y) {
		var lsw = (x & 0xFFFF) + (y & 0xFFFF);
		var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
		return (msw << 16) | (lsw & 0xFFFF)
	};

	function bit_rol(num, cnt) {
		return (num << cnt) | (num >>> (32 - cnt))
	};

	function b64_md5(s) {
		return rstr2b64(rstr_md5(str2rstr_utf8(s)))
	};

	function any_md5(s, e) {
		return rstr2any(rstr_md5(str2rstr_utf8(s)), e)
	};

	function hex_hmac_md5(k, d) {
		return rstr2hex(rstr_hmac_md5(str2rstr_utf8(k), str2rstr_utf8(d)))
	};

	function b64_hmac_md5(k, d) {
		return rstr2b64(rstr_hmac_md5(str2rstr_utf8(k), str2rstr_utf8(d)))
	};

	function any_hmac_md5(k, d, e) {
		return rstr2any(rstr_hmac_md5(str2rstr_utf8(k), str2rstr_utf8(d)), e)
	};
	hex_md5.b64 = b64_md5;
	hex_md5.any = any_md5;
	hex_md5.md5_hmac = hex_hmac_md5;
	hex_md5.b64_hmac = b64_hmac_md5;
	hex_md5.any_hmac = any_hmac_md5;
	return hex_md5
}();
/*
var binary = {
	1000 : ['K','M','B'],
	10000 : ['万','亿','兆']
}*/