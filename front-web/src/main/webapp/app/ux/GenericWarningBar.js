define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dojo/dom-style',
    'dojo/on',
    'app/common/Global',
    'dojo/query',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/dom-class',
    'app/common/Fx',
    'dojo/cookie',
    'dojo/text!./templates/GenericWarningBar.html'
], function(declare, _WidgetBase, _TemplatedMixin, domStyle, on, Global, query, dom, domConstruct, domClass, Fx, cookie, template){
    return declare([_WidgetBase, _TemplatedMixin], {
    	
    	templateString: template,
    	msg: '',
    	
    	_setMsgAttr: function(value) {
    		this.textNode.innerHTML = value;
    	},
    	
    	show: function(autoHide) {
            var me = this;
            if (me.timeID) {
                clearTimeout(me.timeID);
            }
    		Fx.slideIn(this.domNode).play();
            domClass.remove(me.domNode, 'errorhighlight');
            domClass.add(me.domNode, 'errorhighlight');
            if (autoHide) {
                me.timeID = setTimeout(function() {
                    me.hide();
                }, 3000);
            }
    	},
    	
    	hide: function() {
    		Fx.slideOut(this.domNode).play();
    	},
    	
    	addListeners: function() {
    		var me = this;
    		on(me.closeNode, 'click', function() {
    			me.hide();
    			cookie('warningBarClosed', 1);
    		});
    	},
    	
    	postCreate: function(){
    		var me = this;
    		me.addListeners();
    		me.inherited(arguments);
    	}
    });
});