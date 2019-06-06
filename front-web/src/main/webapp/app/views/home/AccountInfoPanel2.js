define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'dojo/dom-construct',
    'dojo/query',
    'app/common/Global',
    'dojo/on',
    'dojo/dom',
    'dojo/text!./templates/AccountInfoPanel2.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		domConstruct, query, Global, on, dom, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	
    	asset_total_value: '',
    	cur_amount: '',
    	stock_asset: '',
    	available_amount: '',
    	
    	_setAsset_total_valueAttr: function(value) {
    		Global.aminNumber(this.assetTotalValueNode, this.get('asset_total_value'), value);
    	},
    	
    	_setCur_amountAttr: function(value) {
    		Global.aminNumber(this.curAmountNode, this.get('cur_amount'), value);
    	},
    	
    	_setStock_assetAttr: function(value) {
    		Global.aminNumber(this.stockAssetNode, this.get('stock_asset'), value);
    	},
    	
    	_setAvailable_amountAttr: function(value) {
    		Global.aminNumber(this.availableAmountNode, this.get('available_amount'), value);
    	},
    	
    	render: function() {
    		var me = this;
    	},
    	
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	postCreate: function() {
    		var me = this;
    	    me.render();
    	    this.inherited(arguments);
    	}
    });
});