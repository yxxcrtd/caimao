define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/ux/GenericTooltip',
    'app/common/Global',
    'dojo/text!./templates/BankcardFailurePanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Tooltip, Global, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	onConfirm: function() {},
    	baseUrl: Global.baseUrl,
    	
    	templateString: template,
    	
    	render: function() {
    		
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});