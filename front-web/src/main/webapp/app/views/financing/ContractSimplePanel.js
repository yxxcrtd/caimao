define([
    'dojo/_base/declare',
    'dijit/_WidgetBase',
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/query',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/on',
    'app/common/Date',
    'dojo/string',
    'dojo/text!./templates/ContractSimplePanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global,
            query, dom, domConstruct, on, DateUtil, string, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {

        templateString: template,
        baseUrl: Global.baseUrl,

        contract: '',
        contractNo:'',
        _setContractAttr: function(value) {
            this.loanAmountNode.innerHTML = Global.formatAmount(value.loanAmount);
            this.netAssetNode.innerHTML =Global.formatAmount(value.cashAmount);
            this.rateNode.innerHTML = value.interestDayRate*30*100+'%';
            this.endDayNode.innerHTML = Global.formatDate(value.contractEndDate);
        },

        _setContractNoAttr: function(value) {
            this.contractNo=value
            this.contractNoNode.innerHTML = value;
        },

        getData: function() {
            return {
                contract_no: this.get('contract_no')
            };
        },


        setValues: function(values) {
            for (var key in values) {
                this.set(key, values[key]);
            }

            this.afterSet();
        },

        afterSet: function() {
            this.loanAmountNode.innerHTML = Global.formatAmount(this.get('loan_amount') - this.get('repay_amount'));
        },

        postCreate: function() {
            var me = this;
            me.inherited(arguments);
        }

    });
});