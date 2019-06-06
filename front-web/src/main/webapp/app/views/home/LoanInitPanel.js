define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'dojo/dom-construct',
    'dojo/query',
    'app/common/Global',
    'dojo/on',
    'dojo/dom',
    'app/ux/GenericButton',
    'dojo/text!./templates/LoanInitPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		domConstruct, query, Global, on, dom, Button, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	render: function() {
    		var me = this;
    		me.loanDXBBtn = new Button({
    			label: '短线宝借款',
    			style: {
    				marginLeft: '0px'
    			},
    			color: 'rgb(242, 158, 94)',
    			hoverColor: 'rgb(242, 158, 94)',
    			innerStyle: {
    				borderColor: 'rgb(242, 158, 94)'
    			},
    			width: 136,
    			height: 40
    		}, me.loanDXBBtnNode);
    		me.loanBtn = new Button({
    			label: '普通借款',
    			style: {
    				marginLeft: '0px'
    			},
    			color: 'rgb(98, 194, 193)',
    			hoverColor: 'rgb(98, 194, 193)',
    			innerStyle: {
    				borderColor: 'rgb(98, 194, 193)'
    			},
    			width: 136,
    			height: 40
    		}, me.loanBtnNode);
    	},
    	
    	postCreate: function() {
    		var me = this;
    	    me.render();
    	    this.inherited(arguments);
    	}
    });
});