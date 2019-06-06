define([ 
    'dojo/_base/declare',
    'dijit/form/Textarea',
    'app/common/Global',
    'dojo/on',
    'dojo/mouse',
    'dojo/dom-construct',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/dom-attr',
    'app/ux/GenericTooltip'
], function (declare, Textarea, Global, on, mouse, domConstruct, domClass, domStyle, domAttr, Tooltip) {
	return declare([Textarea], {
		
		// config params
		cls: 'ui-input ui-textarea-extra',
		inputWidth: 202,
		separator: '：',
		maxLength: 100,
		
		_setInputWidthAttr: function(value) {
			this._set('inputWidth', value);
			this.domNode.style.width = value + 'px';
		},
		
		_setClsAttr: {node: 'domNode', type: 'class'},
		
		addError: function(msg) {
			domClass.add(this.textbox, 'dijitTextBoxError');
			Tooltip.show(this.textbox, msg, 'warning');
		},
		
		checkValidity: function() {
			var me = this;
			if (me.maxLength && me.textbox.value.length >= me.maxLength) {
				me.addError('输入个数不能超过' + me.maxLength);
			}
		},
		
		render: function() {
			var me = this;
//			on(me, 'blur', function(e) {
//				me.checkValidity();
//			});
		},
		
		buildRendering: function() {
			this.inherited(arguments);
		},
		
		postCreate: function() {
    		var me = this;
    		me.render();
    		this.inherited(arguments);
    	}
	});
});