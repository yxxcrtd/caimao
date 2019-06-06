define([
    'app/controllers/Helper',
	'dojo/_base/fx', 
	'dijit/registry',
	'dojo/dom',
	'dojo/on',
	'app/common/Ajax',
	'dojo/query',
	'app/common/Global',
    'dojo/cookie',
    'dojo/dom-attr',
	'dojo/domReady!'
], function(Helper, fx, registry, dom, on, Ajax, query, Global, cookie, domAttr) {
	var config = {},
        a, userName;
	
	function initView() {
		var me = this;
	}
	
	return {
		init: function() {
			initView();
			Helper.init(config);
		}
	}
});