define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'dojo/dom-construct',
    'dojo/query',
    'app/common/Global',
    'dojo/dom',
    'app/ux/GenericTooltip',
    'dojo/text!./templates/ChangeMobileSuccessPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, domConstruct, 
		query, Global,dom,Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	newMobile:'',
    	
    	_setNewMobileAttr: function(value) {
    		this._set('newMobile', value);
    		this.newMobileNode.innerHTML = Global.encodeInfo(value, 3, 3);
    	},
    	
    	
    	postCreate: function(){
    		var me = this;
    	    this.inherited(arguments);
    	}
    });
});