define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'dojo/dom-construct',
    'dojo/query',
    'app/common/Global',
    'dojo/on',
    'dojo/dom-class',
    'dojo/mouse',
    'app/ux/GenericPhoneCodeBox',
    'app/ux/GenericTextBox',
    'app/ux/GenericButton',
    'app/ux/GenericTooltip',
    'dojo/text!./templates/WithdrawValidityPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		domConstruct, query, Global, on, domClass, mouse, PhoneCodeBox,TextBox, Button, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	baseUrl: Global.baseUrl,
    	
    	templateString: template,
    	
    	availableAmount: 0.00,
    	bankcardInfo: {},
    	withdrawAmount: 0.00,
    	verificationCode: '',
    	
    	prev: function() {},
    	next: function() {},
    	
    	// when call setter, manually modify the dom display
    	_setWithdrawAmountAttr: function(value) {
    		this._set('withdrawAmount', value); //call watch
    		var parts = (value + '').split('.');
    		this.withdrawAmountIntNode.innerHTML = parts[0];
    		this.withdrawAmountDecNode.innerHTML = '.' + (parts[1] || '00'); //TODO format the dot
    	},
    	
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	getData: function() {
    		return {
    			'trade_pwd': this.passwordFld.get('value'),
    			'sms_code': this.codeFld.get('value')
    		}
    	},
    	
    	render: function() {
    		var me = this,
    			password = me.passwordFld = new TextBox({
    				'label': '安全密码',
    				type: 'password',
    				validates: [{
    					pattern: /.+/,
    					message: '请输入安全密码'
    				}],
    				style: 'margin-left: 360px',
    				labelStyle: {
    					left: '-130px'
    				}
    			});
    			
        		
    		me.codeFld = new PhoneCodeBox({
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
    		me.preBtn = new Button({
    			label: '上一步',
    			style: {
    				marginLeft: '360px'
    			},
    			color: '#E2E2E2',
    			hoverColor: '#EDEDED',
    			textStyle: {
    				color: '#666666'
    			},
    			innerStyle: {
    				borderColor: '#C9C9C9'
    			},
    			handler: me.prev
    		});
        		
			me.confirmBtn = new Button({
				label: '下一步',
				'handler': me.next,
				 enter: true,
				style: 'margin-left: 5px'
			});
    		me.codeFld.placeAt(me.formNode);
    		password.placeAt(me.formNode);
    		me.preBtn.placeAt(me.formNode);
    		me.confirmBtn.placeAt(me.formNode);
    	},
    	isValid: function() {
    		return this.codeFld.checkValidity() && 
    		       this.passwordFld.checkValidity();
    	},
    	
    	reset: function() {
    		this.passwordFld.reset();
    		this.codeFld.reset();
    	},
    	
    	showError: function(message) {
    		Tooltip.show(message, this.confirmBtn.domNode);
    	},
    	postCreate: function(){
    		var me = this;
    		me.render();
    	    this.inherited(arguments);
    	}
    });
});