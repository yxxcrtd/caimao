define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/ux/GenericTextBox',
    'app/ux/GenericButton',
    'app/ux/GenericTooltip',
    'app/common/Global',
    'app/ux/GenericPhoneCodeBox',
    'dojo/text!./templates/TPasswordSetPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, TextBox, Button, Tooltip, Global, PhoneCodeBox, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	onConfirm: function() {},
    	baseUrl: Global.baseUrl,
    	
    	templateString: template,
    	mobile: '',
    	
    	render: function() {
    		var me = this,
    			verifyCode = me.verifyCodeFld = new PhoneCodeBox({
    				label: '短信验证码',
    				leftOffset: 440,
    				validates: [{
    					pattern: /.+/,
    					message: '请输入短信验证码'
    				}],
    				inputWidth: 100,
    				maxLength: 6
    			}),
    			confirmBtn = me.confirmBtn = new Button({
    				label: '提交',
    				enter:true,
    				handler: me.onConfirm,
    				style: {
    					'marginLeft': '440px'
    				}
    			});
            me.pwdFld = new TextBox({
                label: '设置安全密码',
                leftOffset: 440,
                promptMessage: '6-16位字符，可包含英文字符、数字、符号',
                validates: [{
                    pattern: /.+/,
                    message: '请输入安全密码'
                }, {
                    pattern: /^.{6,16}$/,
                    message: '密码长度在6-16个字符'
                }, {
                    pattern: /^[A-Za-z0-9\s!"#$%&'()*+,-.\/:;<=>?@\[\\\]^_`{|}~]+$/,
                    message: '密码格式错误'
                }],
                type: 'password',
                showStrength: true
            });
            me.confirmPwdFld = new TextBox({
                label: '再次输入',
                leftOffset: 440,
                validates: [{
                    pattern: /.+/,
                    message: '请再次输入安全密码'
                }, {
                    pattern: function() {
                        return me.confirmPwdFld.get('Value') == me.pwdFld.get('Value');
                    },
                    message: '两次密码输入不一致'
                }],
                type: 'password'
            });
    		
    		verifyCode.placeAt(me.formNode);
            me.pwdFld.placeAt(me.formNode);
            me.confirmPwdFld.placeAt(me.formNode);
    		confirmBtn.placeAt(me.formNode);
    	},
    	
    	getData: function() {
    		return {
    			code: this.verifyCodeFld.get('value'),
    			trade_pwd:this.pwdFld.get('value'),
    			mobile: this.mobile
    		};
    	},
    	
    	isValid: function() {
    		return this.verifyCodeFld.checkValidity() && this.pwdFld.checkValidity() && this.confirmPwdFld.checkValidity();
    	},
    	
    	showError: function(err) {
    		Tooltip.show(err.info, this.confirmBtn.domNode);
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});