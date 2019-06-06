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
	'dojo/dom-construct',
	'dojo/mouse',
	'dijit/focus',
	'dojo/cookie',
	'app/views/financing/RepaymentPaybackPanel',
	'app/views/financing/RepaymentConfirmPanel',
	'app/views/financing/RepaymentSuccessPanel',
	'app/ux/GenericPrompt',
	'app/views/financing/ContractSimplePanel',
	'app/common/RSA',
	'app/ux/GenericTooltip',
	'app/common/Date',
	'dojo/_base/lang',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, 
		domConstruct, mouse, focusUtil, cookie, RepaymentPaybackPanel, RepaymentConfirmPanel, 
		RepaymentSuccessPanel, GenericPrompt, ContractSimplePanel, RSA, Tooltip, DateUtil, lang) {
	
	var config = {};
	var topbarUsername = dom.byId('topusername'),
		logoutBtn = dom.byId('logout'),
		content0, content1, content2, content3,
		contractPanel,
		navBarItems = query('li', 'navbar'),
		views = [],
		contractNo,
		productRepayMode,
		key,
		proId;
	
	function getContractNo() {
		var paramIndex = location.href.indexOf('?');
		if (paramIndex > 0) {
			var params = location.href.slice(paramIndex + 1).split('&'),
				paramsObj = {};
			for (var i = 0, len = params.length; i < len; i++) {
				var dict = params[i].split('=');
				paramsObj[dict[0]] = dict[1];
			}
			return paramsObj['contract'];
		}
	}
	
	function next(activity) {
		var index = views.indexOf(activity);
		views[index].domNode.style.display = 'none';
		views[index + 1].domNode.style.display = 'block';
		if (views[index].getValues) {
			views[index + 1].setValues(views[index].getValues());
		}
		domClass.remove(query('.ui-step-active', 'navbar')[0], 'ui-step-active');
		domClass.add(navBarItems[index + 1], 'ui-step-active');
		Global.focusText(query('input', views[index + 1].domNode)[0]);
	}
	
	function prev(activity) {
		activity.reset();
		var index = views.indexOf(activity);
		views[index].domNode.style.display = 'none';
		views[index - 1].domNode.style.display = 'block';
		domClass.remove(query('.ui-step-active', 'navbar')[0], 'ui-step-active');
		domClass.add(navBarItems[index - 1], 'ui-step-active');
	}
	
	function initView() {
		domClass.add(navBarItems[0], 'ui-step-active');
		
		contractNo = getContractNo();
		
		Ajax.post(Global.baseUrl + '/financing/repay/premise', {
			'contract_no': contractNo
		}).then(function(response) {
			if (response.success) {
				contractPanel.domNode.style.display = 'block';
				content1.domNode.style.display = 'block';
				Ajax.post(Global.baseUrl + '/financing/repay/data', {}).then(function(response) {
					if (response.success) {
						var contract = response.data.contract,
						    availableAmount = response.data.availableAmount;
						productRepayMode = response.data.productRepayMode;
						proId = contract.prod_id;
						if (proId == 2) { // duanxianbao
							content1.repayAmountFld.set('value', Global.formatAmount(contract.loan_amount));
							Ajax.post(Global.baseUrl + '/financing/repayinterest/calculate', {
								repay_amount: contract.loan_amount,
								days: DateUtil.difference(DateUtil.parse(contract.interest_begin_date), new Date()),
								day_rate: contract.interest_day_rate,
								repay_mode: productRepayMode
							}).then(function(response) {
								if (response.success) {
									content1.set('interestAmount', response.data);
									content1.nextBtn.set('disabled', false);
								}
							});
							content1.repayAmountFld.set('readonly', true);
							
							// disable today want to repay
							if (DateUtil.format(new Date()) == contract.contract_begin_date) {
								content1.nextBtn.set('disabled', true);
								content1.nextBtn.set('disabledMsg', '请您次日再来还款');
							}
						}
						contractPanel.setValues(contract);
						content1.setValues({availableAmount: availableAmount, loanAmount: contract.loan_amount});
					} else {
						
					}
				});
			} else {
				var info = response.exceptions[0].info;
				content0 = new GenericPrompt();
				content0.placeAt('contentctn');
				content0.set('msg',info);
			}	
		});
		
		contractPanel = new ContractSimplePanel();
		contractPanel.domNode.style.display = 'none';
		contractPanel.placeAt('contentctn');
		content1 = new RepaymentPaybackPanel({
			next: function() {
				if (content1.isValid()) {
					contractPanel.domNode.style.display = 'none';
					next(content1);
				}
			}
		});
		content1.domNode.style.display = 'none';
		content1.placeAt('contentctn');
		content2 = new RepaymentConfirmPanel({
			prev: function() {
				contractPanel.domNode.style.display = 'block';
				prev(content2);
			},
			next: function() {
				if (content2.isValid()) {
					content2.nextBtn.loadingDisabled(true);
					Ajax.post(Global.baseUrl + '/financing/repay/do', RSA.encrypt(key, lang.mixin(contractPanel.getData(), content1.getData(),content2.getData()), 'trade_pwd')).then(function(response) {
						content2.nextBtn.loadingDisabled(false);
						if (response.success) {
						next(content2);
					} else {
						Tooltip.show(response.exceptions[0]['info'], content2.nextBtn.domNode);
					}	
				});
				}
			}
		});
		content2.domNode.style.display = 'none';
		content2.placeAt('contentctn');
		content3 = new RepaymentSuccessPanel();
		content3.domNode.style.display = 'none';
		content3.placeAt('contentctn');
		views.push(content1);
		views.push(content2);
		views.push(content3);
		
		Global.focusText();
	}
	
	function addListeners() {
		on(content1.repayAmountFld, 'blur', function() {
			var vali = this.validates.pop();
			if (this.checkValidity()) {
				this.validates.push(vali);
				Ajax.post(Global.baseUrl + '/financing/repayinterest/calculate', {
					repay_amount: content1.repayAmountFld.getAmount(),
					contract_no: contractNo
				}).then(function(response) {
					if (response.success) {
						content1.set('interestAmount', response.data);
						content1.nextBtn.set('disabled', false);
					}
				});
			}
		});
	}
	
	function initRSA() {
		Ajax.post(Global.baseUrl + '/sec/rsa/get', {
		}).then(function(response) {
			if (response.success) {
				key = RSA.getKeyPair(response.data.exponent, '', response.data.modulus);
			} else {
			}
		});
	}
	
	return {
		init: function() {
			initView();
			Helper.init(config);
			addListeners();
			initRSA();
		}
	}
});