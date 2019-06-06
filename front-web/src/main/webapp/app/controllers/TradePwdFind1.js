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
	'app/views/account/FindTradePwdStep1Panel',
	'app/views/account/SetTradePwdPanel',
	'app/ux/GenericPrompt',
	'app/ux/GenericTooltip',
	'dojo/_base/lang',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, 
		domConstruct, mouse, focusUtil, cookie,RSA,FindTradePwdStep1Panel,SetTradePwdPanel,GenericPrompt,Tooltip,lang) {
	
	var config = {};
	var navBarItems = query('li', 'navbar'),
	    topbarUsername = dom.byId('topusername'), 
		logoutBtn = dom.byId('logout'),
		content0, content1, content2, content3,
		views = [],
		key;

	function prev(activity) {
		activity.reset();
		var index = views.indexOf(activity);
		views[index].domNode.style.display = 'none';
		views[index - 1].domNode.style.display = 'block';
		domClass.remove(query('.ui-step-active', 'navbar')[0], 'ui-step-active');
		domClass.add(navBarItems[index - 1], 'ui-step-active');
	}	
	
	function next(activity) {
		var index = views.indexOf(activity);
		views[index].domNode.style.display = 'none';
		views[index + 1].domNode.style.display = 'block';
		if (views[index].getValues) {
			views[index + 1].setValues(views[index].getValues());
		}
		domClass.remove(query('.ui-step-active', 'navbar')[0], 'ui-step-active');
		domClass.add(navBarItems[index + 1], 'ui-step-active');
		Global.focusText(query('input', views[index + 1].domNode)[0]);
	}
	
	function initView() {
		
		
		content0=new GenericPrompt();
		content0.domNode.style.display = 'none';
		
		Ajax.get(Global.baseUrl + '/userpwdquestion', {
		}).then(function(response) {
			if (response.success) {
				if(response.data.items){
					domClass.add(navBarItems[0], 'ui-step-active');
					content1.set('tradeQquestion',response.data.items);
					 Ajax.get(Global.baseUrl + '/user', {
							}).then(function(response) {
								if (response.success) {
								    content1.set('mobile',response.data.mobile);
								} else {
									//TODO fail is ...
								}
			          });
					 content1.domNode.style.display = 'block';
				}else{
					content0.domNode.style.display = 'block';
					content0.set('msg','请先设置密保问题，才能找回安全密码！');
				}
			} else {
				//TODO fail is ...
			}
		});
		
		content1 = new FindTradePwdStep1Panel({
			next: function() {
				if(content1.isValid()){
					content1.commit.loading(true);
						Ajax.post(Global.baseUrl + '/user/findtradepwd/check',content1.getData())
						.then(function(response){
							content1.commit.loading(false);
							if (response.success) {
								next(content1);
							}else{
								content1.showError(response.msg);
							}
						});
				}
			} 
		});
		
		content0.placeAt('contentctn');
		content1.domNode.style.display = 'none';
		content1.placeAt('contentctn');
		
		content2 = new SetTradePwdPanel({
			prev: function(){
				prev(content2);
			},
			next:function(){
				if(content2.isValid()){
					content2.commit.loading(true);
					Ajax.post(Global.baseUrl + '/user/tradepwd/find', RSA.encrypt(key, lang.mixin(content1.getData(),content2.getData()), 'trade_pwd'))
					.then(function(response){
						content2.commit.loading(false);
						if (response.success) {
							next(content2);
						}else{
							content2.showError(response.msg);
						}
					});
			 }
			}
		});
		content2.domNode.style.display = 'none';
		content2.placeAt('contentctn');
		
		content3 = new GenericPrompt({
			msg: '恭喜您，安全密码已成功找回！',
			type: 'success'
		});
		content3.domNode.style.display = 'none';
		content3.placeAt('contentctn');
		
		views.push(content1);
		views.push(content2);
		views.push(content3);
		views.push(content0);
		
		Global.focusText();
	}
	
	function addListeners() {
		//send smscode event
		on(content1.codeFld.sendBtn, 'click', function() {
				Ajax.post(Global.baseUrl + '/sms/code', {
					biz_type: 6
				}).then(function(response) {
					if (response.success) {
						content1.codeFld.countdown(60);
					} else {
						Tooltip.show(response.msg, content1.codeFld.sendBtn.domNode);
					}
				});
		});
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