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
	'app/views/member/ChangeMailSuccessPanel',
	'app/views/member/ChangeMailPanel',
	'app/common/RSA',
	'app/ux/GenericTooltip',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, 
		domConstruct, mouse, focusUtil, cookie, ChangeMailSuccessPanel, ChangeMailPanel, RSA, Tooltip) {
	
	var config = {};
	var topbarUsername = dom.byId('topusername'),
		logoutBtn = dom.byId('logout'),
		title = dom.byId('headname'),
		content,
		key;
	
	function initView() {
		
		
		Ajax.get(Global.baseUrl + '/user', {
		}).then(function(response) {
			if (response.success) {
				content.setValues({
					oldMail: response.data.email
				});
			} else {
				//TODO fail is ...
				
			}
			
		});
		
		content = new ChangeMailPanel();
		content.commit.onClick = function() {
		if (content.isValid()) {
			content.commit.loading(true);
			Ajax.post(Global.baseUrl + '/user/email/change', RSA.encrypt(key, content.getData(), 'tradePwd')).then(function(response) {
				content.commit.loading(false);	
				if (response.success) {
						var newMail = content.newMailFld.get('value');
						var oldMail = content.get('oldMail');
						content.destroy();
						
						content = new ChangeMailSuccessPanel({
							newMail: newMail,
							oldMail: oldMail
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