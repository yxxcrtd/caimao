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
    'dojo/text!./templates/RechargeConfirmPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin,
            Global, TextBox, Button, domConstruct, Tooltip, query, ViewMixin, Config, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
        templateString: template,

        baseUrl: Global.baseUrl,

        next: function() {},
        prev: function() {},

        _setBankAttr: function(value) {
            this.bankImgNode.src = Config.baseUrl + 'app/resources/image/bosheng/borkers-logo/bank-'+value.bankNo+'.jpg';
        },

        _setAmountAttr: function(value) {
            this.amountNode.innerHTML = Global.formatAmount(value,2);
        },

        _setFeeAttr: function(value) {
            this.tipNode.innerHTML = Global.formatAmount(value,2);
        },

        render: function() {
            var me = this;
            me.preBtn = new Button({
                label: '上一步',
                height: 36,
                style: {
                    marginLeft: '260px'
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
                height: 36,
                style: {
                    marginLeft: '0px'
                },
                enter: true,
                handler: me.next
            }, me.confirmBtnNode);
        },

        afterSet: function(data) {
            this.totalAmountNode.innerHTML = Global.formatAmount(parseFloat(data.fee) + parseFloat(data.amount),2);
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