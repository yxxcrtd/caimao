
	define([
	    'dojo/dom',
	    'dojo/dom-style',
	    'dojo/dom-class',
	    'dojo/dom-construct',
	    'dojo/on',
		'dojo/query',
		'app/common/Ajax',
		'app/common/Global',
		'app/controllers/Helper',
		'dojo/domReady!'
	], function(dom, domStyle, domClass, domConstruct, on, query, Ajax, Global, Helper) {
		
		function initView() {
			// request data by ajax, render data
		}
		
		return {
			init: function() {
				initView();
				Helper.init();
			}
		}
	});