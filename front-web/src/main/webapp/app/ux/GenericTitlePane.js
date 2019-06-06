define([ 
    'dojo/_base/declare',
    'dijit/TitlePane',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/on',
    'dojo/mouse',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/_base/array',
    'dojo/text!./templates/GenericTitlePane.html'
], function (declare, TitlePane, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		Global, on, mouse, domClass, domStyle, array, template) {
	return declare([TitlePane, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
		
		constructor: function(params /*===== , srcNodeRef =====*/){
			this._applyAttributes = function(){
				// summary:
				//		Step during widget creation to copy  widget attributes to the
				//		DOM according to attributeMap and _setXXXAttr objects, and also to call
				//		custom _setXXXAttr() methods.
				//
				//		Skips over blank/false attribute values, unless they were explicitly specified
				//		as parameters to the widget, since those are the default anyway,
				//		and setting tabIndex="" is different than not setting tabIndex at all.
				//
				//		For backwards-compatibility reasons attributeMap overrides _setXXXAttr when
				//		_setXXXAttr is a hash/string/array, but _setXXXAttr as a functions override attributeMap.
				// tags:
				//		private

				// Call this.set() for each property that was either specified as parameter to constructor,
				// or is in the list found above.	For correlated properties like value and displayedValue, the one
				// specified as a parameter should take precedence.
				// Particularly important for new DateTextBox({displayedValue: ...}) since DateTextBox's default value is
				// NaN and thus is not ignored like a default value of "".

				// Step 1: Save the current values of the widget properties that were specified as parameters to the constructor.
				// Generally this.foo == this.params.foo, except if postMixInProperties() changed the value of this.foo.
				var params = {};
				for(var key in this.params || {}){
					params[key] = this._get(key);
				}

				// Step 2: Call set() for each property with a non-falsy value that wasn't passed as a parameter to the constructor
				array.forEach(this.constructor._setterAttrs, function(key){
					if(!(key in params)){
						var val = this._get(key);
						this.set(key, val);
					}
				}, this);

				// Step 3: Call set() for each property that was specified as parameter to constructor.
				// Use params hash created above to ignore side effects from step #2 above.
				for(key in params){
					this.set(key, params[key]);
				}
			};
		},
		
		templateString: template,
		
		title: '',
		
		postCreate: function() {
    		var me = this;
    	    this.inherited(arguments);
    	}
	});
});