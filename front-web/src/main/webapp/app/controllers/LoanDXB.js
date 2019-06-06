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
	'app/views/financing/LoanSuccessPanel',
	'app/views/financing/LoanDXBInfoPanel',
	'app/views/financing/LoanDXBConfirmPanel',
	'dojo/_base/lang',
	'app/common/RSA',
	'dojo/json',
	'dojo/text!app/app.json',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, 
		domClass, cookie, date, stamp, Prompt, LoanSuccessPanel, 
		LoanDXBInfoPanel, LoanDXBConfirmPanel, lang, RSA, JSON, App) {
	
	var config = {};
	App = JSON.parse(App);
	var navBarItems = query('li', 'navbar'),
		topbarUsername = dom.byId('topusername'),
		logoutBtn = dom.byId('logout'),
		content0, content1, content2, content3,
		views = [],
		data = [],
		key;
	
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
		Global.focusText(query('input', views[index - 1].domNode)[0]);
	}
	
	function getData() {
		return RSA.encrypt(key, lang.mixin(content1.getData(), content2.getData()), 'trade_pwd'); 
	}
	
	function initView() {
		// request data by ajax, render data
		domClass.add(navBarItems[0], 'ui-step-active');
		
		
		Ajax.post(Global.baseUrl + '/financing/loan/premise', {
		}).then(function(response) {
			if (response.success) {
				Ajax.post(Global.baseUrl + '/financing/loan/data', {
					produce_id:'2'
				}).then(function(response) {
					if (response.success) {
						var data = response.data;
						content1.domNode.style.display = 'block';
						content1.setValues({
							availableAmount: data.deposit,
							list: data.loanRatioList,
							maxLoanAmount: data.maxLoan,
							deadDate: data.productTerm,
							productMaxLoan: data.productMaxLoan,
							productMinLoan: data.productMinLoan || 100, // TODO
							repayStart: data.productRepayStart,
							productSettleMode: data.productSettleMode
						});
					} else {
						
					}
				});
			} else {
				var info = response.exceptions[0].info;
				content0.placeAt('contentctn');
				content0.set('msg', info);
			}
			
		});
		
		content0 = new Prompt();
		
		content1 = new LoanDXBInfoPanel({
			style: {
				display: 'none'
			},
			next: function() {
				if (content1.isValid()) {
					next(content1);
				}
			},
			ratioUrl: App.ratioUrl
		});
		content2 = new LoanDXBConfirmPanel({
			style: {
				display: 'none'
			},
			next: function() {
				if(content2.isValid()) {
					content2.confirmBtn.loadingDisabled(true);
					Ajax.post(Global.baseUrl + '/financing/loan/apply', getData()).then(function(response) {
						content2.confirmBtn.loadingDisabled(false);
						if (response.success) {
							next(content2);
						} else {
							content2.showError(response.exceptions[0]['info']);
						}
						
					});
				}
			},
			prev: function() {
				prev(content2);
			}
		});
		content3 = new LoanSuccessPanel({
			style: {
				display: 'none'
			}
		});
		content1.placeAt('contentctn');
		content2.placeAt('contentctn');
		content3.placeAt('contentctn');
		views.push(content1);
		views.push(content2);
		views.push(content3);
		
		Global.focusText();
	}
	
	function addListeners() {
		on(content1.loanAmountFld, 'keyUp', function(e) {
			if (isNaN(parseFloat(this.get('value')))) {
				content1.setValues({
					'percentage': '',
					'warningLine': '',
					'rate': '',
					'exposure': ''
				});
				return;
			}
			var value = parseFloat(Global.parseAmount(this.get('value'))),
				percentList,
				availableAmount,
				percent,
				list,
				min, ratio, ratioItem,
				i = 0, len;
			if (content1.get('list')) {
				percentList = content1.get('percentList');
				list = content1.get('list');
				len = percentList.length;
				availableAmount = content1.get('availableAmount');
				percent = 1 / (availableAmount / (value + availableAmount)) - 1;
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
				if (ratioItem) {
					content1.setValues({
						'percentage': ratioItem.loan_ratio,
						'warningLine': (1 + ratioItem.enable_ratio) * availableAmount + value,
						'rate': ratioItem.interest_day_rate * value,
						'exposure': (1 + ratioItem.exposure_ratio) * availableAmount + value
					});
				}
			}
		});
		
		on(dom.byId('protocoltoggle'), 'click', function() {
			var content = dom.byId('protocolcontent');
			if (content.style.display == 'none') {
				content.style.display = 'block';
			} else {
				content.style.display = 'none';
			}
		});
		
		on(dom.byId('protocolcheck'), 'click', function(e) {
			content1.nextBtn.set('disabled', !this.checked);
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