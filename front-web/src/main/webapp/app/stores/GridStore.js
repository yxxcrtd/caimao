/**
 * suit for grid to manage the data sync with server
 * CRUD always use the post method to request
 * */
define([
    'dojo/_base/declare',
	'dojox/data/JsonRestStore',
	'app/common/Ajax',
	'dojo/_base/lang'
], function(declare, JsonRestStore, Ajax, lang) {
	return declare([JsonRestStore], {
		// chained constructor compatible with the legacy declare(),
		// first call the parents' constructor, then self
		constructor: function(options) {
			// add new property
		},
		
		msgTitle: 'Grid Action',
		msgLoadSuccess: 'Load successfully',
		msgCreateSuccess: 'Create successfully',
		msgUpdateSuccess: 'Update successfully',
		msgDeleteSuccess: 'Delete successfully',
		
		// consist the post body
		getData: function(method, args) {
			var params = args.query || {};
			if (method === 'get') {
				if(args && (args.start >= 0 || args.count >= 0)){
					params.start = args.start || 0;
					params.limit = args.count;
				}
				if (args && args.count == Infinity) {
					delete params.start;
					delete params.limit;
				}
				if(args && args.sort && !args.queryStr){
					for(var i = 0; i<args.sort.length; i++){
						//TODO this only support one column order now
						var sort = args.sort[i];
						params.order_field = sort.attribute;
						params.is_asc = !sort.descending;
					}
				}
			}
			return params;
		},
		
		// get data by post method
		fetch: function(args) {
			args = args || {};

			if("syncMode" in args ? args.syncMode : this.syncMode){
				dojox.rpc._sync = true;
			}
			var self = this;
			
			// feedback message
			var data = self.getData('get', args);
			//data.title = self.msgTitle;
			//data.msg = self.msgLoadSuccess;

			var scope = args.scope || self;
			var defResult = Ajax.get(this.target, data);
			var is_hide_mobile = this.target.indexOf('/user/queryRefUser') >= 0;
			defResult.request = args;
			defResult.then(function(results) {
				if(args && (args.start >= 0 || args.count >= 0)){
					self.grid.page = {
							start: args.start,
							count: args.count
					}; // hold on page params
				}
				if(args.clientFetch){
					results = self.clientSideFetch({query:args.clientFetch,sort:args.sort,start:args.start,count:args.count},results);
				}
				// show success false message
				if (!results.success) {
					args.onError && args.onError.call(scope, results.msg, args);
					return results;
				}
				var resultSet = self._processResults(results, self.grid.page);
				if(is_hide_mobile && resultSet.items.length > 0){
					for(var kk in resultSet.items){
						resultSet.items[kk].mobile = resultSet.items[kk].mobile.replace(resultSet.items[kk].mobile.substr(3,4),"****");
					}
				}
				results = args.results = resultSet.items;
				if(args.onBegin){
					args.onBegin.call(scope, resultSet.totalCount, args);
				}
				if(args.onItem){
					for(var i=0; i<results.length;i++){
						args.onItem.call(scope, results[i], args);
					}
				}
				if(args.onComplete){
					args.onComplete.call(scope, args.onItem ? null : results, args);
				}
				return results;
			}, function(err) {
				args.onError && args.onError.call(scope, err, args);
			});
			args.abort = function(){
				// abort the request
				defResult.cancel();
			};
			args.store = this;
			return args;
		},
		
		// post delete
		deleteItem: function(data) {
			var me = this,
				url = me.target + 'delete',
				grid = me.grid;
			data.title = self.msgTitle;
			data.msg = self.msgDeleteSuccess;
			Ajax.post(url, data).then(function(response) {
				me.onSuccess();
			}, function(error) {
				alert('error');
			});
		},
		
		// post update
		updateItem: function(data) {
			var me = this,
				url = me.target + 'update',
				grid = me.grid;
			data.title = self.msgTitle;
			data.msg = self.msgUpdateSuccess;
			Ajax.post(url, data).then(function(response) {
				me.onSuccess();
			}, function(error) {
				alert('error');
			});
		},
		
		// post create
		newItem: function(data) {
			var me = this,
				url = me.target + 'save',
				grid = me.grid;
			data.title = self.msgTitle;
			data.msg = self.msgCreateSuccess;
			Ajax.post(url, data).then(function(response) {
				me.onSuccess();
			}, function(error) {
				alert('error');
			});
		},
		
		onSuccess: function() {
			var grid = this.grid;
			grid._fetch(0, true);
		},
		
		_processResults: function(results, page){
			// index the results
            var isItem = !!results.data.items;
			results.data.items = results.data.items || [];
			var items = isItem ? results.data.items : results.data;
			var count = items ? items.length : 0;
			this.items = items;
			// if we don't know the length, and it is partial result, we will guess that it is twice as big, that will work for most widgets
			return {totalCount:results.data.totalCount ||
                (page.count == count ? (page.start || 0) + count * 2 : count), items: items};
		}
	});
});