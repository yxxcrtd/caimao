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
	'app/views/member/ChangeTradePwdPanel',
	'app/views/member/ChangeTradePwdSuccessPanel',
	'app/common/RSA',
	'app/ux/GenericTooltip',
	'app/ux/GenericPrompt',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, 
		domConstruct, mouse, focusUtil, cookie, ChangeTradePwdPanel,ChangeTradePwdSuccessPanel, RSA, Tooltip, Prompt) {
	
	var config = {};
	var topbarUsername = dom.byId('topusername'),
		logoutBtn = dom.byId('logout'),
		content,
		key;
	
	function initView() {
		
		
		Ajax.get(Global.baseUrl + '/user', {
		}).then(function(response) {
			if (response.success){
				content.domNode.style.display = 'block';
				content.setValues({
					userLoginName: response.data.userName?response.data.userName:response.data.mobile
				});
			}
			/*if (response.success && (response.data.pay_delegate_protocol || response.data.userInit == '9')) {
				content.domNode.style.display = 'block';
				content.setValues({
					userLoginName: response.data.user_name
				});
			} else if (response.success && response.data.isTrust == '0') {
				content0 = new Prompt({
					msg: '请您先完成实名认证'
				});
				content0.linkNode.innerHTML = '实名认证';
				content0.linkNode.href = Global.baseUrl + '/user/certification.htm';
				content0.placeAt('contentctn');
			} else {
				Ajax.post(Global.baseUrl + '/user/agreement/bind', { // get url
					agreementType: '01'
				}).then(function(response) {
					if (response.success) {
						content0 = new Prompt({
							msg: '您还未签约无密协议，请您点击链接完成无密保障'
						});
						content0.linkNode.innerHTML = '无密协议入口';
						on(content0.linkNode, 'click', function() {
							location.href = response.data;
						});
						content0.placeAt('contentctn');
					} else {
					}
				});
			}*/
		});
		
		content = new ChangeTradePwdPanel();
		content.commit.onClick = function() {
			// TODO Without this interface(zhanggl modify)
			if (content.isValid()) {
				content.commit.loading(true)
				Ajax.post(
						Global.baseUrl + '/user/tradepwd/reset',
						RSA.encrypt(key, content.getData(), [ 'old_trade_pwd',
								'new_trade_pwd' ])).then(
						function(response) {
							content.commit.loading(false);
							if (response.success) {
								content.destroy();
								content = new ChangeTradePwdSuccessPanel({});
								/*Ajax.post(Global.baseUrl + '/user/userpwdquestion/list', {
								}).then(function(response) {
									if (response.success) {
										if(response.data.question1_id){
											content.set('if_question','SHI');
										}else{
											content.set('if_question','FOU');
										}
									} else {
										//fail is....
									}
								});*/
								content.placeAt('contentctn');
							} else {
								content.showError(
										response.msg);
							}

						});
			};
		}
		content.domNode.style.display = 'none';
		content.placeAt('contentctn');

	}
	
	function addListeners() {
		
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