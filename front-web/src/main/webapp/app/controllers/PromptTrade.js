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
	'dojo/dom-attr',
	'app/views/home/TradePremisePanel',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, cookie, domAttr, TradePremisePanel) {
	
	var config = {};
	var topbarUsername = dom.byId('topusername'),
		logoutBtn = dom.byId('logout');
	
	function initView() {
		// request data by ajax, render data
		
		
		var flagEl,
			flagStr,
			flagObj, content;
		
		flagEl = document.getElementById('flag');
		flagStr = domAttr.get(flagEl, 'data-value');
		flagObj = JSON.parse(flagStr);
		
		content = new TradePremisePanel();
		content.setValues(flagObj);
		content.placeAt('contentctn');
		
		Global.focusText();
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