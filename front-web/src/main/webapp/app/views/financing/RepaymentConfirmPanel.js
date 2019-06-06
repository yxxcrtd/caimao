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
    'app/ux/GenericTooltip',
    'app/common/Date',
    'dojo/text!./templates/RepaymentConfirmPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global, query, DisplayBox, TextBox, Button, dom, domConstruct, on, Tooltip, DateUtil, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	repayAmount: Global.EMPTY,
		interestAmount: Global.EMPTY,
		totalAmount: Global.EMPTY,
    	
    	next: function() {},
    	prev: function() {},
    	
    	_setRepayAmountAttr: function(value) {
    		this._set('repayAmount', value);
    		this.repayAmountNode.innerHTML = Global.formatAmount(value);
    	},
    	
    	_setInterestAmountAttr: function(value) {
    		this._set('interestAmount', value);
    		this.interestAmountNode.innerHTML = Global.formatAmount(value);
    	},
    	
    	_setTotalAmountAttr: function(value) {
    		this._set('totalAmount', value);
    		this.totalAmountNode.innerHTML = Global.formatAmount(value);
    	},
    			
		setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	render: function() {
    		var me = this,
    			tradePwdCtn, repayAmountCtn;
    		me.tradePwdFld = new TextBox({
    			validates: [{
					pattern: /.+/,
					message: '请输入安全密码 '
				}],
				style: {
					marginLeft: '10px',
					marginBottom: '0px'
				},
				type: 'password'
    		}, me.tradePwdNode);
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
    		}, me.prevBtnNode);
    		me.nextBtn = new Button({
    			label: '提交',
    			style: {
    				marginLeft: '0px'
    			},
    			enter:true,
    			handler: me.next
    		}, me.nextBtnNode);
    	},
    	
    	reset: function() {
    		this.tradePwdFld.reset();
    	},
    	
    	isValid: function() {
    		return this.tradePwdFld.checkValidity();
    	},
    	
    	getData: function() {
    		return {
    			trade_pwd: this.tradePwdFld.get('value')
    		};
    	},
    	
    	showError: function(message) {
    		Tooltip.show(message, this.nextBtn.domNode);
    	},
    	
    	getValues: function() {
    		return {
    			totalAmount: this.get('totalAmount')
    		};
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});