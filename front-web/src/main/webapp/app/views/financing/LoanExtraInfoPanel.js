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
    'dojo/text!./templates/LoanExtraInfoPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global, query, 
		DisplayBox, TextBox, Button, dom, domConstruct, on, DateUtil, LoanProtocolPanel, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	
    	availableAmount: 0.00,
    	contractNo: '',
    	percentage: 0,
    	warningLine: 0,
    	deadDate: '',
    	rate: 0,
    	list: '',
    	percentList: '',
    	loanAmount: 0.00,
    	maxLoanAmount: 0.00,
    	netAssets: 0.00,
    	loan_ratio: Global.EMPTY,
    	interest_day_rate:Global.EMPTY,
    	warningLine:Global.EMPTY,
    	exposure_ratio:Global.EMPTY,
    	total_loanAmount:Global.EMPTY,
    	proMaxLoanAmountNode: Global.EMPTY,
    	ratioUrl: '',
    	
    	next: function() {},
    	
    	_setMaxLoanAmountAttr: function(value) {
    		this._set('maxLoanAmount', value);
    		dom.byId('maxLoanAmount').innerHTML = Global.formatAmount(value);
			this.addAmountFld.validates.push({
				pattern: function() {
					return this.getAmount() <= value;
				},
				message: '金额数值必须小于或等于最大可借金额'
			});
    	},
    	
    	_setRatioUrlAttr: function(value) {
    		this._set('ratioUrl', value);
    		this.ratioLinkNode.href = value;
    	},
    	
    	_setAvailableAmountAttr: function(value) {
    		this._set('availableAmount', value);
			this.availableAmountNode.innerHTML = Global.formatAmount(value);
    	},
    	
    	_setProMaxLoanAmountAttr: function(value) {
    		this._set('proMaxLoanAmount', value);
			this.proMaxLoanAmountNode.innerHTML = Global.formatAmountUnit(Global.formatAmount(value));
    	},
    	
    	_setLoan_ratioAttr: function(value) {
    		this._set('loan_ratio', value);
			this.ratioNode.innerHTML = Global.formatNumber(value / (value + 1) * 100, 2) + '%';
    	},
    	
    	_setInterest_day_rateAttr: function(value) {
    		this._set('interest_day_rate', value);
			this.interest_day_rateNode.innerHTML = Global.formatAmount(value * 300000, 3) + '%';
    	},
    	
    	_setWarningLineAttr: function(value) {
    		this._set('warningLine', value);
			this.warningLineNode.innerHTML = Global.formatAmount(value);
    	},
    	_setExposure_ratioAttr: function(value) {
    		this._set('exposure_ratio', value);
			this.exposureRatioNode.innerHTML = Global.formatAmount(value);
    	},
    	_setTotal_loanAmountAttr: function(value) {
    		this._set('total_loanAmount', value);
			this.totalLoanNode.innerHTML = Global.formatAmount(value);
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
		
		setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	render: function() {
    		var me = this;
    		me.addAmountFld = new TextBox({
    			style: {
					marginLeft: '10px',
					marginBottom: '0px'
				},
				validates: [{
					pattern: /.+/,
					message: '请输入追加金额'
				}],
				isAmount: true,
				isNumber: true,
				limitRegex: /[\d\.]/
    		}, me.addLoanAmountNode);
    		me.nextBtn = new Button({
    			label: '下一步',
    			handler: me.next,
    			leftOffset: 360,
    			enter:true,
    			disabled: true
    		}, me.nextBtnNode);
    		me.protocolPanel = new LoanProtocolPanel();
    		me.protocolPanel.placeAt(me.protocolNode);
    	},
    	
    	isValid: function() {
    		return this.addAmountFld.checkValidity();
    	},
    	
    	getValues: function() {
    		return {
    			loanAmount: this.addAmountFld.getAmount(),
    			loan_ratio: this.ratioNode.innerHTML,
    			deadDate: this.get('deadDate'),
    			interest_day_rate: this.interest_day_rateNode.innerHTML,
    			exposure_ratio:this.exposureRatioNode.innerHTML,
    			warningLine:this.warningLineNode.innerHTML,
    			total_loanAmount:this.totalLoanNode.innerHTML
    		};
    	},
    	
    	getData: function() {
//    		return {
//    			orderAmount: this.extraAmountFld.get('value'),
//    			contractNo: this.get('contractNo')
//    		};
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});