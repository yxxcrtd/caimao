define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'dojo/dom-construct',
    'dojo/query',
    'app/common/Global',
    'dojo/on',
    'dojo/dom-class',
    'dojo/mouse',
    'app/ux/GenericTextBox',
    'app/ux/GenericButton',
    'app/ux/GenericTooltip',
    'dojo/_base/config',
    'dojo/text!./templates/WithdrawInfoPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		domConstruct, query, Global, on, domClass, mouse, TextBox, Button, Tooltip, Config, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	baseUrl: Global.baseUrl,
    	availableAmount: 0.00,
    	bankcardInfo: '',
    	withdrawAmount: 0.00,
    	usernameReal: '',
    	
    	next: function() {},
    	
    	// when call setter, manually modify the dom display
    	_setAvailableAmountAttr: function(value) {
    		value=value/100;
    		this._set('availableAmount', value); //call watch
    		var parts = (value + '').split('.');
    		this.availableAmountIntNode.innerHTML = parts[0];
    		this.availableAmountDecNode.innerHTML = '.' + (parts[1] || '00'); //TODO format the dot
    	},
    	
    	_setBankcardInfoAttr: function(value) {
    		this._set('bankcardInfo', value); //call watch
    		this.bankcardImgNode.src = Config.baseUrl + 'app/resources/image/bosheng/borkers-logo/bank-'+value.bank_no+'.jpg';
    		this.bankcardNoNode.innerHTML = value.bank_card_no;
    		this.cardholderNode.innerHTML = '（' + this.usernameReal + '）';
    	},
    	
    	templateString: template,
    	
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
		
		render: function() {
    		var me = this,
    			withdrawAmountCtn = domConstruct.create('div', {style: 'background: #f9f9f9;padding: 20px 0 5px 0;margin-bottom: 20px;'}, me.formNode),
    			withdrawAmount = me.withdrawAmountFld = new TextBox({
    				label: '取现金额',
    				validates: [{
    					pattern: /.+/,
    					message: '请输入取现金额'
    				}, {
    					pattern: function() {
    						return withdrawAmount.get('value') <= me.availableAmount;
    					},
    					message: '取现金额不能大于可用金额'
    				}],
    				isNumber: true,
    				isAmount: true,
    				limitRegex: /[\d\.]/,
    				style: 'margin-left: 360px',
    				labelStyle: {
    					left: '-130px'
    				},
    				unit:'元'
    			}),
    			confirmBtn = me.confirmBtn = new Button({
    				label: '下一步',
    				handler: me.next,
    				enter: true,
    				style: 'margin-left: 360px'
    			});
    		
    		withdrawAmount.placeAt(withdrawAmountCtn);
    		confirmBtn.placeAt(me.formNode);
    	},
    	
    	getData: function() {
    		return {
    			'withdraw_amount': this.withdrawAmountFld.get('value')*100
    		}
    	},
    	getValues: function() {
    		return {
    			availableAmount: this.availableAmount,
    			bankcardInfo: this.bankcardInfo,
    			withdrawAmount: this.withdrawAmountFld.get('value')
    		};
    	},
    	
    	isValid: function() {
    		return this.withdrawAmountFld.checkValidity();
    	},
    	
    	showError: function(err) {
    		Tooltip.show(err.info, this.confirmBtn.domNode);
    	},
    	
    	postCreate: function(){
    		var me = this;
    		me.render();
    		
    	    this.inherited(arguments);
    	}
    });
});