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
    'dojo/text!./templates/LoanContractFixPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin, Global, query,
		dom, domConstruct, domClass, domAttr, on, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,

        product: '',
        amount: '',

        _setAmountsAttr: function(value) {
            domConstruct.empty(this.selectNode);
            this.amount = value;
            var h = '',
                i = 0, len = value.length;
            h += '<li class="list-amoutCompetition-item list-amoutCompetition-item-left" data-amount="'+value+'">' +
                    '<a class=""><span class="am-ft-orange">'+Global.formatAmountUnit(Global.formatAmount(value))+'</span>元</a></li>'
            
            domConstruct.place(domConstruct.toDom(h), this.selectNode);
        },

        _setProductAttr: function(value) {
            this.product = value;
            if (value == 'p2') { // month loan
                this.titleNode.innerHTML = '所需保证金';
                this.nextNode.style.display = 'none';
            }
        },

        getAmount: function() {
            return this.amount
         },

        render: function() {
            on(this.selectNode, 'li:click', function() {
                query('.active', this.parentNode).removeClass('active');
                domClass.add(this, 'active');
                Tooltip.hide();
            });
        },

        isValid: function() {
            var len = query('.active', this.selectNode).length;
            if (len > 0) {
                return true;
            } else {
                Tooltip.show('请选择金额', this.domNode, 'warning');
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