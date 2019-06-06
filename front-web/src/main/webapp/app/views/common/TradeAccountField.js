define([
    'dojo/_base/declare',
    'dijit/_WidgetBase',
    'dijit/_TemplatedMixin',
    'app/views/ViewMixin',
    'dojo/on',
    'dojo/query',
    'app/common/Fx',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/when',
    'app/common/Data',
    'app/ux/GenericTooltip',
    'dojo/mouse',
    'app/common/Array',
    'app/common/Global',
    'dojo/text!./templates/TradeAccountField.html'
], function(declare, _WidgetBase, _TemplatedMixin, ViewMixin, on, query, Fx, dom,
            domConstruct, domClass, domStyle, when, Data, Tooltip, mouse, ArrayUtil, Global, template){
    return declare([_WidgetBase, _TemplatedMixin, ViewMixin], {
        templateString: template,

        items: '',
        activeItem: '',

        _setItemsAttr: function(items) {
            items = items || [];
            this._set('items', items);
            var html = '', i = 0, len = items.length;
            for (; i<len; i++) {
                var item = items[i];
                html += '<li><span class="li-inner"><em class="mt-num-xl">'+item.operatorNo+'</em>  '+item.prodName+'</span></li>';
            }
            domConstruct.place(domConstruct.toDom(html), this.itemCtn);
        },

        select: function(index) {
            var items = query('li', this.itemCtn);
            items.removeClass('active');
            items.at(index).addClass('active');
            this.activeItem = this.items[index];
        },

        render: function() {
            var me = this;
            when(Data.getTradeAccounts()).then(function(items) {
                items = ArrayUtil.filter(items, function(i) {
                    return i.contractNo;
                });
                me.set('items', items);
                me.select(0);
            });
            on(me.itemCtn, 'li:click', function(e) {
                var items = query('li', me.itemCtn),
                    index = items.indexOf(this);
                me.select(index);
            });
            on(me.itemCtn, on.selector('li', mouse.enter), function(e) {
                var items = query('li', me.itemCtn),
                    index = items.indexOf(this),
                    item = me.items[index],
                    that = this;
                when(Data.getTradeAccountAsset({
                    homs_fund_account: item.homsFundAccount,
                    homs_combine_id: item.homsCombineId
                })).then(function(asset) {
                    if (asset) {
                        var html = '<div>总资产：'+Global.formatAmount(asset.totalAsset, 2)+'元</div>' +
                            '<div>总市值：'+Global.formatAmount(asset.totalMarketValue, 2)+'元</div>' +
                            '<div>总盈利：'+Global.formatAmount(asset.totalProfit, 2)+'元</div>' +
                            '<div>可取金额：'+Global.formatAmount(asset.enableWithdraw, 2)+'元</div>' +
                            '<div>可用余额：'+Global.formatAmount(asset.curAmount, 2)+'元</div>' +
                            '<div>期初账户金额：'+Global.formatAmount(asset.beginAmount, 2)+'元</div>' +
                            '<div>总单元净值：'+Global.formatAmount(asset.totalNetAssets, 2)+'元</div>';
                        if(item.prodTypeId=='0'){//如果是免费体验,没有预警比例和止损比例
                    		html+='<div>借款金额：'+Global.formatAmount(asset.loanAmount, 2)+'元</div>' +
                            '<div>单元现金余额：'+Global.formatAmount(asset.currentCash, 2)+'元</div>';
                    	}else{
                            html+='<div>预警比例：'+asset.enableRatio+'</div>' +
                            '<div>止损比例：'+asset.exposureRatio+'</div>' +
                            '<div>借款金额：'+Global.formatAmount(asset.loanAmount, 2)+'元</div>' +
                            '<div>单元现金余额：'+Global.formatAmount(asset.currentCash, 2)+'元</div>';
                    	}
                        Tooltip.show(html, that);
                    }
                });

            });
            on(me.itemCtn, on.selector('li', mouse.leave), function(e) {
                Tooltip.hide();
            });
        },

        buildRendering: function() {
            var me = this;
            me.inherited(arguments);
            me.render();
        }
    });
});