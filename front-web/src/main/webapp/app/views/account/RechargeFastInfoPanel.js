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
    'dojo/on',
    'app/views/ViewMixin',
    'app/views/common/BankcardSelectionField',
    'dojo/_base/config',
    'dojo/text!./templates/RechargeFastInfoPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		Global, TextBox, Button, domConstruct, Tooltip, query, on, ViewMixin, BankcardSelectionField, Config, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
        templateString: template,
	
	    baseUrl: Global.baseUrl,
    	next: function() {},
    	register: function() {},
    	
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
    	
    	_setTipAttr: function(value) {
    		this._set('tip', Global.parseAmount(value));
    		this.tipNode.innerHTML = isNaN(value) ? '' : Global.formatAmount(value * 100);
    	},
    	
    	render: function() {
    		var me =  this;
            me.bankListFld = new BankcardSelectionField({
                id: 'tofocus',
                label: '充值银行',
                style: {
                    'paddingLeft': '130px'
                }
            });
            me.bankcardNoFld = new TextBox({
                leftOffset: 140,
                label: '储蓄卡卡号',
                validates: [{
                    pattern: /.+/,
                    message: '请输入储蓄卡卡号'
                }, {
                    pattern: function() {
                        return /^\d+$/.test(this.get('value').replace(/\s/g, ''));
                    },
                    message: '储蓄卡卡号必须是数字'
                }, {
                    pattern: function() {
                        return /^\d{6,}$/.test(this.get('value').replace(/\s/g, ''));
                    },
                    message: '储蓄卡卡号至少6位数字'
                }],
                onKeyUp: function(e) {
                    var value = this.get('value').replace(/\s/g,''),
                        len = value.length;
                    if (len == 5 || len == 9 || len == 13 || len == 17) {
                        value = value.split('');
                        while(len > 1) {
                            value.splice(len - 1, 0, ' ');
                            len = len - 4;
                        }
                        value = value.join('');
                        this.set('value', value);
                    }
                }
            });
            me.amountFld = new TextBox({
                leftOffset: 140,
                label: '充值金额',
                validates: [{
                    pattern: /.+/,
                    message: '请输入充值金额'
                }],
                unit: '元',
                limitRegex: /[\d\.]/,
                isAmount: true,
                isNumber: true
            });
            me.bankListFld.placeAt(me.formNode, 2);
            me.bankcardNoFld.placeAt(me.formNode, 5);
            me.amountFld.placeAt(me.formNode, 6);
            me.nextBtn = new Button({
                label: '下一步',
                width: 180,
                height: 36,
                style: {
                    'marginLeft': '300px'
                }
            });
            me.nextBtn.placeAt(me.formNode, 9);
    	},
    	
    	afterSet: function() {
    		var me = this,
    			support = parseInt(this.get('bank_quick_pay')),
    			regist = parseInt(this.get('user_quick_pay'));
    	
    		if (!support || support != 1) {
    			var html = domConstruct.toDom('<span>您绑定的银行卡不支持快捷支付，请重新<a href="'+Global.baseUrl+'/account/bankcard/bind.htm">绑定银行卡</a></span>');
    			domConstruct.place(html, me.bankcardTextNode);
    			domConstruct.destroy(me.actionNode);
    		} else if (support == 1 && !regist && regist != 1) {
    			var html = domConstruct.toDom('<span>您还未签约，请重新<a href="'+Global.baseUrl+'/account/bankcard/bind.htm">绑定银行卡</a></span>');
    			domConstruct.place(html, me.bankcardTextNode);
    			domConstruct.destroy(me.actionNode);
    		} else {
    			me.actionNode.style.display = 'block';
    		}
    	},
    	
    	getValues: function() {
    		return {
    		};
    	},
    	isValid: function() {
    		//return this.rechargeAmountFld.checkValidity();
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});