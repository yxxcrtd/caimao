define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/query',
    'dojo/dom-class',
    'app/ux/GenericTooltip',
    'dojo/text!./templates/TPasswordSuccessPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		Global, query, domClass, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	baseUrl: Global.baseUrl,
    	
        if_question:'',
    	
    	_setIf_questionAttr: function(value) {
    		this._set('if_question', value);
    		if(value=='SHI'){
    			this.ifQuestionNode.innerHTML='修改密保问题';
    			this.ifQuestionLinkNode.href=this.baseUrl+'/account/tradepwd/findquestion.htm';
    			this.ifQuestionNode2.innerHTML='';
    		}
    	},
    	
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	render: function() {
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});