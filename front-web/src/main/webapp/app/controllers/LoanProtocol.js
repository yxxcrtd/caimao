define([
    'app/controllers/Helper',
    'dojo/_base/lang',
	'app/common/Fx', 
	'dijit/registry',
	'dojo/dom',
	'dojo/on',
	'app/common/Ajax',
	'dojo/query',
	'app/common/Global',
	'dojo/dom-class',
	'dojo/cookie',
	'dojo/dom-attr',
	'app/views/financing/LoanDXBProtocolPanel',
	'app/views/financing/LoanProtocolPanel',
	'dojo/domReady!'
], function(Helper, lang, Fx, registry, dom, on, Ajax, query, Global, 
		domClass, cookie, domAttr, LoanDXBProtocolPanel, LoanProtocolPanel) {
	
	var config = {};
	
	function initView() {
		// show whether normal or DXB protocol
		Ajax.post(Global.baseUrl + '/financing/contract/detail', {
		}).then(function(response) {
			if (response.success) {
				var data = response.data,
				protocolPanel;
				if (data.contract) {
					if (data.contract.prod_id == '1') {
						protocolPanel = new LoanProtocolPanel();
					} else if (data.contract.prod_id == '2') {
						protocolPanel = new LoanDXBProtocolPanel();
					}
					protocolPanel.placeAt(document.body);
					var dataExtra = {};
					dataExtra.contract_start_year = data.contract.contract_begin_date.slice(0, 4);
					dataExtra.contract_start_month = data.contract.contract_begin_date.slice(5, 7);
					dataExtra.contract_start_day = data.contract.contract_begin_date.slice(8, 10);
					dataExtra.interest_month_rate = Global.formatNumber(data.contract.interest_day_rate *3000, 3) + '';
					dataExtra.idCard = data.idCard;
					Fx.fillAllWords(query('.contract-fillin'), lang.mixin(dataExtra, data.contract));
				} else if (data.loanApply) {
					if (data.loanApply.prod_id == '1') {
						protocolPanel = new LoanProtocolPanel();
					} else if (data.loanApply.prod_id == '2') {
						protocolPanel = new LoanDXBProtocolPanel();
					}
					protocolPanel.placeAt(document.body);
				}
			} else {
			}
		});
	}
	
	function addListeners() {
		
	}
	
	return {
		init: function() {
			initView();
			Helper.init(config);
			addListeners();
		}
	}
});