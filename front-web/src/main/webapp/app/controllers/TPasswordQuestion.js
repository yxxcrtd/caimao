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
	'app/views/account/TPasswordQuestionPanel',
	'app/views/account/TPasswordQuestionSuccessPanel',
	'app/ux/GenericPrompt',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domStyle, domClass, 
		domConstruct, mouse, focusUtil, cookie, TPasswordQuestionPanel, TPasswordQuestionSuccessPanel, Prompt) {
	
	var config = {};
	var topbarUsername = dom.byId('topusername'),
		logoutBtn = dom.byId('logout'),
		content;
	
	function initView() {
		/*Ajax.get(Global.baseUrl + '/user', {
		}).then(function(response) {
			if (response.success) {
			    var data = response.data,
			    	isTrust = (data.is_trust == '1');
			    if (isTrust) {
			    	content.domNode.style.display = 'block';
			    } else {
			    	var content0 = new Prompt({
			    		msg: '您的安全密码还没有设置，请先设置安全密码。',
			    		linkText: '点击设置安全密码',
			    		link: 'account/tradepwd/set.htm'
			    	});
			    	content0.placeAt('contentctn');
			    }
			} else {
			}
		});*/
		/*Ajax.get(Global.baseUrl + '/tradepwd', {
		}).then(function(response) {
			if (response.success) {
				if(response.data.userTradePwdStrength){
					content.domNode.style.display = 'block';
				}else{
					var content0 = new Prompt({
			    		msg: '您的安全密码还没有设置，请先设置安全密码。',
			    		linkText: '点击设置安全密码',
			    		link: 'account/tradepwd/set.htm'
			    	});
			    	content0.placeAt('contentctn');
				}
			} else {
			}
		});*/
		content = new TPasswordQuestionPanel({
			onConfirm: function() {
				if (content.isValid()) {
					Ajax.post(Global.baseUrl + '/user/pwdquestion/set', content.getData()).then(function(response) {
						if (response.success) {
							content.destroy();
							content = new TPasswordQuestionSuccessPanel({}, 'contentctn');
						} else {
							content.showError(response.msg);
						}
					});
				}
			},
			style: {
				display: 'none'
			}
		});
		content.domNode.style.display = 'block';
		content.placeAt('contentctn');
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