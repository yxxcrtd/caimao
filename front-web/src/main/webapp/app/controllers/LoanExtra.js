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
	'dojo/date',
	'dojo/date/stamp',
	'app/ux/GenericPrompt',
	'app/views/financing/LoanExtraInfoPanel',
	'app/views/financing/LoanExtraConfirmPanel',
	'app/views/financing/LoanExtraSuccessPanel',
	'app/views/financing/ContractSimplePanel',
	'dojo/_base/lang',
	'app/common/RSA',
	'dojo/json',
	'dojo/text!app/app.json',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, cookie, 
		date, stamp, Prompt, LoanExtraInfoPanel, LoanExtraConfirmPanel, LoanExtraSuccessPanel, ContractSimplePanel, lang, RSA, JSON, App) {
	
	var config = {};
	App = JSON.parse(App);
	var navBarItems = query('li', 'navbar'),
		topbarUsername = dom.byId('topusername'),
		logoutBtn = dom.byId('logout'),
		content0, content1, content2, content3,
		views = [],
		data = [],
		key,
		contractNo,
		maxLoanAmount,
		contractPanel;
	
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
	
	function getData() {
		return RSA.encrypt(key, lang.mixin(content1.getData(), content2.getData()), 'tradePwd'); 
	}
	
	function initView() {
		// request data by ajax, render data
		domClass.add(navBarItems[0], 'ui-step-active');
		
		
		contractNo = getContractNo();
		//调追加前提
		Ajax.post(Global.baseUrl + '/financing/add/premise', {
			contract_no: contractNo
		}).then(function(response) {
		    if (response.success) {
		    	// render first, show data then
		    	contractPanel.domNode.style.display = 'block';
				content1.domNode.style.display = 'block';
		    	Ajax.post(Global.baseUrl + '/financing/add/data', {
		    		contract_no: contractNo
				}).then(function(response) {
					if (response.success) {
						contractPanel.setValues(response.data.contract);
						content1.set('list', response.data.loanRatioList);
						content1.set('availableAmount', response.data.deposit);
						content1.set('maxLoanAmount', response.data.maxLoan);
						content1.set('proMaxLoanAmount', response.data.productMaxLoan);
						content2.set('proSettleMode', response.data.productSettleMode);
						content2.set('proTerm', response.data.productTerm);
					} else {
						
					}
				});
		    } else {
				content0 = new Prompt({
					msg: response.exceptions[0]['info']
				});
				content0.placeAt('contentctn');
			}
		});
				
		
		
		contractPanel = new ContractSimplePanel();
		contractPanel.domNode.style.display = 'none';
		contractPanel.placeAt('contentctn');
		
		content1 = new LoanExtraInfoPanel({
			next: function() {
				if(content1.isValid()) {
					contractPanel.domNode.style.display = 'none';
					next(content1);
					
				}
			},
			ratioUrl: App.ratioUrl
		});

		content1.domNode.style.display = 'none';
		content1.placeAt('contentctn');
		
		content2 = new LoanExtraConfirmPanel({
			prev: function() {
				contractPanel.domNode.style.display = 'block';
				prev(content2);
			},
			next: function() {
				if(content2.isValid()) {
					content2.nextBtn.loadingDisabled(true);
					Ajax.post(Global.baseUrl + '/financing/add/apply', RSA.encrypt(key, {
						'contract_no':contractNo,
						'add_amount':content1.addAmountFld.getAmount(),
						'trade_pwd':content2.tradePwdFld.get('value')
					}, 'trade_pwd')
							).then(function(response) {
						content2.nextBtn.loadingDisabled(false);
						if (response.success) {
							next(content2);
						} else {
							content2.showError(response.exceptions[0]['info']);
						}
						
					});
				}
			}
		});
		content2.domNode.style.display = 'none';
		content2.placeAt('contentctn');
		
		content3 = new LoanExtraSuccessPanel({
			
		});
		content3.domNode.style.display = 'none';
		content3.placeAt('contentctn');
		views.push(content1);
		views.push(content2);
		views.push(content3);
		
		Global.focusText();
	}
	
	function addListeners() {

		on(dom.byId('protocoltoggle'), 'click', function() {
			var content = dom.byId('protocolcontent');
			if (content.style.display == 'none') {
				content.style.display = 'block';
			} else {
				content.style.display = 'none';
			}
		});
		
		on(content1.protocolcheck, 'click', function(e) {
			content1.nextBtn.set('disabled', !this.checked);
		});
		
		on(content1.addAmountFld, 'keyUp', function(e) {
			if (isNaN(parseFloat(this.get('value')))) {
				content1.setValues({
					'loan_ratio': '',
					'warningLine': '',
					'interest_day_rate': '',
					'exposure_ratio': '',
					'total_loanAmount': ''
				});
				return;
			}
			var value = parseFloat(Global.parseAmount(this.get('value'))),
				percentList,
				percent,
				list,
				min, ratio, ratioItem,
				i = 0, len;
			if (content1.get('list')) {
				percentList = content1.get('percentList');
				list = content1.get('list');
				len = percentList.length;
				percent = (value + contractPanel.get('loan_amount') - contractPanel.get('repay_amount')) / content1.get('availableAmount');
				var middleList = [].concat(percentList);
				middleList.push(percent);
				middleList.sort(function asc(x,y)
			    	{
					x = parseFloat(x);
					y = parseFloat(y);
					if (x > y)  
			            return 1;
			        if (x < y)          
			        	return -1;
			    });
				var index = middleList.indexOf(percent);
				ratio = middleList[index + 1] || middleList[index - 1];
				for (var i = 0, len = list.length; i < len; i++) {
					if (list[i]['loan_ratio'] == ratio) {
						ratioItem = list[i];
						break;
					}
				}
				content1.setValues({
					'loan_ratio': ratioItem.loan_ratio,
					'warningLine': (1 + ratioItem.enable_ratio) * content1.get('availableAmount') + contractPanel.get('loan_amount') - contractPanel.get('repay_amount') + value,
					'interest_day_rate': ratioItem.interest_day_rate,
					//平仓线
					'exposure_ratio':(1 + ratioItem.exposure_ratio) * content1.get('availableAmount') + contractPanel.get('loan_amount') - contractPanel.get('repay_amount') + value,
					//total loanAmount
					'total_loanAmount':Global.parseAmount(content1.addAmountFld.get('value')) + parseFloat(contractPanel.get('loan_amount'))
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
				//TODO
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