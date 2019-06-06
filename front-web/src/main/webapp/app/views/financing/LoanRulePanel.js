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
    'dojo/on',
    'app/ux/GenericTooltip',
    'app/common/Product',
    'app/common/Position',
    'dojo/_base/config',
    'app/common/Date',
    'dojo/text!./templates/LoanRulePanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin, Global, query,
            dom, domConstruct, on, Tooltip, Product, Position, cfg, DateUtil, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {

        templateString: template,

        product: '',
        productDetails: [],
        selectProductDetail: '',
        ratios: '',
        ratio: '',
        loanAmount: 0.00,
        depositAmount: 0.00,
        term: 0,
        bill: '',
        overtime: cfg.cutOffTime,
        overtimeStr: DateUtil.formatStr(cfg.cutOffTime, 'H:m:s', 'H点m分'),
        time: '',
        unit: 0,
        carry: 0,

        _setTimeAttr: function(value) {
            if (value.slice(11,19) >= this.overtime) {
                domConstruct.destroy(this.todaySelectNode);
            }
        },

        getAbstract: function() {
            return query('input:checked', this.tradeTimeNode)[0].value;
        },

        getUnit: function() {
            return this.unit == 1 ? 0 : undefined;
        },

        render: function() {
            var me = this;
            on(me.cashAmountCtnNode, 'input:click', function() {
                var index = query('input', me.cashAmountCtnNode).indexOf(this),
                    loan = me.loanAmount;
                var ratio = me.newRatios[index];
                me.ratio = ratio;
                me.selectIndex = index;
                var term = me.product.prodTerms;
                var detail = Product.getDetail(me.product, me.productDetails, ratio, loan, term);
                me._set('selectProductDetail', detail);
                if (detail) {
                	me.noteNode.innerHTML = detail.remark;
                } else {
                	me.noteNode.innerHTML = '';
                }
                
                me.initData(loan, ratio, detail);
            });
        },

        filterRatios: function(ratios, loanAmount) {
            var len = this.productDetails.length,
                k = 0, rlen = ratios.length, res = [];
            for (; k<rlen; k++) {
                var ratio = ratios[k];
                for (var i=0; i<len; i++) {
                    var pd = this.productDetails[i];
                    if (ratio >= pd.loanRatioFrom && ratio < pd.loanRatioTo && loanAmount >= pd.loanAmountFrom && loanAmount < pd.loanAmountTo) {
                        res.push(ratio);
                        break;
                    }
                }
            }
            return res;
        },

        produceSelects: function(loanAmount) {
            this.newRatios = this.filterRatios(this.ratios, loanAmount);
            domConstruct.empty(this.cashAmountCtnNode);
            var i = 0, cashHTML = '', len = this.newRatios.length;
            for (; i < len; i++) {
                var ratio = this.newRatios[i];
                //if(ratio >= 4) continue;
                cashHTML += '<label class="radio">' +
                    '<input type="radio" name="optionsRadios" value="option1" data-toggle="radio" class="custom-radio">' +
                    '<span class="td-numberOrange" id="cashAmountNode'+i+'">'+Global.formatAmount(loanAmount / ratio, this.getUnit(), '', this.carry)+'</span><span style="white-space: nowrap">元</span></label>';
            }
            var totalCash = domConstruct.toDom(cashHTML);
            domConstruct.place(totalCash, this.cashAmountCtnNode);
            //Position.adjustSize(this.cashAmountCtnNode);
        },

        afterSet: function(value) {
           // this.noteNode.innerHTML = this.product.prodNote;
            this.termsNode.innerHTML = this.product.prodTerms;
            var i = 0, cashHTML = '';
            this.ratios = Product.getRatios(this.product);
        },

        getValues: function() {
            return {
                loanAmount: this.loanAmount,
                depositAmount: this.depositAmount,
                interestAmount: this.interestAmount,
                bill: this.bill
            };
        },

        select: function(index) {
            var me = this;
            var target = query('input', me.cashAmountCtnNode)[index];
            if (target) {
                target.checked = true;
                me.selectIndex = index;
            } else if(query('input', me.cashAmountCtnNode)[0]) {
                query('input', me.cashAmountCtnNode)[0].checked = true;
                me.selectIndex = 0;
            } else {
                me.selectIndex = 0;
            }
            me.ratio = me.newRatios[me.selectIndex];
            var detail = Product.getDetail(me.product, me.productDetails, me.ratio, me.loanAmount, me.term);
            me.selectProductDetail = detail;
            me.noteNode.innerHTML =detail.remark;// this.product.prodNote;
            this.initData(me.loanAmount, me.ratio, detail);


        },

        isValid: function() {
            var inputs = query('input', this.cashAmountCtnNode);
            var index, i;
            for (i=0;i<inputs.length;i++) {
                if (inputs[i].checked) {
                    index = i;
                    break;
                }
            }
            if (typeof index == 'number') {
                return true;
            } else {
                Tooltip.show('请选择风险保证金', this.cashAmountCtnNode, 'warning');
            }
        },

        initData: function(loan, ratio, detail) {
            var total, enable, exposure, interest;
            if (!loan || !detail || !ratio) {
                total = 0;
                enable = 0;
                exposure = 0;
                interest = '';
            } else {
                total = loan * (1 + ratio) / ratio;
                enable = loan * detail.enableRatio;
                exposure = loan * detail.exposureRatio;
                interest = Product.getBill(this.product, detail);
            }
            this.depositAmount = (loan || 0) / ratio;
            this.interestAmount = interest;
            this.totalAmountNode.innerHTML = Global.formatAmount(total, this.getUnit(), '', this.carry);
            this.enableNode.innerHTML = Global.formatAmount(enable, this.getUnit(), '', this.carry);
            this.exposureNode.innerHTML = Global.formatAmount(exposure, this.getUnit(), '', this.carry);
            this.interestNode.innerHTML = Product.getBillText(interest, loan);
            this.bill = interest;
        },

        _setLoanAmountAttr: function(value) {
            this._set('loanAmount', value);
            this.produceSelects(value);
            this.select(this.selectIndex);
        },

        postCreate: function() {
            var me = this;
            me.render();
            me.inherited(arguments);
        }

    });
});