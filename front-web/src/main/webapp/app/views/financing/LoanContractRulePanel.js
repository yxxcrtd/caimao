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
    'app/ux/GenericComboBox',
    'dojo/store/Memory',
    'app/common/Product',
    'dojo/text!./templates/LoanContractRulePanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin, Global, query,
		dom, domConstruct, on, Tooltip, ComboBox, Memory, Product, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,

        product: '',
        productDetails: [],
        selectProductDetail: '',
        loanAmount: 0.00,
        ratio: '',
        depositAmount: 0.00,
        interestAmount: 0.00,
        term: '',

        render: function() {
            var me = this;
        },

        _setProductAttr: function(product) {
            this._set('product', product);
            var me = this;
            this.noteNode.innerHTML = product.prodNote;
            var unit = product.interestAccrualMode;
            if (unit == 0 || unit == 1) { // day
                var html = '<div class="cm-left text-left"><span class="am-ft-xxxl">'+product.prodTerms+'</span>' +
                    '天—<span class="am-ft-xxxl">N</span>天</div>';
                domConstruct.place(domConstruct.toDom(html), me.daySelectNode);
            } else if(unit == '2') { // month
                var temp = product.prodTerms.split(',');
                var storeData = [], i = 0, len = temp.length;
                for (; i < len; i++) {
                    storeData.push({'id': temp[i], 'name': temp[i] / 30 + '个月'});
                }
                var store = new Memory({
                    data: storeData
                });
                me.daySelect = new ComboBox({
                    store: store,
                    searchAttr: 'id',
                    labelAttr: 'name',
                    editable: false,
                    item: {name: '1个月', id: 30},
                    style: 'width: 70px; display: inline-block;margin-left: 0px;',
                    onChange: function(value) {
                        me.setData(me.loanAmount, me.ratio);
                    }
                }, me.daySelectNode);
            }
        },

        getValues: function() {
            return {
                loanAmount: this.loanAmount,
                depositAmount: this.depositAmount,
                produceId: 4,
                interestAmount: this.interestAmount
            };
        },

        isValid: function() {
            return true;
        },

        initData: function(loan, ratio) {
            var total, enable, exposure, interest;
            if (!loan || !ratio) {
                total = 0;
                enable = 0;
                exposure = 0;
                interest = 0;
            } else {
                total = loan * (1 + ratio.loanRatioFrom) / ratio.loanRatioFrom;
                enable = loan * ratio.enableRatio;
                exposure = loan * ratio.exposureRatio;
                interest = loan * ratio.interestDayRate;
            }
            this.depositAmount = (loan || 0) / ratio.loanRatioFrom;
            this.interestAmount = interest;
            this.totalAmountNode.innerHTML = Global.formatAmount(total);
            this.enableNode.innerHTML = Global.formatAmount(enable);
            this.exposureNode.innerHTML = Global.formatAmount(exposure);
            this.interestNode.innerHTML = Global.formatAmount(interest);
        },



        _setLoanAmountAttr: function(value) {
            this._set('loanAmount', value);
        },

        setData: function(loanAmount, ratio) {
            this._set('loanAmount', loanAmount);
            this._set('ratio', ratio);
            var term;
            if (this.daySelect) {
                term = this.daySelect.value;
            } else {
                term = this.product.prodTerms.split(',')[0];
            }
            this.selectProductDetail = Product.getDetail(this.product,
                    this.product.productDetails || this.productDetails, ratio, loanAmount, term);
            this.term = term;
            this.bill = Product.getBill(this.product, this.selectProductDetail);
            this.billNode.innerHTML = Product.getBillText(this.bill, loanAmount);
        },
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});