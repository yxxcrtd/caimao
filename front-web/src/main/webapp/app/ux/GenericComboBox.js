define([ 
    'dojo/_base/declare',
    'dojo/_base/Deferred',
    'dijit/form/ComboBox',
    'app/ux/GenericTextBox',
    'app/common/Global',
    'dojo/on',
    'dojo/mouse',
    'dojo/dom-construct',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/dom-attr',
    'app/ux/GenericTooltip',
    'dojo/_base/lang',
    'app/common/Ajax',
    'dojo/text!./templates/GenericComboBox.html'
], function (declare, Deferred, ComboBox, TextBox, Global, on, 
		mouse, domConstruct, domClass, domStyle, domAttr, Tooltip, lang, Ajax, template) {
	return declare([ComboBox, TextBox], {
		templateString: template,
		_stopClickEvents: true,
		
		// config params
		label: '',
		labelStyle: '',
		cls: 'ui-input ui-combo-extra',
		inputWidth: 194,
		separator: '：',
		editable: true,
		leftOffset: 0,
		
		// invoke when call set attribute
		_setLabelAttr: function(value) {
			this._set('label', value);
			this.labelNode.innerHTML = value + this.separator;
            domStyle.set(this.labelNode, {
                left: '-130px'
            });
		},
		
		_setLeftOffsetAttr: function(value) {
			this._set('leftOffset', value);
			domStyle.set(this.domNode, {
				marginLeft: value + 'px'
			});
		},
		
		_setInputWidthAttr: function(value) {
			this._set('inputWidth', value);
			this.domNode.style.width = value + 'px';
		},
		
		_setLabelStyleAttr: function(value) {
			this._set('labelStyle', value);
			domStyle.set(this.labelNode, value);
		},
		
		_setClsAttr: {node: 'domNode', type: 'class'},
		
		//get code value
		getFilterValue: function(){
			return this.get("value").replace(/\W.*/,"");
		},
		
		// bind event handlers
		addListeners: function() {
			var me = this;
			// hover the domnode, if has a error, show it
			on(me.domNode, 'mouseenter', function(e) {
				var error = me.getErrorMessage();
				if (me.state === 'Error' && error) {
					me.displayMessage(error);
				}
			});
		},
		
		render: function() {
			var me = this;
			if (!me.get('editable')) {
				me.textbox.disabled = true;
				domStyle.set(me.textbox, {
					cursor: 'pointer'
				});
//				on(me, 'click', function(e) {
//					me.toggleDropDown();
//				});
			}

			on(me, 'click', me._onDropDownMouseDown);
		},
		
		postCreate: function() {
    		var me = this;
    		me.render();
    		me.addListeners();
    	    this.inherited(arguments);
    	},
		
    	isLoaded: function() {
    		return this.store.loaded;
    	},

    	// override
    	_announceOption: function(/*Node*/ node){
			// summary:
			//		a11y code that puts the highlighted option in the textbox.
			//		This way screen readers will know what is happening in the
			//		menu.

			if(!node){
				return;
			}
			// pull the text value from the item attached to the DOM node
			var newValue;
			if(node == this.dropDown.nextButton ||
				node == this.dropDown.previousButton){
				newValue = node.innerHTML;
				this.item = undefined;
				this.value = '';
			}else{
				var item = this.dropDown.items[node.getAttribute("item")];
				newValue = (this.store._oldAPI ? // remove getValue() for 2.0 (old dojo.data API)
					this.store.getValue(item, this.labelAttr || this.searchAttr) : item[this.labelAttr || this.searchAttr]).toString();
				this.set('item', item, false, newValue);
			}
			// get the text that the user manually entered (cut off autocompleted text)
			this.focusNode.value = this.focusNode.value.substring(0, this._lastInput.length);
			// set up ARIA activedescendant
			this.focusNode.setAttribute("aria-activedescendant", domAttr.get(node, "id"));
			// autocomplete the rest of the option to announce change
			this._autoCompleteText(newValue);
		},
		
		// override
		_setItemAttr: function(/*item*/ item, /*Boolean?*/ priorityChange, /*String?*/ displayedValue){
			// summary:
			//		Set the displayed valued in the input box, and the hidden value
			//		that gets submitted, based on a dojo.data store item.
			// description:
			//		Users shouldn't call this function; they should be calling
			//		set('item', value)
			// tags:
			//		private
			var value = '';
			if(item){
				if(!displayedValue){
					displayedValue = this.store._oldAPI ? // remove getValue() for 2.0 (old dojo.data API)
						this.store.getValue(item, this.searchAttr) : item[this.labelAttr || this.searchAttr];
				}
				value = this._getValueField() != this.searchAttr ? this.store.getIdentity(item) : 
					(this.store.getValue ? this.store.getValue(item, this.searchAttr) : item[this.searchAttr]);
			}
			this.set('value', value, priorityChange, displayedValue, item);
		},
		
		// override
//		_handleOnChange: function(/*anything*/ newValue, /*Boolean?*/ priorityChange){
//			// summary:
//			//		Called when the value of the widget has changed.  Saves the new value in this.value,
//			//		and calls onChange() if appropriate.   See _FormWidget._handleOnChange() for details.
//			this._set("value", newValue);
//			this.inherited(arguments);
//			this.afterSelect();
//		},
		
		// override
		loadAndOpenDropDown: function(){
			// summary:
			//		Creates the drop down if it doesn't exist, loads the data
			//		if there's an href and it hasn't been loaded yet, and
			//		then opens the drop down.  This is basically a callback when the
			//		user presses the down arrow button to open the drop down.
			// returns: Deferred
			//		Deferred for the drop down widget that
			//		fires when drop down is created and loaded
			// tags:
			//		protected
			var d = new Deferred(),
				afterLoad = lang.hitch(this, function(){
					this.openDropDown();
					d.resolve(this.dropDown);
				});
			if(!this.isLoaded()){
				this.loadDropDown(afterLoad);
			}else{
				var options = {
					count: Infinity,
					queryOptions: {
						deep: true,
						ignoreCase: true
					},
					start: 0
				};
				var query = {
					count: Infinity,
					start: 0
				}
				query[this.searchAttr] = '*';
				this._openResultList(this.store._items, query, options);
			}
			return d;
		},
		
		// override
		_openResultList: function(/*Object*/ results, /*Object*/ query, /*Object*/ options){
			// summary:
			//		Callback when a search completes.
			// description:
			//		1. generates drop-down list and calls _showResultList() to display it
			//		2. if this result list is from user pressing "more choices"/"previous choices"
			//			then tell screen reader to announce new option
			
			if (this.get('related')) {
				var related = this.get('related');
				var exceptIds = [];
				for (var i = 0, len = related.length; i < len; i++) {
					exceptIds.push(related[i].value);
				}
				var results1 = [];
				results1.nextPage = results.nextPage;
				for (var i = 0, len = results.length; i < len; i++) {
					if (exceptIds.indexOf(results[i]['i'][this.searchAttr]) == -1) {
						results1.push(results[i]);
					}
				}
				results1.total = results1.length;
				results = results1;
			}
			
			var wasSelected = this.dropDown.getHighlightedOption();
			this.dropDown.clearResultList();
			if(!results.length && options.start == 0){ // if no results and not just the previous choices button
				this.closeDropDown();
				return;
			}
			this._nextSearch = this.dropDown.onPage = lang.hitch(this, function(direction){
				results.nextPage(direction !== -1);
				this.focus();
			});

			// Fill in the textbox with the first item from the drop down list,
			// and highlight the characters that were auto-completed. For
			// example, if user typed "CA" and the drop down list appeared, the
			// textbox would be changed to "California" and "ifornia" would be
			// highlighted.

			this.dropDown.createOptions(
				results,
				options,
				lang.hitch(this, "_getMenuLabelFromItem")
			);

			// show our list (only if we have content, else nothing)
			this._showResultList();

			// #4091:
			//		tell the screen reader that the paging callback finished by
			//		shouting the next choice
			if("direction" in options){
				if(options.direction){
					this.dropDown.highlightFirstOption();
				}else if(!options.direction){
					this.dropDown.highlightLastOption();
				}
				if(wasSelected){
					this._announceOption(this.dropDown.getHighlightedOption());
				}
			}else if(this.autoComplete && !this._prev_key_backspace
				// when the user clicks the arrow button to show the full list,
				// startSearch looks for "*".
				// it does not make sense to autocomplete
				// if they are just previewing the options available.
				&& !/^[*]+$/.test(query[this.searchAttr].toString())){
				this._announceOption(this.dropDown.containerNode.firstChild.nextSibling); // 1st real item
			}
		}
	});
});