define([
    'dojo/_base/declare',
    'dijit/_WidgetBase',
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/views/ViewMixin',
    'app/common/Global',
    'dojo/query',
    'dojo/dom',
    'dojo/dom-class',
    'dojo/dom-construct',
    'dojo/on',
    'app/common/Date',
    'dojo/string',
    'app/common/Fx',
    'app/common/Dict',
    'app/common/Product',
    'dojo/text!./templates/ContractInfoPanel.html'
], function (declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin, Global, query, dom, domClass,
             domConstruct, on, DateUtil, string, Fx, Dict, Product, template) {
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {

        templateString: template,

        contract: '',
        homs: '',
        apply: '',

        noLink: false,

        expanded: false,

        _setHomsAttr: function (value) {
            this._set('homs', value);
        },

        _setNoLinkAttr: function (value) {
            if (value) {
                domConstruct.destroy(this.linkCtnNode);
                query('.node-content .txt', this.domNode).style({
                    padding: '25px 45px'
                });
            }
        },

        _setApplyAttr: function (apply) {
            this.apply = apply;
            this.labelNode.innerHTML = '申请单';
            this.contractNoNode.innerHTML = apply.orderNo;
            this.contractStatusNode.innerHTML = Dict.get('loanApplyStatus')[parseInt(apply.verifyStatus)];
            this.contractBeginDateNode.innerHTML = apply.applyDatetime;
            this.loanAmountNode.innerHTML = Global.formatAmount(apply.orderAmount,2);
            this.cashAmountNode.innerHTML = Global.formatAmount(apply.cashAmount,2);
            this.feeNode.innerHTML = Global.getInterest(apply);
            this.contractEndDateNode.innerHTML = apply.contractEndDate;
            this.freezeAmountNode.innerHTML = "冻结金额";
            this.productTypeNode.innerHTML = apply.prodName;
            this.nextSettleInterestDateNode.innerHTML = Global.formatAmount(apply.freezeAmount,2)+" 元";
            query('.node-content .txt', this.domNode).style({
                padding: '25px 25px'
            });
        },

        _setProductAttr: function (product) {
            if(!Product.addable(product)) {
                domConstruct.destroy(this.addLink);
            }

            if(!Product.deferable(product)) {
                domConstruct.destroy(this.deferLink);
            }
            
            if(product.prodTypeId==3 || product.prodTypeId == 0){
            	domConstruct.destroy(this.freezeAmountNode);
            	domConstruct.destroy(this.nextSettleInterestDateNode);
            }
        },

        _setContractAttr: function (contract) {
            this.contract = contract;
            this.ext_text_contract_info.style.display = 'block';
            this.productTypeNode.innerHTML = contract.prodName;
            this.loanAmountNode.innerHTML = Global.formatAmount(contract.loanAmount,2);
            this.cashAmountNode.innerHTML = Global.formatAmount(contract.cashAmount,2);
            this.feeNode.innerHTML = Global.getInterest(contract);
            this.contractBeginDateNode.innerHTML = contract.contractSignDatetime.substr(0,10);
            this.contractEndDateNode.innerHTML = contract.contractEndDate;
            this.nextSettleInterestDateNode.innerHTML = contract.nextSettleInterestDate?contract.nextSettleInterestDate:'无';
            this.labelNode.innerHTML = contract.contractType == '0' ? '主合约' : '子合约';
            this.contractNoNode.innerHTML = contract.contractNo;
            this.contractStatusNode.innerHTML = Dict.get('contractStatus')[contract.contractStatus];
            this.contractLink.href = Global.baseUrl + '/financing/contract.htm?contract=' + contract.contractNo + '&combine=' + this.homs.homsCombineId + '&fundaccount=' + this.homs.homsFundAccount;
            this.addLink.href = Global.baseUrl + '/financing/add.htm?contract=' + contract.contractNo + '&combine=' + this.homs.homsCombineId + '&fundaccount=' + this.homs.homsFundAccount;
            this.deferLink.href = Global.baseUrl + '/financing/defer.htm?contract=' + contract.contractNo + '&combine=' + this.homs.homsCombineId + '&fundaccount=' + this.homs.homsFundAccount;
            this.repayLink.contractNo = contract.contractNo;
            //this.homsCombineIdNode.innerHTML = contract.homsCombineId || '无homs子账号分配';
            if (contract.contractStatus == 1) {
                //domConstruct.destroy(this.linkCtnNode);
                // 如果是历史合约, 取消"合约详情""我要追加""我要还款"几个链接
                domConstruct.destroy(this.hoverNode);
            }
        },

        _setExpandedAttr: function (value) {
            var me = this;
            me.expanded = value;
            domClass[value ? 'remove' : 'add'](me.toggleNode, 'fa-rotate-270');
            Fx[value ? 'expand' : 'collapse'](me.contentNode).play();
        },

        render: function () {
            var me = this;
            /*on(me.hoverNode, 'mouseenter', function () {
                me.menuNode.style.display = 'block';
            });
            on(me.hoverNode, 'mouseleave', function () {
                me.menuNode.style.display = 'none';
            });*/
            on(me.toggleNode, 'click', function () {
                me.set('expanded', !me.expanded);
            });
        },

        postCreate: function () {
            var me = this;
            me.render();
            me.inherited(arguments);
        }

    });
});