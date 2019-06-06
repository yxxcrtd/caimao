define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/query',
    'app/ux/GenericDisplayBox',
    'app/ux/GenericTextBox',
    'app/ux/GenericButton',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/on',
    'app/common/Date',
    'dojo/number',
    'dojo/text!./templates/LoanToBorrowPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global, 
		query, DisplayBox, TextBox, Button, dom, domConstruct, on, DateUtil, number, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	member: '',
    	turnover: '',
    	
    	_setMemberAttr: function(value) {
    		this._set('member', value);
    		this.memberNode.innerHTML = value;
    	},
    	
    	_setTurnoverAttr: function(value) {
    		this._set('turnover', value);
    		this.turnoverNode.innerHTML = number.format(value, {
    			places: 0
    		});
    	},
    	
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	baseUrl: Global.baseUrl,
    	
    	postCreate: function() {
    		var me = this;
    		me.inherited(arguments);
    	}
    	
    });
});