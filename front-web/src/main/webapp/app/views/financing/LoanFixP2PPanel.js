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
    'app/common/Product',
    'app/common/Position',
    'dojo/text!./templates/LoanFixPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin, Global, query,
		dom, domConstruct, domClass, domAttr, on, Tooltip, Product, Position, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,
        itemStyle: '',
        product: '',
        amounts: '',
        amount: '',
        term: 30,
        showFee: false,
        ratios: [],
        ratio: 1,
        unit: 0,
        carry: 0,

        // 记录总的所有的

        _setItemStyleAttr: function(value) {
            this._set('itemStyle', value);
        },
        
        getUnit: function() {
            return this.unit == 1 ? 0 : undefined;
        },

        _setAmountsAttr: function(value) {
            var activeItem, index;
            if (query('.list-amoutCompetition-item.active').length > 0) {
                activeItem = query('.list-amoutCompetition-item.active')[0];
                index = query('.list-amoutCompetition-item', this.domNode).indexOf(activeItem);
            }
            domConstruct.empty(this.selectNode);
            this.amounts = value;
            var h = '',
                i = 0, len = value.length;
            for (; i<len; i++) {
                var showOut = 0;
                //if(this.ratios[i] > 3 && this.product.addToProd == 3) showOut = 1;

                var disabled = '';
                var item = value[i];
                var deposit_amount = document.getElementById("deposit-amount-fld");
                //var fee_value = this.getFee(this.ratios[i], item);
                if (showOut == 1 || (deposit_amount != null && deposit_amount.value < 4000 && this.product.addToProd == 3) || item < this.product.prodAmountMin || item > this.product.prodAmountMax) {
                    disabled = 'disabled';
                }
                h += '<li style="'+this.itemStyle+'" class="list-amoutCompetition-item list-amoutCompetition-item-left '+disabled+' new_list_div" data-amount="'+item+'">' +
                    '<span style="color: #BB0B17 !important">' + (showOut == 1?'今日额度已满':'　　　　　　') + '</span>' +
                    '<div class="amount"><span class="loan am-ft-orange">'+Global.formatAmount(item, this.getUnit(), 'w', this.carry)+'</span>元' +
                    (this.showFee ? '<span class="fee">'+this.getFee(this.ratios[i], item)+'</span>': '') +'</div></li>'
            }
            domConstruct.place(domConstruct.toDom(h), this.selectNode);
            query('.amount', this.selectNode).forEach(function(el) {
                Position.adjustSize(el);
            });
            this.select(index);
        },

        getFee: function(ratio, loanAmount) {
            return Product.getBillText(Product.getBill(this.product, Product.getDetail(this.product, this.product.productDetails, ratio, loanAmount, this.term)), loanAmount);
        },

        _setProductAttr: function(value) {
            this.product = value;
            this.ratios = Product.getRatios(value);
            this.rateTextNode.innerHTML = value.prodLoanRatioMin + '-' + value.prodLoanRatioMax;
        },

        _setTitleAttr: function(value) {
            if (value == 'p2') { // month loan
                this.titleNode.innerHTML = '可融资金额';
                this.nextNode.style.display = 'none';
            }
            if (value == 'add') { // add
                this.titleNode.innerHTML = '所需保证金';
                this.nextNode.style.display = 'none';
            }
        },

        getAmount: function() {
            var selects = query('.active', this.domNode);
            if (selects.length == 1 && !domClass.contains(selects[0], 'disabled')) {
                return parseFloat(domAttr.get(selects[0], 'data-amount'));
            } else {
                return 0;
            }
        },

        select: function(index) {
            if (typeof index == 'number') {
                domClass.add(query('.list-amoutCompetition-item', this.domNode)[index], 'active');
                this.amount = this.amounts[index];
            }
        },

        render: function() {
            on(this.selectNode, 'li:click', function() {
                query('.active', this.parentNode).removeClass('active');
                domClass.add(this, 'active');
                Tooltip.hide();
            });
        },

        // 设置输入框的手续费率
        setFeeRate: function(value) {
            var selectList = query('.active:not(.disabled) .fee', this.selectNode);
            if (selectList.length > 0) {
                selectList[0].innerHTML = "月利率：" + value + "%";
            }
        },

        isValid: function() {
            var len = query('.active:not(.disabled)', this.selectNode).length;
            if (len > 0) {
                return true;
            } else {
                Tooltip.show('请选择可融金额', this.domNode, 'warning');
                return false;
            }
        },
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});