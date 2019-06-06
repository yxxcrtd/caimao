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
    'dojo/text!./templates/TenderPanel2.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin,
            Global, query, Button, dom, domConstruct, on, DateUtil, ViewMixin, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,

        tender: '',

        _setTenderAttr: function(data) {
            this.invsAmountSafeNode.innerHTML = Global.formatAmount(data.invsAmountSafe);
            this.invsAmountPreNode.innerHTML = Global.formatAmount(data.invsAmountPre);
            this.totalAmountNode.innerHTML = Global.formatAmount(data.invsAmountSafe + data.invsAmountPre);
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