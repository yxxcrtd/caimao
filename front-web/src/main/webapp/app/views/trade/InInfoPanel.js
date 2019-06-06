define([
    'dojo/_base/declare',
    'dijit/_WidgetBase',
    'dijit/_TemplatedMixin',
    'app/views/ViewMixin',
    'app/common/Global',
    'app/ux/GenericTextBox',
    'app/ux/GenericButton', 
    'app/views/common/TradeAccountField',
    'dojo/text!./templates/InInfoPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, ViewMixin, Global, TextBox, Button, TradeAccountField, template){
    return declare([_WidgetBase, _TemplatedMixin, ViewMixin], {
        templateString: template,

        account: '',

        _setAccountAttr: function(data) {
            this._set('account', data);
            var enableAmount = data.avalaibleAmount - data.freezeAmount;
            this.enableAmountNode.innerHTML = Global.formatAmount(enableAmount);
            this.amountFld.validates.push({
                pattern: function() {
                    return parseFloat(this.getAmount()) <= enableAmount;
                },
                message: '转入金额不能大于可用余额'
            });
        },

        render: function() {
            var me = this;
            me.tradeAccountFld = new TradeAccountField({}, me.tradeFldNode);
            me.amountFld = new TextBox({
                validates: [{
                    pattern: /.+/,
                    message: '请输入转入金额'
                }],
                limitRegex: /[\d\.]/,
                isAmount: true,
                isNumber: true,
                style: 'display: inline-block;top: 8px;'
            }, me.amountNode);
            me.nextBtn = new Button({
                label: '下一步',
                enter: true
            }, me.nextBtnNode);
        },

        isValid: function() {
            return this.amountFld.checkValidity();
        },

        getValues: function() {
            return {
                num: this.tradeAccountFld.activeItem.operatorNo,
                prodName: this.tradeAccountFld.activeItem.prodName,
                enableAmount: this.account.avalaibleAmount - this.account.freezeAmount,
                amount: this.amountFld.getAmount()
            };
        },

        postCreate: function() {
            var me = this;
            me.render();
            me.inherited(arguments);
        }
    });
});