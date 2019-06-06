define([
    'dojo/_base/declare',
	'dojox/data/QueryReadStore',
	'app/common/Ajax'
], function(declare, QueryReadStore, Ajax) {
	return declare([QueryReadStore], {
		// chained constructor compatible with the legacy declare(),
		// first call the parents' constructor, then self
		constructor: function(options) {
			// add new property 
		},
		_xhrFetchHandler: function(data, request, fetchHandler, errorHandler) {
			data = this._filterResponse(data);
			if(data.label){
				this._labelAttr = data.label;
			} 
			data = data.data;
			var numRows = data.numRows || data.length;

			this._items = [];
			// Store a ref to "this" in each item, so we can simply check if an item
			// really origins form here (idea is from ItemFileReadStore, I just don't know
			// how efficient the real storage use, garbage collection effort, etc. is).
			dojo.forEach(data,function(e){
				this._items.push({i:e, r:this});
			},this);
			
			var identifier = data.identifier;
			this._itemsByIdentity = {};
			if(identifier){
				this._identifier = identifier;
				var i;
				for(i = 0; i < this._items.length; ++i){
					var item = this._items[i].i;
					var identity = item[identifier];
					if(!this._itemsByIdentity[identity]){
						this._itemsByIdentity[identity] = item;
					}else{
						throw new Error(this._className+":  The json data as specified by: [" + this.url + "] is malformed.  Items within the list have identifier: [" + identifier + "].  Value collided: [" + identity + "]");
					}
				}
			}else{
				this._identifier = Number;
				for(i = 0; i < this._items.length; ++i){
					this._items[i].n = i;
				}
			}
			
			// TODO actually we should do the same as dojo.data.ItemFileReadStore._getItemsFromLoadedData() to sanitize
			// (does it really sanititze them) and store the data optimal. should we? for security reasons???
			numRows = this._numRows = (numRows === -1) ? this._items.length : numRows;
			fetchHandler(this._items, request, numRows);
			this._numRows = numRows;
			
			this.loaded = true;
		}
	});	
});