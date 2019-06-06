define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/query',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/on',
    'dojo/dom-class',
    'app/ux/GenericTooltip',
    'dojo/text!./templates/TradePremisePanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		Global, query, dom, domConstruct, on, domClass, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	tradepwdFlag: '',
    	
    	_setTradepwdFlagAttr: function(value) {
    		this._set('tradePwdFlag', value); // watch event
    		this.tradePwdLinkNode.innerHTML = '请先 <a href="'+Global.baseUrl+'/account/tradepwd/set.htm">设置安全密码</a>';
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