define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/query',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/dom-class',
    'dojo/on',
    'app/ux/GenericTooltip',
    'dojo/text!./templates/GenericSlideContainer.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global, 
		query, dom, domConstruct, domClass, on, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,

        items: [],

        getActiveItem: function() {
            var i = 0,
                len = this.items.length;
            for (; i < len; i++) {
                if (this.items[i].activeItem) {
                    return this.items[i];
                }
            }
        },

        next: function() {
            var item = this.getActiveItem(),
                items = this.items,
                index = items.indexOf(item);
            item.hide();
            item.activeItem = false;
            var nextItem = items[index !== items.length - 1 ? (index + 1) : 0];
            nextItem.activeItem = true;
            nextItem.show();
        },

        add: function() {
            var me = this,
                items = this.items,
                initLen = items.length,
                newItems = arguments,
                i = 0, len = newItems.length;
            for (; i < len; i++) {
                var nt = newItems[i];
                items.push(nt);
                nt.domNode.style.display = 'none';
                nt.placeAt(me.domNode);
            }
            if (initLen == 0 && newItems.length > 0) {
                newItems[0].domNode.style.display = 'block';
                newItems[0].activeItem = true;
            }
        },

        isValid: function() {
          return this.getActiveItem().isValid();
        },
    	
    	postCreate: function() {
    		var me = this;
    		me.inherited(arguments);
    	}
    	
    });
});