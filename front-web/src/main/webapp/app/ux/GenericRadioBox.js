define([ 
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/on',
    'dojo/mouse',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/dom-construct',
    'dojo/text!./templates/GenericRadioBox.html'
], function (declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		Global, on, mouse, domClass, domStyle, domConstruct, template) {
	return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
		templateString: template,
		
		// config params
		label: '',
		separator: '：',
		cls: 'ui-radio-extra',
		items: '',
		
		_setClsAttr: {node: 'domNode', type: 'class'},
		
		// invoke when call set attribute
		_setLabelAttr: function(value) {
			this._set('label', value);
			this.labelNode.innerHTML = value + this.separator;
		},
		
		_setItemsAttr: function(value) {
			this._set('items', value);
			var len = value.length,
				i = 0;
			for (; i < len; i++) {
				var radio = domConstruct.toDom('<input value="'+value[i]['value']+'" name="'+value[i]['name']+'" type="radio" class="ui-radio">');
				if (value[i]['checked']) {
					radio.checked = true;
				}
				domConstruct.place(radio, this.domNode);
				if (value[i]['label']) {
					var label = domConstruct.toDom('<span>'+value[i]['label']+'</span>');
					domConstruct.place(label, this.domNode);
				}
			}
		},
		
		postCreate: function() {
    		var me = this;
    	    this.inherited(arguments);
    	}
	});
});