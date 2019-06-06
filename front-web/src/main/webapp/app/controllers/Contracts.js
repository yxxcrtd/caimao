/**
 * show history contract
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
    'app/views/common/PzSideMenu',
    'app/common/Data',
    'dojo/when',
    'dojo/promise/all',
    'app/views/financing/RepaymentPanel',
    'app/ux/GenericTooltip',
    'app/views/financing/ContractInfoPanel',
    'app/common/Dict',
    'dojo/request/notify',
    'app/jquery/Pagination',
	'dojo/domReady!'
], function(Helper, dom, on,
		Ajax, query, domClass, domConstruct, domStyle, RSA, 
		Global, cookie, SideMenu, Data, when, all,
        RepaymentPanel,Tooltip, ContractInfoPanel, Dict, notify,Pagination){
	var config = {},
	queryParams = {
            start: 0,
            limit: 10
     };
	
	function setQuery(params, pageNumber, isFirst) {
		    destoryContractList();
	        if (isFirst) {
	            params.start = 0;
	        }
	        setContractList(params, pageNumber, isFirst);
	}
	
	function setContractList(params, pageNumber, isFirst) {
        when(Data.getCurContracts(params, true), function (data) {
            if (isFirst) {
                $('#light-pagination').pagination({
                    items: data.totalCount,
                    itemsOnPage: 10,
                    prevText: '上一页',
                    nextText: '下一页',
                    onPageClick: function (pageNumber, e) {
                        queryParams.start = (pageNumber - 1) * queryParams.limit;
                        setQuery(queryParams, pageNumber);
                    }
                });
            }
            setList(data);
        });
    }
    
	function destoryContractList(){
		var items = $('#financingctn div');//获取id为financingctn的下的所有div
        if (items.length > 1) {
            for (var i = 1; i < items.length; i++) {//将除了第一行的所有数据先删除，再赋值
               domConstruct.destroy(items[i]);
            }
        }
	}
	
    function initView() {

        var sideMenu = new SideMenu({
        	active: '2 2'
        });
        sideMenu.placeAt('sidemenuctn');
        setQuery(queryParams,1,true);
    }
    
    function setList(data){
     var contracts=data.items || [];
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
                var contractNo = this.contractNo;
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