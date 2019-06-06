define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/query',
    'app/ux/GenericButton',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/on',
    'app/common/Date',
    'app/views/ViewMixin',
    'app/common/Product',
    'app/ux/GenericProgressBar',
    'app/common/SVG',
    'dojo/has',
    'dojo/_base/sniff',
    'dojo/text!./templates/MyTenderListItem.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin,
            Global, query, Button, dom, domConstruct, on, DateUtil, ViewMixin, Product, ProgressBar, SVG, has, sniff, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,

        item: '',

        _setItemAttr: function(item) {
            this.invsName.innerHTML = item.invsName;
            this.invsAmountPreNode.innerHTML = Global.formatAmount(item.invsAmountPre);
            this.invsRateNode.innerHTML = Global.formatNumber(item.invsRate * 100) + '%';
            this.invsDuringNode.innerHTML = Global.formatNumber(item.invsDuring / 30, 0);
            this.invrDataPay.innerHTML = item.invrDatePay;
        },

    	render: function() {
    		var me = this;
            //me.progressBar = new ProgressBar({}, me.progressNode);
    	},
    	
    	postCreate: function() {
    		var me = this;
            me.render();
    		me.inherited(arguments);
    	}
    	
    });
});