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
    'dojo/_base/config',
    'app/common/Date',
    'dojo/text!./templates/LoanRulePanelP2.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin, Global, query,
		dom, domConstruct, on, Tooltip, ComboBox, Memory, Product, cfg, DateUtil, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,

        product: '',
        productDetails: [],
        selectProductDetail: '',
        loanAmount: 0.00,
        depositAmount: 0.00,
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
            var store = new Memory();
            me.daySelect = new ComboBox({
                store: store,
                searchAttr: 'id',
                labelAttr: 'name',
                editable: false,
                item: {name: '1个月', id: 30},
                style: 'width: 70px; display: inline-block;margin-left: 0px;',
                onChange: function(value) {

                }
            }, me.daySelectNode);
        },

        afterSet: function(value) {
            //this.noteNode.innerHTML = this.product.prodNote;
        },

        setData: function(loan, deposit) {
            var total, exposure, enable, interest;
            var detail = Product.getDetail(this.product, this.productDetails, (loan / deposit).toFixed(0), loan, this.daySelect.value);
            //console.log(this.product);
            //console.log(this.productDetails);
            /*if (!deposit || !loan) {
                total = 0;
                exposure = 0;
                enable = 0;
                interest = 0;
                this.noteNode.innerHTML ='';
            } else {
                total = deposit + loan;
                exposure = loan * detail.exposureRatio;
                enable = loan * detail.enableRatio;
                interest = Product.getBill(this.product, detail);
                this.noteNode.innerHTML = detail.remark;
            }*/
            if(!deposit || !detail){
                total = 0;
                exposure = 0;
                enable = 0;
                interest = 0;
                this.noteNode.innerHTML ='';
            }else{
                total = deposit + loan;
                exposure = loan * detail.exposureRatio;
                enable = loan * detail.enableRatio;
                interest = Product.getBill(this.product, detail);
                this.noteNode.innerHTML = detail.remark;
            }



            this.bill = interest;
            this.totalAmountNode.innerHTML = Global.formatAmount(total, this.getUnit(), '', this.carry);
            this.exposureNode.innerHTML = Global.formatAmount(exposure, this.getUnit(), '', this.carry);
            this.enableNode.innerHTML = Global.formatAmount(enable, this.getUnit(), '', this.carry);
            this.interestNode.innerHTML = Product.getBillText(interest, loan);
        },

        getValues: function() {
            return {
                loanAmount: this.loanAmount,
                depositAmount: this.depositAmount,
                bill: this.bill
            };
        },

        isValid: function() {
            return true;
        },
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});