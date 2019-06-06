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
    'dojo/text!./templates/ChangeMailSuccessPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, domConstruct, 
		query, Global,dom,Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	
    	oldMail: '',
    	
    	_setOldMailAttr: function(value) {
    		this._set('oldMail', value);
    		if (value) {
    			this.msgNode.innerHTML = '恭喜您，邮箱修改成功！';
    		}	
    	},
    
    	postCreate: function(){
    		var me = this;
    	    this.inherited(arguments);
    	}
    });
});