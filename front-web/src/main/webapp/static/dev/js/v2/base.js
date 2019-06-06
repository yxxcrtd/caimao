
function request() {
	/***创建XMLHttp对象***/
	var states = [
		"正在初始化……",
		"正在发送请求……",
		"正在接收数据……",
		"正在解析数据……",
		"完成！"
	];

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
		showLog(xmlHttp.readyState);
		for (var key in options.data) {
			argStr += key + '=' + options.data[key] + '&';
		}
		argStr = argStr.replace(/\&$/g, '');
		if (options.type.toUpperCase() == 'GET') {
			xmlHttp.open(options.type, options.url + (argStr == '' ? '' : linkSign + argStr), options.async);
		} else {
			xmlHttp.open(options.type, options.url, options.async);
		}
		showLog(xmlHttp.readyState);
		xmlHttp.setRequestHeader('Content-Type', options.contentType);
		//!options.skipHeader && xmlHttp.setRequestHeader('AVATAR', 'ajax');
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
					if (typeof options.error == 'function') options.error('Server Status: ' + xmlHttp.status);
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
					if (typeof options.error == 'function') options.error('Server Status: ' + xmlHttp.status);
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
			contentType: 'application/x-www-form-urlencoded',
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
		if (!e) {
			console.error("please fill in any parameters first!");
			return
		}
		if (!e.url) {
			console.error("url is required parameters, please check your parameters!");
			return
		}
		options.setVal(e);
		_callbackfn = options.jsonpName || "jsonpCallbackFunctionNo" + (new Date).getTime() + "_" + getARandom();
		window[_callbackfn] = options.success;
		create();
	}

	function create() {
		var e = document.createElement("script"),
			t = /[\?]/g.test(options.url) ? "&" : "?",
			n = head ? head : head = document.getElementsByTagName("head")[0];
		for (var r in options.data) t += r + "=" + options.data[r] + "&";
		t = t + "callback=" + _callbackfn, e.async = !0, e.src = options.url + (t == "?" ? "" : t), n.appendChild(e)
	}


	return {
		requestPayload: requestPayload,
		ajax: ajax,
		jsonp: jsonp
	}
}

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
})();


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
