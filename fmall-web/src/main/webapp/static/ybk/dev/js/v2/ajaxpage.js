(function() {
	var template = ['<div class="pages"><table><tbody><tr><td><div class="pagebox"><div>', '</div></div></td></tr></tbody></table></div>'],
		create = {
			Class: function() {
				return function() {
					this.__construct.apply(this, arguments);
				}
			}
		},
		Bind = function(o, f, a) {
			return f.call(o, a)
		},
		_$ = function(o, t) {
			var o = typeof o != "string" ? o : document.getElementById(o);
			if (!t) {
				return o;
			} else {
				return o.getElementsByTagName(t);
			}
		},
		pages = create.Class(),
		_instance = null;

	function clickAction(obj) {
		var tar;
		Event.add(this, 'click', function(e) {
			Event.stop(e);
			e = e || window.event;
			tar = e.target || e.srcElement;
			if (tar.nodeName == "A") {
				obj.isdraw = false;
				obj.draw(getNum());
			}
		});

		function getNum() {
			if (tar.className) {
				if (tar.className == "prepage") {
					return (obj.current - 1);
				} else if (tar.className == "nextpage") {
					return (obj.current - (-1));
				}
			} else {
				return parseInt(tar.innerHTML);
			}
			return obj.current;
		}
		return this;
	}
	pages.prototype = {
		__construct: function(obj, total, fn, itemLength, current, isdraw) {
			if (total < 2) return (fn && !isdraw && fn(1));
			this.total = total;
			this.cooldown = false;
			this.current = current || 1;
			this.itemLength = itemLength || 5;
			this.fn = fn;
			this.first = false;
			this.isdraw = isdraw;
			this.obj = Bind(_$(obj), clickAction, this);
			this.draw(this.current);
		},
		set: function(obj, total, fn, itemLength, current, isdraw) {
			if (total < 2) return (fn && !isdraw && fn(1));
			this.total = total;
			this.cooldown = false;
			this.current = current || 1;
			this.itemLength = itemLength || 5;
			this.fn = fn;
			this.first = false;
			this.isdraw = isdraw;
			this.obj = Bind(_$(obj), clickAction, this);
			this.draw(this.current);
		},
		draw: function(n,force) {
			var html = "",
				temHtml = "",
				_this = this;
			if (!force && ((this.current === n && this.first) || this.cooldown === true)) return;
			n = n < 0 ? 0 : (n > this.total ? this.total : n);
			!this.isdraw && (this.cooldown = true);
			this.current = n;
			this.first = true;
			if (n != 1) {
				html += '<a href="" class="prepage">上一页</a>'
			} else {
				html += ''
			};
			if (this.total > this.itemLength && !(n - Math.ceil(this.itemLength / 2) <= 0)) html += '<a href="">1</a><i>...</i>';
			if (this.total > this.itemLength) {
				if (n - Math.ceil(this.itemLength / 2) <= 0) {
					for (var i = 0, j; i < this.itemLength; i++) {
						j = i + 1;
						if (j == n) {
							html += '<span class="pager_cur">' + n + '</span>';
						} else {
							html += '<a href="">' + j + '</a>';
						}
					}
				} else if (this.total - Math.ceil(this.itemLength / 2) <= n) {
					for (var i = 1; i < this.itemLength; i++) {
						if (n === this.total) {
							i--;
							break;
						}
						temHtml += '<a href="">' + (n + i) + '</a>';
						if (n + i === this.total) break;
					}
					i++;
					for (var j = this.itemLength - i; i < this.itemLength; i++) {
						html += '<a href="">' + (n - j) + '</a>';
						j--;
					}
					html += '<span class="pager_cur">' + n + '</span>';
					html += temHtml;
				} else {
					for (var i = -1 * Math.floor(this.itemLength / 2); i < Math.ceil(this.itemLength / 2); i++) {
						if (i == 0) {
							html += '<span class="pager_cur">' + n + '</span>';
						} else {
							html += '<a href="">' + (n + i) + '</a>';
						}
					}
				}
			} else {
				for (var i = 1; i <= this.total; i++) {
					if (i == n) {
						html += '<span class="pager_cur">' + n + '</span>';
					} else {
						html += '<a href="">' + i + '</a>';
					}
				}
			}
			if (this.total > this.itemLength && !(this.total - Math.ceil(this.itemLength / 2) <= n)) html += '<i>...</i><a href="">' + this.total + '</a>';
			if (n != this.total) {
				html += '<a href="" class="nextpage">下一页</a>'
			} else {
				html += ''
			};

			this.obj.innerHTML = template[0] + html + template[1];
			this.fn && !this.isdraw && this.fn(n, function() {
				_this.releaseClick.call(_this)
			});
		},
		releaseClick: function() {
			this.cooldown = false;
		}
	};
	window['ajaxPages'] = function(obj, total, fn, itemLength, current, isdraw) {
		if (!_instance) _instance = new pages(obj, total, fn, itemLength, current, isdraw);
		return _instance;
	};
})();