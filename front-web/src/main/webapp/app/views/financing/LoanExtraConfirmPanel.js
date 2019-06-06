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
    'dojo/text!./templates/LoanExtraConfirmPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global, 
		query, DisplayBox, TextBox, Button, dom, domConstruct, on, Tooltip, DateUtil, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	loanAmount: 0.00,
    	loan_ratio: 0,
    	deadDate: '',
    	interest_day_rate: 0,
    	contractNo: '',
    	exposure_ratio:0,
    	warningLine:0,
    	total_loanAmount:0,
    	proSettleMode: '',
    	proTerm: '',
    	
    	prev: function() {},
    	next: function() {},
    	
    	// in order to run after postCreate, we should set the params after creating
    	_setLoanAmountAttr: function(value) {
    		this._set('loanAmount', value);
    		this.loanAmountNode.innerHTML = Global.formatAmount(value);
    	},
    	
    	_setLoan_ratioAttr: function(value) {
    		this._set('loan_ratio', value);
    		this.ratioNode.innerHTML = value;
    	},
    	
    	_setProSettleModeAttr: function(value) {
    		this._set('proSettleMode', value);
    		this.interestModeNode.innerHTML = value;
    	},
    	
    	_setProTermAttr: function(value) {
    		this._set('proTerm', value);
    		this.deadDateNode.innerHTML = DateUtil.format(DateUtil.add(new Date(), 'day', value - 1));
    	},
    	
    	_setInterest_day_rateAttr: function(value) {
    		this._set('rate', value);
    		this.interest_day_rateNode.innerHTML = value;
    	},
    	
    	_setExposure_ratioAttr: function(value) {
    		this._set('exposure_ratio', value);
    		this.exposureRatioNode.innerHTML = value;
    	},
    	_setWarningLineAttr: function(value) {
    		this._set('warningLine', value);
			this.warningLineNode.innerHTML = value;
    	},
		setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	_setTotal_loanAmountAttr: function(value) {
    		this._set('total_loanAmount', value);
			this.totalLoanNode.innerHTML = value;
    	},
    	
    	render: function() {
    		var me = this,
    			tradePwdCtn;
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
    			enter:true,
    			style: {
    				marginLeft: '0px'
    			},
    			handler: me.next
    		}, me.nextBtnNode);
    		me.tradePwdFld = new TextBox({
    			style:{
    				marginLeft:'10px'
    			},
				type: 'password',
				validates: [{
					pattern: /.+/,
					message: '请输入安全密码'
				}]
    		},
    		me.tradePwdNode
    		);
    	},
    	
    	reset: function() {
    		this.tradePwdFld.reset();
    	},
    	
    	isValid: function() {
    		return this.tradePwdFld.checkValidity();
    	},
    	
    	getData: function() {
//    		return {
//    			tradePwd: this.tradePwdFld.get('value')
//    		};
    	},
    	
    	showError: function(message) {
    		Tooltip.show(message, this.nextBtn.domNode);
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