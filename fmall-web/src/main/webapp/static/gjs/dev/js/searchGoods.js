function showSearch(id,fire,code){
	var _this = {};
	_this.box = document.querySelector(id);
	_this.input = _this.box.querySelector('input');
	_this.btn = _this.box.querySelector('dt');
	_this.search = _this.box.querySelector('dd');
	_this.req = fire,
	autoLoop = 0;
	J(setRank,_this,code);
	J(_ssEvent,_this);
	J(_ssCommunication,_this);
}

function setRank(code){
	var _this = this,
		codeArr = code.split("."),
		ex = codeArr.pop(),
		code = codeArr.join("."),
		prv,next;
	h.request(h.api.query_goods_list(ex,{callback : function(d){
		var data;
		if(d.success){
			data = d.data.result;
			for(var i = 0, l = data.length; i < l; i++){
				if(data[i].prodCode == code){
					break;
				}
				prv = data[i].prodCode;
			}
			prv = prv ? prv : data[l-1].prodCode;
			next = data[i+1] ? data[i+1].prodCode : data[0].prodCode;
			_this.box.setAttribute("prv",prv+"."+ex);
			_this.box.setAttribute("next",next+"."+ex);
		}
	}}))
}
function locationGo(tar,box,e){
	Event.stop(e);
	var arr = tar.getAttribute("data");
		code = box.getAttribute(arr),
		tab = $_GET("tab") || 1;
	window.location.href = "?code="+encodeURIComponent(code)+"&tab="+tab;
}
function _ssEvent(){
	var _this = this;
	Event.add(_this.btn,'click',function(e){
		var tar = Event.target(e);
		if(tar.getAttribute('action') == 'default')return locationGo(tar,_this.box,e);
		Event.stop(e);
		_this.search.style.display = "block";
		_this.input.focus();
		autoLoop = 1;

	})
	Event.add(_this.search,'click',function(e){
		var tar = Event.target(e);
		if(tar.getAttribute('action') == 'default')return locationGo(tar,_this.box,e);
		Event.stop(e);
	})
	Event.add(document,'click',function(){
		_this.search.style.display = "none";
		_this.clear();
	})
}

function _ssCommunication(){
	var _this = this,val;
	_this.clear = function(){
		autoLoop = 0;
		val = null;
	};
	!function(){
		var __this = arguments.callee,__arguments = arguments;
		setTimeout(function() {
			__this.apply(this,__arguments)
		}, 500);
		if(!autoLoop)return;
		if(val == _this.input.value || _this.input.value == "")return;
		val = _this.input.value;
		h.request(h.api.query_wizard(val,_this.req));
	}()
}