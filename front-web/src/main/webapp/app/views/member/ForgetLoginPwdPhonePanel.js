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
    'app/ux/GenericTooltip',
    'dojo/text!./templates/ForgetLoginPwdPhonePanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global, query, DisplayBox, 
		TextBox, Button, dom, domConstruct, on, DateUtil, Tooltip, template){
	return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	
    	next: function() {},
    	
    	showError: function(err) {
    		Tooltip.show(err, this.confirmBtn.domNode);
    	},
    	
    	render: function() {
    		var me = this;
    		me.mobileFld = new TextBox({
    			'label': '手机号',
    			leftOffset: 360,
    			validates: [{
					//not empty  
					pattern: /.+/,
					message: '请输入手机号'
				}, {
    				pattern: /^1[34578]\d{9}$/,
    				message: '输入的手机格式不正确'
    			}]
    		});
    		me.codeFld = new TextBox({
    			'label':'验证码',
    			leftOffset: 360,
    			style: {
    				'margin-bottom': '0px'
    			},
    		validates: [{
				//not empty  
				pattern: /.+/,
				message: '请输入验证码'
			}]
    		});
    		me.confirmBtn = new Button({
    			'label': '下一步',
    			style: {
    				marginLeft: '360px'
    			},
    			enter: true,
                disabled: true,
    			handler: me.next
    		});
    		me.codeFld.placeAt(me.formNode, 'first');
    		me.mobileFld.placeAt(me.formNode, 'first');
    		me.confirmBtn.placeAt(me.formNode);
    	},
    	isValid: function() {
    		return this.mobileFld.checkValidity()&&
    		this.codeFld.checkValidity();
    	},
    	getValues: function() {
    		return {
    			mobile: this.mobileFld.get('value')
    		};
    	},
        reset: function() {},
    	getData: function() {
    		return {
    			user_name: this.mobileFld.get('value'),
    			captcha: this.codeFld.get('value'),
    			company: '1'
    		};
    	},
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});