// @koala-append "_action.js"
function _event(box, canvas) {
	//this -> stockChart
	var _this = this,
		stockChart = _this,
		shortcutInput, isFocus, doubleWait = 0,
		bind;
	stockChart.crossStart = 1;

	function keyEvent() {
		if (!bind) {
			Event.add(document, 'keydown', keyboardEvent);
			Event.add(document, 'keyup', keyboardEvent2);
			J(_action, _this, box, canvas, _this['domlist'] ? _this.domlist.shortcut : document.querySelector('#shortcut'));
		}
		bind = 1;
		Event.add(document, 'click', clickEvent);
		//if (!_this['domlist']) return setTimeout(keyEvent, 500);
		Event.add(box, 'click', doubleClickEvent);
		if(document.querySelector('#shortcut')){
			shortcutInput = (_this['domlist'] ? _this.domlist.shortcut : document.querySelector('#shortcut')).parentNode.getElementsByTagName('input')[0];
			Event.add(shortcutInput, 'focus', function() {
				isFocus = 1;
			})
			Event.add(shortcutInput, 'blur', function() {
				isFocus = 0;
			})
		}
	}

	function getStop(n) {
		if (!n || !n.parentNode) return null;
		if (n.getAttribute('stop')) {
			return n;
		}
		return getStop(n.parentNode);
	}

	function once_Event(e) {
		var e = window.event || e,
			kc = e.keyCode,
			tar = Event.target(e),
			act = tar.getAttribute('act');
		//console.log(kc);
		if (act == 'getusable') {
			return tar.setAttribute('cooldown', 1);
		}
		if(!stockChart.options.user.timeShare && !stockChart.shortcutIsShow && kc == 38){
			return stockChart.setLevel(1);
		}
		if(!stockChart.options.user.timeShare && !stockChart.shortcutIsShow && kc == 40){
			return stockChart.setLevel(-1);
		}
		if(stockChart.options.user.timeShare && !stockChart.shortcutIsShow && kc== 13){
			stockChart.kt('k');
		}
		if (getStop(tar) || kc == 116 || kc == 123 || (kc == 8 && !isFocus)) return;
	}

	function lots_Event(e) {
		var e = window.event || e,
			kc = e.keyCode,
			tar = Event.target(e),
			act = tar.getAttribute('act');
		if (getStop(tar) || kc == 116 || kc == 123 || (kc == 8 && !isFocus)) return;

		if (_this[act]) {
			return _this[act](e);
		}
		Event.stop(e);
	}

	function keyboardEvent(e) {
		var tar = Event.target(e);
		if(getStop(tar))return;
		once_Event(e);
		lots_Event(e);
	}

	function keyboardEvent2(e) {
		var tar = Event.target(e);
		if(getStop(tar))return;
		lots_Event(e);
	}

	function clickEvent(e) {
		var e = window.event || e,
			tar = Event.target(e),
			act = attFather(tar, 'act'),
			shortcut = attFather(tar, 'shortcut'),
			stop = attFather(tar, 'stop'),
			data = attFather(tar, 'data-val');
		//console.log(data,act,stop)
		stop && Event.stop(e);
		!shortcut && _this.shortcutIsShow && _this.shortcutHidden();
		_this[act] && _this[act](tar, data, e);
	}

	function doubleClickEvent(e) {
		return;
		if (doubleWait) {
			_this.doubleAction();
		}
		doubleWait = 1;
		setTimeout(function() {
			doubleWait = 0;
		}, stockChart.options.user.doubleClick)
	}

	! function(id, opt) {
		'construct';
		keyEvent();

		Event.add(box, 'mouseover', _this.mouseover);
		Event.add(box, 'mousemove', _this.mousemove);
		Event.add(box, 'mouseout', _this.mouseout);

	}.apply(window, arguments);
}