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
    'app/ux/GenericButton',
    'dojo/text!./templates/LoanInfoPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		domConstruct, query, Global, on, dom, Button, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	accrued_interest: 0.00,
    	loan_amount: 0.00,
    	contract_end_date: '',
    	contract_no: '',
    	
    	_setContract_noAttr: function(value) {
    		this.loanLinkNode.href = this.loanLinkNode.href + '?contract=' + value;
    		this.repayLinkNode.href = this.repayLinkNode.href + '?contract=' + value;
    	},
    	
    	_setAccrued_interestAttr: function(value) {
    		this.accruedInterestNode.innerHTML = Global.formatAmount(value);
    	},
    	
    	_setLoan_amountAttr: function(value) {
    		this.loanAmountNode.innerHTML = Global.formatAmount(value);
    	},
    	
    	_setContract_end_dateAttr: function(value) {
    		this.endDateNode.innerHTML = value;
    	},
    	
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	render: function() {
    		var me = this;
    		me.loanBtn = new Button({
    			label: '追加',
    			style: {
    				marginLeft: '0px'
    			},
    			width: 60
    		}, me.loanBtnNode);
    		me.repayBtn = new Button({
    			label: '还款',
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
    		}, me.repayBtnNode);
    	},
    	
    	postCreate: function() {
    		var me = this;
    	    me.render();
    	    this.inherited(arguments);
    	}
    });
});