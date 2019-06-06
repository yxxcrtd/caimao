function J() {
	var a = [].slice.apply(arguments);
	if (a.length < 2) return a.length ? a.shift()() : null;
	return a.shift().apply(a.shift(), a);
};

function buildDom(dom) {
	function action() {
		var _this = this,
			className, addEvent, size, tip, body, pos, path, timer,isshow,otext,olevel,otime,scc;
		function attachStyle() {
			var div;
			if (window['attached']) return;
			window['attached'] = 1;
			div = document.createElement('div');
			body.appendChild(div);
			div.style.display = 'none';
			div.innerHTML = "<br/><style>"
				+ ".attachTip{position:absolute;padding:0 5px;height:18px;line-height:18px;z-index: 10;}"
				+ ".attachTipFixed{position:fixed;padding:0 5px;z-index: 10;}"
				+ ".noticeTip{background:#ecfcff;color:#5588cc;border:1px solid #5588cc;}"
				+ ".warnTip{background:#fef6de;color:#eb4636;border:1px solid #fef6de;}"
				+ ".errorTip{background:#fef6de;color:#eb4636;border:1px solid #fef6de;}"
				+ "</style>";
		}

		_this.top = function(n) {
			var borderStyle = _this.currentStyle ? _this.currentStyle : document.defaultView.getComputedStyle(_this);
			_this.reset()
			path = {
				left: [0 - parseInt(borderStyle.borderLeftWidth),function(){return pos.x}],
				width : size[0] - 10 - 2,
				top: [(n * 1 || 0) - parseInt(borderStyle.borderTopWidth),function(){return pos.y - 1*tip.offsetHeight}]
			};
			return _this;
		}
		_this.right = function(n) {
			var borderStyle = _this.currentStyle ? _this.currentStyle : document.defaultView.getComputedStyle(_this);
			_this.reset()
			path = {
				left: [size[0] + (n * 1 || 0) - parseInt(borderStyle.borderLeftWidth),function(){return pos.x}],
				top: [0 - parseInt(borderStyle.borderTopWidth),function(){return pos.y}],
				height : size[1] - 2,
				lineHeight :  size[1] - 2
			};
			return _this;
		}
		_this.bottom = function(n) {
			var borderStyle = _this.currentStyle ? _this.currentStyle : document.defaultView.getComputedStyle(_this);
			_this.reset()
			path = {
				left: [0 - parseInt(borderStyle.borderLeftWidth),function(){return pos.x}],
				top: [size[1] + (n * 1 || 0) - parseInt(borderStyle.borderTopWidth),function(){return pos.y}],
				width : size[0] - 10 - 2
			};
			return _this;
		}
		_this.left = function(n) {
			var borderStyle = _this.currentStyle ? _this.currentStyle : document.defaultView.getComputedStyle(_this);
			_this.reset()
			path = {
				top: [0 - parseInt(borderStyle.borderTopWidth),function(){return pos.y}],
				height : size[1] - 2,
				lineHeight :  size[1] - 2,
				left: [(n * 1 || 0) - parseInt(borderStyle.borderLeftWidth),function(){return pos.x -1*tip.offsetWidth}]
			};
			return _this;
		}
		_this.setStyle = function(s){
			tip.style.cssText += s;
			scc = s;
			return  _this;
		}
		_this.insertCss = function(s){
			var n = document.createElement('link');
			n.setAttribute('rel','stylesheet');
			n.setAttribute('type','text/css');
			n.setAttribute('href',s);
			document.querySelector('head').appendChild(n);
			return _this;
		}
		_this.reset = function() {
			size = [_this.offsetWidth, _this.offsetHeight];
			pos = getPosition(_this);
			if(isshow){
				_this.style.border = '';
				tip.style.cssText = 'display:none';
				if (_isFixed(_this)) {
					tip.className = 'attachTip attachTipFixed';
				} else {
					tip.className = 'attachTip';
				}
				scc && _this.setStyle(scc);
				show(otext, olevel, otime);
			}
		}
		_this.notice = function(txt, time) {
			show(txt, 1, time)
		}
		_this.error = function(txt, time) {
			show(txt, 3, time)
		}
		_this.warn = function(txt, time) {
			show(txt, 2, time)
		}
		_this.close = function(){
			hidden();
		}
		function hidden() {
			_this.style.border = '';
			tip.style.cssText = 'display:none';
			if (_isFixed(_this)) {
				tip.className = 'attachTip attachTipFixed';
			} else {
				tip.className = 'attachTip';
			}
			path = null;
			isshow = 0;
			otext = null;
			olevel = null;
			otime = null;
			scc = null;
		}

		function show(text, level, time) {
			timer && clearTimeout(timer);
			timer = null;
			otext = text;
			olevel = level;
			otime = time;
			if(!path){
				_this.bottom();
			}
			tip.innerHTML = text;
			if (level == 3) {
				_this.style.border = '1px solid #eb4636';
			}
			tip.style.display = 'block';
			if (_isFixed(_this)) {
				tip.className = 'attachTip attachTipFixed ' + className[level];
			} else {
				tip.className = 'attachTip ' + className[level];
			}

			for (var k in path) {
				if(typeof path[k] == 'number'){
					if(!tip.style[k])tip.style[k] = path[k] + 'px';
				}else{
					for(var tmp = 0, i = 0, l = path[k].length; i < l; i++){
						tmp += Object.prototype.toString.call(path[k][i]) == '[object Function]' ? path[k][i]() : path[k][i];
					}
					if(!tip.style[k])tip.style[k] = tmp + 'px';
				}				
			}
			if(time){
				timer = setTimeout(hidden,time);
			}
			isshow = 1;
		}! function() {
			'construct';
			addEvent = document.addEventListener ?
				function(o, t, f) {
					o.addEventListener(t, f, false)
			} : function(o, t, f) {
				o.attachEvent('on' + t, f)
			}
			path = 1;
			className = ['level', 'noticeTip', 'warnTip', 'errorTip'];
			tip = document.createElement('span');
			body = document.querySelector('body');
			tip.style.display = 'none';
			body.appendChild(tip);
			addEvent(window, 'resize', _this.reset);
			addEvent(window, 'scroll', _this.reset);
			attachStyle();
			_this.reset();
		}.apply(window, arguments);
		return _this;
	}

	function getPosition(o, f) {
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
	function _isFixed(o) {
		var dom = o, fixBorder, isFixed = false;
		while(dom) {
			fixBorder = dom.currentStyle ? dom.currentStyle : document.defaultView.getComputedStyle(dom);
			if (fixBorder.position == "fixed") {
				isFixed = true;
				break;
			}
			dom = dom.offsetParent;
		}
		return isFixed;
	}
	return J(action, typeof dom == 'string' ? document.querySelector(dom) || document.querySelector("#"+dom) : dom);
}

function cansee(btn){
	var btn = document.querySelector("#"+btn),
		input = btn.parentNode.querySelector('input'),
		parents = btn.parentNode,
		ispassword = 1;
	document.addEventListener ?
		btn.addEventListener('click', switchEyes, false) : btn.attachEvent('onclick', switchEyes);
	function switchEyes(){
		var val = input.value;
		parents.removeChild(input);
		input = document.createElement('input');
		if(ispassword){
			btn.className = 'on';
			input.type = 'text';
			ispassword = 0;
		}else{
			btn.className = '';
			input.type = 'password';
			ispassword = 1;
		}
		input.value = val;
		input.className = "textInput"
		parents.appendChild(input);
	}
}