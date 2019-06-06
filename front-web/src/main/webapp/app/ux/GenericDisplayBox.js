define([ 
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/on',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/dom-construct',
    'dojo/text!./templates/GenericDisplayBox.html'
], function (declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global, on, domClass, domStyle, domConstruct, template) {
	return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
		templateString: template,
		
		// config params
		label: '',
		inputWidth: 'auto',
		separator: '：',
		text: '',
		displayStyle: '',
		value: '',
		unit: '',
		
		// invoke when call set attribute
		_setLabelAttr: function(value) {
			this._set('label', value);
			this.labelNode.innerHTML = value + this.separator;
		},
		
		_setUnitAttr: function(value) {
			this._set('unit', value);
			if (value) {
				domConstruct.create('span', { 
					style: 'font-size: 14px;',
					innerHTML: value}, this.domNode);
			}	
		},
		
		_setInputWidthAttr: function(value) {
			this._set('inputWidth', value);
			domStyle.set(this.displayNode, {
				width: value == 'auto' ? value : value + 'px'
			});
		},
		
		_setDisplayStyleAttr: function(value) {
			this._set('displayStyle', value);
			domStyle.set(this.displayNode, value);
		},
		
		_setTextAttr: function(value) {
			this._set('text', value);
			this.displayNode.innerHTML = value;
		},
		
		_setValueAttr: function(value) {
			this._set('value', value);
			this.displayNode.innerHTML = value;
		},
		
		getValue: function() {
			return this.displayNode.innerHTML;
		},
		
		// bind event handlers
		addListeners: function() {
			var me = this;
		},
		
		postCreate: function() {
    		var me = this;
    		if (me.params.extraHTML) {
    			var dom = domConstruct.toDom(me.params.extraHTML);
    			domConstruct.place(dom, me.domNode);
    		}
    		me.addListeners();
    	    this.inherited(arguments);
    	}
	});
});