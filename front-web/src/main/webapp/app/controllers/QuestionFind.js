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
	'app/views/account/FindQuestionPanel',
	'app/views/account/TPasswordQuestionPanel',
	'app/views/account/TPasswordQuestionSuccessPanel',
	'app/ux/GenericTooltip',
	'dojo/_base/lang',
	'app/ux/GenericPrompt',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, 
		domConstruct, mouse, focusUtil, cookie,RSA,FindQuestionPanel,TPasswordQuestionPanel,TPasswordQuestionSuccessPanel,Tooltip,lang,Prompt) {
	
	var config = {};
	var navBarItems = query('li', 'navbar'),
	    topbarUsername = dom.byId('topusername'), 
		logoutBtn = dom.byId('logout'),
		content1, content2, content3,
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
		
		domClass.add(navBarItems[0], 'ui-step-active');
		
		Ajax.get(Global.baseUrl + '/tradepwd', {
		}).then(function(response) {
			if (response.success) {
				if(response.data.userTradePwdStrength){
					content1.domNode.style.display = 'block';
				}else{
					var content0 = new Prompt({
			    		msg: '您的安全密码还没有设置，请先设置安全密码。',
			    		linkText: '点击设置安全密码',
			    		link: 'account/tradepwd/set.htm'
			    	});
			    	content0.placeAt('contentctn');
				}
			} else {
			}
		});
		
		Ajax.get(Global.baseUrl + '/user', {
		}).then(function(response) {
			if (response.success) {
				content1.setValues(response.data);
			} else {
				//TODO fail is ...
				
			}
			
		});
		
		content1 = new FindQuestionPanel({
			next: function() {
				if(content1.isValid()){
					content1.commit.loading(true);
					Ajax.post(Global.baseUrl + '/user/findpwdquestion/check',RSA.encrypt(key, content1.getData(), 'tradePwd'))
					.then(function(response) {
						content1.commit.loading(false);
						if (response.success) {
							next(content1);
						} else {
							content1.showError(response.msg);
						}
					});
				}	
			} 
		});
		content1.domNode.style.display = 'none';
		content1.placeAt('contentctn');
		
		content2 = new TPasswordQuestionPanel({
			prev: function(){
				prev(content2);
			},
			onConfirm:function(){
				if(content2.isValid()){
					content2.confirmBtn.loading(true);
					Ajax.post(Global.baseUrl + '/user/pwdquestion/reset',RSA.encrypt(key, lang.mixin(content1.getData(),content2.getData()), 'tradePwd'))
					.then(function(response) {
						content2.confirmBtn.loading(false);
						if (response.success) {
							next(content2);
						} else {
							content2.showError(response.msg);
						}
					});
				}
			}
		});
		content2.domNode.style.display = 'none';
		content2.placeAt('contentctn');
		
		content3 = new TPasswordQuestionSuccessPanel();
		content3.domNode.style.display = 'none';
		content3.placeAt('contentctn');
		
		views.push(content1);
		views.push(content2);
		views.push(content3);
		
		Global.focusText();
	}
	
	function addListeners() {
		//send smscode event
		on(content1.codeFld.sendBtn, 'click', function() {
				Ajax.post(Global.baseUrl + '/sms/code', {
					biz_type: 7
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