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
	'app/views/member/ChangeLoginPwdPanel',
	'app/views/member/ChangeLoginPwdSuccessPanel',
	'app/common/RSA',
	'app/ux/GenericTooltip',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, 
		domConstruct, mouse, focusUtil, cookie, ChangeLoginPwdPanel,ChangeLoginPwdSuccessPanel, RSA, Tooltip) {
	
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
					userLoginName: response.data.userName?response.data.userName:response.data.mobile//userName,mobile
				});
			} else {
				//TODO fail is ...
				
			}
			
		});
		
		content = new ChangeLoginPwdPanel();
		content.commit.onClick = function() {
	    if (content.isValid()) {
	      content.commit.loading(true);
		  Ajax.post(Global.baseUrl + '/user/loginpwd/reset', RSA.encrypt(key, content.getData(), ['oldPwd', 'newPwd'])).then(function(response) {
			 content.commit.loading(false);
			  if (response.success) {
					content.destroy();
					content = new ChangeLoginPwdSuccessPanel({
					});
					content.placeAt('contentctn');
				} else {
					content.showError(response.msg);
				}
				
			});
		};
	 }
		content.placeAt('contentctn');

		
		Global.focusText();
	}
	
	function addListeners() {
		
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