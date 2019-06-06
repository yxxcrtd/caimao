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
    'app/ux/GenericTextBox',
    'dojo/text!./templates/LoanRandomPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin, Global, query,
		dom, domConstruct, on, TextBox, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,

        product: '',
        unit: 0,

        _setProductAttr: function(product) {
              this.loanAmountFld.validates.push({
                  pattern: function() {
                      return parseFloat(this.getAmount()) >= product.prodAmountMin;
                  },
                  message: '最小借款额为' + Global.formatAmount(product.prodAmountMin) + '元'
              });
            this.loanAmountFld.validates.push({
                pattern: function() {
                    return parseFloat(this.getAmount()) <= product.prodAmountMax;
                },
                message: '最大借款额为' + Global.formatAmount(product.prodAmountMax) + '元'
            });
            this.loanAmountFld.max=product.prodAmountMax;
            this.loanAmountFld.set('placeHolder', '最少'+Global.formatAmount(product.prodAmountMin,0,'w')+'，最多'+Global.formatAmount(product.prodAmountMax,0,'w'));
        },

        render: function() {
            var me = this;
            me.loanAmountFld = new TextBox({
                style: 'width: 370px;font-size: 30px;',
                validates: [{
                    pattern: /.+/,
                    message: '请输入金额'
                }],
                limitRegex: /[\d\.]/,
                isAmount: true,
                isNumber: true,
                precision: me.unit == 1 ? 0 : 2
            });
            me.loanAmountFld.placeAt(me.ctnNode);
        },

        isValid: function() {
            return this.loanAmountFld.checkValidity();
        },

        getAmount: function() {
            return this.loanAmountFld.getAmount();
        },
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});