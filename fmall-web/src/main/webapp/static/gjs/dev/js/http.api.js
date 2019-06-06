function _api(){
	var _this = this;
	_this.api = {
		notice_list : function(limit,page,callback,type,isHot){
			var data = {};
			data.limit = limit;
			data.page = page;
			type && ( data.type = type );
			isHot && ( data.isHot = isHot );
			return {
				api : '/api/content/gjs_notice_list',
				data : data,
				fn : callback.callback || function(){}
			}
		},
		notice_content : function(id,callback){
			var data = {};
			data.id = id;
			return {
				api : '/api/content/gjs_notice',
				data : data,
				fn : callback.callback || function(){}
			}
		},
		flash_list : function(limit,page,callback,type,isHot){
			var data = {};
			data.limit = limit;
			data.page = page;
			type && ( data.type = type );
			isHot && ( data.isHot = isHot );
			return {
				api : '/api/content/gjs_jin10_list',
				data : data,
				fn : callback.callback || function(){}
			}
		},
		query_hot_goods : function(callback){
			return {
				api : '/api/gjs_trade/queryHotGoodsList',
				data : {},
				fn : callback.callback || function(){}
			}
		},		
		query_goods_list : function(exchange,callback){
			var data = {};
			data.exchange = exchange;
			data.dataType = 0;
			return {
				api : '/api/gjs_trade/queryGoodsList',
				data : data,
				fn : callback.callback || function(){}
			}
		},
		query_lots_goods : function(data,callback){
			data.prodCode = encodeURIComponent(data.prodCode);
			return {
				api : '/api/gjshq/snapshot/query',
				data : data,
				fn : callback.callback || function(){}
			}
		},
		query_good : function(prodCode,callback){
			var data = {};
			data.prodCode = encodeURIComponent(prodCode);
			return {
				api : "/api/gjshq/ticker/query",
				data : data,
				fn : callback.callback || function(){}
			}
		},
		query_history : function(prodCode,lens,callback){
			var data = {};
			data.prodCode = encodeURIComponent(prodCode);
			data.number = lens;
			return {
				api : "/api/gjshq/snapshot/tradeAmountQueryHistory",
				data : data,
				fn : callback.callback || function(){}
			}
		},
		query_funds_njs : function(token,callback){
			var data = {};
			data.token = token;
			data.exchange = "NJS";
			return {
				api : "/api/gjs_trade/queryNJSFunds",
				data : data,
				fn : callback.callback || function(){}
			}
		},
		query_funds_sjs : function(token,callback){
			var data = {};
			data.token = token;
			data.exchange = "SJS";
			return {
				api : "/api/gjs_trade/querySJSFunds",
				data : data,
				fn : callback.callback || function(){}
			}
		},
		do_open_account_NJS : function(data,callback){
			return {
				api : "/api/gjs_account/doOpenAccountNJS",
				data : data,
				fn : callback.callback || function(){}
			}
		},
		query_fav_goods : function(token,callback){
			var data = {};
			data.token = token;
			return {
				api : "/api/gjshq/ownProduct/get",
				data : data,
				fn : callback.callback || function(){}
			}
		},
		add_fav_goods : function(token,ex,code,callback){
			var data = {};
			data.token = token;
			data.exchange = encodeURIComponent(ex);
			data.prodCode = encodeURIComponent(code);
			data.prodName = "";
			return {
				api : "/api/gjshq/ownProduct/save",
				data : data,
				fn : callback.callback || function(){}
			}
		},
		del_fav_goods : function(token,ex,code,callback){
			var data = {};
			data.token = token;
			data.data = encodeURIComponent(JSON.stringify([{exchange:ex,prodCode:code}]));
			return {
				api : "/api/gjshq/ownProduct/delete",
				data : data,
				fn : callback.callback || function(){}
			}
		},
		query_wizard : function(field,callback){
			var data = {};
			data.field = encodeURIComponent(field);
			return {
				api : "/api/gjshq/ownProduct/wizard",
				data : data,
				fn : callback.callback || function(){}
			}
		},
		get_user_info : function(token,callback){
			var data = {};
			data.token = encodeURIComponent(token);
			return {
				api : "/api/user/get_user_info",
				data : data,
				fn : callback.callback || function(){}
			}
		}

	}
}