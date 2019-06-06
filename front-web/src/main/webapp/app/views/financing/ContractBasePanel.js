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
    'app/ux/GenericTooltip',
    'dojo/text!./templates/ContractBasePanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin,
            Global, query, Button, dom, domConstruct, on, DateUtil, ViewMixin, Product, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,

        contract: '',
        homs: '',
        childs: '',

        _setHomsAttr: function(homs) {
            this._set('homs', homs);
        },

        _setContractAttr: function(contract) {
            var me = this;
            me._set('contract', contract);
            me.loanAmountNode.innerHTML = Global.formatAmount(contract.loanAmount,2);
            me.depositAmountNode.innerHTML = Global.formatAmount(contract.cashAmount,2);
            me.totalAmountNode.innerHTML = Global.formatAmount(contract.loanAmount + contract.cashAmount,2);
            me.titleNode.innerHTML = contract.prodName;
            me.typeNode.innerHTML = !contract.relContractNo ? '主' : '子';
            me.noNode.innerHTML = contract.contractNo;
            me.startDateNode.innerHTML = contract.contractSignDatetime;
            me.billNode.innerHTML = Product.getBillText(Product.getBill(contract), contract.loanAmount);
            me.beginDateNode.innerHTML = contract.contractBeginDate;
            me.endDateNode.innerHTML = contract.contractEndDate;
        },

        _setChildsAttr: function(childs) {
        	//如果没有子合约，则不在页面上显示。。进行销毁
        	if(childs.length == 0){
        		domConstruct.destroy(this.subCtnNode);
        		return;
        	}
            var html = '', i = 0, len = childs.length;
            for (; i<len; i++) {
                html += '<a href="'+this.baseUrl + '/financing/contract.htm?contract=' + childs[i] + '&' +
                    'combine='+ this.homs.homsCombineId + '&fundaccount=' + this.homs.homsFundAccount+'">'+childs[i]+'</a><br/>';
            }
            this.subCtnHtml = html;
        },
    	
    	render: function() {
    		var me = this;
            on(me.subCtnNode, 'mouseenter', function() {
                if (me.subCtnHtml) {
                    Tooltip.show(me.subCtnHtml, me.subCtnNode);
                }
            });
            on(me.subCtnNode, 'mouseleave', function() {
                if (me.subCtnHtml) {
                    Tooltip.hide();
                }
            });
    	},
    	
    	postCreate: function() {
    		var me = this;
            me.render();
    		me.inherited(arguments);
    	}
    	
    });
});