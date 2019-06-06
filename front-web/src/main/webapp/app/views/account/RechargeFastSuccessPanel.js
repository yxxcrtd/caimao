define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'app/ux/GenericTextBox',
    'app/ux/GenericButton',
    'dojo/dom-construct',
    'app/ux/GenericTooltip',
    'dojo/query',
    'dojo/text!./templates/RechargeFastSuccessPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		Global, TextBox, Button, domConstruct, Tooltip, query, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
        templateString: template,
	
	    baseUrl: Global.baseUrl,
	    
	    totalAmount: 0.00,
    	
    	_setTotalAmountAttr: function(value) {
    		this._set('totalAmount', value);
    		this.totalAmountNode.innerHTML = Global.formatAmount(value);
    	},

    	
    	render: function() {
    	},
    	
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});