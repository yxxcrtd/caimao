define([
    'dojo/_base/declare',
    'dijit/_WidgetBase',
    'dijit/_TemplatedMixin',
    'app/views/ViewMixin',
    'app/common/Global',
    'app/ux/GenericButton',  
    'dojo/text!./templates/InConfirmPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, ViewMixin, Global, Button, template){
    return declare([_WidgetBase, _TemplatedMixin, ViewMixin], {
        templateString: template,

        _setNumAttr: function(value) {
            this.numNode.innerHTML = value;
        },

        _setProdNameAttr: function(value) {
            this.prodNode.innerHTML = value;
        },

        _setEnableAmountAttr: function(value) {
            this.enableAmountNode.innerHTML = Global.formatAmount(value);
        },

        _setAmountAttr: function(value) {
            this.amountNode.innerHTML = Global.formatAmount(value);
        },

        render: function() {
            var me = this;
            me.prevBtn = new Button({
                label: '上一步',
                height: 36,
                color: '#E2E2E2',
                hoverColor: '#EDEDED',
                textStyle: {
                    color: '#666666'
                },
                innerStyle: {
                    borderColor: '#C9C9C9'
                }
            }, me.prevNode);
            me.confirmBtn = new Button({
                label: '确认转入',
                height: 36,
                style: {
                    marginLeft: '0px'
                },
                enter: true,
                handler: me.next
            }, me.confirmNode);
        },

        postCreate: function() {
            var me = this;
            me.render();
            me.inherited(arguments);
        }
    });
});