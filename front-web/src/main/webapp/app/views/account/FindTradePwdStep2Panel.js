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
    'dojo/text!./templates/FindTradePwdStep2Panel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, domConstruct, 
		query, Global, on, dom, string, GenericButton, TextBox,GenericPhoneCodeBox,  DisplayBox, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	mobile: '',
    	question: '',
    
    	next: function() {},
    	
    	_setMobileAttr: function(value) {
    		this._set('mobile', value);
    		this.oldMobileFld.displayNode.innerHTML = Global.encodeInfo(value, 3, 3);
    	},
    	
    	render: function() {
    		var me = this;
    		me.oldMobileFld = new DisplayBox({
    			'label': '原手机号',
    			validates: [{
					//not empty  
					pattern: /\.+/,
					message: '请输入原手机号'
				}, {
    				pattern: /^1[34578]\d{9}$/,
    				message: '输入的手机格式不正确'
    			}]
    			
    		});
    		me.oldMobileFld.placeAt(me.formNode);
    		
    		me.codeFld = new GenericPhoneCodeBox({
    			'label':'验证码',
    			leftOffset: 360,
    			inputWidth: 100,
	    		validates: [{
					//not empty  
					pattern: /\.+/,
					message: '请输入验证码'
				}]
    		});
    		me.codeFld.placeAt(me.formNode);
    		
    		me.bindBankFld = new TextBox({
    			'label': '绑定银行卡卡号',
    			leftOffset:360,
    			validates:[{
    				pattern:/.+/,
    				message:'请输入银行卡卡号'
    			}]
    		});
    		me.bindBankFld.placeAt(me.formNode);
    		
    		me.bankNumberFld = new TextBox({
    			'label':'持卡人证件号码',
    			leftOffset: 360,
	    		validates: [{
					//not empty  
					pattern: /\.+/,
					message: '请输入持卡人证件号码'
				}]
    		});
    		
    		me.bankNumberFld.placeAt(me.formNode);
    		
    		me.commit = new GenericButton({
    			'label':'下一步',
				style:{
					marginLeft: '360px'
				},
				handler: me.next
    		});
    		me.commit.placeAt(me.formNode);
    		var html = domConstruct.toDom('<a href="'+Global.baseUrl+'/account/tradepwd/findtradepwd.htm" class="am-left-10">重新选择验证方式</a>');
    		domConstruct.place(html, me.formNode);
    	},
    	
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	getData: function() {
    		return {
//    			email: this.newMailFld.get('value'),
//    			tradePwd: this.tradePwdFld.get('value')
    		};
    	},
    	showError: function(message) {
    		Tooltip.show(message, this.commit.domNode);
    	},
    	isValid: function() {
//    		return this.newMailFld.checkValidity() &&
//    			this.tradePwdFld.checkValidity();
    	},
    	postCreate: function(){
    		var me = this;
    		me.render();
    	    this.inherited(arguments);
    	}
    });
});