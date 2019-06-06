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
	'app/views/account/BankcardToBindPanel',
	'app/views/account/BankcardBindedPanel',
	'app/views/common/SideMenu',
    'app/common/User',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, 
		domConstruct, mouse, focusUtil, cookie, BankcardToBindPanel, BankcardBindedPanel,SideMenu, User) {
	
	var config = {};
	var navBarItems = query('.subnav-item', 'subheader'),
		sideBarItems = query('.list-lside-item', 'sidebar'),
		topbarUsername = dom.byId('topusername'),
		logoutBtn = dom.byId('logout'),
		content,sideMenu;
	
	function initView() {
		   sideMenu = new SideMenu({
	            active: '3 4'
	        });
		   sideMenu.placeAt('sidemenuctn');
        User.getBindBankcard(function(bankcard) {
            if (!bankcard) {
                content = new BankcardToBindPanel();
            } else {
                content = new BankcardBindedPanel();
            }
            content.placeAt('titlectn', 'after');
            content.set('bankcard', bankcard);
        });
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