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
    'dojo/text!./templates/RechargeFastInfoPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		Global, TextBox, Button, domConstruct, Tooltip, query, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
        templateString: template,
	
	    baseUrl: Global.baseUrl,
	    
	    bankImg: '',
	    bankcardNo: '',
	    cardholder: '',
    	
    	
    	
    	next: function() {},
    	
//    	_setBankImgAttr: function(value) {
//    		this._set('bankImg', value); //call watch
//    		this.bankImgNode.src = Global.baseUrl + '/app/resources/image/bosheng/borkers-logo/bank-'+value+'.jpg';
//    	},
//    	
//    	_setBankcardNoAttr: function(value) {
//    		this._set('bankcardNo', value); //call watch
//    		this.bankcardNoNode.innerHTML = value;
//    	},
//    	
//    	_setCardholderAttr: function(value) {
//    		this._set('cardholder', value); //call watch
//    		this.cardholderNode.innerHTML = '（' + value + '）';
//    	},
    	
    	render: function() {
//    		var me = this,
//    			rechargeAmountCtn = domConstruct.create('div', {style: 'background: #f9f9f9;padding: 20px 0 5px 0;margin-bottom: 20px;'}, me.formNode),
//    			rechargeAmount = me.rechargeAmountFld = new TextBox({
//    				label: '充值金额',
//    				isAmount: true,
//    				validates: [{
//    					pattern: /.+/,
//    					message: '请输入充值金额'
//    				}, {
//    					pattern: /^\d+\.?\d*$/,
//    					message: '请输入正确的金额格式'
//    				}, {
//    					pattern: function() {
//    						return isNaN(parseFloat(this.get('value'))) ? true : (parseFloat(this.get('value')) > 0);
//    					},
//    					message: '金额数值必须大于0'
//    				}],
//    				limitRegex: /[\d\.]/,
//    				precision: 2,
//    				isNumber: true,
//    				unit: '元',
//    				onKeyPress: function(e) {
//    					var charOrCode = e.charCode || e.keyCode;
//    					if (charOrCode == 13) {
//    						me.onConfirm.call();
//    						e.preventDefault();
//    					}
//    				},
//    				onKeyUp: function(e) {
//    					var value = this.get('value');
//    					me.feeEl.style.display = value ? 'block' : 'none';
//    					var amountEl = query('span', me.feeEl)[0];
//    					amountEl.innerHTML = Global.formatAmount(parseFloat(value) * 0.002 *100);
//    				}
//    			}),
//    			confirmBtn = me.confirmBtn = new Button({
//    				label: '下一步',
//    				handler: me.next
//    			});
//    		
//    		me.feeEl = domConstruct.toDom('<span class="am-ft-md am-lh-30" style="position: absolute;top: -1px;display: none;left: 240px;white-space: nowrap;">手续费<span>0</span>元</span>');
//    		domConstruct.place(me.feeEl, me.rechargeAmountFld.domNode);
//    		rechargeAmount.placeAt(rechargeAmountCtn);
//    		confirmBtn.placeAt(me.formNode);
    	},
    	
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	getValues: function() {
//    		return {
//    			bankImg: this.bankImg,
//    			bankcardNo: this.bankcardNo,
//    			cardholder: this.cardholder,
//    			rechargeAmount: this.rechargeAmountFld.get('value'),
//    			fee: query('span', this.feeEl)[0].innerHTML
//    		};
    	},
    	isValid: function() {
    		//return this.rechargeAmountFld.checkValidity();
    	},
    	
    	showError: function(err) {
    		//Tooltip.show(err.info, this.confirmBtn.domNode);
    	},
    	
    	postCreate: function() {
    		var me = this;
    		//me.render();
    		me.inherited(arguments);
    	}
    	
    });
});