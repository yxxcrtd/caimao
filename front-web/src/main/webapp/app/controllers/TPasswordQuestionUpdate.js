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
	'dojo/dom-construct',
	'dojo/mouse',
	'dijit/focus',
	'dojo/cookie',
	'app/common/RSA',
	'app/views/account/TPasswordQuestionUpdatePanel',
	'app/views/account/TPasswordQuestionSuccessPanel',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domStyle, domClass, 
		domConstruct, mouse, focusUtil, cookie,RSA, TPasswordQuestionUpdatePanel, TPasswordQuestionSuccessPanel) {
	
	var config = {};
	var topbarUsername = dom.byId('topusername'),
		logoutBtn = dom.byId('logout'),
		content,
		key;
	
	function initView() {
		
		
		content = new TPasswordQuestionUpdatePanel({
			onConfirm: function() {
				if (content.isValid()) {
					content.confirmBtn.loadingDisabled(true);
					Ajax.post(Global.baseUrl + '/user/pwdquestion/reset', RSA.encrypt(key, content.getData(), 'trade_pwd')).then(function(response) {
						content.confirmBtn.loadingDisabled(false);
						if (response.success) {
							content.destroy();
							content = new TPasswordQuestionSuccessPanel({});
							content.placeAt('contentctn');
						} else {
							content.showError(response.exceptions[0]['info']);
						}
					});
				}
			}
		});
		content.placeAt('contentctn');
		
		Global.focusText();
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
	function addListeners() {
		
	}
	
	return {
		init: function() {
			initView();
			initRSA();
			Helper.init(config);
			addListeners();
		}
	}
});