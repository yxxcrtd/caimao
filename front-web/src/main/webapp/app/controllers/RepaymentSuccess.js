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
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, cookie) {
	
	var config = {};
	var topbarUsername = dom.byId('topusername'),
		logoutBtn = dom.byId('logout');
	
	function initView() {
		// request data by ajax, render data
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