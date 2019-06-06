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
	'app/views/home/UserStatusPanel',
    'app/views/common/SideMenu',
    'app/common/Data',
    'dojo/when',
    'dojo/promise/all',
    'app/views/home/InfoPanel',
    'app/views/common/ProductCute',
    'app/views/financing/RepaymentPanel',
    'app/ux/GenericTooltip',
    'app/views/financing/InfoPanel',
    'app/views/financing/ContractInfoPanel',
    'app/common/Dict',
    'dojo/request/notify',
    'dojo/_base/config',
	'dojo/domReady!'
], function(Helper, dom, on,
		Ajax, query, domClass, domConstruct, domStyle, RSA, 
		Global, cookie, UserStatusPanel, SideMenu, Data, when, all,
        InfoPanel,ProductCute,RepaymentPanel,Tooltip, FInfoPanel, ContractInfoPanel, Dict, notify, cfg){
	
	var config = {},
        productPanel = dom.byId('product-panel'), contractNum = 0,
        exponent, modulus;

    function initView() {

        when(Data.getHAC(), function(hac) {
            if (hac.length !== 0) {
                var i = 0, len = hac.length;
                for (; i < len; i++) {
                    var homsAccount = hac[i].HomsCombineId,
                        contracts = hac[i].contractList || [],
                        homsAssets = hac[i].HomsAssetsInfo;
                    if (contracts.length > 0) {
                        var FP = new FInfoPanel();
                        FP.placeAt('financingctn');
                        FP.set('data', homsAssets);
                    }
                    for (var j=0; j < contracts.length; j++) {
                        contractNum++;
                        var CP = new ContractInfoPanel();
                        CP.placeAt('financingctn');
                        CP.set('homs', homsAccount);
                        CP.set('contract', contracts[j]);
                        if (FP) {
                            FP.set('title', contracts[j].prodName);
                            FP.set('productType', contracts[j].prodTypeId);
                        }
                        (function(panel, prodId) {
                            when(Data.getProduct({product_id: prodId}), function(data) {
                                panel.set('product', data);
                            });
                        })(CP, contracts[j].prodId);
                        on(CP.repayLink, 'click', function(e) {
                            var contractNo = this.contractNo;
                            var repaymentPanel = new RepaymentPanel({
                                closeAction: 'close',
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
                }
                if (!contractNum) {
                    productPanel.style.display = 'block';
                }
            } else {
                productPanel.style.display = 'block';
            }
        });
    	
        when(Data.getAccount(), function(account) {
            infoPanel.setValues(account);
        });
        when(Data.getUser(), function(user) {
            infoPanel.setValues(user);
        });
        when(Data.getTradePwd(), function(data) {
            infoPanel.set('tradePwd', data.userTradePwdStrength);
        });
        when(Data.getBankcards(), function(data) {
            infoPanel.set('bankcard', data);
        });
        when(Data.getStatisticInfo(), function(statisticInfo) {
        	infoPanel.setValues(statisticInfo);
        });
        var sideMenu = new SideMenu({
            active: 1
        });
        sideMenu.placeAt('sidemenuctn');
        var infoPanel = new InfoPanel({}, 'info-panel');
        if (cfg.isProDay) {
            var product1 = new ProductCute({
                cls: 'icon-introduceIndex icon-introduceIndex01',
                info: cfg.proDay,
                style: {
                    'float': 'left'
                },
                url: Global.baseUrl + '/p1.htm'
            });
            product1.placeAt(productPanel);
        }
        if (cfg.isProMonth) {
            var product2 = new ProductCute({
                cls: 'icon-introduceIndex icon-introduceIndex02',
                info: cfg.proMonth,
                style: {
                    'float': 'left'
                },
                url: Global.baseUrl + '/p2.htm'
            });
            product2.placeAt(productPanel);
        }
        if (cfg.isProFree) {
            /*var product3 = new ProductCute({
                cls: 'icon-introduceIndex icon-introduceIndex03',
                info: cfg.proFree,
                style: {
                    'float': 'left'
                },
                url: Global.baseUrl + '/p3.htm'
            });
            product3.placeAt(productPanel);*/
        }
        if (cfg.isProContest) {
            /*var product4 = new ProductCute({
                cls: 'icon-introduceIndex icon-introduceIndex04',
                info: cfg.proContest,
                style: {
                    'float': 'left'
                },
                url: Global.baseUrl + '/p4.htm'
            });
            product4.placeAt(productPanel);*/
        }

		function shakeStatus() {
            var items = query('i:not(.d-user-status-true)', infoPanel.userStatusNode);
            Global.loop(items.length, function(count) {
                items.at(items.length - count).addClass('phone-calling');
            });

        }

        setTimeout(shakeStatus, 3000);
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