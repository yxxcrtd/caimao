define([ 'dojo/_base/declare',
         'dojox/data/QueryReadStore'    
         ], 
function (declare, QueryReadStore,Tooltip) {
	return declare(QueryReadStore, {
		
		_fetchItems: function(request, fetchHandler, errorHandler){
			// summary:
			//		The request contains the data as defined in the Read-API.
			//		Additionally there is following keyword "serverQuery".
			//
			//		####The *serverQuery* parameter, optional.
			//
			//		This parameter contains the data that will be sent to the server.
			//		If this parameter is not given the parameter "query"'s
			//		data are sent to the server. This is done for some reasons:
			//
			//		- to specify explicitly which data are sent to the server, they
			//		  might also be a mix of what is contained in "query", "queryOptions"
			//		  and the paging parameters "start" and "count" or may be even
			//		  completely different things.
			//		- don't modify the request.query data, so the interface using this
			//		  store can rely on unmodified data, as the combobox dijit currently
			//		  does it, it compares if the query has changed
			//		- request.query is required by the Read-API
			//
			//		I.e. the following examples might be sent via GET:
			//	|	  fetch({query:{name:"abc"}, queryOptions:{ignoreCase:true}})
			//		  the URL will become:   /url.php?name=abc
			//
			//	|	  fetch({serverQuery:{q:"abc", c:true}, query:{name:"abc"}, queryOptions:{ignoreCase:true}})
			//		  the URL will become:   /url.php?q=abc&c=true
			//	|	  // The serverQuery-parameter has overruled the query-parameter
			//	|	  // but the query parameter stays untouched, but is not sent to the server!
			//	|	  // The serverQuery contains more data than the query, so they might differ!

			var serverQuery = request.serverQuery || request.query || {};
			var name = serverQuery.name.replace(/\W.*/,"");
			var regex = /^[\da-zA-Z]+$/;
			if(name == "" || !regex.test(name)){
				return;
			}

			// Compare the last query and the current query by simply json-encoding them,
			// so we dont have to do any deep object compare ... is there some dojo.areObjectsEqual()???
			if( this._lastServerQuery !== null &&
				dojo.toJson(serverQuery) == dojo.toJson(this._lastServerQuery)
				){
				this._numRows = (this._numRows === -1) ? this._items.length : this._numRows;
				fetchHandler(this._items, request, this._numRows);
			}else{
				var xhrFunc = this.requestMethod.toLowerCase() == "post" ? dojo.xhrPost : dojo.xhrGet;
				var xhrHandler = xhrFunc({url:this.url + "/" + name, handleAs:"json-comment-optional" , failOk: true});
				request.abort = function(){
					xhrHandler.cancel();
				};
				xhrHandler.addCallback(dojo.hitch(this, function(data){
					this._xhrFetchHandler(data, request, fetchHandler, errorHandler);
				}));
				xhrHandler.addErrback(function(error){
					errorHandler(error, request);
				});
				// Generate the hash using the time in milliseconds and a randon number.
				// Since Math.randon() returns something like: 0.23453463, we just remove the "0."
				// probably just for esthetic reasons :-).
				this.lastRequestHash = new Date().getTime()+"-"+String(Math.random()).substring(2);
				this._lastServerQuery = dojo.mixin({}, serverQuery);
			}
		},
		
		_xhrFetchHandler: function(data, request, fetchHandler, errorHandler){
			data = this._filterResponse(data);
			if(data.success){
				data = data.data;
				
				var tempData = [];
				dojo.forEach(data,function(e){
					var item = {};
					item.id = e.stockCode;
					item.name = e.stockCode + " " + e.stockName;
					tempData.push(item);
				},this);
				
				data = tempData;
			}else{
				data = [];
			}
						
			var numRows = data.length;
			
			this._items = [];
			// Store a ref to "this" in each item, so we can simply check if an item
			// really origins form here (idea is from ItemFileReadStore, I just don't know
			// how efficient the real storage use, garbage collection effort, etc. is).
			dojo.forEach(data,function(e){
				this._items.push({i:e, r:this});
			},this);
			
			this._itemsByIdentity = {};

			this._identifier = Number;
			for(var i = 0; i < this._items.length; ++i){
				this._items[i].n = i;
			}
			
			// TODO actually we should do the same as dojo.data.ItemFileReadStore._getItemsFromLoadedData() to sanitize
			// (does it really sanititze them) and store the data optimal. should we? for security reasons???
			numRows = this._numRows = (numRows === -1) ? this._items.length : numRows;
			fetchHandler(this._items, request, numRows);
			this._numRows = numRows;
		}
	});
});