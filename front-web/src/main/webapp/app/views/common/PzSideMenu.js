define([
    'dojo/_base/declare',
    'dijit/_WidgetBase',
    'dijit/_TemplatedMixin',
    'app/views/ViewMixin',
    'dojo/on',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/query',
    'app/common/Fx',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/_base/config',
    'dojo/text!./templates/PzSideMenu.html'
], function(declare, _WidgetBase, _TemplatedMixin, ViewMixin, on, dom, domConstruct, query, Fx, domClass, domStyle, cfg, template){
    return declare([_WidgetBase, _TemplatedMixin, ViewMixin], {
        templateString: template,

        active: '',

        _setActiveAttr: function(value) {
            value = value + '';
            var indexs = value.split(' '),
                index = indexs[0],
                subIndex;
            if (indexs.length == 2) {
                subIndex = indexs[1];
            }
            if (subIndex) {
                var fEl = query('>li', this.domNode)[index - 1];
                var next = fEl.nextElementSibling || fEl.nextSibling;
                var cEl = query('>li', next)[subIndex - 1];
                this.expandItem(query('>li', this.domNode)[index - 1]);
                domClass.add(cEl, 'active');
            } else {
                domClass.add(query('>li', this.domNode)[index - 1], 'active');
            }
        },

        render: function() {
            var me = this;
            on(this.domNode, 'li:click', function(e) {
                var next = this.nextElementSibling || this.nextSibling;
                if (next && next.tagName && next.tagName.toLocaleLowerCase() == 'ul') {
                    me.toggleItem(this);
                }
            });
            //if (cfg.isPopularize) {
            //    domConstruct.place(domConstruct.toDom('<li><i class="fa fa-users d-icon">' +
            //        '</i><em><a href="'+this.baseUrl+'/popularize/popularize.htm">推广赚钱</a></em></li>'), me.popularizeNode, 'replace');
            //} else {
            //    domConstruct.destroy(me.popularizeNode);
            //}

            //if (cfg.isProP2P) {
            //    me.P2PNode.style.display = 'block';
            //}
        },

        toggleItem: function(item) {
            this.collapseAll(item);
            var next = item.nextElementSibling || item.nextSibling;
            query('.d-caret-down', item).toggleClass('fa-rotate-180');
            if (next.tagName.toLocaleLowerCase() == 'ul') {
                Fx[next.expanded ? 'collapse' : 'expand'](next).play();
                next.expanded = !next.expanded;
            }
        },

        collapseItem: function(item) {
            var next = item.nextElementSibling || item.nextSibling;
            query('.d-caret-down', item).removeClass('fa-rotate-180');
            if (next.tagName.toLocaleLowerCase() == 'ul') {
                Fx.collapse(next).play();
                next.expanded = false;
            }
        },

        expandItem: function(item) {
            var next = item.nextElementSibling || item.nextSibling;
            query('.d-caret-down', item).addClass('fa-rotate-180');
            if (next.tagName.toLocaleLowerCase() == 'ul') {
                setTimeout(function() {
                    Fx.expand(next).play();
                }, 1);

                next.expanded = true;
            }
        },

        collapseAll: function(item) {
            var me = this;
            var items = query('ul', this.domNode).prev();
            items.forEach(function(i) {
                if (i != item) {
                    me.collapseItem(i);
                }
            });
        },

        buildRendering: function() {
            var me = this;
            me.inherited(arguments);
            me.render();
        }
    });
});