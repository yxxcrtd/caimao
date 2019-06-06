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
    'dojo/text!./templates/RechargeInfoPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin,
            Global, TextBox, Button, domConstruct, Tooltip, query, on, ViewMixin, BankcardSelectionField, Config, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
        templateString: template,

        baseUrl: Global.baseUrl,
        avalaibleAmount: '',
        next: function() {},
        register: function() {},

        _setAvalaibleAmountAttr: function(value) {
            this.avalaibleAmountNode.innerHTML = Global.formatAmount(value,2);
        },

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

        _setFeeAttr: function(value) {
        	this._set('fee', value); 
            this.tipNode.innerHTML = Global.formatAmount(value,2);
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
            me.amountFld = new TextBox({
                leftOffset: 140,
                label: '充值金额',
                style: {
                  'marginBottom': '20px'
                },
                validates: [{
                    pattern: /.+/,
                    message: '请输入充值金额'
                }],
                unit: '元',
                limitRegex: /[\d\.]/,
                isAmount: true,
                isNumber: true
            });
            me.bankListFld.placeAt(me.formNode, 1);
            me.amountFld.placeAt(me.formNode, 4);
            me.nextBtn = new Button({
                label: '下一步',
                enter: true,
                width: 180,
                height: 36,
                style: {
                    'marginLeft': '300px'
                }
            });
            me.nextBtn.placeAt(me.formNode, 7);
        },

        getValues: function() {
            return {
                bank: this.bankListFld.getBank(),
                amount: this.amountFld.getAmount(),
                fee: this.fee
            };
        },
        isValid: function() {
            return this.bankListFld.checkValidity() &&
                this.amountFld.checkValidity();
        },

        postCreate: function() {
            var me = this;
            me.render();
            me.inherited(arguments);
        }

    });
});