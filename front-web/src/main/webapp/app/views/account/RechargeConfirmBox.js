define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dojo/dom-style',
    'dojo/on',
    'dojo/mouse',
    'dijit/DialogUnderlay',
    'app/common/Global',
    'dojo/query',
    'dijit/focus',
    'dojo/dom-class',
    'dojo/dom-construct',
    'dojo/text!./templates/RechargeConfirmBox.html'
], function(declare, _WidgetBase, _TemplatedMixin, domStyle, on, mouse, DialogUnderlay, Global, query, focusUtil, domClass, domConstruct, template){
    return declare([_WidgetBase, _TemplatedMixin], {
    	
    	templateString: template,
    	baseUrl: Global.baseUrl,
    	afterClose: function() {},
    	
    	showUnderlay: function() {
    		DialogUnderlay.show({}, 9999);
    	},
    	
    	show: function() {
    		var me = this;
    		domClass.add(me.domNode, 'zoom-up');
    		domClass.remove(me.domNode, 'zoom-down');
    		me.showUnderlay();
    	},
    	
    	hide: function() {
    		var me = this;
    		domClass.add(me.domNode, 'zoom-down');
    		domClass.remove(me.domNode, 'zoom-up');
    		DialogUnderlay.hide();
    	},
    	
    	postCreate: function(){
    		var me = this;
    		
    		domStyle.set(me.domNode, {
    			position: 'absolute',
    			top: '50%',
    			left: '50%',
    			'marginLeft': '-260px',
    			'marginTop': '-145px',
    			'backgroundColor': '#fff',
    			'zIndex': '10000'
    		});
    		
    		on(me.closeNode, 'click', function() {
    			me.hide();
    			me.afterClose();
    		});
    		    		
    		me.inherited(arguments);
    	}
    });
});