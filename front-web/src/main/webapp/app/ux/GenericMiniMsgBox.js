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
    'app/common/Position',
    'app/common/Fx',
    'dojo/text!./templates/GenericMiniMsgBox.html'
], function (declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		Global, on, domClass, domStyle, domConstruct, Position, Fx, template) {
	return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
		templateString: template,
		
		type: 'success',
		text: '',
		slideOffset: 100,
		
		_setTypeAttr: function(value) {
			if (value == 'success') {
				this.iconNode.innerHTML = '&#xf00c;';
				domStyle.set(this.iconCtnNode, {
					color: '#01BE36'
				});
			} else if (value == 'error') {
				this.iconNode.innerHTML = '&#xf00d;';
				domStyle.set(this.iconCtnNode, {
					color: '#BE0101'
				});
			} else if (value == 'warning') {
				this.iconNode.innerHTML = '&#xf12a;';
				domStyle.set(this.iconCtnNode, {
					color: '#FF7628'
				});
			}
			domStyle.set(this.iconNode, {
				lineHeight: '32px'
			}); // fix ie8
		},
		
		_setTextAttr: function(value) {
			this.msgNode.innerHTML = value;
		},
		
		show: function(body, offset) {
			//Position.center(this.domNode);
			this.domNode.style.display = 'block';
			Fx.slideInOutCenter(this.domNode, body, offset).play();
		},
		
		setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
		
		postCreate: function() {
    		var me = this;
    		this.domNode.style.display = 'none';
    	    this.inherited(arguments);
    	}
	});
});