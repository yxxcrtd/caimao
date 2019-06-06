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
    'dojo/dom-class',
    'dojo/on',
    'app/common/Date',
    'app/ux/GenericDropDownList',
    'app/common/Position',
    'dojo/text!./templates/LoanBorrowedPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global, 
		query, DisplayBox, TextBox, Button, dom, domConstruct, domClass, on, DateUtil, DropDownList, Position, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	isContract: false,
    	contractNo: '',
    	contractType: '',
    	status: '',
    	loanAmountLeft: 0.00,
    	loanAmount: 0.00,
    	repayAmount: 0.00,
    	unsettledInterest: 0.00,
    	totalInterest: 0.00,
    	settledInterest: 0.00,
    	interestRate: '',
    	cashAmount: 0.00,
    	ratio: 0,
    	endDate: '',
    	dayLeft: '',
    	accruedInterest: 0.00,
    	lastContractNo: '',
    	statusMap: {'0': '待审', '1': '已签', '2': '已终止', '3': '作废'},
    	
    	_setContractNoAttr: function(value) {
    		this._set('contractNo', value);
    		if (value) {
    			this.contractNoNode.innerHTML = value;
    		} else {
    			this.contractNoNode.innerHTML = '暂无';
    		}	
    	},
    	
    	_setInterestRateAttr: function(value) {
    		this._set('interestRate', value);
    		this.interestRateNode.innerHTML = Global.formatAmount(value * 300000, 3) + '%';
    	},
    	
    	_setContractTypeAttr: function(value) {
    		this._set('contractType', value);
    		if (value == 1) {
    			domClass.add(this.contractTypeNode, 'icon-contractPu');
    		} else if (value == 2) {
    			domClass.add(this.contractTypeNode, 'icon-contractDuan');
    		}	
    	},
    	
    	_setLastContractNoAttr: function(value) {
    		this._set('lastContractNo', value);
    		if (value) {
    			domConstruct.place(domConstruct.toDom('<span class="Linebar">|</span><a href="'+Global.baseUrl+'/financing/contract.htm?contract='+value+'">原合约</a>'), 
    					this.contractTitleNode);
    		}
    	},
	
    	_setLoanAmountLeftAttr: function(value) {
    		this._set('loanAmountLeft', value);
    		this.loanAmountLeftNode.innerHTML = Global.formatAmount(value);
    	},
    	
    	_setLoanAmountAttr: function(value) {
    		this._set('loanAmount', value);
    		this.loanAmountNode.innerHTML = Global.formatAmount(value);
    	},
    	
    	_setRepayAmountAttr: function(value) {
    		this._set('repayAmount', value);
    		this.repayAmountNode.innerHTML = Global.formatAmount(value);
    	},
    	
    	_setAccruedInterestAttr: function(value) {
			this._set('accruedInterest', value);
			this.accruedInterestNode.innerHTML = Global.formatAmount(value);
		},

    	_setSettledInterestAttr: function(value) {
    		this._set('settledInterest', value);
    		this.settledInterestNode.innerHTML = Global.formatAmount(value);
    	},
    	
    	_setCashAmountAttr: function(value) {
    		this._set('cashAmount', value);
    		this.cashAmountNode.innerHTML = Global.formatAmount(value);
    	},
    	
    	_setRatioAttr: function(value) {
    		this._set('ratio', value);
    		this.ratioNode.innerHTML = Global.formatNumber(value / (value + 1) * 100, 2) + '%';
    	},
    	
    	_setEndDateAttr: function(value) {
    		this._set('endDate', value);
    		this.endDateNode.innerHTML = value;
    	},
    	
    	_setDayLeftAttr: function(value) {
    		this._set('dayLeft', value);
    		this.dayLeftNode.innerHTML = value;
    	},
    	
    	baseUrl: Global.baseUrl,
    	
    	render: function() {
    		var me = this;
    		me.dropDownList = new DropDownList({
    			items: [{
    				text: '合约详情',
    				name: 'contract',
    				link: true
    			}, {
    				text: '追加借款',
    				name: 'addLoan',
    				link: true
    			}, {
    				text: '我要还款',
    				name: 'repayment',
    				link: true
    			}, {
    				text: '展期申请',
    				name: 'extension',
    				link: true
    			}, {
    				text: '融资协议',
    				name: 'protocol',
    				link: true
    			}]
    		});
    		me.dropDownList.placeAt(document.body);
    		var contractEl = query('[name=contract] a', me.dropDownList.domNode)[0],
    			addLoanEl = query('[name=addLoan] a', me.dropDownList.domNode)[0],
    			extensionEl = query('[name=extension] a', me.dropDownList.domNode)[0],
    			repaymentEl = query('[name=repayment] a', me.dropDownList.domNode)[0],
    			protocolEl = query('[name=protocol] a', me.dropDownList.domNode)[0];
    		protocolEl.href = Global.baseUrl + '/financing/loan/protocol.htm';
    		if (me.get('contractNo')) {
    			contractEl.href = Global.baseUrl + '/financing/contract.htm?contract='+me.get('contractNo');
    			addLoanEl.href = Global.baseUrl + '/financing/loan/extra.htm?contract='+me.get('contractNo');
    			extensionEl.href = Global.baseUrl + '/financing/loan/extension.htm?contract='+me.get('contractNo');
    			repaymentEl.href = Global.baseUrl + '/financing/repayment/payback.htm?contract='+me.get('contractNo');
    		} else {
    			domClass.add(contractEl, 'dropdownlist-item-disabled');
    			domClass.add(addLoanEl, 'dropdownlist-item-disabled');
    			domClass.add(extensionEl, 'dropdownlist-item-disabled');
    			domClass.add(repaymentEl, 'dropdownlist-item-disabled');
    		}
    	},
    	
    	addListeners: function() {
    		var me = this;
    		on(me.dropDownCtnNode, 'mouseenter', function(e) {
    			Position.topLeft(me.dropDownList.domNode, me.dropDownCtnNode);
    			me.dropDownList.domNode.style.display = 'block';
    		});
    		on(me.dropDownList, 'mouseleave', function(e) {
    			me.dropDownList.domNode.style.display = 'none';
    		});
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.addListeners();
    		me.inherited(arguments);
    	}
    	
    });
});