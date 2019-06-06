define([
    'app/controllers/Helper',
	'dojo/_base/fx', 
	'dijit/registry',
	'dojo/dom',
	'dojo/on',
	'app/common/Ajax',
	'dojo/query',
	'app/common/Global',
	'dojo/dom-style',
	'dojo/dom-class',
	'dojo/cookie',
	'dojo/dom-construct',
    'app/common/Data',
    'dojo/when',
	'app/views/account/BankcardInfoPanel',
	'app/views/account/BankcardConfirmPanel',
	'app/views/account/BankcardSuccessPanel',
	'app/views/account/BankcardFailurePanel',
	'app/views/account/BankCardPromptPanel',
    'app/ux/GenericPrompt',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, 
		query, Global, domStyle, domClass, cookie, domConstruct,Data,when,
		BankcardInfoPanel, BankcardConfirmPanel, BankcardSuccessPanel, BankcardFailurePanel, BankCardPromptPanel, Prompt) {
	
	var config = {};
	var content1, content2,
		titleEl = dom.byId('headname');
	
	function parseUrl() {
		var paramIndex = location.href.indexOf('?'),
			paramsObj = {};
		if (paramIndex > 0) {
			var params = location.href.slice(paramIndex + 1).split('&');
			for (var i = 0, len = params.length; i < len; i++) {
				var dict = params[i].split('=');
				paramsObj[dict[0]] = dict[1];
			}
		}
		return paramsObj;
	}
	
	function initView() {
		// request data by ajax, render data
		
		var urlParams = parseUrl();
		if (urlParams.edit == 1) { // change title
//			titleEl.innerHTML = '更换银行卡';
		}
		cookie('bank_card_no', null, {expires: -1, path: '/'}); // clear cookie
		
	  when(Data.getBanks(), function(banks) {
		  content1.bankListFld.set('banks', banks);
        });
	  when(Data.getUser(),function(user){
		  content1.set('user',user);
	  });
		content1 = new BankcardInfoPanel({
			onConfirm: function() {
				if(content1.isValid()) {
					//TODO
					content1.confirmBtn.loading(true);
					//var newTab = window.open('');
					Ajax.post(Global.baseUrl + '/user/bankcard/bind', content1.getData()).then(function(response) {
						content1.confirmBtn.loading(false);
						if (response.success) {
                            content1.hide();
                            var successPanel = new Prompt({
                                type: 'success',
                                msg: '恭喜您，银行卡设置成功！',
                                link: 'account/bankcard.htm',
                                linkText: '银行卡信息'
                            });
                            successPanel.placeAt('contentctn', 'first');
                            successPanel.show();
							// temp save
							cookie('bank_card_no', content1.bankcardNoFld.get('value').replace(/\s/g, ''), {path: '/'});
						} else {
							content1.showError(response.msg);
						}
						
					});
				}
			}
		});
		content1.domNode.style.display = 'block';
		content1.placeAt('contentctn');
		content1.provinceDefault();
		var bankProvince = document.getElementById("bank_province");
		if(bankProvince){
			bankProvince.addEventListener('change', function(){
				content1.changeProvince();
			});
		}
	}
	
	return {
		init: function() {
			initView();
			Helper.init(config);
		}
	}
});