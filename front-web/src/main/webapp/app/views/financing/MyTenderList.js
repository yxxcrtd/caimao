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
    'app/views/ViewMixin',
    'app/common/Product',
    'app/views/financing/MyTenderListItem',
    'dojo/text!./templates/MyTenderList.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin,
            Global, query, Button, dom, domConstruct, on, DateUtil, ViewMixin, Product, TenderListItem, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,

        items: [],

        _setItemsAttr: function(items) {
            items = items || [];
            var i = 0, len = items.length;
            for (; i<len; i++) {
                //if (items[i].invsStatus != 3) {
                    var item = new TenderListItem({
                        item: items[i]
                    });
                    item.placeAt(this.itemCtnNode);
               // }
            }
        },

    	render: function() {
    		var me = this;
            if (me.params && me.params.hideTitle) {
                domConstruct.destroy(me.titleNode);
            }
    	},
    	
    	postCreate: function() {
    		var me = this;
            me.render();
    		me.inherited(arguments);
    	}
    	
    });
});