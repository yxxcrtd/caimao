define([
    'app/controllers/Helper',
	'dojo/_base/fx', 
	'dijit/registry',
	'dojo/dom',
	'dojo/on',
	'app/common/Ajax',
	'dojo/query',
	'app/common/Global',
	'dojo/dom-class',
	'dojo/cookie',
	'dojo/dom-style',
	'app/views/financing/ContractBasePanel',
    'app/views/financing/ContractSidePanel',
	'app/views/financing/ContractDetailPanel',
	'app/views/financing/ContractExtendPanel',
	'app/views/financing/ContractExtraPanel',
	'app/views/financing/ContractRatePanel',
	'app/views/financing/ContractRepaymentPanel',
    'app/views/financing/RepaymentPanel',
	'app/common/Date',
	'app/ux/GenericGrid',
	'app/stores/GridStore',
    'app/common/Data',
    'dojo/when',
    'dojo/promise/all',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, cookie, domStyle,
		ContractBasePanel, ContractSidePanel, ContractDetailPanel, ContractExtendPanel, ContractExtraPanel,
        ContractRatePanel, ContractRepaymentPanel, RepaymentPanel, DateUtil, Grid, Store, Data, when, all) {
	
	var config = {};
	var basePanel,
        sidePanel,
		detailPanel,
		extendPanel,
		extraPanel,
		repaymentPanel,
		ratePanel,
		repayGrid, repayGridCfg = {}, repayGridLayout, repayStore,
		rateGrid, rateGridCfg = {}, rateGridLayout, rateStore,
        urlParams = Global.getUrlParam(),
        homsFundAccount = urlParams.fundaccount,
        homsCombineId = urlParams.combine,
        contractNo = urlParams.contract || urlParams.hiscontract;

	function initView() {
        basePanel = new ContractBasePanel();
        basePanel.placeAt('contractctn');
        sidePanel = new ContractSidePanel();
        sidePanel.placeAt('contractsidectn');



        on(sidePanel.repayBtn, 'click', function(e) {
            var repaymentPanel = new RepaymentPanel({
                width: 500
            });
            when(Data.getRepayData(contractNo, true),function(repayData){
                repaymentPanel.set('contractId',repayData.contract.contractNo);
                repaymentPanel.set('loanAmount',repayData.contract.loanAmount);
                repaymentPanel.set('availableBalance',repayData.availableAmount);
                //还款罚息金额
                Ajax.post(Global.baseUrl + "/financing/repayinterest",{
                    'repay_amount':repayData.availableAmount,
                    'contract_no':repayData.contract.contractNo
                }).then(function(response){
                    if(response.success){
                    	//鸿禧没有罚息概念，先注释掉
//                        var punitive = response.data.penaltyAmount;
//                        repaymentPanel.set('punitive',punitive);
                        //合计 = 借款金额 + 罚息
                        repaymentPanel.set('totalAmount',repayData.contract.loanAmount /*+ punitive*/);
                    }else{
                        //
                    }
                });
            });

            repaymentPanel.placeAt(document.body);
            repaymentPanel.show();
        });

        var promise1 = when(Data.getContract(contractNo), function(contract) {
            when(Data.getProduct({product_id: contract.prodId}), function(product) {
                if (product.prodDeferedMode == '1') {
                    extendPanel = new ContractExtendPanel({
                        contractNo: contractNo
                    });
                    extendPanel.placeAt('contractctn');
                    extendPanel.grid.startup();
                }
                sidePanel.set('product', product);
                if(product.prodTypeId != 3 && product.prodTypeId != 0){
                	ratePanel = new ContractRatePanel({
                		contractNo: contractNo
                	});
                	ratePanel.placeAt('contractctn');
                	ratePanel.grid.startup();
                	
                }
            });
            basePanel.set('contract', contract);
            sidePanel.set('contract', contract);
            return contract;
        });
        var promise2 = when(Data.getHomsAsset(homsFundAccount, homsCombineId), function(asset) {
            if (asset) {
                asset.homsCombineId = homsCombineId;
                asset.homsFundAccount = homsFundAccount;
                sidePanel.set('homs', asset);
            }
            return asset;
        });

        all([promise1, promise2]).then(function(result) {
            var contract = result[0],
                homs = result[1];
            if (contract.contractType == '0') { // main contract
                when(Data.getContractsByHoms({homs_fund_account: homs.homsFundAccount, homs_combine_id: homs.homsCombineId}), function(list) {
                    var items = list.items || [],
                        i = 0, len = items.length,
                        item,
                        childIds = [];
                    for (; i<len; i++) {
                        item = items[i];
                        if (item.contractType == '1') {
                            childIds.push(item.contractNo);
                        }
                    }
                    basePanel.set('homs', homs);
                    basePanel.set('childs', childIds);
                });
            }
        });
	}
	
	return {
		init: function() {
			initView();
			Helper.init(config);
		}
	}
});