define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/views/ViewMixin',
    'app/common/Global',
    'dojo/query',
    'app/ux/GenericDisplayBox',
    'app/ux/GenericTextBox',
    'app/ux/GenericButton',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/on',
    'app/ux/GenericTooltip',
    'dojo/text!./templates/LoanExtendSuccessPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin,ViewMixin, Global, query, DisplayBox, TextBox, Button, dom, domConstruct, on, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin,ViewMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	
    	postCreate: function() {
    		var me = this;
    		me.inherited(arguments);
    	}
    	
    });
});