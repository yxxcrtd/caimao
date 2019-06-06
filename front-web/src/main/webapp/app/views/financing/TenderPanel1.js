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
    'app/common/Dict',
    'dojo/text!./templates/TenderPanel1.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin,
            Global, query, Button, dom, domConstruct, on, DateUtil, ViewMixin, Dict, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,

        tender: '',

        _setTenderAttr: function(data) {
            this.invsNameNode.innerHTML = data.invsName;
            this.invsAmountPreNode.innerHTML = Global.formatAmount(data.invsAmountPre);
            this.invsRateNode.innerHTML = Global.formatNumber(data.invsRate * 100, 3) + '%' + '（月利率' + Global.formatNumber(data.invsRate * 100 / 12, 3) + '%）';
            this.invsInterestTypeNode.innerHTML = Dict.get('invsInterestType')[parseInt(data.invsInterestType)];
            this.invsInterestNode.innerHTML = Global.formatAmount(data.invsAmountPre * data.invsRate / 12);
            this.invsDuringNode.innerHTML = data.invsDuring / 30 + '个月';
            this.invsDateRaiseEndNode.innerHTML = data.invsDateRaiseEnd;
            this.progressNode.innerHTML = Global.formatNumber(data.invsAmountActual / data.invsAmountPre * 100) + '%';
            this.invsAmountLeftNode.innerHTML = Global.formatAmount(data.invsAmountPre - data.invsAmountActual);
        },

    	render: function() {
    		var me = this;
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.inherited(arguments);
    	}
    	
    });
});