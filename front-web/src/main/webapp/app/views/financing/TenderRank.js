define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/query',
    'app/ux/GenericButton',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/on',
    'app/common/Date',
    'dojo/dom-class',
    'app/views/ViewMixin',
    'app/common/Product',
    'dojo/text!./templates/TenderRank.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin,
            Global, query, Button, dom, domConstruct, on, DateUtil, domClass, ViewMixin, Product, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,

        item: '',

        _setItemsAttr: function(data) {
            data = data || [];
            var i = 0, len = data.length,
                html = '';

            for (; i<len; i++) {
                var item = data[i];
                html += '<tr><td><em class="date">'+item.invrDatePay.slice(11,16)+'</em></td>' +
                    '<td><em class="phone-num">'+Global.encodeInfo(item.mobile, 3, 3)+'</em></td>' +
                    '<td><em class="money">￥'+Global.formatAmount(item.invrAmountPay, 2)+'</em></td></tr>';
            }
            domConstruct.place(domConstruct.toDom(html), this.rowCtnNode);
        },

    	render: function() {
    		var me = this;
    	},
    	
    	postCreate: function() {
    		var me = this;
            me.render();
    		me.inherited(arguments);
    	}
    	
    });
});