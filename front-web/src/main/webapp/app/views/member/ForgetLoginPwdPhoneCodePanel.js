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
    'app/ux/GenericPhoneCodeBox',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/on',
    'app/common/Date',
    'app/ux/GenericTooltip',
    'dojo/text!./templates/ForgetLoginPwdPhoneCodePanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global, query, DisplayBox, TextBox, Button, GenericPhoneCodeBox, dom, domConstruct, on, DateUtil, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	mobile: '',
    	
    	prev: function() {},
    	next: function() {},
    	
    	_setMobileAttr: function(value) {
    		this._set('mobile', value);
    		this.mobileFld.displayNode.innerHTML = Global.encodeInfo(value, 3, 3);
    	},
		
		setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	getValues: function() {
    		return {
    			mobile: this.mobile,
    			checkCode: this.codeFld.get('value')
    		};
    	},
    	render: function() {
    		var me = this;
    		
    		me.mobileFld = new DisplayBox({
    			'label': '手机号',
    			leftOffset: 360
    		});
    		me.codeFld = new GenericPhoneCodeBox({
    			'label':'短信验证码',
    			style: {
    				marginLeft: '360px'
    			},
    			labelStyle: {
    				left: '-130px'
    			},
    			validates: [{
    				//not empty  
    				pattern: /.+/,
    				message: '请输入验证码'
    			}],
    			buttonLeft: 480,
    			inputWidth: 100
    		});
    		me.prevBtn = new Button({
    			'label': '上一步',
    			color: '#E2E2E2',
    			hoverColor: '#EDEDED',
    			textStyle: {
    				color: '#666666'
    			},
    			innerStyle: {
    				borderColor: '#C9C9C9'
    			},
    			leftOffset: 360,
    			handler: me.prev
    		});
    		me.confirmBtn = new Button({
    			'label': '下一步',
    			style: {
    				marginLeft: '5px'
    			},
    			enter: true,
    			handler: me.next
    		});
    		
    		me.mobileFld.placeAt(me.formNode);
    		me.codeFld.placeAt(me.formNode);
    		me.prevBtn.placeAt(me.formNode);
    		me.confirmBtn.placeAt(me.formNode);
    	},
    	showError: function(err) {
    		Tooltip.show(err, this.confirmBtn.domNode);
    	},
    	reset: function() {
    		this.codeFld.reset();
    	},
    	isValid: function() {
    		return this.codeFld.checkValidity();
    	},
    	getData: function() {
    		return {
    			mobile:this.mobile,
    			code: this.codeFld.get('value'),
    			biz_type:5
    		};
    	},
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});