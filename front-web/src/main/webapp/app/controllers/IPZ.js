/**
 * Controller for login, handler with all business event
 * */
define([
    'app/controllers/Helper',
	'dojo/dom',
	'dojo/on',
	'app/common/Ajax',
	'dojo/query',
	'dojo/dom-class',
	'dojo/dom-construct',
	'dojo/dom-style',
	'app/common/RSA',
	'app/common/Global',
	'dojo/cookie',
    'app/views/common/SideMenu',
    'app/common/Data',
    'dojo/when',
    'dojo/promise/all',
    'app/views/financing/RepaymentPanel',
    'app/ux/GenericTooltip',
    'app/views/financing/ContractInfoPanel',
    'app/common/Dict',
    'dojo/request/notify',
	'dojo/domReady!'
], function(Helper, dom, on,
		Ajax, query, domClass, domConstruct, domStyle, RSA, 
		Global, cookie, SideMenu, Data, when, all,
        RepaymentPanel,Tooltip, ContractInfoPanel, Dict, notify){
	
	var config = {};
    function initView() {

        var sideMenu = new SideMenu({
        	active: '2 3'
        });
        sideMenu.placeAt('sidemenuctn');
        Ajax.get(Global.baseUrl + '/financing/contract/page', {
        	'product_type':"2",
        	'start':0,
        	'limit':10000/*,
        	'start_date':"20141111",
        	'end_date':"20150330"*/
        }).then(function (response) {
            if (response.success) {
            	var contracts=response.data.items || [];
            	 for (var j=0; j < contracts.length; j++) {
                     var CP = new ContractInfoPanel();
                     CP.placeAt('financingctn');
                     CP.set('homs', contracts[j]);
                     CP.set('contract', contracts[j]);
                     (function(panel, prodId) {
                         when(Data.getProduct({product_id: prodId}), function(data) {
                             panel.set('product', data);
                         });
                     })(CP, contracts[j].prodId);
                    // FP.set('title', Dict.get('productID')[contracts[j].prodId]);
                     on(CP.repayLink, 'click', function(e) {
                         var contractNo = CP.contract.contractNo;
                         var repaymentPanel = new RepaymentPanel({
                             width: 500
                         });
                         when(Data.getRepayData(contractNo),function(repayData){
                             repaymentPanel.set('contractId',repayData.contract.contractNo);
                             repaymentPanel.set('loanAmount',repayData.contract.loanAmount);
                             repaymentPanel.set('availableBalance',repayData.availableAmount);
                            //还款罚息金额
                             Ajax.post(Global.baseUrl + "/financing/repayinterest",{
                             	'repay_amount':repayData.availableAmount,
                             	'contract_no':repayData.contract.contractNo
                             }).then(function(response){
                             	if(response.success){
                             		var punitive = response.data.penaltyAmount;
                             		repaymentPanel.set('punitive',punitive);
                                     //合计 = 借款金额 + 罚息
                                     repaymentPanel.set('totalAmount',repayData.contract.loanAmount + punitive);
                             	}else{
                             		//
                             	}
                             });
                         });
                         repaymentPanel.placeAt(document.body);
                         repaymentPanel.show();
                     });
                 }
            } else {
                //TODO
            }

        });

    }

	return {
		init: function() {
			(function () {
                Ajax.get(Global.baseUrl + '/sec/rsa', {
                }).then(function (response) {
                    if (response.success) {
                        modulus = response.data.modulus;
                        exponent = response.data.exponent;
                    } else {
                        //TODO
                    }

                });
            })();
			initView();
			Helper.init(config);
		}
	}
});