define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/query',
    'app/ux/GenericButton',
    'dojo/dom',
    'dojo/dom-attr',
    'dojo/dom-construct',
    'dojo/on',
    'app/common/Date',
    'app/views/ViewMixin',
    'app/common/Product',
    'app/ux/GenericProgressBar',
    'app/common/SVG',
    'dojo/has',
    'dojo/_base/sniff',
    'dojo/text!./templates/TenderListItem.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin,
            Global, query, Button, dom, domAttr, domConstruct, on, DateUtil, ViewMixin, Product, ProgressBar, SVG, has, sniff, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,

        item: '',

        _setItemAttr: function(item) {
            this.invsName.innerHTML = item.invsName;
            domAttr.set(this.invsName, {
            	title: item.invsName
            });
            this.invsAmountPreNode.innerHTML = Global.formatAmount(item.invsAmountPre, '', 'w');
            this.invsRateNode.innerHTML = Global.formatNumber(item.invsRate * 100) + '%';
            this.invsAmountLeftNode.innerHTML = Global.formatAmount(item.invsAmountPre - item.invsAmountActual, '', 'w');
            this.invsDuringNode.innerHTML = Global.formatNumber(item.invsDuring / 30, 0);
            this.invsNumPaysNode.innerHTML = item.invsNumPays;
            if (has("ie") <= 8) {
                this.rateNode.innerHTML = Global.formatNumber(item.invsAmountActual / item.invsAmountPre * 100) + '%';
            } else {
                SVG.circleProgress(this.rateNode, Global.formatNumber(item.invsAmountActual / item.invsAmountPre * 100), 80);
            }
            this.confirmBtn = new Button({
                label: '立即投标',
                width: 110
            }, this.btnNode);
            on(this.confirmBtn, 'click', function() {
                location = Global.baseUrl + '/tenderdetail.htm?invs=' + item.invsId;
            });
            if (parseInt(item.isAblePay) <= 0) {
                this.confirmBtn.set('disabled', true);
                if (item.isAblePay == -1) {
                    this.confirmBtn.set('label', '已满标');
                } else if (item.isAblePay == -2) {
                    this.confirmBtn.set('label', '购买期限已过');
                } else if (item.isAblePay == -3) {
                    this.confirmBtn.set('label', '未到发布时间');
                }
            }
        },

    	render: function() {
    		var me = this;
            //me.progressBar = new ProgressBar({}, me.progressNode);
    	},
    	
    	postCreate: function() {
    		var me = this;
            me.render();
    		me.inherited(arguments);
    	}
    	
    });
});