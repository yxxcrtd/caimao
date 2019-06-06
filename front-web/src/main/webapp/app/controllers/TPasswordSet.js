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
	'app/ux/GenericTextBox',
	'app/views/account/TPasswordSetPanel',
	'app/ux/GenericPrompt',
	'app/ux/GenericTooltip',
	'app/common/RSA',
	'app/common/Data',
	'dojo/when',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, 
		query, Global, domClass, cookie, TextBox, 
		TPasswordSetPanel, Prompt, Tooltip, RSA, Data, when) {
	
	var config = {};
	var key;
	
	function initView() {
        var panel1 = new TPasswordSetPanel();
        panel1.placeAt('contentctn');
        when(Data.getUser(), function(user) {
        	var mobile = user.mobile;
        	panel1.set('mobile', mobile);
        	dom.byId('phonenumber').innerHTML = Global.encodeInfo(mobile, 3, 3);
        });
        
        on(panel1.verifyCodeFld.sendBtn,'click',function(e){
        	Ajax.post(Global.baseUrl + '/sms/code',{
        		'biz_type':3
        	}).then(function(response){
                if (!response.success) {
                    Tooltip.show(response.msg, panel1.verifyCodeFld.sendBtn.domNode, 'warning');
                }
        	});
        });

        on(panel1.confirmBtn, 'click', function (e) {
            if (panel1.isValid()) {
                panel1.confirmBtn.loading(true);
                Ajax.post(Global.baseUrl + '/user/tradepwd/set', RSA.encrypt(key, panel1.getData(), 'trade_pwd')).then(function (response) {
                    panel1.confirmBtn.loading(false);
                    if (response.success) {
                        panel1.domNode.style.display = 'none';
                        var panel2 = new Prompt({
                            msg: '恭喜您，安全密码设置成功！',
                            subMsg: '请妥善保管安全密码，它是平台上进行核心操作时，比如更改绑定手机号，借款合约，资金相关操作，唯一身份认证。',
                            type: 'success',
                            linkText:'返回会员中心',
                            link:'home/index.htm'
                        });
                        panel2.placeAt('contentctn');
                    }
                    else {
                        Tooltip.show(response.msg, panel1.confirmBtn.domNode, 'warning');
                    }
                });
            }
        });
    }
	
	function initRSA() {
		Ajax.get(Global.baseUrl + '/sec/rsa', {
		}).then(function(response) {
			if (response.success) {
				key = RSA.getKeyPair(response.data.exponent, '', response.data.modulus);
			} else {}
			
		});
	}
	
	return {
		init: function() {
			initView();
			initRSA();
			Helper.init(config);
		}
	};
});