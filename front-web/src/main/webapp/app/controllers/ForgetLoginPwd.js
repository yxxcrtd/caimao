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
	'dojo/date',
	'dojo/date/stamp',
	'app/ux/GenericPrompt',
	'app/views/member/ForgetLoginPwdPhonePanel',
	'app/views/member/ForgetLoginPwdPhoneCodePanel',
	'app/views/member/ForgetLoginPwdConfirmPanel',
	'app/views/member/ForgetLoginPwdSuccessPanel',
	'dojo/_base/lang',
	'app/common/RSA',
	'dojo/promise/all',
	'app/ux/GenericTooltip',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, cookie, date, stamp, Prompt, ForgetLoginPwdPhonePanel, 
		ForgetLoginPwdPhoneCodePanel, ForgetLoginPwdConfirmPanel, ForgetLoginPwdSuccessPanel, lang, RSA,all, Tooltip) {
	
	var config = {login: false};
	var navBarItems = query('li', 'navbar'),
		topbarUsername = dom.byId('topusername'),
		logoutBtn = dom.byId('logout'),
		content0,content1,content2,content3,
		views = [],
		data = [],
		key;
	
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
	
	function prev(activity) {
		activity.reset();
		var index = views.indexOf(activity);
		views[index].domNode.style.display = 'none';
		views[index - 1].domNode.style.display = 'block';
		domClass.remove(query('.ui-step-active', 'navbar')[0], 'ui-step-active');
		domClass.add(navBarItems[index - 1], 'ui-step-active');
	}
	
	function changeCaptcha() {
		if (content0) {
			content0.captchaImgNode.src = Global.baseUrl + '/captcha?_=' + new Date().valueOf();
		}
	}
	
	function initView() {
		// request data by ajax, render data
		domClass.add(navBarItems[0], 'ui-step-active');
		
		content0 = new ForgetLoginPwdPhonePanel({
			next: function() {
			  //step1
				if (content0.isValid()) {
					var promise1 = Ajax.post(Global.baseUrl + '/captcha/check', {
						'captcha': content0.codeFld.get('value')
					});
					
					var promise2 = Ajax.post(Global.baseUrl + '/user/mobile/check',{
						'mobile':content0.mobileFld.get('value')
					});
					all([promise1, promise2]).then(function(result) {
						if(result[0].msg){
							content0.showError(result[0].msg);
						}
						if(result[1].success){
							content0.showError("输入的手机号不存在");
						} else {
							next(content0);
							content1.set('mobile',content0.mobileFld.get('value'));
						}
					});
					
				}
		  }
		});
		content0.placeAt('contentctn');
		
		/*content1 = new ForgetLoginPwdPhoneCodePanel({
			prev: function() {
		    	prev(content1);
		    },
			next: function() {
				if(content1.isValid()){
					next(content1);
				}
			}
		});
		content1.domNode.style.display = 'none';
		content1.placeAt('contentctn');*/
		
		content1 = new ForgetLoginPwdConfirmPanel({
			prev: function(){
				prev(content1);
			},
			next: function() {
				if(content1.isValid()){
					content1.confirmBtn.loading(true);
					Ajax.post(Global.baseUrl + '/user/loginpwd/find',RSA.encrypt(key, lang.mixin(content0.getData(),content1.getData()), 'user_pwd'))
					.then(function(response) {
						content1.confirmBtn.loading(false);
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
		
		content3 = new ForgetLoginPwdSuccessPanel({});
		content3.domNode.style.display = 'none';
		content3.placeAt('contentctn');
		
		views.push(content0);
		//views.push(content1);
		views.push(content1);
		views.push(content3);
		
		Global.focusText();
		changeCaptcha();
	}
	
	addListeners = function() {
		on(content0.codeFld, 'keyup', function(event) {
			var me = this;
			var value = me.get('value');
			if (value.length === 4) {
				Ajax.post(Global.baseUrl + '/captcha/check', {
					'captcha': value
				}).then(function(response) {
					if (response.success) {
						content0.confirmBtn.set('disabled', false);
					} else {
						content0.confirmBtn.set('disabled', true);
						Tooltip.show(response.msg, me.domNode,'warning');
					}
				});
			}else{
				Tooltip.show("验证码必须是四位", me.domNode,'warning');
                content0.confirmBtn.set('disabled', true);
			}
		});
		
		on(content0.changeCaptchaNode, 'click', function() {
			changeCaptcha();
		});
		
		//send smscode event
		on(content1.codeFld.sendBtn, 'click', function() {
			//if (!this.disabled) {
				Ajax.post(Global.baseUrl + '/sms/loginpwdcode', {
					'mobile':content1.get('mobile')
				}).then(function(response) {
					if (response.success) {
						content1.codeFld.countdown(60);
					} else {
						Tooltip.show(response.msg, content1.codeFld.sendBtn.innerNode,'warning');
					}
				});
			//}
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
			addListeners();
			Helper.init(config);
			initRSA();
		}
	}
});