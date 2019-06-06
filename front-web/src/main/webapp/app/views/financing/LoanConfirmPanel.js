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
    'dojo/text!./templates/LoanConfirmPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		Global, query, DisplayBox, TextBox, Button, dom, domConstruct, on, Tooltip, DateUtil, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	
    	loanAmount: 0.00,
    	percentage: 0.00,
    	deadDate: '',
    	rate: 0.00,
    	warningLine: 0.00,
    	exposure: 0.00,
    	productSettleMode: '',
    	
    	prev: function() {},
    	next: function() {},
    	
    	// in order to run after postCreate, we should set the params after creating
    	_setLoanAmountAttr: function(value) {
    		this._set('loanAmount', value);
    		this.loanAmountNode.innerHTML = Global.formatAmount(value);
    	},
    	
    	_setProductSettleModeAttr: function(value) {
    		this._set('productSettleMode', value);
    		this.productSettleModeNode.innerHTML = value;
    	},
    	
    	_setPercentageAttr: function(value) {
    		this._set('percentage', value);
    		this.percentageNode.innerHTML = value;
    	},
    	
    	_setDeadDateAttr: function(value) {
    		this._set('deadDate', value);
    		this.deadDateNode.innerHTML = DateUtil.format(DateUtil.add(new Date(), 'day', value - 1));
    	},
    	
    	_setRateAttr: function(value) {
    		this._set('rate', value);
    		this.rateNode.innerHTML = value;
    	},
    	
    	_setWarningLineAttr: function(value) {
    		this._set('warningLine', value);
    		this.warningLineNode.innerHTML = value;
    	},
    	
    	_setExposureAttr: function(value) {
    		this._set('exposure', value);
    		this.exposureNode.innerHTML = value;
    	},
		
		setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	render: function() {
    		var me = this,
    			tradePwdCtn;
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
    		}, me.preBtnNode);
    		me.confirmBtn = new Button({
    			label: '提交',
    			style: {
    				marginLeft: '0px'
    			},
    			enter:true,
    			handler: me.next
    		}, me.confirmBtnNode);
    	},
    	
    	reset: function() {
    		this.tradePwdFld.reset();
    	},
    	
    	isValid: function() {
    		return this.tradePwdFld.checkValidity();
    	},
    	
    	getData: function() {
    		return {
    			trade_pwd: this.tradePwdFld.get('value'),
    			begin_date: DateUtil.format(new Date()),
    			end_date: this.deadDateNode.innerHTML
    		};
    	},
    	
    	showError: function(message) {
    		Tooltip.show(message, this.confirmBtn.domNode);
    	},
    	
    	getValues: function() {
    		return {
    			loanAmount: this.get('loanAmount')
    		};
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});