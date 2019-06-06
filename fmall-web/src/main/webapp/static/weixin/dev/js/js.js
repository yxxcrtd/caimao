
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
function attFather(n, a) {
	if (!n || !a || !n.parentNode) return null;
	if (n.getAttribute(a)) {
		return [n,n.getAttribute(a)];
	}
	return attFather(n.parentNode, a);
}
function setMaskSize(id){
	var mask = document.getElementById(id);
	function size(){
		return window.screen.height || document.documentElement.clientHeight || document.body.clientHeight;
	}
	function setSize(){
		mask.style.height = size() + "px";
	}
	Event.add(window,'touchstart',function(e){mask.style.display == "block" && !attFather(Event.target(e),"customAction") && Event.stop(e)});
	Event.add(window,'touchmove',function(e){mask.style.display == "block" && !attFather(Event.target(e),"customAction") && Event.stop(e)});
	Event.add(window,'touchend',function(e){mask.style.display == "block" && !attFather(Event.target(e),"customAction") && Event.stop(e)});
	Event.add(window,'scroll',setSize);
	Event.add(window,'resize',setSize);
	setSize();
}
function setItem(_this){
	var _this = _this,
		rbox = _this.querySelector("div"),
		div = document.createElement("div"),
		h = rbox.offsetHeight,
		bh = _this.offsetHeight;
	div.innerHTML = "......";
	//div.className = "showMore";
	if(h > bh){
		_this.parentNode.insertBefore(div,_this.nextSibling);
		Event.add(_this,"click",function(){
			if(div.getAttribute("removed"))return;
			div.setAttribute("removed",1);
			_this.parentNode.removeChild(div);
			_this.className = "saying unlikeBtn";
			_this.style.maxHeight = "999999px";
		})
	}else{
		_this.className = "saying unlikeBtn";
	}
}
function showBtnInit(){
	var box = document.getElementById("sayingList"),
		unit = box.querySelectorAll("div.saying");
	for(var l = unit.length;l--;){
		setItem(unit[l]);
	}
}