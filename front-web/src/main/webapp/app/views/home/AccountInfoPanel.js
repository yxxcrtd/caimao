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
    'dojox/gauges/AnalogGauge',
    'app/ux/gauges/GenericAnalogArcIndicator',
    'dojox/gauges/AnalogNeedleIndicator',
    'app/ux/gauges/GenericTextIndicator',
    'dojox/gfx/shape',
    'dojox/gfx/svg',
    'app/ux/GenericLoadingOverlay',
    'app/ux/GenericButton',
    'app/views/home/LoanInfoPanel',
    'app/views/home/LoanCheckPanel',
    'app/views/home/LoanInitPanel',
    'dojo/text!./templates/AccountInfoPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		domConstruct, query, Global, on, dom, AnalogGauge, 
		AnalogArcIndicator, AnalogNeedleIndicator, TextIndicator, 
		shape, svg, LoadingOverlay, Button, LoanInfoPanel, LoanCheckPanel, LoanInitPanel, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	control: '',
    	net_asset: 0.00,
    	total_asset: 0.00,
    	total_market_value: 0.00,
    	avalaible_amount: 0.00,
    	freeze_amount: 0.00,
    	accrued_interest: 0.00,
    	account_balance: 0.00,
    	loan_amount: 0.00,
    	cash_amount: 0.00,
    	gaugeParams: '',
    	contract_end_date: '',
    	contract: '',
    	loanApply: '',
    	
    	_setGaugeParamsAttr: function(value) {
    		var me = this;
    		me.riskControl = new AnalogGauge({
    		    width: 280,
    		    background: '#fff',
    		    height: 160,
    		    startAngle: 90,
    		    endAngle: -90,
    		    cy: 130,
    		    orientation: 'cclockwise',
    		    radius: 80,
    		    ranges: [{low:-100, high:100, color: '#fff'}],
    		    majorTicks: {length: 12, offset: 95, interval: 10, color: 'gray', font:
    		    	{size: "12px"}},
    		    minorTicks: {length: 5, offset: 95, interval: 5, color: 'gray'},
    		    indicators: [
    		        new AnalogArcIndicator({ // red inner edge
					    value:-100,
					    noChange: true,
					    width: 17,
					    offset: 78,
					    color: '#f0700c',
					    hideValues: true
					}),
					new AnalogArcIndicator({ // orange
					    value:-100,
					    noChange: true,
					    width: 15,
					    offset: 80,
					    color: '#ffe04f',
					    hideValues: true,
					    hover:'止损区'
					}),
					new AnalogArcIndicator({
					    value: Global.formatAmount(value.stop * 10000),
					    noChange: true,
					    width: 15,
					    offset: 80,
					    color: '#ff9046',
					    hideValues: true,
					    hover:'预警区'
					}),
					new AnalogArcIndicator({
					    value: Global.formatAmount(value.warning * 10000),
					    noChange: true,
					    width: 15,
					    offset: 80,
					    color: '#7ff78d',
					    hideValues: true,
					    hover:'安全区'
					}),
					new AnalogNeedleIndicator({
						'value': 0,
						noChange: true,
					    'width': 4,
					    'length': 70,
					    'color': '#8e8e8e'
					}),
					new TextIndicator({
						x: 145,
						y: 155,
						color: '#666666',
						font: {family: "Arial", weight: 'bold', size: "18px"}
					})
    		    ]
    		});
    		me.riskControl.placeAt(me.RiskControlGaugeNode);
    		me.riskControl.startup();
    		me.changeControl(value.control);
    	},
    	
    	_setAvalaible_amountAttr: function(value) {
    		Global.aminNumber(this.avalaibleAmountNode, this.get('avalaible_amount'), value);
    	},
    	
    	_setContract_end_dateAttr: function(value) {
    		this.endDateNode.innerHTML = value;
    	},
    	
    	_setFreeze_amountAttr: function(value) {
    		Global.aminNumber(this.freezeAmountNode, this.get('freeze_amount'), value);
    	},
    	
    	_setAccrued_interestAttr: function(value) {
    		Global.aminNumber(this.accruedInterestNode, this.get('accrued_interest'), value / 100);
    	},
    	
    	_setLoan_amountAttr: function(value) {
    		Global.aminNumber(this.loanAmountNode, this.get('loan_amount'), value / 100);
    	},
    	
    	_setNet_assetAttr: function(value) {
    		Global.aminNumber(this.netAssetNode, this.get('net_asset'), value);
    	},
    	
    	_setTotal_assetAttr: function(value) {
    		Global.aminNumber(this.totalAssetNode, this.get('total_asset'), value);
    	},
    	
    	_setTotal_market_valueAttr: function(value) {
    		Global.aminNumber(this.stockPriceNode, this.get('total_market_value'), value);
    	},
    	
    	_setControlAttr: function(value) {
    		this._set('control', value);
    		if (this.riskControl) {
    			this.changeControl(value);
    		}
    	},
    	
    	changeControl: function(value) {
    		var point = this.riskControl.indicators[4],
				text = this.riskControl.indicators[5];
			point.valueNode.value = parseFloat(Global.formatAmount(value * 10000));
			point._updateValue(true);
			text.update(parseFloat(Global.formatAmount(value * 10000)), true);
    	},
    	
    	setValues: function(values, panel) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    		if (panel === 'loan') {
    			this.afterSet();
    		}
    	},
    	
    	// used for render by combine values
    	afterSet: function() {
    		var me = this,
    			contract = this.get('contract'),
    			loanApply = this.get('loanApply');
    		if (!contract && !loanApply) {
    			me.loanInitPanel = new LoanInitPanel({}, me.loanPanelNode);
    		}
    		else if (!contract && loanApply) {
    			me.loanCheckPanel = new LoanCheckPanel({}, me.loanPanelNode);
    			me.loanCheckPanel.set('loan_amount', loanApply['order_amount']);
    			me.loanCheckPanel.set('cash_amount', loanApply['cash_amount']);
    		} else {
    			me.loanInfoPanel = new LoanInfoPanel({}, me.loanPanelNode);
    			me.loanInfoPanel.set('loan_amount', contract['loan_amount'] - contract['repay_amount']);
    			me.loanInfoPanel.set('accrued_interest', contract['accrued_interest']);
    			me.loanInfoPanel.set('contract_end_date', contract['contract_end_date']);
    			me.loanInfoPanel.set('contract_no', contract.contract_no);
    		}
    	},
    	
    	hideLoading: function(panel) {
    		if (panel === 'stock') {
    			this.stockLoadingOverlay.hide();
    		}
    	},
    	
    	showLoading: function(panel) {
    		if (panel === 'stock') {
    			this.stockLoadingOverlay.show();
    		}
    	},
    	
    	render: function() {
    		var me = this;
    		// buttons
    		me.rechargeBtn = new Button({
    			label: '充值',
    			leftOffset: 0,
    			width: 60
    		}, me.rechargeBtnNode);
    		me.withdrawBtn = new Button({
    			label: '取现',
    			leftOffset: 0,
    			width: 60,
    			color: '#E2E2E2',
    			hoverColor: '#EDEDED',
    			textStyle: {
    				color: '#666666'
    			},
    			innerStyle: {
    				borderColor: '#C9C9C9'
    			},
    			disabled: true
    		}, me.withdrawBtnNode);
    		me.loanBtn = new Button({
    			label: '借款',
    			leftOffset: 0,
    			width: 60
    		}, me.loanBtnNode);
    		me.repayBtn = new Button({
    			label: '还款',
    			leftOffset: 0,
    			width: 60,
    			color: '#E2E2E2',
    			hoverColor: '#EDEDED',
    			textStyle: {
    				color: '#666666'
    			},
    			innerStyle: {
    				borderColor: '#C9C9C9'
    			}
    			
    		}, me.repayBtnNode);
    		me.stockLoadingOverlay = new LoadingOverlay({target: me.assetsPanel});
    		document.body.appendChild(me.stockLoadingOverlay.domNode);
    		
    		me.loanInitPanel = new LoanInitPanel();
    		me.loanInfoPanel = new LoanInfoPanel();
    	},
    	
    	postCreate: function() {
    		var me = this;
    	    me.render();
    	    this.inherited(arguments);
    	}
    });
});