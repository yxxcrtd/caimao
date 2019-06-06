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
    'dojo/string',
    'app/ux/GenericButton',
    'app/ux/GenericTextBox',
    'app/ux/GenericPhoneCodeBox',
    'app/ux/GenericDisplayBox',
    'app/ux/GenericTooltip',
    'dojo/text!./templates/ChangeMobilePanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, domConstruct, 
		query, Global, on, dom, string, GenericButton, TextBox,GenericPhoneCodeBox,  DisplayBox, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	username: '',
    	usernameReal: '',
    	oldMobile:'',
    	mphone:'',
    	
    	next: function() {},
    	
    	_setOldMobileAttr: function(value) {
    		this._set('oldMobile', value);
    		this.mobileFld.displayNode.innerHTML = Global.encodeInfo(value, 3, 3);
    	},
    	
    	render: function() {
    		var me = this;
    		me.mobileFld = new DisplayBox({
    			'label': '原手机号'
    		});
    		me.mobileFld.placeAt(me.formNode);
    		
    		me.newMobileFld = new TextBox({
    			'label': '新手机号',
    			style: {
    				marginLeft: '360px'
    			},
    			labelStyle: {
					left: '-130px'
				},
				validates: [{
					//not empty  
					pattern: /.+/,
					message: '请输入手机号码'
				}, {
					pattern: /^1[34578]\d{9}$/,
					message: '请填写您正确的手机号'
				}, {
					pattern: function() {
						return this.get('Value') != me.get('oldMobile');
					},
					message: '新手机号跟原手机号一致，请重新输入'
				}]
    		});
    		me.newMobileFld.placeAt(me.formNode);
    		
    		me.codeFld = new GenericPhoneCodeBox({
    			'label':'验证码',
    			style: {
    				marginLeft: '360px'
    			},
    			labelStyle: {
    				left: '-130px'
    			},
    			buttonLeft: 480,
    			inputWidth: 100
    		});
    		me.codeFld.placeAt(me.formNode);
    		
    		me.loginPwd = new TextBox({
    			'label':'安全密码',
    			'type':'password',
    			style:{
    				marginLeft: '360px'
    			},
    			validates: [{
					//not empty  
					pattern: /.+/,
					message: '请输入安全密码'
				}],
    			labelStyle: {
				left: '-130px'
			}
    		});
    		me.loginPwd.placeAt(me.formNode);
    		
    		me.commit = new GenericButton({
    			'label':'提交',
    			enter: true,
			style:{
				marginLeft: '360px'
			}
    		});
    		me.commit.placeAt(me.formNode);
    	},
    	
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	getData: function() {
    		return {
    			mobile: this.newMobileFld.get('value'),
    			check_code: this.codeFld.get('value'),
    			trade_pwd: this.loginPwd.get('value')
    		};
    	},
    	showError: function(message) {
    		Tooltip.show(message, this.commit.domNode,'warning');
    	},
    	isValid: function() {
    		return this.newMobileFld.checkValidity() &&
    			this.codeFld.checkValidity() && 
    			this.loginPwd.checkValidity();
    	},
    	postCreate: function(){
    		var me = this;
    		me.render();
    	    this.inherited(arguments);
    	}
    });
});