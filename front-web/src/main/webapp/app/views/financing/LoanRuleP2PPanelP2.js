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
    "dijit/form/HorizontalSlider",
    'dojo/store/Memory',
    'app/common/Product',
    'dojo/_base/config',
    'app/common/Date',
    'dojo/text!./templates/LoanRuleP2PPanelP2.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin, Global, query,
		dom, domConstruct, on, Tooltip, ComboBox, HorizontalSlider, Memory, Product, cfg, DateUtil, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,

        product: '',
        productDetails: [],
        p2pConfigList : [],
        selectProductDetail: '',
        loanAmount: 0.00,
        depositAmount: 0.00,
        bill: '',
        overtime: cfg.cutOffTime,
        overtimeStr: DateUtil.formatStr(cfg.cutOffTime, 'H:m:s', 'H点m分'),
        time: '',
        unit: 0,
        carry: 0,

        monthMaxRate:0,

        p2pConfig : [],

        // 是否开启
        isAvailableP2P : true,
        // 财猫出资
        cmValue: 0,
        // 月费率
        monthFeeRate: 0,
        // 杠杆倍数
        lever : 0,
        // 融资的利率
        pzFeeRate : 0,
        // 管理费
        manageFee : 0,
        manageRate : 0,
        // 当利率大于多少，财猫才进行出资
        caimaoRate : 0,

        _setTimeAttr: function(value) {
            if (value.slice(11,19) >= this.overtime) {
                domConstruct.destroy(this.todaySelectNode);
            }
        },
        // 设置月最大利率的
        setMonthMaxRate: function(value) {
            this.monthMaxRate = value;
            if (this.monthFee) {
                this.monthFee.maximum = parseInt(value * 10000);
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
            // 这个是资金使用期限的
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


            // 在写个月利息拖动的
            me.monthFee =  new HorizontalSlider({
                name: "slider",
                value: 65,
                minimum: 65,
                maximum: this.monthMaxRate * 1000,
                intermediateChanges: true,
                style: "width:250px;",
                discreteValues : parseInt(this.monthMaxRate * 1000 - 65),
                pageIncrement : 1,
                onChange: function(value){
                    //dom.byId("sliderValue").value = value;
                }
            }, me.monthFeeNode);

        },

        // 设置用户自定义月费率
        setFeeRate: function(value) {
            this.monthFeeRate = (value / 100).toFixed(4);
            this.interestNode.innerHTML = "月利率：" + value + '%';
            // 变更财猫的出资金额
            this.setCaimaoChuzi(this.p2pConfig, this.loanAmount);
        },

        afterSet: function(value) {
            //this.noteNode.innerHTML = this.product.prodNote;
        },

        // 计算财猫出资的数量
        setCaimaoChuzi: function(p2pConfig, loan) {
            //console.log(loan);
            //console.log(this.monthFeeRate);
            var cmValue = 0;
            if (!p2pConfig) {
                this.isAvailableP2P = false;
            } else if (p2pConfig.isAvailable == true) {
                this.isAvailableP2P = true;
                // 大于设置的值
                if (this.monthFeeRate >= p2pConfig.caimaoRate) {
                    var minInvestValue = parseInt(this.p2pConfigList.invest_min_value * 100);
                    // 计算财猫出资的数量
                    cmValue = parseInt(loan * p2pConfig.chuziRate);
                    var diff = cmValue % minInvestValue;
                    if (diff != 0) {
                        cmValue = parseInt(cmValue + (minInvestValue - diff));
                    }

                    if (cmValue < p2pConfig.chuziMin) cmValue = p2pConfig.chuziMin;
                    if (cmValue > p2pConfig.chuziMax) cmValue = p2pConfig.chuziMax;
                } else {

                }
            } else {
                this.isAvailableP2P = false;
            }
            this.cmValue = cmValue;
            this.caimaoVlaueNode.innerHTML = Global.formatAmount(cmValue, this.getUnit, '', this.carry);

            // 是否显示那些P2P融资的东西
            if (this.isAvailableP2P == true) {
                query('._p2p_node').forEach(function(el) {
                    el.style.display = '';
                });
            } else {
                query('._p2p_node').forEach(function(el) {
                    el.style.display = 'none';
                });
            }
        },

        setData: function(loan, deposit) {
            var total, exposure, enable, interest, cmValue, p2pTotal;
            var detail = Product.getDetail(this.product, this.productDetails, (loan / deposit).toFixed(0), loan, this.daySelect.value);
            this.p2pConfig = Product.getP2PConfig(this.product, this.product.p2pConfig, (loan / deposit).toFixed(0));
            this.loanAmount = loan;

            //console.log(this.p2pConfig);

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

                // 设置滑动块的值
                this.monthFee.value = parseInt(Global.formatNumber(interest[1] * 10000, 0));
                this.monthFee.minimum = this.monthFee.value - 10;
                //this.monthFee.minimum = parseInt(0.012 * 10000);
                this.monthFee.discreteValues = parseInt(this.monthFee.maximum - this.monthFee.minimum);
                // 怎么能让他重置回去呢
                //this.monthFee.postCreate();
                this.monthFee._bumpValue(0);
            }


            // 计算P2P借贷的总数量
            p2pTotal = loan;
            this.monthFeeRate = interest[1];
            this.lever = (loan / deposit).toFixed(0);
            this.pzFeeRate = interest[1];
            this.manageFee = this.p2pConfig == null ? 0 : this.p2pConfig.manageFee;
            this.manageRate = this.p2pConfig == null ? 0 : this.p2pConfig.manageRate;

            this.bill = interest;
            this.totalAmountNode.innerHTML = Global.formatAmount(total, this.getUnit(), '', this.carry);
            this.exposureNode.innerHTML = Global.formatAmount(exposure, this.getUnit(), '', this.carry);
            this.enableNode.innerHTML = Global.formatAmount(enable, this.getUnit(), '', this.carry);
            this.interestNode.innerHTML = Product.getBillText(interest, loan);
            this.p2pValueNode.innerHTML = Global.formatAmount(p2pTotal, this.getUnit, '', this.carry);
            // 设置财猫的出资数量
            this.setCaimaoChuzi(this.p2pConfig, loan);
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