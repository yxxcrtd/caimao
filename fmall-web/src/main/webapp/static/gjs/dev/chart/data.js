// @koala-append "_http.api.js"
// @koala-append "_fire.data.js"
function data(ev, c, m, isSelf) {
	var _this = this,
		r = request(),
		fire = {},
		api = requestAPI(),
		html = {
			cool: {
				dom: c,
				str: c.querySelector('script').innerHTML
			},
			move: {
				dom: m,
				str: m.querySelector('script').innerHTML
			}
		},
		requestRelease = 1,
		autoTimer,
		start, lens, code, rank, ccode;

	function reInit(update) {
		requestRelease = 0;
		r.requestPayload({
			url: location.protocol + "//" + ev.st().options.url.xhr,
			data: api.data,
			success: function(data) {
				var l, isRight;
				if (!data.o) return;
				requestRelease = 1;
				dataHandle(data, update);
			}
		});
	}

	function autoget() {
		autoTimer && clearTimeout(autoTimer);
		autoTimer = null;
		api.release();
		api.stockList(start, lens, code, rank);
		requestRelease && reInit(1);

		autoTimer = setTimeout(autoget, 5000);
	}

	fire.data155 = function(d, u) {
		html.cool.dom.innerHTML = rentmpl(html.cool.str, {
			data: d.o,
			code: ccode
		});
		html.move.dom.innerHTML = rentmpl(html.move.str, {
			data: d.o,
			code: ccode
		});
		ev.load();
	}

	fire.data161 = function(d, u) {
		if (!d) return;
		MAP = JSON.stringify(d.o);
		localStorage.setItem('stockMap', MAP);
		localStorage.setItem('ver', totime(new Date() * 1, 'ymd'));
	}

	function reload(s, l, c, r) {
		start = s || start;
		lens = l || lens;
		code = c || code;
		rank = r || rank;
		api.release();
		api.stockList(start, lens, code, rank);
		reInit();
	}

	function setItem(n) {
		ccode = n;
	}

	function dataHandle(data, isupdate) {
		var o = data.o,
			l = o.length,
			fn;
		for (; l--;) {
			fn = 'data' + o[l].act;
			if (o[l].h == 200)
				fire[fn] && fire[fn](o[l], isupdate);
		}
	}! function(ev, c, m, isSelf) {
		'construct';
		/*
20 //成交量
30 //成交金额
49 //代码
46 //换手
*/
		var date = totime(new Date() * 1, 'ymd'),
			localDate = localStorage.getItem('ver');
		start = 0,
		lens = 100,
		code = 70,
		rank = 10;

		api.stockList(start, lens, code, rank);
		reInit();

		(date != localDate) && api.map() && reInit();
		if (date == localDate) {
			MAP = localStorage.getItem('stockMap');
		}

		ev.returnFn(reload, lens, setItem)
		autoget();
	}.apply(window, arguments);
}

function listEvent(charts, obj, filter) {
	var isfirst = 1,
		code, cur, timer, isClick, ul1, ul2, filterBtn, filterList, reloadFilter, v, header, lens, fnSI;

	function wait() {
		isClick = 0;
	}

	function load() {
		ul1 = document.querySelector('#coolTmp').querySelectorAll('ul');
		ul2 = document.querySelector('#moveList').querySelectorAll('ul');
	}

	function returnChart() {
		return charts.chart1.stockChart;
	}

	function returnFn(fn, l, setItem) {
		reloadFilter = fn;
		lens = l;
		fnSI = setItem;
	}! function() {
		filterBtn = filter.querySelector('span'),
		filterList = filter.querySelector('ul');
		v = 10;
		header = document.querySelector("#list");
		Event.add(header, 'click', function(e) {
			var tar = Event.target(e),
				act = tar.getAttribute('act');

			if (act == 'changeRank') {
				if (v == tar.getAttribute('data-val')) {
					v = tar.getAttribute('data-val2');
					tar.setAttribute('data-val2', tar.getAttribute('data-val'))
					tar.setAttribute('data-val', v)
				} else {
					v = tar.getAttribute('data-val');
					tar.setAttribute('data-val', tar.getAttribute('data-val2'))
					tar.setAttribute('data-val2', v)
				}
				lens = 100;
				obj.scrollTop = 0;
				reloadFilter && reloadFilter(null, lens, null, v)
				v = tar.getAttribute('data-val');
				return;
			}
		})
		Event.add(obj, 'click', function(e) {
			var tar = Event.target(e),
				ul = tarFather(tar, 'ul'),
				uls, idx;
			if (!ul || ul.nodeName.toLowerCase() != 'ul' || !ul.getAttribute('code')) return;
			uls = ul.parentNode.getElementsByTagName("ul")
			if (!isfirst) {
				if (uls[cur.getAttribute('idx')].getAttribute('code') != cur.getAttribute('code')) {
					for (var i = 0, l = uls.length; i < l; i++) {
						if (uls[i].getAttribute('code') == code) {
							cur = uls[i];
							break;
						}
					}
				}
			}
			timer && clearTimeout(timer);
			if (ul != cur) {
				if (cur) {
					idx = cur.getAttribute('idx');
					dom.hasClass(ul1[idx], 'on') && dom.removeClass(ul1[idx], 'on');
					dom.hasClass(ul2[idx], 'on') && dom.removeClass(ul2[idx], 'on');
				}
				cur = ul;
				idx = cur.getAttribute('idx');
				!dom.hasClass(ul1[idx], 'on') && dom.addClass(ul1[idx], 'on');
				!dom.hasClass(ul2[idx], 'on') && dom.addClass(ul2[idx], 'on');
			} else {
				if (isClick) {
					window.location.href = 'stock.html?id=' + ul.getAttribute('code') + '&ex=' + ul.getAttribute('ex');
				}!dom.hasClass(cur, 'on') && dom.addClass(cur, 'on');
			}
			charts.chart1.toStock(ul.getAttribute('code'), ul.getAttribute('ex'))
			charts.chart2.toStock(indcode[ul.getAttribute('ex')], ul.getAttribute('ex'))
			isClick = 1;
			timer = setTimeout(wait, 300);
			fnSI(code = ul.getAttribute('code'));
			isfirst = 0;
		})
		Event.add(filter, 'click', function(e) {
			var tar = Event.target(e),
				val = tar.getAttribute('data-val');
			if (tar.nodeName.toLowerCase() == 'span') {
				!dom.hasClass(filterList, 'on') ? dom.addClass(filterList, 'on') : dom.removeClass(filterList, 'on');
			} else {
				lens = 100;
				obj.scrollTop = 0;
				reloadFilter && reloadFilter(null, lens, val);
				filterBtn.innerHTML = tar.innerHTML;
				!dom.hasClass(filterList, 'on') ? dom.addClass(filterList, 'on') : dom.removeClass(filterList, 'on');
			}
		});
		Event.add(obj, 'scroll', function() {
			var height = lens * 30;
			if (obj.offsetHeight + obj.scrollTop + 300 > height) {
				lens += 100;
				reloadFilter(null, lens)
			}
		})
	}();
	return {
		load: load,
		st: returnChart,
		returnFn: returnFn
	}
}