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
	'app/views/account/UserInfoPanel',
	'dojox/mvc/equals',
	'app/views/common/SideMenu',
    'dojo/when',
    'app/common/Data',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, 
		domConstruct, mouse, focusUtil, cookie, UserInfoPanel, equals,SideMenu,when,Data) {
	
	var config = {};
	var navBarItems = query('.subnav-item', 'subheader'),
		sideBarItems = query('.list-lside-item', 'sidebar'),
		topbarUsername = dom.byId('topusername'),
		logoutBtn = dom.byId('logout'),
		userInfoPanel;
	
	function initView() {
		 var sideMenu = new SideMenu({
	            active: '3 5'
	        });
		 sideMenu.placeAt('sidemenuctn');
		 when(Data.getUser(),function(user){
			 userInfoPanel.set("username",user.userName);
			 userInfoPanel.set("address",user.userAddress);
			 userInfoPanel.set("usernameReal",user.userRealName);
			 userInfoPanel.set("mobile",user.mobile);
			 userInfoPanel.set("email",user.email);
		 });
		 //查询会员扩展信息
		 Ajax.get(Global.baseUrl + '/userextra').then(function(response){
			 if(response.data){
				 userInfoPanel.detailPanel.set('data', response.data);
			 }else{
				 alert("个人资料显示失败");
			 }
			
		 });
		
		 userInfoPanel = new UserInfoPanel({
			saveDetail: function() {
                if (userInfoPanel.detailEditPanel.isValid()) {
                    var oldValues = userInfoPanel.detailPanel.getValues(),
                        newValues = userInfoPanel.detailEditPanel.getValues();
                    var isEdit = !equals.equalsObject(oldValues, newValues);
                    var data = userInfoPanel.detailEditPanel.getData();
                    if (isEdit) {
                        Ajax.post(Global.baseUrl + '/user/enrich', data).then(function(response) {
                            if (response.success) {
                                userInfoPanel.afterSave(true);
                                if (data.userNickName) {
                                    topbarUsername.innerHTML = data.userNickName;
                                }
                            } else {
                                //TODO
                            }
                        });
                    } else {
                        userInfoPanel.switchPanel();
                    }
                }

			}
		});
		 userInfoPanel.placeAt('titlectn', 'after');

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