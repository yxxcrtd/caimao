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
	'app/common/RSA',
	'app/views/account/FindTradePwdStep2Panel',
	'app/views/account/SetTradePwdPanel',
	'app/ux/GenericPrompt',
	'app/ux/GenericTooltip',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, 
		domConstruct, mouse, focusUtil, cookie, 
		RSA, FindTradePwdStep2Panel, SetTradePwdPanel, Prompt, Tooltip) {
	
	var config = {};
	var topbarUsername = dom.byId('topusername'), 
		logoutBtn = dom.byId('logout'),
		content1, content2, content3,
		key;
	function initView() {
		
		
		Ajax.post(Global.baseUrl + '/user/detail', {
		}).then(function(response) {
			if (response.success) {
				content1.setValues(response.data);
			} else {
				//TODO fail is ...
				
			}
			
		});
		
		content1 = new FindTradePwdStep2Panel({
			next: function() {
				content1.domNode.style.display = 'none';
				content2.domNode.style.display = 'block';
				
			} 
		});
		//content1.domNode.style.display = 'none';
		content1.placeAt('contentctn');
		
		content2 = new SetTradePwdPanel({
			next:function(){
				content2.domNode.style.display = 'none';
				content3.domNode.style.display = 'block';
			}
		});
		content2.domNode.style.display = 'none';
		content2.placeAt('contentctn');
		
		content3 = new Prompt({
			msg: '恭喜您，安全密码已成功找回！',
			type: 'success'
		});
		content3.domNode.style.display = 'none';
		content3.placeAt('contentctn');
		
		
		Global.focusText();
	}
	
	function addListeners() {
		
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