function getLocalFav() {
	var fav, objFav = {};

	function add(code) {
		var tmp = [];
		objFav[code] = 1;
		for (var k in objFav) {
			tmp.push(k);
		}
		localStorage.setItem("metalFav", tmp.join(","));
	}

	function del(code) {
		var tmp = [];
		delete objFav[code];
		for (var k in objFav) {
			tmp.push(k);
		}
		localStorage.setItem("metalFav", tmp.join(","));
	}

	function check(code) {
		return objFav[code]
	}! function() {
		fav = localStorage.getItem('metalFav');
		if (fav) {
			fav = fav.split(",");
		} else {
			fav = [];
		}
		for (var i = 0, l = fav.length; i < l; i++) {
			objFav[fav[i]] = 1;
		}
	}()
	return {
		check: check,
		add: add,
		del: del
	};
}

function getCloudFav(ex, code, favBtn) {
	var fav, objFav = {},
		ex = ex,
		favBtn = favBtn,
		code = code;

	function add() {
		h.option = h.api.add_fav_goods(token, ex, code, {
			callback: function(d) {
				if (d.success) {
					favBtn.innerHTML = "<i></i>取消自选";
					favBtn.className = "btn fav unfav";
					objFav[ex + "." + code] = 1;
				}
			}
		});
		h.request(h.option);
	}

	function del() {
		h.option = h.api.del_fav_goods(token, ex, code, {
			callback: function(d) {
				if (d.success) {
					favBtn.innerHTML = "<i></i>加入自选";
					favBtn.className = "btn fav";
					delete objFav[ex + "." + code];
				}
			}
		});
		h.request(h.option);
	}

	function check() {
		return objFav[ex + "." + code];
	}! function() {
		h.option = h.api.query_fav_goods(token, {
			callback: function(d) {
				for (var i = 0, l = d.data.length; i < l; i++) {
					objFav[d.data[i].exchange + "." + d.data[i].prodCode] = 1;
				}
				if (check()) {
					favBtn.innerHTML = "<i></i>取消自选";
					favBtn.className = "btn fav unfav";
				} else {
					favBtn.innerHTML = "<i></i>加入自选";
					favBtn.className = "btn fav";
				}
			}
		});
		h.request(h.option);
	}()
	return {
		check: check,
		add: add,
		del: del
	};
}

function fireGoodBtns(id, tab, code) {
	var box = document.querySelector(id),
		favBtn = box.querySelector("span"),
		returnBtn = box.querySelector("a"),
		codeArr = code.split("."),
		fav;
	returnBtn.href = "market.html?tab=" + tab;
	if (isLogin) {
		fav = getCloudFav(codeArr.pop(), codeArr.join("."), favBtn);
		Event.add(favBtn, 'click', function() {
			if (fav.check()) {
				fav.del();
			} else {
				fav.add();
			}
		})

	} else {
		fav = getLocalFav()
		if (fav.check(code)) {
			favBtn.innerHTML = "<i></i>取消自选";
			favBtn.className = "btn fav unfav";
		} else {
			favBtn.innerHTML = "<i></i>加入自选";
			favBtn.className = "btn fav";
		}
		Event.add(favBtn, 'click', function() {
			if (fav.check(code)) {
				favBtn.innerHTML = "<i></i>加入自选";
				favBtn.className = "btn fav";
				fav.del(code);
			} else {
				favBtn.innerHTML = "<i></i>取消自选";
				favBtn.className = "btn fav unfav";
				fav.add(code);
			}
		})
	}
}

function switchbtn(e, t, a) {
	function n(e) {
		var a = Event.target(e);
		a != r && a.nodeName.toLowerCase() == t.toLowerCase() && (r.className = "btn", r = a, r.className = "on btn")
	}
	var o = document.querySelector(e),
		c = o.querySelectorAll(t),
		r = c[(a || 1) - 1];
	r.className = "btn on", Event.add(o, "click", function(e) {
		n(e)
	})
}