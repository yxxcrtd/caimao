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
	'app/ux/GenericGrid',
	'app/stores/GridStore',
	'app/views/financing/CheckPanel',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, cookie, Grid, Store, CheckPanel) {
	
	var config = {};
	var navBarItems = query('.subnav-item', 'subheader'),
		topbarUsername = dom.byId('topusername'),
		sideBarItems = query('.list-lside-item', 'sidebar'),
		logoutBtn = dom.byId('logout'),
		content;
	
	function initView() {
		// request data by ajax, render data
		domClass.add(navBarItems[2].childNodes[0], 'active');
		domClass.add(sideBarItems[1].childNodes[0], 'active');
		
		
		content = new CheckPanel();
		content.placeAt('contentctn');
		
		Ajax.post(Global.baseUrl + '/financing/loanapply/detail', {
		}).then(function(response) {
			if (response.success) {
				if (!response.data.loanApply) {
					content.setValues({
						list: []
					});
				} else {
					content.setValues({
						list: [response.data.loanApply]
					});
				}
			} else {
				//TODO
			}
		});
		
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