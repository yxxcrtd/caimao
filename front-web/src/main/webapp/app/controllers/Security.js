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
	'app/views/account/SafetyInformationPanel',
	'app/views/common/SideMenu',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, 
		domConstruct, mouse, focusUtil, cookie, SafetyInformationPanel,SideMenu) {
	
	var config = {name: 'Security'};
	var navBarItems = query('.subnav-item', 'subheader'),
		sideBarItems = query('.list-lside-item', 'sidebar'),
		topbarUsername = dom.byId('topusername'),
		logoutBtn = dom.byId('logout'),
		content;
	
	function initView() {
	     sideMenu = new SideMenu({
	            active: '3 6'
	        });
        sideMenu.placeAt('sidemenuctn');
		Ajax.get(Global.baseUrl + '/user', {
		}).then(function(response) {
			if (response.success) {
				content.setValues(response.data);
			} else {
				//TODO
			}
		});
		
		Ajax.get(Global.baseUrl + '/tradepwd', {
		}).then(function(response) {
			if (response.success) {
				content.setValues(response.data);
			} else {
			}
		});
		
		Ajax.get(Global.baseUrl + '/userpwdquestion', {
		}).then(function(response) {
			if (response.success) {
				content.set('tradePwdQuestion',response.data.items);
			} else {
			}
		});
		
		content = new SafetyInformationPanel();
		content.placeAt('titlectn', 'after');
		
		// after
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