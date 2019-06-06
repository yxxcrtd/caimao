define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/views/ViewMixin',
    'app/common/Global',
    'dojo/query',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/dom-class',
    'dojo/dom-attr',
    'dojo/on',
    'app/ux/GenericTooltip',
    'app/ux/GenericButton',
    'app/ux/GenericTextBox',
    'app/common/Date',
    'dojo/text!./templates/LoanEnrollPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin, Global, query,
		dom, domConstruct, domClass, domAttr, on, Tooltip, Button, TextBox, DateUtil, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,

        product: '',
        fee: 0.00,
        totalAmount: 0.00,
        depositAmount: 0.00,
        loanAmount: 0.00,
        info: '',

        _setProductAttr: function(product) {
            var fee;
            this.loanAmountNode.innerHTML = Global.formatAmount(product.prodAmountMin);
            this.loanAmount = product.prodAmountMin;
            this.depositAmountNode.innerHTML = Global.formatAmount(product.prodAmountMin / product.prodLoanRatioMin);
            this.depositAmount = product.prodAmountMin / product.prodLoanRatioMin;
            this.totalAmountNode.innerHTML = Global.formatAmount(product.prodAmountMin * (product.prodLoanRatioMin + 1) / product.prodLoanRatioMin);
            this.dayNode.innerHTML = parseInt(product.prodTerms / 30);
            this.noteNode.innerHTML = Global.delHtmlTag(product.prodNote);
            if (product.prodBillType == 0) { // 0: free, 1: interest, 2: fee
                this.feeNode.innerHTML = Global.formatAmount(0);
                fee = 0;
            } else if (product.prodBillType == 1) {
                this.feeNode.innerHTML = Global.formatAmount(product.productDetails[0].interestDayRate * product.prodAmountMin) + '/天';
                fee = product.productDetails[0].interestDayRate * product.prodAmountMin;
            } else if (product.prodBillType == 2) {
                this.feeNode.innerHTML = Global.formatAmount(product.productDetails[0].fee);
                fee = product.productDetails[0].fee;
            }
            this.set('totalAmount', product.prodAmountMin / product.prodLoanRatioMin + fee);
        },

        _setInfoAttr: function(info) {
            domAttr.set(this.dayTip, {
                'data-help': DateUtil.format(DateUtil.parse(info.rankingDate, 'yyyy-MM-dd'), 'MM月dd日') + '公布战绩，发放奖金'
            });
        },

        _setDisabledAttr: function(value) {
            this.disabled = value;
            this.nextBtn.set('disabled', value);
            if (value) {
                this.nextBtn.set('label', '停止报名');
            }
        },

        render: function() {
            var me = this;
            me.nextBtn = new Button({
                'label': '开始报名',
                enter: true
            }, me.nextBtnNode);
            me.pwdFld = new TextBox({
                label: '安全密码',
                validates: [{
                    pattern: /.+/,
                    message: '请输入安全密码'
                }],
                style: 'margin: 20px 0px 0px 250px;',
                type: 'password'
            }, me.pwdNode);
        },

        isValid: function() {
            return this.pwdFld.checkValidity();
        },
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});