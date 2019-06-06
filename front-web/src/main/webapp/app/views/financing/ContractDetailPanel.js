define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/query',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/on',
    'app/common/Date',
    'dojo/string',
    'app/ux/GenericButton',
    'dojo/text!./templates/ContractDetailPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		Global, query, dom, domConstruct, on, DateUtil, string, Button, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	baseUrl: Global.baseUrl,
    	
    	templateString: template,
    	
    	// contract param show
    	contract_no: '',
    	cur_interest_day_rate: Global.EMPTY,
    	accrued_interest: Global.EMPTY,
    	settled_interest: Global.EMPTY,
    	next_settle_interest_date: Global.EMPTY,
    	cash_amount: Global.EMPTY,
    	loan_ratio: Global.EMPTY,
    	contract_begin_date: Global.EMPTY,
    	contract_end_date: Global.EMPTY,
    	counterpart_user_id: Global.EMPTY,
    	counterpart_idcard: Global.EMPTY,
    	counterpart_name: Global.EMPTY,
    	loan_amount:Global.EMPTY,
    	contract_status: '',
    	
    	_setContract_noAttr: function(value) {
    		this._set('contract_no', value);
    		if (value) {
    			this.extendLinkNode.href = this.extendLinkNode.href + '?contract=' + value;
    		}
    	},
    	
    	_setContract_statusAttr: function(value) {
    		this._set('contract_status', value);
    		if (value == '1' || value == '3') {
    			this.render();
    		}
    	},
    	
    	_setCur_interest_day_rateAttr: function(value) {
    		this._set('cur_interest_day_rate', value);
    		this.interestRateNode.innerHTML = Global.formatAmount(value * 300000, 3) + '%';
    	},
    	
    	_setLoan_amountAttr: function(value) {
    		this._set('loan_amount', value);
    	},
    	
    	_setAccrued_interestAttr: function(value) {
    		this._set('accrued_interest', value);
    		this.accruedInterestNode.innerHTML = Global.formatAmount(value);
    	},
    	
    	_setSettled_interestAttr: function(value) {
    		this._set('settled_interest', value);
    		this.settledInterestNode.innerHTML = Global.formatAmount(value);
    	},
    	
    	_setNext_settle_interest_dateAttr: function(value) {
    		this._set('next_settle_interest_date', value);
    		this.settledDateNode.innerHTML = value;
    	},
    	
    	_setCash_amountAttr: function(value) {
    		this._set('cash_amount', value);
    		this.netAssetNode.innerHTML = Global.formatAmount(value);
    	},
    	
    	_setLoan_ratioAttr: function(value) {
    		this._set('loan_ratio', value);
    		this.ratioNode.innerHTML = Global.formatNumber(value / (value + 1) * 100, 2) + '%';
    	},
    	
    	_setContract_begin_dateAttr: function(value) {
    		this._set('contract_begin_date', value);
    		this.beginDateNode.innerHTML = value;
    		if (!Global.isEmpty(this.get('contract_end_date')) && !Global.isEmpty(value)) {
    			this.dayLeftNode.innerHTML = DateUtil.difference(DateUtil.today(), DateUtil.parse(this.get('contract_end_date')));
    		}
    	},
    	
    	_setContract_end_dateAttr: function(value) {
    		this._set('contract_end_date', value);
    		this.endDateNode.innerHTML = value;
    		if (!Global.isEmpty(this.get('contract_begin_date')) && !Global.isEmpty(value)) {
    			this.dayLeftNode.innerHTML = DateUtil.difference(DateUtil.today(), DateUtil.parse(value));
    		}
    	},
    	
    	_setCounterpart_user_idAttr: function(value) {
    		this._set('counterpart_user_id', value);
    		this.counterpartAccountNode.innerHTML = value;
    	},
    	
    	_setCounterpart_nameAttr: function(value) {
    		this._set('counterpart_name', value);
    		this.counterpartNameNode.innerHTML = Global.encodeInfo(value, 1, 0);
    	},
    	
    	_setCounterpart_idcardAttr: function(value) {
    		this._set('counterpart_idcard', value);
    		this.counterpartCardNode.innerHTML = Global.encodeInfo(value, 4, 4);
    	},
    	
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    		this.afterSet();
    	},
    	
    	afterSet: function() {
    		this.dayInterestNode.innerHTML = Global.formatAmount(this.loan_amount * this.cur_interest_day_rate);
    	},
    	
    	render: function() {
    		var me = this;
    		me.extendBtn = new Button({
    			label: '展期',
    			style: {
    				marginLeft: '0px'
    			},
    			width: 60,
    			color: '#E2E2E2',
    			hoverColor: '#EDEDED',
    			textStyle: {
    				color: '#666666'
    			},
    			innerStyle: {
    				borderColor: '#C9C9C9'
    			}
    		}, me.extendBtnNode);
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.inherited(arguments);
    	}
    	
    });
});