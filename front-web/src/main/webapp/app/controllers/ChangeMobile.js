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
	
	'app/views/member/ChangeMobilePanel',
	'app/views/member/ChangeMobileSuccessPanel',
	'app/common/RSA',
	'app/ux/GenericTooltip',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, 
		domConstruct, mouse, focusUtil, cookie, ChangeMobilePanel,ChangeMobileSuccessPanel, RSA, Tooltip) {
	
	var config = {};
	var topbarUsername = dom.byId('topusername'),
		logoutBtn = dom.byId('logout'),
		content,
		key;
	
	function initView() {
		
		
		Ajax.get(Global.baseUrl + '/user', {
		}).then(function(response) {
			if (response.success) {
				content.setValues({
					oldMobile: response.data.mobile
				});
			} else {
				//TODO fail is ...
				
			}
			
		});
		
		content = new ChangeMobilePanel();
		content.codeFld.sendBtn.setDisabled(true);
		content.commit.onClick = function() {
			if (content.isValid()) {
				content.commit.loading(true);
				Ajax.post(Global.baseUrl + '/user/mobile/reset', RSA.encrypt(key, content.getData(), 'trade_pwd')).then(function(response) {
					content.commit.loading(false);
					if (response.success) {
						var newMobile = content.newMobileFld.get('value');
						content.destroy();
						content = new ChangeMobileSuccessPanel({
							newMobile :newMobile
						});
						content.placeAt('contentctn');
					} else {
						content.showError(response.msg);
					}
				});
			};
			}
		content.placeAt('contentctn');
	}
	
	function addListeners() {
		//send smscode event
		on(content.codeFld.sendBtn, 'click', function() {
					Ajax.post(Global.baseUrl + '/sms/changemobile', {
						mobile: content.newMobileFld.get('value')
					}).then(function(response) {
						if (response.success) {
							content.codeFld.countdown(60);
						} else {
							Tooltip.show(response.msg, content.codeFld.sendBtn.domNode);
						}
					});
		});
		
		// check the unique number of phone when blur
		on(content.newMobileFld, 'blur', function() {
			if (content.newMobileFld.checkValidity()) {
				content.commit.loading(true);
				Ajax.post(Global.baseUrl + '/user/mobile/check', {
					mobile:content.newMobileFld.get('value')
				}).then(function(response) {
					content.commit.loading(false);
					if (response.success) {
						content.codeFld.sendBtn.setDisabled(false);
						content.commit.setDisabled(false);
						domClass.remove(content.newMobileFld.domNode, 'dijitTextBoxError');
					} else {
						content.newMobileFld.displayMessage(response.msg, 'error');
						content.codeFld.sendBtn.setDisabled(true);
						content.commit.setDisabled(true);
					}
				});
			}else{
				content.codeFld.sendBtn.setDisabled(true);
			}
		});
	}
	
	function initRSA() {
		Ajax.get(Global.baseUrl + '/sec/rsa', {
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