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
    'app/common/Dict',
    'dojo/text!./templates/ContractSidePanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin,
            Global, query, Button, dom, domConstruct, on, DateUtil, ViewMixin, Product, Dict, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,

        contract: '',
        homs: '',

        _setContractAttr: function(contract) {
            var me = this;
            me._set('contract', contract);
            me.statusNode.innerHTML = Dict.get('contractStatus')[parseInt(contract.contractStatus)];
            if (contract.prodTypeId == '0') {
                domConstruct.destroy(this.enableRatioCtn);
                domConstruct.destroy(this.exposureRatioCtn);
            }
            me.afterSet();
        },

        _setProductAttr: function (product) {
            if(!Product.addable(product)) {
                this.addBtn.destroy();
            }

            if(!Product.deferable(product)) {
                this.deferBtn.destroy();
            }
        },

        _setHomsAttr: function(homs) {
            var me = this;
            me._set('homs', homs);
            me.accountNode.innerHTML = homs.homsCombineId;
            me.totalAssetNode.innerHTML = Global.formatAmount(homs.totalAsset,2);
            me.enableWithdrawNode.innerHTML = Global.formatAmount(homs.enableWithdraw,2);
            me.totalMarketValueNode.innerHTML = Global.formatAmount(homs.totalMarketValue,2);
            me.totalProfitNode.innerHTML = Global.formatAmount(homs.totalProfit,2);
            this.enableRatioNode.innerHTML = Global.formatAmount(homs.enableRatio * homs.loanAmount,2);
            this.exposureRatioNode.innerHTML = Global.formatAmount(homs.exposureRatio * homs.loanAmount,2);
            me.afterSet();
        },

        afterSet: function() {
            if (this.contract && this.homs) {
                this.addLink.href = Global.baseUrl + '/financing/add.htm?contract=' + this.contract.contractNo + '&combine='+ this.homs.homsCombineId + '&fundaccount=' + this.homs.homsFundAccount;
                this.deferLink.href = Global.baseUrl + '/financing/defer.htm?contract=' + this.contract.contractNo + '&combine='+ this.homs.homsCombineId + '&fundaccount=' + this.homs.homsFundAccount;
            }
        },
    	
    	render: function() {
    		var me = this;
            me.addBtn = new Button({
                label: '追加',
                width: 170,
                height:36,
                color: '#EDEDED',
                hoverColor: '#E2E2E2',
                style: {
                    marginBottom: '10px'
                },
                textStyle: {
                    color: '#666666'
                },
                innerStyle: {
                    borderColor: '#C9C9C9'
                },
                position: 'center'
            });
            me.deferBtn = new Button({
                label: '展期',
                width: 170,
                height:36,
                color: '#EDEDED',
                hoverColor: '#E2E2E2',
                textStyle: {
                    color: '#666666'
                },
                innerStyle: {
                    borderColor: '#C9C9C9'
                },
                position: 'center'
            });
            me.repayBtn = new Button({
                label: '还款',
                width: 170,
                height:36,
                color: '#EDEDED',
                hoverColor: '#E2E2E2',
                textStyle: {
                    color: '#666666'
                },
                innerStyle: {
                    borderColor: '#C9C9C9'
                },
                position: 'center'
            });
            me.inBtn = new Button({
                label: '追加保证金',
                width: 170,
                height:36,
                color: '#EDEDED',
                hoverColor: '#E2E2E2',
                textStyle: {
                    color: '#666666'
                },
                innerStyle: {
                    borderColor: '#C9C9C9'
                },
                position: 'center'
            });
            me.outBtn = new Button({
                label: '转出盈利',
                width: 170,
                height:36,
                color: '#EDEDED',
                hoverColor: '#E2E2E2',
                textStyle: {
                    color: '#666666'
                },
                innerStyle: {
                    borderColor: '#C9C9C9'
                },
                position: 'center'
            });
            me.addBtn.placeAt(me.addBtnNode);
            me.deferBtn.placeAt(me.deferBtnNode);
            me.repayBtn.placeAt(me.repayBtnNode);
            me.inBtn.placeAt(me.inBtnNode);
            me.outBtn.placeAt(me.outBtnNode);
    	},
    	
    	postCreate: function() {
    		var me = this;
            me.render();
    		me.inherited(arguments);
    	}
    	
    });
});