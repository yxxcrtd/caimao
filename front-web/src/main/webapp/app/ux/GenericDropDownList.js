define([ 
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/on',
    'dojo/dom-construct',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/dom-attr'
], function (declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global, 
		on, domConstruct, domClass, domStyle, domAttr) {
	return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
		
		items: [],
		
		buildRendering: function(){
			var me = this;
			this.templateString = '<ul class="td-button-extra" style="display:none;"></ul>';
			this.inherited(arguments);
				
			var	items = me.get('items'),
				len = items.length,
				i = 0;
			for (; i < len; i++) {
				if (items[i].link) {
					var ctn = domConstruct.create('li', {'data-index': i, name: (items[i].name ? items[i].name : '')}, me.domNode);
					domConstruct.create('a', {innerHTML: items[i].text, target: '_blank'}, ctn);
				} else {
					domConstruct.create('li', {'data-index': i, innerHTML: items[i].text, name: (items[i].name ? items[i].name : '')}, me.domNode);
				}
			}
		},
		
		postCreate: function() {
    		var me = this;
    	    this.inherited(arguments);
    	}
	});
});