/**
 * generic grid, extend from enhanced grid
 * specific: paging, set title, CRUD action, sort, query
 * */
define([
	'dojo/_base/declare',
    'dojox/grid/EnhancedGrid',
    'dojo/dom-construct',
    './GenericPagination',
    './GenericGridButton',
    'dojo/on',
    'dojo/mouse',
    'dojo/query',
    'dojox/grid/enhanced/plugins/IndirectSelection',
    'dijit/Dialog',
    'dojox/layout/TableContainer',
    'dijit/form/TextBox',
    'dijit/form/NumberTextBox',
    'dijit/form/Form',
    'dojo/_base/array',
    'dijit/registry',
    'dijit/place',
	'dojo/dom-geometry',
	'dojo/dom-style',
	'dojo/dom-attr',
	'dojo/_base/html',
    'dojo/has'
], function (declare, EnhancedGrid, domConstruct, Pagination, Button, on, mouse, query, IndirectSelection, 
		Dialog, TableContainer, TextBox, NumberTextBox, Form, array, registry, place, domGeometry, domStyle, domAttr, html, has) {
	return declare(EnhancedGrid, {
		autoHeight: true, // adapt the height
		queryParams: {}, // header query
		
		//override
		_onFetchBegin: function(size, req){
			if(!this.scroller){ return; }
			if(this.rowCount != size){
				if(req.isRender){
					this.scroller.init(size, this.keepRows, this.rowsPerPage);
					this.rowCount = size;
					this._setAutoHeightAttr(this.autoHeight, true);
					this._skipRowRenormalize = true;
					this.prerender();
					this._skipRowRenormalize = false;
				}else{
					this.updateRowCount(size);
				}
			}
			if(!size){
				// not allow the rerender the view
				//this.views.render();
				//this._resize();
				this.showMessage(this.noDataMessage);
				//this.focus.initFocusView();
			}else{
				this.showMessage();
			}
		},
		
		// override keep scroll position
		_refresh: function(isRender){
			var st = document.body.scrollTop;
			this._clearData();
			this._fetch(0, isRender);
			document.body.scrollTop = st;
		},
		
		// override
		_onFetchComplete: function(items, req){
			if(!this.scroller){ return; }
			if(items && items.length > 0){
				array.forEach(items, function(item, idx){
					this._addItem(item, req.start+idx, true);
				}, this);
				this.updateRows(req.start, items.length);
				if(req.isRender){
					this.setScrollTop(0);
					this.postrender();
				}else if(this._lastScrollTop){
					this.setScrollTop(this._lastScrollTop);
				}
				if(has("ie")){
					html.setSelectable(this.domNode, this.selectable);
				}	
				// adjust the width after render
				//this.adaptHeight(this._getHeaderHeight());
				//this.adaptWidth();
			}
			delete this._lastScrollTop;
			if(!this._isLoaded){
				this._isLoading = false;
				this._isLoaded = true;
			}
			this._pending_requests[req.start] = false;
			
			// fix the height when no items
			if (items.length == 0) {
				this.viewsNode.style.height = '1px';
			}
		},
		
		// override, in order to implement showall(nopage)
		_storeLayerFetch: function(req){
			// summary:
			//		Extracted fetch specifically for store layer use
			if (!this.get('pageSize')) {
				req.count = Infinity;
			}
			this.store.fetch(req);
		},
		
		// override
		_resize: function(changeSize, resultSize){
			// Restore our pending values, if any
			changeSize = changeSize || this._pendingChangeSize;
			resultSize = resultSize || this._pendingResultSize;
			delete this._pendingChangeSize;
			delete this._pendingResultSize;
			// if we have set up everything except the DOM, we cannot resize
			if(!this.domNode){ return; }
			var pn = this.domNode.parentNode;
			if(!pn || pn.nodeType != 1 || !this.hasLayout() || pn.style.visibility == "hidden" || pn.style.display == "none"){
				return;
			}
			// useful measurement
			var padBorder = this._getPadBorder();
			var hh = undefined;
			var h;
			// grid height
			if(this._autoHeight){
				this.domNode.style.height = 'auto';
			}else if(typeof this.autoHeight == "number"){
				h = hh = this._getHeaderHeight();
				h += (this.scroller.averageRowHeight * this.autoHeight);
				this.domNode.style.height = h + "px";
			}else if(this.domNode.clientHeight <= padBorder.h){
				if(pn == document.body){
					this.domNode.style.height = this.defaultHeight;
				}else if(this.height){
					this.domNode.style.height = this.height;
				}else{
					this.fitTo = "parent";
				}
			}
			// if we are given dimensions, size the grid's domNode to those dimensions
			if(resultSize){
				changeSize = resultSize;
			}
			if(!this._autoHeight && changeSize){
				html.marginBox(this.domNode, changeSize);
				this.height = this.domNode.style.height;
				delete this.fitTo;
			}else if(this.fitTo == "parent"){
				h = this._parentContentBoxHeight = (this._parentContentBoxHeight > 0 ? this._parentContentBoxHeight : html._getContentBox(pn).h);
				this.domNode.style.height = Math.max(0, h) + "px";
			}
			
			var hasFlex = array.some(this.views.views, function(v){ return v.flexCells; });

			if(!this._autoHeight && (h || html._getContentBox(this.domNode).h) === 0){
				// We need to hide the header, since the Grid is essentially hidden.
				this.viewsHeaderNode.style.display = "none";
			}else{
				// Otherwise, show the header and give it an appropriate height.
				this.viewsHeaderNode.style.display = "block";
				if(!hasFlex && hh === undefined){
					hh = this._getHeaderHeight();
				}
			}
			if(hasFlex){
				hh = undefined;
			}

			this.postresize();
			
			// NOTE: it is essential that width be applied before height
			// Header height can only be calculated properly after view widths have been set.
			// This is because flex column width is naturally 0 in Firefox.
			// Therefore prior to width sizing flex columns with spaces are maximally wrapped
			// and calculated to be too tall.
			// adapt width and height in the end after postresize, to fix row count change.
			this.adaptWidth();
			this.adaptHeight(hh);
		},
		
		// add headerquery function
		renderHeaderQuery: function(header, queryValue) {
			var el = query('span', header),
				queryValue = queryValue || this.queryParams[header.fieldName] || '',
				parent = query('.dojoxGridColCaption', header).length > 0 ? query('.dojoxGridColCaption', header)[0] : header.firstChild;
			if (el.length > 0) {
				el[0].innerHTML = queryValue;
			} else {
				domConstruct.create('span', {innerHTML: queryValue}, parent);
			}
			header.queryValue = queryValue;
		},
		
		setHeaderName: function() {
			var headerNode = this.viewsHeaderNode,
				headers = query('.dojoxGridCell:not(:first-child)', headerNode),
				i = 0,
				len = headers.length,
				columns = this.structure[0];
			for (; i < len; i++) {
				headers[i].fieldName = columns[i]['field'];
				this.renderHeaderQuery(headers[i]);
			}
		},
		
		prerender: function() {
			this.inherited(arguments);
			this.setHeaderName();
		},
		
		showQueryDialog: function(isShow, column, event) {
			var me = this;
			function getQueryValue(column) {
				return column.queryValue || '';
			}
			if (isShow) {
				var relPos = column.getBoundingClientRect();
				if (!this.queryDialog) {
					this.queryDialog = domConstruct.create('div', {className: 'gridQueryDialog', style: 'position: absolute;'}, document.body);
					this.queryInput = domConstruct.create('input', {}, this.queryDialog);
					on(this.queryInput, 'keyup', function(e) {
						if (e.keyCode === 13) {
							me.renderHeaderQuery(this.target, this.value);
							me.queryParams[this.target.fieldName] = this.value;
							me.queryAll();
						}
					});
				}
				this.queryDialog.style.top = relPos.top - 32 + 'px';
				this.queryDialog.style.left = relPos.left + 'px';
				this.queryDialog.style.width = relPos.width - 6 + 'px';
				this.queryDialog.style.display = 'block';
				this.queryInput.value = getQueryValue(column);
				this.queryInput.target = column;
				this.queryInput.focus();
			} else {
				this.queryDialog.style.display = 'none';
			}			
		},
		
		queryAll: function() {
			this.setQuery(this.queryParams);
		},
		
		canSort: function(colIndex, field) {
			// summary:
			//		Overwritten
			return false;
		},
		
		postMixInProperties: function(){
			this.inherited(arguments);
			// add paging
			if (this.params.pageSize) {
				var ps = this.params.pageSize;
				this.plugins = {
					pagination: {
						pageSizes: [ps, ps*2, ps*4, 'All'],
			            position: 'bottom',
			            defaultPageSize: ps
					}
					//indirectSelection: {headerSelector:true, width:"40px", styles:"text-align: center;"}
				};
			}
		},
		
		// pop when click create button
		showCreateDialog: function() {
			var me = this;
			if (!this.createDialog) {
				this.createDialog = new Dialog();
				var form = new Form(),
					tableContainer = new TableContainer(),
					ext_flag = new TextBox({
						title: 'ext_flag: ',
						name: 'ext_flag',
						value: 'test'
					}),
					kind_code = new TextBox({
						title: 'kind_code: ',
						name: 'kind_code',
						value: 'test'
					}),
					lifecycle = new TextBox({
						title: 'lifecycle: ',
						name: 'lifecycle',
						value: 'test'
					}),
					param_code = new TextBox({
						title: 'param_code: ',
						id: 'param_code',
						name: 'param_code',
						value: Date.now().toString()
					}),
					param_desc = new TextBox({
						title: 'param_desc: ',
						name: 'param_desc',
						value: 'test'
					}),
					param_name = new TextBox({
						title: 'param_name: ',
						name: 'param_name',
						value: 'test'
					}),
					param_regex = new TextBox({
						title: 'param_regex: ',
						name: 'param_regex',
						value: 'test'
					}),
					param_value = new TextBox({
						title: 'param_value: ',
						name: 'param_value',
						value: 'test'
					}),
					platform = new TextBox({
						title: 'platform: ',
						name: 'platform',
						value: 'test'
					}),
					rel_org = new TextBox({
						title: 'rel_org: ',
						name: 'rel_org',
						value: 'test'
					}),
					button = new Button({
						text: 'confirm',
						style: 'float: right;',
						onClick: function() {
							var values = form.get('value'),
								store = me.store;
							store.newItem(values);
							me.createDialog.hide();
						}
					});
				tableContainer.addChild(ext_flag);
				tableContainer.addChild(kind_code);
				tableContainer.addChild(lifecycle);
				tableContainer.addChild(param_code);
				tableContainer.addChild(param_desc);
				tableContainer.addChild(param_name);
				tableContainer.addChild(param_regex);
				tableContainer.addChild(param_value);
				tableContainer.addChild(platform);
				tableContainer.addChild(rel_org);
				tableContainer.addChild(button);
				tableContainer.placeAt(form);
				this.createDialog.addChild(form);
			}
			this.createDialog.show();
			// reset the id once the window open
			registry.byId('param_code').set('value', Date.now().toString());
		},
		
		// invoke after edit cell
		doApplyCellEdit: function(inValue, inRowIndex, inAttrName){
			this.store.setValue(this.getItem(inRowIndex), inAttrName, inValue);
			this.store.updateItem(this.getItem(inRowIndex));
		},
		
		
		postCreate: function() {
			var me = this;
			this.inherited(arguments);
			// created, show the loading, the correct way is store request
			this._isLoading = true;
			this.showMessage(this.loadingMessage);
			
//			var toolbar = domConstruct.create('div', {className: 'gridToolbar'}, this.domNode, 'first');
//			if (this.params.createButton) {
//				var createButton = new Button({
//					text: 'Create',
//					onClick: function() {
//						me.showCreateDialog();
//					}
//				});
//				createButton.placeAt(toolbar);
//			}
//			if (this.params.deleteButton) {
//				var deleteButton = new Button({
//					text: 'Delete',
//					onClick: function() {
//						var selectedItems = me.selection.getSelected(),
//							ids = [],
//							len = selectedItems.length,
//							i = 0;
//						for (; i < len; i++) {
//							me.store.deleteItem({'param_code': selectedItems[i].param_code});
//						}
//					}
//				});
//				deleteButton.placeAt(toolbar);
//			}
			// add title
			if (this.params.gridTitle) {
				domConstruct.create('div', {innerHTML: '<h3>'+this.params.gridTitle+'</h3>', className: 'gridTitle'}, this.domNode, 'first');
			}
//			on(me.viewsHeaderNode, on.selector('.dojoxGridCell:contains(dojoxGridSortNode)', mouse.enter), function(event) {
//				me.showQueryDialog(true, this, event);
//			});
//			on(me.viewsHeaderNode, on.selector('.dojoxGridCell:contains(dojoxGridSortNode)', mouse.leave), function(event) {
//				me.showQueryDialog(false, this, event);
//			});
			// add td button
			if (me.params.actionButtons) {
				var buttonCtn = domConstruct.toDom('<ul class="td-button-extra" style="display:none;"></ul>');
				var len = me.params.actionButtons.length,
					i = 0;
				for (; i < len; i++) {
					domConstruct.create('li', {'data-index': i, innerHTML: me.params.actionButtons[i].text}, buttonCtn);
				}
				domConstruct.place(buttonCtn, document.body);
				on(buttonCtn, 'click', function(e) {
					var target = e.target || e.srcElement;
					var index = parseInt(domAttr.get(target, 'data-index')),
						rowIndex = parseInt(domAttr.get(buttonCtn, 'data-rowIndex'));
					if (typeof me.params.actionButtons[index]['handler'] == 'function') {
						me.params.actionButtons[index]['handler'].call(target, me.getItem(rowIndex));
					}
					e.stopPropagation();
				});
				on(me, 'CellMouseOver', function(e) {
					if (e.cell.action) {
						var pos = domGeometry.position(e.cellNode, true);
						place.at(buttonCtn, pos, ['TL']);
						domStyle.set(buttonCtn, {
							display: 'block',
							width: pos.w + 2 + 'px'
						});
						domAttr.set(buttonCtn, 'data-rowIndex', e.rowIndex);
					}
				});
				on(buttonCtn, 'mouseleave', function(e) {
					domStyle.set(buttonCtn, {
						display: 'none'
					});
				});
			}
		},
		
		// invoke when cell edit,
		// when edited, put request to server
		onApplyCellEdit: function(value, rowIndex, name) {
			//this.store.updateItem();
		},
		
		// display the exception message
		_onFetchError: function(err, req){
			delete this._lastScrollTop;
			if(!this._isLoaded){
				this._isLoading = false;
				this._isLoaded = true;
				this.showMessage('<span><i class="fa" style="color: #f0700c;">&#xf05a;</i> '+err+'</span>');
			}
			this._pending_requests[req.start] = false;
			this.onFetchError(err, req);
		}
	});
});