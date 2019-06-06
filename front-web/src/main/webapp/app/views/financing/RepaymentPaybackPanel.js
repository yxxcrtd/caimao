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
    'dojo/text!./templates/RepaymentPaybackPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global, query, DisplayBox, TextBox, Button, dom, domConstruct, on, Tooltip, DateUtil, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	
    	availableAmount: Global.EMPTY,
    	interestAmount: Global.EMPTY,
    	totalAmount: Global.EMPTY,
    	loanAmount: '',
    	
    	next: function() {},
    	
    	_setAvailableAmountAttr: function(value) {
			this._set('availableAmount', value);
			this.availableAmountNode.innerHTML = Global.formatAmount(value);
		},
		
		_setTotalAmountAttr: function(value) {
			this._set('totalAmount', value);
		},
		
		_setLoanAmountAttr: function(value) {
			this.repayAmountFld.validates.push({
				pattern: function() {
					return this.getAmount() <= value;
				},
				message: '归还本金应小于或等于融资余额'
			});
		},
		
		_setInterestAmountAttr: function(value) {
			var me = this;
			if (Global.isEmpty(value)) {
				this.interestAmountNode.innerHTML = value;
			} else {
				var total = parseFloat(this.repayAmountFld.getAmount()) + value;
				this.set('totalAmount', total);
				this._set('interestAmount', value);
				this.interestAmountNode.innerHTML = Global.formatAmount(value);
				if (!Global.isEmpty(value) && !isNaN(value)) {
					this.repayAmountFld.validates.pop();
					this.repayAmountFld.validates.push({
						pattern: function() {
							return parseFloat(me.get('availableAmount')) >= total;
						},
						message: '可用金额必须大于或等于归还本金加利息'
					});
					this.repayAmountFld.checkValidity();
				}
			}
		},
		
		setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	render: function() {
    		var me = this,
    			tradePwdCtn, repayAmountCtn;
    		me.repayAmountFld = new TextBox({
    			style: {
					marginLeft: '10px',
					marginBottom: '0px'
				},
				validates: [{
					pattern: /.+/,
					message: '请输入归还本金'
				}],
				isNumber: true,
				isAmount: true,
				limitRegex: /[\d\.]/
    		}, me.repayAmountNode);
    		me.nextBtn = new Button({
    			label: '下一步',
    			handler: me.next,
    			leftOffset: 275,
    			enter:true,
    			disabled: true
    		}, me.nextBtnNode);
    	},
    	
    	isValid: function() {
    		return this.repayAmountFld.checkValidity();
    	},
    	
    	getData: function() {
    		return {
    			repay_amount: this.repayAmountFld.getAmount()
    		};
    	},
    	
    	showError: function(message) {
    		Tooltip.show(message, this.nextBtn.domNode);
    	},
    	
    	getValues: function() {
    		return {
    			repayAmount: this.repayAmountFld.getAmount(),
    			interestAmount: this.get('interestAmount'),
    			totalAmount: parseFloat(this.repayAmountFld.getAmount()) + parseFloat(this.get('interestAmount'))
    		};
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});