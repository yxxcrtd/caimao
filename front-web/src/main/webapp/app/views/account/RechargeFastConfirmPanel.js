define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'app/ux/GenericTextBox',
    'app/ux/GenericButton',
    'dojo/dom-construct',
    'app/ux/GenericTooltip',
    'dojo/query',
    'app/views/ViewMixin',
    'dojo/_base/config',
    'dojo/text!./templates/RechargeFastConfirmPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		Global, TextBox, Button, domConstruct, Tooltip, query, ViewMixin, Config, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
        templateString: template,
	
	    baseUrl: Global.baseUrl,
	    
	    bankImg: '',
	    bankcardNo: '',
	    cardholder: '',
    	totalAmount: 0.00,
    	amount: 0.00,
    	tip: 0.00,
    	
    	next: function() {},
    	prev: function() {},
    	
    	_setBankImgAttr: function(value) {
    		this._set('bankImg', value); //call watch
    		this.bankImgNode.src = Config.baseUrl + 'app/resources/image/bosheng/borkers-logo/bank-'+value+'.jpg';
    	},

    	_setBankcardNoAttr: function(value) {
    		this._set('bankcardNo', value); //call watch
    		this.bankcardNoNode.innerHTML = value;
    	},

    	_setCardholderAttr: function(value) {
    		this._set('cardholder', value); //call watch
    		this.cardholderNode.innerHTML = '（' + value + '）';
    	},

    	_setAmountAttr: function(value) {
    		this._set('amount', value);
    		this.amountNode.innerHTML = Global.fillZero(Global.roundAmount(value));
    	},

    	_setTipAttr: function(value) {
    		this._set('tip', value);
    		this.tipNode.innerHTML = value;
    	},
    	
    	render: function() {
    		var me = this;
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
    			label: '确认充值',
    			style: {
    				marginLeft: '0px'
    			},
    			enter: true,
    			handler: me.next
    		}, me.confirmBtnNode);
    	},
    	
    	reset: function() {
    		//this.tradePwdFld.reset();
    	},
    	
    	afterSet: function() {
    		var totalAmount = Global.parseAmount(parseFloat(this.get('amount')) + parseFloat(this.get('tip')));
    		this.set('totalAmount', totalAmount);
    		this.totalAmountNode.innerHTML = Global.formatAmount(totalAmount);
    	},
    	
    	getValues: function() {
    		return {
    			totalAmount: this.get('totalAmount')
    		};
    	},
    	
    	showError: function(err) {
    		Tooltip.show(err, this.confirmBtn.domNode);
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});