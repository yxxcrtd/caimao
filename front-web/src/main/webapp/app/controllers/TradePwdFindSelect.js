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
	'app/ux/GenericTooltip',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, 
		domConstruct, mouse, focusUtil, cookie,RSA,Tooltip) {
	
	var config = {};
	var topbarUsername = dom.byId('topusername'), 
		logoutBtn = dom.byId('logout'),
		content1, content2, content3,
		key;
	function initView() {
		
		
		Ajax.get(Global.baseUrl + '/user', {
		}).then(function(response) {
			if (response.success) {
				dom.byId('username_').innerHTML =  response.data.userName;
				dom.byId('mobile_1').innerHTML = Global.encodeInfo(response.data.mobile, 3, 3);
			} else {
				//TODO fail is ...
				
			}
			
		});
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