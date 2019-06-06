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
    'app/views/financing/LoanProtocolPanel',
    'dojo/text!./templates/LoanInfoPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global, query, 
		DisplayBox, TextBox, Button, dom, domConstruct, on, DateUtil, LoanProtocolPanel, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	
    	availableAmount: 0.00,
    	maxLoanAmount: 0.00,
    	percentage: 0.00,
    	warningLine: 0.00,
    	deadDate: 0.00,
    	rate: 0.00,
    	list: '',
    	percentList: '',
    	exposure: 0.00,
    	productMaxLoan: 0.00,
    	productMinLoan: 0.00,
    	repayStart: '',
    	productSettleMode: '',
    	ratioUrl: '',
    	
    	next: function() {},
    	
    	// in order to run after postCreate, we should set the params after creating
    	_setAvailableAmountAttr: function(value) {
    		this._set('availableAmount', value);
    		this.availableAmountNode.innerHTML = Global.formatAmount(value);
    	},
    	
    	_setRatioUrlAttr: function(value) {
    		this._set('ratioUrl', value);
    		this.ratioLinkNode.href = value;
    	},
    	
    	_setProductMaxLoanAttr: function(value) {
    		this._set('productMaxLoan', value);
    		this.productMaxLoanNode.innerHTML = Global.formatAmountUnit(Global.formatAmount(value));
    	},
    	
    	_setRepayStartAttr: function(value) {
    		this._set('repayStart', value);
    		this.repayStartNode.innerHTML = value;
    	},
    	
    	_setExposureAttr: function(value) {
    		this._set('exposure', value);
    		this.exposureNode.innerHTML = Global.formatAmount(value);
    	},
    	
    	_setMaxLoanAmountAttr: function(value) {
    		this._set('maxLoanAmount', value);
    		if (Global.isEmpty(value)) {
    			this.maxLoanAmountNode.innerHTML = '';
    		} else {
    			this.maxLoanAmountNode.innerHTML = Global.formatAmount(value);
        		this.loanAmountFld.validates.push({
    				pattern: function() {
    					return this.getAmount() <= value;
    				},
    				message: '金额数值必须小于或等于最大可借金额'
    			});
    		}
    	},
    	
    	_setProductMinLoanAttr: function(value) {
    		this._set('productMinLoan', value);
    		if (Global.isEmpty(value)) {
    			this.productMinLoanNode.innerHTML = '';
    		} else {
    			this.productMinLoanNode.innerHTML = Global.formatAmount(value);
        		this.loanAmountFld.validates.push({
    				pattern: function() {
    					return this.getAmount() >= value;
    				},
    				message: '金额数值必须大于或等于最小可借金额'
    			});
    		}
    	},
    	
    	_setListAttr: function(value) {
    		this._set('list', value);
    		var list = [], i = 0, len = value.length;
    		for (; i < len; i++) {
    			var ratio = value[i].loan_ratio;
    			list.push(ratio);
    		}
    		this.set('percentList', list);
    	},
    	
    	_setPercentageAttr: function(value) {
    		this._set('percentage', value);
    		if (Global.isEmpty(value)) {
    			this.percentageNode.innerHTML = '';
    		} else {
    			this.percentageNode.innerHTML = Global.formatNumber(value / (value + 1) * 100, 2) + '%';
    		}
    	},
    	
    	_setWarningLineAttr: function(value) {
    		this._set('warningLine', value);
    		this.warningLineNode.innerHTML = Global.formatAmount(value);
    	},
    	
    	_setDeadDateAttr: function(value) {
    		this._set('deadDate', value);
    		this.deadDateNode.innerHTML = value;
    	},
    	
    	_setRateAttr: function(value) {
    		this._set('rate', value);
    		if (value) {
    			value = Global.formatAmount(parseFloat(value) * 300000, 3) + '%';
    		}
    		this.rateNode.innerHTML = value;
    	},
		
		setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	render: function() {
    		var me = this,
    			loanAmountCtn;
    		me.loanAmountFld = new TextBox({
				style: {
					marginLeft: '10px',
					marginBottom: '0px'
				},
				validates: [{
					pattern: /.+/,
					message: '请输入融资金额'
				}],
				unit: '元',
				limitRegex: /[\d\.]/,
				isAmount: true,
				isNumber: true
    		}, me.loanAmountNode);
    		me.nextBtn = new Button({
    			label: '下一步',
    			style: {
    				marginLeft: '360px'
    			},
    			enter:true,
    			disabled: true,
    			handler: me.next
    		}, me.nextBtnNode);
    		me.protocolPanel = new LoanProtocolPanel();
    		me.protocolPanel.placeAt(me.protocolNode);
    	},
    	
    	isValid: function() {
    		return this.loanAmountFld.checkValidity();
    	},
    	
    	getValues: function() {
    		return {
    			loanAmount: this.loanAmountFld.getAmount(),
    			percentage: this.percentageNode.innerHTML,
    			deadDate: this.get('deadDate'),
    			rate: this.rateNode.innerHTML,
    			warningLine: this.warningLineNode.innerHTML,
    			exposure: this.exposureNode.innerHTML,
    			productSettleMode: this.get('productSettleMode')
    		};
    	},
    	
    	getData: function() {
    		return {
    			loan_amount: this.loanAmountFld.getAmount(),
    			produce_id: '1'
    		};
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});